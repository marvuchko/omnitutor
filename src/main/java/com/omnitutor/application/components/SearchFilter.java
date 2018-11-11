package com.omnitutor.application.components;

import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.SelectionUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SearchFilter extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private PublishSubject<String> subject = PublishSubject.create();

	private RadioButtonGroup<String> singleSelect;
	
	public SearchFilter() {
		setStyle();
		initComponents();
	}

	private void setStyle() {
		setWidth("90%");
		setMargin(new MarginInfo(true, false, false, false));
	}

	private void initComponents() {
		Panel content = new Panel();
		content.setWidth("100%");
		content.setCaption("Search filters");
		content.setIcon(VaadinIcons.FILTER);
		content.setContent(createRadioButtons());
		content.addStyleNames(ValoTheme.PANEL_WELL, CssUtil.PANEL_CUSTOM_CAPTION);
		content.addStyleNames(CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_SHADOW_CONTENT);
		addComponent(content);
	}

	private Component createRadioButtons() {
		VerticalLayout holder = new VerticalLayout();
		singleSelect = new RadioButtonGroup<String>("Select search filter");
		singleSelect.setItems(SelectionUtil.NEWEST, SelectionUtil.RATING);
		singleSelect.setSelectedItem("None");
		singleSelect.addValueChangeListener(e -> subject.onNext(singleSelect.getValue()));
		holder.addComponent(singleSelect);
		return holder;
	}
	
	public Observable<String> getObservable() {
		return subject.asObservable();
	}

}
