package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SuccessDialog extends Window {

	public SuccessDialog(String message) {
		super(" Success Dialog");
		setStyle();
		initComponents(message);
	}

	private void setStyle() {
		setIcon(VaadinIcons.CHECK);
		setResizable(false);
	}

	private void initComponents(String message) {
		VerticalLayout subContent = new VerticalLayout();
		subContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label errorLabel = new Label();
		errorLabel.setValue(message);
		errorLabel.addStyleNames(CssUtil.TEXT_BLUE);
		subContent.addComponents(errorLabel);
		setContent(subContent);
		setModal(true);
		center();
	}

	private static final long serialVersionUID = 1L;

}
