package org.fao.aoscs.client.module.concept.widgetlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptAlignmentBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConceptAlignment extends ConceptTemplate{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private int cnt = 0;
	private AddValue addValue ;
	private DeleteValue deleteValue ;

	public ConceptAlignment(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable, initData, conceptDetailPanel, classificationDetailPanel);
	}

	private void attachNewImgButton(){

		functionPanel.clear();
		String label = constants.conceptAddAlignment();
		LinkLabelAOS add = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", label, label, true, new ClickHandler() {
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
		for(NonFuncObject value: values.keySet())
		{
			vp.add(getFuncButton(rObj, value, values.get(value)));
			cnt++;
		}
		return vp;
	}

	private HorizontalPanel getFuncButton(final String rObj, final NonFuncObject value, boolean isExplicit){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(3);
		
		LinkLabelAOS delete = new LinkLabelAOS("images/delete-grey.gif", "images/delete-grey-disabled.gif", constants.conceptDeleteAlignment(), true, new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(deleteValue == null || !deleteValue.isLoaded)
					deleteValue = new DeleteValue(rObj, value);
				deleteValue.show();
			}
		});
		hp.add(delete);

		String htmlText = "";
		if(value.getLanguage()!=null && !value.getLanguage().equals("") && !value.getLanguage().equals("null"))
		{
			htmlText = value.getValue()+" ("+value.getLanguage()+")";
		}
		else
		{
			htmlText = ""+value.getValue();
		}
		final HTML html = new HTML(htmlText);
		html.setWordWrap(true);
		html.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!html.getText().equals(""))
				MainApp.openURL(html.getText());
			}
		});
		if(!isExplicit)
		{
			html.addStyleName("link-label-aos-explicit");
			html.addStyleName("cursor-hand");
		}
		else
			html.addStyleName("link-label-blue");
		
		hp.add(html);
		return hp;
	}
	public void initLayout(){
		this.sayLoading();
		if(cDetailObj!=null && cDetailObj.getAlignmentObject()!=null)
		{
			initData(cDetailObj.getAlignmentObject());
		}
		else
			getData();
	}
	
	private void getData()
	{
		AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>()
		{
			public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> result) {
				cDetailObj.setAlignmentObject(result);
				initData(result);
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptAlignmentFail());
			}
		};
		Service.conceptService.getConceptAlignmentValue(conceptObject.getUri(), MainApp.isExplicit, MainApp.userOntology, callback);
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
		table.setWidget(0, 0, new HTML(constants.conceptAlignment()));
		table.setWidget(0, 1, new HTML(constants.conceptValue()));

		int i=0;
		if(!list.isEmpty()){
			for(ClassObject clsObj: getSortedList(list)){
				HashMap<NonFuncObject, Boolean> values = (HashMap<NonFuncObject, Boolean>) list.get(clsObj);
				table.setWidget(i+1, 0, new HTML(clsObj.getLabel()));
				table.setWidget(i+1, 1, getFuncButtons(clsObj.getUri(), values));
				i++;
			}

			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.ALIGNMENT.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptAlignments():constants.conceptAlignment())+"&nbsp;("+(cnt)+")");
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.ALIGNMENT.getTabIndex(), Convert.replaceSpace(cnt>1? constants.conceptAlignments():constants.conceptAlignment())+"&nbsp;("+(cnt)+")");
			conceptRootPanel.add(GridStyle.setTableConceptDetailStyleTop(table,"gstFR1","gstFC1","gstR1","gstPanel1",true));
		}else{
			if(conceptObject.getBelongsToModule()==ConceptObject.CONCEPTMODULE) conceptDetailPanel.tabPanel.getTabBar().setTabHTML(ConceptTab.ALIGNMENT.getTabIndex(), Convert.replaceSpace(constants.conceptAlignment())+"&nbsp;(0)");
			if(conceptObject.getBelongsToModule()==ConceptObject.CLASSIFICATIONMODULE) classificationDetailPanel.tab2Panel.getTabBar().setTabHTML(ConceptTab.ALIGNMENT.getTabIndex(), Convert.replaceSpace(constants.conceptAlignment())+"&nbsp;(0)");
			Label sayNo = new Label(constants.conceptNoAlignment());
			conceptRootPanel.add(sayNo);
			conceptRootPanel.setCellHorizontalAlignment(sayNo, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}

	public class DeleteValue extends FormDialogBox implements ClickHandler{
		String relURI = "";
		private NonFuncObject value;
		public DeleteValue(String relURI/*RelationshipObject rObj*/, NonFuncObject value){
			super(constants.buttonDelete(), constants.buttonCancel());
			this.relURI = relURI;
			this.value = value;
			this.setText(constants.conceptDeleteAlignment());
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
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>(){
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> results){
					cDetailObj.setAlignmentObject(results);
					ConceptAlignment.this.initData();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptDeleteValueFail());
				}
			};

			Service.conceptService.deleteConceptAlignmentValue(MainApp.userOntology, conceptObject.getUri(), relURI, value.getValue(), MainApp.isExplicit, callback);		}

	}

	public class AddValue extends FormDialogBox implements ClickHandler{
		private TextBox value;
		private OlistBox relationship;
		private Image browse;
		private String imgPath = "images/browseButton3-grey.gif";
		//private String relURI = "";

		public AddValue(){
			super(constants.buttonCreate(), constants.buttonCancel());
			String label = constants.conceptAddAlignment();
			this.setText(label);
			setWidth("400px");
			this.initLayout();
		}

		public void initLayout() {
			value = new TextBox();
			value.setWidth("100%");
			value.setTitle(constants.conceptSelectAlignment());

			relationship = new OlistBox();//Convert.makeOListBoxWithValue(propList);
			relationship.setWidth("100%");

			AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>(){
				public void onSuccess(HashMap<String, String> results){
					relationship.addItem("--None--", "");
					for(String uri : results.keySet())
					{
						relationship.addItem(results.get(uri), uri);
					}
				}
				public void onFailure(Throwable caught){
					Window.alert(constants.conceptAlignmentFail());
				}
			};
			Service.conceptService.getConceptAlignment(MainApp.userOntology, callback);
			
			browse = new Image(imgPath);
			browse.setStyleName(Style.Link);
			browse.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					final ConceptAlignmentBrowser cb =((MainApp) RootPanel.get().getWidget(0)).conceptAlignmentBrowser;
					cb.showBrowser(conceptObject.getUri());
					cb.addSubmitClickHandler(new ClickHandler()
					{
						public void onClick(ClickEvent event) {
							if(cb.getSelectedItem()!=null)
								value.setText(cb.getTreeObject().getUri());
						}					
					});						
				}
			});
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(value);
			hp.add(browse);
			hp.setSpacing(3);
			hp.setWidth("100%");
			hp.setCellWidth(value, "100%");
			hp.setCellHorizontalAlignment(value, HasHorizontalAlignment.ALIGN_LEFT);
			hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
			hp.setCellVerticalAlignment(value, HasVerticalAlignment.ALIGN_MIDDLE);
			hp.setCellVerticalAlignment(browse, HasVerticalAlignment.ALIGN_MIDDLE);

			final FlexTable table = new FlexTable();
			table.setWidth("100%");
			table.getColumnFormatter().setWidth(1, "80%");
			table.setWidget(0, 0, new HTML(constants.conceptValue()));
			table.setWidget(0, 1, hp);
			table.setWidget(1, 0, new HTML(constants.conceptProperty()));
			table.setWidget(1, 1, relationship);

			addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		}
		public boolean passCheckInput() {
			if(relationship.getValue(relationship.getSelectedIndex()).equals("--None--")	|| relationship.getValue(relationship.getSelectedIndex()).equals(""))
			{
				return false;
			}
			else
			{
				if(value.getText().length()==0)
				{
					return false;
				}
			}
			return true;
		}
		public void onSubmit() {
			sayLoading();
			String relURI = relationship.getValue(relationship.getSelectedIndex());
			AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>> callback = new AsyncCallback<HashMap<ClassObject, HashMap<NonFuncObject, Boolean>>>(){
				public void onSuccess(HashMap<ClassObject, HashMap<NonFuncObject, Boolean>> results){
					cDetailObj.setAlignmentObject(results);
					ConceptAlignment.this.initData();
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptAddValueFail());
				}
			};

			Service.conceptService.addConceptAlignmentValue(MainApp.userOntology, conceptObject.getUri(), relURI, value.getText(), MainApp.isExplicit, callback);
		}
	}
}
