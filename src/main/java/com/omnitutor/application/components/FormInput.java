package com.omnitutor.application.components;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import lombok.Getter;

@Getter
public class FormInput extends HorizontalLayout {
	
	private static final long serialVersionUID = 1L;
	
	private String value;

	public FormInput(String caption, String value) {
		this.value = value;
		initComponents(caption, value);
	}

	private void initComponents(String caption, String value) {
		Label lblCaption = new Label(caption);
		TextField txtValue = new TextField();
		txtValue.setWidth("100%");
		txtValue.setValue(value);
		txtValue.addValueChangeListener(e -> {
			this.value = txtValue.getValue();
		});
		txtValue.setValueChangeMode(ValueChangeMode.LAZY);
		addComponents(lblCaption, txtValue);
		setExpandRatio(lblCaption, 1);
		setExpandRatio(txtValue, 4);
	}
	
}
