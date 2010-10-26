/* *****************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.vscreen.filters;

import org.eclipse.swt.graphics.Image;

import net.bioclipse.core.api.BioclipseException;
import net.bioclipse.core.api.domain.IMolecule;

/**
 * An IScreeningFilter is used to filter out molecules in VScreen.
 * 
 * @author ola
 */
public interface IScreeningFilter {

    /**
     * 
     * @return the name of the ScreeningFilter
     */
    public String getName();

    /**
     * @return the Id of the ScreeningFilter
     */
    public String getId();

    /**
     * @return the icon for the ScreeningFilter
     */
    public Image getIcon();


    /**
     * @return the XML serialization of this filter
     */
    public String toXML();

    /**
     * 
     * @param xml Initialize from XML serialization
     */
    public void fromXML(String xml);


    /**
     * Selects if the molecule makes it through the filter.
     * @param molecule
     * @return
     * @throws BioclipseException
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException;

    void setIcon( Image icon );

    void setName( String name );

    void setId( String id );

    String getPlugin();

    void setPlugin( String plugin );

    void setIconpath( String iconpath );

    String getDescription();

    void setDescription( String description );

}
