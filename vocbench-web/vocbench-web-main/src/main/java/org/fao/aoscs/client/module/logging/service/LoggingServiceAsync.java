package org.fao.aoscs.client.module.logging.service;

import java.util.ArrayList;
import java.util.List;

import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.UsersVisits;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoggingServiceAsync<T> {
	public void startLog(String token, String ID, AsyncCallback<String> callback);
	public void endLog(String token, AsyncCallback<Void> callback);
	public void viewLog(AsyncCallback<List<String[]>> callback);
	public void getLogInfo(AsyncCallback<List<String>> callback);
	public void requestUsersVisitsRows(Request request, AsyncCallback<ArrayList<UsersVisits>> callback);
}
