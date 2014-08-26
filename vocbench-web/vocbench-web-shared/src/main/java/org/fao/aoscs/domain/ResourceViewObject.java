package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class ResourceViewObject extends LightEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6807685537099350482L;
	
	private ARTURIResourceObject resource;
	private HashMap<String, ArrayList<PredicateObjects>> resourceList = new HashMap<String, ArrayList<PredicateObjects>>();

	/**
	 * @return the resource
	 */
	public ARTURIResourceObject getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(ARTURIResourceObject resource) {
		this.resource = resource;
	}

	/**
	 * @return the resourceList
	 */
	public HashMap<String, ArrayList<PredicateObjects>> getResourceList() {
		return resourceList;
	}

	/**
	 * @param resourceList the resourceList to set
	 */
	public void setResourceList(HashMap<String, ArrayList<PredicateObjects>> resourceList) {
		this.resourceList = resourceList;
	}
	
	/**
	 * @param resourceType
	 * @param resource
	 */
	public void addResourceList(String resourceType, ArrayList<PredicateObjects> resource)
	{
		resourceList.put(resourceType, resource);
	}
	
	/**
	 * @param resourceType
	 */
	public void deleteResourceList(String resourceType)
	{
		resourceList.remove(resourceType);
	}

}
