package com.omnitutor.application.pages;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.query.LoginQueryService;
import com.omnitutor.application.services.query.UserQueryService;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.MailDialogUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RouteUtil;
import com.omnitutor.application.util.SessionService;
import com.omnitutor.application.util.UserConverterUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class LoginPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;
	
	@Autowired
	LoginQueryService loginService;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	UserQueryService userQueryService;

	TextField username;
	PasswordField password;

	@PostConstruct
	void init() {
		setStyle();
		initComponents();
	}

	private void initComponents() {
		addNavbar();
		addLogInForm();
		addFooter();
	}

	private void setStyle() {
		setSizeFull();
		addStyleName(CssUtil.APP_BACKGROUND);
		setMargin(false);
		setSpacing(false);
	}

	private void addNavbar() {
		Navbar navbar = NavbarFactory.createNavbar(NavbarType.DEFAULT, navigator, null);
		addComponent(navbar);
		setExpandRatio(navbar, 1);
	}

	private void addLogInForm() {
		Panel panel = createPanel();
		addComponent(panel);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		setExpandRatio(panel, 10);
	}

	private Panel createPanel() {
		Panel panel = new Panel("Sign in");
		panel.setIcon(VaadinIcons.SIGN_IN_ALT);
		panel.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_CUSTOM_CAPTION);
		panel.addStyleNames(CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_SHADOW_CONTENT);
		panel.setSizeUndefined();
		panel.setContent(createPanelContent());
		return panel;
	}

	private Component createPanelContent() {
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setMargin(new MarginInfo(true, true, false, true));
		panelContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		panelContent.addComponent(createForm());
		panelContent.addComponent(addResetPasswordLink());
		panelContent.addComponent(createSubmitButton());
		return panelContent;
	}

	private Component createForm() {
		VerticalLayout content = new VerticalLayout();
		content.setMargin(new MarginInfo(true, true, false, true));
		username = new TextField();
		username.setWidth("100%");
		username.setPlaceholder("Username");
		password = new PasswordField();
		password.setWidth("100%");
		password.setPlaceholder("Password");
		content.addComponent(username);
		content.addComponent(password);
		return content;
	}

	private Component createSubmitButton() {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(new MarginInfo(true, false, true, false));
		buttonLayout.setWidth("100%");
		buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Button submit = new Button("Sign in");
		submit.setIcon(VaadinIcons.SIGN_IN_ALT);
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addClickListener(e -> handleSubmit());
		buttonLayout.addComponent(submit);
		return buttonLayout;
	}

	private Object handleSubmit() {
		try {
			validateInputs();
			UserQuery user = UserConverterUtil
					.convertEntityToDomain(loginService.logIn(username.getValue(), password.getValue()));
			sessionService.setUser(user);
			navigator.navigateTo(RouteUtil.COURSES);
			clearInputs();
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		return this;
	}

	private void clearInputs() {
		username.setValue("");
		password.setValue("");
	}

	private void validateInputs() throws Exception {
		if (username.getValue().isEmpty())
			throw new Exception("Username can't be blank.");
		if (password.getValue().isEmpty())
			throw new Exception("Password can't be blank.");
	}

	private Component addResetPasswordLink() {
		HorizontalLayout linkLayout = new HorizontalLayout();
		linkLayout.setMargin(false);
		Link resetPassword = new Link();
		resetPassword.setCaption("Forgot your password?");
		resetPassword.addStyleName(ValoTheme.LINK_SMALL);
		linkLayout.addComponent(resetPassword);
		linkLayout.setComponentAlignment(resetPassword, Alignment.MIDDLE_CENTER);
		linkLayout.addLayoutClickListener(e -> sendMail());
		return linkLayout;
	}

	private Object sendMail() {
		MailDialogUtil.showDialog(userQueryService);
		return this;
	}

	private void addFooter() {
		Footer footer = new Footer();
		addComponent(footer);
		setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
		setExpandRatio(footer, 1);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		clearInputs();
	}

}
