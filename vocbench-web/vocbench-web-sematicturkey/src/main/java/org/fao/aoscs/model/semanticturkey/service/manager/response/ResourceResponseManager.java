package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Resource;

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
public class ResourceResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ResourceResponseManager.class);
	
	/**
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static XMLResponseREPLY getPropertyValuesCountRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getPropertyValuesCountRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.property, propertyURI));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static XMLResponseREPLY getPropertyValuesRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getPropertyValuesRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.property, propertyURI));
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getValuesOfPropertiesCountRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.properties, propertyURI), STModel.par(Resource.Par.subProp, subProperties?"true":"false"), STModel.par(Resource.Par.excludePropItSelf, excludePropItSelf?"true":"false"), STModel.par(Resource.Par.excludedProps, excludedProps));
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getValuesOfPropertiesCountRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.properties, propURIs), STModel.par(Resource.Par.subProp, subProperties?"true":"false"), STModel.par(Resource.Par.excludePropItSelf, excludePropItSelf?"true":"false"), STModel.par(Resource.Par.excludedProps, excludedProps));
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getValuesOfPropertiesCountRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.properties, propURIs));
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getValuesOfPropertiesRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.properties, propertyURIs), STModel.par(Resource.Par.subProp, subProperties?"true":"false"), STModel.par(Resource.Par.excludePropItSelf, excludePropItSelf?"true":"false"), STModel.par(Resource.Par.excludedProps, excludedProps));
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getValuesOfPropertiesRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.properties, propURIs), STModel.par(Resource.Par.subProp, subProperties?"true":"false"), STModel.par(Resource.Par.excludePropItSelf, excludePropItSelf?"true":"false"), STModel.par(Resource.Par.excludedProps, excludedProps));
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getValuesOfPropertiesRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.properties, propURIs));
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getTemplatePropertiesRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.role, role), STModel.par(Resource.Par.subPropOf, subPropOf), STModel.par(Resource.Par.notSubPropOf, notSubPropOf));			
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
		Response resp = getSTModel(ontoInfo).resourceService.makeRequest(Resource.Req.getValuesOfDatatypePropertiesRequest, STModel.par(Resource.Par.resource, resourceURI), STModel.par(Resource.Par.excludedProps, propURIs));
		return getXMLResponseREPLY(resp);
	}
	
	
	
	
}
