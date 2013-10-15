package org.fao.aoscs.client.widgetlib.shared.filter;

import java.util.ArrayList;

import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.module.validation.widgetlib.Validator;
import org.fao.aoscs.domain.Users;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class FilterCheckBox extends Composite{
	private CheckBox cb = new CheckBox();
	private String label;
	private Object value;	
	private HorizontalPanel panel = new HorizontalPanel();
	private ArrayList<FilterCheckBox> childList = new ArrayList<FilterCheckBox>();
	private FilterCheckBox parentCheckBox; 
	@SuppressWarnings("rawtypes")
	private ArrayList selectedList;
		
	@SuppressWarnings("rawtypes")
	public FilterCheckBox(ArrayList selectedList, ArrayList<FilterCheckBox> list, final String label, final Object value)
	{
		this.label = label;
		this.value = value;
		this.childList = list;
		this.selectedList = selectedList;
		panel.add(cb);
		Label lab = new Label(label);
		if(value instanceof Users)
		{
			Users u = (Users) value;
			lab = (Label)Validator.makeUsers(label,""+u.getUserId(),Style.Link);
		}
		panel.add(lab);
		panel.setCellVerticalAlignment(lab, HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setSpacing(3);
		initWidget(panel);
	}
	
	@SuppressWarnings("unchecked")
	public boolean setCheck(boolean check)
	{
		cb.setValue(check);
		if(check)
		{
			if(!selectedList.contains(value))
				selectedList.add(value);
		}
		else
		{
			if(selectedList.contains(value))
				selectedList.remove(value);
		}
		if(!childList.isEmpty())
		{
			for(int i=0;i<childList.size();i++)
			{
				FilterCheckBox item = (FilterCheckBox) childList.get(i);
				item.setCheck(check);
				
			}
		}		
		// check siblings
		FilterCheckBox parent = getParentCheckBox();		
		boolean chkSe = check;
		if(parent!=null)
		{
			chkSe = true;			
			ArrayList<FilterCheckBox> siblingList = parent.childList;
			
			if(!check) 
			{
				chkSe = false;
			}
			else
			{
				if(!siblingList.isEmpty())
				{
					for(int i=0;i<siblingList.size();i++)
					{
						FilterCheckBox item = (FilterCheckBox) siblingList.get(i);
						
						if(!item.isCheck())
						{
							chkSe = false;
							break;
						}
					}
				}
			}
			parent.getCb().setValue(chkSe);
		}
		return chkSe;		
	}
	public void addChildList(FilterCheckBox item){
		childList.add(item);
	}
	public boolean isCheck(){
		return cb.getValue();
	}
	public ArrayList<FilterCheckBox> getChildList(){
		return childList;
	}
	public void setChildList(ArrayList<FilterCheckBox> childList) {
		this.childList = childList;
	}
	public Object getValue(){
		return value;
	}
	public void setValue(Object value)
	{
		this.value=value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public FilterCheckBox getParentCheckBox() {
		return parentCheckBox;
	}
	public void setParentCheckBox(FilterCheckBox parentCheckBox) {
		this.parentCheckBox = parentCheckBox;
	}
	public CheckBox getCb() {
		return cb;
	}
	public void setCb(CheckBox cb) {
		this.cb = cb;
	}

}
