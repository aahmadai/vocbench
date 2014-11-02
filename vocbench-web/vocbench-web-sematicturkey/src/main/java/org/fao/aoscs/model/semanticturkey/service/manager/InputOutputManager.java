package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.io.IOException;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.InputOutputResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class InputOutputManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(InputOutputManager.class);
	
	public static boolean loadRDF(OntologyInfo ontoInfo, String inputFile, String baseURI, String formatName)
	{
		XMLResponseREPLY reply = InputOutputResponseManager.loadRDFRequest(ontoInfo, inputFile, baseURI, formatName);
		return reply.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param outputFile
	 * @param allNGsPar
	 * @return
	 */
	public static String saveRDF(OntologyInfo ontoInfo)
	{
		String content = "";
		try {
			content = STUtility.createTempFile().getPath();
			InputOutputResponseManager.saveRDFRequest(ontoInfo, content, false);
		} catch (IOException e) {
		}
		return content;
	}
	
}
