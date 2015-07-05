package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Metadata;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
/**
 * @author Sachit
 *
 */
public class MetadataResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(MetadataResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY getBaseuriRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.getBaseuriRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY getDefaultNamespaceRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.getDefaultNamespaceRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY getNSPrefixMappingsRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.getNSPrefixMappingsRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param prefix
	 * @param namespace
	 * @return
	 */
	public static XMLResponseREPLY setNSPrefixMappingRequest(OntologyInfo ontoInfo, String prefix, String namespace)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.setNSPrefixMappingRequest, 
				STModel.par(Metadata.prefixPar, prefix), 
				STModel.par(Metadata.namespacePar, namespace), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param prefix
	 * @param namespace
	 * @return
	 */
	public static XMLResponseREPLY changeNSPrefixMappingRequest(OntologyInfo ontoInfo, String prefix, String namespace)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.changeNSPrefixMappingRequest, 
				STModel.par(Metadata.prefixPar, prefix), 
				STModel.par(Metadata.namespacePar, namespace), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param namespace
	 * @return
	 */
	public static XMLResponseREPLY removeNSPrefixMappingRequest(OntologyInfo ontoInfo, String namespace)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.removeNSPrefixMappingRequest, 
				STModel.par(Metadata.namespacePar, namespace), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY getNamedGraphsRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.getNamedGraphsRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY getImportsRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.getImportsRequest, 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @return
	 */
	public static XMLResponseREPLY addFromWebRequest(OntologyInfo ontoInfo, String baseuri, String altURL)
	{
		Response resp;
		if(isNULL(altURL))
		{
			resp= getSTModel(ontoInfo).metadataService.makeRequest(Metadata.addFromWebRequest,
				STModel.par(Metadata.baseuriPar, baseuri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		}
		else
		{
			resp= getSTModel(ontoInfo).metadataService.makeRequest(Metadata.addFromWebRequest,
					STModel.par(Metadata.baseuriPar, baseuri),
					STModel.par(Metadata.alturlPar, altURL), 
					STModel.par("ctx_project", ontoInfo.getDbTableName()));
		}
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param ontFileName
	 * @return
	 */
	public static XMLResponseREPLY addFromWebToMirrorRequest(OntologyInfo ontoInfo, String baseuri, String mirrorFile, String altURL)
	{
		
		Response resp;
		if(isNULL(altURL))
		{
			resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.addFromWebToMirrorRequest,
				STModel.par(Metadata.baseuriPar, baseuri),
				STModel.par(Metadata.mirrorFilePar, mirrorFile), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		}
		else
		{
			resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.addFromWebToMirrorRequest,
				STModel.par(Metadata.baseuriPar, baseuri),
				STModel.par(Metadata.alturlPar, altURL),
				STModel.par(Metadata.mirrorFilePar, mirrorFile), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		}
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param localPath
	 * @param ontFileName
	 * @return
	 */
	public static XMLResponseREPLY addFromLocalFileRequest(OntologyInfo ontoInfo, String baseuri, String localFilePath, String mirrorFile)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.addFromLocalFileRequest,
				STModel.par(Metadata.baseuriPar, baseuri),
				STModel.par(Metadata.localFilePathPar, localFilePath),
				STModel.par(Metadata.mirrorFilePar, mirrorFile), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param ontFileName
	 * @return
	 */
	public static XMLResponseREPLY addFromOntologyMirrorRequest(OntologyInfo ontoInfo, String baseuri, String mirrorFile)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.addFromOntologyMirrorRequest,
				STModel.par(Metadata.baseuriPar, baseuri),
				STModel.par(Metadata.mirrorFilePar, mirrorFile), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @return
	 */
	public static XMLResponseREPLY removeImportRequest(OntologyInfo ontoInfo, String baseuri)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.removeImportRequest,
				STModel.par(Metadata.baseuriPar, baseuri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param ontFileName
	 * @return
	 */
	public static XMLResponseREPLY mirrorOntologyRequest(OntologyInfo ontoInfo, String baseuri, String ontFileName)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.mirrorOntologyRequest,
				STModel.par(Metadata.baseuriPar, baseuri),
				STModel.par(Metadata.mirrorFilePar, ontFileName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param defaultNS
	 * @return
	 */
	public static XMLResponseREPLY setDefaultNamespaceRequest(OntologyInfo ontoInfo, String defaultNS)
	{
		Response resp = getSTModel(ontoInfo).metadataService.makeRequest(Metadata.setDefaultNamespaceRequest,
				STModel.par(Metadata.namespacePar, defaultNS),
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
