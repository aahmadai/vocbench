package org.fao.aoscs.client.module.concept;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTree;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Concept extends Composite{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private HorizontalPanel panel = new HorizontalPanel();
	public PermissionObject permissionTable = new PermissionObject();
	public ConceptTree conceptTree = null;
	
	public Concept(final PermissionObject permissionTable){
		initConcept(permissionTable, null, 0);
	}
	
	public Concept(final PermissionObject permissionTable,final String initURI, final int initTab){
		initConcept(permissionTable, initURI, initTab);
	}
	
	public void initConcept(final PermissionObject permissionTable, final String initURI, final int initTab)
	{
		this.permissionTable = permissionTable;
		panel.setSize("100%", "100%");
		LoadingDialog load = new LoadingDialog();
		panel.add(load);
		panel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(panel);		
		
		/*if(MainApp.schemeUri==null || MainApp.schemeUri.equals("") || MainApp.schemeUri.equals("null"))
		{
			panel.clear();
			HTML msg = new HTML(constants.conceptSchemeNotSelected());
			panel.add(msg);
			panel.setCellHorizontalAlignment(msg,HasHorizontalAlignment.ALIGN_CENTER);
			panel.setCellVerticalAlignment(msg, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		else*/
		{
			//if(MainApp.schemeUri==ModuleManager.NOSCHEME)
				//MainApp.schemeUri=null;
			AsyncCallback<InitializeConceptData> callback = new AsyncCallback<InitializeConceptData>()
			{
				public void onSuccess(InitializeConceptData initData)
				{				
					conceptTree = new ConceptTree(initURI, initData, permissionTable, initTab);
					panel.clear();
					panel.add(conceptTree);
				}
				public void onFailure(Throwable caught)
				{
					panel.clear();
					ExceptionManager.showException(caught, constants.conceptLoadFail());
				}
			};		
			Service.conceptService.initData(MainApp.groupId, MainApp.schemeUri, MainApp.userOntology, !MainApp.userPreference.isHideNonpreferred(), MainApp.userPreference.isHideDeprecated(), MainApp.userSelectedLanguage, callback);
		}
	}
	
	public void gotoItem(final String targetItem, final int initTab){
		conceptTree.gotoItem(targetItem, initTab);
	}
	
	public boolean isMainLabelChecked(){
		return conceptTree.showAlsoNonpreferredTerms.getValue();
	}
	
	public void setDisplayLanguage(ArrayList<String> language){
		conceptTree.setDisplayLanguage(language);
	}
	
	public void reload(){
		conceptTree.reload();
	}
	
	public void reload(String uri){
		conceptTree.reload(uri);
	}
}
