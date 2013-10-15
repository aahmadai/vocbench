package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.module.validation.service.ValidationService;
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
import org.fao.aoscs.model.semanticturkey.service.ValidationServiceSTImpl;

public class ValidationServiceSTAdapter implements ValidationService{
	
	private ValidationServiceSTImpl validationService = new ValidationServiceSTImpl();
	
	public  ValidationInitObject getInitData(ValidationFilter vFilter)
	{
		return validationService.getInitData(vFilter);
	}

	public ArrayList<ValidationPermission> getPermission(int groupID)
	{
		return validationService.getPermission(groupID);
	}
	
	public ArrayList<Users> getAllUsers()
	{
		return validationService.getAllUsers();
	}
	
	public ArrayList<OwlStatus> getStatus()
	{
		return validationService.getStatus();
	}
	
	public ArrayList<OwlAction> getAction()
	{
		return validationService.getAction();
	}
	
	public ArrayList<OwlAction> getOtherAction()
	{
		return validationService.getOtherAction();
	}
	
	public int getValidatesize(ValidationFilter vFilter)
	{
		return validationService.getValidatesize(vFilter);
	}
	
	public ArrayList<Validation> requestValidationRows(Request request, ValidationFilter vFilter)
	{
		return validationService.requestValidationRows(request, vFilter);
	}
	
	public int updateValidateQueue(HashMap<Validation, String> value, ValidationFilter vFilter, String subjectPrefix, String bodyPrefix, String bodySuffix)
	{
		return validationService.updateValidateQueue(value, vFilter, subjectPrefix, bodyPrefix, bodySuffix);
	}
	
	public RecentChangesInitObject getRecentChangesInitData(ValidationFilter vFilter){
		return validationService.getRecentChangesInitData(vFilter);
	}

	public int getRecentChangesSize(ValidationFilter vFilter)
	{
		return validationService.getRecentChangesSize(vFilter);
	}
	
	public ArrayList<RecentChanges> requestRecentChangesRows(Request request, ValidationFilter vFilter)
	{
		return validationService.requestRecentChangesRows(request, vFilter);
	}
	
	public ArrayList<RecentChanges> getRecentChangesData(int ontologyID)
	{
		return validationService.getRecentChangesData(ontologyID);
	}

	public ArrayList<RecentChanges> getRecentChangesData(int ontologyID, int recentChangesID) {
		return validationService.getRecentChangesData(ontologyID, recentChangesID);
	}
}
