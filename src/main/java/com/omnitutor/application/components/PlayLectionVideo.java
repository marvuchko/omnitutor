package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class PlayLectionVideo extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	
	private NavigatorUtil navigator;

	public PlayLectionVideo(
			NavigatorUtil navigator,
			String lectureName,
			Long lectureId,
			String duration,
			boolean isWatched) {
		setStyle();
		initProps(navigator);
		initComponents(lectureName, lectureId, duration, isWatched);
	}

	private void initProps(NavigatorUtil navigator) {
		this.navigator = navigator;
	}

	private void setStyle() {
		addStyleNames(CssUtil.SOLID_BORDER_BOTTOM);
	}

	private void initComponents(String lectureName, Long lectureId, String duration, boolean isWatched) {
		Component playButton = createPlayButton(lectureId);
		Component lblLectureName = createLectureNameLabel(lectureName);
		Component lblDuration = createDurationLabel(duration);
		Component lblHasLectureBeenWatched = createWatchedLectureLabel(isWatched);
		addComponents(playButton, lblLectureName, lblDuration, lblHasLectureBeenWatched);
		setExpandRatio(playButton, 1);
		setExpandRatio(lblLectureName, 4);
		setExpandRatio(lblDuration, 1);
		setExpandRatio(lblHasLectureBeenWatched, 1);
	}

	private Component createWatchedLectureLabel(boolean isWatched) {
		Label lblIsWatched = new Label();
		lblIsWatched.setContentMode(ContentMode.HTML);
		lblIsWatched.addStyleName(CssUtil.TEXT_GREEN);
		lblIsWatched.setValue(isWatched ? VaadinIcons.CHECK.getHtml() : "");
		return lblIsWatched;
	}

	private Component createDurationLabel(String duration) {
		Label lblDuration = new Label(VaadinIcons.CLOCK.getHtml() + " " + duration + " min", ContentMode.HTML);
		return lblDuration;
	}

	private Component createLectureNameLabel(String lectureName) {
		Label lblLectureName = new Label("<b>" + lectureName + "</b>", ContentMode.HTML);
		lblLectureName.setWidth("100%");
		return lblLectureName;
	}

	private Component createPlayButton(Long lectureId) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(new MarginInfo(false, false, false, true));
		Label playButton = new Label(VaadinIcons.PLAY.getHtml(), ContentMode.HTML);
		playButton.addStyleNames(CssUtil.TEXT_BLUE, CssUtil.CURSOR_POINTER);
		layout.addComponent(playButton);
		layout.addLayoutClickListener(e -> navigator.navigateTo(RouteUtil.VIDEO + "/" + lectureId));
		return layout;
	}
	
}
