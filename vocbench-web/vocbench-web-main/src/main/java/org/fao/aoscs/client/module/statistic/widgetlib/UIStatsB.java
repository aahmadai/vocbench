/**
 * 
 */
package org.fao.aoscs.client.module.statistic.widgetlib;

import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.domain.StatsB;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class UIStatsB extends Composite {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static UIStatsBUiBinder uiBinder = GWT.create(UIStatsBUiBinder.class);
	private boolean isPrinterFriendly = false;
	@UiField FlexTable topConceptsDepth;
	@UiField FlexTable firstLevelConceptsNumber;
	@UiField FlexTable allLevelConceptsNumber;
	@UiField FlexTable statTable;

	interface UIStatsBUiBinder extends UiBinder<Widget, UIStatsB> {
	}

	public UIStatsB() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public UIStatsB(boolean isPrinterFriendly) {
		this.isPrinterFriendly = isPrinterFriendly;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void loadData(StatsB statsB)
	{
		if(isPrinterFriendly)
		{
			statTable.setBorderWidth(1);
			topConceptsDepth.setBorderWidth(1);
			firstLevelConceptsNumber.setBorderWidth(1);
			allLevelConceptsNumber.setBorderWidth(1);
			
			statTable.removeStyleName("thinBorderTable");
			topConceptsDepth.removeStyleName("thinBorderTable");
			firstLevelConceptsNumber.removeStyleName("thinBorderTable");
			allLevelConceptsNumber.removeStyleName("thinBorderTable");
		}
		
		NumberLabel<Float> lblAverageHierarchyDepth = new NumberLabel<Float>();
		NumberLabel<Integer> lblMinHierarchyDepth = new NumberLabel<Integer>();
		NumberLabel<Integer> lblMaxHierarchyDepth = new NumberLabel<Integer>();
		NumberLabel<Integer> lblConceptsWithMultipleParentage = new NumberLabel<Integer>();
		NumberLabel<Integer> lblBottomLevelConcepts = new NumberLabel<Integer>();
		
		lblAverageHierarchyDepth.setValue(statsB.getAverageHierarchyDepth());
		lblMinHierarchyDepth.setValue(statsB.getMinHierarchyDepth());
		lblMaxHierarchyDepth.setValue(statsB.getMaxHierarchyDepth());
		lblConceptsWithMultipleParentage.setValue(statsB.getConceptsWithMultipleParentage());
		lblBottomLevelConcepts.setValue(statsB.getBottomLevelConcepts());
		
		statTable.setHTML(0, 0, constants.statStatsBAverageHierarchyDepth());
		statTable.setWidget(0, 1, lblAverageHierarchyDepth);
		statTable.setHTML(1, 0, constants.statStatsBMinHierarchyDepth());
		statTable.setWidget(1, 1, lblMinHierarchyDepth);
		statTable.setHTML(2, 0, constants.statStatsBMaxHierarchyDepth());
		statTable.setWidget(2, 1, lblMaxHierarchyDepth);
		statTable.setHTML(3, 0, constants.statStatsBConceptsWithMultipleParentage());
		statTable.setWidget(3, 1, lblConceptsWithMultipleParentage);
		statTable.setHTML(4, 0, constants.statStatsBBottomLevelConcepts());
		statTable.setWidget(4, 1, lblBottomLevelConcepts);
		
		statTable.getCellFormatter().setWidth(0, 0, "75%");
		statTable.getCellFormatter().setWidth(0, 1, "25%");
		
		for (int i = 0; i < statTable.getRowCount(); i++) {
			if(!isPrinterFriendly)
			{
				statTable.getCellFormatter().addStyleName(i, 0, "topbar");
				statTable.getCellFormatter().addStyleName(i, 0, "thinBorderTable");
			}
			statTable.getCellFormatter().setWordWrap(i, 0, true);
		}
		
		loadTable(topConceptsDepth, statsB.getTopConceptsDepth(), constants.statStatsBTopConceptsDepth(), constants.statTopConcept(), constants.statDepth());
		loadTable(firstLevelConceptsNumber, statsB.getFirstLevelConceptsNumber(), constants.statStatsBFirstLevelConceptsNumber(), constants.statTopConcept(), constants.statCountConcept());
		loadTable(allLevelConceptsNumber, statsB.getAllLevelConceptsNumber(), constants.statStatsBAllLevelConceptsNumber(), constants.statTopConcept(), constants.statCountConcept());
		
	}
	
	public void loadTable(FlexTable table, HashMap<String, Integer> list, String title, String colname1, String colname2)
	{
		FlexCellFormatter headerFormatter = table.getFlexCellFormatter();
		table.setHTML(0, 0, title);
		headerFormatter.setColSpan(0, 0, 2);
		
		table.setHTML(1, 0, colname1);
		table.setHTML(1, 1, colname2);
		
		table.getCellFormatter().setWidth(1, 0, "75%");
		table.getCellFormatter().setWidth(1, 1, "25%");
		
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < table.getCellCount(j); i++) {
				if(!isPrinterFriendly)
				{
					table.getCellFormatter().addStyleName(j, i, "topbar");
					table.getCellFormatter().addStyleName(j, i, "thinBorderTable");
				}
				table.getCellFormatter().setWordWrap(j, i, true);
			}
		}
		
		int i=2;
		for(Object lang : list.keySet())
		{
			NumberLabel<Integer> tCount = new NumberLabel<Integer>();
			tCount.setValue(list.get(lang));
			table.setHTML(i, 0, lang.toString());
			table.setWidget(i, 1, tCount);
			i++;
		}
	}


}
