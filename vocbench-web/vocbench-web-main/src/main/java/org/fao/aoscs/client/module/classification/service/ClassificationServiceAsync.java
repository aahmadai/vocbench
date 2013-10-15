package org.fao.aoscs.client.module.classification.service;


import org.fao.aoscs.domain.ClassificationObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClassificationServiceAsync<T> {
	
	public void initData(int group_id, OntologyInfo ontoInfo ,AsyncCallback<InitializeConceptData> callback);
	public void getCategoryTree(OntologyInfo ontoInfo, AsyncCallback<ClassificationObject> callback);
	
	public void addFirstNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,TermObject termObject,ConceptObject concept,SchemeObject schemeObject,AsyncCallback<String> callback);
	public void addNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,TermObject termObject,ConceptObject concept,SchemeObject schemeObject,AsyncCallback<String> callback);
	public void makeLinkToFirstConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,String conceptName,SchemeObject schemeObject,AsyncCallback<String> callback);
	public void makeLinkToConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,String conceptName,SchemeObject schemeObject,AsyncCallback<String> callback);
	
	public void deleteCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,SchemeObject schemeObject,AsyncCallback<Void> callback);
	
	public void addNewScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject sObj,AsyncCallback<Void> callback);
	public void editScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj,SchemeObject newSObj,AsyncCallback<Void> callback);
	public void deleteScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj,AsyncCallback<Void> callback);
}
