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
import java.util.Iterator;
import java.util.List;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.LogUtils;
import net.bioclipse.core.util.TimeCalculater;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.structuredb.Activator;
import net.bioclipse.structuredb.business.IJavaStructuredbManager;
import net.bioclipse.structuredb.domain.DBMolecule;
import net.bioclipse.structuredb.domain.TextAnnotation;
import net.bioclipse.vscreen.filters.IDoubleFilter;
import net.bioclipse.vscreen.filters.IScreeningFilter;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;

/**
 * 
 * @author ola
 *
 */
public class VScreenManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(VScreenManager.class);

    /**
     * List of screening filters as read from Extension Point
     */
    private List<IScreeningFilter> screeningFilters;
    
    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "vscreen";
    }
    

    public void filter(String dbname, IScreeningFilter filter, String label, 
                       IProgressMonitor monitor) throws BioclipseException{
        
        List<IScreeningFilter> filters=new ArrayList<IScreeningFilter>();
        filters.add(filter);
        filter(dbname, filters, label, monitor);        
    }

    /**
     * Filters and puts result in same database under a new label
     * @param dbname
     * @param filters
     * @param label
     * @param monitor
     * @throws BioclipseException
     */
    public void filter(String dbname, List<IScreeningFilter> filters, 
                       String label, 
                       IProgressMonitor monitor) throws BioclipseException{

        filter( dbname, filters, dbname, label, monitor );
        
    }
    
    /**
     * Filters and puts result in arbitrary DB and label
     * @param dbname
     * @param filters
     * @param newDBname
     * @param label
     * @param monitor
     * @throws BioclipseException
     */
    public void filter(String dbname, List<IScreeningFilter> filters, 
                       String newDBname, String label, 
                       IProgressMonitor monitor) throws BioclipseException{

        //Get managers we need
     IJavaStructuredbManager sdb=Activator.getDefault().getStructuredbManager();
        
        if (!(sdb.allDatabaseNames().contains( dbname )))
            throw new BioclipseException( "No database exists with name: " 
                                          + dbname );
        
        if (filters==null || filters.size()<=0)
            throw new BioclipseException( "Please provide at least one filter");

        if (label==null || label.length()<=0)
            throw new BioclipseException( "Label must not be empty");

        //Verify new DB exists
        if (!sdb.allDatabaseNames().contains( newDBname )){
            //create new DB
            logger.debug("Database: " + newDBname + " does not exist, " +
            		"creating this now.");
            sdb.createDatabase( newDBname );
                        
        }

        //Create the annotation to store in.
        TextAnnotation filteredAnnotation = sdb.createTextAnnotation( newDBname, 
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
            for (IScreeningFilter filter : filters){
                try {
                    if (filter.passFilter( molecule )){
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
                if (dbname==newDBname){
                    molecule.addAnnotation(filteredAnnotation);
                    sdb.updateMolecule(dbname, molecule);
                }
                else{
                    //New DB, we need to copy the molecule to new database
                    DBMolecule newmol = sdb.createMolecule( newDBname, 
                                        molecule.getName(), 
                                        molecule );

                    //Also, we need to annotate it with the filteredAnnotation
                    sdb.annotate( newDBname, newmol, filteredAnnotation );
                    sdb.save( newDBname, newmol );
                }
            }

            monitor.worked( 1 );
        
        }        
    }

    public IScreeningFilter createFilter(String filtername, double value) 
    throws BioclipseException{

        //FIXME, for now treat as operator=""
        return createFilter( filtername, "", value );
    }
    
    public IScreeningFilter createFilter(String filtername, String operator, 
                                double threshold) throws BioclipseException{
        
        filtername=filtername.toLowerCase();
        
        if (getFilterByName( filtername )==null)
            throw new BioclipseException( "No filter with name: " +filtername );

        //Look up and instantiate filter
        IScreeningFilter filter;
        try {
            filter = getFilterByName(filtername);
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

    private IScreeningFilter getFilterByName( String filtername ) 
    throws BioclipseException {
        for (IScreeningFilter filter : getFilterMap()){
            if (filter.getName().equalsIgnoreCase( filtername ))
                return filter;
        }
        return null;
    }


    public List<String> listFilters() throws BioclipseException{
        List<String> ret= new ArrayList<String>();
        for (IScreeningFilter filter : getFilterMap()){
            ret.add(filter.getName());
        }
        return ret;
    }

    /**
     * Get map of available filters. If null, initialize from EP.
     * @return
     * @throws BioclipseException 
     */
    private List<IScreeningFilter> getFilterMap() throws BioclipseException {

        if (screeningFilters!=null) return screeningFilters;

        IExtensionRegistry registry = Platform.getExtensionRegistry();

        if ( registry == null ) throw new BioclipseException(
                "Eclipse registry=null. Cannot get screeningfilters from EPs.");
        // it likely means that the Eclipse workbench has not
        // started, for example when running non plugin-tests

        //Store filters here
        screeningFilters = new ArrayList<IScreeningFilter>();

        IExtensionPoint serviceObjectExtensionPoint = registry
        .getExtensionPoint("net.bioclipse.vscreen.filter");

        IExtension[] serviceObjectExtensions
        = serviceObjectExtensionPoint.getExtensions();

        for(IExtension extension : serviceObjectExtensions) {
            for( IConfigurationElement element
                    : extension.getConfigurationElements() ) {

                if (element.getName().equals("screeningFilter")){

                    String pid=element.getAttribute("id");
                    String pname=element.getAttribute("name");
                    String picon=element.getAttribute("icon");
                    String pdescription=element.getAttribute("description");
                    String pluginID=element.getNamespaceIdentifier();


                    Object obj;
                    try {
                        obj = element.createExecutableExtension("class");
                        IScreeningFilter filter = (IScreeningFilter) obj;
                        filter.setId( pid );
                        filter.setName(  pname );
                        filter.setIconpath( picon );
                        filter.setDescription( pdescription );
                        filter.setPlugin( pluginID );

                        screeningFilters.add( filter );
                    } catch ( CoreException e ) {
                        LogUtils.handleException( e, logger, 
                                    net.bioclipse.vscreen.Activator.PLUGIN_ID );
                    }
                }
            }
        }

        return screeningFilters;

    }



}
