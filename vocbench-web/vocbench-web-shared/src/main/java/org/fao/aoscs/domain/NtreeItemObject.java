package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;


public class NtreeItemObject extends LightEntity{
	
	private static final long serialVersionUID = 6677056748387457465L;

	private String name = new String();

	private ArrayList<String> child = new ArrayList<String>();
	
	public void setChild(ArrayList<String> child) {
		this.child = child;
	}
	public ArrayList<String> getChild() {
		return child;
	}
	public void addChild(String cObj) {
		this.child.add(cObj);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getChildCount(){
		return child.size();
	}
	public String getChild(int index){
		return (String) child.get(index);
	}

}
