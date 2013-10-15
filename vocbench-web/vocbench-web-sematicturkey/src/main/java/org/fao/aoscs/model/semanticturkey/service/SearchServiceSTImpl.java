package org.fao.aoscs.model.semanticturkey.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptShowObject;
import org.fao.aoscs.domain.InitializeSearchData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.Request;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.domain.SearchRequest;
import org.fao.aoscs.domain.SearchResponse;
import org.fao.aoscs.domain.SearchResultObject;
import org.fao.aoscs.domain.SearchResultObjectList;
import org.fao.aoscs.hibernate.QueryFactory;
import org.fao.aoscs.model.semanticturkey.ConfigConstants;
import org.fao.aoscs.model.semanticturkey.service.manager.ObjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.PropertyManager;
import org.fao.aoscs.model.semanticturkey.service.manager.SKOSManager;
import org.fao.aoscs.model.semanticturkey.service.manager.VocbenchManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SearchServiceSTImpl {
	
	protected static Logger logger = LoggerFactory.getLogger(SearchServiceSTImpl.class);
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public InitializeSearchData initData(OntologyInfo ontoInfo) {
		logger.debug("initializing search data");
		InitializeSearchData data = new InitializeSearchData();
		logger.debug("search data initialized");
		data.setStatus(QueryFactory.getHibernateSQLQuery( "SELECT status, id FROM owl_status ORDER BY id"));
		data.setDataTypes(PropertyManager.getAllRangeDatatype());
		data.setScheme(getSchemes(ontoInfo));

		data.setTermCodeProperties(PropertyManager.getTermCodePropertiesName(ontoInfo));
        data.setConceptAttributes(PropertyManager.getConceptAttributes(ontoInfo));
		data.setConceptNotes(PropertyManager.getConceptNotes(ontoInfo));
		data.setTermAttributes(PropertyManager.getTermAttributes(ontoInfo));
        
		return data;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public ArrayList<String[]> getSchemes(OntologyInfo ontoInfo) {
		logger.debug("getting schemes: ");
		return SKOSManager.getAllSchemesList(ontoInfo);
	}
	
	/**
	 * @param searchObj
	 * @param ontoInfo
	 * @return
	 */
	public String getSearchResultsSize(SearchParameterObject searchObj, OntologyInfo ontoInfo)
	{
		return ""+searchConcept(searchObj, ontoInfo).size();
	}
	
	/**
	 * @param request
	 * @param searchObj
	 * @param ontoInfo
	 * @return
	 */
	public SearchResultObjectList requestSearchResultsRows(Request request, SearchParameterObject searchObj, OntologyInfo ontoInfo) 
	{	
		logger.debug("inside : requestSearchResultsRows(...)");
		SearchResultObjectList searchResultListObject = new SearchResultObjectList();
		ArrayList<SearchResultObject> retList = new ArrayList<SearchResultObject>();

		List<ConceptShowObject> list = searchConcept(searchObj, ontoInfo);
		searchResultListObject.setSearchResultTotalCount(list.size());
	    list = getSortedSearchResult(list, searchObj.getSelectedLangauge()); // sort the list in alphabetic order

	    int startRow = request.getStartRow();
	    int numRow = request.getNumRows();
	    if(numRow <0) numRow = 0;
	    int endRow = startRow + numRow;
	    if(endRow > list.size()) endRow = list.size();
	    
		list = list.subList(startRow, endRow);
		retList = getSearchResultObject(list, searchObj);

		searchResultListObject.setSearchResultObjectList(retList);
		return searchResultListObject;
	}
	
	/**
	 * @param req
	 * @param includeNotes
	 * @param languages
	 * @param ontoInfo
	 * @return
	 */
	public SearchResponse getSuggestions(SearchRequest req, boolean includeNotes, ArrayList<String> languages, OntologyInfo ontoInfo) {
		logger.debug("inside getSuggestions");
		SearchResponse resp = new SearchResponse();
		resp.setTimestamp(req.getTimestamp());
        // Now set the suggestions in the response
		resp.setSuggestions(getSuggestionList(ontoInfo, languages, req));
        // Send the response back to the client
        return resp;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public Integer indexOntology(OntologyInfo ontoInfo)
	{
		//STARRED MOD (Enable below line to index ontology using index button)
		if(VocbenchManager.createIndexes(ontoInfo))
			return 1;
		else 
			return 0;
	}
	
	/**
	 * @param list
	 * @return
	 */
	public String[] convertAttributesToString(HashMap<String, ArrayList<NonFuncObject>> list)
	{
		String[] values = new String[2];
		values[0] = "";
		values[1] = "";
		Set<String> rel = list.keySet();
	    for(String r:rel){
	        for(NonFuncObject nfObj:list.get(r)){
	        	String separator = "";
	        	String val = values[0];
	        	if(val !=null && val.length()>0)
	        		separator = STXMLUtility.ST_SEPARATOR;
	        	else
	        	{
	        		separator = "";
	        	}
	        	String attributeValue="\""+nfObj.getValue()+"\"";
	        	if(nfObj.getLanguage()!=null && !nfObj.getLanguage().equals("") && !nfObj.getLanguage().equals("null"))
	        		attributeValue += "@"+nfObj.getLanguage();
	        	if(nfObj.getType()!=null)
	        		attributeValue += "^^<"+nfObj.getType()+">";
	        	values[0] += separator+r;
        		values[1] += separator+attributeValue;
	        }
	    }
	    return values;
	}
	
	/**
	 * @param searchObj
	 * @param ontoInfo
	 * @return
	 */
	private ArrayList<ConceptShowObject> searchConcept(SearchParameterObject searchObj, OntologyInfo ontoInfo) {
		logger.debug("getting search results size for search parameter object: " + searchObj);
		//printSearchParameterObject(searchObj);
		
		String[] conceptAttributes = convertAttributesToString(searchObj.getConceptAttribute());
		String[] termAttributes = convertAttributesToString(searchObj.getTermAttribute());
		
		return VocbenchManager.searchConcept(ontoInfo, 
			getSearchRegex(searchObj.getRegex()), 
			searchObj.getKeyword(), 
			STUtility.convertArrayToString(searchObj.getSelectedLangauge(), STXMLUtility.ST_LANG_SEPARATOR), 
			!searchObj.getCaseSensitive(), 
			searchObj.isOnlyPreferredTerm(),
			ConfigConstants.ISINDEXING,
			false,
			searchObj.getIncludeNotes(),
			checkNull(searchObj.getTermCodeRepository()),
			checkNull(searchObj.getTermCode()),
			checkNull(searchObj.getConceptRelationship()),
			"",
			checkNull(searchObj.getTermRelationship()),
			"",
			conceptAttributes[0],
			conceptAttributes[1],
			termAttributes[0],
			termAttributes[1],
			checkNull(searchObj.getStatus()),
			checkNull(searchObj.getScheme()));	
		
	}
	
	private String checkNull(String str)
	{
		if(str==null || str.equals("null"))
			return "";
		else
			return str;
	}
	
	
	
	/**
	 * @param searchObj
	 */
	public void printSearchParameterObject(SearchParameterObject searchObj)
	{
		logger.info("entering getSearchResults:" + searchObj);
		logger.info("searchTerm-------------->> "+ searchObj.getKeyword());
		logger.info("wildcardOp-------------->> "+ searchObj.getRegex());
		logger.info("caseSensitive----------->> "+ searchObj.getCaseSensitive());
		logger.info("includeNotes------------>> "+ searchObj.getIncludeNotes()); 		
		logger.info("preferedTermOnly-------->> "+ searchObj.isOnlyPreferredTerm());
		
		logger.info("termCodeRelation-------->> "+ searchObj.getTermCodeRepository());
		logger.info("termCode---------------->> "+ searchObj.getTermCode());
		
		logger.info("concept relationship------------>> "+ searchObj.getConceptRelationship());		
		logger.info("term relationship------------>> "+ searchObj.getTermRelationship());		
		logger.info("classificationScheme---->> "+ searchObj.getScheme()); 
		logger.info("status------------------>> "+ searchObj.getStatus());
		for(String str : searchObj.getSelectedLangauge())
			logger.info("> "+str);	
		
		Set<String> rel1 = searchObj.getConceptAttribute().keySet();
	    logger.info("concept att relation size = " + rel1.size());
	    for(String r:rel1){
	        ArrayList<NonFuncObject> nf = searchObj.getConceptAttribute().get(r);
	        for(NonFuncObject o:nf){
	        	logger.info("value = " + o.getValue());
	            logger.info(" -- lang = " + o.getLanguage());
	        }
	    }
	    
	    Set<String> rel = searchObj.getTermAttribute().keySet();
	    logger.info("term att relation size = " + rel.size());
	    for(String r:rel){
	        ArrayList<NonFuncObject> nf = searchObj.getTermAttribute().get(r);
	        for(NonFuncObject o:nf){
	        	logger.info("value = " + o.getValue());
	            logger.info(" -- lang = " + o.getLanguage());
	        }
	    }
	}

	/**
	 * @param list
	 * @param langlist
	 * @return
	 */
	private List<ConceptShowObject> getSortedSearchResult(List<ConceptShowObject> list, ArrayList<String> langlist)
	{
		List<ConceptShowObject> retList = new ArrayList<ConceptShowObject>();
		final HashMap<String, ConceptShowObject> cList = new HashMap<String, ConceptShowObject>();
		final ArrayList<ConceptShowObject> emptycList = new ArrayList<ConceptShowObject>();
		
		for(ConceptShowObject conceptShowObject: list)
		{
			ConceptObject cObj = conceptShowObject.getConceptObject();
			if(cObj !=null){
				String label = ObjectManager.createTreeObjectLabel(cObj, true, false, langlist);
				if(label!=null)
				{
					if(label.startsWith("###EMPTY###"))
						emptycList.add(conceptShowObject);
					else
					{
						label = label.replace("<b>", "").replace("</b>", "");
						cList.put(label+cObj.getUri(), conceptShowObject);
					}
				}
			}
		}
		
		List<String> labelKeys = new ArrayList<String>(cList.keySet()); 
		Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
		
		for (Iterator<String> itr = labelKeys.iterator(); itr.hasNext();){ 
			String str = itr.next();
			retList.add(cList.get(str));
        }
		retList.addAll(emptycList);
		return retList;
	}
	
	/**
	 * @param conceptList
	 * @param searchObj
	 * @return
	 */
	private ArrayList<SearchResultObject> getSearchResultObject(List<ConceptShowObject> conceptList, SearchParameterObject searchObj)
	{
		ArrayList<SearchResultObject> retList = new ArrayList<SearchResultObject>();
		
		//check relationship
    	/*if(searchObj.getRelationship() != null ){	
    	    OWLObjectProperty prop = owlModel.getOWLObjectProperty(searchObj.getRelationship());
    		if(prop!=null)
			{
    			RelationshipObject rObj = ProtegeUtility.makeObjectRelationshipObject(prop);
    			for(String clsName: conceptList)
    			{
    				SearchResultObject searchResultObject = new SearchResultObject();
        			searchResultObject.setRelationshipObject(rObj);
    				searchResultObject.setConceptObject(getConceptObject(owlCls, searchObj));
    				  
	    			for (Iterator<?> itr = ProtegeUtil.getConceptInstance(owlModel, owlCls).getPropertyValues(prop).iterator(); itr.hasNext();) {
	    				OWLIndividual destIndividual = (OWLIndividual) itr.next();
	    				OWLNamedClass owlDestcls = (OWLNamedClass) destIndividual.getProtegeType();
	    				searchResultObject.addDestConceptObject(getConceptObject(owlDestcls, searchObj));
	    			}
	    			retList.add(searchResultObject);  
    			}
	    	}
		}
    	else*/
    	{
    		for(ConceptShowObject conceptShowObject: conceptList)
			{
				SearchResultObject searchResultObject = new SearchResultObject();
				searchResultObject.setConceptShowObject(conceptShowObject);
				retList.add(searchResultObject);
			}
    	}
		return retList;
	
	}
	
	
	
	private String getSearchRegex(String searchRegex)
	{
	    
		if (searchRegex.equals(SearchParameterObject.EXACT_MATCH)) 
    	{
    		return "exact match";
		} 
		else if (searchRegex.equals(SearchParameterObject.START_WITH)) 
		{
			return "starts";
		} 
		else if (searchRegex.equals(SearchParameterObject.CONTAIN)) 
		{    		    
			return "contains";
		}
		else if (searchRegex.equals(SearchParameterObject.END_WITH)) 
        {
            return "ends";
        }
		else if (searchRegex.equals(SearchParameterObject.EXACT_WORD)) 
		{
		    return "exact";
		}
		else
			 return "contains";
	}
	
		
	/**
	 * @param ontoInfo
	 * @param languages
	 * @param req
	 * @return
	 */
	private List<String> getSuggestionList(OntologyInfo ontoInfo, ArrayList<String> languages, SearchRequest req)
	{
		logger.debug("inside getSuggestionList");
		List<String> suggestions = new ArrayList<String>(req.getLimit());
		List<String> labelList = VocbenchManager.searchLabel(ontoInfo, "startsWith", req.getQuery().toLowerCase(), STUtility.convertArrayToString(languages, STXMLUtility.ST_LANG_SEPARATOR), true, ConfigConstants.ISINDEXING);
		for (String label: labelList) 
		{
			if(suggestions.size()>req.getLimit()-1)
				break;
			else
				suggestions.add(label);
		}
		return suggestions;
	}

}
