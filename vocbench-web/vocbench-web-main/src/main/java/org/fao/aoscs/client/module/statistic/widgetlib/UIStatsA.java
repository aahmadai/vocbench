package org.fao.aoscs.client.module.statistic.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.domain.InitializeStatisticalData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.StatsA;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
public class UIStatsA extends Composite {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static UIStatsAUiBinder uiBinder = GWT.create(UIStatsAUiBinder.class);
	private boolean isPrinterFriendly = false;
	@UiField FlexTable termForLang;
	@UiField FlexTable statTable;
	
	
	interface UIStatsAUiBinder extends UiBinder<Widget, UIStatsA> {
	}

	public UIStatsA() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public UIStatsA(boolean isPrinterFriendly) {
		this.isPrinterFriendly = isPrinterFriendly;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void loadData(InitializeStatisticalData initData, StatsA statsA)
	{
		if(isPrinterFriendly)
		{
			statTable.setBorderWidth(1);
			termForLang.setBorderWidth(1);
			
			statTable.removeStyleName("thinBorderTable");
			termForLang.removeStyleName("thinBorderTable");
		}
		
		loadStatTable(statsA.getConceptsCount(), statsA.getTermsCount(), statsA.getTopConceptsCount(), statsA.getTermLangCount());
		loadTermForLang(initData.getLanguageList(), statsA.getTermForLang());
	}
	
	private void loadStatTable(int conceptsCountVal, int termsCountVal, int topConceptsCountVal, int termLangCountVal)
	{
		NumberLabel<Integer> conceptsCount = new NumberLabel<Integer>();
		NumberLabel<Integer> termsCount = new NumberLabel<Integer>();
		NumberLabel<Integer> topConceptsCount = new NumberLabel<Integer>();
		NumberLabel<Integer> termLangCount = new NumberLabel<Integer>();
		
		conceptsCount.setValue(conceptsCountVal);
		termsCount.setValue(termsCountVal);
		topConceptsCount.setValue(topConceptsCountVal);
		termLangCount.setValue(termLangCountVal);
		
		statTable.setHTML(0, 0, constants.statStatsATotalConcepts());
		statTable.setWidget(0, 1, conceptsCount);
		statTable.setHTML(1, 0, constants.statStatsATotalTerms());
		statTable.setWidget(1, 1, termsCount);
		statTable.setHTML(2, 0, constants.statStatsATotalTopConcepts());
		statTable.setWidget(2, 1, topConceptsCount);
		statTable.setHTML(3, 0, constants.statStatsATotalTotalLang());
		statTable.setWidget(3, 1, termLangCount);
		
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
	}
	
	private void loadTermForLang(ArrayList<LanguageCode> langList, HashMap<String, Integer> tpl)
	{
		FlexCellFormatter headerFormatter = termForLang.getFlexCellFormatter();
		termForLang.setHTML(0, 0, constants.statStatsATotalTermsEachLang());
		headerFormatter.setColSpan(0, 0, 2);
		
		termForLang.getCellFormatter().setWidth(1, 0, "75%");
		termForLang.getCellFormatter().setWidth(1, 1, "25%");
		
		
		termForLang.setWidget(1, 0, new HTML(constants.statLanguage()));
		termForLang.setWidget(1, 1, new HTML(constants.statCountTerm()));
		
		
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < termForLang.getCellCount(j); i++) {
				if(!isPrinterFriendly)
				{
					termForLang.getCellFormatter().addStyleName(j, i, "topbar");
					termForLang.getCellFormatter().addStyleName(j, i, "thinBorderTable");
				}
				termForLang.getCellFormatter().setWordWrap(j, i, true);
			}
		}
		
		for(int i =0; i<langList.size();i++)
		{
			LanguageCode lang = langList.get(i);
			termForLang.setWidget(i+2, 0, new HTML(""+lang.getLocalLanguage()+" ("+lang.getLanguageCode().toLowerCase()+")"));
			NumberLabel<Integer> countLabel = new NumberLabel<Integer>();
			Integer cntObj = tpl.get(lang.getLanguageCode().toLowerCase());
			countLabel.setValue(cntObj==null?0:cntObj);
			termForLang.setWidget(i+2, 1, countLabel);
		}
	}
}
