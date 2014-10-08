/**
 * 
 */
package org.fao.aoscs.server;

import java.util.ArrayList;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.icv.service.ICVService;
import org.fao.aoscs.domain.DanglingConceptObject;
import org.fao.aoscs.domain.ManagedDanglingConceptObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

/**
 * @author rajbhandari
 *
 */
public class ICVServiceImpl extends PersistentRemoteService implements ICVService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3484207893405834199L;
	/**
	 * 
	 */
	private ICVService icvService;
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
		
		icvService = new ModelManager().getICVService();
	}
	@Override
	public ArrayList<DanglingConceptObject> listDanglingConcepts(
			OntologyInfo ontoInfo) throws Exception {
		return icvService.listDanglingConcepts(ontoInfo);
	}
	@Override
	public void manageDanglingConcepts(OntologyInfo ontoInfo,
			ArrayList<ManagedDanglingConceptObject> list) throws Exception {
		icvService.manageDanglingConcepts(ontoInfo, list);
		
	}
	
	
}