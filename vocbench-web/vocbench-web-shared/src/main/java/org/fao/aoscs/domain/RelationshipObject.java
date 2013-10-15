package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class RelationshipObject extends LightEntity {

	private static final long serialVersionUID = 4934317615087868517L;
	
	private ArrayList<LabelObject> labelList = new ArrayList<LabelObject>();
	
	private ArrayList<LabelObject> commentList = new ArrayList<LabelObject>();

	private String uri;
	
	private String name ;
	
	private String parent;
	
	private String inverse;
	
	private boolean rootItem = false;
	
	//private RelationshipObject parentObject;
	
	private String type;
	
	private boolean isFunctional = false;

	private boolean isInverseFunctional = false;
	
	private boolean isSymmetric = false;
	
	private boolean isTransitive = false;

	private DomainRangeObject domainRangeObject;
	
	public static final String OBJECT = "owl:ObjectProperty";
	
	public static final String DATATYPE = "owl:DatatypeProperty";
	
	public static final String ANNOTATION = "owl:AnnotationProperty";
	
	public static final String ONTOLOGY = "owl:OntologyProperty";
	
	public static final String RDF = "rdf:Property";
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parentURI) {
		this.parent = parentURI;
	}

	public String getInverse() {
		return inverse;
	}

	public void setInverse(String inverse) {
		this.inverse = inverse;
	}

	public ArrayList<LabelObject> getLabelList() {
		return labelList;
	}
	
	public void setLabelList(ArrayList<LabelObject> labelList) {
		this.labelList = labelList;
	}
	
	/*public String getLabel(ArrayList<String> userSelectedLanguage)
	{
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = labelObj.getLanguage();
			
			if(userSelectedLanguage.contains(lang))
			{
				String label = labelObj.getLabel();
				if(labelStr.equals(""))
					labelStr += " "+label+" ("+lang+")";
				else
					labelStr += ", "+label+" ("+lang+")";
			}
		}
		return labelStr;
	}
	
	public String getLabel()
	{
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = labelObj.getLanguage();
			String label = labelObj.getLabel();
			if(labelStr.equals(""))
				labelStr += " "+label+" ("+lang+")";
			else
				labelStr += ", "+label+" ("+lang+")";
		}
		return labelStr;
	}*/
	
	public void addLabel(String label, String language) {
		LabelObject labelObj = new LabelObject();
		labelObj.setLabel(label);
		labelObj.setLanguage(language);
		labelList.add(labelObj);
	}
	
	public void removeLabel(String label, String language) {
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = labelList.get(i);
			String lab = labelObj.getLabel();
			String lang = labelObj.getLanguage();
			if(lab.equals(label) && lang.equals(language))
			{
				labelList.remove(labelObj);
			}
		}
	}
	
	public ArrayList<LabelObject> getCommentList() {
		return commentList;
	}
	
	public void setCommentList(ArrayList<LabelObject> commentList) {
		this.commentList = commentList;
	}

	public void addComment(String comment, String language) {
		LabelObject labelObj = new LabelObject();
		labelObj.setLabel(comment);
		labelObj.setLanguage(language);
		commentList.add(labelObj);
	}
	
	public void removeComment(String comment, String language) {
		for(int i=0;i<commentList.size();i++)
		{
			LabelObject labelObj = commentList.get(i);
			String lab = labelObj.getLabel();
			String lang = labelObj.getLanguage();
			if(lab.equals(comment) && lang.equals(language))
			{
				commentList.remove(labelObj);
			}
		}
	}
	
	public boolean isRootItem() {
		return rootItem;
	}
	
	public void setRootItem(boolean rootItem) {
		this.rootItem = rootItem;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*public RelationshipObject getParentObject() {
		return parentObject;
	}

	public void setParentObject(RelationshipObject parentObject) {
		this.parentObject = parentObject;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFunctional() {
		return isFunctional;
	}
	
	public void setFunctional(boolean isFunctional) {
		this.isFunctional = isFunctional;
	}

	public boolean isInverseFunctional() {
		return isInverseFunctional;
	}

	public void setInverseFunctional(boolean isInverseFunctional) {
		this.isInverseFunctional = isInverseFunctional;
	}

	public boolean isSymmetric() {
		return isSymmetric;
	}

	public void setSymmetric(boolean isSymmetric) {
		this.isSymmetric = isSymmetric;
	}

	public boolean isTransitive() {
		return isTransitive;
	}

	public void setTransitive(boolean isTransitive) {
		this.isTransitive = isTransitive;
	}

	public void setDomainRangeObject(DomainRangeObject domainRangeObject) {
		this.domainRangeObject = domainRangeObject;
	}

	public DomainRangeObject getDomainRangeObject() {
		return domainRangeObject;
	}
	
}
