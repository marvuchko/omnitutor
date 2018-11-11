package com.omnitutor.application.components;

import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class TakenCourseCard extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	private NavigatorUtil navigator;
	private CourseQuery course;

	public TakenCourseCard(CourseQuery course, NavigatorUtil navigator) {
		setStyle();
		initProps(course, navigator);
		initComponents();
	}

	private void initProps(CourseQuery course, NavigatorUtil navigator) {
		this.navigator = navigator;
		this.course = course;
	}

	private void setStyle() {
		setSpacing(false);
		setMargin(false);
		setWidth("100%");
		setHeight("90px");
	}

	private void initComponents() {
		Panel content = new Panel();
		content.setSizeFull();
		content.addStyleNames(
				CssUtil.CURSOR_POINTER,
				CssUtil.PANEL_SHADOW_CAPTION,
				CssUtil.PANEL_SHADOW_CONTENT);
		content.setContent(createContent());
		addComponent(content);
		addLayoutClickListener(e -> {
			navigator.navigateTo(RouteUtil.COURSE_OVERVIEW + "/" + course.getId());
		});
	}

	private Component createContent() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(false);
		layout.setSizeFull();
		Component image = createImage();
		Component text = createText();
		layout.addComponents(image, text);
		layout.setExpandRatio(image, 1);
		layout.setExpandRatio(text, 6);
		return layout;
	}

	private Component createImage() {
		Image image = new Image(null, new ExternalResource(course.getImageUrl()));
		image.setSizeFull();
		image.addStyleName(CssUtil.IMAGE_FIT);
		return image;
	}

	private Component createText() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setSpacing(false);
		VerticalLayout titleHolder = new VerticalLayout();
		titleHolder.setMargin(new MarginInfo(false, false, false, true));
		titleHolder.setSizeFull();
		titleHolder.addStyleNames(CssUtil.SOLID_BORDER_BOTTOM, CssUtil.BACKGROUND_LIGHT_GRAY);
		titleHolder.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		Label title = new Label(course.getTitle());
		title.addStyleNames(CssUtil.TEXT_LARGE, CssUtil.TEXT_BLUE);
		titleHolder.addComponent(title);
		VerticalLayout descHolder = new VerticalLayout();
		descHolder.setMargin(new MarginInfo(false, false, false, true));
		descHolder.setSizeFull();
		descHolder.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		descHolder.addStyleName(CssUtil.BACKGROUND_LIGHT_GRAY);
		Label desc = new Label(course.getDescription());
		desc.addStyleName(CssUtil.TEXT_BLUE);
		descHolder.addComponent(desc);
		layout.addComponents(titleHolder, descHolder);
		layout.setExpandRatio(titleHolder, 2);
		layout.setExpandRatio(descHolder, 1);
		return layout;
	}
	
}
