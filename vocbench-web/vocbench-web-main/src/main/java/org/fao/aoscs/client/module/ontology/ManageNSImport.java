/**
 * 
 */
package org.fao.aoscs.client.module.ontology;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.MultiUploader;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.misc.OlistBox;
import org.fao.aoscs.domain.OntologyMirror;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author rajbhandari
 *
 */
public class ManageNSImport extends FormDialogBox implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private ListBox listBox;
	private TextBox baseURI;
	private TextBox altURL;
	private FileUpload localFile;
	private TextBox mirrorFile;
	private OlistBox mirrorlistBox;
	private MultiUploader uploader;
	
	//private FormPanel form = new FormPanel();
	private FlowPanel modulePanel = new FlowPanel();
	private FlexTable addModulePanel = new FlexTable();
	private FlexTable modulePanel1 = new FlexTable();
	
	public static int ADDNSIMPORT = -1;
	public static int DELETENSIMPORT = 0;
	public static int ADDNSIMPORTFROMWEB = 1;
	public static int ADDNSIMPORTFROMLOCAL = 2;
	public static int ADDNSIMPORTFROMWEBTOMIRROR = 3;
	public static int ADDNSIMPORTFROMONTOLOGYMIRROR = 4;
	
	private int action = -1;
	
	
	private NSImportDialogBoxOpener opener = null;
	public interface NSImportDialogBoxOpener {
	    void nsImportDialogBoxSubmit();
	    void nsImportLoadingDialog();
	}
		
	public ManageNSImport(int action, String baseURI){
		super();
		this.action = action;
		this.setWidth("400px");
		this.setText(constants.ontologyImports());
		this.initLayout(baseURI);
	}
	
	public void initLayout(String baseURIVal) {
		
		
		baseURI = new TextBox();
		baseURI.setWidth("100%");
		baseURI.setText(baseURIVal);
		baseURI.setName("baseURI");
		
		altURL = new TextBox();
		altURL.setWidth("100%");
		
		mirrorFile = new TextBox();
		mirrorFile.setWidth("100%");
		mirrorFile.setName("mirrorFile");
		
		localFile = new FileUpload();
		localFile.setWidth("100%");
		localFile.setName("localFile");
		
		mirrorlistBox = new OlistBox();
		mirrorlistBox.setWidth("100%");
		
		
		uploader = new MultiUploader();
		uploader.setMaximumFiles(1);
		uploader.addOnFinishUploadHandler(new OnFinishUploaderHandler() {
			public void onFinish(IUploader uploader) {
		    	if (uploader.getStatus() == Status.SUCCESS) {
			        submit.setEnabled(true);
		    	}
		    }
		});
		
        listBox = new ListBox();
		listBox.setWidth("100%");
		listBox.addItem(constants.ontologyAddImportFromWeb(), ""+ADDNSIMPORTFROMWEB);
		listBox.addItem(constants.ontologyAddImportFromLocal(), ""+ADDNSIMPORTFROMLOCAL);
		listBox.addItem(constants.ontologyAddImportFromWebToMirror(), ""+ADDNSIMPORTFROMWEBTOMIRROR);
		listBox.addItem(constants.ontologyAddImportFromOntologyMirror(), ""+ADDNSIMPORTFROMONTOLOGYMIRROR);
		listBox.setSelectedIndex(0);
		listBox.addChangeHandler(new ChangeHandler() {
			
			public void onChange(ChangeEvent event) {
				modulePanel1.removeAllRows();
				switch(listBox.getSelectedIndex())
				{
					case 0:
						
						modulePanel1.setWidget(0, 0, new HTML(constants.ontologyBaseURI()));			
						modulePanel1.setWidget(0, 1, baseURI);
						
						modulePanel1.setWidget(1, 0, new HTML(constants.ontologyAlternativeURL()));
						modulePanel1.setWidget(1, 1, altURL);
						addModulePanel.setWidget(1, 0, GridStyle.setTableConceptDetailStyleleft(modulePanel1, "gslRow1", "gslCol1", "gslPanel1"));
						
						break;
					case 1:
						
						modulePanel1.setWidget(0, 0, new HTML(constants.ontologyBaseURI()));			
						modulePanel1.setWidget(0, 1, baseURI);
						
						modulePanel1.setWidget(1, 0, new HTML(constants.ontologyLocalFile()));
						//modulePanel1.setWidget(1, 1, localFile);
						modulePanel1.setWidget(1, 1, uploader);
						
						modulePanel1.setWidget(2, 0, new HTML(constants.ontologyMirrorFile()));
						modulePanel1.setWidget(2, 1, mirrorFile);
						
						//addModulePanel.setWidget(1, 0, getFormPanel(GridStyle.setTableConceptDetailStyleleft(modulePanel1, "gslRow1", "gslCol1", "gslPanel1")));
						addModulePanel.setWidget(1, 0, GridStyle.setTableConceptDetailStyleleft(modulePanel1, "gslRow1", "gslCol1", "gslPanel1"));
						
						submit.setEnabled(false);
						
						break;
						
					case 2:
						
						modulePanel1.setWidget(0, 0, new HTML(constants.ontologyBaseURI()));			
						modulePanel1.setWidget(0, 1, baseURI);
						
						modulePanel1.setWidget(1, 0, new HTML(constants.ontologyMirrorFile()));
						modulePanel1.setWidget(1, 1, mirrorFile);
						
						modulePanel1.setWidget(2, 0, new HTML(constants.ontologyAlternativeURL()));
						modulePanel1.setWidget(2, 1, altURL);
						
						addModulePanel.setWidget(1, 0, GridStyle.setTableConceptDetailStyleleft(modulePanel1, "gslRow1", "gslCol1", "gslPanel1"));
			    		
						break;
						
					case 3:
						
						modulePanel1.setWidget(0, 0, new HTML(constants.ontologyNamespace()));
						modulePanel1.setWidget(0, 1, mirrorlistBox);
						
						AsyncCallback<ArrayList<OntologyMirror>> callback = new AsyncCallback<ArrayList<OntologyMirror>>(){
							public void onSuccess(ArrayList<OntologyMirror> result){
								for(OntologyMirror ontMirror : result)
								{
									mirrorlistBox.addItem(ontMirror.getNamespace(), ontMirror);
								}
								mirrorlistBox.setSelectedIndex(0);
								addModulePanel.setWidget(1, 0, GridStyle.setTableConceptDetailStyleleft(modulePanel1, "gslRow1", "gslCol1", "gslPanel1"));
							}
							public void onFailure(Throwable caught){
								ExceptionManager.showException(caught, constants.ontologyImportsManageFail());
							}
						};
						Service.ontologyService.getOntologyMirror(MainApp.userOntology, callback);
						break;
						
					default:
						break;
				}
				
			}
			
		}); 
		
		modulePanel1.setSize("100%", "100%");
		modulePanel1.setCellPadding(3);
		modulePanel1.setCellSpacing(3);
		modulePanel1.getColumnFormatter().setWidth(1, "80%");
		
		modulePanel1.setWidget(0, 0, new HTML(constants.ontologyBaseURI()));			
		modulePanel1.setWidget(0, 1, baseURI);
		modulePanel1.setWidget(1, 0, new HTML("Alternative URL"));
		modulePanel1.setWidget(1, 1, altURL);
		
		addModulePanel.setWidget(1, 0, GridStyle.setTableConceptDetailStyleleft(modulePanel1, "gslRow1", "gslCol1", "gslPanel1"));
		
		
        if(action == ADDNSIMPORT)
		{
        	
        	addModulePanel.setSize("100%", "100%");
        	addModulePanel.setWidget(0, 0, listBox);
        	modulePanel.add(addModulePanel);
        	this.submit.setText(constants.buttonAdd());
		}
		else if(action == DELETENSIMPORT)
		{
			FlexTable delModulePanel = new FlexTable();
			delModulePanel.setSize("100%", "100%");
			delModulePanel.setWidget(0, 0, new HTML(constants.ontologyBaseURI()));			
			delModulePanel.setWidget(0, 1, baseURI);
			delModulePanel.getColumnFormatter().setWidth(1, "80%");

			modulePanel.add(GridStyle.setTableConceptDetailStyleleft(delModulePanel, "gslRow1", "gslCol1", "gslPanel1"));
			baseURI.setReadOnly(true);
			this.submit.setText(constants.buttonDelete());
		}
        addWidget(modulePanel);
		
	}
	
	public boolean passCheckInput() {
		if(action == ADDNSIMPORT)
		{
			switch(listBox.getSelectedIndex())
			{
				case 0:
					return (baseURI.getText().length()!=0);
				case 1:
					return (baseURI.getText().length()!=0 && mirrorFile.getText().length()!=0 && uploader.getServerInfo().message.length()!=0/*&& localFile.getFilename().length()!=0*/);
				case 2:
					return (baseURI.getText().length()!=0 && mirrorFile.getText().length()!=0);
				case 3:
					return (mirrorlistBox.getSelectedIndex()!=-1);
				default:
					return false;
			}
		}
		else if(action == DELETENSIMPORT)
			return (baseURI.getText().length()!=0);
		else
			return false;
	}
	
	public void show(NSImportDialogBoxOpener opener)
	{
		this.opener = opener;
		show();
	}
	
	public void onCancel()
	{
		if(uploader!=null)
			uploader.cancel();
		super.onCancel();
	}
	
	public void onSubmit() {
		if(opener!=null)
			opener.nsImportLoadingDialog();
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean result){
				if(!result)
					Window.alert(constants.ontologyImportsManageFail());
				if(uploader!=null)
					uploader.cancel();
				if(opener!=null)
					opener.nsImportDialogBoxSubmit();
			}
			public void onFailure(Throwable caught){
				if(uploader!=null)
					uploader.cancel();
				if(opener!=null)
					opener.nsImportDialogBoxSubmit();
				ExceptionManager.showException(caught, constants.ontologyImportsManageFail());
			}
		};
		if(action == ADDNSIMPORT)
		{
			switch(listBox.getSelectedIndex())
			{
				case 0:
					Service.ontologyService.addFromWeb(MainApp.userOntology, baseURI.getText(), altURL.getText(), callback);
					break;
				case 1:
					Service.ontologyService.addFromLocalFile(MainApp.userOntology, baseURI.getText(), uploader.getServerInfo().message, mirrorFile.getText(), callback);
					break;
				case 2:
					Service.ontologyService.addFromWebToMirror(MainApp.userOntology, baseURI.getText(), mirrorFile.getText(), altURL.getText(), callback);
					break;
				case 3:
					OntologyMirror ontMirror = (OntologyMirror) mirrorlistBox.getObject(mirrorlistBox.getSelectedIndex());
					Service.ontologyService.addFromOntologyMirror(MainApp.userOntology, ontMirror.getNamespace(), ontMirror.getLocalFile(), callback);
					break;
				default:
					break;
			}
		}
		else if(action == DELETENSIMPORT)
			Service.ontologyService.removeImport(MainApp.userOntology, baseURI.getText(), callback);
	}
	
	/*public FormPanel getFormPanel(Widget widget) {
	    
		form = new FormPanel();
	    form.setAction(GWT.getModuleBaseURL()+"fileupload");
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    form.setWidget(widget);

	    form.addSubmitHandler(new FormPanel.SubmitHandler() {
    		public void onSubmit(SubmitEvent event) {
    			if ("".equalsIgnoreCase(localFile.getFilename())) {
    				event.cancel(); // cancel the event
    			}
    		}
    	});

    	form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
    		public void onSubmitComplete(SubmitCompleteEvent event) {
    			Window.alert(event.getResults()+"<  submitcomplete: Action: "+action+"  \nimport: "+listBox.getSelectedIndex()+"  \nbase: "+baseURI.getText()+"  \nlocal: "+localFile.getFilename()+"  \naltURL: "+altURL.getText()+"  \nmirror: "+mirrorFile.getText()+"  \nindex: "+mirrorlistBox.getSelectedIndex());
    			hide();
    			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
    				public void onSuccess(Boolean result){
    					if(result)
    					{
    						if(opener!=null)
    							opener.nsImportDialogBoxSubmit();
    					}
    					else
    						Window.alert(constants.ontologyImportsManageFail());
    				}
    				public void onFailure(Throwable caught){
    					ExceptionManager.showException(caught, constants.ontologyImportsManageFail());
    				}
    			};
    			Service.ontologyService.addFromLocalFile(MainApp.userOntology, baseURI.getText(), localFile.getFilename(), mirrorFile.getText(), callback);
    		}
    	});
    	
    	return form;

	  }*/
}
