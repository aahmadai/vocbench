package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class StInstancesId  extends LightEntity {

	private static final long serialVersionUID = 3070012143380752121L;
	private String stName;
	private String stDomain;
	private String stPort;

	public StInstancesId() {
	}

	public StInstancesId(String stName, String stDomain, String stPort) {
		this.stName = stName;
		this.stDomain = stDomain;
		this.stPort = stPort;
	}

	public String getStName() {
		return this.stName;
	}

	public void setStName(String stName) {
		this.stName = stName;
	}

	public String getStDomain() {
		return this.stDomain;
	}

	public void setStDomain(String stDomain) {
		this.stDomain = stDomain;
	}

	public String getStPort() {
		return this.stPort;
	}

	public void setStPort(String stPort) {
		this.stPort = stPort;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof StInstancesId))
			return false;
		StInstancesId castOther = (StInstancesId) other;

		return ((this.getStName() == castOther.getStName()) || (this
				.getStName() != null && castOther.getStName() != null && this
				.getStName().equals(castOther.getStName())))
				&& ((this.getStDomain() == castOther.getStDomain()) || (this
						.getStDomain() != null
						&& castOther.getStDomain() != null && this
						.getStDomain().equals(castOther.getStDomain())))
				&& ((this.getStPort() == castOther.getStPort()) || (this
						.getStPort() != null && castOther.getStPort() != null && this
						.getStPort().equals(castOther.getStPort())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getStName() == null ? 0 : this.getStName().hashCode());
		result = 37 * result
				+ (getStDomain() == null ? 0 : this.getStDomain().hashCode());
		result = 37 * result
				+ (getStPort() == null ? 0 : this.getStPort().hashCode());
		return result;
	}

}
