package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class DanglingConceptObject extends LightEntity{

	private static final long serialVersionUID = -181354092006946255L;
	private String conceptURI;
	private String schemeURI;

	/**
	 * @return the conceptURI
	 */
	public String getConceptURI() {
		return conceptURI;
	}
	/**
	 * @param conceptURI the conceptURI to set
	 */
	public void setConceptURI(String conceptURI) {
		this.conceptURI = conceptURI;
	}
	/**
	 * @return the schemeURI
	 */
	public String getSchemeURI() {
		return schemeURI;
	}
	/**
	 * @param schemeURI the schemeURI to set
	 */
	public void setSchemeURI(String schemeURI) {
		this.schemeURI = schemeURI;
	}
}
