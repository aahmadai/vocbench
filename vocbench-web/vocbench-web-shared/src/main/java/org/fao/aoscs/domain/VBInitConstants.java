package org.fao.aoscs.domain;

import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class VBInitConstants extends LightEntity{


	private static final long serialVersionUID = 5546965938941164106L;
	
	private String buildVersion;
	private HashMap<String, String> VBConstants = new HashMap<String, String>();
	/**
	 * @return the buildVersion
	 */
	public String getBuildVersion() {
		return buildVersion;
	}
	/**
	 * @param buildVersion the buildVersion to set
	 */
	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}
	/**
	 * @return the vBConstants
	 */
	public HashMap<String, String> getVBConstants() {
		return VBConstants;
	}
	/**
	 * @param vBConstants the vBConstants to set
	 */
	public void setVBConstants(HashMap<String, String> vBConstants) {
		VBConstants = vBConstants;
	}
}