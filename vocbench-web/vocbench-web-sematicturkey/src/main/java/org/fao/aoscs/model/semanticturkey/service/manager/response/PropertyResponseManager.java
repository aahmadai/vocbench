package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Property;
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
public class PropertyResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(PropertyResponseManager.class);
	
	/**
	 * @param propertyRequest
	 * @return
	 */
	public static XMLResponseREPLY getPropertiesTreeRequest(OntologyInfo ontoInfo, String propertyRequest)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(propertyRequest, 
				STModel.par(Property.Par.inferencePar, "true"), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propertyName
	 * @return
	 */
	public static XMLResponseREPLY getPropertyDescriptionRequest(OntologyInfo ontoInfo, String propertyURI)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.propertyDescriptionRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyURI), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}

	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @return
	 */
	public static XMLResponseREPLY addExternalPropValueRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addExternalPropValueRequest, 
				STModel.par(Property.Par.instanceQNamePar, resourceURI), 
				STModel.par(Property.Par.propertyQNamePar, propertyURI),
				STModel.par(Property.Par.valueField, value),
				STModel.par(Property.Par.type, RDFTypesEnum.uri.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param lang
	 * @param range
	 * @param type
	 * @return
	 */
	public static XMLResponseREPLY createAndAddPropValueRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String lang, String range, String type)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.createAndAddPropValueRequest, 
				STModel.par(Property.Par.instanceQNamePar, resourceURI), 
				STModel.par(Property.Par.propertyQNamePar, propertyURI),
				STModel.par(Property.Par.valueField, value),
				STModel.par(Property.Par.langField, lang),
				STModel.par(Property.Par.rangeQNamePar, range),
				STModel.par(Property.Par.type, type), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param range
	 * @param type
	 * @return
	 */
	public static XMLResponseREPLY createAndAddPropValueRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String range, String type)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.createAndAddPropValueRequest, 
				STModel.par(Property.Par.instanceQNamePar, resourceURI), 
				STModel.par(Property.Par.propertyQNamePar, propertyURI),
				STModel.par(Property.Par.valueField, value),
				STModel.par(Property.Par.rangeQNamePar, range),
				STModel.par(Property.Par.type, type), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param lang
	 * @param range
	 * @param type
	 * @return
	 */
	public static XMLResponseREPLY addExistingPropValueRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String uri)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addExistingPropValueRequest, 
				STModel.par(Property.Par.instanceQNamePar, resourceURI), 
				STModel.par(Property.Par.propertyQNamePar, propertyURI),
				STModel.par(Property.Par.valueField, uri),
				STModel.par(Property.Par.type, RDFTypesEnum.uri.toString()), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param oldValue
	 * @param range
	 * @param oldRange
	 * @param type
	 * @param oldType
	 * @param lang
	 * @param oldLang
	 * @return
	 */
	public static XMLResponseREPLY updatePropValueRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String oldValue, String range, String oldRange, String lang, String oldLang, String type, String oldType)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.updatePropValueRequest, 
				STModel.par(Property.Par.instanceQNamePar, resourceURI), 
				STModel.par(Property.Par.propertyQNamePar, propertyURI),
				STModel.par(Property.Par.rangeQNamePar, range),
				STModel.par(Property.Par.oldRangeQNamePar, oldRange),
				STModel.par(Property.Par.valueField, value),
				STModel.par(Property.Par.oldValueField, oldValue),
				STModel.par(Property.Par.langField, lang),
				STModel.par(Property.Par.oldLangField, oldLang),
				STModel.par(Property.Par.type, type),
				STModel.par(Property.Par.oldType, oldType), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param lang
	 * @param range
	 * @param type
	 * @return
	 */
	public static XMLResponseREPLY removePropValueRequest(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String lang, String range, String type)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.removePropValueRequest, 
				STModel.par(Property.Par.instanceQNamePar, resourceURI), 
				STModel.par(Property.Par.propertyQNamePar, propertyURI),
				STModel.par(Property.Par.valueField, value),
				STModel.par(Property.Par.langField, lang),
				STModel.par(Property.Par.rangeQNamePar, range),
				STModel.par(Property.Par.type, type), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propertyUri
	 * @param propertyType
	 * @param superPropertyUri
	 * @return
	 */
	public static XMLResponseREPLY addPropertyRequest(OntologyInfo ontoInfo, String propertyUri, String propertyType, String superPropertyUri)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addPropertyRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par("propertyType", propertyType), 
				STModel.par("superPropertyQName", superPropertyUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propertyUri
	 * @param propertyType
	 * @return
	 */
	public static XMLResponseREPLY addTopPropertyRequest(OntologyInfo ontoInfo, String propertyUri, String propertyType)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addPropertyRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par("propertyType", propertyType), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propertyUri
	 * @param domainUri
	 * @return
	 */
	public static XMLResponseREPLY addPropertyDomainRequest(OntologyInfo ontoInfo, String propertyUri, String domainUri)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addPropertyDomainRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par(Property.Par.domainPropertyQNamePar, domainUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propertyUri
	 * @param domainUri
	 * @return
	 */
	public static XMLResponseREPLY removePropertyDomainRequest(OntologyInfo ontoInfo, String propertyUri, String domainUri)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.removePropertyDomainRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par(Property.Par.domainPropertyQNamePar, domainUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propertyUri
	 * @param rangeUri
	 * @return
	 */
	public static XMLResponseREPLY addPropertyRangeRequest(OntologyInfo ontoInfo, String propertyUri, String rangeUri)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addPropertyRangeRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par("rangePropertyQName", rangeUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param propertyUri
	 * @param rangeUri
	 * @return
	 */
	public static XMLResponseREPLY removePropertyRangeRequest(OntologyInfo ontoInfo, String propertyUri, String rangeUri)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.removePropertyRangeRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par("rangePropertyQName", rangeUri), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static XMLResponseREPLY setDataRangeRequest(OntologyInfo ontoInfo, String propertyUri, String values)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.setDataRangeRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par(Property.Par.valuesField, values), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static XMLResponseREPLY addValueToDatarangeRequest(OntologyInfo ontoInfo, String dataRange, String value)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addValueToDatarangeRequest, 
				STModel.par(Property.Par.dataRangePar, dataRange), 
				STModel.par(Property.Par.valueField, value), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static XMLResponseREPLY addValuesToDatarangeRequest(OntologyInfo ontoInfo, String dataRange, String values)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.addValuesToDatarangeRequest, 
				STModel.par(Property.Par.dataRangePar, dataRange), 
				STModel.par(Property.Par.valuesField, values), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static XMLResponseREPLY removeValueFromDatarangeRequest(OntologyInfo ontoInfo, String dataRange, String value)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.removeValueFromDatarangeRequest, 
				STModel.par(Property.Par.dataRangePar, dataRange), 
				STModel.par(Property.Par.valueField, value), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param dataRange
	 * @param nodeType
	 * @return
	 */
	public static XMLResponseREPLY parseDataRangeRequest(OntologyInfo ontoInfo, String dataRange, String nodeType)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.parseDataRangeRequest, 
				STModel.par(Property.Par.dataRangePar, dataRange), 
				STModel.par(Property.Par.nodeTypePar, nodeType), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param visualize
	 * @return
	 */
	public static XMLResponseREPLY getRangeRequest(OntologyInfo ontoInfo, String propertyUri, boolean visualize)
	{
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(
				Property.Req.getRangeRequest, 
				STModel.par(Property.Par.propertyQNamePar, propertyUri), 
				STModel.par(Property.Par.visualize, visualize?"true":"false"), 
				STModel.par("ctx_project", ontoInfo.getDbTableName())
				);
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param classes
	 * @param role
	 * @param subPropOf
	 * @param notSubPropOf
	 * @return
	 */
	public static XMLResponseREPLY getPropertiesForDomainsRequest(OntologyInfo ontoInfo, ArrayList<String> classes, String role, ArrayList<String> subPropOf, ArrayList<String> notSubPropOf)
	{
		String classesStr = STUtility.convertArrayToString(classes, STXMLUtility.ST_SEPARATOR);
		String subPropOfStr = STUtility.convertArrayToString(subPropOf, STXMLUtility.ST_SEPARATOR);
		String notSubPropOfStr = STUtility.convertArrayToString(notSubPropOf, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(Property.Req.getPropertiesForDomainsRequest, 
				STModel.par(Property.Par.classes, classesStr), 
				STModel.par(ResourceOld.Par.role, role), 
				STModel.par(ResourceOld.Par.subPropOf, subPropOfStr), 
				STModel.par(ResourceOld.Par.notSubPropOf, notSubPropOfStr), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));			
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param role
	 * @param subPropOf
	 * @param notSubPropOf
	 * @return
	 */
	public static XMLResponseREPLY getPropertyListRequest(OntologyInfo ontoInfo, String role, ArrayList<String> subPropOf, ArrayList<String> notSubPropOf)
	{
		String subPropOfStr = STUtility.convertArrayToString(subPropOf, STXMLUtility.ST_SEPARATOR);
		String notSubPropOfStr = STUtility.convertArrayToString(notSubPropOf, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(Property.Req.getPropertyListRequest, 
				STModel.par(ResourceOld.Par.role, role), 
				STModel.par(ResourceOld.Par.subPropOf, subPropOfStr), 
				STModel.par(ResourceOld.Par.notSubPropOf, notSubPropOfStr), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));			
		return getXMLResponseREPLY(resp);
	}
	
	
	/**
	 * @param ontoInfo
	 * @param propertyRequest
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getAnnotationPropertiesTreeRequest(OntologyInfo ontoInfo, ArrayList<String> excludedProps)
	{
		String excludedPropsStr = STUtility.convertArrayToString(excludedProps, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(Property.Req.getAnnotationPropertiesTreeRequest, 
				STModel.par(Property.Par.excludedProps, excludedPropsStr), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyRequest
	 * @param excludedProps
	 * @return
	 */
	public static XMLResponseREPLY getPlainRDFPropertiesRequest(OntologyInfo ontoInfo, ArrayList<String> excludedProps)
	{
		String excludedPropsStr = STUtility.convertArrayToString(excludedProps, STXMLUtility.ST_SEPARATOR);
		Response resp = getSTModel(ontoInfo).propertyService.makeRequest(Property.Req.getPlainRDFPropertiesRequest, 
				STModel.par(Property.Par.excludedProps, excludedPropsStr), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
