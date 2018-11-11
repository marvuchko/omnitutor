package com.omnitutor.application.pages;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.domain.query.LectureQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.command.UserOnLectureCommandService;
import com.omnitutor.application.services.query.LectureQueryService;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.LectureConverterUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.SessionService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Video;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class VideoPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	LectureQueryService lectureQueryService;
	
	@Autowired
	UserOnLectureCommandService userOnLectureCommandService;

	@Autowired
	SessionService sessionService;

	private UserQuery user;

	private LectureQuery lecture;

	private List<LectureQuery> lectures;

	private Video video;

	private int lectureIndex;

	@PostConstruct
	void init() {
		setStyle();
	}

	private void setStyle() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
		addStyleName(CssUtil.BACKGROUND_BLACK);
	}

	private void initComponents() {
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
		Panel content = new Panel();
		content.setSizeFull();
		content.addStyleName(ValoTheme.PANEL_BORDERLESS);
		content.setContent(createPageLayout());
		return content;
	}

	private Component createPageLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.setSpacing(false);
		Component leftCourseButton = createLeftCourseButton();
		Component videoPlayer = createVideoPlayer();
		Component rightCourseButton = createRightCourseButton();
		layout.addComponent(leftCourseButton);
		layout.addComponent(videoPlayer);
		layout.addComponent(rightCourseButton);
		layout.setExpandRatio(leftCourseButton, 1);
		layout.setExpandRatio(videoPlayer, 6);
		layout.setExpandRatio(rightCourseButton, 1);
		return layout;
	}

	private Component createLeftCourseButton() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.addStyleName(CssUtil.BACKGROUND_BLACK);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		if (lectureIndex == 0)
			return layout;
		HorizontalLayout clickableLayout = new HorizontalLayout();
		Label previousLection = new Label(VaadinIcons.ARROW_CIRCLE_LEFT.getHtml(), ContentMode.HTML);
		previousLection.setDescription("Go to previous lection.");
		previousLection.addStyleNames(CssUtil.TEXT_HUGE, CssUtil.TEXT_WHITE, CssUtil.CURSOR_POINTER);
		clickableLayout.addLayoutClickListener(e -> goToPreviousLection());
		clickableLayout.addComponent(previousLection);
		layout.addComponent(clickableLayout);
		return layout;
	}

	private Object goToPreviousLection() {
		Long lectureId = lectures.get(lectureIndex - 1).getId();
		try {
			lecture = LectureConverterUtil.convertEntityToDomain(lectureQueryService.findById(lectureId));
			addLectureToCompleated();
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		lectureIndex--;
		removeAllComponents();
		initComponents();
		return this;
	}

	private Component createRightCourseButton() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.addStyleName(CssUtil.BACKGROUND_BLACK);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		if (lectureIndex == lectures.size() - 1)
			return layout;
		HorizontalLayout clickableLayout = new HorizontalLayout();
		Label nextLection = new Label(VaadinIcons.ARROW_CIRCLE_RIGHT.getHtml(), ContentMode.HTML);
		nextLection.setDescription("Go to next lection.");
		nextLection.addStyleNames(CssUtil.TEXT_HUGE, CssUtil.TEXT_WHITE, CssUtil.CURSOR_POINTER);
		clickableLayout.addLayoutClickListener(e -> goToNextLection());
		clickableLayout.addComponent(nextLection);
		layout.addComponent(clickableLayout);
		return layout;
	}

	private Object goToNextLection() {
		Long lectureId = lectures.get(lectureIndex + 1).getId();
		try {
			lecture = LectureConverterUtil.convertEntityToDomain(lectureQueryService.findById(lectureId));
			addLectureToCompleated();
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		lectureIndex++;
		removeAllComponents();
		initComponents();
		return this;
	}

	private Component createVideoPlayer() {
		HorizontalLayout videoHolder = new HorizontalLayout();
		videoHolder.setSizeFull();
		videoHolder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		videoHolder.addStyleName(CssUtil.BACKGROUND_BLACK);
		video = new Video(lecture.getTitle(), new ExternalResource(lecture.getVideoURL()));
		video.setPoster(new ExternalResource(lecture.getImageURL()));
		video.setWidth("100%");
		video.setHeight("70%");
		video.addStyleNames(CssUtil.CAPTION_VIDEO);
		videoHolder.addComponent(video);
		return videoHolder;
	}

	private void addLectureToCompleated() {
		try {
			userOnLectureCommandService.addUserOnLecture(user.getId(), lecture.getId());
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		user = sessionService.getUser();
		lectures = sessionService.getLectures();
		if (lectures == null)
			return;
		Long lectureId = new Long(event.getParameters());
		try {
			lecture = LectureConverterUtil.convertEntityToDomain(lectureQueryService.findById(lectureId));
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		findLectureIndex();
		if (user == null)
			return;
		removeAllComponents();
		initComponents();
		addLectureToCompleated();
	}

	private void findLectureIndex() {
		for (int i = 0; i < lectures.size(); i++)
			if (lectures.get(i).getId().longValue() == lecture.getId().longValue()) {
				lectureIndex = i;
				break;
			}
	}

}
