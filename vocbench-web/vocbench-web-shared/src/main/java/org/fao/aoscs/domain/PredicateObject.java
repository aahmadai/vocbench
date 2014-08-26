package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class PredicateObject extends LightEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062649178374605286L;
	private ARTURIResourceObject uri;
	public ARTURIResourceObject getUri() {
		return uri;
	}
	public void setUri(ARTURIResourceObject uri) {
		this.uri = uri;
	}
	

}
