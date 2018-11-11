package com.omnitutor.application.pages;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.components.QuestionAndAnswerItem;
import com.omnitutor.application.domain.query.QuestionAnswerQuery;
import com.omnitutor.application.domain.query.QuestionQuery;
import com.omnitutor.application.domain.query.QuizQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.command.QuizCommandService;
import com.omnitutor.application.services.query.QuestionAnswerQueryService;
import com.omnitutor.application.services.query.QuestionQueryService;
import com.omnitutor.application.services.query.QuizQueryService;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.QuestionAnswerConverterUtil;
import com.omnitutor.application.util.QuestionConverterUtil;
import com.omnitutor.application.util.QuizConverterUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.RouteUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.SuccessMessageUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class QuizPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	QuizQueryService quizQueryService;
	
	@Autowired
	QuizCommandService quizCommandService;

	@Autowired
	QuestionQueryService questionQueryService;

	@Autowired
	QuestionAnswerQueryService questionAnswerQueryService;

	@Autowired
	SessionService sessionService;

	private UserQuery user;

	private QuizQuery quiz;

	private List<QuestionQuery> quizQuestions;

	private List<ArrayList<QuestionAnswerQuery>> questionAnswers;
	
	private List<QuestionAndAnswerItem> questionComponents;

	private boolean resultsChecked;

	private int score;

	@PostConstruct
	void init() {
		setStyle();
		initProps();
	}

	private void initProps() {
		questionComponents = new ArrayList<>();
	}

	private void setStyle() {
		setMargin(false);
		setSpacing(false);
		setSizeFull();
		addStyleName(CssUtil.APP_BACKGROUND);
	}

	private void initComponents() {
		addNavbar();
		addContent();
		addFooter();
	}

	private void addNavbar() {
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
		Panel contentPanel = new Panel("Quiz");
		contentPanel.setIcon(VaadinIcons.QUESTION_CIRCLE_O);
		contentPanel.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_HEADER_CAPITON,
				CssUtil.PANEL_SHADOW_CONTENT);
		contentPanel.setWidth("900px");
		contentPanel.setContent(createQuizContent());
		content.addComponent(contentPanel);
		layout.addComponent(content);
		return layout;
	}

	private Component createQuizContent() {
		VerticalLayout layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label name = new Label(quiz.getName());
		name.addStyleName(ValoTheme.LABEL_H2);
		Component description = createDescription();
		Component questionsWithAnswers = createQuestionsWithAnswers();
		Component subtitle = createSubtitle();
		Component checkAnswers = createCheckAnswers();
		layout.addComponents(name, description, subtitle, questionsWithAnswers, checkAnswers);
		return layout;
	}

	private Component createCheckAnswers() {
		Button checkAnswers = new Button("Check answers");
		checkAnswers.setWidth("100%");
		checkAnswers.setIcon(VaadinIcons.CHECK);
		checkAnswers.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		checkAnswers.addClickListener(e -> checkForAnswers());
		Button goToMyCourses = new Button("Go to my courses");
		goToMyCourses.setWidth("100%");
		goToMyCourses.setIcon(VaadinIcons.BOOK);
		goToMyCourses.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		goToMyCourses.addClickListener(e -> navigator.navigateTo(RouteUtil.MY_COURSES));
		if(resultsChecked) return goToMyCourses;
		return checkAnswers;
	}

	private Object checkForAnswers() {
		resultsChecked = true;
		score = 0;
		questionComponents.stream().forEach(component -> {
			if(component.highlightAnswers()) score += 10;
		});
		try {
			quizCommandService.addPointsToCourse(user.getId(), quiz.getCourse().getId(), quiz.getId(), score);
			SuccessMessageUtil.showSuccessDialog("Answers has been checked!");
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		refreshView();
		return this;
	}

	private void refreshView() {
		removeAllComponents();
		initComponents();
	}

	private Component createSubtitle() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setHeight("40px");
		layout.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label text = new Label("There could be multiple correct answers");
		text.addStyleName(ValoTheme.LABEL_BOLD);
		layout.addComponent(text);
		return layout;
	}

	private Component createDescription() {
		Panel holder = new Panel("Quiz description");
		holder.addStyleName(ValoTheme.PANEL_WELL);
		holder.setWidth("100%");
		VerticalLayout layout = new VerticalLayout();
		Label desc = new Label(quiz.getDescription(), ContentMode.HTML);
		desc.setWidth("100%");
		desc.addStyleName(ValoTheme.LABEL_LARGE);
		layout.addComponent(desc);
		holder.setContent(layout);
		return holder;
	}

	private Component createQuestionsWithAnswers() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(new MarginInfo(false, true, false, true));
		ArrayList<ArrayList<QuestionAnswerQuery>> answers = new ArrayList<>(questionAnswers);
		quizQuestions.stream().forEach(question -> {
			if(resultsChecked) {
				QuestionAndAnswerItem item = questionComponents.remove(0);
				layout.addComponent(item);
				questionComponents.add(item);
				return;
			}
			QuestionAndAnswerItem item = new QuestionAndAnswerItem(question, answers.remove(0));
			questionComponents.add(item);
			layout.addComponent(item);
		});
		return layout;
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
		Long quizId = new Long(event.getParameters());
		queryAllProps(quizId);
		if (user == null)
			return;
		refreshView();
	}

	private void queryAllProps(Long quizId) {
		try {
			quiz = QuizConverterUtil.convertEntityToDomain(quizQueryService.findById(quizId));
			quizQuestions = QuestionConverterUtil
					.convertEntityListToDomainList(questionQueryService.findAllByQuiz(quiz.getId()));
			questionAnswers = new ArrayList<ArrayList<QuestionAnswerQuery>>();
			quizQuestions.stream().forEach(question -> {
				try {
					questionAnswers.add(QuestionAnswerConverterUtil
							.convertEntityListToDomainList(questionAnswerQueryService.findAllByQuestion(question.getId())));
				} catch (Exception e) {
					ErrorMessageUtil.showErrorDialog(e.getMessage());
				}
			});
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
	}

}
