package org.fao.aoscs.client.module.validation.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.RecentChanges;
import org.fao.aoscs.domain.RecentChangesInitObject;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationFilter;
import org.fao.aoscs.domain.ValidationInitObject;
import org.fao.aoscs.domain.ValidationPermission;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ValidationServiceAsync<T> {
	public void getInitData(ValidationFilter vFilter, AsyncCallback<ValidationInitObject> callback);
	public void getPermission(int groupID, AsyncCallback<ArrayList<ValidationPermission>> callback);
	public void getAllUsers(AsyncCallback<ArrayList<Users>> callback);
	public void getStatus(AsyncCallback<ArrayList<OwlStatus>> callback);
	public void getAction(AsyncCallback<ArrayList<OwlAction>> callback);
	public void getOtherAction(AsyncCallback<ArrayList<OwlAction>> callback);
	public void getValidatesize(ValidationFilter vFilter, AsyncCallback<Integer> callback);
	public void updateValidateQueue(HashMap<Validation, String> value, ValidationFilter vFilter, String subjectPrefix, String bodyPrefix, String bodySuffix, AsyncCallback<Integer> callback);
	public void requestValidationRows(Request request, ValidationFilter vFilter, AsyncCallback<ArrayList<Validation>> callback);
	public void getRecentChangesSize(ValidationFilter vFilter, AsyncCallback<Integer> callback);
	public void getRecentChangesData(int ontologyID, AsyncCallback<ArrayList<RecentChanges>> callback);
	void getRecentChangesData(int ontologyID, int recentChangesID, AsyncCallback<ArrayList<RecentChanges>> callback);
	public void getRecentChangesInitData(ValidationFilter vFilter, AsyncCallback<RecentChangesInitObject> callback);
	public void requestRecentChangesRows(Request request, ValidationFilter vFilter, AsyncCallback<ArrayList<RecentChanges>> callback);

}
