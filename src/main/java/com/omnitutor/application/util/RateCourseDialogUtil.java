package com.omnitutor.application.util;

import com.omnitutor.application.components.RateCourseDialog;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.services.command.CourseCommandService;
import com.vaadin.ui.UI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RateCourseDialogUtil {

	private static UI rootUI;

	public static void showRateCourseDialog(CourseQuery course, CourseCommandService commandService) {
		RateCourseDialog dialog = new RateCourseDialog(course, commandService);
		rootUI.addWindow(dialog);
	}

	public static void setRootUI(UI ui) {
		rootUI = ui;
	}

}
