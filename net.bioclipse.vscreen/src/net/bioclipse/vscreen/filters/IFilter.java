package net.bioclipse.vscreen.filters;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.structuredb.domain.DBMolecule;


public interface IFilter {

    boolean doMatch( DBMolecule molecule ) throws BioclipseException;
    public String getName();
}
