package com.omnitutor.application.components;

import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

class CourseGridItem extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	
	private CourseQuery course;
private NavigatorUtil navigator;

	public CourseGridItem(NavigatorUtil navigator, CourseQuery course) {
		setStyle();
		initProps(navigator, course);
		initComponent();
	}

	private void initProps(NavigatorUtil navigator, CourseQuery course) {
		this.navigator = navigator;
		this.course = course;
	}

	private void setStyle() {
		setWidth("100%");
		setHeight("50px");
		setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		addStyleNames(CssUtil.SOLID_BORDER_LEFT, CssUtil.SOLID_BORDER_BOTTOM);
	}

	private void initComponent() {
		Component image = createImage();
		addComponent(image);
		Label title = new Label(course.getTitle());
		title.addStyleNames(ValoTheme.LABEL_BOLD, CssUtil.TEXT_BLUE);
		addComponent(title);
		HorizontalLayout editHolder = new HorizontalLayout();
		Label edit = new Label(VaadinIcons.EDIT.getHtml(), ContentMode.HTML);
		edit.setDescription("Edit course");
		edit.addStyleNames(CssUtil.TEXT_LARGE, CssUtil.TEXT_GREEN, CssUtil.CURSOR_POINTER);
		editHolder.addComponent(edit);
		editHolder.addLayoutClickListener(e -> navigator.navigateTo(RouteUtil.COURSE_FORM + "/" + course.getId()));
		addComponent(editHolder);
		HorizontalLayout deleteHolder = new HorizontalLayout();
		Label delete = new Label(VaadinIcons.CLOSE_CIRCLE.getHtml(), ContentMode.HTML);
		delete.setDescription("Delete course");
		delete.addStyleNames(CssUtil.TEXT_LARGE, CssUtil.TEXT_RED, CssUtil.CURSOR_POINTER);
		deleteHolder.addComponent(delete);
		addComponent(deleteHolder);
		setExpandRatio(image, 2);
		setExpandRatio(title, 8);
		setExpandRatio(editHolder, 1);
		setExpandRatio(deleteHolder, 1);
	}

	private Component createImage() {
		Image image = new Image(null, new ExternalResource(course.getImageUrl()));
		image.setSizeFull();
		image.addStyleName(CssUtil.IMAGE_FIT);
		return image;
	}
	
}
