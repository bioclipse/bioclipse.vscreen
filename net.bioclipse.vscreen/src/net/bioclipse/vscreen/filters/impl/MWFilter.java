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
package net.bioclipse.vscreen.filters.impl;

import org.apache.log4j.Logger;

import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.core.api.BioclipseException;
import net.bioclipse.core.api.domain.IMolecule;
import net.bioclipse.vscreen.filters.AbstractDoubleFilter;
import net.bioclipse.vscreen.filters.IDoubleFilter;


/**
 * An MWFilter is an IDoubleFilter that filters out molecules based on 
 * their molecular weight.
 * 
 * @author ola
 *
 */
public class MWFilter extends AbstractDoubleFilter implements IDoubleFilter{

    private static final Logger logger = Logger.getLogger(MWFilter.class);

    private ICDKManager cdk;

    /**
     * Constructor.
     */
    public MWFilter() {
        cdk = 
          net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();
    }

    /**
     * Filter out molecules base on molecular weight,
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException {
        double val = cdk.calculateMass( molecule);
        boolean result=compare( val, getThreshold());
        logger.debug(" Mol: + " + molecule + " has " + getName() + "=" + val + 
                     " Required: " + operatorToString( getOperator()) + 
                     ""+ getThreshold() + " PASS=" + result );
       return result;
    }

}
