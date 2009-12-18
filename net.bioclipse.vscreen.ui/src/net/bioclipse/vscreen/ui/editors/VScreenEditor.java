package net.bioclipse.vscreen.ui.editors;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * 
 * @author ola
 */
public class VScreenEditor extends EditorPart{

    private static final Logger logger = Logger.getLogger(VScreenEditor.class);

    private TreeViewer viewer;


    @Override
    public void createPartControl( Composite parent ) {
        
        //Treeviewer
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);

        viewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        ColumnViewerToolTipSupport.enableFor(viewer);

        viewer.setContentProvider(new VScreenContentProvider());
        viewer.setLabelProvider(new LabelProvider());

        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        viewer.getTree().setLayoutData(gridData);
        
        viewer.setInput(new String[]{"WEEHOOW"});
        
//        makeActions();
//        hookContextMenu();
//        contributeToActionBars();
        
    }
    

    @Override
    public void doSave( IProgressMonitor monitor ) {
        System.out.println("NOT IMPLEMENTED");
    }

    @Override
    public void doSaveAs() {
        System.out.println("NOT IMPLEMENTED");
    }

    @Override
    public void init( IEditorSite site, IEditorInput input )
                                                     throws PartInitException {
        setSite( site );
        setInput( input );
        if ( input instanceof IFileEditorInput ) {
            IFileEditorInput finput = (IFileEditorInput) input;
            IFile file=finput.getFile();
            
            logger.debug("File to parse: " + file.getFullPath());
            
            //TODO Parse file into model. XOM?
        }
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void setFocus() {
        viewer.getTree().setFocus();
    }

    
}
