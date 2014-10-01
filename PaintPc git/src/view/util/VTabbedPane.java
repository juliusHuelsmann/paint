//package declaration
package view.util;

//import declarations
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.settings.Error;
import model.settings.Status;
import model.settings.ViewSettings;
import control.util.CTabbedPane;
import view.forms.Page;


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
	 * JButton for closing the tabbedPane.
	 */
	private JLabel jlbl_close;
	
	/**
	 * array of Panels for each JButton.
	 */
	private JPanel  [] jpnl_stuff;
	
	/**
	 * JLabel for the background.
	 */
	private JLabel jpnl_background, jlbl_whiteBackground;
	
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
	        titleProportionHeight = 20;
	
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
	 * 
	 */
	private int oldHeight = 0, oldVisibleHeight = 0;

	/**
	 * Whether pressed or not.
	 */
	private boolean press = false;
	
	/**
	 * The closing JPanel.
	 */
	private JPanel jpnl_close;
	
	/**
	 * JLabel containing the time, date and the working time.
	 */
	private JLabel jlbl_closeTime;
	
	/**
	 * Constructor initialize view and corresponding controller class..
	 */
	public VTabbedPane() {
		
		//initialize JPanel and alter settings
		super();
		super.setFocusable(false);
		super.setLayout(null);
		super.setOpaque(false);
		
		//initialize the container for the title JButton and the tab  JPanels
		jpnl_contains = new JPanel();
		jpnl_contains.setFocusable(false);
		jpnl_contains.setOpaque(true);
		jpnl_contains.setBackground(new Color(0, 0, 0, 0));
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
        jpnl_contains.add(jpnl_background);

        jpnl_close = new JPanel();
        jpnl_close.setFocusable(false);
        jpnl_close.setOpaque(true);
        jpnl_close.setBackground(Color.white);
        jpnl_close.setLayout(null);
        super.add(jpnl_close);
        
        jlbl_closeTime = new JLabel("Sonntag, 2014 09 14\t 20:17:40");
        jlbl_closeTime.setForeground(Color.black);
        jlbl_closeTime.setOpaque(false);
        jlbl_closeTime.setFocusable(false);
        jlbl_closeTime.setFont(new Font("Courier new",
                Font.ITALIC, (2 + 2 + 2) * (2)));
        jlbl_closeTime.setVisible(false);
        jpnl_close.add(jlbl_closeTime);
        
        jlbl_close = new JLabel();
        jlbl_close.setFocusable(false);
        jlbl_close.setOpaque(false);
        jlbl_close.addMouseMotionListener(new MouseMotionListener() {
            
            @Override public void mouseMoved(final MouseEvent _event) { }
            @Override public void mouseDragged(final MouseEvent _event) {
                moveTab(_event);
            }
        });
        jlbl_close.addMouseListener(new MouseListener() {
            
            @Override public void mouseReleased(final MouseEvent _event) {
                press = false;
                if (getHeight() <= (oldVisibleHeight + (oldVisibleHeight 
                        / titleProportionHeight)) / 2) {
                    closeTabbedPane();
                } else {
                    openTabbedPane();
                }
            }
            @Override public void mousePressed(final MouseEvent _event) {
                press = true;
            }
            @Override public void mouseExited(final MouseEvent _event)  { }
            @Override public void mouseEntered(final MouseEvent _event) { }
            @Override public void mouseClicked(final MouseEvent _event) { }
        });
        jlbl_close.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, Color.black));
        jlbl_close.setFocusable(false);
        jpnl_close.add(jlbl_close);

        //background at the top where the buttons can be found.
        jlbl_whiteBackground = new JLabel();
        jlbl_whiteBackground.setBackground(Color.white);
        jlbl_whiteBackground.setOpaque(true);
        jlbl_whiteBackground.setFocusable(false);
        super.add(jlbl_whiteBackground);
        
		//initialize controller class
		control = new CTabbedPane(this);
		startTimeThread();
		
		super.setVisible(false);
	}
	
	
	@Override public final void setVisible(final boolean _b) {
	    
	    if (_b) {
	        openTab(openTab);
	    }
	    
	    super.setVisible(_b);
	}
	
	/**
	 * Initialize the time log thread.
	 */
	private void startTimeThread() {

        new Thread() {
            public void run() {
                
                double start = System.currentTimeMillis();
                while (true) {
                    try {
                        Thread.sleep(2 * (2 + 2 + 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DateFormat dateFormat = new SimpleDateFormat(
                            "yyyy MM dd   HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    
                    String wochentag = "";
                    switch(cal.get(Calendar.DAY_OF_WEEK)) {
                    case 0:
                        wochentag = "Samstag";
                        break;
                    case 1:
                        wochentag = "Sonntag";
                        break;
                    case 2:
                        wochentag = "Montag";
                        break;
                    case 2 + 1:
                        wochentag = "Dienstag";
                        break;
                    case 2 + 2:
                        wochentag = "Mittwoch";
                        break;
                    case 2 + 2 + 1:
                        wochentag = "Donnerstag";
                        break;
                    case (2 + 1) * 2:
                        wochentag = "Freitag";
                        break;
                    case (2 + 1) * 2 + 1:
                        wochentag = "Samstag";
                        break;
                    default:
                        wochentag = "Tolltag";
                        break;
                    }
                    
                    double currentTime = System.currentTimeMillis() - start;
                    String bearbeitung = "Bearbeitungszeit: ";

                    final int max_min = 60, max_h = 60, max_d = 24, 
                            max_s = 1000;
                    
                    int sekunden = (int) (currentTime / max_s);
                    int minuten = (int) (sekunden / max_min);
                    int stunden = (int) (minuten / max_h);
                    int tage = stunden / max_d;
                    
                    sekunden = sekunden % max_min;
                    minuten = minuten % max_h;
                    stunden = stunden % max_h;

                    if (tage != 0) {
                        bearbeitung += tage + " Tage, " + stunden + " h, "
                                + minuten + " m, " + sekunden + "s";
                    } else if (stunden != 0) {

                        bearbeitung +=  stunden + " h, "
                                + minuten + " m, " + sekunden + "s";
                    } else if (minuten != 0) {

                        bearbeitung += minuten + " m, " + sekunden + "s";
                    } else if (sekunden != 0) {

                        bearbeitung +=  sekunden + "s";
                    }

                    jlbl_closeTime.setText(wochentag 
                            + "   " + dateFormat.format(cal.getTime())
                            + "   " + "" + bearbeitung);
                }
            }
        } .start();
	}
	
	
	/**
	 * MOVE THE TAB.
	 * @param _e the event.
	 */
	private void moveTab(final MouseEvent _e) {

        jpnl_contains.setVisible(true);
        if (press) {
            super.setSize(getWidth(), _e.getYOnScreen()); 
            jpnl_close.setLocation(0, getHeight() - jlbl_close.getHeight());
            jpnl_background.setSize(getWidth(), getHeight());
            Page.getInstance().setLocation(0, getHeight());
            setComponentZOrder(jpnl_close, 0);
            jlbl_closeTime.setVisible(false);
        } 
	}
	
	
	/**
	 * only super size setting method caller from outside the class.
	 * @param _width the width 
	 * @param _height the height.
	 */
	private void setComponentSize(final int _width, final int _height) {
	    super.setSize(_width, _height);
	}
	
	
	/**
	 * close the tab.
	 */
	private void closeTabbedPane() {
	    
	    jpnl_contains.setVisible(false);
	    Thread t_closeTab = new Thread() {
	        public void run() {

	            int startHeight = getHeight();
	            final int max = 20;
	            for (int i = 0; i < max; i++) {

	                setComponentSize(getWidth(), startHeight
	                        + (oldHeight / titleProportionHeight - startHeight)
	                        * i / max);
	                jpnl_close.setLocation(0, getHeight());
	                jpnl_background.setSize(getWidth(), getHeight()
	                        - jpnl_background.getY());
	                Page.getInstance().setLocation(0, getHeight());
	                
	                try {
	                    Thread.sleep(2);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                
	            }

	            setComponentSize(getWidth(), oldHeight / titleProportionHeight);
	            jpnl_close.setLocation(0, getHeight() - jlbl_close.getHeight());
	            jpnl_background.setSize(getWidth(), getHeight()
                        - jpnl_background.getY());
	            Page.getInstance().setLocation(0, getHeight());

	            setComponentZOrder(jpnl_close, 1);
	            jlbl_closeTime.setVisible(true);
	            
	        }
	    };
	    t_closeTab.start();
	    
	}
	
	/**
	 * open the tab.
	 */
	private void openTabbedPane() {

        jpnl_contains.setVisible(true);
        Thread t_closeTab = new Thread() {
            public void run() {

                int startHeight = getHeight();
                int startHeight2 = jpnl_close.getY();
                final int max = 20;
                for (int i = 0; i < max; i++) {

                    setComponentSize(getWidth(), startHeight
                            + (oldHeight - startHeight)
                            * i / max);
                    jpnl_close.setLocation(0, startHeight2 
                            + (oldVisibleHeight - startHeight2) * i / max);
                    
                    jlbl_closeTime.setBounds(0 , 2, jpnl_close.getWidth() 
                            / 2, jpnl_close.getHeight());
                    
                    jpnl_background.setSize(getWidth(), 
                            startHeight2 + (oldVisibleHeight - startHeight2) 
                            * i / max
                            - jpnl_background.getY());
                    Page.getInstance().setLocation(0, startHeight2 
                            + (oldVisibleHeight - startHeight2) * i / max);
                    
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                setComponentSize(getWidth(), oldHeight);
                jpnl_close.setLocation(0, 
                        oldVisibleHeight);
                jpnl_background.setSize(getWidth(), oldVisibleHeight 
                        - jpnl_background.getY());
                jpnl_background.repaint();
                Page.getInstance().setLocation(0, oldVisibleHeight 
                        + jpnl_close.getHeight());
                setComponentZOrder(jpnl_close, 1);
            }
        };
        t_closeTab.start();
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
		
		//set position of background to last painted
        jpnl_contains.setComponentZOrder(
                jpnl_background, jpnl_stuff.length - 1);
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
		jpnl.setVisible(true);
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
	    
	    final boolean newOpening = true;
	    
	    if (newOpening) {
	        
	        //the last tab
	        final int lastTab = openTab;
            
            //save currently open tab
            openTab = _index;

            //go through the list of headline JButtons and tab JPanels
            for (int i = 0; i < jbtn_stuffHeadline.length; i++) {

                
                //set the standard background and a border at the bottom for 
                //each not selected panel and button
                jbtn_stuffHeadline[i].setBackground(Color.white);
                jbtn_stuffHeadline[i].setBorder(BorderFactory.createMatteBorder(
                        0, 0, 1, 0, ViewSettings.CLR_BORDER));

                //make the tab JPanel visible
                jpnl_stuff[i].setLocation(getWidth() * (i - lastTab), 
                        jpnl_stuff[i].getY());
                jpnl_stuff[i].setVisible(true);
            }
            
            //set the selected background for the currently selected tab
            //and create a border everywhere except at the bottom
            jbtn_stuffHeadline[_index].setBackground(
                    ViewSettings.CLR_BACKGROUND_DARK);
            jbtn_stuffHeadline[_index].setBorder(
                    BorderFactory.createMatteBorder(
                    1, 1, 0, 1, ViewSettings.CLR_BORDER));
            jpnl_contains.setComponentZOrder(jbtn_stuffHeadline[_index], 1);

          super.setSize(getWidth(), visibleHeight);
            new Thread() {
                public void run() {

                    final int max = 150;
                    
                    //go through the list of headline JButtons and tab 
                    //JPanels
                    for (int percent = 0; percent <= max; percent++) {
                        for (int i = 0; i < jbtn_stuffHeadline.length; i++) {
                            
                            int cStartLocation = getWidth() * (i - lastTab);
                            int cEndLocation = getWidth() * (i - _index);
                            
                            //make the tab JPanel visible
                            jpnl_stuff[i].setLocation(cStartLocation
                                    + (cEndLocation - cStartLocation)
                                    * percent / max,
                                    jpnl_stuff[i].getY());
                            jpnl_stuff[openTab].repaint();
                        }
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            setSize(getWidth(), oldHeight);
                        }
                    }
                    for (int i = 0; i < jbtn_stuffHeadline.length; i++) {
                        int cStartLocation = getWidth() * (i - lastTab);
                        int cEndLocation = getWidth() * (i - _index);

                        jpnl_stuff[i].setLocation(cStartLocation
                                + cEndLocation - cStartLocation,
                                jpnl_stuff[i].getY());
                    }
                    setSize(getWidth(), oldHeight);
                    jpnl_stuff[openTab].repaint();
                    
                }
            } .start();

	    } else {

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
            jbtn_stuffHeadline[_index].setBorder(
                    BorderFactory.createMatteBorder(
                    1, 1, 0, 1, ViewSettings.CLR_BORDER));
            
            //make the tab JPanel visible
            jpnl_stuff[_index].setVisible(true);
            
            //set the size of open tab
            flip(Status.isNormalRotation());
	    }
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

        //save the height  
        this.oldHeight = _height;
        this.oldVisibleHeight = _visibleHeight;
        
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

        final int titleWidth = getWidth() / titleProportionWidth,
                selectedtitleWidth = getWidth() / (titleProportionWidth),
                titleHeight = getHeight() / titleProportionHeight;
        
        //set the size of container JPanel (whole width and height)
        jpnl_contains.setSize(getWidth(), getHeight());
        
        //set background size (only visible width and height shown)
        jpnl_background.setSize(getWidth(), visibleHeight 
                - titleHeight - titleY + 1);

        jlbl_whiteBackground.setSize(getWidth(), visibleHeight);
        final int height = 25;
        
        //if is normal rotated.
	    if (_normalRotation) {
	        
	        //because the border should be visible 
            jpnl_background.setLocation(0, titleHeight + titleY - 1);
            jpnl_contains.setLocation(0, 0);
            jlbl_close.setSize(getWidth(), height);
            jpnl_close.setBounds(0, visibleHeight, getWidth(), height);
            

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