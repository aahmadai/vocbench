package org.fao.aoscs.server.rss;

import java.util.ArrayList;

import org.fao.aoscs.client.module.constant.RecentChangesConstants;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.LabelObject;
import org.fao.aoscs.domain.PermissionGroupMapId;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersGroups;

public class LabelRSS {
	
	public static final int ITEMLABEL = 0;
	public static final int ITEMCHANGE = 1;
	public static final int ITEMOLD = 2;
	
	public static String makeLabel(RecentChangeData rc, int returnType)
	{
		Users u = null;
		RelationshipObject r = null;
		UsersGroups g  = null;		
		PermissionGroupMapId pgm = null;
		switch(rc.getActionId())
		{
			case 43: // user-add				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					u = (Users)(rc.getObject().get(0));
					return makeLabel(u.getFirstName()+" "+u.getLastName()+" ("+u.getUsername()+")" , RecentChangesConstants.USER_TYPE);
				}
				if(returnType == ITEMCHANGE){					
					return "";
				}				
				if(returnType == ITEMOLD){					
					return "";
				}				
				break;
			case 44: // user-edit				
				if(returnType == ITEMLABEL)
					u = (Users)(rc.getObject().get(0));
				else if(returnType == ITEMCHANGE)	
					u = (Users)(rc.getObject().get(0));					
				else if(returnType == ITEMOLD)	
					u = (Users)(rc.getOldObject().get(0));					
				return makeLabel(u.getFirstName()+" "+u.getLastName()+" ("+u.getUsername()+")" , RecentChangesConstants.USER_TYPE);
				
			case 46: //relationship-create				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return "";
				}
				else if(returnType == ITEMOLD)
					return "";										
								
			case 47: //relationship-delete
				if(returnType == ITEMLABEL)
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);								
				else if(returnType == ITEMCHANGE)
					return "";										
				else if(returnType == ITEMOLD)
					return "";										
				
			
			case 48: //relationship-edit-label-create				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD){
					return "";
				}
			case 49: //relationship-edit-label-edit				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change:" + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.RELATIONSHIP_TYPE);
				}	
			case 50: //relationship-edit-label-delete				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return "";
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.RELATIONSHIP_TYPE);
				}	
			case 51: //relationship-edit-definition-create				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return "";
				}
			case 52: //relationship-edit-definition-edit				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}	
			case 53: //relationship-edit-definition-delete				
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return "";
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}	
			case 54: //relationship-edit-property-create
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return "";
				}	
			case 55: //relationship-edit-property-delete
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return "";
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 56: //relationship-edit-inverse-property-create
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return "";
				}	
			case 57: //relationship-edit-inverse-property-edit
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return "";					
				}	
			case 58: //relationship-edit-inverse-property-delete
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return "";										
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);										
				}
			case 59: //relationship-edit-domain-create
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return "";					
				}
			case 60: //relationship-edit-domain-delete
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return "";					
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 61: //relationship-edit-range-create
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE  && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return "";					
				}	
			case 62: //relationship-edit-range-edit
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 63: //relationship-edit-range-delete
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return "";
				}
				else if(returnType == ITEMOLD && rc.getOldObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return "<br><br> Old Value: " + makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 64: //relationship-edit-range-value-add
				if(returnType == ITEMLABEL && rc.getObject().size()>0){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(makeRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE && rc.getNewObject().size()>0){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return "<br><br> Change: " + makeLabel( to.getLabel() + ": " + to.getDescription() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return "";
										
				}	
			case 65: //group-create
				if(rc.getObject().size()>0)
				{
					g  = (UsersGroups)(rc.getObject().get(0));
					if(returnType == ITEMLABEL){										
						return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "<br><br> Change: " + makeLabel( "Description: " + g.getUsersGroupsDesc() , RecentChangesConstants.NO_TYPE);
					}
					else if(returnType == ITEMOLD){
						return "";					
					}
				}
				else
					return "";
			case 66: //group-edit
				if(rc.getObject().size()>0)
				{
					g  = (UsersGroups)(rc.getObject().get(0));
					if(returnType == ITEMLABEL){										
						return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "<br><br> Change: " + makeLabel( g.getUsersGroupsDesc() , RecentChangesConstants.NO_TYPE);
					}
					else if(returnType == ITEMOLD){
						g  = (UsersGroups)(rc.getOldObject().get(0));
						return "<br><br> Old Value: " + makeLabel( g.getUsersGroupsDesc() , RecentChangesConstants.NO_TYPE);									
					}	
				}
				else
					return "";
			case 67: //group-delete
				if(rc.getObject().size()>0)
				{
					g  = (UsersGroups)(rc.getObject().get(0));
					if(returnType == ITEMLABEL){										
						return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "";
					}
					else if(returnType == ITEMOLD){
						return "";									
					}
				}
				else
					return "";
			case 68: //group-permission-add
				if(rc.getObject().size()>0)
				{
					pgm  = (PermissionGroupMapId)(rc.getObject().get(0));
					if(returnType == ITEMLABEL){										
						return makeLabel( pgm.getGroupName() , RecentChangesConstants.GROUP_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "<br><br> Change: " + makeLabel( pgm.getPermitName() , RecentChangesConstants.NO_TYPE);
					}
					else if(returnType == ITEMOLD){
						return "";									
					}
				}
				else
					return "";
			case 69: //group-permission-delete
				if(rc.getObject().size()>0)
				{
					pgm  = (PermissionGroupMapId)(rc.getObject().get(0));
					if(returnType == ITEMLABEL){										
						return makeLabel( pgm.getGroupName() , RecentChangesConstants.GROUP_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "";
					}
					else if(returnType == ITEMOLD){
						return "<br><br> Old Value: " + makeLabel( pgm.getPermitName() , RecentChangesConstants.NO_TYPE);									
					}	
				}
				else
					return "";
			case 70: //group-member-add
				if(rc.getObject().size()>0)
				{
					g  = (UsersGroups)(rc.getObject().get(0));
					u = (Users)(rc.getNewObject().get(0));
					if(returnType == ITEMLABEL){										
						return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "<br><br> Change: " + makeLabel( u.getUsername() , RecentChangesConstants.NO_TYPE);
					}
					else if(returnType == ITEMOLD){
						return "";									
					}	
				}
				else
					return "";
			case 71: //group-member-delete
				if(rc.getObject().size()>0)
				{
					g  = (UsersGroups)(rc.getObject().get(0));
					u = (Users)(rc.getOldObject().get(0));
					if(returnType == ITEMLABEL){										
						return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "";									
					}
					else if(returnType == ITEMOLD){
						return "<br><br> Old Value: " + makeLabel( u.getUsername() , RecentChangesConstants.NO_TYPE);
					}
				}
				else
					return "";
			case 74: //export
				if(rc.getObject().size()>0)
				{
					ExportParameterObject export  = (ExportParameterObject)(rc.getObject().get(0));
					if(returnType == ITEMLABEL){					
						return makeLabel( export.getExportFormat() , RecentChangesConstants.EXPORT_TYPE );
					}
					else if(returnType == ITEMCHANGE){
						return "";									
					}
					else if(returnType == ITEMOLD){
						return "";
					}
				}
				else
					return "";
		}
		return "";
	}
	
	private static String makeLabel(String label, int objType)
	{
		String imgURL = "<img src='images/spacer.gif' border='0'>";
		switch(objType){
		case RecentChangesConstants.NO_TYPE:
			imgURL = "<img src='images/spacer.gif' border='0'>";
			break;
		case RecentChangesConstants.USER_TYPE:
			imgURL = "<img src='images/New-users.gif' border='0'>";
			break;
		case RecentChangesConstants.RELATIONSHIP_TYPE:
			imgURL = "<img src='images/relationship-object-logo.gif' border='0'>";
			break;
		case RecentChangesConstants.GROUP_TYPE:
			imgURL = "<img src='images/usericon.gif' border='0'>";
			break;	
		case RecentChangesConstants.EXPORT_TYPE:
			imgURL = "<img src='images/export_small.gif' border='0'>";
			break;	
		}								
		return imgURL + " " + label;			
	}
	
	private static String makeRelationshipLabel(RelationshipObject rObj)
	{
		ArrayList<LabelObject> labelList = rObj.getLabelList();
		String labelStr = "";
		for(int i=0;i<labelList.size();i++)
		{
			LabelObject labelObj = (LabelObject) labelList.get(i);
			String lang = labelObj.getLanguage();
			String label = labelObj.getLabel();
			if(labelStr.equals(""))
				labelStr += " "+label+" ("+lang+")";
			else
				labelStr += ", "+label+" ("+lang+")";
		}
		return labelStr;
	}
}
