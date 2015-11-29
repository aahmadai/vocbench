package org.fao.aoscs.client;

import java.util.ArrayList;

import org.fao.aoscs.client.ManageProject.ProjectDialogBoxOpener;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.module.project.service.ProjectService.ProjectServiceUtil;
import org.fao.aoscs.client.module.system.service.SystemService.SystemServiceUtil;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.UsersGroups;
import org.fao.aoscs.domain.UsersPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectPreferenceDlg extends DialogBoxAOS implements ProjectDialogBoxOpener{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private OlistBox lstgroups = new OlistBox();
	private OlistBox lstlang = new OlistBox();
	private OlistBox lstontology = new OlistBox();
	
	private TextArea txtgroups = new TextArea();
	private TextArea txtlang = new TextArea();
	private TextArea txtontology = new TextArea();
	
	private VerticalPanel panelgroups = new VerticalPanel();
	private VerticalPanel panellang = new VerticalPanel();		
	private VerticalPanel panelontology = new VerticalPanel();
	
	//private boolean isAdmin = false;
	
	private Button btnSubmit = new Button(constants.buttonSubmit());
	private Button btnCancel = new Button(constants.buttonCancel());
	
	ArrayList<String> menu = new ArrayList<String>();
	
	private ManageProject addManageProject;
	private ManageProject deleteManageProject;
	
	private UserLogin userLoginObj;
	
	public SelectPreferenceDlg(final UserLogin  userLoginObj) {				
		this.userLoginObj = userLoginObj;
		//load initial data
		load();
		//checkAdmin(userLoginObj.getUserid());
	}
	
	public void initPanels(){
		// popup element
		this.setText(constants.selPref());
		
		lstgroups.setWidth("200px");
		lstlang.setWidth("200px");
		lstontology.setWidth("200px");
		
		lstgroups.setVisibleItemCount(6);
		lstlang.setVisibleItemCount(6);
		lstontology.setVisibleItemCount(6);
		
		txtgroups.setWidth("200px");
		txtlang.setWidth("200px");
		txtontology.setWidth("200px");
		
		txtgroups.setVisibleLines(4);
		txtlang.setVisibleLines(4);
		txtontology.setVisibleLines(4);
		
		txtgroups.setReadOnly(true);
		txtlang.setReadOnly(true);
		txtontology.setReadOnly(true);
		
		lstgroups.getElement().getStyle().setCursor(Cursor.POINTER);
		lstlang.getElement().getStyle().setCursor(Cursor.POINTER);
		lstontology.getElement().getStyle().setCursor(Cursor.POINTER);
		
		txtgroups.getElement().getStyle().setMarginTop(0, Unit.PX);
		txtlang.getElement().getStyle().setMarginTop(0, Unit.PX);
		txtontology.getElement().getStyle().setMarginTop(0, Unit.PX);
		
		ImageAOS addButton = new ImageAOS(constants.buttonAdd(), "images/add-grey.gif", "images/add-grey-disabled.gif", userLoginObj.isAdministrator(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addManageProject == null || !addManageProject.isLoaded )
					addManageProject = new ManageProject(ManageProject.ADD, null, userLoginObj.getUserid());
				addManageProject.show(SelectPreferenceDlg.this);
			}
		});
		
		ImageAOS deleteButton = new ImageAOS(constants.buttonDelete(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", userLoginObj.isAdministrator(), new ClickHandler() {
			public void onClick(ClickEvent event) 
			{
				if(lstontology.getSelectedIndex()!=-1)
				{
					OntologyInfo ontoInto = (OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex());
					if(deleteManageProject == null || !deleteManageProject.isLoaded)
						deleteManageProject = new ManageProject(ManageProject.DELETE, ontoInto, userLoginObj.getUserid());
					deleteManageProject.show(SelectPreferenceDlg.this);					
				}
			}
		});
		
		HorizontalPanel projectButtonPanel = new HorizontalPanel();
    	projectButtonPanel.add(addButton);
    	projectButtonPanel.add(deleteButton);
		
	    TitleBodyWidget tbwidgetgroup = new TitleBodyWidget(constants.selPrefGroup(), lstgroups, null, txtgroups,  "200px", "100%");
	    TitleBodyWidget tbwidgetlang = new TitleBodyWidget(constants.selPrefLanguage(), lstlang, null, txtlang, "200px", "100%");
	    TitleBodyWidget tbwidgetontology = new TitleBodyWidget(constants.selPrefOntology(), lstontology, projectButtonPanel, txtontology, "200px", "100%");
	    
	    panelontology.add(tbwidgetontology);
		panelgroups.add(tbwidgetgroup);
		panellang.add(tbwidgetlang);

		FlexTable flexpanel = new FlexTable();  		  	 
		flexpanel.setCellSpacing(5);
		flexpanel.setCellPadding(5);
		flexpanel.setWidth("95%");
		int colCount = 0; 
		if(panelontology.isVisible()){
			flexpanel.setWidget(0,colCount,panelontology);
			colCount++;
		}
		if(panelgroups.isVisible()){
			flexpanel.setWidget(0,colCount,panelgroups);
			colCount++;
		}
		/*if(panellang.isVisible()){		
			flexpanel.setWidget(0,colCount,panellang);
			colCount++;
		}*/
		
		for(int i=0; i<colCount; i++){
			int width =  100/colCount;
			flexpanel.getCellFormatter().setWidth(0, i, width+"%");
			flexpanel.getCellFormatter().setVerticalAlignment(0,i,HasVerticalAlignment.ALIGN_TOP);
			flexpanel.getCellFormatter().setHorizontalAlignment(0,i,HasHorizontalAlignment.ALIGN_CENTER);
			flexpanel.getCellFormatter().setHeight(0,i, "100%");
		}
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(10);
		panel.setWidth("100%");
		panel.add(flexpanel);
				
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		hp.add(btnSubmit);
		hp.add(btnCancel);
		
		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");
		bottompanel.addStyleName("bottombar");
		bottompanel.add(hp);
		bottompanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(panel);
		vp.add(bottompanel);
		
		vp.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_RIGHT);
		vp.setWidth("100%");
		setWidget(vp);
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Main.gotoLoginScreen();
				hide();
			}
		
		});

		btnSubmit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				
				if(lstontology.getItemCount()<1)
					Window.alert(constants.conceptCompleteInfo());
				else if(lstgroups.getItemCount()<1) {
		    			Window.alert(constants.loginNoAssignGroup());
				}
				else
				{
				
					final OntologyInfo selectedOntoInfo = (OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex());
					AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
						public void onSuccess(Boolean val) {
							if(val)
							{
								hide(); 
								AsyncCallback<ArrayList<String>> callback1 = new AsyncCallback<ArrayList<String>>() {
					    			public void onSuccess(ArrayList<String> userSelectedLanguage) {	   				
					    				userLoginObj.setOntology(selectedOntoInfo);
					    				userLoginObj.setUserSelectedLanguage(userSelectedLanguage);
					    				//userLoginObj.setLanguage(((LanguageInterface) lstlang.getObject(lstlang.getSelectedIndex())).getLanguageCode());
										userLoginObj.setLanguage(constants.mainLocale());
										if(userLoginObj.getGroupid() == null){
											userLoginObj.setGroupid(""+((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsId());
											userLoginObj.setGroupname(""+((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsName());
										}
							    		AsyncCallback<UserLogin> cbkauthorize = new AsyncCallback<UserLogin>() {
							    			public void onSuccess(UserLogin result) {	   				
							    				if(result==null){
								    				try {
														Window.alert(constants.selPrefNoMatch());
													} catch (Exception e) {
														e.printStackTrace();
													}
							    				} else {
							    					try {	    						
							    						// Create session User data from session for feed to profile query 
							    						AsyncCallback<UserLogin> callback1 = new AsyncCallback<UserLogin>() {
							    						    public void onSuccess(UserLogin userLoginObj) {
							    						    	String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
										    					String userLocale = "";
										    					if(userLoginObj.getUsersPreference()!=null && userLoginObj.getUsersPreference().getLanguageCodeInterface()!=null)
										    						userLocale = userLoginObj.getUsersPreference().getLanguageCodeInterface().toLowerCase();
										    					if(!userLocale.equals("") && !currentLocale.equalsIgnoreCase(userLocale))
										    					{
										    						Window.Location.replace(MainApp.getLocaleURL("locale", userLocale));
										    					}
										    					else
										    					{
								    						    	MainApp mainApp = new MainApp(userLoginObj);
								    						    	new LogManager().startLog(""+userLoginObj.getUserid());
								    						    	mainApp.setWidth("100%");
																	Main.replaceRootPanel(mainApp); 
										    					}
							    						    }
							    						    public void onFailure(Throwable caught) {
							    						    	ExceptionManager.showException(caught, constants.selPrefFail());
							    						    }
							    						 };
							    						Service.systemService.checkSession(MainApp.USERLOGINOBJECT_SESSIONNAME, callback1); // Get userlogin from session		    						
													} catch (Exception e) {
														e.printStackTrace();
													}
										        }
							    			}
										    public void onFailure(Throwable caught) {
										    	ExceptionManager.showException(caught, constants.selPrefFail());
										    }
							    		};		
							    		
										Service.systemService.getAuthorization(MainApp.USERLOGINOBJECT_SESSIONNAME, userLoginObj,cbkauthorize);	
					    				
					    			}
								    public void onFailure(Throwable caught) {
								    	ExceptionManager.showException(caught, constants.selPrefFail());
								    }
					    		};		
					    		
								Service.systemService.getUserSelectedLanguageCode(Integer.parseInt(userLoginObj.getUserid()), selectedOntoInfo.getOntologyId(), callback1);
							}
							else
								
								Window.alert(constants.projectSTServiceFail());
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.projectSTServiceFail());
						}
					};
					ProjectServiceUtil.getInstance().isSTServerStarted(selectedOntoInfo, callback);
				}
			}
		});
	}
	public void load()
	{

		final AsyncCallback<ArrayList<OntologyInfo>> callback = new AsyncCallback<ArrayList<OntologyInfo>>() {
			public void onSuccess(ArrayList<OntologyInfo> list) {
				loadOntologyList(list, userLoginObj.getUserid(), -1);
			    initPanels();
			    center();
				
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.selPrefNoLoad());
			}
		};
		SystemServiceUtil.getInstance().getOntology(userLoginObj.getUserid(), callback);
/*		final AsyncCallback<InitializeUsersPreferenceData> callback = new AsyncCallback<InitializeUsersPreferenceData>() {
			public void onSuccess(InitializeUsersPreferenceData initUserPreference) {
				
				loadData(initUserPreference);
				initPanels();
				center();
				
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.selPrefNoLoad());
			}
		};
		SystemServiceUtil.getInstance().initSelectPreferenceData(Integer.parseInt(userLoginObj.getUserid()), callback);
*/	}
	
	/*public void loadData(InitializeUsersPreferenceData initUserPreference)
	{
		//loadGroupList(initUserPreference.getUsergroups(), ""+initUserPreference.getUsersPreference().getUserId(), initUserPreference.getUsersPreference().getOntologyId());
		//loadLangList(initUserPreference, ""+initUserPreference.getUsersPreference().getUserId(), initUserPreference.getUsersPreference().getOntologyId());
	    //loadOntologyList(initUserPreference.getOntology(), ""+initUserPreference.getUsersPreference().getUserId(), initUserPreference.getUsersPreference().getOntologyId());
		
	}*/
	
	/*private void checkAdmin(String userId)
	{
		AsyncCallback<ArrayList<UsersGroups>> callback = new AsyncCallback<ArrayList<UsersGroups>>() {
			public void onSuccess(ArrayList<UsersGroups> listgroups) {
				for(int i=0;i<listgroups.size();i++){
		    		UsersGroups userGroups = (UsersGroups) listgroups.get(i);
		    		if(userGroups.getUsersGroupsId()==1)
		    			isAdmin = true;
		    	}
				load();
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.selPrefNoLoad());
			}
		};
		SystemServiceUtil.getInstance().getUserGroup(Integer.parseInt(userId), 0, callback);
		
	}*/
	
	public void loadOntologyList(ArrayList<OntologyInfo> ontolist, final String userId, final int selectedOntologyId)
	{
		
	    lstontology.clear();
	    /*if(ontolist.size() < 1){
	    	hide();
	    	Window.alert(constants.selLoadOntologyFail());
	    	Main.gotoLoginScreen();
	    }*/
	    if(ontolist.size() > 0){			
			for(int i=0;i<ontolist.size();i++)
			{
	    		OntologyInfo ontoInfo = (OntologyInfo) ontolist.get(i);
	    		lstontology.addItem(ontoInfo.getOntologyName(), ontoInfo);
	    		if(ontoInfo.getOntologyId()==selectedOntologyId)
	    			lstontology.setSelectedIndex(i);
			}
			
			int cnt = 0;
			if(userId.equals(""))
			{
				for(int i=0;i<lstontology.getItemCount();i++)
				{
					if(selectedOntologyId == ((OntologyInfo)lstontology.getObject(i)).getOntologyId())
					{
						cnt = i;
						break;
					}
				}
			}
			//if(cnt>0)
			if(lstontology.getItemCount()>0)
			{
				lstontology.setSelectedIndex(cnt);
				lstontology.setItemSelected(cnt, true);
				DomEvent.fireNativeEvent(Document.get().createChangeEvent(), lstontology);
				loadUserGroup(userId);
			}
			
			if(lstontology.getSelectedIndex()!=-1)
			{
				String descOntology = ((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex())).getOntologyDescription();
			    lstontology.setTitle(descOntology);
			    txtontology.setText(descOntology);
			}
		    lstontology.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event) {
					loadUserGroup(userId);
				}
		    });
	    }
	}
	
	public void loadUserGroup(final String userId)
	{
		if(lstontology.getSelectedIndex()!=-1)
		{
			String descOntology = ((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex())).getOntologyDescription();
			final int ontologyId = ((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex())).getOntologyId();
			lstontology.setTitle(descOntology);
			txtontology.setText(descOntology);
			AsyncCallback<ArrayList<UsersGroups>> callback = new AsyncCallback<ArrayList<UsersGroups>>() {
				public void onSuccess(ArrayList<UsersGroups> list) {
					loadGroupList(list, userId, ontologyId);
					AsyncCallback<UsersPreference> callback = new AsyncCallback<UsersPreference>() {
						public void onSuccess(UsersPreference usersPreference) {
							if(usersPreference.getId()==null || usersPreference.getId().getUserId()==0 || usersPreference.getId().getOntologyId()==0)
							{
								AsyncCallback<UsersPreference> callback1 = new AsyncCallback<UsersPreference>() {
									public void onSuccess(UsersPreference usersPreference) {
										userLoginObj.setUsersPreference(usersPreference);
									}
									public void onFailure(Throwable caught) {
										ExceptionManager.showException(caught, constants.selPrefNoLoad());
									}
								};
								UserPreferenceServiceUtil.getInstance().getUsersPreference(Integer.parseInt(userId), -1, callback1);
							}
							else
								userLoginObj.setUsersPreference(usersPreference);
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.selPrefNoLoad());
						}
					};
					UserPreferenceServiceUtil.getInstance().getUsersPreference(Integer.parseInt(userId), ontologyId, callback);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.selPrefNoLoad());
				}
			};
			SystemServiceUtil.getInstance().getUserGroup(Integer.parseInt(userId), ontologyId, callback);
		}
	}
	
	/*public void loadLangList(InitializeUsersPreferenceData initUserPreference, String userId, int selectedOntologyId)
	{
	
		// Set languages for the interface
	    lstlang.clear();
	    ArrayList<LanguageInterface> listlang = initUserPreference.getInterfaceLanguage();
		for(int i=0;i<listlang.size();i++)
		{
			LanguageInterface langInterface = (LanguageInterface) listlang.get(i);
			lstlang.addItem(langInterface.getLocalLanguage(), langInterface);
		}
		
		int index = 0;
		String defaultlang = "en";
		if(initUserPreference.getUsersPreference().getUserId() != 0)
			defaultlang = initUserPreference.getUsersPreference().getLanguageCodeInterface().toLowerCase();
		for(int i=0;i<lstlang.getItemCount();i++)
		{
			if(defaultlang.equals(((LanguageInterface)lstlang.getObject(i)).getLanguageCode().toLowerCase()))
			{
				index = i;
				break;
			}
		}
		lstlang.setSelectedIndex(index);
		
		String descLang = ((LanguageInterface) lstlang.getObject(lstlang.getSelectedIndex())).getLanguageNote();
		lstlang.setTitle(descLang);
	    txtlang.setText(descLang);
	    lstlang.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				String descLang = ((LanguageInterface) lstlang.getObject(lstlang.getSelectedIndex())).getLanguageNote();
				lstlang.setTitle(descLang);
				txtlang.setText(descLang);
			}
	    	
	    });
		
	}*/
	
	public void loadGroupList(ArrayList<UsersGroups> listgroups, String userId, int selectedOntologyId)
	{
		//Set user groups
		lstgroups.clear();
		for(int i=0;i<listgroups.size();i++){
    		UsersGroups userGroups = (UsersGroups) listgroups.get(i);
    		lstgroups.addItem(userGroups.getUsersGroupsName(),userGroups);
    		//if(userGroups.getUsersGroupsId()==1)
    			//isAdmin = true;
    	}
		
    	if(lstgroups.getItemCount()>0)
    		lstgroups.setItemSelected(0, true);
    	
    	String descGroups = ((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsDesc();
	    lstgroups.setTitle(descGroups);
	    txtgroups.setText(descGroups);
	    lstgroups.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				String descGroups = ((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsDesc();
				lstgroups.setTitle(descGroups);
				txtgroups.setText(descGroups);
			}
	    	
	    });
	    
	    if(userLoginObj.getNoOfGroup()<2){
			panelgroups.setVisible(false);
		}else{
			if(userLoginObj.getGroupid()!= null &&  userLoginObj.getGroupid().equals(ConfigConstants.VISITORGROUPID)){
				panelgroups.setVisible(false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.ManageProject.ProjectDialogBoxOpener#projectDialogBoxSubmit(java.util.ArrayList, java.lang.String, int)
	 */
	public void projectDialogBoxSubmit(ArrayList<OntologyInfo> ontolist,
			String userId, int selectedOntologyId) {
		loadOntologyList(ontolist, userId, selectedOntologyId);
		
	}
}
