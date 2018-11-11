package com.omnitutor.application.util;

import org.springframework.stereotype.Service;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;

@Service
@UIScope
public class NavigatorUtil {

	private Navigator navigator;
	
	public void init(UI ui, SingleComponentContainer container) {
		navigator = new Navigator(ui, container);
	}
	
	public void addRoute(String route, View view) {
		navigator.addView(route, view);
	}
	
	public void addRoute(String route, Class<? extends View> viewClass) {
		navigator.addView(route, viewClass);
	}
	
	public void navigateTo(String url) {
		navigator.navigateTo(url);
	}
	
}
