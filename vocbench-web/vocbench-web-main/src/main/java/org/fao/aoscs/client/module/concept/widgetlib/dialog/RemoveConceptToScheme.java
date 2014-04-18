/**
 * 
 */
package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTree;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.OntologyInfo;

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
public class RemoveConceptToScheme extends FormDialogBox {
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private OlistBox schemeList ;
	private String conceptURI;
	private VerticalPanel panel = new VerticalPanel();
	
	public RemoveConceptToScheme(){
		super(constants.buttonDelete(), constants.buttonCancel());
		this.setText(constants.conceptSchemeDelete());
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
	}
	
	public RemoveConceptToScheme(String scheme){
		super(constants.buttonDelete(), constants.buttonCancel());
		this.setText(constants.conceptSchemeDelete());
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
	}
	
	public void setConcept(String conceptURI, HashMap<String, String> list)
	{
		this.conceptURI = conceptURI;
		initLayout(list);
	}
	
	public void initLayout(HashMap<String, String> list) {
		schemeList.clear();
		schemeList.addItem("--Select--", "");
		for(String schemeName : list.keySet())
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
		removeScheme(schemeList.getValue((schemeList.getSelectedIndex())), conceptURI, MainApp.userOntology);
	}
	
	
	public static void removeScheme(final String scheme, final String conceptURI, final OntologyInfo ontoInfo)
	{
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if(result)
				{
					Window.alert(constants.conceptSchemeRemoveFail());
				}
				else
				{
					AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
						public void onSuccess(Boolean result) {
							if(result)
							{
								ConceptTree conceptTree = ModuleManager.getMainApp().getConcept().conceptTree;
								Window.alert(constants.conceptSchemeRemoved()+": "+scheme);
								if(scheme.equals(MainApp.schemeUri))
								{
									conceptTree.reload(conceptTree.getSelectedConceptObject().getParentURI());
								}
								else
								{
									conceptTree.reload(conceptURI);
								}
									
							}
						}
						public void onFailure(Throwable caught) {
							ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
						}
					};
					Service.conceptService.removeConceptFromScheme(MainApp.userOntology, conceptURI, scheme, callback);
				}
			}
			public void onFailure(Throwable caught) {
				ExceptionManager.showException(caught, constants.conceptSchemeSetSchemeFail());
			}
		};
		Service.conceptService.checkRemoveConceptFromScheme(MainApp.userOntology, conceptURI, scheme, callback);
	}
}
