package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.Main;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.widgetlib.shared.label.LinkLabel;
import org.fao.aoscs.client.widgetlib.shared.panel.Spacer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Generates browser compatibility warning message
 *
 */
public class BrowserCompatibilityInfo  extends HorizontalPanel{
	
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private Image warnIcon;
	private HTML warnText;
	
	private LinkLabel labelSafari;
	private LinkLabel labelChrome;
	private LinkLabel labelFirefox;
	private LinkLabel labelIE;
	
	public BrowserCompatibilityInfo() {
		super();
		this.setWidth("100%");
		// Add compatible browsers;
		labelSafari = new LinkLabel("images/browser-safari.png", "Safari", "Safari");
		labelChrome= new LinkLabel("images/browser-chrome.png", "Google Chrome", "Google Chrome");
		labelFirefox = new LinkLabel("images/browser-firefox.png", "Mozilla Firefox", "Mozilla Firefox");
		labelIE = new LinkLabel("images/browser-ie.png", "Internet Explorer", "Internet Explorer");
		
		labelSafari.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Main.openURL("http://www.apple.com/safari/download/" , "_blank");
			}
		});
		
		labelChrome.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Main.openURL("http://www.google.com/chrome/intl/en/landing_chrome.html" , "_blank");
			}
		});
		
		labelFirefox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Main.openURL("http://www.mozilla.com/en-US/firefox/" , "_blank");
			}
		});
		
		labelIE.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent arg0) {
				Main.openURL("http://windows.microsoft.com/en-us/internet-explorer/browser-ie" , "_blank");
			}
		});
	
		warnIcon = new Image("images/warning-small.png");
		warnText = new HTML(constants.mainPageTitle()+" "+constants.mainBrowserWarn(), false);
		HorizontalPanel browsers = addBrowsers();
		
		Spacer s = new Spacer("100%", "100%");
		
		add(new Spacer("20px", "100%"));
		add(warnIcon);
		add(warnText);
		add(browsers);
		setCellHorizontalAlignment(warnIcon, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(warnText, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(browsers, HasHorizontalAlignment.ALIGN_LEFT);
		setCellVerticalAlignment(warnIcon, HasVerticalAlignment.ALIGN_MIDDLE);
		setCellVerticalAlignment(warnText, HasVerticalAlignment.ALIGN_MIDDLE);
		setCellVerticalAlignment(browsers, HasVerticalAlignment.ALIGN_MIDDLE);
		setSpacing(5);
		setStyleName("browserCompatibilityInfo-container");
		add(browsers);
		add(s);
		setCellWidth(s, "100%");
	}
	
	private HorizontalPanel addBrowsers(){
		HorizontalPanel bs = new HorizontalPanel();
		bs.setSpacing(5);
		bs.add(labelSafari);
		bs.add(new HTML("&nbsp;&nbsp;"));
		bs.add(labelChrome);
		bs.add(new HTML("&nbsp;&nbsp;"));
		bs.add(labelFirefox);
		bs.add(new HTML("&nbsp;&nbsp;"));
		bs.add(labelIE);
		return bs;
	}

		
	
}
