/* *****************************************************************************
 * Copyright (c) 2009 Ola S pjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.vscreen.filters.impl;

import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.vscreen.filters.AbstractDoubleFilter;
import net.bioclipse.vscreen.filters.IDoubleFilter;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.XLogPDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;

/**
 * 
 * @author ola
 *
 */
public class XlogPFilter extends AbstractDoubleFilter implements IDoubleFilter{

    private static final Logger logger = Logger.getLogger(XlogPFilter.class);

    private ICDKManager cdk;
    private XLogPDescriptor xlogp;

    /**
     * Constructor.
     */
    public XlogPFilter() {
        cdk = 
          net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();
        xlogp=new XLogPDescriptor();
        try {
            //Detect aromaticity but do not use salicyl correction factor
            xlogp.setParameters( new Object[]{true,false} );
        } catch ( CDKException e ) {
            e.printStackTrace();
        }

    }
    

    /**
     * Calculate xlogp and compare with threshold
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException {

        ICDKMolecule cdkmol = cdk.asCDKMolecule( molecule );

        try{
            DescriptorValue res = xlogp.calculate( cdkmol.getAtomContainer() );
            DoubleResult val = (DoubleResult) res.getValue();
            boolean result=compare( val.doubleValue(), getThreshold());
//            if (!result)
            logger.debug(" Mol: + " + cdkmol + " has " + getName() + "=" + val + 
                         " Required: " + operatorToString( getOperator()) + 
                         ""+ getThreshold() + " PASS=" + result );
            return result;
        }catch (Exception e){
            throw new BioclipseException( "Error calculating " + getName() + 
                                          " for mol: " + cdkmol);
        }

    }

}
