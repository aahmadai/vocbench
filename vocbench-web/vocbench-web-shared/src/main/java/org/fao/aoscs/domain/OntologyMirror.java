package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

public class OntologyMirror extends LightEntity{

	private static final long serialVersionUID = -4906262255940083283L;
	
	private String namespace; 
	private String localFile;
	
	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}
	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	/**
	 * @return the localFile
	 */
	public String getLocalFile() {
		return localFile;
	}
	/**
	 * @param localFile the localFile to set
	 */
	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}
	
}
