package org.fao.aoscs.client.module.term.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptHistoryCellTable;
import org.fao.aoscs.client.module.term.TermDetailTabPanel;
import org.fao.aoscs.client.module.term.TermTemplate;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
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

public class TermInformation extends TermTemplate{
	private ConceptHistoryCellTable rcTable;	
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel tbPanel = new VerticalPanel();
	
	public TermInformation(PermissionObject permissionTable, InitializeConceptData initData, TermDetailTabPanel termDetailPanel){
		super(permissionTable,initData, termDetailPanel);		
	}
	public void initLayout(){
		this.sayLoading();
		final Grid table = new Grid(3,2);
		AsyncCallback<InformationObject> callback = new AsyncCallback<InformationObject>(){
			public void onSuccess(InformationObject iObj){
				clearPanel(); // clear loading message
																
				table.setWidget(0, 0, new HTML(constants.conceptCreateDate()));
				table.setWidget(0, 1, new HTML(iObj.getCreateDate()));
				table.setWidget(1, 0, new HTML(constants.conceptUpdateDate()));
				table.setWidget(1, 1, new HTML(iObj.getUpdateDate()));
				table.setWidget(2, 0, new HTML(constants.conceptStatus()));
				table.setWidget(2, 1, new HTML(iObj.getStatus()));
				
				table.setWidth("100%");
				
				VerticalPanel spacer = new VerticalPanel();
				termRootPanel.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
				termRootPanel.setCellHorizontalAlignment(table, HasHorizontalAlignment.ALIGN_CENTER);
				termRootPanel.add(spacer);
				termRootPanel.add(tbPanel);
				termRootPanel.setCellHeight(spacer, "8px");
				termRootPanel.setCellWidth(tbPanel, "100%");
				termRootPanel.setCellHeight(tbPanel, "100%");
				termRootPanel.setCellVerticalAlignment(table, HasVerticalAlignment.ALIGN_TOP);
				termRootPanel.setCellVerticalAlignment(tbPanel, HasVerticalAlignment.ALIGN_TOP);
				
				showLoading();
				
				Service.conceptService.getConceptHistoryInitData(termObject.getUri() , MainApp.userOntology.getOntologyId() , 2 , new AsyncCallback<RecentChangesInitObject>() 
				{
					public void onSuccess(RecentChangesInitObject list) 
					{
						clearLoading();
						
						userList = list.getUsers();
						actionList = list.getActions();
															
						rcTable = new ConceptHistoryCellTable(userList, actionList, list.getSize(),termObject.getUri(),2);
						
						Widget tb = rcTable.getLayout();

						tbPanel.add(tb);						
						tbPanel.setCellWidth(tb, "100%");
						tbPanel.setCellHeight(tb, "100%");
						
						termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.history, constants.termHistory()+"&nbsp;("+list.getSize()+")");
					}
					public void onFailure(Throwable caught) {
						//ExceptionManager.showException(caught, constants.homeRCDataFail());
						GWT.log(caught.getLocalizedMessage()+"  : GETTING RECENT CHANGES DATA FAIL", null);
					}
				});
				
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.termGetTermInfoFail());
			}
		};
		Service.termService.getTermInformation(termObject.getUri(), MainApp.userOntology, callback);
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
