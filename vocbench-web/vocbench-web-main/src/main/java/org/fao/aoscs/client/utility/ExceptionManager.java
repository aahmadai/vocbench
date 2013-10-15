/**
 * 
 */
package org.fao.aoscs.client.utility;

import org.fao.aoscs.client.locale.LocaleConstants;
import org.fao.aoscs.client.module.constant.ConfigConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * @author rajbhandari
 *
 */
public class ExceptionManager {
	private static LocaleConstants constants = (LocaleConstants) GWT.create(LocaleConstants.class);
	
	public static void showException(Throwable caught, String message)
	{
		String alertMsg = "";
		if(!message.equals(""))
			message += "\n\n";
		
		alertMsg = message;
		if(ConfigConstants.EXCEPTIONDETAILSSHOW)
			alertMsg += constants.exceptionExceptionDetails()+":\n"+caught.getLocalizedMessage();
		
		if(!alertMsg.equals(""))
			Window.alert(alertMsg);
	}
}
