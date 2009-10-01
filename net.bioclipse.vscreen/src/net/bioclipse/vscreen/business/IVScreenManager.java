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

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.vscreen.filters.IFilter;

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
    public void filter(String dbname, IFilter filter, String label);

    @Recorded
    @PublishedMethod(params="String dbname, List<IFilter> filters, " +
                            "String label",
       methodSummary = "Filter a database for a certian set of Filters. "+
          "Creates a new label in the database.")
    public void filter(String dbname, List<IFilter> filters, String label);

    @Recorded
    @PublishedMethod(
       methodSummary = "Returns an empty list for Filters.")
    public IFilter createFilter(String filtername, String operator,double value)
        throws BioclipseException;

}
