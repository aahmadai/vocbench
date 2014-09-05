package org.fao.aoscs.model.semanticturkey.util;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.domain.ARTBNodeObject;
import org.fao.aoscs.domain.ARTLiteralObject;
import org.fao.aoscs.domain.ARTURIResourceObject;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.model.semanticturkey.STModelConstants;
import org.fao.aoscs.model.semanticturkey.service.manager.PropertyManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSManager;
import org.fao.aoscs.model.semanticturkey.service.manager.VocbenchManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SKOSXLResponseManager;
import org.fao.aoscs.server.utility.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sun.syndication.io.impl.DateParser;

/**
 * @author rajbhandari
 *
 */
public class STUtility {
	
	protected static Logger logger = LoggerFactory.getLogger(STUtility.class);
	
	/**
	 * @param uri
	 * @param name
	 * @param conceptInstance
	 * @param nameSpace
	 * @param scheme
	 * @param statusID
	 * @param status
	 * @param dateCreate
	 * @param dateModified
	 * @param parentInstance
	 * @param parentURI
	 * @param parentObject
	 * @param term
	 * @param rootItem
	 * @param displayedConceptLabel
	 * @param hasChild
	 * @param belongsToModule
	 * @return
	 */
	public static ConceptObject createConceptObject(String uri, String scheme, String status, int statusID, Date dateCreate, Date dateModified, String parentURI,  HashMap<String,TermObject> term, boolean rootItem, boolean hasChild)
	{
		ConceptObject cObj = new ConceptObject();
		cObj.setUri(uri);
		//cObj.setName(name);
		//cObj.setConceptInstance(conceptInstance);
		//cObj.setNameSpace(nameSpace);
		cObj.setScheme(scheme);

		cObj.setStatusID(statusID);
		cObj.setStatus(status);
		cObj.setDateCreate(dateCreate);
		cObj.setDateModified(dateModified);
		
		//cObj.setParentInstance(parentInstance);
		cObj.setParentURI(parentURI);
		//cObj.setParentObject(parentObject);
		
		//cObj.setDisplayedConceptLabel(displayedConceptLabel);
		cObj.setTerm(term);
		cObj.setRootItem(rootItem);
		
		cObj.setHasChild(hasChild);
		//cObj.setBelongsToModule(belongsToModule);
		
		return cObj;
	}
	
	/**
	 * 
	 * @param name
	 * @param uri
	 * @param label
	 * @param status
	 * @param hasChild
	 * @return
	 */
	public static TreeObject createTreeObject(OntologyInfo ontoInfo, String uri, String label, String status, boolean hasChild)
	{
		TreeObject treeObj = new TreeObject();
		treeObj.setUri(uri);
		treeObj.setStatus(status);
		treeObj.setLabel(label);
		treeObj.setHasChild(hasChild);
		
		return treeObj;
	}
	
	/**
	 * 
	 * @param conceptUri
	 * @param conceptName
	 * @param uri
	 * @param name
	 * @param label
	 * @param lang
	 * @param statusID
	 * @param status
	 * @param dateCreate
	 * @param dateModified
	 * @param mainLabel
	 * @return
	 */
	public static TermObject createTermObject(String conceptUri, String termUri, String label, String lang, String status, int statusID, Date dateCreate, Date dateModified, boolean isPreferred)
	{
		TermObject termObj = new TermObject();
		termObj.setConceptUri(conceptUri);
		//termObj.setConceptName(conceptName);
		termObj.setUri(termUri);
		//termObj.setName(termName);
		termObj.setLabel(label);
		termObj.setLang(lang);
		termObj.setStatusID(statusID);
		termObj.setStatus(status);
		termObj.setDateCreate(dateCreate);
		termObj.setDateModified(dateModified);
		termObj.setMainLabel(isPreferred);
		return termObj;
	}
	
	/**
	 * 
	 * @param label
	 * @param lang
	 * @return
	 */
	public static LabelObject createLabelObject(String label, String lang)
	{
		LabelObject labelObj = new LabelObject();
		labelObj.setLabel(label);
		labelObj.setLanguage(lang);
		return labelObj;
	}
	
	/**
	 * 
	 * @param uri
	 * @param name
	 * @param labelList
	 * @param parentURI
	 * @param parentObject
	 * @param rootItem
	 * @param type
	 * @param isFunctional
	 * @param domainRangeDatatypeObject
	 * @return
	 */
	public static RelationshipObject createRelationshipObject(String uri, String name, ArrayList<LabelObject> labelList, String parentURI, RelationshipObject parentObject, boolean rootItem, String type, boolean isFunctional, DomainRangeObject domainRangeDatatypeObject)
	{
		RelationshipObject relObj = new RelationshipObject();
		relObj.setUri(uri);
		relObj.setName(name);
		relObj.setLabelList(labelList);
		relObj.setParent(parentURI);
		relObj.setRootItem(rootItem);
		relObj.setType(type);
		relObj.setFunctional(isFunctional);
		relObj.setDomainRangeObject(domainRangeDatatypeObject);
		return relObj;
	}
	
	/**
	 * 
	 * @param uri
	 * @param label
	 * @param type
	 * @param name
	 * @param hasChild
	 * @return
	 */
	public static ClassObject createClassObject(String uri, String label, String type, String name, boolean hasChild)
	{
		ClassObject classObj = new ClassObject();
		classObj.setUri(uri);
		classObj.setLabel(label);
		classObj.setType(type);
		classObj.setName(name);
		classObj.setHasChild(hasChild);
		return classObj;
	}
	
	/**
	 * @param uri
	 * @param name
	 * @param dateCreate
	 * @param dateModified
	 * @param source
	 * @param sourceURL
	 * @param idtList
	 * @param type
	 * @return
	 */
	public static IDObject createIDObject(String uri, Date dateCreate, Date dateModified, String source, String sourceURL, ArrayList<TranslationObject> idtList, int type)
	{
		IDObject idObj = new IDObject();
		idObj.setIDType(type);
		idObj.setIDUri(uri);
		idObj.setIDDateCreate(dateCreate);
		idObj.setIDDateModified(dateModified);
		idObj.setIDSource(source);
		idObj.setIDSourceURL(sourceURL);
		idObj.setIDTranslationList(idtList);
		return idObj;
	}
	
	/**
	 * @param uri
	 * @param label
	 * @param lang
	 * @param description
	 * @param type
	 * @return
	 */
	public static TranslationObject createTranslationObject(String uri, String label, String lang, String description, int type)
	{
		TranslationObject trObj = new TranslationObject();
		trObj.setUri(uri);
		trObj.setLabel(label);
		trObj.setLang(lang);
		trObj.setDescription(description);
		trObj.setType(type);
		return trObj;
	}
	
	/**
	 * @param resource
	 * @return
	 */
	public static ARTURIResourceObject createARTURIResourceObject(STResource resource)
	{
		ARTURIResourceObject stObj = new ARTURIResourceObject();
		stObj.setExplicit(resource.isExplicit());
		stObj.setRole(resource.getRole().name());
		stObj.setShow(resource.getRendering());
		stObj.setUri(resource.getARTNode().asURIResource().getURI());
		//System.out.println("ARTURIResourceObject: "+stObj.getShow()+" : "+stObj.getNominalValue());
		return stObj;
	}
	
	/**
	 * @param resource
	 * @return
	 */
	public static ARTLiteralObject createARTLiteralObject(STLiteral resource, boolean isTypedLiteral)
	{
		ARTLiteralObject stObj = new ARTLiteralObject();
		stObj.setLabel(resource.getARTNode().asLiteral().getLabel());
		if(isTypedLiteral)
		{
			stObj.setLang("");
			stObj.setDatatype(resource.getARTNode().asLiteral().getDatatype().getURI());
		}
		else
		{
			stObj.setLang(resource.getARTNode().asLiteral().getLanguage());
			stObj.setDatatype("");
		}
		stObj.setTypedLiteral(isTypedLiteral);
		stObj.setShow(resource.getRendering());
		stObj.setExplicit(resource.isExplicit());
		//System.out.println("ARTLiteralObject: "+stObj.getNominalValue());
		return stObj;
	}
	
	
	/**
	 * @param resource
	 * @return
	 */
	public static ARTBNodeObject createARTBNodeObject(STResource resource)
	{
		ARTBNodeObject stObj = new ARTBNodeObject();
		stObj.setId(resource.getARTNode().asBNode().getID());
		stObj.setExplicit(resource.isExplicit());
		stObj.setShow(resource.getRendering());
		//System.out.println("ARTBNodeObject: "+stObj.getNominalValue());
		return stObj;
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static boolean checkNull(String str)
	{
		return (str==null || str.equals("") || str.equals("null"));
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkBoolean(String str)
	{
		return (str.equals("true"))?true:false;
	}
	
	/**
	 * @param status
	 * @param isHideDeprecated
	 * @return
	 */
	public static boolean checkDeprecated(String status, boolean isHideDeprecated)
    {
        boolean chk = true;
        if (isHideDeprecated)
        {
            if (/*
                 * status.equals(OWLStatusConstants.PROPOSED_DEPRECATED_STRINGVALUE
                 * ) ||
                 */status.equals(OWLStatusConstants.DEPRECATED))
                chk = false;
        }
        return chk;
    }
	
	/**
	 * @param lObj
	 * @return
	 */
	public static String getLabelLang(LabelObject lObj)
	{
		String str = "";
		if(lObj!=null)
		{
			if(lObj.getLabel()!=null && !lObj.getLabel().equals(""))
			{
				str = lObj.getLabel();
				if(lObj.getLanguage()!=null && !lObj.getLanguage().equals(""))
					str += " ("+lObj.getLanguage()+")";
			}
		}
		return str;
	}
	
	/**
	 * @param obj
	 */
	public static void printObject(Object obj)
	{
		
		 try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
				  if (pd.getReadMethod() != null && !"class".equals(pd.getName()))
				  {
					  String name = pd.getName();
					  Object value = pd.getReadMethod().invoke(obj);
					  if(value!=null)
						  System.out.println(name+" : "+value);
				  }
				}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Date getW3CDate(String date)
	{
		date = date.trim();
		if(date!=null && !date.equals("") && !date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");  
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				Date d = df.parse(date);
				return d;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return getDate(date, "EEE MMM d  HH:mm:ss z yyyy") ;
			}
		}
		else
			return null;
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static String getDisplayDate(Date d) 
	{
		if(d!=null)
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				return df.format(d);
			}
			catch(Exception e)
			{
				return d.toString();
			}
		}
		else
			return "";
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static String getDisplayDate(String date) 
	{
		if(!date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				Date d = DateParser.parseW3CDateTime(date);
				return df.format(d);
			}
			catch(Exception e)
			{
				return date;
			}
		}
		else
			return null;
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Date getDate(String date) 
	{
		if(!date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return getDate(date, "EEE MMM d  HH:mm:ss z yyyy") ;
			}
		}
		else
			return null;
	}
	
	/**
	 * @param date 
	 * @param format
	 * @return
	 */
	public static Date getDate(String date, String format) 
	{
		if(!date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat(format);
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else
			return null;
	}
	
	/**
	 * @param str
	 * @param delimeter
	 * @return
	 */
	public static String getcamelCased(String str)
	{
		str = WordUtils.capitalize(str).replaceAll(" ", "");
		if (str.length() > 0) 
		{             
			str = str.substring(0, 1).toLowerCase()+ str.substring(1);         
		} 
		return str;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public static String getNamespace(String uri) {
		if (uri.contains("#"))			
			return uri.split("#")[0]+"#";
		else {
			int index = uri.lastIndexOf("/");
			return uri.substring(0, index+1);
		}
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	/*public static String getLocalName(String uri) {
		if (uri.contains("#"))			
			return uri.split("#")[1];
		else {
			String[] contents = uri.split("/");
			return contents[contents.length-1];
		}
	}*/
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	/*public static String getName(OntologyInfo ontoInfo, String uri) {
		String namespacePrefix = MetadataManager.getPrefixForNamespace(ontoInfo, getNamespace(uri));
		if(!namespacePrefix.equals(""))
		{
			namespacePrefix += ":";
			return namespacePrefix+getLocalName(uri);
		}
		else
			return uri;
	}*/
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	/*public static String getUri(OntologyInfo ontoInfo, String name) {
		if (name.contains(":"))
		{
			String[] str = name.split(":");
			if(str.length>1)
			{
				String ns = MetadataManager.getNamespaceForPrefix(ontoInfo, str[0]);
				if(ns.equals(""))
					return name;
				else
					return ns+str[1];
			}
			else
				return name; 
		}
		else 
		{
			return name;
		}
	}*/
	
	/**
	 * @param resourceURI
	 */
	public static void setInstanceUpdateDate(OntologyInfo ontoInfo, String resourceURI){
		//PropertyManager.removeAllPropValue(ontoInfo, resourceURI, STModelConstants.DCTNAMESPACE+STModelConstants.DCTMODIFIED);
		//PropertyManager.addTypedLiteralPropValue(ontoInfo, resourceURI, STModelConstants.DCTNAMESPACE+STModelConstants.DCTMODIFIED, DateParser.formatW3CDateTime(DateUtility.getROMEDate()), "xsd:"+XmlSchema.DATETIME);
		VocbenchManager.updateResourceModifiedDate(ontoInfo, resourceURI, "en");
	}
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param status
	 */
	public static void setInstanceStatus(OntologyInfo ontoInfo, String resourceURI, String status){
		PropertyManager.removeAllPropValue(ontoInfo, resourceURI, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS);
		PropertyManager.addPlainLiteralPropValue(ontoInfo, resourceURI, STModelConstants.VOCBENCHNAMESPACE+STModelConstants.HASSTATUS, status, null);
	}
	
	/**
	 * @param termUri
	 * @return
	 */
	public static java.util.Date getCreatedDate(OntologyInfo ontoInfo, String resourceURI)
	{
		for(STLiteral stLiteral : PropertyManager.getTypedLiteralPropValues(ontoInfo, resourceURI, STModelConstants.DCTNAMESPACE+STModelConstants.DCTCREATED))
		{
			String dateVal = stLiteral.getLabel().trim();
			if(!dateVal.equals(""))
	    		return DateParser.parseW3CDateTime(dateVal);
		}
		return DateUtility.getROMEDate();
	}
	
	/**
	 * @param conceptURI
	 * @param label
	 * @param lang
	 * @return
	 */
	public static String getTermURI(OntologyInfo ontoInfo, String conceptURI, String label, String lang, boolean isPreferred)
	{
		XMLResponseREPLY reply = SKOSXLResponseManager.getLabelsRequest(ontoInfo, conceptURI, lang);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			if(isPreferred)
			{
				for(Element prefElem : STXMLUtility.getChildElementByTagName(dataElement, "prefLabel"))
				{
					for(STResource stResource : STXMLUtility.getURIResource(prefElem))
					{
						if(stResource.getRendering().equals(label) && stResource.getInfo("lang").equals(lang))
						return stResource.getARTNode().asURIResource().getURI();
					}
				}
			}
			else
			{
				for(Element altElem : STXMLUtility.getChildElementByTagName(dataElement, "altLabels"))
				{
					for(Element elem : STXMLUtility.getChildElementByTagName(altElem, "collection"))
					{
						for(STResource stResource : STXMLUtility.getURIResource(elem))
						{
							if(stResource.getRendering().equals(label) && stResource.getInfo("lang").equals(lang))
								return stResource.getARTNode().asURIResource().getURI();
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @param ontoInfo
	 * @param oldSchemeURI
	 * @param newSchemeURI
	 * @param conceptURI
	 * @param parentConceptURI
	 * @param status
	 * @param actionId
	 * @param userId
	 */
	public static void linkConcept(OntologyInfo ontoInfo, String oldSchemeURI, String newSchemeURI, String conceptURI, String parentConceptURI){
		
		if(parentConceptURI==null || parentConceptURI.equals(""))
		{
			SKOSManager.addTopConcept(ontoInfo, conceptURI, newSchemeURI);
		}
		else if(!conceptURI.equals(parentConceptURI))
		{
			boolean isTopconcept = SKOSManager.isTopConcept(ontoInfo, conceptURI, oldSchemeURI);
			SKOSManager.addBroaderConcept(ontoInfo, conceptURI, parentConceptURI);
			
			// TODO on ST UPDATE : Fixes for when adding concept which is a top concept to different broader concept, it removes itself as top concept.
			try
			{
				if(isTopconcept)
					SKOSManager.addTopConcept(ontoInfo, conceptURI, oldSchemeURI);
			}
			catch(Exception e)
			{
				logger.debug(e.getLocalizedMessage());
			}
		}
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeUri
	 * @param conceptURI
	 * @param parentConceptURI
	 * @param status
	 * @param actionId
	 * @param userId
	 * @return
	 */
	public static Integer unlinkConcept(OntologyInfo ontoInfo, String schemeUri, String conceptURI, String parentConceptURI){
		int cnt = 0;
		if(SKOSManager.isTopConcept(ontoInfo, conceptURI, schemeUri))
			cnt = 1;
		ArrayList<String> broaderList = SKOSManager.getBroaderConceptsURI(ontoInfo, conceptURI, schemeUri);
		if(broaderList!=null)
			cnt += broaderList.size();
		if(cnt>1)
		{
			if(parentConceptURI==null || parentConceptURI.equals(""))
			{
				SKOSManager.removeTopConcept(ontoInfo, conceptURI, schemeUri);
			}
			else 
				SKOSManager.removeBroaderConcept(ontoInfo, conceptURI, parentConceptURI);
		}
		return cnt;
	}
	
	public static String convertArrayToString(ArrayList<?> list, String separator)
	{
		String value = null;
		if(list!=null)
		{
			for(Object obj : list)
			{
				if(value!=null)
					value += separator;
				else
					value = "";
				value += obj;
			}
		}
		return value;
	}
	
	public static void printBean(Object bean) {
	   System.out.println("bean: "+ToStringBuilder.reflectionToString(bean));
	 }
	
}
