package org.fao.aoscs.client.module.term.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.term.TermDetailTabPanel;
import org.fao.aoscs.client.module.term.TermTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TermProperty extends TermTemplate{

	public static String TERMATTRIBUTES = "0";
	public static String TERMNOTATION = "1";
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private String property ;
	private int cnt = 0;
	private AddValue addValue ;
	private EditValue editValue ;
	private DeleteValue deleteValue ;
	private HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> propValueList = new HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>();

	public TermProperty(String property, PermissionObject permisstionTable, InitializeConceptData initData, TermDetailTabPanel termDetailPanel){
		super(permisstionTable, initData, termDetailPanel);
		this.property = property ;
	}

	private void attachNewImgButton(){

		functionPanel.clear();
		String label = "";
		boolean permission = false;
		if(property.equals(TermProperty.TERMNOTATION)){
			label = constants.conceptAddNotation();
			permission = permissionTable.contains(OWLActionConstants.TERMNOTECREATE, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang()));
		}else if(property.equals(TermProperty.TERMATTRIBUTES)){
			label = constants.conceptAddAttributes();
			permission = permissionTable.contains(OWLActionConstants.TERMATTRIBUTECREATE, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang()));
		}
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
		for(NonFuncObject value:values.keySet())
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
		    if(property.equals(TermProperty.TERMNOTATION)){
		    	labelEdit = constants.conceptEditNotation();
                labelDelete = constants.conceptDeleteNotation();
            	permissionEdit = permissionTable.contains(OWLActionConstants.TERMNOTEEDIT, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang())) && isExplicit;
            	permissionDelete = permissionTable.contains(OWLActionConstants.TERMNOTEDELETE, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang())) && isExplicit;
		    }
		    else if(property.equals(TermProperty.TERMATTRIBUTES)){
		    	labelEdit = constants.conceptEditAttributes();
		        labelDelete = constants.conceptDeleteAttributes();
		        permissionEdit = permissionTable.contains(OWLActionConstants.TERMATTRIBUTEEDIT, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang())) && isExplicit;
            	permissionDelete = permissionTable.contains(OWLActionConstants.TERMATTRIBUTEDELETE, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang())) && isExplicit;
		    }

			hp.setSpacing(3);

			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", labelEdit, permissionEdit,  new ClickHandler() {
				public void onClick(ClickEvent event) {
					/*boolean chk = true;
					if(rObj.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEAGROVOC) && termObject.isMainLabel())
					{
						chk = Window.confirm(constants.termChangeCodeAGROVOC());
					}
					if(chk)*/
					{
						if(editValue == null || !editValue.isLoaded)
							editValue = new EditValue(rObj, value);
						editValue.show();
					}
				}
			});

			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", labelDelete, permissionDelete, new ClickHandler() {
				public void onClick(ClickEvent event) {
					/*boolean chk = true;
					if(rObj.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEAGROVOC) && termObject.isMainLabel())
					{
						chk = Window.confirm(constants.termChangeCodeAGROVOC());
					}
					if(chk)*/
					{
						if(deleteValue == null || !deleteValue.isLoaded)
							deleteValue = new DeleteValue(rObj, value);
						deleteValue.show();
					}
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
		{
				html.setStyleName("link-label-aos-explicit");
		}
		hp.add(html);
		
		
		return hp;
	}
	public void initLayout(){
		this.sayLoading();
		
		if(property.equals(TermProperty.TERMNOTATION))
		{
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
			{
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> list) {
					propValueList = list;
					initData(propValueList);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.termAttributeFail());
				}
			};
			Service.termService.getTermNotationValue(conceptObject.getUri(), termObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
		}
		else if(property.equals(TermProperty.TERMATTRIBUTES))
		{
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
			{
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> list) {
					propValueList = list;
					initData(propValueList);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.termAttributeFail());
				}
			};
			Service.termService.getTermAttributeValue(conceptObject.getUri(), termObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
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
		if(property.equals(TermProperty.TERMNOTATION)){
			table.setWidget(0, 0, new HTML(constants.conceptNotations()));
		}else if(property.equals(TermProperty.TERMATTRIBUTES)){
			table.setWidget(0, 0, new HTML(constants.conceptAttributes()));
		}
		table.setWidget(0, 1, new HTML(constants.termValue()));

		int i=0;
		if(!list.isEmpty()){
			for(ClassObject clsObj: getSortedList(list)){
				HashMap<NonFuncObject, Boolean> values = (HashMap<NonFuncObject, Boolean>) list.get(clsObj);
				table.setWidget(i+1, 0, new HTML(clsObj.getLabel()));
				table.setWidget(i+1, 1, getFuncButtons(clsObj.getUri(), values));
				i++;
			}
			if(property.equals(TermProperty.TERMNOTATION)){
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.notation, Convert.replaceSpace(cnt>1?constants.conceptNotations():constants.conceptNotation())+"&nbsp;("+(cnt)+")");
			}else if(property.equals(TermProperty.TERMATTRIBUTES)){
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.attribute, Convert.replaceSpace(cnt>1?constants.conceptAttributes():constants.conceptAttribute())+"&nbsp;("+(cnt)+")");
			}
			termRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			String label = "No value";
			if(property.equals(TermProperty.TERMNOTATION)){
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.notation, Convert.replaceSpace(constants.conceptNotation())+"&nbsp;(0)");
				label = constants.conceptNoNotation();
			}else if(property.equals(TermProperty.TERMATTRIBUTES)){
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.attribute, Convert.replaceSpace(constants.conceptAttribute())+"&nbsp;(0)");
				label = constants.conceptNoAttributes();
			}
			Label sayNo = new Label(label);
			termRootPanel.add(sayNo);
			termRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
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

		public EditValue(String relURI, NonFuncObject oldValue){
			super();
			this.relURI = relURI;
			this.oldValue = oldValue;
			this.setText(constants.termEditValue());
			this.initLayout();
			setWidth("400px");

		}
		public void initLayout() {

			relationship = new LabelAOS();
			relationship.setText(relURI);

			final FlexTable table = new FlexTable();
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidget(0, 0, new HTML(constants.conceptRelationship()));
			table.setWidget(0, 1, relationship);

			
			if(relURI!=null)
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


			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onSuccess(Void results){
					TermProperty.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptEditValueFail());
				}
			};


			OwlStatus status = null;
			int actionId = 0 ;

			/*if(property.equals(TermProperty.TERMNOTATION)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termNoteEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termNoteEdit));
			}else if(property.equals(TermProperty.TERMATTRIBUTES)){*/
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termAttributeEdit);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termAttributeEdit));
			//}

			Service.termService.editPropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, relURI, drObj, termObject, conceptObject, callback);
		}


	}
	public class DeleteValue extends FormDialogBox implements ClickHandler{
		//private RelationshipObject rObj;
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
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onSuccess(Void results){
					TermProperty.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptDeleteValueFail());
				}
			};

			OwlStatus status = null;
			int actionId = 0 ;

			/*if(property.equals(TermProperty.TERMNOTATION)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termNoteDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termNoteDelete));
			}else if(property.equals(TermProperty.TERMATTRIBUTES)){*/
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termAttributeDelete);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termAttributeDelete));
			//}


			Service.termService.deletePropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, value, relURI, termObject, conceptObject, callback);
		}

	}
	public class AddValue extends FormDialogBox implements ClickHandler{
		private TextArea value;
		private ListBox language;
		private OlistBox relationship;
		private OlistBox values;
		private ListBox dataTypes;
		private ListBox box;

		private String relURI = "";
		private ArrayList<ClassObject> list = null;
		private ArrayList<String> datatype = null;
		private String type = "";
		private DomainRangeObject drObj = new DomainRangeObject();
		
		public AddValue(){
			super(constants.buttonCreate(), constants.buttonCancel());
			String label = "";
			if(property.equals(TermProperty.TERMNOTATION)){
				label = constants.conceptAddNotation();
			}else if(property.equals(TermProperty.TERMATTRIBUTES)){
				label = constants.conceptAddAttributes();
			}
			this.setText(label);
			setWidth("400px");
			initLayout();
		}

		public void initLayout() {
			value = new TextArea();
			value.setVisibleLines(3);
			value.setWidth("100%");

			language = new ListBox();
			//language = Convert.makeListBoxWithValue((ArrayList<String[]>)MainApp.getLanguage());
			language = Convert.makeListWithEmptyDefaultValueUserLanguages(MainApp.languageDict, MainApp.getUserLanguagePermissionList());
			language.setWidth("100%");

			values = new OlistBox();
			values.setWidth("100%");
			
			/*
			ArrayList<RelationshipObject> propList = new ArrayList<RelationshipObject>();
			ArrayList<RelationshipObject> currList = new ArrayList<RelationshipObject>(propValueList.keySet());
			//if(property.equals(ModelConstants.RTERMEDITORIALDATATYPEPROPERTY)){
				//propList = Convert.filterOutAddedFunctionalProperty(currList, initData.getTermEditorialAttributes());
			//}
			//else if(property.equals(ModelConstants.RTERMDOMAINDATATYPEPROPERTY)){
				propList.addAll(Convert.filterOutAddedFunctionalProperty(currList, initData.getTermEditorialAttributes()));
				propList.addAll(Convert.filterOutAddedFunctionalProperty(currList, initData.getTermDomainAttributes()));
			//}
			*/
			relationship = new OlistBox();//Convert.makeOListBoxWithValue(propList);
			relationship.setWidth("100%");
			
			
			if(property.equals(TermProperty.TERMNOTATION)){
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
					public void onSuccess(HashMap<String, String> results){
						 relationship.addItem("--None--", "");
						for(String uri : results.keySet()){
							relationship.addItem(results.get(uri), uri);
						}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.termAttributeFail());
					}
				};
				Service.termService.getTermNotation(termObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);	
			}
			else if(property.equals(TermProperty.TERMATTRIBUTES)){
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
					public void onSuccess(HashMap<String, String> results){
						 relationship.addItem("--None--", "");
						for(String uri : results.keySet()){
							relationship.addItem(results.get(uri), uri);
						}
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.termAttributeFail());
					}
				};
				Service.termService.getTermAttributes(termObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);	
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
									table.setWidget(1, 0, new HTML(constants.termValue()));
									table.setWidget(1, 1, values);
								}
								else
								{
									table.setWidget(1, 0, new HTML(constants.termValue()));
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

										table.setWidget(2, 0, new HTML(constants.termDatatype()));
										table.setWidget(2, 1, box);
										
										box.addChangeHandler(new ChangeHandler() {
											
											public void onChange(ChangeEvent event) {
												String value = box.getValue(box.getSelectedIndex());
												if(value.equals(DomainRangeObject.plainliteral))
												{
													table.setWidget(2, 0, new HTML(constants.conceptLanguage()));
													table.setWidget(2, 1, language);
												}
												else if(value.equals(DomainRangeObject.typedLiteral))
												{
													HashMap<String, String> hMap = initData.getDataTypes();
													hMap.putAll(MainApp.customDatatype);
													dataTypes = Convert.makeListBoxWithValueEmptyDefaultValue(hMap);
													dataTypes.setSize("100%", "100%");
													table.setWidget(2, 0, new HTML(constants.termType()));
													table.setWidget(2, 1, dataTypes);
												}
											}
										});
									}
									else 
									{
										table.setWidget(2, 0, new HTML(constants.termLanguage()));
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
			if(list!=null && list.size()>0)
			{
				ClassObject clsObj = (ClassObject) values.getObject(values.getSelectedIndex());
				nonFuncObj.setValue(clsObj.getLabel());
				nonFuncObj.setType(clsObj.getUri());
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
			

			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onSuccess(Void results){
					TermProperty.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.termAddValueFail());
				}
			};

			OwlStatus status = null;
			int actionId = 0 ;


			/*if(property.equals(TermProperty.TERMNOTATION)){
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termNoteCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termNoteCreate));
				}else if(property.equals(TermProperty.TERMATTRIBUTES)){*/
				status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termAttributeCreate);
				actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termAttributeCreate));
			//}


			Service.termService.addPropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, nonFuncObj, relURI, drObj, termObject, conceptObject, callback);

		}


	}
}
