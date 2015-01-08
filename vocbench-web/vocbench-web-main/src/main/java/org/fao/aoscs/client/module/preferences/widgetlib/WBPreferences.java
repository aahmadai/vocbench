package org.fao.aoscs.client.module.preferences.widgetlib;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.LanguageFilter;
import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageCode;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersLanguage;
import org.fao.aoscs.domain.UsersLanguageId;
import org.fao.aoscs.domain.UsersOntology;
import org.fao.aoscs.domain.UsersOntologyId;
import org.fao.aoscs.domain.UsersPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WBPreferences extends Composite implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private VerticalPanel panel = new VerticalPanel();
	private TextBox userlogin = new TextBox();
	private PasswordTextBox newpassword = new PasswordTextBox();
	private PasswordTextBox confirmpassword = new PasswordTextBox();
	private TextBox useremail = new TextBox();
	private TextBox newuseremail = new TextBox();
	private TextBox confirmuseremail = new TextBox();
	private OlistBox langlistCS = new OlistBox(true);
	private OlistBox langlistCSPending = new OlistBox(true);
	
	private Image btnaddlang = new Image("images/add-grey.gif");
	private Image btnremovelang = new Image("images/delete-grey.gif");
	
	private Image btnremovependinglang = new Image("images/delete-grey.gif");
	private Image btnremovependingonto = new Image("images/delete-grey.gif");
	
	private Image btnaddonto = new Image("images/add-grey.gif");
	private Image btnremoveonto = new Image("images/delete-grey.gif");
	
	private ListBox langlistInterface = new ListBox();
	private OlistBox ontologylist = new OlistBox(true);
	private OlistBox ontologylistPending = new OlistBox(true);
	private ListBox ontology = new ListBox();
	private CheckBox showURICheck = new CheckBox();
	private CheckBox showAlsoNonPreferredCheck = new CheckBox();
	private CheckBox showOnlySelectedLanguages = new CheckBox();
	private CheckBox hideDeprecatedCheck = new CheckBox();
	private CheckBox showInferredAndExplicit = new CheckBox();
	private ListBox initialScreen = new ListBox();
	private Button savepref = new Button();
	private Button clearpref = new Button();
	private Users user = new Users();
	private LanguageDataDialog newlangDialog;
	private OntologyPendingDialog newontoDialog;
	
	public WBPreferences()
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
		
		final AsyncCallback<InitializeUsersPreferenceData> callback = new AsyncCallback<InitializeUsersPreferenceData>() {
			public void onSuccess(InitializeUsersPreferenceData initUserPreference) {
				init(initUserPreference);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.prefNotLoad());
			}
		};
		Service.userPreferenceService.getInitData(MainApp.userId, callback);		
	    initWidget(panel);		  
	}
	
	public void init(InitializeUsersPreferenceData initUserPreference){  

		final FlexTable detailPanel = new FlexTable();
		detailPanel.setSize("100%", "100%");
			  
		// Login info
		for(int i=0;i<detailPanel.getRowCount();i++)
		{
			detailPanel.getCellFormatter().setWidth(i, 0, "40%");	
			detailPanel.getCellFormatter().setWidth(i, 1, "60%");	
		}
		
		detailPanel.setWidget(0,0, new HTML(constants.prefLogin()));
	    detailPanel.setWidget(0,1, userlogin);
	    userlogin.setWidth("100%");
	    detailPanel.setWidget(1,0, new HTML(constants.prefNewPassword()));
	    detailPanel.setWidget(1,1, newpassword);
	    newpassword.setWidth("100%");
	    newpassword.addValueChangeHandler(new ValueChangeHandler<String>(){public void onValueChange(ValueChangeEvent<String> event){MainApp.dataChanged = true;}});
	    detailPanel.setWidget(2,0, new HTML(constants.prefConfirmPassword()));
	    detailPanel.setWidget(2,1, confirmpassword);
	    confirmpassword.setWidth("100%");
	    	    
	    detailPanel.setWidget(3,0, new HTML(constants.prefEmail()));
	    detailPanel.setWidget(3,1, useremail);
	    useremail.setWidth("100%");
	    detailPanel.setWidget(4,0, new HTML(constants.prefNewEmail()));
	    detailPanel.setWidget(4,1, newuseremail);	    	    
	    newuseremail.setWidth("100%");
	    newuseremail.addValueChangeHandler(new ValueChangeHandler<String>(){public void onValueChange(ValueChangeEvent<String> event){MainApp.dataChanged = true;}});
	    detailPanel.setWidget(5,0, new HTML(constants.prefConfirmEmail()));
	    detailPanel.setWidget(5,1, confirmuseremail);	    	    
	    confirmuseremail.setWidth("100%");
	    
	    // Ontology
	    detailPanel.setWidget(6,0, new HTML(constants.prefOntology()));
	    detailPanel.setWidget(6,1, ontology);
		ontology.setWidth("100%");		
		
		// Initial screen preferences
		detailPanel.setWidget(7, 0, new HTML(constants.prefInitialScreen()));
		detailPanel.setWidget(7, 1, initialScreen);
		initialScreen.setWidth("100%");
		
		// Initial screen preferences
		detailPanel.setWidget(8, 0, new HTML(constants.prefLanguageInterface()));
		detailPanel.setWidget(8, 1, langlistInterface);
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
		
		TitleBodyWidget vpPanel = new TitleBodyWidget(constants.prefDetails(), vPanel, null, ((MainApp.getBodyPanelWidth()-120)/2) + "px", "100%");
		
		
		ArrayList<String> userMenu = (ArrayList<String>) MainApp.userMenu;
		Iterator<String> lst = userMenu.iterator();
    	while(lst.hasNext()){
    		initialScreen.addItem((String) lst.next());
    	}
		initialScreen.setSelectedIndex(1);
      
		// LANGUAGE LIST
		//langlistCS.setVisibleItemCount(25);
		
		

		btnaddlang.setTitle(constants.buttonAdd());
		btnaddlang.addClickHandler(this);
		
		btnremovelang.setTitle(constants.buttonRemove());
		btnremovelang.addClickHandler(this);
		
		btnremovependinglang.setTitle(constants.buttonRemove());
		btnremovependinglang.addClickHandler(this);

		btnremovependingonto.setTitle(constants.buttonRemove());
		btnremovependingonto.addClickHandler(this);
		
		HorizontalPanel hpnbtngroup = new HorizontalPanel();
		hpnbtngroup.add(btnaddlang);
		hpnbtngroup.add(btnremovelang);
		
		TitleBodyWidget vpLang = new TitleBodyWidget(constants.prefUserLanguage(), langlistCS, hpnbtngroup, ((MainApp.getBodyPanelWidth()-120)/4)  + "px", "100%");
		
		HorizontalPanel hpnbtnpendinglang = new HorizontalPanel();
		hpnbtnpendinglang.add(btnremovependinglang);
		
		TitleBodyWidget vpLang_pending = new TitleBodyWidget(constants.prefUserPendingLanguage(), langlistCSPending, hpnbtnpendinglang, ((MainApp.getBodyPanelWidth()-120)/4)  + "px", "100%");
		
		VerticalPanel lang_panel = new VerticalPanel();
		lang_panel.setSpacing(1);
		lang_panel.add(vpLang);
		lang_panel.add(vpLang_pending);
		
		//ontologylistPending.setVisibleItemCount(28);
		
		btnaddonto.setTitle(constants.buttonAdd());
		btnaddonto.addClickHandler(this);
		btnremoveonto.setTitle(constants.buttonRemove());
		btnremoveonto.addClickHandler(this);
		
		HorizontalPanel hpnbtnonto = new HorizontalPanel();
		hpnbtnonto.add(btnaddonto);
		hpnbtnonto.add(btnremoveonto);
		
		TitleBodyWidget ontology_ft = new TitleBodyWidget(constants.prefUserOntology(), ontologylist, hpnbtnonto, ((MainApp.getBodyPanelWidth()-120)/4)  + "px", "100%");
		
		HorizontalPanel hpnbtnpendingonto = new HorizontalPanel();
		hpnbtnpendingonto.add(btnremovependingonto);
		
		TitleBodyWidget ontology_ft_pending = new TitleBodyWidget(constants.prefUserPendingOntology(), ontologylistPending, hpnbtnpendingonto, ((MainApp.getBodyPanelWidth()-120)/4)  + "px", "100%");
		
		VerticalPanel ontology_panel = new VerticalPanel();
		ontology_panel.setSpacing(1);
		ontology_panel.add(ontology_ft);
		ontology_panel.add(ontology_ft_pending);
		
		HorizontalPanel rightPanel = new HorizontalPanel();
		rightPanel.setWidth("100%");
		rightPanel.add(lang_panel);		
		rightPanel.add(new Spacer("25px","100%"));		
		rightPanel.add(ontology_panel);
		
		// FLEX PANEL FOR DETIAL, LANGUAGE LIST, INTERFACE LANGUAGE LIST
		HorizontalPanel flexpanel = new HorizontalPanel(); 		
		flexpanel.setSpacing(10);
		flexpanel.setWidth("100%");
		flexpanel.add(vpPanel);	
		flexpanel.add(rightPanel);
		
		Scheduler.get().scheduleDeferred(new Command() {
            public void execute()
            {  
            	int offset = 16;
            	langlistCS.setHeight(((vPanel.getOffsetHeight()/2)-offset)+"px");
            	langlistCSPending.setHeight(((vPanel.getOffsetHeight()/2)-offset)+"px");
            	ontologylist.setHeight(((vPanel.getOffsetHeight()/2)-offset)+"px");
            	ontologylistPending.setHeight(((vPanel.getOffsetHeight()/2)-offset)+"px");
            }
        });		

		flexpanel.setCellVerticalAlignment(vpPanel, HasVerticalAlignment.ALIGN_TOP);
		flexpanel.setCellHorizontalAlignment(vpPanel, HasHorizontalAlignment.ALIGN_CENTER);
		flexpanel.setCellVerticalAlignment(vpLang, HasVerticalAlignment.ALIGN_TOP);
		flexpanel.setCellHorizontalAlignment(vpLang, HasHorizontalAlignment.ALIGN_CENTER);
		flexpanel.setCellVerticalAlignment(ontology_ft_pending, HasVerticalAlignment.ALIGN_TOP);
		flexpanel.setCellHorizontalAlignment(ontology_ft_pending, HasHorizontalAlignment.ALIGN_CENTER);
		
		
		
		VerticalPanel ft_panel = new VerticalPanel();
		ft_panel.setSize("100%", "100%");
		ft_panel.add(flexpanel);
		ft_panel.setCellHorizontalAlignment(flexpanel, HasHorizontalAlignment.ALIGN_CENTER);
		ft_panel.setCellVerticalAlignment(flexpanel, HasVerticalAlignment.ALIGN_TOP);
		ft_panel.setSpacing(5);
		
		
		// Set languages for the CS
		loadLanglistCS();
		
		// Set user ontology
		loadUserOntology();
		
		// Set requested ontology
		loadOntologyPending();
		
		// Set languages for the interface
		Iterator<LanguageInterface> list = initUserPreference.getInterfaceLanguage().iterator();
		while(list.hasNext()){
			LanguageInterface langInterface = (LanguageInterface) list.next();
			langlistInterface.addItem(langInterface.getLocalLanguage()+" ("+langInterface.getLanguageCode().toLowerCase()+")", langInterface.getLanguageCode());
		}
		
		// Fill the ontology
    	for (Iterator<OntologyInfo> iter = initUserPreference.getOntology().iterator(); iter.hasNext();) {
				
    		OntologyInfo ontoInfo = (OntologyInfo) iter.next();
    		ontology.addItem(ontoInfo.getOntologyName(), ""+ontoInfo.getOntologyId());
    	}
		
    	loadUser(initUserPreference.getUsersInfo());
    	MainApp.userPreference = initUserPreference.getUsersPreference();
    	loadUserPref();
		
		VerticalPanel bodyPanel = new VerticalPanel();
	    bodyPanel.setSize("100%", "100%");
		bodyPanel.add(ft_panel);
		bodyPanel.setCellHorizontalAlignment(ft_panel,  HasHorizontalAlignment.ALIGN_CENTER);
		bodyPanel.setCellVerticalAlignment(ft_panel,  HasVerticalAlignment.ALIGN_TOP);
				
		BodyPanel mainPanel = new BodyPanel(constants.preferences() , bodyPanel , null);
		panel.clear();
		panel.setSize("100%", "100%");
		panel.add(mainPanel);	      
	    panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
	    panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);
		
	} // end main corpus: WBPreferences
	
	public void loadUser(Users u)
	{
    	// Set user info
    	user = u;
    	// Set values
    	userlogin.setReadOnly(true);
    	userlogin.setText(user.getUsername());		  
		// Set email with current user email
    	useremail.setReadOnly(true);
		useremail.setText(user.getEmail());			
		newpassword.setText("");
		confirmpassword.setText("");
		newuseremail.setText("");
		confirmuseremail.setText("");		
	}
	
	public void loadUserPref()
	{
		// Set user preference
		UsersPreference userPreference = (UsersPreference) MainApp.userPreference;
		if(userPreference.getUserId()!=0)
		{
			ontology.setSelectedIndex(getIndex(ontology, ""+userPreference.getOntologyId()));
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
	
	public void loadOntologyPending()
	{
		
		AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
		    public void onSuccess(ArrayList<String[]> user) {
		    	for(int i=0;i<user.size();i++){
		    		String[] item = (String[]) user.get(i);
		    		ontologylistPending.addItem(item[0], item[1]);					    		
		    	}
		    }

		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.userListOntologyFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().getPendingOntology(MainApp.userId, callbackpref);
	}
	
	public void loadUserOntology()
	{
		AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
		    public void onSuccess(ArrayList<String[]> list) {
		    	for(int i=0;i<list.size();i++){
		    		String[] item = (String[]) list.get(i);
		    		ontologylist.addItem(item[0], item[1]);					    		
		    	}
		    }

		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.userListOntologyFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().getUserOntology(MainApp.userId, callbackpref);
	}
	
	private void loadLanglistCS()
	{
		AsyncCallback<ArrayList<UsersLanguage>> callbackpref = new AsyncCallback<ArrayList<UsersLanguage>>() {
		    public void onSuccess(ArrayList<UsersLanguage> result) {
		    	langlistCS.clear();
		    	ArrayList<String> userLanguage = new ArrayList<String>();
		    	for(UsersLanguage userLang : result){		
					userLanguage.add(userLang.getId().getLanguageCode().toLowerCase());	
		    	}
		    	
				ArrayList<LanguageCode> lang = (ArrayList<LanguageCode>) MainApp.languageCode;
				for(LanguageCode lc: lang){
					if(userLanguage.contains(lc.getLanguageCode().toLowerCase()))	
					{
						langlistCS.addItem(lc.getLanguageNote(), lc);
					}
				}
		    }

		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.userListLangFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().getUsersLanguage(MainApp.userId, callbackpref);
		
		loadPendingLanglistCS();
		
	}
	
	private void loadPendingLanglistCS()
	{
		
		AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
		    public void onSuccess(ArrayList<String[]> result) {
		    	
		    	langlistCSPending.clear();
		    	ArrayList<String> userLang = new ArrayList<String>();
		    	for(String[] item: result)
		    	{
		    		userLang.add(item[1]);
		    	}
				ArrayList<LanguageCode> lang = (ArrayList<LanguageCode>) MainApp.languageCode;
		    	for(LanguageCode lc : lang)
				{
					if(userLang.contains(lc.getLanguageCode().toLowerCase()))	
					{
						langlistCSPending.addItem(lc.getLanguageNote(), lc);
					}
				}
		    }

		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.userListOntologyFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().getPendingLanguage(MainApp.userId, callbackpref);
		
	}
	
	public int getIndex(ListBox list, String value)
	{
		for(int i=0;i<list.getItemCount();i++)
		{
			if(value.toLowerCase().equals(list.getValue(i).toLowerCase()))
				return i;
		}
		return 0;
	}
	
	// Check form validation
	public boolean validateUser()
	{
		if(newuseremail.getText().equals("") && confirmuseremail.getText().equals("") && newpassword.getText().equals("") && confirmpassword.getText().equals(""))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean validateEmailChange(){	
		boolean isComplete =true;
		if(!newuseremail.getText().equals("") || !confirmuseremail.getText().equals(""))
		{
			if(!newuseremail.getText().equals(confirmuseremail.getText())) {
					Window.alert(constants.prefEmailMismatch());
					newuseremail.setFocus(true);
					isComplete = false;
			}
			else
				user.setEmail(newuseremail.getText());
		}
		return isComplete;
	}
	
	public boolean validatePasswordChange()
	{	
		boolean isComplete =true;		
		if(!newpassword.getText().equals("")  || !confirmpassword.getText().equals(""))
		{
			if(!newpassword.getText().equals(confirmpassword.getText())) {
				Window.alert(constants.prefPasswordMismatch());
				newpassword.setFocus(true);
				isComplete = false;
			}
			else if (newpassword.getText().length()<6){
				Window.alert(constants.prefPasswordMinCharacter());
				newpassword.setFocus(true);
				isComplete=false;
			}
			else
			{
				user.setPassword(newpassword.getText());
			}
		}
		return isComplete;
	}
	
	public int getSelectedItemCnt()
	{
		int cnt = 0;
		for(int i=0;i<langlistCS.getItemCount();i++){
			if(langlistCS.isItemSelected(i)) {
				cnt++;
			}
		}
		return cnt;
	}
	
    /*
     * Functions to manage events 
     * 
     * */
	
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if (sender==btnaddlang){
			if(newlangDialog == null || !newlangDialog.isLoaded)
				newlangDialog = new LanguageDataDialog();				
			newlangDialog.show();
		}
		else if (sender==btnremovelang){
			boolean isAllNotSelected = false;
			for(int i = langlistCS.getItemCount() -1;i>=0; i--){
				if(!langlistCS.isItemSelected(i))
				{
					isAllNotSelected = true;
				}
			}
			if(!isAllNotSelected)
			{
				Window.alert(constants.prefLanguageNoRemoveAll());
				return;
			}
			else if(langlistCS.getSelectedIndex()==-1)
			{
				Window.alert(constants.prefLanguageNotSelected());
				return;
			}
			else if((Window.confirm(constants.prefLanguageSelectRemove()))==false)
			{
				return;
			}
			ArrayList<String> deleteList = new ArrayList<String>();
			for(int i = langlistCS.getItemCount() -1;i>=0; i--){
				if( langlistCS.isItemSelected(i))
				{
					LanguageCode lc = (LanguageCode) langlistCS.getObject(i);
					deleteList.add(lc.getLanguageCode());
				}
			}
			deleteUsersLanguage(deleteList);
											    
		}
		else if (sender==btnremovependinglang){
			if(langlistCSPending.getSelectedIndex()==-1)
			{
				Window.alert(constants.prefLanguageNotSelected());
				return;
			}
			else if((Window.confirm(constants.prefLanguageSelectRemove()))==false)
			{
				return;
			}
			ArrayList<String> deleteList = new ArrayList<String>();
			for(int i = langlistCSPending.getItemCount() -1;i>=0; i--){
				if( langlistCSPending.isItemSelected(i))
				{
					LanguageCode lc = (LanguageCode) langlistCSPending.getObject(i);
					deleteList.add(lc.getLanguageCode());
				}
			}
			deleteUsersPendingLanguage(deleteList);
											    
		}
		else if (sender==btnaddonto){
			if(newontoDialog == null || !newontoDialog.isLoaded)
				newontoDialog = new OntologyPendingDialog();				
			newontoDialog.show();
		}
		else if (sender==btnremoveonto){
			boolean isAllNotSelected = false;
			for(int i = ontologylist.getItemCount() -1;i>=0; i--){
				if(!ontologylist.isItemSelected(i))
				{
					isAllNotSelected = true;
				}
			}
			if(!isAllNotSelected)
			{
				Window.alert(constants.prefOntologyNoRemoveAll());
				return;
			}
			else if(ontologylist.getSelectedIndex()==-1)
			{
				Window.alert(constants.prefOntologyNotSelected());
				return;
			}
			else if((Window.confirm(constants.prefOntologySelectRemove()))==false)
			{
				return;
			}
			ArrayList<String> deleteList = new ArrayList<String>();
			for(int i = ontologylist.getItemCount() -1;i>=0; i--){
				if( ontologylist.isItemSelected(i))
				{
					deleteList.add(ontologylist.getValue(i));
				}
			}
			deleteUsersOntology(deleteList);
											    
		}
		else if (sender==btnremovependingonto){
			if(ontologylistPending.getSelectedIndex()==-1)
			{
				Window.alert(constants.prefOntologyNotSelected());
				return;
			}
			else if((Window.confirm(constants.prefOntologySelectRemove()))==false)
			{
				return;
			}
			ArrayList<String> deleteList = new ArrayList<String>();
			for(int i = ontologylistPending.getItemCount() -1;i>=0; i--){
				if( ontologylistPending.isItemSelected(i))
				{
					deleteList.add(ontologylistPending.getValue(i));
				}
			}
			deleteUsersPendingOntology(deleteList);
											    
		}
		else if(sender == clearpref){			
			clear();

		} else if(sender == savepref){

			if(validateUser())
			{ 
				if(validatePasswordChange() && validateEmailChange())
					updateUser();
			}
			else
			{
				updatePref();				
			}
		} 

	} 
	
	public void clear()
	{
		// reset default values
		loadUser(user);
		loadUserPref(); 
		
	}
	
	public void updateUser()
	{
		boolean isPasswordChange = false;
		if(!newpassword.getText().equals("")  && !confirmpassword.getText().equals(""))
		{
			isPasswordChange = true;
		}
		
		if(isPasswordChange)
		{
			if(newuseremail.getText().equals("") && confirmuseremail.getText().equals(""))
			{
				user.setEmail(useremail.getText());
			}
		}
		
			
		
		AsyncCallback<Users> callbackuser = new AsyncCallback<Users>() {
		    public void onSuccess(Users result) {
		    	user = (Users) result;
		    	loadUser(user);
		    	
		    	updatePref();
		    }
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.prefPasswordEmailchangeFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().updateUsers(user, isPasswordChange ,callbackuser);
	}
	
	private void deleteUsersLanguage(final ArrayList<String> langlist)
	{
		AsyncCallback<ArrayList<UsersLanguage>> callbackpref = new AsyncCallback<ArrayList<UsersLanguage>>() {
		    public void onSuccess(ArrayList<UsersLanguage> result) {
		    	ArrayList<String> userLanguage = new ArrayList<String>();
		    	for(int i=0;i<result.size();i++){		
		    		UsersLanguage userLang = result.get(i);
		    		UsersLanguageId  userLangID = userLang.getId();
					userLanguage.add(userLangID.getLanguageCode().toLowerCase());	
		    	}
		    	MainApp.setUserPermissionLanguage(userLanguage);
		    	loadLanglistCS();
		    	LanguageFilter.reloadLanguages(ModuleManager.getMainApp());
		    }
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.prefLanguageSaveFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().deleteUsersLanguage(MainApp.userId, langlist, callbackpref);
	}
	
	private void deleteUsersPendingLanguage(final ArrayList<String> langlist)
	{
		AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> result) {
				langlistCSPending.clear();
		    	ArrayList<String> userLang = new ArrayList<String>();
		    	for(String[] item: result)
		    	{
		    		userLang.add(item[1]);
		    	}
				ArrayList<LanguageCode> lang = (ArrayList<LanguageCode>) MainApp.languageCode;
		    	for(LanguageCode lc : lang)
				{
					if(userLang.contains(lc.getLanguageCode().toLowerCase()))	
					{
						langlistCSPending.addItem(lc.getLanguageNote(), lc);
					}
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.prefLanguageSaveFail());
			}
		};
		UserPreferenceServiceUtil.getInstance().deleteUsersPendingLanguage(MainApp.userId, langlist, callbackpref);
	}
	
	private void deleteUsersOntology(final ArrayList<String> langlist)
	{
		AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
		    public void onSuccess(ArrayList<String[]> list) {
		    	ontologylist.clear();
		    	for(int i=0;i<list.size();i++){
		    		String[] item = (String[]) list.get(i);
		    		ontologylist.addItem(item[0], item[1]);					    		
		    	}
		    }
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.prefOntologySaveFail());
		    }
		};
		UserPreferenceServiceUtil.getInstance().deleteUsersOntology(MainApp.userId, langlist, callbackpref);
	}
	
	private void deleteUsersPendingOntology(final ArrayList<String> langlist)
	{
		AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> list) {
				ontologylistPending.clear();
				for(int i=0;i<list.size();i++){
		    		String[] item = (String[]) list.get(i);
		    		ontologylistPending.addItem(item[0], item[1]);					    		
		    	}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.prefOntologySaveFail());
			}
		};
		UserPreferenceServiceUtil.getInstance().deleteUsersPendingOntology(MainApp.userId, langlist, callbackpref);
	}
	
	public void updatePref()
	{
		AsyncCallback<UsersPreference> callbackpref = new AsyncCallback<UsersPreference>() {
		    public void onSuccess(UsersPreference result) {
		    	MainApp.userPreference = (UsersPreference) result;
		    	loadUserPref();
		    	
		    	Window.alert(constants.prefSaved());
		    }
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.prefNotSaved());
		    }
		};
		
		UsersPreference userPref = new UsersPreference();
		userPref.setOntologyId(Integer.parseInt(ontology.getValue(ontology.getSelectedIndex())));
		userPref.setFrequency("Daily");		
		userPref.setInitialPage(initialScreen.getValue(initialScreen.getSelectedIndex()));
		userPref.setLanguageCodeInterface(langlistInterface.getValue(langlistInterface.getSelectedIndex()));
		userPref.setHideUri(!showURICheck.getValue());
		userPref.setHideNonpreferred(!showAlsoNonPreferredCheck.getValue());
		userPref.setHideNonselectedlanguages(showOnlySelectedLanguages.getValue());
		userPref.setHideDeprecated(hideDeprecatedCheck.getValue());
		userPref.setShowInferredAndExplicit(showInferredAndExplicit.getValue());
		
		if(MainApp.userPreference.getUserId()==0)
		{
			userPref.setUserId(MainApp.userId);
			UserPreferenceServiceUtil.getInstance().addUsersPreference(userPref, callbackpref);
		}
		else
		{
			userPref.setUserId(MainApp.userPreference.getUserId());
			UserPreferenceServiceUtil.getInstance().updateUsersPreference(userPref, callbackpref);
		}

	    // Todo
	    // Update the session variables if password is changed 
	}
	
	public void savePreference()
	{
	    if(validateUser())
        { 
            if(validatePasswordChange() && validateEmailChange())
                updateUser();
            
        }
        else
        {
            updatePref();               
        }
	}
	
	private class LanguageDataDialog extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnAdd = new Button(constants.buttonAdd());
		private Button btnCancel = new Button(constants.buttonCancel());
		private OlistBox lstdata = new OlistBox(true);

		public LanguageDataDialog() {
			this.setText(constants.prefSelectLanguages());				
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table.setWidth("100%");
			table.setWidget(0, 0, new HTML(""));
			table.setWidget(1,0,lstdata);
		    lstdata.setVisibleItemCount(20);
			lstdata.setSize("250px", "200px");

			table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			// Popup element
			HorizontalPanel tableHP = new HorizontalPanel();
			tableHP.setSpacing(10);
			tableHP.add(table);
			userpanel.add(tableHP);
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(btnAdd);				
			btnAdd.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					if(lstdata.getSelectedIndex()==-1)
					{
						Window.alert(constants.prefLanguageSelectOne());
						return;
					}					
					
					final ArrayList<UsersLanguage> langList = new ArrayList<UsersLanguage>();
					final ArrayList<LanguageCode> itemList = new ArrayList<LanguageCode>();
					for(int i=0;i<lstdata.getItemCount();i++)
					{						
						if(lstdata.isItemSelected(i)){
							
							LanguageCode lc = (LanguageCode) lstdata.getObject(i);
							itemList.add(lc);
							
							UsersLanguageId ulID = new UsersLanguageId();
							ulID.setLanguageCode(lc.getLanguageCode());
							ulID.setStatus(0);
							ulID.setUserId(MainApp.userId);
							
							UsersLanguage ul = new UsersLanguage();
							ul.setId(ulID);
							
							langList.add(ul);
						}
					}
					hide();	
					AsyncCallback<Void> callbackpref = new AsyncCallback<Void>() {
					    public void onSuccess(Void result) {
					    	String lang = "";
					    	for(LanguageCode lc: itemList)
					    	{
					    		langlistCSPending.addItem(lc.getLanguageNote(), lc);
					    		if(lang.length()>0)
					    			lang += ", "+ lc.getLanguageNote();
					    		else
					    			lang = lc.getLanguageNote();
					    	}
					    	mailAlert(lang, "language(s)");
					    	loadLanglistCS();
				    	 }
					    public void onFailure(Throwable caught) {
					    	ExceptionManager.showException(caught, constants.userAddUserGroupFail());
					    }
					};
					UserPreferenceServiceUtil.getInstance().addUsersLanguage(langList, callbackpref);
				}
				
			});
			hp.add(btnCancel);
			hp.setSpacing(5);
			
			VerticalPanel hpVP = new VerticalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setWidth("100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			
			btnCancel.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					hide();
				}
				
			});
			userpanel.add(hpVP);
			
			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			setWidget(userpanel);
		}
		
		public void initData()
		{
			lstdata.clear();
			
			AsyncCallback<ArrayList<String>> callbackpref = new AsyncCallback<ArrayList<String>>() {
			    public void onSuccess(ArrayList<String> langlist) {
			    		
		    		ArrayList<LanguageCode> lang = (ArrayList<LanguageCode>) MainApp.languageCode;
		    		Iterator<LanguageCode> list = lang.iterator();
		    		while(list.hasNext()){
		    			LanguageCode lc = (LanguageCode) list.next();
		    			if(langlist.contains(lc.getLanguageCode().toLowerCase()))	
		    			{
		    				lstdata.addItem(lc.getLanguageNote(), lc);
		    			}
		    		}
		    	}

			    public void onFailure(Throwable caught) {
			    	ExceptionManager.showException(caught, constants.userListLangFail());
			    }
			};
			UserPreferenceServiceUtil.getInstance().getNonAssignedLanguage(MainApp.userId, callbackpref);
		}
		
		public void show()
		{
			initData();
			super.show();
		}
	}
	
	private class OntologyPendingDialog extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnAdd = new Button(constants.buttonAdd());
		private Button btnCancel = new Button(constants.buttonCancel());
		private OlistBox lstdata = new OlistBox(true);

		public OntologyPendingDialog() {
			this.setText(constants.userSelectOntology());				
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table.setWidth("100%");
			
			table.setWidget(0, 0, new HTML(""));
			table.setWidget(1,0,lstdata);
		    lstdata.setVisibleItemCount(20);
			lstdata.setSize("250px", "200px");

			table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			// Popup element
			HorizontalPanel tableHP = new HorizontalPanel();
			tableHP.setSpacing(10);
			tableHP.add(table);
			userpanel.add(tableHP);
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(btnAdd);				
			btnAdd.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					if(lstdata.getSelectedIndex()==-1)
					{
						Window.alert(constants.prefOntologySelectOne());
						return;
					}
					ArrayList<UsersOntology> uoList = new ArrayList<UsersOntology>();
					final ArrayList<String[]> itemList = new ArrayList<String[]>();
					for(int i=0;i<lstdata.getItemCount();i++)
					{						
						if(lstdata.isItemSelected(i)){
							final int selindex = i;
							UsersOntologyId uoID = new UsersOntologyId();
							uoID.setOntologyId(Integer.parseInt(lstdata.getValue(i)));
							uoID.setUserId(MainApp.userId);
							
							UsersOntology uo = new UsersOntology();
							uo.setId(uoID);
							uo.setStatus(0);
							
							uoList.add(uo);
							
							String[] item = new String[2];
							item[0] = lstdata.getItemText(selindex);
							item[1] = lstdata.getValue(selindex);
							itemList.add(item);
							
						}
					}	
					
					hide();	
					AsyncCallback<Void> callbackpref = new AsyncCallback<Void>() {
					    public void onSuccess(Void result) {
					    	String ontology = "";
					    	for(String[] item: itemList)
					    	{
					    		ontologylistPending.addItem(item[0], item[1]);
					    		if(ontology.length()>0)
					    			ontology += ", "+ item[0];
					    		else
					    			ontology = item[0];
					    	}
					    	mailAlert(ontology, "ontology");
							
				    	 }
					    public void onFailure(Throwable caught) {
					    	ExceptionManager.showException(caught, constants.userAddUserGroupFail());
					    }
					};
					UserPreferenceServiceUtil.getInstance().addUsersOntology(uoList, callbackpref);
				}
			});
			hp.add(btnCancel);
			hp.setSpacing(5);
			
			VerticalPanel hpVP = new VerticalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setWidth("100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
			
			btnCancel.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					hide();
				}
				
			});
			userpanel.add(hpVP);
			
			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			setWidget(userpanel);
		}
		
		public void initData()
		{
			lstdata.clear();
			
			AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
			    public void onSuccess(ArrayList<String[]> user) {
			    	for(int i=0;i<user.size();i++){
			    		String[] item = (String[]) user.get(i);
			    		lstdata.addItem(item[0], item[1]);					    		
			    	}
			    }

			    public void onFailure(Throwable caught) {
			    	ExceptionManager.showException(caught, constants.userListOntologyFail());
			    }
			};
			UserPreferenceServiceUtil.getInstance().getNonAssignedOntology(MainApp.userId, callbackpref);
		}
		
		public void show()
		{
			initData();
			super.show();
		}
	}
	
	public void mailAlert(String list, String type) {
		
		String to = user.getEmail();
		String subject = messages.mailPreferencesSubject(constants.mainPageTitle()+" "+ (Main.DISPLAYVERSION!=null?Main.DISPLAYVERSION:"")+ " " + ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.DEV))? "(DEVELOPMENT)" : ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.SANDBOX))? "(SANDBOX)" : "")));
		String body = messages.mailPreferencesBody(user.getFirstName(), user.getLastName(), constants.mainPageTitle(), GWT.getHostPageBaseURL(), Main.DISPLAYVERSION, type, list);

		AsyncCallback<Void> cbkmail = new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail);

		to = "ADMIN";
		subject = messages.mailAdminPreferencesSubject(constants.mainPageTitle()+" "+ (Main.DISPLAYVERSION!=null?Main.DISPLAYVERSION:"")+ " " + ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.DEV))? "(DEVELOPMENT)" : ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.SANDBOX))? "(SANDBOX)" : "")), type);
		body = messages.mailAdminPreferencesBody(type, constants.mainPageTitle(), GWT.getHostPageBaseURL(), Main.DISPLAYVERSION, user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), list);
		
		AsyncCallback<Void> cbkmail1 = new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				GWT.log("Mail Send Successfully", null);
			}

			public void onFailure(Throwable caught) {
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, subject, body, cbkmail1);
	}
	
} 