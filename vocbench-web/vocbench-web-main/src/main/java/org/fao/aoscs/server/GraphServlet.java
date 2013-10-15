package org.fao.aoscs.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.hibernate.HibernateUtilities;
import org.fao.aoscs.model.semanticturkey.service.GraphServiceSTImpl;
import org.hibernate.Session;

/**
 * @author rajbhandari
 *
 */

public class GraphServlet extends HttpServlet{

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
            HashMap<String, String> parameters = new HashMap<String, String>();
            String ontology = "";
            String serviceVal = "";
            String requestVal = "";
            
        	Enumeration<?> itr = request.getParameterNames();
            while (itr.hasMoreElements()) {
                String name = (String) itr.nextElement();
                String value = request.getParameter(name);                
                if(name.equals("service"))
                	serviceVal = value;
                else if(name.equals("request"))
                	requestVal = value;
                else if(name.equals("ontology"))
                	ontology = value;
                else
                	parameters.put(name, value);
            }
            
            byte[] responseStr = new GraphServiceSTImpl().getResponse(getOntology(ontology), serviceVal, requestVal, parameters);
            
            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new String(responseStr, "UTF-8"));
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
	
	/**
	 * @param ontology
	 * @return
	 */
	public OntologyInfo getOntology(String ontology)
	{
		OntologyInfo ontoInfo = new OntologyInfo();	
		try {
			String sqlStr = "SELECT * FROM ontology_info WHERE ontology_name =  '"+ontology+"' ";
			
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