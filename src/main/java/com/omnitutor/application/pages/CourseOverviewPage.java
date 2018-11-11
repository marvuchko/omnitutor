package com.omnitutor.application.pages;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.components.PlayLectionVideo;
import com.omnitutor.application.components.QuizItem;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.domain.query.LectureQuery;
import com.omnitutor.application.domain.query.QuizQuery;
import com.omnitutor.application.domain.query.UserOnLectureQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.server.push.BroadcastMessageUtil;
import com.omnitutor.application.server.push.MessageDistributionService;
import com.omnitutor.application.services.command.CourseCommandService;
import com.omnitutor.application.services.query.CourseQueryService;
import com.omnitutor.application.services.query.LectureQueryService;
import com.omnitutor.application.services.query.QuizQueryService;
import com.omnitutor.application.services.query.UserOnLectureQueryService;
import com.omnitutor.application.util.CourseConverterUtil;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.LectureConverterUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.QuizConverterUtil;
import com.omnitutor.application.util.RateCourseDialogUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.RouteUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.UserOnLectureConverterUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import rx.Observable;

@SpringComponent
@UIScope
public class CourseOverviewPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	CourseQueryService courseQueryService;
	
	@Autowired
	CourseCommandService courseCommandService;

	@Autowired
	LectureQueryService lectureQueryService;

	@Autowired
	UserOnLectureQueryService userOnLectureQueryService;
	
	@Autowired
	QuizQueryService quizQueryService;

	@Autowired
	SessionService sessionService;
	
	@Autowired
	MessageDistributionService messageDistributionService;

	private UserQuery user;

	private CourseQuery course;

	private List<LectureQuery> courseLectures;

	private int totalDuration;

	private List<UserOnLectureQuery> compleatedLectures;
	
	private Observable<String> ratingObservable;

	private List<QuizQuery> courseQuizzes;

	@PostConstruct
	void init() {
		setStyle();
		subscribeToObservable();
	}

	private void subscribeToObservable() {
		ratingObservable = messageDistributionService.getObservable();
		ratingObservable.subscribe(message -> {
			if(message.equals(BroadcastMessageUtil.COURSE_MODIFIED)) {
				reloadCourse();
				removeAllComponents();
				initComponents();
			}
		});
	}

	private void reloadCourse() {
		course = CourseConverterUtil.convertEntityToDomain(courseQueryService.findById(course.getId()));
	}

	private void setStyle() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
		addStyleName(CssUtil.APP_BACKGROUND);
	}

	public void initComponents() {
		addNavbar();
		addContent();
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
		Component contentHolder = createPageContent();
		addComponent(contentHolder);
		setExpandRatio(contentHolder, 11);
	}

	private Component createPageContent() {
		HorizontalLayout content = new HorizontalLayout();
		content.setWidth("100%");
		content.setSpacing(false);
		Component courseHolder = createCourseHolder();
		Component mentorHolder = createMentorHolder();
		content.addComponent(courseHolder);
		content.addComponent(mentorHolder);
		content.setExpandRatio(courseHolder, 5);
		content.setExpandRatio(mentorHolder, 2);
		return content;
	}

	private Component createCourseHolder() {
		Panel courseHolder = new Panel();
		courseHolder.addStyleName(CssUtil.VIEWPORT_HEIGHT);
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSpacing(false);
		layout.addComponent(createCourseImage());
		layout.addComponent(createTabSheet());
		courseHolder.setContent(layout);
		courseHolder.addStyleNames(CssUtil.PANEL_SHADOW_CONTENT, ValoTheme.PANEL_WELL);
		return courseHolder;
	}

	private Component createTabSheet() {
		TabSheet tabsheet = new TabSheet();
		tabsheet.setWidth("100%");
		tabsheet.setHeight("500px");
		tabsheet.addStyleNames(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS, ValoTheme.TABSHEET_PADDED_TABBAR);
		tabsheet.addTab(createTableOfContents(), "Table of contents", VaadinIcons.LIST);
		tabsheet.addTab(createCourseDescription(), "Description", VaadinIcons.INFO);
		tabsheet.addTab(createCourseQuiz(), "Quiz", VaadinIcons.QUESTION);
		return tabsheet;
	}

	private Component createCourseQuiz() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		if(courseQuizzes == null || courseQuizzes.size() == 0) {
			layout.addComponent(new Label("Ther are no quizes for this course!"));
			return layout;
		}
		courseQuizzes.stream().forEach(quiz -> {
			QuizItem item = new QuizItem(quiz, navigator);
			layout.addComponent(item);
		});
		return layout;
	}

	private Component createCourseDescription() {
		Panel content = new Panel();
		content.setSizeFull();
		content.addStyleName(ValoTheme.PANEL_BORDERLESS);
		VerticalLayout holder = new VerticalLayout();
		holder.setSpacing(false);
		Label text = new Label();
		text.setWidth("100%");
		text.addStyleNames(CssUtil.TEXT_JUSTIFY);
		text.setValue(course.getDetailedDescription());
		holder.addComponent(text);
		content.setContent(holder);
		return content;
	}

	private Component createTableOfContents() {
		Panel container = new Panel();
		container.setSizeFull();
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(createTableOfContentsHeader());
		courseLectures.stream().forEach(lecture -> {
			boolean compleated = compleatedLectures.stream().anyMatch(compleatedLecture -> lecture.getId()
					.longValue() == compleatedLecture.getLecture().getId().longValue());
			PlayLectionVideo lectionVideo = new PlayLectionVideo(navigator, lecture.getTitle(), lecture.getId(),
					lecture.getDurationInMinutes().toString(), compleated);
			lectionVideo.setWidth("100%");
			layout.addComponent(lectionVideo);
		});
		container.setContent(layout);
		return container;
	}

	private Component createTableOfContentsHeader() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.addStyleName(CssUtil.DOUBLE_BORDER_BOTTOM);
		Component playVideoLabel = new Label("<b>PLAY VIDEO</b>", ContentMode.HTML);
		Component lectureTitleLabel = new Label("<b>TITLE</b>", ContentMode.HTML);
		Component lectureDurationLabel = new Label("<b>DURATION</b>", ContentMode.HTML);
		Component lectureCompleatedLabel = new Label("<b>COMPLEATED</b>", ContentMode.HTML);
		layout.addComponents(playVideoLabel, lectureTitleLabel, lectureDurationLabel, lectureCompleatedLabel);
		layout.setExpandRatio(playVideoLabel, 1);
		layout.setExpandRatio(lectureTitleLabel, 4);
		layout.setExpandRatio(lectureDurationLabel, 1);
		layout.setExpandRatio(lectureCompleatedLabel, 1);
		return layout;
	}

	private Component createCourseImage() {
		VerticalLayout imageContainer = new VerticalLayout();
		imageContainer.setMargin(false);
		imageContainer.setSpacing(false);
		imageContainer.setHeight("450px");
		imageContainer.addStyleName(CssUtil.COURSE_IMAGE_CONTAINER);
		VerticalLayout outer = new VerticalLayout();
		outer.setMargin(false);
		outer.setSpacing(false);
		outer.setHeight("450px");
		outer.addStyleName(CssUtil.COURSE_IMAGE_OUTER);
		VerticalLayout inner = new VerticalLayout();
		inner.setMargin(false);
		inner.setSpacing(false);
		inner.setHeight("450px");
		inner.addStyleName(CssUtil.COURSE_IMAGE_INNER);
		Image image = new Image(null, new ExternalResource(course.getImageUrl()));
		image.setWidth("100%");
		image.setHeight("450px");
		image.addStyleName(CssUtil.IMAGE_FIT);
		inner.addComponent(createCourseImageOverlay());
		outer.addComponent(image);
		outer.addComponent(inner);
		imageContainer.addComponent(outer);
		return imageContainer;
	}

	private Component createCourseImageOverlay() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		VerticalLayout detailLayout = new VerticalLayout();
		detailLayout.setSizeFull();
		Label courseTitle = new Label(course.getTitle(), ContentMode.HTML);
		courseTitle.addStyleNames(CssUtil.TEXT_WHITE, CssUtil.TEXT_SHADOW, CssUtil.TEXT_HUGE);
		Label courseDetails = new Label(course.getDescription(), ContentMode.HTML);
		courseDetails.addStyleNames(CssUtil.TEXT_WHITE, CssUtil.TEXT_SHADOW, ValoTheme.LABEL_LARGE);
		Button startWatchingButton = new Button("Start watching");
		detailLayout.addComponents(courseTitle, courseDetails);
		startWatchingButton.setIcon(VaadinIcons.MOVIE);
		startWatchingButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		startWatchingButton.addClickListener(e -> startWatchingTheCourse());
		Button rateCourseButton = new Button("Rate course");
		rateCourseButton.setIcon(VaadinIcons.STAR);
		rateCourseButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		rateCourseButton.addClickListener(e -> rateCourse(this));
		HorizontalLayout buttonHolder = new HorizontalLayout();
		buttonHolder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		buttonHolder.setSizeFull();
		buttonHolder.addStyleName(CssUtil.BACKGROUND_BLACK);
		buttonHolder.addComponents(startWatchingButton, rateCourseButton);
		layout.addComponents(detailLayout, buttonHolder);
		layout.setComponentAlignment(buttonHolder, Alignment.BOTTOM_CENTER);
		layout.setExpandRatio(detailLayout, 5);
		layout.setExpandRatio(buttonHolder, 1);
		return layout;
	}

	private Object startWatchingTheCourse() {
		LectureQuery firstLecture = courseLectures.stream().findFirst().get();
		if(firstLecture != null) navigator.navigateTo(RouteUtil.VIDEO + "/" + firstLecture.getId());
		return this;
	}

	private Object rateCourse(CourseOverviewPage courseOverviewPage) {
		RateCourseDialogUtil.showRateCourseDialog(course, courseCommandService);
		return this;
	}

	private Component createMentorHolder() {
		VerticalLayout layout = new VerticalLayout();
		layout.setHeight("100%");
		layout.addStyleNames(CssUtil.BACKGROUND_DARK_BLUE);
		layout.setMargin(false);
		VerticalLayout mentorHolder = new VerticalLayout();
		mentorHolder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		mentorHolder.addComponent(createMentorLabel());
		mentorHolder.addComponent(createMentorImage());
		mentorHolder.addComponent(createMentorInfo());
		mentorHolder.addComponent(createBlankComponent());
		mentorHolder.addComponent(createCourseAdditionalInfoLabel());
		mentorHolder.addComponent(createCourseAdditionalInfo());
		layout.addComponent(mentorHolder);
		return layout;
	}

	private Component createBlankComponent() {
		return new Label();
	}

	private Component createCourseAdditionalInfo() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(new MarginInfo(false, true, false, true));
		Label courseTotalDuration = new Label(
				"Total duration:  " + VaadinIcons.CLOCK.getHtml() + "<span> " + totalDuration + "</span> min",
				ContentMode.HTML);
		courseTotalDuration.addStyleName(CssUtil.TEXT_WHITE);
		courseTotalDuration.setWidth("100%");
		Label courseRating = new Label("Rating: <span style=\"color: rgb(255,238,88);\">" + VaadinIcons.STAR.getHtml()
				+ "</span><span> " + String.format("%.02f", course.getRating().floatValue()) +
				"</span>", ContentMode.HTML);
		courseRating.addStyleName(CssUtil.TEXT_WHITE);
		courseRating.setWidth("100%");
		layout.addComponents(courseTotalDuration, courseRating);
		return layout;
	}

	private Component createCourseAdditionalInfoLabel() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		layout.addStyleNames(CssUtil.SOLID_WHITE_BORDER_BOTTOM, CssUtil.MARGIN_BOT);
		Label lblAdditionalInfo = new Label("COURSE INFO", ContentMode.HTML);
		lblAdditionalInfo.addStyleNames(CssUtil.TEXT_WHITE, CssUtil.TEXT_LARGE);
		layout.addComponent(lblAdditionalInfo);
		return layout;
	}

	private Component createMentorInfo() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(new MarginInfo(false, true, false, true));
		UserQuery mentor = course.getCreator();
		Label mentorFirstName = new Label(
				"Full name: <b>" + mentor.getFirstName() + " " + mentor.getLastName() + "</b>", ContentMode.HTML);
		mentorFirstName.addStyleName(CssUtil.TEXT_WHITE);
		mentorFirstName.setWidth("100%");
		Label mentorEmail = new Label("Email: <b>" + mentor.getEmail() + "</b>", ContentMode.HTML);
		mentorEmail.addStyleName(CssUtil.TEXT_WHITE);
		mentorEmail.setWidth("100%");
		layout.addComponents(mentorFirstName, mentorEmail);
		return layout;
	}

	private Component createMentorLabel() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		layout.addStyleNames(CssUtil.SOLID_WHITE_BORDER_BOTTOM, CssUtil.MARGIN_BOT);
		Label lblMentor = new Label("MENTOR", ContentMode.HTML);
		lblMentor.addStyleNames(CssUtil.TEXT_WHITE, CssUtil.TEXT_LARGE);
		layout.addComponent(lblMentor);
		return layout;
	}

	private Component createMentorImage() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.addStyleName(CssUtil.MARGIN_BOT);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Image mentorImage = new Image(null, new ExternalResource(course.getCreator().getImageURL()));
		mentorImage.addStyleNames(CssUtil.MENTOR_IMAGE, CssUtil.IMAGE_FIT);
		mentorImage.setWidth("75px");
		mentorImage.setHeight("75px");
		layout.addComponent(mentorImage);
		return layout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		user = sessionService.getUser();
		Long courseId = new Long(event.getParameters());
		totalDuration = 0;
		try {
			course = CourseConverterUtil.convertEntityToDomain(courseQueryService.findById(courseId));
			courseLectures = LectureConverterUtil
					.convertEntityListToDomainList(lectureQueryService.findAllByCourse(course.getId()));
			courseLectures.stream().forEach(lecture -> totalDuration += lecture.getDurationInMinutes().intValue());
			sessionService.setLectures(courseLectures);
			compleatedLectures = UserOnLectureConverterUtil
					.convertEntityListToDomainList(userOnLectureQueryService.findAllByUser(user.getId()));
			courseQuizzes = QuizConverterUtil.convertEntityListToDomainList(
					quizQueryService.findAllQuizzesForCourse(course.getId()));
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		if (user == null || course == null)
			return;
		removeAllComponents();
		initComponents();
	}

}
