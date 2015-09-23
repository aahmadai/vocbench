package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptAlignment;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddNewEnglishDefinition.AddNewEnglishDefinitionSuccessEvent;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.AddNewEnglishDefinition.AddNewEnglishDefinitionSuccessHandler;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptAlignmentBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.NonFuncObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class AddConceptAlignment extends FormDialogBox implements ClickHandler{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private TextBox value;
	private OlistBox relationship;
	private Image browse;
	private String imgPath = "images/browseButton3-grey.gif";
	private String conceptURI = "";

	public AddConceptAlignment(String conceptURI){
		super(constants.buttonCreate(), constants.buttonCancel());
		String label = constants.conceptAddAlignment();
		this.setText(label);
		this.conceptURI = conceptURI;
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
				cb.showBrowser(conceptURI);
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
	
	/*public void onSubmit() {
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
	}*/
}
