package com.omnitutor.application.pages;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.domain.query.CommentQuery;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.domain.query.LectureQuery;
import com.omnitutor.application.domain.query.UserOnCourseQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.server.push.Broadcaster;
import com.omnitutor.application.services.command.CommentCommandService;
import com.omnitutor.application.services.command.UserOnCourseCommandService;
import com.omnitutor.application.services.query.CommentQueryService;
import com.omnitutor.application.services.query.CourseQueryService;
import com.omnitutor.application.services.query.LectureQueryService;
import com.omnitutor.application.services.query.UserOnCourseQueryService;
import com.omnitutor.application.util.CommentConverterUtil;
import com.omnitutor.application.util.CourseConverterUtil;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.LectureConverterUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.ResourceUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.RouteUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.SuccessMessageUtil;
import com.omnitutor.application.util.UserOnCourseConverterUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class CoursePage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	CourseQueryService courseQueryService;

	@Autowired
	LectureQueryService lectureQueryService;

	@Autowired
	CommentQueryService commentQueryService;

	@Autowired
	CommentCommandService commentCommandService;

	@Autowired
	UserOnCourseCommandService userOnCourseCommandService;

	@Autowired
	UserOnCourseQueryService userOnCourseQueryService;

	@Autowired
	SessionService sessionService;

	private CourseQuery model;

	private VerticalLayout courseHolder;

	private List<LectureQuery> lectures;

	private List<CommentQuery> comments;

	private TextArea commentInput;

	private Long courseId;

	private Component commentContainer;

	private UserQuery user;

	private List<UserOnCourseQuery> userOnCourses;

	@PostConstruct
	void init() {
		setStyle();
	}

	private void setStyle() {
		setSizeFull();
		setSpacing(false);
		setMargin(false);
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
		CssLayout content = new CssLayout();
		content.setSizeFull();
		Panel contentPanel = new Panel();
		contentPanel.setSizeFull();
		contentPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		contentPanel.setContent(createCourseContent());
		content.addComponent(contentPanel);
		addComponent(content);
		setExpandRatio(content, 10);
	}

	private Component createCourseContent() {
		courseHolder = new VerticalLayout();
		return courseHolder;
	}

	private Component createCourseInfo() {
		VerticalLayout courseInfoContainer = new VerticalLayout();
		courseInfoContainer.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		courseInfoContainer.setMargin(new MarginInfo(true, false, false, false));
		Panel holder = new Panel();
		holder.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_CUSTOM_CAPTION,
				CssUtil.PANEL_SHADOW_CONTENT);
		holder.setWidth("900px");
		VerticalLayout courseInfoCard = new VerticalLayout();
		courseInfoCard.setMargin(false);
		courseInfoCard.setSpacing(false);
		courseInfoCard.addComponent(createCourseImage());
		courseInfoCard.addComponent(createCourseTitleWithDetails());
		courseInfoCard.addComponent(createCourseTabbedPane());
		if (userIsNotOnCourse())
			courseInfoCard.addComponent(createCourseJoinButton());
		else if (userSessionExists())
			courseInfoCard.addComponent(createGoToCourseButton());
		holder.setContent(courseInfoCard);
		courseInfoContainer.addComponent(holder);
		return courseInfoContainer;
	}

	private Component createGoToCourseButton() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(new MarginInfo(false, true, true, true));
		Button joinButton = new Button("Go to course");
		joinButton.setWidth("100%");
		joinButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		joinButton.setIcon(VaadinIcons.ACADEMY_CAP);
		joinButton.addClickListener(e -> goToCourseOverview());
		layout.addComponent(joinButton);
		return layout;
	}

	private Object goToCourseOverview() {
		navigator.navigateTo(RouteUtil.COURSE_OVERVIEW + "/" + model.getId());
		return this;
	}

	private boolean userIsNotOnCourse() {
		return sessionService.getUser() != null && userOnCourses.stream()
				.noneMatch(userOnCourse -> userOnCourse.getCourse().getId().longValue() == model.getId().longValue());
	}

	private Component createCourseJoinButton() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(new MarginInfo(false, true, true, true));
		Button joinButton = new Button("Join course");
		joinButton.setWidth("100%");
		joinButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		joinButton.setIcon(VaadinIcons.CHECK_CIRCLE);
		joinButton.addClickListener(e -> applyForCourse());
		layout.addComponent(joinButton);
		return layout;
	}

	private Object applyForCourse() {
		try {
			boolean result = userOnCourseCommandService.addUserOnCourseIfNotExists(user.getId(), courseId);
			if (result) {
				SuccessMessageUtil.showSuccessDialog("You have successfuly joined this course");
				userOnCourses = UserOnCourseConverterUtil.convertEntityListToDomainList(
						userOnCourseQueryService.getAllCoursesUserHasTaken(user.getId()));
				refreshView();
				return this;
			}
			navigator.navigateTo(RouteUtil.COURSE_OVERVIEW + "/" + courseId);
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		return this;
	}

	private void refreshView() {
		removeAllComponents();
		initComponents();
		courseHolder.removeAllComponents();
		courseHolder.addComponent(createCourseInfo());
		commentContainer = createCourseComments();
		courseHolder.addComponent(commentContainer);
	}

	private Component createCourseTabbedPane() {
		VerticalLayout holder = new VerticalLayout();
		holder.setHeight("352px");
		holder.setMargin(new MarginInfo(false, true, true, true));
		TabSheet tabsheet = new TabSheet();
		tabsheet.setSizeFull();
		tabsheet.addStyleNames(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS, ValoTheme.TABSHEET_PADDED_TABBAR);
		tabsheet.addTab(createTableOfContents(), "Table of contents", VaadinIcons.LIST);
		tabsheet.addTab(createCourseDescription(), "Description", VaadinIcons.INFO);
		holder.addComponent(tabsheet);
		return holder;
	}

	private Component createTableOfContents() {
		Panel content = new Panel();
		content.setSizeFull();
		content.addStyleName(ValoTheme.PANEL_BORDERLESS);
		VerticalLayout holder = new VerticalLayout();
		holder.setSpacing(false);
		Accordion accordion = new Accordion();
		addItemsToTableOfContent(accordion);
		holder.addComponent(accordion);
		content.setContent(holder);
		return content;
	}

	private void addItemsToTableOfContent(Accordion accordion) {
		lectures.stream().forEach(lecture -> {
			VerticalLayout holder = new VerticalLayout();
			holder.setSpacing(false);
			VerticalLayout durationHolder = new VerticalLayout();
			durationHolder.setMargin(new MarginInfo(false, true, false, true));
			durationHolder.addComponent(new Label(
					"<h4><b>Duration: </b><b>" + lecture.getDurationInMinutes() + "</b> min</h4>", ContentMode.HTML));
			durationHolder.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
			holder.addComponent(durationHolder);
			VerticalLayout descriptionHolder = new VerticalLayout();
			descriptionHolder.setMargin(new MarginInfo(false, true, false, true));
			descriptionHolder.addComponent(
					new Label("<h4><b>Description: </b>" + lecture.getDescription() + "</h4>", ContentMode.HTML));
			holder.addComponent(descriptionHolder);
			accordion.addTab(holder, lecture.getTitle(), VaadinIcons.DOT_CIRCLE);
		});
	}

	private Component createCourseDescription() {
		Panel content = new Panel();
		content.setSizeFull();
		content.addStyleName(ValoTheme.PANEL_BORDERLESS);
		VerticalLayout holder = new VerticalLayout();
		holder.setSpacing(false);
		Label text = new Label(model.getDetailedDescription(), ContentMode.HTML);
		text.setWidth("100%");
		text.addStyleNames(CssUtil.TEXT_JUSTIFY);
		holder.addComponent(text);
		content.setContent(holder);
		return content;
	}

	private Component createCourseTitleWithDetails() {
		VerticalLayout holder = new VerticalLayout();
		holder.setMargin(false);
		holder.setSpacing(false);
		holder.addComponents(addTitleLabel(), addCreatorLabel(), addCreatedAtLabel());
		return holder;
	}

	private Component addCreatedAtLabel() {
		VerticalLayout holder = new VerticalLayout();
		holder.setMargin(new MarginInfo(false, true, false, true));
		Label createdAt = new Label();
		createdAt.addStyleNames(CssUtil.TEXT_BLUE, CssUtil.SOLID_BORDER_BOTTOM, CssUtil.MARGIN_BOT);
		createdAt.setValue("Posted on January 1, 2018 at 12:00 PM");
		createdAt.setWidth("100%");
		holder.addComponent(createdAt);
		return holder;
	}

	private Component addCreatorLabel() {
		VerticalLayout holder = new VerticalLayout();
		holder.setMargin(new MarginInfo(false, true, true, true));
		Label creator = new Label();
		creator.addStyleNames(CssUtil.SOLID_BORDER_BOTTOM);
		creator.setContentMode(ContentMode.HTML);
		creator.setValue("<b style=\"color: rgb(25,118,210);\"> by " + model.getCreator().getFirstName() + " "
				+ model.getCreator().getLastName() + "</b>");
		creator.setWidth("100%");
		holder.addComponent(creator);
		return holder;
	}

	private Component addTitleLabel() {
		VerticalLayout holder = new VerticalLayout();
		holder.setMargin(new MarginInfo(false, true, false, true));
		Label title = new Label();
		title.setWidth("100%");
		title.setContentMode(ContentMode.HTML);
		title.setValue("<h1 style=\"color: rgb(13,71,161);\"><b>" + model.getTitle() + "</b></h1>");
		holder.addComponent(title);
		return holder;
	}

	private Component createCourseImage() {
		VerticalLayout holder = new VerticalLayout();
		holder.setMargin(false);
		Image image = new Image(null, new ExternalResource(model.getImageUrl()));
		image.setWidth("100%");
		image.setHeight("350px");
		image.addStyleName(CssUtil.SHADOW_SMALL);
		image.addStyleNames(CssUtil.SOLID_BORDER_BOTTOM, CssUtil.IMAGE_FIT);
		holder.addComponent(image);
		return holder;
	}

	private Component createCourseComments() {
		VerticalLayout courseInfoContainer = new VerticalLayout();
		courseInfoContainer.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		courseInfoContainer.setMargin(new MarginInfo(true, false, true, false));
		Panel holder = new Panel();
		holder.setCaption("Comments");
		holder.setIcon(VaadinIcons.COMMENT);
		holder.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_CUSTOM_CAPTION,
				CssUtil.PANEL_SHADOW_CONTENT);
		holder.setWidth("900px");
		VerticalLayout courseCommentCard = new VerticalLayout();
		courseCommentCard.setMargin(false);
		courseCommentCard.setSpacing(false);
		courseCommentCard.addComponent(createCommentsHolder());
		holder.setContent(courseCommentCard);
		courseInfoContainer.addComponent(holder);
		return courseInfoContainer;
	}

	private Component createCommentsHolder() {
		VerticalLayout holder = new VerticalLayout();
		holder.setSpacing(false);
		holder.addComponent(createCommentInputs());
		addAllCommentsForThisCourse(holder);
		return holder;
	}

	private Component createCommentInputs() {
		HorizontalLayout holder = new HorizontalLayout();
		holder.setWidth("100%");
		holder.setMargin(new MarginInfo(false, false, true, false));
		holder.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		Panel container = new Panel("Leave a comment:");
		container.setWidth("100%");
		container.addStyleName(ValoTheme.PANEL_WELL);
		VerticalLayout containerContent = new VerticalLayout();
		commentInput = new TextArea();
		commentInput.setPlaceholder("Enter a comment here...");
		commentInput.setWidth("100%");
		containerContent.addComponent(commentInput);
		Button submit = new Button("Submit");
		submit.addClickListener(e -> submitComment());
		submit.setIcon(VaadinIcons.ARROW_FORWARD);
		submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		containerContent.addComponents(commentInput, submit);
		container.setContent(containerContent);
		holder.addComponent(container);
		return holder;
	}

	private Object submitComment() {
		String text = commentInput.getValue();
		if (text == null || text.isEmpty())
			return this;
		try {
			UserQuery user = sessionService.getUser();
			commentCommandService.createComment(courseId, user != null ? user.getId() : null, text);
			courseHolder.removeComponent(commentContainer);
			comments = CommentConverterUtil
					.convertEntityListToDomainList(commentQueryService.findAllByCourse(courseId));
			commentContainer = createCourseComments();
			courseHolder.addComponent(commentContainer);
			Broadcaster.broadcast("User " + getUserFullName()
					+ " has commented your course! Message: <b>" + text + "</b>", user, model.getCreator());
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		return this;
	}

	private String getUserFullName() {
		return user != null ? user.getFirstName() + " " + user.getLastName() : "Annonymus User";
	}

	private void addAllCommentsForThisCourse(VerticalLayout holder) {
		comments.stream().forEach(comment -> {
			HorizontalLayout container = new HorizontalLayout();
			container.setWidth("100%");
			Image creatorImage = comment.getCreator() != null
					? new Image(null, new ExternalResource(comment.getCreator().getImageURL()))
					: new Image(null, new ThemeResource(ResourceUtil.PROFILE_IMAGE_DEFAULT));
			creatorImage.addStyleNames(CssUtil.IMAGE_FIT, CssUtil.PROFILE_IMAGE);
			creatorImage.setWidth("50px");
			creatorImage.setHeight("50px");
			container.addComponent(creatorImage);
			container.setComponentAlignment(creatorImage, Alignment.MIDDLE_CENTER);
			container.setExpandRatio(creatorImage, 1);
			VerticalLayout textHolder = new VerticalLayout();
			textHolder.setMargin(new MarginInfo(false, true, false, true));
			textHolder.setSpacing(false);
			Label creatorFullName = new Label(getFullName(comment), ContentMode.HTML);
			creatorFullName.setWidth("100%");
			creatorFullName.addStyleNames(ValoTheme.LABEL_H4, ValoTheme.LABEL_BOLD);
			Label commentText = new Label();
			commentText.setWidth("100%");
			commentText.addStyleNames(ValoTheme.LABEL_SMALL, CssUtil.MARGIN_BOT);
			commentText.setValue(comment.getText());
			textHolder.addComponents(creatorFullName, commentText);
			textHolder.addStyleName(CssUtil.SOLID_BORDER_LEFT);
			container.addComponent(textHolder);
			container.setComponentAlignment(textHolder, Alignment.MIDDLE_CENTER);
			container.setExpandRatio(textHolder, 4);
			container.addStyleNames(CssUtil.SOLID_BORDER_BOTTOM);
			holder.addComponent(container);
		});
	}

	private String getFullName(CommentQuery comment) {
		return comment.getCreator() != null
				? comment.getCreator().getFirstName() + " " + comment.getCreator().getLastName()
				: "Annonymus User";
	}

	private void addFooter() {
		Footer footer = new Footer();
		addComponent(footer);
		setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		setExpandRatio(footer, 1);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		try {
			user = sessionService.getUser();
			removeAllComponents();
			initComponents();
			courseId = Long.valueOf(event.getParameters());
			model = CourseConverterUtil.convertEntityToDomain(courseQueryService.findById(courseId));
			if (userSessionExists())
				userOnCourses = UserOnCourseConverterUtil.convertEntityListToDomainList(
						userOnCourseQueryService.getAllCoursesUserHasTaken(user.getId()));
			lectures = LectureConverterUtil
					.convertEntityListToDomainList(lectureQueryService.findAllByCourse(courseId));
			comments = CommentConverterUtil
					.convertEntityListToDomainList(commentQueryService.findAllByCourse(courseId));
			courseHolder.removeAllComponents();
			courseHolder.addComponent(createCourseInfo());
			commentContainer = createCourseComments();
			courseHolder.addComponent(commentContainer);
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
	}

	private boolean userSessionExists() {
		return user != null;
	}

}
