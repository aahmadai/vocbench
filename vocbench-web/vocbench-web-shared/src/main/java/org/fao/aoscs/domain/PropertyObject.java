package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class PropertyObject extends LightEntity {

	private static final long serialVersionUID = 7513698801301663537L;
	private String uri;
	private String name ;
	private boolean deleteForbidden = false;
	private String type;
	
	private String parent;
	private boolean rootItem = false;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDeleteForbidden() {
		return deleteForbidden;
	}
	public void setDeleteForbidden(boolean deleteForbidden) {
		this.deleteForbidden = deleteForbidden;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public boolean isRootItem() {
		return rootItem;
	}
	public void setRootItem(boolean rootItem) {
		this.rootItem = rootItem;
	}
	
}
