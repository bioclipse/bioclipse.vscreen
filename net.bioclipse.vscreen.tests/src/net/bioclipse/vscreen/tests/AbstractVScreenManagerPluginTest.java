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
import java.net.URL;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.FileUtil;
import net.bioclipse.structuredb.Activator;
import net.bioclipse.structuredb.business.IStructuredbManager;
import net.bioclipse.vscreen.business.IVScreenManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author ola
 *
 */
public abstract class AbstractVScreenManagerPluginTest {


    protected static IVScreenManager managerNamespace;
    
    private final String TEST_DB_NAME="TestDB";
    private final String TEST_FILE = "/net/bioclipse/vscreen/tests/data/Fragments2.sdf";
    IStructuredbManager sdb;
    
    @Before
    public void initialize() throws IOException, CoreException, BioclipseException{
        
        sdb = Activator.getDefault().getStructuredbManager();
        
        //Set up and populate a StructureDB instance
        if ( sdb.allDatabaseNames().contains(TEST_DB_NAME) ) {
        sdb.deleteDatabase(TEST_DB_NAME);
        }
        sdb.createDatabase(TEST_DB_NAME);
        
        IFile file=null;
        URL url = FileLocator.toFileURL(Platform.getBundle(net.bioclipse.vscreen.tests.Activator.PLUGIN_ID)
                                        .getEntry(TEST_FILE));
        String path=url.getFile();
        file = FileUtil.createLinkedFile(path );
        
        sdb.addMoleculesFromSDF(TEST_DB_NAME, file);
        
    }
    
    @Test
    public void testDatabaseSetup() {
        assertTrue("Test database not set up correctyl.",
                   sdb.allDatabaseNames().contains( TEST_DB_NAME ));

        assertEquals("TEST DB does not contain 5 mols.", 5,
                     sdb.numberOfMoleculesInDatabaseInstance( TEST_DB_NAME ));
        
    }

}
