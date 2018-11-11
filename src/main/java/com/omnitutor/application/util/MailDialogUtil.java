package com.omnitutor.application.util;

import com.omnitutor.application.components.MailDialog;
import com.omnitutor.application.services.query.UserQueryService;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailDialogUtil {
	
	private static UI rootUI;
	
	public static void showDialog(UserQueryService userQueryService) {
		MailDialog mailDialog = new MailDialog(userQueryService);
		rootUI.addWindow(mailDialog);
	}

	public static void setRootUI(UI ui) {
		rootUI = ui;
	}

}
