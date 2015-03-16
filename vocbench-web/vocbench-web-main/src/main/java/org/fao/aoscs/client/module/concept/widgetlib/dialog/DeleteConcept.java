package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTab;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.tree.ConceptCellTreeAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

public class DeleteConcept extends FormDialogBox{
	private ConceptObject cObj;
	private HTML msg = new HTML();
	private InitializeConceptData initData;
	private ConceptCellTreeAOS treePanel;
	private ConceptObject selectedConceptObject;
	private boolean showAlsoNonpreferredTerms;
	//public String rootConceptURI = null;
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	
	public DeleteConcept(ConceptCellTreeAOS treePanel, InitializeConceptData initData, ConceptObject selectedConceptObject, boolean showAlsoNonpreferredTerms){
		super(constants.buttonDelete(), constants.buttonCancel());
		setWidth("400px");
		this.treePanel = treePanel;
		this.initData = initData;
		this.selectedConceptObject = selectedConceptObject;
		this.showAlsoNonpreferredTerms = showAlsoNonpreferredTerms;
		this.setText(constants.conceptDelete());
		this.initLayout();
	}
	
	public void setConcept(ConceptObject cObj)
	{
		this.cObj = cObj;
		msg.setHTML(messages.conceptDeleteWarning(getConceptText(cObj)));
	}
	 
	public String getConceptText(ConceptObject cObj){
		String text = "";
		HashMap<String, TermObject> map = cObj.getTerm();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String termIns = (String) it.next();
			TermObject tObj = (TermObject) map.get(termIns);
			if(text.equals(""))
				text +=  tObj.getLabel()+" ("+tObj.getLang()+")";	
			else
				text += ", "+tObj.getLabel()+" ("+tObj.getLang()+")";
		}
		return text;
	}
	
	public void initLayout() {
		Grid table = new Grid(1,2);
		table.setWidget(0, 0, getWarningImage());
		table.setWidget(0, 1, msg);
		table.setWidth("100%");
		
		addWidget(table);
	}
	
	public void onSubmit() {
		treePanel.showLoading(true);
		
		OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptDelete);
		int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptDelete));

		AsyncCallback<Void> callback = new AsyncCallback<Void>(){
			public void onSuccess(Void result){
				treePanel.reloadItem(selectedConceptObject.getUri(), ConceptTab.HISTORY.getTabIndex(), showAlsoNonpreferredTerms, MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, MainApp.schemeUri, MainApp.userOntology);
				ModuleManager.resetValidation();
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptDeleteFail());
			}
		};
		Service.conceptService.deleteConcept(MainApp.userOntology,actionId, MainApp.userId,status, cObj, callback);
	}
}