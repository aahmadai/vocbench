/**
 * 
 */
package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.CopyConceptToScheme.OnCopyConceptToSchemeReady;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.InitializeConceptData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author rajbhandari
 *
 */
public class AddConceptToScheme extends FormDialogBox {
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	
	private OlistBox schemeList ;
	private String conceptURI;
	private InitializeConceptData initData;
	private VerticalPanel panel = new VerticalPanel();
	
	public AddConceptToScheme(InitializeConceptData initData){
		super(constants.buttonAdd(), constants.buttonCancel());
		this.initData = initData;
		this.setText(constants.conceptSchemeAdd());
		setWidth("400px");
		
		panel.setSize("100%", "100%");
		LoadingDialog load = new LoadingDialog();
		panel.add(load);
		panel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		addWidget(panel);	
		schemeList = new OlistBox();
		schemeList.addItem("--Select--", "");
		schemeList.setWidth("100%");
		//this.initLayout();
	}
	
	public void setConcept(String conceptURI)
	{
		this.conceptURI = conceptURI;
		initLayout();
	}
	
	public void initLayout() {
		
		final AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
			public void onSuccess(HashMap<String, String> list) {
				schemeList.clear();
				schemeList.addItem("--Select--", "");
				
				List<String> keys = new ArrayList<String>(list.keySet());
				Collections.sort(keys);
				
				for(String schemeName : keys)
				{
					schemeList.addItem(schemeName, list.get(schemeName));
				}
				Grid table = new Grid(1,2);
				table.setWidget(0, 0,new HTML(constants.conceptSchemeScheme()));
				table.setWidget(0, 1, schemeList);
				table.setWidth("100%");
				table.getColumnFormatter().setWidth(1,"80%");
				
				panel.clear();
				panel.add(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptSchemeGetSchemeFail());
			}
		};
		Service.conceptService.getExcludedConceptSchemes(conceptURI, MainApp.userLanguage, MainApp.isExplicit, MainApp.userOntology, callback);
	};
	
	public boolean passCheckInput() {
		boolean pass = false;
		if(schemeList.getValue((schemeList.getSelectedIndex())).equals("--None--") || schemeList.getValue((schemeList.getSelectedIndex())).equals("")){
			pass = false;
		}else {
			pass = true;
		}
		return pass;
	}

	public void onSubmit() {
		final String scheme = schemeList.getValue((schemeList.getSelectedIndex()));
		
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if(result)
				{
					addConceptToScheme(scheme);
				}
				else
				{
					if(Window.confirm(messages.conceptSchemeNotAvailableWarning(scheme)))
					{
						CopyConceptToScheme copyConceptToScheme = new CopyConceptToScheme(conceptURI, scheme, initData);
						copyConceptToScheme.show();
						copyConceptToScheme.doCopyConceptToSchemeAction(new OnCopyConceptToSchemeReady() {
							public void doCopyConceptToSchemeAction() {
								addConceptToScheme(scheme);
							}
						});
					}
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
			}
		};
		Service.conceptService.checkConceptAddToScheme(MainApp.userOntology, conceptURI, scheme, callback);
	}
	
	private void addConceptToScheme(final String scheme)
	{
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if(result)
				{
					Window.alert(constants.conceptSchemeAdded()+": "+scheme);
					ModuleManager.getMainApp().reloadConceptTree();
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
			}
		};
		Service.conceptService.addConceptToScheme(MainApp.userOntology, conceptURI, scheme, callback);
	}
}