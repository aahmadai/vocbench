/**
 * 
 */
package org.fao.aoscs.client.utility;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.Cookies;

/**
 * @author rajbhandari
 *
 */
public class CookieManager {
	
	/**
	 * The timeout before a cookie expires, in milliseconds. Current one week.
	 */
	public static final int COOKIE_TIMEOUT = 1000 * 60 * 60 * 24 * 7;
	public static final String COOKIE_SEPARATOR = "~";
	
	
	public static String getCookie(String cookieName)
	{
		String cookie = Cookies.getCookie(cookieName);
		if(cookie==null || cookie.equalsIgnoreCase("null"))
		{
			cookie = "";
		}
		return cookie;
	}
	public static ArrayList<String> getCookieList(String cookieName)
	{
		ArrayList<String> list = new ArrayList<String>();
		String cookie = getCookie(cookieName);
		if(cookie!=null && !cookie.equals("") && !cookie.equalsIgnoreCase("null"))
		{
			String[] logincookielist =  cookie.split(COOKIE_SEPARATOR);
			for (int i=0;i<logincookielist.length;i++) 
			{
				list.add(logincookielist[i]);
			}
		}
		return list;
	}
	
	public static boolean contains(String cookieName, String value)
	{
			ArrayList<String> list = getCookieList(cookieName);
			if(list.contains(value))
				return true;
			else 
				return false;
	}
	
	public static void addToCookieList(String cookieName, String value)
	{
		if(value!= null && !value.equals("") && !value.equalsIgnoreCase("null"))
		{
			String cookie = getCookie(cookieName);
			if(cookie!=null && cookie.equals("") && !cookie.equalsIgnoreCase("null"))
			{
				
			}
			ArrayList<String> list = getCookieList(cookieName);
			if(!list.contains(value))
			{
				//Window.alert("add("+cookieName+"): "+cookie+COOKIE_SEPARATOR+value);
				Cookies.setCookie(cookieName, cookie+COOKIE_SEPARATOR+value, new Date((new Date()).getTime() + COOKIE_TIMEOUT));
			}
		}
			
	}
	
	public static void removeFromCookieList(String cookieName, String value)
	{
		if(value!= null && !value.equals("") && !value.equalsIgnoreCase("null"))
		{
			ArrayList<String> list = getCookieList(cookieName);
		
			if(list.contains(value))
			{
				list.remove(value);
			}
			String cookie = "";
			for (int i=0;i<list.size();i++) 
			{
				if(i==0)
					cookie = list.get(i);
				else
					cookie += COOKIE_SEPARATOR+list.get(i);
			}
			//Window.alert("remove("+cookieName+"): "+cookie+COOKIE_SEPARATOR+value);
			Cookies.setCookie(cookieName, cookie, new Date((new Date()).getTime() + COOKIE_TIMEOUT));
			
			
		}
			
	}
}
