package org.fao.aoscs.model.semanticturkey;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class STModelConstants {
	
	protected static Logger logger = LoggerFactory.getLogger(STModelConstants.class);
	
	//VOCBENCH
	public static String VOCBENCHNAMESPACE;
	public static String HASSTATUS;	
	
	//Dublin Core Terms
	public static String DCTNAMESPACE;
	public static String DCTCREATED;
	public static String DCTMODIFIED;
	
	public static void loadConstants(HashMap<String, String> cMap)
	{
		VOCBENCHNAMESPACE 		= cMap.get("VOCBENCHNAMESPACE");
		HASSTATUS  				= cMap.get("HASSTATUS");
		
		DCTNAMESPACE  			= cMap.get("DCTNAMESPACE");
		DCTCREATED  			= cMap.get("DCTCREATED");
		DCTMODIFIED  			= cMap.get("DCTMODIFIED");
	}
	
}
