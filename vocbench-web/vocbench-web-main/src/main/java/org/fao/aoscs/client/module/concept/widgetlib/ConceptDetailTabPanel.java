package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.ManageConceptTab;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.ConceptDetailObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptDetailTabPanel extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public HorizontalPanel topBar = new HorizontalPanel();
	public HorizontalPanel selectedConceptPanel;	
	public CheckBox showInferredAndExplicit = new CheckBox();
	public HTML showInferredAndExplicitHTML= new HTML("&nbsp;"+constants.conceptShowInferredAndExplicit());
	public Button tabShowHide = new Button(constants.conceptShowHideTabs());
	public DecoratedTabPanel tabPanel ; 
	public ManageConceptTab manageConceptTab;
	private ArrayList<ConceptTab> tabVisibleList = new ArrayList<ConceptTab>();
	private ConceptDetailObject cDetailObj = new ConceptDetailObject();
	
	public ConceptInformation cInfo;
	public ConceptImage cImage ;
	public ConceptDefinition cDef ;
	public ConceptRelationship cRel ;
	public ConceptProperty cNote;
	public ConceptProperty cAttrib;
	public ConceptProperty cNotation;
	public ConceptProperty cAnnotation;
	public ConceptProperty cOther;
	public ConceptProperty cType;
	public ConceptSchemes cScheme;
	public ConceptAlignment cAlign;
	public ConceptResourceView cResourceView;
	public ConceptHierarchy cHier;
	public Term t ;
	
	private ConceptTab selectedTab = ConceptTab.TERM;
	
	public void sayLoading(){
		t.sayLoading();
		cInfo.sayLoading();
		cImage.sayLoading();
		cDef.sayLoading();
		cRel.sayLoading();
		cNote.sayLoading();
		cAttrib.sayLoading();
		cNotation.sayLoading();
		cAnnotation.sayLoading();
		cOther.sayLoading();
		cType.sayLoading();
		cScheme.sayLoading();
		cAlign.sayLoading();
		cResourceView.sayLoading();
		cHier.sayLoading();
	}
	
	public void setSelectedTab(int index){
		tabPanel.selectTab(index);
	}
	
	public ConceptTab getSelectedTab()
	{
		return selectedTab;
	}
	
	public ArrayList<ConceptTab> getVisibleTab()
	{
		return tabVisibleList;
	}
	
	public void setTabHTML(ConceptTab tab, int cnt)
	{
		String str = (cnt)>1? tab.getPluralText():tab.getSingularText();
		if(cnt>-1)
			str += "&nbsp;("+(cnt)+")" ;
		tabPanel.getTabBar().setTabHTML(tab.getTabIndex(), Convert.replaceSpace(str));
	}
	
	public void reload(){
		t.reload();
		cInfo.reload();
		cImage.reload();
		cDef.reload();
		cRel.reload();
		cNote.reload();
		cAttrib.reload();
		cNotation.reload();
		cAnnotation.reload();
		cOther.reload();
		cType.reload();
		cScheme.reload();
		cAlign.reload();
		cResourceView.reload();
		cHier.reload();
	}
	
	private void tabShowHide()
	{
		tabPanel.clear();
		
		for (ConceptTab tab : tabVisibleList) {
			switch(tab.getSortIndex())
			{
				case 0: //Term
					tabPanel.add(t, Convert.replaceSpace(ConceptTab.TERM.getSingularText()));
					ConceptTab.TERM.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 1: //Definition
					tabPanel.add(cDef, Convert.replaceSpace(constants.conceptDefinition()));
					ConceptTab.DEFINITION.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 2: //Attribute
					tabPanel.add(cAttrib, Convert.replaceSpace(constants.conceptAttribute()));
					ConceptTab.ATTRIBUTE.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 3://Relationship
					tabPanel.add(cRel, Convert.replaceSpace(constants.conceptRelationship()));
					ConceptTab.RELATIONSHIP.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 4://Alignment
					tabPanel.add(cAlign, Convert.replaceSpace(constants.conceptAlignment()));
					ConceptTab.ALIGNMENT.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 5: //Note
					tabPanel.add(cNote, Convert.replaceSpace(constants.conceptNote()));
					ConceptTab.NOTE.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 6://Annotation
					tabPanel.add(cAnnotation, Convert.replaceSpace(constants.conceptAnnotation()));
					ConceptTab.ANNOTATION.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 7://Image
					tabPanel.add(cImage, Convert.replaceSpace(constants.conceptImage()));
					ConceptTab.IMAGE.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 8://Schemes
					tabPanel.add(cScheme, Convert.replaceSpace(constants.conceptScheme()));
					ConceptTab.SCHEME.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 9://Other
					tabPanel.add(cOther, Convert.replaceSpace(constants.conceptOther()));
					ConceptTab.OTHER.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 10: //Notation
					tabPanel.add(cNotation, Convert.replaceSpace(constants.conceptNotation()));
					ConceptTab.NOTATION.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 11: //Type
					tabPanel.add(cType, Convert.replaceSpace(constants.conceptType()));
					ConceptTab.TYPE.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 12://Resource view
					tabPanel.add(cResourceView, Convert.replaceSpace(constants.conceptResourceView()));
					ConceptTab.RESOURCEVIEW.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 13://Hierarchy
					tabPanel.add(cHier, Convert.replaceSpace(constants.conceptHierarchy()));
					ConceptTab.HIERARCHY.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
				case 14://History
					tabPanel.add(cInfo, Convert.replaceSpace(constants.conceptHistory()));	
					ConceptTab.HISTORY.setTabIndex(tabPanel.getWidgetCount()-1);
					break;
			}
		} 
		
		loadTab(cDetailObj);
	}
	
	public ConceptDetailTabPanel(PermissionObject permisstionTable,InitializeConceptData initData){

		selectedConceptPanel = new HorizontalPanel();		
		selectedConceptPanel.setWidth("100%");
		selectedConceptPanel.setSpacing(2);
		
		tabPanel = new DecoratedTabPanel();
		tabPanel.setAnimationEnabled(true);
		tabPanel.setSize("99%", "100%");
		tabPanel.getDeckPanel().setHeight("100%");
		
		cInfo = new ConceptInformation(permisstionTable,initData, this, null);
		cImage = new ConceptImage(permisstionTable,initData, this, null);
		cDef = new ConceptDefinition(permisstionTable,initData, this, null);
		cRel = new ConceptRelationship(permisstionTable,initData, this, null);
		cNote = new ConceptProperty(ConceptProperty.CONCEPTNOTE, permisstionTable, initData, this, null);
		cAttrib = new ConceptProperty(ConceptProperty.CONCEPTATTRIBUTE, permisstionTable, initData, this, null);
		cNotation = new ConceptProperty(ConceptProperty.CONCEPTNOTATION, permisstionTable, initData, this, null);
		cAnnotation = new ConceptProperty(ConceptProperty.CONCEPTANNOTATION, permisstionTable, initData, this, null);
		cOther = new ConceptProperty(ConceptProperty.CONCEPTOTHER, permisstionTable, initData, this, null);
		cType = new ConceptProperty(ConceptProperty.CONCEPTTYPE, permisstionTable, initData, this, null);
		cScheme = new ConceptSchemes(permisstionTable, initData, this, null);
		cAlign = new ConceptAlignment(permisstionTable, initData, this, null);
		cResourceView = new ConceptResourceView(permisstionTable, initData, this, null);
		cHier = new ConceptHierarchy(permisstionTable, initData, this, null);
		t = new Term(permisstionTable,initData, this, null);
		
		tabVisibleList.add(ConceptTab.TERM);
		tabVisibleList.add(ConceptTab.DEFINITION);
		tabVisibleList.add(ConceptTab.ATTRIBUTE);
		tabVisibleList.add(ConceptTab.RELATIONSHIP);
		tabVisibleList.add(ConceptTab.ALIGNMENT);
		tabVisibleList.add(ConceptTab.NOTE);
		//tabVisibleList.add(ConceptTab.ANNOTATION);
		tabVisibleList.add(ConceptTab.IMAGE);
		tabVisibleList.add(ConceptTab.SCHEME);
		//tabVisibleList.add(ConceptTab.OTHER);
		//tabVisibleList.add(ConceptTab.NOTATION);
		//tabVisibleList.add(ConceptTab.TYPE);
		tabVisibleList.add(ConceptTab.HIERARCHY);
		//tabVisibleList.add(ConceptTab.TYPE);
		//tabVisibleList.add(ConceptTab.RESOURCEVIEW);
		tabVisibleList.add(ConceptTab.HISTORY);
		
		tabShowHide();

		tabPanel.addSelectionHandler(new SelectionHandler<Integer>()
		{
			public void onSelection(SelectionEvent<Integer> event) {
				selectedTab = tabVisibleList.get(event.getSelectedItem());
				ConceptTemplate w = (ConceptTemplate) tabPanel.getWidget(event.getSelectedItem());
				if(w.getConceptObject()!=null)
					w.initData();
			}
			
		});
		
		tabShowHide.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(manageConceptTab == null || !manageConceptTab.isLoaded)
					manageConceptTab = new ManageConceptTab();
				manageConceptTab.show(tabVisibleList);
				manageConceptTab.addSubmitClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						tabVisibleList = manageConceptTab.getSelectedTab();
						tabShowHide();
					}					
				});		
			}
		});
		
		HorizontalPanel tabHP = new HorizontalPanel();
		tabHP.setSize("100%", "100%");
		tabHP.setSpacing(5);
		tabHP.add(tabPanel);
		tabHP.setCellHeight(tabPanel, "100%");
		
		topBar.setStyleName("showshort");
		topBar.setWidth("100%");
		
		topBar.add(selectedConceptPanel);					
		topBar.add(showInferredAndExplicit);
		topBar.add(showInferredAndExplicitHTML);
		topBar.add(new Spacer("10px", "100%"));
		topBar.add(tabShowHide);
		topBar.add(new Spacer("10px", "100%"));
		
		topBar.setCellHorizontalAlignment(selectedConceptPanel, HasHorizontalAlignment.ALIGN_LEFT);
		topBar.setCellWidth(selectedConceptPanel, "100%");
		topBar.setCellHorizontalAlignment(showInferredAndExplicit, HasHorizontalAlignment.ALIGN_RIGHT);
		topBar.setCellVerticalAlignment(showInferredAndExplicit, HasVerticalAlignment.ALIGN_MIDDLE);
		topBar.setCellHorizontalAlignment(showInferredAndExplicitHTML, HasHorizontalAlignment.ALIGN_RIGHT);
		topBar.setCellVerticalAlignment(showInferredAndExplicitHTML, HasVerticalAlignment.ALIGN_MIDDLE);
		topBar.setCellHorizontalAlignment(tabShowHide, HasHorizontalAlignment.ALIGN_RIGHT);
		topBar.setCellVerticalAlignment(tabShowHide, HasVerticalAlignment.ALIGN_MIDDLE);
		
		VerticalPanel panel = new VerticalPanel();		
		panel.add(topBar);					
		panel.add(tabHP);
		panel.setCellHeight(topBar, "18px");
		panel.setCellHeight(tabHP, "100%");
		panel.setCellWidth(tabHP, "100%");
		panel.setCellVerticalAlignment(tabHP, HasVerticalAlignment.ALIGN_TOP);
		panel.setSize("100%", "100%");		
		initWidget(panel);
	}
	
	public void resetTab()
	{
		for(ConceptTab tab : tabVisibleList)
		{
			setTabHTML(tab, 0);
		}
		
		/*
		if(tabVisibleList.contains(ConceptTab.TERM))
			tabPanel.getTabBar().setTabHTML(ConceptTab.TERM.getTabIndex(), Convert.replaceSpace(constants.conceptTerm()));
		if(tabVisibleList.contains(ConceptTab.DEFINITION))
			tabPanel.getTabBar().setTabHTML(ConceptTab.DEFINITION.getTabIndex(), Convert.replaceSpace(constants.conceptDefinition()));
		if(tabVisibleList.contains(ConceptTab.NOTE))
			tabPanel.getTabBar().setTabHTML(ConceptTab.NOTE.getTabIndex(), Convert.replaceSpace(constants.conceptNote()));
		if(tabVisibleList.contains(ConceptTab.ATTRIBUTE))
			tabPanel.getTabBar().setTabHTML(ConceptTab.ATTRIBUTE.getTabIndex(), Convert.replaceSpace(constants.conceptAttribute()));
		if(tabVisibleList.contains(ConceptTab.NOTATION))
			tabPanel.getTabBar().setTabHTML(ConceptTab.NOTATION.getTabIndex(), Convert.replaceSpace(constants.conceptNotation()));
		if(tabVisibleList.contains(ConceptTab.ANNOTATION))
			tabPanel.getTabBar().setTabHTML(ConceptTab.ANNOTATION.getTabIndex(), Convert.replaceSpace(constants.conceptAnnotation()));
		if(tabVisibleList.contains(ConceptTab.OTHER))
			tabPanel.getTabBar().setTabHTML(ConceptTab.OTHER.getTabIndex(), Convert.replaceSpace(constants.conceptOther()));
		if(tabVisibleList.contains(ConceptTab.RELATIONSHIP))
			tabPanel.getTabBar().setTabHTML(ConceptTab.RELATIONSHIP.getTabIndex(), Convert.replaceSpace(constants.conceptRelationship()));
		if(tabVisibleList.contains(ConceptTab.HISTORY))
			tabPanel.getTabBar().setTabHTML(ConceptTab.HISTORY.getTabIndex(), Convert.replaceSpace(constants.conceptHistory()));
		if(tabVisibleList.contains(ConceptTab.IMAGE))
			tabPanel.getTabBar().setTabHTML(ConceptTab.IMAGE.getTabIndex(), Convert.replaceSpace(constants.conceptImage()));
		if(tabVisibleList.contains(ConceptTab.SCHEME))
			tabPanel.getTabBar().setTabHTML(ConceptTab.SCHEME.getTabIndex(), Convert.replaceSpace(constants.conceptScheme()));
		if(tabVisibleList.contains(ConceptTab.ALIGNMENT))
			tabPanel.getTabBar().setTabHTML(ConceptTab.ALIGNMENT.getTabIndex(), Convert.replaceSpace(constants.conceptAlignment()));
		if(tabVisibleList.contains(ConceptTab.HIERARCHY))
			tabPanel.getTabBar().setTabHTML(ConceptTab.HIERARCHY.getTabIndex(), Convert.replaceSpace(constants.conceptHierarchy()));
		*/
	}
	
	public void initData(ConceptDetailObject cDetailObj){
		if(cDetailObj!=null)
		{
			this.cDetailObj = cDetailObj;
			t.setConceptObject(cDetailObj.getConceptObject());
			cDef.setConceptObject(cDetailObj.getConceptObject());
			cNote.setConceptObject(cDetailObj.getConceptObject());
			cAttrib.setConceptObject(cDetailObj.getConceptObject());
			cNotation.setConceptObject(cDetailObj.getConceptObject());
			cAnnotation.setConceptObject(cDetailObj.getConceptObject());
			cOther.setConceptObject(cDetailObj.getConceptObject());
			cType.setConceptObject(cDetailObj.getConceptObject());
			cRel.setConceptObject(cDetailObj.getConceptObject());
			cInfo.setConceptObject(cDetailObj.getConceptObject());
			cImage.setConceptObject(cDetailObj.getConceptObject());
			cScheme.setConceptObject(cDetailObj.getConceptObject());
			cAlign.setConceptObject(cDetailObj.getConceptObject());
			cResourceView.setConceptObject(cDetailObj.getConceptObject());
			cHier.setConceptObject(cDetailObj.getConceptObject());
			
			t.setConceptDetailObject(cDetailObj);
			cDef.setConceptDetailObject(cDetailObj);
			cNote.setConceptDetailObject(cDetailObj);
			cAttrib.setConceptDetailObject(cDetailObj);
			cNotation.setConceptDetailObject(cDetailObj);
			cAnnotation.setConceptDetailObject(cDetailObj);
			cOther.setConceptDetailObject(cDetailObj);
			cType.setConceptDetailObject(cDetailObj);
			cRel.setConceptDetailObject(cDetailObj);
			cInfo.setConceptDetailObject(cDetailObj);
			cImage.setConceptDetailObject(cDetailObj);
			cScheme.setConceptDetailObject(cDetailObj);
			cAlign.setConceptDetailObject(cDetailObj);
			cResourceView.setConceptDetailObject(cDetailObj);
			cHier.setConceptDetailObject(cDetailObj);
			
			//t.initData();
		}
		
	}
	
	public void loadTab(ConceptDetailObject cDetailObj)
	{
		this.cDetailObj = cDetailObj;
		tabPanel.selectTab(selectedTab.getTabIndex());
		
		for (ConceptTab tab : tabVisibleList) {
			switch(tab.getSortIndex())
			{
				case 0: //Term
					setTabHTML(tab, cDetailObj.getTermCount());
					break;
				case 1: //Definition
					setTabHTML(tab, cDetailObj.getDefinitionCount());
					break;
				case 2: //Attribute
					setTabHTML(tab, cDetailObj.getAttributeCount());
					break;
				case 3://Relationship
					setTabHTML(tab, cDetailObj.getRelationCount());
					break;
				case 4://Alignment
					setTabHTML(tab, cDetailObj.getAlignmentCount());
					break;
				case 5: //Note
					setTabHTML(tab, cDetailObj.getNoteCount());
					break;
				case 6://Annotation
					setTabHTML(tab, cDetailObj.getAnnotationCount());
					break;
				case 7://Image
					setTabHTML(tab, cDetailObj.getImageCount());
					break;
				case 8://Schemes
					setTabHTML(tab, cDetailObj.getSchemeCount());
					break;
				case 9://Other
					setTabHTML(tab, cDetailObj.getOtherCount());
					break;
				case 10: //Notation
					setTabHTML(tab, cDetailObj.getNotationCount());
					break;
				case 11: //Type
					setTabHTML(tab, cDetailObj.getTypeCount());
					break;
				case 12://Resource 
					setTabHTML(tab, -1);
					break;
				case 13://Hierarchy
					setTabHTML(tab, -1);
					break;
				case 14://History
					setTabHTML(tab, cDetailObj.getHistoryCount());
					break;
			}
		} 
		
		/*
		if(tabVisibleList.contains(ConceptTab.TERM))
			tabPanel.getTabBar().setTabHTML(ConceptTab.TERM.getTabIndex(), Convert.replaceSpace((cDetailObj.getTermCount())>1? constants.conceptTerms():constants.conceptTerm() ) +"&nbsp;("+(cDetailObj.getTermCount())+")" );
		if(tabVisibleList.contains(ConceptTab.DEFINITION))
			tabPanel.getTabBar().setTabHTML(ConceptTab.DEFINITION.getTabIndex(), Convert.replaceSpace((cDetailObj.getDefinitionCount())>1? constants.conceptDefinitions():constants.conceptDefinition() ) +"&nbsp;("+(cDetailObj.getDefinitionCount())+")" );
		if(tabVisibleList.contains(ConceptTab.NOTE))
			tabPanel.getTabBar().setTabHTML(ConceptTab.NOTE.getTabIndex(), Convert.replaceSpace((cDetailObj.getNoteCount())>1? constants.conceptNotes():constants.conceptNote() ) +"&nbsp;("+(cDetailObj.getNoteCount())+")" );
		if(tabVisibleList.contains(ConceptTab.ATTRIBUTE))
			tabPanel.getTabBar().setTabHTML(ConceptTab.ATTRIBUTE.getTabIndex(), Convert.replaceSpace((cDetailObj.getAttributeCount())>1? constants.conceptAttributes():constants.conceptAttribute() ) +"&nbsp;("+(cDetailObj.getAttributeCount())+")" );
		if(tabVisibleList.contains(ConceptTab.NOTATION))
			tabPanel.getTabBar().setTabHTML(ConceptTab.NOTATION.getTabIndex(), Convert.replaceSpace((cDetailObj.getNotationCount())>1? constants.conceptNotations():constants.conceptNotation() ) +"&nbsp;("+(cDetailObj.getNotationCount())+")" );
		if(tabVisibleList.contains(ConceptTab.ANNOTATION))
			tabPanel.getTabBar().setTabHTML(ConceptTab.ANNOTATION.getTabIndex(), Convert.replaceSpace((cDetailObj.getAnnotationCount())>1? constants.conceptAnnotations():constants.conceptAnnotation() ) +"&nbsp;("+(cDetailObj.getAnnotationCount())+")" );
		if(tabVisibleList.contains(ConceptTab.OTHER))
			tabPanel.getTabBar().setTabHTML(ConceptTab.OTHER.getTabIndex(), Convert.replaceSpace((cDetailObj.getOtherCount())>1? constants.conceptOthers():constants.conceptOther() ) +"&nbsp;("+(cDetailObj.getOtherCount())+")" );
		if(tabVisibleList.contains(ConceptTab.RELATIONSHIP))
			tabPanel.getTabBar().setTabHTML(ConceptTab.RELATIONSHIP.getTabIndex(), Convert.replaceSpace((cDetailObj.getRelationCount())>1? constants.conceptRelationships():constants.conceptRelationship() ) +"&nbsp;("+(cDetailObj.getRelationCount())+")" );
		if(tabVisibleList.contains(ConceptTab.IMAGE))
			tabPanel.getTabBar().setTabHTML(ConceptTab.IMAGE.getTabIndex(), Convert.replaceSpace((cDetailObj.getImageCount())>1? constants.conceptImages():constants.conceptImage() ) +"&nbsp;("+(cDetailObj.getImageCount())+")" );
		if(tabVisibleList.contains(ConceptTab.SCHEME))
			tabPanel.getTabBar().setTabHTML(ConceptTab.SCHEME.getTabIndex(), Convert.replaceSpace((cDetailObj.getSchemeCount())>1? constants.conceptSchemes():constants.conceptScheme() ) +"&nbsp;("+(cDetailObj.getSchemeCount())+")" );
		if(tabVisibleList.contains(ConceptTab.ALIGNMENT))
			tabPanel.getTabBar().setTabHTML(ConceptTab.ALIGNMENT.getTabIndex(), Convert.replaceSpace((cDetailObj.getAlignmentCount())>1? constants.conceptAlignments():constants.conceptAlignment() ) +"&nbsp;("+(cDetailObj.getAlignmentCount())+")" );
		if(tabVisibleList.contains(ConceptTab.HIERARCHY))
			tabPanel.getTabBar().setTabHTML(ConceptTab.HIERARCHY.getTabIndex(), Convert.replaceSpace(constants.conceptHierarchy()));
	*/
	}
	
	public void loadHistoryTab(int historyCount)
	{
		if(tabVisibleList.contains(ConceptTab.HISTORY.getTabIndex()))
			tabPanel.getTabBar().setTabHTML(ConceptTab.HISTORY.getTabIndex(), Convert.replaceSpace((historyCount)>1? constants.conceptHistory():constants.conceptHistory() ) +"&nbsp;("+(historyCount)+")" );
	}
	
	public void clearData(){
		t.sayLoading();
		cDef.sayLoading();
		cNote.sayLoading();
		cAttrib.sayLoading();
		cNotation.sayLoading();
		cAnnotation.sayLoading();
		cOther.sayLoading();
		cType.sayLoading();
		cRel.sayLoading();
		cInfo.sayLoading();
		cImage.sayLoading();
		cScheme.sayLoading();
		cAlign.sayLoading();
		cResourceView.sayLoading();
		cHier.sayLoading();
	}
}
