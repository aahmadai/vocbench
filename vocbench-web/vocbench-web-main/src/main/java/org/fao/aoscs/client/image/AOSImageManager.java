/**
 * 
 */
package org.fao.aoscs.client.image;

import org.fao.aoscs.client.MainApp;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author rajbhandari
 *
 */
public class AOSImageManager {

	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static ImageResource getConceptImageResource(String conceptURI)
	{
		//if(conceptURI.startsWith(ModelConstants.ONTOLOGYBASENAMESPACE))
		{
			return MainApp.aosImageBundle.conceptIcon();
		}
		//else
		//{
		//	MainApp.aosImageBundle.categoryIcon();
		//}
	}
	
	/**
	 * @param conceptURI
	 * @return
	 */
	public static String getConceptImageURL(String conceptURI)
	{
		String imgURL = "images/concept_logo.gif";
		//if(conceptURI.startsWith(ModelConstants.ONTOLOGYBASENAMESPACE))
		{
			imgURL = "images/concept_logo.gif";
		}
		/*else
		{
			imgURL = "images/category_logo.gif";
		}*/
		return imgURL;
	}
}
