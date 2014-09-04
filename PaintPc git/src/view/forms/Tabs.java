//package declaration
package view.forms;

//import declarations
import java.awt.Color;
import java.awt.Graphics;

import settings.ViewSettings;
import view.forms.tabs.Insert;
import view.forms.tabs.Paint;
import view.forms.tabs.Selection;
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
     * Tabs.
     */
    private Paint tab_paint;
    
    /**
     * 
     */
    public Tabs() {
        
        //TabbedPane for different pages
        super();
        super.setSize(
                ViewSettings.VIEW_WIDTH_TB, 
                ViewSettings.VIEW_HEIGHT_TB,
                ViewSettings.VIEW_HEIGHT_TB_VISIBLE);
        super.setOpaque(true);
        super.setFocusable(false);
        super.setVisible(false);

        /*
         * tab paint
         */
        super.addTab("zeichnen");
        tab_paint = Paint.getInstance();
        super.addToTab(0, tab_paint);

        /*
         * tab insert
         */
        super.addTab("einfuegen");
        Insert tab_insert = new Insert(ViewSettings.VIEW_HEIGHT_TB);
        super.addToTab(1, tab_insert);
        

        /*
         * 
         */
        super.addTab("auswahl");
        Selection tab_selection = new Selection(
                ViewSettings.VIEW_HEIGHT_TB);
        super.addToTab(2, tab_selection);

        /*
         * tab view
         */
        super.addTab("Ansicht");   //view

        /*
         * tab print
         */
        super.addTab("Drucken");
        

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
