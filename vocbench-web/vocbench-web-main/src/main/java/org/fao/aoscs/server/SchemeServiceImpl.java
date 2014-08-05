package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.scheme.service.SchemeService;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemeServiceImpl extends PersistentRemoteService implements SchemeService {
	
	private static final long serialVersionUID = -8716000785148203270L;
	protected static Logger logger = LoggerFactory.getLogger(SchemeServiceImpl.class);
	private SchemeService schemeService;

	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		logger.info("starting ConceptService initialization");

		// Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;
		// End of Bean Manager initialization
		
		schemeService = new ModelManager().getSchemeService();
		logger.info("ConceptService initialized");
	}
	

	@Override
	public ArrayList<LabelObject> getSchemeLabel(OntologyInfo ontoInfo, String schemeURI)
			throws Exception {
		return schemeService.getSchemeLabel(ontoInfo, schemeURI);
	}

	@Override
	public boolean addSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			String label, String lang) throws Exception {
		return schemeService.addSchemeLabel(ontoInfo, schemeURI, label, lang);
	}

	@Override
	public boolean editSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			String label, String lang) throws Exception {
		return schemeService.editSchemeLabel(ontoInfo, schemeURI, label, lang);
	}
	
	@Override
	public boolean deleteSchemeLabel(OntologyInfo ontoInfo, String schemeURI,
			String label, String lang) throws Exception {
		return schemeService.deleteSchemeLabel(ontoInfo, schemeURI, label, lang);
	}


	@Override
	public boolean addScheme(OntologyInfo ontoInfo, String scheme,
			String label, String lang, String schemeLang) throws Exception {
		return schemeService.addScheme(ontoInfo, scheme, label, lang, schemeLang);
	}


	@Override
	public String deleteScheme(OntologyInfo ontoInfo, String scheme,
			boolean setForceDeleteDanglingConcepts,
			boolean forceDeleteDanglingConcepts) throws Exception {
		return schemeService.deleteScheme(ontoInfo, scheme, setForceDeleteDanglingConcepts, forceDeleteDanglingConcepts);
	}


	@Override
	public boolean setScheme(OntologyInfo ontoInfo, String scheme)
			throws Exception {
		return schemeService.setScheme(ontoInfo, scheme);
	}


	@Override
	public HashMap<String, String> getSchemes(OntologyInfo ontoInfo,
			String schemeLang) throws Exception {
		return schemeService.getSchemes(ontoInfo, schemeLang);
	}


}