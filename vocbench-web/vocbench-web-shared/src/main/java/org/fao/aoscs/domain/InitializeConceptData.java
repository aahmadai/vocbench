package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;


public class InitializeConceptData extends LightEntity{

	private static final long serialVersionUID = -882900083093205890L;

	private ArrayList<String[]> source = new ArrayList<String[]>();
	 
	private HashMap<String,String> actionMap = new HashMap<String,String>();

	private HashMap<String,OwlStatus> actionStatus = new HashMap<String,OwlStatus>();

	private HashMap<String, String> dataTypes = new HashMap<String, String>();
	
	private ArrayList<TreeObject> conceptTreeObject = new ArrayList<TreeObject>();
	
	private ClassificationObject classificationObject =  new ClassificationObject();
	
	private int belongsToModule;
	
    public static final int CONCEPTMODULE = 0;
	
	public static final int CLASSIFICATIONMODULE = 1;
	
	public HashMap<String, String> getActionMap() {
		return actionMap;
	}
	public void setActionMap(HashMap<String, String> actionMap) {
		this.actionMap = actionMap;
	}
	public HashMap<String, OwlStatus> getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(HashMap<String, OwlStatus> actionStatus) {
		this.actionStatus = actionStatus;
	}
	public ArrayList<String[]> getSource() {
		return source;
	}
	public void setSource(ArrayList<String[]> source) {
		this.source = source;
	}
	public ArrayList<TreeObject> getConceptTreeObject() {
		return conceptTreeObject;
	}
	public void setConceptTreeObject(ArrayList<TreeObject> tree) {
		this.conceptTreeObject = tree;
	}
	public ClassificationObject getClassificationObject() {
		return classificationObject;
	}
	public void setClassificationObject(ClassificationObject classificationObject) {
		this.classificationObject = classificationObject;
	}
	public int getBelongsToModule() {
		return belongsToModule;
	}
	public void setBelongsToModule(int belongsToModule) {
		this.belongsToModule = belongsToModule;
	}
	public HashMap<String, String> getDataTypes() {
		return dataTypes;
	}
	public void setDataTypes(HashMap<String, String> dataTypes) {
		this.dataTypes = dataTypes;
	}

}
