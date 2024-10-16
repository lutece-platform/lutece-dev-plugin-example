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
package fr.paris.lutece.plugins.example.business;

import fr.paris.lutece.portal.service.i18n.Localizable;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Locale;

/**
 * This is the business class for the object Project
 */
public class Project implements Localizable, IExtendableResource {


    private static final long serialVersionUID = 1L;

    public static final String PROPERTY_RESOURCE_TYPE = "Project_resource_type";

    // Variables declarations
    private int _nId;
    private Locale _locale;

    @NotEmpty( message = "#i18n{example.validation.project.Name.notEmpty}" )
    @Size( max = 50, message = "#i18n{example.validation.project.Name.size}" )
    private String _strName;

    @NotEmpty( message = "#i18n{example.validation.project.Description.notEmpty}" )
    @Size( max = 255, message = "#i18n{example.validation.project.Description.size}" )
    private String _strDescription;

    @NotEmpty( message = "#i18n{example.validation.project.ImageUrl.notEmpty}" )
    @Size( max = 255, message = "#i18n{example.validation.project.ImageUrl.size}" )
    private String _strImageUrl;

    @Min ( value = 5 , message = "#i18n{example.validation.project.cost.range}" )
    @Max ( value = 25 , message = "#i18n{example.validation.project.cost.range}" )
    private int _nCost;

    public static final String MESSAGE_INVALID_COST = "#i18n{example.validation.project.cost.range}";

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     *
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName
     *            The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription
     *            The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the ImageUrl
     *
     * @return The ImageUrl
     */
    public String getImageUrl( )
    {
        return _strImageUrl;
    }

    /**
     * Sets the ImageUrl
     *
     * @param strImageUrl
     *            The ImageUrl
     */
    public void setImageUrl( String strImageUrl )
    {
        _strImageUrl = strImageUrl;
    }

    /**
     * Sets the cost
     * @param nCost The cost
     */
    public void setCost(int nCost) {
        this._nCost = nCost;
    }

    /**
     * Returns the cost
     * @return The cost
     */
    public int getCost( )
    {
        return _nCost;
    }


    /**
     * Cost control :
     *
     * @return true if cost is a mutiple of 5, false otherwise
     */
    @JsonIgnore
    public boolean isCostValid( )
    {
        return ( _nCost % 5 == 0  );
    }


    @Override
    public void setLocale(Locale locale) {
        this._locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getIdExtendableResource() {
        return Integer.toString(_nId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getExtendableResourceType() {
        return PROPERTY_RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getExtendableResourceName() {
        return _strName;
    }

    @Override
    @JsonIgnore
    public String getExtendableResourceDescription() {
        return _strDescription;
    }

    @Override
    @JsonIgnore
    public String getExtendableResourceImageUrl() {
        return _strImageUrl;
    }

}
