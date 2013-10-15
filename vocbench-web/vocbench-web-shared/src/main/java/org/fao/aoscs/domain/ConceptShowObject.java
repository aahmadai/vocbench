/**
 * 
 */
package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author rajbhandari
 *
 */
public class ConceptShowObject extends LightEntity{
	
	private static final long serialVersionUID = 5666292711962885288L;
	
	private ConceptObject conceptObject = new ConceptObject();
	private String show;
	/**
	 * @return the conceptObject
	 */
	public ConceptObject getConceptObject() {
		return conceptObject;
	}
	/**
	 * @param conceptObject the conceptObject to set
	 */
	public void setConceptObject(ConceptObject conceptObject) {
		this.conceptObject = conceptObject;
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

}
