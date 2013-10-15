package org.fao.aoscs.server.utility;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailUtil {
	
	protected static Log logger = LogFactory.getLog(MailUtil.class);

	public static void sendMail(String to, String subject, String body)
	{
		sendMessage(to, null, null , subject, body);
	}
	public static void sendMail(String to, String cc, String subject, String body)
	{
		sendMessage(to, cc, null , subject, body);
	}
	public static void sendMail(String to, String cc, String bcc, String subject, String body)
	{
		sendMessage(to, cc, bcc, subject, body);
	}
	
	private static void sendMessage(String to, String cc, String bcc, String subject, String body)
	{
		try
		{
			PropertiesConfiguration config = new PropertiesConfiguration("Config.properties");

			final String host = config.getString("CFG.MAIL.HOST");
			final int port = config.getInt("CFG.MAIL.PORT");
			final String user = config.getString("CFG.MAIL.USER");
			final String password = config.getString("CFG.MAIL.PASSWORD");
			String from = config.getString("CFG.MAIL.FROM");
			String from_alias = config.getString("CFG.MAIL.FROM_ALIAS");
			String toAdmin = to;
			if(to.equals("ADMIN"))
			{
				toAdmin = StringUtils.join(config.getStringArray("CFG.MAIL.ADMIN"), ",");
			}
			
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", host);
			props.put("mail.smtps.auth", "true");
			props.put("mail.smtps.quitwait", "false");
			
			Session mailConnection = Session.getDefaultInstance(props);
			mailConnection.setDebug(false);
			final Transport transport = mailConnection.getTransport();
			
			final Message message = new MimeMessage(mailConnection);
			message.setContent(body, "text/plain;charset=UTF8");
			message.setFrom(new InternetAddress(from, from_alias));
			message.setReplyTo(InternetAddress.parse(from, false));
			if(toAdmin!=null)
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toAdmin, false));
			if(cc!=null)
				message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc, false));
			if(bcc!=null)
				message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(bcc, false));
			message.setSubject(subject);
			
			Runnable r = new Runnable() {
	            public void run() {
	            	try 
	            	{
	            	  	transport.connect(host, port, user, password);     
		      			transport.sendMessage(message,message.getAllRecipients());
		      			transport.close();
	            	}
	            	catch (Exception e) 
	            	{
	            		logger.error("Mail send failed: "+e.getMessage());
	            	}
	            } 
	          };
	          Thread t = new Thread(r);
	          t.start();
		}
		catch(Exception e)
		{
			logger.error("Mail send failed: "+e.getMessage());
		}
	}
}


