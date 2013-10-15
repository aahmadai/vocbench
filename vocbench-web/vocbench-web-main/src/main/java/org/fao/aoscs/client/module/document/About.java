package org.fao.aoscs.client.module.document;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.document.widgetlib.RecentChangesCellTable;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.filter.FilterRecentChanges;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class About extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel recentChangesPanel   = new VerticalPanel();
	//RecentChangesTable rcTable = new RecentChangesTable();
	RecentChangesCellTable rcTable = new RecentChangesCellTable();
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();
	private VerticalPanel mainPanel = new VerticalPanel();	
	private VerticalPanel dPanel = new VerticalPanel();
	private FilterRecentChanges rcFilter = null;

	public About(){
		initData();
	    initWidget(mainPanel);
	}
	
	public HorizontalPanel makeTitlePanel(){
		HorizontalPanel titlePanel   = new HorizontalPanel();
		titlePanel.clear();
		titlePanel.setWidth("100%");
		titlePanel.addStyleName("maintopbar");

		Image reload = new Image("images/reload-grey.gif");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reLoad();
			}
		});
		reload.setTitle(constants.homeReload());
		reload.setStyleName(Style.Link);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		hp.add(reload);
		hp.setCellVerticalAlignment(reload, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellHorizontalAlignment(reload, HasHorizontalAlignment.ALIGN_LEFT);

		HorizontalPanel SHPanel = new HorizontalPanel();
		Image filterImg = new Image("images/filter-grey.gif");
		Label applyFilter = new Label(constants.valFilterApply());
		applyFilter.setStyleName(Style.Link);
		applyFilter.addStyleName(Style.colorBlack);
		applyFilter.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(rcFilter == null)
				{
					rcFilter = new FilterRecentChanges(About.this);
				}
				rcFilter.show();
			}
		});
		SHPanel.setSpacing(5);
		SHPanel.add(filterImg);
		SHPanel.add(applyFilter);

		HTML titleName = new HTML(constants.homeRecentChanges());
		titleName.setWordWrap(false);
		titleName.addStyleName("maintopbartitle");

		titlePanel.add(titleName);
		titlePanel.setCellWidth(titleName, "50px");
		titlePanel.add(hp);
		titlePanel.add(SHPanel);
		titlePanel.setCellVerticalAlignment(titleName, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(titleName, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(SHPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(SHPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		return titlePanel;
	}

	
	public void initData()
	{
		initLoadingTable();
		
		Service.validationService.getRecentChangesInitData(MainApp.vFilter, new AsyncCallback<RecentChangesInitObject>() {
			public void onSuccess(RecentChangesInitObject list) {
				userList = list.getUsers();
				actionList = list.getActions();
				
				VerticalPanel bodyPanel = new VerticalPanel();
				recentChangesPanel.setSize("100%", "100%");
				bodyPanel.add(recentChangesPanel);	
				bodyPanel.setSpacing(0);
				bodyPanel.setCellHeight(recentChangesPanel, "100%");
				bodyPanel.setCellWidth(recentChangesPanel, "100%");
				bodyPanel.setSize("100%", "100%");
				bodyPanel.setCellVerticalAlignment(recentChangesPanel, HasVerticalAlignment.ALIGN_TOP);
				bodyPanel.setCellHorizontalAlignment(recentChangesPanel, HasHorizontalAlignment.ALIGN_CENTER);
				
				VerticalPanel aboutPanel = new VerticalPanel();
				
				aboutPanel.add(makeTitlePanel());
				aboutPanel.add(bodyPanel);
				aboutPanel.setCellWidth(bodyPanel, "100%");
				aboutPanel.setCellHeight(bodyPanel, "100%");
				
				dPanel.add(aboutPanel);
				dPanel.setStyleName("borderbar");
				populateTableModel(list.getSize());
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.homeRCDataFail());
			}
		});				
	}
	

	public void reLoad()
	{
		initLoadingTable();
		Service.validationService.getRecentChangesSize(MainApp.vFilter, new AsyncCallback<Integer>() {
			public void onSuccess(Integer size) {
				
				populateTableModel(size);
			}

			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.homeRCDataFail());
			}
		});
	}
	
	public void initLoadingTable(){
		LoadingDialog loadingDialog = new LoadingDialog();
		mainPanel.clear();
		mainPanel.setSize("100%", "100%");
		mainPanel.add(loadingDialog);
		mainPanel.setCellHorizontalAlignment(loadingDialog, HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(loadingDialog, HasVerticalAlignment.ALIGN_MIDDLE);
		
	}
	
	public void filterByLanguage()
	{
		reLoad();
		// TODO (Done) with reload mechanisam
		/*for(int i=0;i<rcTable.getDataTable().getRowCount();i++){
			RecentChanges rc = (RecentChanges)rcTable.getPagingScrollTable().getRowValue(i);
			for(int j=0;j<3;j++)
			{
				Widget w = RecentChangesTable.getTablePanel(j, Style.Link, rc.getModifiedObject());
				w.addStyleName("gwt-NoBorder");
				rcTable.getDataTable().setWidget(i, j, w);
			}
		}*/

	}
	
	public void populateTableModel(int size)
	{
		mainPanel.clear();
		mainPanel.setSize("100%", "100%");
		mainPanel.add(dPanel);
		mainPanel.setCellHeight(dPanel, "100%");
		mainPanel.setCellWidth(dPanel, "100%");
		mainPanel.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Widget w = rcTable.getLayout(MainApp.vFilter, userList, actionList, size);
	    recentChangesPanel.clear();
	    recentChangesPanel.add(w);
	    recentChangesPanel.setCellVerticalAlignment(w, HasVerticalAlignment.ALIGN_TOP);
	    recentChangesPanel.setCellWidth(w, "100%");
	    recentChangesPanel.setCellHeight(w, "100%");
	    
	}
	
	public ArrayList<Users> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<Users> userList) {
		this.userList = userList;
	}

	public ArrayList<OwlAction> getActionList() {
		return actionList;
	}

	public void setActionList(ArrayList<OwlAction> actionList) {
		this.actionList = actionList;
	}
	
}


	
