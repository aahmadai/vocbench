package org.fao.aoscs.hibernate;

import org.fao.aoscs.client.module.constant.ConfigConstants;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

public class HibernateUtilities 
{
	
	//----
	// Constants
	//----
	/**
	 * The stateful configuration file
	 */
	private static final String CONFIGURATION_FILE = "hibernate.cfg.xml";
	public static SessionFactory sessionFactory;
	public static Configuration hbConfig;
	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	
    static {
        createSessionFactory();
    }
    
	public static void createSessionFactory()
	{
		try {
			
			hbConfig = new Configuration();
			if(ConfigConstants.DB_CONNECTIONURL!=null)
				hbConfig.setProperty("hibernate.connection.url", ConfigConstants.DB_CONNECTIONURL);
			if(ConfigConstants.DB_USERNAME!=null)
				hbConfig.setProperty("hibernate.connection.username", ConfigConstants.DB_USERNAME);
			if(ConfigConstants.DB_PASSWORD!=null)
				hbConfig.setProperty("hibernate.connection.password", ConfigConstants.DB_PASSWORD);
	    	hbConfig.configure(CONFIGURATION_FILE);
	    	
			// Create the SessionFactory from hibernate.cfg.xml				
        	sessionFactory = hbConfig.buildSessionFactory();
		} 
		catch (Throwable ex) 
		{
			// Make sure you log the exception, as it might be swallowed
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void reloadSessionFactory()
	{
		closeSessionFactory();
		createSessionFactory();
	}
	
    public static SessionFactory getSessionFactory()
	{
		if (sessionFactory == null)
		{
			createSessionFactory();
		}
		return sessionFactory;
	}
    
    public static void closeSessionFactory()
	{
    	if (sessionFactory != null)
		{
    		if(!sessionFactory.isClosed())
    			sessionFactory.close();
			sessionFactory = null;
			closeSession();
		}
	}


    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = getSessionFactory().openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        session.set(null);
        if (s != null)
            s.close();
    }

}
