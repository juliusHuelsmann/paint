//package declaration
package view.forms;

//import declarations
import java.awt.Toolkit;

import model.settings.ViewSettings;
import control.ControlPaint;
import control.util.CItem;
import view.View;
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
     * Tab for general painting stuff like pen, colors etc.
     */
    private Paint tab_paint;
    
    private Print tab_print;
    
    private Write tab_write;
    
    private Selection tab_selection;
    
    private static Tabs instance;
    /**
     * Tab for things which can be inserted.
     */
    private Insert tab_insert;
    
    /**
     * Empty utility class constructor.
     */
    public Tabs(View _view, ControlPaint _cp) {
    	super(_view);
    	addTabbedListener(_cp.getcTabs());
    	instance = this;


        
        //TabbedPane for different pages
        super.setSize(
                ViewSettings.getView_widthTb(), 
                ViewSettings.getView_heightTB(),
                ViewSettings.getView_heightTB_visible());
        super.setOpaque(true);
        super.setVisible(false);
        super.setFocusable(false);

        int tabNumber = 0;
        /*
         * tab paint
         */
        super.addTab("Painting");
        tab_paint =  new Paint(_cp.getcTabPaint());
        super.addToTab(tabNumber, tab_paint);
        tabNumber++;

        /*
         * 
         */
        super.addTab("Writing");
        tab_write = new Write(_cp.getcTabWrite());
        super.addToTab(tabNumber, tab_write);
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
        tab_selection = new Selection(_cp.getcTabPaint());
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
        tab_print = new Print(_cp.getcTabPrint());
        super.addToTab(tabNumber, tab_print);
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


        super.setVisible(true);
    
    
    
    
    }
    
    
    /**
     * 
     * pseudo- constructor: adds instances of tabs to tabbedPane.
     */
    
    
    
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
     * Checks whether a menu is open.
     * @return whether a menu is open.
     */
    public boolean isMenuOpen() {
        
    	return (tab_paint.getIt_stift1().isOpen() 
    			|| tab_paint.getIt_stift1().isOpen() 
    			|| tab_paint.getIt_stift2().isOpen() 
    			|| tab_paint.getIt_selection().isOpen() 
    			|| tab_paint.getIt_color().isOpen() 
    			|| tab_insert.getIa_diagram().isOpen() 
    			|| tab_insert.getIa_geo().isOpen() 
    			|| tab_insert.getIa_maths().isOpen());
    }
    
    
    public static final Tabs getInstance() {

    		return instance;
    }

    /**
     * @return the tab_paint
     */
    public  Paint getTab_paint() {
        return tab_paint;
    }


	/**
	 * @return the tab_print
	 */
	public Print getTab_print() {
		return tab_print;
	}


	/**
	 * @param tab_print the tab_print to set
	 */
	public void setTab_print(Print tab_print) {
		this.tab_print = tab_print;
	}


	/**
	 * @return the tab_write
	 */
	public Write getTab_write() {
		return tab_write;
	}


	/**
	 * @param tab_write the tab_write to set
	 */
	public void setTab_write(Write tab_write) {
		this.tab_write = tab_write;
	}


	/**
	 * @return the tab_selection
	 */
	public Selection getTab_selection() {
		return tab_selection;
	}


	/**
	 * @param tab_selection the tab_selection to set
	 */
	public void setTab_selection(Selection tab_selection) {
		this.tab_selection = tab_selection;
	}
}
