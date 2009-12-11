/*******************************************************************************
 * Copyright (c) 2009 Ola S pjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.vscreen.filters;

import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;

import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.XLogPDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;

/**
 * 
 * @author ola
 *
 */
public class XlogPFilter extends AbstractDoubleFilter implements IDoubleFilter{

    private ICDKManager cdk;
    private XLogPDescriptor xlogp;

    /**
     * Constructor.
     */
    public XlogPFilter() {
        cdk = 
          net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();
        xlogp=new XLogPDescriptor();

    }
    

    /**
     * Calculate xlogp and compare with threshold
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException {

        ICDKMolecule cdkmol = cdk.asCDKMolecule( molecule );

        try{
            DescriptorValue res = xlogp.calculate( cdkmol.getAtomContainer() );
            DoubleResult val = (DoubleResult) res.getValue();
            return compare( val.doubleValue(), getThreshold());
        }catch (Exception e){
            throw new BioclipseException( "Error calculating XlogP for mol: " + 
                                          cdkmol);
        }

    }

}
