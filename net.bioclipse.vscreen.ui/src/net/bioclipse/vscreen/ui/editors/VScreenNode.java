package net.bioclipse.vscreen.ui.editors;

import java.util.List;


public class VScreenNode {
    
    
    public List<VScreenNode> getChildren() {
    
        return children;
    }

    
    public void setChildren( List<VScreenNode> children ) {
    
        this.children = children;
    }

    List<VScreenNode> children;

}
