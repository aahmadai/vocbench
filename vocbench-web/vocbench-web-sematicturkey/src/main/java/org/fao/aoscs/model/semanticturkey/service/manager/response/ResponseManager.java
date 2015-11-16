package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.ServiceVocabulary.RepliesStatus;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseERROR;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseEXCEPTION;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.fao.aoscs.model.semanticturkey.util.STModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ResponseManager.class);

	/**
	 * @return
	 */
	protected static STModel getSTModel(OntologyInfo ontoInfo)
	{
		return STModelFactory.getSTModel(ontoInfo);
	}
	
	/**
	 * @param resp
	 * @return
	 */
	protected static XMLResponseREPLY getXMLResponseREPLY(Response resp)
	{
		if(resp!=null)
		{
			if(resp instanceof XMLResponseREPLY)
			{
				logger.debug("\n----- ST RESPONSE -----\n"+resp.getResponseContent());
				if(((XMLResponseREPLY) resp).getReplyStatus().equals(RepliesStatus.fail))
					throw new RuntimeException(((XMLResponseREPLY) resp).getReplyMessage());
				else
					return ((XMLResponseREPLY) resp);
			}
			else if(resp instanceof XMLResponseERROR)
			{
				logger.error("\n----- ST ERROR ----- "+((XMLResponseERROR) resp).getMessage());
				throw new RuntimeException(((XMLResponseERROR) resp).getMessage());
			}
			else if(resp instanceof XMLResponseEXCEPTION)
			{
				logger.error("\n----- ST EXCEPTION ----- "+((XMLResponseEXCEPTION) resp).getMessage());
				throw new RuntimeException(((XMLResponseEXCEPTION) resp).getMessage());
			}
			else
				logger.error(resp.getResponseContent());
				//(new RuntimeException(resp.getResponseContent())).printStackTrace();
			}
		return null;
	}
	
	public static boolean getReplyStatus(XMLResponseREPLY reply)
	{
		if(reply!=null)
		{
			return reply.getReplyStatus().equals(RepliesStatus.ok);
		}
		return false;
	}
	
	public static boolean isNULL(String val)
	{
		return (val==null || val.equals("") || val.equals(""));
	}
}
