/**
 * 
 */
package org.fao.aoscs.client.module.statistic.widgetlib;

import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.domain.StatsC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class UIStatsC extends Composite {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static UIStatsCUiBinder uiBinder = GWT.create(UIStatsCUiBinder.class);
	private boolean isPrinterFriendly = false;

	@UiField FlexTable conceptRelationCount;
	@UiField FlexTable conceptRelationOccurrences;
	@UiField FlexTable termRelationCount;
	@UiField FlexTable termRelationOccurrences;
	@UiField FlexTable conceptAttributesCount;
	@UiField FlexTable conceptAttributesOccurrences;
	@UiField FlexTable termAttributesCount;
	@UiField FlexTable termAttributesOccurrences; 
	
	
	interface UIStatsCUiBinder extends UiBinder<Widget, UIStatsC> {
	}

	public UIStatsC() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public UIStatsC(boolean isPrinterFriendly) {
		this.isPrinterFriendly = isPrinterFriendly;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void loadData(StatsC statsC)
	{
		loadOccurrences(conceptRelationOccurrences, constants.statStatsCConceptRelationCount(), statsC.getConceptRelationOccurrences(), statsC.getConceptRelationCount());
		loadOccurrences(termRelationOccurrences, constants.statStatsCTermRelationCount(), statsC.getTermRelationOccurrences(), statsC.getTermRelationCount());
		loadOccurrences(conceptAttributesOccurrences, constants.statStatsCConceptAttributesCount(), statsC.getConceptAttributesOccurrences(), statsC.getConceptAttributesCount());
		loadOccurrences(termAttributesOccurrences, constants.statStatsCTermAttributesCount(), statsC.getTermAttributesOccurrences(), statsC.getTermAttributesCount());
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
	
}
