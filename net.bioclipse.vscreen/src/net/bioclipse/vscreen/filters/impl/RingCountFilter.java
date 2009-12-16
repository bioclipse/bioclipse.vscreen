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
import net.bioclipse.vscreen.filters.AbstractDoubleFilter;
import net.bioclipse.vscreen.filters.IDoubleFilter;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.ringsearch.SSSRFinder;

/**
 * 
 * @author ola
 *
 */
public class RingCountFilter extends AbstractDoubleFilter implements IDoubleFilter{

    private static final Logger logger = Logger.getLogger(RingCountFilter.class);

    private ICDKManager cdk;

    /**
     * Constructor.
     */
    public RingCountFilter() {
        cdk = 
          net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();

    }
    

    /**
     * Calculate number of rings and compare with threshold
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException {

        ICDKMolecule cdkmol = cdk.asCDKMolecule( molecule );
        SSSRFinder sssrf = new SSSRFinder(cdkmol.getAtomContainer());
        IRingSet sssr = sssrf.findSSSR();
        int val=sssr.getAtomContainerCount();
        
        boolean result=compare( val, (int)getThreshold());
        logger.debug(" Mol: + " + molecule + " has " + getName() + "=" + val + 
                     " Required: " + operatorToString( getOperator()) + 
                     ""+ getThreshold() + " PASS=" + result );
        return result;
    }

}
