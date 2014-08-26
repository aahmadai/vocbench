package org.fao.aoscs.model;

import java.lang.reflect.Method;

import org.fao.aoscs.client.module.classification.service.ClassificationService;
import org.fao.aoscs.client.module.concept.service.ConceptService;
import org.fao.aoscs.client.module.consistency.service.ConsistencyService;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.export.service.ExportService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelManager implements BasicModelManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ModelManager.class);
	
	public String getModelType()
	{
		return ConfigConstants.MODELCLASS;
	}

	public ConceptService getConceptService() {
		
		ConceptService conceptService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getConceptService");
			conceptService = (ConceptService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return conceptService;
	}

	public TreeService getTreeService() {
		TreeService treeService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getTreeService");
			treeService = (TreeService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return treeService;
	}

	public SchemeService getSchemeService() {
		SchemeService schemeService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getSchemeService");
			schemeService = (SchemeService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return schemeService;
	}
	
	public ClassificationService getClassificationService() {
		ClassificationService classificationService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getClassificationService");
			classificationService = (ClassificationService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return classificationService;
	}


	public ConsistencyService getConsistencyService() {
		ConsistencyService consistencyService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getConsistencyService");
			consistencyService = (ConsistencyService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return consistencyService;
	}


	public ExportService getExportService() {
		ExportService exportService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getExportService");
			exportService = (ExportService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return exportService;
	}


	public RelationshipService getRelationshipService() {
		RelationshipService relationshipService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getRelationshipService");
			relationshipService = (RelationshipService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return relationshipService;
	}


	public SearchService getSearchService() {
		SearchService searchService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getSearchService");
			searchService = (SearchService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return searchService;
	}


	public StatisticsService getStatisticsService() {
		StatisticsService statisticsService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getStatisticsService");
			statisticsService = (StatisticsService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return statisticsService;
	}


	public TermService getTermService() {
		TermService termService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getTermService");
			termService = (TermService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return termService;
	}


	public ValidationService getValidationService() {
		ValidationService validationService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getValidationService");
			validationService = (ValidationService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return validationService;
	}
	
	public OntologyService getOntologyService() {
		
		OntologyService ontologyService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getOntologyService");
			ontologyService = (OntologyService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return ontologyService;
	}
	
	public ProjectService getProjectService() {
		
		ProjectService projectService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getProjectService");
			projectService = (ProjectService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return projectService;
	}
	
	public ImportService getImportService() {
		
		ImportService importService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getImportService");
			importService = (ImportService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return importService;
	}
	
	public ResourceService getResourceService() {
		
		ResourceService resourceService = null;
		try
		{
			Class<?> cls = Class.forName(getModelType());
			Method m = cls.getMethod("getResourceService");
			resourceService = (ResourceService) m.invoke(cls.newInstance());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return resourceService;
	}

}
