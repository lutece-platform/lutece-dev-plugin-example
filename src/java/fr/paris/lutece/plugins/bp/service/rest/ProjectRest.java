package fr.paris.lutece.plugins.bp.service.rest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import fr.paris.lutece.plugins.bp.business.Project;
import fr.paris.lutece.plugins.bp.business.ProjectHome;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.portal.service.message.SiteMessageException;

import net.sf.json.JSONObject;


@Path("rest")
public class ProjectRest {

	@GET
	@Path("/json/project")	
	@Produces(MediaType.APPLICATION_JSON)
	public String getProjects() {
		
		Collection<Project> ListeProject =  ProjectHome.getProjectsList();
		 
		
		 JSONObject ListejsonProject = new JSONObject(  );
		 
		
		for (Project project : ListeProject)
		{
		
			 JSONObject json = new JSONObject(  );
			 json.accumulate( "id", project.getId());
			 json.accumulate( "name", project.getName());
			 json.accumulate( "description", project.getDescription());
			 json.accumulate( "cout", project.getCout());
			 json.accumulate( "ImageUrl", project.getImageUrl());
			 
			 
			 ListejsonProject.accumulate("project", json);		
			 
		}
		
		
			//jsonListeProject.accumulate(Integer.toString(cpt), this.buildJSON(projet));
			

		return ListejsonProject.toString(2);
		
	}
	


	
	private String buildJSON(Project project){
		String strJSON = "";
		
		 JSONObject json = new JSONObject(  );
		 json.accumulate( "id", project.getId());
		 json.accumulate( "name", project.getName());
		 json.accumulate( "description", project.getDescription());
		 json.accumulate( "cout", project.getCout());
		 
		 JSONObject jsonProject = new JSONObject(  );
		 jsonProject.accumulate( "project", json );
		 strJSON = jsonProject.toString(2);
		 
		 return strJSON;
	}

	@GET
	@Path("/json/project/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProject(@PathParam("id") int nId) throws SiteMessageException{

		String strJSON = "";
		try
	 {
		Project project = ProjectHome.findByPrimaryKey(nId);
		strJSON = this.buildJSON(project);
	 }
			        catch ( NumberFormatException e )
		        {
		            strJSON = JSONUtil.formatError( "Invalid project number", 3 );
		        }
			        catch ( Exception e )
		        {
		           strJSON = JSONUtil.formatError( "project not found", 1 );
	       }
		
		return strJSON;

	}

//	@PUT
//	@Path("/json/project/add")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes( MediaType.APPLICATION_JSON )
//	public Project addProject(Project project) {
//		ProjectHome.create(project);
//		
//		return project;
//	}
}
