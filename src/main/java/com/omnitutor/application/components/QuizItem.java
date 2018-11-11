package com.omnitutor.application.components;

import com.omnitutor.application.domain.query.QuizQuery;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class QuizItem extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	private QuizQuery quiz;

	private NavigatorUtil navigator;

	public QuizItem(QuizQuery quiz, NavigatorUtil navigator) {
		setStyle();
		initProps(quiz, navigator);
		initComponents();
	}

	private void initProps(QuizQuery quiz, NavigatorUtil navigator) {
		this.quiz = quiz;
		this.navigator = navigator;
	}

	private void setStyle() {
		setWidth("100%");
		setHeight("60px");
		setMargin(new MarginInfo(false, true, false, true));
		addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	}

	private void initComponents() {
		Label name = new Label("<b>" + VaadinIcons.QUESTION_CIRCLE_O.getHtml() + " " + quiz.getName() + "</b>",
				ContentMode.HTML);
		name.setWidth("100%");
		name.addStyleName(CssUtil.TEXT_BLUE);
		Button startQuiz = new Button("Start quiz");
		startQuiz.setIcon(VaadinIcons.ARROW_CIRCLE_RIGHT_O);
		startQuiz.setWidth("100%");
		startQuiz.addStyleName(ValoTheme.BUTTON_PRIMARY);
		startQuiz.addClickListener(e -> navigator.navigateTo(RouteUtil.QUIZ + "/" + quiz.getId()));
		addComponents(name, startQuiz);
		setExpandRatio(name, 5);
		setExpandRatio(startQuiz, 1);
	}

}
