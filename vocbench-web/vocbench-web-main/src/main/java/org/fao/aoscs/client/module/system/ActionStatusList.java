package org.fao.aoscs.client.module.system;

import java.util.ArrayList;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.system.StatusItem.StatusItemDeleteEventHandler;
import org.fao.aoscs.client.utility.ExceptionManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Widget to hold list of status permissions for an action	 
 */
public class ActionStatusList extends VerticalPanel {


	public final HandlerManager handlerManager = new HandlerManager(this);
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private String gId;
	private String aId;
	
	public ActionStatusList() {
		super();
		setWidth("100%");			
	}
	
	public void initStatus(String groupId, String actionId, final String title, final int action){
		gId = groupId;
		aId = actionId;
		resetWidget();
		add(new HTML("<b>"+title+"</b>"));
		AsyncCallback<ArrayList<String[]>> groupcallback = new AsyncCallback<ArrayList<String[]>>() 
		{
			public void onSuccess(ArrayList<String[]> tmp) 
			{
				if(tmp.size()>0){
					for(int i=0;i<tmp.size();i++){
						String[] item = (String[]) tmp.get(i);
						int status_id = Integer.parseInt(item[0]);						
						String status = item[1].toUpperCase();
						
						StatusItem sItem = new StatusItem(status, gId, aId, ""+status_id, action);
						// when the StatusItem is deleted
						sItem.addStatusItemDeleteEventHandler(new StatusItemDeleteEventHandler(){
							public void onStatusItemDeleteEvent() {
								initStatus(gId, aId, title, action);
							}
						});
						add(sItem);
						setCellHorizontalAlignment(sItem, HasHorizontalAlignment.ALIGN_LEFT);
					}
				}
				else{
					add(new HTML("n/a"));
					//handlerManager.fireEvent(new ActionStatusListEvent(1));
				}
				setVisible(true);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.groupLoadGroupsFail());
			}
		};
		// get unassigned status for given action and group
		String sqlStr = "";
		if(action==StatusItem.ACTION_STATUS)
			sqlStr = "SELECT os.id, os.status from status_action_map sam LEFT JOIN owl_status os on sam.status_id = os.id where group_id = " + groupId + " and action_id = " + actionId;
		else if(action==StatusItem.ACTION_STATUS_PERMISSION)
			sqlStr = "SELECT os.id, os.status from permission_functionality_map pfm LEFT JOIN owl_status os on pfm.status = os.id where group_id = " + groupId + " and function_id = " + actionId;
		Service.queryService.execHibernateSQLQuery(sqlStr, groupcallback);
	}
	
	/**
	 * Clear all status permissions from widget 
	 */
	public void resetWidget(){
		setVisible(false);
		clear();
	}
	
	public void onActionStatusListEventHandler(ActionStatusListEventHandler handler) {
		handlerManager.addHandler(ActionStatusListEvent.getType(), handler);
	}

	// general purpose event
	static class ActionStatusListEvent extends GwtEvent<ActionStatusListEventHandler> {
		int param;
		private static final Type<ActionStatusListEventHandler> TYPE = new Type<ActionStatusListEventHandler>();
		public ActionStatusListEvent(int param) {
			this.param = param;
		}
		public static Type<ActionStatusListEventHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(ActionStatusListEventHandler handler) {
			handler.onActionStatusListEvent(param);
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<ActionStatusListEventHandler> getAssociatedType() {
			return TYPE;
		}
	}
	
	// Button clicked event handler
	public interface ActionStatusListEventHandler extends EventHandler {
		void onActionStatusListEvent(int param);
	}
	
}
