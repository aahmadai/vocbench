package org.fao.aoscs.client.module.logging;

import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class LogManager {
	
	public static String token = "";
	public void startLog(final String user)
	{
        AsyncCallback<String> callback = new AsyncCallback<String>() {
		    public void onSuccess(String sessionresult) {
		    	token = (String) sessionresult;
		    }
		    public void onFailure(Throwable caught) {
		    	//ExceptionManager.showException(caught, constants.logStartFail());
		    	//Window.alert(constants.logStartFail());
		    }
		 };
		 Service.loggingService.startLog(token, user, callback); 
	}
	
	public void endLog()
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		    public void onSuccess(Void sessionresult) {
		    	logOut();
		    }
		    public void onFailure(Throwable caught) {
		    	//ExceptionManager.showException(caught, constants.logEndFail());
		    	//	Window.alert(constants.logEndFail());
		    }
		 };
		 Service.loggingService.endLog(token, callback); 
	}
	
	public void logOut()
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onSuccess(Void result){
				Main.signOut();
			}
			public void onFailure(Throwable caught){
				//ExceptionManager.showException(caught, constants.logSignoutFail());
				//Window.alert(constants.logSignoutFail());
			}
		};
		Service.systemService.clearSession(callback);
	}
	
	
}
