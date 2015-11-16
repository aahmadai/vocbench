package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.ResourceURIPanel;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.ResourceURIPanel.ResourceURIPanelOpener;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.utility.TimeConverter;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.PermissionObject;
import org.fao.aoscs.domain.TranslationObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConceptDefinition extends ConceptTemplate implements ResourceURIPanelOpener{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private ArrayList<ArrayList<String>> langlist = new ArrayList<ArrayList<String>>();

	private AddNewDefinition addNewDefinition;
	private EditExternalSource editExternalSource;
	private DeleteExternalSource deleteExternalSource;
	private AddExternalSource addExternalSource;
	private EditDefinitionLabel editDefinitionLabel;
	private DeleteDefinitionLabel deleteDefinitionLabel;
	private DeleteDefinition deleteDefinition;
	private AddDefinitionLabel addDefinitionLabel;
	
	public ConceptDefinition(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}

	private void attachNewDefButton(){
		functionPanel.clear();
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONCREATE, getConceptObject().getStatusID());
		// Create definition
		LinkLabelAOS addFunc = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddDefinition(), constants.conceptAddDefinition(), permission, new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				if(addNewDefinition == null || !addNewDefinition.isLoaded)
					addNewDefinition = new AddNewDefinition();
				addNewDefinition.show();
			}
		});
		functionPanel.add(addFunc);
	}
	
	private VerticalPanel getDateTable(IDObject dObj){

		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.conceptCreateDate()));
		table.setWidget(0, 1, new HTML(TimeConverter.formatDate(dObj.getIDDateCreate())));
		table.setWidget(1, 0, new HTML(constants.conceptUpdateDate()));
		table.setWidget(1, 1, new HTML(TimeConverter.formatDate(dObj.getIDDateModified())));
		
		table.getColumnFormatter().setWidth(1, "80%");
		table.setWidth("100%");
		return GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1");
		
	}

	private VerticalPanel getOtherPropTable(IDObject dObj){

		FlexTable table = new FlexTable();
		HashMap<String, ArrayList<String>> otherPropList = dObj.getOtherPropList(); 
		for(String prop : otherPropList.keySet())
		{
			ArrayList<String> list = otherPropList.get(prop);
			VerticalPanel vp = new VerticalPanel();
			for(String value: list)
			{
				vp.add(new HTML(value));
			}
			int rowCnt = table.getRowCount();
			table.setWidget(rowCnt, 0, new HTML(prop));
			table.setWidget(rowCnt, 1, vp);
		}
		
		table.getColumnFormatter().setWidth(1, "80%");
		table.setWidth("100%");
		return GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1");
		
	}
	
	private VerticalPanel getSourceTable(int number, IDObject dObj){

		FlexTable table = new FlexTable();
		
		if(dObj!=null && dObj.getIDSourceURL() != null && !dObj.getIDSourceURL().equals(""))
		{
			int rowCount = table.getRowCount();
			table.setWidget(rowCount, 0, new HTML(constants.conceptSource()));
			table.setWidget(rowCount, 1, getSourcePanel(number, dObj,  "<a href=\""+dObj.getIDSourceURL()+"\" target=\"_blank\">"+dObj.getIDSourceURL()+"</a>", true));
		}
		
		if(dObj!=null && dObj.getIDSource() != null && !dObj.getIDSource().equals(""))
		{
			int rowCount = table.getRowCount();
			table.setWidget(rowCount, 0, new HTML(constants.conceptSource()));
			table.setWidget(rowCount, 1, getSourcePanel(number, dObj,  dObj.getIDSource(), false));
		}
		if(table.getRowCount()>0)
			table.getColumnFormatter().setWidth(1, "80%");
		table.setWidth("100%");
		return GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1");
	}
	
	private Widget getSourcePanel(int number, final IDObject dObj, String label, final boolean isSourceURL)
	{
		ArrayList<String> langs = langlist.get(number);
		
		HorizontalPanel hpURL = new HorizontalPanel();
		hpURL.setSpacing(3);
		
		if(label!=null && !label.equals(""))
		{
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_EXTSOURCEEDIT, getConceptObject().getStatusID(), langs, MainApp.getPermissionCheck(langs));
			// Source edit
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditSource(), permission, new ClickHandler(){;
				public void onClick(ClickEvent event) {
					if(editExternalSource == null || !editExternalSource.isLoaded)
						editExternalSource = new EditExternalSource(dObj, isSourceURL);
					editExternalSource.show();
				}
			});

			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_EXTSOURCEDELETE, getConceptObject().getStatusID(), langs, MainApp.getPermissionCheck(langs));
			// source delete
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteSource(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteExternalSource == null  || !deleteExternalSource.isLoaded)
						deleteExternalSource = new DeleteExternalSource(dObj);
					deleteExternalSource.show();
				}
			});
			
			HTML link =  new HTML(label);

			hpURL.add(edit);
			hpURL.add(delete);
			hpURL.add(link);
		}
		
		return hpURL;
	}

	private VerticalPanel getTranslationTable(IDObject dObj){
		ArrayList<TranslationObject> list = dObj.getIDTranslationList();
		Grid table = new Grid(list.size()+1,2);
		table.setWidget(0, 0, new HTML(constants.conceptLanguage()));
		table.setWidget(0, 1, new HTML(constants.conceptDefinition()));
		ArrayList<String> langs = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			final TranslationObject tObj = (TranslationObject) list.get(i);
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			langs.add(tObj.getLang());
			boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONTRANSLATIONEDIT, getConceptObject().getStatusID(), tObj.getLang(), MainApp.getPermissionCheck(tObj.getLang()));
			// Definition translation edit
			LinkLabelAOS edit = new LinkLabelAOS("images/edit-grey.gif", "images/edit-grey-disabled.gif", constants.conceptEditTranslation(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(editDefinitionLabel == null || !editDefinitionLabel.isLoaded)
						editDefinitionLabel = new EditDefinitionLabel(tObj);
					editDefinitionLabel.show();
				}
			});

			permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONTRANSLATIONDELETE, getConceptObject().getStatusID(), tObj.getLang(), MainApp.getPermissionCheck(tObj.getLang()));
			// Definition translation delete
			LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteTranslation(), permission, new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(deleteDefinitionLabel == null || !deleteDefinitionLabel.isLoaded)
						deleteDefinitionLabel = new DeleteDefinitionLabel(tObj);
					deleteDefinitionLabel.show();
				}
			});
			hp.add(edit);
			hp.add(delete);

			HTML lang = new HTML(getFullnameofLanguage(tObj.getLang()));
			lang.setWordWrap(false);

			HTML label = new HTML(tObj.getLabel());
			label.setWordWrap(true);
			hp.add(label);

			table.getColumnFormatter().setWidth(1, "100%");
			table.setWidget(i+1, 0, lang);
			table.setWidget(i+1, 1, hp);
		}
		langlist.add(langs);
		table.setWidth("100%");
		return GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true);
	}

	private HorizontalPanel getDefinitionNumber(int number , final IDObject dObj){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(3);
		Label label = new Label(Integer.toString(number));
		hp.add(label);
		ArrayList<String> langs = langlist.get(number-1);
		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONDELETE, getConceptObject().getStatusID(), langs, MainApp.getPermissionCheck(langs));
		// Definition delete
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteDefinition(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteDefinition == null || !deleteDefinition.isLoaded)
					deleteDefinition = new DeleteDefinition(dObj);
				deleteDefinition.show();
			}
		});
		hp.add(delete);
		hp.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
		return hp;
	}

	private HorizontalPanel getAddTranslationFunction(final IDObject dObj){
		HorizontalPanel hp = new HorizontalPanel();

		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_DEFINITIONTRANSLATIONCREATE, getConceptObject().getStatusID());
		// Create translation
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif",  "images/add-grey-disabled.gif",constants.conceptAddNewTranslation(), constants.conceptAddDefTranslation(), permission, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(addDefinitionLabel == null || !addDefinitionLabel.isLoaded)
					addDefinitionLabel = new AddDefinitionLabel(dObj);
				addDefinitionLabel.show();
			}
		});
		hp.add(add);
		return hp;
	}
	
	private HorizontalPanel getAddSourcFunction(final IDObject dObj){
		HorizontalPanel hp = new HorizontalPanel();

		boolean permission = permissionTable.contains(OWLActionConstants.CONCEPTEDIT_EXTSOURCECREATE, getConceptObject().getStatusID());
		// Source Create
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", constants.conceptAddNewSource(), constants.conceptAddNewSource(), permission, new ClickHandler(){
			public void onClick(ClickEvent event) {
				if(addExternalSource == null || !addExternalSource.isLoaded)
					addExternalSource = new AddExternalSource(dObj);
				addExternalSource.show();
			}
		});
		hp.add(add);
		return hp;
	}

	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getDefinitionObject()!=null)
		{
			initData(cDetailObj.getDefinitionObject());
		}
		else
		{
			AsyncCallback<DefinitionObject> callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results) {
					cDetailObj.setDefinitionObject(results);
					initData((DefinitionObject) results);
				}
				public void onFailure(Throwable caught) {
					ExceptionManager.showException(caught, constants.conceptGetDefinitionFail());
				}
			};

			Service.conceptService.getConceptDefinition(conceptObject.getUri(), MainApp.userOntology, callback);
		}
	}
	
	/*private HorizontalPanel uriPanel(final ManageResourceURI manageResourceURI, String uri, final String conceptUri){
		final HTML URI = new HTML();
		URI.setText(uri);
		URI.setWidth("100%");
		URI.setWordWrap(true);
		URI.addStyleName("link-label-blue");
		URI.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!URI.getText().equals(""))
				MainApp.openURL(URI.getText());
			}
		});
				
		HTML label = new HTML("&nbsp;&nbsp;"+constants.conceptUri()+":&nbsp;");
		label.setStyleName(Style.fontWeightBold);
		
		Image editURI = new Image("images/edit-grey.gif");
		editURI.setTitle(constants.buttonEdit());
		editURI.setStyleName(Style.Link);
		editURI.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				manageResourceURI.addSubmitClickHandler(new ClickHandler() {
					public void onClick(ClickEvent arg0) {
						
						if(Window.confirm(constants.refactorRenameURIWaring()))
						{
							manageResourceURI.showLoading(true);
							AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
								public void onSuccess(Boolean result){
									manageResourceURI.showLoading(false);
									if(result)
									{
										manageResourceURI.hide();
										ModuleManager.getMainApp().reloadConceptTree(conceptUri);
									}
									else
										Window.alert(constants.refactorActionFailed());
								}
								public void onFailure(Throwable caught){
									ExceptionManager.showException(caught, constants.refactorActionFailed());
								}
							};
							Service.refactorService.renameResource(MainApp.userOntology, manageResourceURI.getOldURI().getValue(), manageResourceURI.getNewURI().getValue(), callback);
						}
					}
				});
				manageResourceURI.show(URI.getText());
			}
		});
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(label);		
		hp.add(URI);
		hp.add(editURI);
		hp.setWidth("100%");
		hp.setStyleName("showuri");
		hp.setCellWidth(URI, "100%");
		hp.setCellHorizontalAlignment(URI, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(editURI, HasHorizontalAlignment.ALIGN_RIGHT);
		return hp;
	}*/
	
	private void initData(DefinitionObject dfObj)
	{
		clearPanel();
		if(dfObj!=null && !dfObj.isEmpty()){
			attachNewDefButton();
			ArrayList<IDObject> dObjList = dfObj.getDefinitionListOnly();
			Grid table = new Grid(dObjList.size()+1,2);
			table.setWidget(0, 0, new HTML(constants.conceptNo()));
			table.setWidget(0, 1, new HTML(constants.conceptDefinition()));
			table.setWidth("100%");

			for (int i = 0; i < dObjList.size(); i++) {
				IDObject dObj = (IDObject) dObjList.get(i);
				
				HorizontalPanel func = getAddTranslationFunction(dObj);
				HorizontalPanel funcSource = getAddSourcFunction(dObj);
				
				VerticalPanel vp = new VerticalPanel();
				vp.add(func);
				vp.setCellHorizontalAlignment(func, HasHorizontalAlignment.ALIGN_RIGHT);
				vp.add(getTranslationTable(dObj));
				if(dObj!=null && (dObj.getIDSource() == null || dObj.getIDSource().equals("")) && (dObj.getIDSourceURL() == null || dObj.getIDSourceURL().equals("")))
				{
					vp.add(funcSource);
					vp.setCellHorizontalAlignment(funcSource, HasHorizontalAlignment.ALIGN_RIGHT);
				}
				else
					vp.add(getSourceTable(i, dObj));
				vp.add(getDateTable(dObj));
				vp.add(getOtherPropTable(dObj));
				vp.setWidth("100%");
				vp.setSpacing(5);
				
				ResourceURIPanel resourceURIPanel = new ResourceURIPanel(ConceptDefinition.this);
				resourceURIPanel.setResourceURI(dObj.getIDUri());
				
				VerticalPanel bodyPanel = new VerticalPanel();
				bodyPanel.setSize("100%", "100%");
				bodyPanel.add(resourceURIPanel);
				bodyPanel.add(vp);
				
				table.setWidget(i+1, 0, getDefinitionNumber(i+1, dObj));
				table.setWidget(i+1, 1, bodyPanel);
			}
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.DEFINITION.getTabIndex(), Convert.replaceSpace(dObjList.size()>1? constants.conceptDefinitions():constants.conceptDefinition())+"&nbsp;("+(dObjList.size())+")");
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.DEFINITION.getTabIndex(), Convert.replaceSpace(dObjList.size()>1? constants.conceptDefinitions():constants.conceptDefinition())+"&nbsp;("+(dObjList.size())+")");
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1-NoPadding","gstPanel1",true));
		}else{
			attachNewDefButton();
			Label sayNo = new Label(constants.conceptNoDefinition());
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.DEFINITION.getTabIndex(), Convert.replaceSpace(constants.conceptDefinition())+"&nbsp;(0)");
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.DEFINITION.getTabIndex(), Convert.replaceSpace(constants.conceptDefinition())+"&nbsp;(0)");
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}

	public class DeleteExternalSource extends FormDialogBox implements ClickHandler{

		private IDObject ido;

		public DeleteExternalSource(IDObject ido){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptDeleteSource());
			setWidth("400px");
			initLayout();
		}

		public void initLayout() {
			HTML message = new HTML(constants.conceptDefinitionSourceDeleteWarning());

			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1,message);
			table.setWidth("100%");

			addWidget(table);
		}

		public void onSubmit() {
			sayLoading();

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptDeleteExternalSourceFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionExtSourceDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionExtSourceDelete));



			Service.conceptService.deleteDefinitionExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
		}

	}

	public class AddExternalSource extends FormDialogBox implements ClickHandler{
		//private ListBox source;
		private TextBox URL;
		private IDObject ido;
		private RadioButton rdoSrc;
		private RadioButton rdoUrl;

		public AddExternalSource(IDObject ido){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptEditExternalSource());
			setWidth("400px");
			initLayout();
		}

		public void initLayout() {
			
			final FlexTable table = new FlexTable();
			//source = new ListBox();
			//source = Convert.makeSourceListBox((ArrayList<String[]>)initData.getSource());
			//source.setWidth("100%");

			URL = new TextBox();
			URL.setWidth("100%");
			
			rdoUrl = new RadioButton("radioGroup", constants.conceptUrl());
			rdoSrc = new RadioButton("radioGroup", constants.conceptDescription());
			rdoUrl.setValue(true);
			
			HorizontalPanel hpSrcUrl = new HorizontalPanel();
			hpSrcUrl.setWidth("100%");
			hpSrcUrl.add(rdoUrl);
			hpSrcUrl.add(rdoSrc);
			hpSrcUrl.setCellHorizontalAlignment(rdoUrl, HasAlignment.ALIGN_LEFT);
			hpSrcUrl.setCellHorizontalAlignment(rdoSrc, HasAlignment.ALIGN_LEFT);

			table.setWidget(0, 0, new HTML(constants.buttonSelect()));
			table.setWidget(0, 1, hpSrcUrl);
			table.setWidget(1, 0, new HTML(constants.conceptSource()));
			table.setWidget(1, 1, URL);
			//table.setWidget(1, 0, new HTML(constants.conceptSource()));
			//table.setWidget(1, 1, source);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");

			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));

			/*source.addChangeHandler(new ChangeHandler()
			{

			    public void onChange(ChangeEvent arg0)
                {
                   if(source.getItemText(source.getSelectedIndex()).equals("Book")){
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptSourceTitle());
                   }else{
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptUrl());
                   }
                }});*/
		}


		public boolean passCheckInput() {
			boolean pass = false;
			if(/*source.getValue((source.getSelectedIndex())).equals("") || */URL.getText().length()==0 ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}


		public void onSubmit() {
			sayLoading();
			if(rdoUrl.getValue())
				ido.setIDSourceURL(URL.getText());
			else
				ido.setIDSource(URL.getText());
			//ido.setIDSource(source.getValue(source.getSelectedIndex()));

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptAddExternalSourceFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionExtSourceCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionExtSourceCreate));


			Service.conceptService.addDefinitionExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
		}


	}

	public class EditExternalSource extends FormDialogBox implements ClickHandler{
		//private ListBox source ;
		private TextBox URL;
		private IDObject ido;
		private boolean isSourceURL = true;

		public EditExternalSource(IDObject ido, boolean isSourceURL){
			super();
			this.ido = ido;
			this.isSourceURL = isSourceURL;
			this.setText(constants.conceptEditExternalSource());
			setWidth("400px");
			initLayout();
		}

		public void initLayout() {
			
			final FlexTable table = new FlexTable();
			
			//source = new ListBox();
			//source =  Convert.makeSelectedSourceListBox((ArrayList<String[]>)initData.getSource(),ido.getIDSource());
			//source.setWidth("100%");

			URL = new TextBox();
			URL.setWidth("100%");
			
			if(isSourceURL)
			{
				URL.setText(ido.getIDSourceURL());
			}
			else 
			{
				URL.setText(ido.getIDSource());
			}
			

			table.setWidget(0, 0, new HTML(constants.conceptSource()));
			table.setWidget(0, 1, URL);
			//table.setWidget(1, 0, new HTML(constants.conceptSource()));
			//table.setWidget(1, 1, source);
			table.setWidth("100%");

			/*if(source.getValue(source.getSelectedIndex()).equals("Book")){
			    ((HTML)table.getWidget(1,0)).setText(constants.conceptSourceTitle());
			}
			source.addChangeHandler(new ChangeHandler()
            {
                public void onChange(ChangeEvent arg0)
                {
                   if(source.getItemText(source.getSelectedIndex()).equals("Book"))
                   {
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptSourceTitle());
                   }else
                   {
                       ((HTML)table.getWidget(1,0)).setText(constants.conceptUrl());
                   }
              }});*/
			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}

		public boolean passCheckInput() {
			boolean pass = false;
			if(/*source.getValue((source.getSelectedIndex())).equals("") || */URL.getText().equals("") ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}

		public void onSubmit() {
			sayLoading();

			IDObject idoNew = new IDObject();
			idoNew.setIDType(IDObject.DEFINITION);
			idoNew.setIDUri(ido.getIDUri());
			if(isSourceURL)
			{
				idoNew.setIDSourceURL(URL.getText());
			}
			else 
			{
				idoNew.setIDSource(URL.getText());
			}
			//idoNew.setIDSource(source.getValue(source.getSelectedIndex()));

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptEditTranslationFail());
				}
			};


			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionExtSourceEdit);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionExtSourceEdit));


			Service.conceptService.editDefinitionExternalSource(MainApp.userOntology,actionId, status, MainApp.userId, ido, idoNew, conceptObject, callback);
		}

	}
	
	public class AddDefinitionLabel extends FormDialogBox{

		private ListBox language;
		private TextArea def;
		private IDObject ido;
		
		

		public AddDefinitionLabel(IDObject ido){
			super(constants.buttonCreate(), constants.buttonCancel());
			this.ido = ido;
			this.setText(constants.conceptAddDefinitionLabel()); //"Add new label"
			setWidth("400px");
			initLayout();
		}

		public void initLayout() {
			def = new TextArea();
			def.setWidth("100%");
			def.setVisibleLines(3);
			
			language = new ListBox();
			language = Convert.makeListWithUserLanguagesFilterOutAdded(MainApp.languageDict, MainApp.getUserLanguagePermissionList(), Convert.getUsedLangList(ido));
			language.setWidth("100%");

			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptDefinition()));
			table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(0, 1, def);
			table.setWidget(1, 1, language);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");

			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}

		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("--None--") || language.getValue((language.getSelectedIndex())).equals("") || def.getText().length()==0 ){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}

		public void onSubmit() {
			sayLoading();

			TranslationObject transObjNew = new TranslationObject();
			transObjNew.setType(TranslationObject.DEFINITIONTRANSLATION);
			transObjNew.setLabel(def.getText());
			transObjNew.setLang(language.getValue(language.getSelectedIndex()));
			transObjNew.setUri(ido.getIDUri());

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptAddTranslationFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionTranslationCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionTranslationCreate));


			Service.conceptService.addDefinitionLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObjNew, ido, conceptObject, callback);
		}
	}

	public class DeleteDefinition extends FormDialogBox implements ClickHandler{
		private IDObject ido ;

		public DeleteDefinition(IDObject ido){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.ido = ido;
			setWidth("400px");
			setText(constants.conceptDeleteDefinition());
			initLayout();
		}

		public void initLayout() {
			HTML message = new HTML(constants.conceptDefinitionDeleteWarning());

			Grid table = new Grid(1,2);
			table.setWidget(0, 0,getWarningImage());
			table.setWidget(0, 1, message);

			addWidget(table);
		}


	    public void onSubmit() {
	    	DeleteDefinition.this.hide();
			sayLoading();

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptAddDefinitionFail());
				}
			};
			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionDelete));

			Service.conceptService.deleteDefinition(MainApp.userOntology,actionId, status, MainApp.userId, ido, conceptObject, callback);
	    }

	}

	public class DeleteDefinitionLabel extends FormDialogBox implements ClickHandler{
		private TranslationObject transObj;

		public DeleteDefinitionLabel(TranslationObject transObj){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.transObj = transObj;
			this.setText(constants.conceptDeleteDefinitionLabel());
			setWidth("400px");
			initLayout();
		}

		public void initLayout()
		{
			HTML message = new HTML(messages.conceptDefinitionLabelDeleteWarning(transObj.getLabel(), transObj.getLang()));
			Grid table = new Grid(1,2);
			table.setWidget(0,0,getWarningImage());
			table.setWidget(0,1, message);
			table.setWidth("100%");

			addWidget(table);
		}

		public void onSubmit() {
			sayLoading();

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptDeleteDefinitionFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionTranslationDelete);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionTranslationDelete));

			Service.conceptService.deleteDefinitionLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObj, conceptObject, callback);
		}
	}

	public class EditDefinitionLabel extends FormDialogBox implements ClickHandler{

		private TextArea def ;
		private ListBox language ;
		private TranslationObject transObj ;

		public EditDefinitionLabel(TranslationObject transObj){
			super();
			this.transObj = transObj;
			this.setText(constants.conceptEditDefinitionLabel());
			setWidth("400px");
			initLayout();
		}

		public void initLayout() {
			language = new ListBox();
			language =  Convert.makeSelectedLanguageListBox((ArrayList<String[]>)MainApp.getLanguage(),transObj.getLang());
			language.setWidth("100%");
			language.setEnabled(false);

			def = new TextArea();
			def.setText(transObj.getLabel());
			def.setWidth("100%");
			def.setVisibleLines(3);

			Grid table = new Grid(2,2);
			table.setWidget(0, 0, new HTML(constants.conceptDefinition()));
			table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(0, 1, def);
			table.setWidget(1, 1, language);
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");

			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		}


		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("--None--") || language.getValue((language.getSelectedIndex())).equals("") || def.getText().length()==0){
				pass = false;
			}else{
				pass = true;
			}
			return pass;
		}

		public void onSubmit() {
			sayLoading();

			TranslationObject transObjNew = new TranslationObject();
			transObjNew.setType(TranslationObject.DEFINITIONTRANSLATION);
			transObjNew.setLabel(def.getText().replaceAll("\"", "\\\\\""));
			transObjNew.setLang(language.getValue(language.getSelectedIndex()));
			transObjNew.setUri(transObj.getUri());

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptAddDefinitionFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get("term-edit");
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionTranslationEdit));

			Service.conceptService.editDefinitionLabel(MainApp.userOntology,actionId, status, MainApp.userId, transObj, transObjNew,  conceptObject, callback);
		}

	}

	public class AddNewDefinition extends FormDialogBox {
		public ListBox language ;
		public TextArea def ;
		//private ListBox source;
		private TextBox URL;
		private RadioButton rdoSrc;
		private RadioButton rdoUrl;

		public AddNewDefinition(){
			super(constants.buttonCreate(), constants.buttonCancel());
			setWidth("400px");
			this.setText(constants.conceptAddDefinition());
			this.initLayout();
		}

		public void initLayout(){
			
			final FlexTable table = new FlexTable();
			
			language = new ListBox();
			language = Convert.makeListWithUserLanguages(MainApp.languageDict, MainApp.getUserLanguagePermissionList());
			language.setWidth("100%");

			def = new TextArea();
			def.setVisibleLines(3);
			def.setWidth("100%");

			//source = new ListBox();
			//source = Convert.makeSourceListBox((ArrayList<String[]>)initData.getSource());
			//source.setWidth("100%");

			URL = new TextBox();
			URL.setWidth("100%");
			
			rdoUrl = new RadioButton("radioGroup", constants.conceptUrl());
			rdoSrc = new RadioButton("radioGroup", constants.conceptDescription());
			rdoUrl.setValue(true);
			
			HorizontalPanel hpSrcUrl = new HorizontalPanel();
			hpSrcUrl.setWidth("100%");
			hpSrcUrl.add(rdoUrl);
			hpSrcUrl.add(rdoSrc);
			hpSrcUrl.setCellHorizontalAlignment(rdoUrl, HasAlignment.ALIGN_LEFT);
			hpSrcUrl.setCellHorizontalAlignment(rdoSrc, HasAlignment.ALIGN_LEFT);
			
			table.setWidget(0, 0, new HTML(constants.conceptDefinition()));
			table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
			table.setWidget(2, 0, new HTML(constants.buttonSelect()));
			table.setWidget(3, 0, new HTML(constants.conceptSource()));
			table.setWidget(0, 1, def);
			table.setWidget(1, 1, language);
			table.setWidget(2, 1, hpSrcUrl);
			table.setWidget(3, 1, URL);
			//table.setWidget(3, 1, source);
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidth("100%");

			addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));

			/*source.addChangeHandler(new ChangeHandler()
			{

			    public void onChange(ChangeEvent arg0)
                {
                   if(source.getItemText(source.getSelectedIndex()).equals("Book")){
                       ((HTML)table.getWidget(3,0)).setText(constants.conceptSourceTitle());
                   }else{
                       ((HTML)table.getWidget(3,0)).setText(constants.conceptUrl());
                   }
                }
		    });*/
		}

		public boolean passCheckInput() {
			boolean pass = false;
			if(language.getValue((language.getSelectedIndex())).equals("--None--") || language.getValue((language.getSelectedIndex())).equals("") || def.getText().equals("")){
				pass = false;
			}
			else {
				if(ConfigConstants.MANDATORY_DEFINITION_NAMESPACES.contains(MainApp.defaultNamespace))
				{
					if(/*source.getValue((source.getSelectedIndex())).equals("") || */URL.getText().length()==0)
						 pass = false;
					 else
						 pass = true;
				}
				else
					pass = true;
			}
			return pass;
		}

		public void onSubmit(){
			sayLoading();

			TranslationObject transObj = new TranslationObject();
			transObj.setType(TranslationObject.DEFINITIONTRANSLATION);
			transObj.setLabel(def.getText());
			transObj.setLang(language.getValue(language.getSelectedIndex()));

			IDObject ido = new IDObject();
			ido.setIDType(IDObject.DEFINITION);
			ido.addIDTranslationList(transObj);
			if(rdoUrl.getValue())
				ido.setIDSourceURL(URL.getText());
			else
				ido.setIDSource(URL.getText());
			//ido.setIDSource(source.getValue(source.getSelectedIndex()));

			AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
				public void onSuccess(DefinitionObject results){
					cDetailObj.setDefinitionObject(results);
					ConceptDefinition.this.initData();
					ModuleManager.resetValidation();
				}
				public void onFailure(Throwable caught){
					cDetailObj.setDefinitionObject(null);
					ConceptDefinition.this.initData();
					ExceptionManager.showException(caught, constants.conceptAddDefinitionFail());
				}
			};

			OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionCreate));

			Service.conceptService.addDefinition(MainApp.userOntology,actionId, status, MainApp.userId, transObj, ido, conceptObject, callback);
		}
	}
	
	public void resourceURIPanelSubmit(String newResourceURI) {
		ModuleManager.getMainApp().reloadConceptTree();
	}
}
