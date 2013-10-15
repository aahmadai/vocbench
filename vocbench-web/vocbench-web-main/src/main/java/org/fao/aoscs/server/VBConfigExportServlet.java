/**
 * 
 */
package org.fao.aoscs.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

/**
 * @author rajbhandari
 *
 */
public class VBConfigExportServlet extends HttpServlet{
	
	private static final long serialVersionUID = -3828240243825720555L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			PropertiesConfiguration config = new PropertiesConfiguration("Config.properties");
			File file = config.getFile();
			String data = FileUtils.readFileToString(file, "UTF-8");
			
			ServletOutputStream op = response.getOutputStream(); 
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + config.getFileName() + "");  
            op.write(data.getBytes("UTF-8"));
            op.flush(); 
            op.close(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			doGet(request, response); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
