package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Partner extends VerticalPanel{
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	public Partner() {
		super();

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(10);
		DOM.setStyleAttribute(vp.getElement(), "paddingLeft", "25px");	
		vp.add(getDevelopedBy());
		
		vp.add(new HTML("<hr/>"));
		vp.add(getFundedBy());		
		
		if(ConfigConstants.PARTNERS.equalsIgnoreCase("FAO"))
		{
			vp.add(new HTML("<hr/>"));
			vp.add(getCollaboratingPartners());
			
			vp.add(new HTML("<hr/>"));
			vp.add(getAgrovocParnters());		
		}
		
		ScrollPanel sc = new ScrollPanel();
		sc.setSize("530px", "400px");			
		sc.add(vp);
		this.add(sc);
		
		/*Frame frame = new Frame();
		frame.setSize("530px", "400px");
		frame.setUrl(GWT.getHostPageBaseURL()+ "partner.html");
		this.add(frame);*/
	}
	
	private Widget getDevelopedBy()
	{
		HTML ackTitle = new HTML(constants.mainPageTitle()+" "+constants.mainPartner());
		DOM.setStyleAttribute(ackTitle.getElement(), "paddingTop", "10px");
		ackTitle.setStyleName("ack-title");
		 
		AcknowledgementWidget ackFao   = new AcknowledgementWidget("images/logo_fao.jpg","Food and Agriculture Organization of the United Nations","Food and Agriculture Organization of the United Nations","for a world without hunger","http://www.fao.org");
		AcknowledgementWidget ackUnitov 	= new AcknowledgementWidget("images/logo_tv1.png","Università degli Studi di Roma","Università degli Studi di Roma","Tor Vergata","http://web.uniroma2.it");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(5);
		vp.setSize("100%", "100%");
		vp.add(ackTitle);
		vp.add(ackFao);			
		vp.add(ackUnitov);
		DOM.setStyleAttribute(ackFao.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackUnitov.getElement(), "paddingLeft", "10px");
		return vp;
	}
	
	
	private Widget getCollaboratingPartners()
	{
		HTML ackTitlePartners = new HTML(constants.otherPartner());
		DOM.setStyleAttribute(ackTitlePartners.getElement(), "paddingTop", "10px");
		ackTitlePartners.setStyleName("ack-title");
		
		AcknowledgementWidget ackKu    = new AcknowledgementWidget("images/logo_ku.jpg","Kasetsart University","Kasetsart University","Bangkok, Thailand","http://www.ku.ac.th");
		AcknowledgementWidget ackAgris = new AcknowledgementWidget("images/logo_agris.jpg","Thai National Agris Center","Thai National Agris Center","Bangkok, Thailand","http://thaiagris.lib.ku.ac.th/");
		AcknowledgementWidget ackMimos 	= new AcknowledgementWidget("images/mimos_logo_big.jpg","MIMOS Berhad","MIMOS Berhad","Kuala Lumpur, Malaysia","http://www.mimos.my/");
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(5);
		vp.setSize("100%", "100%");
		vp.add(ackTitlePartners);
		vp.add(ackKu);
		vp.add(ackAgris);
		vp.add(ackMimos);
		DOM.setStyleAttribute(ackKu.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackAgris.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackMimos.getElement(), "paddingLeft", "10px");
		return vp;
	}
	
	private Widget getFundedBy()
	{
		HTML fundedBy = new HTML(constants.mainPageTitle()+" "+constants.fundedBy(), false);
		fundedBy.setStyleName("ackLabel");
		fundedBy.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		AcknowledgementWidget agINFRA = new AcknowledgementWidget("images/agINFRA_logo.jpg","agINFRA","agINFRA","(EC  7th framework program INFRA-2011-1.2.2, Grant agreement no: 283770)","http://aginfra.eu" );
		agINFRA.setImageSize("47", "36");
		AcknowledgementWidget SemaGrow = new AcknowledgementWidget("images/SemaGrow_logo.jpg","SemaGrow","SemaGrow","(EC 7th framework program ICT-2011.4.4, Grant agreement no: 318497)","http://www.semagrow.eu" );
		SemaGrow.setImageSize("112", "36");
		
		VerticalPanel vpFunded = new VerticalPanel();
		vpFunded.setSpacing(10);
		vpFunded.setSize("100%", "100%");
		vpFunded.add(agINFRA);	
		vpFunded.add(SemaGrow);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(5);
		vp.setSize("100%", "100%");
		vp.add(fundedBy);		
		vp.add(vpFunded);	
		return vp;
	}
	
	private Widget getAgrovocParnters()
	{
		HTML ack = new HTML(constants.mainAck2());
		ack.setStyleName("ackLabel");
		ack.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		AcknowledgementWidget ackIcrisat = new AcknowledgementWidget("images/logo_icrisat.png","ICRISAT","ICRISAT","International Crops Research Institute for the Semi-Arid Tropics","http://www.icrisat.org" );
		
		HTML contrib = new HTML(constants.mainAck1());
		contrib.setStyleName("ackLabel");
		contrib.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		AcknowledgementWidget ackProf = new AcknowledgementWidget(null,null,"Prof. Dagobert Soergel","UNIVERSITY OF MARYLAND",null);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(5);
		vp.setSize("100%", "100%");
		vp.add(ack);		
		vp.add(ackIcrisat);
		vp.add(new HTML("<hr/>"));
		vp.add(contrib);				
		vp.add(ackProf);	
		DOM.setStyleAttribute(ackIcrisat.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackProf.getElement(), "paddingLeft", "10px");
		return vp;
	}
	
}
