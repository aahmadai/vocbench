package org.fao.aoscs.domain;

import java.util.List;

import net.sf.gilead.pojo.gwt.LightEntity;

public class SearchResponse extends LightEntity {

	private static final long serialVersionUID = -6692822542608964072L;
	
	private long timestamp;
	private List<String> suggestions;
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the suggestions
	 */
	public List<String> getSuggestions() {
		return suggestions;
	}

	/**
	 * @param suggestions the suggestions to set
	 */
	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}

	
	
}
