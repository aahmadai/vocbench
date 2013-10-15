package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ValidationFilter extends LightEntity {

	private static final long serialVersionUID = 9030480177168627213L;

	private int userID = 0;
	
	private int groupID = 0;
	
	private OntologyInfo ontoInfo = new OntologyInfo();
	
	private ArrayList<Integer> selectedStatusList = new ArrayList<Integer>(); 

	private ArrayList<Integer> selectedUserList = new ArrayList<Integer>();

	private ArrayList<Integer> selectedActionList = new ArrayList<Integer>();
	
	private ArrayList<String> selectedLanguageList = new ArrayList<String>();
	
	private Date fromDate =  null;
	
	private Date toDate =  null;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public OntologyInfo getOntoInfo() {
		return ontoInfo;
	}

	public void setOntoInfo(OntologyInfo ontoInfo) {
		this.ontoInfo = ontoInfo;
	}

	public ArrayList<Integer> getSelectedStatusList() {
		return selectedStatusList;
	}

	public void setSelectedStatusList(ArrayList<Integer> arrayList) {
		this.selectedStatusList = arrayList;
	}
	
	public void addSelectedStatusList(Integer value)
	{
		this.selectedStatusList.add(value);	
	}
	
	public void removeSelectedStatusList(Integer value)
	{
		this.selectedStatusList.remove(value);	
	}
	
	public ArrayList<Integer> getSelectedUserList() {
		return selectedUserList;
	}

	public void setSelectedUserList(ArrayList<Integer> arrayList) {
		this.selectedUserList = arrayList;
	}

	public void addSelectedUserList(Integer value)
	{
		this.selectedUserList.add(value);	
	}
	
	public void removeSelectedUserList(Integer value)
	{
		this.selectedUserList.remove(value);	
	}
	
	public ArrayList<Integer> getSelectedActionList() {
		return selectedActionList;
	}

	public void setSelectedActionList(ArrayList<Integer> arrayList) {
		this.selectedActionList = arrayList;
	}
	
	public void addSelectedActionList(Integer value)
	{
		this.selectedActionList.add(value);	
	}
	
	public void removeSelectedActionList(Integer value)
	{
		this.selectedActionList.remove(value);	
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public ArrayList<String> getSelectedLanguageList() {
		return selectedLanguageList;
	}

	public void setSelectedLanguageList(ArrayList<String> selectedLanguageList) {
		this.selectedLanguageList = selectedLanguageList;
	}
	
}
