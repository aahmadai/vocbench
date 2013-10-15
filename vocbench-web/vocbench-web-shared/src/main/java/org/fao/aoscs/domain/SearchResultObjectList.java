package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class SearchResultObjectList extends LightEntity{

	private static final long serialVersionUID = -9119860122085611284L;
	private int searchResultTotalCount = 0;
	private ArrayList<SearchResultObject> searchResultObjectList = new ArrayList<SearchResultObject>();
	
	public void setSearchResultTotalCount(int searchResultTotalCount) {
		this.searchResultTotalCount = searchResultTotalCount;
	}
	public int getSearchResultTotalCount() {
		return searchResultTotalCount;
	}
	public void setSearchResultObjectList(ArrayList<SearchResultObject> searchResultObjectList) {
		this.searchResultObjectList = searchResultObjectList;
	}
	public ArrayList<SearchResultObject> getSearchResultObjectList() {
		return searchResultObjectList;
	}

	
}
