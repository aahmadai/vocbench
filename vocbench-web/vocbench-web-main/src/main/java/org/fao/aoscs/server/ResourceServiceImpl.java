/**
 * 
 */
package org.fao.aoscs.server;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.resourceview.service.ResourceService;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.ResourceViewObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

/**
 * @author rajbhandari
 *
 */
public class ResourceServiceImpl extends PersistentRemoteService implements ResourceService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1178332188179777473L;
	private ResourceService resourceService;
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
		
		resourceService = new ModelManager().getResourceService();
	}
	
	@Override
	public ResourceViewObject getResourceView(OntologyInfo ontoInfo, String resourceURI, boolean showOnlyExplicit) throws Exception {
		return resourceService.getResourceView(ontoInfo, resourceURI, showOnlyExplicit);
	}
}