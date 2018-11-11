package com.omnitutor.application.components;

import java.util.ArrayList;

import com.omnitutor.application.domain.command.QuestionAnswerCommand;
import com.omnitutor.application.domain.command.QuestionCommand;
import com.omnitutor.application.services.command.QuizCommandService;
import com.omnitutor.application.util.CreateQuestionDialogUtil;
import com.omnitutor.application.util.CssUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CreateQuestionDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	private RichTextArea textInput;

	private Grid<QuestionAnswerCommand> grid;

	private ArrayList<QuestionAnswerCommand> answers;

	private TextField answerInput;

	private Button addAnswer;

	private Button removeAnswers;

	private CheckBox correctAnswer;
	
	public CreateQuestionDialog(QuizCommandService quizCommandService) {
		super(" Create quiz question", VaadinIcons.QUESTION_CIRCLE_O, "");
		setStyle();
		initProps(quizCommandService);
		initComponents();
	}

	private void setStyle() {
		setIcon(VaadinIcons.PLUS_CIRCLE_O);
		setResizable(false);
	}

	private void initProps(QuizCommandService quizCommandService) {
		this.answers = new ArrayList<QuestionAnswerCommand>();
	}

	private void initComponents() {
		holder.setWidth("750px");
		holder.setHeight("550px");
		VerticalLayout layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Component questionText = createQuestionText();
		Component questionAnswers = createAnswers();
		Component submit = createSubmit();
		layout.addComponents(questionText, questionAnswers, submit);
		holder.setContent(layout);
	}

	private Component createSubmit() {
		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.setIcon(VaadinIcons.DATABASE);
		submit.setWidth("100%");
		submit.addClickListener(e -> onSubmit());
		return submit;
	}

	private Object onSubmit() {
		QuestionCommand question = new QuestionCommand();
		question.setQuestionText(textInput.getValue());
		question.setAnswers(answers);
		CreateQuestionDialogUtil.publish(question);
		this.close();
		return this;
	}

	private Component createAnswers() {
		Panel holder = new Panel("Quiz answers");
		holder.setWidth("100%");
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		Component answerInput = createAnswerInput();
		Component answerGrid = createAnswerGrid();
		Component answerButtons = createAnswerButtons();
		layout.addComponents(answerInput, answerGrid, answerButtons);
		holder.setContent(layout);
		return holder;
	}

	private Component createAnswerInput() {
		HorizontalLayout holder = new HorizontalLayout();
		holder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		holder.setWidth("100%");
		CssLayout layout = new CssLayout();
		layout.setWidth("100%");
		layout.addStyleName(CssUtil.TEXT_CENTER);
		answerInput = new TextField();
		answerInput.setWidth("90%");
		answerInput.setPlaceholder("Enter answer");
		Button clear = new Button();
		clear.setIcon(VaadinIcons.CLOSE_CIRCLE);
		clear.addClickListener(e -> answerInput.setValue(""));
		layout.addComponents(answerInput, clear);
		layout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		correctAnswer = new CheckBox("Is answer correct");
		holder.addComponents(layout, correctAnswer);
		holder.setExpandRatio(layout, 3);
		holder.setExpandRatio(correctAnswer, 1);
		return holder;
	}

	private Component createAnswerGrid() {
		grid = new Grid<QuestionAnswerCommand>();
		grid.setHeightByRows(4);
		grid.setWidth("100%");
		grid.setItems(answers);
		grid.removeAllColumns();
		grid.addColumn(QuestionAnswerCommand::getText).setCaption("Answers");
		grid.addColumn(QuestionAnswerCommand::getIsCorrect).setCaption("Correct answer");
		grid.setSelectionMode(SelectionMode.SINGLE);
		return grid;
	}

	private Component createAnswerButtons() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		addAnswer = new Button("Add answer");
		addAnswer.setWidth("100%");
		addAnswer.setIcon(VaadinIcons.CHECK_CIRCLE_O);
		addAnswer.addClickListener(e -> addAnswerToGrid());
		removeAnswers = new Button("Remove answers");
		removeAnswers.setWidth("100%");
		removeAnswers.setIcon(VaadinIcons.CLOSE);
		removeAnswers.addClickListener(e -> removeSelectedAnswers());
		layout.addComponents(addAnswer, removeAnswers);
		return layout;
	}

	private Object removeSelectedAnswers() {
		answers.remove(grid.getSelectedItems().stream().findFirst().get());
		grid.setItems(answers);
		return this;
	}

	private Object addAnswerToGrid() {
		if(answerInput.getValue().isEmpty()) return this;
		String answer = answerInput.getValue();
		answerInput.setValue("");
		QuestionAnswerCommand answerCommand = new QuestionAnswerCommand();
		answerCommand.setText(answer);
		answerCommand.setIsCorrect(correctAnswer.getValue());
		answers.add(answerCommand);
		correctAnswer.clear();
		grid.setItems(answers);
		return this;
	}

	private Component createQuestionText() {
		Panel holder = new Panel("Question text");
		holder.setWidth("100%");
		holder.setHeight("300px");
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		textInput = new RichTextArea();
		textInput.setSizeFull();
		layout.addComponents(textInput);
		holder.setContent(layout);
		return holder;
	}

}
