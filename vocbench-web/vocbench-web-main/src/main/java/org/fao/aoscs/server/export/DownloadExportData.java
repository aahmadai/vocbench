package org.fao.aoscs.server.export;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

public class DownloadExportData extends HttpServlet{

	private static final long serialVersionUID = -5989419573348960057L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String filename = request.getParameter("filename")==null?"export.txt":request.getParameter("filename");
			String key = request.getParameter("key")==null?"":request.getParameter("key");
			long size = request.getParameter("size")==null?(1024*1024*1024*5):Long.parseLong(request.getParameter("size"));
			boolean forcezip = request.getParameter("forcezip")==null?false:Boolean.parseBoolean(request.getParameter("forcezip"));
			byte bytes[] = new byte[2048];
			if(!key.equals(""))
			{
				try
				{
					File file = new File(key);
					if(forcezip || file.length()>size)
					{
						bytes = zipFile(file.getPath(), filename);
						response.setContentType("application/zip");
						response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".zip");
					}
					else
					{
						bytes = FileUtils.readFileToString(file, "UTF-8").getBytes("UTF-8");
						response.setContentType("application/octet-stream");
						response.setHeader("Content-Disposition", "attachment; filename=" + filename);
					}
					 if (file != null) {
					      file.delete();
					 }
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			ServletOutputStream op = response.getOutputStream(); 
            op.write(bytes);
            op.flush(); 
            op.close(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String data = "No Export Data";
			String filename = request.getParameter("filename")==null?"export.txt":request.getParameter("filename");
			String key = request.getParameter("key")==null?"":request.getParameter("key");
			if(!key.equals(""))
			{
				try
				{
					//data = (String) request.getSession().getAttribute(key);
					//request.getSession().removeAttribute(key);

					File file = new File(key);
					data = FileUtils.readFileToString(file, "UTF-8");
					 if (file != null) {
					      file.delete();
					 }
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			ServletOutputStream op = response.getOutputStream(); 
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");  
            op.write(data.getBytes("UTF-8"));
            op.flush(); 
            op.close(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
	
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
	
	private byte[] zipFile(String fileName, String zipFileName) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
 
        
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        zos.putNextEntry(new ZipEntry(zipFileName));
 
        int bytesRead;
        while ((bytesRead = bis.read(bytes)) != -1) {
        	zos.write(bytes, 0, bytesRead);
        }
        
        zos.closeEntry();
        bis.close();
        fis.close();									
        
        zos.flush();
        baos.flush();
        
        zos.close();
        baos.close();
 
        return baos.toByteArray();
    }
}
