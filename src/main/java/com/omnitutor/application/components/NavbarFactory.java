package com.omnitutor.application.components;

import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.SessionService;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NavbarFactory {
	
	public static enum NavbarType {
		DEFAULT, USER, ADMIN
	}
	
	public static NavbarType getType(String role) {
		switch(role) {
		case RoleUtil.USER:
			return NavbarType.USER;
		case RoleUtil.ADMIN:
			return NavbarType.ADMIN;
		default:
			return NavbarType.DEFAULT;
		}
	}

	public static Navbar createNavbar(
			NavbarType type,
			NavigatorUtil navigator,
			SessionService sessionService) {
		switch(type) {
		case DEFAULT : 
			return new NavbarGuest(navigator);
		case USER:
			return new NavbarUser(navigator, sessionService);
		case ADMIN:
			return null;
		default: 
			return new NavbarGuest(navigator);
		}
	}
	
}
