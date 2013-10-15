package org.fao.aoscs.client.module.term.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.term.TermDetailTabPanel;
import org.fao.aoscs.client.module.term.TermTemplate;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.RelationshipBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.TermBrowser;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TermRelationshipObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class TermRelationship extends TermTemplate {
	private LocaleConstants constants = (LocaleConstants) GWT
			.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT
			.create(LocaleMessages.class);
	private AddNewTermRelationship addNewTermRelationship;
	private EditTermRelationship editTermRelationship;
	private DeleteTermRelationship deleteTermRelationship;
	//private TermCodeBrowser tcBrowser;

	public TermRelationship(PermissionObject permissionTable,
			InitializeConceptData initData, TermDetailTabPanel termDetailPanel) {
		super(permissionTable, initData, termDetailPanel);
	}

	private void attachNewImgButton() {

		functionPanel.clear();
		boolean permission = permissionTable.contains(OWLActionConstants.TERMRELATIONSHIPADD, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang()));
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.termAddRelationship(),constants.termAddRelationship(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (addNewTermRelationship == null || !addNewTermRelationship.isLoaded)
					addNewTermRelationship = new AddNewTermRelationship();
				addNewTermRelationship.show();
			}
		});
		this.functionPanel.add(add);
	}

	private HorizontalPanel getFunctionButton(final ClassObject rObj, final TermObject tObj, boolean isExplicit) {
		HorizontalPanel hp = new HorizontalPanel();

		hp.setSpacing(3);
		boolean permission = permissionTable.contains(OWLActionConstants.TERMRELATIONSHIPEDIT, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang())) && isExplicit;
		LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.termEditRelationship(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (editTermRelationship == null || !editTermRelationship.isLoaded)
					editTermRelationship = new EditTermRelationship(rObj,tObj);
				editTermRelationship.show();
			}
		});

		permission = permissionTable.contains(OWLActionConstants.TERMRELATIONSHIPDELETE, OWLStatusConstants.getOWLStatusID(termObject.getStatus()), termObject.getLang(), MainApp.getPermissionCheck(termObject.getLang())) && isExplicit;
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.termDeleteRelationship(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (deleteTermRelationship == null || !deleteTermRelationship.isLoaded)
					deleteTermRelationship = new DeleteTermRelationship(rObj, tObj);
				deleteTermRelationship.show();
			}
		});
		
		HTML html = new HTML(tObj.getLabel() + " (" + tObj.getLang() + ")");
		if(!isExplicit)
		{
			html.setStyleName("link-label-aos-explicit");
		}
		hp.add(edit);
		hp.add(delete);
		hp.add(html);
		return hp;
	}

	private Grid getDestinationTerm(ClassObject rObj, HashMap<TermObject, Boolean> result) {
		Grid table = new Grid(result.size(), 1);
		int i=0;
		for(TermObject tObj : result.keySet())
		{
			table.setWidget(i, 0, getFunctionButton(rObj, tObj, result.get(tObj)));
			i++;
		}
		table.setWidth("100%");
		return table;
	}
	
	private ArrayList<ClassObject> getSortedList(HashMap<ClassObject, HashMap<TermObject, Boolean>> list)
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

	public void initLayout() {
		this.sayLoading();
		AsyncCallback<TermRelationshipObject> callback = new AsyncCallback<TermRelationshipObject>() {
			public void onSuccess(TermRelationshipObject trObj) {
				clearPanel();
				attachNewImgButton();
				if (!trObj.isEmpty())
				{
					HashMap<ClassObject, HashMap<TermObject, Boolean>> resultList = trObj.getResult();
					Iterator<ClassObject> it = getSortedList(resultList).iterator();
					Grid table = new Grid(resultList.size() + 1, 2);
					table.setWidget(0, 0, new HTML(constants.termRelationship()));
					table.setWidget(0, 1, new HTML(constants.termTerm()));
					int i = 1;
					int count = 0;
					while (it.hasNext()) {
						ClassObject rObj = (ClassObject) it.next();
						HashMap<TermObject, Boolean> termList = (HashMap<TermObject, Boolean>) resultList.get(rObj);
						//table.setWidget(i, 0, new HTML(Convert.getRelationshipLabel(rObj)));
						//table.setWidget(i, 1, getDestinationTerm(rObj, termList));
						table.setWidget(i, 0, new HTML(rObj.getLabel()));
						table.setWidget(i, 1, getDestinationTerm(rObj, termList));
						i++;
						count += termList.size();
					}
					termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.relationship, (count>1? constants.termRelationships() : constants.termRelationship()) + "&nbsp;(" + count+ ")");
					termRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
				}
				else
				{
					termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.relationship, constants.termRelationship() + "&nbsp;(0)");
					Label sayNo = new Label(constants.termNoRelationship());
					termRootPanel.add(sayNo);
					termRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
				}
			}

			public void onFailure(Throwable caught) {
				termDetailPanel.tabPanel.getTabBar().setTabHTML(TermDetailTabPanel.relationship, constants.termRelationship() + "&nbsp;(0)");
				Label sayNo = new Label(constants.termNoRelationship());
				termRootPanel.add(sayNo);
				termRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
				ExceptionManager.showException(caught, constants.termGetRelationshipFail());
			}
		};
		
		Service.termService.getTermRelationship(conceptObject.getUri(), termObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
	}

	private class EditTermRelationship extends FormDialogBox implements ClickHandler {
		private Image browseRel;
		private Image browseTerm;

		private LabelAOS selectedRel;
		private LabelAOS selectedTerm;

		private ClassObject relClsObj;
		private TermObject destObj;

		private TermBrowser termBrowser;

		public EditTermRelationship(ClassObject relClsObj, TermObject destObj) {
			super();
			this.relClsObj = relClsObj;
			this.destObj = destObj;
			this.setText(constants.termEditRelationship());
			this.initLayout();
			setWidth("400px");

		}

		public void onButtonClicked(Widget sender) {
			if (sender.equals(browseRel))
			{
				final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser;
				rb.showBrowser(RelationshipBrowser.REL_TERM);
				rb.addSubmitClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						selectedRel.setText(rb.getSelectedItem(),rb.getRelationshipObject().getUri());
					}
				});
			}

			if (sender.equals(browseTerm)) {
				if (termBrowser == null || termBrowser.isLoaded)
					termBrowser = new TermBrowser(termObject, selectedTerm);
				termBrowser.show();
			}
		}

		public void initLayout() {

			selectedRel = new LabelAOS("--None--", null);
			selectedTerm = new LabelAOS("--None--", new TermObject());

			browseRel = new Image("images/browseButton3-grey.gif");
			browseRel.setStyleName(Style.Link);
			browseRel.addClickHandler(this);

			browseTerm = new Image("images/browseButton3-grey.gif");
			browseTerm.setStyleName(Style.Link);
			browseTerm.addClickHandler(this);

			selectedRel = new LabelAOS("--None--", new RelationshipObject());
			//selectedRel.setText(Convert.getRelationshipLabel(rObj), rObj);
			selectedRel.setText(relClsObj.getLabel(), relClsObj.getUri());

			selectedTerm = new LabelAOS("--None--", new TermObject());
			selectedTerm.setText(destObj.getLabel(), destObj);

			HorizontalPanel hpRel = new HorizontalPanel();
			hpRel.add(selectedRel);
			hpRel.add(browseRel);
			hpRel.setWidth("100%");
			hpRel.setCellHorizontalAlignment(browseRel, HasHorizontalAlignment.ALIGN_RIGHT);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(selectedTerm);
			hp.add(browseTerm);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(browseTerm, HasHorizontalAlignment.ALIGN_RIGHT);

			Grid table = new Grid(2, 2);
			table.setWidget(0, 0, new HTML(constants.termRelationship()));
			table.setWidget(1, 0, new HTML(constants.termTerm()));
			table.setWidget(0, 1, hpRel);
			table.setWidget(1, 1, hp);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");

			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}

		public boolean passCheckInput() {
			boolean pass = false;
			if (selectedRel == null || selectedTerm == null) {
				pass = false;
			} else {
				String relURI = (String) selectedRel.getValue();
				TermObject tObj = (TermObject) selectedTerm.getValue();
				if (tObj == null || relURI == null) {
					pass = false;
				} else {
					if (relURI == null || tObj.getUri() == null) {
						pass = false;
					} else {
						if ((((String) relURI).length() == 0)
								|| (((String) tObj.getUri()).length() == 0)) {
							pass = false;
						} else {
							pass = true;
						}
					}
				}
			}
			return pass;
		}

		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(submit)){
				if(passCheckInput()){
					/*if(((RelationshipObject)(selectedRel.getValue())).getUri().equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASTRANSLATION) && termObject.getLang().equals(((TermObject) selectedTerm.getValue()).getLang()))
					{
						Window.alert(constants.termNoTranslationRel());
					}
					else*/
					{
						this.hide();
						submit.setEnabled(false);
						onSubmit();
					}
				}else{
					Window.alert(constants.conceptCompleteInfo());
				}
			}else if(sender.equals(cancel)){
				this.hide();			
			}
			onButtonClicked(sender);
		}

		public void onSubmit() {
			sayLoading();

			String newRObjURI = (String) selectedRel.getValue();// new RelationshipObject();

			TermObject newTObj = (TermObject) selectedTerm.getValue();

			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				public void onSuccess(Void results) {
					TermRelationship.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}

				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.termEditRelationshipFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(
					ConceptActionKey.termRelationshipEdit);
			int actionId = Integer.parseInt((String) initData.getActionMap()
					.get(ConceptActionKey.termRelationshipEdit));

			Service.termService.editTermRelationship(MainApp.userOntology,
					actionId, status, MainApp.userId, relClsObj.getUri(), newRObjURI,
					termObject, destObj, newTObj, conceptObject, callback);
		}

	}

	private class DeleteTermRelationship extends FormDialogBox implements ClickHandler {

		private TermObject destObj;
		private ClassObject relClsObj;

		public DeleteTermRelationship(ClassObject relClsObj, TermObject destObj) {
			super(constants.buttonDelete(), constants.buttonCancel());
			this.relClsObj = relClsObj;
			this.destObj = destObj;
			this.setText(constants.termRelationshipDeletion());
			this.initLayout();
			setWidth("400px");
		}

		public void initLayout() {
			HTML message = new HTML(messages.termDeleteRelationshipWarning(relClsObj.getLabel(), termObject.getLabel() , termObject.getLang() , destObj.getLabel(), destObj.getLang()));
			Grid table = new Grid(1, 2);
			table.setWidget(0, 0, getWarningImage());
			table.setWidget(0, 1, message);

			addWidget(table);
		}

		public void onSubmit() {
			sayLoading();
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				public void onSuccess(Void results) {
					TermRelationship.this.setURI(termObject, conceptObject);
					ModuleManager.resetValidation();
				}

				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.termDeleteRelationshipFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(
					ConceptActionKey.termRelationshipDelete);
			int actionId = Integer.parseInt((String) initData.getActionMap()
					.get(ConceptActionKey.termRelationshipDelete));

			Service.termService.deleteTermRelationship(MainApp.userOntology,
					actionId, status, MainApp.userId, relClsObj.getUri(), termObject,
					destObj, conceptObject, callback);
		}

	}

	private class AddNewTermRelationship extends FormDialogBox implements ClickHandler {
		private Image browseRel;
		private Image browseTerm;

		private LabelAOS selectedRel;
		private LabelAOS selectedTerm;

		private TermBrowser termBrowser;

		public AddNewTermRelationship() {
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.termCreateRelationshipToAnotherTerm());
			this.initLayout();

		}

		public void initLayout() {

			browseRel = new Image("images/browseButton3-grey.gif");
			browseRel.setStyleName(Style.Link);
			browseRel.addClickHandler(this);

			browseTerm = new Image("images/browseButton3-grey.gif");
			browseTerm.setStyleName(Style.Link);
			browseTerm.addClickHandler(this);

			selectedRel = new LabelAOS("--None--", null);
			selectedTerm = new LabelAOS("--None--", new TermObject());

			HorizontalPanel hpRel = new HorizontalPanel();
			hpRel.add(selectedRel);
			hpRel.add(browseRel);
			hpRel.setWidth("100%");
			hpRel.setCellHorizontalAlignment(browseRel,
					HasHorizontalAlignment.ALIGN_RIGHT);

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(selectedTerm);
			hp.add(browseTerm);
			hp.setWidth("100%");
			hp.setCellHorizontalAlignment(browseTerm,
					HasHorizontalAlignment.ALIGN_RIGHT);

			Grid table = new Grid(2, 2);
			table.setWidget(0, 0, new HTML(constants.termRelationship()));
			table.setWidget(1, 0, new HTML(constants.termDestination()));
			table.setWidget(0, 1, hpRel);
			table.setWidget(1, 1, hp);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");

			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}

		public void onButtonClicked(Widget sender) {
			if (sender.equals(browseRel)) {
				final RelationshipBrowser rb =((MainApp) RootPanel.get().getWidget(0)).relationshipBrowser;
				rb.showBrowser(RelationshipBrowser.REL_TERM);
				rb.addSubmitClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						selectedRel.setText(rb.getSelectedItem(),rb.getRelationshipObject());
					}
				});
			}
			if (sender.equals(browseTerm)) {
				if (termBrowser == null || termBrowser.isLoaded)
					termBrowser = new TermBrowser(termObject, selectedTerm);
				termBrowser.show();
			}
		}

		public boolean passCheckInput() {

			boolean pass = false;

			if (selectedRel == null || selectedTerm == null) {
				pass = false;
			} else {
				RelationshipObject rObj = (RelationshipObject) selectedRel
						.getValue();
				TermObject tObj = (TermObject) selectedTerm.getValue();
				if (tObj == null || rObj == null) {
					pass = false;
				} else {
					if (rObj.getUri() == null || tObj.getUri() == null) {
						pass = false;
					} else {
						if ((((String) rObj.getUri()).length() == 0)
								|| (((String) tObj.getUri()).length() == 0)) {
							pass = false;
						} else {
							pass = true;
						}
					}
				}
			}
			return pass;
		}

		public void onClick(ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			if(sender.equals(submit)){
				if(passCheckInput()){
					/*if(((RelationshipObject)(selectedRel.getValue())).getUri().equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASTRANSLATION) && termObject.getLang().equals(((TermObject) selectedTerm.getValue()).getLang()))
					{
						Window.alert(constants.termNoTranslationRel());
					}
					else*/
					{
						this.hide();
						submit.setEnabled(false);
						onSubmit();
					}
				}else{
					Window.alert(constants.conceptCompleteInfo());
				}
			}else if(sender.equals(cancel)){
				this.hide();			
			}
			onButtonClicked(sender);
		}

		public void onSubmit() {
			sayLoading();

			final RelationshipObject rObj = (RelationshipObject) selectedRel.getValue();
			final TermObject destTermObj = (TermObject) selectedTerm.getValue();

			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				public void onSuccess(Void results)
				{
					/*if(rObj.getUri().equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASTRANSLATION) && !termObject.isMainLabel() && !destTermObj.isMainLabel())
					{
						if(Window.confirm(constants.termSelectCodeAGROVOC()))
						{
							ArrayList<TermObject> terms = new ArrayList<TermObject>();
							terms.add(termObject);
							terms.add(destTermObj);
							if (tcBrowser == null || !tcBrowser.isLoaded)
								tcBrowser = new TermCodeBrowser();
							tcBrowser.submit.addClickHandler(new ClickHandler()
							{
								public void onClick(ClickEvent event) {
									AsyncCallback<Void> callback = new AsyncCallback<Void>(){
										public void onSuccess(Void results){
											termDetailPanel.tRel.setURI(termObject, conceptObject);
											ModuleManager.resetValidation();
										}
										public void onFailure(Throwable caught){
											ExceptionManager.showException(caught, constants.termEditValueFail());
										}
									};

									NonFuncObject nonFuncObj = new NonFuncObject();
									nonFuncObj.setValue(tcBrowser.getNewTermCode());

									NonFuncObject oldValue = new NonFuncObject();
									oldValue.setValue(tcBrowser.getOldTermCode());

									TermObject selectedTermObject = tcBrowser.getTermObjectWithNewCode();
									OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termNoteEdit);
									int	actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termNoteEdit));

									Service.termService.updatePropertyValue(MainApp.userOntology,actionId, status, MainApp.userId, oldValue, nonFuncObj, ModelConstants.COMMONBASENAMESPACE+ModelConstants.RHASCODEAGROVOC, selectedTermObject, conceptObject, callback);

								}
							});
							tcBrowser.setValues(terms);
							tcBrowser.show();
						}else
						{
							termDetailPanel.tRel.setURI(termObject, conceptObject);
							ModuleManager.resetValidation();
						}
					}
					else*/
					{
						termDetailPanel.tRel.setURI(termObject, conceptObject);
						ModuleManager.resetValidation();
					}
				}

				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.termAddRelationshipFail());
				}
			};
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(
					ConceptActionKey.termRelationshipAdd);
			int actionId = Integer.parseInt((String) initData.getActionMap()
					.get(ConceptActionKey.termRelationshipAdd));

			Service.termService.addTermRelationship(MainApp.userOntology,
					actionId, status, MainApp.userId, rObj.getUri(), termObject,
					destTermObj, conceptObject, callback);
		}
	}

	/*public class TermCodeBrowser extends FormDialogBox implements ClickHandler {
		private ArrayList<TermObject> termObjects;
		private Grid table;
		LoadingDialog sayLoading = new LoadingDialog();

		public TermCodeBrowser() {
			super();
			this.setText(constants.termCodeBrowser());
			setWidth("400px");
			setHeight("200px");
			this.panel.add(sayLoading);
		}

		public void setValues(ArrayList<TermObject> termObjects)
		{
			this.termObjects = termObjects;
			this.initLayout();
		}

		public void initLayout() {

			AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
				public void onSuccess(HashMap<String, String> termCodeMap) {
					TermCodeBrowser.this.panel.remove(sayLoading);
					table = new Grid(termCodeMap.size()+1, 2);
					table.setWidget(0, 0, new HTML(constants.termCode()));
					table.setWidget(0, 1, new HTML(constants.termTerm()));
					if (!termCodeMap.isEmpty())
					{
						for (int i=0;i<termObjects.size();i++) {

							TermObject tObj = (TermObject) termObjects.get(i);
							String termCode = termCodeMap.get(tObj.getUri());

							VRadioButton cb = new VRadioButton("term", termCode , tObj);

							HorizontalPanel hp = new HorizontalPanel();
							if (tObj.isMainLabel())
							{
								hp.add(new HTML(tObj.getLabel()+"&nbsp;(" + tObj.getStatus()+ "&nbsp;[preferred]&nbsp;" + ")&nbsp;"));
							} else
							{
							    hp.add(new HTML(tObj.getLabel()+"&nbsp;(" + tObj.getStatus()+ ")&nbsp;"));
							}
							hp.setCellVerticalAlignment(cb,HasVerticalAlignment.ALIGN_MIDDLE);
							hp.setCellVerticalAlignment(hp.getWidget(0),HasVerticalAlignment.ALIGN_MIDDLE);

							table.setWidget(i+1, 0, cb);
							table.setWidget(i+1, 1, hp);
						}
					}

					table.setWidth("100%");
					table.getColumnFormatter().setWidth(1, "80%");

					ScrollPanel sc = new ScrollPanel();
					sc.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
					sc.setHeight("400px");
					sc.setWidth("100%");

					addWidget(sc);
				}

				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.termGetTermCodeFail());
				}
			};
			ArrayList<String> terms = new ArrayList<String>();
			for (TermObject tObj: termObjects) {

				terms.add(tObj.getUri());
			}
			Service.termService.getTermCodes(terms, MainApp.userOntology, callback);
		}

		public String getNewTermCode() {
			String termCode = "";
			for (int i = 1; i < table.getRowCount(); i++) {
				VRadioButton cb = (VRadioButton) table.getWidget(i, 0);
				if (cb.getValue()) {
					termCode = (String) cb.getText();
				}
			}
			return termCode;
		}

		public String getOldTermCode() {
			String termCode = "";
			for (int i = 1; i < table.getRowCount(); i++) {
				VRadioButton cb = (VRadioButton) table.getWidget(i, 0);
				if (!cb.getValue()) {
					termCode = (String) cb.getText();
				}
			}
			return termCode;
		}

		public TermObject getTermObjectWithNewCode() {
			TermObject tObj = new TermObject();
			for (int i = 1; i < table.getRowCount(); i++) {
				VRadioButton cb = (VRadioButton) table.getWidget(i, 0);
				if (!cb.getValue()) {
					tObj = (TermObject) cb.getObject();
				}
			}
			return tObj;
		}


	}*/
}