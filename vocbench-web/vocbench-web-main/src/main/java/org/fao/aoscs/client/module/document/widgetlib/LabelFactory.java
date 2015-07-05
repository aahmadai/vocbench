package org.fao.aoscs.client.module.document.widgetlib;

import java.util.ArrayList;

import net.sf.gilead.pojo.gwt.LightEntity;

import org.fao.aoscs.client.image.AOSImageManager;
import org.fao.aoscs.client.module.constant.RecentChangesConstants;
import org.fao.aoscs.client.module.constant.Style;
import org.fao.aoscs.client.utility.Convert;
import org.fao.aoscs.client.widgetlib.shared.panel.HorizontalFlowPanel;
import org.fao.aoscs.domain.ExportParameterObject;
import org.fao.aoscs.domain.PermissionGroupMapId;
import org.fao.aoscs.domain.RecentChangeData;
import org.fao.aoscs.domain.RelationshipObject;
import org.fao.aoscs.domain.TranslationObject;
import org.fao.aoscs.domain.Users;
import org.fao.aoscs.domain.UsersGroups;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class LabelFactory {
	
	static final int ITEMLABEL = 0;
	static final int ITEMCHANGE = 1;
	static final int ITEMOLD = 2;
	
	public static Widget makeLabel(RecentChangeData rc, int returnType)
	{
		Users u = null;
		RelationshipObject r = null;
		UsersGroups g  = null;		
		PermissionGroupMapId pgm = null;		
		switch(rc.getActionId())
		{
			case 43: // user-add				
				if(returnType == ITEMLABEL){
					u = (Users)(rc.getObject().get(0));
					return makeLabel(u.getFirstName()+" "+u.getLastName()+" ("+u.getUsername()+")" , RecentChangesConstants.USER_TYPE);
				}
				if(returnType == ITEMCHANGE){					
					return new HTML("&nbsp;");
				}				
				if(returnType == ITEMOLD){					
					return new HTML("&nbsp;");
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
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD)
					return new HTML("&nbsp;");										
				
				
			case 47: //relationship-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);	
				}
				else if(returnType == ITEMCHANGE)
					return new HTML("&nbsp;");										
				else if(returnType == ITEMOLD){
					r = (RelationshipObject)(rc.getOldObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);	
				}
				
			
			case 48: //relationship-edit-label-create				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = new TranslationObject();
					ArrayList<LightEntity> list = rc.getNewObject();
					if(list!=null)
					{
						if(list.size()>0)
							to = (TranslationObject)(list.get(0));
					}
					if(to.getLabel()==null && to.getLang()==null)
						return new HTML("&nbsp;");
					else
						return makeLabel( (to.getLabel()==null?"":to.getLabel()) + (to.getLang()==null?"":" (" + to.getLabel()+ ")")  , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}
			case 49: //relationship-edit-label-edit				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.RELATIONSHIP_TYPE);
				}	
			case 50: //relationship-edit-label-delete				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.RELATIONSHIP_TYPE);
				}	
			case 51: //relationship-edit-definition-create				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}
			case 52: //relationship-edit-definition-edit				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}	
			case 53: //relationship-edit-definition-delete				
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() + " (" + to.getLang() + ")" , RecentChangesConstants.NO_TYPE);
				}	
			case 54: //relationship-edit-property-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
			case 55: //relationship-edit-property-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 56: //relationship-edit-inverse-property-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
			case 57: //relationship-edit-inverse-property-edit
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}	
			case 58: //relationship-edit-inverse-property-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");										
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}
			case 59: //relationship-edit-domain-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}
			case 60: //relationship-edit-domain-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");					
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 61: //relationship-edit-range-create
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");					
				}	
			case 62: //relationship-edit-range-edit
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 63: //relationship-edit-range-delete
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					TranslationObject to = (TranslationObject)(rc.getOldObject().get(0));
					return makeLabel( to.getLabel() , RecentChangesConstants.NO_TYPE);					
				}	
			case 64: //relationship-edit-range-value-add
				if(returnType == ITEMLABEL){
					r = (RelationshipObject)(rc.getObject().get(0));
					return makeLabel(Convert.getRelationshipLabel(r) , RecentChangesConstants.RELATIONSHIP_TYPE);
				}
				else if(returnType == ITEMCHANGE){
					TranslationObject to = (TranslationObject)(rc.getNewObject().get(0));
					return makeLabel( to.getLabel() + ": " + to.getDescription() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
										
				}
			case 65: //group-create
				g  = (UsersGroups)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( "Description: " + g.getUsersGroupsDesc() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
			case 66: //group-edit
				g  = (UsersGroups)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( "Description: " + g.getUsersGroupsDesc() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					g  = (UsersGroups)(rc.getOldObject().get(0));
					return makeLabel( "Description: " + g.getUsersGroupsDesc() , RecentChangesConstants.NO_TYPE);									
				}	
			case 67: //group-delete
				g  = (UsersGroups)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");								
				}
				
			case 68: //group-permission-add
				pgm  = (PermissionGroupMapId)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( pgm.getGroupName() , RecentChangesConstants.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( pgm.getPermitName() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");									
				}
				
			case 69: //group-permission-delete
				pgm  = (PermissionGroupMapId)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( pgm.getGroupName() , RecentChangesConstants.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");
				}
				else if(returnType == ITEMOLD){
					return makeLabel( pgm.getPermitName() , RecentChangesConstants.NO_TYPE);									
				}	
			case 70: //group-member-add
				g  = (UsersGroups)(rc.getObject().get(0));
				u = (Users)(rc.getNewObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return makeLabel( u.getUsername() , RecentChangesConstants.NO_TYPE);
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");									
				}	
				
			case 71: //group-member-delete
				g  = (UsersGroups)(rc.getObject().get(0));
				u = (Users)(rc.getOldObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( g.getUsersGroupsName() , RecentChangesConstants.GROUP_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");									
				}
				else if(returnType == ITEMOLD){
					return makeLabel( u.getUsername() , RecentChangesConstants.NO_TYPE);
				}
				
			case 74: //export
				ExportParameterObject export  = (ExportParameterObject)(rc.getObject().get(0));
				if(returnType == ITEMLABEL){										
					return makeLabel( export.getExportFormat() , RecentChangesConstants.EXPORT_TYPE );
				}
				else if(returnType == ITEMCHANGE){
					return new HTML("&nbsp;");									
				}
				else if(returnType == ITEMOLD){
					return new HTML("&nbsp;");
				}	
		}
		return new HTML("&nbsp;");
	}
	
	private static Widget makeLabel(String label, int objType){
		String imgURL = "images/spacer.gif";
		switch(objType){
		case RecentChangesConstants.NO_TYPE:
			imgURL = "images/spacer.gif";
			break;
		case RecentChangesConstants.USER_TYPE:
			imgURL = "images/New-users.gif";
			break;
		case RecentChangesConstants.RELATIONSHIP_TYPE:
			imgURL = AOSImageManager.getPropObjectImageURL();
			break;
		case RecentChangesConstants.GROUP_TYPE:
			imgURL = "images/usericon.gif";
			break;
		case RecentChangesConstants.EXPORT_TYPE:
			imgURL = "images/export_small.gif";
			break;	
		}
						
		HTML txt = new HTML(" "+label);
		txt.addStyleName(Style.label_line_height_20);
		HorizontalFlowPanel panel = new HorizontalFlowPanel();
		panel.add(new Image(imgURL));
		panel.add(new HTML("&nbsp;&nbsp;"));
		panel.add(txt);
		return panel;			
	}
}
