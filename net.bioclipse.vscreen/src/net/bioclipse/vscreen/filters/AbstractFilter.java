package net.bioclipse.vscreen.filters;

import java.util.Iterator;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.TimeCalculater;
import net.bioclipse.structuredb.Activator;
import net.bioclipse.structuredb.business.IStructuredbManager;
import net.bioclipse.structuredb.domain.DBMolecule;
import net.bioclipse.structuredb.domain.TextAnnotation;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

/**
 * 
 * @author ola
 *
 */
public abstract class AbstractFilter {
    

    private IProgressMonitor monitor;
    private String label;
    private String dbname;

    public AbstractFilter(String dbname, String label, IProgressMonitor monitor){
        this.dbname=dbname;
        this.label=label;
        this.monitor=monitor;
    }

   
   
    /**
     * Loop over a StructureDB and call doFilter
     * @throws IllegalArgumentException
     * @throws BioclipseException
     */
    public void run() throws IllegalArgumentException, BioclipseException{

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
            if (cnt%20==1){
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

            if (doMatch(molecule)){
                molecule.addAnnotation(filteredAnnotation);
                sdb.updateMolecule(dbname, molecule);
            }

            monitor.worked( 1 );
        }        
    }

    public abstract boolean doMatch(DBMolecule molecule) 
                throws BioclipseException;
    

}
