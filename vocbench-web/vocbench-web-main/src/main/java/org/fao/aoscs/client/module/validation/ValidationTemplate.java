package org.fao.aoscs.client.module.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.constant.OWLActionConstants;
import org.fao.aoscs.client.module.constant.OWLStatusConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.widgetlib.ValidationCellTable;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.filter.FilterValidation;
import org.fao.aoscs.domain.OwlAction;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.Validation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ValidationTemplate extends Composite{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	public VerticalPanel validatorPanel = new VerticalPanel();
	public VerticalPanel tablePanel = new VerticalPanel();
	public HorizontalPanel titlePanel   = new HorizontalPanel();
	public HorizontalPanel validationFooterPanel = new HorizontalPanel();
	public FilterValidation valFilter = null;

	private ArrayList<OwlStatus> statusList = new ArrayList<OwlStatus>();
	private ArrayList<Users> userList = new ArrayList<Users>();
	private ArrayList<OwlAction> actionList = new ArrayList<OwlAction>();
	private HashMap<Integer, Integer> acceptvalidationList = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> rejectvalidationList = new HashMap<Integer, Integer>();
	public ValidationCellTable vTable = null;

	public ValidationTemplate(){

		HorizontalPanel dPanel = new HorizontalPanel();
		dPanel.add(validatorPanel);
		dPanel.setStyleName("borderbar");

		VerticalPanel tempPanel = new VerticalPanel();
		tempPanel.setSize("100%", "100%");
		tempPanel.add(dPanel);
		tempPanel.setCellWidth(dPanel, "100%");
		tempPanel.setCellHeight(dPanel, "100%");
		tempPanel.setCellHorizontalAlignment(dPanel, HasHorizontalAlignment.ALIGN_CENTER);
		tempPanel.setCellVerticalAlignment(dPanel, HasVerticalAlignment.ALIGN_MIDDLE);


		initWidget(tempPanel);
	}

	public static void loadUser(String userID){
		Iterator<?> it = RootPanel.get().iterator();
		while(it.hasNext()){
			Object o = it.next();
			if(o instanceof MainApp)
			{
				MainApp m = (MainApp) o;
				m.loadUser(userID);
			}

		}
	}

	public void initLoadingTable(){
		int height = tablePanel.getOffsetHeight();
		int width = tablePanel.getOffsetWidth();
		LoadingDialog sayLoading = new LoadingDialog();
		tablePanel.clear();
		tablePanel.setSize(width+"px", height+"px");
		tablePanel.add(sayLoading);
	}

	public void init(int object){}
	public void initTable(int object){}
	public void update(HashMap<Validation, String> updateValue){
		initLoadingTable();
		Service.validationService.updateValidateQueue(updateValue, MainApp.vFilter, messages.mailValidationSubjectPrefix(constants.mainPageTitle()), messages.mailValidationBodyPrefix(),messages.mailValidationBodySuffix(ConfigConstants.EMAIL_FROM, constants.mainPageTitle()), new AsyncCallback<Integer>() {
			public void onSuccess(Integer size) {
				try
				{
					ModuleManager.resetConcept();
					ModuleManager.resetClassification();
					//ConceptModule.reloadRecentChanges();
				}
				catch(Exception e)
				{}
				initTable(size);
			}
			public void onFailure(Throwable caught) {
				//ExceptionManager.showException(caught, ""); 
				initTable(0);
			}
		});
	}
	public void reLoad()
	{
		initLoadingTable();
		Service.validationService.getValidatesize(MainApp.vFilter, new AsyncCallback<Integer>() {
			public void onSuccess(Integer result) {
				int size = 0;
				try
				{
					size = (Integer) result;
				}
				catch(Exception e)
				{}
				initTable(size);
			}

			public void onFailure(Throwable caught) {
				initTable(0);
				ExceptionManager.showException(caught, constants.valLoadValDataSizeFail());
			}
		});
	}

	public int getSelectedIndex(ListBox lb, String selectedItem)
	{
		for(int i=0;i<lb.getItemCount();i++){
			if(selectedItem.equals(lb.getItemText(i))){
				return i;
			}
		}
		return -1;
	}

	public static String getStatusFromID(int id, ArrayList<OwlStatus> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlStatus os = (OwlStatus)list.get(i);
			if(id == os.getId()) value = os.getStatus();
		}
		return value;
	}
	
	public static int getIDFromStatus(String status, ArrayList<OwlStatus> list)
	{
		int value = -1;
		for(int i=0;i<list.size();i++){
			OwlStatus os = (OwlStatus)list.get(i);
			if(status == os.getStatus()) value = os.getId();
		}
		return value;
	}

	public static String getActionFromID(int id, ArrayList<OwlAction> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			OwlAction os = (OwlAction)list.get(i);
			if(id == os.getId())
				value = os.getAction() + ((os.getActionChild() != null && os.getActionChild().length() > 0) ? "-"+os.getActionChild() : "" );
		}
		return value;
	}

	public static String getUserNameFromID(int id, ArrayList<Users> list)
	{
		String value = "";
		for(int i=0;i<list.size();i++){
			Users u = (Users) list.get(i);
			if(id  == u.getUserId()) value = u.getUsername();
		}
		return value;
	}

	public void checkEnable(CheckBox accept, CheckBox reject, int rowstatus)
	{
		accept.setEnabled(false);
		reject.setEnabled(false);
		if(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1) && acceptvalidationList.containsKey(new Integer(rowstatus)))
			accept.setEnabled(true);
		if(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1) && rejectvalidationList.containsKey(new Integer(rowstatus)))
			reject.setEnabled(true);
	}

	/*public void changeStatus(HashMap<Integer, Integer> list, int row, int column, int oldselectedItem, ArrayList<OwlStatus> statusList)
	{
		if(list.containsKey(new Integer(oldselectedItem)))
		{
			//int newselectedItem = ((Integer)list.get(new Integer(oldselectedItem))).intValue();
			//vTable.getDataTable().setWidget(row, column, new HTML(""+getStatusFromID(newselectedItem, statusList)));
			//vTable.getDataTable().getCellFormatter().addStyleName(row, column, "validate-red");
		}
	}*/


	public void makeTitlePanel(){
		titlePanel.clear();
		titlePanel.setWidth("100%");
		titlePanel.addStyleName("maintopbar");

		Image reload = new Image("images/reload-grey.gif");
		reload.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reLoad();
			}
		});
		reload.setTitle(constants.valReload());
		reload.setStyleName(Style.Link);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		hp.add(reload);
		hp.setCellVerticalAlignment(reload, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellHorizontalAlignment(reload, HasHorizontalAlignment.ALIGN_LEFT);

		HorizontalPanel SHPanel = new HorizontalPanel();
		Image filterImg = new Image("images/filter-grey.gif");
		Label applyFilter = new Label(constants.valFilterApply());
		applyFilter.setStyleName(Style.Link);
		applyFilter.addStyleName(Style.colorBlack);
		applyFilter.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				valFilter.show();
				}
			});
		SHPanel.setSpacing(5);
		SHPanel.add(filterImg);
		SHPanel.add(applyFilter);

		HTML titleName = new HTML(constants.valTitle());
		titleName.setWordWrap(false);
		titleName.addStyleName("maintopbartitle");

		titlePanel.add(titleName);
		titlePanel.setCellWidth(titleName, "50px");
		titlePanel.add(hp);
		titlePanel.add(SHPanel);
		titlePanel.setCellVerticalAlignment(titleName, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(titleName, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
		titlePanel.setCellVerticalAlignment(SHPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setCellHorizontalAlignment(SHPanel, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public void makeValidationPanel()
	{
		validationFooterPanel.clear();
		DOM.setStyleAttribute(validationFooterPanel.getElement(), "background", "#ffffff url('images/bg_headergradient.png') repeat-x bottom left");
		validationFooterPanel.setSize("100%", "100%");

		final CheckBox acceptAll = new CheckBox(constants.valAcceptAll());
		final CheckBox rejectAll = new CheckBox(constants.valRejectAll());
		Button doneButton = new Button(constants.valValidate());

		doneButton.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1) || MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1));
		acceptAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1));
		rejectAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1));
		acceptAll.setWidth("100%");
		rejectAll.setWidth("100%");
		acceptAll.addStyleName(Style.colorBlack);
		rejectAll.addStyleName(Style.colorBlack);
		acceptAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Widget sender = (Widget) event.getSource();
				//Window.alert(">> "+((CheckBox)sender).getValue());
				if(((CheckBox)sender).getValue()){
					
					/*for(int i=0;i<vTable.getDataTable().getDisplayedItems().size();i++)
					{
						Element element = (Element) vTable.getDataTable().getRowElement(i);
						SelectElement inputElement = (SelectElement) element.getChild(9).getFirstChild().getFirstChild();
						Window.alert(""+inputElement);
						inputElement.setSelectedIndex(1);
					}*/
					
					
					for(Validation v :vTable.getDataTable().getVisibleItems())
					{
						
					if(acceptvalidationList.containsKey(new Integer(v.getStatus())))
						{
							int newselectedItem = ((Integer)acceptvalidationList.get(new Integer(v.getStatus()))).intValue();
							v.setStatusLabel(Validator.getStatusFromID(newselectedItem, statusList));
							v.setIsAccept(true);
							//vTable.getDataTable().addColumnStyleName(v.getStatusColumn(), "validate-red");
							//getDataTable().setWidget(row, column, new HTML(""+Validator.getStatusFromID(newselectedItem, statusList)));
							//getDataTable().getCellFormatter().addStyleName(row, column, "validate-red");
						}
					}
					vTable.getDataTable().redraw();
					
					/*for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						if(vTable.getDataTable().getRowFormatter().isVisible(j))
						{
							org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);

							if(acceptvalidationList.containsKey(new Integer(v.getStatus())))
							{
								changeStatus(acceptvalidationList, j, v.getStatusColumn(),v.getStatus() ,statusList);
								vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(0)).setValue(true);
								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(1)).setEnabled(false);
							}
						}
					}*/
					
					
					rejectAll.setEnabled(false);
				}
				else
				{
					
					for(Validation v :vTable.getDataTable().getVisibleItems())
					{
						v.setStatusLabel(Validator.getStatusFromID(v.getStatus(), statusList));
						v.setIsAccept(null);
						//vTable.getDataTable().removeColumnStyleName(v.getStatusColumn(), "validate-red");
					}
					vTable.getDataTable().redraw();
					
					/*for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
						vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(getStatusFromID(v.getStatus(), statusList)));
						vTable.getDataTable().getCellFormatter().removeStyleName(j, v.getStatusColumn(), "validate-red");
						((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(0)).setValue(false);
						checkEnable(((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(0)), ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(1)), v.getStatus());
					}*/
					
					rejectAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONREJECTED, -1) && true);
				}

			}
		});

		rejectAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Widget sender = (Widget) event.getSource();
				if(((CheckBox)sender).getValue()){
					
					
					for(Validation v :vTable.getDataTable().getVisibleItems())
					{
						if(rejectvalidationList.containsKey(new Integer(v.getStatus())))
						{
							v.setIsAccept(false);
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
							{
								if(v.getOldStatus()==0)
								{
									v.setStatusLabel(Validator.getStatusFromID(OWLStatusConstants.DELETED_ID, statusList));
									//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(OWLStatusConstants.DELETED_ID, statusList)));
								}
								else
								{
									v.setStatusLabel(Validator.getStatusFromID(v.getOldStatus(), statusList));
									//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(v.getOldStatus(), statusList)));
								}
								//vTable.getDataTable().addColumnStyleName(v.getStatusColumn(), "validate-red");
								//getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");	
							}
							else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
							{
								v.setStatusLabel(Validator.getStatusFromID(v.getOldStatus(), statusList));
								//vTable.getDataTable().addColumnStyleName(v.getStatusColumn(), "validate-red");
								//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(v.getOldStatus(), statusList)));
								//getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");
							}
							else
							{
								int newselectedItem = ((Integer)rejectvalidationList.get(new Integer(v.getStatus()))).intValue();
								v.setStatusLabel(Validator.getStatusFromID(newselectedItem, statusList));
								//vTable.getDataTable().addColumnStyleName(v.getStatusColumn(), "validate-red");
								//getDataTable().setWidget(row, v.getStatusColumn(), new HTML(""+Validator.getStatusFromID(newselectedItem, statusList)));
								//getDataTable().getCellFormatter().addStyleName(row, v.getStatusColumn(), "validate-red");
							}
							
						}
					}
					vTable.getDataTable().redraw();
					
					
					/*for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						if(vTable.getDataTable().getRowFormatter().isVisible(j))
						{
							org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
							if(rejectvalidationList.containsKey(new Integer(v.getStatus())))
							{
								if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
								{
										if(v.getOldStatus()==0)
											vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(OWLStatusConstants.DELETED_ID, statusList)));
										else
											vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(v.getOldStatus(), statusList)));
										vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
								}
								else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
								{
									vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(v.getOldStatus(), statusList)));
									vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
								}
								else
								{
									int newselectedItem = ((Integer)rejectvalidationList.get(new Integer(v.getStatus()))).intValue();
									vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(""+getStatusFromID(newselectedItem, statusList)));
									vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
									if(newselectedItem==OWLStatusConstants.DELETED_ID)
									{
										TextArea ta = (TextArea) vTable.getDataTable().getWidget(j, v.getNoteColumn());
										ta.setReadOnly(false);
										ta.setFocus(true);
									}
								}

								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(1)).setValue(true);
								((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(0)).setEnabled(false);
							}
						}
					}*/
					acceptAll.setEnabled(false);
				}
				else
				{
					/*for(int j=0;j<vTable.getDataTable().getRowCount();j++){
						org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
						TextArea ta = (TextArea) vTable.getDataTable().getWidget(j, v.getNoteColumn());
						ta.setReadOnly(true);
						ta.setText(v.getNote()==null?"":v.getNote());
						vTable.getDataTable().setWidget(j, v.getStatusColumn(), new HTML(getStatusFromID(v.getStatus(), statusList)));
						vTable.getDataTable().getCellFormatter().removeStyleName(j, v.getStatusColumn(), "validate-red");
						((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(1)).setValue(false);
						checkEnable(((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(0)), ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(1)), v.getStatus());
					}*/
					
					for(Validation v :vTable.getDataTable().getVisibleItems())
					{
						v.setStatusLabel(Validator.getStatusFromID(v.getStatus(), statusList));
						v.setIsAccept(null);
						//vTable.getDataTable().removeColumnStyleName(v.getStatusColumn(), "validate-red");
					}
					vTable.getDataTable().redraw();
					
					acceptAll.setEnabled(MainApp.permissionTable.contains(OWLActionConstants.VALIDATIONACCEPTED, -1) && true);
				}
			}
		});
		
		doneButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				HashMap<Validation, String> updateValue = new HashMap<Validation, String>();
				/*for(int j=0;j<vTable.getDataTable().getRowCount();j++){
					org.fao.aoscs.domain.Validation v = (org.fao.aoscs.domain.Validation)vTable.getPagingScrollTable().getRowValue(j);
					
					CheckBox accept = ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(0));
					CheckBox reject = ((CheckBox)((VerticalPanel)vTable.getDataTable().getWidget(j,vTable.getDataTable().getColumnCount()-2)).getWidget(1));
					
					vTable.getDataTable().getCellFormatter().addStyleName(j, v.getStatusColumn(), "validate-red");
					
					int oldselectedItem = v.getStatus();
					if(accept.getValue() && !reject.getValue())
					{
						if(acceptvalidationList.containsKey(new Integer(oldselectedItem)))
						{
							Integer newselectedItem = (Integer)acceptvalidationList.get(new Integer(oldselectedItem));
							v.setIsAccept(new Boolean(true));
							v.setStatus(newselectedItem.intValue());
							v.setStatusLabel(getStatusFromID(newselectedItem.intValue(), statusList));
							v.setNote(((TextArea)vTable.getDataTable().getWidget(j, v.getNoteColumn())).getText());
							v.setValidatorId(MainApp.userId);

							// check if status is validated, isValidated is set to false so that it's status can be changed to published
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
								v.setIsValidate(new Boolean(false));
							else
								v.setIsValidate(new Boolean(true));

							v.setDateModified(new Date());
							updateValue.put(v, getValidationMailBody(v, 
									vTable.getDataTable().getWidget(j,0).getElement().getInnerText(), 
									vTable.getDataTable().getWidget(j,1).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,2).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,3).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,4).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,5).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,8).getElement().getInnerText(),
									true));
						}
					}
					else if(reject.getValue() && !accept.getValue())
					{
						if(rejectvalidationList.containsKey(new Integer(oldselectedItem)))
						{
							int newselectedItem = ((Integer)rejectvalidationList.get(new Integer(oldselectedItem))).intValue();
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
							{
								if(v.getOldStatus()==0)
									newselectedItem = OWLStatusConstants.DELETED_ID;
								else
									newselectedItem = v.getOldStatus();
							}
							else if(v.getStatus()==OWLStatusConstants.PROPOSED_DEPRECATED_ID)
							{
								newselectedItem = v.getOldStatus();
							}

							v.setIsAccept(new Boolean(false));
							v.setStatus(newselectedItem);
							v.setDateModified(new Date());

							v.setStatusLabel(getStatusFromID(newselectedItem, statusList));
							v.setNote(((TextArea)vTable.getDataTable().getWidget(j, v.getNoteColumn())).getText());
							v.setValidatorId(MainApp.userId);

							// check if status is validated, isValidated is set to false so that it's status can be changed to published
							if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
								v.setIsValidate(new Boolean(false));
							else
								v.setIsValidate(new Boolean(true));
							updateValue.put(v, getValidationMailBody(v, 
									vTable.getDataTable().getWidget(j,0).getElement().getInnerText(), 
									vTable.getDataTable().getWidget(j,1).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,2).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,3).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,4).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,5).getElement().getInnerText(),
									vTable.getDataTable().getWidget(j,8).getElement().getInnerText(),
									false));
						}
					}
				}*/
				
				
				/*for(int i=0;i<vTable.getDataTable().getDisplayedItems().size();i++)
				{
					Element element = (Element) vTable.getDataTable().getRowElement(i);
					SelectElement inputElement = (SelectElement) element.getChild(9).getFirstChild().getFirstChild();
					Window.alert(""+inputElement);
					inputElement.setSelectedIndex(1);
					v*/
				
				
				for(Validation v :vTable.getDataTable().getVisibleItems())
				{
					//Window.alert(v.get+" :: " + vTable.getDataTable().getColumn(v.getNoteColumn()-1).getValue(v));
					String action = "" + vTable.getDataTable().getColumn(v.getNoteColumn()-1).getValue(v);
					
					if(!action.equals("") && (action.equals(constants.buttonAccept()) || action.equals(constants.buttonReject())))
					{
						if(action.equals(constants.buttonAccept()))
						{
							v.setIsAccept(new Boolean(true));
						}
						else if(action.equals(constants.buttonReject()))
						{
							v.setIsAccept(new Boolean(false));
						}
								
						v.setStatus(getIDFromStatus(v.getStatusLabel(), statusList));
						v.setNote(v.getNote());
						v.setValidatorId(MainApp.userId);
	
						// check if status is validated, isValidated is set to false so that it's status can be changed to published
						if(v.getStatus()==OWLStatusConstants.VALIDATED_ID)
							v.setIsValidate(new Boolean(false));
						else
							v.setIsValidate(new Boolean(true));
						
						//Window.alert(action+" :: "+v.getNote());
						/*Window.alert(action+" :: "+
								new HTML(vTable.getDataTable().getColumn(0).getValue(v).toString()).getText()+", "+
								new HTML(vTable.getDataTable().getColumn(1).getValue(v).toString()).getText()+", "+
								new HTML(vTable.getDataTable().getColumn(2).getValue(v).toString()).getText()+", "+
								vTable.getDataTable().getColumn(3).getValue(v).toString()+", "+
								vTable.getDataTable().getColumn(4).getValue(v).toString()+", "+
								vTable.getDataTable().getColumn(5).getValue(v).toString()+", "+
								v.getStatusLabel()+", "+
								v.getIsAccept());	*/	
						
						updateValue.put(v, getValidationMailBody(v, 
								new HTML(vTable.getDataTable().getColumn(0).getValue(v).toString()).getText(), 
								new HTML(vTable.getDataTable().getColumn(1).getValue(v).toString()).getText(),
								new HTML(vTable.getDataTable().getColumn(2).getValue(v).toString()).getText(),
								vTable.getDataTable().getColumn(3).getValue(v).toString(),
								vTable.getDataTable().getColumn(4).getValue(v).toString(),
								vTable.getDataTable().getColumn(5).getValue(v).toString(),
								v.getStatusLabel(),
								v.getIsAccept()));
					}
				}
				if(updateValue.size()>0)
				{
					update(updateValue);
					acceptAll.setValue(false);
					rejectAll.setValue(false);
					acceptAll.setEnabled(true);
					rejectAll.setEnabled(true);
				}
			}
		});

		HorizontalPanel hp = new HorizontalPanel();
		DOM.setStyleAttribute(hp.getElement(), "background", "#ffffff url('images/bg_headergradient.png') repeat-x bottom left");
		hp.setSpacing(5);
		hp.add(acceptAll);
		hp.add(rejectAll);
		hp.add(doneButton);
		hp.setCellHorizontalAlignment(acceptAll, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellHorizontalAlignment(rejectAll, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellHorizontalAlignment(doneButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setCellVerticalAlignment(acceptAll, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellVerticalAlignment(rejectAll, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setCellVerticalAlignment(doneButton, HasVerticalAlignment.ALIGN_MIDDLE);

		validationFooterPanel.add(hp);
		validationFooterPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public String getValidationMailBody(Validation v, String concept, String newval, String oldval, String action, String owner, String modifier, String status, boolean validate)
	{
		String body = "";
		body += "Status : "+(validate?"Accepted":"Rejected")+" \n";
		body += "Action : "+action+" \n";
		body += "Validated by : "+Validator.getUserNameFromID(MainApp.userId, userList)+" \n\n";
		
		body += "Concept/Term/Relationships/Users/Others : "+concept+" \n\n";
		if(newval!=null && !newval.equals("") && !newval.equals("null"))
			body += "New value : "+newval+" \n\n";
		if(oldval!=null && !oldval.equals("") && !oldval.equals("null"))
			body += "Old value : "+oldval+"\n\n";
		body += "Owner : "+owner+" \n";
		body += "Modifer : "+modifier+" \n\n";
		body += "Create date : "+DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss").format(v.getDateCreate())+" \n";
		body += "Modified date : "+DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss").format(v.getDateModified())+" \n\n";
		if(v.getNote()!=null && !v.getNote().equals("") && !v.getNote().equals("null"))
			body += "Comments : "+v.getNote()+" \n\n";
		body += "Version : "+constants.mainPageTitle()+" "+ConfigConstants.DISPLAYVERSION+" \n";
		body += "Link : " + GWT.getHostPageBaseURL() + "\n\n";
		return body;
	}



	public ArrayList<OwlStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(ArrayList<OwlStatus> statusList) {
		this.statusList = statusList;
	}

	public ArrayList<Users> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<Users> userList) {
		this.userList = userList;
	}

	public ArrayList<OwlAction> getActionList() {
		return actionList;
	}

	public void setActionList(ArrayList<OwlAction> actionList) {
		this.actionList = actionList;
	}

	public HashMap<Integer, Integer> getAcceptvalidationList() {
		return acceptvalidationList;
	}

	public void setAcceptvalidationList(HashMap<Integer, Integer> acceptvalidationList) {
		this.acceptvalidationList = acceptvalidationList;
	}

	public HashMap<Integer, Integer> getRejectvalidationList() {
		return rejectvalidationList;
	}

	public void setRejectvalidationList(HashMap<Integer, Integer> rejectvalidationList) {
		this.rejectvalidationList = rejectvalidationList;
	}

	public void addAcceptvalidationList(Integer name, Integer value)
	{
		this.acceptvalidationList.put(name, value);
	}

	public void addRejectvalidationList(Integer name, Integer value)
	{
		this.rejectvalidationList.put(name, value);
	}

}

