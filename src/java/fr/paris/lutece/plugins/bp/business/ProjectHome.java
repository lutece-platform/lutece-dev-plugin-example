/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *	 and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *	 and the following disclaimer in the documentation and/or other materials
 *	 provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *	 contributors may be used to endorse or promote products derived from
 *	 this software without specific prior written permission.
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
 
package fr.paris.lutece.plugins.bp.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for Project objects
 */

public final class ProjectHome
{
    // Static variable pointed at the DAO instance

    private static IProjectDAO _dao = SpringContextService.getBean( "bp.projectDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "bp" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ProjectHome(  )
    {
    }

    /**
     * Create an instance of the project class
     * @param project The instance of the Project which contains the informations to store
     * @return The  instance of project which has been created with its primary key.
     */
    public static Project create( Project project )
    {
        _dao.insert( project, _plugin );

        return project;
    }

    /**
     * Update of the project which is specified in parameter
     * @param project The instance of the Project which contains the data to store
     * @return The instance of the  project which has been updated
     */
    public static Project update( Project project )
    {
        _dao.store( project, _plugin );

        return project;
    }

    /**
     * Remove the project whose identifier is specified in parameter
     * @param nKey The project Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a project whose identifier is specified in parameter
     * @param nKey The project primary key
     * @return an instance of Project
     */
    public static Project findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the project objects and returns them in form of a collection
     * @return the collection which contains the data of all the project objects
     */
    public static Collection<Project> getProjectsList( )
    {
        return _dao.selectProjectsList( _plugin );
    }
    
    /**
     * Load the id of all the project objects and returns them in form of a collection
     * @return the collection which contains the id of all the project objects
     */
    public static Collection<Integer> getIdProjectsList( )
    {
        return _dao.selectIdProjectsList( _plugin );
    }
}

