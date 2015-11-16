package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ConceptDetailObject extends LightEntity{

	private static final long serialVersionUID = -3207867800157365870L;
	
	private ConceptObject conceptObject =  new ConceptObject();
	private ConceptTermObject conceptTermObject =  new ConceptTermObject();
	private InformationObject informationObject =  new InformationObject();
	private ImageObject imageObject =  new ImageObject();
	private DefinitionObject definitionObject = new DefinitionObject();
	private HierarchyObject hierarchyObject = new HierarchyObject();
	private RelationObject relationObject =  new RelationObject();
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> attributeObject = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> noteObject = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> notationObject = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> annotationObject = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> otherObject = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> typeObject = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> alignmentObject = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();
	private HashMap<String, String> schemeObject = null;
	
	private int termCount = 0;
	private int definitionCount = 0;
	private int noteCount = 0;
	private int attributeCount = 0;
	private int notationCount = 0;
	private int annotationCount = 0;
	private int otherCount = 0;
	private int typeCount = 0;
	private int relationCount = 0;
	private int historyCount = 0;
	private int imageCount = 0;
	private int schemeCount = 0;
	private int alignmentCount = 0;

	/**
	 * @return the conceptObject
	 */
	public ConceptObject getConceptObject() {
		return conceptObject;
	}
	/**
	 * @return the conceptTermObject
	 */
	public ConceptTermObject getConceptTermObject() {
		return conceptTermObject;
	}
	/**
	 * @return the informationObject
	 */
	public InformationObject getInformationObject() {
		return informationObject;
	}
	/**
	 * @return the imageObject
	 */
	public ImageObject getImageObject() {
		return imageObject;
	}
	/**
	 * @return the definitionObject
	 */
	public DefinitionObject getDefinitionObject() {
		return definitionObject;
	}
	/**
	 * @return the relationObject
	 */
	public RelationObject getRelationObject() {
		return relationObject;
	}
	/**
	 * @param conceptObject the conceptObject to set
	 */
	public void setConceptObject(ConceptObject conceptObject) {
		this.conceptObject = conceptObject;
	}
	/**
	 * @param conceptTermObject the conceptTermObject to set
	 */
	public void setConceptTermObject(ConceptTermObject conceptTermObject) {
		this.conceptTermObject = conceptTermObject;
	}
	/**
	 * @param informationObject the informationObject to set
	 */
	public void setInformationObject(InformationObject informationObject) {
		this.informationObject = informationObject;
	}
	/**
	 * @param imageObject the imageObject to set
	 */
	public void setImageObject(ImageObject imageObject) {
		this.imageObject = imageObject;
	}
	/**
	 * @param definitionObject the definitionObject to set
	 */
	public void setDefinitionObject(DefinitionObject definitionObject) {
		this.definitionObject = definitionObject;
	}
	/**
	 * @param relationObject the relationObject to set
	 */
	public void setRelationObject(RelationObject relationObject) {
		this.relationObject = relationObject;
	}
	/**
	 * @param attributeObject the attributeObject to set
	 */
	public void setAttributeObject(HashMap<ClassObject,HashMap<NonFuncObject, Boolean>> attributeObject) {
		this.attributeObject = attributeObject;
	}
	/**
	 * @return the attributeObject
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getAttributeObject() {
		return attributeObject;
	}
	public void setHierarchyObject(HierarchyObject hierarchyObject) {
		this.hierarchyObject = hierarchyObject;
	}
	public HierarchyObject getHierarchyObject() {
		return hierarchyObject;
	}
	/**
	 * @param noteObject the noteObject to set
	 */
	public void setNoteObject(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> noteObject) {
		this.noteObject = noteObject;
	}
	/**
	 * @return the noteObject
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getNoteObject() {
		return noteObject;
	}
	
	public int getTermCount() {
		return termCount;
	}
	public void setTermCount(int termCount) {
		this.termCount = termCount;
	}
	public int getDefinitionCount() {
		return definitionCount;
	}
	public void setDefinitionCount(int definitionCount) {
		this.definitionCount = definitionCount;
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
	public int getNotationCount() {
		return notationCount;
	}
	public void setNotationCount(int notationCount) {
		this.notationCount = notationCount;
	}
	public int getAnnotationCount() {
		return annotationCount;
	}
	public void setAnnotationCount(int annotationCount) {
		this.annotationCount = annotationCount;
	}
	public int getOtherCount() {
		return otherCount;
	}
	public void setOtherCount(int otherCount) {
		this.otherCount = otherCount;
	}
	public int getNoteCount() {
		return noteCount;
	}
	public void setNoteCount(int noteCount) {
		this.noteCount = noteCount;
	}
	public int getImageCount() {
		return imageCount;
	}
	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}
	public int getHistoryCount() {
		return historyCount;
	}
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getNotationObject() {
		return notationObject;
	}
	public void setNotationObject(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> notationObject) {
		this.notationObject = notationObject;
	}
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getAnnotationObject() {
		return annotationObject;
	}
	public void setAnnotationObject(
			HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> annotationObject) {
		this.annotationObject = annotationObject;
	}
	public HashMap<String, String> getSchemeObject() {
		return schemeObject;
	}
	public void setSchemeObject(HashMap<String, String> schemeObject) {
		this.schemeObject = schemeObject;
	}
	public int getSchemeCount() {
		return schemeCount;
	}
	public void setSchemeCount(int schemeCount) {
		this.schemeCount = schemeCount;
	}
	/**
	 * @return the alignmentCount
	 */
	public int getAlignmentCount() {
		return alignmentCount;
	}
	/**
	 * @param alignmentCount the alignmentCount to set
	 */
	public void setAlignmentCount(int alignmentCount) {
		this.alignmentCount = alignmentCount;
	}
	/**
	 * @return the alignmentObject
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getAlignmentObject() {
		return alignmentObject;
	}
	/**
	 * @param alignmentObject the alignmentObject to set
	 */
	public void setAlignmentObject(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> alignmentObject) {
		this.alignmentObject = alignmentObject;
	}
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getOtherObject() {
		return otherObject;
	}
	public void setOtherObject(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> otherObject) {
		this.otherObject = otherObject;
	}
	/**
	 * @return the typeObject
	 */
	public HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> getTypeObject() {
		return typeObject;
	}
	/**
	 * @param typeObject the typeObject to set
	 */
	public void setTypeObject(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> typeObject) {
		this.typeObject = typeObject;
	}
	/**
	 * @return the typeCount
	 */
	public int getTypeCount() {
		return typeCount;
	}
	/**
	 * @param typeCount the typeCount to set
	 */
	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}
}

