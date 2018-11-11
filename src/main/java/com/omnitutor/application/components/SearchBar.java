package com.omnitutor.application.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SearchBar extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private TextField searchInput;
	
	public PublishSubject<String> searchSubject;
	
	public SearchBar(String placeHolder) {
		setStyle();
		initSubject();
		initComponents(placeHolder);
	}

	private void setStyle() {
		setWidth("100%");
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	}
	
	private void initSubject() {
		searchSubject = PublishSubject.create();
	}

	private void initComponents(String placeHolder) {
		CssLayout content = new CssLayout();
		searchInput = new TextField();
		searchInput.setWidth("75%");
		searchInput.setPlaceholder(placeHolder);
		searchInput.addValueChangeListener(e -> updateSearch());
		searchInput.setValueChangeMode(ValueChangeMode.LAZY);
		Button clearButton = new Button();
		clearButton.setDescription("Clears the current searching input.");
		clearButton.setIcon(VaadinIcons.CLOSE);
		clearButton.addClickListener(e -> clearSearchInput());
		content.addComponents(searchInput, clearButton);
		content.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		addComponent(content);
	}

	private Object updateSearch() {
		searchSubject.onNext(searchInput.getValue());
		return this;
	}

	private Object clearSearchInput() {
		searchInput.clear();
		searchSubject.onNext(searchInput.getValue());
		return this;
	}
	
	public Observable<String> getSearchObservable() {
		return searchSubject.asObservable();
	}

}
