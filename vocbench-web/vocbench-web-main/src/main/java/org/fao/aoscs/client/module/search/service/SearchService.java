package org.fao.aoscs.client.module.search.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchRequest;
import org.fao.aoscs.domain.SearchResponse;
import org.fao.aoscs.domain.SearchResultObjectList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("search")
public interface SearchService extends RemoteService{
 
	public InitializeSearchData initData(OntologyInfo ontoInfo) throws Exception;
	public ArrayList<String[]> getSchemes(OntologyInfo ontoInfo) throws Exception;
	public String getSearchResultsSize (SearchParameterObject searchObj, OntologyInfo ontoInfo) throws Exception;
	public SearchResultObjectList requestSearchResultsRows (Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo) throws Exception; 
	public SearchResponse getSuggestions(SearchRequest req, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo) throws Exception;
	public Integer indexOntology(OntologyInfo ontoInfo) throws Exception;
	public ArrayList<ArrayList<String>> getSparqlSearchResults(OntologyInfo ontoInfo, String query, String language, boolean infer) throws Exception;
	
	public static class SearchServiceUtil{
		private static SearchServiceAsync<?> instance;
		public static SearchServiceAsync<?> getInstance()
		{
			if (instance == null) {
				instance = (SearchServiceAsync<?>) GWT.create(SearchService.class);
			}
			return instance;
		}
    }
}

