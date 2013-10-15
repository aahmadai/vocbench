package org.fao.aoscs.domain;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author rajbhandari
 *
 */
public class VBConfig extends LightEntity {

	private static final long serialVersionUID = 4280670875631407291L;
	
	//Mail Configuration
	private String vbMailHost;
	private String vbMailPort;
	private String vbMailUser;
	private String vbMailPassword;
	private String vbMailFrom;
	private String vbMailFromAlias;
	private String vbMailAdmin;
	
	//Semantic Turkey Configuration
	private String stMethod;
	private String stIP;
	private String stPort;
	private String stPath;
	
	/**
	 * @return the vbMailHost
	 */
	public String getVbMailHost() {
		return vbMailHost;
	}
	/**
	 * @param vbMailHost the vbMailHost to set
	 */
	public void setVbMailHost(String vbMailHost) {
		this.vbMailHost = vbMailHost;
	}
	/**
	 * @return the vbMailPort
	 */
	public String getVbMailPort() {
		return vbMailPort;
	}
	/**
	 * @param vbMailPort the vbMailPort to set
	 */
	public void setVbMailPort(String vbMailPort) {
		this.vbMailPort = vbMailPort;
	}
	/**
	 * @return the vbMailUser
	 */
	public String getVbMailUser() {
		return vbMailUser;
	}
	/**
	 * @param vbMailUser the vbMailUser to set
	 */
	public void setVbMailUser(String vbMailUser) {
		this.vbMailUser = vbMailUser;
	}
	/**
	 * @return the vbMailPassword
	 */
	public String getVbMailPassword() {
		return vbMailPassword;
	}
	/**
	 * @param vbMailPassword the vbMailPassword to set
	 */
	public void setVbMailPassword(String vbMailPassword) {
		this.vbMailPassword = vbMailPassword;
	}
	/**
	 * @return the vbMailFrom
	 */
	public String getVbMailFrom() {
		return vbMailFrom;
	}
	/**
	 * @param vbMailFrom the vbMailFrom to set
	 */
	public void setVbMailFrom(String vbMailFrom) {
		this.vbMailFrom = vbMailFrom;
	}
	/**
	 * @return the vbMailFromAlias
	 */
	public String getVbMailFromAlias() {
		return vbMailFromAlias;
	}
	/**
	 * @param vbMailFromAlias the vbMailFromAlias to set
	 */
	public void setVbMailFromAlias(String vbMailFromAlias) {
		this.vbMailFromAlias = vbMailFromAlias;
	}
	/**
	 * @return the vbMailAdmin
	 */
	public String getVbMailAdmin() {
		return vbMailAdmin;
	}
	/**
	 * @param vbMailAdmin the vbMailAdmin to set
	 */
	public void setVbMailAdmin(String vbMailAdmin) {
		this.vbMailAdmin = vbMailAdmin;
	}
	/**
	 * @return the stMethod
	 */
	public String getStMethod() {
		return stMethod;
	}
	/**
	 * @param stMethod the stMethod to set
	 */
	public void setStMethod(String stMethod) {
		this.stMethod = stMethod;
	}
	/**
	 * @return the stIP
	 */
	public String getStIP() {
		return stIP;
	}
	/**
	 * @param stIP the stIP to set
	 */
	public void setStIP(String stIP) {
		this.stIP = stIP;
	}
	/**
	 * @return the stPort
	 */
	public String getStPort() {
		return stPort;
	}
	/**
	 * @param stPort the stPort to set
	 */
	public void setStPort(String stPort) {
		this.stPort = stPort;
	}
	/**
	 * @return the stPath
	 */
	public String getStPath() {
		return stPath;
	}
	/**
	 * @param stPath the stPath to set
	 */
	public void setStPath(String stPath) {
		this.stPath = stPath;
	}
}
