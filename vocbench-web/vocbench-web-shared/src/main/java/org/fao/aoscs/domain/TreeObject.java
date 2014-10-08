package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TreeObject extends LightEntity {

	private static final long serialVersionUID = 590936046867486590L;
	
	private String uri;
	private String status;
	private String label;
	private boolean hasChild;
	private String parentURI;	

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the hasChild
	 */
	public boolean isHasChild() {
		return hasChild;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param hasChild the hasChild to set
	 */
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	/**
	 * @return the parentURI
	 */
	public String getParentURI() {
		return parentURI;
	}

	/**
	 * @param parentURI the parentURI to set
	 */
	public void setParentURI(String parentURI) {
		this.parentURI = parentURI;
	}
}
