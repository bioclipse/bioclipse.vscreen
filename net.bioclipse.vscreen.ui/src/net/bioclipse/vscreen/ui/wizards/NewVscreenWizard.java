package net.bioclipse.vscreen.ui.wizards;

import net.bioclipse.core.util.StringInput;
import net.bioclipse.core.util.StringStorage;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

/**
 * Creates a new JS file with VScreen content
 * 
 * @author ola
 */
public class NewVscreenWizard extends Wizard implements INewWizard {

    public static final String WIZARD_ID =
        "net.bioclipse.vscreen.ui.wizards.NewVscreenWizard";
    
    public static String newline = System.getProperty("line.separator");
    
    private static final String FILE_CONTENT =
        "// VScreen file" + newline + 
        "var DB = \"myDB\";  //Refer to an existing StructureDB database" +
        newline +
        "//Create some filters"+ newline +
        "filters=java.util.ArrayList();"+ newline +
        "filters.add(vscreen.createFilter(\"XlogP\" , \"<\" , 3));"+ newline +
        newline +
        "//Creen DB and place results in new DB instance"+ newline +
        "vscreen.filter(DB,filters,\"filteredDB\",\"filtered\");";

    
    private IWorkbenchWindow activeWindow;
    
    /**
     * Creates a wizard for creating a new file resource in the workspace.
     */
    public NewVscreenWizard() {
        super();
    }

    public void addPages() {
    }

    public boolean canFinish() {
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        setWindowTitle("New VScreen file");
        setNeedsProgressMonitor(true);
        activeWindow = workbench.getActiveWorkbenchWindow();
    }

    public boolean performFinish() {
      //Open editor with content (String) as content
        IEditorInput input = createEditorInput();
        IWorkbenchPage page = activeWindow.getActivePage();
        try {
            page.openEditor(input, "net.bioclipse.jseditor.editor");
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        return true;
    }

    private IEditorInput createEditorInput() {
        IStorage storage = new StringStorage(FILE_CONTENT);
        IEditorInput input = new StringInput(storage);
        return input;
    }

}
