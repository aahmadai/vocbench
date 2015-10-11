package org.fao.aoscs.model.semanticturkey.util;

import it.uniroma2.art.semanticturkey.exceptions.STInitializationException;
import it.uniroma2.art.semanticturkey.utilities.XMLHelp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fao.aoscs.model.semanticturkey.STModelConstants;
import org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceHttpWrapper;
import org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper;

public class STModel {

	protected static Log logger = LogFactory.getLog(STModel.class);

	public ServiceWrapper administrationService;
	public ServiceWrapper annotationService;
	public ServiceWrapper clsService;
	public ServiceWrapper deleteService;
	public ServiceWrapper individualService;
	public ServiceWrapper inputOutputService;
	public ServiceWrapper metadataService;
	public ServiceWrapper modifyNameService;
	public ServiceWrapper pageService;
	public ServiceWrapper propertyService;
	public ServiceWrapper searchOntologyService;
	public ServiceWrapper synonymsService;
	public ServiceWrapper systemStartService;
	public ServiceWrapper projectsOldService;
	public ServiceWrapper projectsService;
	public ServiceWrapper pluginsService;
	public ServiceWrapper resourceViewService;
	public ServiceWrapper icvService;
	public ServiceWrapper refactorService;
	public ServiceWrapper resourceService;
	public ServiceWrapper sparqlService;
	public ServiceWrapper statementService;
	public ServiceWrapper environmentService;
	public ServiceWrapper ontManagerService;
	public ServiceWrapper skosService;
	public ServiceWrapper skosXLService;
	public ServiceWrapper vocbenchService;
	public ServiceWrapper agrovocService;

	/**
	 * @param type
	 * @throws STInitializationException
	 * @throws IOException
	 */
	public STModel(String stURL) {
		initModelConstants();
		httpInitialize();
		initializeServiceHttpWrappers(stURL);
	}

	// INITIALIZE METHODS
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#initModelConstants()
	 */
	public void initModelConstants() {
		HashMap<String, String> mcMap = new HashMap<String, String>();
		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration("STModelConstants.properties");
			Iterator<?> it = config.getKeys();
			while(it.hasNext())
			{
				String key = (String) it.next();
				mcMap.put(key, config.getString(key));
			}
		} catch (ConfigurationException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		STModelConstants.loadConstants(mcMap);
	}

	/**
	 * @throws IOException
	 */
	public void httpInitialize() {
		try {
			XMLHelp.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// END OF INITIALIZE METHODS

	/**
	 * 
	 */
	protected void initializeServiceHttpWrappers(String stURL) {
		administrationService = new ServiceHttpWrapper("administration", stURL);
		annotationService = new ServiceHttpWrapper("annotation", stURL);
		clsService = new ServiceHttpWrapper("cls", stURL);
		deleteService = new ServiceHttpWrapper("delete", stURL);
		environmentService = new ServiceHttpWrapper("environment", stURL);
		individualService = new ServiceHttpWrapper("individual", stURL);
		inputOutputService = new ServiceHttpWrapper("InputOutput", stURL);
		metadataService = new ServiceHttpWrapper("metadata", stURL);
		modifyNameService = new ServiceHttpWrapper("modifyName", stURL);
		pageService = new ServiceHttpWrapper("page", stURL);
		pluginsService = new ServiceHttpWrapper("Plugins", stURL);
		propertyService = new ServiceHttpWrapper("property", stURL);
		projectsOldService = new ServiceHttpWrapper("projects", stURL);
		projectsService = new ServiceHttpWrapper("Projects", stURL);
		resourceService = new ServiceHttpWrapper("resource", stURL);
		resourceViewService = new ServiceHttpWrapper("ResourceView", stURL);
		icvService = new ServiceHttpWrapper("ICV", stURL);
		refactorService = new ServiceHttpWrapper("Refactor", stURL);
		searchOntologyService = new ServiceHttpWrapper("ontologySearch", stURL);
		synonymsService = new ServiceHttpWrapper("synonyms", stURL);
		systemStartService = new ServiceHttpWrapper("systemStart", stURL);
		sparqlService = new ServiceHttpWrapper("sparql", stURL);
		statementService = new ServiceHttpWrapper("statement", stURL);
		ontManagerService = new ServiceHttpWrapper("ontManager", stURL);
		skosService = new ServiceHttpWrapper("skos", stURL);
		skosXLService = new ServiceHttpWrapper("skosxl", stURL);
		vocbenchService = new ServiceHttpWrapper("vocbench", stURL);
		agrovocService = new ServiceHttpWrapper("agrovoc", stURL);
	}

	/**
	 * @param par
	 * @param value
	 * @return
	 */
	public static ParameterPair par(String par, String value) {
		return new ParameterPair(par, value);
	}


}
