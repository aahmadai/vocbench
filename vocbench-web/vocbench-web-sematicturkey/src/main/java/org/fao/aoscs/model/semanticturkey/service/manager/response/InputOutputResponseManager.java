package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.InputOutput;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class InputOutputResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(InputOutputResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @param inputFile
	 * @param baseURI
	 * @param formatName
	 * @return
	 */
	public static XMLResponseREPLY loadRDFRequest(OntologyInfo ontoInfo, String inputFile, String baseURI, String formatName)
	{
		Response resp = getSTModel(ontoInfo).inputOutputService.makeRequest(InputOutput.loadRDFRequest, 
				STModel.par(InputOutput.filePar, inputFile), 
				STModel.par(InputOutput.baseUriPar, baseURI), 
				STModel.par(InputOutput.formatPar, formatName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param outputFile
	 * @param allNGsPar
	 * @return
	 */
	public static XMLResponseREPLY saveRDFRequest(OntologyInfo ontoInfo, String outputFile, Boolean allNGsPar)
	{
		Response resp = getSTModel(ontoInfo).inputOutputService.makeRequest(InputOutput.saveRDFRequest, 
				STModel.par(InputOutput.filePar, outputFile), 
				STModel.par(InputOutput.allNGsPar, allNGsPar.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
}