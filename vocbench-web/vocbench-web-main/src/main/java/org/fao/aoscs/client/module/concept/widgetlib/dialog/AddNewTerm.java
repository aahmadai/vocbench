package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FlexDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.ConceptTermObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.TermObject;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddNewTerm extends FlexDialogBox implements ClickHandler
{
	final private HandlerManager handlerManager = new HandlerManager(this);
	private TextBox term;
	private ListBox lang;
	private CheckBox main;
	private ConceptObject conceptObject;
	private InitializeConceptData initData;
	private boolean doLoop = false;
	private boolean isLanguageExist = false;
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel loadingPanel = new VerticalPanel();
	private VerticalPanel panel = new VerticalPanel();

	public AddNewTerm(InitializeConceptData initData, ConceptObject conceptObject)
	{
		super(constants.buttonAdd(), constants.buttonCancel(), constants.buttonAddAgain());
		this.setText(constants.conceptAddNewTerm());
		setWidth("400px");
		this.initData = initData;
		this.conceptObject = conceptObject;
		this.initLayout();
	}

	public AddNewTerm(InitializeConceptData initData, ConceptObject conceptObject, String conceptLabel)
	{
		super(constants.buttonAdd(), constants.buttonCancel(), constants.buttonAddAgain());
		this.setText(messages.conceptAddNewTerm(conceptLabel));
		setWidth("400px");
		this.initData = initData;
		this.conceptObject = conceptObject;
		this.initLayout();
	}

	public void initLayout()
	{

		lang = new ListBox();
		lang = Convert.makeListWithUserLanguages(MainApp.languageDict, MainApp.getUserLanguagePermissionList());
		lang.setWidth("100%");

		term = new TextBox();
		term.setWidth("100%");

		main = new CheckBox(constants.conceptPreferredTerm());

		Grid table = new Grid(2,2);
		table.setWidget(0, 0, new HTML(constants.conceptNewTerm()));
		table.setWidget(1, 0, new HTML(constants.conceptLanguage()));
		table.setWidget(0, 1, term);
		table.setWidget(1, 1, lang);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(1, "80%");

		
		mainPanel.add(GridStyle.setTableConceptDetailStyleleft(table,"gslRow1", "gslCol1", "gslPanel1"));
		mainPanel.add(main);
		mainPanel.setSpacing(0);
		mainPanel.setWidth("100%");
		mainPanel.setCellHorizontalAlignment(main, HasHorizontalAlignment.ALIGN_RIGHT);
		
		LoadingDialog sayLoading = new LoadingDialog();
		loadingPanel.add(sayLoading);
		loadingPanel.setSize("100%", "100%");
		loadingPanel.setCellHorizontalAlignment(sayLoading, HasHorizontalAlignment.ALIGN_CENTER);
		loadingPanel.setCellVerticalAlignment(sayLoading, HasVerticalAlignment.ALIGN_MIDDLE);
		loadingPanel.setCellHeight(sayLoading, "100%");
		loadingPanel.setCellWidth(sayLoading, "100%");

		panel.clear();
		panel.add(mainPanel);
		panel.setSpacing(0);
		panel.setWidth("100%");
		
		addWidget(panel);
	}

	public boolean passCheckInput() {
		boolean pass = false;
		if(lang.getValue((lang.getSelectedIndex())).equals("--None--") || lang.getValue((lang.getSelectedIndex())).equals("") ||term.getText().length()==0 ){
			pass = false;
		}else{
			pass = true;
		}
		return pass;
	}

	public void onLoopSubmit()
	{
		doLoop = true;
		onSubmit();
	}

	public void show(){
		super.show();
	}

	public void hide()
	{
		super.hide();
		if(isLanguageExist)
		{
			isLanguageExist = false;
			Window.alert(constants.conceptAddTermNoLangSelected());
		}
	}

	public void setIsLanguageExist(boolean bool)
	{
		isLanguageExist = bool;
	}

	public void resetDialog(){
		term.setText("");
		lang.setItemSelected(0, true);
		loop.setEnabled(true);
	}

	public void onCancel(){
		hide();
	}

	public void onSubmit()
	{
		showLoading();
		//show();
		OwlStatus status = (OwlStatus) initData.getActionStatus().get(ConceptActionKey.termCreate);
		int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.termCreate));

		TermObject newObject = new TermObject();
		newObject.setConceptUri(conceptObject.getUri());
		//newObject.setConceptName(conceptObject.getName());
		newObject.setLabel(term.getText());
		newObject.setLang(lang.getValue(lang.getSelectedIndex()));
		newObject.setStatus(status.getStatus());
		newObject.setStatusID(status.getId());
		newObject.setMainLabel(main.getValue());

		if(isLanguageExist==false && !MainApp.userSelectedLanguage.contains(newObject.getLang().toLowerCase()) )
		{
			isLanguageExist = true;
		}

		AsyncCallback<ConceptTermObject>callback = new AsyncCallback<ConceptTermObject>()
		{
			public void onSuccess(ConceptTermObject results)
			{
				if(results==null)
				{
					Window.alert(constants.conceptAddTermFailDuplicate());
					showWidget();
				}
				else
				{
					if(doLoop){
						doLoop = false;
						resetDialog();
						showWidget();
						//show();
					}
					else{
						hide();
					}
					handlerManager.fireEvent(new AddNewTermSuccessEvent(results));
				}
				ModuleManager.resetValidation();
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.conceptAddTermFail());
			}
		};
		Service.conceptService.addTerm(MainApp.userOntology,actionId, status, MainApp.userId, newObject, conceptObject, ConfigConstants.CODETYPE, callback);
	}

	public void addAddNewTermSuccessHandler(AddNewTermSuccessHandler handler) {
		handlerManager.addHandler(AddNewTermSuccessEvent.getType(),handler);
	}

	static class AddNewTermSuccessEvent extends GwtEvent<AddNewTermSuccessHandler> {
		private ConceptTermObject results;
		private static final Type<AddNewTermSuccessHandler> TYPE = new Type<AddNewTermSuccessHandler>();
		public AddNewTermSuccessEvent(ConceptTermObject results) {
			this.results = results;
		}
		public static Type<AddNewTermSuccessHandler> getType() {
			return TYPE;
		}
		@Override
		protected void dispatch(AddNewTermSuccessHandler handler) {
			handler.onAddNewTermSuccess(results);
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<AddNewTermSuccessHandler> getAssociatedType() {
			return TYPE;
		}
	}

	// Button clicked event handler
	public interface AddNewTermSuccessHandler extends EventHandler {
		void onAddNewTermSuccess(ConceptTermObject results);
	}
	
	public void showLoading()
	{
		panel.clear();
		panel.add(loadingPanel);
		panel.setSpacing(0);
		panel.setWidth("100%");
	}
	
	public void showWidget()
	{
		panel.clear();
		panel.add(mainPanel);
		panel.setSpacing(0);
		panel.setWidth("100%");
	}

}
