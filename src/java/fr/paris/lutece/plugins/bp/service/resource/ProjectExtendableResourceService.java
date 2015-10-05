package fr.paris.lutece.plugins.bp.service.resource;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.bp.business.Project;
import fr.paris.lutece.plugins.bp.business.ProjectHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;

public class ProjectExtendableResourceService  implements IExtendableResourceService{

	
	private final static String MESSAGE_PROJECT_RESOURCE_TYPE_DESCRIPTION ="bp.message.ressource.type.description";
	@Override
	public IExtendableResource getResource(String strIdResource, String strResourceType) {
		// TODO Auto-generated method stub
		if ( StringUtils.isNotBlank( strIdResource ) && StringUtils.isNumeric( strIdResource ) )
		{
			int nIdProject = Integer.parseInt( strIdResource );
			return ProjectHome.findByPrimaryKey(nIdProject);
		}
		
		return null;
	}

	@Override
	public String getResourceType() {
		// TODO Auto-generated method stub
		return Project.getPropertyRessourceType();
	}

	@Override
	public String getResourceTypeDescription(Locale locale) {
		// TODO Auto-generated method stub
		
		return I18nService.getLocalizedString( MESSAGE_PROJECT_RESOURCE_TYPE_DESCRIPTION, locale );
		
	
	}



	@Override
	public boolean isInvoked(String strResourceType) {
		// TODO Auto-generated method stub
		
		return Project.getPropertyRessourceType().equals(strResourceType );
	}

	@Override
	public String getResourceUrl(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
