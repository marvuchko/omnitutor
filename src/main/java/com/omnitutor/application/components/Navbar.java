package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.ResourceUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

public abstract class Navbar extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	protected final NavigatorUtil navigator;

	protected HorizontalLayout navbarItemsHolder;
	
	public Navbar(NavigatorUtil navigatorService) {
		navigator = navigatorService;
		setStyle();
		initComponents();
	}

	private void setStyle() {
		setSizeFull();
		addStyleName(CssUtil.NAVBAR_DEFAULT);
	}

	private void initComponents() {
		addLogo();
		addNavbarItems();
	}

	private void addNavbarItems() {
		navbarItemsHolder = new HorizontalLayout();
		navbarItemsHolder.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		addComponent(navbarItemsHolder);
		setComponentAlignment(navbarItemsHolder, Alignment.MIDDLE_RIGHT);
		setExpandRatio(navbarItemsHolder, 3);
	}

	private void addLogo() {
		HorizontalLayout logoHolder = new HorizontalLayout();
		logoHolder.setMargin(new MarginInfo(false, false, false, true));
		Image logo = new Image(null, new ThemeResource(ResourceUtil.LOGO_SMALL_DETAILS));
		logo.addStyleName(CssUtil.NAVBAR_ICON);
		logoHolder.addComponent(logo);
		addComponent(logoHolder);
		setComponentAlignment(logoHolder, Alignment.MIDDLE_LEFT);
		setExpandRatio(logoHolder, 1);
	}
	
	protected Component createNavbarSeparator() {
		Label separator = new Label();
		separator.setContentMode(ContentMode.HTML);
		separator.setValue(VaadinIcons.LINE_V.getHtml());
		separator.addStyleName(CssUtil.TEXT_BLUE);
		return separator;
	}
	
	protected Component createNavbarBlankItem() {
		return new Label();
	}

}
