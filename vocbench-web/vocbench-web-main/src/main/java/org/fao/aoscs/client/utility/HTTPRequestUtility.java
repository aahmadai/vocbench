package org.fao.aoscs.client.utility;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.HTML;

public class HTTPRequestUtility {

	public static HTML getHTMLResponse(String url)
	{
		final HTML html = new HTML();
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

		try 
		{
			builder.sendRequest(null, new RequestCallback() {
		    public void onError(Request request, Throwable exception) {
		    }

		    public void onResponseReceived(Request request, Response response) {
		      if (200 == response.getStatusCode()) {
		    	  html.setHTML(response.getText());
		      }
		    }
		  });
		} catch (RequestException e) {
		}
		return html;
	}
}
