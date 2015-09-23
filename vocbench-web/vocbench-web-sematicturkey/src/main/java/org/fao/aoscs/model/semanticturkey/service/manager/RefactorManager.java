package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.io.File;
import java.io.IOException;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.RefactorResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;


/**
 * @author rajbhandari
 *
 */
public class RefactorManager extends ResponseManager {

	
	/**
	 * @param ontoInfo
	 * @param oldResource
	 * @param newResource
	 */
	public static Boolean renameResource(OntologyInfo ontoInfo, String oldResource, String newResource)
	{
		XMLResponseREPLY reply = RefactorResponseManager.renameResourceRequest(ontoInfo, oldResource, newResource);
		return  reply.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param sourceBaseURI
	 * @param targetBaseURI
	 * @param graphArrayString
	 */
	public static Boolean replaceBaseURI(OntologyInfo ontoInfo, String sourceBaseURI, String targetBaseURI, String graphArrayString)
	{
		
		XMLResponseREPLY reply = RefactorResponseManager.replaceBaseURIRequest(ontoInfo, sourceBaseURI, targetBaseURI, graphArrayString);
		return reply.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 */
	public static Boolean convertLabelsToSKOSXL(OntologyInfo ontoInfo)
	{
		
		XMLResponseREPLY reply = RefactorResponseManager.convertLabelsToSKOSXLRequest(ontoInfo);
		return reply.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 */
	public static Boolean reifySKOSDefinitions(OntologyInfo ontoInfo)
	{
		XMLResponseREPLY reply = RefactorResponseManager.reifySKOSDefinitionsRequest(ontoInfo);
		return reply.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param format
	 * @param ext
	 * @param toSKOS
	 * @param keepSKOSXLabels
	 * @param toFlatDefinitions
	 * @param keepReifiedDefinition
	 * @return
	 */
	public static String exportByFlattening(OntologyInfo ontoInfo, String format, String ext, Boolean toSKOS, Boolean keepSKOSXLabels,
			Boolean toFlatDefinitions, Boolean keepReifiedDefinition)
	{
		String filename = "";
		File tempfile;
		try {
			tempfile = STUtility.createTempFile();
			filename = tempfile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!filename.equals(""))
		{
			XMLResponseREPLY reply = RefactorResponseManager.exportByFlatteningRequest(ontoInfo, format, ext, toSKOS, keepSKOSXLabels, toFlatDefinitions, keepReifiedDefinition);
			if(reply!=null && reply.isAffirmative())
				return filename;
		}
		return "";
	}
	
	/**
	 * @param ontoInfo
	 */
	public static String exportWithSKOSLabels(OntologyInfo ontoInfo)
	{
		String filename = "";
		File tempfile;
		try {
			tempfile = STUtility.createTempFile();
			filename = tempfile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!filename.equals(""))
		{
			XMLResponseREPLY reply = RefactorResponseManager.exportWithSKOSLabelsRequest(ontoInfo, filename);
			if(reply!=null && reply.isAffirmative())
				return filename;
		}
		return "";
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static String exportWithFlatSKOSDefinitions(OntologyInfo ontoInfo)
	{
		String filename = "";
		File tempfile;
		try {
			tempfile = STUtility.createTempFile();
			filename = tempfile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!filename.equals(""))
		{
			XMLResponseREPLY reply = RefactorResponseManager.exportWithFlatSKOSDefinitionsRequest(ontoInfo, filename);
			if(reply!=null && reply.isAffirmative())
				return filename;
		}
		return "";
	}
	
	/**
	 * @param ontoInfo
	 * @param copyAlsoSKOSXLabels
	 * @param copyAlsoReifiedDefinition
	 * @return
	 * @throws Exception
	 */
	public static String exportWithTransformations(OntologyInfo ontoInfo, boolean copyAlsoSKOSXLabels, boolean copyAlsoReifiedDefinition) throws Exception {
		String filename = "";
		File tempfile;
		try {
			tempfile = STUtility.createTempFile();
			filename = tempfile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!filename.equals(""))
		{
			XMLResponseREPLY reply = RefactorResponseManager.exportWithTransformations(ontoInfo, filename, copyAlsoSKOSXLabels, copyAlsoReifiedDefinition);
			if(reply!=null && reply.isAffirmative())
				return filename;
		}
		return "";
	}
	
}
