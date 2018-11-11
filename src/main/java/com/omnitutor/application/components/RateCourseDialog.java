package com.omnitutor.application.components;

import com.omnitutor.application.domain.query.CourseQuery;
import com.omnitutor.application.server.push.BroadcastMessageUtil;
import com.omnitutor.application.server.push.Broadcaster;
import com.omnitutor.application.services.command.CourseCommandService;
import com.omnitutor.application.util.CssUtil;
import com.omnitutor.application.util.ErrorMessageUtil;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class RateCourseDialog extends Dialog {

	private static final long serialVersionUID = 1L;
	
	private HorizontalLayout[] rateButtons;
	
	private int selectedButton;

	private CourseCommandService courseCommandService;
	
	public RateCourseDialog(CourseQuery course, CourseCommandService courseCommandService) {
		super(" Rate course dialog", VaadinIcons.STAR, "Rate course: " + course.getTitle());
		setStyle();
		initProps(courseCommandService);
		initComponents(course);
	}

	private void initProps(CourseCommandService courseCommandService) {
		rateButtons = new HorizontalLayout[10];
		this.courseCommandService = courseCommandService;
	}

	private void setStyle() {
		setIcon(VaadinIcons.STAR);
		setResizable(false);	
	}

	private void initComponents(CourseQuery course) {
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Component rateButtons = createRateButtons();
		Component submitButton = createSubmitButton(course);
		panelContent.addComponents(rateButtons, submitButton);
		holder.setContent(panelContent);
	}

	private Component createRateButtons() {
		HorizontalLayout layout = new HorizontalLayout();
		for(int i = 0; i < rateButtons.length; i++) {
			rateButtons[i] = new HorizontalLayout();
			rateButtons[i].setMargin(true);
			Component star = createEmptyStar();
			rateButtons[i].addComponent(star);
			rateButtons[i].addStyleName(CssUtil.CURSOR_POINTER);
			rateButtons[i].addLayoutClickListener(e -> rateCourse(e));
			layout.addComponent(rateButtons[i]);
		}
		layout.addStyleName(CssUtil.DOUBLE_BORDER_BOTTOM);
		return layout;
	}

	private Object rateCourse(LayoutClickEvent e) {
		for(int i = 0; i < rateButtons.length; i++)
			if(e.getSource() == rateButtons[i]) selectedButton = i;
		for(int i = 0; i < rateButtons.length; i++) {
			rateButtons[i].removeAllComponents();
			rateButtons[i].addComponent(createEmptyStar());
		}
		for(int i = 0; i <= selectedButton; i++) {
			rateButtons[i].removeAllComponents();
			rateButtons[i].addComponent(createFullStarComponent());
		}
		return this;
	}

	private Component createFullStarComponent() {
		Label emptyStar = new Label(VaadinIcons.STAR.getHtml(), ContentMode.HTML);
		emptyStar.addStyleNames(CssUtil.TEXT_LARGE, CssUtil.TEXT_YELLOW);
		return emptyStar;
	}

	private Component createEmptyStar() {
		Label emptyStar = new Label(VaadinIcons.STAR_O.getHtml(), ContentMode.HTML);
		emptyStar.addStyleNames(CssUtil.TEXT_LARGE, CssUtil.TEXT_YELLOW);
		return emptyStar;
	}

	private Component createSubmitButton(CourseQuery course) {
		Button submit = new Button("Submit rating");
		submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submit.setIcon(VaadinIcons.DATABASE);
		Window self = this;
		submit.addClickListener(e -> {
			try {
				courseCommandService.rateCourse(course.getId(), selectedButton);
				self.close();
				Broadcaster.broadcast(BroadcastMessageUtil.COURSE_MODIFIED);
			} catch (Exception ex) {
				ErrorMessageUtil.showErrorDialog(ex.getMessage());
			}
		});
		return submit;
	}

}
