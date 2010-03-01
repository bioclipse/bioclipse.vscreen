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
package net.bioclipse.vscreen.filters.impl;

import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.vscreen.filters.AbstractDoubleFilter;
import net.bioclipse.vscreen.filters.IDoubleFilter;

import org.apache.log4j.Logger;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.RuleOfFiveDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.IntegerResult;

/**
 * 
 * @author ola
 *
 */
public class RuleOfFiveFilter extends AbstractDoubleFilter implements IDoubleFilter{

    private static final Logger logger = Logger.getLogger(RuleOfFiveFilter.class);
    
    private ICDKManager cdk;
    private RuleOfFiveDescriptor ro5;

    /**
     * Constructor.
     */
    public RuleOfFiveFilter() {
        cdk = 
          net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();
        ro5=new RuleOfFiveDescriptor();

    }
    

    /**
     * Calculate Ro5 and compare with threshold
     */
    public boolean passFilter( IMolecule molecule ) throws BioclipseException {

        ICDKMolecule cdkmol = cdk.asCDKMolecule( molecule );

        try{
            DescriptorValue res = ro5.calculate( cdkmol.getAtomContainer() );
            IntegerResult val = (IntegerResult) res.getValue();
            boolean result=compare( val.intValue(), (int)getThreshold());
            logger.debug(" Mol: + " + molecule + " has " + getName() + "=" + val + 
                         " Required: " + operatorToString( getOperator()) + 
                         ""+ getThreshold() + " PASS=" + result );
            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw new BioclipseException( e.getMessage());
        }

    }

}
