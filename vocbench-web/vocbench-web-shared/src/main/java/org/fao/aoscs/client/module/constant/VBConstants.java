package org.fao.aoscs.client.module.constant;

import java.util.HashMap;

public class VBConstants {
	
	public static String SANDBOXLINK;
	public static String WEBSERVICESINFO;
	public static String VOCBENCHINFO;
	public static String CONTACTUS;
	public static String COPYRIGHTLINK;
	public static String ARTGROUPLINK;
	public static String FLINTEDITORLINK;
	
	public static void loadConstants(HashMap<String, String> cMap)
	{
		SANDBOXLINK				= cMap.get("PAGE.SANDBOXLINK");
		WEBSERVICESINFO			= cMap.get("PAGE.WEBSERVICESINFO");
		VOCBENCHINFO			= cMap.get("PAGE.VOCBENCHINFO");
		CONTACTUS				= cMap.get("PAGE.CONTACTUS");
		COPYRIGHTLINK			= cMap.get("PAGE.COPYRIGHTLINK");
		ARTGROUPLINK			= cMap.get("PAGE.ARTGROUPLINK");
		FLINTEDITORLINK			= cMap.get("PAGE.FLINTEDITORLINK");
	}
}
