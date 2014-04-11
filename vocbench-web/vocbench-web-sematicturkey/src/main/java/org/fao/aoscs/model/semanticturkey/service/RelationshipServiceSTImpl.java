package org.fao.aoscs.model.semanticturkey.service;

import it.uniroma2.art.owlart.vocabulary.OWL;
import it.uniroma2.art.owlart.vocabulary.RDF;
import it.uniroma2.art.owlart.vocabulary.RDFS;
import it.uniroma2.art.owlart.vocabulary.RDFTypesEnum;
import it.uniroma2.art.owlart.vocabulary.SKOS;
import it.uniroma2.art.owlart.vocabulary.SKOSXL;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.module.constant.OWLProperties;
import org.fao.aoscs.domain.ClassObject;
import org.fao.aoscs.domain.DomainRangeObject;
import org.fao.aoscs.domain.InitializeRelationshipData;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.RelationshipTreeObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.TreeObject;
import org.fao.aoscs.hibernate.DatabaseUtil;
import org.fao.aoscs.model.semanticturkey.service.manager.ClsManager;
import org.fao.aoscs.model.semanticturkey.service.manager.DeleteManager;
import org.fao.aoscs.model.semanticturkey.service.manager.ObjectManager;
import org.fao.aoscs.model.semanticturkey.service.manager.PropertyManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.VocbenchResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STUtility;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class RelationshipServiceSTImpl {
	
	/* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addDomain(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void addDomain(RelationshipObject rObj, String uri, int actionId, int userId, OntologyInfo ontoInfo){
    	
    	PropertyManager.addPropertyDomainRequest(ontoInfo, rObj.getUri(), uri);
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(uri);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }
    /*public void addDomain(RelationshipObject rObj, String uri, int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(owlModel.getResourceNameForURI(uri));
    	p.addUnionDomainClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(rdfCls.getName());			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addNewRelationship(java.lang.String, java.lang.String, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.RelationshipObject, java.lang.String, org.fao.aoscs.domain.OntologyInfo, int, int)
	 */
	public RelationshipObject addNewRelationship(String label, String language, String type, String superPropertyUri, String namespace, OntologyInfo ontoInfo, int userId, int actionId){
		
		String propertyName = STUtility.getcamelCased(label); 
		String propertyUri = namespace+propertyName;

		// Add Property
		if(superPropertyUri==null || superPropertyUri.equals(OWL.OBJECTPROPERTY) || superPropertyUri.equals(OWL.DATATYPEPROPERTY) || superPropertyUri.equals(OWL.ANNOTATIONPROPERTY) || superPropertyUri.equals(OWL.ONTOLOGYPROPERTY))
			PropertyManager.addTopProperty(ontoInfo, propertyUri, type);
		else
			PropertyManager.addProperty(ontoInfo, propertyUri, type, superPropertyUri);

		// Add label
		PropertyManager.addPlainLiteralPropValue(ontoInfo, propertyUri, RDFS.LABEL, label, language);
		
		LabelObject labelObj = new LabelObject();
		labelObj.setLabel(label);
		labelObj.setLanguage(language);
		
		ArrayList<LabelObject> labelList = new ArrayList<LabelObject>();
		labelList.add(labelObj);
		
		RelationshipObject newProperty = new RelationshipObject();
		newProperty.setParent(superPropertyUri);
		newProperty.setType(type);
		newProperty.setUri(propertyUri);
		newProperty.setName(propertyName);
		newProperty.setLabelList(labelList);

		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
		obj.add(newProperty);

		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());		
		
		return newProperty;
	}
	/*public RelationshipObject addNewRelationship(String label , String language , RelationshipObject selectedItem ,RelationshipObject parentOfSelectedItem,RelationshipObject newProperty,String position, OntologyInfo ontoInfo , int userId , int actionId){
		long timeStamp = new java.util.Date().getTime();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);

		if(position.equals("sameLevel")){
			selectedItem = parentOfSelectedItem;
		}
		
		if(selectedItem.getType().equalsIgnoreCase(RelationshipObject.OBJECT)){
			OWLObjectProperty refProp = owlModel.getOWLObjectProperty(selectedItem.getName());
			OWLObjectProperty newProp = owlModel.createOWLObjectProperty(label.replaceAll(" ", "")+"_"+timeStamp);
			newProp.addLabel(label, language);
			newProp.addSuperproperty(refProp);
			
			newProperty.setUri(newProp.getURI());
			newProperty.setName(newProp.getName());
			newProperty.setParent(selectedItem.getUri());
			newProperty.setParentObject(selectedItem);
		}else{
			OWLDatatypeProperty refProp = owlModel.getOWLDatatypeProperty(selectedItem.getName());
			OWLDatatypeProperty newProp = owlModel.createOWLDatatypeProperty(label.replaceAll(" ", "")+"_"+timeStamp);
			newProp.addLabel(label, language);
			newProp.addSuperproperty(refProp);
			
			newProperty.setUri(newProp.getURI());
			newProperty.setName(newProp.getName());
			newProperty.setParent(selectedItem.getUri());
			newProperty.setParentObject(selectedItem);
		}		

		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
		obj.add(newProperty);

		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());		
		///owlModel.dispose();
		
		
		return newProperty;
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addProperty(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void addProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
		PropertyManager.addExistingPropValue(ontoInfo, rObj.getUri(), RDF.TYPE, OWLproperties);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(OWLproperties);
		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
	/*public void addProperty(RelationshipObject rObj,String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		if(OWLproperties.equals(OWLProperties.FUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setFunctional(true);
		}else if(OWLproperties.equals(OWLProperties.INVERSEFUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setInverseFunctional(true);
		}else if(OWLproperties.equals(OWLProperties.SYMMETRIC)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setSymmetric(true);
		}else if(OWLproperties.equals(OWLProperties.TRANSITIVE)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setTransitive(true);
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(OWLproperties);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#AddPropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void AddPropertyComment(RelationshipObject rObj,String comment,String language, int actionId, int userId, OntologyInfo ontoInfo){
		
		PropertyManager.addPlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.COMMENT, comment, language);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(comment);
		tnew.setLang(language);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());		
	}
	/*public void AddPropertyComment(RelationshipObject rObj,String comment,String language, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		RDFSLiteral commentValue = owlModel.createRDFSLiteral(comment, language);
		RDFProperty commentProp = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		p.addPropertyValue(commentProp, commentValue);
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(comment);
		tnew.setLang(language);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());		
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#AddPropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	/*public void AddPropertyLabel(RelationshipObject rObj,String label,String language,int actionId , int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		p.addLabel(label, language);				
		///owlModel.dispose();
		
		RecentChangeData rcData = new RecentChangeData();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(label);
		tnew.setLang(language);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}*/
	public void AddPropertyLabel(RelationshipObject rObj, String label, String language, int actionId, int userId, OntologyInfo ontoInfo){

		PropertyManager.addPlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.LABEL, label, language);
		
		RecentChangeData rcData = new RecentChangeData();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(label);
		tnew.setLang(language);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
	
	/* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void addRange(RelationshipObject rObj, String uri, int actionId, int userId, OntologyInfo ontoInfo){
    	
    	PropertyManager.addPropertyRange(ontoInfo, rObj.getUri(), uri);
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(uri);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }
    /*public void addRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLObjectProperty p =  owlModel.getOWLObjectProperty(rObj.getName());
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(cls);
    	p.addUnionRangeClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(cls);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }*/
    
    
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#editRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject editRange(RelationshipObject rObj, String oldURI, String newURI, int actionId, int userId, OntologyInfo ontoInfo){
    	
    	if(PropertyManager.removePropertyRange(ontoInfo, rObj.getUri(), oldURI))
    	{
	    	PropertyManager.addPropertyRange(ontoInfo, rObj.getUri(), newURI);
	    		
	    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
			obj.add(rObj);
			
			TranslationObject told = new TranslationObject();
			told.setLabel(oldURI);
			ArrayList<LightEntity> oldw = new ArrayList<LightEntity>();
			oldw.add(told);
							
			TranslationObject tnew = new TranslationObject();
			tnew.setLabel(newURI);
			ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
			neww.add(tnew);
			
			RecentChangeData rcData = new RecentChangeData();
			rcData.setObject(obj);			
			rcData.setNewObject(neww);
			rcData.setOldObject(oldw);
			rcData.setActionId(actionId);
			rcData.setModifierId(userId);
			rcData.setOwnerId(userId);
			
			DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    	}
    	
    	return getDomainRange(rObj.getUri(), ontoInfo);
    }
    
    /* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#editRangeValues(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.util.HashMap, java.util.HashMap, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
    public DomainRangeObject editRangeValues(RelationshipObject rObj, String uri, HashMap<String, String> oldValues, HashMap<String, String> newValues, int actionId, int userId, OntologyInfo ontoInfo){
    	
    	String oldvals = "";
    	for(String oldValue : oldValues.keySet())
    	{        	
    		if(!newValues.containsKey(oldValue))
    		{
    			String finaloldValue = "\""+oldValue+"\"^^<"+oldValues.get(oldValue)+">";;
    			PropertyManager.removeValueFromDatarange(ontoInfo, uri, finaloldValue);
    			if(oldvals.equals(""))
    				oldvals = oldValue; 
    			else
    				oldvals += ", " + oldValue;
    		}
    	}
    	
    	String newvals = "";
    	for(String newValue : newValues.keySet())
    	{        	
    		if(!oldValues.containsKey(newValue))
        	{
        		String finalnewValue = "\""+newValue+"\"^^<"+newValues.get(newValue)+">";;
        		PropertyManager.addValuesToDatarange(ontoInfo, uri, finalnewValue);
            	if(newvals.equals(""))
            		newvals = newValue; 
            	else
            		newvals += ", " + newValue;
        	}
    	}
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
		
		TranslationObject told = new TranslationObject();
		told.setLabel(oldvals);
		ArrayList<LightEntity> oldw = new ArrayList<LightEntity>();
		oldw.add(told);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(newvals);
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);
		rcData.setOldObject(oldw);
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    	
    	return getDomainRange(rObj.getUri(), ontoInfo);
    }
		
    /* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#addRangeValues(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.util.HashMap, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject addRangeValues(RelationshipObject rObj, String uri, HashMap<String, String> values , int actionId, int userId, OntologyInfo ontoInfo){    	
    	String vals = "";
    	if((values != null) && (values.size() > 0))
        {	
    		for(String value : values.keySet())
        	{        	
        		String finalValue = "\""+value+"\"^^<"+values.get(value)+">";
        		//PropertyManager.addValuesToDatarange(ontoInfo, uri, finalValue);
            	if(vals.equals(""))
            		vals = finalValue; 
            	else
            		vals += STXMLUtility.ST_SEPARATOR + finalValue;
        	}
        	PropertyManager.setDataRange(ontoInfo, rObj.getUri(), vals);
        }
        else
        {
        	PropertyManager.addPropertyRange(ontoInfo, rObj.getUri(), uri);
        }
        
        
        
        ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(uri);
		tnew.setDescription(vals);
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
		
		return getDomainRange(rObj.getUri(), ontoInfo);
    }
    
    
    /* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRangeValue(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> deleteRangeValue(RelationshipObject rObj, String dataRange, String value, int actionId, int userId, OntologyInfo ontoInfo) {
		
		PropertyManager.removeValueFromDatarange(ontoInfo, dataRange, value);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
		
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(value);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
		
		return getRangeValues(dataRange, ontoInfo);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRangeValues(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> getRangeValues(String dataRange, OntologyInfo ontoInfo) {
		return PropertyManager.parseDataRange(ontoInfo, dataRange, RDFTypesEnum.bnode.toString());
	}

    
    /*public void addRangeValues(RelationshipObject rObj, String type, ArrayList<String> values , int actionId, int userId, OntologyInfo ontoInfo){    	
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);    	
    	RDFProperty p  =  owlModel.getOWLDatatypeProperty(rObj.getName());
    	RDFSDatatype datatype = owlModel.getXSDstring();
    	
    	if(type.equals("Any"))		datatype = null;
    	if(type.equals("Boolean"))	datatype = owlModel.getXSDboolean();    	    	
    	if(type.equals("Float"))	datatype = owlModel.getXSDfloat();    	    	
    	if(type.equals("Int"))		datatype = owlModel.getXSDint();    	    	
    	if(type.equals("String"))	datatype = owlModel.getXSDstring();
    	
    	setRange(p, datatype);
    	String vals = "";
        if((values != null) && (values.size() > 0))
        {	
        	Iterator<String> ite = values.iterator();
        	int i=0;
        	while(ite.hasNext())
        	{        	
        		String temp = ite.next();
	        	setRangeValue(p , datatype, temp);
	        	vals += i>0? "; " : "";
	        	vals += temp;
	        	i++;	        	
        	}		
        }
        ///owlModel.dispose();
        
        ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(type);
		tnew.setDescription(vals);
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setNewObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }*/
	
	/* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteDomain(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject deleteDomain(RelationshipObject rObj, String uri,  int actionId, int userId, OntologyInfo ontoInfo){
    	PropertyManager.removePropertyDomainRequest(ontoInfo, rObj.getUri(), uri);
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(uri);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
		
		return getDomainRange(rObj.getUri(), ontoInfo);
    }
    /*public void deleteDomain(RelationshipObject rObj, String cls,  int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLProperty p =  owlModel.getOWLProperty(rObj.getName());    	
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(cls);    	
    	p.removeUnionDomainClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(cls);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteInverseProperty(int, int, org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject)
	 */
	public void deleteInverseProperty(int actionId, int userId, OntologyInfo ontoInfo, RelationshipObject rObj)
	{
		PropertyManager.removeAllPropValue(ontoInfo, rObj.getUri(), OWL.INVERSEOF);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);				
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}
	
	/* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteProperty(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public void deleteProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
    	PropertyManager.removeResourcePropValue(ontoInfo, rObj.getUri(), RDF.TYPE, OWLproperties);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(OWLproperties);			
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
/*    public void deleteProperty(RelationshipObject rObj, String OWLproperties, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);

		if(OWLproperties.equals(OWLProperties.FUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setFunctional(false);
		}else if(OWLproperties.equals(OWLProperties.INVERSEFUNCTIONAL)){
			OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
			p.setInverseFunctional(false);
		}else if(OWLproperties.equals(OWLProperties.SYMMETRIC)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setSymmetric(false);
		}else if(OWLproperties.equals(OWLProperties.TRANSITIVE)){
			OWLObjectProperty p = owlModel.getOWLObjectProperty(rObj.getName());
			p.setTransitive(false);
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(OWLproperties);			
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#DeletePropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void DeletePropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		PropertyManager.removePlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.COMMENT, oldComment, oldLanguage);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldComment);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}
	/*public void DeletePropertyComment(RelationshipObject rObj, String oldComment,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		
		RDFSLiteral oldCommentValue = owlModel.createRDFSLiteral(oldComment, oldLanguage);
		RDFProperty commentProp = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		
		for (Iterator<?> iter = p.getComments().iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof RDFSLiteral) {
				RDFSLiteral element = (RDFSLiteral) obj;
				if (oldComment.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
					p.removePropertyValue(commentProp, oldCommentValue);
				}
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldComment);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#DeletePropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void DeletePropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		PropertyManager.removePlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.LABEL, oldLabel, oldLanguage);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldLabel);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}
	/*public void DeletePropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		Collection<?> c = p.getLabels();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			DefaultRDFSLiteral element = (DefaultRDFSLiteral) iter.next();
			if (oldLabel.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
				p.removeLabel(oldLabel, oldLanguage);
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldLabel);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}*/
	
	/* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRange(org.fao.aoscs.domain.RelationshipObject, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
     */
    public DomainRangeObject deleteRange(RelationshipObject rObj, String uri, int actionId, int userId, OntologyInfo ontoInfo){

    	PropertyManager.removePropertyRange(ontoInfo, rObj.getUri(), uri);
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(uri);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
		
		return getDomainRange(rObj.getUri(), ontoInfo);
    }
    /*public void deleteRange(RelationshipObject rObj, String cls, int actionId, int userId, OntologyInfo ontoInfo){
    	OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
    	OWLObjectProperty p =  owlModel.getOWLObjectProperty(rObj.getName());
    	RDFSClass rdfCls = owlModel.getRDFSNamedClass(cls);
    	p.removeUnionRangeClass(rdfCls);
    	///owlModel.dispose();
    	
    	ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(cls);			
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(neww);						
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
    }*/

	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#deleteRelationship(org.fao.aoscs.domain.RelationshipObject, org.fao.aoscs.domain.OntologyInfo)
	 */
	public boolean deleteRelationship(RelationshipObject rObj, int actionId, int userId, OntologyInfo ontoInfo){
		
		boolean success = false;
		
		success = DeleteManager.deleteProperty(ontoInfo, rObj.getUri());
		
		if(success)
		{
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();
			obj.add(rObj);
			
			RecentChangeData rcData = new RecentChangeData();
			rcData.setObject(obj);			
			rcData.setOldObject(obj);						
			rcData.setActionId(actionId);
			rcData.setModifierId(userId);
			rcData.setOwnerId(userId);
			
			DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());
		}
		
		return success;
	}
	/*public void deleteRelationship(RelationshipObject selectedItem, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		String type = selectedItem.getType();
		if(type.equalsIgnoreCase(RelationshipObject.OBJECT)){
			OWLObjectProperty refProp = owlModel.getOWLObjectProperty(selectedItem.getName());
			refProp.delete();
		}else{
			OWLDatatypeProperty refProp = owlModel.getOWLDatatypeProperty(selectedItem.getName());
			refProp.delete();
		}
		///owlModel.dispose();
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#EditPropertyComment(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void EditPropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		
		if(PropertyManager.removePlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.COMMENT, oldComment, oldLanguage))
		{
			PropertyManager.addPlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.COMMENT, newComment, newLanguage);
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
			obj.add(rObj);
							
			TranslationObject told = new TranslationObject();
			told.setLabel(oldComment);
			told.setLang(oldLanguage);		
			ArrayList<LightEntity> old = new ArrayList<LightEntity>();
			old.add(told);
			
			TranslationObject tnew = new TranslationObject();
			tnew.setLabel(newComment);
			tnew.setLang(newLanguage);		
			ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
			neww.add(tnew);
			
			RecentChangeData rcData = new RecentChangeData();
			rcData.setObject(obj);			
			rcData.setOldObject(old);		
			rcData.setNewObject(neww);		
			rcData.setActionId(actionId);
			rcData.setModifierId(userId);
			rcData.setOwnerId(userId);
			
			DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
		}
	}
	/*public void EditPropertyComment(RelationshipObject rObj,String oldComment,String oldLanguage,String newComment,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		RDFSLiteral oldCommentValue = owlModel.createRDFSLiteral(oldComment, oldLanguage);
		RDFSLiteral newCommentValue = owlModel.createRDFSLiteral(newComment, newLanguage);
		RDFProperty commentProp = owlModel.getRDFProperty(RDFSNames.Slot.COMMENT);
		
		Collection<?> c = p.getComments();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof RDFSLiteral) {
				RDFSLiteral element = (RDFSLiteral) obj;
				if (oldComment.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
					p.removePropertyValue(commentProp, oldCommentValue);
					p.addPropertyValue(commentProp, newCommentValue);
				}
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject told = new TranslationObject();
		told.setLabel(oldComment);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(newComment);
		tnew.setLang(newLanguage);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);			
		rcData.setOldObject(old);		
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#EditPropertyLabel(org.fao.aoscs.domain.RelationshipObject, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public void EditPropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage,String newLabel,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		
		if(PropertyManager.removePlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.LABEL, oldLabel, oldLanguage))
		{
			PropertyManager.addPlainLiteralPropValue(ontoInfo, rObj.getUri(), RDFS.LABEL, newLabel, newLanguage);
			
			ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
			obj.add(rObj);
							
			TranslationObject tnew = new TranslationObject();
			tnew.setLabel(newLabel);
			tnew.setLang(newLanguage);		
			ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
			neww.add(tnew);
			
			TranslationObject told = new TranslationObject();
			told.setLabel(oldLabel);
			told.setLang(oldLanguage);		
			ArrayList<LightEntity> old = new ArrayList<LightEntity>();
			old.add(told);
			
			RecentChangeData rcData = new RecentChangeData();
			rcData.setObject(obj);
			rcData.setNewObject(neww);		
			rcData.setOldObject(old);		
			rcData.setActionId(actionId);
			rcData.setModifierId(userId);
			rcData.setOwnerId(userId);
			
			DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
		}
	}
	/*public void EditPropertyLabel(RelationshipObject rObj,String oldLabel,String oldLanguage,String newLabel,String newLanguage, int actionId, int userId, OntologyInfo ontoInfo){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLProperty p =  owlModel.getOWLProperty(rObj.getName());
		Collection<?> c = p.getLabels();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			DefaultRDFSLiteral element = (DefaultRDFSLiteral) iter.next();
			if (oldLabel.equals(element.getString()) && oldLanguage.equals(element.getLanguage())) {
				p.removeLabel(oldLabel, oldLanguage);
				p.addLabel(newLabel, newLanguage);
			}
		}
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(newLabel);
		tnew.setLang(newLanguage);		
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		TranslationObject told = new TranslationObject();
		told.setLabel(oldLabel);
		told.setLang(oldLanguage);		
		ArrayList<LightEntity> old = new ArrayList<LightEntity>();
		old.add(told);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setOldObject(old);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());	
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getClassItemList(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<ClassObject> getClassItemList(String clsName, OntologyInfo ontoInfo){
		return ClsManager.getSubClasses(ontoInfo, clsName);
	}
	/*public ArrayList<ClassObject> getClassItemList(String rootName,OntologyInfo ontoInfo){
		ArrayList<ClassObject> list = new ArrayList<ClassObject>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLNamedClass cls = owlModel.getOWLNamedClass(rootName);
		for (Iterator<?> iter = cls.getSubclasses(false).iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof OWLNamedClass) {
				OWLNamedClass childCls = (OWLNamedClass) obj;
				if(!childCls.isSystem()){
					ClassObject cObj = new ClassObject();
					cObj.setUri(childCls.getURI());
					cObj.setLabel(childCls.getName());
					cObj.setName(childCls.getName());
					if(childCls.getSubclassCount()>0){
			    		cObj.setHasChild(true);
			    	}else{
			    		cObj.setHasChild(false);
			    	}
					list.add(cObj);
				}
			}
		}
		///owlModel.dispose();
		return list; 	
	}*/
	
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getInverseProperty(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public RelationshipObject getInverseProperty(OntologyInfo ontoInfo, String relationship){
		if(relationship!=null)
		{
			RelationshipObject rObj = ObjectManager.createRelationshipObject(ontoInfo, relationship);
			
			if(rObj!=null)
			{
				return ObjectManager.createRelationshipObject(ontoInfo, rObj.getInverse());
			}
			else
				return null;
		}
		else
			return null;
	}
	/*public RelationshipObject getInverseProperty(OntologyInfo ontoInfo, String relationship){
		if(relationship!=null)
		{
			OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
			OWLObjectProperty prop = owlModel.getOWLObjectProperty(owlModel.getResourceNameForURI(relationship));
			RDFProperty insProp = prop.getInverseProperty();
			
			if(insProp!=null){
				RelationshipObject rObj = new RelationshipObject();
				rObj.setUri(insProp.getURI());
				rObj.setName(insProp.getName());
				if (insProp instanceof OWLObjectProperty) {
					rObj.setType(RelationshipObject.OBJECT);
				}else if (insProp instanceof OWLDatatypeProperty){
					rObj.setType(RelationshipObject.DATATYPE);
				}
				
				Collection<?> labelList = insProp.getLabels();
		    	for (Iterator<?> iterator = labelList.iterator(); iterator.hasNext();) {
		    		
		    		Object obj = (Object) iterator.next();
		    		if (obj instanceof DefaultRDFSLiteral) {
						DefaultRDFSLiteral label = (DefaultRDFSLiteral) obj;
						rObj.addLabel(label.getString(), label.getLanguage());
					}else{
						rObj.addLabel(obj.toString(), "");
					}
				}
		    	///owlModel.dispose();
				return rObj;
			}else{
				///owlModel.dispose();
				return null;
			}
		}
		else
			return null;
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getObjectPropertyTree(java.lang.Integer, boolean, org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationshipTreeObject getObjectPropertyTree(Integer relType, boolean includeSelfRelationship, OntologyInfo ontoInfo){
		
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		
		if(relType == 0)
		{
			rtObj =  getObjectPropertyTree(ontoInfo, SKOS.RELATED, includeSelfRelationship, rtObj);
		}
		else if(relType == 1)
		{
			rtObj = getObjectPropertyTree(ontoInfo, SKOSXL.LABELRELATION, includeSelfRelationship, rtObj);
		}
		else if(relType == 2)
		{
			rtObj = getObjectPropertyTree(ontoInfo, SKOS.RELATED, includeSelfRelationship, rtObj);
			rtObj = getObjectPropertyTree(ontoInfo, SKOSXL.LABELRELATION, includeSelfRelationship, rtObj);
		}
		else if(relType == 3)
		{
			rtObj = getPropertyTree(ontoInfo, RelationshipObject.OBJECT);
		}
	    return rtObj;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipObject(org.fao.aoscs.domain.OntologyInfo, java.lang.String)
	 */
	public RelationshipObject getRelationshipObject(OntologyInfo ontoInfo, String propertyURI) {
		return ObjectManager.createRelationshipObject(ontoInfo, propertyURI);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipTree(org.fao.aoscs.domain.OntologyInfo)
	 */
	public RelationshipTreeObject getRelationshipTree(String type, OntologyInfo ontoInfo){
		return getPropertyTree(ontoInfo, type);
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipTree(java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<TreeObject> getNarrowerRelationship(String propertyURI, ArrayList<String> lang, OntologyInfo ontoInfo){
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#initData(int, org.fao.aoscs.domain.OntologyInfo)
	 */
	public InitializeRelationshipData initData(int group_id, OntologyInfo ontoInfo){
		InitializeRelationshipData data = new InitializeRelationshipData();
		//data.setRelationshipTree(getRelationshipTree(ontoInfo, RelationshipObject.OBJECT));
		//data.setClassTree(getClassItemList("owl:Thing", ontoInfo));
		data.setAllDataType(PropertyManager.getAllRangeDatatype());
		//data.setActionMap(SystemUtility.getActionMap(group_id));
		return data;
	}
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#setInverseProperty(int, int, org.fao.aoscs.domain.OntologyInfo, org.fao.aoscs.domain.RelationshipObject, java.lang.String)
	 */
	public void setInverseProperty(int actionId, int userId, OntologyInfo ontoInfo, RelationshipObject rObj, String relUri){
		PropertyManager.addExistingPropValue(ontoInfo, rObj.getUri(), OWL.INVERSEOF, relUri);
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(relUri);				
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());			
	}
	/*public void setInverseProperty(int actionId , int userId, OntologyInfo ontoInfo,RelationshipObject rObj, String insName){
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		OWLObjectProperty prop = owlModel.getOWLObjectProperty(rObj.getName());
		OWLObjectProperty insProp = owlModel.getOWLObjectProperty(insName);
		prop.setInverseProperty(insProp);
		///owlModel.dispose();
		
		ArrayList<LightEntity> obj = new ArrayList<LightEntity>();		
		obj.add(rObj);
						
		TranslationObject tnew = new TranslationObject();
		tnew.setLabel(insName);				
		ArrayList<LightEntity> neww = new ArrayList<LightEntity>();
		neww.add(tnew);
		
		RecentChangeData rcData = new RecentChangeData();
		rcData.setObject(obj);
		rcData.setNewObject(neww);		
		rcData.setActionId(actionId);
		rcData.setModifierId(userId);
		rcData.setOwnerId(userId);
		
		DatabaseUtil.addRecentChange(rcData, ontoInfo.getOntologyId());			
	}*/
	

	/**
	 * @param dataElement
	 * @param rootElement
	 * @param rootPropertyName
	 * @param type
	 * @return
	 
	private static Element getChildProperty(Element dataElement, Element rootElement, String rootPropertyName, String type)
	{
		for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
		{
			for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "Property"))
			{
				if(rootPropertyName.equals(propElement.getAttribute("name")))
				{
					rootElement = propElement;
				}
				rootElement = getChildProperty(propElement, rootElement, rootPropertyName, type);
			}
		}
		return rootElement;
	}*/

	/**
	 * @param dataElement
	 * @param rtObj
	 * @param type
	 * @return
	 */
	private static RelationshipTreeObject getChildProperty(OntologyInfo ontoInfo, Element dataElement, RelationshipTreeObject rtObj, String type)
	{
		for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
		{
			for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "Property"))
			{
				if(type.equals(propElement.getAttribute("type")))
				{
					rtObj = getPropertyDetail(ontoInfo, rtObj, propElement.getAttribute("name"), propElement.getAttribute("uri"), dataElement.getAttribute("uri"), false, type);
					rtObj = getChildProperty(ontoInfo, propElement, rtObj, type);
				}
			}
		}
		return rtObj;
	}

	/**
	 * @param dataElement
	 * @param rtObj
	 * @param type
	 * @return
	 */
	private static RelationshipTreeObject getSubProperty(Element dataElement, RelationshipTreeObject rtObj, String type)
	{
		for(Element subpropElement : STXMLUtility.getChildElementByTagName(dataElement, "SubProperties"))
		{
			for(Element propElement : STXMLUtility.getChildElementByTagName(subpropElement, "PropInfo"))
			{
				rtObj = getPropertyDetail(propElement, rtObj, false, type);
			}
		}
		return rtObj;
	}
	
	/**
	 * @param ontoInfo
	 * @param rtObj
	 * @param propertyName
	 * @param parentPropertyName
	 * @param rootItem
	 * @param type
	 * @return
	 */
	/*private static RelationshipTreeObject getPropertyDetail(OntologyInfo ontoInfo, RelationshipTreeObject rtObj, String propertyName, String parentPropertyName, boolean rootItem, String type)
	{
		String rootProp = "";
		if(rootItem)
		{
			rootProp = STUtility.getUri(ontoInfo, type);
		}
		else
		{
			rootProp = STUtility.getUri(ontoInfo, parentPropertyName);
		}

		ArrayList<LabelObject> propertyDefinitions = new ArrayList<LabelObject>();

		RelationshipObject rObj = new RelationshipObject();
		rObj.setUri(STUtility.getUri(ontoInfo, propertyName));
		rObj.setName(propertyName);
		rObj.setType(type);
		rObj.setRootItem(rootItem);
		rObj.setParent(rootProp);
		
		rtObj.addRelationshipList(rObj);
		rtObj.addParentChild(rootProp, rObj);
		rtObj.addRelationshipDefinition(rObj.getUri(), propertyDefinitions);
		return rtObj;
	}*/
	
	/**
	 * @param ontoInfo
	 * @param rtObj
	 * @param propertyName
	 * @param parentPropertyName
	 * @param rootItem
	 * @param type
	 * @param nsMap
	 * @return
	 */
	private static RelationshipTreeObject getPropertyDetail(OntologyInfo ontoInfo, RelationshipTreeObject rtObj, String propertyName, String propertyURI, String parentPropertyURI, boolean rootItem, String type)
	{
		ArrayList<LabelObject> propertyDefinitions = new ArrayList<LabelObject>();
		RelationshipObject rObj = new RelationshipObject();
		rObj.setUri(propertyURI);
		rObj.setName(propertyName);
		rObj.setType(type);
		rObj.setRootItem(rootItem);
		rObj.setParent(parentPropertyURI);
		
		rtObj.addRelationshipList(rObj);
		rtObj.addParentChild(parentPropertyURI, rObj);
		rtObj.addRelationshipDefinition(rObj.getUri(), propertyDefinitions);
		return rtObj;
	}
	
	/**
	 * @param rtObj
	 * @param propertyName
	 * @param rootItem
	 * @return
	 */
	private static RelationshipTreeObject getPropertyDetail(Element element, RelationshipTreeObject rtObj, boolean rootItem, String type)
	{
		ArrayList<LabelObject> propertyDefinitions = new ArrayList<LabelObject>();
		RelationshipObject rObj = new RelationshipObject();

		//Super Types
		for(Element superTypesElement : STXMLUtility.getChildElementByTagName(element, "SuperTypes"))
		{
			for(Element colElement : STXMLUtility.getChildElementByTagName(superTypesElement, "collection"))
			{
				for(Element uriElement : STXMLUtility.getChildElementByTagName(colElement, "uri"))
				{
					rObj.setParent(uriElement.getTextContent());
				}
			}
		}
		
		//Definitions
		for(Element defListElement : STXMLUtility.getChildElementByTagName(element, "definitions"))
		{
			for(Element defElement : STXMLUtility.getChildElementByTagName(defListElement, "definition"))
			{
				LabelObject lObj = new LabelObject();
				lObj.setLabel(defElement.getAttribute("label"));
				lObj.setLanguage(defElement.getAttribute("lang"));
				propertyDefinitions.add(lObj);
			}
		}
		
		//Property Info
		for(Element superTypesElement : STXMLUtility.getChildElementByTagName(element, "propertyInfo"))
		{
			for(Element colElement : STXMLUtility.getChildElementByTagName(superTypesElement, "property"))
			{
				for(Element uriElement : STXMLUtility.getChildElementByTagName(colElement, "uri"))
				{
					rObj.setUri(uriElement.getTextContent());
					rObj.setName(uriElement.getAttribute("show"));
				}
			}
		}
		
		rtObj = getSubProperty(element, rtObj, RelationshipObject.OBJECT);
		
		rObj.setType(type);
		rObj.setRootItem(rootItem);
		//if(rObj.getParent()==null)
			//rObj.setParent(type);
		
		rtObj.addRelationshipList(rObj);
		rtObj.addParentChild(rObj.getParent(), rObj);
		rtObj.addRelationshipDefinition(rObj.getUri(), propertyDefinitions);
		
		
		
		
		return rtObj;
	}
	
	/**
	 * @param ontoInfo
	 * @param type
	 * @return
	 */
	public RelationshipTreeObject getPropertyTree(OntologyInfo ontoInfo, String type)
	{
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		XMLResponseREPLY reply = PropertyManager.getPropertiesTreeRequest(ontoInfo, type);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				if(type.equals(propElement.getAttribute("type")))
				{
					rtObj = getPropertyDetail(ontoInfo, rtObj, propElement.getAttribute("name"), propElement.getAttribute("uri"), null, true, type);
					rtObj = getChildProperty(ontoInfo, propElement, rtObj, type);
				}
			}
		}
	    
		return rtObj;
	}
	
	
	/**
	 * @return
	
	private RelationshipTreeObject getDatatypePropertyTree()
	{
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		XMLResponseREPLY reply = PropertyResponseManager.getPropertiesTreeRequest(Property.Req.getDatatypePropertiesTreeRequest);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				rtObj = getPropertyDetail(rtObj, propElement.getAttribute("name"), RelationshipObject.DATATYPE, true, RelationshipObject.DATATYPE);
				rtObj = getChildProperty(propElement, rtObj, RelationshipObject.DATATYPE);
			}
		}
	    
		return rtObj;
	} */
	
	/**
	 * @return
	 
	public RelationshipTreeObject getObjectPropertyTree()
	{
		RelationshipTreeObject rtObj = new RelationshipTreeObject();
		XMLResponseREPLY reply = PropertyResponseManager.getPropertiesTreeRequest(Property.Req.getObjPropertiesTreeRequest);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				rtObj = getPropertyDetail(rtObj, propElement.getAttribute("name"), RelationshipObject.OBJECT, true, RelationshipObject.OBJECT);
				rtObj = getChildProperty(propElement, rtObj, RelationshipObject.OBJECT);
			}
		}
	    
		return rtObj;
	}*/
    
    /**
	 * @param rootNode
	 * @param includeSelfRelationship
	 * @return
	 */
	private RelationshipTreeObject getObjectPropertyTree(OntologyInfo ontoInfo, String rootPropURI, boolean includeSelfRelationship, RelationshipTreeObject rtObj)
	{
		XMLResponseREPLY reply = VocbenchResponseManager.getSubProperties(ontoInfo, rootPropURI, includeSelfRelationship, false);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "PropInfo"))
			{
				rtObj = getPropertyDetail(propElement, rtObj, includeSelfRelationship, RelationshipObject.OBJECT);
			}
		}
	    
		return rtObj;
	}
	/*private RelationshipTreeObject getObjectPropertyTree(String rootNode, boolean includeSelfRelationship, RelationshipTreeObject rtObj)
	{
	    Element rootElement = null;
	    
		XMLResponseREPLY reply = PropertyResponseManager.getPropertiesTreeRequest(Property.Req.getObjPropertiesTreeRequest);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Property"))
			{
				if(rootNode.equals(propElement.getAttribute("name")))
				{
					rootElement = propElement;
				}
				else 
					rootElement = getChildProperty(propElement, rootElement, rootNode, RelationshipObject.OBJECT);
			}
			
			if(includeSelfRelationship)
				rtObj = getPropertyDetail(rtObj, rootNode, RelationshipObject.OBJECT,true, RelationshipObject.OBJECT);
			
		   	if(rootElement!=null)
		   	{
		   		rtObj = getChildProperty(rootElement, rtObj, RelationshipObject.OBJECT);
		   	}
		}
	    
		return rtObj;
	}*/
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipLabels(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<LabelObject> getRelationshipLabels(String propertyURI, OntologyInfo ontoInfo){
		
		return getRelationshipObject(ontoInfo, propertyURI).getLabelList();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipComments(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<LabelObject> getRelationshipComments(String propertyURI, OntologyInfo ontoInfo){
		return getRelationshipObject(ontoInfo, propertyURI).getCommentList();
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getRelationshipProperties(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public ArrayList<String> getRelationshipProperties(String propertyURI, OntologyInfo ontoInfo){
		
		RelationshipObject relationshipObject = getRelationshipObject(ontoInfo, propertyURI);
		ArrayList<String> list = new ArrayList<String>();
		if(relationshipObject.isFunctional())
			list.add(OWLProperties.FUNCTIONAL);
		if(relationshipObject.isInverseFunctional())
			list.add(OWLProperties.INVERSEFUNCTIONAL);
		if(relationshipObject.isTransitive())
			list.add(OWLProperties.TRANSITIVE);
		if(relationshipObject.isSymmetric())
			list.add(OWLProperties.SYMMETRIC);
		return list; 
	}
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getDomainRange(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getDomainRange(String propertyURI, OntologyInfo ontoInfo){
		return getRelationshipObject(ontoInfo, propertyURI).getDomainRangeObject();
	}
	
	
	/* (non-Javadoc)
	 * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#getDomainRangeDatatype(java.lang.String, org.fao.aoscs.domain.OntologyInfo)
	 */
	public DomainRangeObject getDomainRangeDatatype(String propertyURI, OntologyInfo ontoInfo){				
		return getRelationshipObject(ontoInfo, propertyURI).getDomainRangeObject();
	}
	
	
	/* (non-Javadoc)
     * @see org.fao.aoscs.client.module.relationship.service.RelationshipService#setDataTypeRange(java.lang.String, java.lang.String, org.fao.aoscs.domain.OntologyInfo)
     
    public void setDataTypeRange(String relationship, String type, OntologyInfo ontoInfo){
   }*/
    
    /**
	 * @param relationship
	 * @param owlModel
	 * @return
	 
	private ArrayList<ClassObject> getDomain(String relationship, OWLModel owlModel){
		ArrayList<ClassObject> list = new ArrayList<ClassObject>();
		OWLProperty p =  owlModel.getOWLProperty(owlModel.getResourceNameForURI(relationship));
	    Collection<?> c = p.getDomains(true);
	    for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Object obj = (Object) iter.next();
			if (obj instanceof DefaultOWLUnionClass) {
				DefaultOWLUnionClass element = (DefaultOWLUnionClass) obj;
				Iterator<?> it =  element.listOperands();
				while (it.hasNext()) {
					DefaultOWLNamedClass cls = (DefaultOWLNamedClass) it.next();
					ClassObject cObj = new ClassObject();
					cObj.setType(DomainRangeObject.DOMAIN);
					cObj.setUri(cls.getURI());
					cObj.setName(cls.getName());
					cObj.setLabel(cls.getName());
					list.add(cObj);
				}
			}else if (obj instanceof DefaultOWLNamedClass) {
				DefaultOWLNamedClass element = (DefaultOWLNamedClass) obj;
				ClassObject cObj = new ClassObject();
				cObj.setType(DomainRangeObject.DOMAIN);
				cObj.setUri(element.getURI());
				cObj.setName(element.getName());
				cObj.setLabel(element.getName());
				list.add(cObj);
			}
		}
	    return list; 
	}*/
	
	/**
	 * @param ontoInfo
	 * @return
	 
	private HashMap<String, String> getRDFSDatatype(OntologyInfo ontoInfo){
		HashMap<String, String> map = new HashMap<String, String>();
		OWLModel owlModel = ProtegeModelFactory.getOWLModel(ontoInfo);
		Collection<?> c = owlModel.getRDFSDatatypes();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			RDFSDatatype type = (RDFSDatatype) iter.next();			
			map.put(type.getBrowserText(), type.getName());
		}
		///owlModel.dispose();
		return map;
	}*/	
	
	

}