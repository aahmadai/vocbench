package org.fao.aoscs.client.module.concept.widgetlib;

import org.fao.aoscs.client.module.classification.widgetlib.ClassificationDetailTab;
import org.fao.aoscs.client.module.concept.ConceptTemplate;
import org.fao.aoscs.client.module.resourceview.ResourceView;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.PermissionObject;

public class ConceptResourceView extends ConceptTemplate{
	
	public ConceptResourceView(PermissionObject permisstionTable,InitializeConceptData initData, ConceptDetailTabPanel conceptDetailPanel, ClassificationDetailTab classificationDetailPanel){
		super(permisstionTable,initData, conceptDetailPanel, classificationDetailPanel);
	}

	public void initLayout(){
		this.sayLoading();
		conceptRootPanel.setSpacing(3);
		functionPanel.setVisible(false);
		
		ResourceView resView = new ResourceView();
		
		clearPanel(); // clear loading message		
		resView.init(cDetailObj.getConceptObject().getUri());
		
		conceptRootPanel.setSize("100%", "100%");
		conceptRootPanel.add(resView);
		conceptRootPanel.setCellHeight(resView, "100%");						
		conceptRootPanel.setCellWidth(resView, "100%");
	}
	
}
