package org.fao.aoscs.client.widgetlib.shared.filter;

import java.util.ArrayList;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.domain.OwlAction;

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

public class ActionFilter extends Composite {

	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel actionPanel  = new VerticalPanel();
	
	public ActionFilter(ArrayList<OwlAction> actionList)
	{
		final CheckBox all = new CheckBox(constants.valAll()); 

		actionPanel.clear();		
		actionPanel.setStyleName(Style.filter_panel_border);		
		actionPanel.addStyleName(Style.filter_panel_background);
		actionPanel.setSize("100%", "417px");
		
		Label lb = new Label(constants.valFilterAction());
		lb.setWidth("100%");							
		lb.setStyleName(Style.filter_title_background);		
		
		final ArrayList<FilterCheckBox> itemContainer = new ArrayList<FilterCheckBox>();
		VerticalPanel vp = new VerticalPanel();
		for(int i=0;i<actionList.size();i++){
			OwlAction act = (OwlAction) actionList.get(i);
			String actionValue = act.getAction();
			if(!act.getActionChild().equals(""))
				actionValue += "-"+act.getActionChild();
			final FilterCheckBox item = new FilterCheckBox(MainApp.vFilter.getSelectedActionList(), new ArrayList<FilterCheckBox>(), actionValue, act.getId());
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
			item.setCheck(MainApp.vFilter.getSelectedActionList().contains((Integer)item.getValue()));
			itemContainer.add(item);
			vp.add(item);
		}
		ScrollPanel sc = new ScrollPanel();
		sc.setSize("100%", "417px");
		sc.setStyleName(Style.filter_scroll_background);
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
				
		actionPanel.add(lb);
		actionPanel.add(sc);
		actionPanel.setCellWidth(sc, "100%");
		actionPanel.setCellHeight(sc, "100%");
		actionPanel.add(lPanel);
		initWidget(actionPanel);
	}
	
}

