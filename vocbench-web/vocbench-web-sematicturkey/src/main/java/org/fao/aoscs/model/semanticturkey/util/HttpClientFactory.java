package org.fao.aoscs.model.semanticturkey.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientFactory {

	protected static Log logger = LogFactory.getLog(HttpClientFactory.class);
	
	private static HttpClient httpclient;

    public synchronized static HttpClient getHttpClient() {
  
        if (httpclient != null)
            return httpclient;
         
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setDefaultMaxPerRoute(100);
		cm.setMaxTotal(200);
		
		logger.debug("MaxPerRoute/MaxTotal: "+cm.getDefaultMaxPerRoute()+"/"+ cm.getMaxTotal());
		
		RequestConfig defaultRequestConfig = RequestConfig.custom().build(); 
		
		httpclient = HttpClients.custom() 
			.setConnectionManager(cm) 
			.setDefaultRequestConfig(defaultRequestConfig) 
			.setConnectionReuseStrategy(new DefaultConnectionReuseStrategy()) 
			.build(); 
  
        return httpclient;
    } 
	
}
