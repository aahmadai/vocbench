package org.fao.aoscs.client.module.constant;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ConfigObject;
import org.fao.aoscs.domain.VBConfigInfo;

/**
 * @author sachit
 *
 */
public class ConfigConstants {
	
	@VBConfigInfo(key="DB.CONNECTIONURL", description="Database Connection URL: modify according to your DB installation", defaultValue="jdbc:mysql://localhost:3306/administrator_version20?createDatabaseIfNotExist=true&requireSSL=false&useUnicode=true&characterEncoding=utf-8", mandatory=true)
	public static String DB_CONNECTIONURL;
	
	@VBConfigInfo(key="DB.USERNAME", description="Database Username", defaultValue="root", mandatory=true)
	public static String DB_USERNAME;
	
	@VBConfigInfo(key="DB.PASSWORD", description="Database Password", defaultValue="", mandatory=true)
	public static String DB_PASSWORD;
	
	@VBConfigInfo(key="EMAIL.HOST", description="SMTP Mail Server IP/URL", defaultValue="smtp.gmail.com", mandatory=true)
	public static String EMAIL_HOST;
	
	@VBConfigInfo(key="EMAIL.PORT", description="Mail Server Port", defaultValue="465", mandatory=true)
	public static Integer EMAIL_PORT;
	
	@VBConfigInfo(key="EMAIL.USER", description="Mail Server Username", defaultValue="xxxx@gmail.com", mandatory=true)
	public static String EMAIL_USER;
	
	@VBConfigInfo(key="EMAIL.PASSWORD", description="Mail Server Password", defaultValue="xxxx", mandatory=true)
	public static String EMAIL_PASSWORD;
	
	@VBConfigInfo(key="EMAIL.FROM", description="Mail From", defaultValue="xxxx@gmail.com", mandatory=true)
	public static String EMAIL_FROM;
	
	@VBConfigInfo(key="EMAIL.FROM.ALIAS", description="Mail From Alias ", defaultValue="VocBench Admin", mandatory=true)
	public static String EMAIL_FROM_ALIAS;
	
	@VBConfigInfo(key="EMAIL.ADMIN", description="Mail Administrator Email(s) (For multiple emails use semicolon ';' as a separator)", defaultValue="xxxx@gmail.com", mandatory=true)
	public static ArrayList<String> EMAIL_ADMIN;
	
	@VBConfigInfo(key="MODEL.CLASS", description="Model Manager classname: don't modify this unless you really know what you are doing :-)", defaultValue="org.fao.aoscs.model.semanticturkey.STManager", mandatory=true)
	public static String MODELCLASS;
	
	@VBConfigInfo(key="PERMISSION.CHECK", description="Set true/false to enable/disable permission checks based on user groups (Default: true)", defaultValue="true", mandatory=true)
	public static Boolean PERMISSIONCHECK;
	
	@VBConfigInfo(key="PERMISSION.HIDE", description="Set true/false to show/hide button in case of permission allowed/restricted (Default: false)", defaultValue="false", mandatory=true)
	public static Boolean PERMISSIONHIDE;
	
	@VBConfigInfo(key="PERMISSION.DISABLE", description="Set true/false to enable/disable buttons in case of permission allowed/restricted (Default: true)", defaultValue="true", mandatory=true)
	public static Boolean PERMISSIONDISABLE;
	
	@VBConfigInfo(key="PERMISSION.LANGUAGECHECK", description="Set true/false to allow/restrict actions based on user permission langauge (Default: true)", defaultValue="true", mandatory=true)
	public static Boolean PERMISSIONLANGUAGECHECK;
	
	@VBConfigInfo(key="PAGE.SANDBOXLINK", description="Link to Sandbox version of VocBench", defaultValue="http://202.73.13.50:55481/vocbench/", mandatory=true)
	public static String SANDBOXLINK;
	
	@VBConfigInfo(key="PAGE.WEBSERVICESINFO", description="Link to web services page", defaultValue="http://aims.fao.org/tools/vocbench/access", mandatory=true)
	public static String WEBSERVICESINFO;
	
	@VBConfigInfo(key="PAGE.VOCBENCHINFO", description="Link to VocBench page", defaultValue="http://aims.fao.org/tools/vocbench-2", mandatory=true)
	public static String VOCBENCHINFO;
	
	@VBConfigInfo(key="PAGE.CONTACTUS", description="Link to contact page", defaultValue="http://aims.fao.org/contact", mandatory=true)
	public static String CONTACTUS;
	
	@VBConfigInfo(key="PAGE.COPYRIGHTLINK", description="Link for FAO copyright page", defaultValue="http://www.fao.org/corp/copyright/en/", mandatory=true)
	public static String COPYRIGHTLINK;
	
	@VBConfigInfo(key="PAGE.ARTGROUPLINK", description="Link for ART Research Group page", defaultValue="http://art.uniroma2.it/", mandatory=true)
	public static String ARTGROUPLINK;
	
	@VBConfigInfo(key="PAGE.FLINTEDITORLINK", description="Link for Flint SPARQL Editor", defaultValue="https://github.com/TSO-Openup/FlintSparqlEditor", mandatory=true)
	public static String FLINTEDITORLINK;
	
	@VBConfigInfo(key="VB.ONTOLOGY.VERSION", description="Specify VocBench version", defaultValue="2.0", mandatory=true)
	public static String VERSION;

	@VBConfigInfo(key="VB.MODE", description="Mode can be either DEV/PRO/SANDBOX (DEV: Development; PRO: Production; SANDBOX: Sandbox version)", defaultValue="DEV", mandatory=true)
	public static String MODE;
	
	@VBConfigInfo(key="SEARCH.TIMEOUT", description="Amount of time (in milliseconds) search waits for user to input another character before sending request", defaultValue="300", mandatory=false)
	public static Integer SEARCHTIMEOUT;
	
	@VBConfigInfo(key="SETTINGS.MANDATORY.DEFINITION.NAMESPACES", description="Any project having its namespace in this list will mandatorily ask for Source/Link values in its skos:definitions (For multiple namespaces use semicolon ';' as a separator)", defaultValue="http://aims.fao.org/aos/agrovoc/", mandatory=false)
	public static ArrayList<String> MANDATORY_DEFINITION_NAMESPACES;
	
	@VBConfigInfo(key="SETTINGS.CODETYPE", description="Set this value with custom datatype to set skos:notation property for every term instance created [e.g. For AGROVOC -> http://aims.fao.org/aos/agrovoc/AgrovocCode]", defaultValue="", mandatory=false)
	public static String CODETYPE;
	
	@VBConfigInfo(key="SETTINGS.CUSTOMDATATYPE", description="CUSTOM DATATYPE (For multiple datatype use semicolon ';' as a separator) [e.g. For AGROVOC -> http://aims.fao.org/aos/agrovoc/AgrovocCode]", defaultValue="", mandatory=false)
	public static ArrayList<String> CUSTOMDATATYPE;
	
	@VBConfigInfo(key="SETTINGS.CONCEPTNAVIGATIONHISTORY.MAXSIZE", description="No. of Concepts in navigation history list", defaultValue="25", mandatory=true)
	public static Integer CONCEPTNAVIGATIONHISTORYMAXSIZE;
	
	@VBConfigInfo(key="SETTINGS.EXCEPTION.DETAILS.SHOW", description="Set to true to show exception details in case of system malfunctions", defaultValue="true", mandatory=true)
	public static Boolean EXCEPTIONDETAILSSHOW;
	
	@VBConfigInfo(key="SETTINGS.ZIP.SIZE", description="Set this property to force zipped download to be separated into chuck of ZIP.SIZE bytes", defaultValue="5242880", mandatory=true)
	public static String ZIPSIZE;
	
	@VBConfigInfo(key="VISITOR.SHOWGUESTLOGIN", description="Set to true to enable guest login which doesn't require any user credentials (Default: false)", defaultValue="false", mandatory=true)
	public static Boolean SHOWGUESTLOGIN;
	
	@VBConfigInfo(key="VISITOR.ISVISITOR", description="Set to true to enable visitor only mode (Default: false)", defaultValue="false", mandatory=true)
	public static Boolean ISVISITOR;
	
	@VBConfigInfo(key="VISITOR.GUESTUSERNAME", description="Visitor's account username", defaultValue="Guest", mandatory=true)
	public static String GUESTUSERNAME;
	
	@VBConfigInfo(key="VISITOR.GUESTPASSWORD", description="Visitor's account password", defaultValue="1q2w3e4r", mandatory=true)
	public static String GUESTPASSWORD;
	
	@VBConfigInfo(key="VISITOR.GROUPID", description="Visitor's group id", defaultValue="12", mandatory=true)
	public static String VISITORGROUPID;
	
	@VBConfigInfo(key="VISITOR.GROUPNAME", description="Visitor's group name", defaultValue="Visitor", mandatory=true)
	public static String VISITORGROUPNAME;
	
	public static void loadConstants(HashMap<String, ConfigObject> cMap)
	{
		try
		{
			EMAIL_FROM 						= getStringValue(cMap.get("EMAIL.FROM"));
			
			MODELCLASS 						= getStringValue(cMap.get("MODEL.CLASS"));
			
			SANDBOXLINK						= getStringValue(cMap.get("PAGE.SANDBOXLINK"));
			WEBSERVICESINFO					= getStringValue(cMap.get("PAGE.WEBSERVICESINFO"));
			VOCBENCHINFO					= getStringValue(cMap.get("PAGE.VOCBENCHINFO"));
			CONTACTUS						= getStringValue(cMap.get("PAGE.CONTACTUS"));
			COPYRIGHTLINK					= getStringValue(cMap.get("PAGE.COPYRIGHTLINK"));
			ARTGROUPLINK					= getStringValue(cMap.get("PAGE.ARTGROUPLINK"));
			FLINTEDITORLINK					= getStringValue(cMap.get("PAGE.FLINTEDITORLINK"));
			
			PERMISSIONCHECK  				= getStringValue(cMap.get("PERMISSION.CHECK")).equalsIgnoreCase("true")? true : false;
			PERMISSIONHIDE  				= getStringValue(cMap.get("PERMISSION.HIDE")).equalsIgnoreCase("true")? true : false;
			PERMISSIONDISABLE  				= getStringValue(cMap.get("PERMISSION.DISABLE")).equalsIgnoreCase("true")? true : false;
			PERMISSIONLANGUAGECHECK 		= getStringValue(cMap.get("PERMISSION.LANGUAGECHECK")).equalsIgnoreCase("true")? true : false;
			
			SEARCHTIMEOUT					= Integer.parseInt(getStringValue(cMap.get("SEARCH.TIMEOUT")));
			
			EXCEPTIONDETAILSSHOW			= getStringValue(cMap.get("SETTINGS.EXCEPTION.DETAILS.SHOW")).equalsIgnoreCase("true")? true : false;
			ZIPSIZE							= getStringValue(cMap.get("SETTINGS.ZIP.SIZE"));
			CONCEPTNAVIGATIONHISTORYMAXSIZE	= Integer.parseInt(getStringValue(cMap.get("SETTINGS.CONCEPTNAVIGATIONHISTORY.MAXSIZE")));
			CODETYPE 						= getStringValue(cMap.get("SETTINGS.CODETYPE"));
			CUSTOMDATATYPE					= getListFromString(getStringValue(cMap.get("SETTINGS.CUSTOMDATATYPE")), cMap.get("SETTINGS.CUSTOMDATATYPE").getSeparator());
			MANDATORY_DEFINITION_NAMESPACES	= getListFromString(getStringValue(cMap.get("SETTINGS.MANDATORY.DEFINITION.NAMESPACES")), cMap.get("SETTINGS.MANDATORY.DEFINITION.NAMESPACES").getSeparator());
			
			
			VERSION							= getStringValue(cMap.get("VB.ONTOLOGY.VERSION"));
			MODE							= getStringValue(cMap.get("VB.MODE"));
	
			ISVISITOR						= getStringValue(cMap.get("VISITOR.ISVISITOR")).equalsIgnoreCase("true")? true : false;
			SHOWGUESTLOGIN  				= getStringValue(cMap.get("VISITOR.SHOWGUESTLOGIN")).equalsIgnoreCase("true")? true : false;
			GUESTUSERNAME					= getStringValue(cMap.get("VISITOR.GUESTUSERNAME"));
			GUESTPASSWORD					= getStringValue(cMap.get("VISITOR.GUESTPASSWORD"));
			VISITORGROUPID					= getStringValue(cMap.get("VISITOR.GROUPID"));
			VISITORGROUPNAME				= getStringValue(cMap.get("VISITOR.GROUPNAME"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static String getStringValue(ConfigObject configObject)
	{
		if(configObject!=null)
		{
			String str = (String) configObject.getValue();
			if(str!=null)
				return str;
		}
		return "";
	}
	
	private static ArrayList<String> getListFromString(String value, String separator)
	{
		ArrayList<String> list = new ArrayList<String>();
		if(value!=null)
		{
			for(String str : value.split(separator))
	        {
	        	if(str!=null && str.length()>0)
	        	{
	        		str = str.trim();
	        		list.add(str);
	        	}
	        }
		}
		return list;
	}
	
}
