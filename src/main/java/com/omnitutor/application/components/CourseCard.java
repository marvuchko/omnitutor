package com.omnitutor.application.components;

import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CourseCard extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	NavigatorUtil navigator;
	
	private CourseQuery courseModel;
	
	public CourseCard(CourseQuery course, NavigatorUtil navUtil) {
		courseModel = course;
		navigator = navUtil;
		setStyle();
		initComponents();
	}

	private void setStyle() {
		setWidth("310px");
		setHeight("550px");
		setMargin(new MarginInfo(true, true, true, false));
	}

	private void initComponents() {
		Panel contentPanel = new Panel();
		contentPanel.setSizeFull();
		contentPanel.addStyleNames(ValoTheme.PANEL_WELL);
		contentPanel.addStyleNames(
				CssUtil.CURSOR_POINTER,
				CssUtil.PANEL_SHADOW_CAPTION,
				CssUtil.PANEL_SHADOW_CONTENT);
		contentPanel.addClickListener(e -> goToCardPage());
		VerticalLayout content = new VerticalLayout();
		content.setMargin(false);
		content.setSizeFull();
		content.setSpacing(false);
		Component image = createCardImage();
		content.addComponent(image);
		content.setExpandRatio(image, 6);
		Component title = createCardTitle();
		content.addComponent(title);
		content.setExpandRatio(title, 1);
		Component description = createCardDescription();
		content.addComponent(description);
		content.setExpandRatio(description, 2);
		Component footer = createCardRating();
		content.addComponent(footer);
		content.setExpandRatio(footer, 1);
		content.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		contentPanel.setContent(content);
		addComponent(contentPanel);
	}

	private Object goToCardPage() {
		navigator.navigateTo(RouteUtil.COURSE + "/" + courseModel.getId());
		return this;
	}

	private Component createCardTitle() {
		HorizontalLayout holder = new HorizontalLayout();
		holder.setSizeFull();
		holder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		holder.addStyleNames(CssUtil.SOLID_BORDER_BOTTOM);
		HorizontalLayout content = new HorizontalLayout();	
		Label title = new Label(courseModel.getTitle());
		title.addStyleNames(ValoTheme.LABEL_BOLD, CssUtil.TEXT_LARGE, CssUtil.TEXT_BLUE);
		content.addComponent(title);
		holder.addComponent(content);
		return holder;
	}

	private Component createCardRating() {
		HorizontalLayout holder = new HorizontalLayout();
		holder.setSizeFull();
		holder.addStyleName(CssUtil.SOLID_BORDER_TOP);
		holder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		holder.addStyleName(CssUtil.BACKGROUND_BLUE);
		HorizontalLayout content = new HorizontalLayout();
		Label footer = new Label("<span style=\"color: rgb(255,238,88);\">" + 
				VaadinIcons.STAR.getHtml() + 
				"</span> Rating:  <b>" + 
				String.format("%.02f", courseModel.getRating().floatValue()) +
				"</b>", ContentMode.HTML);
		footer.addStyleName(CssUtil.TEXT_WHITE);
		content.addComponent(footer);
		holder.addComponent(content);
		return holder;
	}

	private Component createCardImage() {
		HorizontalLayout holder = new HorizontalLayout();
		holder.setSizeFull();
		holder.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		Image imageFromUrl = new Image(null, new ExternalResource(courseModel.getImageUrl()));
		imageFromUrl.setSizeFull();
		imageFromUrl.addStyleName(CssUtil.IMAGE_FIT);
		holder.addComponent(imageFromUrl);
		return holder;
	}

	private Component createCardDescription() {
		HorizontalLayout holder = new HorizontalLayout();
		holder.setSizeFull();
		HorizontalLayout content = new HorizontalLayout();
		content.setSizeFull();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		content.setMargin(new MarginInfo(false, true, false, true));
		Label description = new Label(
				"<span style=\"color: rgb(33,150,243); word-wrap: break-word;\">" +
				courseModel.getDescription() + "</span>",
				ContentMode.HTML);
		description.setWidth("100%");
		description.addStyleName(CssUtil.TEXT_CENTER);
		content.addComponent(description);
		holder.addComponent(content);
		return holder;
	}

}
