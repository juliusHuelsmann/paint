//package declaration
package view.forms;

//import declarations
import java.awt.Toolkit;

import model.settings.ViewSettings;
import control.util.CItem;
import view.tabs.Export;
import view.tabs.Insert;
import view.tabs.Look;
import view.tabs.Paint;
import view.tabs.PaintObjects;
import view.tabs.Print;
import view.tabs.Selection;
import view.tabs.Write;
import view.util.VTabbedPane;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Tabs extends VTabbedPane {

    /**
     * The instance.
     */
    private static Tabs instance;

    /**
     * Tab for general painting stuff like pen, colors etc.
     */
    private Paint tab_paint;
    
    
    /**
     * Tab for things which can be inserted.
     */
    private Insert tab_insert;
    
    /**
     * Empty utility class constructor.
     */
    private Tabs() {
    	
    }
    
    
    /**
     * 
     * pseudo- constructor: adds instances of tabs to tabbedPane.
     */
    public void initialize() {

        
        //TabbedPane for different pages
        super.setSize(
                ViewSettings.getView_widthTb(), 
                ViewSettings.getView_heightTB(),
                ViewSettings.getView_heightTB_visible());
        super.setOpaque(true);
        super.setFocusable(false);
        super.setVisible(false);

        int tabNumber = 0;
        /*
         * tab paint
         */
        super.addTab("Painting");
        tab_paint = Paint.getInstance();
        super.addToTab(tabNumber, tab_paint);
        tabNumber++;

        /*
         * 
         */
        super.addTab("Writing");
        super.addToTab(tabNumber, Write.getInstance());
        tabNumber++;


        /*
         * tab insert
         */
        super.addTab("Insertion");
        tab_insert = Insert.getInstance();
        super.addToTab(tabNumber, tab_insert);
        tabNumber++;

        
        /*
         * 
         */
        super.addTab("Selection");
        Selection tab_selection = Selection.getInstance();
        super.addToTab(tabNumber, tab_selection);
        tabNumber++;
        /*
         * tab view
         */
        super.addTab("View");   //view
        super.addToTab(tabNumber, Look.getInstance());
        tabNumber++;

        /*
         * 
         */
        super.addTab("Export");
        super.addToTab(tabNumber, Export.getInstance());
        tabNumber++;
        


        
        /*
         * tab print
         */
        super.addTab("Printing");
        super.addToTab(tabNumber, Print.getInstance());
        tabNumber++;


        super.addTab("Debug");
        PaintObjects tab_pos = PaintObjects.getInstance();
        PaintObjects.getInstance().setSize(
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
                ViewSettings.getView_heightTB());
        super.addToTab(tabNumber, tab_pos);
        tabNumber++;
        
        /*
         * tab print
         */
        super.addTab("Projects");
        tabNumber++;

        /*
         * tab print
         */
        super.addTab("Goals");
        tabNumber++;

        /*
         * tab print
         */
        super.addTab("Overview");
        tabNumber++;


        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");
        super.addTab("");

    
    }
    
    
    
    /**
     * Closes all menus in tabs.
     */
    public void closeMenues() {
        
        //menus in paint tab
        tab_paint.getIt_stift1().setOpen(false);
        tab_paint.getIt_stift2().setOpen(false);
        tab_paint.getIt_selection().setOpen(false);
        tab_paint.getIt_color().setOpen(false);

        //menus in insert tab
        tab_insert.getIa_diagram().setOpen(false);
        tab_insert.getIa_geo().setOpen(false);
        tab_insert.getIa_maths().setOpen(false);
        
        //reset open tab.
        CItem.getInstance().reset();
    }
    
    
    
    

    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static Tabs getInstance() {
        
        if (instance == null) {
            instance = new Tabs();
            instance.initialize();
        }
        return instance;
    }

    /**
     * @return the tab_paint
     */
    public final Paint getTab_paint() {
        return tab_paint;
    }
}
