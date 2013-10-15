package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author rajbhandari
 *
 */
public class ImportPathObject extends LightEntity{

	private static final long serialVersionUID = 6484016629446705227L;

	private HashMap<String, ImportObject> importList = new HashMap<String, ImportObject>();
	private HashMap<String, ArrayList<String>> childList = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> rootItems = new ArrayList<String>();
	
	public void addImportObject(ImportObject impObj, String parentURI)
	{
		String uri = impObj.getUri();
		if(!importList.containsKey(uri))
			importList.put(uri, impObj);
		
		if(parentURI==null)
		{
			rootItems.add(uri);
			childList.put(uri, new ArrayList<String>());
		}
		else
		{
			ArrayList<String> list = new ArrayList<String>();
			if(childList.containsKey(parentURI)){
				list = childList.get(parentURI);
				childList.remove(parentURI);
			}
			if(!list.contains(uri))
				list.add(uri);
			this.childList.put(parentURI, list);
		}
		
	}
	
	public ImportObject getImportObject(String uri)
	{
		return importList.get(uri);
	}
	
	public ArrayList<String> getChildImportObject(String uri)
	{
		return childList.get(uri);
	}
	
	/**
	 * @return the rootItem
	 */
	public ArrayList<String> getRootItems() {
		return rootItems;
	}

	/**
	 * @param rootItem the rootItem to set
	 */
	public void setRootItems(ArrayList<String> rootItems) {
		this.rootItems = rootItems;
	}

	/**
	 * @return the importList
	 */
	public HashMap<String, ImportObject> getImportList() {
		return importList;
	}

	/**
	 * @param importList the importList to set
	 */
	public void setImportList(HashMap<String, ImportObject> importList) {
		this.importList = importList;
	}

	/**
	 * @return the childList
	 */
	public HashMap<String, ArrayList<String>> getChildList() {
		return childList;
	}

	/**
	 * @param childList the childList to set
	 */
	public void setChildList(HashMap<String, ArrayList<String>> childList) {
		this.childList = childList;
	}

	
}
