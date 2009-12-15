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

import org.openscience.cdk.interfaces.IAtom;

/**
 * 
 * @author ola
 *
 */
public class RestrictElementFilter extends AbstractParamFilter 
                                   implements IParamFilter{

    private ICDKManager cdk;
    private List<String> allowedSymbols;

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
        
        if (allowedSymbols==null) initParams();
        if (allowedSymbols.size()<=0) return false;

        ICDKMolecule cdkmol = cdk.asCDKMolecule( molecule );
        
        for (IAtom atom : cdkmol.getAtomContainer().atoms()){
            if (!allowedSymbols.contains( atom.getSymbol()))
                return false;
        }
        
        return true;
    }

    private void initParams() throws BioclipseException {
        allowedSymbols=new ArrayList<String>();
        if (getParameters()==null) throw new BioclipseException( 
                                   "No parameters for RestrictElementFilter!" );
        String[] elements = getParameters().split( "," );
        for (String element : elements){
            allowedSymbols.add( element );
        }
    }

}
