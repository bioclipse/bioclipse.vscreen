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

import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.vscreen.filters.AbstractParamFilter;
import net.bioclipse.vscreen.filters.IParamFilter;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtom;

/**
 * 
 * @author ola
 *
 */
public class RestrictElementFilter extends AbstractParamFilter 
                                   implements IParamFilter{

    private static final Logger logger = Logger.getLogger(RestrictElementFilter.class);

    private ICDKManager cdk;

    /**
     * Constructor.
     */
    public RestrictElementFilter() {
        cdk = 
          net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();

    }
    
    /**
     * Return true if all atoms are of allowed symbols.
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException {
        
        if (getParameters()==null || getParameters().size()<=0 ) throw 
             new BioclipseException( "No parameters for filter: " + getName() );

        ICDKMolecule cdkmol = cdk.asCDKMolecule( molecule );
        
        for (IAtom atom : cdkmol.getAtomContainer().atoms()){
            if (!getParameters().contains( atom.getSymbol())){
                logger.debug(" Mol: + " + molecule + " contained: " 
                             +  atom.getSymbol() + " which is not allowed.");
                return false;
            }
        }
        
        return true;
    }


}
