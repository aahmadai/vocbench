package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ClassTreeObject extends LightEntity{
 

	private static final long serialVersionUID = 2615795657078007617L;

	private HashMap<String,ClassTreeObjectItem> classTreeList = new HashMap<String, ClassTreeObjectItem>();

	private HashMap<String,java.util.ArrayList<ClassTreeObjectItem>> parentChild = new HashMap<String,java.util.ArrayList<ClassTreeObjectItem>>();
	
	private boolean classSelected = false;
	
	public HashMap<String, ClassTreeObjectItem> getClassTreeList() {
		return classTreeList;
	}
	
	public ClassTreeObjectItem getClassTreeObjectItem(String className){
		if(this.classTreeList.containsKey(className)){
			return (ClassTreeObjectItem) classTreeList.get(className);
		}else{
			return null;
		}
	}

	public ArrayList<ClassTreeObjectItem> getRootItem(){
		ArrayList<ClassTreeObjectItem> list = new ArrayList<ClassTreeObjectItem>();
		Iterator<String> it = classTreeList.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			ClassTreeObjectItem ctObj = (ClassTreeObjectItem) classTreeList.get(key);
			if(ctObj.isRootItem()){
				list.add(ctObj);
			}
		}
		return list;
	}
	public void addClassTreeList(ClassTreeObjectItem ctObj) {
		if(!classTreeList.containsKey(ctObj.getName())){
			this.classTreeList.put(ctObj.getName(), ctObj);
		}
	}
	
	public boolean hasClass(String className){
		return classTreeList.containsKey(className);
	}

	public void setClassTreeList(
			HashMap<String, ClassTreeObjectItem> classTreeList) {
		this.classTreeList = classTreeList;
	}

	public void setParentChild(
			HashMap<String, java.util.ArrayList<ClassTreeObjectItem>> parentChild) {
		this.parentChild = parentChild;
	}

	public HashMap<String, ArrayList<ClassTreeObjectItem>> getParentChild() {
		return parentChild;
	}
	
	public boolean hasChild(String className){
		return parentChild.containsKey(className);
	}

	public ArrayList<ClassTreeObjectItem> getChildOf(String parentURI){
		ArrayList<ClassTreeObjectItem> list = new ArrayList<ClassTreeObjectItem>();
		if(parentChild.containsKey(parentURI)){
			list = (ArrayList<ClassTreeObjectItem>) parentChild.get(parentURI);
		}
		return list;
	}
	
	public void addParentChild(String parentURI ,ClassTreeObjectItem childObj) {
		if(!parentChild.containsKey(parentURI)){
			ArrayList<ClassTreeObjectItem> list = new ArrayList<ClassTreeObjectItem>();
			parentChild.put(parentURI, list);
			list.add(childObj);
		}else{
			((ArrayList<ClassTreeObjectItem>)parentChild.get(parentURI)).add(childObj);
		}
	}

	public boolean isClassSelected() {
		return classSelected;
	}

	public void setClassSelected(boolean classTreeSelected) {
		this.classSelected = classTreeSelected;
	}

}
