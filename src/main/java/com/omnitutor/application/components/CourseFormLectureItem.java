package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

public class CourseFormLectureItem extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	
	private Integer duration;
	private String lectureTitle;
	
	public CourseFormLectureItem(Image image, String lectureTitle, Integer duration) {
		setStyle();
		initProps(lectureTitle, duration);
		initComponents(image);
	}

	private void setStyle() {
		setWidth("100%");
		setHeight("45px");
		addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setMargin(new MarginInfo(false, true, false, true));
	}

	private void initProps(String lectureTitle, Integer duration) {
		this.lectureTitle = lectureTitle;
		this.duration = duration;
	}

	private void initComponents(Image image) {
		Label title = new Label(lectureTitle);
		Label description = new Label(
				VaadinIcons.CLOCK.getHtml() + " " +
				duration.toString() + " min", ContentMode.HTML);
		HorizontalLayout removeLectureHolder = new HorizontalLayout();
		Label removeLecture = new Label(VaadinIcons.CLOSE_CIRCLE.getHtml(), ContentMode.HTML);
		removeLecture.setDescription("Remove lecture");
		removeLecture.addStyleNames(CssUtil.TEXT_LARGE, CssUtil.TEXT_RED, CssUtil.CURSOR_POINTER);
		removeLectureHolder.addComponent(removeLecture);
		image.setSizeFull();
		addComponents(image, title, description, removeLectureHolder);
		setExpandRatio(image, 1);
		setExpandRatio(title, 1);
		setExpandRatio(description, 1);
		setExpandRatio(removeLectureHolder, 1);
	}

}
