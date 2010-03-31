/* *****************************************************************************
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

import java.util.List;

/**
 * An IParamFilter can take a String as input parameter
 * @author ola
 */
public interface IParamFilter extends IScreeningFilter{

    public void setParameterString(String params);
    public String getParameterString();

    public List<String> getParameters();
    public void setParameterss( List<String> params );

}
