/*******************************************************************************
 * Copyright (c) 2009 Ola Spjuth.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ola Spjuth - initial API and implementation
 ******************************************************************************/
package net.bioclipse.vscreen.filters;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.LogUtils;
import net.bioclipse.vscreen.Activator;


/**
 * An abstract implementation of an IParamFilter.
 * 
 * @author ola
 *
 */
public abstract class AbstractParamFilter extends AbstractScreeningFilter 
                                           implements IParamFilter {

    private static final Logger logger = 
        Logger.getLogger(AbstractParamFilter.class);

    String paramString;
    
    public List<String> getParameters() {
        return params;
    }

    
    public void setParameterss( List<String> params ) {
        this.params = params;
    }

    private List<String> params;
    
    public String getParameterString() {
        return paramString;
    }

    public void setParameterString( String parameters ) {
        this.paramString = parameters;
        try {
            initParams();
        } catch ( BioclipseException e ) {
            LogUtils.handleException( e, logger, Activator.PLUGIN_ID );
        }
    }
    
    private void initParams() throws BioclipseException {
        params=new ArrayList<String>();
        if (getParameterString()==null) throw new BioclipseException( 
                                   "No parameters for RestrictElementFilter!" );
        String[] elements = getParameterString().split( "," );
        for (String element : elements){
            params.add( element );
        }
    }


    public String toXML(){
        String ret=" <filter type='" + getName() + "'>\n";
        for (String param : params){
            ret=ret + "    <parameter name='param' vlue='" + param+ " />\n";
        }
        ret = ret + "</filter>\n";
        return ret;
    }
    
    public void fromXML(String xml){
        //TODO
    }

}
