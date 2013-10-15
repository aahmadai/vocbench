package org.fao.aoscs.client;

import java.util.ArrayList;

import org.fao.aoscs.client.ManageProject.ProjectDialogBoxOpener;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.module.project.service.ProjectService.ProjectServiceUtil;
import org.fao.aoscs.client.module.system.service.SystemService.SystemServiceUtil;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.InitializeUsersPreferenceData;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.UserLogin;
import org.fao.aoscs.domain.UsersGroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
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
	
	private boolean isAdmin = false;
	
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
		
		DOM.setStyleAttribute(lstgroups.getElement(), "cursor", "pointer");
	    DOM.setStyleAttribute(lstlang.getElement(), "cursor", "pointer");
	    DOM.setStyleAttribute(lstontology.getElement(), "cursor", "pointer");
	    
		DOM.setStyleAttribute(txtgroups.getElement(), "marginTop", "0px");
		DOM.setStyleAttribute(txtlang.getElement(), "marginTop", "0px");
		DOM.setStyleAttribute(txtontology.getElement(), "marginTop", "0px");
	    
		
		ImageAOS addButton = new ImageAOS(constants.buttonAdd(), "images/add-grey.gif", "images/add-grey-disabled.gif", isAdmin, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addManageProject == null || !addManageProject.isLoaded )
					addManageProject = new ManageProject(ManageProject.ADD, null, userLoginObj.getUserid());
				addManageProject.show(SelectPreferenceDlg.this);
			}
		});
		
		ImageAOS deleteButton = new ImageAOS(constants.buttonDelete(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", isAdmin, new ClickHandler() {
			public void onClick(ClickEvent event) 
			{
				if(lstontology.getSelectedIndex()>0)
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
	    
		panelgroups.add(tbwidgetgroup);
		panellang.add(tbwidgetlang);
		panelontology.add(tbwidgetontology);

		FlexTable flexpanel = new FlexTable();  		  	 
		flexpanel.setCellSpacing(5);
		flexpanel.setCellPadding(5);
		flexpanel.setWidth("95%");
		int colCount = 0; 
		if(panelgroups.isVisible()){
			flexpanel.setWidget(0,colCount,panelgroups);
			colCount++;
		}
		/*if(panellang.isVisible()){		
			flexpanel.setWidget(0,colCount,panellang);
			colCount++;
		}*/
		if(panelontology.isVisible()){
			flexpanel.setWidget(0,colCount,panelontology);
			colCount++;
		}
		
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
				else
				{
				
					final OntologyInfo selectedOntoInfo = (OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex());
					AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
						public void onSuccess(Boolean val) {
							if(val)
							{
								hide(); 
								userLoginObj.setOntology(selectedOntoInfo);
					    		//userLoginObj.setLanguage(((LanguageInterface) lstlang.getObject(lstlang.getSelectedIndex())).getLanguageCode());
								userLoginObj.setLanguage(constants.mainLocale());
								if(userLoginObj.getGroupid() == null){
									userLoginObj.setGroupid(""+((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsId());
									userLoginObj.setGroupname(""+((UsersGroups) lstgroups.getObject(lstgroups.getSelectedIndex())).getUsersGroupsName());
								}
								//Window.open(GWT.getHostPageBaseURL()+"index.html?locale="+userLoginObj.getLanguage().toLowerCase(), "_self","schollbars=0,toolbar=0,resizable=1,status=no");
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
								    						Window.open(GWT.getHostPageBaseURL()+"index.html?locale="+userLocale, "_self", null);
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

		final AsyncCallback<InitializeUsersPreferenceData> callback = new AsyncCallback<InitializeUsersPreferenceData>() {
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
	}
	
	public void loadData(InitializeUsersPreferenceData initUserPreference)
	{
		userLoginObj.setUsersPreference(initUserPreference.getUsersPreference());
		
		//Set user groups
		lstgroups.clear();
		ArrayList<UsersGroups> listgroups = initUserPreference.getUsergroups();
		for(int i=0;i<listgroups.size();i++){
    		UsersGroups userGroups = (UsersGroups) listgroups.get(i);
    		lstgroups.addItem(userGroups.getUsersGroupsName(),userGroups);
    		if(userGroups.getUsersGroupsId()==1)
    			isAdmin = true;
    	}
		
    	
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
	    
	    // Fill the ontology
	    loadOntologyList(initUserPreference.getOntology(), ""+initUserPreference.getUsersPreference().getUserId(), initUserPreference.getUsersPreference().getOntologyId());
		
	}
	
	public void loadOntologyList(ArrayList<OntologyInfo> ontolist, String userId, int selectedOntologyId)
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
			if(cnt>0)
				lstontology.setItemSelected(cnt, true);
	
			String descOntology = ((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex())).getOntologyDescription();
		    lstontology.setTitle(descOntology);
		    txtontology.setText(descOntology);
		    lstontology.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event) {
					String descOntology = ((OntologyInfo) lstontology.getObject(lstontology.getSelectedIndex())).getOntologyDescription();
					lstontology.setTitle(descOntology);
					txtontology.setText(descOntology);
				}
		    });
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
