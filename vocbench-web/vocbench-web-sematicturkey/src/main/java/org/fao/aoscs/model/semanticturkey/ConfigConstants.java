package org.fao.aoscs.model.semanticturkey;

import java.util.HashMap;

public class ConfigConstants {

	public static boolean ISINDEXING = true;
	
	public static void loadConstants(HashMap<String, String> cMap)
	{
		ISINDEXING = Boolean.parseBoolean((String)cMap.get("ISINDEXING"));
	}
}
