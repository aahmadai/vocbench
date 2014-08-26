package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class ARTNodeObject extends LightEntity{

	private static final long serialVersionUID = 2869053083820017094L;
	
	private boolean isLiteral;
	private boolean isResource;
	private boolean isURIResource;
	private String show;
	private boolean explicit;
	/**
	 * @return the isLiteral
	 */
	public boolean isLiteral() {
		return isLiteral;
	}
	/**
	 * @param isLiteral the isLiteral to set
	 */
	public void setLiteral(boolean isLiteral) {
		this.isLiteral = isLiteral;
	}
	/**
	 * @return the isResource
	 */
	public boolean isResource() {
		return isResource;
	}
	/**
	 * @param isResource the isResource to set
	 */
	public void setResource(boolean isResource) {
		this.isResource = isResource;
	}
	/**
	 * @return the isURIResource
	 */
	public boolean isURIResource() {
		return isURIResource;
	}
	/**
	 * @param isURIResource the isURIResource to set
	 */
	public void setURIResource(boolean isURIResource) {
		this.isURIResource = isURIResource;
	}
	/**
	 * @return the nominalValue
	 */
	public String getNominalValue() {
		return "";
	}
	/**
	 * @return the toNT
	 */
	public String toNT() {
		return "";
	}
	/**
	 * @return the show
	 */
	public String getShow() {
		return show;
	}
	/**
	 * @param show the show to set
	 */
	public void setShow(String show) {
		this.show = show;
	}
	/**
	 * @return the explicit
	 */
	public boolean isExplicit() {
		return explicit;
	}
	/**
	 * @param explicit the explicit to set
	 */
	public void setExplicit(boolean explicit) {
		this.explicit = explicit;
	}
}
