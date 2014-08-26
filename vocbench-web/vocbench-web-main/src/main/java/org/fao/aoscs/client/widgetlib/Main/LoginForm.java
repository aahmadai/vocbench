package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.Login;
import org.fao.aoscs.client.SelectPreferenceDlg;
import org.fao.aoscs.client.Service;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.UserLogin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginForm extends VerticalPanel{
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private SelectPreferenceDlg selectPrefDlg;
	
 	public LoginForm() 
	{		
		super();
		init(false);
	}
 	
 	private void init(boolean showLogin)
 	{
 		this.clear();
 		VerticalPanel loginTitle = new VerticalPanel();
		loginTitle.setStyleName("loginTitle");
		loginTitle.add(new HTML(constants.loginTitle()));
		if(!ConfigConstants.ISVISITOR){
			Login login = new Login();
			login.setStyleName("login");		
			
			this.add(loginTitle);
			this.add(login);
			this.setCellWidth(loginTitle, "100%");
			this.setCellWidth(login, "100%");
		}else{
			LinkLabel signin = new LinkLabel(null, constants.loginSignInAsVisitor(), constants.loginSignInAsVisitor()); 
			signin.setLabelStyle("toolbar-link");
			signin.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent arg0) {
					signIn();
				}
			});
			
			this.add(loginTitle);
			if(showLogin)
			{
				Login login = new Login();
				login.setStyleName("login");
				this.add(login);
				this.add(signin);
			}
			else
			{
				this.add(new Spacer("100%", "20px"));
				LinkLabel linkLabel = new LinkLabel(null, constants.loginSignInAsAdministrator(), constants.loginSignInAsAdministrator()); 
				linkLabel.setLabelStyle("toolbar-link");
				linkLabel.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent arg0) {
						init(true);
					}
				});
				this.add(signin);
				this.add(linkLabel);
			}
			
			this.setSpacing(10);
			this.setCellVerticalAlignment(signin,HasVerticalAlignment.ALIGN_MIDDLE);
			this.setCellHorizontalAlignment(signin,HasHorizontalAlignment.ALIGN_CENTER);
			this.setCellWidth(loginTitle, "100%");
			this.setCellHeight(loginTitle, "100%");
		}
 	}
	
	private void signIn()
	{
		AsyncCallback<UserLogin> callback = new AsyncCallback<UserLogin>() {
			public void onSuccess(UserLogin result) {	   				
				if(result==null){
    				try 
    				{
						Window.alert(constants.loginNoMatch());
					} 
    				catch (Exception e) 
    				{
						e.printStackTrace();
					}
				} 
				else 
				{
					UserLogin userLoginObj = (UserLogin) result;
					
					if(userLoginObj.getNoOfGroup()==0)
					{
		    			Window.alert(constants.loginNoAssignGroup());
		    		}
					else if(userLoginObj.getUserSelectedLanguage().size()<1)
					{
						Window.alert(constants.loginNoAssignLang());
					}
					else 
					{
						userLoginObj.setGroupid(ConfigConstants.VISITORGROUPID);
						userLoginObj.setGroupname(ConfigConstants.VISITORGROUPNAME);
			    		if(selectPrefDlg == null || !selectPrefDlg.isLoaded)
			    			selectPrefDlg = new SelectPreferenceDlg(userLoginObj);
				    	selectPrefDlg.show();	    						    		
			    	}
		        }
			}
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.loginFail());
		    }
		};		    		
		Service.systemService.getAuthorize(ConfigConstants.GUESTUSERNAME, ConfigConstants.GUESTPASSWORD, callback);
	}
}
