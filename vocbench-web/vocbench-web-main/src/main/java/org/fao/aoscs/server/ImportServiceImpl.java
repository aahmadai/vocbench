/**
 * 
 */
package org.fao.aoscs.server;

import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.importdata.service.ImportService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

/**
 * @author rajbhandari
 *
 */
public class ImportServiceImpl extends PersistentRemoteService implements ImportService {

	private static final long serialVersionUID = 4529373126455442725L;
	private ImportService importService;
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
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));
		
		importService = new ModelManager().getImportService();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.importdata.service.ImportService#loadData(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Boolean loadData(OntologyInfo ontoInfo, String inputFile, String baseURI, String formatName) throws Exception
	{
		return importService.loadData(ontoInfo, inputFile, baseURI, formatName);
		
	}

	@Override
	public HashMap<String, String> getRDFFormat(OntologyInfo ontoInfo)
			throws Exception {
		return importService.getRDFFormat(ontoInfo);
	}
	
	
	
}