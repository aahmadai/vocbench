package org.fao.aoscs.client.module.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.panel.TitleBodyWidget;
import org.fao.aoscs.domain.Users;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserDetail extends Composite {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private ListBox lstusergroups = new ListBox(true);
	private ListBox lstuserlangs = new ListBox(true);
	
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

	private HashMap<String, String> countryList = new HashMap<String, String>();

	public UserDetail() {					 
		
		// GROUP ASSIGNMENT			
		lstusergroups.setVisibleItemCount(8);
		lstusergroups.setHeight("100px");

		TitleBodyWidget wGroup = new TitleBodyWidget(constants.userGroup(), lstusergroups, null, (((MainApp.getBodyPanelWidth()-80) * 0.25)-17) + "px", "100%");


		// LANGUAGE LIST			
		lstuserlangs.setVisibleItemCount(8);
		lstuserlangs.setHeight("100px");
		
		TitleBodyWidget wLang = new TitleBodyWidget(constants.userLang(), lstuserlangs, null, (((MainApp.getBodyPanelWidth()-80) * 0.25)-17)  + "px", "100%");

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
		
		TitleBodyWidget wUserDetail = new TitleBodyWidget(constants.userDetails(), GridStyle.setTableRowStyle(ft, "#F4F4F4", "#E8E8E8", 4), null,  ((MainApp.getBodyPanelWidth()-80) * 0.5)  + "px", "100%");		
		
		userSubPanel.add(wUserDetail);
		
		VerticalPanel userMainPanel = new VerticalPanel();
		userMainPanel.setSize("500px", "100%");
		userMainPanel.add(userSubPanel);
		
		HorizontalPanel subPanel = new HorizontalPanel();
		subPanel.add(userMainPanel);					
		subPanel.setSpacing(10);
		subPanel.setCellHorizontalAlignment(userMainPanel,  HasHorizontalAlignment.ALIGN_LEFT);
		
		panel.clear();
		panel.add(userMainPanel);	      
		panel.setCellHorizontalAlignment(userMainPanel,  HasHorizontalAlignment.ALIGN_LEFT);
		panel.setCellVerticalAlignment(userMainPanel,  HasVerticalAlignment.ALIGN_TOP);

		initWidget(panel);
		initCountryList();
	}
	
	public void loadUserDetail(final String userid){
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
				
				initGroupList(userid);
				initUserLang(userid);
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
	
	private void initGroupList(String userid) {
		lstusergroups.clear();

		String sqlStr = "SELECT DISTINCT users_groups_name,users_groups.users_groups_id " +
		" FROM users_groups INNER JOIN users_groups_map " +
		"ON users_groups.users_groups_id = users_groups_map.users_group_id " +
		" WHERE users_groups_map.users_id= '"+userid +"'";
		generateListData(lstusergroups, sqlStr, constants.userListGroupFail());
	}

	private void initUserLang(String userid) {
		lstuserlangs.clear();
		
		String sqlStr = "SELECT local_language,language_code FROM language_code WHERE " +
				 "language_code IN ( SELECT language_code FROM users_language WHERE user_id =  '"+userid +"' and status=1) order by language_order";
		
		generateListData(lstuserlangs, sqlStr, constants.userListLangFail());
	}   	

}