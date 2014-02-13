package org.fao.aoscs.domain;

import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * @author sachit
 *
 */
public class DBMigrationObject extends LightEntity{
	
	private static final long serialVersionUID = -3358182041592130554L;
	private String version;
	private String description;
	private String type;
	private String script;
	private Integer checksum;
	private Date installedOn;
	private Integer executionTime;
	private String state;
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}
	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}
	/**
	 * @return the checksum
	 */
	public Integer getChecksum() {
		return checksum;
	}
	/**
	 * @param checksum the checkSum to set
	 */
	public void setChecksum(Integer checksum) {
		this.checksum = checksum;
	}
	/**
	 * @return the installedOn
	 */
	public Date getInstalledOn() {
		return installedOn;
	}
	/**
	 * @param installedOn the installedOn to set
	 */
	public void setInstalledOn(Date installedOn) {
		this.installedOn = installedOn;
	}
	/**
	 * @return the executionTime
	 */
	public Integer getExecutionTime() {
		return executionTime;
	}
	/**
	 * @param executionTime the executionTime to set
	 */
	public void setExecutionTime(Integer executionTime) {
		this.executionTime = executionTime;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
     * Checks if this migration is installed.
     */
	public boolean isInstalled()
	{
		return this.getState().equals("PREINIT") || this.getState().equals("SUCCESS")  || this.getState().equals("MISSING_SUCCESS") || this.getState().equals("FUTURE_SUCCESS") || this.getState().contains("OUT_OF_ORDER");
	}

}
