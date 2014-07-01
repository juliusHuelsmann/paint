//package declaration
package view.util;

//import declarations
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.util.CTabbedPane;
import settings.Error;
import settings.Status;
import settings.ViewSettings;


/**
 * The tabbedPanel.
 * 
 * @author Julius Huelsmann
 * @version %I% %U%
 */
@SuppressWarnings("serial")
public class VTabbedPane extends JPanel {

	/**
	 * array of headline JButtons.
	 */
	private JButton [] jbtn_stuffHeadline;
	
	/**
	 * array of Panels for each JButton.
	 */
	private JPanel  [] jpnl_stuff;
	
	/**
	 * JLabel for the background.
	 */
	private JLabel jpnl_background;
	
	/**
	 * JPanel for the content 
	 *     JButton for title
	 *     JPanel for content of tab.
	 */
	private JPanel jpnl_contains;
	
	/*
	 * settings
	 */
	
	/**
	 * The bounds of the title JButton and location of the Tab JPanel.
	 */
	private int titleX = 1, titleY = 1;
	
	/**
	 * The size of the title JButton getSize() / the size of title JButton
	 * is the real size.
	 */
	private final int titleProportionWidth = 15,
	        titleProportionHeight = 35;
	
	/**
	 * The visible height.
	 */
	private int visibleHeight = 0;
	
	/**
	 * The currently open tab.
	 */
	private int openTab = 0;
	
	/**
	 * The controller class.
	 */
	private CTabbedPane control;
	
	/**
	 * Constructor initialize view and corresponding controller class..
	 */
	public VTabbedPane() {
		
		//initialize JPanel and alter settings
		super();
		super.setFocusable(false);
		super.setLayout(null);
		super.setBackground(new Color(0, 0, 0, 0));
		super.setOpaque(false);

		//initialize the container for the title JButton and the tab 
		//JPanels
		jpnl_contains = new JPanel();
		jpnl_contains.setFocusable(false);
		jpnl_contains.setOpaque(false);
		jpnl_contains.setLayout(null);
		super.add(jpnl_contains);

		//initialize the Background JLabel
		jpnl_background = new JLabel();
		jpnl_background.setFocusable(false);
		jpnl_background.setOpaque(true);
		jpnl_background.setLayout(null);
        jpnl_background.setBackground(ViewSettings.CLR_BACKGROUND_DARK);
        jpnl_background.setBorder(BorderFactory.createMatteBorder(
                1, 0, 1, 1, ViewSettings.CLR_BORDER));
		super.add(jpnl_background);
		
		//initialize controller class
		control = new CTabbedPane(this);
	}
	

	/**
	 * add a new tab and its title to tabbedPane.
	 * @param _title the title of the new tab.
	 */
	public final void addTab(final String _title) {

		//if the JPanel is not initialized yet.
		if (jpnl_stuff == null) {
			
		    //create arrays for content JPanel and headline JButton
			jpnl_stuff = new JPanel[1];
			jbtn_stuffHeadline = new JButton[1];

			//initialize title button and content JPanel
			jbtn_stuffHeadline[0] = initJbtn_title(0, _title);
			jpnl_stuff[0] = initJpnl_tab();
			
		} else {
			
		    //create new arrays
			JPanel [] jpnl_stuff2 = new JPanel [jpnl_stuff.length + 1];
			JButton[] jbtn_stuff2 = new JButton[jpnl_stuff.length + 1];

			//save old JPanels
			for (int i = 0; i < jpnl_stuff.length; i++) {
				jpnl_stuff2[i] = jpnl_stuff[i];
				jbtn_stuff2[i] = jbtn_stuffHeadline[i];
				jpnl_stuff2[i].setVisible(false);
			}
			
			//initialize the new JPanels.
			jbtn_stuff2[jpnl_stuff.length] = initJbtn_title(
			        jpnl_stuff.length, _title);
			jpnl_stuff2[jpnl_stuff.length] = initJpnl_tab();
			
			//save the new created JPanel and JButton arrays.
			jpnl_stuff = jpnl_stuff2;
			jbtn_stuffHeadline = jbtn_stuff2;
		}
		
		//open the first tab by default and repaint the TabbedPane
		openTab(openTab);
		flip(true);
	}
	
	
	/**
     * add component at special tab index.
     * 
     * @param _index the tab index.
     * @param _c the component which is added.
     * 
     * @return the added component
     */
    public final Component addToTab(final int _index, final Component _c) {
    	
    
           //if the JPanel for the stuff does not exist yet, not a single
        //tab has been added before and it is thus impossible to add
        //a component to a special tab.
        if (jpnl_stuff == null) {
            
            //print error message
            Error.printError(getClass().getSimpleName(), "add(...)", 
                    "add an item to a not existing JPanel (no item added yet)", 
                    new Exception("no item added yet."), 
                    Error.ERROR_MESSAGE_INTERRUPT);
    
            //return null
            return null;
    		
    	} else if (_index < 0 || _index >= jpnl_stuff.length) {
    
            //print error message
            Error.printError(getClass().getSimpleName(), "add(...)", 
                    "add an item to a not existing JPanel"
                            + "(index out of range)",
                            new Exception("index out of range."), 
                            Error.ERROR_MESSAGE_INTERRUPT);
    
            //return null
            return null;
            
    	} else if (_c == null) {
    
            //print error message
    	    Error.printError(getClass().getSimpleName(), "add(...)", 
    	            "add an item to a not existing JPanel" 
    	                    + "(Component to add is null)", 
    	                    new Exception("Component to add is null"),
    	                    Error.ERROR_MESSAGE_INTERRUPT);
    
            //return null
            return null;
    	} else {
    		_c.setFocusable(false);
    		return jpnl_stuff[_index].add(_c);
    	}
    }


    /**
	 * initialize a new headline for JButton which is added to TabbedPane.
	 * 
	 * @param _index the current index of tab
	 * @param _text the headline
	 * @return the JButton.
	 */
	private JButton initJbtn_title(final int _index, final String _text) {
	
		VButtonWrapper jbtn = new VButtonWrapper(_index);
		jbtn.setVisible(true);
		jbtn.setBackground(Color.white);
		jbtn.setBorder(null);
        jbtn.addActionListener(control);
        jbtn.addMouseListener(control);
		jbtn.setFocusable(false);
		jbtn.setText(_text);
		jbtn.setContentAreaFilled(false);
		jbtn.setOpaque(true);
		jpnl_contains.add(jbtn);
		return jbtn;
	}
	
	
	/**
	 * initialize a new JPanel for the tab.
	 * @return the ready JPanel.
	 */
	private JPanel initJpnl_tab() {

		JPanel jpnl = new JPanel();
		jpnl.setVisible(false);
		jpnl.setLayout(null);
		jpnl.setFocusable(false);
		jpnl.setOpaque(false);
		jpnl_contains.add(jpnl);
		return jpnl;
	}

	
	/**
	 * open a special tab and highlight is headline.
	 * 
	 * @param _index the index which is to be highlighted
	 */
	public final void openTab(final int _index) {
	    
	    //save currently open tab
	    openTab = _index;
	    
	    //go through the list of headline JButtons and tab JPanels
		for (int i = 0; i < jbtn_stuffHeadline.length; i++) {

		    //set the tab invisible
			jpnl_stuff[i].setVisible(false);
			
			//set the standard background and a border at the bottom for 
			//each not selected panel and button
            jbtn_stuffHeadline[i].setBackground(Color.white);
			jbtn_stuffHeadline[i].setBorder(BorderFactory.createMatteBorder(
			        0, 0, 1, 0, ViewSettings.CLR_BORDER));
		}
		
		//set the selected background for the currently selected tab
		//and create a border everywhere except at the bottom
		jbtn_stuffHeadline[_index].setBackground(
		        ViewSettings.CLR_BACKGROUND_DARK);
        jbtn_stuffHeadline[_index].setBorder(BorderFactory.createMatteBorder(
                1, 1, 0, 1, ViewSettings.CLR_BORDER));
		
		//make the tab JPanel visible
		jpnl_stuff[_index].setVisible(true);
		
		//set the size of open tab
		flip(Status.isNormalRotation());
	}
	
	/**
     * set the size of the TabbedPane and its components by flip method.
     * 
     * @param _width the width
     * @param _height the height
     * @param _visibleHeight the visible height.
     */
    public final void setSize(final int _width, final int _height, 
            final int _visibleHeight) {
        
        //set total size
    	super.setSize(_width, _height);
    	
    	//save the visible height
    	this.visibleHeight = _visibleHeight;
    	
    	//set the size and location of the components depending on 
    	//the current rotation
    	flip(Status.isNormalRotation());
    }


    /**
     * set size and location of contents depending on the current
     * rotation given in parameter.
     * 
     * @param _normalRotation whether normal rotation or not
     */
    public final void flip(final boolean _normalRotation) {

        int titleWidth = getWidth() / titleProportionWidth;
        int selectedtitleWidth = getWidth() / (titleProportionWidth);
        int titleHeight = getHeight() / titleProportionHeight;
        
        //set the size of container JPanel (whole width and height)
        jpnl_contains.setSize(getWidth(), getHeight());
        
        //set background size (only visible width and height shown)
        jpnl_background.setSize(getWidth(), visibleHeight);
        
        //if is normal rotated.
	    if (_normalRotation) {
	        
	        //because the border should be visible 
            jpnl_background.setLocation(0, titleHeight + titleY - 1);
            jpnl_contains.setLocation(0, 0);

            //set size and location of headlines and tabs.
	        for (int index = 0; jbtn_stuffHeadline != null 
	                && index < jbtn_stuffHeadline.length; index++) {
	            
	            //set sizes. if highlighted, the headline is greater than
	            //usually.
	            if (index == openTab) {

	                jbtn_stuffHeadline[index].setSize(
	                        selectedtitleWidth, titleHeight);
	            } else {

	                jbtn_stuffHeadline[index].setSize(titleWidth, titleHeight);
	            }
                jpnl_stuff[index].setSize(
                        getWidth(), getHeight() - titleHeight - titleY);
	            
                //set locations depending on previous locations.
                //if index == 0 there is no previous location, it has to be
                //addressed in particular
                if (index == 0) {
                   jbtn_stuffHeadline[0].setLocation(titleX, titleY); 
                } else {
                    jbtn_stuffHeadline[index].setLocation(
                            jbtn_stuffHeadline[index - 1].getX() 
                            + jbtn_stuffHeadline[index - 1].getWidth(),
                            titleY);
                }
	            jpnl_stuff[index].setLocation(0, titleHeight + titleY);
	        }
	    } else {

	        jpnl_background.setLocation(0, getHeight() 
	                - (titleHeight + titleY - 1 + jpnl_background.getHeight()));
	    }
	}


    /**
     * @return the openTab
     */
    public final int getOpenTab() {
        return openTab;
    }
}