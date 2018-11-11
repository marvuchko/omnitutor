package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class NavbarItem extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	
	public NavbarItem(VaadinIcons icon, String text, LayoutClickListener listener) {
		initComponents(icon, text, listener);
	}

	private void initComponents(VaadinIcons icon, String text, LayoutClickListener listener) {
		Label content = new Label(icon.getHtml() + "&nbsp;" + text, ContentMode.HTML);
		content.addStyleName(CssUtil.NAVBAR_ITEM);
		addLayoutClickListener(listener);
		addComponent(content);
	}

}
