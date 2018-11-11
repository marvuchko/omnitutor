package com.omnitutor.application.components;

import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.omnitutor.application.util.SessionService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;

class NavbarUser extends Navbar {

	private static final long serialVersionUID = 1L;
	
	private SessionService sessionService;
	
	public NavbarUser(NavigatorUtil navigatorService, SessionService sessionService) {
		super(navigatorService);
		initProps(sessionService);
		addNewComponents();
	}

	private void initProps(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	private void addNewComponents() {
		navbarItemsHolder.addComponent(createCoursesNavbarItem());
		navbarItemsHolder.addComponent(createNavbarBlankItem());
		navbarItemsHolder.addComponent(createProfileNavbarItem());
		navbarItemsHolder.addComponent(createNavbarBlankItem());
		navbarItemsHolder.addComponent(createMyCoursesNavbarItem());
		navbarItemsHolder.addComponent(createNavbarSeparator());
		navbarItemsHolder.addComponent(createLogOutNavbarItem());
		navbarItemsHolder.addComponent(createNavbarBlankItem());
	}

	private Component createCoursesNavbarItem() {
		return new NavbarItem(VaadinIcons.ACADEMY_CAP, "Courses", e -> navigator.navigateTo(RouteUtil.COURSES));
	}

	private Component createProfileNavbarItem() {
		return new NavbarItem(VaadinIcons.USER, "Profile", e -> navigator.navigateTo(RouteUtil.PROFILE));
	}

	private Component createMyCoursesNavbarItem() {
		return new NavbarItem(VaadinIcons.BOOK, "My courses", e -> navigator.navigateTo(RouteUtil.MY_COURSES));
	}

	private Component createLogOutNavbarItem() {
		return new NavbarItem(VaadinIcons.SIGN_OUT, "Log out", e -> {
			sessionService.setUser(null);
			navigator.navigateTo(RouteUtil.ROOT);
		});
	}

}
