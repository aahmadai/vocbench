package org.fao.aoscs.client.widgetlib.Main;

import org.fao.aoscs.client.locale.LocaleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Partner extends VerticalPanel{
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);

	public Partner() {
		super();
		 		 
		HTML ackTitle = new HTML(constants.mainPartner());
		DOM.setStyleAttribute(ackTitle.getElement(), "paddingTop", "10px");
		ackTitle.setStyleName("ack-title");
		 
		AcknowledgementWidget ackFao   = new AcknowledgementWidget("images/logo_fao.jpg","Food and Agriculture Organization of the United Nations","Food and Agriculture Organization of the United Nations","for a world without hunger","http://www.fao.org");
		AcknowledgementWidget ackKu    = new AcknowledgementWidget("images/logo_ku.jpg","Kasetsart University","Kasetsart University","Bangkok, Thailand","http://www.ku.ac.th");
		AcknowledgementWidget ackAgris = new AcknowledgementWidget("images/logo_agris.jpg","Thai National Agris Center","Thai National Agris Center","Bangkok, Thailand","http://thaiagris.lib.ku.ac.th/");
		AcknowledgementWidget ackMimos 	= new AcknowledgementWidget("images/mimos_logo_big.jpg","MIMOS Berhad","MIMOS Berhad","Kuala Lumpur, Malaysia","http://www.mimos.my/");
		AcknowledgementWidget ackUnitov 	= new AcknowledgementWidget("images/logo_tv1.png","Università degli Studi di Roma","Università degli Studi di Roma","Tor Vergata","http://web.uniroma2.it");
		
		HTML contrib = new HTML(constants.mainAck1());
		contrib.setStyleName("ackLabel");
		contrib.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
				 
		HTML ack = new HTML(constants.mainAck2());
		ack.setStyleName("ackLabel");
		ack.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		HTML agINFRA1 = new HTML("VocBench is partially funded by the  agINFRA project", false);
		agINFRA1.setStyleName("ackLabel");
		agINFRA1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		AcknowledgementWidget agINFRA2 = new AcknowledgementWidget("images/agINFRA_logo.jpg","agINFRA","agINFRA","(EC  7th framework program INFRA-2011-1.2.2, Grant agreement no: 283770)","http://aginfra.eu" );
		agINFRA2.setImageSize("47", "36");
		AcknowledgementWidget ackIcrisat = new AcknowledgementWidget("images/logo_icrisat.png","ICRISAT","ICRISAT","International Crops Research Institute for the Semi-Arid Tropics","http://www.icrisat.org" );
		AcknowledgementWidget ackProf = new AcknowledgementWidget(null,null,"Prof. Dagobert Soergel","UNIVERSITY OF MARYLAND",null);
				
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(10);
		DOM.setStyleAttribute(vp.getElement(), "paddingLeft", "25px");	
		vp.add(ackTitle);
		vp.add(ackFao);			
		vp.add(ackKu);
		vp.add(ackAgris);
		vp.add(ackMimos);
		vp.add(ackUnitov);
		vp.add(new HTML("<hr/>"));
		vp.add(agINFRA1);		
		vp.add(agINFRA2);	
		vp.add(new HTML("<hr/>"));
		vp.add(ack);		
		vp.add(ackIcrisat);
		vp.add(new HTML("<hr/>"));
		vp.add(contrib);				
		vp.add(ackProf);		
		DOM.setStyleAttribute(ackFao.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackKu.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackAgris.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackMimos.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackUnitov.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackIcrisat.getElement(), "paddingLeft", "10px");
		DOM.setStyleAttribute(ackProf.getElement(), "paddingLeft", "10px");
		this.add(vp);		
	}	
}
