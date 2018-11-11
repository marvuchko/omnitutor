package com.omnitutor.application.components;

import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.query.UserQueryService;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.GmailUtil;
import com.omnitutor.application.util.SuccessMessageUtil;
import com.omnitutor.application.util.UserConverterUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class MailDialog extends Window {

	private static final long serialVersionUID = 1L;

	private TextField email;

	private UserQueryService userQueryService;

	public MailDialog(UserQueryService userQueryService) {
		super(" Password recovery");
		setStyle();
		initProps(userQueryService);
		initComponents();
	}

	private void initProps(UserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}

	private void setStyle() {
		setIcon(VaadinIcons.CHECK);
		setResizable(false);
	}

	private void initComponents() {
		VerticalLayout subContent = new VerticalLayout();
		subContent.setWidth("400px");
		subContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Panel holder = new Panel();
		holder.setWidth("100%");
		VerticalLayout layout = new VerticalLayout();
		email = new TextField();
		email.setWidth("100%");
		email.setPlaceholder("Enter e-mail");
		Button submit = new Button("Send password recovery");
		submit.setWidth("100%");
		submit.addClickListener(e -> sendMail());
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.setIcon(VaadinIcons.MAILBOX);
		layout.addComponents(email, submit);
		holder.setContent(layout);
		subContent.addComponent(holder);
		setContent(subContent);
		setModal(true);
		center();
	}

	private Object sendMail() {
		try {
			UserQuery user = UserConverterUtil.convertEntityToDomain(userQueryService.findByEmail(email.getValue()));
			GmailUtil.sendMail(email.getValue(), "Password recovery",
					"Your username: " + user.getUsername() + " Your password: " + user.getPassword());
			close();
			SuccessMessageUtil.showSuccessDialog("E-mail sent successfully!");
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		return this;
	}

}
