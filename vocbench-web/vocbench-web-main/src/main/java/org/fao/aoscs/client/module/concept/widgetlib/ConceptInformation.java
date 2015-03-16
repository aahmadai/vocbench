package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptInformation extends ConceptTemplate{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	ConceptHistoryCellTable rcTable;
	private VerticalPanel tbPanel = new VerticalPanel();
	
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();


	public ConceptInformation(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}

	public void initLayout(){
		this.sayLoading();
		conceptRootPanel.setSpacing(3);
		functionPanel.setVisible(false);
		if(cDetailObj!=null && cDetailObj.getInformationObject()!=null)
		{
			initData(cDetailObj.getInformationObject());
		}
		else
		{
			Service.conceptService.getConceptInformation(conceptObject.getUri(), MainApp.userOntology,  new AsyncCallback<InformationObject>(){
				public void onSuccess(InformationObject iObj){
					cDetailObj.setInformationObject(iObj);
					initData(iObj);
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptMakeConceptInfoPanelFail());
				}
			});
		}
		
	}

	public void initData(InformationObject iObj)
	{
		clearPanel(); // clear loading message
		
		final Grid table = new Grid(3,2);
		table.setSize("100%", "100%");
		table.setWidget(0, 0, new HTML(constants.conceptCreateDate()));
		table.setWidget(0, 1, new HTML(iObj.getCreateDate()));
		table.setWidget(1, 0, new HTML(constants.conceptUpdateDate()));
		table.setWidget(1, 1, new HTML(iObj.getUpdateDate()));
		table.setWidget(2, 0, new HTML(constants.conceptStatus()));
		table.setWidget(2, 1, new HTML(iObj.getStatus()));
		
		VerticalPanel spacer = new VerticalPanel();
		
		conceptRootPanel.setSize("100%", "100%");
		conceptRootPanel.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		conceptRootPanel.add(spacer);
		conceptRootPanel.add(tbPanel);
		conceptRootPanel.setCellHorizontalAlignment(table, HasHorizontalAlignment.ALIGN_CENTER);
		conceptRootPanel.setCellHeight(spacer, "5px");
		conceptRootPanel.setCellHeight(tbPanel, "100%");						
		conceptRootPanel.setCellWidth(tbPanel, "100%");
		
		showLoading();
		
		initTable(iObj.getRecentChangesInitObject());
		
	
		/*Service.conceptService.getConceptHistoryInitData(conceptObject.getUri() , MainApp.userOntology.getOntologyId() , 1 , new AsyncCallback<RecentChangesInitObject>() 
		{
			public void onSuccess(RecentChangesInitObject result) 
			{
				initTable(result);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.homeRCDataFail());
			}
		});*/
	}
	
	public void initTable(RecentChangesInitObject list)
	{
		clearLoading();
		
		userList = list.getUsers();
		actionList = list.getActions();
		
		rcTable = new ConceptHistoryCellTable(userList, actionList, list.getSize(), conceptObject.getUri(), 1);						
		
		Widget tb = rcTable.getLayout();
		
		tbPanel.add(tb);						
		tbPanel.setCellWidth(tb, "100%");
		tbPanel.setCellHeight(tb, "100%");
		
		if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.HISTORY.getTabIndex(), Convert.replaceSpace(constants.conceptHistory())+"&nbsp;("+(list.getSize())+")");
		if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.HISTORY.getTabIndex(), Convert.replaceSpace(constants.conceptHistory())+"&nbsp;("+(list.getSize())+")");
	
	}
	
	public void showLoading(){
		clearLoading();
		LoadingDialog sayLoading = new LoadingDialog();
		tbPanel.add(sayLoading);
		tbPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		tbPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		tbPanel.setCellHeight(sayLoading, "100%");
		tbPanel.setCellWidth(sayLoading, "100%");
	}
	
	public void clearLoading()
	{
		tbPanel.clear();
		tbPanel.setSize("100%","100%");
	}
}
