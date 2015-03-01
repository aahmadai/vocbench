/**
 * 
 */
package org.fao.aoscs.server;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.refactor.service.RefactorService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

/**
 * @author rajbhandari
 *
 */
public class RefactorServiceImpl extends PersistentRemoteService implements RefactorService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5836758605346590902L;

	private RefactorService refactorService;
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
		
		refactorService = new ModelManager().getRefactorService();
	}
	@Override
	public boolean changeResourceName(OntologyInfo ontoInfo, String oldResource,
			String newResource) throws Exception {
		return refactorService.changeResourceName(ontoInfo, oldResource, newResource);		
	}
	@Override
	public boolean replaceBaseURI(OntologyInfo ontoInfo, String sourceBaseURI,
			String targetBaseURI, String graphArrayString) throws Exception {
		return refactorService.replaceBaseURI(ontoInfo, sourceBaseURI, targetBaseURI, graphArrayString);		
	}
	@Override
	public boolean convertLabelsToSKOSXL(OntologyInfo ontoInfo) throws Exception {
		return refactorService.convertLabelsToSKOSXL(ontoInfo);		
	}
	@Override
	public String exportWithSKOSLabels(OntologyInfo ontoInfo) throws Exception {
		return refactorService.exportWithSKOSLabels(ontoInfo);		
	}
	@Override
	public boolean reifySKOSDefinitions(OntologyInfo ontoInfo) throws Exception {
		return refactorService.reifySKOSDefinitions(ontoInfo);		
	}
	@Override
	public String exportWithFlatSKOSDefinitions(OntologyInfo ontoInfo) throws Exception {
		return refactorService.exportWithFlatSKOSDefinitions(ontoInfo);		
	}
	@Override
	public String exportWithTransformations(OntologyInfo ontoInfo,
			boolean copyAlsoSKOSXLabels, boolean copyAlsoReifiedDefinition)
			throws Exception {
		return refactorService.exportWithTransformations(ontoInfo, copyAlsoSKOSXLabels, copyAlsoReifiedDefinition);
	}
	
	
	
}