package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ErrorDialog extends Window {

	private static final long serialVersionUID = 1L;
	
	public ErrorDialog(String message) {
		super(" Error Dialog");
		setStyle();
		initComponents(message);
	}

	private void setStyle() {
		setIcon(VaadinIcons.WARNING);
		setResizable(false);
	}

	private void initComponents(String message) {
		VerticalLayout subContent = new VerticalLayout();
		subContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label errorLabel = new Label();
		errorLabel.setValue(message);
		errorLabel.addStyleNames(CssUtil.TEXT_RED);
		subContent.addComponents(errorLabel);
		setContent(subContent);
		setModal(true);
		center();
	}
	
}
