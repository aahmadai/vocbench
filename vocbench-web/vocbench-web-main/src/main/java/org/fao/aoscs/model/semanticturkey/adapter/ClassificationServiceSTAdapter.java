package org.fao.aoscs.model.semanticturkey.adapter;

import org.fao.aoscs.client.module.classification.service.ClassificationService;
import org.fao.aoscs.domain.ClassificationObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.model.semanticturkey.service.ClassificationServiceSTImpl;

public class ClassificationServiceSTAdapter implements ClassificationService{

	private ClassificationServiceSTImpl classificationService = new ClassificationServiceSTImpl();

	public InitializeConceptData initData(int group_id, OntologyInfo ontoInfo){
		return classificationService.initData(group_id, ontoInfo);
	}
	
	public ClassificationObject getCategoryTree(OntologyInfo ontoInfo){
		return classificationService.getCategoryTree(ontoInfo);
	}
	
	public void deleteScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj){
		classificationService.deleteScheme(ontoInfo, actionId, userId, status, oldSObj);
	}

	public void editScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj,SchemeObject newSObj){
		classificationService.editScheme(ontoInfo, actionId, userId, status, oldSObj, newSObj);
	}
	
	public void addNewScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject sObj){
		classificationService.addNewScheme(ontoInfo, actionId, userId, status, sObj);
	}
	
	public String addFirstNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,TermObject termObject ,ConceptObject concept,SchemeObject schemeObject){
		return classificationService.addFirstNewCategory(ontoInfo, actionId, userId, status, termObject, concept, schemeObject);
	}
	
	public void deleteCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,SchemeObject schemeObject){
		classificationService.deleteCategory(ontoInfo, actionId, userId, status, conceptObject, schemeObject);
		
	}
	public String addNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,TermObject termObject,ConceptObject concept,SchemeObject schemeObject){
		return classificationService.addNewCategory(ontoInfo, actionId, userId, status, refConcept, position, termObject, concept, schemeObject);
	}
	
	public String makeLinkToFirstConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,String conceptName,SchemeObject schemeObject){
		return classificationService.makeLinkToFirstConcept(ontoInfo, actionId, userId, status, conceptName, schemeObject);
	}
	
	public String makeLinkToConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,String conceptName,SchemeObject schemeObject){
		
		return classificationService.makeLinkToConcept(ontoInfo, actionId, userId, status, refConcept, position, conceptName, schemeObject);
	}
}
