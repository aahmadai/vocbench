package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ClsResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
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
