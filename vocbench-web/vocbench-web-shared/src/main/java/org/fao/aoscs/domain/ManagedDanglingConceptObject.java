package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class ManagedDanglingConceptObject extends LightEntity{

	private static final long serialVersionUID = 6107154290436743837L;
	private DanglingConceptObject dcObj;
	private String uri;
	private int action = 0;
	
	public static int SET_TOP_CONCEPT = 1;
	public static int SET_BROADER_CONCEPT = 2;
	
	/**
	 * @return the dcObj
	 */
	public DanglingConceptObject getDcObj() {
		return dcObj;
	}
	/**
	 * @param dcObj the dcObj to set
	 */
	public void setDcObj(DanglingConceptObject dcObj) {
		this.dcObj = dcObj;
	}
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
	 * @return the action
	 */
	public int getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(int action) {
		this.action = action;
	}
	
}
