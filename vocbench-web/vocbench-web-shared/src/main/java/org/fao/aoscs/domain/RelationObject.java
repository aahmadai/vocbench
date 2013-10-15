package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class RelationObject extends LightEntity{

	private static final long serialVersionUID = 3940812391467681877L;
	
	private HashMap<ClassObject, HashMap<ConceptShowObject, Boolean>>  result = new HashMap<ClassObject, HashMap<ConceptShowObject, Boolean>>();
	
	public void setResult(HashMap<ClassObject, HashMap<ConceptShowObject, Boolean>> result) {
		this.result = result;
	}
	
	public HashMap<ClassObject, HashMap<ConceptShowObject, Boolean>> getResult() {
		return result;
	}

	public void addResult(ClassObject rObj, HashMap<ConceptShowObject, Boolean> conceptList) {
		this.result.put(rObj, conceptList);
	}
	public boolean hasValue(){
		return !result.isEmpty();
	}
	
	public int getRelationshipCount()
	{
		int cnt=0;
		for(ClassObject rObj: result.keySet())
		{
			cnt+= result.get(rObj).size();
		}
		return cnt;
	}
}
