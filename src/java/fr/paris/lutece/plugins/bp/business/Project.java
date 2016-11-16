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

import org.hibernate.validator.constraints.*;

import java.util.Locale;

import javax.validation.constraints.*;

import fr.paris.lutece.portal.service.i18n.Localizable;
import fr.paris.lutece.portal.service.resource.IExtendableResource;


/**
 * This is the business class for the object Project
 */
public class Project implements Localizable, IExtendableResource
{
    
    // Variables declarations 
    private int _nId;
    
    @NotEmpty( message = "#i18n{bp.validation.project.Name.notEmpty}" )
    @Size( max = 50, message = "#i18n{bp.validation.project.Name.size}" )
    private String _strName;
    @NotEmpty( message = "#i18n{bp.validation.project.Description.notEmpty}" )
    @Size( max = 255, message = "#i18n{bp.validation.project.Description.size}" )
    private String _strDescription;
    @URL( message = "#i18n{portal.validation.message.url}" )
    @NotEmpty( message = "#i18n{bp.validation.project.ImageUrl.notEmpty}" )
    @Size( max = 255, message = "#i18n{bp.validation.project.ImageUrl.size}" )
    private String _strImageUrl;
    

   // @NotEmpty( message = "#i18n{bp.validation.project.Cout.limite}" )
   // @Min(value = 5, message = "#i18n{bp.validation.project.Cout.limite}")
   // @Max(value = 25, message = "#i18n{bp.validation.project.Cout.limite}")
   // @Range(min = 5, max=25, message = "#i18n{bp.validation.project.Cout.limite}")
    private int _nCout;
    
    
    //proprietes
    private static final String PROPERTY_RESOURCE_TYPE = "project";

    
    public static String getPropertyRessourceType(  )
    {
        return PROPERTY_RESOURCE_TYPE;
    }
    
    
    public int getCout(  )
    {
        return _nCout;
    }

    public void setCout( int _nCout )
    {
        this._nCout = _nCout;
    }

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Name
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the ImageUrl
     * @return The ImageUrl
     */
    public String getImageUrl(  )
    {
        return _strImageUrl;
    }

    /**
     * Sets the ImageUrl
     * @param strImageUrl The ImageUrl
     */
    public void setImageUrl( String strImageUrl )
    {
        _strImageUrl = strImageUrl;
    }

    @Override
    public String getExtendableResourceDescription(  )
    {
        return null;
    }

    @Override
    public String getExtendableResourceType(  )
    {
        return PROPERTY_RESOURCE_TYPE;
    }

    @Override
    public String getIdExtendableResource(  )
    {
        return Integer.toString( getId(  ) );
    }

    @Override
    public void setLocale( Locale arg0 )
    {
        
    }

    @Override
    public String getExtendableResourceImageUrl(  )
    {
        return getImageUrl(  );
    }

    @Override
    public String getExtendableResourceName(  )
    {
        return getName(  );
    }

    @Override
    public String toString(  )
    {
        return "Project [_nId=" + _nId + ", _strName=" + _strName + ", _strDescription=" + _strDescription
            + ", _strImageUrl=" + _strImageUrl + ", _nCout=" + _nCout + "]";
    }
}
