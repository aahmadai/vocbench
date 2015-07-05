package org.fao.aoscs.server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.ontology.service.OntologyService;
import org.fao.aoscs.domain.ImportPathObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OntologyMirror;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

/**
 * @author rajbhandari
 *
 */
public class OntologyServiceImpl extends PersistentRemoteService implements OntologyService {

	private static final long serialVersionUID = 5838431084624026062L;
	private OntologyService ontologyService;
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
		
		ontologyService = new ModelManager().getOntologyService();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getNSPrefixMappings()
	 */
	public HashMap<String, String> getNSPrefixMappings(OntologyInfo ontoInfo) throws Exception{
		return ontologyService.getNSPrefixMappings(ontoInfo);	
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getImports(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ImportPathObject getImports(OntologyInfo ontoInfo) throws Exception{
		return ontologyService.getImports(ontoInfo);	
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getBaseuri(org.fao.aoscs.domain.OntologyInfo)
	 */
	public String getBaseuri(OntologyInfo ontoInfo) throws Exception{
		return ontologyService.getBaseuri(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getDefaultNamespace(org.fao.aoscs.domain.OntologyInfo)
	 */
	public String getDefaultNamespace(OntologyInfo ontoInfo) throws Exception{
		return ontologyService.getDefaultNamespace(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addNSPrefixMapping(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean addNSPrefixMapping(OntologyInfo ontoInfo, String namespace,
			String prefix) throws Exception{
		return ontologyService.addNSPrefixMapping(ontoInfo, namespace, prefix);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#editNSPrefixMapping(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean editNSPrefixMapping(OntologyInfo ontoInfo, String namespace,
			String prefix) throws Exception{
		return ontologyService.editNSPrefixMapping(ontoInfo, namespace, prefix);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#deleteNSPrefixMapping(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean deleteNSPrefixMapping(OntologyInfo ontoInfo, String namespace) throws Exception{
		return ontologyService.deleteNSPrefixMapping(ontoInfo, namespace);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getNamedGraphs(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<String> getNamedGraphs(OntologyInfo ontoInfo)
			throws Exception {
		return ontologyService.getNamedGraphs(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromWeb(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean addFromWeb(OntologyInfo ontoInfo, String baseuri, String altURL) throws Exception{
		return ontologyService.addFromWeb(ontoInfo, baseuri, altURL);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromWebToMirror(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean addFromWebToMirror(OntologyInfo ontoInfo, String baseuri, String mirrorFile, String altURL) throws Exception{
		return ontologyService.addFromWebToMirror(ontoInfo, baseuri, mirrorFile, altURL);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromLocalFile(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Boolean addFromLocalFile(OntologyInfo ontoInfo, String baseuri,
			String localFilePath, String mirrorFile) throws Exception{
		return ontologyService.addFromLocalFile(ontoInfo, baseuri, localFilePath, mirrorFile);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromOntologyMirror(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean addFromOntologyMirror(OntologyInfo ontoInfo, String baseuri,
			String mirrorFile) throws Exception{
		return ontologyService.addFromOntologyMirror(ontoInfo, baseuri, mirrorFile);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#removeImport(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean removeImport(OntologyInfo ontoInfo, String baseuri) throws Exception{
		return ontologyService.removeImport(ontoInfo, baseuri);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#mirrorOntology(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean mirrorOntology(OntologyInfo ontoInfo, String baseuri,
			String ontFileName) throws Exception{
		return ontologyService.mirrorOntology(ontoInfo, baseuri, ontFileName);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getOntologyMirror(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<OntologyMirror> getOntologyMirror(OntologyInfo ontoInfo) throws Exception{
		return ontologyService.getOntologyMirror(ontoInfo);
	}

	@Override
	public Boolean setDefaultNamespace(OntologyInfo ontoInfo, String defaultNS)
			throws Exception {
		return ontologyService.setDefaultNamespace(ontoInfo, defaultNS);
	}

	@Override
	public Boolean setBaseURIandDefaultNamespace(OntologyInfo ontoInfo,
			String baseURI, String defaultNS) throws Exception {
		return ontologyService.setBaseURIandDefaultNamespace(ontoInfo, baseURI, defaultNS);
	}

}