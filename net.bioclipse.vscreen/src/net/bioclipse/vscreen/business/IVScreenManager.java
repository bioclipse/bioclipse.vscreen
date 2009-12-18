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

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.vscreen.filters.IScreeningFilter;

/**
 * 
 * @author ola
 *
 */
@PublishedClass(
    value="A manager for Virtual Screening."
)
public interface IVScreenManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(params="String dbname, IFilter filter, " +
                            "String label",
       methodSummary = "Filter a database for a Filter. "+
          "Creates a new label in the database.")
    public void filter(String dbname, IScreeningFilter filter, String label)
        throws BioclipseException;
    
    @Recorded
    @PublishedMethod(params="String dbname, List<IFilter> filters, " +
                            "String label",
       methodSummary = "Filter a database for a certian set of Filters. "+
          "Creates a new label in the database.")
    public void filter(String dbname, List<IScreeningFilter> filters, String label)
        throws BioclipseException;
    
    @Recorded
    @PublishedMethod(params="String dbname, List<IFilter> filters, " +
                            "String newDBname, String label",
       methodSummary = "Filter a database for a certian set of Filters. "+
          "Creates a new database with a new label, holding the results.")
    public void filter(String dbname, List<IScreeningFilter> filters, 
                       String newDBname, String label)
    throws BioclipseException;

    /**
     * Used for testing only.
     */
    public void filter(String dbname, List<IScreeningFilter> filters, 
                       String newDBname, String label, 
                       IProgressMonitor monitor);

    @Recorded
    @PublishedMethod(params="String filtername, String params",
       methodSummary = "Create a filter by name and parameters.")
    public IScreeningFilter createFilter(String filtername, String params)
    throws BioclipseException;

    @Recorded
    @PublishedMethod(params="String filtername, String operator, double value",
       methodSummary = "Create a filter by name, operator, and threshold.")
    public IScreeningFilter createFilter(String filtername, String operator, 
                                         double threshold) 
    throws BioclipseException;

    @Recorded
    @PublishedMethod(params="String filtername, double value",
       methodSummary = "Create a filter by name, and value.")
    public IScreeningFilter createFilter(String filtername, double value) 
    throws BioclipseException;

    @Recorded
    @PublishedMethod(
       methodSummary = "Returns a list of available Filters.")
    public List<String> listFilters() throws BioclipseException;

}
