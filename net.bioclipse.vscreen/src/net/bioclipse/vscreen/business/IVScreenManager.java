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

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="A manager for Virtual Screening."
)
public interface IVScreenManager extends IBioclipseManager {
    
    @Recorded
    @PublishedMethod(params="String dbname, String SMILES, String label",
       methodSummary = "Filter a database for a SMILES and create a new label.")
    public void filterSMILES(String dbname, String SMILES, String label)
            throws IllegalArgumentException, BioclipseException;
    
    
    @Recorded
    @PublishedMethod(params="String dbname, String molWeight, " +
                            "String operator, String label",
       methodSummary = "Filter a database for a certian MW and Operator. " +
       		"Creates a new label in the database.")
    public void filterMW(String dbname, String molWeight, 
                         String operator, String label) 
                         throws IllegalArgumentException, BioclipseException;
    
}
