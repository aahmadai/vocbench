package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.owlart.model.ARTURIResource;
import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.owlart.vocabulary.SKOS;
import it.uniroma2.art.owlart.vocabulary.SKOSXL;
import it.uniroma2.art.owlart.vocabulary.XmlSchema;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Property;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.model.semanticturkey.STModelConstants;
import org.fao.aoscs.model.semanticturkey.service.manager.response.PropertyResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResourceResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STLiteral;
import org.fao.aoscs.model.semanticturkey.util.STResource;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class PropertyManager extends ResponseManager {
	
	public static String OBJECTPROPERTY = "objectProperty";
	public static String DATATYPEPROPERTY = "datatypeProperty";
	public static String ANNOTATIONPROPERTY = "annotationProperty";

	/**
	 * @return
	 */
	public static ArrayList<String> getExcludedConceptDatatypeProperties(){
		ArrayList<String> propertyURIs = new ArrayList<String>();
		propertyURIs.add(STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		propertyURIs.add(SKOS.NOTATION);
		return propertyURIs;
	}
	
	/**
	 * @return
	 */
	public static ArrayList<String> getExcludedTermDatatypeProperties(){
		ArrayList<String> propertyURIs = new ArrayList<String>();
		propertyURIs.add(STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		propertyURIs.add(SKOSXL.LITERALFORM);
		propertyURIs.add(SKOS.NOTATION);
		return propertyURIs;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static HashMap<String, String> getConceptNotes(OntologyInfo ontoInfo){
		ArrayList<String> subPropOf = new ArrayList<String>();
		subPropOf.add(SKOS.NOTE);
		
		ArrayList<String> notSubPropOf = new ArrayList<String>();
		notSubPropOf.add(SKOS.DEFINITION);
		
		return getPropertyList(ontoInfo, PropertyManager.ANNOTATIONPROPERTY, subPropOf, notSubPropOf, true);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static HashMap<String, String> getConceptAttributes(OntologyInfo ontoInfo){
		
		ArrayList<String> classes = new ArrayList<String>();
		classes.add(SKOS.CONCEPT);
		
		return getPropertiesForDomains(ontoInfo, classes, PropertyManager.DATATYPEPROPERTY, null, null, true);
		
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static HashMap<String, String> getConceptAlignment(OntologyInfo ontoInfo){
		ArrayList<String> subPropOf = new ArrayList<String>();
		subPropOf.add(SKOS.MAPPINGRELATION);
		
		return getPropertyList(ontoInfo, PropertyManager.OBJECTPROPERTY, subPropOf, null, true);
	}
	
	/**
	 * @return
	 */
	public static ArrayList<String> getConceptObjectProperties(){
		ArrayList<String> propertyURIs = new ArrayList<String>();
		propertyURIs.add(STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED);
		propertyURIs.add(STModelConstants.DCTNAMESPACE+STModelConstants.DCTMODIFIED);
		propertyURIs.add(STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		return propertyURIs;
	}
	
	/**
	 * @return
	 */
	public static ArrayList<String> getTermObjectProperties(){
		ArrayList<String> propertyURIs = new ArrayList<String>();
		propertyURIs.add(STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED);
		propertyURIs.add(STModelConstants.DCTNAMESPACE+STModelConstants.DCTMODIFIED);
		propertyURIs.add(STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		//propertyURIs.add(STModelConstants.COMMONBASENAMESPACE+STModelConstants.HASCODEAGROVOC);
		return propertyURIs;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static HashMap<String, String> getTermAttributes(OntologyInfo ontoInfo){
		
		ArrayList<String> classes = new ArrayList<String>();
		classes.add(SKOSXL.LABEL);
		
		ArrayList<String> notSubPropOf = new ArrayList<String>();
		notSubPropOf.add(SKOSXL.LITERALFORM);
		notSubPropOf.add(SKOS.NOTATION);
		
		return getPropertiesForDomains(ontoInfo, classes, PropertyManager.DATATYPEPROPERTY, null, notSubPropOf, true);
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static HashMap<String, String> getTermCodePropertiesName(OntologyInfo ontoInfo){
		//return getPropertyURIList(ontoInfo, Property.Req.getDatatypePropertiesTreeRequest, SKOS.NOTATION, false);
		
		ArrayList<String> subPropOf = new ArrayList<String>();
		subPropOf.add(SKOS.NOTATION);
		
		return getPropertyList(ontoInfo, PropertyManager.DATATYPEPROPERTY, subPropOf, null, true);
	}
	
	/**
	 * @param ontoInfo
	 * @param type
	 * @return
	 */
	public static XMLResponseREPLY getPropertiesTreeRequest(OntologyInfo ontoInfo, String type)
	{
		String propertyRequest = "";
		
		if(type.equals(RelationshipObject.OBJECT))
			propertyRequest = Property.Req.getObjPropertiesTreeRequest;
		else if(type.equals(RelationshipObject.DATATYPE))
			propertyRequest = Property.Req.getDatatypePropertiesTreeRequest;
		if(type.equals(RelationshipObject.ANNOTATION))
			propertyRequest = Property.Req.getAnnotationPropertiesTreeRequest;
		else if(type.equals(RelationshipObject.ONTOLOGY))
			propertyRequest = Property.Req.getOntologyPropertiesTreeRequest;
		
		return PropertyResponseManager.getPropertiesTreeRequest(ontoInfo, propertyRequest);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyURI
	 * @return
	 */
	public static DomainRangeObject getRange(OntologyInfo ontoInfo, String propertyURI)
	{
		
		DomainRangeObject drObject = new DomainRangeObject();
		
		XMLResponseREPLY reply = PropertyResponseManager.getRangeRequest(ontoInfo, propertyURI, false);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			// Ranges
			for(Element propertyElem : STXMLUtility.getChildElementByTagName(dataElement, "ranges"))
			{
				String rngType = propertyElem.getAttribute("rngType");
				drObject.setRangeType(rngType);
				
				for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
				{
					
					ClassObject classObj = new ClassObject();
					classObj.setType(DomainRangeObject.RANGE);
					classObj.setUri(uriElem.getTextContent());
					classObj.setLabel(uriElem.getTextContent());
					drObject.addRange(classObj);
				}
				
				for(Element bnodeElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.bnode.toString()))
				{
					
					XMLResponseREPLY reply1 = PropertyResponseManager.parseDataRangeRequest(ontoInfo, bnodeElem.getTextContent(), RDFTypesEnum.bnode.toString());
					if(reply1!=null)
					{
						Element dataElement1 = reply1.getDataElement();
						for(Element uriElem1 : STXMLUtility.getChildElementByTagName(dataElement1, RDFTypesEnum.typedLiteral.toString()))
						{
							
							ClassObject classObj = new ClassObject();
							classObj.setType(DomainRangeObject.RANGE);
							classObj.setName(uriElem1.getAttribute("typeQName"));
							classObj.setUri(uriElem1.getAttribute("type"));
							classObj.setLabel(uriElem1.getTextContent());
							drObject.addRange(classObj);
						}
					}
				}
			}
		}
		return drObject;
	}
	
	/**
	 * @return
	 */
	public static HashMap<String,String> getAllRangeDatatype()
	{
		HashMap<String,String> map = new HashMap<String,String>();
		map.put(XmlSchema.BOOLEAN, XmlSchema.BOOLEAN);
		map.put(XmlSchema.DATE, XmlSchema.DATE);
		map.put(XmlSchema.DATETIME, XmlSchema.DATETIME);
		map.put(XmlSchema.DURATION, XmlSchema.DURATION);
		map.put(XmlSchema.FLOAT, XmlSchema.FLOAT);
		map.put(XmlSchema.INT, XmlSchema.INT);
		map.put(XmlSchema.STRING, XmlSchema.STRING);
		map.put(XmlSchema.TIME, XmlSchema.TIME);
		return map;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param lang
	 * @return
	 */
	public static boolean addPlainLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String lang) {
		if(lang!=null && (lang.equals("null") || lang.equals("")))	lang = null;
		XMLResponseREPLY reply = PropertyResponseManager.createAndAddPropValueRequest(ontoInfo, resourceURI, propertyURI, value, lang, null, RDFTypesEnum.plainLiteral.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param range
	 * @return
	 */
	public static boolean addTypedLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String range)
	{
		XMLResponseREPLY reply = PropertyResponseManager.createAndAddPropValueRequest(ontoInfo, resourceURI, propertyURI, value, range, RDFTypesEnum.typedLiteral.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param oldValue
	 * @param range
	 * @param oldRange
	 * @param lang
	 * @param oldLang
	 * @return
	 */
	public static boolean updateTypedLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String oldValue, String range, String oldRange)
	{
		XMLResponseREPLY reply = PropertyResponseManager.updatePropValueRequest(ontoInfo, resourceURI, propertyURI, value, oldValue, range, oldRange, null, null, RDFTypesEnum.typedLiteral.toString(), RDFTypesEnum.typedLiteral.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param oldValue
	 * @param range
	 * @param oldRange
	 * @param lang
	 * @param oldLang
	 * @return
	 */
	public static boolean updatePlainLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String oldValue, String lang, String oldLang)
	{
		if(lang!=null && (lang.equals("null") || lang.equals("")))	lang = null;
		if(oldLang!=null && (oldLang.equals("null") || oldLang.equals("")))	oldLang = null;
		XMLResponseREPLY reply = PropertyResponseManager.updatePropValueRequest(ontoInfo, resourceURI, propertyURI, value, oldValue, null, null, lang, oldLang, RDFTypesEnum.plainLiteral.toString(), RDFTypesEnum.plainLiteral.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param lang
	 * @return
	 */
	public static boolean removePlainLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String lang)
	{
		if(lang!=null && (lang.equals("null") || lang.equals("")))	lang = null;
		XMLResponseREPLY reply = PropertyResponseManager.removePropValueRequest(ontoInfo, resourceURI, propertyURI, value, lang, null, RDFTypesEnum.plainLiteral.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param range
	 * @return
	 */
	public static boolean removeTypedLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String range)
	{
		XMLResponseREPLY reply = PropertyResponseManager.removePropValueRequest(ontoInfo, resourceURI, propertyURI, value, null, range, RDFTypesEnum.typedLiteral.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @param range
	 * @return
	 */
	public static boolean addResourcePropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value, String range)
	{
		XMLResponseREPLY reply = PropertyResponseManager.createAndAddPropValueRequest(ontoInfo, resourceURI, propertyURI, value, null, range, RDFTypesEnum.resource.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @return
	 */
	public static boolean removeResourcePropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value)
	{
		XMLResponseREPLY reply = PropertyResponseManager.removePropValueRequest(ontoInfo, resourceURI, propertyURI, value, null, null, RDFTypesEnum.uri.toString());
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static boolean removeAllPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		XMLResponseREPLY reply = ResourceResponseManager.getPropertyValuesRequest(ontoInfo, resourceURI, propertyURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(colElem))
				{
					removePlainLiteralPropValue(ontoInfo, resourceURI, propertyURI, stLiteral.getLabel(), stLiteral.getLanguage());
				}
				for(STLiteral stLiteral : STXMLUtility.getTypedLiteral(colElem))
				{
					removeTypedLiteralPropValue(ontoInfo, resourceURI, propertyURI, stLiteral.getLabel(), stLiteral.getDatatypeURI());
				}
				for(STResource stResource : STXMLUtility.getURIResource(colElem))
				{
					removeResourcePropValue(ontoInfo, resourceURI, propertyURI, stResource.getARTNode().asURIResource().getURI());
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @return
	 */
	public static boolean addExternalPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addExternalPropValueRequest(ontoInfo, resourceURI, propertyURI, value);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @param value
	 * @return
	 */
	public static boolean addExistingPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI, String value)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addExistingPropValueRequest(ontoInfo, resourceURI, propertyURI, value);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static ArrayList<STLiteral> getPlainLiteralPropValues(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{

		ArrayList<STLiteral> propValues = new ArrayList<STLiteral>();
		XMLResponseREPLY reply = ResourceResponseManager.getPropertyValuesRequest(ontoInfo, resourceURI, propertyURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				propValues.addAll(STXMLUtility.getPlainLiteral(colElem));
			}
		}
		return propValues;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static STLiteral getPlainLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		for(STLiteral stLiteral : getPlainLiteralPropValues(ontoInfo, resourceURI, propertyURI))
		{
			return stLiteral;
		}
		return null;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static ArrayList<STLiteral> getTypedLiteralPropValues(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{

		ArrayList<STLiteral> propValues = new ArrayList<STLiteral>();
		XMLResponseREPLY reply = ResourceResponseManager.getPropertyValuesRequest(ontoInfo, resourceURI, propertyURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				propValues.addAll(STXMLUtility.getTypedLiteral(colElem));
			}
		}
		return propValues;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static STLiteral getTypedLiteralPropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		for(STLiteral stLiteral : getTypedLiteralPropValues(ontoInfo, resourceURI, propertyURI))
		{
			return stLiteral;
		}
		return null;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static ArrayList<STResource> getResourcePropValues(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{

		ArrayList<STResource> propValues = new ArrayList<STResource>();
		XMLResponseREPLY reply = ResourceResponseManager.getPropertyValuesRequest(ontoInfo, resourceURI, propertyURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				propValues.addAll(STXMLUtility.getURIResource(colElem));
			}
		}
		return propValues;
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param propertyURI
	 * @return
	 */
	public static STResource getResourcePropValue(OntologyInfo ontoInfo, String resourceURI, String propertyURI)
	{
		for(STResource stResource : getResourcePropValues(ontoInfo, resourceURI, propertyURI))
		{
			return stResource;
		}
		return null;
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param propertyType
	 * @return
	 */
	public static boolean addTopProperty(OntologyInfo ontoInfo, String propertyUri, String propertyType)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addTopPropertyRequest(ontoInfo, propertyUri, propertyType);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param propertyUri
	 * @param propertyType
	 * @param superPropertyUri
	 * @return
	 */
	public static boolean addProperty(OntologyInfo ontoInfo, String propertyUri, String propertyType, String superPropertyUri)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addPropertyRequest(ontoInfo, propertyUri, propertyType, superPropertyUri);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param domainUri
	 * @return
	 */
	public static boolean addPropertyDomainRequest(OntologyInfo ontoInfo, String propertyUri, String domainUri)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addPropertyDomainRequest(ontoInfo, propertyUri, domainUri);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param propertyUri
	 * @param domainUri
	 * @return
	 */
	public static boolean removePropertyDomainRequest(OntologyInfo ontoInfo, String propertyUri, String domainUri)
	{
		XMLResponseREPLY reply = PropertyResponseManager.removePropertyDomainRequest(ontoInfo, propertyUri, domainUri);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param rangeUri
	 * @return
	 */
	public static boolean addPropertyRange(OntologyInfo ontoInfo, String propertyUri, String rangeUri)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addPropertyRangeRequest(ontoInfo, propertyUri, rangeUri);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param propertyUri
	 * @param rangeUri
	 * @return
	 */
	public static boolean removePropertyRange(OntologyInfo ontoInfo, String propertyUri, String rangeUri)
	{
		XMLResponseREPLY reply = PropertyResponseManager.removePropertyRangeRequest(ontoInfo, propertyUri, rangeUri);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static boolean setDataRange(OntologyInfo ontoInfo, String propertyUri, String values)
	{
		XMLResponseREPLY reply = PropertyResponseManager.setDataRangeRequest(ontoInfo, propertyUri, values);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static boolean addValueToDatarange(OntologyInfo ontoInfo, String dataRange, String value)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addValueToDatarangeRequest(ontoInfo, STXMLUtility.ST_BLANKNODEPREFIX+dataRange, value);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static boolean addValuesToDatarange(OntologyInfo ontoInfo, String dataRange, String values)
	{
		XMLResponseREPLY reply = PropertyResponseManager.addValuesToDatarangeRequest(ontoInfo, STXMLUtility.ST_BLANKNODEPREFIX+dataRange, values);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyUri
	 * @param values
	 * @return
	 */
	public static boolean removeValueFromDatarange(OntologyInfo ontoInfo, String dataRange, String value)
	{
		XMLResponseREPLY reply = PropertyResponseManager.removeValueFromDatarangeRequest(ontoInfo, STXMLUtility.ST_BLANKNODEPREFIX+dataRange, value);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param dataRange
	 * @param nodeType
	 * @return
	 */
	public static ArrayList<ClassObject> parseDataRange(OntologyInfo ontoInfo, String dataRange, String nodeType)
	{
		ArrayList<ClassObject> list = new ArrayList<ClassObject>();
		XMLResponseREPLY reply = PropertyResponseManager.parseDataRangeRequest(ontoInfo, dataRange, nodeType);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(dataElement, RDFTypesEnum.typedLiteral.toString()))
			{
				ClassObject clsObj = new ClassObject();
				clsObj.setUri(typedLiteralElem.getAttribute("type"));
				clsObj.setName(typedLiteralElem.getAttribute("typeQName"));
				clsObj.setLabel(typedLiteralElem.getTextContent());
				clsObj.setType(DomainRangeObject.RANGE);
				
				list.add(clsObj);
			}	
			
			for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(dataElement, RDFTypesEnum.plainLiteral.toString()))
			{
				ClassObject clsObj = new ClassObject();
				clsObj.setUri(typedLiteralElem.getAttribute("lang"));
				clsObj.setName(typedLiteralElem.getAttribute("lang"));
				clsObj.setLabel(typedLiteralElem.getTextContent());
				clsObj.setType(DomainRangeObject.RANGE);
				
				list.add(clsObj);
			}	
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param classes
	 * @param role
	 * @param subPropOf
	 * @param notSubPropOf
	 * @param explicit
	 * @return
	 */
	public static HashMap<String, String> getPropertiesForDomains(OntologyInfo ontoInfo, ArrayList<String> classes, String role, ArrayList<String> subPropOf, ArrayList<String> notSubPropOf, boolean explicit)
	{
		HashMap<String, String> list = new HashMap<String, String>();
		XMLResponseREPLY reply = PropertyResponseManager.getPropertiesForDomainsRequest(ontoInfo, classes, role, subPropOf, notSubPropOf);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for (Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(colElem, explicit))
				{
					ARTURIResource res = stResource.getARTNode().asURIResource();
					list.put(res.getURI(), stResource.getRendering());
				}
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param role
	 * @param subPropOf
	 * @param notSubPropOf
	 * @param explicit
	 * @return
	 */
	public static HashMap<String, String> getPropertyList(OntologyInfo ontoInfo, String role, ArrayList<String> subPropOf, ArrayList<String> notSubPropOf, boolean explicit)
	{
		HashMap<String, String> list = new HashMap<String, String>();
		XMLResponseREPLY reply = PropertyResponseManager.getPropertyListRequest(ontoInfo, role, subPropOf, notSubPropOf);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for (Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
			{
				for(STResource stResource : STXMLUtility.getURIResource(colElem, explicit))
				{
					list.put(stResource.getARTNode().asURIResource().getURI(), stResource.getRendering());
				}
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param excludedProps
	 * @param explicit
	 * @return
	 */
	public static HashMap<String, String> getAnnotationPropertiesTree(OntologyInfo ontoInfo, ArrayList<String> excludedProps, boolean explicit)
	{
		HashMap<String, String> list = new HashMap<String, String>();
		XMLResponseREPLY reply = PropertyResponseManager.getAnnotationPropertiesTreeRequest(ontoInfo, excludedProps);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				list.put(propElement.getAttribute("uri"), propElement.getAttribute("name"));
				list = getChildProperty(ontoInfo, propElement, list);
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param dataElement
	 * @param list
	 * @return
	 */
	private static HashMap<String, String> getChildProperty(OntologyInfo ontoInfo, Element dataElement, HashMap<String, String> list)
	{
		for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
		{
			for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "Property"))
			{
				list.put(propElement.getAttribute("uri"), propElement.getAttribute("name"));
			}
		}
		return list;
	}
	
	/**
	 * @param ontoInfo
	 * @param excludedProps
	 * @param explicit
	 * @return
	 */
	public static HashMap<String, String> getPlainRDFProperties(OntologyInfo ontoInfo, ArrayList<String> excludedProps, boolean explicit)
	{
		HashMap<String, String> list = new HashMap<String, String>();
		XMLResponseREPLY reply = PropertyResponseManager.getPlainRDFPropertiesRequest(ontoInfo, excludedProps);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				list.put(propElement.getAttribute("uri"), propElement.getAttribute("name"));
				list = getChildProperty(ontoInfo, propElement, list);
			}
		}
		return list;
	}
	
		/**
		 * @return
		 */
		/*public static HashMap<String, String> getTermPropertiesName(OntologyInfo ontoInfo){
			//return getPropertyURIList(ontoInfo, Property.Req.getAnnotationPropertiesTreeRequest, RDFS.LABEL, false);
			ArrayList<String> subPropOf = new ArrayList<String>();
			subPropOf.add(RDFS.LABEL);
			return getPropertyList(ontoInfo, PropertyManager.ANNOTATIONPROPERTY, subPropOf, null, true);
		}*/
		
		/**
		 * @return
		 */
		/*public static String getConceptDefinitionPropertiesName(OntologyInfo ontoInfo){
			return SKOS.DEFINITION;
		}*/
		
		
		
		/**
		 * @return
		 */
		/*public static ArrayList<String> getConceptObjectPropertiesName(OntologyInfo ontoInfo){
			
			ArrayList<String> classes = new ArrayList<String>();
			classes.add(SKOS.RELATED);
			
			return classes;
		}*/
		
		
		/**
		 * @return
		 */
		/*public static String getConcepImagePropertiesName(OntologyInfo ontoInfo){
			return STModelConstants.FOAFNAMESPACE+STModelConstants.FOAFIMG;
		}*/
		
		
		
		
		/**
		 * @return
		 */
		/*public static HashMap<String, String> getTermObjectPropertiesName(OntologyInfo ontoInfo){
			//return getPropertyURIList(ontoInfo, Property.Req.getObjPropertiesTreeRequest, SKOSXL.LABELRELATION, true);
			
			ArrayList<String> subPropOf = new ArrayList<String>();
			subPropOf.add(SKOSXL.LABELRELATION);
			
			return getPropertyList(ontoInfo, PropertyManager.OBJECTPROPERTY, subPropOf, null, true);
			
		}*/
		
		
		
		

		/**
		 * @return
		 */
		/*public static HashMap<String, String> getAllTermCodeProperties(OntologyInfo ontoInfo){
			//ArrayList<RelationshipObject> list = getProperties(ontoInfo, getTermCodePropertiesName(ontoInfo));
			//return list;
			return  getPropertyUriNameMap(ontoInfo, Property.Req.getDatatypePropertiesTreeRequest, SKOS.NOTATION, false);
		}*/
		
		/**
		 * @return
		 */
		/*public static HashMap<String, DomainRangeObject> getTermCodeType(OntologyInfo ontoInfo){
			HashMap<String, DomainRangeObject> typeList = new HashMap<String, DomainRangeObject>();	
			
			for(String property : getTermCodePropertiesName(ontoInfo).keySet())
			{
				typeList.put(property, getRange(ontoInfo, property));
			}
			return typeList;
		}*/
		
		
		
		
		/**
		 * @param ontoInfo
		 * @param propertyList
		 * @return
		 */
		/*public static ArrayList<RelationshipObject> getProperties(OntologyInfo ontoInfo, ArrayList<String> propertyList){
			ArrayList<RelationshipObject> list = new ArrayList<RelationshipObject>();
			for (String property : propertyList) 
			{
				list.add(getRelationshipObject(ontoInfo, property));
			}
			return list;
		}*/
		
		/**
		 * @param ontoInfo
		 * @param propertyRequest
		 * @param propertyURI
		 * @param includeParentProperty
		 * @return
		 */
		/*public static HashMap<String, String> getPropertyUriNameMap(OntologyInfo ontoInfo, String propertyRequest, String propertyURI, boolean includeParentProperty){
			HashMap<String, String> map = new HashMap<String, String>();
			XMLResponseREPLY reply = PropertyResponseManager.getPropertiesTreeRequest(ontoInfo, propertyRequest);
			if(reply!=null)
			{
				Element dataElement = reply.getDataElement();
				for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
				{
					String uri = propElement.getAttribute("uri");
					String name = propElement.getAttribute("name");
					if(uri.equals(propertyURI))
					{
						map = getChildPropertyUriNameMap(propElement, map);
						if(includeParentProperty)
							map.put(uri, name);
					}
				}
			}
			return map;
		}*/
		
		/**
		 * @param dataElement
		 * @param propertyUriNameMap
		 * @return
		 */
		/*private static HashMap<String, String> getChildPropertyUriNameMap(Element dataElement, HashMap<String, String> propertyUriNameMap)
		{
			for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
			{
				for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "Property"))
				{
					String uri = propElement.getAttribute("uri");
					String name = propElement.getAttribute("name");
					if(!propertyUriNameMap.containsKey(uri))
						propertyUriNameMap.put(uri, name);
					propertyUriNameMap = getChildPropertyUriNameMap(propElement, propertyUriNameMap);
				}
			}
			return propertyUriNameMap;
		}*/
		
		/**
		 * @param property
		 * @return
		 */
		/*public static ArrayList<String> getPropertyURIList(OntologyInfo ontoInfo, String propertyRequest, String propertyURI, boolean includeParentProperty){
			ArrayList<String> list = new ArrayList<String>();
			//propertyNameList = new HashMap<String, ArrayList<String>>();;
			if(propertyNameList.containsKey(propertyRequest+"_"+propertyURI))
			{
				list = propertyNameList.get(propertyRequest+"_"+propertyURI);
			}
			else
			{
				XMLResponseREPLY reply = PropertyResponseManager.getPropertiesTreeRequest(ontoInfo, propertyRequest);
				if(reply!=null)
				{
					Element dataElement = reply.getDataElement();
					for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
					{
						String uri = propElement.getAttribute("uri");
						if(uri.equals(propertyURI))
							list = getChildPropertyURIList(propElement, list);
					}
					if(includeParentProperty)
						list.add(propertyURI);
					propertyNameList.put(propertyRequest+"_"+propertyURI, list);
				}
			}
			return list;
		}*/
		
		/**
		 * @param dataElement
		 * @param propertyNameList
		 * @return
		 */
		/*private static ArrayList<String> getChildPropertyURIList(Element dataElement, ArrayList<String> propertyNameList)
		{
			for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
			{
				for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "Property"))
				{
					String uri = propElement.getAttribute("uri");
					if(!propertyNameList.contains(uri))
						propertyNameList.add(uri);
					propertyNameList = getChildPropertyURIList(propElement, propertyNameList);
				}
			}
			return propertyNameList;
		}*/
		
		/**
		 * @param propertyURI
		 * @return
		 */
		/*public static RelationshipObject getRelationshipObject(OntologyInfo ontoInfo, String propertyURI)
		{
			//relationshipObjects = new HashMap<String, RelationshipObject>();
			if(!relationshipObjects.containsKey(propertyURI))
			{
				RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
				if(rObj!=null)
					relationshipObjects.put(propertyURI, rObj);
			}
			return relationshipObjects.get(propertyURI);
		}*/

		
		/*public static ArrayList<String> getRange(OntologyInfo ontoInfo, String propertyURI)
		{
			ArrayList<String> rangeDatatype = new ArrayList<String>();
			XMLResponseREPLY reply = PropertyResponseManager.getRangeRequest(ontoInfo, propertyURI, false);
			if(reply!=null)
			{
				Element dataElement = reply.getDataElement();
				// Ranges
				for(Element propertyElem : STXMLUtility.getChildElementByTagName(dataElement, "ranges"))
				{
					for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
					{
						rangeDatatype.add(uriElem.getTextContent());
					}
				}
			}
			return rangeDatatype;
		}*/
		/*public static String getRangeDatatype(OntologyInfo ontoInfo, String propertyURI)
		{
			String rangeDatatype = "";
			XMLResponseREPLY reply = PropertyResponseManager.getPropertyDescriptionRequest(ontoInfo, propertyURI);
			if(reply!=null)
			{
				Element dataElement = reply.getDataElement();
				if(dataElement.getAttribute("type").equals(SKOS.templateandvalued))
				{
					// Ranges
					for(Element propertyElem : STXMLUtility.getChildElementByTagName(dataElement, "ranges"))
					{
						for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
						{
							rangeDatatype = getRangeDatatype(uriElem);
						}
					}
				}
			}
			return rangeDatatype;
		}*/
		
		/**
		 * @param uriElem
		 * @return
		 */
		/*public static String getRangeDatatype1(Element uriElem)
		{
			String rangeDatatype = "";
			if(uriElem.getTextContent().equals(XmlSchema.BOOLEAN))
				rangeDatatype= XmlSchema.BOOLEAN;
			else if(uriElem.getTextContent().equals(XmlSchema.DATE))
				rangeDatatype = XmlSchema.DATE;
			else if(uriElem.getTextContent().equals(XmlSchema.DATETIME))
				rangeDatatype = XmlSchema.DATETIME;
			else if(uriElem.getTextContent().equals(XmlSchema.DURATION))
				rangeDatatype = XmlSchema.DURATION;
			else if(uriElem.getTextContent().equals(XmlSchema.FLOAT))
				rangeDatatype = XmlSchema.FLOAT;
			else if(uriElem.getTextContent().equals(XmlSchema.INT))
				rangeDatatype = XmlSchema.INT;
			else if(uriElem.getTextContent().equals(XmlSchema.STRING))
				rangeDatatype = XmlSchema.STRING;
			else if(uriElem.getTextContent().equals(XmlSchema.TIME))
				rangeDatatype = XmlSchema.TIME;
			return rangeDatatype;
		}*/
	
	//static HashMap<String, RelationshipObject> relationshipObjects = new HashMap<String, RelationshipObject>();
	//static HashMap<String, ArrayList<String>> propertyNameList = new HashMap<String, ArrayList<String>>();

		
}
