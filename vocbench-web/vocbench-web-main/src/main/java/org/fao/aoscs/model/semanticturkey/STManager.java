package org.fao.aoscs.model.semanticturkey;

import org.fao.aoscs.client.module.classification.service.ClassificationService;
import org.fao.aoscs.client.module.concept.service.ConceptService;
import org.fao.aoscs.client.module.consistency.service.ConsistencyService;
import org.fao.aoscs.client.module.export.service.ExportService;
import org.fao.aoscs.client.module.icv.service.ICVService;
import org.fao.aoscs.client.module.importdata.service.ImportService;
import org.fao.aoscs.client.module.ontology.service.OntologyService;
import org.fao.aoscs.client.module.project.service.ProjectService;
import org.fao.aoscs.client.module.relationship.service.RelationshipService;
import org.fao.aoscs.client.module.resourceview.service.ResourceService;
import org.fao.aoscs.client.module.scheme.service.SchemeService;
import org.fao.aoscs.client.module.search.service.SearchService;
import org.fao.aoscs.client.module.statistic.service.StatisticsService;
import org.fao.aoscs.client.module.term.service.TermService;
import org.fao.aoscs.client.module.validation.service.ValidationService;
import org.fao.aoscs.client.widgetlib.shared.tree.service.TreeService;
import org.fao.aoscs.model.BasicModelManager;
import org.fao.aoscs.model.semanticturkey.adapter.ClassificationServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ConceptServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ConsistencyServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ExportServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ICVServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ImportServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.OntologyServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ProjectServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.RelationshipServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ResourceServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.SchemeServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.SearchServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.StatisticsServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.TermServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.TreeServiceSTAdapter;
import org.fao.aoscs.model.semanticturkey.adapter.ValidationServiceSTAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class STManager implements BasicModelManager {
	
	protected static Logger logger = LoggerFactory.getLogger(STManager.class);

	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getConceptService()
	 */
	public ConceptService getConceptService() {
		return new ConceptServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getTreeService()
	 */
	public TreeService getTreeService() {
		return new TreeServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getClassificationService()
	 */
	public ClassificationService getClassificationService() {
		return new ClassificationServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getTermService()
	 */
	public TermService getTermService() {
		return new TermServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getRelationshipService()
	 */
	public RelationshipService getRelationshipService() {
		return new RelationshipServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getSearchService()
	 */
	public SearchService getSearchService() {
		return new SearchServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getExportService()
	 */
	public ExportService getExportService() {
		return new ExportServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getConsistencyService()
	 */
	public ConsistencyService getConsistencyService() {
		return new ConsistencyServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getStatisticsService()
	 */
	public StatisticsService getStatisticsService() {
		return new StatisticsServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getValidationService()
	 */
	public ValidationService getValidationService() {
		return new ValidationServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getOntologyService()
	 */
	public OntologyService getOntologyService() {
		return new OntologyServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getImportService()
	 */
	public ImportService getImportService() {
		return new ImportServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getProjectService()
	 */
	public ProjectService getProjectService() {
		return new ProjectServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getSchemeService()
	 */
	public SchemeService getSchemeService() {
		return new SchemeServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getResourceService()
	 */
	public ResourceService getResourceService() {
		return new ResourceServiceSTAdapter();
	}


	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.BasicModelManager#getICVService()
	 */
	public ICVService getICVService() {
		return new ICVServiceSTAdapter();
	}

}
