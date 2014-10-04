//package declaration
package view.forms;

//import declarations
import java.awt.Toolkit;

import model.settings.ViewSettings;
import control.util.CItem;
import view.forms.tabs.Insert;
import view.forms.tabs.Outlook;
import view.forms.tabs.Paint;
import view.forms.tabs.PaintObjects;
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
                ViewSettings.VIEW_WIDTH_TB, 
                ViewSettings.VIEW_HEIGHT_TB,
                ViewSettings.VIEW_HEIGHT_TB_VISIBLE);
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
        Selection tab_selection = Selection.getInstance(
                ViewSettings.VIEW_HEIGHT_TB);
        super.addToTab(tabNumber, tab_selection);
        tabNumber++;
        /*
         * tab view
         */
        super.addTab("Ansicht");   //view
        super.addToTab(tabNumber, Outlook.getInstance());
        tabNumber++;

        /*
         * 
         */
        super.addTab("Export");
        tabNumber++;
        


        
        /*
         * tab print
         */
        super.addTab("Drucken");
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



        super.addTab("paintObejcts");
        PaintObjects tab_pos = PaintObjects.getInstance();
        PaintObjects.getInstance().setSize(
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
                ViewSettings.VIEW_HEIGHT_TB);
        super.addToTab(tabNumber, tab_pos);
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
        tabNumber++;

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
        tab_insert.getIt_pfeileUAE().setOpen(false);
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
