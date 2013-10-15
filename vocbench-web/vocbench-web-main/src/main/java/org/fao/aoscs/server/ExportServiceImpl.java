package org.fao.aoscs.server;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.export.service.ExportService;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.InitializeExportData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

public class ExportServiceImpl extends PersistentRemoteService implements ExportService {
	
	private static final long serialVersionUID = 4448572872961275047L;
	private ExportService exportService;
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
		
		exportService = new ModelManager().getExportService();
	}
	
	public InitializeExportData initData(OntologyInfo ontoInfo) throws Exception
	{
		return exportService.initData(ontoInfo);
	}
	
	public String getExportData(ExportParameterObject exp,OntologyInfo ontoInfo) throws Exception
	{
		return exportService.getExportData(exp, ontoInfo);
	}
	
	public String export(ExportParameterObject exp, int userId, int actionId, OntologyInfo ontoInfo) throws Exception{
		return exportService.export(exp, userId, actionId, ontoInfo);
	}
}
