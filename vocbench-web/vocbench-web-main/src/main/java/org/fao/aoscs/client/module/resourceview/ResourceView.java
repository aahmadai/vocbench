package org.fao.aoscs.client.module.resourceview;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.dialog.ManageResourceURI;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.ImageAOS;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabelAOS;
import org.fao.aoscs.domain.ARTBNodeObject;
import org.fao.aoscs.domain.ARTLiteralObject;
import org.fao.aoscs.domain.ARTNodeObject;
import org.fao.aoscs.domain.ARTURIResourceObject;
import org.fao.aoscs.domain.PredicateObject;
import org.fao.aoscs.domain.PredicateObjects;
import org.fao.aoscs.domain.ResourceViewObject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResourceView extends Composite {

	private static ResourceViewUiBinder uiBinder = GWT
			.create(ResourceViewUiBinder.class);

	private ManageResourceURI manageResourceURI;

	interface ResourceViewUiBinder extends UiBinder<Widget, ResourceView> {
	}

	public ResourceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	@UiField TextBox txtResourceURL;
	@UiField Button btnRenameResourceURL;
	@UiField FlowPanel pnlResourceView;
	
	public void init(String resourceURI)
	{
		txtResourceURL.setText(resourceURI);
		pnlResourceView.add(new LoadingDialog());
		AsyncCallback<ResourceViewObject> callback = new AsyncCallback<ResourceViewObject>(){
			public void onSuccess(ResourceViewObject result){
				load(result);
			}
			public void onFailure(Throwable caught){
				pnlResourceView.clear();
				ExceptionManager.showException(caught, constants.conceptResourceViewFail());
			}
		};
		Service.resourceService.getResourceView(MainApp.userOntology, resourceURI, false/*MainApp.isExplicit*/, callback);
	}
	
	private void load(ResourceViewObject resourceViewObj)
	{
		txtResourceURL.setText(resourceViewObj.getResource().getNominalValue());
		pnlResourceView.clear();
		for(String key : resourceViewObj.getResourceList().keySet())
		{
			VerticalPanel vp = new VerticalPanel();
			vp.setSize("100%", "100%");
			vp.setSpacing(5);
			for(PredicateObjects predObjs : resourceViewObj.getResourceList().get(key))
			{
				PredicateObject predObj = predObjs.getPredicate();
				if(predObj!=null)
				{
					LinkLabelAOS lbl = new LinkLabelAOS("images/add-grey.gif", "images/add-grey-disabled.gif", predObj.getUri().getShow(), predObj.getUri().getShow(), true, new ClickHandler()
					{
						public void onClick(ClickEvent event) {
							Window.alert(constants.conNotAvailable());
						}
					});
					vp.add(lbl);
				}
				
				for(final ARTNodeObject rdfResource : predObjs.getObjects())
				{
					String value = "";
					if(rdfResource instanceof ARTURIResourceObject) {
						value = rdfResource.getShow();
					}else if(rdfResource instanceof ARTLiteralObject){
						ARTLiteralObject temp_rdfResource = (ARTLiteralObject) rdfResource;
						value = "\""+temp_rdfResource.getLabel()+"\"";
						if(temp_rdfResource.isTypedLiteral())
							value += "^^<"+temp_rdfResource.getDatatype()+">";
						else
							value += "@"+temp_rdfResource.getLang();
					} else if(rdfResource instanceof ARTBNodeObject){
						ARTBNodeObject temp_rdfResource = (ARTBNodeObject) rdfResource;
						value = temp_rdfResource.getId();
					}
					
					
					TextBox txtValue = new TextBox();
					txtValue.setWidth("100%");
					txtValue.setText(value);
					txtValue.setReadOnly(true);
					
					if(rdfResource.isResource())
					{
						DOM.setStyleAttribute(txtValue.getElement(), "cursor", "pointer");
						txtValue.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								ResourceViewer resourceViewTest = new ResourceViewer();
								resourceViewTest.show(rdfResource.getNominalValue());
							}
						});
					}
					
					ImageAOS btnRemove = new ImageAOS(constants.buttonDelete(), "images/edit-grey.gif", "images/edit-grey-disabled.gif", rdfResource.isExplicit(), new ClickHandler()
					{
						public void onClick(ClickEvent event) {
							Window.alert(constants.conNotAvailable());
						}
					});
					
					HorizontalPanel hp = new HorizontalPanel();
					hp.setSize("100%", "100%");
					hp.setSpacing(5);
					hp.add(txtValue);
					hp.add(btnRemove);
					hp.setCellWidth(txtValue, "100%");
					vp.add(hp);
				}
			}
			pnlResourceView.add(getDisclosurePanel(key, vp));
		}
		
	}
	
	private DisclosurePanel getDisclosurePanel(String label, Widget content)
	{
		DisclosurePanel resDisclosurePanel = new DisclosurePanel(label.toUpperCase());
		resDisclosurePanel.setWidth("100%");
		resDisclosurePanel.setOpen(true);
		resDisclosurePanel.setAnimationEnabled(true);
		resDisclosurePanel.ensureDebugId("cwDisclosurePanel");
		resDisclosurePanel.setContent(content);
		return resDisclosurePanel;
	}

	@UiHandler("btnRenameResourceURL")
	void onClick(ClickEvent e) {
		if(manageResourceURI == null || !manageResourceURI.isLoaded )
			manageResourceURI = new ManageResourceURI();
		manageResourceURI.show(txtResourceURL.getText());
	}

}
