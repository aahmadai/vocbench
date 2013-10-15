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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TermServiceAsync<T> {
	void getTermDetail(OntologyInfo ontoInfo, ArrayList<String> langList,
			String termURI, boolean isExplicit,
			AsyncCallback<TermDetailObject> callback);
	public void getTermInformation(String termURI, OntologyInfo ontoInfo, AsyncCallback<InformationObject> callback);
	void getTermRelationship(String cls, String termIns, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<TermRelationshipObject> callback);
	void getTermNotationValue(
			String cls,
			String termIns,
			boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	void getTermAttributeValue(
			String cls,
			String termIns,
			boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback);
	
	
	//public void getTermCode(String cls,String termIns, OntologyInfo ontoInfo, AsyncCallback<TermCodeObject> callback);
	//public void getTermCodes(ArrayList<String> terms, OntologyInfo ontoInfo, AsyncCallback<HashMap<String, String>> callback);
	
	void addTermRelationship(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, String propertyURI,
			TermObject termObj, TermObject destTermObj,
			ConceptObject conceptObject, AsyncCallback<Void> callback);
	void editTermRelationship(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, String oldPropertyURI,
			String newPropertyURI, TermObject termObj,
			TermObject oldDestTermObj, TermObject newDestTermObj,
			ConceptObject conceptObject, AsyncCallback<Void> callback);
	void deleteTermRelationship(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, String propertyURI,
			TermObject termObj, TermObject destTermObj,
			ConceptObject conceptObject, AsyncCallback<Void> callback);
	
	void addPropertyValue(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, NonFuncObject value,
			String propertyURI, DomainRangeObject drObj, TermObject termObject,
			ConceptObject conceptObject, AsyncCallback<Void> callback);
	void editPropertyValue(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, NonFuncObject oldValue,
			NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, TermObject termObject,
			ConceptObject conceptObject, AsyncCallback<Void> callback);
	void deletePropertyValue(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, NonFuncObject oldValue,
			String propertyURI, TermObject termObject,
			ConceptObject conceptObject, AsyncCallback<Void> callback);

	public void updatePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId, NonFuncObject oldValue,NonFuncObject newValue,String relationshipUri,TermObject termObject,ConceptObject conceptObject, AsyncCallback<Void> callback);
	public void getConceptTermObject(String cls, OntologyInfo ontoInfo, AsyncCallback<ConceptTermObject> callback);
	void getTermAttributes(String resourceURI, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	void getTermNotation(String resourceURI, boolean isExplicit,
			OntologyInfo ontoInfo,
			AsyncCallback<HashMap<String, String>> callback);
	
	
	/*public void addTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject, AsyncCallback<Void> callback);
	public void editTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject oldTcObj,TermCodesObject newTcObj,TermObject termObject,ConceptObject conceptObject, AsyncCallback<Void> callback);
	public void deleteTermCode(OntologyInfo ontoInfo,int actionId,OwlStatus status,int userId,TermCodesObject tcObj,TermObject termObject,ConceptObject conceptObject, AsyncCallback<Void> callback);*/

}
