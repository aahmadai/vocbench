package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class LinkingConceptObject extends LightEntity {

	private static final long serialVersionUID = -8054326301246289271L;

	private String uri;

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
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

	private String scheme;
	
	private String parentURI;
	
	
	
}
