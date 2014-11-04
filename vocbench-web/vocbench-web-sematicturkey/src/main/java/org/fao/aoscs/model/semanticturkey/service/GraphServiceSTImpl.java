/**
 * 
 */
package org.fao.aoscs.model.semanticturkey.service;

import it.uniroma2.art.semanticturkey.servlet.Response;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.wrappers.ServiceWrapper;
import org.fao.aoscs.model.semanticturkey.util.ParameterPair;
import org.fao.aoscs.model.semanticturkey.util.STModel;

/**
 * @author rajbhandari
 *
 */
public class GraphServiceSTImpl extends ResponseManager {
	
	public byte[] getResponse(OntologyInfo ontoInfo, String service, String request, HashMap<String, String> parameters) throws UnsupportedEncodingException {
		
		ServiceWrapper serviceWrapper = null;
		if(service.equals("resource"))
			serviceWrapper = getSTModel(ontoInfo).resourceService;
		else if(service.equals("vocbench"))
			serviceWrapper = getSTModel(ontoInfo).vocbenchService;
		else if(service.equals("property"))
			serviceWrapper = getSTModel(ontoInfo).propertyService;
		else if(service.equals("skos"))
			serviceWrapper = getSTModel(ontoInfo).skosService;
		
		ParameterPair[] pair = new ParameterPair[parameters.size()+1];
		int i=0;
		for(String name : parameters.keySet())
		{
			pair [i] = STModel.par(name, parameters.get(name));
			i++;
		}
		pair[i] = STModel.par("ctx_project", ontoInfo.getDbTableName());
		
		if(serviceWrapper!=null)
		{
			Response resp = serviceWrapper.makeRequest(request, pair);
			return resp.getResponseContent().getBytes("UTF-8");
		}
		return new byte[0];
	}

}
