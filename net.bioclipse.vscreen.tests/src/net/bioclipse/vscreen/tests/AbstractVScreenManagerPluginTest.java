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
package net.bioclipse.vscreen.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestFailure;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.FileUtil;
import net.bioclipse.structuredb.Activator;
import net.bioclipse.structuredb.business.IJavaStructuredbManager;
import net.bioclipse.vscreen.business.IVScreenManager;
import net.bioclipse.vscreen.filters.IScreeningFilter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author ola
 *
 */
public abstract class AbstractVScreenManagerPluginTest {


    protected static IVScreenManager vscreen;
    
    private final String TEST_DB_NAME="TestDB";
    private final String TEST_FILE = "/net/bioclipse/vscreen/tests/data/Fragments2.sdf";
    IJavaStructuredbManager sdb;
    
    @Before
    public void initialize() throws IOException, CoreException, BioclipseException, URISyntaxException{
        
        sdb = Activator.getDefault().getStructuredbManager();
        
        //Set up and populate a StructureDB instance
        if ( sdb.allDatabaseNames().contains(TEST_DB_NAME) ) {
            sdb.deleteDatabase(TEST_DB_NAME, new NullProgressMonitor());
        }
        sdb.createDatabase(TEST_DB_NAME);
        
        IFile file=null;
        URI uri = getClass().getResource(TEST_FILE).toURI();
        URL url = FileLocator.toFileURL(uri.toURL());
        String path = url.getFile();

//        URL url = FileLocator.toFileURL(Platform.getBundle(
//                                net.bioclipse.vscreen.tests.Activator.PLUGIN_ID)
//                                .getEntry(TEST_FILE));
//        String path=url.getFile();
        file = FileUtil.createLinkedFile(path );
        
        sdb.addMoleculesFromSDF(TEST_DB_NAME, file, new NullProgressMonitor());
        
    }
    
    @Test
    public void testDatabaseSetup() {
        assertTrue("Test database not set up correctyl.",
                   sdb.allDatabaseNames().contains( TEST_DB_NAME ));

        assertEquals("TEST DB does not contain 5 mols.", 5,
                     sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME ));
        
    }

    @Test
    public void testXlogPFilter() throws BioclipseException {
        
        sdb = Activator.getDefault().getStructuredbManager();

        int nomols=sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME );
        assertEquals("Incorrect number of mols in TEST DB.", 5, nomols );
        
        //Add some filters
        List<IScreeningFilter>filters = new ArrayList<IScreeningFilter>();
        filters.add(vscreen.createFilter("XlogP" , "<" , 3));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterXLOGP", "xlogp" );
        assertEquals(3, sdb.numberOfMoleculesInDatabaseInstance( "filterXLOGP" ));

        //Add another filters
        filters.add(vscreen.createFilter("XlogP" , ">" , 1.5));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterXLOGP2", "xlogp2" );
        assertEquals(1, sdb.numberOfMoleculesInDatabaseInstance( "filterXLOGP2" ));

    }
    
    

    @Test
    public void testMWFilter() throws BioclipseException {
        
        sdb = Activator.getDefault().getStructuredbManager();

        int nomols=sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME );
        assertEquals("Incorrect number of mols in TEST DB.", 5, nomols );
        
        //Add some filters
        List<IScreeningFilter>filters = new ArrayList<IScreeningFilter>();
        filters.add(vscreen.createFilter("MW" , "<" , 290));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterMW", "mw" );
        assertEquals(3, sdb.numberOfMoleculesInDatabaseInstance( "filterMW" ));

        //Add another filters
        filters.add(vscreen.createFilter("MW" , ">" , 240));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterMW2", "xlogpMW2" );
        assertEquals(1, sdb.numberOfMoleculesInDatabaseInstance( "filterMW2" ));

    }
    
    @Test
    public void testElementFilter() throws BioclipseException {
        
        sdb = Activator.getDefault().getStructuredbManager();

        int nomols=sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME );
        assertEquals("Incorrect number of mols in TEST DB.", 5, nomols );
        
        //Add some filters
        List<IScreeningFilter>filters = new ArrayList<IScreeningFilter>();
        filters.add(vscreen.createFilter("restrictElement", "C,N,O,Cl,S"));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterElement", "element" );
        assertEquals(5, sdb.numberOfMoleculesInDatabaseInstance( "filterElement" ));

        //Add another filters
        filters.add(vscreen.createFilter("restrictElement", "C,N,O,S"));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterElement2", "element2" );
        assertEquals(3, sdb.numberOfMoleculesInDatabaseInstance( "filterElement2" ));

    }
    
    @Test
    public void testRo5Filter() throws BioclipseException {
        
        sdb = Activator.getDefault().getStructuredbManager();

        int nomols=sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME );
        assertEquals("Incorrect number of mols in TEST DB.", 5, nomols );
        
        //Add some filters
        List<IScreeningFilter>filters = new ArrayList<IScreeningFilter>();
        
        //Should have less than 2 failures
        filters.add(vscreen.createFilter("RuleOfFive", "<=" , 2));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterRuleOfFive", "RuleOfFive" );
        assertEquals(4, sdb.numberOfMoleculesInDatabaseInstance( "filterRuleOfFive" ));

        //Add another filter, no failures now required
        filters.add(vscreen.createFilter("RuleOfFive", "=" , 0));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterRuleOfFive2", "RuleOfFive2" );
        assertEquals(1, sdb.numberOfMoleculesInDatabaseInstance( "filterRuleOfFive2" ));

    }
 
    @Test
    public void testSMARTSFilter() throws BioclipseException {
        
        sdb = Activator.getDefault().getStructuredbManager();

        int nomols=sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME );
        assertEquals("Incorrect number of mols in TEST DB.", 5, nomols );
        
        //Add some filters
        List<IScreeningFilter>filters = new ArrayList<IScreeningFilter>();
        filters.add(vscreen.createFilter("restrictElement", "C,N,O,Cl,S,F"));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterElement", "element" );
        assertEquals(4, sdb.numberOfMoleculesInDatabaseInstance( "filterElement" ));

        //Add another filters
        filters.add(vscreen.createFilter("restrictElement", "C,N,O"));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterElement2", "element2" );
        assertEquals(1, sdb.numberOfMoleculesInDatabaseInstance( "filterElement2" ));

    }

    @Test
    public void testRingCountFilter() throws BioclipseException {
        
        sdb = Activator.getDefault().getStructuredbManager();

        int nomols=sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME );
        assertEquals("Incorrect number of mols in TEST DB.", 5, nomols );
        
        //Add some filters
        List<IScreeningFilter>filters = new ArrayList<IScreeningFilter>();
        
        //Should have less than 2 failures
        filters.add(vscreen.createFilter("RingCount"    , ">=" , 1));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterRingCount", "RingCount" );
        assertEquals(4, sdb.numberOfMoleculesInDatabaseInstance( "filterRingCount" ));

        //Add another filter, no failures now required
        filters.add(vscreen.createFilter("RingCount"    , ">=" , 2));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterRingCount2", "RingCount2" );
        assertEquals(1, sdb.numberOfMoleculesInDatabaseInstance( "filterRingCount2" ));

    }

    @Test
    public void testTPSA() throws BioclipseException {
        
        sdb = Activator.getDefault().getStructuredbManager();

        int nomols=sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME );
        assertEquals("Incorrect number of mols in TEST DB.", 5, nomols );
        
        //Add some filters
        List<IScreeningFilter>filters = new ArrayList<IScreeningFilter>();
        
        //Should have less than 2 failures
        filters.add(vscreen.createFilter("TPSA"    , "<" , 100));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterTPSA", "TPSA" );
        assertEquals(3, sdb.numberOfMoleculesInDatabaseInstance( "filterTPSA" ));

        //Add another filter, no failures now required
        filters.add(vscreen.createFilter("TPSA"    , ">" , 80));

        //Screen with filter and assert results
        vscreen.filter(TEST_DB_NAME, filters, "filterTPSA", "TPSA" );
        assertEquals(2, sdb.numberOfMoleculesInDatabaseInstance( "filterTPSA" ));

    }

}
