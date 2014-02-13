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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("validation")
public interface ValidationService extends RemoteService{

	public ValidationInitObject getInitData(ValidationFilter vFilter) throws Exception;
 
	public ArrayList<ValidationPermission> getPermission(int groupID) throws Exception;

	public ArrayList<Users> getAllUsers() throws Exception;

	public ArrayList<OwlStatus> getStatus() throws Exception;

	public ArrayList<OwlAction> getAction() throws Exception;
	
	public ArrayList<OwlAction> getOtherAction() throws Exception;

	public int getValidatesize(ValidationFilter vFilter) throws Exception;

	public int updateValidateQueue(HashMap<Validation, String> value, ValidationFilter vFilter, String subjectPrefix, String bodyPrefix, String bodySuffix) throws Exception;
 
	public ArrayList<Validation> requestValidationRows(Request request, ValidationFilter vFilter) throws Exception;
	
	public int getRecentChangesSize(ValidationFilter vFilter) throws Exception;

	//public ArrayList<RecentChanges> getRecentChangesData(int ontologyID) throws Exception;
	//public ArrayList<RecentChanges> getRecentChangesData(int ontologyID, int recentChangesID) throws Exception;

	public RecentChangesInitObject getRecentChangesInitData(ValidationFilter vFilter) throws Exception;

	public ArrayList<RecentChanges> requestRecentChangesRows(Request request, ValidationFilter vFilter) throws Exception;
	
	
	
	public static class ValidationServiceUtil{
		private static ValidationServiceAsync<?> instance;
		public static ValidationServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (ValidationServiceAsync<?>) GWT.create(ValidationService.class);
			}
			return instance;
		}
    }
}