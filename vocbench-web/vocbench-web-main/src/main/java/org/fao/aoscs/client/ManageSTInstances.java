/**
 * 
 */
package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.Arrays;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.GridStyle;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.domain.StInstances;
import org.fao.aoscs.domain.StInstancesId;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class ManageSTInstances extends FormDialogBox implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	private TextBox txtSTName;
	private TextBox txtSTDomain;
	private TextBox txtSTPort;

	public static int STURL_ADD = 0;
	public static int STURL_DELETE = 1;
	
	private int action = -1;
	
	private STURLDialogBoxOpener opener;
	
	private StInstances stInstances;
	
	public interface STURLDialogBoxOpener {
	    void stURLDialogBoxSubmit();
	}
	
	public ManageSTInstances(int action){
		super();
		this.action = action;
		this.stInstances = new StInstances(new StInstancesId());
		this.initLayout();
	}
		
	public ManageSTInstances(int action, StInstances stInstances){
		super();
		this.stInstances = stInstances;
		this.action = action;
		this.initLayout();
	}
	
	public void initLayout() {
		this.setWidth("400px");
		
		txtSTDomain = new TextBox();
		txtSTDomain.setWidth("100%");
		if(stInstances.getId().getStDomain()==null || stInstances.getId().getStDomain().equals(""))
		{
			txtSTDomain.setText("localhost");
			txtSTDomain.addStyleName(Style.label_color_gray);
		}
		else
		{
			txtSTDomain.setText(stInstances.getId().getStDomain());
			txtSTDomain.removeStyleName(Style.label_color_gray);
		}
		txtSTDomain.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				txtSTDomain.removeStyleName(Style.label_color_gray);
				if((stInstances.getId().getStName()==null || stInstances.getId().getStName().equals("")) && checkStyleExists(txtSTName, Style.label_color_gray))
				{
					txtSTName.setText(txtSTDomain.getText()+"-"+txtSTPort.getText());
				}
			}
		});
		
		txtSTPort = new TextBox();
		txtSTPort.setWidth("100%");
		if(stInstances.getId().getStPort()==null || stInstances.getId().getStPort().equals(""))
		{
			txtSTPort.setText("1979");
			txtSTPort.addStyleName(Style.label_color_gray);
		}
		else
		{
			txtSTPort.setText(stInstances.getId().getStPort());
			txtSTPort.removeStyleName(Style.label_color_gray);
		}
		txtSTPort.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				txtSTPort.removeStyleName(Style.label_color_gray);
				if((stInstances.getId().getStName()==null || stInstances.getId().getStName().equals("")) && checkStyleExists(txtSTName, Style.label_color_gray))
				{
					txtSTName.setText(txtSTDomain.getText()+"-"+txtSTPort.getText());
				}			
			}
		});
		
		txtSTName = new TextBox();
		txtSTName.setWidth("100%");
		if(stInstances.getId().getStName()==null || stInstances.getId().getStName().equals(""))
		{
			txtSTName.setText(txtSTDomain.getText()+"-"+txtSTPort.getText());
			txtSTName.addStyleName(Style.label_color_gray);
		}
		else
		{
			txtSTName.setText(stInstances.getId().getStName());
			txtSTName.removeStyleName(Style.label_color_gray);
		}
		txtSTName.addKeyDownHandler(new KeyDownHandler() {
			
			public void onKeyDown(KeyDownEvent arg0) {
				txtSTName.removeStyleName(Style.label_color_gray);
			}
		});
		
		FlexTable table = new FlexTable();
		table.setWidget(0, 0, new HTML(constants.projectSTServerName()));
		table.setWidget(0, 1, txtSTName);
		table.setWidget(1, 0, new HTML(constants.projectSTServerDomain()));
		table.setWidget(1, 1, txtSTDomain);
		table.setWidget(2, 0, new HTML(constants.projectSTServerPort()));
		table.setWidget(2, 1, txtSTPort);
		table.setWidth("100%");
		table.getColumnFormatter().setWidth(0, "25%");
		
		String title = "";
		String buttonText = "";
		switch(action)
		{
			case 0:
				title = constants.buttonAdd()+" "+constants.projectSTServerInstance();
				buttonText = constants.buttonAdd();
				break;
			case 1:
				title = constants.buttonDelete()+" "+constants.projectSTServerInstance();
				buttonText = constants.buttonDelete();
				txtSTName.setEnabled(false);
				txtSTDomain.setEnabled(false);
				txtSTPort.setEnabled(false);
				break;
			default:
				break;
		}
		
		addWidget(GridStyle.setTableConceptDetailStyleleft(table, "gslRow1", "gslCol1", "gslPanel1"));
		this.setText(title);
		this.submit.setText(buttonText);
	}
	
	private boolean checkStyleExists(Widget w, String style)
	{
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(w.getStyleName().split(" ")));
		return list.contains(style);
	}
	
	public boolean passCheckInput() {
		boolean pass = txtSTName.getText().length()==0 && txtSTDomain.getText().length()==0 && txtSTPort.getText().length()==0;
		return !pass;
	}
	
	public void show(STURLDialogBoxOpener opener)
	{
		this.opener = opener;
		show();
		
	}
	
	public void onSubmit() {
		
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>(){
			public void onSuccess(Boolean result){
				if(result)
				{
					if(opener!=null)
						opener.stURLDialogBoxSubmit();
				}
				else
					Window.alert(constants.projectSTServerManageFail());
			}
			public void onFailure(Throwable caught){
				ExceptionManager.showException(caught, constants.projectSTServerManageFail());
			}
		};
		
		switch(action)
		{
			case 0:
				stInstances = new StInstances(new StInstancesId(txtSTName.getText(), txtSTDomain.getText(), txtSTPort.getText()));
				Service.systemService.addSTServer(MainApp.userOntology, stInstances, callback);
				break;
			case 1:
				Service.systemService.deleteSTServer(MainApp.userOntology, stInstances, callback);
				break;
		}
	}
	
}
