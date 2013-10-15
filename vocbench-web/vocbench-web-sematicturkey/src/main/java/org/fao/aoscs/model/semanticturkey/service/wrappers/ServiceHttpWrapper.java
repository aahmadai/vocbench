package org.fao.aoscs.model.semanticturkey.service.wrappers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import it.uniroma2.art.semanticturkey.servlet.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.fao.aoscs.model.semanticturkey.util.ParameterPair;

public class ServiceHttpWrapper extends ServletExtensionHttpWrapper implements ServiceWrapper {

	protected static Log logger = LogFactory.getLog(ServiceHttpWrapper.class);

	/**
	 * @param id
	 * @param httpclient
	 */
	public ServiceHttpWrapper(String id, HttpClient httpclient, String stServerScheme, String stServerIP, int stServerPort, String stServerPath) {
		super(id, httpclient, stServerScheme, stServerIP, stServerPort, stServerPath);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper#makeRequest(java.lang.String, org.fao.aoscs.model.semanticturkey.util.ParameterPair[])
	 */
	public Response makeRequest(String request, ParameterPair... pars) {
		if (pars==null || (pars.length==0))
			return askServer(getId(), request);		
		ArrayList<String> parameterLists = new ArrayList<String>();
		for (ParameterPair pair : pars) {	
			String value = pair.getParValue();
			if(value!=null && !value.equals(""))
			{
				parameterLists.add(pair.getParName()+"="+escape(value));
			}
		}
		String[] parameters = new String[parameterLists.size()];
		for(int i=0;i<parameterLists.size();i++)
		{
			parameters[i] = parameterLists.get(i);
		}
		return askServer(getId(), request, parameters);
	}
	
	/**
	 * @param parameter
	 * @return
	 */
	protected String escape(String parameter) {
		
		//TODO find a decent escape method somewhere in java
		try {
			return URLEncoder.encode(parameter,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return parameter;
		}
		/*return parameter.replace(" ", "%20")
						.replace("\n", "%0A")
						.replace("#", "%23")
						.replace("\\", "%5C")
						.replace("\"", "%22")
						.replace("|", "%7C")
						.replace("^", "%5E")
						.replace("<", "%3C")
						.replace(">", "%3E");*/	
	}

	
}
