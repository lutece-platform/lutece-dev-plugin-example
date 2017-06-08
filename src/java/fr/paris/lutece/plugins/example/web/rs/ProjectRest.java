/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.example.web.rs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.example.business.ExtendedProject;
import fr.paris.lutece.plugins.example.business.Project;
import fr.paris.lutece.plugins.example.business.ProjectHome;
import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.plugins.extend.modules.hit.service.HitService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.spring.SpringContextService;

@Component
@Path( RestConstants.BASE_PATH + "example" )
public class ProjectRest
{
	private ObjectMapper _mapper = new ObjectMapper();
	static final Logger LOGGER = Logger.getLogger( RestConstants.REST_LOGGER );

   /**
     * get project list
     *
     * @return the json file of the project list
     * @throws JsonProcessingException
     */
    @GET
    @Path( "/projects" )
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectList( ) throws JsonProcessingException
    {
        Collection<Project> listProject = ProjectHome.getProjectsList( );

        return _mapper.writeValueAsString( listProject );
    }

    /**
     * get a project
     *
     * @param nId
     * @return the json file of the project l
     * @throws SiteMessageException
     */
    @GET
    @Path( "/projects/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public String getProjectById( @PathParam( "id" ) int nId ) throws SiteMessageException
    {
    	try
        {
            Project project = ProjectHome.findByPrimaryKey( nId );
            return _mapper.writeValueAsString( project );
        }
        catch( Exception e )
        {
            return JSONUtil.formatError( "project not found", 1 );
        }
    }

    /**
     * get project stats
     * 
     * @param nId
     * @return the json file of the project stats
     * @throws SiteMessageException 
     */
    @GET
    @Path("/projects_stats/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProjectStatsById(@PathParam("id") int nId) throws SiteMessageException {


        ObjectMapper mapper = new ObjectMapper();
        try {
                    
            return mapper.writeValueAsString( getExtendedProject( ProjectHome.findByPrimaryKey(nId) ) );
            
        } catch (NumberFormatException e) {
            return JSONUtil.formatError("Invalid project number", 3);
        } catch (Exception e) {
            return JSONUtil.formatError("project not found", 1);
        }
    }

    /**
     * get project stats 
     * 
     * @param project
     * @return the extended project
     */
    private ExtendedProject getExtendedProject(Project project) {
        
        // get the HitService in the spring context
        HitService hs = SpringContextService.getBean(HitService.BEAN_SERVICE);

        String strIdExtendableResource = String.valueOf(project.getId()); // extend resource ID
        String strExtendableResourceType = Project.PROPERTY_RESOURCE_TYPE; // extend resource type

        // search nb of hits
        Hit hit = hs.findByParameters(strIdExtendableResource, strExtendableResourceType);
        if (hit == null) {
            hit = new Hit();
        }
        
        ExtendedProject extProject = (ExtendedProject)project;
        extProject.setHit(hit);
        
        return extProject;
    }

 

}
