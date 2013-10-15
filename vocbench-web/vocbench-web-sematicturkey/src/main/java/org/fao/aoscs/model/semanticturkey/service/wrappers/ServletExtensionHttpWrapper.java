package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.ResponseParser;
import it.uniroma2.art.semanticturkey.utilities.XMLHelp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServletExtensionHttpWrapper {

	protected static Log logger = LogFactory.getLog(ServletExtensionHttpWrapper.class);
	
	protected HttpClient httpclient;
	protected String id;
	
	protected String stServerScheme;
	protected String stServerIP;
	protected int stServerPort;
	protected String stServerPath;

	/**
	 * @param id
	 * @param httpclient
	 */
	public ServletExtensionHttpWrapper(String id, HttpClient httpclient, String stServerScheme, String stServerIP, int stServerPort, String stServerPath) {
		this.httpclient = httpclient;
		this.id = id;
		this.stServerScheme = stServerScheme;
		this.stServerIP = stServerIP;
		this.stServerPort = stServerPort;
		this.stServerPath = stServerPath;
	}
	
	/**
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	// HTTP SERVER METHODS

	/**
	 * @param query
	 * @return
	 * @throws URISyntaxException
	 */
	protected Response askServer(String query) throws URISyntaxException {
		//HttpGet http = prepareGet(query);
		HttpPost http = preparePost(query);
		try {
			HttpResponse response = httpclient.execute(http);
			return handleResponse(response); 
		} catch (ClientProtocolException e) {
			http.abort();
			e.printStackTrace();
		} catch (IOException e) {
			http.abort();
			e.printStackTrace();
		} catch (IllegalStateException e) {
			http.abort();
			e.printStackTrace();
		} catch (SAXException e) {
			http.abort();
			e.printStackTrace();
		}	
		return null;
	}

	/**
	 * @param service
	 * @param request
	 * @param parameters
	 * @return
	 */
	protected Response askServer(String service, String request, String... parameters) {
		StringBuffer query = new StringBuffer();
		query.append("service=" + service + "&request=" + request);
		for (int i = 0; i < parameters.length; i++)
			query.append("&" + parameters[i]);
		try {
			return askServer(query.toString());
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * @param query
	 * @return
	 * @throws URISyntaxException
	 */
	protected HttpGet prepareGet(String query) throws URISyntaxException {
		URI uri = URIUtils.createURI(stServerScheme, stServerIP, stServerPort, stServerPath,  query, null);
		logger.debug("\n"+uri);
		HttpGet httpget = new HttpGet(uri);
		return httpget;
	}
	
	/**
	 * @param query
	 * @return
	 * @throws URISyntaxException
	 */
	protected HttpPost preparePost(String query) throws URISyntaxException {
		URI uri = URIUtils.createURI(stServerScheme, stServerIP, stServerPort, stServerPath, query, null);
		logger.debug("\n"+uri);
		HttpPost httpPost = new HttpPost(uri);
		
		/*
	    try {
	    	ArrayList<NameValuePair> postParameters;
	    	postParameters = new ArrayList<NameValuePair>();
	    	StringTokenizer st = new StringTokenizer(query, "&");
	    	while(st.hasMoreElements())
	    	{
	    		String pairs = (String)st.nextElement();
	    		String[] param = pairs.split("=");
	    		if(param.length>1)
	    		{
	    			//System.out.println(param[0]+" = "+param[1]);
	    			postParameters.add(new BasicNameValuePair(param[0], param[1]));
	    		}
	    	}
	    	
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 */
	
		
		return httpPost;
	}
	
	/**
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SAXException 
	 */
	protected Response handleResponse(HttpResponse response) throws IllegalStateException, IOException, SAXException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedInputStream in = new BufferedInputStream(instream);
				Document doc = XMLHelp.inputStream2XML(in);
				return ResponseParser.getResponseFromXML(doc);
			}

		return null;
	}

	
	
	// END OF HTTP SERVER METHODS
	
}
