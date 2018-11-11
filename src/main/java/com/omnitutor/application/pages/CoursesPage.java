package com.omnitutor.application.pages;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.omnitutor.application.components.CourseCard;
import com.omnitutor.application.components.Footer;
import com.omnitutor.application.components.Navbar;
import com.omnitutor.application.components.NavbarFactory;
import com.omnitutor.application.components.NavbarFactory.NavbarType;
import com.omnitutor.application.components.SearchBar;
import com.omnitutor.application.components.SearchFilter;
import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.domain.query.UserQuery;
import com.omnitutor.application.services.query.CourseQueryService;
import com.omnitutor.application.util.CourseConverterUtil;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.omnitutor.application.util.NavigatorUtil;
import com.omnitutor.application.util.RoleUtil;
import com.omnitutor.application.util.SelectionUtil;
import com.omnitutor.application.util.SessionService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import rx.Observable;

@SpringComponent
@UIScope
public class CoursesPage extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	@Autowired
	NavigatorUtil navigator;

	@Autowired
	SessionService sessionService;

	@Autowired
	CourseQueryService courseQueryService;

	private CssLayout courseSearchContent;

	private String searchMode;

	@PostConstruct
	void init() {
		setStyle();
		initProps();
		initComponents();
	}

	private void initProps() {
		this.searchMode = SelectionUtil.NEWEST;
	}

	private void setStyle() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
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
		HorizontalLayout content = new HorizontalLayout();
		content.setSizeFull();
		VerticalLayout search = createSidemenu();
		content.addComponent(search);
		content.setExpandRatio(search, 1);
		Panel coursesSearchResaults = createCourseSearchResault();
		content.addComponent(coursesSearchResaults);
		content.setExpandRatio(coursesSearchResaults, 3);
		addComponent(content);
		setComponentAlignment(content, Alignment.MIDDLE_CENTER);
		setExpandRatio(content, 10);
	}

	private VerticalLayout createSidemenu() {
		VerticalLayout holder = new VerticalLayout();
		holder.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		CssLayout courseSearchHolder = new CssLayout();
		courseSearchHolder.setSizeFull();
		Panel searchBarPanel = new Panel("Search");
		searchBarPanel.setWidth("90%");
		searchBarPanel.addStyleName(CssUtil.PANEL_CUSTOM_CAPTION);
		searchBarPanel.addStyleNames(CssUtil.PANEL_SHADOW_CAPTION, CssUtil.PANEL_SHADOW_CONTENT);
		searchBarPanel.setIcon(VaadinIcons.SEARCH);
		searchBarPanel.addStyleName(ValoTheme.PANEL_WELL);
		SearchBar searchBar = new SearchBar("Search courses");
		subscribeToSearch(searchBar.getSearchObservable());
		searchBarPanel.setContent(searchBar);
		SearchFilter filter = new SearchFilter();
		subscribeToFilter(filter.getObservable());
		courseSearchHolder.addComponents(searchBarPanel, filter);
		holder.addComponent(courseSearchHolder);
		return holder;
	}

	private void subscribeToFilter(Observable<String> observable) {
		observable.subscribe(mode -> {
			searchMode = mode;
		});
	}

	private void subscribeToSearch(Observable<String> searchObservable) {
		searchObservable.subscribe(search -> {
			courseSearchContent.removeAllComponents();
			if (searchMode.equals(SelectionUtil.NEWEST))
				courseQueryService.findAllByTitleContainingOrderByIdDesc(search, PageRequest.of(0, 20)).stream()
						.forEach(course -> addCourseCard(CourseConverterUtil.convertEntityToDomain(course),
								courseSearchContent));
			if (searchMode.equals(SelectionUtil.RATING))
				courseQueryService.findAllByTitleContainingOrderByRatingDesc(search, PageRequest.of(0, 20)).stream()
						.forEach(course -> addCourseCard(CourseConverterUtil.convertEntityToDomain(course),
								courseSearchContent));
			else
				courseQueryService.findAllByTitleContaining(search, PageRequest.of(0, 20)).stream()
						.forEach(course -> addCourseCard(CourseConverterUtil.convertEntityToDomain(course),
								courseSearchContent));
		});
	}

	private Panel createCourseSearchResault() {
		Panel courseSearchContentHolder = new Panel();
		courseSearchContentHolder.setSizeFull();
		courseSearchContentHolder.addStyleNames(ValoTheme.PANEL_BORDERLESS, ValoTheme.PANEL_SCROLL_INDICATOR);
		courseSearchContent = new CssLayout();
		courseSearchContent.setWidth("100%");
		courseSearchContent.addStyleName(CssUtil.TEXT_CENTER);
		courseSearchContentHolder.setContent(courseSearchContent);
		return courseSearchContentHolder;
	}

	private Object addCourseCard(CourseQuery course, CssLayout courseSearchContent) {
		courseSearchContent.addComponent(new CourseCard(course, navigator));
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
		try {
			removeAllComponents();
			initComponents();
			courseSearchContent.removeAllComponents();
			courseQueryService.findAllByTitleContaining("", PageRequest.of(0, 20)).getContent().stream().forEach(
					course -> addCourseCard(CourseConverterUtil.convertEntityToDomain(course), courseSearchContent));
		} catch (Exception e) {
			ErrorMessageUtil.showErrorDialog(e.getMessage());
		}
	}

}
