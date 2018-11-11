package com.omnitutor.application.components;

import java.util.List;
import java.util.stream.Collectors;

import com.omnitutor.application.domain.query.QuestionAnswerQuery;
import com.omnitutor.application.domain.query.QuestionQuery;
import com.omnitutor.application.util.CssUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class QuestionAndAnswerItem extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	private List<QuestionAnswerQuery> answers;

	private QuestionQuery question;

	private Label status;

	private CheckBoxGroup<QuestionAnswerQuery> cbg;

	public QuestionAndAnswerItem(QuestionQuery question, List<QuestionAnswerQuery> answers) {
		setStyle();
		initProps(question, answers);
		initComponents();
	}

	private void setStyle() {
		setWidth("100%");
		setMargin(new MarginInfo(true, false, true, false));
		addStyleName(CssUtil.DOUBLE_BORDER_BOTTOM);
		setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
	}

	private void initProps(QuestionQuery question, List<QuestionAnswerQuery> answers) {
		this.question = question;
		this.answers = answers;
	}

	private void initComponents() {
		cbg = new CheckBoxGroup<QuestionAnswerQuery>(question.getQuestionText());
		cbg.setItems(answers);
		status = new Label();
		status.setContentMode(ContentMode.HTML);
		status.addStyleName(CssUtil.TEXT_HUGE);
		addComponents(cbg, status);
		setExpandRatio(cbg, 4);
		setExpandRatio(status, 1);
	}

	private boolean checkAnswers() {
		int selected = cbg.getSelectedItems().stream().filter(item -> item.getIsCorrect()).collect(Collectors.toList())
				.size();
		int correct = answers.stream().filter(item -> item.getIsCorrect()).collect(Collectors.toList()).size();
		return selected == correct;
	}
	
	public boolean highlightAnswers() {
		if(checkAnswers()) {
			status.setValue(VaadinIcons.CHECK.getHtml());
			status.addStyleName(CssUtil.TEXT_GREEN);
			return true;
		}
		status.setValue(VaadinIcons.CLOSE.getHtml());
		status.addStyleName(CssUtil.TEXT_RED);
		return false;
	}

}
