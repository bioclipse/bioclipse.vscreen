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

 import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.RecordableList;
import net.bioclipse.core.util.TimeCalculater;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.structuredb.Activator;
import net.bioclipse.structuredb.business.IStructuredbManager;
import net.bioclipse.structuredb.domain.DBMolecule;
import net.bioclipse.structuredb.domain.TextAnnotation;
import net.bioclipse.vscreen.filters.IDoubleFilter;
import net.bioclipse.vscreen.filters.IFilter;
import net.bioclipse.vscreen.filters.MWFilter;
import net.bioclipse.vscreen.filters.XlogPFilter;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

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
    

    public void filter(String dbname, IFilter filter, String label, 
                       IProgressMonitor monitor){
        
        List<IFilter> filters=new ArrayList<IFilter>();
        filters.add(filter);
        filter(dbname, filters, label, monitor);        
    }
    
    public void filter(String dbname, List<IFilter> filters, String label, 
                       IProgressMonitor monitor){

        //Get managers we need
        IStructuredbManager sdb=Activator.getDefault().getStructuredbManager();

        //Create the new annotation to store filtered set in
        TextAnnotation filteredAnnotation = sdb.createTextAnnotation( dbname, 
                                                                      "label",
                                                                      label );

        int noMols=sdb.numberOfMoleculesInDatabaseInstance( dbname );
        monitor.beginTask( "Screening molecules...", noMols );
        Iterator<DBMolecule> iterator = sdb.allStructuresIterator(dbname);
        long starttime=System.currentTimeMillis();
        int cnt=0;

        //And iterate over all molecules
        while ( iterator.hasNext() ) {
            cnt++;
            
            //Update progress at regular intervals 
            if (cnt%10==1){
                monitor.subTask( "Screening molecule " + cnt + " of " + noMols 
                                 + " (" 
                                 + TimeCalculater.generateTimeRemainEst( 
                                                        starttime, cnt, noMols )
                                                        + ")");
            }

            //Check for cancellation
            if (monitor.isCanceled())
                throw new OperationCanceledException();

            //Get mol, filter, and add annotation if match
            DBMolecule molecule = iterator.next();

            //For each filter, do matching. All must succeed in order to pass
            int matchesRequired=filters.size();
            int matches=0;
            for (IFilter filter : filters){
                try {
                    if (filter.doMatch( molecule )){
                        matches++;
                    }
                } catch ( BioclipseException e ) {
                    logger.error("Filter " + filter.getName() 
                                 + " failed for mol: " + cnt 
                                 + ". Reason: " + e.getMessage());
                }
            }

            //If all filters passed, add annotation to mol
            if (matches>=matchesRequired){
                molecule.addAnnotation(filteredAnnotation);
                sdb.updateMolecule(dbname, molecule);
            }

            monitor.worked( 1 );
        
        }        
    }

    
    public IFilter createFilter(String filtername, String operator, 
                                double threshold) throws BioclipseException{
        
        filtername=filtername.toLowerCase();
        
        //Look up available filters. Now, just a hardcoded list.
        //TODO: Extend with an extension point!
        Map<String, Class> filterMap=new HashMap<String, Class>();
        filterMap.put( "xlogp", XlogPFilter.class );
        filterMap.put( "molweight", MWFilter.class );
        if (!(filterMap.containsKey( filtername )))
            throw new BioclipseException( "No filter with name: " +filtername );

        //Look up and instantiate filter
        IFilter filter;
        try {
            filter = (IFilter) filterMap.get( filtername ).newInstance();
            if ( filter instanceof IDoubleFilter ) {
                IDoubleFilter df= (IDoubleFilter) filter;
                df.setOperator( operator );
                df.setThreshold( threshold );
                return df;
            }
        } catch ( Exception e ) {
            throw new BioclipseException( "Error instantiating filter: " + e.getMessage() );
        }

        throw new BioclipseException( "Filter found but not supported: " 
                                      + filtername );
        
    }


}
