package com.omnitutor.application.pages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.omnitutor.application.components.Chart;
import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.FormInput;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.domain.command.UserCommand;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.domain.query.LectureQuery;
import com.omnitutor.application.domain.query.UserOnCourseQuery;
import com.omnitutor.application.domain.query.UserOnLectureQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.command.UserCommandService;
import com.omnitutor.application.services.query.CourseQueryService;
import com.omnitutor.application.services.query.LectureQueryService;
import com.omnitutor.application.services.query.UserOnCourseQueryService;
import com.omnitutor.application.services.query.UserOnLectureQueryService;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.DropBoxUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.FileUploadUtil;
import com.omnitutor.application.util.LectureConverterUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.SuccessMessageUtil;
import com.omnitutor.application.util.UserConverterUtil;
import com.omnitutor.application.util.UserOnCourseConverterUtil;
import com.omnitutor.application.util.UserOnLectureConverterUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class ProfilePage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	FileUploadUtil fileService;

	@Autowired
	UserOnCourseQueryService userOnCourse;

	@Autowired
	CourseQueryService courseQueryService;

	@Autowired
	UserCommandService userCommandService;

	@Autowired
	LectureQueryService lectureQueryService;

	@Autowired
	UserOnLectureQueryService userOnLectureQueryService;

	@Autowired
	SessionService sessionService;

	private Image image;

	private UserQuery user;

	private FormInput firstNameInput;

	private FormInput lastNameInput;

	private FormInput emailInput;

	private FormInput usernameInput;

	private FormInput passwordInput;

	private boolean hasImageBeenChanged;

	private List<CourseQuery> userCourses;

	@PostConstruct
	void init() {
		setStyle();
	}

	private void setStyle() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
		addStyleNames(CssUtil.APP_BACKGROUND);
	}

	private void initComponents() {
		addNavbar();
		addContent();
		addFooter();
	}

	private void addNavbar() {
		UserQuery user = sessionService.getUser();
		NavbarType type = NavbarFactory.getType(RoleUtil.getRole(user));
		Navbar navbar = NavbarFactory.createNavbar(type, navigator, sessionService);
		addComponent(navbar);
		setComponentAlignment(navbar, Alignment.TOP_CENTER);
		setExpandRatio(navbar, 1);
	}

	private void addContent() {
		Panel contentHolder = new Panel();
		contentHolder.setSizeFull();
		contentHolder.addStyleName(ValoTheme.PANEL_BORDERLESS);
		VerticalLayout content = new VerticalLayout();
		VerticalLayout panelHolder = new VerticalLayout();
		panelHolder.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		Panel contentPanel = new Panel("User profile");
		contentPanel.setIcon(VaadinIcons.USER);
		contentPanel.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_HEADER_CAPITON,
				CssUtil.PANEL_SHADOW_CONTENT);
		contentPanel.setWidth("900px");
		contentPanel.setContent(createProfileContent());
		panelHolder.addComponent(contentPanel);
		content.addComponent(panelHolder);
		contentHolder.setContent(content);
		addComponent(contentHolder);
		setExpandRatio(contentHolder, 10);
	}

	private Component createProfileContent() {
		VerticalLayout holder = new VerticalLayout();
		holder.setSpacing(false);
		holder.addComponent(createUserInfo());
		if (userHasTakenCourses())
			holder.addComponent(createTakenCoursesChart());
		if (hasUserQuizzes())
			holder.addComponent(createQuizzesScoreChart());
		holder.addComponent(createUserEdit());
		return holder;
	}

	private boolean userHasTakenCourses() {
		return userCourses.size() > 0;
	}

	private Component createTakenCoursesChart() {
		VerticalLayout chartHolder = new VerticalLayout();
		chartHolder.setMargin(false);
		chartHolder.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		Chart chart = new Chart("COURSE LECTURES COMPLETION", findAllCompleatedCourseLabels(),
				findAllCompleatedCoursePercentages());
		chart.setWidth("90%");
		chartHolder.addComponent(chart);
		chartHolder.addComponent(createBlankSpace());
		chartHolder.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
		return chartHolder;
	}

	private List<String> findAllCompleatedCourseLabels() {
		return userCourses.stream().map(course -> course.getTitle()).collect(Collectors.toList());
	}

	private Component createQuizzesScoreChart() {
		VerticalLayout chartHolder = new VerticalLayout();
		chartHolder.setMargin(false);
		chartHolder.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		Chart chart = new Chart("COURSE QUIZZES COMPLETITON", findAllCourseLabels(), findAllPercentages());
		chart.setWidth("90%");
		chartHolder.addComponent(chart);
		chartHolder.addComponent(createBlankSpace());
		chartHolder.setComponentAlignment(chart, Alignment.MIDDLE_CENTER);
		return chartHolder;
	}

	private boolean hasUserQuizzes() {
		return user.getUserOnCourses().stream().anyMatch(userOnCourse -> userOnCourse.getHasQuiz());
	}

	private List<Float> findAllPercentages() {
		List<UserOnCourseQuery> userOnCourses = user.getUserOnCourses().stream()
				.filter(userOnCourse -> userOnCourse.getHasQuiz()).collect(Collectors.toList());
		List<CourseQuery> courses = new ArrayList<>();
		userOnCourses.stream().forEach(userOnCourse -> {
			List<CourseQuery> query = userCourses.stream()
					.filter(course -> userOnCourse.getCourse().getId().longValue() == course.getId())
					.collect(Collectors.toList());
			courses.addAll(query);
		});
		List<Float> percentages = new ArrayList<>();
		for (int i = 0; i < courses.size(); i++) {
			float percentage = (float) (userOnCourses.get(i).getScore().intValue())
					/ (float) (courses.get(i).getMaxPoints().intValue()) * 100;
			percentages.add(new Float(percentage));
		}
		return percentages;
	}

	private List<String> findAllCourseLabels() {
		List<UserOnCourseQuery> userOnCourses = user.getUserOnCourses().stream()
				.filter(userOnCourse -> userOnCourse.getHasQuiz()).collect(Collectors.toList());
		List<CourseQuery> courses = new ArrayList<>();
		userOnCourses.stream().forEach(userOnCourse -> {
			List<CourseQuery> query = userCourses.stream()
					.filter(course -> userOnCourse.getCourse().getId().longValue() == course.getId())
					.collect(Collectors.toList());
			courses.addAll(query);
		});
		return courses.stream().map(course -> course.getTitle()).collect(Collectors.toList());
	}

	private Component createBlankSpace() {
		return new Label();
	}

	private Component createUserEdit() {
		VerticalLayout editForm = new VerticalLayout();
		editForm.setMargin(new MarginInfo(false, true, true, true));
		editForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label subtitle = new Label("PERSONAL INFO");
		subtitle.addStyleNames(ValoTheme.LABEL_H2, ValoTheme.LABEL_BOLD);
		editForm.addComponent(subtitle);
		firstNameInput = new FormInput("First name: ", user.getFirstName());
		firstNameInput.setWidth("80%");
		editForm.addComponent(firstNameInput);
		lastNameInput = new FormInput("Last name: ", user.getLastName());
		lastNameInput.setWidth("80%");
		editForm.addComponent(lastNameInput);
		emailInput = new FormInput("Email: ", user.getEmail());
		emailInput.setWidth("80%");
		editForm.addComponent(emailInput);
		usernameInput = new FormInput("Username: ", user.getUsername());
		usernameInput.setWidth("80%");
		editForm.addComponent(usernameInput);
		passwordInput = new FormInput("Password: ", user.getPassword());
		passwordInput.setWidth("80%");
		editForm.addComponent(passwordInput);
		Button saveChanges = new Button("Save changes");
		saveChanges.setIcon(VaadinIcons.DATABASE);
		saveChanges.addStyleName(ValoTheme.BUTTON_PRIMARY);
		saveChanges.addClickListener(e -> saveAllChanges());
		editForm.addComponent(saveChanges);
		return editForm;
	}

	private Object saveAllChanges() {
		SharedLinkMetadata link = null;
		if (hasImageBeenChanged) {
			try {
				link = DropBoxUtil.uploadFile("/profile", fileService.getExtension(), fileService.getData());
			} catch (DbxException | IOException e) {
				ErrorMessageUtil.showErrorDialog(e.getMessage());
			}
		}
		UserCommand modifiedUser = UserCommand.builder().id(user.getId()).email(emailInput.getValue())
				.firstName(firstNameInput.getValue()).lastName(lastNameInput.getValue())
				.username(usernameInput.getValue()).password(passwordInput.getValue()).firstLogin(user.getFirstLogin())
				.lastLogin(user.getLastLogin()).role(user.getRole())
				.imageURL(hasImageBeenChanged ? link.getUrl().split("[?]")[0] + "?raw=1" : user.getImageURL()).build();
		try {
			userCommandService.modifyUser(UserConverterUtil.convertDomainToEntity(modifiedUser));
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		hasImageBeenChanged = false;
		SuccessMessageUtil.showSuccessDialog("User data successfully modified.");
		UserQuery newUser = UserConverterUtil.convertDomainToDomain(modifiedUser);
		sessionService.setUser(newUser);
		refreshView();
		return this;
	}

	private Component createUserInfo() {
		HorizontalLayout infoHolder = new HorizontalLayout();
		infoHolder.setWidth("100%");
		infoHolder.setHeight("200px");
		infoHolder.addStyleNames(CssUtil.SOLID_BORDER_BOTTOM);
		Component profileImage = createProfileImage();
		Component profileData = createProfileData();
		infoHolder.addComponents(profileImage, profileData);
		infoHolder.setExpandRatio(profileImage, 1);
		infoHolder.setExpandRatio(profileData, 5);
		return infoHolder;
	}

	private Component createProfileImage() {
		VerticalLayout imageHolder = new VerticalLayout();
		imageHolder.setMargin(false);
		imageHolder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		image = new Image(null, new ExternalResource(user.getImageURL()));
		image.setWidth("135px");
		image.setHeight("135px");
		image.addStyleNames(CssUtil.IMAGE_FIT);
		imageHolder.addComponent(image);
		Upload upload = new Upload(null, fileService);
		upload.setButtonCaption("Change image");
		upload.addSucceededListener(e -> setImage(upload));
		imageHolder.addComponent(upload);
		return imageHolder;
	}

	private Object setImage(Upload upload) {
		FileUploadUtil reciever = (FileUploadUtil) upload.getReceiver();
		ByteArrayOutputStream data = reciever.getData();
		if (!reciever.getFileType().equals(FileUploadUtil.IMAGE)) {
			ErrorMessageUtil.showErrorDialog("Uploaded file is not an image.");
			return this;
		}
		StreamSource source = new StreamSource() {
			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				return new ByteArrayInputStream(data.toByteArray());
			}
		};
		image.setSource(new StreamResource(source, new Date().toString()));
		hasImageBeenChanged = true;
		return this;
	}

	private Component createProfileData() {
		HorizontalLayout dataContainer = new HorizontalLayout();
		dataContainer.setSizeFull();
		dataContainer.addComponent(createPersonalInfo());
		dataContainer.addComponent(createPersonalStatistics());
		return dataContainer;
	}

	private Component createPersonalStatistics() {
		VerticalLayout statisticsLayout = new VerticalLayout();
		statisticsLayout.setHeight("185px");
		statisticsLayout.setMargin(false);
		statisticsLayout.setSpacing(false);
		statisticsLayout.addStyleName(CssUtil.SOLID_BORDER_LEFT);
		HorizontalLayout statisticsTitle = new HorizontalLayout();
		statisticsTitle.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		statisticsTitle.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		statisticsTitle.setWidth("100%");
		Label title = new Label("Login statistics");
		title.addStyleName(ValoTheme.LABEL_H4);
		statisticsTitle.addComponent(title);
		statisticsLayout.addComponent(statisticsTitle);
		VerticalLayout statisticsData = new VerticalLayout();
		statisticsData.setMargin(new MarginInfo(false, false, false, true));
		statisticsData.setSpacing(false);
		statisticsData.addComponent(new Label(
				"First login: <i>" + user.getFirstLogin().toLocalDate().toString() + "</i>", ContentMode.HTML));
		statisticsData.addComponent(createBlankSpace());
		statisticsData.addComponent(
				new Label("Last login: <i>" + user.getLastLogin().toLocalDate().toString() + "</i>", ContentMode.HTML));
		statisticsLayout.addComponent(statisticsData);
		return statisticsLayout;
	}

	private Component createPersonalInfo() {
		VerticalLayout dataLayout = new VerticalLayout();
		dataLayout.setSpacing(false);
		dataLayout.setMargin(false);
		dataLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		Label fullName = new Label("<b>" + user.getFirstName() + " " + user.getLastName() + "</b>", ContentMode.HTML);
		fullName.addStyleNames(CssUtil.TEXT_LARGE);
		dataLayout.addComponent(fullName);
		dataLayout.addComponent(createBlankSpace());
		Label email = new Label("<b>" + user.getEmail() + "</b>", ContentMode.HTML);
		email.addStyleNames(CssUtil.TEXT_BLUE);
		dataLayout.addComponent(email);
		dataLayout.addComponent(createBlankSpace());
		Label role = new Label("Role: <i>" + user.getRole() + "</i>", ContentMode.HTML);
		role.addStyleNames(ValoTheme.LABEL_SMALL);
		dataLayout.addComponent(role);
		return dataLayout;
	}

	private void addFooter() {
		Footer footer = new Footer();
		addComponent(footer);
		setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		setExpandRatio(footer, 1);
	}

	private void refreshView() {
		user = sessionService.getUser();
		if (user == null)
			return;
		user.setUserOnCourses(UserOnCourseConverterUtil
				.convertEntityListToDomainList(userOnCourse.getAllCoursesUserHasTaken(user.getId())));
		userCourses = new ArrayList<CourseQuery>();
		userCourses.addAll(user.getUserOnCourses().stream().map(userOnCourses -> userOnCourses.getCourse())
				.collect(Collectors.toList()));
		removeAllComponents();
		initComponents();
	}
	
	private List<Float> findAllCompleatedCoursePercentages() {
		List<Float> percentage = new ArrayList<>();
		userCourses.stream().forEach(course -> {
			try {
				List<LectureQuery> courseLectures = LectureConverterUtil
						.convertEntityListToDomainList(lectureQueryService.findAllByCourse(course.getId()));
				List<UserOnLectureQuery> userLectures = UserOnLectureConverterUtil
						.convertEntityListToDomainList(userOnLectureQueryService.findAllByUser(user.getId()));
				List<LectureQuery> compleatedLectures = new ArrayList<>();
				courseLectures.stream().forEach(lecture -> {
					if (userLectures.stream().anyMatch(userOnLecture -> lecture.getId().longValue() == userOnLecture
							.getLecture().getId().longValue()))
						compleatedLectures.add(lecture);
				});
				float nomOfCompleatedLectures = compleatedLectures.size();
				float numOfLectures = courseLectures.size() > 0 ? courseLectures.size() : 1;
				percentage.add(new Float(100 * nomOfCompleatedLectures / numOfLectures));
			} catch (Exception e) {
				ErrorMessageUtil.showErrorDialog(e.getMessage());
			}
		});
		return percentage;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		refreshView();
	}

}
