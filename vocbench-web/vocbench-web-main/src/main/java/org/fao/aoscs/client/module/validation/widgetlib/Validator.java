package org.fao.aoscs.client.module.validation.widgetlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.MainApp;
import org.fao.aoscs.client.image.AOSImageManager;
import org.fao.aoscs.client.module.concept.widgetlib.ConceptTab;
import org.fao.aoscs.client.module.validation.ValidationTemplate;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.utility.ModuleManager;
import org.fao.aoscs.client.widgetlib.shared.filter.FilterValidation;
import org.fao.aoscs.client.widgetlib.shared.panel.HorizontalFlowPanel;
import org.fao.aoscs.domain.AttributesObject;
import org.fao.aoscs.domain.ConceptObject;
import org.fao.aoscs.domain.IDObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.LinkingConceptObject;
import org.fao.aoscs.domain.NonFuncObject;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.SchemeObject;
import org.fao.aoscs.domain.TermObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Validation;
import org.fao.aoscs.domain.ValidationPermission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Validator extends ValidationTemplate {

	public void init(int size)
	{
		initLoadingTable();
		if(valFilter == null)
			valFilter = new FilterValidation(this);
		initLayout(size);
	}

	public void initLayout(int size)
	{
		validatorPanel.clear();
		validatorPanel.setSize("100%","100%");
	    
		makeTitlePanel();
		initTable(size);
		makeValidationPanel();
		
		validatorPanel.add(titlePanel);
		validatorPanel.add(tablePanel);
		validatorPanel.add(validationFooterPanel);
		validatorPanel.setCellWidth(tablePanel, "100%");
		validatorPanel.setCellHeight(tablePanel, "100%");
		
		
	}

	public void initTable(int size)
	{
		vTable = new ValidationCellTable();
		vTable.setTable(MainApp.vFilter, getStatusList(), getUserList(), getActionList(), getAcceptvalidationList(), getRejectvalidationList(), size);
	    tablePanel.clear();
	    tablePanel.setSize("100%", "100%");
	    tablePanel.add(vTable.getLayout());
	    tablePanel.setCellHeight(vTable.getLayout(), "100%");
	    tablePanel.setCellWidth(vTable.getLayout(), "100%");
	}

	public static String checkNullValueInParenthesis(String obj)
	{
		if(obj==null || obj.length()<1)
			return "";
		else 
			return " ("+obj+")";
	}

	public void managePermission(ArrayList<ValidationPermission> list)
	{
		Iterator<ValidationPermission> itr = list.iterator();
		while(itr.hasNext())
		{
			ValidationPermission vp = (ValidationPermission) itr.next();
			if(vp.getAction()==1)
			{
				addAcceptvalidationList(new Integer(vp.getStatus()), new Integer(vp.getNewstatus()));	
			}
			else  if(vp.getAction()==2)
			{
				addRejectvalidationList(new Integer(vp.getStatus()), new Integer(vp.getNewstatus()));
			}
		}
	}

	public void filterByLanguage(){
		reLoad();
	}
	
	public static Widget makeUsers(String user, final String id, String style)
	{
		Label label = new Label(user);
		label.setStyleName(style);
		label.setTitle(user);
		label.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadUser(id);
			}
		});
		return label;
	}
	public static Widget getImagePanel(final int type, final String conceptURI, final String link, final String schemeURI, String style, final boolean isAddAction, final int tab, final int belongsToModule)
	{
		String imgURL = "images/spacer.gif";
		if(type==1)
		{
			if(conceptURI!=null)
			{
				imgURL = AOSImageManager.getConceptImageURL(conceptURI);
			}
			else
			{
				imgURL = "images/concept_logo.gif";
			}
		}
		else if(type==2){
			imgURL = "images/term-logo.gif";
		}
		else if(type==3){
			imgURL = "images/relationship-object-logo.gif";
		}
		else if(type==4){
			imgURL = "images/scheme-object-logo.gif";
		}
		else
			imgURL = "images/spacer.gif";
		Image image = new Image(imgURL);
		if(link!= null)
		{
			//if(!link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY) && !link.equals(ModelConstants.CCATEGORY))
			{
				image.setStyleName(style);
				image.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						ModuleManager.gotoItem(link, schemeURI, isAddAction, tab, belongsToModule, type);
					}
				});
			}
		}
		return image;
	}
	public static Widget getLabelPanel(final int type, String text, String title, final String link, final String schemeURI, String style, final boolean isAddAction, final int tab, final int belongsToModule)
	{
		if(text != null && !text.equals(""))
		{
			HTML label = new HTML(" "+text, true);
			if(link!=null)
			{
				//if(!link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.CDOMAINCONCEPT) && !link.equals(ModelConstants.COMMONBASENAMESPACE+ModelConstants.CCATEGORY) && !link.equals(ModelConstants.CCATEGORY))
				{
					label.setStyleName(style);
					label.setTitle(title);
					label.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							ModuleManager.gotoItem(link, schemeURI, isAddAction, tab, belongsToModule, type);
						}
					});
				}
			}
			return label;
		}
		else
		{
			Image image = new Image("images/label-not-found.gif");
			image.setTitle(MainApp.constants.homeNoTerm());
			image.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					ModuleManager.gotoItem(link, schemeURI, isAddAction, tab, belongsToModule, type);
				}
			});
			return image;
		}
	}
	public static Widget getExtLinkLabelPanel(String text, String title, String link, String style)
	{
		if(link!=null && !link.startsWith("http://"))
			link = "http://"+link;
		HTML label = new HTML("<a href=\""+link+"\" target=\"_blank\">"+text+"</a>");
			label.setStyleName(style);
			label.setTitle(title);
			return label;
	}
	public static Widget makeConceptLabel(ConceptObject cObj, String style, final boolean isAddAction, final int tab, final int objectType)
	{

		HashMap<String, TermObject> tObjList = new HashMap<String, TermObject>();
		String schemeURI = "";
		int belongsToModule = 0;
		if(cObj!=null)
		{
			tObjList = (HashMap<String, TermObject>) cObj.getTerm();
			schemeURI = cObj.getScheme();
			belongsToModule = cObj.getBelongsToModule();
		}
		
		HorizontalFlowPanel panel = new HorizontalFlowPanel();
		if(tObjList!=null)
		{
			Iterator<String> itr = tObjList.keySet().iterator();
			while(itr.hasNext())
			{
				String key = (String)itr.next();
				TermObject tObj = (TermObject) tObjList.get(key);
				String language = tObj.getLang();
				if(MainApp.userSelectedLanguage.contains(language))
				{
					String newconceptLabel = "";
					if(panel.getWidgetCount()==0)
					{
						panel.add(getImagePanel(objectType, tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
						panel.add(new HTML("&nbsp;&nbsp;"));
					}
					else
					{
						newconceptLabel += ", ";
					}
					newconceptLabel += tObj.getLabel()+" ("+tObj.getLang()+")";
					panel.add(getLabelPanel(objectType, newconceptLabel ,tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
				}
			}
			if(panel.getWidgetCount()<1)
			{
				panel.add(getImagePanel(objectType, "", null, schemeURI, style, isAddAction, tab, belongsToModule));
				panel.add(new HTML("&nbsp;&nbsp;"));				
				panel.add(getLabelPanel(objectType, "" ,"Not avaliable in selected language. ", null, schemeURI, style, isAddAction, tab, belongsToModule));
			}
		}
		else
			panel.add(new HTML("&nbsp;"));
		return panel;
	}
	public static HorizontalFlowPanel makeTermLabel(ConceptObject cObj, TermObject tObjList, String schemeURI, String style, final boolean isAddAction, final int tab, final int objectType)
	{
		int belongsToModule = 0;
		if(cObj!=null)
		{
			belongsToModule = cObj.getBelongsToModule();
		}
		
		HorizontalFlowPanel panel = new HorizontalFlowPanel();
		if(tObjList!=null)
		{
			TermObject tObj = tObjList;
			String language = tObj.getLang();
			String newconceptLabel = "";
			if(MainApp.userSelectedLanguage.contains(language))
			{
				newconceptLabel = tObj.getLabel()+" ("+tObj.getLang()+")";
			}
			panel.add(getImagePanel(objectType, tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
			panel.add(new HTML("&nbsp;&nbsp"));
			panel.add(getLabelPanel(objectType, newconceptLabel ,tObj.getConceptUri(), tObj.getConceptUri(), schemeURI, style, isAddAction, tab, belongsToModule));
		}
		else
			panel.add(new HTML("&nbsp;"));
		return panel;
	}
	public static HorizontalFlowPanel makeLabel(String text, String title, String link , String schemeURI, String style, final boolean isAddAction, final int tab, final int objectType, int belongsToModule)
	{
		HorizontalFlowPanel panel = new HorizontalFlowPanel();
		panel.add(getImagePanel(objectType, title, link, schemeURI, style, isAddAction, tab, belongsToModule));
		panel.add(new HTML("&nbsp;&nbsp"));
		panel.add(getLabelPanel(objectType, text ,title, link, schemeURI, style, isAddAction, tab, belongsToModule));
		return panel;
	}
	public static HorizontalFlowPanel makeLabelOnly(String text, String title, String link , String schemeURI, String style, final boolean isAddAction, final int tab, final int objectType, boolean isSource, int belongsToModule)
	{
		HorizontalFlowPanel panel = new HorizontalFlowPanel();
		if(isSource)
			panel.add(getExtLinkLabelPanel(text ,title, link, style));
		else
			panel.add(getLabelPanel(objectType, text ,title, link, schemeURI, style, isAddAction, tab, belongsToModule));
		return panel;
	}
	public static String makeRelationshipLabel(RelationshipObject rObj)
	{
		ArrayList<LabelObject> labelList = rObj.getLabelList();
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = labelObj.getLanguage();
			
			if(MainApp.userSelectedLanguage.contains(lang))
			{
				String label = labelObj.getLabel();
				if(labelStr.equals(""))
					labelStr += " "+label+" ("+lang+")";
				else
					labelStr += ", "+label+" ("+lang+")";
			}
		}
		if(labelStr.equals("") && rObj.getName()!=null)
				labelStr = rObj.getName();
		if(labelStr.equals("") && rObj.getUri()!=null)
			labelStr = rObj.getUri();
		return labelStr;
	}
	public static Widget makeLabelWithRelation(Validation v, String style, final boolean isAddAction, final int tab, final int objectType, boolean isOld, boolean isSource)
	{
		int belongsToModule = 0;
		ConceptObject conceptObj = v.getConceptObject();
		if(conceptObj!=null)
		{
			belongsToModule = conceptObj.getBelongsToModule();
		}
		HorizontalFlowPanel hp = new HorizontalFlowPanel();
		ArrayList<LightEntity> list = new ArrayList<LightEntity>();
		if(isOld)
		{
			if(v.getOldRelationshipObject()!=null)
				hp.add(makeLabel(makeRelationshipLabel(v.getOldRelationshipObject()), v.getOldRelationshipObject().getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, 3, belongsToModule));
			if(v.getOldObject()!=null)
			{
				list = (ArrayList<LightEntity>) v.getOldObject();
				
			}
		}
		else
		{
			if(v.getNewRelationshipObject()!=null)
				hp.add(makeLabel(makeRelationshipLabel(v.getNewRelationshipObject()), v.getNewRelationshipObject().getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, 3, belongsToModule));
			if(v.getNewObject()!=null)
			{
				list = (ArrayList<LightEntity>) v.getNewObject();
			}
		}
		if(list.size()>0)
		{
			Object obj = list.get(0);
			if(obj instanceof ConceptObject)
			{
				ConceptObject cObj = (ConceptObject) obj;
				hp.add(makeConceptLabel(cObj, style, isAddAction, tab, objectType));
			}
			else if(obj instanceof TermObject)
			{
				TermObject tObj = (TermObject) obj;
				hp.add(makeLabel(tObj.getLabel()+checkNullValueInParenthesis(tObj.getLang()), tObj.getUri(), tObj.getConceptUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType,  belongsToModule));
			}
			else if(obj instanceof SchemeObject)
			{
				SchemeObject sObj = (SchemeObject) obj;
				hp.add(makeLabel(sObj.getSchemeLabel()+checkNullValueInParenthesis("en"), sObj.getDescription(), sObj.getSchemeInstance(), sObj.getSchemeInstance(), style, isAddAction, tab, objectType,  belongsToModule));
			}
			else if(obj instanceof AttributesObject)
			{
				AttributesObject aObj = (AttributesObject) obj;
				RelationshipObject rObj = aObj.getRelationshipObject();
				NonFuncObject nfObj = aObj.getValue();
				String label = "";
				if(rObj!=null)
					label += Convert.getRelationshipLabel(rObj, MainApp.constants.mainLocale())+" : "; 
				if(nfObj!=null)
					label += nfObj.getValue() + checkNullValueInParenthesis(nfObj.getLanguage());
				hp.add(makeLabelOnly(label, v.getConceptObject().getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
			}
			else if(obj instanceof IDObject)
			{
				IDObject idObj = (IDObject) obj;
				if(idObj.getIDType()==IDObject.DEFINITION)
				{
					if(isSource)
						hp.add(makeLabelOnly(idObj.getIDSource(), idObj.getIDSourceURL(), idObj.getIDSourceURL(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
					else
					{
						ArrayList<TranslationObject> trObjects = idObj.getIDTranslationList();
						String label = "";
						for(int i=0;i<trObjects.size();i++)
						{
							if(!label.equals("")) label += ", ";
							label += ((TranslationObject)trObjects.get(i)).getLabel()+checkNullValueInParenthesis(((TranslationObject)trObjects.get(i)).getLang());
						}
						
						hp.add(makeLabelOnly(label, idObj.getIDUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
					}
				}
				else if(idObj.getIDType()==IDObject.IMAGE)
				{
					if(isSource)
						hp.add(makeLabelOnly(idObj.getIDSource(), idObj.getIDSourceURL(), idObj.getIDSourceURL(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
					else
					{
						ArrayList<TranslationObject> trObjects = idObj.getIDTranslationList();
						String label = "";
						for(int i=0;i<trObjects.size();i++)
						{
							if(!label.equals("")) label += ", ";
							label += ((TranslationObject)trObjects.get(i)).getLabel()+checkNullValueInParenthesis(((TranslationObject)trObjects.get(i)).getLang());
						}
						hp.add(makeLabelOnly(label, idObj.getIDUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
					}
				}
			}
			else if(obj instanceof TranslationObject)
			{
				TranslationObject trObj = (TranslationObject) obj;
				if(trObj.getType()==TranslationObject.DEFINITIONTRANSLATION)
				{
					hp.add(makeLabelOnly(trObj.getLabel()+checkNullValueInParenthesis(trObj.getLang()), trObj.getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));	
				}
				else if(trObj.getType()==TranslationObject.IMAGETRANSLATION)
				{
					hp.add(makeLabelOnly(trObj.getLabel()+checkNullValueInParenthesis(trObj.getLang()), trObj.getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));
				}
			}
			else if(obj instanceof LinkingConceptObject)
			{
				LinkingConceptObject cObj = (LinkingConceptObject) obj;
				String text = cObj.getParentURI();
				if(text==null || text.equals(""))
					text = "Root concept";
				hp.add(makeLabelOnly(text, cObj.getUri(), v.getConceptObject().getUri(), v.getConceptObject().getScheme(), style, isAddAction, tab, objectType, isSource,  belongsToModule));	
			}
		}
		if(hp.getWidgetCount()<1)
			hp.add(new HTML("&nbsp;"));
		return hp;
	}
	public static Widget getLabelPanel(int col, Validation v, String style)
	{

		switch (v.getAction()) {

		 case 1:     //     "concept-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 2:     //     "concept-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, false, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, false, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, false, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 3:     //     "concept-relationship-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1, true, false);
			 break;
	
		 case 4:     //     "concept-relationship-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1, true, false);
			 break;
	
		 case 5:     //     "concept-relationship-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.RELATIONSHIP.getTabIndex(), 1, true, false);
			 break;
	
		 case 6:     //     "term-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, true, false);	
			 break;
	
		 case 7:     //     "term-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, true, false);
			 break;
	
		 case 8:     //     "term-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, true, false);
			 break;
	
		 case 9:     //     "term-relationship-add"
			 if(col==0) return makeTermLabel(v.getConceptObject(), v.getTermObject(), v.getConceptObject().getScheme(),style, true, ConceptTab.TERM.getTabIndex(), 2);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, true, false);
			 break;
	
		 case 10:     //     "term-relationship-edit"
			 if(col==0) return makeTermLabel(v.getConceptObject(), v.getTermObject(), v.getConceptObject().getScheme(),style, true, ConceptTab.TERM.getTabIndex(), 2);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, true, false);
			 break;
	
		 case 11:     //     "term-relationship-delete"
			 if(col==0) return makeTermLabel(v.getConceptObject(), v.getTermObject(), v.getConceptObject().getScheme(),style, true, ConceptTab.TERM.getTabIndex(), 2);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 2, true, false);
			 break;
	
		 case 12:     //     "term-note-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 13:     //     "term-note-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 14:     //     "term-note-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 15:     //     "term-attribute-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 16:     //     "term-attribute-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 17:     //     "term-attribute-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.TERM.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.TERM.getTabIndex(), 1, true, false);
			 break;
	
		 case 18:     //     "concept-edit-note-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
	
		 case 19:     //     "concept-edit-note-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
	
		 case 20:     //     "concept-edit-note-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
	
		 case 21:     //     "concept-edit-definition-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, false);
			 break;
	
		 case 22:     //     "concept-edit-definition-translation-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, false);
			 break;
	
		 case 23:     //     "concept-edit-definition-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, false);
			 break;
		 
		 case 24:     //     "concept-edit-image-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, false);
			 break;
		 
		 case 25:     //     "concept-edit-image-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, false);
			 break;
		 
		 case 26:     //     "concept-edit-image-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, false);
			 break;
			 
		 case 27:     //     "concept-edit-image-translation-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, false);
			 break;
			 
		 case 28:     //     "concept-edit-image-translation-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, false);
			 break;
			 
		 case 29:     //     "concept-edit-definition-translation-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, false);
			 break;
			 
		 case 30:     //     "concept-edit-definition-translation-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, false);
			 break;
			 
		 case 31:     //     "concept-edit-ext-source-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, true);
			 break;
			 
		 case 32:     //     "concept-edit-ext-source-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, true);
			 break;
			 
		 case 33:     //     "concept-edit-ext-source-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.DEFINITION.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.DEFINITION.getTabIndex(), 1, true, true);
			 break;
			 
		 case 34:     //     "concept-edit-image-source-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, true);
			 break;
			 
		 case 35:     //     "concept-edit-image-source-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, true);
			 break;
			 
		 case 36:     //     "concept-edit-image-source-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.IMAGE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, false, true);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.IMAGE.getTabIndex(), 1, true, true);
			 break;
			 
		 case 37:     //     "concept-edit-attribute-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
	
		 case 38:     //     "concept-edit-attribute-edit"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
	
		 case 39:     //     "concept-edit-attribute-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
		 case 40:     //     "scheme-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 4);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 4, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 4, true, false);
			 break;
		 case 41:     //     ""mapping-create"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
		 case 42:     //     "mapping-delete"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
		 case 76:     //     "move-concept"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
			 
		 case 77:     //     "link-concept"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
			 
		 case 78:     //     "unlink-concept"
			 if(col==0) return makeConceptLabel(v.getConceptObject(), style, true, ConceptTab.NOTE.getTabIndex(), 1);
			 if(col==1) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, false, false);
			 if(col==2) return makeLabelWithRelation(v, style, true, ConceptTab.NOTE.getTabIndex(), 1, true, false);
			 break;
		 default: 
			 GWT.log("Invalid Action.", null);	
		 	break;
	    }
		return new HTML("");
	}

}
