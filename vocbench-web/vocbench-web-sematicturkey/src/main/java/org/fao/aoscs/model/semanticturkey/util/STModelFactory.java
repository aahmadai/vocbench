package org.fao.aoscs.model.semanticturkey.util;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.fao.aoscs.domain.OntologyInfo;

/**
 * @author rajbhandari
 *
 */
public class STModelFactory {

	protected static Log logger = LogFactory.getLog(STModelFactory.class);
	static HashMap<String, STModel> stModelsMap;
	static {
		stModelsMap = new HashMap<String, STModel>();
	}
	
	
	public static synchronized STModel getSTModel(OntologyInfo ontoInfo) {
		STModel stModel = null;
		if(isSTServerStarted(ontoInfo))
		{		
			stModel = stModelsMap.get(ontoInfo.getModelID()+"_"+ontoInfo.getDbDriver());
			if (stModel==null) {
				stModel = createSTModel(ontoInfo);	
				stModelsMap.put(ontoInfo.getModelID()+"_"+ontoInfo.getDbDriver(), stModel);
			}
		}
		else
			throw new RuntimeException("Connection to server failed");
		return stModel;
	}
	
	/**
	 * @return
	 */
	public static synchronized STModel createSTModel(OntologyInfo ontoInfo) {
		STModel stModel = new STModel(ontoInfo.getDbDriver());
		return stModel;
	}
	
	/*public Boolean isSTServerStarted(OntologyInfo ontoInfo)  {
		String URLName = ontoInfo.getDbDriver();
		try
		{
	      HttpURLConnection.setFollowRedirects(false);
	      HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      con.setConnectTimeout(15000); //set timeout to 15 seconds
	      con.setReadTimeout(15000);
	      //logger.debug("Connection response code: "+con.getResponseCode()+"  :: "+ HttpURLConnection.HTTP_OK);
	      System.out.println("Connection URL: "+URLName);
	      System.out.println("Connection response code: "+con.getResponseCode()+"  :: "+ HttpURLConnection.HTTP_OK);
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	    	//logger.debug("Connection error:"+e.getLocalizedMessage());
	    	e.printStackTrace();
	       return false;
	    }
	}*/
	
	public static Boolean isSTServerStarted(OntologyInfo ontoInfo)  {
		String URLName = ontoInfo.getDbDriver();
		HttpClient httpclient;
		try
		{
			HttpGet httpRequest = new HttpGet(getSTURL(URLName));
			httpclient = HttpClients.createDefault(); 
			HttpResponse response = httpclient.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
	        logger.debug("Connection response code: "+statusCode+"  :: "+ HttpURLConnection.HTTP_OK+" : "+URLName);
			return (statusCode == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			logger.debug("Connection error:"+e.getLocalizedMessage());
			return false;
		}
	}
	
	public static URI getSTURL(String stURL)
	{
		URI uri;
		try {
			URI stURI = new URI(stURL);
			uri = new URIBuilder()
			.setScheme(stURI.getScheme())
			.setHost(stURI.getHost())
			.setPort(stURI.getPort())
			.setPath("/semanticturkey/resources/stserver/STServer")
			.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return uri;
	}
	
	
	
}
