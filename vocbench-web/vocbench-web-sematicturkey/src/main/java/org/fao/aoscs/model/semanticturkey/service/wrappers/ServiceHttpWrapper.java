package org.fao.aoscs.model.semanticturkey.service.wrappers;

import it.uniroma2.art.semanticturkey.servlet.Response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.fao.aoscs.model.semanticturkey.util.ParameterPair;

public class ServiceHttpWrapper extends ServletExtensionHttpWrapper implements ServiceWrapper {

	protected static Log logger = LogFactory.getLog(ServiceHttpWrapper.class);

	/**
	 * @param id
	 * @param stURL
	 */
	public ServiceHttpWrapper(String id, String stURL) {
		super(id, stURL);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper#makeRequest(java.lang.String, org.fao.aoscs.model.semanticturkey.util.ParameterPair[])
	 */
	public Response makeRequest(String request, ParameterPair... pars) {
		List<NameValuePair> parameterLists = new ArrayList<NameValuePair>();
		parameterLists.add(new BasicNameValuePair("service", getId()));
		parameterLists.add(new BasicNameValuePair("request", request));
		
		if (pars==null || (pars.length==0))
			return askServer(parameterLists);	
		
		for (ParameterPair pair : pars) {	
			String value = pair.getParValue();
			if(value!=null && !value.equals(""))
			{
				parameterLists.add(new BasicNameValuePair(pair.getParName(), value));
			}
		}
		return askServer(parameterLists);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper#makeRequest(java.lang.String, org.fao.aoscs.model.semanticturkey.util.ParameterPair[])
	 */
	public Response makeNewRequest(String request, ParameterPair... pars) {
		List<NameValuePair> parameterLists = new ArrayList<NameValuePair>();
		if (pars!=null && (pars.length>0))
		{
			for (ParameterPair pair : pars) {	
				String value = pair.getParValue();
				if(value!=null && !value.equals(""))
				{
					parameterLists.add(new BasicNameValuePair(pair.getParName(), value));
				}
			}
		}
		return askNewServer(getId(), request, parameterLists);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper#makeHttpRequest(java.lang.String, org.fao.aoscs.model.semanticturkey.util.ParameterPair[])
	 */
	public String makeHttpRequest(String request, ParameterPair... pars) {
		List<NameValuePair> parameterLists = new ArrayList<NameValuePair>();
		if (pars!=null && (pars.length>0))
		{
			for (ParameterPair pair : pars) {	
				String value = pair.getParValue();
				if(value!=null && !value.equals(""))
				{
					parameterLists.add(new BasicNameValuePair(pair.getParName(), value));
				}
			}
		}
		return askHttpServer(getId(), request, parameterLists);
	}
}
