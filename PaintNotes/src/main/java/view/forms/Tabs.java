//package declaration
package view.forms;

//import declarations
import java.awt.Toolkit;

import model.settings.ViewSettings;
import control.ControlPaint;
import control.util.CItem;
import view.View;
import view.tabs.About;
import view.tabs.Export;
import view.tabs.Insert;
import view.tabs.Look;
import view.tabs.Paint;
import view.tabs.Debug;
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
    
    
    /**
     * Tab which contains the print items.
     */
    private Print tab_print;
    
    
    /**
     * Tab which contains writing presettings.
     */
    private Write tab_write;
    
    /**
     * Tab for changing the current selection in some way.
     */
    private Selection tab_selection;
    
    /**
     * Export tab. In here, the export settings of the page can be set.
     * Does not change the way the page is displayed, that is done in view tab.
     */
    private Export tab_export;
    /**
     * Tab in which the user is able to change the layout of the page as it is
     * displayed. Does not change the export settings!
     */
    private Look tab_look;
    
    /**
     * Debug tab.
     */
    private Debug tab_pos;
    
    /**
     * Tab which contains the information on the program and the possibilty to
     * update the program.
     */
    private About tab_about;
    
    /**
     * Tab for things which can be inserted.
     */
    private Insert tab_insert;
    
    /**
     * Empty utility class constructor.
     */
    public Tabs(final View _view) {
    	super(_view);
    }
    
    
    
    /**
     * 
     */
    public void reApplySize() {

        super.setSize(
                ViewSettings.getView_widthTb(), 
                ViewSettings.getView_heightTB(),
                ViewSettings.getView_heightTB_visible());
        
        if (tab_paint != null) {
            tab_paint.applySize();
        }
        
        if (tab_print != null) {
        	tab_print.applySize();
        }
        if (tab_write != null) {
        	tab_write.applySize();
        }
        
        if (tab_selection != null) {
        	tab_selection.applySize();
        }
        
        if (tab_export != null) {
        	tab_export.applySize();
        }
        
        if (tab_look != null) {
        	tab_look.applySize();
        }
        
        
        if (tab_pos != null) {
        	tab_pos.applySize();
        }
        
        if (tab_about != null) {
        	tab_about.applySize();
        }
        
        if (tab_insert != null) {
        	tab_insert.applySize();
        }
    }
    
    
    /**
     * Initialize the contents of the Tab.
     * @param _view	the main view class
     * @param _cp	the main controller class
     */
    public void initialize(
    		final View _view, final ControlPaint _cp) {
    	super.setTabbedListener(_cp.getcTabs());


        
        //TabbedPane for different pages
    	reApplySize();
        super.setOpaque(true);
        super.setVisible(false);
        super.setFocusable(false);

        int tabNumber = 0;
        /*
         * tab paint
         */
        super.addTab("Painting");
        tab_paint =  new Paint(
        		_cp, _cp.getcTabPaint(), _cp,
        		_cp.getcTabPaintStatus());
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
        tab_insert = new Insert(_cp.getcTabPaintStatus(), _cp);
        super.addToTab(tabNumber, tab_insert);
        tabNumber++;

        
        /*
         * 
         */
        super.addTab("Selection");
        tab_selection = new Selection();
        tab_selection.initialize(
        		_cp.getcTabPaint(), _cp.getcTabSelection(), _cp, 
        		_cp.getcTabPaintStatus());
        super.addToTab(tabNumber, tab_selection);
        tabNumber++;
        /*
         * tab view
         */
        super.addTab("View");   //view
        tab_look = new Look(_cp.getcTabLook());
        super.addToTab(tabNumber, tab_look);
        tabNumber++;

        /*
         * 
         */
        super.addTab("Export");
        tab_export = new Export();
        tab_export.initialize(_cp.getcTabExport());
        super.addToTab(tabNumber, tab_export);
        tabNumber++;
        


        
        /*
         * tab print
         */
        super.addTab("Printing");
        tab_print = new Print(_cp.getcTabPrint());
        super.addToTab(tabNumber, tab_print);
        tabNumber++;


        super.addTab("Debug");
        tab_pos = new Debug(_cp.getcTabPaintObjects());
        tab_pos.setSize(
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
        /*
         * tab print
         */
        super.addTab("About");
        tab_about = new About(_cp.getcTabAbout());
        tab_about.setSize(
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
                ViewSettings.getView_heightTB());
        super.addToTab(tabNumber, tab_about);
        
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
        tab_paint.getTb_erase().setOpen(false);

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
    			||  tab_paint.getTb_erase().isOpen()
    			|| tab_insert.getIa_diagram().isOpen() 
    			|| tab_insert.getIa_geo().isOpen() 
    			|| tab_insert.getIa_maths().isOpen());
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
	 * @return the tab_write
	 */
	public Write getTab_write() {
		return tab_write;
	}


	/**
	 * @return the tab_selection
	 */
	public Selection getTab_selection() {
		return tab_selection;
	}


	/**
	 * @return the tab_export
	 */
	public Export getTab_export() {
		return tab_export;
	}



	/**
	 * @return the tab_look
	 */
	public Look getTab_look() {
		return tab_look;
	}


	/**
	 * @return the tab_pos
	 */
	public Debug getTab_debug() {
		return tab_pos;
	}


	/**
	 * @return the tab_insert
	 */
	public Insert getTab_insert() {
		return tab_insert;
	}

	/**
	 * @return the tab_about
	 */
	public About getTab_about() {
		return tab_about;
	}
}
