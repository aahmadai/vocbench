package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

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
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

public class ValidationServiceImpl extends PersistentRemoteService implements ValidationService{
	
	private static final long serialVersionUID = 2404029514881638242L;
	private ValidationService validationService;

	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		
		validationService = new ModelManager().getValidationService();
	}
	
	public  ValidationInitObject getInitData(ValidationFilter vFilter) throws Exception
	{
		return validationService.getInitData(vFilter);
	}

	public ArrayList<ValidationPermission> getPermission(int groupID) throws Exception
	{
		return validationService.getPermission(groupID);
	}
	
	public ArrayList<Users> getAllUsers() throws Exception
	{
		return validationService.getAllUsers();
	}
	
	public ArrayList<OwlStatus> getStatus() throws Exception
	{
		return validationService.getStatus();
	}
	
	public ArrayList<OwlAction> getAction() throws Exception
	{
		return validationService.getAction();
	}
	
	public ArrayList<OwlAction> getOtherAction() throws Exception
	{
		return validationService.getOtherAction();
	}
	
	public int getValidatesize(ValidationFilter vFilter) throws Exception
	{
		return validationService.getValidatesize(vFilter);
	}
	
	public ArrayList<Validation> requestValidationRows(Request request, ValidationFilter vFilter) throws Exception
	{
		return validationService.requestValidationRows(request, vFilter);
	}
	
	public int updateValidateQueue(HashMap<Validation, String> value, ValidationFilter vFilter, String subjectPrefix, String bodyPrefix, String bodySuffix) throws Exception
	{
		return validationService.updateValidateQueue(value, vFilter, subjectPrefix, bodyPrefix, bodySuffix);
	}
	
	public RecentChangesInitObject getRecentChangesInitData(ValidationFilter vFilter) throws Exception
	{
		return validationService.getRecentChangesInitData(vFilter);
	}

	public int getRecentChangesSize(ValidationFilter vFilter) throws Exception
	{
		return validationService.getRecentChangesSize(vFilter);
	}
	
	public ArrayList<RecentChanges> requestRecentChangesRows(Request request, ValidationFilter vFilter) throws Exception
	{
		return validationService.requestRecentChangesRows(request, vFilter);
	}
	
	/*public ArrayList<RecentChanges> getRecentChangesData(int ontologyID) throws Exception
	{
		return validationService.getRecentChangesData(ontologyID);
	}

	public ArrayList<RecentChanges> getRecentChangesData(int ontologyID, int recentChangesID) throws Exception
	{
		return validationService.getRecentChangesData(ontologyID, recentChangesID);
	}*/
}
