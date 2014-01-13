package org.fao.aoscs.model.semanticturkey.util;

import it.uniroma2.art.owlart.models.conf.ModelConfiguration;
import it.uniroma2.art.semanticturkey.exceptions.STInitializationException;
import it.uniroma2.art.semanticturkey.ontology.OntologyManagerFactory;
import it.uniroma2.art.semanticturkey.plugin.PluginManager;
import it.uniroma2.art.semanticturkey.servlet.main.Administration;
import it.uniroma2.art.semanticturkey.servlet.main.Annotation;
import it.uniroma2.art.semanticturkey.servlet.main.Cls;
import it.uniroma2.art.semanticturkey.servlet.main.Delete;
import it.uniroma2.art.semanticturkey.servlet.main.Environment;
import it.uniroma2.art.semanticturkey.servlet.main.Individual;
import it.uniroma2.art.semanticturkey.servlet.main.InputOutput;
import it.uniroma2.art.semanticturkey.servlet.main.Metadata;
import it.uniroma2.art.semanticturkey.servlet.main.ModifyName;
import it.uniroma2.art.semanticturkey.servlet.main.OntManager;
import it.uniroma2.art.semanticturkey.servlet.main.OntoSearch;
import it.uniroma2.art.semanticturkey.servlet.main.Page;
import it.uniroma2.art.semanticturkey.servlet.main.Plugins;
import it.uniroma2.art.semanticturkey.servlet.main.Projects;
import it.uniroma2.art.semanticturkey.servlet.main.Property;
import it.uniroma2.art.semanticturkey.servlet.main.Resource;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;
import it.uniroma2.art.semanticturkey.servlet.main.SKOSXL;
import it.uniroma2.art.semanticturkey.servlet.main.SPARQL;
import it.uniroma2.art.semanticturkey.servlet.main.Statement;
import it.uniroma2.art.semanticturkey.servlet.main.Synonyms;
import it.uniroma2.art.semanticturkey.servlet.main.SystemStart;
import it.uniroma2.art.semanticturkey.utilities.XMLHelp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.fao.aims.aos.vocbench.services.Agrovoc;
import org.fao.aims.aos.vocbench.services.VOCBENCH;
import org.fao.aoscs.model.semanticturkey.STModelConstants;
import org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceDirectWrapper;
import org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceHttpWrapper;
import org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper;

public class STModel {

	protected static Log logger = LogFactory.getLog(STModel.class);

	private String accessType = "http";

	protected HttpClient httpclient;
	protected String stServerScheme;
	protected String stServerIP;
	protected int stServerPort;
	protected String stServerPath;

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
	public ServiceWrapper projectsService;
	public ServiceWrapper pluginsService;
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
	public void initialize(String stServerScheme, String stServerIP, int stServerPort, String stServerPath) throws STInitializationException, IOException {

		initModelConstants();

		/*this.accessType = ConfigConstants.STMETHOD;
		this.stServerScheme = ConfigConstants.STMETHOD;
		this.stServerIP = ConfigConstants.STSERVERIP;
		this.stServerPort = Integer.parseInt(ConfigConstants.STSERVERPORT);
		this.stServerPath = ConfigConstants.STSERVERPATH;*/
		
		this.stServerScheme = stServerScheme;
		this.stServerIP = stServerIP;
		this.stServerPort = stServerPort;
		this.stServerPath = stServerPath;
		
		if (accessType.equals("http")) {
			httpInitialize();
			initializeServiceHttpWrappers();
		} else if (accessType.equals("direct")) {
			directInitialize();
			initializeServiceDirectWrappers();
		} else
			throw new IllegalArgumentException(
					"ServiceTest need to be initialized either with \"http\" or \"direct\" argument");
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends OntologyManagerFactory<ModelConfiguration>> getOntologyManagerClass() {
		try {
			// I would have used the direct reference to the class, but
			// OntologyManagerFactorySesame2Impl.class is of type ...<? extends ModelConfiguration> and not
			// directly ...<ModelConfiguration>. I tried to change all the signatures to accept
			// <? extends ModelConfiguration> but I incurred in:
			// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
			// so shortest way is to leave everything as it is and change the reference here by using the
			// Class.forName method on a String, which is not resolved at compile time
			// ...damned generics..
			return (Class<? extends OntologyManagerFactory<ModelConfiguration>>) Class.forName("it.uniroma2.art.semanticturkey.ontology.sesame2.OntologyManagerFactorySesame2Impl");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @throws STInitializationException
	 * @throws IOException
	 */
	public void directInitialize() throws STInitializationException, IOException {
		try {
			XMLHelp.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// the PluginManager allows to be used in "direct access test mode", so that when invoked internally
		// by Semantic
		// Turkey, it will return instances of the OntologyManager class specified here instead of those
		// dynamically loaded through felix
		PluginManager.setTestOntManagerFactoryImpl(getOntologyManagerClass());
		PluginManager.setDirectAccessTest(true);
		
		// then the rest is done normally as of ST initialization, with the data inside test directory being
		// copied on its own to the SemanticTurkeyDataFolder if it does not exist (i.e. ST first install) or
		// left as it is it it exists
	}

	/**
	 * @throws IOException
	 */
	public void httpInitialize() throws IOException {
		try {
			XMLHelp.initialize();
			/*String extensionDir = new File(System.getProperty("user.dir")).toURI().toString();
			System.out.println("ext dir: " + extensionDir);
			SemanticTurkey.initialize(extensionDir);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
		cm.setDefaultMaxPerRoute(100);
		cm.setMaxTotal(200);
		
		logger.debug("MaxPerRoute/MaxTotal: "+cm.getDefaultMaxPerRoute()+"/"+ cm.getMaxTotal());
		
		httpclient = new DefaultHttpClient(cm);
	}
	
	// END OF INITIALIZE METHODS

	/**
	 * 
	 */
	protected void initializeServiceHttpWrappers() {
		administrationService = new ServiceHttpWrapper("administration", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		annotationService = new ServiceHttpWrapper("annotation", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		clsService = new ServiceHttpWrapper("cls", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		deleteService = new ServiceHttpWrapper("delete", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		environmentService = new ServiceHttpWrapper("environment", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		individualService = new ServiceHttpWrapper("individual", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		inputOutputService = new ServiceHttpWrapper("inputOutput", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		metadataService = new ServiceHttpWrapper("metadata", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		modifyNameService = new ServiceHttpWrapper("modifyName", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		pageService = new ServiceHttpWrapper("page", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		pluginsService = new ServiceHttpWrapper("plugins", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		propertyService = new ServiceHttpWrapper("property", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		projectsService = new ServiceHttpWrapper("projects", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		resourceService = new ServiceHttpWrapper("resource", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		searchOntologyService = new ServiceHttpWrapper("ontologySearch", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		synonymsService = new ServiceHttpWrapper("synonyms", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		systemStartService = new ServiceHttpWrapper("systemStart", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		sparqlService = new ServiceHttpWrapper("sparql", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		statementService = new ServiceHttpWrapper("statement", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		ontManagerService = new ServiceHttpWrapper("ontManager", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		skosService = new ServiceHttpWrapper("skos", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		skosXLService = new ServiceHttpWrapper("skosxl", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		vocbenchService = new ServiceHttpWrapper("vocbench", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
		agrovocService = new ServiceHttpWrapper("agrovoc", httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
	}

	/**
	 * 
	 */
	protected void initializeServiceDirectWrappers() {
		administrationService = new ServiceDirectWrapper(new Administration(""));
		annotationService = new ServiceDirectWrapper(new Annotation(""));
		clsService = new ServiceDirectWrapper(new Cls(""));
		deleteService = new ServiceDirectWrapper(new Delete(""));
		environmentService = new ServiceDirectWrapper(new Environment(""));
		individualService = new ServiceDirectWrapper(new Individual(""));
		inputOutputService = new ServiceDirectWrapper(new InputOutput(""));
		metadataService = new ServiceDirectWrapper(new Metadata(""));
		modifyNameService = new ServiceDirectWrapper(new ModifyName(""));
		pageService = new ServiceDirectWrapper(new Page(""));
		pluginsService = new ServiceDirectWrapper(new Plugins(""));
		propertyService = new ServiceDirectWrapper(new Property(""));
		projectsService = new ServiceDirectWrapper(new Projects(""));
		resourceService = new ServiceDirectWrapper(new Resource(""));
		searchOntologyService = new ServiceDirectWrapper(new OntoSearch(""));
		synonymsService = new ServiceDirectWrapper(new Synonyms(""));
		systemStartService = new ServiceDirectWrapper(new SystemStart(""));
		sparqlService = new ServiceDirectWrapper(new SPARQL(""));
		statementService = new ServiceDirectWrapper(new Statement(""));
		ontManagerService = new ServiceDirectWrapper(new OntManager(""));
		skosService = new ServiceDirectWrapper(new SKOS(""));
		skosXLService = new ServiceDirectWrapper(new SKOSXL(""));
		vocbenchService = new ServiceDirectWrapper(new VOCBENCH(""));
		agrovocService = new ServiceDirectWrapper(new Agrovoc(""));
	}

	/**
	 * @param par
	 * @param value
	 * @return
	 */
	public static ParameterPair par(String par, String value) {
		return new ParameterPair(par, value);
	}

	/**
	 * 
	 */
	public void pause() {
		try {
			System.out.println("press a key");
			System.in.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 */
	protected void tearDown() throws Exception {
		System.out.println("tearin' down the test");
		// repository.close(); //this reference is null!!! recheck the whole system to have good unit testing
		System.out.println("repository disposed");
		//PluginManager.stopFelix();
		System.out.println("Felix stopped: resources under SemanticTurkeyData directory have been released");
		System.out.println("test teared down");
	}

	/**
	 * @return
	 */
	public String getAccessType() {
		return accessType;
	}

}
