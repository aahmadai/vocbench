package org.fao.aoscs.model.semanticturkey.service;

import org.fao.aoscs.domain.ClassificationObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.InitializeConceptData;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.OwlStatus;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;

public class ClassificationServiceSTImpl{

	
	public InitializeConceptData initData(int group_id, OntologyInfo ontoInfo){
		InitializeConceptData data = new InitializeConceptData();
		try
		{
			//TODO Replace Protege based code with new one
			/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			data.setSource(SystemUtility.getSource());
			data.setTermCodeProperties(ProtegeUtility.getAllTermCodeProperties(owlModel));
			data.setConceptDomainAttributes(ProtegeUtility.getDatatypeProperties(owlModel, ProtegeModelConstants.RCONCEPTDOMAINDATATYPEPROPERTY));
			data.setConceptEditorialAttributes(ProtegeUtility.getDatatypeProperties(owlModel, ProtegeModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY));
			data.setConceptEditorialAttributes(ProtegeUtility.getDatatypeProperties(owlModel, ProtegeModelConstants.RCONCEPTEDITORIALDATATYPEPROPERTY));
			data.setTermDomainAttributes(ProtegeUtility.getDatatypeProperties(owlModel, ProtegeModelConstants.RTERMDOMAINDATATYPEPROPERTY));
			data.setActionMap(SystemUtility.getActionMap(group_id));
			data.setActionStatus(SystemUtility.getActionStatusMap(group_id));
			data.setTermCodePropertyType(ProtegeUtility.getTermCodeType(owlModel));
			data.setBelongsToModule(InitializeConceptData.CLASSIFICATIONMODULE);
			data.setClassificationObject(getCategoryTree(ontoInfo));
			///owlModel.dispose();
*/		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}
	
	
	
	public ClassificationObject getCategoryTree(OntologyInfo ontoInfo){
		ClassificationObject clsObj = new ClassificationObject();
		//TODO Replace Protege based code with new one
		/*try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLNamedClass scheme = owlModel.getOWLNamedClass(ProtegeModelConstants.CCLASSIFICATIONSCHEME);
			OWLProperty hasCategory = owlModel.getOWLProperty(ProtegeModelConstants.RHASCATEGORY);
			
			
			for (Iterator<?> iter = scheme.getInstances(true).iterator(); iter.hasNext();) {
				OWLIndividual schemeIns = (OWLIndividual) iter.next();
				if(!schemeIns.isBeingDeleted())
				{
					SchemeObject sObj = getSchemeObject(schemeIns);
					for (Iterator<?> iterator = schemeIns.getPropertyValues(hasCategory).iterator(); iterator.hasNext();) {
						OWLIndividual categoryIns = (OWLIndividual) iterator.next();
						OWLProperty isSubPropertyOf = owlModel.getOWLProperty(sObj.getRIsSub()); 	
						ConceptObject cObj = ProtegeUtility.makeConceptObject(owlModel, (OWLNamedClass)categoryIns.getProtegeType());		
						cObj.setHasChild(false);
						cObj.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
						cObj.setScheme(schemeIns.getURI());
						OWLIndividual parent = getCategoryParent(isSubPropertyOf, categoryIns);
						if(parent!=null ){
							cObj.setParentURI(parent.getURI());
							cObj.setRootItem(false);
						}else{
							cObj.setRootItem(true);
						}		
						sObj.addCategoryList(cObj.getUri(), cObj);
					}
					clsObj.addSchemeList(sObj.getSchemeInstance(), sObj);
				}
			}
			///owlModel.dispose();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
		return clsObj;
	}
	//TODO Replace Protege code with new one
	/*public OWLIndividual getCategoryParent(OWLProperty isSubPropertyOf,OWLIndividual categoryIns){
		OWLIndividual ins  = null;
		for (Iterator<?> iter = categoryIns.getPropertyValues(isSubPropertyOf).iterator(); iter.hasNext();) {
			OWLIndividual parentIns = (OWLIndividual) iter.next();
			ins = parentIns;
		}
		return ins;
	}*/
	
	//TODO Replace Protege code with new one
	
	/*public SchemeObject getSchemeObject(OWLIndividual schemeIns){
		SchemeObject sObj = new SchemeObject();
		//TODO Replace Protege based code with new one
		sObj.setNamespace(schemeIns.getNamespace());
		sObj.setSchemeName(schemeIns.getName());
		sObj.setSchemeInstance(schemeIns.getURI());
		sObj.setNameSpaceCatagoryPrefix(schemeIns.getNamespacePrefix());
		String schemeLabel =getSchemeLabel(schemeIns);
		String schemeDescription =getSchemeComment(schemeIns);
		if(schemeLabel!=null){
			sObj.setSchemeLabel(schemeLabel);
		}
		if(schemeDescription!=null){
			sObj.setDescription(schemeDescription);
		}
		sObj.setRHasSub(ProtegeModelConstants.RHAS+schemeIns.getLocalName().replaceAll("i_", "")+ProtegeModelConstants.SUBCATEGORY);
		sObj.setRIsSub(ProtegeModelConstants.RIS+schemeIns.getLocalName().replaceAll("i_", "")+ProtegeModelConstants.SUBCATEGORYOF);
		return sObj;
	}*/
	/*private String getSchemeLabel(OWLIndividual schemeIns){
		String label = null;
		Collection<?> co = schemeIns.getLabels();
		for (Iterator<?> iter = co.iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof RDFSLiteral) {
				RDFSLiteral element = (RDFSLiteral) obj;
				if(element.getLanguage().equals("en")){
					label = element.getString();
				}
			}
		}
		return label;
	}
	
	private String getSchemeComment(OWLIndividual schemeIns){
		String comment = null;
		for (Iterator<?> iter = schemeIns.getComments().iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof String) {
				String element = (String) obj;
				comment = element;
			}
		}
		return comment;
	}*/
	
	public void deleteScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj){
		//TODO Replace Protege code with new one
		/*try
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			
			for (Iterator<?> it = owlModel.getOWLNamedClass(ProtegeModelConstants.CCATEGORY).getSubclasses(true).iterator(); it.hasNext();) 
			{		    		    
				OWLNamedClass cls = (OWLNamedClass) it.next();			
				for (Iterator<?> jt = cls.getInstances(false).iterator(); jt.hasNext();) 
				{
					OWLIndividual individual = (OWLIndividual) jt.next();
		        	Object obj = individual.getPropertyValue(owlModel.getOWLProperty(ProtegeModelConstants.RBELONGSTOSCHEME));
		        	if(obj instanceof OWLIndividual)
		        	{
		        		OWLIndividual scheme = (OWLIndividual) obj;
			        	if(scheme.getName().equals(oldSObj.getSchemeName()))
			        	{
			        		individual.delete();
			        		cls.delete();
			        	}
		        	}
	    			
				}
			}
			
			for (Iterator<?> iter = owlModel.getOWLNamedClass(ProtegeModelConstants.CCLASSIFICATIONSCHEME).getInstances(true).iterator(); iter.hasNext();) {
				OWLIndividual schemeInsa = (OWLIndividual) iter.next();
				if(schemeInsa.getName().equals(oldSObj.getSchemeName()))
				{
					schemeInsa.delete();
					schemeInsa = null;
				}
				
			}
			
			OWLObjectProperty isSubOf = owlModel.getOWLObjectProperty(oldSObj.getRIsSub());
			if(isSubOf!=null)
			{
				isSubOf.delete();
			}

			
			OWLObjectProperty hasSub = owlModel.getOWLObjectProperty(oldSObj.getRHasSub());
			if(isSubOf!=null)
			{
				hasSub.delete();
			}
			
			if(owlModel.getNamespaceManager().getPrefix(oldSObj.getNamespace())!=null)
				owlModel.getNamespaceManager().removePrefix(oldSObj.getNameSpaceCatagoryPrefix());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		///owlModel.dispose();
*/	}

	public void editScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject oldSObj,SchemeObject newSObj){
		//TODO Replace Protege code with new one
		/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(oldSObj.getSchemeName());
		schemeIns.removeLabel(oldSObj.getSchemeLabel(), "en");
		schemeIns.addLabel(newSObj.getSchemeLabel(), "en");
		schemeIns.setComment(newSObj.getDescription());
		
		TermObject termObj = new TermObject();
		termObj.setLabel(oldSObj.getSchemeLabel());
		termObj.setLang("en");
		termObj.setMainLabel(true);
		
		ConceptObject blankConcept = new ConceptObject();
		blankConcept.addTerm(termObj.getUri(), termObj);
		blankConcept.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);*/
	/*	
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(blankConcept));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(newSObj));
		v.setOldValue(DatabaseUtil.setObject(oldSObj));
		v.setOntologyId(ontologyId);
		v.setDateModified(Date.getROMEDate());
		DatabaseUtil.createObject(v);*/
		
		///owlModel.dispose();
	}
	
	public void addNewScheme(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,SchemeObject sObj){
		
		//TODO Replace Protege code with new one
		/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass classification = owlModel.getOWLNamedClass(ProtegeModelConstants.CCLASSIFICATIONSCHEME);
		OWLNamedClass cCategory = owlModel.getOWLNamedClass(ProtegeModelConstants.CCATEGORY);
		owlModel.getNamespaceManager().setPrefix(sObj.getNamespace(), sObj.getNameSpaceCatagoryPrefix());
		OWLIndividual schemeIns = classification.createOWLIndividual(sObj.getSchemeName());
		schemeIns.addLabel(sObj.getSchemeLabel(), "en");
		schemeIns.addComment(sObj.getDescription());
		//String nameSpaceForCategory = schemeIns.getName().replaceAll("i_", "").toLowerCase();
		//owlModel.getNamespaceManager().setPrefix(ProtegeModelConstants.COREBASENAMESPACE+nameSpaceForCategory+"#", nameSpaceForCategory);
		
		OWLObjectProperty hasSubCategory =  owlModel.getOWLObjectProperty(ProtegeModelConstants.RHASSUBCATEGORY);
		OWLObjectProperty newHasSub = owlModel.createOWLObjectProperty(sObj.getRHasSub());
		newHasSub.addLabel(ProtegeModelConstants.RHAS+" "+sObj.getNameSpaceCatagoryPrefix().toUpperCase()+" "+ProtegeModelConstants.SUBCATEGORY, "en");
		newHasSub.addSuperproperty(hasSubCategory);
		newHasSub.setTransitive(true);
		newHasSub.setDomain(cCategory);
		newHasSub.setRange(cCategory);
		
		OWLObjectProperty isSubCategoryOf =  owlModel.getOWLObjectProperty(ProtegeModelConstants.RISSUBCATEGORYOF);
		OWLObjectProperty newIsSub = owlModel.createOWLObjectProperty(sObj.getRIsSub());
		newIsSub.addLabel(ProtegeModelConstants.RIS+" "+sObj.getNameSpaceCatagoryPrefix().toUpperCase()+" "+ProtegeModelConstants.SUBCATEGORYOF, "en");
		newIsSub.addSuperproperty(isSubCategoryOf);
		newIsSub.setTransitive(true);
		newIsSub.setDomain(cCategory);
		newIsSub.setRange(cCategory);
		
		newIsSub.setInverseProperty(newHasSub);
		
		TermObject termObj = new TermObject();
		termObj.setLabel("New Scheme");
		termObj.setLang("en");
		termObj.setMainLabel(true);
		
		ConceptObject blankConcept = new ConceptObject();
		blankConcept.addTerm(termObj.getUri(), termObj);
		blankConcept.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(blankConcept));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(sObj));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
*/	}
	
	//TODO Replace Protege code with new one
	/*private void setInstanceUpdateDate(OntologyInfo ontoInfo, OWLModel owlModel,OWLIndividual ins){
		OWLProperty updateDate = owlModel.getOWLProperty(ProtegeModelConstants.RHASUPDATEDDATE);
		ins.setPropertyValue(updateDate, DateUtility.getDateTime());
	}*/
	
	public String addFirstNewCategory(OntologyInfo ontoInfo, int actionId,int userId,OwlStatus status,TermObject termObject ,ConceptObject conceptObject,SchemeObject schemeObject){

		//TODO Replace Protege code with new one
		/*long unique = new java.util.Date().getTime();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty lexicon = owlModel.getOWLProperty(ProtegeModelConstants.RHASLEXICALIZATION);
		OWLProperty mainlabel = owlModel.getOWLProperty(ProtegeModelConstants.RISMAINLABEL);
		OWLProperty createDate = owlModel.getOWLProperty(ProtegeModelConstants.RHASDATECREATED);
		OWLProperty statusProp = owlModel.getOWLProperty(ProtegeModelConstants.RHASSTATUS);
		OWLProperty hasCategory = owlModel.getOWLProperty(ProtegeModelConstants.RHASCATEGORY);
		OWLNamedClass nounCls = owlModel.getOWLNamedClass(ProtegeModelConstants.CNOUN);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
		
		OWLNamedClass cls = owlModel.createOWLNamedClass(schemeObject.getNameSpaceCatagoryPrefix()+":"+"c_"+unique);
		OWLNamedClass cCategory = owlModel.getOWLNamedClass(ProtegeModelConstants.CCATEGORY);
		cls.addSuperclass(cCategory);
		
		OWLIndividual term = (OWLIndividual) nounCls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+termObject.getLang().toLowerCase()+"_"+unique);
		term.addLabel(termObject.getLabel(), termObject.getLang());
		term.addPropertyValue(mainlabel, termObject.isMainLabel());
		term.addPropertyValue(createDate, DateUtility.getDateTime());
		term.addPropertyValue(statusProp, status.getStatus());
		setInstanceUpdateDate(ontoInfo, owlModel, term);
		 
		OWLIndividual ins = (OWLIndividual) cls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+unique);
		ins.addPropertyValue(lexicon, term);
		ins.addPropertyValue(createDate, DateUtility.getDateTime());
		ins.addPropertyValue(statusProp, status.getStatus());
		setInstanceUpdateDate(ontoInfo, owlModel, ins);
		schemeIns.addPropertyValue(hasCategory, ins);
		
		
		//conceptObject.setConceptInstance(ins.getURI());
		conceptObject.setUri(cls.getURI());
		conceptObject.setName(cls.getName());
		conceptObject.setStatus(status.getStatus());
		conceptObject.setScheme(schemeIns.getURI());
		termObject.setUri(term.getURI());
		termObject.setName(term.getName());
		termObject.setConceptUri(conceptObject.getUri());
		termObject.setConceptName(conceptObject.getName());
		conceptObject.addTerm(termObject.getUri(), termObject);
		 
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(getCategoryParentObject()));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(conceptObject));
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		v.setOntologyId(ontoInfo.getOntologyId());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
*/		return conceptObject.getUri();
	}
	/*private ConceptObject getCategoryParentObject(){
		ConceptObject parentObject = new ConceptObject();
		
		//TODO Replace Protege code with new one
		TermObject termObject = new TermObject();
		termObject.setLabel("Top level concept");
		termObject.setUri("");
		termObject.setLang("en");
		termObject.setConceptUri(ProtegeModelConstants.CCATEGORY);
		termObject.setConceptName(ProtegeModelConstants.CCATEGORY);
		
		
		parentObject.setUri(ProtegeModelConstants.COMMONBASENAMESPACE+ProtegeModelConstants.CCATEGORY);
		parentObject.addTerm(termObject.getUri(), termObject);
		parentObject.setName(ProtegeModelConstants.CCATEGORY);
		parentObject.setBelongsToModule(ConceptObject.CLASSIFICATIONMODULE);
		
		return parentObject;
	}*/
	public void deleteCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject conceptObject,SchemeObject schemeObject){
		
		//TODO Replace Protege code with new one
		/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptObject.getName());
		OWLIndividual ins = ProtegeUtility.getConceptInstance(owlModel, cls);
		OWLProperty statusProp = owlModel.getOWLProperty(ProtegeModelConstants.RHASSTATUS);
		ins.setPropertyValue(statusProp, status.getStatus());
		setInstanceUpdateDate(ontoInfo, owlModel, ins);
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(conceptObject));
		v.setOldValue(DatabaseUtil.setObject(conceptObject));
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setAction(actionId);
		v.setOldStatusLabel(conceptObject.getStatus());
		v.setOldStatus(conceptObject.getStatusID());
		v.setStatus(status.getId());
		v.setStatusLabel(status.getStatus());
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(conceptObject.getDateCreate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();
*/		
	}
	public String addNewCategory(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject parentObject,String position,TermObject termObject,ConceptObject conceptObject,SchemeObject schemeObject){
		//TODO Replace Protege code with new one
		/*try
		{
			long unique = new java.util.Date().getTime();
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLProperty lexicon = owlModel.getOWLProperty(ProtegeModelConstants.RHASLEXICALIZATION);
			OWLProperty mainlabel = owlModel.getOWLProperty(ProtegeModelConstants.RISMAINLABEL);
			OWLProperty createDate = owlModel.getOWLProperty(ProtegeModelConstants.RHASDATECREATED);
			OWLProperty statusProp = owlModel.getOWLProperty(ProtegeModelConstants.RHASSTATUS);
			OWLProperty hasCategory = owlModel.getOWLProperty(ProtegeModelConstants.RHASCATEGORY);
			OWLNamedClass nounCls = owlModel.getOWLNamedClass(ProtegeModelConstants.CNOUN);
			
			OWLNamedClass cls = owlModel.createOWLNamedClass(schemeObject.getNameSpaceCatagoryPrefix()+":"+"c_"+unique);
			OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
			
			OWLIndividual term = (OWLIndividual) nounCls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+termObject.getLang().toLowerCase()+"_"+unique);
			term.addLabel(termObject.getLabel(), termObject.getLang());
			term.addPropertyValue(mainlabel, termObject.isMainLabel());
			term.addPropertyValue(createDate, DateUtility.getDateTime());
			term.addPropertyValue(statusProp, status.getStatus());
			setInstanceUpdateDate(ontoInfo, owlModel, term);
			 
			OWLIndividual ins = (OWLIndividual) cls.createInstance(schemeObject.getNameSpaceCatagoryPrefix()+":"+"i_"+unique);
			ins.addPropertyValue(lexicon, term);
			ins.addPropertyValue(createDate, DateUtility.getDateTime());
			ins.addPropertyValue(statusProp, status.getStatus());
			setInstanceUpdateDate(ontoInfo, owlModel, ins);
			schemeIns.addPropertyValue(hasCategory, ins);
			
			
			OWLNamedClass parentCls;
			OWLIndividual parentIns;
			if(!parentObject.getName().endsWith(ProtegeModelConstants.CCATEGORY)){
				parentCls = owlModel.getOWLNamedClass(parentObject.getName());
				parentIns = ProtegeUtility.getConceptInstance(owlModel, parentCls);
				OWLProperty isSub = owlModel.getOWLProperty(schemeObject.getRIsSub());
				ins.addPropertyValue(isSub,parentIns);
			}
			else
			{
				parentObject = getCategoryParentObject();
				parentCls = owlModel.getOWLNamedClass(ProtegeModelConstants.CCATEGORY);
			}
			cls.addSuperclass(parentCls);
			
			//conceptObject.setConceptInstance(ins.getURI());
			conceptObject.setUri(cls.getURI());
			conceptObject.setName(cls.getName());
			conceptObject.setStatus(status.getStatus());
			conceptObject.setScheme(schemeObject.getSchemeInstance());
			termObject.setUri(term.getURI());
			termObject.setName(term.getName());
			termObject.setConceptUri(conceptObject.getUri());
			termObject.setConceptName(conceptObject.getName());
			conceptObject.addTerm(termObject.getUri(), termObject);
			 
			Validation v = new Validation();
			v.setConcept(DatabaseUtil.setObject(parentObject));
			v.setAction(actionId);
			v.setOwnerId(userId);
			v.setModifierId(userId);
			v.setStatus(status.getId());
			v.setNewValue(DatabaseUtil.setObject(conceptObject));
			v.setDateCreate(DateUtility.getROMEDate());
			v.setDateModified(DateUtility.getROMEDate());
			v.setOntologyId(ontoInfo.getOntologyId());
			DatabaseUtil.createObject(v);
			///owlModel.dispose();
			return conceptObject.getUri();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}*/
		return "";
	}
	
	public String makeLinkToFirstConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,String conceptName,SchemeObject schemeObject){
		ConceptObject conceptObject = new ConceptObject();
		//TODO Replace Protege code with new one
		/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual ins = ProtegeUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasCategory = owlModel.getOWLProperty(ProtegeModelConstants.RHASCATEGORY);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
		schemeIns.addPropertyValue(hasCategory, ins);
		
		conceptObject = ProtegeUtility.makeConceptObject(owlModel, cls);
		conceptObject.setScheme(schemeObject.getSchemeInstance());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(getCategoryParentObject()));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(conceptObject));
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		v.setOntologyId(ontoInfo.getOntologyId());
		DatabaseUtil.createObject(v);
		///owlModel.dispose();*/
		return conceptObject.getUri();
	}
	
	public String makeLinkToConcept(OntologyInfo ontoInfo,int actionId,int userId,OwlStatus status,ConceptObject refConcept,String position,String conceptName,SchemeObject schemeObject){
		ConceptObject conceptObject = new ConceptObject();
		//TODO Replace Protege code with new one
		/*OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(conceptName);
		OWLIndividual ins = ProtegeUtility.getConceptInstance(owlModel, cls);
		OWLProperty hasCategory = owlModel.getOWLProperty(ProtegeModelConstants.RHASCATEGORY);
		OWLIndividual schemeIns = owlModel.getOWLIndividual(schemeObject.getSchemeName());
		String parentURI = refConcept.getUri();
		if(!position.equals(CellTreeAOS.SUBLEVEL)){
			parentURI = refConcept.getParentURI();
		}
		
		schemeIns.addPropertyValue(hasCategory, ins);
		
		OWLNamedClass parentCls = owlModel.getOWLNamedClass(owlModel.getResourceNameForURI(parentURI));
		if(!parentCls.getName().equals(ProtegeModelConstants.CCATEGORY)){
			OWLIndividual parentIns = ProtegeUtility.getConceptInstance(owlModel, parentCls);
			OWLProperty hasSub = owlModel.getOWLProperty(schemeObject.getRHasSub());
			OWLProperty isSub = owlModel.getOWLProperty(schemeObject.getRIsSub());
			ins.addPropertyValue(isSub,parentIns);
			parentIns.addPropertyValue(hasSub,ins);
		}
		conceptObject = ProtegeUtility.makeConceptObject(owlModel, cls);
		conceptObject.setScheme(schemeObject.getSchemeInstance());
		
		Validation v = new Validation();
		v.setConcept(DatabaseUtil.setObject(refConcept));
		v.setAction(actionId);
		v.setOwnerId(userId);
		v.setModifierId(userId);
		v.setStatus(status.getId());
		v.setNewValue(DatabaseUtil.setObject(conceptObject));
		v.setOntologyId(ontoInfo.getOntologyId());
		v.setDateCreate(DateUtility.getROMEDate());
		v.setDateModified(DateUtility.getROMEDate());
		DatabaseUtil.createObject(v);	
		///owlModel.dispose();*/
		return conceptObject.getUri();
	}
}
