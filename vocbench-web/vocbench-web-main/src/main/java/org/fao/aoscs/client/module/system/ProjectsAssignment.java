package org.fao.aoscs.client.module.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.preferences.service.UsersPreferenceService.UserPreferenceServiceUtil;
import org.fao.aoscs.client.utility.ExceptionManager;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ProjectsAssignment extends Composite implements ClickHandler, ChangeHandler {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	private ListBox lstusers= new ListBox();
	private ListBox lstuserontology = new ListBox(true);
	private VerticalPanel panel = new VerticalPanel();

	private Image btnadduser = new Image("images/add-grey.gif");
	private Image btndeluser = new Image("images/delete-grey.gif");

	private ProjectAssignDialogBox projectAssignDialogBox;

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

		btnadduser.setTitle(constants.buttonAdd());
		btndeluser.setTitle(constants.buttonRemove());
		btnadduser.addClickHandler(this);
		btndeluser.addClickHandler(this);

		TitleBodyWidget wOntology = new TitleBodyWidget(constants.projectProjects(), lstuserontology, null, ((MainApp.getBodyPanelWidth()-80) * 0.5)  + "px", "100%");

		TitleBodyWidget wUserList = new TitleBodyWidget(constants.userUsers(), lstusers, hpnbtnusers, ((MainApp.getBodyPanelWidth()-80) * 0.5)  +"px", "100%");
		
		HorizontalPanel subPanel = new HorizontalPanel();
		subPanel.add(wOntology);	
		subPanel.add(wUserList);					
		subPanel.setSpacing(10);

		BodyPanel mainPanel = new BodyPanel(constants.projectManagement() , subPanel , null);
		panel.clear();
		panel.add(mainPanel);	      
		panel.setCellHorizontalAlignment(mainPanel,  HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(mainPanel,  HasVerticalAlignment.ALIGN_TOP);

		initWidget(panel);
		initUserOntology();	 
	}

	private void initUserList(String ontologyid){
		AsyncCallback<ArrayList<String[]>>callback = new AsyncCallback<ArrayList<String[]>>() {
			public void onSuccess(ArrayList<String[]> tmp) {
				lstusers.clear();
				for(int i=0;i<tmp.size();i++){
					String[] item = (String[]) tmp.get(i); 
					lstusers.addItem(item[0],item[1]);				    						    		
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
			 }
	
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.projectListProjectFail());
		    }
		};
		Service.systemService.getOntologyList(callback);
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
	}

	public void onChange(ChangeEvent event)
	{			
		Widget sender = (Widget) event.getSource(); 
		if(sender == lstuserontology){		
			String getnum=lstuserontology.getValue(lstuserontology.getSelectedIndex());
			initUserList(getnum);
		}
	}

	private class ProjectAssignDialogBox extends DialogBoxAOS implements ClickHandler{
		private VerticalPanel userpanel = new VerticalPanel();
		private Button btnAdd = new Button(constants.buttonAdd());
		private Button btnCancel = new Button(constants.buttonCancel());
		private OlistBox lstdata = new OlistBox();

		public void initData(int selectedontologyid)
		{
			this.setText(constants.userUsers());
			AsyncCallback<ArrayList<Users>> callbackpref = new AsyncCallback<ArrayList<Users>>() {
				public void onSuccess(ArrayList<Users> userList) {
					lstdata.clear();
					for(Users user : userList)
					{
			    		lstdata.addItem(user.getUsername(), user);					    		
			    	}
				 }
		
			    public void onFailure(Throwable caught) {
			    	ExceptionManager.showException(caught, constants.userListUserFail());
			    }
			};
			UserPreferenceServiceUtil.getInstance().getNonAssignedUsers(selectedontologyid, callbackpref);
		}
		
		public ProjectAssignDialogBox(final String selectedontologyid) {
			
			initData(Integer.parseInt(selectedontologyid));
			
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
			
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSize("100%", "100%");
			hp.add(btnAdd);				
			hp.add(btnCancel);
			hp.setSpacing(5);
			hp.setCellHorizontalAlignment(btnAdd, HasHorizontalAlignment.ALIGN_RIGHT);
			hp.setCellHorizontalAlignment(btnCancel, HasHorizontalAlignment.ALIGN_RIGHT);
			DOM.setStyleAttribute(hp.getElement(), "border", "1px");
			
			VerticalPanel hpVP = new VerticalPanel();			
			hpVP.setStyleName("bottombar");
			hpVP.setSize("100%", "100%");
			hpVP.add(hp);
			hpVP.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);

			btnAdd.addClickHandler(this);
			btnCancel.addClickHandler(this);
			
			userpanel.add(hpVP);
			userpanel.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
			
			setWidget(userpanel);
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
							}
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.projectAddUserProjectFail());
						}
					};
					Service.systemService.addUsersToOntology(ontologyId, values, callbackAddGroups);
							
					this.hide();
				}
			} // onclick
		} //--- user Dialog box
	}

}

