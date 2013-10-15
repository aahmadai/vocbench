package org.fao.aoscs.model.semanticturkey.util;

import it.uniroma2.art.semanticturkey.exceptions.STInitializationException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fao.aoscs.domain.OntologyInfo;

/**
 * @author rajbhandari
 *
 */
public class STModelFactory {

	protected static Log logger = LogFactory.getLog(STModelFactory.class);
	static HashMap<String, STModel> stModelsMap;
	static {
		stModelsMap = new HashMap<String, STModel>();
	}
	
	
	public static synchronized STModel getSTModel(OntologyInfo ontoInfo) {
		STModel stModel = stModelsMap.get(ontoInfo.getModelID());
		if (stModel==null) {
			stModel = createSTModel(ontoInfo);	
			stModelsMap.put(ontoInfo.getModelID(), stModel);
		}
		return stModel;
	}
	
	/**
	 * @return
	 */
	public static synchronized STModel createSTModel(OntologyInfo ontoInfo) {
		STModel stModel = new STModel();
		try {
			URL aURL = new URL(ontoInfo.getDbDriver());
			stModel.initialize(aURL.getProtocol(), aURL.getHost(), aURL.getPort(), aURL.getPath());
		} catch (STInitializationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
		return stModel;
	}
	
}
