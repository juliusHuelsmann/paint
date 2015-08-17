//package declaration
package view.forms;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//import declarations
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import view.tabs.Project;

import model.settings.ViewSettings;
import control.ControlPaint;
import control.util.CItem;
import view.View;
import view.tabs.AboutPaint;
import view.tabs.Export;
import view.tabs.Insert;
import view.tabs.Look;
import view.tabs.Tools;
import view.tabs.Debug;
import view.tabs.Print;
import view.tabs.Selection;
import view.tabs.Write;
import view.tabs.settings.Settings;
import view.tabs.settings.SettingsAbout;
import view.tabs.settings.SettingsExport;
import view.tabs.settings.SettingsInsert;
import view.tabs.settings.SettingsLook;
import view.tabs.settings.SettingsPrint;
import view.tabs.settings.SettingsSelection;
import view.tabs.settings.SettingsTools;
import view.tabs.settings.SettingsWrite;
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
    private Tools tab_paint;
    
    
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
     * Tab for project. 
     */
    private Project tab_project;
    /**
     * Debug tab.
     */
    private Debug tab_pos;
    
    /**
     * Tab which contains the information on the program and the possibilty to
     * update the program.
     */
    private AboutPaint tab_about;
    
    /**
     * Tab for things which can be inserted.
     */
    private Insert tab_insert;
    
    private Settings set_about, set_export, set_insert, set_print, set_tools, set_view, set_write,
    set_selection, set_debug;
    
    /**
     * Empty utility class constructor.
     */
    public Tabs(final View _view) {
    	super(_view);
    }
    
    
    
    /**
     * 
     */
    public void reApplySize(final View _view) {

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

        if (set_about != null) {
        	set_about.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
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
//    	reApplySize();
        super.setOpaque(true);
        super.setVisible(false);
        super.setFocusable(false);

        int tabNumber = 0;
        /*
         * tab paint
         */
        super.addTab("Tools");
        tab_paint =  new Tools(
        		_cp, _cp.getcTabPaint(), _cp,
        		_cp.getcTabPaintStatus());
        super.addToTab(tabNumber, tab_paint);

        set_tools = new SettingsTools();
        set_tools.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_tools);
        
        
        tabNumber++;

        /*
         * 
         */
        super.addTab("Write");
        tab_write = new Write(_cp.getcTabWrite());
        super.addToTab(tabNumber, tab_write);

        set_write = new SettingsWrite();
        set_write.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_write);
        tabNumber++;


        /*
         * tab insert
         */
        super.addTab("Insert");
        tab_insert = new Insert(_cp.getcTabPaintStatus(), _cp);
        super.addToTab(tabNumber, tab_insert);

        set_insert = new SettingsInsert();
        set_insert.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_insert);
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

        set_selection = new SettingsSelection();
        set_selection.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_selection);
        tabNumber++;
        /*
         * tab view
         */
        super.addTab("View");   //view
        tab_look = new Look(_cp.getcTabLook());
        super.addToTab(tabNumber, tab_look);

        set_view = new SettingsLook();
        set_view.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_view);
        tabNumber++;

        /*
         * 
         */
        super.addTab("Export");
        tab_export = new Export();
        tab_export.initialize(_cp.getcTabExport());
        super.addToTab(tabNumber, tab_export);

        set_export = new SettingsExport();
        set_export.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_export);
        tabNumber++;
        


        
        /*
         * tab print
         */
        super.addTab("Print");
        tab_print = new Print(_cp.getcTabPrint());
        super.addToTab(tabNumber, tab_print);
        set_print = new SettingsPrint();
        set_print.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_print);
        tabNumber++;

        
        /*
         * tab project
         */
        super.addTab("Project");
        tab_project = new Project(_cp.getcTabProject());
        super.addToTab(tabNumber, tab_project);
//        set_print = new SettingsPrint();
//        set_print.setSize(
//                _view.getWidth(), 
//                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
//        super.addToTabLayer2(tabNumber, set_print);
        tabNumber++;


        super.addTab("Debug");
        tab_pos = new Debug(_cp.getcTabPaintObjects());
        tab_pos.setSize(
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
                ViewSettings.getView_heightTB());
        super.addToTab(tabNumber, tab_pos);
        set_debug = new SettingsPrint();
        set_debug.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_debug);
        tabNumber++;
        
//        /*
//         * tab print
//         */
//        super.addTab("Projects");
//        tabNumber++;
//
//        /*
//         * tab print
//         */
//        super.addTab("Goals");
//        tabNumber++;
//
//        /*
//         * tab print
//         */
//        super.addTab("Overview");
//        tabNumber++;
        /*
         * tab print
         */
        super.addTab("Paint");
        tab_about = new AboutPaint(_cp.getcTabAbout());
        super.addToTab(tabNumber, new JLabel("hier"));
        tab_about.setSize(
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
                ViewSettings.getView_heightTB());
        
        super.addToTab(tabNumber, tab_about);
        set_about = new SettingsAbout();
        set_about.setSize(
                _view.getWidth(), 
                getVisibleHeightEnitelyOpen() - ViewSettings.getView_heightTB_visible());
        super.addToTabLayer2(tabNumber, set_about);
        
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
     * Initialize the help listeners for components of implementations of tab.
     * @param _jf	the JFrame which contains everything
     * @param _c	the help-form.
     */
    public void initializeHelpListeners(final JFrame _jf, final Help _c) {
    	tab_about.initializeHelpListeners(_jf, _c);
    	tab_export.initializeHelpListeners(_jf, _c);
    	tab_insert.initializeHelpListeners(_jf, _c);
    	tab_look.initializeHelpListeners(_jf, _c);
    	tab_paint.initializeHelpListeners(_jf, _c);
    	tab_pos.initializeHelpListeners(_jf, _c);
    	tab_print.initializeHelpListeners(_jf, _c);
    	tab_selection.initializeHelpListeners(_jf, _c);
    	tab_write.initializeHelpListeners(_jf, _c);
    	
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
    public  Tools getTab_paint() {
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
	public AboutPaint getTab_about() {
		return tab_about;
	}



	/**
	 * @return the tab_project
	 */
	public Project getTab_project() {
		return tab_project;
	}
}
