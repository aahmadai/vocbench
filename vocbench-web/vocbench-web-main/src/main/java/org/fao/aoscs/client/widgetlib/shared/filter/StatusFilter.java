package org.fao.aoscs.client.widgetlib.shared.filter;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.domain.OwlStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatusFilter extends Composite{

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel statusPanel  = new VerticalPanel();
	
	public StatusFilter(ArrayList<OwlStatus> statusList, HashMap<Integer, Integer> acceptValidationList, HashMap<Integer, Integer> rejectValidationList)
	{
		final CheckBox all = new CheckBox(constants.valAll());
		
		statusPanel.clear();
		statusPanel.setStyleName(Style.filter_panel_border);
		statusPanel.addStyleName(Style.filter_panel_background);
		statusPanel.setSize("100%", "100%");
		
		Label lb = new Label(constants.valFilterStatus());
		lb.setWidth("100%");
		lb.setStyleName(Style.filter_title_background);		
		
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<statusList.size();i++){
			OwlStatus os = (OwlStatus)statusList.get(i);
			if(acceptValidationList.containsKey(new Integer(os.getId())) || rejectValidationList.containsKey(new Integer(os.getId())))
			{
				final FilterCheckBox item = new FilterCheckBox(MainApp.vFilter.getSelectedStatusList(), new ArrayList<FilterCheckBox>(), os.getStatus(), os.getId());
				final CheckBox cb = item.getCb();
				cb.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						item.setCheck(cb.getValue());
						boolean chk = true;
						if(cb.getValue())
						{							 
							for(int i=0;i<itemContainer.size();i++){							
								FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
								if(!tmp.isCheck()){
									chk = false;
									break;
								}
							}
						}
						else {
							chk = false;
						}
						all.setValue(chk);					
					}
				});
				item.setCheck(MainApp.vFilter.getSelectedStatusList().contains((Integer)item.getValue()));
				itemContainer.add(item);
				vp.add(item);
			}
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setStyleName(Style.filter_scroll_background);
		sc.setSize("100%", "195px");
		sc.add(vp);
		
		all.setWidth("100%");		
		all.addStyleName(Style.font_11);		
		boolean allChk = true;
		for(int i=0;i<itemContainer.size();i++){
			FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
			if(!tmp.isCheck())
			{
				allChk = false;
				break;
			}
		}
		all.setValue(allChk);
		all.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(all.getValue()){
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(true);
					}
				}else{
					for(int i=0;i<itemContainer.size();i++){
						FilterCheckBox tmp = (FilterCheckBox) itemContainer.get(i);
						tmp.setCheck(false);
					}
				}
			}
		});
		
		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.setSpacing(3);
		bottomPanel.add(all);		
		bottomPanel.setCellVerticalAlignment(all, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomPanel.setCellHorizontalAlignment(all, HasHorizontalAlignment.ALIGN_RIGHT);
		
		HorizontalPanel lPanel = new HorizontalPanel();
		lPanel.setStyleName("bottombar");
		lPanel.setSize("100%","100%");		
		lPanel.add(bottomPanel);
		lPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		lPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
		
		
		statusPanel.add(lb);
		statusPanel.add(sc);
		statusPanel.setCellWidth(sc, "100%");
		statusPanel.setCellHeight(sc, "100%");
		statusPanel.add(lPanel);
	
		initWidget(statusPanel);
	}

}
