package org.fao.aoscs.server.rss;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadRSS extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String format = request.getParameter("format")==null?"atom_1.0":request.getParameter("format");
			int ontologyId = request.getParameter("ontologyId")==null?0:Integer.parseInt(request.getParameter("ontologyId"));
			int rcid = request.getParameter("rcid")==null?0:Integer.parseInt(request.getParameter("rcid"));
			int limit = request.getParameter("limit")==null?15:Integer.parseInt(request.getParameter("limit"));
			limit = (limit==0)?15:limit;
			limit = (limit>1000)?1000:limit;
			int page = request.getParameter("page")==null?0:Integer.parseInt(request.getParameter("page"));
			String feedtype = request.getParameter("feedtype")==null?UtilityRSS.FEEDTYPE_PAGED:request.getParameter("feedtype");
			String basepath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			String data = new GenerateRSS().getFeed(basepath, format, ontologyId, rcid, limit, page, feedtype);
			
			response.setHeader("Content-Disposition", "inline; filename=rss.xml");
			response.setContentType("application/rss+xml"); 
			
			ServletOutputStream op = response.getOutputStream(); 
			op.write(data.getBytes("UTF-8"));
			op.flush(); 
			op.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
