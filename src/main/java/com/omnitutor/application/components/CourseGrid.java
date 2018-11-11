package com.omnitutor.application.components;

import java.util.List;

import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CourseGrid extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private List<CourseQuery> courses;

	public CourseGrid(NavigatorUtil navigator, List<CourseQuery> courses) {
		setStyle();
		initProps(courses);
		initComponents(navigator);
	}

	private void setStyle() {
		setMargin(false);
		setSpacing(false);
	}

	private void initProps(List<CourseQuery> courses) {
		this.courses = courses;
	}

	private void initComponents(NavigatorUtil navigator) {
		Component gridHeader = createGridHeader();
		addComponent(gridHeader);
		createGridItems(navigator, this);
	}

	private Component createGridHeader() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setHeight("50px");
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		layout.addStyleNames(CssUtil.DOUBLE_BORDER_BOTTOM, CssUtil.DOUBLE_BORDER_TOP);
		Label imageLabel = new Label("Image");
		imageLabel.addStyleNames(ValoTheme.LABEL_BOLD, CssUtil.TEXT_BLUE);
		layout.addComponent(imageLabel);
		Label titleLabel = new Label("Title");
		titleLabel.addStyleNames(ValoTheme.LABEL_BOLD, CssUtil.TEXT_BLUE);
		layout.addComponent(titleLabel);
		Label optionsLabel = new Label("Options");
		optionsLabel.addStyleNames(ValoTheme.LABEL_BOLD, CssUtil.TEXT_BLUE);
		layout.addComponent(optionsLabel);
		layout.setExpandRatio(imageLabel, 2);
		layout.setExpandRatio(titleLabel, 8);
		layout.setExpandRatio(optionsLabel, 2);
		return layout;
	}

	private void createGridItems(NavigatorUtil navigator, VerticalLayout layout) {
		courses.stream().forEach(course -> {
			CourseGridItem item = new CourseGridItem(navigator, course);
			layout.addComponent(item);
		});
	}
	
}
