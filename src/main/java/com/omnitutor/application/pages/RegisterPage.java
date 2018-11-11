package com.omnitutor.application.pages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.domain.command.UserCommand;
import com.omnitutor.application.services.command.RegisterCommandService;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.DropBoxUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.FileUploadUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.ResourceUtil;
import com.omnitutor.application.util.SuccessMessageUtil;
import com.omnitutor.application.util.UserConverterUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class RegisterPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;
	
	@Autowired
	FileUploadUtil fileService;
	
	@Autowired
	RegisterCommandService registerService;

	private TextField firstName;
	
	private TextField lastName;
	
	private TextField userName;
	
	private TextField email;
	
	private PasswordField password;
	
	private Image image;

	@PostConstruct
	void init() {
		setStyle();
		initComponents();
	}

	private void setStyle() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
		addStyleName(CssUtil.APP_BACKGROUND);
	}

	private void initComponents() {
		addNavbar();
		addRegisterForm();
		addFooter();
	}

	private void addNavbar() {
		Navbar navbar = NavbarFactory.createNavbar(NavbarType.DEFAULT, navigator, null);
		addComponent(navbar);
		setComponentAlignment(navbar, Alignment.TOP_CENTER);
		setExpandRatio(navbar, 1);
	}

	private void addRegisterForm() {
		Panel panel = createPanel();
		addComponent(panel);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		setExpandRatio(panel, 10);
	}

	private Panel createPanel() {
		Panel panel = new Panel("Register");
		panel.setIcon(VaadinIcons.USERS);
		panel.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_CUSTOM_CAPTION);
		panel.addStyleNames(CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_SHADOW_CONTENT);
		panel.setSizeUndefined();
		panel.setContent(createPanelContent());
		return panel;
	}

	private Component createPanelContent() {
		VerticalLayout content = new VerticalLayout();
		content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		content.setSizeFull();
		content.setMargin(false);
		HorizontalLayout inputContent = new HorizontalLayout();
		inputContent.addStyleName(CssUtil.SOLID_BORDER_BOTTOM);
		inputContent.addComponent(createUserInputForm());
		inputContent.addComponent(createUserImageForm());
		inputContent.setWidth("100%");
		inputContent.setHeight("80%");
		content.addComponent(inputContent);
		content.addComponent(createSubmitButton());
		return content;
	}

	private Component createUserInputForm() {
		VerticalLayout inputForm = new VerticalLayout();
		inputForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		inputForm.setMargin(new MarginInfo(false, true, true, true));
		VerticalLayout labelHolder = new VerticalLayout();
		labelHolder.setMargin(false);
		inputForm.addComponent(labelHolder);
		firstName = new TextField();
		firstName.setPlaceholder("First name");
		lastName = new TextField();
		lastName.setPlaceholder("Last name");
		userName = new TextField();
		userName.setPlaceholder("Username");
		email = new TextField();
		email.setPlaceholder("Email");
		password = new PasswordField();
		password.setPlaceholder("Password");
		inputForm.addComponents(firstName, lastName, userName, password, email);
		return inputForm;
	}

	private Component createUserImageForm() {
		VerticalLayout imageHolder = new VerticalLayout();
		imageHolder.setMargin(new MarginInfo(false, true, true, true));
		imageHolder.addStyleName(CssUtil.SOLID_BORDER_LEFT);
		imageHolder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		imageHolder.setSizeFull();
		VerticalLayout labelHolder = new VerticalLayout();
		labelHolder.setMargin(false);
		imageHolder.addComponent(labelHolder);
		imageHolder.addComponent(createProfileImage());
		imageHolder.addComponent(createUploadComponent());
		return imageHolder;
	}

	private Component createProfileImage() {
		image = new Image(null, new ThemeResource(ResourceUtil.PROFILE_IMAGE_DEFAULT));
		image.addStyleNames(CssUtil.PROFILE_IMAGE, CssUtil.IMAGE_FIT);
		image.setWidth("150px");
		image.setHeight("150px");
		return image;
	}

	private Component createUploadComponent() {
		HorizontalLayout uploadHolder = new HorizontalLayout();
		uploadHolder.setMargin(new MarginInfo(true, false, false, false));
		Upload upload = new Upload(null, fileService);
		upload.setButtonCaption("Upload profile image");
		upload.addSucceededListener(e -> setImage(upload));
		uploadHolder.addComponent(upload);
		return uploadHolder;
	}

	private Object setImage(Upload upload) {
		FileUploadUtil reciever = (FileUploadUtil) upload.getReceiver();
		ByteArrayOutputStream data = reciever.getData();
		if (!reciever.getFileType().equals(FileUploadUtil.IMAGE)) {
			ErrorMessageUtil.showErrorDialog("Uploaded file is not an image.");
			return this;
		}
		StreamSource source = new StreamSource() {
			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				return new ByteArrayInputStream(data.toByteArray());
			}
		};
		image.setSource(new StreamResource(source, new Date().toString()));
		return this;
	}

	private Component createSubmitButton() {
		HorizontalLayout submitButtonHolder = new HorizontalLayout();
		submitButtonHolder.addStyleName(CssUtil.MARGIN_BOT);
		submitButtonHolder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Button submit = new Button("Register");
		submit.setIcon(VaadinIcons.USERS);
		submit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		submit.addClickListener(e -> registerUser());
		submitButtonHolder.addComponent(submit);
		return submitButtonHolder;
	}

	private Object registerUser() {
		try {
			validateInputs();
			SharedLinkMetadata link = DropBoxUtil.uploadFile("/profile", fileService.getExtension(),
					fileService.getData());
			UserCommand user = UserCommand.builder().email(email.getValue()).firstName(firstName.getValue())
					.lastName(lastName.getValue()).password(password.getValue()).username(userName.getValue())
					.imageURL(link.getUrl().split("[?]")[0] + "?raw=1").build();
			registerService.createNewUser(UserConverterUtil.convertDomainToEntity(user));
			SuccessMessageUtil.showSuccessDialog("You have successfuly created account");
			clearInputs();
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
		return this;
	}

	private void clearInputs() {
		firstName.setValue("");
		lastName.setValue("");
		email.setValue("");
		userName.setValue("");
		password.setValue("");
		image.setSource(new ThemeResource(ResourceUtil.PROFILE_IMAGE_DEFAULT));
	}

	private void validateInputs() throws Exception {
		if (firstName.getValue().isEmpty())
			throw new Exception("First name can't be blank.");
		if (lastName.getValue().isEmpty())
			throw new Exception("Last name can't be blank.");
		if (userName.getValue().isEmpty())
			throw new Exception("Username can't be blank");
		if (password.getValue().isEmpty())
			throw new Exception("Password can't be blank");
		if (email.getValue().isEmpty())
			throw new Exception("Email cant be blank");
		if (email.getValue().indexOf("@") < 0)
			throw new Exception("Email must contain \"@\" symbol.");
		if (!(image.getSource() instanceof StreamResource))
			throw new Exception("You haven't uploaded profile image.");
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
