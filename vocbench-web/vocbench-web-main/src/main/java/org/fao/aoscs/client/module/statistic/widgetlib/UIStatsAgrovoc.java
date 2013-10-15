/**
 * 
 */
package org.fao.aoscs.client.module.statistic.widgetlib;

import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.domain.StatsAgrovoc;
import org.fao.aoscs.domain.TermSubVocabStat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class UIStatsAgrovoc extends Composite {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static UIStatsAgrovocUiBinder uiBinder = GWT.create(UIStatsAgrovocUiBinder.class);
	private boolean isPrinterFriendly = false;
	
	@UiField FlexTable conceptSubVocabCount;
	@UiField FlexTable conceptSubVocabOccurrences;
	@UiField FlexTable termSubVocabCount;
	@UiField FlexTable termSubVocabOccurrences;


	interface UIStatsAgrovocUiBinder extends UiBinder<Widget, UIStatsAgrovoc> {
	}

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public UIStatsAgrovoc() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public UIStatsAgrovoc(boolean isPrinterFriendly) {
		this.isPrinterFriendly = isPrinterFriendly;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void loadData(StatsAgrovoc statsAgrovoc)
	{
		loadOccurrences(conceptSubVocabOccurrences, constants.statStatsAgrovocConceptSubVocabCount(), statsAgrovoc.getConceptSubVocabOccurrences(), statsAgrovoc.getConceptSubVocabCount());
		loadTermSubVocabOccurrences(termSubVocabOccurrences, constants.statStatsAgrovocTermSubVocabCount(), statsAgrovoc.getTermSubVocabOccurrences(), statsAgrovoc.getTermSubVocabCount());
	}
	
	private void loadOccurrences(FlexTable table, String titleHeader, HashMap<String, Integer> occurrencesList, int countVal)
	{
		NumberLabel<Integer> tCount = new NumberLabel<Integer>();
		tCount.setValue(countVal);
		
		table.setHTML(0, 0, titleHeader);
		table.setWidget(0, 1, tCount);
		
		table.getCellFormatter().setWidth(0, 0, "75%");
		table.getCellFormatter().setWidth(0, 1, "25%");
		
		if(isPrinterFriendly)
		{
			table.setBorderWidth(1);
			table.removeStyleName("thinBorderTable");
		}
		else
		{
			table.getCellFormatter().addStyleName(0, 0, "topbar");
			table.getCellFormatter().addStyleName(0, 0, "thinBorderTable");
		}
		
		int i=0;
		for(String uri : occurrencesList.keySet())
		{
			NumberLabel<Integer> count = new NumberLabel<Integer>();
			count.setValue(occurrencesList.get(uri));
			
			table.setWidget(i+1, 0, new HTML(uri));
			table.setWidget(i+1, 1, count);
			i++;
		}
	}
	
	private void loadTermSubVocabOccurrences(FlexTable table, String titleHeader, HashMap<String, TermSubVocabStat> termSubVocabOccurences, int countVal)
	{
		NumberLabel<Integer> tCount = new NumberLabel<Integer>();
		tCount.setValue(countVal);
		
		table.setHTML(0, 0, titleHeader);
		table.setWidget(0, 1, tCount);
		
		table.getCellFormatter().setWidth(0, 0, "75%");
		table.getCellFormatter().setWidth(0, 1, "25%");
		
		if(isPrinterFriendly)
		{
			table.setBorderWidth(1);
			table.removeStyleName("thinBorderTable");
		}
		else
		{
			table.getCellFormatter().addStyleName(0, 0, "topbar");
			table.getCellFormatter().addStyleName(0, 0, "thinBorderTable");
		}
		
		FlexCellFormatter headerFormatter = table.getFlexCellFormatter();
		
		int i=1;
		for(String name : termSubVocabOccurences.keySet())
		{
			
			headerFormatter.setColSpan(i, 0, 2);
			table.getCellFormatter().setWidth(i, 0, "100%");
			
			TermSubVocabStat termSubVocabStat = termSubVocabOccurences.get(name);
			
			FlexTable flexTable = new FlexTable();
			flexTable.setSize("100%", "100%");
			
			loadOccurrences(flexTable, termSubVocabStat.getName(), termSubVocabStat.getLangOccurrences(), termSubVocabStat.getOccurences());
			table.setWidget(i, 0, flexTable);
			DOM.setStyleAttribute(table.getCellFormatter().getElement(i, 0), "padding","0px");
			
			i++;
		}
	}
}
