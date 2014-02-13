package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class FeedEntry extends LightEntity{

	private static final long serialVersionUID = 947072274008575749L;
	private String action;
	private String creator;
	private String contributor;
	private String desc;
	private int modifiedId;
	private Date modifiedDate;
	private ArrayList<String> languages = new ArrayList<String>();
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the contributor
	 */
	public String getContributor() {
		return contributor;
	}
	/**
	 * @param contributor the contributor to set
	 */
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the modifiedId
	 */
	public int getModifiedId() {
		return modifiedId;
	}
	/**
	 * @param modifiedId the modifiedId to set
	 */
	public void setModifiedId(int modifiedId) {
		this.modifiedId = modifiedId;
	}
	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public ArrayList<String> getLanguages() {
		return languages;
	}
	public void setLanguages(ArrayList<String> languages) {
		this.languages = languages;
	} 
}
