package org.fao.aoscs.server;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.classification.service.ClassificationService;
import org.fao.aoscs.domain.ClassificationObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;

public class ClassificationServiceImpl extends PersistentRemoteService implements ClassificationService{

	private static final long serialVersionUID = -7732410471281708562L;
	private ClassificationService classificationService;

	//-------------------------------------------------------------------------
	//
	// Initialization of Remote service : must be done before any server call !
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Bean Manager initialization
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));
		
		classificationService = new ModelManager().getClassificationService();
	}
	
	public InitializeConceptData initData(int group_id, OntologyInfo ontoInfo) throws Exception{
		return classificationService.initData(group_id, ontoInfo);
	}
	
	public ClassificationObject getCategoryTree(OntologyInfo ontoInfo) throws Exception{
		return classificationService.getCategoryTree(ontoInfo);
	}
	
	public void deleteScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj) throws Exception{
		classificationService.deleteScheme(ontoInfo, actionId, userId, status, oldSObj);
	}

	public void editScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj,SchemeObject newSObj) throws Exception{
		classificationService.editScheme(ontoInfo, actionId, userId, status, oldSObj, newSObj);
	}
	
	public void addNewScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject sObj) throws Exception{
		classificationService.addNewScheme(ontoInfo, actionId, userId, status, sObj);
	}
	
	public String addFirstNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,TermObject termObject ,ConceptObject concept,SchemeObject schemeObject) throws Exception{
		return classificationService.addFirstNewCategory(ontoInfo, actionId, userId, status, termObject, concept, schemeObject);
	}
	
	public void deleteCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,SchemeObject schemeObject) throws Exception{
		classificationService.deleteCategory(ontoInfo, actionId, userId, status, conceptObject, schemeObject);
		
	}
	public String addNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,TermObject termObject,ConceptObject concept,SchemeObject schemeObject) throws Exception{
		return classificationService.addNewCategory(ontoInfo, actionId, userId, status, refConcept, position, termObject, concept, schemeObject);
	}
	
	public String makeLinkToFirstConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,String conceptName,SchemeObject schemeObject) throws Exception{
		return classificationService.makeLinkToFirstConcept(ontoInfo, actionId, userId, status, conceptName, schemeObject);
	}
	
	public String makeLinkToConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,String conceptName,SchemeObject schemeObject) throws Exception{
		
		return classificationService.makeLinkToConcept(ontoInfo, actionId, userId, status, refConcept, position, conceptName, schemeObject);
	}
}
