package com.omnitutor.application.pages;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.domain.command.QuestionCommand;
import com.omnitutor.application.domain.command.QuizCommand;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.command.QuizCommandService;
import com.omnitutor.application.services.query.CourseQueryService;
import com.omnitutor.application.services.query.UserQueryService;
import com.omnitutor.application.util.CourseConverterUtil;
import com.omnitutor.application.util.CreateQuestionDialogUtil;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.SuccessMessageUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import rx.Observable;

@SpringComponent
@UIScope
public class CreateQuizPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	SessionService sessionService;

	@Autowired
	CourseQueryService courseQueryService;

	@Autowired
	UserQueryService userQueryService;

	@Autowired
	QuizCommandService quizCommandService;

	private QuizCommand quiz;

	private ComboBox<CourseQuery> selectCourse;

	private UserQuery user;

	private List<CourseQuery> courses;

	private Grid<QuestionCommand> grid;

	private Observable<QuestionCommand> questionObservable;

	private TextField quizName;

	private RichTextArea quizDesc;

	@PostConstruct
	void init() {
		setStyle();
	}

	private void initProps() {
		quiz = new QuizCommand();
		quiz.setQuestions(new ArrayList<>());
		questionObservable = CreateQuestionDialogUtil.getObservable();
	}

	private void subscribeToObservable() {
		questionObservable.subscribe(question -> {
			quiz.getQuestions().add(question);
			grid.setItems(quiz.getQuestions());
		});
	}

	private void setStyle() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
		addStyleName(CssUtil.APP_BACKGROUND);
	}

	private void initComponents() {
		addHeader();
		addContent();
		addFooter();
	}

	private void addHeader() {
		UserQuery user = sessionService.getUser();
		NavbarType type = NavbarFactory.getType(RoleUtil.getRole(user));
		Navbar navbar = NavbarFactory.createNavbar(type, navigator, sessionService);
		addComponent(navbar);
		setComponentAlignment(navbar, Alignment.TOP_CENTER);
		setExpandRatio(navbar, 1);
	}

	private void addContent() {
		Panel content = new Panel();
		content.addStyleName(ValoTheme.PANEL_BORDERLESS);
		content.setSizeFull();
		content.setContent(createContent());
		addComponent(content);
		setExpandRatio(content, 10);
	}

	private Component createContent() {
		VerticalLayout layout = new VerticalLayout();
		VerticalLayout content = new VerticalLayout();
		content.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		Panel contentPanel = new Panel("Create quiz");
		contentPanel.setIcon(VaadinIcons.QUESTION_CIRCLE);
		contentPanel.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_HEADER_CAPITON,
				CssUtil.PANEL_SHADOW_CONTENT);
		contentPanel.setWidth("900px");
		contentPanel.setContent(createQuizForm());
		content.addComponent(contentPanel);
		layout.addComponent(content);
		return layout;
	}

	private Component createQuizForm() {
		VerticalLayout layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Component selectCourse = createSelectCourseComponent();
		Component nameAndDescOfQuiz = createNameAndDescOfQuiz();
		Component questionInput = createQuestionInput();
		Component quizItems = createQuizItems();
		Component submitButton = createSubmitButton();
		layout.addComponents(selectCourse, nameAndDescOfQuiz, questionInput, quizItems, submitButton);
		return layout;
	}

	private Component createSubmitButton() {
		Button submit = new Button("Submit");
		submit.setIcon(VaadinIcons.DATABASE);
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.setWidth("100%");
		submit.addClickListener(e -> onSubmit());
		return submit;
	}

	private Object onSubmit() {
		try {
			validateInputs();
			quiz.setName(quizName.getValue());
			quiz.setDescription(quizDesc.getValue());
			quiz.setCourseId(selectCourse.getSelectedItem().get().getId());
			quizCommandService.createQuiz(quiz);
			clearInputs();
			SuccessMessageUtil.showSuccessDialog("You have successfuly created new Quiz:  " + quiz.getName());
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		return this;
	}

	private void clearInputs() {
		quizName.setValue("");
		quizDesc.setValue("");
		grid.setItems(new ArrayList<>());
	}

	private void validateInputs() throws Exception {
		if (quizName.getValue().isEmpty())
			throw new Exception("Quiz name cannot be empty!");
		if (quiz.getQuestions().size() == 0)
			throw new Exception("There are no lectures added!");
	}

	private Component createSelectCourseComponent() {
		Panel holder = new Panel("Select course");
		VerticalLayout layout = new VerticalLayout();
		selectCourse = new ComboBox<CourseQuery>();
		selectCourse.setWidth("100%");
		selectCourse.setItems(courses);
		selectCourse.setSelectedItem(courses.get(0));
		selectCourse.setEmptySelectionAllowed(false);
		selectCourse.addSelectionListener(e -> {
			CourseQuery course = selectCourse.getSelectedItem().get();
			quiz.setCourseId(course.getId());
		});
		layout.addComponent(selectCourse);
		holder.setContent(layout);
		return holder;
	}

	private Component createNameAndDescOfQuiz() {
		Panel nameAndDesc = new Panel("Quiz name and description");
		nameAndDesc.setWidth("100%");
		nameAndDesc.setHeight("400px");
		nameAndDesc.addStyleName(ValoTheme.PANEL_WELL);
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		quizName = new TextField();
		quizName.setPlaceholder("Enter quiz name");
		quizName.setWidth("100%");
		quizDesc = new RichTextArea("Quiz description (Optional)");
		quizDesc.setSizeFull();
		layout.addComponents(quizName, quizDesc);
		layout.setExpandRatio(quizName, 1);
		layout.setExpandRatio(quizDesc, 7);
		nameAndDesc.setContent(layout);
		return nameAndDesc;
	}

	private Component createQuestionInput() {
		Panel questionInput = new Panel("Create question");
		questionInput.setWidth("100%");
		questionInput.setHeight("100px");
		questionInput.addStyleName(ValoTheme.PANEL_WELL);
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		Button newQuestion = new Button("Add new question");
		newQuestion.setWidth("100%");
		newQuestion.setIcon(VaadinIcons.PLUS_CIRCLE);
		newQuestion.addClickListener(e -> {
			CreateQuestionDialogUtil.showDialog(quizCommandService);
		});
		layout.addComponent(newQuestion);
		questionInput.setContent(layout);
		return questionInput;
	}

	private Component createQuizItems() {
		Panel holder = new Panel("Questions");
		holder.setWidth("100%");
		holder.addStyleName(ValoTheme.PANEL_WELL);
		VerticalLayout layout = new VerticalLayout();
		grid = new Grid<QuestionCommand>();
		grid.setHeightByRows(4);
		grid.setWidth("100%");
		grid.setItems(quiz.getQuestions());
		grid.removeAllColumns();
		grid.addColumn(QuestionCommand::getQuestionText).setCaption("Question");
		Button removeSelected = new Button("Remove selected questions");
		removeSelected.setIcon(VaadinIcons.CLOSE);
		removeSelected.setWidth("100%");
		removeSelected.addClickListener(e -> {
			QuestionCommand question = grid.getSelectedItems().stream().findFirst().get();
			quiz.getQuestions().remove(question);
			grid.setItems(quiz.getQuestions());
		});
		layout.addComponents(grid, removeSelected);
		holder.setContent(layout);
		return holder;
	}

	private void addFooter() {
		Footer footer = new Footer();
		addComponent(footer);
		setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		setExpandRatio(footer, 1);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		user = sessionService.getUser();
		try {
			courses = CourseConverterUtil.convertEntityListToDomainList(
					courseQueryService.findAllByCreator(userQueryService.findById(user.getId())));
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		if (user == null || courses == null || courses.size() == 0)
			return;
		removeAllComponents();
		initProps();
		initComponents();
		subscribeToObservable();
	}

}
