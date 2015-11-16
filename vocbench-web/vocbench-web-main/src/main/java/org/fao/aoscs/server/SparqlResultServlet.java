package org.fao.aoscs.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.semanticturkey.service.manager.SparqlManager;
import org.hibernate.Session;

/**
 * @author rajbhandari
 *
 */

public class SparqlResultServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		doPost(request, response);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        try{
            String ontology = "";
            String query = "";
            String language = "";
            String mode = "";
            boolean infer = false;
            
        	Enumeration<?> itr = request.getParameterNames();
            while (itr.hasMoreElements()) {
                String name = (String) itr.nextElement();
                String value = request.getParameter(name);         
                if(name.equals("query"))
                	query = value;
                else if(name.equals("language"))
                	language = value;
                else if(name.equals("infer"))
                	infer = value.equals("true")?true:false;
                else if(name.equals("ontology"))
                	ontology = value;
                else if(name.equals("mode"))
                	mode = value;
            }
            
            
            
            byte[] responseStr = getSparqlExportData(getOntology(ontology), query, language, infer, mode);
            
            response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=result.txt"); 
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new String(responseStr, "UTF-8"));
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
	
	/**
	 * @param ontoInfo
	 * @param query
	 * @param language
	 * @param infer
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private byte[] getSparqlExportData(OntologyInfo ontoInfo, String query, String language, boolean infer, String mode) throws UnsupportedEncodingException
	{
		ArrayList<ArrayList<String>> result =  SparqlManager.resolveQuery(ontoInfo, query, language, infer, mode);
		StringBuffer strBuffer = new StringBuffer();
		for(ArrayList<String> row : result)
		{
			String line = "";
			for(String str : row)
			{
				if(line.length()>0)
					line += ", ";
				line += str;
			}
			strBuffer.append(line+"\n");
		}
		return strBuffer.toString().getBytes("UTF-8");
	}
	
	/**
	 * @param ontology
	 * @return
	 */
	private OntologyInfo getOntology(String ontology)
	{
		OntologyInfo ontoInfo = new OntologyInfo();	
		try {
			String sqlStr = "SELECT * FROM ontology_info WHERE ontology_id =  '"+ontology+"' ";
			
			Session s = HibernateUtilities.currentSession();
			@SuppressWarnings("unchecked")
			ArrayList<OntologyInfo> ontoList = (ArrayList<OntologyInfo>) s.createSQLQuery(sqlStr).addEntity(OntologyInfo.class).list();
            
            if(ontoList.size()>0)
            	ontoInfo = ontoList.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtilities.closeSession();
			
		}
		return ontoInfo;

	}
}