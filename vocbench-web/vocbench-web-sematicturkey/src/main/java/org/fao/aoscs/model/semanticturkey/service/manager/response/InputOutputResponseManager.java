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
	public static String saveRDFRequest(OntologyInfo ontoInfo, String ext, String fileFormat, Boolean allNGsPar)
	{
		String responseString = getSTModel(ontoInfo).inputOutputService.makeHttpRequest("saveRDF", 
				STModel.par("ext", ext), 
				STModel.par("format", fileFormat), 
				STModel.par("allNGs", allNGsPar.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return responseString;
	}
}