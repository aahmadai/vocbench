package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ClassTreeObjectItem extends LightEntity {

	
	private static final long serialVersionUID = -686412816146090552L;

	private boolean deleteForbidden;
	private String name;
	private String numInst;
	private boolean rootItem = false;
	/**
	 * @return the deleteForbidden
	 */
	public boolean isDeleteForbidden() {
		return deleteForbidden;
	}
	/**
	 * @param deleteForbidden the deleteForbidden to set
	 */
	public void setDeleteForbidden(boolean deleteForbidden) {
		this.deleteForbidden = deleteForbidden;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the numInst
	 */
	public String getNumInst() {
		return numInst;
	}
	/**
	 * @param numInst the numInst to set
	 */
	public void setNumInst(String numInst) {
		this.numInst = numInst;
	}
	/**
	 * @return the rootItem
	 */
	public boolean isRootItem() {
		return rootItem;
	}
	/**
	 * @param rootItem the rootItem to set
	 */
	public void setRootItem(boolean rootItem) {
		this.rootItem = rootItem;
	}
	
}
