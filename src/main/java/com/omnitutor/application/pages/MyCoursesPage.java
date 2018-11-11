package com.omnitutor.application.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.CourseGrid;
import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.components.TakenCourseCard;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.query.CourseQueryService;
import com.omnitutor.application.services.query.UserOnCourseQueryService;
import com.omnitutor.application.services.query.UserQueryService;
import com.omnitutor.application.util.CourseConverterUtil;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.RouteUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.UserOnCourseConverterUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class MyCoursesPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	UserOnCourseQueryService userOnCourseQueryService;

	@Autowired
	CourseQueryService courseQueryService;

	@Autowired
	UserQueryService userQueryService;

	@Autowired
	SessionService sessionService;

	private UserQuery user;

	private List<CourseQuery> takenCourses;

	private List<CourseQuery> createdCourses;

	@PostConstruct
	void init() {
		setStyle();
		initProps();
	}

	private void setStyle() {
		setMargin(false);
		setSpacing(false);
		setSizeFull();
	}

	private void initProps() {
		takenCourses = new ArrayList<>();
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
		HorizontalLayout content = new HorizontalLayout();
		content.setSizeFull();
		content.setSpacing(false);
		content.addComponent(createMyLearningCourses());
		content.addComponent(createMyCreatedCourses());
		addComponent(content);
		setExpandRatio(content, 10);
	}

	private Component createMyCreatedCourses() {
		Panel courseContent = new Panel("Created Courses");
		courseContent.setIcon(VaadinIcons.OPEN_BOOK);
		courseContent.setSizeFull();
		courseContent.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_HEADER_CAPITON,
				CssUtil.PANEL_SHADOW_CONTENT);
		VerticalLayout layout = new VerticalLayout();
		Component actions = createCreatedCoursesActions();
		Component coursesCreated = createCreatedCourses();
		layout.addComponents(actions, coursesCreated);
		courseContent.setContent(layout);
		return courseContent;
	}

	private Component createCreatedCoursesActions() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Button createCourse = new Button("Create Course");
		createCourse.setIcon(VaadinIcons.PLUS_CIRCLE);
		createCourse.addStyleName(ValoTheme.BUTTON_PRIMARY);
		createCourse.addClickListener(e -> navigator.navigateTo(RouteUtil.COURSE_FORM));
		createCourse.setWidth("100%");
		Button createQuiz = new Button("Create quiz");
		createQuiz.setIcon(VaadinIcons.QUESTION_CIRCLE);
		createQuiz.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		createQuiz.setWidth("100%");
		createQuiz.addClickListener(e -> handleCreateQuiz());
		Button quizManager = new Button("Delete quiz");
		quizManager.setIcon(VaadinIcons.CLOSE_CIRCLE);
		quizManager.addStyleName(ValoTheme.BUTTON_DANGER);
		quizManager.setWidth("100%");
		quizManager.addClickListener(e -> deleteQuiz());
		layout.addComponents(createCourse, createQuiz, quizManager);
		layout.setExpandRatio(createCourse, 2);
		layout.setExpandRatio(createQuiz, 1);
		layout.setExpandRatio(quizManager, 1);
		return layout;
	}

	private Object handleCreateQuiz() {
		if (thereAreNoCreatedCourses()) {
			ErrorMessageUtil.showErrorDialog("There are no created courses");
			return this;
		}
		navigator.navigateTo(RouteUtil.CREATE_QUIZ_PAGE);
		return this;
	
	}

	private Object deleteQuiz() {
		if (thereAreNoCreatedCourses())
			ErrorMessageUtil.showErrorDialog("There are no created courses");
		return this;
	}

	private Component createCreatedCourses() {
		if (thereAreNoCreatedCourses())
			return createNoCreatedCourses();
		Component courses = new CourseGrid(navigator, createdCourses);
		courses.setWidth("100%");
		return courses;
	}

	private boolean thereAreNoCreatedCourses() {
		return createdCourses == null || createdCourses.size() == 0;
	}

	private Component createNoCreatedCourses() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		layout.addStyleName(CssUtil.DOUBLE_BORDER_TOP);
		Label text = new Label("You haven't created any course.");
		text.addStyleNames(ValoTheme.LABEL_H4);
		layout.addComponent(text);
		return layout;
	}

	private Component createMyLearningCourses() {
		Panel courseContent = new Panel("Taken Courses");
		courseContent.setIcon(VaadinIcons.NOTEBOOK);
		courseContent.setSizeFull();
		courseContent.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_HEADER_CAPITON,
				CssUtil.PANEL_SHADOW_CONTENT);
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(createMyCoursesLayout());
		courseContent.setContent(layout);
		return courseContent;
	}

	private Component createMyCoursesLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		if (thereAreNoTakenCourses())
			return createNoTakenCoursesLabel();
		takenCourses.stream().forEach(course -> {
			TakenCourseCard card = new TakenCourseCard(course, navigator);
			layout.addComponent(card);
		});
		return layout;
	}

	private boolean thereAreNoTakenCourses() {
		return takenCourses == null || takenCourses.size() == 0;
	}

	private Component createNoTakenCoursesLabel() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label text = new Label("You haven't taken any course.");
		text.addStyleNames(ValoTheme.LABEL_H4);
		layout.addComponent(text);
		return layout;
	}

	private void addFooter() {
		Footer footer = new Footer();
		addComponent(footer);
		setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		setExpandRatio(footer, 1);
	}

	private void findAllTakenCourses() {
		takenCourses.clear();
		takenCourses.addAll(user.getUserOnCourses().stream().map(userOnCourse -> userOnCourse.getCourse())
				.collect(Collectors.toList()));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		user = sessionService.getUser();
		if (user == null)
			return;
		user.setUserOnCourses(UserOnCourseConverterUtil
				.convertEntityListToDomainList(userOnCourseQueryService.getAllCoursesUserHasTaken(user.getId())));
		findAllCreatedCourses();
		findAllTakenCourses();
		removeAllComponents();
		initComponents();
	}

	private void findAllCreatedCourses() {
		try {
			createdCourses = CourseConverterUtil.convertEntityListToDomainList(
					courseQueryService.findAllByCreator(userQueryService.findById(user.getId())));
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
	}

}
