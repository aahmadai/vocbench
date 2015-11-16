package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class IDObject extends LightEntity {

	private static final long serialVersionUID = 1L;

	private String IDUri;
	
	private String IDSource;

	private String IDSourceURL;
	
	private Date IDDateCreate;

	private Date IDDateModified;
	
	private ArrayList<TranslationObject> IDTranslationList = new ArrayList<TranslationObject>();
	
	private HashMap<String, ArrayList<String>> otherPropList = new HashMap<String, ArrayList<String>>();
	
	private int IDType;
	
	/**
	 * Constant for Concept/Term Object :  1 for Definition
	 */
	public static final int IMAGE = 1;
	/**
	 * Constant for Concept/Term Object :  2 for Image
	 */
	public static final int DEFINITION = 2;
	
	public Date getIDDateCreate() {
		return IDDateCreate;
	}
	public void setIDDateCreate(Date dateCreate) {
		IDDateCreate = dateCreate;
	}
	public Date getIDDateModified() {
		return IDDateModified;
	}
	public void setIDDateModified(Date dateModified) {
		IDDateModified = dateModified;
	}
	public String getIDSource() {
		return IDSource;
	}
	public void setIDSource(String source) {
		IDSource = source;
	}
	public String getIDSourceURL() {
		return IDSourceURL;
	}
	public void setIDSourceURL(String sourceURL) {
		IDSourceURL = sourceURL;
	}
	public int getIDType() {
		return IDType;
	}
	public void setIDType(int type) {
		IDType = type;
	}
	public String getIDUri() {
		return IDUri;
	}
	public void setIDUri(String uri) {
		IDUri = uri;
	}
	public ArrayList<TranslationObject> getIDTranslationList() {
		return IDTranslationList;
	}
	public void setIDTranslationList(ArrayList<TranslationObject> idtList) {
		IDTranslationList = idtList;
	}
	public void addIDTranslationList(TranslationObject tObj) {
		IDTranslationList.add(tObj);
	}
	public boolean hasSource(){
		if(IDSource == null){
			return false;
		}else{
			return true;
		}
	}
	public HashMap<String, String> getExistingLanguage(){
		HashMap<String, String> langList = new HashMap<String, String>();
		for (int i = 0; i < IDTranslationList.size(); i++) {
			TranslationObject tObj = (TranslationObject) IDTranslationList.get(i);
			if(!langList.containsKey(tObj.getLang())){
				langList.put(tObj.getLang(), tObj.getLang());
			}
		}
		return langList;
	}
	/**
	 * @return the otherPropList
	 */
	public HashMap<String, ArrayList<String>> getOtherPropList() {
		return otherPropList;
	}
	/**
	 * @param otherPropList the otherPropList to set
	 */
	public void setOtherPropList(HashMap<String, ArrayList<String>> otherPropList) {
		this.otherPropList = otherPropList;
	}
	
	/**
	 * @param prop
	 * @param value
	 */
	public void addOtherPropList(String prop, String value) {
		ArrayList<String> list = otherPropList.get(prop);
		if(list==null)
		{
			list = new ArrayList<String>();
		}
		else
			otherPropList.remove(prop);
		if(!list.contains(value))
			list.add(value);
		otherPropList.put(prop, list);
		
	}
}
