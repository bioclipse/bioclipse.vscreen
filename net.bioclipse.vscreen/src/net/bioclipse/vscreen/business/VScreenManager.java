/*******************************************************************************
 * Copyright (c) 2009  Ola Spjuth <ola@bioclipse.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.vscreen.business;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.vscreen.filters.MWFilter;
import net.bioclipse.vscreen.filters.SMILESFilter;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * @author ola
 *
 */
public class VScreenManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(VScreenManager.class);

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "vscreen";
    }
    
    /**
     * Filter on Molecular Weight
     */
    public void filterMW(String dbname, String molWeight, 
                         String operator, String label, 
                         IProgressMonitor monitor) 
                         throws IllegalArgumentException, BioclipseException{
              
        MWFilter filter=new MWFilter(dbname, 
                                     molWeight, 
                                     operator, 
                                     label, 
                                     monitor);
        filter.run();
    }
    
    /**
     * Filter on SMILES containing a certain string
     */
    public void filterSMILES(String dbname, String SMILES, String label, 
                       IProgressMonitor monitor) 
                       throws IllegalArgumentException, BioclipseException{
        
        SMILESFilter filter=new SMILESFilter(dbname, SMILES, label, monitor);
        filter.run();

    }

}
