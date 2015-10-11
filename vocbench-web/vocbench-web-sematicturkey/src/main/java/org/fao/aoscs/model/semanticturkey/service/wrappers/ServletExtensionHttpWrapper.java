package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.ResponseParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.fao.aoscs.model.semanticturkey.util.HttpClientFactory;
import org.fao.aoscs.model.semanticturkey.util.STInfo;
import org.fao.aoscs.model.semanticturkey.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServletExtensionHttpWrapper {

	protected static Log logger = LogFactory.getLog(ServletExtensionHttpWrapper.class);
	
	protected String id;
	protected String stURL;

	/**
	 * @param id
	 * @param stURL
	 */
	public ServletExtensionHttpWrapper(String id, String stURL) {
		this.id = id;
		this.stURL = stURL;
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
	protected Response askServer(List<NameValuePair> parameterLists) {
		//HttpGet http = prepareGet(parameterLists);
		HttpPost http = preparePost(parameterLists);
		try {
			HttpResponse response = HttpClientFactory.getHttpClient().execute(http);
			return handleResponse(response, http.getURI().toString()); 
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
		} catch (ParserConfigurationException e) {
			http.abort();
			e.printStackTrace();
		}
		finally
		{
			http.releaseConnection();
		}
		return null;
	}
	
	/**
	 * @param query
	 * @return
	 * @throws URISyntaxException
	 */
	protected Response askNewServer(String service, String request, List<NameValuePair> parameterLists) {
		//HttpGet http = prepareGet(parameterLists);
		//HttpPost http = preparePost(parameterLists);
		HttpGet http = prepareNewGet(service, request, parameterLists);
		try {
			HttpResponse response = HttpClientFactory.getHttpClient().execute(http);
			return handleResponse(response, http.getURI().toString()); 
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
		} catch (ParserConfigurationException e) {
			http.abort();
			e.printStackTrace();
		}
		finally
		{
			http.releaseConnection();
		}
		return null;
	}
	
	/**
	 * @param service
	 * @param request
	 * @param parameterLists
	 * @return
	 */
	protected String askHttpServer(String service, String request, List<NameValuePair> parameterLists) {
		//HttpGet http = prepareGet(parameterLists);
		//HttpPost http = preparePost(parameterLists);
		HttpGet http = prepareNewGet(service, request, parameterLists);
		try {
			HttpResponse response = HttpClientFactory.getHttpClient().execute(http);
			return handleHttpResponse(response, http.getURI().toString());  
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
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		finally
		{
			http.releaseConnection();
		}
		return null;
	}

	/**
	 * @param parameterLists
	 * @return
	 * @throws URISyntaxException
	 */
	protected URI getURI(List<NameValuePair> parameterLists)
	{
		URI uri;
		try {
			URI stURI = new URI(stURL);
			uri = new URIBuilder()
			.setScheme(stURI.getScheme())
			.setHost(stURI.getHost())
			.setPort(stURI.getPort())
			.setPath("/semanticturkey/resources/stserver/STServer")
			.setParameters(parameterLists)
			.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return uri;
	}
	
	/**
	 * @param parameterLists
	 * @return
	 * @throws URISyntaxException
	 */
	protected URI getNewURI(String service, String request, List<NameValuePair> parameterLists)
	{
		URI uri;
		try {
			URI stURI = new URI(stURL);
			uri = new URIBuilder()
			.setScheme(stURI.getScheme())
			.setHost(stURI.getHost())
			.setPort(stURI.getPort())
			.setPath("/"+STInfo.getSTName()+"/"+STInfo.getGroupId()+"/"+STInfo.getArtifactId()+"/"+service+"/"+request)
			.setParameters(parameterLists)
			.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return uri;
	}
	

	/**
	 * @param query
	 * @return
	 * @throws URISyntaxException
	 */
	protected HttpGet prepareGet(List<NameValuePair> parameterLists) {
		HttpGet httpget = new HttpGet(getURI(parameterLists));
		return httpget;
	}
	
	/**
	 * @param query
	 * @return
	 * @throws URISyntaxException
	 */
	protected HttpPost preparePost(List<NameValuePair> parameterLists) {
		HttpPost httpPost = new HttpPost(getURI(parameterLists));
		return httpPost;
	}
	
	/**
	 * @param query
	 * @return
	 * @throws URISyntaxException
	 */
	protected HttpGet prepareNewGet(String service, String request, List<NameValuePair> parameterLists) {
		HttpGet httpGet = new HttpGet(getNewURI(service, request, parameterLists));
		return httpGet;
	}
	
	/**
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	protected Response handleResponse(HttpResponse response, String query) throws IllegalStateException, IOException, SAXException, ParserConfigurationException {
		//System.out.println("\n"+query);
		logger.debug("\n"+query);
		InputStream in = null;
		try
		{
			in = getInputStream(response);
			if(in!=null)
			{
				Document doc = XMLUtil.inputStream2XML(in);
				return ResponseParser.getResponseFromXML(doc);
			}
			else return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			 IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * @param response
	 * @param query
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	protected String handleHttpResponse(HttpResponse response, String query) throws IllegalStateException, IOException, SAXException, ParserConfigurationException {
		//System.out.println("\n"+query);
		logger.debug("\n"+query);
		InputStream in = null;
		try
		{
			in = getInputStream(response);
			if(in!=null)
			{
				return IOUtils.toString(in, "UTF-8"); 
			}
			else return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			 IOUtils.closeQuietly(in);
		}
	}
	
	protected InputStream getInputStream(HttpResponse httpResponse) {
        try {
        	InputStream is = null;
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                is = httpResponse.getEntity().getContent();
            } else {
                 EntityUtils.consume(httpResponse.getEntity());
            }
            return is;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	// END OF HTTP SERVER METHODS
	
}
