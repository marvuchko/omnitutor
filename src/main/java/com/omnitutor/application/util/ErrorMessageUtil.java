package com.omnitutor.application.util;

import com.omnitutor.application.components.ErrorDialog;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessageUtil {

	private static UI rootUI;
	
	public static void showErrorDialog(String message) {
		ErrorDialog dialog = new ErrorDialog(message);
		rootUI.addWindow(dialog);
	}
	
	public static void setRootUI(UI ui) {
		rootUI = ui;
	}
	
}
