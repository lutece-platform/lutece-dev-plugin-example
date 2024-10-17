/*
 * Copyright (c) 2002-2023, City of Paris
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
package fr.paris.lutece.plugins.example.web;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * PaginatedJspBean  abstract class for JSP Bean
 */
public abstract class PaginatedJspBean<S, T> extends MVCAdminJspBean
{
	private static final long serialVersionUID = 1L;

	// Properties
	private final String PROPERTY_SUFFIX_DEFAULT_LIST_ITEM_PER_PAGE = ".listItems.itemsPerPage";

	// Parameters
	private static final String PARAMETER_PAGE_INDEX = "page_index";

	// Markers
	private static final String MARK_PAGINATOR = "paginator";
	private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

	// Variables
	private int _nDefaultItemsPerPage;
	private String _strCurrentPageIndex;
	private int _nItemsPerPage;


	/**
	 * Return a model that contains the list and paginator infos
	 * 
	 * @param request
	 *            The HTTP request
	 * @param strBookmark
	 *            The bookmark
	 * @param list
	 *            The list of item
	 * @param strManageJsp
	 *            The JSP
	 * @return The model
	 */
	protected Map<String, Object> getPaginatedListModel( HttpServletRequest request, String strBookmark, List<S> list, String strManageJsp )
	{
		final int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( getPlugin( ).getName( ) + PROPERTY_SUFFIX_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
		_strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
		_nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, nDefaultItemsPerPage );

		final UrlItem url = new UrlItem( strManageJsp );
		final String strUrl = url.getUrl( );

		// PAGINATOR
		final LocalizedPaginator<S> paginator = new LocalizedPaginator<>( list, _nItemsPerPage, strUrl, PARAMETER_PAGE_INDEX, _strCurrentPageIndex,
				getLocale( ) );

		final Map<String, Object> model = getModel( );

		model.put( MARK_NB_ITEMS_PER_PAGE, String.valueOf( _nItemsPerPage ) );
		model.put( MARK_PAGINATOR, paginator );
		model.put( strBookmark, getItemsFromIds( paginator.getPageItems( ) ) );

		return model;
	}

	/**
	 * Get Items from Ids list
	 *
	 * @param listIds
	 * @return the populated list of items corresponding to the id List
	 */
	abstract List<T> getItemsFromIds( List<S> listIds );
}

