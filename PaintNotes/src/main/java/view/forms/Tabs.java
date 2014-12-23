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
public class Tabs extends VTabbedPane {

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
     * Constructor: adds instances of tabs to tabbedpane.
     */
    public Tabs() {
        
        //TabbedPane for different pages
        super();
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
        super.addTab("zeichnen");
        tab_paint = Paint.getInstance();
        super.addToTab(tabNumber, tab_paint);
        tabNumber++;

        /*
         * 
         */
        super.addTab("Schreiben");
        super.addToTab(tabNumber, Write.getInstance());
        tabNumber++;
        
        /*
         * tab insert
         */
        super.addTab("einfuegen");
        tab_insert = Insert.getInstance();
        super.addToTab(tabNumber, tab_insert);
        tabNumber++;

        /*
         * 
         */
        super.addTab("auswahl");
        Selection tab_selection = Selection.getInstance();
        super.addToTab(tabNumber, tab_selection);
        tabNumber++;
        /*
         * tab view
         */
        super.addTab("Ansicht");   //view
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
        super.addTab("Drucken");
        super.addToTab(tabNumber, Print.getInstance(tabNumber));
        tabNumber++;


        super.addTab("paintObejcts");
        PaintObjects tab_pos = PaintObjects.getInstance();
        PaintObjects.getInstance().setSize(
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
                ViewSettings.getView_heightTB());
        super.addToTab(tabNumber, tab_pos);
        tabNumber++;
        
        /*
         * tab print
         */
        super.addTab("Projekte");
        tabNumber++;

        /*
         * tab print
         */
        super.addTab("Ziele");
        tabNumber++;

        /*
         * tab print
         */
        super.addTab("Uebersicht");
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
    public final void closeMenues() {
        
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
    public static final Tabs getInstance() {
        
        if (instance == null) {
            instance = new Tabs();
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