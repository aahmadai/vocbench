package org.fao.aoscs.client.module.system;

import java.util.ArrayList;
import java.util.Iterator;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FlexDialogBox;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionFunctionalityMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddGroupActionsDialog extends FlexDialogBox implements ClickHandler {
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private VerticalPanel container = new VerticalPanel();
	
	
	private ListBox actionList = new ListBox();
	private ListBox statusList = new ListBox();
	private VerticalPanel permission_statusList = new VerticalPanel();
	private String groupId;
	public String actionId;
	private int selectedStatusId = -1;
	
	public AddGroupActionsDialog(String gid, final String aid) {
		super(constants.buttonAdd(), constants.buttonCancel(), constants.buttonAddAgain());
		this.groupId = gid;
		setText(constants.groupAddAction());
		center();
		
		initActionList(gid, aid);
		
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {	
			public void onSuccess(Integer tmp) {
				selectedStatusId = tmp;
				initStatusList(aid, groupId);
				initPermissionStatusList(aid, groupId);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.groupSelectAction());
			}
		};
		Service.systemService.getSelectedGroupActionStatusID(gid, aid, callback);
		
		// create UI for action-status input
		container.add(createStatusInput(constants.groupActions()+":&nbsp;", actionList));
		container.add(createStatusInput(constants.groupAddActionStatus()+":&nbsp;", statusList));
		container.add(permission_statusList);

		container.setSpacing(5);
		container.setWidth("100%");
		
		addWidget(container);
		
		// action list select handler
		actionList.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				ListBox b = (ListBox)event.getSource();
				final String action = b.getValue(b.getSelectedIndex());
				if(action.equals("-1"))
				{
					statusList.clear();
					permission_statusList.clear();
				}
				else
				{
					initStatusList(action, groupId);
					initPermissionStatusList(action, groupId);
				}
			}
		});
		
	}
	
	private Widget createStatusInput(String label, Widget w){
		HorizontalPanel addActionContainer = new HorizontalPanel();
		addActionContainer.add(new HTML("<b>"+label+"</b>"));
		addActionContainer.add(w);
		return addActionContainer;
	}
	
	private void initActionList(final String groupId, final String aid){
		AsyncCallback<ArrayList<OwlAction>> callback = new AsyncCallback<ArrayList<OwlAction>>() {	
			public void onSuccess(ArrayList<OwlAction> tmp) {
			    Iterator<OwlAction> it = tmp.iterator();
			    actionList.addItem("--"+constants.buttonSelect()+"--", "-1");
			    int selectedIndex = 0;
			    int index = 1;
				while(it.hasNext()){
					OwlAction oa = (OwlAction)it.next();
					String label = oa.getAction() + (oa.getActionChild().length()>0? "-"+oa.getActionChild() : "");
					actionList.addItem(label, ""+oa.getId());
					if(aid != null){
						if(aid.equals(""+oa.getId()))
							selectedIndex = index;
					}
					index++;
				}
				actionList.setSelectedIndex(selectedIndex);
				if(selectedIndex > 0)
					initStatusList(aid, groupId);
			}

			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.groupUserListFail());
			}
		};
		
		Service.systemService.getUnassignedActions(groupId, callback);
	}
	
	private void initStatusList(final String action, String groupId){
		AsyncCallback<ArrayList<OwlStatus>> callback = new AsyncCallback<ArrayList<OwlStatus>>() {	
			public void onSuccess(ArrayList<OwlStatus> tmp) {
				statusList.clear();
				statusList.addItem("--"+constants.buttonSelect()+"--", "-1");
				for(int i=0;i<tmp.size();i++){
					OwlStatus os = (OwlStatus)tmp.get(i);
					statusList.addItem(os.getStatus(), ""+os.getId());
					if(os.getId() == selectedStatusId)
						statusList.setSelectedIndex(i+1);
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.groupUserListFail());
			}
		};
		Service.validationService.getStatus(callback);
	}
	
	private void initPermissionStatusList(final String action, String groupId){
		AsyncCallback<ArrayList<OwlStatus>> callback = new AsyncCallback<ArrayList<OwlStatus>>() {	
			public void onSuccess(ArrayList<OwlStatus> tmp) {
				Iterator<OwlStatus> it = tmp.iterator();
				permission_statusList.clear();
				permission_statusList.add(new HTML("<b>"+constants.groupAddActionStatusPermission()+"</b>"));
				while(it.hasNext()){
					OwlStatus os = (OwlStatus)it.next();
					String label = os.getStatus();
					CheckBox cb = new CheckBox(label);
					cb.setFormValue(""+os.getId());
					permission_statusList.add(cb);
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.groupUserListFail());
			}
		};
		Service.systemService.getUnassignedActionStatus(groupId, action, callback);
	}

	public boolean passCheckInput(){
		boolean ret = false;
		int wCount = permission_statusList.getWidgetCount();
		for(int i=0; i<wCount; i++){
			Widget w = permission_statusList.getWidget(i);
			if(w instanceof CheckBox)
			{
				CheckBox cb = (CheckBox) w;
				if(cb.getValue())
					ret = true;
			}
		}
		
		if(actionList.getSelectedIndex() > 0 && (ret || statusList.getSelectedIndex() > 0))
			ret = true;
		else 
			ret = false;
		return ret;
	}
	
	public void onSubmit(){
		int wCount = permission_statusList.getWidgetCount();
		ArrayList<PermissionFunctionalityMap> map = new ArrayList<PermissionFunctionalityMap>();
		actionId = actionList.getValue(actionList.getSelectedIndex());
		for(int i=0; i<wCount; i++){
			Widget w = permission_statusList.getWidget(i);
			if(w instanceof CheckBox)
			{
				CheckBox cb = (CheckBox) w;
				if(cb.getValue()){
					PermissionFunctionalityMap item = new PermissionFunctionalityMap();
					item.setFunctionId(Integer.parseInt(actionId));
					item.setGroupId(Integer.parseInt(this.groupId));
					item.setStatus(Integer.parseInt(cb.getFormValue()));
					map.add(item);
				}
			}
		}
		
		PermissionFunctionalityMap pfm = new PermissionFunctionalityMap();
		pfm.setFunctionId(Integer.parseInt(actionId));
		pfm.setGroupId(Integer.parseInt(this.groupId));
		pfm.setStatus(Integer.parseInt(statusList.getValue(statusList.getSelectedIndex())));
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>(){	
			public void onSuccess(Void result){
				if(doLoop){
					doLoop = false;
					resetDialog();
					show();
				}
				else
					hide();
				handlerManager.fireEvent(new FlexDialogSubmitClickedEvent(null));
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.groupUserListFail());
			}
		};
		Service.systemService.addActionToGroup(map, pfm,  callback);
	}
	
	public void onLoopSubmit(){
		doLoop = true;
		onSubmit();
	}
	
	public void resetDialog(){
		initActionList(this.groupId, "");
		statusList.setSelectedIndex(0);
		permission_statusList.clear();
		loop.setEnabled(true);
	}
}