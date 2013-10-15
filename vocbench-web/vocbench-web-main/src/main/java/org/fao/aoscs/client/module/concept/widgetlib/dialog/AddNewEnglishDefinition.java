package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.DefinitionObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class AddNewEnglishDefinition extends FormDialogBox {
	
	final private HandlerManager handlerManager = new HandlerManager(this);
	private TextArea def;
	private ListBox source;
	private TextBox URL;
	private ConceptObject conceptObject;
	private TermObject termObject;
	private String parentConceptURI;
	private String namespace;
	private InitializeConceptData initData;
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	public String rootConceptURI = null;
	
	public AddNewEnglishDefinition(InitializeConceptData initData, ConceptObject conceptObject, TermObject termObject, String parentConceptURI, String namespace){ 
		super(constants.buttonSubmit(), constants.buttonCancel());
		setWidth("400px");
		this.setText(constants.conceptAddDefinition());
		this.initData = initData;
		this.conceptObject = conceptObject;
		this.termObject = termObject;
		this.parentConceptURI = parentConceptURI;
		this.namespace = namespace;
		this.initLayout();
	}
	
	public void initLayout(){
		
		def = new TextArea();
		def.setVisibleLines(3);
		def.setWidth("100%");
		
		source = new ListBox();
		source = Convert.makeSourceListBox((ArrayList<String[]>)initData.getSource());
		source.setWidth("100%");
		
		URL = new TextBox();	
		URL.setWidth("100%");
		
		final Grid table = new Grid(4,2);
		table.setWidget(0, 0, new HTML(constants.conceptDefinition()));
		table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
		table.setWidget(2, 0, new HTML(constants.conceptSource()));
		table.setWidget(3, 0, new HTML(constants.conceptUrl()));
		table.setWidget(0, 1, def);
		table.setWidget(1, 1, new HTML("en"));
		table.setWidget(2, 1, source);
		table.setWidget(3, 1, URL);
		table.getColumnFormatter().setWidth(1, "80%");
		table.setWidth("100%");
		
		addWidget(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		
		source.addChangeHandler(new ChangeHandler()
		{

		    public void onChange(ChangeEvent arg0)
            {			      
               if(source.getItemText(source.getSelectedIndex()).equals("Book")){                       
                   ((HTML)table.getWidget(1,0)).setText(constants.conceptSourceTitle());
               }else{
                   ((HTML)table.getWidget(1,0)).setText(constants.conceptUrl());
               }
            }
	    });
	}
		  
	public boolean passCheckInput() {
		boolean pass = false;
		if(def.getText().equals("")/* || source.getValue((source.getSelectedIndex())).equals("") || URL.getText().length()==0 */){
			pass = false;
		}else {
			if(ConfigConstants.MANDATORY_DEFINITION_NAMESPACES.contains(MainApp.defaultNamespace))
			{
				 if(source.getValue((source.getSelectedIndex())).equals("") || URL.getText().length()==0)
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
		
		int actionIdConceptCreate = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptCreate));
		
		final AsyncCallback<ConceptObject> callback = new AsyncCallback<ConceptObject>(){
			public void onSuccess(ConceptObject results){
				
				final ConceptObject conceptObject = (ConceptObject) results ;
				
				TranslationObject transObj = new TranslationObject();
				transObj.setType(TranslationObject.DEFINITIONTRANSLATION);
				transObj.setLabel(def.getText());
				transObj.setLang("en");
				
				IDObject ido = new IDObject();
				ido.setIDType(IDObject.DEFINITION);
				ido.addIDTranslationList(transObj);
				ido.setIDSourceURL(URL.getText());
				ido.setIDSource(source.getValue(source.getSelectedIndex()));
				
				ModuleManager.resetValidation();
				
				AsyncCallback<DefinitionObject>  callback = new AsyncCallback<DefinitionObject>(){
					public void onSuccess(DefinitionObject results){
						//treePanel.gotoItem(conceptObject.getUri(), rootConceptURI, InfoTab.term, showAlsoNonpreferredTerms.getValue(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage);
						ModuleManager.resetValidation();
						handlerManager.fireEvent(new AddNewEnglishDefinitionSuccessEvent(conceptObject));	
					}
					public void onFailure(Throwable caught){
						ExceptionManager.showException(caught, constants.conceptAddDefinitionFail());
					}
				};

				OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.conceptEditDefinitionCreate);
				int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditDefinitionCreate));

				Service.conceptService.addDefinition(MainApp.userOntology,actionId, status, MainApp.userId, transObj, ido, conceptObject, callback);
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptAddFail());
			}
		};
		Service.conceptService.addNewConcept(MainApp.userOntology, actionIdConceptCreate, MainApp.userId, MainApp.schemeUri, namespace, conceptObject, termObject, parentConceptURI, ConfigConstants.CODETYPE, callback);
		
	}
	
	 public void onCancel(){
		 
		 if(Window.confirm(constants.conceptConfirmSkipAddDefinition()))
	    	this.hide();
	    }
	
	public void addAddNewEnglishDefinitionSuccessHandler(AddNewEnglishDefinitionSuccessHandler handler) {
		handlerManager.addHandler(AddNewEnglishDefinitionSuccessEvent.getType(), handler);
		
		
		
		
	}
	
	// Event for indicating add new English definition successful
	static class AddNewEnglishDefinitionSuccessEvent extends GwtEvent<AddNewEnglishDefinitionSuccessHandler> {
		private static final Type<AddNewEnglishDefinitionSuccessHandler> TYPE = new Type<AddNewEnglishDefinitionSuccessHandler>();
		private ConceptObject cObj = new ConceptObject();
		public AddNewEnglishDefinitionSuccessEvent(ConceptObject cObj) {
			this.cObj = cObj;
		}
		public static Type<AddNewEnglishDefinitionSuccessHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(AddNewEnglishDefinitionSuccessHandler handler) {
			handler.onAddNewEnglishDefinitionSuccess(cObj);
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<AddNewEnglishDefinitionSuccessHandler> getAssociatedType() {
			return TYPE;
		}
	}
	public interface AddNewEnglishDefinitionSuccessHandler extends EventHandler {
		void onAddNewEnglishDefinitionSuccess(ConceptObject conceptObject);
	}
}