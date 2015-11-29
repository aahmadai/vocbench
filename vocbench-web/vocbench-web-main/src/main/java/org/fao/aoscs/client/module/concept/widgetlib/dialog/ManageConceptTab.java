/**
 * 
 */
package org.fao.aoscs.client.module.concept.widgetlib.dialog;

import java.util.ArrayList;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTab;
import org.fao.aoscs.client.widgetlib.shared.dialog.FormDialogBox;
import org.fao.aoscs.client.widgetlib.shared.label.CheckBoxAOS;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author rajbhandari
 *
 */
public class ManageConceptTab extends FormDialogBox implements ClickHandler{
	
	private LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	private VerticalPanel vp = new VerticalPanel();
	private ArrayList<ConceptTab> tabVisibleList;
	private RadioButton selectall = new RadioButton("allOpt" , constants.buttonSelectAll());
	private RadioButton clearall = new RadioButton("allOpt" , constants.buttonClearAll());

	public ManageConceptTab(){
		super();
		this.setText(constants.conceptShowHideTabs());
		this.setWidth("400px");
		this.initLayout();
	}
		
	public void initLayout() {
		for (ConceptTab tab : ConceptTab.values()) {
			CheckBoxAOS<ConceptTab> chkBox = new CheckBoxAOS<ConceptTab>(tab.getSingularText(), tab);
			vp.add(chkBox);
		}
		
		selectall.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for(int i=0;i<vp.getWidgetCount();i++)
				{
					Widget w  = vp.getWidget(i);
					if(w instanceof CheckBoxAOS)
					{
						@SuppressWarnings("unchecked")
						CheckBoxAOS<Integer> chk = (CheckBoxAOS<Integer>) w;
						chk.setValue(selectall.getValue());
					}
				}
			}
		});
		
		clearall.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for(int i=0;i<vp.getWidgetCount();i++)
				{
					Widget w  = vp.getWidget(i);
					if(w instanceof CheckBoxAOS)
					{
						@SuppressWarnings("unchecked")
						CheckBoxAOS<Integer> chk = (CheckBoxAOS<Integer>) w;
						chk.setValue(false);
					}
				}
			}
		});
		
		HorizontalPanel leftBottomWidget = new HorizontalPanel();
		leftBottomWidget.add(selectall);
		leftBottomWidget.add(clearall);
		
		setLeftBottomWidget(leftBottomWidget);
		addWidget(vp);
	}
	
	public HandlerRegistration addSubmitClickHandler(ClickHandler handler) {
		return this.submit.addClickHandler(handler);
	}
	
	
	public void onClick(ClickEvent event) 
	{
		Widget sender = (Widget) event.getSource();
		if(sender.equals(submit))
		{
			onSubmit();
		}else if(sender.equals(cancel))
		{
			onCancel();			
		}
		onButtonClicked(sender);
	}
	
	public void show(ArrayList<ConceptTab> tabVisibleList)
	{
		this.tabVisibleList = tabVisibleList;
		setSelectedTab();
		super.show();
	}
	
	@SuppressWarnings("unchecked")
	public void setSelectedTab()
	{
		for(int i=0;i<vp.getWidgetCount();i++)
		{
			Widget w  = vp.getWidget(i);
			if(w instanceof CheckBoxAOS)
			{
				CheckBoxAOS<Integer> chk = (CheckBoxAOS<Integer>) w;
				chk.setValue(tabVisibleList.contains(chk.getObject()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ConceptTab> getSelectedTab()
	{
		ArrayList<ConceptTab> list = new ArrayList<ConceptTab>();
		for(int i=0;i<vp.getWidgetCount();i++)
		{
			Widget w  = vp.getWidget(i);
			if(w instanceof CheckBoxAOS)
			{
				CheckBoxAOS<ConceptTab> chk = (CheckBoxAOS<ConceptTab>) w;
				if(chk.getValue())
					list.add(chk.getObject());
			}
		}
		return list;
	}
}
