package org.fao.aoscs.client.module.relationship;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.relationship.widgetlib.RelationshipTree;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.PermissionObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Relationship extends Composite {
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private HorizontalPanel panel = new HorizontalPanel();
	public RelationshipTree relationshipTree;
	
	public Relationship(final PermissionObject permissionTable){
		panel.setSize("100%", "100%");
		LoadingDialog load = new LoadingDialog();
		panel.add(load);
		panel.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
		
		AsyncCallback<InitializeRelationshipData> callback = new AsyncCallback<InitializeRelationshipData>(){
			public void onSuccess(InitializeRelationshipData initData){
				panel.clear();
				relationshipTree = new RelationshipTree(initData, permissionTable);
				panel.add(relationshipTree);
				panel.setCellWidth(relationshipTree, "100%");
				panel.setCellHeight(relationshipTree, "100%");
				panel.setSize("100%", "100%");
			}
			public void onFailure(Throwable caught){
				panel.clear();
				ExceptionManager.showException(caught, constants.relInitDataFail());
			}
		};
		Service.relationshipService.initData(MainApp.groupId, MainApp.userOntology, callback);
		initWidget(panel);
	}
	
	public void setDisplayLanguage(ArrayList<String> language){
		relationshipTree.setDisplayLanguage(language);
	}
	
}
