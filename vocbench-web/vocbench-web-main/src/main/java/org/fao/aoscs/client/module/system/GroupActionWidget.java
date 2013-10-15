package org.fao.aoscs.client.module.system;

import java.util.ArrayList;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.system.ActionStatusList.ActionStatusListEventHandler;
import org.fao.aoscs.client.utility.ExceptionManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GroupActionWidget extends HorizontalPanel{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public ListBox actions = new ListBox();
	private ActionStatusList status = new ActionStatusList();
	private ActionStatusList permission_status = new ActionStatusList();
	private String groupId = null;

	public GroupActionWidget() {
		super();
		actions.setVisibleItemCount(16);
		actions.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(groupId != null)
				{
					initActionStatusList(groupId, ""+actions.getValue(actions.getSelectedIndex()));
					initActionPermissionStatusList(groupId, ""+actions.getValue(actions.getSelectedIndex()));
				}
			}
		});
		actions.setWidth("100%");
		
		
		VerticalPanel statusPanel = new VerticalPanel();
		statusPanel.setSpacing(1);
		statusPanel.setSize("100%", "100%");
		
		statusPanel.add(status);
		statusPanel.add(permission_status);
		
		setCellHorizontalAlignment(status, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(permission_status, HasHorizontalAlignment.ALIGN_LEFT);
		status.onActionStatusListEventHandler(new ActionStatusListEventHandler(){
			public void onActionStatusListEvent(int param) {
				if(param==1){ // remove action
					initActionList(groupId, ""+actions.getValue(actions.getSelectedIndex()));
				}
			}
		});
		permission_status.onActionStatusListEventHandler(new ActionStatusListEventHandler(){
			public void onActionStatusListEvent(int param) {
				if(param==1){ // remove action
					initActionList(groupId, ""+actions.getValue(actions.getSelectedIndex()));
				}
			}
		});
		
		add(actions);
		add(statusPanel);
		setCellWidth(actions, "60%");
		setCellWidth(statusPanel, "40%");
		setCellHorizontalAlignment(actions, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(statusPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
	}
	
	/**
	 * Initialize action ListBox with actions assigned to a group
	 * @param groupId
	 */
	public void initActionList(String groupId, final String actionId){
		this.groupId = groupId;
		status.resetWidget();
		permission_status.resetWidget();
		AsyncCallback<ArrayList<String[]>> groupcallback = new AsyncCallback<ArrayList<String[]>>() 
		{
			public void onSuccess(ArrayList<String[]> tmp) 
			{
				actions.clear();
				for(int i=0;i<tmp.size();i++){
					String[] item = (String[]) tmp.get(i);
					String action = item[1].toUpperCase();						
					String child = item[2].toUpperCase();					
					String val = child.length()>0? action +"_"+child : action;
					actions.addItem(val,item[0]);
					if(actionId.equals(item[0]))
						actions.setSelectedIndex(i);
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.groupLoadGroupsFail());
			}
		};
		String sqlStr = "SELECT oa.id, oa.action, oa.action_child FROM permission_functionality_map pfm, owl_action oa WHERE pfm.function_id = oa.id AND pfm.group_id = " + groupId + " GROUP BY pfm.function_id";
		Service.queryService.execHibernateSQLQuery(sqlStr, groupcallback);
	}
	
	/**
	 * Get status permissions for a group and action
	 * @param groupId
	 * @param actionId
	 */
	public void initActionStatusList(String groupId, String actionId){
		status.initStatus(groupId, actionId, constants.groupAddActionStatus(), StatusItem.ACTION_STATUS);
	}
	
	/**
	 * Get status permissions for a group and action
	 * @param groupId
	 * @param actionId
	 */
	public void initActionPermissionStatusList(String groupId, String actionId){
		permission_status.initStatus(groupId, actionId, constants.groupAddActionStatusPermission(), StatusItem.ACTION_STATUS_PERMISSION);
	}


	public String getSelectedAction(){
		if(actions.getSelectedIndex() > -1){
			return actions.getValue(actions.getSelectedIndex());
		}
		else
			return null;
	}
}