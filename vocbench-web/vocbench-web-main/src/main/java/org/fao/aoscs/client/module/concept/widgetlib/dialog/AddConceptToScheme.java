/**
 * 
 */
package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;

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
	private OlistBox schemeList ;
	private String conceptURI;
	private VerticalPanel panel = new VerticalPanel();
	
	public AddConceptToScheme(){
		super(constants.buttonAdd(), constants.buttonCancel());
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
		this.initLayout();
	}
	
	public void setConcept(String conceptURI)
	{
		this.conceptURI = conceptURI;
		initLayout();
	}
	
	public void initLayout() {
		
		final AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
			public void onSuccess(HashMap<String, String> list) {
				for(String schemeName : list.keySet())
				{
					final String scheme = list.get(schemeName);
					
					if(!scheme.equals(MainApp.schemeUri))
						schemeList.addItem(schemeName, scheme);
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
		Service.conceptService.getSchemes(MainApp.userOntology, callback);
		
		
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
					Window.alert(constants.conceptSchemeAdded()+": "+scheme);
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
			}
		};
		Service.conceptService.addConceptToScheme(MainApp.userOntology, conceptURI, scheme, callback);
	}
}