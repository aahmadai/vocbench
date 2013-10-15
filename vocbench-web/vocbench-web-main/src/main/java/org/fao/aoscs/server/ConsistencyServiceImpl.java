package org.fao.aoscs.server;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.consistency.service.ConsistencyService;
import org.fao.aoscs.domain.Consistency;
import org.fao.aoscs.domain.ConsistencyInitObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

public class ConsistencyServiceImpl extends PersistentRemoteService implements ConsistencyService{
	
	private static final long serialVersionUID = 288904786686164268L;
	private ConsistencyService consistencyService;
	
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
		
		consistencyService = new ModelManager().getConsistencyService();

	}
	
	public ConsistencyInitObject getInitData(OntologyInfo ontoInfo) throws Exception
	{
		return consistencyService.getInitData(ontoInfo);
	}
	
	public HashMap<String, Consistency> getConsistencyQueue(int selection, OntologyInfo ontoInfo) throws Exception
	{
		return consistencyService.getConsistencyQueue(selection, ontoInfo);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String, Consistency> updateConsistencyQueue(List value, int selection, OntologyInfo ontoInfo) throws Exception
	{
		return consistencyService.updateConsistencyQueue(value, selection, ontoInfo);
	}
	
	
	
}
