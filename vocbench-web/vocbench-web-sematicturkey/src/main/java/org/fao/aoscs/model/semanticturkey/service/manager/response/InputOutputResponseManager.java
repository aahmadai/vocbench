package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.InputOutputOld;

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
	public static XMLResponseREPLY loadRDFRequest(OntologyInfo ontoInfo, String inputFile, String baseURI, String fileFormat)
	{
		Response resp = getSTModel(ontoInfo).inputOutputService.makeRequest(InputOutputOld.loadRDFRequest, 
				STModel.par(InputOutputOld.filePar, inputFile), 
				STModel.par(InputOutputOld.baseUriPar, baseURI), 
				STModel.par(InputOutputOld.formatPar, fileFormat), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param outputFile
	 * @param allNGsPar
	 * @return
	 */
	public static XMLResponseREPLY saveRDFRequest(OntologyInfo ontoInfo, String outputFile, String fileFormat, Boolean allNGsPar)
	{
		Response resp = getSTModel(ontoInfo).inputOutputService.makeRequest(InputOutputOld.saveRDFRequest, 
				STModel.par(InputOutputOld.filePar, outputFile), 
				STModel.par(InputOutputOld.formatPar, fileFormat), 
				STModel.par(InputOutputOld.allNGsPar, allNGsPar.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
}