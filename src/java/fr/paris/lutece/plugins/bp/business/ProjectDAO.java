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
package fr.paris.lutece.plugins.bp.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Project objects
 */
public final class ProjectDAO implements IProjectDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_project ) FROM bp_project";
    private static final String SQL_QUERY_SELECT = "SELECT id_project, name, description, image_url,cout FROM bp_project WHERE id_project = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO bp_project ( id_project, name, description, image_url,cout ) VALUES ( ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM bp_project WHERE id_project = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE bp_project SET id_project = ?, name = ?, description = ?, image_url = ?, cout= ? WHERE id_project = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_project, name, description, image_url,cout FROM bp_project";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_project FROM bp_project";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Project project, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        project.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, project.getId(  ) );
        daoUtil.setString( 2, project.getName(  ) );
        daoUtil.setString( 3, project.getDescription(  ) );
        daoUtil.setString( 4, project.getImageUrl(  ) );
        daoUtil.setInt( 5, project.getCout(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Project load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        Project project = null;

        if ( daoUtil.next(  ) )
        {
            project = new Project(  );
            project.setId( daoUtil.getInt( 1 ) );
            project.setName( daoUtil.getString( 2 ) );
            project.setDescription( daoUtil.getString( 3 ) );
            project.setImageUrl( daoUtil.getString( 4 ) );
            project.setCout( daoUtil.getInt( 5 ) );
        }

        daoUtil.free(  );

        return project;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Project project, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, project.getId(  ) );
        daoUtil.setString( 2, project.getName(  ) );
        daoUtil.setString( 3, project.getDescription(  ) );
        daoUtil.setString( 4, project.getImageUrl(  ) );
        daoUtil.setInt( 5, project.getCout(  ) );
        daoUtil.setInt( 6, project.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Project> selectProjectsList( Plugin plugin )
    {
        Collection<Project> projectList = new ArrayList<Project>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Project project = new Project(  );

            project.setId( daoUtil.getInt( 1 ) );
            project.setName( daoUtil.getString( 2 ) );
            project.setDescription( daoUtil.getString( 3 ) );
            project.setImageUrl( daoUtil.getString( 4 ) );
            project.setCout( daoUtil.getInt( 5 ) );

            projectList.add( project );
        }

        daoUtil.free(  );

        return projectList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdProjectsList( Plugin plugin )
    {
        Collection<Integer> projectList = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            projectList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return projectList;
    }
}