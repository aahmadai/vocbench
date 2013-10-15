package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;


public class TermRelationshipObject extends LightEntity{

	private static final long serialVersionUID = 5664689197830339225L;
	
	private HashMap<ClassObject, HashMap<TermObject, Boolean>> result = new HashMap<ClassObject, HashMap<TermObject, Boolean>>();

	public HashMap<ClassObject, HashMap<TermObject, Boolean>> getResult() {
		return result;
	}
	
	public void setResult(HashMap<ClassObject, HashMap<TermObject, Boolean>> result) {
		this.result = result;
	}

	public void addResult(ClassObject rObj,HashMap<TermObject, Boolean> conceptList) {
		this.result.put(rObj, conceptList);
	}
	public boolean hasValue(){
		return !result.isEmpty();
	}
	public boolean isEmpty(){
		return result.isEmpty();
	}
}
