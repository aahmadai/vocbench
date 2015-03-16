package org.fao.aoscs.client.module.concept.widgetlib;

import org.fao.aoscs.client.MainApp;

	public enum ConceptTab {
		TERM (0, MainApp.constants.conceptTerm(), MainApp.constants.conceptTerms()),
		DEFINITION (1, MainApp.constants.conceptDefinition(), MainApp.constants.conceptDefinitions()),
		ATTRIBUTE (2, MainApp.constants.conceptAttribute(), MainApp.constants.conceptAttributes()),
		RELATIONSHIP (3, MainApp.constants.conceptRelationship(), MainApp.constants.conceptRelationships()),
		ALIGNMENT (4, MainApp.constants.conceptAlignment(), MainApp.constants.conceptAlignments()),
		NOTE (5, MainApp.constants.conceptNote(), MainApp.constants.conceptNotes()),
		ANNOTATION (6, MainApp.constants.conceptAnnotation(), MainApp.constants.conceptAnnotations()),
		IMAGE (7, MainApp.constants.conceptImage(), MainApp.constants.conceptImages()),
		SCHEME (8, MainApp.constants.conceptScheme(), MainApp.constants.conceptSchemes()),
		OTHER (9, MainApp.constants.conceptOther(), MainApp.constants.conceptOthers()),
		NOTATION (10, MainApp.constants.conceptNotation(), MainApp.constants.conceptNotations()),
		HIERARCHY (11, MainApp.constants.conceptHierarchy(), MainApp.constants.conceptNotations()),
		HISTORY (12, MainApp.constants.conceptHistory(), MainApp.constants.conceptHistory());
	
	private final int sortIndex;
	private final String singularText;
	private final String pluralText;
	private int tabIndex = 0;

	private ConceptTab(int sortIndex, String singularText, String pluralText) {
        this.sortIndex = sortIndex;
        this.singularText = singularText;
        this.pluralText = pluralText;
    }
    
	 public int getSortIndex() {
    	return this.sortIndex;
    }
	 
    public int getTabIndex() {
        return this.tabIndex;
    }
    
    public void setTabIndex(int tabIndex) {
    	this.tabIndex = tabIndex;
    }
    
    public String getSingularText() {
        return this.singularText;
    }
    
    public String getPluralText() {
        return this.pluralText;
    }
}
