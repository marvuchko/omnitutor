package com.omnitutor.application.util;

import com.omnitutor.application.components.SuccessDialog;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessMessageUtil {
	
	private static UI rootUI;
	
	public static void showSuccessDialog(String message) {
		SuccessDialog dialog = new SuccessDialog(message);
		rootUI.addWindow(dialog);
	}
	
	public static void setRootUI(UI ui) {
		rootUI = ui;
	}
	
}
