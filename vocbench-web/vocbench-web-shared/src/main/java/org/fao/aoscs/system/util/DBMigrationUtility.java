package org.fao.aoscs.system.util;

import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.fao.aoscs.domain.DBMigrationObject;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

public class DBMigrationUtility {
	
    Flyway flyway;
    
	public DBMigrationUtility(String dbURL, String dbUsername, String dbPassword) throws Exception
	{
		try
        {
			// Create the Flyway instance
	        flyway = new Flyway();
	        
	        // Point it to the database
	        flyway.setDataSource(dbURL, dbUsername, dbPassword);
	        flyway.setEncoding("UTF-8");
        }
        catch(Exception e)
        {
        	throw new Exception(e.getMessage());
        }
    
	}
	
	public ArrayList<DBMigrationObject> runDBMigrate(String initVersion) throws Exception
	{
        try
        {
			// Start the migration
	        flyway.setInitOnMigrate(true);
	        if(initVersion!=null && initVersion.length()>0)
	        	flyway.setInitVersion(initVersion);
	        flyway.repair();
	        flyway.migrate();
        }
        catch(Exception e)
        {
        	throw new Exception(e.getMessage());
        }
        return getDBMigrationList();
	}
	
	public ArrayList<DBMigrationObject> getDBMigrationList()
	{
		ArrayList<DBMigrationObject> list = new ArrayList<DBMigrationObject>();
		for(MigrationInfo mInfo: flyway.info().all())
        {
			DBMigrationObject dbmObj = new DBMigrationObject();
			dbmObj.setChecksum(mInfo.getChecksum());
			dbmObj.setDescription(mInfo.getDescription());
			dbmObj.setExecutionTime(mInfo.getExecutionTime());
			dbmObj.setInstalledOn(mInfo.getInstalledOn());
			dbmObj.setScript(mInfo.getScript());
			dbmObj.setState(mInfo.getState().name());
			dbmObj.setType(mInfo.getType().name());
			dbmObj.setVersion(mInfo.getVersion().getVersion());
			list.add(dbmObj);
			//printMigrationInfo(mInfo);
        }
		return list;
	}
	
	public void printMigrationInfo(MigrationInfo mInfo)
	{
		System.out.println("\nChecksum: "+mInfo.getChecksum());
		System.out.println("Desc: "+mInfo.getDescription());
		System.out.println("ExecutionTime: "+mInfo.getExecutionTime());
		System.out.println("Installed on: "+mInfo.getInstalledOn());
		System.out.println("Script: "+mInfo.getScript());
		System.out.println("State: "+mInfo.getState());
		System.out.println("Type: "+mInfo.getType());
    	System.out.println("Version: "+mInfo.getVersion());
	}
	
	public static void main(String args[])
	{
		// Get database properties from config file
		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration("Config.properties");
			
			// Point it to the database
	        DBMigrationUtility dbmUtility = new DBMigrationUtility(config.getString("CFG.DB.CONNECTIONURL"), config.getString("CFG.DB.USERNAME"), config.getString("CFG.DB.PASSWORD"));
	        
	        dbmUtility.runDBMigrate(null);
	        
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
