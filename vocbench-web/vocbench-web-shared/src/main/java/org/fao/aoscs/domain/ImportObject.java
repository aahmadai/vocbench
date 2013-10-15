/**
 * 
 */
package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author rajbhandari
 *
 */
public class ImportObject extends LightEntity{
	
	private static final long serialVersionUID = -735891008487723096L;

	private static final String LOCAL = "LOCAL";
	private static final String WEB = "WEB";
	
	private String localfile;
	private String status;
	private String uri;
	
	
	public boolean isLocal()
	{
		return getStatus().equals(LOCAL);
	}
	
	public boolean isWeb()
	{
		return getStatus().equals(WEB);
	}
	
	/**
	 * @return the localfile
	 */
	public String getLocalfile() {
		return localfile;
	}
	/**
	 * @param localfile the localfile to set
	 */
	public void setLocalfile(String localfile) {
		this.localfile = localfile;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}
