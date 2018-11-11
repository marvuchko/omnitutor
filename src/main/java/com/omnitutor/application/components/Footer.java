package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class Footer extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	
	public Footer() {
		setStyle();
		initComponents();
	}

	private void setStyle() {
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setSizeFull();
		addStyleNames(CssUtil.FOOTER);
	}

	private void initComponents() {
		HorizontalLayout content = new HorizontalLayout();
		Label text = new Label("<b>Copyright &copy; Marko Vučković "
				+ VaadinIcons.LINE_V.getHtml() + 
				" 2018 - present</b>", ContentMode.HTML);
		text.addStyleName(CssUtil.FOOTER_TEXT);
		content.addComponent(text);
		addComponents(content);
	}

}
