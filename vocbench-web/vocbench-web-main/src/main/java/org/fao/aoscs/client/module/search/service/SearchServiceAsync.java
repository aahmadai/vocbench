package org.fao.aoscs.client.module.search.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchRequest;
import org.fao.aoscs.domain.SearchResponse;
import org.fao.aoscs.domain.SearchResultObjectList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync<T> {
	public void initData(OntologyInfo ontoInfo, AsyncCallback<InitializeSearchData> callback);
	void getSchemes(OntologyInfo ontoInfo, String schemeLang,
			AsyncCallback<ArrayList<String[]>> callback);
	public void getSearchResultsSize (SearchParameterObject searchObj, OntologyInfo ontoInfo, AsyncCallback<String> callback);
	public void requestSearchResultsRows (Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo,AsyncCallback<SearchResultObjectList> callback);
	public void getSuggestions(SearchRequest request, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo, AsyncCallback<SearchResponse> callback);
	public void indexOntology (OntologyInfo ontoInfo, AsyncCallback<Integer> callback);
	void getSparqlSearchResults(OntologyInfo ontoInfo, String query,
			String language, boolean infer, String mode,
			AsyncCallback<ArrayList<ArrayList<String>>> callback);
	
}
