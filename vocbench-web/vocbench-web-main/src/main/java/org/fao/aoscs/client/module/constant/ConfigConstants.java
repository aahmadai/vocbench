package org.fao.aoscs.client.module.constant;

import java.util.ArrayList;
import java.util.HashMap;

import org.fao.aoscs.domain.ConfigObject;

public class ConfigConstants {
	
	// Global Constants
	public static String DEV = "DEV";
	public static String PRO = "PRO";
	public static String SANDBOX = "SANDBOX";

	public static boolean ISCONFIGSET;
	
	public static boolean ISVISITOR;
	public static boolean SHOWGUESTLOGIN;

	public static boolean PERMISSIONCHECK;
	public static boolean PERMISSIONHIDE;
	public static boolean PERMISSIONDISABLE;
	public static boolean PERMISSIONLANGUAGECHECK;

	public static String GUESTUSERNAME;
	public static String GUESTPASSWORD;
	public static String VISITORGROUPID;
	public static String VISITORGROUPNAME;
	
	public static String VERSION;
	public static String DISPLAYVERSION;
	public static String MODE;
	public static String SANDBOXLINK;
	
	public static String WEBSERVICESINFO;
	public static String VOCBENCHINFO;
	public static String CONTACTUS;
	public static String COPYRIGHTLINK;

	// Exception Details
	public static boolean EXCEPTIONDETAILSSHOW;
	
	public static int SEARCHTIMEOUT;
	public static boolean ISINDEXING;

	public static int CONCEPTNAVIGATIONHISTORYMAXSIZE;

	// Derived constants
	public static String VERSIONTEXT;

	//AGROVOC NAMESPACE
	public static String AGROVOCNAMESPACE;
	
	//Service constants
	public static String MODELCLASS;
	
	//Mail Constants
	public static String EMAIL_FROM;
	
	//NAMESPACES WITH MANDATORY DEFINITION SOURCE/LINK
	public static ArrayList<String> MANDATORY_DEFINITION_NAMESPACES = new ArrayList<String>();
	
	//CODETYPE TYPE
	public static String CODETYPE;
	
	//CUSTOM DATATYPE
	public static ArrayList<String> CUSTOMDATATYPE = new ArrayList<String>();
	
	
	public static void loadConstants(HashMap<String, ConfigObject> cMap)
	{
		ISCONFIGSET						= cMap.get("ISCONFIGSET").getValue().equalsIgnoreCase("true")? true : false;
		
		ISVISITOR						= cMap.get("ISVISITOR").getValue().equalsIgnoreCase("true")? true : false;
		SHOWGUESTLOGIN  				= cMap.get("SHOWGUESTLOGIN").getValue().equalsIgnoreCase("true")? true : false;

		PERMISSIONCHECK  				= cMap.get("PERMISSIONCHECK").getValue().equalsIgnoreCase("true")? true : false;
		PERMISSIONHIDE  				= cMap.get("PERMISSIONHIDE").getValue().equalsIgnoreCase("true")? true : false;
		PERMISSIONDISABLE  				= cMap.get("PERMISSIONDISABLE").getValue().equalsIgnoreCase("true")? true : false;
		PERMISSIONLANGUAGECHECK 		= cMap.get("PERMISSIONLANGUAGECHECK").getValue().equalsIgnoreCase("true")? true : false;

		GUESTUSERNAME					= (String)cMap.get("GUESTUSERNAME").getValue();
		GUESTPASSWORD					= (String)cMap.get("GUESTPASSWORD").getValue();
		VISITORGROUPID					= (String)cMap.get("VISITORGROUPID").getValue();
		VISITORGROUPNAME				= (String)cMap.get("VISITORGROUPNAME").getValue();
		
		VERSION							= (String)cMap.get("VERSION").getValue();
		DISPLAYVERSION					= (String)cMap.get("DISPLAYVERSION").getValue();
		MODE							= (String)cMap.get("MODE").getValue();
		SANDBOXLINK						= (String)cMap.get("SANDBOXLINK").getValue();

		WEBSERVICESINFO					= (String)cMap.get("WEBSERVICESINFO").getValue();
		VOCBENCHINFO					= (String)cMap.get("VOCBENCHINFO").getValue();
		CONTACTUS						= (String)cMap.get("CONTACTUS").getValue();
		COPYRIGHTLINK					= (String)cMap.get("COPYRIGHTLINK").getValue();
		
		EXCEPTIONDETAILSSHOW			= cMap.get("EXCEPTION.DETAILS.SHOW").getValue().equalsIgnoreCase("true")? true : false;

		SEARCHTIMEOUT					= Integer.parseInt(cMap.get("SEARCHTIMEOUT").getValue());
		ISINDEXING						= Boolean.parseBoolean((String)cMap.get("ISINDEXING").getValue());

		CONCEPTNAVIGATIONHISTORYMAXSIZE	= Integer.parseInt(cMap.get("CONCEPTNAVIGATIONHISTORYMAXSIZE").getValue());
		
		AGROVOCNAMESPACE 				= cMap.get("AGROVOCNAMESPACE").getValue();

		MODELCLASS 						= (String)cMap.get("MODELCLASS").getValue();

		VERSIONTEXT 					= DISPLAYVERSION + " " + ((MODE.equals(DEV))? "(DEVELOPMENT)" : ((MODE.equals(SANDBOX))? "(SANDBOX)" : ""));

		EMAIL_FROM 						= (String)cMap.get("MAIL.FROM").getValue();
		
		MANDATORY_DEFINITION_NAMESPACES	= getList(cMap.get("MANDATORY.DEFINITION.NAMESPACES").getValue(), ";");

		CODETYPE 						= (String)cMap.get("CODETYPE").getValue();
		
		CUSTOMDATATYPE					= getList(cMap.get("CUSTOMDATATYPE").getValue(), ";");
		

	}
	
	private static ArrayList<String> getList(String value, String separator)
	{
		ArrayList<String> list = new ArrayList<String>();
		for(String str : value.split(separator))
        {
        	if(str!=null && str.length()>0)
        	{
        		str = str.trim();
        		list.add(str);
        	}
        }
		return list;
	}
}
