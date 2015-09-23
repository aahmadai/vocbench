package org.fao.aoscs.client.module.system;

import java.util.ArrayList;

import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManagePendingRequests extends DialogBoxAOS implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);

	
	private VerticalPanel panel = new VerticalPanel();
	private Button cancel = new Button(constants.buttonClose());
	private int tableWidth = 800;
	
	private FlexTable languageDataTable = new FlexTable();
	private VerticalPanel langaugePanel = new VerticalPanel();
	private VerticalPanel languageDataPanel = new VerticalPanel();
	
	private FlexTable ontologyDataTable = new FlexTable();
	private VerticalPanel ontologyPanel = new VerticalPanel();
	private VerticalPanel ontologyDataPanel = new VerticalPanel();
	
	
	public ManagePendingRequests(){
		this.setText(constants.userManagePendingRequest());
		cancel.addClickHandler(this);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setWidth("100%");
		hp.setStyleName("bottombar");
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(cancel);
		
		hp.add(buttonPanel);
		hp.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		panel.setSize("800px", "400px");
		panel.setSpacing(0);
		panel.add(makeFilterPanel());
		panel.add(hp);
		panel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_RIGHT);
		setWidget(panel);
		
		loadLanguage();
		loadOntology();
	}
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender.equals(cancel)){
			this.hide();
		}
	}
	
	public VerticalPanel makeFilterPanel()
	{
		VerticalPanel subpanel = new VerticalPanel();
		subpanel.setSize("100%", "100%");
		subpanel.setSpacing(10);
		subpanel.add(getOntologyTableWidget());
		//subpanel.add(getLanguageTableWidget());
		return subpanel;
	}
	
	private void loadLanguage()
	{
		tableLoading(langaugePanel);
		final AsyncCallback<ArrayList<String[]>> callback = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> nsMapping) {
				langaugePanel.clear();
				langaugePanel.add(languageDataPanel);
				languageDataTable.removeAllRows();
				int i=0;
				
				for(String[] prefix : nsMapping)
				{
					final ArrayList<String> languageCode = new ArrayList<String>();
					languageCode.add(prefix[0]);
					final String item = prefix[1];
					final String userId = prefix[2];
					final String username = prefix[3];
					final String fname = prefix[4];
					final String lname = prefix[5];
					final String email = prefix[6];
					
					languageDataTable.setWidget(i, 0 , new HTML(username));
					languageDataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					languageDataTable.setWidget(i, 1 , new HTML(item));
					languageDataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);
					
					Button btn_Add = new Button(constants.buttonAdd());
					btn_Add.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							AsyncCallback<Void> callback = new AsyncCallback<Void>(){
								public void onSuccess(Void result) {
									mailAlert(item, constants.userLang(), username, fname, lname, email);
									loadLanguage();
								}
								public void onFailure(Throwable caught) {
									ExceptionManager.showException(caught, constants.userAddUserLanguageFail());
								}
							};
							Service.systemService.addLanguagesToUser(userId, languageCode, callback);
						}
					});
					
					languageDataTable.setWidget(i, 2 , btn_Add);
					languageDataTable.getCellFormatter().setHorizontalAlignment(i , 2 , HasHorizontalAlignment.ALIGN_RIGHT);
					i++;
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.userListLangFail());
			}
		};
		Service.userPreferenceService.getPendingLanguage(callback);
		
	}
	
	private Widget getLanguageTableWidget()
	{
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		headerTable.setText(0, 0, constants.userUsers());		
		headerTable.setText(0, 1, constants.userLang());	
		headerTable.setText(0, 2, "");	
		headerTable.addStyleName("topbar");
		headerTable.setHeight("25px");
		
		headerTable.getCellFormatter().setWidth(0, 0, ((tableWidth-60)/2)+"px");
		headerTable.getCellFormatter().setWidth(0, 1, ((tableWidth-60)/2)+"px");
		headerTable.getCellFormatter().setWidth(0, 2, "60px");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_RIGHT);
		
		languageDataTable.setCellPadding(1);
		languageDataTable.setCellSpacing(1);
		
		ScrollPanel sc = new ScrollPanel();
		sc.setWidth(tableWidth+"px");
		sc.setHeight((MainApp.getBodyPanelHeight()-425)+"px");
		sc.add(GridStyle.setTableRowStyle(languageDataTable, "#F4F4F4", "#E8E8E8", 3));
		
		languageDataPanel.addStyleName("borderbar");
		languageDataPanel.add(headerTable);
		languageDataPanel.add(sc);
		
		langaugePanel.add(languageDataPanel);
		langaugePanel.setSpacing(10);
		
	    Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.buttonReload());
		reload.setStyleName(Style.Link);
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadLanguage();
			}
		});
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.add(reload);
	    
		return makeWidget(constants.prefUserPendingLanguage(), langaugePanel, hp);
	}
	
	private void loadOntology()
	{
		tableLoading(ontologyPanel);
		final AsyncCallback<ArrayList<String[]>> callback = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> nsMapping) {
				ontologyPanel.clear();
				ontologyPanel.add(ontologyDataPanel);
				ontologyDataTable.removeAllRows();
				int i=0;
				
				for(String[] prefix : nsMapping)
				{
					final ArrayList<String> ontologyId = new ArrayList<String>();
					ontologyId.add(prefix[0]);
					final String item = prefix[1];
					final String userId = prefix[2];
					final String username = prefix[3];
					final String fname = prefix[4];
					final String lname = prefix[5];
					final String email = prefix[6];
					
					ontologyDataTable.setWidget(i, 0 , new HTML(username));
					ontologyDataTable.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_LEFT);
					
					ontologyDataTable.setWidget(i, 1 , new HTML(item));
					ontologyDataTable.getCellFormatter().setHorizontalAlignment(i , 1 , HasHorizontalAlignment.ALIGN_LEFT);
					
					
					Button btn_Add = new Button(constants.buttonAdd());
					btn_Add.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							AsyncCallback<Void> callback = new AsyncCallback<Void>(){
								public void onSuccess(Void result) {
									mailAlert(item, constants.userLang(), username, fname, lname, email);
									loadOntology();
								}
								public void onFailure(Throwable caught) {
									ExceptionManager.showException(caught, constants.userAddUserOntologyFail());
								}
							};
							Service.systemService.addOntologiesToUser(userId, ontologyId, callback);
						}
					});
					
					ontologyDataTable.setWidget(i, 2 , btn_Add);
					ontologyDataTable.getCellFormatter().setHorizontalAlignment(i , 2 , HasHorizontalAlignment.ALIGN_RIGHT);
					i++;
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.userListOntologyFail());
			}
		};
		Service.userPreferenceService.getPendingOntology(callback);
		
	}
	
	private Widget getOntologyTableWidget()
	{
		FlexTable headerTable = new FlexTable();
		headerTable.setWidth(tableWidth+"px");
		headerTable.setText(0, 0, constants.userUsers());		
		headerTable.setText(0, 1, constants.userOntology());	
		headerTable.setText(0, 2, "");	
		headerTable.addStyleName("topbar");
		headerTable.setHeight("25px");
		
		headerTable.getCellFormatter().setWidth(0, 0, ((tableWidth-60)/2)+"px");
		headerTable.getCellFormatter().setWidth(0, 1, ((tableWidth-60)/2)+"px");
		headerTable.getCellFormatter().setWidth(0, 2, "60px");
		
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 0 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 1 , HasHorizontalAlignment.ALIGN_LEFT);
		headerTable.getFlexCellFormatter().setHorizontalAlignment(0, 2 , HasHorizontalAlignment.ALIGN_RIGHT);
		
		ontologyDataTable.setCellPadding(1);
		ontologyDataTable.setCellSpacing(1);
		
		ScrollPanel sc = new ScrollPanel();
		sc.setWidth(tableWidth+"px");
		sc.setHeight((MainApp.getBodyPanelHeight()-425)+"px");
		sc.add(GridStyle.setTableRowStyle(ontologyDataTable, "#F4F4F4", "#E8E8E8", 3));
		
		ontologyDataPanel.addStyleName("borderbar");
		ontologyDataPanel.add(headerTable);
		ontologyDataPanel.add(sc);
		
		ontologyPanel.add(ontologyDataPanel);
		ontologyPanel.setSpacing(10);
		
		Image reload = new Image("images/reload-grey.gif");
		reload.setTitle(constants.buttonReload());
		reload.setStyleName(Style.Link);
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadOntology();
			}
		});
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(reload);
		
		return makeWidget(constants.prefUserPendingOntology(), ontologyPanel, hp);
	}

	/**
	 * @param title
	 * @param widget
	 * @param buttonPanel
	 * @return
	 */
	private Widget makeWidget(String title, Widget widget, HorizontalPanel buttonPanel)
	{
		TitleBodyWidget importWidget = new TitleBodyWidget(title, widget, buttonPanel, "100%", "100%");

		HorizontalPanel widgetPanel = new HorizontalPanel();
		widgetPanel.setSize("100%", "100%");
		widgetPanel.add(importWidget);	
		widgetPanel.setSpacing(10);
		widgetPanel.setCellWidth(importWidget, "100%");
		widgetPanel.setCellVerticalAlignment(importWidget, HasVerticalAlignment.ALIGN_TOP);

		return widgetPanel;
	}
	
	public void tableLoading(VerticalPanel panel){
		String width = tableWidth+"px";
		String height = ""+panel.getOffsetHeight();
		panel.clear();
		panel.setSize(width, height);
		LoadingDialog sayLoading = new LoadingDialog();
		panel.add(sayLoading);
		panel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void mailAlert(String list, String type, String username, String fname, String lname, String email){
		String to = email;
		String subject = messages.mailUserApprovalSubject(constants.mainPageTitle()+" "+ (Main.DISPLAYVERSION!=null?Main.DISPLAYVERSION:"")+ " " + ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.DEV))? "(DEVELOPMENT)" : ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.SANDBOX))? "(SANDBOX)" : "")), type);
		String body = messages.mailUserApprovalBody(fname, lname, type, list, constants.mainPageTitle(), GWT.getHostPageBaseURL(), Main.DISPLAYVERSION);
		
		AsyncCallback<Void> cbkmail = new AsyncCallback<Void>(){
			public void onSuccess(Void result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				//ExceptionManager.showException(caught, "Mail Send Failed");
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail);

		to = "ADMIN";
		subject = messages.mailAdminUserApprovalSubject(constants.mainPageTitle()+" "+ (Main.DISPLAYVERSION!=null?Main.DISPLAYVERSION:"")+ " " + ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.DEV))? "(DEVELOPMENT)" : ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.SANDBOX))? "(SANDBOX)" : "")), type);
		body = messages.mailAdminUserApprovalBody(type,  constants.mainPageTitle(), GWT.getHostPageBaseURL(), Main.DISPLAYVERSION, username, fname, lname, email, list);
		
		AsyncCallback<Void> cbkmail1 = new AsyncCallback<Void>(){
			public void onSuccess(Void result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				//ExceptionManager.showException(caught, "Mail Send Failed");
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail1);

	} 

}
