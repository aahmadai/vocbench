package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;


public class TreePathObject extends LightEntity{

	private static final long serialVersionUID = 6484016629446705227L;

	private HashMap<String,NtreeItemObject > itemList = new HashMap<String,NtreeItemObject >();


	private TreeObject rootItem = new TreeObject();
	
	public HashMap<String,NtreeItemObject> getItemList() {
		return itemList;
	}
	
	public void setItemList(
			HashMap<String, NtreeItemObject> itemList) {
		this.itemList = itemList;
	}
	
	public void addItemList(NtreeItemObject ntObj) {
		if(!this.itemList.containsKey(ntObj.getName())){
			this.itemList.put(ntObj.getName(), ntObj);
		}
	}
	public TreeObject getRootItem() {
		return rootItem;
	}
	public void setRootItem(TreeObject rootItem) {
		this.rootItem = rootItem;
	}
	public boolean isEmpty(){
		return itemList.isEmpty();
	}
	
	public boolean hasItemInPath(String URI){
		return itemList.containsKey(URI);
	}
	public NtreeItemObject getItemInPath(String name){
		if(itemList.containsKey(name)){
			return (NtreeItemObject) itemList.get(name);
		}else{
			return null;
		}
	}
}
