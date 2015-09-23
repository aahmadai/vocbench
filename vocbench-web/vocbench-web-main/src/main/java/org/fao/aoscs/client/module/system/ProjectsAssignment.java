package org.fao.aoscs.client.module.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.LanguageFilter;
import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.DialogBoxAOS;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.client.widgetlib.shared.panel.BodyPanel;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ProjectsAssignment extends Composite implements ClickHandler, ChangeHandler {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);

	private ListBox lstusers= new ListBox();
	private ListBox lstuserontology = new ListBox(true);
	private ListBox lstusergroups = new ListBox(true);
	private ListBox lstuserlangs = new ListBox(true);
	
	private Image btnaddlang = new Image("images/add-grey.gif");
	private Image btnremovelang = new Image("images/delete-grey.gif");
	private Image btnaddgroup = new Image("images/add-grey.gif");
	private Image btnremovegroup = new Image("images/delete-grey.gif");

	
	private FlexTable ft = new FlexTable();
	private TextBox txtloginname = new TextBox();
	private TextBox txtfname = new TextBox();
	private TextBox txtlname = new TextBox();
	private TextBox txtemail = new TextBox();
	private TextBox txtaddress = new TextBox();
	private TextBox txturl = new TextBox();
	private TextBox txtaffiliation = new TextBox();
	private TextBox txtcountry = new TextBox();
	private TextBox txtpostcode = new TextBox();
	private TextBox txtwphone = new TextBox();
	private TextBox txtmphone = new TextBox();
	private TextBox txtchat = new TextBox();
	private VerticalPanel userSubPanel = new VerticalPanel();
	
	private VerticalPanel panel = new VerticalPanel();

	private Image btnadduser = new Image("images/add-grey.gif");
	private Image btndeluser = new Image("images/delete-grey.gif");

	private ProjectAssignDialogBox projectAssignDialogBox;
	private UserAssignDialogBox newUserDialog;
	private ManagePendingRequests managePendingRequests;
	
	private HashMap<String, String> countryList = new HashMap<String, String>();

	public ProjectsAssignment() {					 
		
		// USER LIST
		lstusers.addChangeHandler(this);
		lstusers.setVisibleItemCount(28);

		// ONTOLOGY LIST			
		lstuserontology.addChangeHandler(this);
		lstuserontology.setVisibleItemCount(28);
		
		HorizontalPanel hpnbtnusers = new HorizontalPanel();
		hpnbtnusers.add(btnadduser);
		hpnbtnusers.add(btndeluser);  

		btnadduser.setTitle(constants.projectAddUser());
		btndeluser.setTitle(constants.projectRemoveUser());
		btnadduser.addClickHandler(this);
		btndeluser.addClickHandler(this);
		
		// GROUP ASSIGNMENT			

		lstusergroups.addChangeHandler(this);
		lstusergroups.setVisibleItemCount(8);

		btnaddgroup.setTitle(constants.buttonAdd());
		btnremovegroup.setTitle(constants.buttonRemove());
		btnaddgroup.addClickHandler(this);
		btnremovegroup.addClickHandler(this);

		HorizontalPanel hpnbtngroup = new HorizontalPanel();
		hpnbtngroup.add(btnaddgroup);
		hpnbtngroup.add(btnremovegroup);

		TitleBodyWidget wGroup = new TitleBodyWidget(constants.userGroup(), lstusergroups, hpnbtngroup, (((MainApp.getBodyPanelWidth()-80) * 0.25)-17) + "px", "100%");


		// LANGUAGE LIST			

		lstuserlangs.addChangeHandler(this);
		lstuserlangs.setVisibleItemCount(8);

		HorizontalPanel hpnbtnlang = new HorizontalPanel();
		hpnbtnlang.add(btnaddlang);
		hpnbtnlang.add(btnremovelang);  

		btnaddlang.setTitle(constants.buttonAdd());
		btnremovelang.setTitle(constants.buttonRemove());
		btnaddlang.addClickHandler(this);
		btnremovelang.addClickHandler(this);

		TitleBodyWidget wLang = new TitleBodyWidget(constants.userLang(), lstuserlangs, hpnbtnlang, (((MainApp.getBodyPanelWidth()-80) * 0.25)-17)  + "px", "100%");

		// USER DETAIL
		
		ft.setWidget(0, 0, new HTML(constants.registerLoginName()));
		ft.setWidget(0, 1, txtloginname);
		txtloginname.setWidth("100%");
		txtloginname.setReadOnly(true);
		ft.getFlexCellFormatter().setColSpan(0, 1, 3);

		ft.setWidget(1, 0, new HTML(constants.registerFirstName()));
		ft.setWidget(1, 1, txtfname);
		txtfname.setWidth("100%");
		txtfname.setReadOnly(true);
		ft.setWidget(1, 2, new HTML(constants.registerLastName()));
		ft.setWidget(1, 3, txtlname);
		txtlname.setWidth("100%");
		txtlname.setReadOnly(true);

		ft.setWidget(2, 0, new HTML(constants.registerEmail()));
		ft.setWidget(2, 1, txtemail);
		txtemail.setWidth("100%");
		txtemail.setReadOnly(true);
		ft.setWidget(2, 2, new HTML(constants.registerAffiliation()));
		ft.setWidget(2, 3, txtaffiliation);
		txtaffiliation.setWidth("100%");
		txtaffiliation.setReadOnly(true);
				
		ft.setWidget(3, 0, new HTML(constants.registerContactAddress()));
		ft.setWidget(3, 1, txtaddress);
		txtaddress.setWidth("100%");
		txtaddress.setReadOnly(true);
		ft.getFlexCellFormatter().setColSpan(3, 1, 3);
		
		ft.setWidget(4, 0, new HTML(constants.registerCountry()));
		ft.setWidget(4, 1, txtcountry);
		txtcountry.setWidth("100%");
		txtcountry.setReadOnly(true);
		ft.setWidget(4, 2, new HTML(constants.registerPostalCode()));
		ft.setWidget(4, 3, txtpostcode);
		txtpostcode.setWidth("100%");
		txtpostcode.setReadOnly(true);

		ft.setWidget(5, 0, new HTML(constants.registerURL()));
		ft.setWidget(5, 1, txturl);
		txturl.setWidth("100%");
		txturl.setReadOnly(true);
		ft.setWidget(5, 2, new HTML(constants.registerChat()));	
		ft.setWidget(5, 3, txtchat);
		txtchat.setWidth("100%");
		txtchat.setReadOnly(true);
		
		ft.setWidget(6, 0, new HTML(constants.registerWorkPhone()));
		ft.setWidget(6, 1, txtwphone);
		txtwphone.setWidth("100%");
		txtwphone.setReadOnly(true);
		ft.setWidget(6, 2, new HTML(constants.registerMobilePhone()));
		ft.setWidget(6, 3, txtmphone);
		txtmphone.setWidth("100%");
		txtmphone.setReadOnly(true);
		
		ft.setWidget(7, 0, wGroup);
		ft.getFlexCellFormatter().setColSpan(7, 0, 2);
		ft.setWidget(7, 1, wLang);
		ft.getFlexCellFormatter().setColSpan(7, 1, 2);
		
		TitleBodyWidget wOntology = new TitleBodyWidget(constants.projectProjects(), lstuserontology, null, ((MainApp.getBodyPanelWidth()-80) * 0.25)  + "px", "100%");

		TitleBodyWidget wUserList = new TitleBodyWidget(constants.userUsers(), lstusers, hpnbtnusers, ((MainApp.getBodyPanelWidth()-80) * 0.25)  +"px", "100%");
		
		TitleBodyWidget wUserDetail = new TitleBodyWidget(constants.userDetails(), GridStyle.setTableRowStyle(ft, "#F4F4F4", "#E8E8E8", 4), null,  ((MainApp.getBodyPanelWidth()-80) * 0.5)  + "px", "100%");		
		
		userSubPanel.add(wUserDetail);
		userSubPanel.setVisible(false);
		
		VerticalPanel userMainPanel = new VerticalPanel();
		userMainPanel.setSize(((MainApp.getBodyPanelWidth()-80) * 0.5)  + "px", "100%");
		userMainPanel.add(userSubPanel);
		
		HorizontalPanel subPanel = new HorizontalPanel();
		subPanel.add(wOntology);	
		subPanel.add(wUserList);					
		subPanel.add(userMainPanel);					
		subPanel.setSpacing(10);
		subPanel.setCellHorizontalAlignment(wOntology,  HasHorizontalAlignment.ALIGN_LEFT);
		subPanel.setCellHorizontalAlignment(wUserList,  HasHorizontalAlignment.ALIGN_LEFT);
		subPanel.setCellHorizontalAlignment(userMainPanel,  HasHorizontalAlignment.ALIGN_LEFT);
		
		HorizontalPanel topRightPanel = new HorizontalPanel();
		HTML lblPendingRequests = new HTML(constants.projectPendingRequests(), false);
		lblPendingRequests.setStyleName(Style.Link);
		lblPendingRequests.addStyleName(Style.colorBlack);
		lblPendingRequests.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(managePendingRequests == null || !managePendingRequests.isLoaded )
					managePendingRequests = new ManagePendingRequests();
				managePendingRequests.show();
				}
			});
		topRightPanel.setSpacing(5);
		topRightPanel.add(lblPendingRequests);

		BodyPanel mainPanel = new BodyPanel(constants.projectManagement() , subPanel , topRightPanel);
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_LEFT);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);

		initWidget(panel);
		initCountryList();
		initUserOntology();	 
	}
	
	private void initUserDetail(final String userid, final String projectid){
		AsyncCallback<Users>callback = new AsyncCallback<Users>() {
			public void onSuccess(Users user) {
				userSubPanel.setVisible(true);
				txtloginname.setText(user.getUsername());
				txtfname.setText(user.getFirstName());
				txtlname.setText(user.getLastName());
				txtemail.setText(user.getEmail());
				txtaffiliation.setText(user.getAffiliation());
				txtaddress.setText(user.getContactAddress());
				txtpostcode.setText(user.getPostalCode());
				txtcountry.setText(countryList.get(user.getCountryCode()));
				txturl.setText(user.getCountryCode());
				txturl.setText(user.getUserUrl());
				txtwphone.setText(user.getWorkPhone());
				txtmphone.setText(user.getMobilePhone());
				txtchat.setText(user.getChatAddress());
				
				initGroupList(userid, projectid);
				initUserLang(userid, projectid);
			}

			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.userListUserFail());
			}
		};
		Service.systemService.getUser(userid, callback);
		
	}
	
	private void initCountryList(){
		AsyncCallback<ArrayList<String[]>>callback = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> tmp) {
				for(int i=0;i<tmp.size();i++){
					String[] item = (String[]) tmp.get(i); 
					countryList.put(item[1], item[0]);			    						    		
				}
			}

			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.userListUserFail());
			}
		};
		Service.systemService.getCountryCodes(callback);
		
	}

	private void initUserList(final String ontologyid){
		AsyncCallback<ArrayList<String[]>>callback = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> tmp) {
				lstusers.clear();
				userSubPanel.setVisible(false);
				for(int i=0;i<tmp.size();i++){
					String[] item = (String[]) tmp.get(i); 
					lstusers.addItem(item[0],item[1]);				    						    		
				}
				if(lstusers.getItemCount()>0)
				{
					lstusers.setSelectedIndex(0);
					initUserDetail(lstusers.getValue(lstusers.getSelectedIndex()), ontologyid);
				}
			}

			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.userListUserFail());
			}
		};
		Service.systemService.getUserAssignedtoOntology(ontologyid, callback);
		
	}

	private void initUserOntology() {
		
		AsyncCallback<ArrayList<OntologyInfo>> callback = new AsyncCallback<ArrayList<OntologyInfo>>() {
			public void onSuccess(ArrayList<OntologyInfo> list) {
				lstuserontology.clear();
				for(OntologyInfo ontoInfo : list)
				{
					lstuserontology.addItem(ontoInfo.getOntologyName(), ""+ontoInfo.getOntologyId());					    		
		    	}
				if(lstuserontology.getItemCount()>0)
				{
					lstuserontology.setSelectedIndex(0);
					initUserList(lstuserontology.getValue(lstuserontology.getSelectedIndex()));
				}
			 }
	
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.projectListProjectFail());
		    }
		};
		Service.systemService.getOntologyList(callback);
	}   
	
	public void generateListData(final Widget sender,String querystr,final String errmsg){

		AsyncCallback<ArrayList<String[]>>cbklist = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> tmp) {
				((ListBox) sender).clear();
				for(int i=0;i<tmp.size();i++){
					String[] item = (String[]) tmp.get(i); 
					((ListBox) sender).addItem(item[0],item[1]);	
				}
			}

			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, errmsg);
			}
		};
		Service.queryService.execHibernateSQLQuery(querystr, cbklist);			
	}
	
	private void initGroupList(String userid, String projectid) {
		lstusergroups.clear();

		/*String sqlStr = "SELECT DISTINCT users_groups_name,users_groups.users_groups_id " +
		" FROM users_groups INNER JOIN users_groups_map " +
		"ON users_groups.users_groups_id = users_groups_map.users_group_id " +
		" WHERE users_groups_map.users_id= '"+userid +"'";*/
		String sqlStr = "SELECT DISTINCT users_groups_name,users_groups.users_groups_id " +
						" FROM users_groups INNER JOIN users_groups_projects " +
						"ON users_groups.users_groups_id = users_groups_projects.users_group_id " +
						" WHERE users_groups_projects.users_id= '"+userid +"' && users_groups_projects.project_id= '"+projectid +"'";
		generateListData(lstusergroups, sqlStr, constants.userListGroupFail());
	}

	private void initUserLang(String userid, String projectid) {
		lstuserlangs.clear();
		
		/*sqlStr = "SELECT local_language,language_code.language_code " +
		"FROM language_code INNER JOIN users_language " +
		"ON language_code.language_code = users_language.language_code " +
		" WHERE users_language.user_id = '"+userid +"'";*/
		
		/*String sqlStr = "SELECT local_language,language_code FROM language_code WHERE " +
				 "language_code IN ( SELECT language_code FROM users_language WHERE user_id =  '"+userid +"' and status=1) order by language_order";*/
		
		String sqlStr = "SELECT local_language,language_code FROM language_code WHERE " +
				 "language_code IN ( SELECT language_code FROM users_language_projects WHERE user_id =  '"+userid +"' and project_id =  '"+projectid +"') order by language_order";
		
		generateListData(lstuserlangs, sqlStr, constants.userListLangFail());
	}   	


	public void onClick(ClickEvent event) {	
		Widget sender = (Widget) event.getSource();
		if (sender==btnadduser){
			if(projectAssignDialogBox == null || !projectAssignDialogBox.isLoaded)
				projectAssignDialogBox = new ProjectAssignDialogBox(lstuserontology.getValue(lstuserontology.getSelectedIndex()));				
			projectAssignDialogBox.show();
		}else if(sender == btndeluser){	
			if((Window.confirm(constants.projectConfirmRemoveUser()))==false){
				return;
			}
			if(lstusers.getSelectedIndex()==-1){
				Window.alert(constants.projectNoProject());
				return;
			}
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					lstusers.removeItem(lstusers.getSelectedIndex());
				}

				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.projectUserRemoveFail());
				}
			};
			Service.systemService.deleteUsersFromOntology(lstuserontology.getValue(lstuserontology.getSelectedIndex()), lstusers.getValue(lstusers.getSelectedIndex()), callback);
		}
		else if (sender==btnaddlang){
			if(newUserDialog == null || !newUserDialog.isLoaded)
				newUserDialog = new UserAssignDialogBox(2,lstusers.getValue(lstusers.getSelectedIndex()), lstuserontology.getValue(lstuserontology.getSelectedIndex()));				
			newUserDialog.show();
		}else if(sender == btnremovelang){	
			if((Window.confirm(constants.userConfirmRemoveLang()))==false){
				return;
			}
			if(lstuserlangs.getSelectedIndex()==-1){
				Window.alert(constants.userNoLang());
				return;
			}
			AsyncCallback<Integer> cbkdellang = new AsyncCallback<Integer>() {
				public void onSuccess(Integer result) {
					lstuserlangs.removeItem(lstuserlangs.getSelectedIndex());
					// create shell command for send mail to user in group 
				}

				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.userLangRemoveFail());
				}
			};
			/*String sql1 = "DELETE FROM users_language " +
			"WHERE language_code='"+lstuserlangs.getValue(lstuserlangs.getSelectedIndex())+"'" +
			"AND user_id ='"+lstusers.getValue(lstusers.getSelectedIndex())+"'";*/
			String sql1 = "DELETE FROM users_language_projects " +
					"WHERE language_code='"+lstuserlangs.getValue(lstuserlangs.getSelectedIndex())+"'" +
					"AND user_id ='"+lstusers.getValue(lstusers.getSelectedIndex())+"' AND project_id ='"+lstuserontology.getValue(lstuserontology.getSelectedIndex())+"' ";

			Service.queryService.hibernateExecuteSQLUpdate(sql1,cbkdellang);

		}else if(sender == btnaddgroup){
			if(newUserDialog == null || !newUserDialog.isLoaded)									
				newUserDialog = new UserAssignDialogBox(1,lstusers.getValue(lstusers.getSelectedIndex()), lstuserontology.getValue(lstuserontology.getSelectedIndex()));				
			newUserDialog.show();
		}else if(sender == btnremovegroup){
			if((Window.confirm(constants.userConfirmRemoveGroup()))==false){
				return;
			}
			if(lstusergroups.getSelectedIndex()==-1){
				Window.alert(constants.userNoGroup());
				return;
			}
			AsyncCallback<Integer> cbkdelgroup = new AsyncCallback<Integer>() {
				public void onSuccess(Integer result) {
					lstusergroups.removeItem(lstusergroups.getSelectedIndex());
					// create shell command for send mail to user in group 
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.userGroupRemoveFail());
				}
			};
			/*String sqlStr = "DELETE FROM users_groups_map " +
			"WHERE users_group_id='"+lstusergroups.getValue(lstusergroups.getSelectedIndex())+"'" +
			"AND users_id ='"+lstusers.getValue(lstusers.getSelectedIndex())+"'";*/
			String sqlStr = "DELETE FROM users_groups_projects " +
					"WHERE users_group_id='"+lstusergroups.getValue(lstusergroups.getSelectedIndex())+"'" +
					"AND users_id ='"+lstusers.getValue(lstusers.getSelectedIndex())+"' AND project_id ='"+lstuserontology.getValue(lstuserontology.getSelectedIndex())+"' ";
			Service.queryService.hibernateExecuteSQLUpdate(sqlStr,cbkdelgroup);

		}
	}

	public void onChange(ChangeEvent event)
	{			
		Widget sender = (Widget) event.getSource(); 
		if(sender == lstuserontology){		
			initUserList(lstuserontology.getValue(lstuserontology.getSelectedIndex()));
		}
		else if(sender == lstusers){		
			initUserDetail(lstusers.getValue(lstusers.getSelectedIndex()), lstuserontology.getValue(lstuserontology.getSelectedIndex()));
		}
	}
	
	private class UserAssignDialogBox extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btngroupadd = new Button(constants.buttonAdd());
		private Button btngroupcancel = new Button(constants.buttonCancel());
		private ListBox lstdata = new ListBox(true);
		private TextBox txthidden = new TextBox();
		private CheckBox showAll = new CheckBox(constants.userShowOnlyRequested(), true);
		private String userId = "0";
		private String projectId = "0";

		public void initData(int selecteduserid, int selectedprojectid, final int typeid, boolean isAll)
		{
			
			AsyncCallback<ArrayList<String[]>> callbackpref = new AsyncCallback<ArrayList<String[]>>() {
				public void onSuccess(ArrayList<String[]> user) {
					lstdata.clear();
					for(int i=0;i<user.size();i++){
			    		String[] item = (String[]) user.get(i);
			    		lstdata.addItem(item[0], item[1]);					    		
			    	}
				 }
		
			    public void onFailure(Throwable caught) {
			    	if(typeid ==1)
			    		ExceptionManager.showException(caught, constants.userListGroupFail());
					else if(typeid ==2)
						ExceptionManager.showException(caught, constants.userListLangFail());
			    }
			};
			
			switch (typeid) 
			{
				case 1 : // Add user group
					this.setText(constants.userSelectGroup());
					showAll.setVisible(true);
					if(isAll)
					{
						UserPreferenceServiceUtil.getInstance().getPendingGroup(selecteduserid, selectedprojectid, callbackpref);
					}
					else
					{
						UserPreferenceServiceUtil.getInstance().getNonAssignedAndPendingGroup(selecteduserid, selectedprojectid, callbackpref);
					}
					break;
				case 2 : // Add user language
					this.setText(constants.userSelectLang());
					showAll.setVisible(true);
					if(isAll)
					{
						UserPreferenceServiceUtil.getInstance().getPendingLanguage(selecteduserid, selectedprojectid, callbackpref);
					}
					else
					{
						UserPreferenceServiceUtil.getInstance().getNonAssignedAndPendingLanguage(selecteduserid, selectedprojectid, callbackpref);
					}
					break;
			}
		}
		
		public UserAssignDialogBox(final int typeid, final String selecteduserid, final String selectedprojectid) {
			this.userId = selecteduserid;
			this.projectId = selectedprojectid;
			
			initData(Integer.parseInt(selecteduserid), Integer.parseInt(selectedprojectid), typeid, false);
			
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
			//tableHP.setSpacing(10);
			tableHP.add(table);
			userpanel.add(tableHP);
			
			showAll.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					initData(Integer.parseInt(selecteduserid), Integer.parseInt(selectedprojectid), typeid, showAll.getValue());
				}
			});
			
			HorizontalPanel showAllPanel = new HorizontalPanel();
			showAllPanel.add(showAll);
			showAllPanel.setCellHorizontalAlignment(showAll, HasHorizontalAlignment.ALIGN_LEFT);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSize("100%", "100%");
			hp.add(showAllPanel);
			
			hp.add(btngroupadd);				
			btngroupadd.addClickHandler(this);
			hp.add(btngroupcancel);
			hp.setSpacing(5);
			hp.setCellWidth(showAllPanel, "100%");
			hp.setCellHorizontalAlignment(showAllPanel, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellVerticalAlignment(showAllPanel, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellHorizontalAlignment(btngroupadd, HasHorizontalAlignment.ALIGN_RIGHT);
			hp.setCellHorizontalAlignment(btngroupcancel, HasHorizontalAlignment.ALIGN_RIGHT);
			
			VerticalPanel hpVP = new VerticalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setSize("100%", "100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);

			btngroupcancel.addClickHandler(this);
			txthidden.setText(Integer.toString(typeid));
			txthidden.setVisible(false);
			userpanel.add(hpVP);
			userpanel.add(txthidden);
			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			
			setWidget(userpanel);
		}

		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(btngroupcancel)){			
				this.hide();
			}else if(sender.equals(btngroupadd)){
				//---- Save process
				if(lstdata.getSelectedIndex()==-1){
					Window.alert(constants.userNoData());
					return;
				}
				
				final HashMap<String,String> data = new HashMap<String,String>();
				for(int i=0;i<lstdata.getItemCount(); i++){
					if(lstdata.isItemSelected(i)){
						data.put(lstdata.getItemText(i), lstdata.getValue(i));
					}
				}
				
				final ArrayList<String> values = new ArrayList<String>(data.values());
				
				if(data.size()>0){
					switch (Integer.parseInt(txthidden.getText())){
					case 1: // groups
						AsyncCallback<Void> callbackAddGroups = new AsyncCallback<Void>(){
							public void onSuccess(Void result) {
								for(String item : data.keySet()){
									lstusergroups.addItem(item, data.get(item));	
								}
							}
							public void onFailure(Throwable caught) {
								ExceptionManager.showException(caught, constants.userAddUserGroupFail());
							}
						};
						Service.systemService.addGroupsToUser(userId, projectId, values, callbackAddGroups);
						break;
					case 2: // languages
						AsyncCallback<Void> callbackAddLanguages = new AsyncCallback<Void>(){
							public void onSuccess(Void result) {
								for(String item : data.keySet()){
									lstuserlangs.addItem(item, data.get(item));	
									if(userId.equals(""+MainApp.userId))
									{
										MainApp.addUserPermissionLanguage(data.get(item));
										MainApp.addUserSelectedLanguage(data.get(item));
										LanguageFilter.reloadLanguages(ModuleManager.getMainApp());
									}
								}
								
							}
							public void onFailure(Throwable caught) {
								ExceptionManager.showException(caught, constants.userAddUserLanguageFail());
							}
						};
						Service.systemService.addLanguagesToUser(userId, projectId, values, callbackAddLanguages);						
						break;
					}
				}
				
				this.hide();
				if(Integer.parseInt(txthidden.getText())==2 || Integer.parseInt(txthidden.getText())==1){
					String item = "";
					for(int i=0;i<lstdata.getItemCount();i++){						
						if(lstdata.isItemSelected(i)){

							if(item.length()>0)
								item += ", "+lstdata.getItemText(i);
							else
								item = lstdata.getItemText(i);
						}
					}
					//check user is activated or not before sending email
					//if(chkactive.getValue())
					{
						String type = "";
						if(Integer.parseInt(txthidden.getText())==2)
							type = constants.userLang();
						if(Integer.parseInt(txthidden.getText())==1)
							type = constants.userGroup();
						mailAlert(item, type);
					}
				}
			}
		} // onclick
	} //--- user Dialog box
	
	private class ProjectAssignDialogBox extends DialogBoxAOS implements ClickHandler{
		private HorizontalPanel mainPanel = new HorizontalPanel();
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnAdd = new Button(constants.buttonAdd());
		private Button btnCancel = new Button(constants.buttonCancel());
		private OlistBox lstdata = new OlistBox();
		private CheckBox showAll = new CheckBox(constants.userShowOnlyRequested(), true);
		private UserDetail ud = new UserDetail();

		public void initData(final int selectedontologyid, boolean isAll)
		{
			this.setText(constants.userUsers());
			showAll.setVisible(true);
			AsyncCallback<ArrayList<Users>> callbackpref = new AsyncCallback<ArrayList<Users>>() {
				public void onSuccess(ArrayList<Users> userList) {
					lstdata.clear();
					for(Users user : userList)
					{
			    		lstdata.addItem(user.getUsername(), user);					    		
			    	}
					if(lstdata.getItemCount()>0)
					{
						lstdata.setSelectedIndex(0);
						ud.loadUserDetail(""+((Users)lstdata.getObject(lstdata.getSelectedIndex())).getUserId());
					}
				 }
		
			    public void onFailure(Throwable caught) {
			    	ExceptionManager.showException(caught, constants.userListUserFail());
			    }
			};
			if(isAll)
			{
				UserPreferenceServiceUtil.getInstance().getPendingUsers(selectedontologyid, callbackpref);
			}
			else
			{
				UserPreferenceServiceUtil.getInstance().getNonAssignedUsers(selectedontologyid, callbackpref);
			}
		}
		
		public ProjectAssignDialogBox(final String selectedontologyid) {
			
			initData(Integer.parseInt(selectedontologyid), false);
			
			final FlexTable table = new FlexTable();
			table.setBorderWidth(0);
			table.setCellPadding(0);
			table.setCellSpacing(1);
			table.setWidth("100%");
			table.setWidget(0, 0, new HTML(""));
			table.setWidget(1,0,lstdata);
			lstdata.setVisibleItemCount(40);
			lstdata.setSize("250px", "335px");
			lstdata.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					ud.loadUserDetail(""+((Users)lstdata.getObject(lstdata.getSelectedIndex())).getUserId());
				}
			});

			table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
			table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
			
			// Popup element
			HorizontalPanel tableHP = new HorizontalPanel();
			//tableHP.setSpacing(10);
			tableHP.add(table);
			userpanel.add(tableHP);
			
			showAll.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					initData(Integer.parseInt(selectedontologyid), showAll.getValue());
				}
			});
			
			HorizontalPanel showAllPanel = new HorizontalPanel();
			showAllPanel.add(showAll);
			showAllPanel.setCellHorizontalAlignment(showAll, HasHorizontalAlignment.ALIGN_LEFT);
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSize("100%", "100%");
			hp.add(showAllPanel);
			hp.add(btnAdd);				
			hp.add(btnCancel);
			hp.setSpacing(5);
			hp.setCellHorizontalAlignment(btnAdd, HasHorizontalAlignment.ALIGN_RIGHT);
			hp.setCellHorizontalAlignment(btnCancel, HasHorizontalAlignment.ALIGN_RIGHT);
			DOM.setStyleAttribute(hp.getElement(), "border", "1px");
			
			HorizontalPanel hpVP = new HorizontalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setSize("100%", "100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);

			btnAdd.addClickHandler(this);
			btnCancel.addClickHandler(this);
			
			userpanel.add(hpVP);
			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			
			TitleBodyWidget userWidget = new TitleBodyWidget(constants.userUsers(), userpanel, null, "100%", "100%");

			mainPanel.setSpacing(10);
			mainPanel.add(userWidget);
			mainPanel.add(ud);
			
			setWidget(mainPanel);
		}

		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(btnCancel)){			
				this.hide();
			}
			else if(sender.equals(btnAdd)){
				if(lstdata.getSelectedIndex()==-1){
					Window.alert(constants.userNoData());
					return;
				}
				final String ontologyId = lstuserontology.getValue(lstuserontology.getSelectedIndex());
				final HashMap<String,String> data = new HashMap<String,String>();
				for(int i=0;i<lstdata.getItemCount(); i++){
					if(lstdata.isItemSelected(i)){
						data.put(lstdata.getItemText(i), ""+((Users)lstdata.getObject(i)).getUserId());
					}
				}
				
				final ArrayList<String> values = new ArrayList<String>(data.values());
				
				if(data.size()>0)
				{
					AsyncCallback<Void> callbackAddGroups = new AsyncCallback<Void>(){
						public void onSuccess(Void result) {
							for(String item : data.keySet()){
								lstusers.addItem(item, data.get(item));
								setItemSelected(item, ontologyId);
							}
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.projectAddUserProjectFail());
						}
					};
					Service.systemService.addUsersToOntology(ontologyId, values, callbackAddGroups);
							
					this.hide();
					String item = "";
					for(int i=0;i<lstdata.getItemCount();i++){						
						if(lstdata.isItemSelected(i)){

							if(item.length()>0)
								item += ", "+lstdata.getItemText(i);
							else
								item = lstdata.getItemText(i);
						}
					}
					mailAlert(item, constants.userOntology());
				}
			} // onclick
		} //--- user Dialog box
	}
	
	private void setItemSelected(String item, String ontologyid)
	{
		for(int i=0;i<lstusers.getItemCount();i++)
		{
			if(lstusers.getItemText(i).equals(item))
			{
				lstusers.setSelectedIndex(i);
				initUserDetail(lstusers.getValue(lstusers.getSelectedIndex()), ontologyid);
			}
		}
	}
	
	public void mailAlert(String list, String type){
		String to = txtemail.getText();
		String subject = messages.mailUserApprovalSubject(constants.mainPageTitle()+" "+ (Main.DISPLAYVERSION!=null?Main.DISPLAYVERSION:"")+ " " + ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.DEV))? "(DEVELOPMENT)" : ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.SANDBOX))? "(SANDBOX)" : "")), type);
		String body = messages.mailUserApprovalBody(txtfname.getText(), txtlname.getText(), type, list, constants.mainPageTitle(), GWT.getHostPageBaseURL(), Main.DISPLAYVERSION);
		
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
		String cc = MainApp.userEmail;
		subject = messages.mailAdminUserApprovalSubject(constants.mainPageTitle()+" "+ (Main.DISPLAYVERSION!=null?Main.DISPLAYVERSION:"")+ " " + ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.DEV))? "(DEVELOPMENT)" : ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.SANDBOX))? "(SANDBOX)" : "")), type);
		body = messages.mailAdminUserApprovalBody(type,  constants.mainPageTitle(), GWT.getHostPageBaseURL(), Main.DISPLAYVERSION, txtloginname.getText(), txtfname.getText(), txtlname.getText(), txtemail.getText(), list);
		
		AsyncCallback<Void> cbkmail1 = new AsyncCallback<Void>(){
			public void onSuccess(Void result) {
				GWT.log("Mail Send Successfully", null);
			}
			public void onFailure(Throwable caught) {
				//ExceptionManager.showException(caught, "Mail Send Failed");
				GWT.log("Mail Send Failed", null);
			}
		};
		Service.systemService.SendMail(to, cc, subject, body, cbkmail1);

	} 

}

