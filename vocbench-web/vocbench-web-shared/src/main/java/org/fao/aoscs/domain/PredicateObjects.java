package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class PredicateObjects extends LightEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1069257980867393610L;
	private PredicateObject predicate;
	ArrayList<ARTNodeObject> objects = new ArrayList<ARTNodeObject>();
	/**
	 * @return the predicate
	 */
	public PredicateObject getPredicate() {
		return predicate;
	}
	/**
	 * @return the objects
	 */
	public ArrayList<ARTNodeObject> getObjects() {
		return objects;
	}
	/**
	 * @param objects the objects to set
	 */
	public void setObjects(ArrayList<ARTNodeObject> objects) {
		this.objects = objects;
	}
	/**
	 * @param predicate the predicate to set
	 */
	public void setPredicate(PredicateObject predicate) {
		this.predicate = predicate;
	}
	

}
