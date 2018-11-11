package com.omnitutor.application.pages;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Footer;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.ResourceUtil;
import com.omnitutor.application.util.RouteUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class LandingPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;
	
	@PostConstruct
	void init() {
		setStyle();
		initComponents();
	}

	private void setStyle() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
		addStyleName(CssUtil.APP_BACKGROUND);
	}

	private void initComponents() {
		addDetailedLogoAndButtons();
		addFooter();
	}

	private void addFooter() {
		Footer footer = new Footer();
		addComponent(footer);
		setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		setExpandRatio(footer, 1);
	}

	private void addDetailedLogoAndButtons() {
		VerticalLayout landing = new VerticalLayout();
		landing.setMargin(new MarginInfo(true, false, false, false));
		landing.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Image image = new Image(null, new ThemeResource(ResourceUtil.DETAILS_LOGO));
		image.setWidth("40%");
		landing.addComponents(image, addButtons(), addRegistrationLink());
		addComponent(landing);
		setComponentAlignment(landing, Alignment.MIDDLE_CENTER);
		setExpandRatio(landing, 11);
	}

	private Component addButtons() {
		HorizontalLayout landingButtons = new HorizontalLayout();
		landingButtons.setMargin(new MarginInfo(true, false, false, false));
		Button courses = new Button("Courses");
		courses.setIcon(VaadinIcons.ACADEMY_CAP);
		courses.addStyleName(ValoTheme.BUTTON_PRIMARY);
		courses.addClickListener(e -> navigator.navigateTo(RouteUtil.COURSES));
		Button logIn = new Button("Log in");
		logIn.setIcon(VaadinIcons.SIGN_IN_ALT);
		logIn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		logIn.addClickListener(e -> navigator.navigateTo(RouteUtil.LOGIN));
		landingButtons.addComponents(courses, logIn);
		addComponent(landingButtons);
		return landingButtons;
	}

	private Component addRegistrationLink() {
		HorizontalLayout linkHolder = new HorizontalLayout();
		linkHolder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label text = new Label();
		text.setValue("Don't have an account? Create one here...");
		text.addStyleNames(CssUtil.TEXT_BLUE, CssUtil.LINK_LARGE);
		linkHolder.addComponent(text);
		linkHolder.addLayoutClickListener(e -> navigator.navigateTo(RouteUtil.REGISTER));
		return linkHolder;
	}

}
