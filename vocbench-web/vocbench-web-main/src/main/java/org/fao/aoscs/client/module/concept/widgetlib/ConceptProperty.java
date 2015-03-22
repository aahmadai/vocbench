package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptProperty extends ConceptTemplate{

	public static String CONCEPTNOTE = "CONCEPTNOTE";
	public static String CONCEPTATTRIBUTE = "CONCEPTATTRIBUTE";
	public static String CONCEPTNOTATION = "CONCEPTNOTATION";
	public static String CONCEPTANNOTATION = "CONCEPTANNOTATION";
	public static String CONCEPTOTHER = "CONCEPTOTHER";
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private String property ;
	private int cnt = 0;
	private AddValue addValue ;
	private EditValue editValue ;
	private DeleteValue deleteValue ;

	public ConceptProperty(String property, PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable, initData, conceptDetailPanel, classificationDetailPanel);
		this.property = property ;
	}

	private void attachNewImgButton(){

		functionPanel.clear();
		String label = "";
		boolean permission = false;
		if(property.equals(ConceptProperty.CONCEPTNOTE)){
			label = constants.conceptAddNotes();
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_NOTECREATE, getConceptObject().getStatusID());
		}else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
			label = constants.conceptAddAttributes();
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTECREATE, getConceptObject().getStatusID());
		}
		else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
			label = constants.conceptAddNotation();
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTECREATE, getConceptObject().getStatusID());
		}
		else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
			label = constants.conceptAddAnnotation();
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTECREATE, getConceptObject().getStatusID());
		}
		else if(property.equals(ConceptProperty.CONCEPTOTHER)){
			label = constants.conceptAddOther();
			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTECREATE, getConceptObject().getStatusID());
		}

		//permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_NOTECREATE, getConceptObject().getStatusID());
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", label, label, permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addValue == null || !addValue.isLoaded)
					addValue = new AddValue();
				addValue.show();
			}
		});
		add.setLabelText(label);
		this.functionPanel.add(add);

	}

	private VerticalPanel getFuncButtons(String rObj, HashMap<NonFuncObject, Boolean> values)
	{
		VerticalPanel vp = new VerticalPanel();
		for(NonFuncObject value: values.keySet())
		{
			vp.add(getFuncButton(rObj, value, values.get(value)));
			cnt++;
		}
		return vp;
	}

	private HorizontalPanel getFuncButton(final String rObj, final NonFuncObject value, boolean isExplicit){
		HorizontalPanel hp = new HorizontalPanel();
		//if(permissionTable.get("concept-nonfunctional").equals("enable")&& permissionTable.get("concept-nonfunctional").equals("enable"))
		{
		    String labelEdit = "";
		    String labelDelete = "";
		    boolean permissionEdit = false;
		    boolean permissionDelete = false;
		    
		    if(property.equals(ConceptProperty.CONCEPTNOTE)){
		    	labelEdit = constants.conceptEditNotes();
                labelDelete = constants.conceptDeleteNotes();
                permissionEdit = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_NOTEEDIT, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
                permissionDelete = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_NOTEDELETE, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;

		    }
		    else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
		    	labelEdit = constants.conceptEditAttributes();
		        labelDelete = constants.conceptDeleteAttributes();
		        permissionEdit = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEEDIT, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		        permissionDelete = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEDELETE, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		    }
		    else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
		    	labelEdit = constants.conceptEditNotation();
		        labelDelete = constants.conceptDeleteNotation();
		        permissionEdit = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEEDIT, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		        permissionDelete = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEDELETE, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		    }
		    else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
		    	labelEdit = constants.conceptEditAnnotation();
		    	labelDelete = constants.conceptDeleteAnnotation();
		    	permissionEdit =  false;//permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEEDIT, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		    	permissionDelete = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEDELETE, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		    }
		    else if(property.equals(ConceptProperty.CONCEPTOTHER)){
		    	labelEdit = constants.conceptEditOther();
		    	labelDelete = constants.conceptDeleteOther();
		    	permissionEdit = false;//permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEEDIT, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		    	permissionDelete = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_ATTRIBUTEDELETE, getConceptObject().getStatusID(), value.getLanguage(), MainApp.getPermissionCheck(value.getLanguage())) && isExplicit;
		    }

			hp.setSpacing(3);
			
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", labelEdit, permissionEdit, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editValue == null || !editValue.isLoaded)
						editValue = new EditValue(rObj, value);
					editValue.show();
				}
			});

			
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", labelDelete, permissionDelete, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteValue == null || !deleteValue.isLoaded)
						deleteValue = new DeleteValue(rObj, value);
					deleteValue.show();
				}
			});
			hp.add(edit);
			hp.add(delete);
		}
		HTML html = new HTML();
		if(value.getLanguage()!=null && !value.getLanguage().equals("") && !value.getLanguage().equals("null"))
		{
			html = new HTML(value.getValue()+" ("+value.getLanguage()+")");
		}
		else
		{
			html = new HTML(""+value.getValue());
		}
		if(!isExplicit)
			html.setStyleName("link-label-aos-explicit");
		hp.add(html);
		return hp;
	}
	public void initLayout(){
		this.sayLoading();
		if(property.equals(ConceptProperty.CONCEPTNOTE))
		{
			if(cDetailObj!=null && cDetailObj.getNoteObject()!=null)
			{
				initData(cDetailObj.getNoteObject());
			}
			else
				getData();
		}
		else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE))
		{
			if(cDetailObj!=null && cDetailObj.getAttributeObject()!=null)
			{
				initData(cDetailObj.getAttributeObject());
			}
			else
				getData();
		}
		else if(property.equals(ConceptProperty.CONCEPTNOTATION))
		{
			if(cDetailObj!=null && cDetailObj.getNotationObject()!=null)
			{
				initData(cDetailObj.getNotationObject());
			}
			else
				getData();
		}
		else if(property.equals(ConceptProperty.CONCEPTANNOTATION))
		{
			if(cDetailObj!=null && cDetailObj.getAnnotationObject()!=null)
			{
				initData(cDetailObj.getAnnotationObject());
			}
			else
				getData();
		}
		else if(property.equals(ConceptProperty.CONCEPTOTHER))
		{
			if(cDetailObj!=null && cDetailObj.getOtherObject()!=null)
			{
				initData(cDetailObj.getOtherObject());
			}
			else
				getData();
		}
	}
	
	private void getData()
	{
		if(property.equals(ConceptProperty.CONCEPTNOTE))
		{
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
			{
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> result) {
					cDetailObj.setNoteObject(result);
					initData(result);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptAttributeFail());
				}
			};
			Service.conceptService.getConceptNoteValue(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
		else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE))
		{
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
			{
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> result) {
					cDetailObj.setAttributeObject(result);
					initData(result);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptAttributeFail());
				}
			};
			Service.conceptService.getConceptAttributeValue(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
		else if(property.equals(ConceptProperty.CONCEPTNOTATION))
		{
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
			{
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> result) {
					cDetailObj.setNotationObject(result);
					initData(result);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptAttributeFail());
				}
			};
			Service.conceptService.getConceptNotationValue(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
		else if(property.equals(ConceptProperty.CONCEPTANNOTATION))
		{
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
					{
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> result) {
					cDetailObj.setAnnotationObject(result);
					initData(result);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptAttributeFail());
				}
			};
			Service.conceptService.getConceptAnnotationValue(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
		else if(property.equals(ConceptProperty.CONCEPTOTHER))
		{
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
					{
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> result) {
					cDetailObj.setOtherObject(result);
					initData(result);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptAttributeFail());
				}
					};
					Service.conceptService.getConceptOtherValue(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
		
	}
	
	private ArrayList<ClassObject> getSortedList(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> list)
	{
		ArrayList<ClassObject> sortedList = new ArrayList<ClassObject>();
		ArrayList<ClassObject> sortedNonExplicitList = new ArrayList<ClassObject>();
		
		HashMap<String, ClassObject> sortedRelationConceptList = new HashMap<String, ClassObject>();
		for(ClassObject clsObj : list.keySet())
		{
			sortedRelationConceptList.put(clsObj.getLabel(), clsObj);
		}
		List<String> labelKeys = new ArrayList<String>(sortedRelationConceptList.keySet()); 
		Collections.sort(labelKeys, String.CASE_INSENSITIVE_ORDER);
		
		for (Iterator<String> itr = labelKeys.iterator(); itr.hasNext();){
			ClassObject clsObj = sortedRelationConceptList.get(itr.next());
			if(list.get(clsObj).values().contains(true))
				sortedList.add(clsObj);
			else
				sortedNonExplicitList.add(clsObj);
        }

		sortedList.addAll(sortedNonExplicitList);
		return sortedList;
	}

	private void initData(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> list)
	{
		clearPanel();
		cnt = 0;
		attachNewImgButton();
		Grid table = new Grid(list.size()+1,2);
		if(property.equals(ConceptProperty.CONCEPTNOTE)){
			table.setWidget(0, 0, new HTML(constants.conceptNotes()));
		}else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
			table.setWidget(0, 0, new HTML(constants.conceptAttributes()));
		}
		else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
			table.setWidget(0, 0, new HTML(constants.conceptNotation()));
		}
		else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
			table.setWidget(0, 0, new HTML(constants.conceptAnnotation()));
		}
		else if(property.equals(ConceptProperty.CONCEPTOTHER)){
			table.setWidget(0, 0, new HTML(constants.conceptOther()));
		}
		table.setWidget(0, 1, new HTML(constants.conceptValue()));

		int i=0;
		if(!list.isEmpty()){
			for(ClassObject clsObj: getSortedList(list)){
				HashMap<NonFuncObject, Boolean> values = (HashMap<NonFuncObject, Boolean>) list.get(clsObj);
				table.setWidget(i+1, 0, new HTML(clsObj.getLabel()));
				table.setWidget(i+1, 1, getFuncButtons(clsObj.getUri(), values));
				i++;
			}

			if(property.equals(ConceptProperty.CONCEPTNOTE)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.NOTE.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptNotes():constants.conceptNote())+"&nbsp;("+(cnt)+")");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.NOTE.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptNotes():constants.conceptNote())+"&nbsp;("+(cnt)+")");
			}else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.ATTRIBUTE.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptAttributes():constants.conceptAttribute())+"&nbsp;("+(cnt)+")");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.ATTRIBUTE.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptAttributes():constants.conceptAttribute())+"&nbsp;("+(cnt)+")");
			}
			else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.NOTATION.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptNotations():constants.conceptNotation())+"&nbsp;("+(cnt)+")");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.NOTATION.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptNotations():constants.conceptNotation())+"&nbsp;("+(cnt)+")");
			}
			else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.ANNOTATION.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptAnnotations():constants.conceptAnnotation())+"&nbsp;("+(cnt)+")");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.ANNOTATION.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptAnnotations():constants.conceptAnnotation())+"&nbsp;("+(cnt)+")");
			}
			else if(property.equals(ConceptProperty.CONCEPTOTHER)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.OTHER.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptOthers():constants.conceptOther())+"&nbsp;("+(cnt)+")");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.OTHER.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptOthers():constants.conceptOther())+"&nbsp;("+(cnt)+")");
			}
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			String label = "No value";
			if(property.equals(ConceptProperty.CONCEPTNOTE)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.NOTE.getTabIndex(), Convert.replaceSpace(constants.conceptNote())+"&nbsp;(0)");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.NOTE.getTabIndex(), Convert.replaceSpace(constants.conceptNote())+"&nbsp;(0)");
				label = constants.conceptNoNotes();
			}else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.ATTRIBUTE.getTabIndex(), Convert.replaceSpace(constants.conceptAttribute())+"&nbsp;(0)");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.ATTRIBUTE.getTabIndex(), Convert.replaceSpace(constants.conceptAttribute())+"&nbsp;(0)");
				label = constants.conceptNoAttributes();
			}
			else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.NOTATION.getTabIndex(), Convert.replaceSpace(constants.conceptNotation())+"&nbsp;(0)");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.NOTATION.getTabIndex(), Convert.replaceSpace(constants.conceptNotation())+"&nbsp;(0)");
				label = constants.conceptNoNotation();
			}
			else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.ANNOTATION.getTabIndex(), Convert.replaceSpace(constants.conceptAnnotation())+"&nbsp;(0)");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.ANNOTATION.getTabIndex(), Convert.replaceSpace(constants.conceptAnnotation())+"&nbsp;(0)");
				label = constants.conceptNoAnnotation();
			}
			else if(property.equals(ConceptProperty.CONCEPTOTHER)){
				if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.OTHER.getTabIndex(), Convert.replaceSpace(constants.conceptOther())+"&nbsp;(0)");
				if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.OTHER.getTabIndex(), Convert.replaceSpace(constants.conceptOther())+"&nbsp;(0)");
				label = constants.conceptNoOther();
			}
			Label sayNo = new Label(label);
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}

	public class EditValue extends FormDialogBox implements ClickHandler{
		private TextArea value;
		private ListBox values;
		private ListBox language;
		private LabelAOS relationship;
		private NonFuncObject oldValue;
		private DomainRangeObject drObj = new DomainRangeObject();
		private String relURI;
		private ArrayList<String> list = null;
		private String type = "";
		

		public EditValue(String relURI/*RelationshipObject rObj*/, NonFuncObject oldValue){
			super();
			this.relURI = relURI;
			this.oldValue = oldValue;
			String label = "";
			if(property.equals(ConceptProperty.CONCEPTNOTE)){
                label = constants.conceptEditNotes();
            }else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
                label = constants.conceptEditAttributes();
            }
            else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
                label = constants.conceptEditNotation();
            }
            else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
            	label = constants.conceptEditAnnotation();
            }
            else if(property.equals(ConceptProperty.CONCEPTOTHER)){
            	label = constants.conceptEditOther();
            }
			this.setText(label);
			this.initLayout();
			setWidth("400px");

		}
		public void initLayout() {
			
			
			relationship = new LabelAOS();
			//relationship.setText(Convert.getRelationshipLabel(rObj, constants.mainLocale()), rObj);
			relationship.setText(relURI);

			final FlexTable table = new FlexTable();
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			table.setWidget(0, 1, relationship);
			
			type = "";
			list = null;

			
			if(relURI!=null && !relURI.equals(""))
			{
				AsyncCallback<DomainRangeObject> callback = new AsyncCallback<DomainRangeObject>(){
					public void onSuccess(DomainRangeObject results){
						if(results!=null)
						{
							drObj = results;
							type = drObj.getRangeType();
							list = new ArrayList<String>();
							
							for(ClassObject clsObj : drObj.getRange())
							{
								if(!type.equals(DomainRangeObject.typedLiteral))
								{
									list.add(clsObj.getLabel());
								}
							}
						}

						//check value or values
						if(list!=null && list.size()>0)
						{
							values = Convert.makeListBoxSingleValueWithSelectedValue(list, oldValue.getValue());
							values.setWidth("100%");
							table.setWidget(1, 0, new HTML(constants.conceptValue()));
							table.setWidget(1, 1, values);
						}
						else
						{
							value = new TextArea();
							value.setText(oldValue.getValue());
							value.setVisibleLines(5);
							value.setWidth("100%");
							table.setWidget(1, 0, new HTML(constants.conceptValue()));
							table.setWidget(1, 1, value);

							// check language
							if(!type.equals(DomainRangeObject.typedLiteral) && oldValue.getLanguage()!=null && !oldValue.getLanguage().equals("null") && !oldValue.getLanguage().equals(""))
							{
								language = new ListBox();
								language = Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),oldValue.getLanguage());
								language.setWidth("100%");
								language.setEnabled(false);
								table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
								table.setWidget(2, 1, language);
							}
						}
						addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));						
						
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptAddValueFail());
					}
				};
				Service.conceptService.getPropertyRange(relURI, MainApp.userOntology, callback);
				
				
			}
				
		}
		public boolean passCheckInput() {
			if(relationship.getText()==null){
				return false;
			}
			else
			{
				//check value or values
				if(list!=null && list.size()>0)
				{
					if(values.getValue(values.getSelectedIndex()).equals("--None--") || values.getValue(values.getSelectedIndex()).equals(""))
					{
						return false;
					}
				}
				else
				{
					if(value.getText().length()==0)
					{
						return false;
					}
					// check language
					if(!type.equals(DomainRangeObject.typedLiteral) && oldValue.getLanguage()!=null && !oldValue.getLanguage().equals("null") && !oldValue.getLanguage().equals(""))
					{
						if(language.getValue(language.getSelectedIndex()).equals("--None--") || language.getValue(language.getSelectedIndex()).equals(""))
						{
							return false;
						}
					}
				}

			}
			return true;
		}
		public void onSubmit() {
			sayLoading();

			NonFuncObject nonFuncObj = new NonFuncObject();
			//check value or values
			nonFuncObj.setType(oldValue.getType());
			if(list!=null && list.size()>0)
			{
				nonFuncObj.setValue(values.getValue(values.getSelectedIndex()));
			}
			else
			{
				nonFuncObj.setValue(value.getText());
				// check language
				if(!type.equals(DomainRangeObject.typedLiteral) && oldValue.getLanguage()!=null && !oldValue.getLanguage().equals("null") && !oldValue.getLanguage().equals(""))
				{
					nonFuncObj.setLanguage(language.getValue(language.getSelectedIndex()));
				}
			}

			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>(){
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> results){
					if(property.equals(ConceptProperty.CONCEPTNOTE))
					{
						cDetailObj.setNoteObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE))
					{
						cDetailObj.setAttributeObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTNOTATION))
					{
						cDetailObj.setNotationObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTANNOTATION))
					{
						cDetailObj.setAnnotationObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTOTHER))
					{
						cDetailObj.setOtherObject(results);
					}
					ConceptProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptEditValueFail());
				}
			};

			OwlStatus status = null;
			int actionId = 0 ;

			
			if(property.equals(ConceptProperty.CONCEPTNOTE)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditNoteEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditNoteEdit));
				Service.conceptService.editConceptNoteValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeEdit));
				Service.conceptService.editConceptAttributeValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeEdit));
				Service.conceptService.editConceptNotationValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeEdit));
				Service.conceptService.editConceptAnnotationValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTOTHER)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeEdit));
				Service.conceptService.editConceptOtherValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}

		}


	}
	public class DeleteValue extends FormDialogBox implements ClickHandler{
		String relURI = "";
		private NonFuncObject value;
		public DeleteValue(String relURI/*RelationshipObject rObj*/, NonFuncObject value){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.relURI = relURI;
			this.value = value;
			this.setText(constants.conceptDeleteValue());
			setWidth("400px");
			this.initLayout();

		}
		public void initLayout() {
			HTML message = new HTML(constants.conceptValueDeleteWarning());

			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);

			addWidget(table);
		};
		public void onSubmit() {
			sayLoading();
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>(){
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> results){
					if(property.equals(ConceptProperty.CONCEPTNOTE))
					{
						cDetailObj.setNoteObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE))
					{
						cDetailObj.setAttributeObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTNOTATION))
					{
						cDetailObj.setNotationObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTANNOTATION))
					{
						cDetailObj.setAnnotationObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTOTHER))
					{
						cDetailObj.setOtherObject(results);
					}
					ConceptProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptDeleteValueFail());
				}
			};

			OwlStatus status = null;
			int actionId = 0 ;

			if(property.equals(ConceptProperty.CONCEPTNOTE)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditNoteDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditNoteDelete));
				Service.conceptService.deleteConceptNoteValue(MainApp.userOntology,actionId, status, MainApp.userId, value, relURI, conceptObject, MainApp.isExplicit, callback);
			}else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeDelete));
				Service.conceptService.deleteConceptAttributeValue(MainApp.userOntology,actionId, status, MainApp.userId, value, relURI, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeDelete));
				Service.conceptService.deleteConceptNotationValue(MainApp.userOntology,actionId, status, MainApp.userId, value, relURI, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeDelete));
				Service.conceptService.deleteConceptAnnotationValue(MainApp.userOntology,actionId, status, MainApp.userId, value, relURI, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTOTHER)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeDelete));
				Service.conceptService.deleteConceptOtherValue(MainApp.userOntology,actionId, status, MainApp.userId, value, relURI, conceptObject, MainApp.isExplicit, callback);
			}
		}

	}

	public class AddValue extends FormDialogBox implements ClickHandler{
		private TextArea value;
		private ListBox language;
		private OlistBox relationship;
		private OlistBox values;
		private ListBox dataTypes;
		private ListBox box;
		
		private Image browse ;
		private String imgPath = "images/browseButton3-grey.gif";
		private TextBox destConcept;

		private DomainRangeObject drObj = new DomainRangeObject();
		private String relURI = "";
		private ArrayList<ClassObject> list = null;
		private String type = "";
		private ArrayList<String> datatype = null;
		private HorizontalPanel leftBottomWidget = new HorizontalPanel();
		private HorizontalPanel showAllPanel = new HorizontalPanel();
		private CheckBox chkBox = new CheckBox(constants.relShowAllDatatypeProperties());
		
		public AddValue(){
			super(constants.buttonCreate(), constants.buttonCancel());
			String label = "";
            if(property.equals(ConceptProperty.CONCEPTNOTE)){
                label = constants.conceptAddNotes();
            }else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
                label = constants.conceptAddAttributes();
                leftBottomWidget.add(addShowAllWidget());
        		setLeftBottomWidget(leftBottomWidget);
            }
            else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
                label = constants.conceptAddNotation();
            }
            else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
            	label = constants.conceptAddAnnotation();
            }
            else if(property.equals(ConceptProperty.CONCEPTOTHER)){
            	label = constants.conceptAddOther();
            }
			this.setText(label);
			setWidth("400px");
			this.initLayout();
		}
		
		public Widget addShowAllWidget()
		{
			chkBox.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					reloadAttributes();
				}
			});
			
			showAllPanel.add(chkBox);
			showAllPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			showAllPanel.setCellVerticalAlignment(chkBox, HasVerticalAlignment.ALIGN_MIDDLE);
			return showAllPanel;
		}
		
		public void reloadAttributes()
		{
			relationship.clear();
			relationship.addItem("--None--", "");
			if(chkBox.getValue())
			{
				AsyncCallback<RelationshipTreeObject> callback = new AsyncCallback<RelationshipTreeObject>(){
					public void onSuccess(RelationshipTreeObject results){
						HashMap<String,RelationshipObject> relationshipList = results.getRelationshipList();
						for(String uri : relationshipList.keySet()){
							relationship.addItem(relationshipList.get(uri).getName(), uri);
						}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptAttributeFail());
					}
				};
				Service.relationshipService.getDatatypePropertiesTree(MainApp.userOntology, callback);
			}
			else
			{
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
					public void onSuccess(HashMap<String, String> results){
						for(String uri : results.keySet()){
							relationship.addItem(results.get(uri), uri);
						}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptAttributeFail());
					}
				};
				Service.conceptService.getConceptAttributes(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
			}
		}

		public void initLayout() {
			value = new TextArea();
			value.setVisibleLines(3);
			value.setWidth("100%");

			language = new ListBox();
			language = Convert.makeListWithEmptyDefaultValueUserLanguages(MainApp.languageDict, MainApp.getUserLanguagePermissionList());
			language.setWidth("100%");

			values = new OlistBox();
			values.setWidth("100%");
			
			relationship = new OlistBox();//Convert.makeOListBoxWithValue(propList);
			relationship.setWidth("100%");

			if(property.equals(ConceptProperty.CONCEPTNOTE)){
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
					public void onSuccess(HashMap<String, String> results){
						relationship.addItem("--None--", "");
						for(String uri : results.keySet())
						{
							relationship.addItem(results.get(uri), uri);
						}
					}
					public void onFailure(Throwable caught){
						Window.alert(constants.conceptAttributeFail());
					}
				};
				Service.conceptService.getConceptNotes(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE)){
				reloadAttributes();
			}
			else if(property.equals(ConceptProperty.CONCEPTNOTATION)){
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
					public void onSuccess(HashMap<String, String> results){
						 relationship.addItem("--None--", "");
						for(String uri : results.keySet()){
							relationship.addItem(results.get(uri), uri);
						}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptAttributeFail());
					}
				};
				Service.conceptService.getConceptNotation(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTANNOTATION)){
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
					public void onSuccess(HashMap<String, String> results){
						relationship.addItem("--None--", "");
						for(String uri : results.keySet()){
							relationship.addItem(results.get(uri), uri);
						}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptAttributeFail());
					}
				};
				Service.conceptService.getConceptAnnotation(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTOTHER)){
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
					public void onSuccess(HashMap<String, String> results){
						relationship.addItem("--None--", "");
						for(String uri : results.keySet()){
							relationship.addItem(results.get(uri), uri);
						}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptAttributeFail());
					}
				};
				Service.conceptService.getConceptOther(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
			}
			

			final FlexTable table = new FlexTable();
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			table.setWidget(0, 1, relationship);

			relationship.addChangeHandler(new ChangeHandler()
			{
				public void onChange(ChangeEvent event) {
					while(table.getRowCount()>1)
					{
						table.removeRow(table.getRowCount()-1);
					}
					datatype = null;
					type = "";
					list = null;

					relURI = relationship.getValue(relationship.getSelectedIndex());
					if(relURI!=null && !relURI.equals(""))
					{
						AsyncCallback<DomainRangeObject> callback = new AsyncCallback<DomainRangeObject>(){
							public void onSuccess(DomainRangeObject results){
								if(results!=null)
								{
									drObj = results;
									type = drObj.getRangeType();
									datatype = new ArrayList<String>();
									list = new ArrayList<ClassObject>();
									
									for(ClassObject clsObj : drObj.getRange())
									{
										if(type.equals(DomainRangeObject.typedLiteral))
										{
											datatype.add(clsObj.getUri());
										}
										else if(type.equals(DomainRangeObject.resource))
										{
											datatype.add(clsObj.getUri());
										}
										else 
										{
											list.add(clsObj);
										}
									}
								}

								//check value or values
								if(list!=null && list.size()>0)
								{
									values = Convert.makeOListBoxWithClassObjectValue(list);
									table.setWidget(1, 0, new HTML(constants.conceptValue()));
									table.setWidget(1, 1, values);
								}
								else if(type.equals(DomainRangeObject.resource))
								{
									table.setWidget(1, 0, new HTML(constants.conceptResource()));
									table.setWidget(1, 1, getConceptBrowseButton());
									dataTypes = Convert.makeListBoxSingleValueWithValueEmptyDefaultValue(datatype);
									table.setWidget(2, 0, new HTML(constants.termType()));
									table.setWidget(2, 1, dataTypes);
								}
								else
								{
									table.setWidget(1, 0, new HTML(constants.conceptValue()));
									table.setWidget(1, 1, value);
									
									if(type.equals(DomainRangeObject.typedLiteral))
									{
										dataTypes = Convert.makeListBoxSingleValueWithValueEmptyDefaultValue(datatype);
										table.setWidget(2, 0, new HTML(constants.termType()));
										table.setWidget(2, 1, dataTypes);
									}
									else if(type.equals(DomainRangeObject.literal) || type.equals(DomainRangeObject.undetermined))
									{
										box = new ListBox();
										box.setSize("100%", "100%");
										box.addItem("Select", "");
										box.addItem(DomainRangeObject.plainliteral);
										box.addItem(DomainRangeObject.typedLiteral);
										if(type.equals(DomainRangeObject.undetermined))
											box.addItem(DomainRangeObject.resource);

										table.setWidget(2, 0, new HTML(constants.termDatatype()));
										table.setWidget(2, 1, box);
										
										box.addChangeHandler(new ChangeHandler() {
											
											public void onChange(ChangeEvent event) {
												String value = box.getValue(box.getSelectedIndex());
												if(value.equals(DomainRangeObject.plainliteral))
												{
													table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
													table.setWidget(2, 1, language);
													type = DomainRangeObject.plainliteral;
												}
												else if(value.equals(DomainRangeObject.typedLiteral))
												{
													HashMap<String, String> hMap = initData.getDataTypes();
													hMap.putAll(MainApp.customDatatype);
													dataTypes = Convert.makeListBoxWithValueEmptyDefaultValue(hMap);
													dataTypes.setSize("100%", "100%");
													table.setWidget(2, 0, new HTML(constants.termType()));
													table.setWidget(2, 1, dataTypes);
													type = DomainRangeObject.typedLiteral;
												}
												else if(value.equals(DomainRangeObject.resource))
												{
													table.setWidget(1, 0, new HTML(constants.conceptResource()));
													table.setWidget(1, 1, getConceptBrowseButton());
													table.removeRow(2);
													type = DomainRangeObject.resource;
												}
											}
										});
									}
									else
									{
										table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
										table.setWidget(2, 1, language);
									}
								}
								GridStyle.updateTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1");
								
								
							}
							public void onFailure(Throwable caught){
								ExceptionManager.showException(caught, constants.conceptAddValueFail());
							}
						};
						Service.conceptService.getPropertyRange(relURI, MainApp.userOntology, callback);
						
						
					}
				}

			});
			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		}
		
		private HorizontalPanel getConceptBrowseButton()
		{
			destConcept = new TextBox();
			destConcept.setWidth("100%");
			browse = new Image(imgPath);
			browse.setStyleName(Style.Link);
			browse.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					final ConceptBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptBrowser; 
					cb.showBrowser();
					cb.addSubmitClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event) {
							if(cb.getSelectedItem()!=null)
								destConcept.setValue(cb.getTreeObject().getUri());
						}					
					});						
				}
			});

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(destConcept);
			hp.add(browse);
			hp.setWidth("100%");
			hp.setCellWidth(destConcept, "100%");
			hp.setCellHorizontalAlignment(destConcept, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
			
			return hp;
		}
		public boolean passCheckInput() {
			if(relationship.getValue(relationship.getSelectedIndex()).equals("--None--")	|| relationship.getValue(relationship.getSelectedIndex()).equals(""))
			{
				return false;
			}
			else
			{
				//check value or values
				if(list!=null && list.size()>0)
				{
					//if(values.getValue(values.getSelectedIndex()).equals("--None--") || values.getValue(values.getSelectedIndex()).equals(""))
					if(values.getObject(values.getSelectedIndex())==null || ((ClassObject)values.getObject(values.getSelectedIndex())).getUri().equals(""))
					{
						return false;
					}
				}
				else if(type.equals(DomainRangeObject.resource))
				{
					if(destConcept.getValue().length()==0)
					{
						return false;
					}
				}
				else if(type.equals(DomainRangeObject.typedLiteral))
				{
					if(dataTypes!=null && (dataTypes.getValue(dataTypes.getSelectedIndex()).equals("--None--") || dataTypes.getValue(dataTypes.getSelectedIndex()).equals("")))
					{
						return false;
					}
					// Add more checks
				}
				else
				{
					if(value.getText().length()==0)
					{
						return false;
					}
					if(box!=null && (box.getValue(box.getSelectedIndex()).equals("--None--") || box.getValue(box.getSelectedIndex()).equals("")))
					{
						return false;
					}
					if(dataTypes!=null && (dataTypes.getValue(dataTypes.getSelectedIndex()).equals("--None--") || dataTypes.getValue(dataTypes.getSelectedIndex()).equals("")))
					{
						return false;
					}
					// check language
					/*if(!type.equals(DomainRangeObject.typedLiteral))
					{
						if(language.getValue(language.getSelectedIndex()).equals("--None--") || language.getValue(language.getSelectedIndex()).equals(""))
						{
							return false;
						}
					}*/
				}
			}
			return true;
		}
		public void onSubmit() {
			sayLoading();

			NonFuncObject nonFuncObj = new NonFuncObject();
			//check value or values
			//nonFuncObj.setType(dataTypes.getValue(dataTypes.getSelectedIndex()));
			if(list!=null && list.size()>0)
			{
				ClassObject clsObj = (ClassObject) values.getObject(values.getSelectedIndex());
				nonFuncObj.setValue(clsObj.getLabel());
				nonFuncObj.setType(clsObj.getUri());
			}
			else if(type.equals(DomainRangeObject.resource))
			{
				nonFuncObj.setValue(destConcept.getValue());
			}
			else
			{
				nonFuncObj.setValue(value.getText());
				// check language
				if(!type.equals(DomainRangeObject.typedLiteral))
				{
					nonFuncObj.setLanguage(language.getValue(language.getSelectedIndex()));
				}
				if(dataTypes!=null)
					nonFuncObj.setType(dataTypes.getValue(dataTypes.getSelectedIndex()));
			}


			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>(){
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> results){
					if(property.equals(ConceptProperty.CONCEPTNOTE))
					{
						cDetailObj.setNoteObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE))
					{
						cDetailObj.setAttributeObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTNOTATION))
					{
						cDetailObj.setNotationObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTANNOTATION))
					{
						cDetailObj.setAnnotationObject(results);
					}
					else if(property.equals(ConceptProperty.CONCEPTOTHER))
					{
						cDetailObj.setOtherObject(results);
					}

					ConceptProperty.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptAddValueFail());
				}
			};

			OwlStatus status = null;
			int actionId = 0 ;

			if(property.equals(ConceptProperty.CONCEPTNOTE))
			{
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditNoteCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditNoteCreate));
				Service.conceptService.addConceptNoteValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTATTRIBUTE))
			{
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeCreate));
				Service.conceptService.addConceptAttributeValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTNOTATION))
			{
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeCreate));
				Service.conceptService.addConceptNotationValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTANNOTATION))
			{
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeCreate));
				Service.conceptService.addConceptAnnotationValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
			else if(property.equals(ConceptProperty.CONCEPTOTHER))
			{
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditAttributeCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditAttributeCreate));
				Service.conceptService.addConceptOtherValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, relURI, drObj, conceptObject, MainApp.isExplicit, callback);
			}
		}
	}
}
