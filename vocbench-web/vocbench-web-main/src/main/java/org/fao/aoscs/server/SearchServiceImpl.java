package org.fao.aoscs.server;

import java.util.ArrayList;

import javax.servlet.ServletException;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.gwt.GwtConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.fao.aoscs.client.module.search.service.SearchService;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchRequest;
import org.fao.aoscs.domain.SearchResponse;
import org.fao.aoscs.domain.SearchResultObjectList;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchServiceImpl extends PersistentRemoteService  implements SearchService {
	
	private static final long serialVersionUID = 8165760079971898449L;
	protected static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
	private SearchService searchService;
	//private ArrayList<String> conceptList = null;
	
	//-------------------------------------------------------------------------
	// Initialization of Remote service : must be done before any server call !
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		logger.info("initializing search service");
		super.init();
		
	//	Bean Manager initialization
		logger.info("initializing search service (hybernate code)");
		setBeanManager(GwtConfigurationHelper.initGwtStatelessBeanManager( new HibernateUtil(HibernateUtilities.getSessionFactory())));;		
		
		searchService = new ModelManager().getSearchService();
		
		logger.info("end search service initialization");
		
	}

	public InitializeSearchData initData(OntologyInfo ontoInfo) throws Exception{
		return searchService.initData(ontoInfo);
	}
	


	public ArrayList<String[]> getSchemes(OntologyInfo ontoInfo) throws Exception{
		return searchService.getSchemes(ontoInfo);
	}
	
	public String getSearchResultsSize(SearchParameterObject searchObj, OntologyInfo ontoInfo) throws Exception
	{
		return searchService.getSearchResultsSize(searchObj, ontoInfo);
	}
	
	public SearchResultObjectList requestSearchResultsRows(Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo) throws Exception 
	{	
		return searchService.requestSearchResultsRows(request, searchObj, ontoInfo);
	}
	
	public SearchResponse getSuggestions(SearchRequest req, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo) throws Exception{
		return searchService.getSuggestions(req, includeNotes, languages, ontoInfo);
	}
	
	public Integer indexOntology(OntologyInfo ontoInfo) throws Exception
	{
		return searchService.indexOntology(ontoInfo);
	}
	
}
