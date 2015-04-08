package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ConceptObject extends LightEntity {

	private static final long serialVersionUID = 7851152314158181535L;

	public static final int CONCEPTMODULE = 0;
	
	public static final int CLASSIFICATIONMODULE = 1;

	private String uri;

	private int statusID;
	
	//private String displayedConceptLabel;
	
	//private String conceptInstance;
	
	private String status;
	
	private Date dateCreate;

	private Date dateModified;
	
	//private String name;
	
	//private String nameSpace;
	
	private ArrayList<String> scheme = new ArrayList<String>();;
	
	//private String parentInstance;
	
	private String parentURI;
	
	//private ConceptObject parentObject;

	private HashMap<String,TermObject> term = new HashMap<String,TermObject>();
	
	private boolean rootItem;
	
	private boolean hasChild;
	
	private int belongsToModule;
		
	public boolean isRootItem() {
		return rootItem;
	}

	public void setRootItem(boolean rootItem) {
		this.rootItem = rootItem;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusID() {
		return statusID;
	}

	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/*public String getDisplayedConceptLabel() {
		return displayedConceptLabel;
	}

	public void setDisplayedConceptLabel(String displayedConceptLabel) {
		this.displayedConceptLabel = displayedConceptLabel;
	}


	public String getConceptInstance() {
		return conceptInstance;
	}


	public void setConceptInstance(String conceptInstance) {
		this.conceptInstance = conceptInstance;
	}
	
	public String getParentInstance() {
		return parentInstance;
	}


	public void setParentInstance(String parentInstance) {
		this.parentInstance = parentInstance;
	}
	
	public ConceptObject getParentObject() {
		return parentObject;
	}


	public void setParentObject(ConceptObject parentObject) {
		this.parentObject = parentObject;
	}

	

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}*/

	public ArrayList<String> getScheme() {
		return scheme;
	}

	public void setScheme(ArrayList<String> scheme) {
		this.scheme = scheme;
	}
	
	public void addScheme(String scheme) {
		this.scheme.add(scheme);
	}

	public HashMap<String, TermObject> getTerm() {
		return term;
	}
	
	public void setTerm(HashMap<String, TermObject> term) {
		this.term = term;
	}

	public void addTerm(String termIns,TermObject tObj) {
		if(!this.term.containsKey(termIns)){
			this.term.put(termIns, tObj);
		}
	}

	public String getParentURI() {
		return parentURI;
	}

	public void setParentURI(String parentURI) {
		this.parentURI = parentURI;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	/*public String getName() {
		return name;
	}*/

	/*public void setName(String name) {
		this.name = name;
	}*/

	public int getBelongsToModule() {
		return belongsToModule;
	}

	public void setBelongsToModule(int belongsToModule) {
		this.belongsToModule = belongsToModule;
	}
	
	public String toString() {
		return getUri();
	}
	
}
