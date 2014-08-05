package org.fao.aoscs.model.semanticturkey.adapter;

import java.util.ArrayList;

import org.fao.aoscs.client.module.search.service.SearchService;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchRequest;
import org.fao.aoscs.domain.SearchResponse;
import org.fao.aoscs.domain.SearchResultObjectList;
import org.fao.aoscs.model.semanticturkey.service.SearchServiceSTImpl;

public class SearchServiceSTAdapter implements SearchService {
	
	private SearchServiceSTImpl searchService = new SearchServiceSTImpl();

	public InitializeSearchData initData(OntologyInfo ontoInfo) {
		return searchService.initData(ontoInfo);
	}
	
	/**
	 * @param ontoInfo
	 * @param schemeLang
	 * @return
	 */
	public ArrayList<String[]> getSchemes(OntologyInfo ontoInfo, String schemeLang) {
		return searchService.getSchemes(ontoInfo, schemeLang);
	}
	
	public String getSearchResultsSize(SearchParameterObject searchObj, OntologyInfo ontoInfo)
	{
		return searchService.getSearchResultsSize(searchObj, ontoInfo);
	}
	
	public SearchResultObjectList requestSearchResultsRows(Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo) 
	{	
		return searchService.requestSearchResultsRows(request, searchObj, ontoInfo);
	}
	
	public SearchResponse getSuggestions(SearchRequest req, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo) {
		return searchService.getSuggestions(req, includeNotes, languages, ontoInfo);
	}
	
	public Integer indexOntology(OntologyInfo ontoInfo)
	{
		return searchService.indexOntology(ontoInfo);
	}

	public ArrayList<ArrayList<String>> getSparqlSearchResults(OntologyInfo ontoInfo,
			String query, String language, boolean infer) throws Exception {
		return searchService.getSparqlSearchResults(ontoInfo, query, language, infer);
	}
	
}
