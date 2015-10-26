package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.owlart.io.RDFFormat;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.InputOutputResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.RefactorResponseManager;
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
	
	/**
	 * @param ontoInfo
	 * @param inputFile
	 * @param baseURI
	 * @param formatName
	 * @return
	 */
	public static boolean loadRDF(OntologyInfo ontoInfo, String inputFile, String baseURI, String fileFormat)
	{
		XMLResponseREPLY reply = InputOutputResponseManager.loadRDFRequest(ontoInfo, inputFile, baseURI, fileFormat);
		return reply.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static HashMap<String, String> getRDFFormat(OntologyInfo ontoInfo)
	{
		
		HashMap<String, String> list = new HashMap<String, String>();
		list.put(RDFFormat.RDFXML.getName(), "rdf");
		list.put(RDFFormat.RDFXML_ABBREV.getName(), "rdf");
		list.put(RDFFormat.NTRIPLES.getName(), "nt");
		list.put(RDFFormat.N3.getName(), "nt");
		list.put(RDFFormat.TRIG.getName(), "trig");
		list.put(RDFFormat.TRIX.getName(), "trix");
		list.put(RDFFormat.TRIXEXT.getName(), "trix-ext");
		list.put(RDFFormat.TURTLE.getName(), "ttl");
		list.put(RDFFormat.NQUADS.getName(), "nq");
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static String saveRDF(OntologyInfo ontoInfo, String ext, String fileFormat)
	{
		String filename = "";
		File tempfile;
		try {
			tempfile = STUtility.createTempFile(ext);
			String str = InputOutputResponseManager.saveRDFRequest(ontoInfo, ext, fileFormat, false);
			FileUtils.writeStringToFile(tempfile, str, "UTF-8");
			filename = tempfile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}
	
}
