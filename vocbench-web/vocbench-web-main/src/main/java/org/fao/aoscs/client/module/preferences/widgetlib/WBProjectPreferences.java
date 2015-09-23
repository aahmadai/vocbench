package org.fao.aoscs.client.module.preferences.widgetlib;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.UsersPreference;
import org.fao.aoscs.domain.UsersPreferenceId;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WBProjectPreferences extends Composite implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private VerticalPanel panel = new VerticalPanel();
	
	private ListBox langlistInterface = new ListBox();
	private ListBox initialScreen = new ListBox();
	
	private CheckBox showURICheck = new CheckBox();
	private CheckBox showAlsoNonPreferredCheck = new CheckBox();
	private CheckBox showOnlySelectedLanguages = new CheckBox();
	private CheckBox hideDeprecatedCheck = new CheckBox();
	private CheckBox showInferredAndExplicit = new CheckBox();
	private Button savepref = new Button();
	private Button clearpref = new Button();
	
	private UsersPreference userPreference;
	
	public WBProjectPreferences()
	{
		/*
		 * Load the necessary information 
		 * 
		 * */			
		
		LoadingDialog loadingDialog = new LoadingDialog();
		panel.clear();
		panel.setSize("100%", "100%");
	    panel.add(loadingDialog);
	    panel.setCellHorizontalAlignment(loadingDialog, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(loadingDialog, HasVerticalAlignment.ALIGN_MIDDLE);
		
		init();
	    initWidget(panel);		  
	}
	
	public void init(){  

		final FlexTable detailPanel = new FlexTable();
		detailPanel.setSize("100%", "100%");
			  
		// Login info
		for(int i=0;i<detailPanel.getRowCount();i++)
		{
			detailPanel.getCellFormatter().setWidth(i, 0, "40%");	
			detailPanel.getCellFormatter().setWidth(i, 1, "60%");	
		}
		
		
		// Initial screen preferences
		detailPanel.setWidget(1, 0, new HTML(constants.prefInitialScreen()));
		detailPanel.setWidget(1, 1, initialScreen);
		initialScreen.setWidth("100%");
		
		// Initial screen preferences
		detailPanel.setWidget(2, 0, new HTML(constants.prefLanguageInterface()));
		detailPanel.setWidget(2, 1, langlistInterface);
		langlistInterface.setWidth("100%");
		
		// FILTER OPTION CHECK BOXES
		showURICheck.setText(constants.prefShowURI());
		showAlsoNonPreferredCheck.setText(constants.prefShowNonPreferredTermsAlso());
		showOnlySelectedLanguages.setText(constants.prefShowOnlySelectedLanguages());
		hideDeprecatedCheck.setText(constants.prefHideDeprecated());
		showInferredAndExplicit.setText(constants.conceptShowInferredAndExplicit());
		
		FlexTable filterOption = new FlexTable();
		filterOption.setSize("100%", "100%");
		filterOption.setCellSpacing(0);
		filterOption.setCellPadding(1);
		filterOption.setWidget(0, 0, showURICheck);
		filterOption.setWidget(0, 1, showAlsoNonPreferredCheck);
		filterOption.setWidget(1, 0, showOnlySelectedLanguages);
		filterOption.setWidget(1, 1, hideDeprecatedCheck);
		filterOption.setWidget(2, 0, showInferredAndExplicit);
		
		// SAVE and CLEAR
		savepref.setText(constants.buttonSave());
		savepref.addClickHandler(this);
		clearpref.setText(constants.buttonClear());
		clearpref.addClickHandler(this);
		
		FlexTable submit_ft = new FlexTable();
		submit_ft.setSize("100%", "100%");
		submit_ft.setWidget(0, 0, savepref);
		submit_ft.setWidget(0, 1, clearpref);
		submit_ft.setSize("100px", "100%");
		
		final HorizontalPanel panel_row3 = new HorizontalPanel();
		panel_row3.setSize("100%", "35px");
		panel_row3.add(submit_ft);
		panel_row3.addStyleName("bottombar");
		panel_row3.setCellHorizontalAlignment(submit_ft, HasHorizontalAlignment.ALIGN_RIGHT);
		
		final VerticalPanel vPanel = new VerticalPanel();		
		vPanel.add(GridStyle.setTableRowStyle(detailPanel, "#F4F4F4", "#E8E8E8", 3));
		vPanel.add(filterOption);
		vPanel.add(panel_row3);
		vPanel.setCellVerticalAlignment(panel_row3,  HasVerticalAlignment.ALIGN_BOTTOM);
		vPanel.setCellHorizontalAlignment(panel_row3,  HasHorizontalAlignment.ALIGN_CENTER);
		vPanel.setCellHeight(panel_row3, "100%");
		
		TitleBodyWidget vpPanel = new TitleBodyWidget(constants.prefDetails(), vPanel, null, (MainApp.getBodyPanelWidth()-130) + "px", "100%");
		
		
		ArrayList<String> userMenu = (ArrayList<String>) MainApp.userMenu;
		Iterator<String> lst = userMenu.iterator();
    	while(lst.hasNext()){
    		initialScreen.addItem((String) lst.next());
    	}
		initialScreen.setSelectedIndex(1);
      
		// FLEX PANEL FOR DETIAL, LANGUAGE LIST, INTERFACE LANGUAGE LIST
		HorizontalPanel flexpanel = new HorizontalPanel(); 		
		flexpanel.setSpacing(10);
		flexpanel.setWidth("100%");
		flexpanel.add(vpPanel);	

		flexpanel.setCellVerticalAlignment(vpPanel, HasVerticalAlignment.ALIGN_TOP);
		flexpanel.setCellHorizontalAlignment(vpPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel ft_panel = new VerticalPanel();
		ft_panel.setSize("100%", "100%");
		ft_panel.add(flexpanel);
		ft_panel.setCellHorizontalAlignment(flexpanel, HasHorizontalAlignment.ALIGN_CENTER);
		ft_panel.setCellVerticalAlignment(flexpanel, HasVerticalAlignment.ALIGN_TOP);
		ft_panel.setSpacing(5);
		
		// Set languages for the interface
		AsyncCallback<ArrayList<LanguageInterface>> callback = new AsyncCallback<ArrayList<LanguageInterface>>() {
			public void onSuccess(ArrayList<LanguageInterface> result) {
				Iterator<LanguageInterface> list = result.iterator();
				while(list.hasNext()){
					LanguageInterface langInterface = (LanguageInterface) list.next();
					langlistInterface.addItem(langInterface.getLocalLanguage()+" ("+langInterface.getLanguageCode().toLowerCase()+")", langInterface.getLanguageCode());
				}
		    	loadUserPref();
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.prefNotLoad());
			}
		};
		Service.systemService.getInterfaceLang(callback);
		
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(ft_panel);	      
	    panel.setCellHorizontalAlignment(ft_panel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(ft_panel,  HasVerticalAlignment.ALIGN_TOP);
		
	} // end main corpus: WBPreferences
	
	public void loadUserPref()
	{
		
		final AsyncCallback<UsersPreference> callback = new AsyncCallback<UsersPreference>() {
			public void onSuccess(UsersPreference userPreference1) {
				
				// Set user preference
				userPreference = userPreference1;
				if(userPreference.getId() != null && userPreference.getId().getUserId()!=0)
				{
					//ontology.setSelectedIndex(getIndex(ontology, ""+userPreference.getOntologyId()));
					initialScreen.setSelectedIndex(getIndex(initialScreen, ""+userPreference.getInitialPage()));
					langlistInterface.setSelectedIndex(getIndex(langlistInterface, ""+userPreference.getLanguageCodeInterface()));
					showURICheck.setValue(!userPreference.isHideUri());
					showAlsoNonPreferredCheck.setValue(!userPreference.isHideNonpreferred());
					showOnlySelectedLanguages.setValue(userPreference.isHideNonselectedlanguages());
					hideDeprecatedCheck.setValue(userPreference.isHideDeprecated());
					showInferredAndExplicit.setValue(userPreference.isShowInferredAndExplicit());
				}
				else
				{
					langlistInterface.setSelectedIndex(getIndex(langlistInterface, "en"));
			        showURICheck.setValue(false);
			        showAlsoNonPreferredCheck.setValue(false);
			        showOnlySelectedLanguages.setValue(false);
			        hideDeprecatedCheck.setValue(false);
			        showInferredAndExplicit.setValue(false);
			        initialScreen.setSelectedIndex(0);
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.prefNotLoad());
			}
		};
		Service.userPreferenceService.getUsersPreference(MainApp.userId, MainApp.userOntology.getOntologyId(), callback);	
	}
	
	private int getIndex(ListBox list, String value)
	{
		for(int i=0;i<list.getItemCount();i++)
		{
			if(value.toLowerCase().equals(list.getValue(i).toLowerCase()))
				return i;
		}
		return 0;
	}
	
    /*
     * Functions to manage events 
     * 
     * */
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if(sender == clearpref){			
			clear();

		} else if(sender == savepref){
			updatePref();				
		} 

	} 
	
	public void clear()
	{
		// reset default values
		loadUserPref(); 
	}
	
	
	public void updatePref()
	{
		AsyncCallback<UsersPreference> callbackpref = new AsyncCallback<UsersPreference>() {
		    public void onSuccess(UsersPreference result) {
		    	userPreference = (UsersPreference) result;
		    	loadUserPref();
		    	
		    	Window.alert(constants.prefSaved());
		    }
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.prefNotSaved());
		    }
		};
		
		
		UsersPreference userPref = new UsersPreference();
		//userPref.setOntologyId(Integer.parseInt(ontology.getValue(ontology.getSelectedIndex())));
		userPref.setFrequency("Daily");		
		userPref.setInitialPage(initialScreen.getValue(initialScreen.getSelectedIndex()));
		userPref.setLanguageCodeInterface(langlistInterface.getValue(langlistInterface.getSelectedIndex()));
		userPref.setHideUri(!showURICheck.getValue());
		userPref.setHideNonpreferred(!showAlsoNonPreferredCheck.getValue());
		userPref.setHideNonselectedlanguages(showOnlySelectedLanguages.getValue());
		userPref.setHideDeprecated(hideDeprecatedCheck.getValue());
		userPref.setShowInferredAndExplicit(showInferredAndExplicit.getValue());
		
		UsersPreferenceId userPrefId = new UsersPreferenceId();
		if(userPreference.getId() == null || userPreference.getId().getUserId()==0 || userPreference.getId().getOntologyId()==0)
		{
			userPrefId.setUserId(MainApp.userId);
			userPrefId.setOntologyId(MainApp.userOntology.getOntologyId());
			userPref.setId(userPrefId);
			UserPreferenceServiceUtil.getInstance().addUsersPreference(userPref, callbackpref);
		}
		else
		{
			userPrefId.setUserId(userPreference.getId().getUserId());
			userPrefId.setOntologyId(userPreference.getId().getOntologyId());
			userPref.setId(userPrefId);
			UserPreferenceServiceUtil.getInstance().updateUsersPreference(userPref, callbackpref);
		}
	}
	
} 