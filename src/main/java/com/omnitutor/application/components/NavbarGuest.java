package com.omnitutor.application.components;

import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;

class NavbarGuest extends Navbar {

	private static final long serialVersionUID = 1L;

	public NavbarGuest(NavigatorUtil navigatorService) {
		super(navigatorService);
		addNewComponents();
	}

	private void addNewComponents() {
		navbarItemsHolder.addComponent(createCoursesNavbarItem());
		navbarItemsHolder.addComponent(createNavbarSeparator());
		navbarItemsHolder.addComponent(createLogInNavbarItem());
		navbarItemsHolder.addComponent(createNavbarBlankItem());
		navbarItemsHolder.addComponent(createRegisterNavbarItem());
		navbarItemsHolder.addComponent(createNavbarBlankItem());
	}

	private Component createCoursesNavbarItem() {
		return new NavbarItem(VaadinIcons.ACADEMY_CAP, "Courses", e -> navigator.navigateTo(RouteUtil.COURSES));
	}
	
	private Component createLogInNavbarItem() {
		return new NavbarItem(VaadinIcons.SIGN_IN_ALT, "Sign in", e -> navigator.navigateTo(RouteUtil.LOGIN));
	}

	private Component createRegisterNavbarItem() {
		return new NavbarItem(VaadinIcons.USERS, "Register", e -> navigator.navigateTo(RouteUtil.REGISTER));
	}
	
}
