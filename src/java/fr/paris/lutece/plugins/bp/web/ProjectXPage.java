/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
package fr.paris.lutece.plugins.bp.web;

import fr.paris.lutece.plugins.bp.business.Project;
import fr.paris.lutece.plugins.bp.business.ProjectHome;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.resource.ExtendableResourcePluginActionManager;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.url.UrlItem;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import org.apache.commons.httpclient.HttpException;

//import fr.paris.lutece.portal.service.util.AppPathService;
//	/import fr.paris.lutece.plugins.rest.service.RestConstants;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;

/**
 * This class provides the user interface to manage Project xpages ( manage,
 * create, modify, remove )
 */

@Controller(xpageName = "project", pageTitleI18nKey = "bp.xpage.project.pageTitle", pagePathI18nKey = "bp.xpage.project.pagePathLabel")
public class ProjectXPage extends MVCApplication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Templates
	private static final String TEMPLATE_MANAGE_PROJECTS = "/skin/plugins/bp/manage_projects.html";
	private static final String TEMPLATE_DETAIL_PROJECT = "/skin/plugins/bp/details_project.html";
	private static final String TEMPLATE_WEBSERVICE_PROJECT = "/skin/plugins/bp/webservice_projects.html";

	// Parameters
	private static final String PARAMETER_ID_PROJECT = "id";

	// Markers
	private static final String MARK_PROJECT_LIST = "project_list";
	private static final String MARK_PROJECT = "project";

	// Views
	private static final String VIEW_MANAGE_PROJECTS = "manageProjects";
	private static final String VIEW_DETAILS_PROJECT = "detailsProject";
	private static final String VIEW_WEBSERVICE_PROJECT = "webserviceProject";

	// Session variable to store working values
	private Project _project;

	@View(value = VIEW_MANAGE_PROJECTS, defaultView = true)
	public XPage getManageProjects(HttpServletRequest request) {
		_project = null;
		Map<String, Object> model = getModel();
		model.put(MARK_PROJECT_LIST, ProjectHome.getProjectsList());

		return getXPage(TEMPLATE_MANAGE_PROJECTS, request.getLocale(), model);
	}

	/**
	 * Returns the form to update info about a project
	 *
	 * @param request
	 *            The Http request
	 * @return The HTML form to update info
	 */
	@View(VIEW_DETAILS_PROJECT)
	public XPage getDetailsProject(HttpServletRequest request) {
		int nId = Integer.parseInt(request.getParameter(PARAMETER_ID_PROJECT));

		if (_project == null || (_project.getId() != nId)) {
			_project = ProjectHome.findByPrimaryKey(nId);
		}

		Map<String, Object> model = getModel();
		model.put(MARK_PROJECT, _project);

		return getXPage(TEMPLATE_DETAIL_PROJECT, request.getLocale(), model);
	}

	/**
	 * Returns project from web service JSON
	 *
	 * @param request
	 *            The Http request
	 * @return The HTML form to update info
	 */
	@View(VIEW_WEBSERVICE_PROJECT)
	public XPage getWebserviceProjects(HttpServletRequest request) {

		String str_reponse = "{\"Reponse\":\"Reponse\"}";
		Collection<Project> LesProject = new ArrayList<Project>();
		try {

			HttpAccess Client = new HttpAccess();

			str_reponse = Client
					.doGet("http://localhost:8080/bp/rest/json/project");

			JSONObject jsonObject = (JSONObject) JSONSerializer
					.toJSON(str_reponse);

			JSONArray array = JSONArray.fromObject(jsonObject.get("project"));
			for (Object object : array) {
				JSONObject jsonStr = (JSONObject) JSONSerializer.toJSON(object);
				Project project = new Project();
				project.setId((int) jsonStr.get("id"));
				project.setCout((int) jsonStr.get("cout"));
				project.setDescription((String) jsonStr.get("description"));
				project.setImageUrl((String) jsonStr.get("ImageUrl"));
				project.setName((String) jsonStr.get("name"));
				LesProject.add(project);

			}

		} catch (HttpAccessException e) {

		}

		Map<String, Object> model = getModel();
		model.put(MARK_PROJECT_LIST, LesProject);

		return getXPage(TEMPLATE_WEBSERVICE_PROJECT, request.getLocale(), model);

	}

}
