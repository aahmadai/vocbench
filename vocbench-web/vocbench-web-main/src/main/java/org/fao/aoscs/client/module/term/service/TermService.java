package org.fao.aoscs.client.module.term.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InformationObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.TermDetailObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TermRelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("term")
public interface TermService extends RemoteService{
	public TermDetailObject getTermDetail(OntologyInfo ontoInfo, ArrayList<String> langList, String termURI, boolean isExplicit) throws Exception;
	public InformationObject getTermInformation(String termURI, OntologyInfo ontoInfo) throws Exception;
	TermRelationshipObject getTermRelationship(String cls, String termIns, boolean isExplicit,OntologyInfo ontoInfo) throws Exception;
	//public ArrayList<NonFuncObject> getTermNonFunc(String cls,String termIns,String property, OntologyInfo ontoInfo) throws Exception;
	
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getTermNotationValue(String cls, String termIns, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getTermAttributeValue(String cls, String termIns, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;

	//public TermCodeObject getTermCode(String cls,String termIns, OntologyInfo ontoInfo) throws Exception;
	//public HashMap<String, String> getTermCodes(ArrayList<String> terms, OntologyInfo ontoInfo) throws Exception;
	
	public HashMap<String, String> getTermNotation(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	public HashMap<String, String> getTermAttributes(String resourceURI, boolean isExplicit, OntologyInfo ontoInfo) throws Exception;
	
	
	public void addTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId, String propertyURI,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject) throws Exception;
	public void editTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String oldPropertyURI, String newPropertyURI,TermObject termObj,TermObject oldDestTermObj,TermObject newDestTermObj,ConceptObject conceptObject) throws Exception;
	public void deleteTermRelationship(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,TermObject termObj,TermObject destTermObj,ConceptObject conceptObject) throws Exception;
	
/*	public void addTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject) throws Exception;
	public void editTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String oldValue,String oldLanguage,String newValue,String newLanguage,TermObject termObject,ConceptObject conceptObject) throws Exception;
	public void deleteTermNoneFuncValue(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,String propertyURI,String value,String language,TermObject termObject,ConceptObject conceptObject) throws Exception;
*/
	public void addPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject value, String propertyURI, DomainRangeObject drObj, TermObject termObject,ConceptObject conceptObject) throws Exception;
	public void editPropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue, String propertyURI, DomainRangeObject drObj, TermObject termObject,ConceptObject conceptObject) throws Exception;
	public void deletePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue, String propertyURI, TermObject termObject,ConceptObject conceptObject) throws Exception;

	public void updatePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,String relationshipUri,TermObject termObject,ConceptObject conceptObject) throws Exception;
	public ConceptTermObject getConceptTermObject(String cls, OntologyInfo ontoInfo) throws Exception;
	
	/*public void addTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject) throws Exception;
	public void editTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject oldTcObj,TermCodesObject newTcObj,TermObject termObject,ConceptObject conceptObject) throws Exception;
	public void deleteTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject) throws Exception;*/
	
	public static class TermServiceUtil{
		private static TermServiceAsync<?> instance;
		public static TermServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (TermServiceAsync<?>) GWT.create(TermService.class);
			}
			return instance;
		}
    }
}

