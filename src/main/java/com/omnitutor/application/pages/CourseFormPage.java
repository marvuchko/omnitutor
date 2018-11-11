package com.omnitutor.application.pages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.domain.LectureDomain;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.domain.query.LectureQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.query.CourseQueryService;
import com.omnitutor.application.services.query.LectureQueryService;
import com.omnitutor.application.util.CourseConverterUtil;
import com.omnitutor.application.util.CreateLectureDialogUtil;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.FileUploadUtil;
import com.omnitutor.application.util.LectureConverterUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.ResourceUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.SessionService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class CourseFormPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	CourseQueryService courseQueryService;
	
	@Autowired
	LectureQueryService lectureQueryService;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	FileUploadUtil fileUpload;

	@Autowired
	NavigatorUtil navigator;

	private Image image;

	private TextField title;

	private TextArea description;

	private RichTextArea detailedDescription;

	private CourseQuery course;

	private List<LectureDomain> courseLectures;

	@PostConstruct
	void init() {
		setStyle();
	}

	private void setStyle() {
		setMargin(false);
		setSpacing(false);
		setSizeFull();
		addStyleName(CssUtil.APP_BACKGROUND);
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
		Panel content = new Panel();
		content.addStyleName(ValoTheme.PANEL_BORDERLESS);
		content.setSizeFull();
		content.setContent(createContent());
		addComponent(content);
		setExpandRatio(content, 10);
	}

	private Component createContent() {
		VerticalLayout layout = new VerticalLayout();
		VerticalLayout content = new VerticalLayout();
		content.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		Panel contentPanel = new Panel("Course Form");
		contentPanel.setIcon(VaadinIcons.ACADEMY_CAP);
		contentPanel.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_HEADER_CAPITON,
				CssUtil.PANEL_SHADOW_CONTENT);
		contentPanel.setWidth("900px");
		contentPanel.setContent(createCourseForm());
		content.addComponent(contentPanel);
		layout.addComponent(content);
		return layout;
	}

	private Component createCourseForm() {
		VerticalLayout layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Component titleAndDescription = createCourseTitleAndDescription();
		Component courseImage = createCourseImage();
		Component courseLectures = createCourseLectures();
		Component submitCourse = createSubmitCourse();
		layout.addComponents(titleAndDescription, courseImage, courseLectures, submitCourse);
		return layout;
	}

	private Component createSubmitCourse() {
		Button submit = new Button("Submit");
		submit.setIcon(VaadinIcons.DATABASE);
		submit.addStyleNames(ValoTheme.BUTTON_PRIMARY);
		submit.addClickListener(e -> handleSubmit());
		return submit;
	}

	private Object handleSubmit() {
		try {
			validateInputs();
		} catch (Exception ex) {
			ErrorMessageUtil.showErrorDialog(ex.getMessage());
		}
		return this;
	}

	private Component createCourseTitleAndDescription() {
		Panel titleAndDescription = new Panel("Course Title and Description");
		titleAndDescription.setWidth("100%");
		titleAndDescription.setHeight("500px");
		titleAndDescription.addStyleName(ValoTheme.PANEL_WELL);
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		title = new TextField();
		title.setValue(getCourseTitleOrEmptyString());
		title.setPlaceholder("Enter course title");
		title.setWidth("100%");
		description = new TextArea();
		description.setPlaceholder("Course short info");
		description.setSizeFull();
		description.setValue(getCourseDescriptionOrEmptyString());
		detailedDescription = new RichTextArea("Course detailed description");
		detailedDescription.setSizeFull();
		detailedDescription.setValue(getCourseDetailedDescriptionOrEmptyString());
		layout.addComponents(title, description, detailedDescription);
		layout.setExpandRatio(title, 1);
		layout.setExpandRatio(description, 2);
		layout.setExpandRatio(detailedDescription, 7);
		titleAndDescription.setContent(layout);
		return titleAndDescription;
	}

	private String getCourseTitleOrEmptyString() {
		return course != null ? course.getTitle() : "";
	}

	private String getCourseDescriptionOrEmptyString() {
		return course != null ? course.getDescription() : "";
	}

	private String getCourseDetailedDescriptionOrEmptyString() {
		return course != null ? course.getDetailedDescription() : "";
	}

	private Component createCourseImage() {
		Panel courseImage = new Panel("Course Image");
		courseImage.setWidth("100%");
		courseImage.setHeight("400px");
		courseImage.addStyleName(ValoTheme.PANEL_WELL);
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		image = new Image();
		image.setSizeFull();
		image.addStyleName(CssUtil.IMAGE_FIT);
		Upload uploadImage = new Upload(null, fileUpload);
		uploadImage.setButtonCaption("Change course image");
		uploadImage.setWidth("100%");
		uploadImage.addSucceededListener(e -> setImage(uploadImage));
		layout.addComponents(image, uploadImage);
		layout.setExpandRatio(image, 7);
		layout.setExpandRatio(uploadImage, 1);
		courseImage.setContent(layout);
		return courseImage;
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
		return this;
	}

	private Component createCourseLectures() {
		Panel courseLectures = new Panel("Course lectures");
		courseLectures.setWidth("100%");
		courseLectures.addStyleName(ValoTheme.PANEL_WELL);
		VerticalLayout layout = new VerticalLayout();
		Component createdLectures = createCreatedLectures();
		Component addNewLecture = createAddNewLecture();
		layout.addComponents(createdLectures, addNewLecture);
		courseLectures.setContent(layout);
		return courseLectures;
	}

	private Component createCreatedLectures() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		Grid<LectureDomain> lectureGrid = new Grid<>(LectureDomain.class);
		lectureGrid.setWidth("100%");
		lectureGrid.setHeightMode(HeightMode.ROW);
		lectureGrid.setHeightByRows(4);
		lectureGrid.setSelectionMode(SelectionMode.MULTI);
		lectureGrid.removeAllColumns();
		lectureGrid.addColumn(LectureDomain::getTitle).setCaption("Title");
		lectureGrid.addColumn(LectureDomain::getDurationInMinutes).setCaption("Duration in minutes");
		lectureGrid.setItems(courseLectures);
		lectureGrid.addSelectionListener(e -> {
			System.out.println("Selection occured");
		});
		layout.addComponent(lectureGrid);
		return layout;
	}

	private Component createAddNewLecture() {
		Button addLecture = new Button("Add lecture");
		addLecture.setWidth("100%");
		addLecture.setIcon(VaadinIcons.PLUS_CIRCLE);
		addLecture.addClickListener(e -> 
			CreateLectureDialogUtil.showCreateLecturesDialog(fileUpload, courseLectures));
		return addLecture;
	}

	private void addFooter() {
		Footer footer = new Footer();
		addComponent(footer);
		setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		setExpandRatio(footer, 1);
	}

	private void setCourseImage() {
		Resource source = course != null ? new ExternalResource(course.getImageUrl())
				: new ThemeResource(ResourceUtil.COURSE_IMAGE_PLACEHOLDER);
		image.setSource(source);
	}

	private void validateInputs() throws Exception {
		if (title.getValue().isEmpty())
			throw new Exception("Course title can't be blank!");
		if (description.getValue().isEmpty())
			throw new Exception("Course info can't be blank!");
		if (detailedDescription.getValue().isEmpty())
			throw new Exception("Course description can't be blank!");
		if (image.getSource() instanceof ThemeResource)
			throw new Exception("You haven't uploaded course image!");
	}

	private void getAllCouresLectures() {
		try {
			List<LectureQuery> lecturesQuery = course != null
					? LectureConverterUtil
							.convertEntityListToDomainList(lectureQueryService.findAllByCourse(course.getId()))
					: new ArrayList<>();
			courseLectures = lecturesQuery.stream().map(LectureConverterUtil::convertQueryToViewModel)
					.collect(Collectors.toList());
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Long courseId = event.getParameters().isEmpty() ? -1 : new Long(event.getParameters());
		course = CourseConverterUtil.convertEntityToDomain(courseQueryService.findById(courseId));
		getAllCouresLectures();
		removeAllComponents();
		initComponents();
		setCourseImage();
	}

}
