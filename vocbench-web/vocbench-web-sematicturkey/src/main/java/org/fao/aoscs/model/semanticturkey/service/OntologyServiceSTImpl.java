package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ImportPathObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OntologyMirror;
import org.fao.aoscs.model.semanticturkey.service.manager.AdministrationManager;
import org.fao.aoscs.model.semanticturkey.service.manager.MetadataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author rajbhandari
 *
 */
public class OntologyServiceSTImpl {
	
	protected static Logger logger = LoggerFactory.getLogger(OntologyServiceSTImpl.class);

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getNSPrefixMappings()
	 */
	public HashMap<String, String> getNSPrefixMappings(OntologyInfo ontoInfo) {
		return MetadataManager.getNSPrefixMappings(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getImports(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ImportPathObject getImports(OntologyInfo ontoInfo) {
		return MetadataManager.getImports(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getBaseuri(org.fao.aoscs.domain.OntologyInfo)
	 */
	public String getBaseuri(OntologyInfo ontoInfo) {
		return MetadataManager.getBaseuri(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getDefaultNamespace(org.fao.aoscs.domain.OntologyInfo)
	 */
	public String getDefaultNamespace(OntologyInfo ontoInfo) {
		return MetadataManager.getDefaultNamespace(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addNSPrefixMapping(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean addNSPrefixMapping(OntologyInfo ontoInfo, String namespace,
			String prefix) {
		return MetadataManager.setNSPrefixMappingRequest(ontoInfo, prefix, namespace);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#editNSPrefixMapping(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean editNSPrefixMapping(OntologyInfo ontoInfo, String namespace,
			String prefix) {
		return MetadataManager.changeNSPrefixMappingRequest(ontoInfo, prefix, namespace);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#deleteNSPrefixMapping(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean deleteNSPrefixMapping(OntologyInfo ontoInfo, String namespace) {
		return MetadataManager.removeNSPrefixMappingRequest(ontoInfo, namespace);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getNamedGraphs(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<String> getNamedGraphs(OntologyInfo ontoInfo)
			throws Exception {
		return MetadataManager.getNamedGraphs(ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromWeb(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean addFromWeb(OntologyInfo ontoInfo, String baseuri, String altURL) {
		return MetadataManager.addFromWeb(ontoInfo, baseuri, altURL);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromWebToMirror(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean addFromWebToMirror(OntologyInfo ontoInfo, String baseuri,
			String mirrorFile, String altURL) {
		return MetadataManager.addFromWebToMirror(ontoInfo, baseuri, mirrorFile, altURL);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromLocalFile(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Boolean addFromLocalFile(OntologyInfo ontoInfo, String baseuri,
			String localFilePath, String mirrorFile) {
		return MetadataManager.addFromLocalFile(ontoInfo, baseuri, localFilePath, mirrorFile);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#addFromOntologyMirror(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean addFromOntologyMirror(OntologyInfo ontoInfo, String baseuri,
			String mirrorFile) {
		return MetadataManager.addFromOntologyMirror(ontoInfo, baseuri, mirrorFile);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#removeImport(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public Boolean removeImport(OntologyInfo ontoInfo, String baseuri) {
		return MetadataManager.removeImport(ontoInfo, baseuri);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#mirrorOntology(org.fao.aoscs.domain.OntologyInfo, java.lang.String, java.lang.String)
	 */
	public Boolean mirrorOntology(OntologyInfo ontoInfo, String baseuri, String ontFileName) {
		return MetadataManager.mirrorOntology(ontoInfo, baseuri, ontFileName);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.ontology.service.OntologyService#getOntologyMirror(org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<OntologyMirror> getOntologyMirror(OntologyInfo ontoInfo) {
		return AdministrationManager.getOntologyMirror(ontoInfo);
	}
}
