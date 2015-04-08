package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

public class PropertyTreeObject extends LightEntity{
 
	private static final long serialVersionUID = 5016522879388270751L;

	private HashMap<String,PropertyObject> propertyList = new HashMap<String,PropertyObject>();

	private HashMap<String,java.util.ArrayList<PropertyObject>> parentChild = new HashMap<String,java.util.ArrayList<PropertyObject>>();
	
	private boolean relationshipSelected = false;
	
	public HashMap<String, PropertyObject> getPropertyList() {
		return propertyList;
	}
	
	public PropertyObject getPropertyObject(String propertyURI){
		if(this.propertyList.containsKey(propertyURI)){
			return (PropertyObject) propertyList.get(propertyURI);
		}else{
			return null;
		}
	}

	public ArrayList<PropertyObject> getRootItem(){
		ArrayList<PropertyObject> list = new ArrayList<PropertyObject>();
		Iterator<String> it = propertyList.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			PropertyObject propObj = (PropertyObject) propertyList.get(key);
			if(propObj.isRootItem()){
				list.add(propObj);
			}
		}
		return list;
	}
	public void addPropertyList(PropertyObject propObj) {
		if(!propertyList.containsKey(propObj.getUri())){
			this.propertyList.put(propObj.getUri(), propObj);
		}
	}
	
	public boolean hasProperty(String propertyURI){
		return propertyList.containsKey(propertyURI);
	}

	public void setPropertyList(
			HashMap<String, PropertyObject> relationshipList) {
		this.propertyList = relationshipList;
	}

	public void setParentChild(
			HashMap<String, java.util.ArrayList<PropertyObject>> parentChild) {
		this.parentChild = parentChild;
	}

	public HashMap<String, ArrayList<PropertyObject>> getParentChild() {
		return parentChild;
	}
	
	public boolean hasChild(String propertyURI){
		return parentChild.containsKey(propertyURI);
	}

	public ArrayList<PropertyObject> getChildOf(String parentURI){
		ArrayList<PropertyObject> list = new ArrayList<PropertyObject>();
		if(parentChild.containsKey(parentURI)){
			list = (ArrayList<PropertyObject>) parentChild.get(parentURI);
		}
		return list;
	}
	
	public void addParentChild(String parentURI ,PropertyObject childObj) {
		if(!parentChild.containsKey(parentURI)){
			ArrayList<PropertyObject> list = new ArrayList<PropertyObject>();
			parentChild.put(parentURI, list);
			list.add(childObj);
		}else{
			((ArrayList<PropertyObject>)parentChild.get(parentURI)).add(childObj);
		}
	}

	public boolean isPropertySelected() {
		return relationshipSelected;
	}

	public void setPropertySelected(boolean relationshipSelected) {
		this.relationshipSelected = relationshipSelected;
	}

}
