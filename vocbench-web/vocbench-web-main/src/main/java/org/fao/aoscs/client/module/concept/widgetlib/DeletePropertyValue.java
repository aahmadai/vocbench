package org.fao.aoscs.client.module.concept.widgetlib;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

public class DeletePropertyValue extends FormDialogBox implements ClickHandler{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public DeletePropertyValue(){
		super(constants.buttonDelete(), constants.buttonCancel());
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
	
	
	/*public void onSubmit() {
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
	}*/

}