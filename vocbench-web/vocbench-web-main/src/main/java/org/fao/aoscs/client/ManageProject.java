/**
 * 
 */
package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.STServerInstances.SelectSTServerDialogBoxOpener;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.project.service.ProjectService.ProjectServiceUtil;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.HelpPanel;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.OntologyConfigurationManager;
import org.fao.aoscs.domain.OntologyConfigurationParameters;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.PluginConfiguration;
import org.fao.aoscs.domain.PluginConfigurationParameter;
import org.fao.aoscs.domain.StInstances;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class ManageProject extends FormDialogBox implements ClickHandler, SelectSTServerDialogBoxOpener{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private ListBox stURL;
	private Button stManage;
	private TextBox projectName;
	private TextArea projectDesc;
	private ListBox projectType;
	private TextBox baseURI;
	private ListBox tripleStore;
	private ListBox plugins;
	private OlistBox mode;
	private OlistBox pluginConfig;
	private OntologyInfo ontoInfo;
	private HashMap<String, OntologyConfigurationParameters> ontConfigurationParametersMap;
	
	private FlexTable topTable;
	private FlexTable tripleTable;
	private FlexTable middleTable;
	private FlexTable bottomTable;
	private FlexTable pluginTable;
	private FlexTable middlePluginsTable;
	private FlexTable bottomPluginsTable;
	
	private VerticalPanel mainPanel;
	
	private VerticalPanel topMainPanel = new VerticalPanel();
	private VerticalPanel tripleMainPanel = new VerticalPanel();
	private VerticalPanel pluginsMainPanel = new VerticalPanel();
	
	private VerticalPanel topPanel;
	
	private VerticalPanel triplePanel;
	private VerticalPanel middlePanel;
	private VerticalPanel bottomPanel;
	
	private VerticalPanel pluginsPanel;
	private VerticalPanel middlePluginsPanel;
	private VerticalPanel bottomPluginsPanel;

	public static int ADD = 0;
	public static int DELETE = 1;
	
	private int action = -1;
	
	private int widthTable = 700;  
	private int widthCol1 = 450;  
	private int widthCol2 = 275;
	private int widthCol3 = 25;
	private int widthCol4 = 25;
	private int widthWidget = 275;
	
	private ProjectDialogBoxOpener opener;
	
	private STServerInstances selectSTServer;

	private String userId;
	
	public interface ProjectDialogBoxOpener {
	    void projectDialogBoxSubmit(ArrayList<OntologyInfo> ontolist, String userId, int selectedOntologyId);
	}
		
	public ManageProject(int action, OntologyInfo ontoInfo, String userId){
		super();
		
		mainPanel = new VerticalPanel();
		topPanel = new VerticalPanel();
		
		triplePanel = new VerticalPanel();
		middlePanel = new VerticalPanel();
		bottomPanel = new VerticalPanel();
		
		pluginsPanel = new VerticalPanel();
		middlePluginsPanel = new VerticalPanel();
		bottomPluginsPanel = new VerticalPanel();
		
		triplePanel.setVisible(false);
		middlePanel.setVisible(false);
		bottomPanel.setVisible(false);
		
		pluginsPanel.setVisible(false);
		middlePluginsPanel.setVisible(false);
		bottomPluginsPanel.setVisible(false);
		
		this.userId = userId;
		this.ontoInfo = ontoInfo;
		this.action = action;
		this.setWidth(widthTable+"px");
		this.initLayout();
		
	}
	
	public void initLayout() {
		
		projectName = new TextBox();
		projectName.setWidth(widthWidget+"px");
		if(ontoInfo!=null && ontoInfo.getOntologyName()!=null)
			projectName.setText(ontoInfo.getOntologyName());
		
		projectDesc = new TextArea();
		projectDesc.setWidth(widthWidget+"px");
		projectDesc.setVisibleLines(2);
		
		projectType = new ListBox();
		projectType.setWidth(widthWidget+"px");
		projectType.addItem("SKOS-XL", "it.uniroma2.art.owlart.models.SKOSXLModel");

		stURL = new ListBox();
		stURL.setWidth("100%");
		stURL.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				connect();
			}
		});
		listSTServerInstances();
		
		baseURI = new TextBox();
		baseURI.setWidth(widthWidget+"px");
		
		tripleStore = new ListBox();
		tripleStore.setWidth(widthWidget+"px");
		triplePanel.add(tripleStore);
		
		plugins = new ListBox();
		plugins.setWidth(widthWidget+"px");
		pluginsPanel.add(plugins);
		
		stManage = new Button(constants.buttonManage());
		stManage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(selectSTServer == null || !selectSTServer.isLoaded )
					selectSTServer = new STServerInstances();
				selectSTServer.show(ManageProject.this); 
			}
		});
			
		HorizontalPanel stPanel = new HorizontalPanel();
		stPanel.setSize(widthWidget+"px", "100%");
		stPanel.add(stURL);
		stPanel.add(stManage);
		stPanel.setCellWidth(stURL, "100%");
		
		mode = new OlistBox();
		mode.setWidth(widthWidget+"px");
		
		pluginConfig = new OlistBox();
		pluginConfig.setWidth(widthWidget+"px");

		tripleStore.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				String val = tripleStore.getValue(tripleStore.getSelectedIndex());
				if(!val.equals(""))
				{
					mode.clear();
					mode.addItem(constants.buttonSelect(), "");
					getOntManagerParameters(val);
					middlePanel.setVisible(true);
					bottomTable.removeAllRows();
					bottomPanel.setVisible(false);
				}
				else
				{
					tripleStore.setSelectedIndex(0);
					mode.setSelectedIndex(-1);
					middlePanel.setVisible(false);
					bottomTable.removeAllRows();
					bottomPanel.setVisible(false);
				}
					
			}
		});
		
		mode.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				bottomTable.removeAllRows();
				OntologyConfigurationManager ontConfigurationManager = (OntologyConfigurationManager) mode.getObject(mode.getSelectedIndex());
				if(ontConfigurationManager!=null)
				{
					ontConfigurationParametersMap = new HashMap<String, OntologyConfigurationParameters>();
					for(int i=0;i<ontConfigurationManager.getParameters().size();i++)
					{
						OntologyConfigurationParameters ontConfigurationParameters = ontConfigurationManager.getParameters().get(i);
						ontConfigurationParametersMap.put(ontConfigurationParameters.getName(), ontConfigurationParameters);
						bottomTable.setWidget(i, 0, new HTML(ontConfigurationParameters.getName()+(ontConfigurationParameters.isRequired()?"*":"")));
						TextBox txtBox = new TextBox();
						txtBox.setName(ontConfigurationParameters.getName());
						txtBox.setWidth(widthWidget-widthCol3-10+"px");
						txtBox.setValue(ontConfigurationParameters.getValue());
						bottomTable.setWidget(i, 1, txtBox);
						bottomTable.setWidget(i, 2, new HelpPanel(ontConfigurationParameters.getDescription()));
						bottomTable.getCellFormatter().setHorizontalAlignment(i, 2, HasHorizontalAlignment.ALIGN_CENTER);
					}
					
					FlexTable ft = new FlexTable();
					ft.setWidget(0, 0, GridStyle.setTableConceptDetailStyleleft(bottomTable, "gslRow1", "gslCol1", "gslPanel1"));
					
					bottomPanel.clear();
					bottomPanel.add(GridStyle.setTableConceptDetailStyleleft(ft, "gslRow1", "gslCol1", "gslPanel1"));
					bottomPanel.setVisible(true);
				}
				else
				{
					bottomPanel.setVisible(false);
				}
			}
		});
		
		plugins.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				String val = plugins.getValue(plugins.getSelectedIndex());
				if(!val.equals(""))
				{
					pluginConfig.clear();
					pluginConfig.addItem(constants.buttonSelect(), "");
					getPluginConfigurations(val);
					middlePluginsPanel.setVisible(true);
					bottomPluginsTable.removeAllRows();
					bottomPluginsPanel.setVisible(false);
				}
				else
				{
					plugins.setSelectedIndex(0);
					pluginConfig.setSelectedIndex(-1);
					middlePluginsPanel.setVisible(false);
					bottomPluginsTable.removeAllRows();
					bottomPluginsPanel.setVisible(false);
				}
					
			}
		});
		
		pluginConfig.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				loadpluginConfigurationList();
			}
		});
		
		topTable = new FlexTable();
		topTable.setWidth(widthTable+"px");
		topTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		topTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		topTable.setWidget(0, 0, new HTML(constants.projectProjectName(), true));			
		topTable.setWidget(0, 1, projectName);
		
		tripleTable = new FlexTable();
		tripleTable.setWidth(widthTable+"px");
		tripleTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		tripleTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		tripleTable.setWidget(0, 0, new HTML(constants.projectTripleStore(), true));			
		tripleTable.setWidget(0, 1, tripleStore);
		triplePanel.add(GridStyle.setTableConceptDetailStyleleft(tripleTable, "gslRow1", "gslCol1", "gslPanel1"));
		
		middleTable = new FlexTable();
		middleTable.setWidth(widthTable+"px");
		middleTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		middleTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		middleTable.setWidget(0, 0, new HTML(constants.projectTripleMode(), true));			
		middleTable.setWidget(0, 1, mode);
		middlePanel.add(GridStyle.setTableConceptDetailStyleleft(middleTable, "gslRow1", "gslCol1", "gslPanel1"));
		
		bottomTable = new FlexTable();
		bottomTable.setWidth(widthTable-8+"px");
		bottomTable.getColumnFormatter().setWidth(0, widthCol1+3+"px");
		bottomTable.getColumnFormatter().setWidth(1, widthCol2-widthCol3-3+"px");
		bottomTable.getColumnFormatter().setWidth(2, widthCol3+"px");
		
		pluginTable = new FlexTable();
		pluginTable.setWidth(widthTable+"px");
		pluginTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		pluginTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		pluginTable.setWidget(0, 0, new HTML("<b>"+constants.projectURIGenerator()+"</b>", true));			
		pluginTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		pluginTable.setWidget(1, 0, new HTML(constants.projectPlugins(), true));			
		pluginTable.setWidget(1, 1, plugins);
		pluginsPanel.add(GridStyle.setTableConceptDetailStyleleft(pluginTable, "gslRow1", "gslCol1", "gslPanel1"));
		
		middlePluginsTable = new FlexTable();
		middlePluginsTable.setWidth(widthTable+"px");
		middlePluginsTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		middlePluginsTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		middlePluginsTable.setWidget(0, 0, new HTML(constants.projectPluginConfiguration(), true));			
		middlePluginsTable.setWidget(0, 1, pluginConfig);
		middlePluginsPanel.add(GridStyle.setTableConceptDetailStyleleft(middlePluginsTable, "gslRow1", "gslCol1", "gslPanel1"));
		
		bottomPluginsTable = new FlexTable();
		bottomPluginsTable.setWidth(widthTable-8+"px");
		bottomPluginsTable.getColumnFormatter().setWidth(0, widthCol1+5+"px");
		bottomPluginsTable.getColumnFormatter().setWidth(1, widthCol2-widthCol3-widthCol4-5+"px");
		bottomPluginsTable.getColumnFormatter().setWidth(2, widthCol3+"px");
		bottomPluginsTable.getColumnFormatter().setWidth(3, widthCol4+"px");
		
		String title = "";
		String buttonText = "";
		switch(action)
		{
			case 0:
				title = constants.projectAddProject();
				buttonText = constants.buttonAdd();
				topTable.setWidget(1, 0, new HTML(constants.projectProjectDesc(), true));			
				topTable.setWidget(1, 1, projectDesc);
				topTable.setWidget(2, 0, new HTML(constants.projectProjectType(), true));			
				topTable.setWidget(2, 1, projectType);
				topTable.setWidget(3, 0, new HTML(constants.projectBaseURI(), true));			
				topTable.setWidget(3, 1, baseURI);
				topTable.setWidget(4, 0, new HTML(constants.projectSTServerInstance(), true));			
				topTable.setWidget(4, 1, stPanel);
				break;
			case 1:
				title = constants.projectDeleteProject();
				buttonText = constants.buttonDelete();
				projectName.setReadOnly(true);
				break;
			default:
				break;
		}
		
		topPanel.add(GridStyle.setTableConceptDetailStyleleft(topTable, "gslRow1", "gslCol1", "gslPanel1"));
		
		topMainPanel.add(topPanel);
		
		tripleMainPanel.add(triplePanel);
		tripleMainPanel.add(middlePanel);
		tripleMainPanel.add(bottomPanel);
		
		pluginsMainPanel.add(pluginsPanel);
		pluginsMainPanel.add(middlePluginsPanel);
		pluginsMainPanel.add(bottomPluginsPanel);

		mainPanel.setSpacing(5);
		mainPanel.add(topMainPanel);
		mainPanel.add(tripleMainPanel);
		mainPanel.add(pluginsMainPanel);
		
		addWidget(mainPanel);
		
		this.setText(title);
		this.submit.setText(buttonText);
	}
	
	private HorizontalPanel getPluginsButton()
	{
		HorizontalPanel hp = new HorizontalPanel();
		Button addButton = new Button(constants.buttonAdd());
		Button restoreButton = new Button(constants.buttonRestore());
		
		
		final ManagePluginConfigurationParameter mpcp = new ManagePluginConfigurationParameter();
		mpcp.addSubmitClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event) {
				int rowCnt = bottomPluginsTable.getRowCount();
				bottomPluginsTable.setWidget(rowCnt, 0, new HTML(mpcp.getName().getValue()));
				TextBox txtBox = new TextBox();
				txtBox.setName(mpcp.getName().getValue());
				txtBox.setWidth(widthWidget-widthCol3-widthCol4-10+"px");
				bottomPluginsTable.setWidget(rowCnt, 1, txtBox);
				bottomPluginsTable.setWidget(rowCnt, 2, new HelpPanel(mpcp.getDescription().getValue()));
				bottomPluginsTable.setWidget(rowCnt, 3, getRemoveButton());
				bottomPluginsTable.getCellFormatter().setHorizontalAlignment(rowCnt, 2, HasHorizontalAlignment.ALIGN_CENTER);
				
				bottomPluginsPanel.remove(0);
				bottomPluginsPanel.insert(GridStyle.setTableConceptDetailStyleleft(bottomPluginsTable, "gslRow1", "gslCol1", "gslPanel1"), 0);
			
			}					
		});	
		
		
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mpcp.show();
			}
		});
		
		restoreButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadpluginConfigurationList();
			}
		});
		
		
		hp.add(addButton);
		hp.add(restoreButton);
		return hp;
	}
	
	private ImageAOS getRemoveButton()
	{
		ImageAOS deleteButton = new ImageAOS(constants.buttonDelete(), "images/delete-grey.gif", "images/delete-grey-disabled.gif", true, new ClickHandler() {
			public void onClick(ClickEvent event) 
			{
				FlexTable flexTable = (FlexTable) ((ImageAOS) event.getSource()).getParent();
				int rowIndex = flexTable.getCellForEvent(event).getRowIndex();
		        flexTable.removeRow(rowIndex);
			}
		});
		return deleteButton;
	}
	
	private void connect()
	{
		tripleStore.clear();
		mode.clear();
		bottomTable.removeAllRows();
		
		tripleMainPanel.setVisible(false);
		triplePanel.setVisible(false);
		middlePanel.setVisible(false);
		bottomPanel.setVisible(false);
		
		plugins.clear();
		pluginConfig.clear();
		bottomPluginsTable.removeAllRows();
		
		pluginsMainPanel.setVisible(false);
		pluginsPanel.setVisible(false);
		middlePluginsPanel.setVisible(false);
		bottomPluginsPanel.setVisible(false);
		
		
		if(!stURL.getValue(stURL.getSelectedIndex()).equals(""))
		{
			String errMsg = getPreCheckErrMsg();
			
			if(errMsg.equals(""))
			{
				ontoInfo = new OntologyInfo();
				ontoInfo.setOntologyName(projectName.getText());
				ontoInfo.setDbTableName(projectName.getText());
				ontoInfo.setOntologyDescription(projectDesc.getText());
				ontoInfo.setDbDriver(stURL.getValue(stURL.getSelectedIndex()));
				ontoInfo.setDbUrl("");
				ontoInfo.setDbUsername("");
				ontoInfo.setDbPassword("");
				
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					public void onSuccess(Boolean val) {
						if(val)
						{
							listTripleStores();
							listURIGenerator();
						}
						else
						{
							stURL.setSelectedIndex(0);
							Window.alert(constants.projectSTServiceFail());
						}
					}
					public void onFailure(Throwable caught) {
						stURL.setSelectedIndex(0);
						ExceptionManager.showException(caught, constants.projectSTServiceFail());
					}
				};
				ProjectServiceUtil.getInstance().isSTServerStarted(ontoInfo, callback);
			}
			else
			{
				stURL.setSelectedIndex(0);
				Window.alert(constants.conceptCompleteInfo()+errMsg);
			}
		}
	}
	
	private String getPreCheckErrMsg()
	{
		if(projectName.getText().equals(""))
		{
			return " - "+constants.projectProjectNameEmpty();
		}
		else if(projectDesc.getText().equals(""))
		{
			return " - "+constants.projectProjectDescEmpty();
		}
		else if(projectType.getValue(projectType.getSelectedIndex()).length()==0)
		{
			return " - "+constants.projectProjectTypeEmpty();
		}
		else if(!Convert.isValidURL(baseURI.getText()))
		{
			return " - "+constants.projectBaseURIInvalid();
		}
		else if(stURL.getValue(stURL.getSelectedIndex()).equals(""))
		{
			return " - "+constants.projectSTEmpty();
		}
		else if(!Convert.isValidURL(stURL.getValue(stURL.getSelectedIndex())))
		{
			return " - "+constants.projectSTInvalid();
		}
		return "";
	}
	
	private String getPostCheckErrMsg()
	{
		if(triplePanel.isVisible() && tripleStore.getValue(tripleStore.getSelectedIndex()).length()==0)
		{
			return " - "+constants.projectTripleStoreEmpty();
		}
		else if(middlePanel.isVisible() && mode.getValue(mode.getSelectedIndex()).length()==0)
		{
			return " - "+constants.projectTripleStoreModeEmpty();
		}
		else
		{
			boolean chk = false;
			for(int i=0;i<bottomTable.getRowCount();i++)
			{
				TextBox txtBox = (TextBox) bottomTable.getWidget(i, 1);
				if(ontConfigurationParametersMap!=null)
				{
					OntologyConfigurationParameters ontConfigurationParameters = ontConfigurationParametersMap.get(txtBox.getName());
					if(ontConfigurationParameters!=null  && ontConfigurationParameters.isRequired())
					{
						chk = (txtBox.getText().equals(""));
						if(chk)
							break;
					}
				}
				
			}
			
			if(chk)
				return " - "+constants.projectTripleStoreModeConfigurationParameterInvalid();
		}
		
		return "";
	}
	
	public void listSTServerInstances()
	{
		stURL.clear();
		stURL.addItem(constants.buttonSelect(), "");
		AsyncCallback<ArrayList<StInstances>> callback = new AsyncCallback<ArrayList<StInstances>>() {
			public void onSuccess(ArrayList<StInstances> result) {
				for(StInstances stIns : result)
				{
					stURL.addItem(stIns.getId().getStName(), "http://"+stIns.getId().getStDomain()+":"+stIns.getId().getStPort());
				}
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.projectSTServerLoadFail());
			}
		};
		Service.systemService.listSTServer(ontoInfo, callback);
	}
	
	public void listTripleStores()
	{
		tripleStore.clear();
		tripleStore.addItem(constants.buttonSelect(), "");
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>(){
			public void onSuccess(ArrayList<String> result){
				if(result!=null)
				{
					for(String str : result)
					{
						String label = str;
						int index = -1;
						if(str.length()>0)
							index = str.lastIndexOf(".");
						if(index!=-1 && str.length()>index+1)
							label = str.substring(index+1);
						tripleStore.addItem(label, str);
					}
					tripleMainPanel.setVisible(true);
					triplePanel.setVisible(true);
					tripleStore.setSelectedIndex(0);
					mode.setSelectedIndex(-1);
					middlePanel.setVisible(false);
					bottomTable.removeAllRows();
					bottomPanel.setVisible(false);
				}
				else
					Window.alert(constants.projectManageServiceFail());
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.projectManageServiceFail());
			}
		};
		Service.projectService.listTripleStores(ontoInfo, callback);
	}
	
	public void listURIGenerator()
	{
		plugins.clear();
		plugins.addItem(constants.buttonSelect(), "");
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>(){
			public void onSuccess(ArrayList<String> result){
				if(result!=null)
				{
					for(String str : result)
					{
						String label = str;
						int index = -1;
						if(str.length()>0)
							index = str.lastIndexOf(".");
						if(index!=-1 && str.length()>index+1)
							label = str.substring(index+1);
						plugins.addItem(label, str);
					}
					pluginsMainPanel.setVisible(true);
					pluginsPanel.setVisible(true);
					plugins.setSelectedIndex(0);
					pluginConfig.setSelectedIndex(-1);
					middlePluginsPanel.setVisible(false);
					bottomPluginsTable.removeAllRows();
					bottomPluginsPanel.setVisible(false);
				}
				else
					Window.alert(constants.projectManageServiceFail());
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.projectGetAvailablePluginsFail());
			}
		};
		Service.projectService.getAvailablePlugins(ontoInfo, "it.uniroma2.art.semanticturkey.plugin.extpts.URIGenerator", callback);
	}
	
	private void getOntManagerParameters(String ontMgrID)
	{
		AsyncCallback<ArrayList<OntologyConfigurationManager>> callback = new AsyncCallback<ArrayList<OntologyConfigurationManager>>(){
			public void onSuccess(ArrayList<OntologyConfigurationManager> result){
				for(OntologyConfigurationManager ontConfigurationManager : result)
				{
					mode.addItem(ontConfigurationManager.getShortName(), ontConfigurationManager);
				}
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.projectManageProjectFail());
			}
		};
		Service.projectService.getOntManagerParameters(ontoInfo, ontMgrID, callback);
	}
	
	private void getPluginConfigurations(String factoryID)
	{
		AsyncCallback<ArrayList<PluginConfiguration>> callback = new AsyncCallback<ArrayList<PluginConfiguration>>(){
			public void onSuccess(ArrayList<PluginConfiguration> result){
				for(PluginConfiguration pluginConfiguration : result)
				{
					pluginConfig.addItem(pluginConfiguration.getShortName(), pluginConfiguration);
				}
				
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.projectGetPluginConfigurationsFail());
			}
		};
		Service.projectService.getPluginConfigurations(ontoInfo, factoryID, callback);
	}

	private void loadpluginConfigurationList()
	{
		bottomPluginsTable.removeAllRows();
		PluginConfiguration pluginConfiguration = (PluginConfiguration) pluginConfig.getObject(pluginConfig.getSelectedIndex());
		if(pluginConfiguration!=null)
		{
			int i=0;
			for(PluginConfigurationParameter pluginConfigurationParameter : pluginConfiguration.getPar())
			{
				bottomPluginsTable.setWidget(i, 0, new HTML(pluginConfigurationParameter.getName()+(pluginConfigurationParameter.isRequired()?"*":"")));
				TextBox txtBox = new TextBox();
				txtBox.setName(pluginConfigurationParameter.getName());
				txtBox.setWidth(widthWidget-widthCol3-widthCol4-10+"px");
				txtBox.setValue(pluginConfigurationParameter.getValue());
				bottomPluginsTable.setWidget(i, 1, txtBox);
				bottomPluginsTable.setWidget(i, 2, new HelpPanel(pluginConfigurationParameter.getDescription()));
				bottomPluginsTable.setWidget(i, 3, getRemoveButton());
				bottomPluginsTable.getCellFormatter().setHorizontalAlignment(i, 2, HasHorizontalAlignment.ALIGN_CENTER);
				i++;
			}
			
			FlexTable ft = new FlexTable();
			ft.setWidget(0, 0, GridStyle.setTableConceptDetailStyleleft(bottomPluginsTable, "gslRow1", "gslCol1", "gslPanel1"));
			ft.setWidget(1, 0, getPluginsButton());
			
			bottomPluginsPanel.clear();
			bottomPluginsPanel.add(GridStyle.setTableConceptDetailStyleleft(ft, "gslRow1", "gslCol1", "gslPanel1"));
			bottomPluginsPanel.setVisible(true);
		}
		else
		{
			bottomPluginsPanel.setVisible(false);
		}
	}
	
	public boolean passCheckInput() {
		boolean pass = false;
		String errMsg = constants.conceptCompleteInfo();
		switch(action)
		{
			case 0:
				/*pass = (!projectName.getText().equals("") && 
						!projectDesc.getText().equals("") && 
						projectType.getValue(projectType.getSelectedIndex()).length()!=0 &&
						!stURL.getValue(stURL.getSelectedIndex()).equals("") && 
						Convert.isValidURL(baseURI.getText()) && Convert.isValidURL(stURL.getValue(stURL.getSelectedIndex())));
				
				if(triplePanel.isVisible())
					pass = pass &&	tripleStore.getValue(tripleStore.getSelectedIndex()).length()!=0;
				else
					pass = false;
				
				if(middlePanel.isVisible())
					pass = pass && mode.getValue(mode.getSelectedIndex()).length()!=0;
				else
					pass = false;
				
				boolean chk = true;
				for(int i=0;i<bottomTable.getRowCount();i++)
				{
					TextBox txtBox = (TextBox) bottomTable.getWidget(i, 1);
					if(ontConfigurationParametersMap!=null)
					{
						OntologyConfigurationParameters ontConfigurationParameters = ontConfigurationParametersMap.get(txtBox.getName());
						if(ontConfigurationParameters!=null  && ontConfigurationParameters.isRequired())
						{
							chk = (!txtBox.getText().equals(""));
							if(!chk)
								break;
						}
					}
					
				}
				
				pass = pass && chk;
				
				*/
				
				String tmpErrMsg = getPreCheckErrMsg();
				if(tmpErrMsg.equals(""))
				{
					String tmpErrMsg2 = getPostCheckErrMsg();
					if(tmpErrMsg2.equals(""))
					{
						pass = true;
					}
					else 
					{
						errMsg += tmpErrMsg2;
						break;
					}
				}
				else 
				{
					errMsg += tmpErrMsg;
					break;
				}
				
				break;
			case 1:
				pass = projectName.getText().length()!=0;
				break;
			default:
				break;
		}
		
		if(!pass)
			Window.alert(errMsg);
		return pass;
	}
	
	public void onClick(ClickEvent event) 
	{
		Widget sender = (Widget) event.getSource();
		if(sender.equals(submit))
		{
			if(passCheckInput())
			{
				if(passCheckHide())
					this.hide();
				submit.setEnabled(false);
				onSubmit();
			}
		}
		else if(sender.equals(cancel))
		{
			this.onCancel();			
		}
		onButtonClicked(sender);
	}
	
	public void show(ProjectDialogBoxOpener opener)
	{
		this.opener = opener;
		show();
		
	}
	
	public void onSubmit() {
		
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean result){
				if(result)
				{
					
					switch(action)
					{
						case 0:
							
							AsyncCallback<OntologyInfo> callback = new AsyncCallback<OntologyInfo>(){
								public void onSuccess(OntologyInfo ontoInfo){
									if(ontoInfo!=null)
									{
										final int selectedOntologyId = ontoInfo.getOntologyId();
										AsyncCallback<ArrayList<OntologyInfo>> callback2 = new AsyncCallback<ArrayList<OntologyInfo>>(){
											public void onSuccess(ArrayList<OntologyInfo> result){
												if(result.size()>0)
												{
													if(opener!=null)
													{
														opener.projectDialogBoxSubmit(result, userId, selectedOntologyId);
													}
												}
												else
													Window.alert(constants.projectManageProjectFail());
											}
											public void onFailure(Throwable caught){
												ExceptionManager.showException(caught, constants.projectManageProjectFail());
											}
										};
										Service.systemService.getOntology(userId, callback2);
									}
									else
										Window.alert(constants.projectManageProjectFail());
								}
								public void onFailure(Throwable caught){
									ExceptionManager.showException(caught, constants.projectManageProjectFail());
								}
							};
							Service.systemService.addOntology(userId, ontoInfo, callback);
							break;
						case 1:
							AsyncCallback<ArrayList<OntologyInfo>> callback1 = new AsyncCallback<ArrayList<OntologyInfo>>(){
								public void onSuccess(ArrayList<OntologyInfo> result){
									if(result.size()>0)
									{
										if(opener!=null)
										{
											opener.projectDialogBoxSubmit(result, userId, 0);
										}
									}
									else
										Window.alert(constants.projectManageProjectFail());
								}
								public void onFailure(Throwable caught){
									ExceptionManager.showException(caught, constants.projectManageProjectFail());
								}
							};
							Service.systemService.deleteOntology(userId, ontoInfo.getOntologyId(), callback1);
							break;
					}
				}
				else
					Window.alert(constants.projectManageProjectFail());
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.projectManageProjectFail());
			}
		};
		switch(action)
		{
			case 0:
				HashMap<String, String> cfgPars = new HashMap<String, String>();
				for(int i=0;i<bottomTable.getRowCount();i++)
				{
					TextBox txtBox = (TextBox) bottomTable.getWidget(i, 1);
					cfgPars.put(txtBox.getName(), txtBox.getText());
				}
				
				String uriGeneratorFactoryID = "";
				String uriGenConfigurationClass = "";
				
				if(plugins.getSelectedIndex()>0)
				{
					uriGeneratorFactoryID = plugins.getValue(plugins.getSelectedIndex());
				}
				if(pluginConfig.getSelectedIndex()>0)
				{
					PluginConfiguration pc = (PluginConfiguration) pluginConfig.getObject(pluginConfig.getSelectedIndex());
					if(pc!=null)
						uriGenConfigurationClass = pc.getType();
				}
				
				HashMap<String, String> pcfgPars = new HashMap<String, String>();
				for(int i=0;i<bottomPluginsTable.getRowCount();i++)
				{
					TextBox txtBox = (TextBox) bottomPluginsTable.getWidget(i, 1);
					pcfgPars.put(txtBox.getName(), txtBox.getText());
				}
				
				Service.projectService.createNewProject(ontoInfo, projectName.getText(), baseURI.getText(), tripleStore.getValue(tripleStore.getSelectedIndex()), 
						((OntologyConfigurationManager) mode.getObject(mode.getSelectedIndex())).getType(), projectType.getValue(projectType.getSelectedIndex()), cfgPars, 
						uriGeneratorFactoryID, uriGenConfigurationClass, pcfgPars, callback);
				break;
			case 1:
				Service.projectService.deleteProject(ontoInfo, projectName.getText(), callback);
				break;
			default:
				break;
		}
	}
	
	public void selectSTServerDialogBoxSubmit() {
		listSTServerInstances();
	}

}
