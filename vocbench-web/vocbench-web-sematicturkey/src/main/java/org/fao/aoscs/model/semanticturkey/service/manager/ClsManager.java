package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ClassTreeObject;
import org.fao.aoscs.domain.ClassTreeObjectItem;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ClsResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class ClsManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ClsManager.class);
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static ClassTreeObject getClassTree(OntologyInfo ontoInfo)
	{
		ClassTreeObject ctObj = new ClassTreeObject();
		Response resp= ClsResponseManager.getClassTreeRequest(ontoInfo);
		
		Element dataElement = ((XMLResponseREPLY) resp).getDataElement();
		for(Element classElem : STXMLUtility.getChildElementByTagName(dataElement, "Class"))
		{
			ctObj = getClassDetail(ontoInfo, ctObj, classElem.getAttribute("name"), Boolean.parseBoolean(classElem.getAttribute("deleteForbidden")), classElem.getAttribute("numInst"), null, true);
			ctObj = getChildClass(ontoInfo, classElem, ctObj);
		}
		return ctObj;
	}
	
	private static ClassTreeObject getClassDetail(OntologyInfo ontoInfo, ClassTreeObject ctObj, String name, boolean deleteForbidden, String numInst, String parentURI, boolean rootItem)
	{
		ClassTreeObjectItem cObj = STUtility.createClassTreeObjectItem(name, deleteForbidden, numInst, rootItem);
		
		ctObj.addClassTreeList(cObj);
		ctObj.addParentChild(parentURI, cObj);
		return ctObj;
	}
	
	private static ClassTreeObject getChildClass(OntologyInfo ontoInfo, Element dataElement, ClassTreeObject ctObj)
	{
		for(Element subClassElem : STXMLUtility.getChildElementByTagName(dataElement, "SubClasses"))
		{
			for(Element classElem : STXMLUtility.getChildElementByTagName(subClassElem, "Class"))
			{
				ctObj = getClassDetail(ontoInfo, ctObj, classElem.getAttribute("name"), Boolean.parseBoolean(classElem.getAttribute("deleteForbidden")), classElem.getAttribute("numInst"), dataElement.getAttribute("name"), false);
				ctObj = getChildClass(ontoInfo, classElem, ctObj);
			}
		}
		return ctObj;
	}
	
	/**
	 * @param clsName
	 * @return
	 */
	public static ArrayList<ClassObject> getSubClasses(OntologyInfo ontoInfo, String clsName)
	{
		ArrayList<ClassObject> classObjList = new ArrayList<ClassObject>();
		Response resp= ClsResponseManager.getSubClassesRequest(ontoInfo, clsName, true, true);
		
		Element dataElement = ((XMLResponseREPLY) resp).getDataElement();
		for(Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
		{
			for(Element uriElement : STXMLUtility.getChildElementByTagName(colElem, "uri"))
			{
				ClassObject cObj = new ClassObject();
				String show = uriElement.getAttribute("show");
				//String numInst = uriElement.getAttribute("numInst");
				cObj.setUri(uriElement.getTextContent());
				cObj.setLabel(show);
				cObj.setName(show);
				if(uriElement.getAttribute("more").equals("1")?true:false){
		    		cObj.setHasChild(true);
		    	}else{
		    		cObj.setHasChild(false);
		    	}
				classObjList.add(cObj);
			}
		}
		
		return classObjList;
	}
	
	
	
}
