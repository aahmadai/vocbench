package org.fao.aoscs.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author rajbhandari
 *
 */

public class FileUpload extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/*public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		doPost(request, response);
	}*/
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        System.out.println("Serverrrrrrrrrrrrrrrr");
		
		ServletFileUpload upload = new ServletFileUpload();

        try{
            FileItemIterator iter = upload.getItemIterator(request);
            System.out.println(iter);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                if(name.equals("localFile"))
                {
	                InputStream stream = item.openStream();
	                System.out.println("filename: "+name+"  "+stream);
	
	
	                // Process the input stream
	                //ByteArrayOutputStream out = new ByteArrayOutputStream();
	                File f = new File("c:\\data\\test.txt");
	                System.out.println("filepath: "+f.getPath()+"  "+f.exists());
	                FileOutputStream out = new FileOutputStream(f);
	                int len;
	                byte[] buffer = new byte[8192];
	                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
	                    out.write(buffer, 0, len);
	                }
	
	               /* int maxFileSize = 10*(1024*1024); //10 megs max 
	                if (out.size() > maxFileSize) { 
	                    throw new RuntimeException("File is > than " + maxFileSize);
	                }*/
	                
	                out.flush();
	                out.close();
                }
            }
            response.setContentType("text/plain");
            response.getWriter().write("OK");
        }
        catch(Exception e){
            //throw new RuntimeException(e);
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("NOTOK");
        }

    }
}