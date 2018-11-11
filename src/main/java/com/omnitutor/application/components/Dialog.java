package com.omnitutor.application.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class Dialog extends Window {

	private static final long serialVersionUID = 1L;
	
	protected Panel holder;

	public Dialog(String message, VaadinIcons icon, String subtitle) {
		super(message);
		setStyle(icon);
		init(subtitle);
	}

	private void setStyle(VaadinIcons icon) {
		setIcon(icon);
		setResizable(false);
	}

	private void init(String subtitle) {
		VerticalLayout subContent = new VerticalLayout();
		subContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		holder = new Panel(subtitle);
		holder.setWidth("100%");
		subContent.addComponent(holder);
		setContent(subContent);
		setModal(true);
		center();
	}

}
