package org.fao.aoscs.server.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtility {
	
	/**Get current date based on ROME time*/
	public static String getDate(){
		//set locale and timezone
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Locale.setDefault(new Locale("en", "US"));  
		// set format
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat(DATE_FORMAT);
		//sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));     
		
		return sdf.format(cal.getTime());
	}
	
	/**Get current date & time based on ROME time*/
	public static String getDateTime(){
		//set locale and timezone
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Locale.setDefault(new Locale("en", "US"));  
		// set format
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat(DATE_FORMAT);
		//sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		return sdf.format(cal.getTime());
	}
	
	/**Get current date based on ROME time*/
	public static java.util.Date getROMEDate(){
		//set locale and timezone
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Locale.setDefault(new Locale("en", "US"));  
		//TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
		return cal.getTime();
		
	}
	
	public static Date getDate(String date) 
	{
		if(!date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return getDate(date, "EEE MMM d  HH:mm:ss z yyyy") ;
			}
		}
		else
			return null;
	}
	
	public static Date getDate(String date, String format) 
	{
		if(!date.equals("null"))
		{
			SimpleDateFormat df = new SimpleDateFormat(format);
			
			if(date.length()<11) date += " 00:00:00";
			try
			{
				return df.parse(date);
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else
			return null;
	}
}
