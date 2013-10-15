/**
 * 
 */
package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.HelpPanel;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.OntologyConfigurationManager;
import org.fao.aoscs.domain.OntologyConfigurationParameters;
import org.fao.aoscs.domain.OntologyInfo;

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

/**
 * @author rajbhandari
 *
 */
public class ManageProject extends FormDialogBox implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private TextBox stURL;
	private Button stConnect;
	private TextBox projectName;
	private TextArea projectDesc;
	private ListBox projectType;
	private TextBox baseURI;
	private ListBox tripleStore;
	private OlistBox mode;
	private OntologyInfo ontoInfo;
	private HashMap<String, OntologyConfigurationParameters> ontConfigurationParametersMap;
	
	private FlexTable topTable;
	private FlexTable tripleTable;
	private FlexTable middleTable;
	private FlexTable bottomTable;
	
	private VerticalPanel mainPanel;
	private VerticalPanel topPanel;
	private VerticalPanel triplePanel;
	private VerticalPanel middlePanel;
	private VerticalPanel bottomPanel;

	public static int ADD = 0;
	public static int DELETE = 1;
	
	private int action = -1;
	
	private int widthTable = 400;  
	private int widthCol1 = 200;  
	private int widthCol2 = 225;
	private int widthCol3 = 25;
	private int widthWidget = 225;
	
	private ProjectDialogBoxOpener opener;

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
		
		triplePanel.setVisible(false);
		middlePanel.setVisible(false);
		bottomPanel.setVisible(false);
		
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
		projectType.addItem("SKOS-XL", "SKOS-XL");

		stURL = new TextBox();
		stURL.setWidth("100%");
		
		baseURI = new TextBox();
		baseURI.setWidth(widthWidget+"px");
		
		tripleStore = new ListBox();
		tripleStore.setWidth(widthWidget+"px");
		triplePanel.add(tripleStore);
		
		stConnect = new Button(constants.buttonConnect());
		stConnect.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				
				if(!projectName.getText().equals("") && !projectDesc.getText().equals("") && projectType.getValue(projectType.getSelectedIndex()).length()!=0 && !stURL.getText().equals("") && Convert.isValidURL(baseURI.getText()) && Convert.isValidURL(stURL.getText()))
				{
					ontoInfo = new OntologyInfo();
					ontoInfo.setOntologyName(projectName.getText());
					ontoInfo.setDbTableName(projectName.getText());
					ontoInfo.setOntologyDescription(projectDesc.getText());
					ontoInfo.setDbDriver(stURL.getText());
					ontoInfo.setDbUrl("");
					ontoInfo.setDbUsername("");
					ontoInfo.setDbPassword("");
					listTripleStores();
					
				}
				else
					Window.alert(constants.conceptCompleteInfo());
			}
		});
		
		HorizontalPanel stPanel = new HorizontalPanel();
		stPanel.setSize(widthWidget+"px", "100%");
		stPanel.add(stURL);
		stPanel.add(stConnect);
		stPanel.setCellWidth(stURL, "100%");
		
		mode = new OlistBox();
		mode.setWidth(widthWidget+"px");

		tripleStore.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				String val = tripleStore.getValue(tripleStore.getSelectedIndex());
				if(!val.equals(""))
				{
					mode.clear();
					mode.addItem(constants.buttonSelect(), "");
					getOntManagerParameters(val);
					middlePanel.setVisible(true);
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
						bottomTable.setWidget(i, 0, new HTML(ontConfigurationParameters.getName()));
						TextBox txtBox = new TextBox();
						txtBox.setName(ontConfigurationParameters.getName());
						txtBox.setWidth(widthWidget-widthCol3-5+"px");
						txtBox.setValue(ontConfigurationParameters.getValue());
						bottomTable.setWidget(i, 1, txtBox);
						bottomTable.setWidget(i, 2, new HelpPanel(ontConfigurationParameters.getDescription()));
						bottomTable.getCellFormatter().setHorizontalAlignment(i, 2, HasHorizontalAlignment.ALIGN_CENTER);
					}
					bottomPanel.clear();
					bottomPanel.add(GridStyle.setTableConceptDetailStyleleft(bottomTable, "gslRow1", "gslCol1", "gslPanel1"));
					bottomPanel.setVisible(true);
				}
				else
				{
					bottomPanel.setVisible(false);
				}
			}
		});
		
		topTable = new FlexTable();
		topTable.setWidth(widthTable+"px");
		topTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		topTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		topTable.setWidget(0, 0, new HTML(constants.projectProjectName()));			
		topTable.setWidget(0, 1, projectName);
		
		
		tripleTable = new FlexTable();
		tripleTable.setWidth(widthTable+"px");
		tripleTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		tripleTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		tripleTable.setWidget(0, 0, new HTML(constants.projectTripleStore()));			
		tripleTable.setWidget(0, 1, tripleStore);
		triplePanel.add(GridStyle.setTableConceptDetailStyleleft(tripleTable, "gslRow1", "gslCol1", "gslPanel1"));
		
		middleTable = new FlexTable();
		middleTable.setWidth(widthTable+"px");
		middleTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		middleTable.getColumnFormatter().setWidth(1, widthCol2+"px");
		middleTable.setWidget(0, 0, new HTML(constants.projectTripleMode()));			
		middleTable.setWidget(0, 1, mode);
		middlePanel.add(GridStyle.setTableConceptDetailStyleleft(middleTable, "gslRow1", "gslCol1", "gslPanel1"));
		
		bottomTable = new FlexTable();
		bottomTable.setWidth(widthTable+"px");
		bottomTable.getColumnFormatter().setWidth(0, widthCol1+"px");
		bottomTable.getColumnFormatter().setWidth(1, widthCol2-widthCol3+"px");
		bottomTable.getColumnFormatter().setWidth(2, widthCol3+"px");
		
		String title = "";
		String buttonText = "";
		switch(action)
		{
			case 0:
				title = constants.projectAddProject();
				buttonText = constants.buttonAdd();
				topTable.setWidget(1, 0, new HTML(constants.projectProjectDesc()));			
				topTable.setWidget(1, 1, projectDesc);
				topTable.setWidget(2, 0, new HTML(constants.projectProjectType()));			
				topTable.setWidget(2, 1, projectType);
				topTable.setWidget(3, 0, new HTML(constants.projectBaseURI()));			
				topTable.setWidget(3, 1, baseURI);
				topTable.setWidget(4, 0, new HTML(constants.projectSTServerURL()));			
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
		
		mainPanel.add(topPanel);
		mainPanel.add(triplePanel);
		mainPanel.add(middlePanel);
		mainPanel.add(bottomPanel);
		
		addWidget(mainPanel);
		
		this.setText(title);
		this.submit.setText(buttonText);
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
	
	public void getOntManagerParameters(String ontMgrID)
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

	
	public boolean passCheckInput() {
		boolean pass = false;
		switch(action)
		{
			case 0:
				pass = (!projectName.getText().equals("") && 
						!projectDesc.getText().equals("") && 
						projectType.getValue(projectType.getSelectedIndex()).length()!=0 &&
						!stURL.getText().equals("") && 
						Convert.isValidURL(baseURI.getText()) && Convert.isValidURL(stURL.getText()));
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
				break;
			case 1:
				pass = projectName.getText().length()!=0;
				break;
			default:
				break;
		}
		return pass;
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
				Service.projectService.createNewProject(ontoInfo, projectName.getText(), baseURI.getText(), tripleStore.getValue(tripleStore.getSelectedIndex()), ((OntologyConfigurationManager) mode.getObject(mode.getSelectedIndex())).getType(), projectType.getValue(projectType.getSelectedIndex()), cfgPars, callback);
				break;
			case 1:
				Service.projectService.deleteProject(ontoInfo, projectName.getText(), callback);
				break;
			default:
				break;
		}
	}
	
	
}
