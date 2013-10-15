package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class TermDetailObject extends LightEntity{

	private static final long serialVersionUID = 3974834978828021236L;
	
	private InformationObject informationObject =  new InformationObject();
	private int relationCount = 0;
	private int attributeCount = 0;
	private int notationCount = 0;
	private int historyCount = 0;
	
	public InformationObject getInformationObject() {
		return informationObject;
	}
	public void setInformationObject(InformationObject informationObject) {
		this.informationObject = informationObject;
	}
	public int getRelationCount() {
		return relationCount;
	}
	public void setRelationCount(int relationCount) {
		this.relationCount = relationCount;
	}
	public int getAttributeCount() {
		return attributeCount;
	}
	public void setAttributeCount(int attributeCount) {
		this.attributeCount = attributeCount;
	}
	public int getHistoryCount() {
		return historyCount;
	}
	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}
	public int getNotationCount() {
		return notationCount;
	}
	public void setNotationCount(int notationCount) {
		this.notationCount = notationCount;
	}
	
}

