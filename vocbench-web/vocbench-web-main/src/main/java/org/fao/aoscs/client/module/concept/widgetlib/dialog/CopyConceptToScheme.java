package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConceptActionKey;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.ConceptBrowser;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.LabelAOS;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OwlStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CopyConceptToScheme extends FormDialogBox{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private Image browse ;
	private LabelAOS destConcept;
	private CheckBox rootConceptChb;
	private String conceptURI;
	private String schemeURI;
	private InitializeConceptData initData;
	
	public CopyConceptToScheme(String conceptURI, String schemeURI, InitializeConceptData initData){
		super();
		this.conceptURI = conceptURI;
		this.schemeURI = schemeURI;
		this.initData = initData;
		this.setText(constants.conceptCopy());
		setWidth("400px");
		this.initLayout();
	}
	
	public void onButtonClicked(Widget sender) {
		if(sender.equals(browse)){
			
			final ConceptBrowser cb = new ConceptBrowser();
			cb.showBrowser(schemeURI);
			cb.addSubmitClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event) {
					destConcept.setValue(cb.getSelectedItem(),cb.getTreeObject().getUri());
				}					
			});		
		}
	}
	public void initLayout() {
		
		destConcept = new LabelAOS("--None--", null);

		browse = new Image("images/browseButton3-grey.gif");
		browse.addClickHandler(this);
		browse.setStyleName(Style.Link);

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(destConcept);
		hp.add(browse);
		hp.setWidth("100%");
		hp.setCellHorizontalAlignment(destConcept, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(browse, HasHorizontalAlignment.ALIGN_RIGHT);
		
		rootConceptChb = new CheckBox(constants.conceptSetRoot());
		rootConceptChb.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destConcept.setValue("--None--",null);
			}
		});
		
		Grid table = new Grid(2,2);
		table.setWidget(0, 0,new HTML(constants.conceptNewParent()));
		table.setWidget(0, 1, hp);
		table.setWidget(1, 1,rootConceptChb);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(1,"80%");
		
		addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
	};
	
	public boolean passCheckInput() {
		boolean pass = false;
		if(destConcept.getValue()==null)
		{
			if(rootConceptChb.getValue()){
				pass = true;
			}else{
				pass = false;
			}
		}
		else
		{
			String dObj = (String)destConcept.getValue();
			if(dObj==null)
			{
				pass = false;
			}
			else
			{
				if(dObj.equals(""))
				{
					pass = false;
				}
				else
				{
					if( (((String)dObj).length()==0))
					{
						pass = false;
					}
					else 
					{
						pass = true;
					}
						
				}
			}
		}
		return pass;
	}

	public void onSubmit() {
		
		String parentConceptURI;
		if(rootConceptChb.getValue()){
			parentConceptURI = null;
		}else{
			parentConceptURI = (String)destConcept.getValue();	
		}
		
		if(conceptURI.equals(parentConceptURI))
		{
			Window.alert(constants.conceptSameSourceDestinationFail());
		}
		else
		{
			AsyncCallback<Void> callback = new AsyncCallback<Void>(){
				public void onSuccess(Void results){
					if(onCopyConceptToSchemeReady!=null)
					{
						onCopyConceptToSchemeReady.doCopyConceptToSchemeAction();
					}
				}
				public void onFailure(Throwable caught){
					ExceptionManager.showException(caught, constants.conceptCopyFail());
				}
			};
			
			//OwlStatus status = null;
			//int actionId = -1;	
			OwlStatus status = (OwlStatus)initData.getActionStatus().get(ConceptActionKey.conceptEditLinkConcept);
			int actionId = Integer.parseInt((String)initData.getActionMap().get(ConceptActionKey.conceptEditLinkConcept));
			
			Service.conceptService.copyConcept(MainApp.userOntology, MainApp.schemeUri, schemeURI, conceptURI, parentConceptURI, status, actionId, MainApp.userId, callback);
		}
	}
	
	public OnCopyConceptToSchemeReady onCopyConceptToSchemeReady;
	
	public interface OnCopyConceptToSchemeReady{
		public void doCopyConceptToSchemeAction();
	}
	
	public void doCopyConceptToSchemeAction(OnCopyConceptToSchemeReady onCopyConceptToSchemeReady2){	
		this.onCopyConceptToSchemeReady = onCopyConceptToSchemeReady2;
	 }
	
	
}