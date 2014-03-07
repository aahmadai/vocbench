package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PartnerFooter extends VerticalPanel{
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	public PartnerFooter() {
		super();

		VerticalPanel hp = new VerticalPanel();
		hp.setSize("100%", "100%");
		
		Widget developedBy = getDevelopedBy();
		hp.add(developedBy);
		hp.setCellHorizontalAlignment(developedBy, HasHorizontalAlignment.ALIGN_LEFT);
		DOM.setStyleAttribute(developedBy.getElement(), "borderBottom", "1px solid #CFD9EB");
		
		Widget fundedBy = getFundedBy();
		hp.add(fundedBy);
		hp.setCellHorizontalAlignment(fundedBy, HasHorizontalAlignment.ALIGN_LEFT);
		DOM.setStyleAttribute(fundedBy.getElement(), "borderTop", "1px solid #FFFFFF");
		
		Widget collaborator = getCollaborator();
		hp.add(collaborator);
		hp.setCellHorizontalAlignment(collaborator, HasHorizontalAlignment.ALIGN_LEFT);
		DOM.setStyleAttribute(collaborator.getElement(), "borderTop", "1px solid #FFFFFF");
		DOM.setStyleAttribute(fundedBy.getElement(), "borderBottom", "1px solid #CFD9EB");
		
		Widget agrovocPartners = getAgrovocParnters();
		hp.add(agrovocPartners);
		hp.setCellHorizontalAlignment(agrovocPartners, HasHorizontalAlignment.ALIGN_CENTER);
		
		DOM.setStyleAttribute(collaborator.getElement(), "borderBottom", "1px solid #CFD9EB");
		DOM.setStyleAttribute(agrovocPartners.getElement(), "borderTop", "1px solid #FFFFFF");
		
		this.add(hp);
		
		/*Frame frame = new Frame();
		frame.setSize("100%", "100%");
		frame.setUrl(GWT.getHostPageBaseURL()+ "partner_footer.html");
		this.add(frame);*/
	}
	
	
	
	 public static Widget getDevelopedBy()
	 {
		 HTML ackleftTitle = new HTML(constants.mainPageTitle()+" "+constants.mainPartner(), false);
		 DOM.setStyleAttribute(ackleftTitle.getElement(), "paddingTop", "10px");
		 DOM.setStyleAttribute(ackleftTitle.getElement(), "paddingLeft", "30px");
		 ackleftTitle.setStyleName("ack-title");
		
		 AcknowledgementWidget ackFao = new AcknowledgementWidget("images/logo_fao.gif","Food and Agriculture Organization of the United Nations","Food and Agriculture Organization of the United Nations","for a world without hunger","http://www.fao.org");
		 AcknowledgementWidget ackUnitov = new AcknowledgementWidget("images/logo_tv.png","Università degli Studi di Roma","Università degli Studi di Roma","Tor Vergata","http://web.uniroma2.it");
		
		 HorizontalPanel topleft = new HorizontalPanel();
		 topleft.setSize("100%","100%");
		 DOM.setStyleAttribute(topleft.getElement(), "padding", "10px");
		 DOM.setStyleAttribute(topleft.getElement(), "paddingLeft", "60px");
		 topleft.add(ackFao);
		 topleft.add(ackUnitov);
		 topleft.setCellHorizontalAlignment(ackFao, HasHorizontalAlignment.ALIGN_LEFT);
		 topleft.setCellHorizontalAlignment(ackUnitov, HasHorizontalAlignment.ALIGN_LEFT);
		 topleft.setCellWidth(ackFao, "50%");
		 topleft.setCellWidth(ackUnitov, "50%");
		
		VerticalPanel topleftContainer = new VerticalPanel();
		topleftContainer.setSize("100%", "100%");
		topleftContainer.add(ackleftTitle);
		topleftContainer.add(topleft);
		topleftContainer.setCellVerticalAlignment(ackleftTitle, HasVerticalAlignment.ALIGN_TOP);
		topleftContainer.setCellHorizontalAlignment(ackleftTitle, HasHorizontalAlignment.ALIGN_LEFT);
		
		return topleftContainer;
	 }
	 
	 private static Widget getFundedBy()
	 {
		VerticalPanel middle = new VerticalPanel();
		middle.setSize("100%","100%");
		
		HTML fundedBy = new HTML(constants.mainPageTitle()+" "+constants.fundedBy(), false);
		fundedBy.setStyleName("ackLabel");
		DOM.setStyleAttribute(fundedBy.getElement(), "padding", "10px");
		DOM.setStyleAttribute(fundedBy.getElement(), "paddingLeft", "30px");
		fundedBy.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		AcknowledgementWidget agINFRA = new AcknowledgementWidget("images/agINFRA_logo.jpg","agINFRA","agINFRA","(EC  7th framework program INFRA-2011-1.2.2, Grant agreement no: 283770)","http://aginfra.eu" );
		agINFRA.setImageSize("47", "36");
		AcknowledgementWidget SemaGrow = new AcknowledgementWidget("images/SemaGrow_logo.jpg","SemaGrow","SemaGrow","(EC 7th framework program ICT-2011.4.4, Grant agreement no: 318497)","http://www.semagrow.eu" );
		SemaGrow.setImageSize("112", "36");
		
		HorizontalPanel fundedByPanel = new HorizontalPanel();
		DOM.setStyleAttribute(fundedByPanel.getElement(), "padding", "10px");
		DOM.setStyleAttribute(fundedByPanel.getElement(), "paddingLeft", "60px");
		fundedByPanel.setSize("100%", "100%");
		fundedByPanel.add(agINFRA);
		fundedByPanel.add(SemaGrow);
		fundedByPanel.setCellHorizontalAlignment(agINFRA, HasHorizontalAlignment.ALIGN_LEFT);
		fundedByPanel.setCellHorizontalAlignment(SemaGrow, HasHorizontalAlignment.ALIGN_LEFT);
		
		middle.add(fundedBy);
		middle.add(fundedByPanel);
		
		return middle;
	 }

	 public static Widget getCollaborator()
	 {
		HTML ackrightTitle = new HTML(constants.otherPartner(), false);
		DOM.setStyleAttribute(ackrightTitle.getElement(), "paddingTop", "10px");
		DOM.setStyleAttribute(ackrightTitle.getElement(), "paddingLeft", "30px");
		ackrightTitle.setStyleName("ack-title");
			
		AcknowledgementWidget ackKu    	= new AcknowledgementWidget("images/logo_ku.gif","Kasetsart University","Kasetsart University","Bangkok, Thailand","http://www.ku.ac.th");
		AcknowledgementWidget ackAgris 	= new AcknowledgementWidget("images/logo_agris.gif","Thai National Agris Center","Thai National Agris Center","Bangkok, Thailand","http://thaiagris.lib.ku.ac.th/");
		AcknowledgementWidget ackMimos 	= new AcknowledgementWidget("images/mimos_logo.jpg","MIMOS Berhad","MIMOS Berhad","Kuala Lumpur, Malaysia","http://www.mimos.my/");
				
		HorizontalPanel topright = new HorizontalPanel();
		topright.setSize("100%","100%");
		DOM.setStyleAttribute(topright.getElement(), "padding", "10px");
		DOM.setStyleAttribute(topright.getElement(), "paddingLeft", "60px");
		topright.add(ackKu);
		topright.add(ackAgris);
		topright.add(ackMimos);
		topright.setCellHorizontalAlignment(ackKu, HasHorizontalAlignment.ALIGN_LEFT);
		topright.setCellHorizontalAlignment(ackAgris, HasHorizontalAlignment.ALIGN_LEFT);
		topright.setCellHorizontalAlignment(ackMimos, HasHorizontalAlignment.ALIGN_LEFT);
		
		VerticalPanel toprightContainer = new VerticalPanel();
		toprightContainer.setSize("100%", "100%");
		toprightContainer.add(ackrightTitle);
		toprightContainer.add(topright);
		toprightContainer.setCellVerticalAlignment(ackrightTitle, HasVerticalAlignment.ALIGN_TOP);
		toprightContainer.setCellHorizontalAlignment(ackrightTitle, HasHorizontalAlignment.ALIGN_LEFT);
		
		return toprightContainer;
	 }
	 
	 private static Widget getAgrovocParnters()
	 {
	 	HTML contrib = new HTML(constants.mainAck2(), false);
		contrib.setStyleName("ackLabel");
		contrib.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		DOM.setStyleAttribute(contrib.getElement(), "paddingTop", "10px");
		DOM.setStyleAttribute(contrib.getElement(), "paddingLeft", "30px");

		HTML ack = new HTML(constants.mainAck1(), false);
		ack.setStyleName("ackLabel");
		ack.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);	
		DOM.setStyleAttribute(ack.getElement(), "paddingTop", "10px");
		DOM.setStyleAttribute(ack.getElement(), "paddingLeft", "30px");
		
		AcknowledgementWidget icrisat = new AcknowledgementWidget("images/logo_icrisat.png","ICRISAT","ICRISAT","International Crops Research Institute for the Semi-Arid Tropics","http://www.icrisat.org" );
		icrisat.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);	
		DOM.setStyleAttribute(icrisat.getElement(), "padding", "5px");
		DOM.setStyleAttribute(icrisat.getElement(), "paddingLeft", "60px");
		
		AcknowledgementWidget maryLand = new AcknowledgementWidget(null, null, "Prof. Dagobert Soergel","UNIVERSITY OF MARYLAND",null);
		maryLand.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);	
		DOM.setStyleAttribute(maryLand.getElement(), "padding", "5px");
		DOM.setStyleAttribute(maryLand.getElement(), "paddingLeft", "60px");
		
		VerticalPanel leftBottom = new VerticalPanel();
		leftBottom.setSize("100%","100%");
		leftBottom.add(contrib);
		leftBottom.add(icrisat);

		VerticalPanel rightBottom = new VerticalPanel();
		rightBottom.setSize("100%","100%");
		rightBottom.add(ack);
		rightBottom.add(maryLand);

		HorizontalPanel bottom = new HorizontalPanel();
		bottom.setSize("100%","100%");
		bottom.add(leftBottom);
		bottom.add(rightBottom);
		
		return bottom;
	 }
}
