package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.ResourceOld;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
/**
 * @author sachit
 *
 */
public class ResourceResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ResourceResponseManager.class);
	
	/**
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static XMLResponseREPLY getPropertyValuesCountRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getPropertyValuesCountRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.property, propertyURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static XMLResponseREPLY getPropertyValuesRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getPropertyValuesRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.property, propertyURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfPropertiesCountRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI, boolean subProperties, boolean excludePropItSelf, String excludedProps)
	{
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfPropertiesCountRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.properties, propertyURI), 
				STModel.par(ResourceOld.Par.subProp, subProperties?"true":"false"), 
				STModel.par(ResourceOld.Par.excludePropItSelf, excludePropItSelf?"true":"false"), 
				STModel.par(ResourceOld.Par.excludedProps, excludedProps), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURIs
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfPropertiesCountRequest(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> propertyURIs, boolean subProperties, boolean excludePropItSelf, String excludedProps)
	{
		String propURIs = STUtility.convertArrayToString(propertyURIs, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfPropertiesCountRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.properties, propURIs), 
				STModel.par(ResourceOld.Par.subProp, subProperties?"true":"false"), 
				STModel.par(ResourceOld.Par.excludePropItSelf, excludePropItSelf?"true":"false"), 
				STModel.par(ResourceOld.Par.excludedProps, excludedProps), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURIs
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfPropertiesCountRequest(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> propertyURIs)
	{
		String propURIs = STUtility.convertArrayToString(propertyURIs, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfPropertiesCountRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.properties, propURIs), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURIs
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfPropertiesRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURIs, boolean subProperties, boolean excludePropItSelf, String excludedProps)
	{
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfPropertiesRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.properties, propertyURIs), 
				STModel.par(ResourceOld.Par.subProp, subProperties?"true":"false"), 
				STModel.par(ResourceOld.Par.excludePropItSelf, excludePropItSelf?"true":"false"), 
				STModel.par(ResourceOld.Par.excludedProps, excludedProps), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURIs
	 * @param subProperties
	 * @param excludePropItSelf
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfPropertiesRequest(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> propertyURIs, boolean subProperties, boolean excludePropItSelf, String excludedProps)
	{
		String propURIs = STUtility.convertArrayToString(propertyURIs, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfPropertiesRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.properties, propURIs), 
				STModel.par(ResourceOld.Par.subProp, subProperties?"true":"false"), 
				STModel.par(ResourceOld.Par.excludePropItSelf, excludePropItSelf?"true":"false"), 
				STModel.par(ResourceOld.Par.excludedProps, excludedProps), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param resourceURI
	 * @param propertyURIs
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfPropertiesRequest(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> propertyURIs)
	{
		String propURIs = STUtility.convertArrayToString(propertyURIs, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfPropertiesRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.properties, propURIs), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param role
	 * @return
	 */
	public static XMLResponseREPLY getTemplatePropertiesRequest(OntologyInfo ontoInfo, String resourceURI, String role, String subPropOf, String notSubPropOf)
	{
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getTemplatePropertiesRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.role, role), 
				STModel.par(ResourceOld.Par.subPropOf, subPropOf), 
				STModel.par(ResourceOld.Par.notSubPropOf, notSubPropOf), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));			
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfDatatypePropertiesRequest(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> excludedProps)
	{
		String propURIs = STUtility.convertArrayToString(excludedProps, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfDatatypePropertiesRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.excludedProps, propURIs), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfAnnotationsPropertiesHierarchicallyRequest(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> excludedProps)
	{
		String excludedPropsStr = STUtility.convertArrayToString(excludedProps, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfAnnotationsPropertiesHierarchicallyRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.excludedProps, excludedPropsStr), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getValuesOfPlainRDFPropertiesRequest(OntologyInfo ontoInfo, String resourceURI, ArrayList<String> excludedProps)
	{
		String propURIs = STUtility.convertArrayToString(excludedProps, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(ResourceOld.Req.getValuesOfPlainRDFPropertiesRequest, 
				STModel.par(ResourceOld.Par.resource, resourceURI), 
				STModel.par(ResourceOld.Par.excludedProps, propURIs), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
}
