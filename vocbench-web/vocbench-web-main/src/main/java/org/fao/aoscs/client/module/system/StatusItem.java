package org.fao.aoscs.client.module.system;

import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class StatusItem extends HorizontalPanel{

	final protected HandlerManager handlerManager = new HandlerManager(this);
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	public static int ACTION_STATUS = 0;
	public static int ACTION_STATUS_PERMISSION = 1;
	
	private HTML text;
	private Image icon = new Image("images/delete-grey.gif");
	
	private String groupId;
	private String actionId;
	private String statusId;
	
	public StatusItem(String label, String gId, String aId, String sId, final int action){
		super();
		groupId = gId;
		actionId = aId;
		statusId = sId;
		text = new HTML(label);
		add(icon);
		add(text);
		setWidth("100%");
		setCellWidth(icon, "20px");
		setCellHorizontalAlignment(text, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_LEFT);
		setCellVerticalAlignment(text, HasVerticalAlignment.ALIGN_MIDDLE);
		setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_MIDDLE);
		setSpacing(3);
		
		this.icon.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				AsyncCallback<Void> callback = new AsyncCallback<Void>() 
				{
					public void onSuccess(Void result) {
						handlerManager.fireEvent(new StatusItemDeleteEvent());
					}
					public void onFailure(Throwable caught) {
						ExceptionManager.showException(caught, constants.groupLoadGroupsFail());
					}
				};
				if(action==StatusItem.ACTION_STATUS)
					Service.systemService.removeActionStatusFromGroup(groupId, actionId, statusId, callback);
				else if(action==StatusItem.ACTION_STATUS_PERMISSION)
					Service.systemService.removeActionFromGroup(groupId, actionId, statusId, callback);
			}
		});
	}
	
	public void addStatusItemDeleteEventHandler(StatusItemDeleteEventHandler handler){
		handlerManager.addHandler(StatusItemDeleteEvent.getType() ,handler);
	}
	
	static class StatusItemDeleteEvent extends GwtEvent<StatusItemDeleteEventHandler> {
		private static final Type<StatusItemDeleteEventHandler> TYPE = new Type<StatusItemDeleteEventHandler>();
		
		public StatusItemDeleteEvent() {
		}
		public static Type<StatusItemDeleteEventHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(StatusItemDeleteEventHandler handler) {
			handler.onStatusItemDeleteEvent();
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<StatusItemDeleteEventHandler> getAssociatedType() {
			return TYPE;
		}
	}

	public interface StatusItemDeleteEventHandler extends EventHandler {
		void onStatusItemDeleteEvent();
	}

}
