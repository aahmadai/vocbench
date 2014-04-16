package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

public class ConceptSchemes extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public ConceptSchemes(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}

	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getSchemeObject()!=null)
		{
			initData(cDetailObj.getSchemeObject());
		}
		else
		{
			AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
				public void onSuccess(HashMap<String, String> schemeObject) {
					cDetailObj.setSchemeObject(schemeObject);
					initData(schemeObject);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptSchemeGetSchemeFail());
				}
			};
			Window.alert(conceptObject.getUri());
			Service.conceptService.getConceptSchemeValue(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
	}
	
	private void initData(HashMap<String, String> sObj)
	{
		clearPanel();
		if(sObj!=null && sObj.size()>0){
			int cnt = sObj.size();
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(InfoTab.scheme, Convert.replaceSpace(cnt>1? constants.conceptSchemes():constants.conceptScheme())+"&nbsp;("+(cnt)+")");
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(getTable(sObj),"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}
		else
		{
			Label sayNo = new Label(constants.conceptNoSchemes());
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}
	
	private FlexTable getTable(HashMap<String, String> sObj)
	{
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.conceptSchemeLabel()));
		table.setWidget(0, 1, new HTML(constants.conceptSchemeUri()));
		table.getColumnFormatter().setWidth(1, "80%");
		int i=1;
		for(String label: sObj.keySet()){
			String uri = sObj.get(label);
			
			HTML htmlLabel = new HTML(label);
			HTML htmlUri = new HTML(uri);
			
			if(uri.equals(MainApp.schemeUri))
			{
				htmlLabel.addStyleName(Style.fontWeightBold);
				htmlUri.addStyleName(Style.fontWeightBold);
			}
			
			DOM.setStyleAttribute(htmlLabel.getElement(), "lineHeight", "20px");
			DOM.setStyleAttribute(htmlUri.getElement(), "lineHeight", "20px");
			
			table.setWidget(i, 0, htmlLabel);
			table.setWidget(i, 1, htmlUri);
			
			i++;
		}
		return table;
	}

	
}
