package org.fao.aoscs.model;

import org.fao.aoscs.client.module.classification.service.ClassificationService;
import org.fao.aoscs.client.module.concept.service.ConceptService;
import org.fao.aoscs.client.module.consistency.service.ConsistencyService;
import org.fao.aoscs.client.module.export.service.ExportService;
import org.fao.aoscs.client.module.importdata.service.ImportService;
import org.fao.aoscs.client.module.ontology.service.OntologyService;
import org.fao.aoscs.client.module.project.service.ProjectService;
import org.fao.aoscs.client.module.relationship.service.RelationshipService;
import org.fao.aoscs.client.module.scheme.service.SchemeService;
import org.fao.aoscs.client.module.search.service.SearchService;
import org.fao.aoscs.client.module.statistic.service.StatisticsService;
import org.fao.aoscs.client.module.term.service.TermService;
import org.fao.aoscs.client.module.validation.service.ValidationService;
import org.fao.aoscs.client.widgetlib.shared.tree.service.TreeService;

public interface BasicModelManager {
	
	public ConceptService getConceptService();
	public SchemeService getSchemeService();
	public TreeService getTreeService();
	public ClassificationService getClassificationService();
	public TermService getTermService();
	public RelationshipService getRelationshipService();
	public SearchService getSearchService();
	public ExportService getExportService();
	public ConsistencyService getConsistencyService();
	public StatisticsService getStatisticsService();
	public ValidationService getValidationService();
	public OntologyService getOntologyService();
	public ImportService getImportService();
	public ProjectService getProjectService();
}
