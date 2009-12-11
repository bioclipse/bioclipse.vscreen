package net.bioclipse.vscreen.filters;

import org.eclipse.swt.graphics.Image;

/**
 * An asbtract class for screening filters handling common fields.
 * 
 * @author ola
 */
public abstract class AbstractScreeningFilter implements IScreeningFilter{

    private Image icon;
    private String name;
    private String id;
    private String plugin;
    private String iconpath;
    private String description;

    

    public AbstractScreeningFilter(Image icon, String name, String id,
            String plugin, String iconpath, String description) {
        super();
        this.icon = icon;
        this.name = name;
        this.id = id;
        this.plugin = plugin;
        this.iconpath = iconpath;
        this.description = description;
    }

    public AbstractScreeningFilter(String name, String id, String plugin) {
        super();
        this.name = name;
        this.id = id;
        this.plugin = plugin;
    }
    
    public AbstractScreeningFilter() {
    }

    /**
     * Cache icon on first use.
     */
    public Image getIcon() {
        //Create the icon if not already done so
        if (icon==null && plugin!=null && iconpath!=null)
            icon=net.bioclipse.vscreen.Activator.imageDescriptorFromPlugin( 
                      plugin, iconpath ).createImage();
        return icon;
    }

    
    public void setIcon( Image icon ) {
        this.icon = icon;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName( String name ) {
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId( String id ) {
        this.id = id;
    }

    public String getPlugin() {
        return plugin;
    }
    
    public void setPlugin( String plugin ) {
        this.plugin = plugin;
    }

    public String getIconpath() {
        return iconpath;
    }
    
    public void setIconpath( String iconpath ) {
        this.iconpath = iconpath;
    }

    
    public String getDescription() {
    
        return description;
    }

    
    public void setDescription( String description ) {
    
        this.description = description;
    }

    
}
