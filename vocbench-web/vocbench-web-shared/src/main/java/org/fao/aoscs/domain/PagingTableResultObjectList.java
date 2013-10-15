package org.fao.aoscs.domain;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

public class PagingTableResultObjectList extends LightEntity{

	private static final long serialVersionUID = -1411511127550796709L;
	private int resultTotalCount = 0;
	private ArrayList<Object> resultObjectList = new ArrayList<Object>();
	
	public int getResultTotalCount() {
		return resultTotalCount;
	}
	public void setResultTotalCount(int resultTotalCount) {
		this.resultTotalCount = resultTotalCount;
	}
	public ArrayList<Object> getResultObjectList() {
		return resultObjectList;
	}
	public void setResultObjectList(ArrayList<Object> resultObjectList) {
		this.resultObjectList = resultObjectList;
	}
	
	

	
}
