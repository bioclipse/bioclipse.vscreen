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

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.structuredb.domain.DBMolecule;

import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.XLogPDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;

/**
 * 
 * @author ola
 *
 */
public class XlogPFilter extends AbstractDoubleFilter implements IDoubleFilter{

    public XlogPFilter() {
    }

    public String getName() {
        return "XlogP";
    }

    /**
     * Calculate xlogp and compare with threshold
     */
    public boolean doMatch( DBMolecule molecule ) throws BioclipseException {
        
            XLogPDescriptor xlogp=new XLogPDescriptor();
            try{
                DescriptorValue res = xlogp.calculate( molecule.getAtomContainer() );
                DoubleResult val = (DoubleResult) res.getValue();
                return compare( val.doubleValue(), getThreshold());
            }catch (Exception e){
                throw new BioclipseException( "Error calculating XlogP for mol: " + molecule );
            }
        
    }

}
