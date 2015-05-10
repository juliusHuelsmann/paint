//package declaration
package view.util;

//import declarations
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import view.util.mega.MButton;
import view.util.mega.MPanel;
import model.settings.Constants;
import model.settings.Status;
import model.util.paint.Utils;
import control.interfaces.ActivityListener;
import control.util.CScrollPane;

/**
 * ScrollPane.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class VScrollPane extends MPanel {

    /*
     * Components.
     */
    
	/**
	 * The MButtons for moving.
	 */
	private MButton jbtn_toTop, jbtn_toBottom, jbtn_center;
	
	/**
	 * Component which is moved.
	 */
	private Component jpnl_toLocate;
	
	/**
	 * MPanel for owner.
	 */
	private MPanel jpnl_owner;

	/*
	 * Controller
	 */
	
	/**
	 * Controller class.
	 */
	private CScrollPane control;
	
	/*
	 * settings
	 */
	
    /**
     * The size of the icons.
     */
    private int icon_size = (2 + 2) * (2 + 2 + 1);

    /**
     * Whether to scroll vertically or horizontally.
     */
    private boolean verticalScroll = true;
    
    /**
     * The RGB color for border for the buttons.
     */
    private final int rgbBorderHighlight = 204, rgbBorderShadow = 124;

	
	/**
	 * Constructor. Initialize components.
	 * 
	 * @param _jpnl_toLocate the panel of which the location is changed
	 * @param _jpnl_owener its owner
	 * @param _vertical whether vertical or not
	 */
	public VScrollPane(final Component _jpnl_toLocate, 
	        final MPanel _jpnl_owener, final boolean _vertical) {
	    
	    //initialize
		super();
		super.setLayout(null);
		super.setFocusable(true);
        super.setBackground(Color.white);
        super.setOpaque(false);

        //save values
		this.jpnl_toLocate = _jpnl_toLocate;
		this.jpnl_owner = _jpnl_owener;
		this.verticalScroll = _vertical;
		this.control = new CScrollPane(this);

		//add listeners
        jpnl_toLocate.addMouseMotionListener(control);
        super.addKeyListener(control);
		
		//initialize MButtons for up, button and center.
		if (_vertical) {
		    
			jbtn_toTop = initializeMButton(Constants.SP_PATH_UP);
			jbtn_toBottom = initializeMButton(Constants.SP_PATH_DOWN);
			
		} else {

			jbtn_toTop = initializeMButton(Constants.SP_PATH_LEFT);
			jbtn_toBottom = initializeMButton(Constants.SP_PATH_RIGHT);
		}
		jbtn_center = initializeMButton(null);
	}

	
	
	
	/**
	 * Set ActivityListener for the Scroll-Pane view class. If set twice, the
	 * secondly added ActivityListner is used and the first one is forgotten.
	 * If the activityListener is to be removed, call setActivityListener(null).
	 * 
	 * @param _activityListener	The activityListener which is added
	 */
	public final void setActivityListener(
			final ActivityListener _activityListener) {
		control.setActivityListener(_activityListener);
	}
	
	/**
	 * Method for initializing MButton for scrolling.
	 * @param _icon the path of the icon.
	 * @return the MButton 
	 */
	private MButton initializeMButton(final String _icon) {
	    
	    //initialize MButton.
		MButton jbtn = new MButton();
		jbtn.setContentAreaFilled(false);
		jbtn.setSize(icon_size, icon_size);
		jbtn.setOpaque(true);
		jbtn.setBorder(BorderFactory.createEtchedBorder(new Color(
		        rgbBorderHighlight, rgbBorderHighlight, rgbBorderHighlight), 
		        new Color(rgbBorderShadow, 
		                rgbBorderShadow, rgbBorderShadow)));
		jbtn.setFocusable(false);
		jbtn.addMouseMotionListener(control);
		jbtn.addMouseListener(control);
		
		//set icon if up or down button
		//else set background color.
		if (_icon == null) {
			jbtn.setBackground(Color.white);
		} else {
			jbtn.setIcon(new ImageIcon(Utils.resizeImage(
			        jbtn.getWidth(), jbtn.getWidth(), _icon)));
		}
		
		//add and return the MButton
		super.add(jbtn);
		return jbtn;
	}

	
	
	/**
	 * set size of the VScrollPane and its components.
	 * @param _width the width of the scrollPane
	 * @param _height the height of the scrollPane
	 */
	@Override public final void setSize(final int _width, final int _height) {
	    
	    //
		if (verticalScroll) {
			super.setSize(icon_size, _height);
			jbtn_toTop.setSize(icon_size, icon_size);
			jbtn_toTop.setLocation(0, 0);
			jbtn_toBottom.setSize(icon_size, icon_size);
			jbtn_toBottom.setLocation(0, getHeight() - icon_size * 2);
		} else {
			super.setSize(_width, icon_size);
			jbtn_toTop.setSize(icon_size, icon_size);
			jbtn_toTop.setLocation(0, getHeight() - icon_size);
			jbtn_toBottom.setSize(icon_size, icon_size);
			jbtn_toBottom.setLocation(getWidth() - icon_size * 2,
			        getHeight() - icon_size);
		}

//		Util.getScrollStroke(jbtn_toTop);
//		Util.getScrollStroke(jbtn_toBottom);
//		Util.getScrollStroke(jbtn_center);
		recalculateCenterBounds();
	}

    /**
     * if the size of the to - locate component is changed, adapt size saved 
     * inside the ScrollPane and scroll to the end of the panel.
     */
    public final void reload() {

        //scroll down to the end of the ScrollPane
        int y = jpnl_owner.getHeight() - jpnl_toLocate.getHeight();
        jpnl_toLocate.setLocation(jpnl_toLocate.getX(), y);

        //repaint owner and update sizes
        jpnl_owner.repaint();
        recalculateCenterBounds();
    }

	
    /**
     * recalculate the current size of the center - MButton (used if size
     * of one of the components is changed).
     */
    public final void recalculateCenterBounds() {
        
        //if verticalScroll is enabled
        if (verticalScroll) {

            /*
             * set size of the MButton jbtn_center
             */

            //calculate the current relationship between the Component which 
            //is to be located and its owner and then update the size of the
            //center MButton
            float percentage = Constants.MAX_PERCENTAGE 
                    * jpnl_owner.getHeight() / jpnl_toLocate.getHeight();
            jbtn_center.setSize(icon_size, (int) (jpnl_owner.getHeight() 
                    * percentage / Constants.MAX_PERCENTAGE));
            
            /*
             * set location of the MButton jbtn_center
             */
            setCenterLocationVertical();
        } else {

            //calculate the current relationship between the Component which 
            //is to be located and its owner and then update the size of the
            //center MButton
            float percentage = Constants.MAX_PERCENTAGE * jpnl_owner.getWidth() 
                    / jpnl_toLocate.getWidth();
            jbtn_center.setSize((int) (jpnl_owner.getWidth() * percentage 
                    / Constants.MAX_PERCENTAGE), jbtn_center.getHeight());
            
            /*
             * set location of the MButton jbtn_center
             */
            setCenterLocationHorizontal();
        }
//        jpnl_owner.repaint();
    }
    
    
    /**
     * set the location of the center MButton jbtn_center.
     */
    private void setCenterLocationVertical() {

        /*
         * calculate percentages
         */
        
        //calculate the amount of pixel, which correspond to the x 
        //location of jbtn_center / component to locate in case of
        //100 percent scrolled.
        int jbtn_center100percentPX = jbtn_toBottom.getY() 
                - jbtn_toTop.getHeight() - jbtn_center.getHeight();
        int cmp_toLocate100percentPX = jpnl_toLocate.getHeight()
                - jpnl_owner.getHeight();

        
        if (!Status.isNormalRotation()) {

            jbtn_center100percentPX = (-jbtn_center.getHeight() 
                    + jbtn_toTop.getY()) 
                    - (jbtn_toBottom.getHeight() 
                    + jbtn_toBottom.getY());
        }
        
        
        /*
         * set location of the MButton jbtn_center
         */
        
        //if the component is not great enough to be scrolled, make the
        //scrollPane invisible.
        //otherwise calculate the current position of the center MButton
        if (cmp_toLocate100percentPX <= 0) {

            //set visible false
            jbtn_center.setVisible(false);
            jbtn_toBottom.setVisible(false);
            jbtn_toTop.setVisible(false);
        
        } else {

            //set visible true
            jbtn_center.setVisible(true);
            jbtn_toBottom.setVisible(true);
            jbtn_toTop.setVisible(true);

            //calculate the currently real percentage of the jbtn_center
            float percentage = -jpnl_toLocate.getY() * Constants.MAX_PERCENTAGE 
                    / cmp_toLocate100percentPX;

            if (Status.isNormalRotation()) {

                //set location of the MButton jbtn_center
                jbtn_center.setLocation(0, (int) (percentage 
                        * jbtn_center100percentPX / Constants.MAX_PERCENTAGE)
                        + jbtn_toTop.getHeight());
            } else {

                int y = jbtn_toTop.getY() - jbtn_center.getHeight()
                        - (int) (percentage 
                        * jbtn_center100percentPX / Constants.MAX_PERCENTAGE);
                //set location of the MButton jbtn_center
                jbtn_center.setLocation(0, y);
            }
        }
    } 
    
    
    
    /**
     * set the location of the center MButton jbtn_center.
     */
    private void setCenterLocationHorizontal() {

        /*
         * calculate percentages
         */
        
        //calculate the amount of pixel, which correspond to the x 
        //location of jbtn_center / component to locate in case of
        //100 percent scrolled.
        int jbtn_center100percentPX = jbtn_toBottom.getX() 
                - jbtn_toTop.getWidth() - jbtn_center.getWidth();
        int cmp_toLocate100percentPX = jpnl_toLocate.getWidth()
                - jpnl_owner.getWidth();
        

        if (!Status.isNormalRotation()) {

            jbtn_center100percentPX = (-jbtn_center.getWidth() 
                    + jbtn_toTop.getX()) 
                    - (jbtn_toBottom.getWidth() 
                    + jbtn_toBottom.getX());
        }
        /*
         * set location of the MButton jbtn_center
         */
        
        //if the component is not great enough to be scrolled, make the
        //scrollPane invisible.
        //otherwise calculate the current position of the center MButton
        if (cmp_toLocate100percentPX <= 0) {

            //set visible false
            jbtn_center.setVisible(false);
            jbtn_toBottom.setVisible(false);
            jbtn_toTop.setVisible(false);
        
        } else {

            //set visible true
            jbtn_center.setVisible(true);
            jbtn_toBottom.setVisible(true);
            jbtn_toTop.setVisible(true);

            //calculate the currently real percentage of the jbtn_center
            float percentage = -jpnl_toLocate.getX() * Constants.MAX_PERCENTAGE 
                    / cmp_toLocate100percentPX;
            
            if (Status.isNormalRotation()) {

                //set location of the MButton jbtn_center
                jbtn_center.setLocation((int) (percentage 
                        * jbtn_center100percentPX / Constants.MAX_PERCENTAGE)
                        + jbtn_toTop.getWidth(), 0);
                    
            } else {

                int x = jbtn_toTop.getX() - jbtn_center.getWidth()
                        - (int) (percentage * jbtn_center100percentPX 
                                / Constants.MAX_PERCENTAGE);
                //set location of the MButton jbtn_center
                jbtn_center.setLocation(x, 0);
            }
        }
    }
    
	/*
	 * getter and setter methods
	 */

    /**
     * @return the icon_size
     */
    public final int getIcon_size() {
        return icon_size;
    }


    /**
     * @param _icon_size the icon_size to set
     */
    public final void setIcon_size(final int _icon_size) {
        this.icon_size = _icon_size;
    }


    /**
     * @return the jbtn_toTop
     */
    public final MButton getJbtn_toTop() {
        return jbtn_toTop;
    }



    /**
     * @return the jbtn_toBottom
     */
    public final MButton getJbtn_toBottom() {
        return jbtn_toBottom;
    }


    /**
     * @return the jbtn_center
     */
    public final MButton getJbtn_center() {
        return jbtn_center;
    }


    /**
     * @return the verticalScroll
     */
    public final boolean isVerticalScroll() {
        return verticalScroll;
    }


    /**
     * @param _verticalScroll the verticalScroll to set
     */
    public final void setVerticalScroll(final boolean _verticalScroll) {
        this.verticalScroll = _verticalScroll;
    }


    /**
     * @return the jpnl_toLocate
     */
    public final Component getJpnl_toLocate() {
        return jpnl_toLocate;
    }


    /**
     * @return the jpnl_owner
     */
    public final MPanel getJpnl_owner() {
        return jpnl_owner;
    }
}
