package com.omnitutor.application.util;

import java.util.List;

import com.omnitutor.application.components.CreateLectureDialog;
import com.omnitutor.application.domain.LectureDomain;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateLectureDialogUtil {

	private static UI rootUI;

	public static void showCreateLecturesDialog(FileUploadUtil fileUpload, List<LectureDomain> lectures) {
		CreateLectureDialog dialog = new CreateLectureDialog(fileUpload, lectures);
		rootUI.addWindow(dialog);
	}
	
	public static void setRootUI(UI ui) {
		rootUI = ui;
	}
	
}
