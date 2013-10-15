package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.module.term.service.TermService;
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
import org.fao.aoscs.model.semanticturkey.service.TermServiceSTImpl;

/**
 * @author rajbhandari
 *
 */
public class TermServiceSTAdapter implements TermService{
	
	private TermServiceSTImpl termService = new TermServiceSTImpl();
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermDetail(org.fao.aoscs.domain.OntologyInfo, java.util.ArrayList, java.lang.String, boolean)
	 */
	public TermDetailObject getTermDetail(OntologyInfo ontoInfo,
			ArrayList<String> langList, String termURI, boolean isExplicit) {
		return termService.getTermDetail(ontoInfo, langList, termURI, isExplicit);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermInformation(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public InformationObject getTermInformation(String termUri, OntologyInfo ontoInfo){
		return termService.getTermInformation(termUri, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getConceptTermObject(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ConceptTermObject getConceptTermObject(String cls, OntologyInfo ontoInfo){
		return termService.getConceptTermObject(cls, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermRelationship(java.lang.String, java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public TermRelationshipObject getTermRelationship(String cls,String termIns, boolean isExplicit, OntologyInfo ontoInfo){
		return termService.getTermRelationship(cls, termIns, isExplicit, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#addTermRelationship(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, java.lang.String, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void addTermRelationship(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, String propertyURI,
			TermObject termObj, TermObject destTermObj,
			ConceptObject conceptObject) {
		termService.addTermRelationship(ontoInfo, actionId, status, userId, propertyURI, termObj, destTermObj, conceptObject);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#editTermRelationship(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, java.lang.String, java.lang.String, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void editTermRelationship(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, String oldPropertyURI,
			String newPropertyURI, TermObject termObj,
			TermObject oldDestTermObj, TermObject newDestTermObj,
			ConceptObject conceptObject) {
		termService.editTermRelationship(ontoInfo, actionId, status, userId, oldPropertyURI, newPropertyURI, termObj, oldDestTermObj, newDestTermObj, conceptObject);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#deleteTermRelationship(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, java.lang.String, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void deleteTermRelationship(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, String propertyURI,
			TermObject termObj, TermObject destTermObj,
			ConceptObject conceptObject) {
		termService.deleteTermRelationship(ontoInfo, actionId, status, userId, propertyURI, termObj, destTermObj, conceptObject);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#updatePropertyValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void updatePropertyValue(OntologyInfo ontoInfo ,int actionId,OwlStatus status,int userId,NonFuncObject oldValue, NonFuncObject newValue, String relationshipUri, TermObject termObject, ConceptObject conceptObject){
		termService.updatePropertyValue(ontoInfo, actionId, status, userId, oldValue, newValue, relationshipUri, termObject, conceptObject);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#addPropertyValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void addPropertyValue(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, NonFuncObject value,
			String propertyURI, DomainRangeObject drObj, TermObject termObject,
			ConceptObject conceptObject) {
		termService.addPropertyValue(ontoInfo, actionId, status, userId, value, propertyURI, drObj, termObject, conceptObject);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#editPropertyValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.DomainRangeObject, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void editPropertyValue(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, NonFuncObject oldValue,
			NonFuncObject newValue, String propertyURI,
			DomainRangeObject drObj, TermObject termObject,
			ConceptObject conceptObject) {
		termService.editPropertyValue(ontoInfo, actionId, status, userId, oldValue, newValue, propertyURI, drObj, termObject, conceptObject);
		
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#deletePropertyValue(org.fao.aoscs.domain.OntologyInfo, int, org.fao.aoscs.domain.OwlStatus, int, org.fao.aoscs.domain.NonFuncObject, java.lang.String, org.fao.aoscs.domain.TermObject, org.fao.aoscs.domain.ConceptObject)
	 */
	public void deletePropertyValue(OntologyInfo ontoInfo, int actionId,
			OwlStatus status, int userId, NonFuncObject oldValue,
			String propertyURI, TermObject termObject,
			ConceptObject conceptObject) {
		termService.deletePropertyValue(ontoInfo, actionId, status, userId, oldValue, propertyURI, termObject, conceptObject);
		
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermCodes(java.util.ArrayList, org.fao.aoscs.domain.OntologyInfo)
	 */
	/*public HashMap<String, String> getTermCodes(ArrayList<String> terms, OntologyInfo ontoInfo)
	{
		return termService.getTermCodes(terms, ontoInfo);
	}*/

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermAttributes(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getTermAttributes(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) {
		return termService.getTermAttributes(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermNotation(java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<String, String> getTermNotation(String resourceURI,
			boolean isExplicit, OntologyInfo ontoInfo) {
		return termService.getTermNotation(resourceURI, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermNotationValue(java.lang.String, java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getTermNotationValue(
			String cls, String termIns, boolean isExplicit,
			OntologyInfo ontoInfo) {
		return termService.getTermNotationValue(cls, termIns, isExplicit, ontoInfo);
	}

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.term.service.TermService#getTermAttributeValue(java.lang.String, java.lang.String, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getTermAttributeValue(
			String cls, String termIns, boolean isExplicit,
			OntologyInfo ontoInfo) {
		return termService.getTermAttributeValue(cls, termIns, isExplicit, ontoInfo);
	}

}
