package org.fao.aoscs.client.module.icv.service;

import java.util.ArrayList;

import org.fao.aoscs.domain.DanglingConceptObject;
import org.fao.aoscs.domain.ManagedDanglingConceptObject;
import org.fao.aoscs.domain.OntologyInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rajbhandari
 *
 */
	@RemoteServiceRelativePath("icv")
	public interface ICVService extends RemoteService {

		public ArrayList<DanglingConceptObject> listDanglingConcepts(OntologyInfo ontoInfo) throws Exception;
		public void manageDanglingConcepts(OntologyInfo ontoInfo, ArrayList<ManagedDanglingConceptObject> list) throws Exception;
		
		public static class ICVServiceUtil{
			private static ICVServiceAsync<?> instance;
			public static ICVServiceAsync<?> getInstance(){
				if (instance == null) {
					instance = (ICVServiceAsync<?>) GWT.create(ICVService.class);
				}
				return instance;
	      }
	    }
	   }
