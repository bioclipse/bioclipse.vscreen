/*******************************************************************************
 * Copyright (c) 2009 Ola S pjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.vscreen.filters.impl;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.vscreen.filters.AbstractParamFilter;
import net.bioclipse.vscreen.filters.IParamFilter;

/**
 * 
 * @author ola
 *
 */
public class SMARTSFilter extends AbstractParamFilter 
                                   implements IParamFilter{

    private ICDKManager cdk;
    private List<String> smarts;

    /**
     * Constructor.
     */
    public SMARTSFilter() {
        cdk = 
          net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();

    }
    
    /**
     * Return true if no match in SMARTS
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException {
        
        if (smarts==null) initParams();
        if (smarts.size()<=0) return false;

        ICDKMolecule cdkmol = cdk.asCDKMolecule( molecule );
        
        for (String smart : smarts){
            //Filter out if there is a match
            if (cdk.smartsMatches( cdkmol, smart ))
                return false;
        }
        
        return true;
    }

    private void initParams() throws BioclipseException {
        smarts=new ArrayList<String>();
        if (getParameters()==null) throw new BioclipseException( 
            "No parameters for SMARTSFilter!" );
        String[] elements = getParameters().split( " " );
        for (String element : elements){
            smarts.add( element );
        }
    }

}
