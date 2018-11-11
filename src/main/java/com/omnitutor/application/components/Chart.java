package com.omnitutor.application.components;

import java.util.List;

import com.omnitutor.application.util.CssUtil;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class Chart extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public Chart(String title, List<String> labels, List<Float> percentages){
		setStyle();
		initComponent(title, labels, percentages);
	}

	private void setStyle() {
		setMargin(false);
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	}

	private void initComponent(String title, List<String> labels, List<Float> percentages) {
		addTitle(title);
		addChartBody(labels, percentages);
	}

	private void addChartBody(List<String> labels, List<Float> percantages) {
		HorizontalLayout chartBodyHolder = new HorizontalLayout();
		chartBodyHolder.setWidth("100%");
		Component chartLabels = createChartLabels(labels, percantages);
		Component chartLines = createChartLines(percantages);
		chartBodyHolder.addComponents(chartLabels, chartLines);
		chartBodyHolder.setExpandRatio(chartLabels, 2);
		chartBodyHolder.setExpandRatio(chartLines, 3);
		addComponent(chartBodyHolder);
	}

	private Component createChartLabels(List<String> labels, List<Float> percantages) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		for(int i = 0; i < labels.size(); i++) {
			String percentageString = String.format("%.02f", percantages.get(i).floatValue());
			layout.addComponent(new Label(labels.get(i) + 
					": <b><i>" + percentageString + "%</i></b>", ContentMode.HTML));
		}
		return layout;
	}

	private Component createChartLines(List<Float> percantages) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleNames(CssUtil.CHART_PERCENTAGE, CssUtil.SOLID_BORDER);
		layout.setMargin(false);
		for(int i = 0; i < percantages.size(); i++) {
			HorizontalLayout bar = new HorizontalLayout();
			bar.addStyleNames(CssUtil.CHART_BAR_COLOR);
			String percentageString = String.format("%.02f", percantages.get(i).floatValue());
			bar.setWidth(percentageString + "%");
			bar.addComponent(new Label());
			layout.addComponent(bar);
		}
		return layout;
	}

	private void addTitle(String title) {
		Label chartTitle = new Label(title);
		chartTitle.addStyleNames(ValoTheme.LABEL_H2, ValoTheme.LABEL_BOLD);
		addComponent(chartTitle);
	}
	
}
