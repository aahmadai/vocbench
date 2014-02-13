package org.fao.aoscs.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.locale.LocaleMessages;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.client.module.logging.LogManager;
import org.fao.aoscs.client.utility.ExceptionManager;
import org.fao.aoscs.client.utility.HelpUtility;
import org.fao.aoscs.client.widgetlib.Main.AcknowledgementWidget;
import org.fao.aoscs.client.widgetlib.Main.BrowserCompatibilityInfo;
import org.fao.aoscs.client.widgetlib.Main.Footer;
import org.fao.aoscs.client.widgetlib.Main.Header;
import org.fao.aoscs.client.widgetlib.Main.LoginForm;
import org.fao.aoscs.client.widgetlib.Main.QuickLinks;
import org.fao.aoscs.client.widgetlib.Main.WhatIsNew;
import org.fao.aoscs.client.widgetlib.shared.dialog.LoadingDialog;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;
import org.fao.aoscs.domain.ConfigObject;
import org.fao.aoscs.domain.LanguageInterface;
import org.fao.aoscs.domain.UserLogin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Main implements EntryPoint {

	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private static LocaleMessages messages = (LocaleMessages) GWT.create(LocaleMessages.class);
	private static VerticalPanel panel;
	private static HorizontalPanel langInterfacePanel = new HorizontalPanel();
	private static VerticalPanel centerContainer = new VerticalPanel();
	private static HashMap<String, ConfigObject> configObjectMap = new HashMap<String, ConfigObject>();

	public void onModuleLoad() {
		
		initLayout();
		
		 Window.addWindowClosingHandler(new ClosingHandler() {
				public void onWindowClosing(ClosingEvent event) {
					new LogManager().endLog();
				}
	        });
	}
	
	private static void loadConfigContainer()
	{
		ConfigContainer configContainer = new ConfigContainer(configObjectMap);
		centerContainer.clear();
		centerContainer.add(configContainer);
	}
	
	private static void loadDBMigration()
	{
		DBMigration dbMigration = new DBMigration();
		centerContainer.clear();
		centerContainer.add(dbMigration);
	}
	
	private static void loadCenterContainer()
	{
		AsyncCallback<ArrayList<LanguageInterface>> callback = new AsyncCallback<ArrayList<LanguageInterface>>()
		{
			public void onSuccess(ArrayList<LanguageInterface> result) {
				
				Window.setTitle(constants.mainPageTitle()+" :: "+constants.mainVersion()+" "+ (ConfigConstants.DISPLAYVERSION!=null?ConfigConstants.DISPLAYVERSION:"")+ " " + ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.DEV))? "(DEVELOPMENT)" : ((ConfigConstants.MODE !=null && ConfigConstants.MODE.equals(MainApp.SANDBOX))? "(SANDBOX)" : "")));

				centerContainer.clear();
				
				//Main Content
				HTML descTitle = new HTML(constants.mainWelcome());
				DOM.setStyleAttribute(descTitle.getElement(), "fontWeight", "bold");
				DOM.setStyleAttribute(descTitle.getElement(), "fontSize", "13px");

				VerticalPanel briefLeft = new VerticalPanel();
				briefLeft.setSpacing(2);
				briefLeft.setSize("100%","100%");
				briefLeft.add(descTitle);
				briefLeft.add(getVocbenchDescription());
				if(!ConfigConstants.ISVISITOR)
					briefLeft.add(getVisitorInfo());
				if(ConfigConstants.MODE.equals(MainApp.PRO))
				{
					if(!ConfigConstants.SANDBOXLINK.equals(""))
						briefLeft.add(getSandboxInfo());
				}
				if(ConfigConstants.MODE.equals(MainApp.SANDBOX))
					briefLeft.add(getAnonymousInfo());

				Image flyer = new Image("images/flyer.jpg");
				DOM.setStyleAttribute(flyer.getElement(), "cursor", "pointer");
				flyer.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event) {
						Window.open("ftp://ftp.fao.org/gi/gil/gilws/aims/references/flyers/csworkbench_en.pdf","_blank","schollbars=0,toolbar=0,resizable=1,status=no" );
					}}
				);

				VerticalPanel briefRight= new VerticalPanel();
				briefRight.setSize("100%","100%");
				briefRight.add(flyer);

				final HorizontalPanel briefMiddle = new HorizontalPanel();
				briefMiddle.setSize("100%","100%");
				briefMiddle.add(briefLeft);
				briefMiddle.add(new Spacer("20px", "100%"));
				//briefMiddle.add(briefRight);

				WhatIsNew whatIsNew = new WhatIsNew();
				whatIsNew.addNewItem(new HTML(constants.mainPageTitle() +" Web Services Beta version has been released for developers of agricultural information management systems to incorporate the " + constants.mainPageTitle() + " into their applications via <a href='"+ConfigConstants.WEBSERVICESINFO+"' target='_blank'>web services</a>."));

				VerticalPanel content = new VerticalPanel();
				content.setStyleName("front-content");
				content.add(briefMiddle);
				content.add(whatIsNew);

				// Sidebar
				final LinkLabel help = new LinkLabel("images/help.png", constants.mainHelpTitle(), constants.mainHelp(), "term-Label");
				help.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						HelpUtility.openHelp("LOGIN");
					}
				});

				LoginForm loginForm = new LoginForm();
				loginForm.setSize("100%","100%");

			 	LinkLabel forgetPassword = new LinkLabel("images/password.png", constants.mainForgotPasswordTitle(), constants.mainForgotPassword(), "term-Label");
			 	forgetPassword.addClickHandler(new ClickHandler(){
						public void onClick(ClickEvent event) {
							ForgetPassword forgotPwdPanel = new ForgetPassword();
							centerContainer.clear();
							centerContainer.setSpacing(10);
							centerContainer.add(forgotPwdPanel);
					}
			 	});

			 	LinkLabel createAccount = new LinkLabel("images/register.png", constants.mainRegisterTitle(), constants.mainRegister(), "term-Label");
				createAccount.addClickHandler(new ClickHandler(){
			 		public void onClick(ClickEvent event) {
			 			Register register = new Register();
			 			centerContainer.clear();
			 			centerContainer.add(register);
					}
			 	});

				VerticalPanel loginUtility = new VerticalPanel();
				loginUtility.setSpacing(5);
				loginUtility.setStyleName("loginUtility");
				if(!ConfigConstants.ISVISITOR){
					loginUtility.add(forgetPassword);
					loginUtility.add(createAccount);
				}
				loginUtility.setCellHorizontalAlignment(forgetPassword, HasHorizontalAlignment.ALIGN_CENTER);
				loginUtility.setCellHorizontalAlignment(createAccount, HasHorizontalAlignment.ALIGN_CENTER);

				VerticalPanel sideBarContainer = new VerticalPanel();
				sideBarContainer.setSize("100%", "100%");
				sideBarContainer.add(help);
				sideBarContainer.add(loginForm);
				sideBarContainer.add(new Spacer("100%","10px"));
				sideBarContainer.add(loginUtility);
				sideBarContainer.setCellHorizontalAlignment(help, HasHorizontalAlignment.ALIGN_RIGHT);
				sideBarContainer.setCellHorizontalAlignment(loginForm, HasHorizontalAlignment.ALIGN_CENTER);
				sideBarContainer.setCellHorizontalAlignment(loginUtility, HasHorizontalAlignment.ALIGN_CENTER);

				sideBarContainer.setCellHeight(help , "5px");
				sideBarContainer.setCellHeight(loginUtility , "30px");

				sideBarContainer.setCellWidth(loginForm , "100%");
				sideBarContainer.setCellWidth(loginUtility , "100%");

				VerticalPanel sideBar= new VerticalPanel();
				sideBar.setStyleName("sideBar");
				sideBar.add(sideBarContainer);

				HorizontalPanel mainContiner = new HorizontalPanel();
				mainContiner.setSize("100%","100%");
				mainContiner.add(content);
				mainContiner.add(sideBar);
				mainContiner.setCellWidth(sideBar, "225px");

				// Acknowledgements
				HorizontalPanel ackPartner = new HorizontalPanel();
				ackPartner.setSize("100%", "100%");
				ackPartner.add(getPartners());

				VerticalPanel ack = new VerticalPanel();
				ack.setStyleName("ack");
				ack.add(ackPartner);

				VerticalPanel ackContainer = new VerticalPanel();
				ackContainer.setStyleName("ack-container");
				ackContainer.add(ack);
				ackContainer.setCellHeight(ack, "100%");
				ackContainer.setCellWidth(ack, "100%");

				// Browser warning message
				BrowserCompatibilityInfo bcInfo = new BrowserCompatibilityInfo();

				VerticalPanel bcContainer = new VerticalPanel();
				bcContainer.setSize("100%", "100%");
				bcContainer.setStyleName("bc-container");
				bcContainer.add(bcInfo);
				bcContainer.setCellHeight(bcInfo, "100%");
				bcContainer.setCellWidth(bcInfo, "100%");

				centerContainer.setSize("100%","100%");
				centerContainer.add(mainContiner);
				centerContainer.add(ackContainer);
				centerContainer.add(bcContainer);
				centerContainer.setCellVerticalAlignment(ackContainer, HasVerticalAlignment.ALIGN_BOTTOM);
				centerContainer.setCellVerticalAlignment(bcContainer, HasVerticalAlignment.ALIGN_BOTTOM);
				centerContainer.setCellHeight(mainContiner, "100%");

				langInterfacePanel.setStyleName("header-quickLinks");
				langInterfacePanel.clear();
				langInterfacePanel.add(getLanguageBar(result));
			}
		    public void onFailure(Throwable caught) {
		    	loadConfigContainer();
		    	ExceptionManager.showException(caught, constants.mainDBError());
		    }
		};
		
		Service.systemService.getInterfaceLang(callback);
	}
	
	private static boolean checkConfig(final HashMap<String, ConfigObject> configObjectMap)
	{
		int i=0;
		for(String key : configObjectMap.keySet())
		{
			ConfigObject configObject = configObjectMap.get(key);
			if((configObject.getValue()==null || configObject.getValue().equals(""))) 
			{
				i++;
			}
		}
		return i>0;
	}
	
	private static void checkDBConnection()
	{
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
		    public void onSuccess(Boolean result) {
		    	if(result)
		    		loadDBMigration();
		    	else
		    	{
		    		Window.alert("Database connection failed");
		    		loadConfigContainer();
		    	}
		    }
		    public void onFailure(Throwable caught) {
		    	loadConfigContainer();
		    	ExceptionManager.showException(caught, "Database connection failed!");
		    }
		 };
		Service.systemService.checkDBConnection(callback); 
	}
	
	private static void loadContainer()
	{
		if(checkConfig(configObjectMap))
		{
			loadConfigContainer();
		}
		else
		{
			ConfigConstants.loadConstants(configObjectMap);
			checkDBConnection();
		}
	}
	
	public static void onSuccess()
	{
		/* User data from session for feed to profile query */
		AsyncCallback<UserLogin> callback = new AsyncCallback<UserLogin>() {
		    public void onSuccess(UserLogin userLoginObj) {
		    	// init page layout
		    	if(userLoginObj==null){
		    		loadCenterContainer();
		    	}else{
		    		// Get information from session
					MainApp mainApp = new MainApp(userLoginObj);
					new LogManager().startLog(""+userLoginObj.getUserid());
					mainApp.setWidth("100%");
					Main.replaceRootPanel(mainApp);
		    	}
		    }
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.mainSessionExpired());
		    }
		 };
		Service.systemService.checkSession(MainApp.USERLOGINOBJECT_SESSIONNAME, callback); // Get userlogin from session
	}

	private static void initLayout()
	{
		AsyncCallback<HashMap<String, ConfigObject>> callback = new AsyncCallback<HashMap<String, ConfigObject>>()
		{
			public void onSuccess(final HashMap<String, ConfigObject> result) {
				configObjectMap = result;
				centerContainer = new VerticalPanel();
				centerContainer.setSize("100%", "100%");
				LoadingDialog load = new LoadingDialog();
				centerContainer.add(load);
				centerContainer.setCellHorizontalAlignment(load,HasHorizontalAlignment.ALIGN_CENTER);
				centerContainer.setCellVerticalAlignment(load, HasVerticalAlignment.ALIGN_MIDDLE);
				
				panel = new VerticalPanel();
				panel.setSize("100%", "100%");
				
				loadContainer();
				
				// Quick links
				QuickLinks quickLinks = new QuickLinks("header-quickLinks",true);

				HorizontalPanel topPanel = new HorizontalPanel();
				topPanel.add(quickLinks);
				topPanel.add(langInterfacePanel);
				topPanel.setCellVerticalAlignment(quickLinks, HasVerticalAlignment.ALIGN_MIDDLE);
				topPanel.setCellHorizontalAlignment(quickLinks, HasHorizontalAlignment.ALIGN_LEFT);
				topPanel.setCellVerticalAlignment(langInterfacePanel, HasVerticalAlignment.ALIGN_MIDDLE);
				topPanel.setCellHorizontalAlignment(langInterfacePanel, HasHorizontalAlignment.ALIGN_RIGHT);
				topPanel.setCellHeight(quickLinks, "30px");
				topPanel.setCellHeight(langInterfacePanel, "30px");
				topPanel.setCellWidth(quickLinks, "100%");
				topPanel.setStyleName("header-quickLinks");

				// Header
				Header header = new Header();
				
				//Footer
				Footer footer = new Footer();

				// Add everything
				panel.add(topPanel);
				panel.add(header);
				panel.add(centerContainer);
				panel.add(footer);
				panel.setCellHeight(header, "40px");
				panel.setCellHeight(topPanel, "30px");
				panel.setCellWidth(header, "100%");
				panel.setCellWidth(centerContainer, "100%");
				panel.setCellHeight(centerContainer, "100%");
				panel.setCellHorizontalAlignment(centerContainer, HasHorizontalAlignment.ALIGN_CENTER);
				panel.setCellVerticalAlignment(footer, HasVerticalAlignment.ALIGN_BOTTOM);

				RootPanel.get().add(panel);
			}
		    public void onFailure(Throwable caught) {
		    	ExceptionManager.showException(caught, constants.configConfigurationLoadFail());
		    }
		};

		Service.systemService.loadConfigConstants(callback);
	 }

	public static void replaceRootPanel(Widget object){
		 RootPanel.get().clear();
		 RootPanel.get().add(object);
	}

	 public static void gotoLoginScreen(){
		 RootPanel.get().clear();
		 initLayout();
	 }
	 public static native void openURL(String url) /*-{
	   $wnd.open(url,'_blank','');
	}-*/;
	 public static native void openURL(String url, String target) /*-{
	   $wnd.open(url,target,'');
	}-*/;
	 public static native String getUserAgent() /*-{
	   return $wnd.navigator.appName;
	}-*/;

	 public static void signOut(){
		 RootPanel.get().clear();
		 History.newItem(null);
		 initLayout();
	 }

	 public static void mailAlert(String fname, String lname, String pemail,String userName,String password){
		  String to = pemail;
		  String subject = messages.mailChangePasswordSubject(constants.mainPageTitle());
		  String body = messages.mailChangePasswordBody(fname, lname, constants.mainPageTitle(), userName, password, ConfigConstants.EMAIL_FROM);

		  AsyncCallback<Void> cbkmail = new AsyncCallback<Void>(){
			  public void onSuccess(Void result) {
				  GWT.log("Mail Send Successfully", null);
			    }
			    public void onFailure(Throwable caught) {
			    	//ExceptionManager.showException(caught, "Mail Send Failed");
			    	GWT.log("Mail Send Failed", null);
			    }
		  };
		  Service.systemService.SendMail(to, subject, body, cbkmail);
	}

	 private static Widget getPartners()
	 {
		VerticalPanel hp = new VerticalPanel();
		hp.setSize("100%", "100%");
		
		Widget topContainer = getTopContainer();
		hp.add(topContainer);
		hp.setCellHorizontalAlignment(topContainer, HasHorizontalAlignment.ALIGN_CENTER);
		DOM.setStyleAttribute(topContainer.getElement(), "borderBottom", "1px solid #CFD9EB");

		Widget middleContainer = getMiddleContainer();
		hp.add(middleContainer);
		hp.setCellHorizontalAlignment(topContainer, HasHorizontalAlignment.ALIGN_CENTER);
		DOM.setStyleAttribute(middleContainer.getElement(), "borderTop", "1px solid #FFFFFF");
		
		if(ConfigConstants.PARTNERS.equalsIgnoreCase("FAO"))
		{
			Widget bottomContainer = getBottomContainer();
			hp.add(bottomContainer);
			hp.setCellHorizontalAlignment(bottomContainer, HasHorizontalAlignment.ALIGN_CENTER);
			
			DOM.setStyleAttribute(middleContainer.getElement(), "borderBottom", "1px solid #CFD9EB");
			DOM.setStyleAttribute(bottomContainer.getElement(), "borderTop", "1px solid #FFFFFF");
		}
		
		return hp;
		}
	 
	 private static Widget getTopContainer()
	 {
		 HTML ackleftTitle = new HTML(constants.mainPageTitle()+" "+constants.mainPartner(), false);
		DOM.setStyleAttribute(ackleftTitle.getElement(), "paddingTop", "10px");
		DOM.setStyleAttribute(ackleftTitle.getElement(), "paddingLeft", "30px");
		ackleftTitle.setStyleName("ack-title");
		
		HTML ackrightTitle = new HTML(constants.otherPartner(), false);
		DOM.setStyleAttribute(ackrightTitle.getElement(), "paddingTop", "10px");
		DOM.setStyleAttribute(ackrightTitle.getElement(), "paddingLeft", "30px");
		ackrightTitle.setStyleName("ack-title");

		AcknowledgementWidget ackFao   	= new AcknowledgementWidget("images/logo_fao.gif","Food and Agriculture Organization of the United Nations","Food and Agriculture Organization of the United Nations","for a world without hunger","http://www.fao.org");
		AcknowledgementWidget ackUnitov 	= new AcknowledgementWidget("images/logo_tv.png","Università degli Studi di Roma","Università degli Studi di Roma","Tor Vergata","http://web.uniroma2.it");
		
		HorizontalPanel topleft = new HorizontalPanel();
		topleft.setSize("100%","100%");
		DOM.setStyleAttribute(topleft.getElement(), "padding", "4px");
		topleft.add(ackFao);
		topleft.add(ackUnitov);
		topleft.setCellHorizontalAlignment(ackFao, HasHorizontalAlignment.ALIGN_CENTER);
		topleft.setCellHorizontalAlignment(ackUnitov, HasHorizontalAlignment.ALIGN_LEFT);
		
		AcknowledgementWidget ackKu    	= new AcknowledgementWidget("images/logo_ku.gif","Kasetsart University","Kasetsart University","Bangkok, Thailand","http://www.ku.ac.th");
		AcknowledgementWidget ackAgris 	= new AcknowledgementWidget("images/logo_agris.gif","Thai National Agris Center","Thai National Agris Center","Bangkok, Thailand","http://thaiagris.lib.ku.ac.th/");
		AcknowledgementWidget ackMimos 	= new AcknowledgementWidget("images/mimos_logo.jpg","MIMOS Berhad","MIMOS Berhad","Kuala Lumpur, Malaysia","http://www.mimos.my/");
		
		HorizontalPanel topright = new HorizontalPanel();
		topright.setSize("100%","100%");
		DOM.setStyleAttribute(topright.getElement(), "padding", "4px");
		topright.add(ackKu);
		topright.add(ackAgris);
		topright.add(ackMimos);
		topright.setCellHorizontalAlignment(ackKu, HasHorizontalAlignment.ALIGN_RIGHT);
		topright.setCellHorizontalAlignment(ackAgris, HasHorizontalAlignment.ALIGN_RIGHT);
		topright.setCellHorizontalAlignment(ackMimos, HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel topleftContainer = new VerticalPanel();
		topleftContainer.setSize("100%", "100%");
		topleftContainer.add(ackleftTitle);
		topleftContainer.add(topleft);
		topleftContainer.setCellVerticalAlignment(ackleftTitle, HasVerticalAlignment.ALIGN_TOP);
		topleftContainer.setCellHorizontalAlignment(ackleftTitle, HasHorizontalAlignment.ALIGN_LEFT);
		
		VerticalPanel toprightContainer = new VerticalPanel();
		toprightContainer.setSize("100%", "100%");
		toprightContainer.add(ackrightTitle);
		toprightContainer.add(topright);
		toprightContainer.setCellVerticalAlignment(ackrightTitle, HasVerticalAlignment.ALIGN_TOP);
		toprightContainer.setCellHorizontalAlignment(ackrightTitle, HasHorizontalAlignment.ALIGN_LEFT);
		
		HorizontalPanel topContainer = new HorizontalPanel();
		topContainer.setSize("100%", "100%");
		topContainer.add(topleftContainer);
		topContainer.add(toprightContainer);
		return topContainer;
	 }
	 
	 private static Widget getMiddleContainer()
	 {
	 	HorizontalPanel middleContainer = new HorizontalPanel();
		middleContainer.setSize("100%", "100%");
		
		VerticalPanel middle = new VerticalPanel();
		DOM.setStyleAttribute(middle.getElement(), "padding", "10px");
		DOM.setStyleAttribute(middle.getElement(), "paddingLeft", "30px");
		middle.setSize("100%","100%");
		
		HTML fundedBy = new HTML(constants.mainPageTitle()+" "+constants.fundedBy(), false);
		fundedBy.setStyleName("ackLabel");
		fundedBy.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		HorizontalPanel fundedByPanel = new HorizontalPanel();
		DOM.setStyleAttribute(fundedByPanel.getElement(), "padding", "4px");
		fundedByPanel.setSize("100%", "100%");
		
		AcknowledgementWidget agINFRA = new AcknowledgementWidget("images/agINFRA_logo.jpg","agINFRA","agINFRA","(EC  7th framework program INFRA-2011-1.2.2, Grant agreement no: 283770)","http://aginfra.eu" );
		agINFRA.setImageSize("47", "36");
		AcknowledgementWidget SemaGrow = new AcknowledgementWidget("images/SemaGrow_logo.jpg","SemaGrow","SemaGrow","(EC 7th framework program ICT-2011.4.4, Grant agreement no: 318497)","http://www.semagrow.eu" );
		SemaGrow.setImageSize("112", "36");
		
		fundedByPanel.add(agINFRA);
		fundedByPanel.add(SemaGrow);
		
		middle.add(fundedBy);
		middle.add(fundedByPanel);
		
		middleContainer.add(middle);
		return middleContainer;
	 }
	 
	 private static Widget getBottomContainer()
	 {
	 	HTML contrib = new HTML(constants.mainAck2(), false);
		contrib.setStyleName("ackLabel");
		contrib.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		HTML ack = new HTML(constants.mainAck1(), false);
		ack.setStyleName("ackLabel");
		ack.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);	
	 
		VerticalPanel leftBottom = new VerticalPanel();
		leftBottom.add(contrib);
		leftBottom.add(new AcknowledgementWidget("images/logo_icrisat.png","ICRISAT","ICRISAT","International Crops Research Institute for the Semi-Arid Tropics","http://www.icrisat.org" ));

		VerticalPanel rightBottom = new VerticalPanel();
		rightBottom.add(ack);
		rightBottom.add(new Spacer("100%", "10px"));
		rightBottom.add(new AcknowledgementWidget(null, null, "Prof. Dagobert Soergel","UNIVERSITY OF MARYLAND",null));

		HorizontalPanel bottom = new HorizontalPanel();
		DOM.setStyleAttribute(bottom.getElement(), "padding", "10px");
		DOM.setStyleAttribute(bottom.getElement(), "paddingLeft", "30px");
		bottom.setSize("100%","100%");
		bottom.add(leftBottom);
		bottom.add(rightBottom);
		
		HorizontalPanel bottomContainer = new HorizontalPanel();
		bottomContainer.setSize("100%", "100%");
		bottomContainer.add(bottom);
		
		return bottomContainer;
	 }

	private static HorizontalPanel getLanguageBar(final ArrayList<LanguageInterface> langList)
	{
		final ListBox langMenuBar = new ListBox();
		for(int i=0 ; i<langList.size() ; i++)
		{
			LanguageInterface langInterface = (LanguageInterface) langList.get(i);
			langMenuBar.addItem(langInterface.getLocalLanguage(), langInterface.getLanguageCode().toLowerCase());
			if(langList.get(i).getLanguageCode().toLowerCase().equals(constants.mainLocale().toLowerCase()))
				langMenuBar.setSelectedIndex(i);

		}
		langMenuBar.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				try
				{
					Window.open(GWT.getHostPageBaseURL()+"index.html?locale="+langMenuBar.getValue(langMenuBar.getSelectedIndex()), "_self", null);
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
		});
		Image map = new Image("images/map-grey.gif");

		HorizontalPanel langPanel = new HorizontalPanel();
		langPanel.setSpacing(5);
		langPanel.add(map);
		langPanel.add(langMenuBar);
		langPanel.setCellVerticalAlignment(langMenuBar, HasVerticalAlignment.ALIGN_MIDDLE);
		langPanel.setCellHorizontalAlignment(langMenuBar, HasHorizontalAlignment.ALIGN_LEFT);
		langPanel.setCellVerticalAlignment(map, HasVerticalAlignment.ALIGN_MIDDLE);

		return langPanel;
	}

	private static VerticalPanel getVocbenchDescription(){
		HTML desc = new HTML(constants.mainBrief());
		DOM.setStyleAttribute(desc.getElement(), "fontSize", "12px");

		LinkLabel learnMore = new LinkLabel(null, constants.mainLearnMore(), constants.mainLearnMore());
		learnMore.setLabelStyle("toolbar-link");
		learnMore.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Window.open(ConfigConstants.VOCBENCHINFO, null, null);
			}
		});
		VerticalPanel vp = new VerticalPanel();
		vp.add(desc);
		vp.add(learnMore);
		vp.setSpacing(8);
		return vp;
	}

	private static VerticalPanel getVisitorInfo(){
		HTML ca = new HTML("&nbsp;"+constants.mainMustRegister()+"&nbsp;");
		ca.setStyleName("link-label-blue");
		ca.addClickHandler(new ClickHandler(){
	 		public void onClick(ClickEvent event) {
	 			Register register = new Register();
	 			centerContainer.clear();
	 			centerContainer.add(register);
			}
	 	});
		HTML ca0 = new HTML(constants.mainMustLogin() + "&nbsp;"+ constants.mainMustLoginOr());
		HTML ca1 = new HTML(constants.mainToUseWB());

		HorizontalPanel briefHp = new HorizontalPanel();
		briefHp.add(ca0);
		briefHp.add(ca);
		briefHp.add(ca1);

		DOM.setStyleAttribute(ca0.getElement(), "fontSize", "12px");
		DOM.setStyleAttribute(ca.getElement(), "fontSize", "12px");
		DOM.setStyleAttribute(ca1.getElement(), "fontSize", "12px");

		VerticalPanel vp = new VerticalPanel();
		vp.add(briefHp);
		vp.setSpacing(8);
		return vp;
	}

	private static VerticalPanel getSandboxInfo(){
		HTML sandboxText = new HTML(constants.mainSandboxInfo());
		DOM.setStyleAttribute(sandboxText.getElement(), "fontSize", "12px");

		LinkLabel sandboxLink = new LinkLabel(null, "Click here for Sandbox", "Click here for Sandbox");
		sandboxLink.setLabelStyle("toolbar-link");
		sandboxLink.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				Window.open(ConfigConstants.SANDBOXLINK, null, null);
			}
		});

		VerticalPanel vp = new VerticalPanel();
		vp.add(sandboxText);
		vp.add(sandboxLink);
		vp.setSpacing(8);
		return vp;
	}

	private static VerticalPanel getAnonymousInfo(){
		HTML text = new HTML(constants.mainAnonymousInfo());
		DOM.setStyleAttribute(text.getElement(), "fontSize", "12px");
		VerticalPanel vp = new VerticalPanel();
		vp.add(text);
		vp.setSpacing(8);
		return vp;
	}
}

