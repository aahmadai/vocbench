package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class FilterPreferencesId extends LightEntity {

	private static final long serialVersionUID = 676358850589197836L;
	
	private int filterId;
	private int userId;
	private int ontologyId;

	public FilterPreferencesId() {
	}

	public FilterPreferencesId(int filterId, int userId, int ontologyId) {
		this.filterId = filterId;
		this.userId = userId;
		this.ontologyId = ontologyId;
	}

	public int getFilterId() {
		return this.filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getOntologyId() {
		return this.ontologyId;
	}

	public void setOntologyId(int ontologyId) {
		this.ontologyId = ontologyId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FilterPreferencesId))
			return false;
		FilterPreferencesId castOther = (FilterPreferencesId) other;

		return (this.getFilterId() == castOther.getFilterId())
				&& (this.getUserId() == castOther.getUserId())
				&& (this.getOntologyId() == castOther.getOntologyId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getFilterId();
		result = 37 * result + this.getUserId();
		result = 37 * result + this.getOntologyId();
		return result;
	}

}
