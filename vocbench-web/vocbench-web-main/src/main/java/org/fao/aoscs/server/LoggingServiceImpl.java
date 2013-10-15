package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.logging.service.LoggingService;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.UsersVisits;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.system.service.LoggingServiceSystemImpl;


public class LoggingServiceImpl extends PersistentRemoteService  implements LoggingService{

	private static final long serialVersionUID = 4993831639315721413L;
	private LoggingServiceSystemImpl loggingServiceSystemImpl;
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
		
		loggingServiceSystemImpl = new LoggingServiceSystemImpl();
	}

	public String startLog(String token, String ID){
		String ipadd = this.getThreadLocalRequest().getRemoteAddr();
		String tok = this.getThreadLocalRequest().getSession().getId();
		return loggingServiceSystemImpl.startLog(token, ID, ipadd, tok);	
	}
	
	public void endLog(String token){
		String ipadd = this.getThreadLocalRequest().getRemoteAddr();
		loggingServiceSystemImpl.endLog(token, ipadd);
	}
	
	public List<String[]> viewLog(){
		return loggingServiceSystemImpl.viewLog();
	}
	
	public List<String> getLogInfo(){
		return loggingServiceSystemImpl.getLogInfo(); 
	}	
	
	public ArrayList<UsersVisits> requestUsersVisitsRows(Request request) {
		return loggingServiceSystemImpl.requestUsersVisitsRows(request);
	}
}
