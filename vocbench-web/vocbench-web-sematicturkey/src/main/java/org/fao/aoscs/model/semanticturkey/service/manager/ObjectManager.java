package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.owlart.model.impl.ARTNodeFactoryImpl;
import it.uniroma2.art.owlart.vocabulary.OWL;
import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptShowObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.ImageObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.model.semanticturkey.STModelConstants;
import org.fao.aoscs.model.semanticturkey.service.manager.response.PropertyResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.VocbenchResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STLiteral;
import org.fao.aoscs.model.semanticturkey.util.STLiteralImpl;
import org.fao.aoscs.model.semanticturkey.util.STNode;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sun.syndication.io.impl.DateParser;

/**
 * @author rajbhandari
 *
 */
public class ObjectManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ObjectManager.class);
	
	/**
	 * @param conceptURI
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public static TreeObject createTreeObject(OntologyInfo ontoInfo, String conceptURI, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList, String parentURI)
	{
		String status = "";
		String conceptShow = "";
		boolean hasChild = false;
		Collection<STLiteral> prefList = new ArrayList<STLiteral>();
		Collection<STLiteral> altList = new ArrayList<STLiteral>();
		
		XMLResponseREPLY reply = VocbenchResponseManager.getConceptDescriptionRequest(ontoInfo, conceptURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			for(Element conceptInfoElement : STXMLUtility.getChildElementByTagName(dataElement, "conceptInfo"))
			{
				
				for(Element conceptElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "concept"))
				{
					for(Element uriElement : STXMLUtility.getChildElementByTagName(conceptElement, "uri"))
					{
						status = uriElement.getAttribute("status");
						conceptShow = uriElement.getAttribute("show");
						hasChild = uriElement.getAttribute("more").equals("1")?true:false;
					}
				}
				for(Element labelsElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "labels"))
				{
					for(Element labelcollectionElement : STXMLUtility.getChildElementByTagName(labelsElement, "collection"))
					{
						for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(labelcollectionElement, RDFTypesEnum.plainLiteral.toString()))
						{
							boolean explicit = STUtility.checkBoolean(typedLiteralElem.getAttribute("explicit"));
							String label = typedLiteralElem.getTextContent();
							String lang = typedLiteralElem.getAttribute("lang");
							String show = typedLiteralElem.getAttribute("status");
							boolean isPreferred = Boolean.parseBoolean(typedLiteralElem.getAttribute("isPreferred"));
							ARTNodeFactoryImpl artNFI = new ARTNodeFactoryImpl();
							if(isPreferred)
								prefList.add(new STLiteralImpl(artNFI.createLiteral(label, lang), explicit, show));
							else
								altList.add(new STLiteralImpl(artNFI.createLiteral(label, lang), explicit, show));
						}				
					}
				}
			}
		}
		
		String label = createTreeObjectLabel(conceptURI, prefList, altList, showAlsoNonpreferredTerms, isHideDeprecated, langList);
		if(label.startsWith("###EMPTY###"))
			label = conceptShow;
		return STUtility.createTreeObject(ontoInfo, conceptURI, label, status, hasChild, parentURI);
	}
	
	/**
	 * @param propertyName
	 * @param propertyURI
	 * @return
	 */
	public static RelationshipObject createRelationshipObject(OntologyInfo ontoInfo, String propertyURI)
	{
		RelationshipObject rObj = null;
		if(propertyURI!=null && !propertyURI.equals(""))
		{
			rObj = new RelationshipObject();
			rObj.setUri(propertyURI);
			// TODO replace property name with uri
			rObj.setName(propertyURI);
			
			XMLResponseREPLY reply = PropertyResponseManager.getPropertyDescriptionRequest(ontoInfo, rObj.getUri());
			if(reply!=null)
			{
				Element dataElement = reply.getDataElement();
				if(dataElement.getAttribute("type").equals(SKOS.templateandvalued))
				{
					
					// Types
					for(Element TypesElement : STXMLUtility.getChildElementByTagName(dataElement, "Types"))
					{
						for(Element colElement : STXMLUtility.getChildElementByTagName(TypesElement, "collection"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(colElement, RDFTypesEnum.uri.toString()))
							{
								String typeUri = uriElem.getTextContent();
								if(typeUri.equals(OWL.OBJECTPROPERTY))
									rObj.setType(RelationshipObject.OBJECT);
								else if(typeUri.equals(OWL.DATATYPEPROPERTY))
									rObj.setType(RelationshipObject.DATATYPE);
								else if(typeUri.equals(OWL.ANNOTATIONPROPERTY))
									rObj.setType(RelationshipObject.ANNOTATION);
								else if(typeUri.equals(OWL.ONTOLOGYPROPERTY))
									rObj.setType(RelationshipObject.ONTOLOGY);
								else if(typeUri.equals(OWL.FUNCTIONALPROPERTY))
								{
									rObj.setFunctional(true);
								}
								else if(typeUri.equals(OWL.INVERSEFUNCTIONALPROPERTY))
								{
									rObj.setInverseFunctional(true);
								}
								else if(typeUri.equals(OWL.TRANSITIVEPROPERTY))
								{
									rObj.setTransitive(true);
									rObj.setType(RelationshipObject.OBJECT);
									
								}
								else if(typeUri.equals(OWL.SYMMETRICPROPERTY))
								{
									rObj.setSymmetric(true);
									rObj.setType(RelationshipObject.OBJECT);
									
								}
							}
						}
					}
					
					// SuperTypes
					for(Element TypesElement : STXMLUtility.getChildElementByTagName(dataElement, "SuperTypes"))
					{
						for(Element colElement : STXMLUtility.getChildElementByTagName(TypesElement, "collection"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(colElement, RDFTypesEnum.uri.toString()))
							{
								rObj.setParent(uriElem.getTextContent());
							}
						}
					}
					
					// Facets (symmetric, functional, inverseFunctional, transitive, inverseOf)
					for(Element facetsElement : STXMLUtility.getChildElementByTagName(dataElement, "facets"))
					{
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "functional"))
						{
							rObj.setFunctional(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "inverseFunctional"))
						{
							rObj.setInverseFunctional(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "symmetric"))
						{
							rObj.setSymmetric(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "transitive"))
						{
							rObj.setTransitive(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "inverseOf"))
						{
							for(Element invuriElem : STXMLUtility.getChildElementByTagName(uriElem, RDFTypesEnum.uri.toString()))
							{
								rObj.setInverse(invuriElem.getTextContent());
							}
						}
						if(rObj.getInverse()==null)	rObj.setInverse("");
					}
					
					DomainRangeObject drObject = new DomainRangeObject();
					
					// Ranges
					for(Element propertyElem : STXMLUtility.getChildElementByTagName(dataElement, "ranges"))
					{
						String rngType = propertyElem.getAttribute("rngType");
						drObject.setRangeType(rngType);
						
						//if(rngType.equals(DomainRangeObject.resource))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
							{
								
								ClassObject classObj = new ClassObject();
								classObj.setType(DomainRangeObject.RANGE);
								classObj.setName(uriElem.getAttribute("show"));
								classObj.setUri(uriElem.getTextContent());
								classObj.setLabel(uriElem.getAttribute("show"));
								drObject.addRange(classObj);
								//drObject.setRangeDataType(PropertyManager.getRangeDatatype(uriElem));
							}
						}
						//else if(rngType.equals(DomainRangeObject.literal))
						{
							for(Element bnodeElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.bnode.toString()))
							{
								String rangeVal = bnodeElem.getAttribute("show");
								if(rangeVal.length()>1)
								{
									rangeVal = rangeVal.substring(1,rangeVal.length()-1);
								}
								
								ClassObject classObj = new ClassObject();
								classObj.setType(DomainRangeObject.RANGE);
								classObj.setName(bnodeElem.getTextContent());
								classObj.setUri(bnodeElem.getTextContent());
								classObj.setLabel(rangeVal);
								drObject.addRange(classObj);
								
							}
						}
					}
					
					//Domains
					for(Element domainsElem : STXMLUtility.getChildElementByTagName(dataElement, "domains"))
					{
						for(Element uriElem : STXMLUtility.getChildElementByTagName(domainsElem, RDFTypesEnum.uri.toString()))
						{
							ClassObject classObj = new ClassObject();
							classObj.setType(DomainRangeObject.DOMAIN);
							classObj.setName(uriElem.getAttribute("show"));
							classObj.setUri(uriElem.getTextContent());
							classObj.setLabel(uriElem.getAttribute("show"));
							drObject.addDomain(classObj);
						}
					}
					
					rObj.setDomainRangeObject(drObject);
					
					//Properties
					for(Element propertyElem : STXMLUtility.getPropertiesElement(dataElement))
					{
						String propName = propertyElem.getAttribute("name");
						if(propName.equals("rdfs:label"))
						{
							for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(propertyElem, false))
							{
								rObj.addLabel(stLiteral.getLabel(), stLiteral.getLanguage());
							}
						}
						else if(propName.equals("rdfs:comment"))
						{
							for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(propertyElem, false))
							{
								rObj.addComment(stLiteral.getLabel(), stLiteral.getLanguage());
							}
						}
					}
				}
			}
		}
		return rObj;
	}
	/*
	 public static RelationshipObject createRelationshipObject(OntologyInfo ontoInfo, String propertyURI)
	{
		RelationshipObject rObj = null;
		if(propertyURI!=null && !propertyURI.equals(""))
		{
			rObj = new RelationshipObject();
			rObj.setUri(propertyURI);
			rObj.setName(STUtility.getName(ontoInfo, propertyURI));
			
			XMLResponseREPLY reply = PropertyResponseManager.getPropertyDescriptionRequest(ontoInfo, rObj.getUri());
			if(reply!=null)
			{
				Element dataElement = reply.getDataElement();
				if(dataElement.getAttribute("type").equals(SKOS.templateandvalued))
				{
					
					// Types
					for(Element TypesElement : STXMLUtility.getChildElementByTagName(dataElement, "Types"))
					{
						for(Element colElement : STXMLUtility.getChildElementByTagName(TypesElement, "collection"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(colElement, RDFTypesEnum.uri.toString()))
							{
								String typeUri = uriElem.getTextContent();
								if(typeUri.equals(OWL.OBJECTPROPERTY))
									rObj.setType(RelationshipObject.OBJECT);
								else if(typeUri.equals(OWL.DATATYPEPROPERTY))
									rObj.setType(RelationshipObject.DATATYPE);
								else if(typeUri.equals(OWL.ANNOTATIONPROPERTY))
									rObj.setType(RelationshipObject.ANNOTATION);
								else if(typeUri.equals(OWL.ONTOLOGYPROPERTY))
									rObj.setType(RelationshipObject.ONTOLOGY);
								else if(typeUri.equals(OWL.FUNCTIONALPROPERTY))
								{
									rObj.setFunctional(true);
								}
								else if(typeUri.equals(OWL.INVERSEFUNCTIONALPROPERTY))
								{
									rObj.setInverseFunctional(true);
								}
								else if(typeUri.equals(OWL.TRANSITIVEPROPERTY))
								{
									rObj.setTransitive(true);
									rObj.setType(RelationshipObject.OBJECT);
									
								}
								else if(typeUri.equals(OWL.SYMMETRICPROPERTY))
								{
									rObj.setSymmetric(true);
									rObj.setType(RelationshipObject.OBJECT);
									
								}
							}
						}
					}
					
					// SuperTypes
					for(Element TypesElement : STXMLUtility.getChildElementByTagName(dataElement, "SuperTypes"))
					{
						for(Element colElement : STXMLUtility.getChildElementByTagName(TypesElement, "collection"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(colElement, RDFTypesEnum.uri.toString()))
							{
								rObj.setParent(uriElem.getTextContent());
							}
						}
					}
					
					// Facets (symmetric, functional, inverseFunctional, transitive, inverseOf)
					for(Element facetsElement : STXMLUtility.getChildElementByTagName(dataElement, "facets"))
					{
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "functional"))
						{
							rObj.setFunctional(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "inverseFunctional"))
						{
							rObj.setInverseFunctional(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "symmetric"))
						{
							rObj.setSymmetric(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "transitive"))
						{
							rObj.setTransitive(uriElem.getAttribute("value").equals("true"));
						}
						for(Element uriElem : STXMLUtility.getChildElementByTagName(facetsElement, "inverseOf"))
						{
							for(String inverse: STXMLUtility.getValues(uriElem))
							{
								rObj.setInverse(STUtility.getUri(ontoInfo, inverse));
							}
						}
						if(rObj.getInverse()==null)	rObj.setInverse("");
					}
					
					DomainRangeObject drObject = new DomainRangeObject();
					
					// Ranges
					for(Element propertyElem : STXMLUtility.getChildElementByTagName(dataElement, "ranges"))
					{
						String rngType = propertyElem.getAttribute("rngType");
						drObject.setRangeType(rngType);
						
						//if(rngType.equals(DomainRangeObject.resource))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.uri.toString()))
							{
								
								ClassObject classObj = new ClassObject();
								classObj.setType(DomainRangeObject.RANGE);
								classObj.setName(uriElem.getAttribute("show"));
								classObj.setUri(uriElem.getTextContent());
								classObj.setLabel(uriElem.getAttribute("show"));
								drObject.addRange(classObj);
								//drObject.setRangeDataType(PropertyManager.getRangeDatatype(uriElem));
							}
						}
						//else if(rngType.equals(DomainRangeObject.literal))
						{
							for(Element bnodeElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.bnode.toString()))
							{
								String rangeVal = bnodeElem.getAttribute("show");
								if(rangeVal.length()>1)
								{
									rangeVal = rangeVal.substring(1,rangeVal.length()-1);
								}
								
								ClassObject classObj = new ClassObject();
								classObj.setType(DomainRangeObject.RANGE);
								classObj.setName(bnodeElem.getTextContent());
								classObj.setUri(bnodeElem.getTextContent());
								classObj.setLabel(rangeVal);
								drObject.addRange(classObj);
								
							}
						}
					}
					
					//Domains
					for(Element domainsElem : STXMLUtility.getChildElementByTagName(dataElement, "domains"))
					{
						for(Element domainElem : STXMLUtility.getChildElementByTagName(domainsElem, "domain"))
						{
							ClassObject classObj = new ClassObject();
							classObj.setType(DomainRangeObject.DOMAIN);
							classObj.setName(domainElem.getAttribute("name"));
							classObj.setUri(STUtility.getUri(ontoInfo, domainElem.getAttribute("name")));
							classObj.setLabel(domainElem.getAttribute("name"));
							drObject.addDomain(classObj);
						}
					}
					
					rObj.setDomainRangeObject(drObject);
					
					//Properties
					for(Element propertyElem : STXMLUtility.getPropertiesElement(dataElement))
					{
						String propName = propertyElem.getAttribute("name");
						if(propName.equals("rdfs:label"))
						{
							for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(propertyElem, false))
							{
								rObj.addLabel(stLiteral.getLabel(), stLiteral.getLanguage());
							}
						}
						else if(propName.equals("rdfs:comment"))
						{
							for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(propertyElem, false))
							{
								rObj.addComment(stLiteral.getLabel(), stLiteral.getLanguage());
							}
						}
					}
				}
			}
		}
		return rObj;
	} 
	 
	 */
	
	/**
	 * @param resourceURI
	 * @return
	 */
	public static InformationObject createInformationObject(OntologyInfo ontoInfo, String resourceURI, boolean explicit)
	{
		InformationObject infoObj = new InformationObject();
		
		HashMap<ClassObject, ArrayList<STNode>> list = ResourceManager.getValuesOfProperties(ontoInfo, resourceURI, PropertyManager.getConceptObjectProperties(), explicit);
		for(ClassObject clsObj : list.keySet())
		{
			ArrayList<STNode> stNodes = list.get(clsObj);
			for(STNode stNode : stNodes)
			{
				if(stNode instanceof STLiteral)
				{
					STLiteral stLiteral = (STLiteral) stNode;
					if(clsObj.getUri().equals(STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED))
					{
						infoObj.setCreateDate(STUtility.getDisplayDate(DateParser.parseW3CDateTime(stLiteral.getLabel())));
					}
					else if(clsObj.getUri().equals(STModelConstants.DCTNAMESPACE+STModelConstants.DCTMODIFIED))
					{
						infoObj.setUpdateDate(STUtility.getDisplayDate(DateParser.parseW3CDateTime(stLiteral.getLabel())));
					}
					else if(clsObj.getUri().equals(STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS))
					{
						infoObj.setStatus(stLiteral.getLabel());
					}
				}
			}
		}
		
		return infoObj;
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static ConceptObject createConceptObject(OntologyInfo ontoInfo, String conceptURI)
	{
		ConceptObject cObj = new ConceptObject();
		XMLResponseREPLY reply = VocbenchResponseManager.getConceptDescriptionRequest(ontoInfo, conceptURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element conceptInfoElement : STXMLUtility.getChildElementByTagName(dataElement, "conceptInfo"))
			{
				cObj = createConceptShowObject(ontoInfo, conceptInfoElement).getConceptObject();
			}
		}
		return cObj;
	}
	
	/**
	 * @param ontoInfo
	 * @param dataElement
	 * @return
	 */
	/*public static ConceptObject createConceptObject(OntologyInfo ontoInfo, Element conceptInfoElement)
	{
		String conceptURI = "";
		String scheme = "";
		String status = "";
		int statusID = 0;
		Date dateCreate = null;
		Date dateModified = null;
		String parentURI = "";
		HashMap<String,TermObject> term= new HashMap<String,TermObject>();
		boolean rootItem = false;
		boolean hasChild = false;
		
		
			
			for(Element conceptElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "concept"))
			{
				for(Element uriElement : STXMLUtility.getChildElementByTagName(conceptElement, "uri"))
				{
					conceptURI = uriElement.getTextContent();
					scheme = uriElement.getAttribute("scheme");
					status = uriElement.getAttribute("status");
					dateCreate = DateParser.parseW3CDateTime(uriElement.getAttribute("createdDate"));
					dateModified = DateParser.parseW3CDateTime(uriElement.getAttribute("lastUpdate"));
					statusID = OWLStatusConstants.getOWLStatusID(status);
					rootItem = uriElement.getAttribute("isTopConcept").equals("1")?true:false;
					hasChild = uriElement.getAttribute("more").equals("1")?true:false;
				}
			}
			for(Element SuperTypesElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "SuperTypes"))
			{
				
				for(Element collectionElement : STXMLUtility.getChildElementByTagName(SuperTypesElement, "collection"))
				{
					for(Element uriElement : STXMLUtility.getChildElementByTagName(collectionElement, "uri"))
					{
						parentURI = uriElement.getTextContent();
					}
				}
			}
			for(Element labelsElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "labels"))
			{
				for(Element labelcollectionElement : STXMLUtility.getChildElementByTagName(labelsElement, "collection"))
				{
					for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(labelcollectionElement, RDFTypesEnum.plainLiteral.toString()))
					{
						String termURI = typedLiteralElem.getAttribute("termURI");
						String label = typedLiteralElem.getTextContent();
						String lang = typedLiteralElem.getAttribute("lang");
						String termStatus = typedLiteralElem.getAttribute("status");
						int termStatusID = OWLStatusConstants.getOWLStatusID(termStatus);
						boolean isPreferred = Boolean.parseBoolean(typedLiteralElem.getAttribute("isPreferred"));
						Date termDateCreate = DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("createdDate"));
						Date termDateModified = DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("lastUpdate"));
						
						TermObject tObj = STUtility.createTermObject(conceptURI, termURI, label, lang, termStatus, termStatusID, termDateCreate, termDateModified, isPreferred);
						term.put(tObj.getUri(), tObj);
					}				
				}
			}
		
		
		ConceptObject cObj = STUtility.createConceptObject(conceptURI, scheme, status, statusID, dateCreate, dateModified, parentURI, term, rootItem, hasChild);
		
		return cObj;
	}*/
	
	/**
	 * @param ontoInfo
	 * @param dataElement
	 * @return
	 */
	public static ConceptShowObject createConceptShowObject(OntologyInfo ontoInfo, Element conceptInfoElement)
	{
		String conceptURI = "";
		String conceptShow = "";
		String scheme = "";
		String status = "";
		int statusID = 0;
		Date dateCreate = null;
		Date dateModified = null;
		String parentURI = "";
		HashMap<String,TermObject> term= new HashMap<String,TermObject>();
		boolean rootItem = false;
		boolean hasChild = false;
		
		
			
			for(Element conceptElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "concept"))
			{
				for(Element uriElement : STXMLUtility.getChildElementByTagName(conceptElement, "uri"))
				{
					conceptURI = uriElement.getTextContent();
					conceptShow = uriElement.getAttribute("show");
					scheme = uriElement.getAttribute("scheme");
					status = uriElement.getAttribute("status");
					dateCreate = DateParser.parseW3CDateTime(uriElement.getAttribute("createdDate"));
					dateModified = DateParser.parseW3CDateTime(uriElement.getAttribute("lastUpdate"));
					statusID = OWLStatusConstants.getOWLStatusID(status);
					rootItem = uriElement.getAttribute("isTopConcept").equals("1")?true:false;
					hasChild = uriElement.getAttribute("more").equals("1")?true:false;
				}
			}
			for(Element SuperTypesElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "SuperTypes"))
			{
				
				for(Element collectionElement : STXMLUtility.getChildElementByTagName(SuperTypesElement, "collection"))
				{
					for(Element uriElement : STXMLUtility.getChildElementByTagName(collectionElement, "uri"))
					{
						parentURI = uriElement.getTextContent();
					}
				}
			}
			for(Element labelsElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "labels"))
			{
				for(Element labelcollectionElement : STXMLUtility.getChildElementByTagName(labelsElement, "collection"))
				{
					for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(labelcollectionElement, RDFTypesEnum.plainLiteral.toString()))
					{
						String termURI = typedLiteralElem.getAttribute("termURI");
						String label = typedLiteralElem.getTextContent();
						String lang = typedLiteralElem.getAttribute("lang");
						String termStatus = typedLiteralElem.getAttribute("status");
						int termStatusID = OWLStatusConstants.getOWLStatusID(termStatus);
						boolean isPreferred = Boolean.parseBoolean(typedLiteralElem.getAttribute("isPreferred"));
						Date termDateCreate = DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("createdDate"));
						Date termDateModified = DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("lastUpdate"));
						
						TermObject tObj = STUtility.createTermObject(conceptURI, termURI, label, lang, termStatus, termStatusID, termDateCreate, termDateModified, isPreferred);
						term.put(tObj.getUri(), tObj);
					}				
				}
			}
		
		
		ConceptShowObject conceptShowObject = new ConceptShowObject();
		conceptShowObject.setConceptObject(STUtility.createConceptObject(conceptURI, scheme, status, statusID, dateCreate, dateModified, parentURI, term, rootItem, hasChild));
		conceptShowObject.setShow(conceptShow);
		return conceptShowObject;
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @return
	 */
	public static ConceptShowObject createConceptShowObject(OntologyInfo ontoInfo, String conceptURI)
	{
		ConceptShowObject conceptShowObject = new ConceptShowObject();
		XMLResponseREPLY reply = VocbenchResponseManager.getConceptDescriptionRequest(ontoInfo, conceptURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element conceptInfoElement : STXMLUtility.getChildElementByTagName(dataElement, "conceptInfo"))
			{
				conceptShowObject = createConceptShowObject(ontoInfo, conceptInfoElement);
			}
		}
		return conceptShowObject;
	}
	
	/**
	 * @param termName
	 * @param termURI
	 * @param isMainLabel
	 * @param conceptURI
	 * @return
	 */
	public static TermObject createTermObject(OntologyInfo ontoInfo, String termURI)
	{
		TermObject tObj = new TermObject();
		XMLResponseREPLY reply = VocbenchResponseManager.getLabelDescriptionRequest(ontoInfo, termURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(dataElement, RDFTypesEnum.plainLiteral.toString()))
			{
				tObj.setLabel(typedLiteralElem.getTextContent());
				tObj.setLang(typedLiteralElem.getAttribute("lang"));
				tObj.setMainLabel(Boolean.parseBoolean(typedLiteralElem.getAttribute("isPreferred")));
				tObj.setStatus(typedLiteralElem.getAttribute("status"));
				tObj.setDateCreate(DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("createdDate")));
				tObj.setDateModified(DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("lastUpdate")));
				tObj.setConceptUri(typedLiteralElem.getAttribute("conceptURI"));
				//tObj.setConceptName(STUtility.getName(ontoInfo, typedLiteralElem.getAttribute("conceptURI")));
				tObj.setUri(termURI);
				//tObj.setName(STUtility.getName(ontoInfo, termURI));
			}				
		}
		return tObj;
	}
	
	/**
	 * @param ontoInfo
	 * @param termURI
	 * @param label
	 * @param lang
	 * @param isMainLabel
	 * @param conceptURI
	 * @return
	 */
	public static TermObject createTermObject(OntologyInfo ontoInfo, String termURI, String label, String lang, boolean isMainLabel, String conceptURI, boolean explicit)
	{
		TermObject tObj = new TermObject();
		//tObj.setName(STUtility.getName(ontoInfo, termURI));
		tObj.setUri(termURI);
		tObj.setMainLabel(isMainLabel);
		//tObj.setConceptName(STUtility.getName(ontoInfo, conceptURI));
		tObj.setConceptUri(conceptURI);
		tObj.setLabel(label);
		tObj.setLang(lang);
		
		
				
		HashMap<ClassObject, ArrayList<STNode>> list = ResourceManager.getValuesOfProperties(ontoInfo, termURI, PropertyManager.getTermObjectProperties(), explicit);
		for(ClassObject clsObj : list.keySet())
		{
			ArrayList<STNode> stNodes = list.get(clsObj);
			//System.out.println(propURI+" : "+stNodes.size());
			for(STNode stNode : stNodes)
			{
				if(stNode instanceof STLiteral)
				{
					STLiteral stLiteral = (STLiteral) stNode;
					if(clsObj.getUri().equals(STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED))
					{
						tObj.setDateCreate(DateParser.parseW3CDateTime(stLiteral.getLabel()));
					}
					else if(clsObj.getUri().equals(STModelConstants.DCTNAMESPACE+STModelConstants.DCTMODIFIED))
					{
						tObj.setDateModified(DateParser.parseW3CDateTime(stLiteral.getLabel()));
					}
					else if(clsObj.getUri().equals(STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS))
					{
						String status = stLiteral.getLabel();
						tObj.setStatus(status);
						tObj.setStatusID(OWLStatusConstants.getOWLStatusID(status));
					}
					/*else if(clsObj.getUri().equals(STModelConstants.COMMONBASENAMESPACE+STModelConstants.HASCODEAGROVOC))
					{
						AttributesObject aObj = new AttributesObject();
				    	NonFuncObject nfObj = new NonFuncObject();
				    	nfObj.setValue(stLiteral.getLabel());
				    	aObj.setValue(nfObj);
				    	aObj.setRelationshipObject(createRelationshipObject(ontoInfo, clsObj.getUri()));
						tObj.addTermCode(aObj.getRelationshipObject().getUri(), aObj);
					}*/
				}
			}
		}
		return tObj;
	}
	
	/**
	 * @param conceptURI
	 * @param langList
	 * @return
	 */
	public static ConceptTermObject createConceptTermObject(OntologyInfo ontoInfo, String conceptURI){
		ConceptTermObject ctObj = new ConceptTermObject();
		XMLResponseREPLY reply = VocbenchResponseManager.getConceptDescriptionRequest(ontoInfo, conceptURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element conceptInfoElement : STXMLUtility.getChildElementByTagName(dataElement, "conceptInfo"))
			{
				for(Element labelsElement : STXMLUtility.getChildElementByTagName(conceptInfoElement, "labels"))
				{
					for(Element labelcollectionElement : STXMLUtility.getChildElementByTagName(labelsElement, "collection"))
					{
						for(Element typedLiteralElem : STXMLUtility.getChildElementByTagName(labelcollectionElement, RDFTypesEnum.plainLiteral.toString()))
						{
							String termURI = typedLiteralElem.getAttribute("termURI");
							String label = typedLiteralElem.getTextContent();
							String lang = typedLiteralElem.getAttribute("lang");
							String termStatus = typedLiteralElem.getAttribute("status");
							int termStatusID = OWLStatusConstants.getOWLStatusID(termStatus);
							boolean isPreferred = Boolean.parseBoolean(typedLiteralElem.getAttribute("isPreferred"));
							Date termDateCreate = DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("createdDate"));
							Date termDateModified = DateParser.parseW3CDateTime(typedLiteralElem.getAttribute("lastUpdate"));
							
							TermObject tObj = STUtility.createTermObject(conceptURI, termURI, label, lang, termStatus, termStatusID, termDateCreate, termDateModified, isPreferred);
							ctObj.addTermList(tObj.getLang(), tObj);
						}				
					}
				}
			}
		}
		return ctObj;
		//return createConceptTermObject(SKOSXLManager.getLabels(ontoInfo, conceptURI));
	}
	
	/**
	 * @param termObjects
	 * @return
	 */
	public static ConceptTermObject createConceptTermObject(ArrayList<TermObject> termObjects){
		ConceptTermObject ctObj = new ConceptTermObject();
		for(TermObject tObj : termObjects)
		{	
			ctObj.addTermList(tObj.getLang(), tObj);
		}
		return ctObj;
	}
	
	/**
	 * @param conceptURI
	 * @param prefList
	 * @param altList
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public static String createTreeObjectLabel(String conceptURI, Collection<STLiteral> prefList, Collection<STLiteral> altList, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		
		ArrayList<String> sortedList = new ArrayList<String>();
		ArrayList<String> checkMainLabelList = new ArrayList<String>();
		
		for(STLiteral stLiteral : prefList)
		{
			String label = stLiteral.getLabel();
			String language = stLiteral.getLanguage();
			String status = stLiteral.getRendering();
				
			if((langList!=null && langList.contains(language)) && STUtility.checkDeprecated(status, isHideDeprecated)){
				sortedList.add(language+"###"+label);
				checkMainLabelList.add(language+"###"+label);
			}	
		}
		
		if(showAlsoNonpreferredTerms)
		{
			for(STLiteral stLiteral : altList)
			{
				String label = stLiteral.getLabel();
				String language = stLiteral.getLanguage();
				String status = stLiteral.getRendering();
					
				if((langList!=null && langList.contains(language)) && STUtility.checkDeprecated(status, isHideDeprecated)){
					sortedList.add(language+"###"+label);
				}	
			}
		}
		
		String termLabel = getSortedTreeObjectLabel(sortedList, checkMainLabelList, langList, conceptURI);
		return termLabel;
	}
	
	/**
	 * @param cObj
	 * @param showAlsoNonpreferredTerms
	 * @param isHideDeprecated
	 * @param langList
	 * @return
	 */
	public static String createTreeObjectLabel(ConceptObject cObj, boolean showAlsoNonpreferredTerms, boolean isHideDeprecated, ArrayList<String> langList)
	{
		
		ArrayList<String> sortedList = new ArrayList<String>();
		ArrayList<String> checkMainLabelList = new ArrayList<String>();
		
		HashMap<String, TermObject> termList = cObj.getTerm();
		
		for(TermObject tObj : termList.values())
		{
		 	String label = tObj.getLabel();
			String language = tObj.getLang();
			String status = tObj.getStatus();
			
			if(tObj.isMainLabel())
			{
				if((langList!=null && langList.contains(language)) && STUtility.checkDeprecated(status, isHideDeprecated)){
					sortedList.add(language+"###"+label);
					checkMainLabelList.add(language+"###"+label);
				}	
			}
			else
			{
				if((langList!=null && langList.contains(language)) && STUtility.checkDeprecated(status, isHideDeprecated)){
					sortedList.add(language+"###"+label);
				}	
			}
		}

		String termLabel = getSortedTreeObjectLabel(sortedList, checkMainLabelList, langList, cObj.getUri());
		return termLabel;
	}
		
	/**
	 * @param sortedList
	 * @param checkMainLabelList
	 * @param langList
	 * @param conceptURI
	 * @return
	 */
	private static String getSortedTreeObjectLabel(ArrayList<String> sortedList, ArrayList<String> checkMainLabelList, ArrayList<String> langList, String conceptURI)
	{
		Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
		
		HashMap<String, StringBuffer> sortedLangMap = new HashMap<String, StringBuffer>();
		
		String termLabel = "";
		for (int i = 0; i < sortedList.size(); i++) {
			String str =  sortedList.get(i);
			String[] element = str.split("###");
			String separator = "; ";
			if(i==(sortedList.size()-1))
				separator = "";
			if(element.length==2){
				
				StringBuffer sb = sortedLangMap.get(element[0]);
				if(sb==null)
					sb = new StringBuffer();
				else
					sortedLangMap.remove(element[0]);
				
				if(checkMainLabelList.contains(str))
				{
					sb.append("<b>"+ element[1] + " ("+element[0]+")"+separator+"</b>");
				}
				else
				{
					sb.append(element[1] + " ("+element[0]+")"+separator);
				}
				sortedLangMap.put(element[0], sb);
				
			}
		}
		
		for(String lang : langList)
		{
			StringBuffer lblBuffer = sortedLangMap.get(lang);
			if(lblBuffer!=null)	termLabel = termLabel + lblBuffer.toString();
		}
		
		if(termLabel.length()==0)
			termLabel = "###EMPTY###"+conceptURI;
		
		return termLabel;
	}
	
	/**
	 * @param ontoInfo
	 * @param propertyName
	 * @param rootpropertyName
	 * @return
	 *//*
	public static RelationshipTreeObject createRelationshipTreeObject1(OntologyInfo ontoInfo, String propertyName, String rootpropertyName)
	{
		
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		String rootProp = "";
		ArrayList<LabelObject> propertyDefinitions = new ArrayList<LabelObject>();
		
		RelationshipObject rObj = new RelationshipObject();
		rObj.setUri(STUtility.getUri(ontoInfo, propertyName));
		rObj.setName(propertyName);
		rObj.setType(RelationshipObject.OBJECT);
		rObj.setRootItem(propertyName.equals(rootpropertyName));
		
		XMLResponseREPLY reply = PropertyResponseManager.getPropertyDescriptionRequest(ontoInfo, propertyName);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			if(dataElement.getAttribute("type").equals(SKOS.templateandvalued))
			{
				// SuperTypes
				for(Element TypesElement : STXMLUtility.getChildElementByTagName(dataElement, "SuperTypes"))
				{
					for(Element colElement : STXMLUtility.getChildElementByTagName(TypesElement, "collection"))
					{
						for(Element uriElem : STXMLUtility.getChildElementByTagName(colElement, RDFTypesEnum.uri.toString()))
						{
							rootProp = uriElem.getTextContent();
							rObj.setParent(rootProp);
						}
					}
				}
				
				// label, definition
				for(Element propertiesElement : STXMLUtility.getChildElementByTagName(dataElement, "Properties"))
				{
					for(Element propertyElem : STXMLUtility.getChildElementByTagName(propertiesElement, "Property"))
					{
						if(propertyElem.getAttribute("name").equals("rdfs:label"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.plainLiteral.toString()))
							{
								rObj.addLabel(uriElem.getTextContent(), uriElem.getAttribute("lang"));
							}
						}
						else if(propertyElem.getAttribute("name").equals("rdfs:comments"))
						{
							for(Element uriElem : STXMLUtility.getChildElementByTagName(propertyElem, RDFTypesEnum.plainLiteral.toString()))
							{
								LabelObject label = new LabelObject();
					    		label.setLabel(uriElem.getTextContent());
					    		label.setLanguage(uriElem.getAttribute("lang"));
					    		propertyDefinitions.add(label);
							}
						}
					}
				}
			}
		}
		
		rtObj.addRelationshipList(rObj);
		rtObj.addParentChild(rootProp, rObj);
		rtObj.addRelationshipDefinition(rObj.getUri() , propertyDefinitions);
		return rtObj;
	}*/
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static ImageObject createImageObject(OntologyInfo ontoInfo, String conceptURI)
	{

		ImageObject dObj = new ImageObject();
		XMLResponseREPLY reply = VocbenchResponseManager.getConceptImageRequest(ontoInfo, conceptURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			for(Element definitonsElement : STXMLUtility.getChildElementByTagName(dataElement, "images"))
			{
				for(Element definitionElem : STXMLUtility.getChildElementByTagName(definitonsElement, "image"))
				{
					IDObject idObj = new IDObject();
					idObj.setIDType(IDObject.IMAGE);
					
					for(Element uriElem : STXMLUtility.getChildElementByTagName(definitionElem, "uri"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(uriElem, RDFTypesEnum.uri.toString()))
						{
							idObj.setIDUri(elem.getTextContent());
						}
					}
				
					for(Element createdElem : STXMLUtility.getChildElementByTagName(definitionElem, "created"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(createdElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDDateCreate(DateParser.parseW3CDateTime(elem.getTextContent()));
						}
					}
					
					for(Element modifiedElem : STXMLUtility.getChildElementByTagName(definitionElem, "modified"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(modifiedElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDDateModified(DateParser.parseW3CDateTime(elem.getTextContent()));
						}
					}
					
					
					for(Element sourceLinkElem : STXMLUtility.getChildElementByTagName(definitionElem, "sourceLink"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(sourceLinkElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDSourceURL(elem.getTextContent());
						}
					}
					
					for(Element sourceElem : STXMLUtility.getChildElementByTagName(definitionElem, "source"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(sourceElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDSource(elem.getTextContent());
						}
					}
					
					HashMap<String, TranslationObject> tObjList = new HashMap<String, TranslationObject>();
					
					for(Element labelElem : STXMLUtility.getChildElementByTagName(definitionElem, "label"))
					{
						
						for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(labelElem))
						{
							TranslationObject tObj = tObjList.get(stLiteral.getLanguage());
							if(tObj == null)
								tObj = new TranslationObject();
							else
								tObjList.remove(stLiteral.getLanguage());
							tObj.setLabel(stLiteral.getLabel());
				    		tObj.setLang(stLiteral.getLanguage());
				    		tObjList.put(stLiteral.getLanguage(), tObj);
						}
					}
					
					for(Element labelElem : STXMLUtility.getChildElementByTagName(definitionElem, "comment"))
					{
						for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(labelElem))
						{
							TranslationObject tObj = tObjList.get(stLiteral.getLanguage());
							if(tObj == null)
								tObj = new TranslationObject();
							else
								tObjList.remove(stLiteral.getLanguage());
							tObj.setDescription(stLiteral.getLabel());
				    		tObjList.put(stLiteral.getLanguage(), tObj);
						}
					}
					
					for(String lang : tObjList.keySet())
					{
						TranslationObject tObj = tObjList.get(lang);
						tObj.setType(TranslationObject.DEFINITIONTRANSLATION);
						tObj.setUri(idObj.getIDUri());
						idObj.addIDTranslationList(tObj);
					}
					if(idObj.getIDUri()!=null)
						dObj.addImageList(idObj.getIDUri(), idObj);
				}
			}
		}
		return dObj;
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static DefinitionObject createDefinitionObject(OntologyInfo ontoInfo, String conceptURI)
	{

		DefinitionObject dObj = new DefinitionObject();
		XMLResponseREPLY reply = VocbenchResponseManager.getConceptDefinitionRequest(ontoInfo, conceptURI);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			
			for(Element definitonsElement : STXMLUtility.getChildElementByTagName(dataElement, "definitions"))
			{
				for(Element definitionElem : STXMLUtility.getChildElementByTagName(definitonsElement, "definition"))
				{
					IDObject idObj = new IDObject();
					idObj.setIDType(IDObject.DEFINITION);
					
					for(Element uriElem : STXMLUtility.getChildElementByTagName(definitionElem, "uri"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(uriElem, RDFTypesEnum.uri.toString()))
						{
							idObj.setIDUri(elem.getTextContent());
						}
					}
				
					for(Element createdElem : STXMLUtility.getChildElementByTagName(definitionElem, "created"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(createdElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDDateCreate(DateParser.parseW3CDateTime(elem.getTextContent()));
						}
					}
					
					for(Element modifiedElem : STXMLUtility.getChildElementByTagName(definitionElem, "modified"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(modifiedElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDDateModified(DateParser.parseW3CDateTime(elem.getTextContent()));
						}
					}
					
					
					for(Element sourceLinkElem : STXMLUtility.getChildElementByTagName(definitionElem, "sourceLink"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(sourceLinkElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDSourceURL(elem.getTextContent());
						}
					}
					
					for(Element sourceElem : STXMLUtility.getChildElementByTagName(definitionElem, "source"))
					{
						for(Element elem : STXMLUtility.getChildElementByTagName(sourceElem, RDFTypesEnum.typedLiteral.toString()))
						{
							idObj.setIDSource(elem.getTextContent());
						}
					}
					
					for(Element labelElem : STXMLUtility.getChildElementByTagName(definitionElem, "label"))
					{
						for(STLiteral stLiteral : STXMLUtility.getPlainLiteral(labelElem))
						{
							TranslationObject tObj = new TranslationObject();
							tObj.setLabel(stLiteral.getLabel());
				    		tObj.setLang(stLiteral.getLanguage());
				    		tObj.setType(TranslationObject.DEFINITIONTRANSLATION);
				    		tObj.setUri(idObj.getIDUri());
				    		idObj.addIDTranslationList(tObj);
						}
					}
					
					if(idObj.getIDUri()!=null)
						dObj.addDefinitionList(idObj.getIDUri(), idObj);
				}
			}
		}
		return dObj;
	}
	
}
