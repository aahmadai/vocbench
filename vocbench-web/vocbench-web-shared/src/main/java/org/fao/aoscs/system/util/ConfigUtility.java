package org.fao.aoscs.system.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.apache.commons.io.FileUtils;
import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.fao.aoscs.domain.ConfigObject;
import org.fao.aoscs.domain.VBConfigInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;

public class ConfigUtility {
	/**
	 * @return
	 */
	public static LinkedHashMap<String, ConfigObject> loadConfigConstants()
	{
		LinkedHashMap<String, ConfigObject> mcMap = new LinkedHashMap<String, ConfigObject>();
		try {
			loadConfigConstantsFromPropertyFile(getConfigPropertiesConfiguration());
			mcMap = getConfigObjects();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return mcMap;
	}
	
	private static String getPrefix(String prefix)
	{
		if(prefix.length()>0)
		{
			int index = prefix.indexOf(".");
			if(index>=0)
			{
				return prefix.substring(0, index);
			}
		}
		return prefix;
	}
	
	/**
	 * @param configObjectMap
	 */
	public static void updateConfigConstants(Map<String, ConfigObject> configObjectMap)
	{
		try {
			ArrayList<String> list = new ArrayList<String>(configObjectMap.keySet());
			Collections.sort(list);
			
			LinkedHashMap<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();
			for(String key : list)
			{
				String prefix = getPrefix(key);
				ArrayList<String> namelist = map.get(prefix);
				if(namelist==null)
					namelist = new ArrayList<String>();
				namelist.add(key);
				map.put(prefix, namelist);
			}
			
			PropertiesConfiguration config = getConfigPropertiesConfiguration();
			config.clear();
			PropertiesConfigurationLayout layout = config.getLayout();
			layout.setHeaderComment("CONFIGURATION FILE");
			for(String prefix : map.keySet())
			{
				boolean isFirstKey = true;
				for(String key : map.get(prefix))
				{
					ConfigObject configObj = configObjectMap.get(key);
					config.setProperty(configObj.getKey(), configObj.getValue());
					layout.setComment(configObj.getKey(), configObj.getDescription());
					if(isFirstKey) 
					{
						layout.setBlancLinesBefore(configObj.getKey(), 1);
						layout.setComment(configObj.getKey(), "--- "+prefix+" PROPERTIES ---"+"\n"+configObj.getDescription());
						isFirstKey = false;
					}
					else
						layout.setComment(configObj.getKey(), configObj.getDescription());
				}
			}
			config.save();
			HibernateUtilities.reloadSessionFactory();
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws ConfigurationException
	 */
	public static LinkedHashMap<String, ConfigObject> getConfigConstants(String filename) throws ConfigurationException
	{
		LinkedHashMap<String, ConfigObject> mcMap = new LinkedHashMap<String, ConfigObject>();
		try {
			
			PropertiesConfiguration rb = new PropertiesConfiguration(filename);
			for (Field field : ConfigConstants.class.getFields()) 
			{
			//Iterator<?> en1 = rb.getKeys();
			//while (en1.hasNext()) {
				//String key = (String) en1.next();
				//Field field = ConfigConstants.class.getField(key);
				VBConfigInfo vbConfigInfo = field.getAnnotation(VBConfigInfo.class);
				String key = vbConfigInfo.key();
				String value = rb.getString(key);
				value = (value==null)?"":value;

				ConfigObject configObj = new ConfigObject();
				configObj.setKey(key);
				configObj.setValue(value);
				configObj.setSeparator(vbConfigInfo.separator());
				if(vbConfigInfo!=null)
				{
					configObj.setMandatory(vbConfigInfo.mandatory());
					if(vbConfigInfo.defaultValue()!=null)
						configObj.setDefaultValue(vbConfigInfo.defaultValue());
					if(vbConfigInfo.description()!=null)
						configObj.setDescription(vbConfigInfo.description());
				}
				
				mcMap.put(key, configObj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mcMap;
	}
	
	/**
	 * @return
	 * @throws ConfigurationException
	 */
	private static PropertiesConfiguration getConfigPropertiesConfiguration()
	{
		PropertiesConfiguration pc;
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			URL url = classloader.getResource("./Config.properties");
			File f;
			if(url==null)
			{
				
				f = new File(classloader.getResource(".").getPath(), "Config.properties");
				if(!f.exists())
				{
					try {
						FileUtils.write(f, "");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				pc = new PropertiesConfiguration(f);
			}
			else
			{
				pc = new PropertiesConfiguration(url);
			}
			//System.out.println("path: "+pc.getPath());
			return pc;
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return new PropertiesConfiguration();
	}
	
	private static void loadConfigConstantsFromPropertyFile(PropertiesConfiguration config) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ConfigurationException
	{
		for (Field field : ConfigConstants.class.getFields()) 
		{
			field.setAccessible(true);
			VBConfigInfo vbConfigInfo = field.getAnnotation(VBConfigInfo.class);
			String value = config.getString(vbConfigInfo.key());
			//System.out.println(vbConfigInfo.key()+" ==== "+value);
			if(value!=null && !value.equals(""))
			{
				String propertyType = field.getType().getName();
				//System.out.println("propertyType: "+propertyType);
				if(propertyType.equalsIgnoreCase("java.lang.Integer"))
				{
					field.set(null, Integer.parseInt(value));
				}
				else if(propertyType.equalsIgnoreCase("java.lang.Boolean"))
				{
					field.set(null, Boolean.parseBoolean(value));
				}
				else if(propertyType.equalsIgnoreCase("java.util.ArrayList"))
				{
					field.set(null, getList(value, vbConfigInfo.separator()));
				}
				else
				{
					field.set(null, value);
				}
			}
			//System.out.println(vbConfigInfo.key()+" ======= "+field.get(null));
		}
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
	
	/**
	 * @param pc
	 * @return
	 */
	private static LinkedHashMap<String, ConfigObject> getConfigObjects()
	{
		LinkedHashMap<String, ConfigObject> mcMap = new LinkedHashMap<String, ConfigObject>();
		try {
			
			Field[] fields = ConfigConstants.class.getFields();
			for(Field field: fields)
			{
				VBConfigInfo vbConfigInfo = field.getAnnotation(VBConfigInfo.class);
				
				ConfigObject configObj = new ConfigObject();
				configObj.setKey(vbConfigInfo.key());
				configObj.setSeparator(vbConfigInfo.separator());
				
				Object obj = field.get(null);
				if(obj!=null)
				{
					if(obj instanceof ArrayList<?>)
						configObj.setValue(getStringFromList((ArrayList<?>)obj, vbConfigInfo.separator()));
					else
						configObj.setValue(""+obj);
				}
				else
					configObj.setValue(null);
					
				
				if(vbConfigInfo!=null)
				{
					configObj.setMandatory(vbConfigInfo.mandatory());
					if(vbConfigInfo.defaultValue()!=null)
						configObj.setDefaultValue(vbConfigInfo.defaultValue());
					if(vbConfigInfo.description()!=null)
						configObj.setDescription(vbConfigInfo.description());
				}
				
				mcMap.put(vbConfigInfo.key(), configObj);
				//System.out.println("Key = "+configObj.getKey()+" Value = "+configObj.getValue());
				//if((value==null || value.equals("")) && vbConfigInfo.mandatory()) 
				/*{
					System.out.println("");
					System.out.println("Key = "+configObj.getKey());
					System.out.println("Value = "+configObj.getValue());
					System.out.println("Default Value = "+configObj.getDefaultValue());
					System.out.println("Description = "+configObj.getDescription());
					System.out.println("Separator = "+configObj.getSeparator());
					System.out.println("Mandatory = "+configObj.isMandatory());
				}*/
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mcMap;
	}
	
	private static String getStringFromList(ArrayList<?> list, String separator)
	{
		String value = "";
		for(Object obj : list)
        {
        	String str = (String) obj;
			if(!value.equals(""))
        	{
        		value += separator;
        	}
        	value += str.trim();
        }
		return value;
	}
	

}
