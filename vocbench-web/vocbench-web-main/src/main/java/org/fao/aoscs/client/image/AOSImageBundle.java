package org.fao.aoscs.client.image;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AOSImageBundle extends ClientBundle {
	public static final AOSImageBundle INSTANCE =  GWT.create(AOSImageBundle.class);

	@Source("org/fao/aoscs/client/image/icons/label_not_found.gif")
	public ImageResource labelNotFound();
	  
	@Source("org/fao/aoscs/client/image/icons/concept_logo.gif")
	public ImageResource conceptIcon();

	@Source("org/fao/aoscs/client/image/icons/category_logo.gif")
	public ImageResource categoryIcon();

	@Source("org/fao/aoscs/client/image/icons/relationship-object-logo.gif")
	public ImageResource relationshipObjectIcon();
	
	@Source("org/fao/aoscs/client/image/icons/relationship-datatype-logo.gif")
	public ImageResource relationshipDatatypeIcon();
	
	@Source("org/fao/aoscs/client/image/icons/relationship-annotation-logo.gif")
	public ImageResource relationshipAnnotationIcon();
	
	@Source("org/fao/aoscs/client/image/icons/relationship-ontology-logo.gif")
	public ImageResource relationshipOntologyIcon();
	
	@Source("org/fao/aoscs/client/image/icons/add-grey.gif")
	public ImageResource addIcon();
	
	@Source("org/fao/aoscs/client/image/icons/edit-grey.gif")
	public ImageResource editIcon();
	
	@Source("org/fao/aoscs/client/image/icons/delete-grey.gif")
	public ImageResource deleteIcon();
	
	@Source("org/fao/aoscs/client/image/icons/download-grey.png")
	public ImageResource downloadIcon();
	
	@Source("org/fao/aoscs/client/image/icons/wiki.gif")
	public ImageResource wikiIcon();
	
	@Source("org/fao/aoscs/client/image/icons/local.png")
	public ImageResource localIcon();
	
	@Source("org/fao/aoscs/client/image/icons/web.png")
	public ImageResource webIcon();
	
}

