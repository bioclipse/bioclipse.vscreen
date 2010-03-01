package net.bioclipse.vscreen.ui.editors;

import java.util.List;


public class VScreenEditorModel {

    
    public List<VScreenNode> getNodes() {
    
        return nodes;
    }

    
    public void setNodes( List<VScreenNode> nodes ) {
    
        this.nodes = nodes;
    }

    List<VScreenNode> nodes;
    
}
