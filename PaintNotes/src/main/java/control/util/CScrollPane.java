//package declaration
package control.util;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import control.interfaces.ActivityListener;
import model.settings.Constants;
import model.settings.Error;
import model.settings.State;
import model.util.paint.Utils;
import view.util.VScrollPane;


/**
 * Controller of ScrollPane.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class CScrollPane 
implements MouseMotionListener, MouseListener, KeyListener {

	
	/**
	 * The activity listener which can be added to the CScrollPane.
	 */
	private ActivityListener activityListener;
	
	
	/**
	 * Set the activityListener.
	 * @param _activityListener the activityListener
	 */
	public final void setActivityListener(
			final ActivityListener _activityListener) {
		this.activityListener = _activityListener;
	}
    /**
     * The view class.
     */
    private VScrollPane view;

    /*
     * interaction values
     */
    
    /**
     * The points where the dragging started, and the starting location
     * of the center JButton.
     */
    private Point pnt_dragStartOnScreen, pnt_centerStartLocation;
    

    /**
     * if the up or down button is pressed in this moment /
     * if a key is pressed in this moment.
     */
    private boolean upDownPressed = false, keyPressed = false;

    /**
     * the amount of pixel to move.
     */
    private final int moveStep = 15;
    
    /*
     * settings values
     */
    
    /**
     * the color RGBS for the JButtons.
     */
    public static final int BORDER_PRESSED_HL_RGB = 173, 
            BORDER_PRESSED_BG_RGB = 109,
            BORDER_PRESSED_CENTER = 211,
            BORDER_RELEASED_HL_RGB = 204, 
            BORDER_RELEASED_BG_RGB = 124;

    /**
     * Constructor save view class.
     * 
     * @param _view the view class.
     */
    public CScrollPane(final VScrollPane _view) {
        this.view = _view;
    }
    
    
    /**
     * Listener does both graphical things:
     * 
     * if the mouse is pressed, the graphical user interface has to be changed;
     * this method changes the background and the border if a button is clicked.
     * 
     * but also starts to move the Component if the up or down buttons
     * is pressed or saves the initializing values for center drag.
     * 
     * @param _event the mousePressed event.
     */
    public final void mousePressed(final MouseEvent _event) {
    
        //the center button is pressed
        if (_event.getSource().equals(view.getJbtn_center())) {

            //update the border
            view.getJbtn_center().setBorder(BorderFactory.createEtchedBorder(
                    new Color(BORDER_PRESSED_HL_RGB, 
                            BORDER_PRESSED_HL_RGB, 
                            BORDER_PRESSED_HL_RGB), 
                            new Color(BORDER_PRESSED_BG_RGB,
                                    BORDER_PRESSED_BG_RGB,
                                    BORDER_PRESSED_BG_RGB)));
            
            //update the background color
            view.getJbtn_center().setBackground(new Color(
                    BORDER_PRESSED_CENTER,
                    BORDER_PRESSED_CENTER,
                    BORDER_PRESSED_CENTER));
            
        } else if (_event.getSource().equals(view.getJbtn_toTop())) {

            //update the border
            view.getJbtn_toTop().setBorder(BorderFactory.createEtchedBorder(
                    new Color(BORDER_PRESSED_HL_RGB, 
                            BORDER_PRESSED_HL_RGB, 
                            BORDER_PRESSED_HL_RGB), 
                            new Color(BORDER_PRESSED_BG_RGB,
                                    BORDER_PRESSED_BG_RGB,
                                    BORDER_PRESSED_BG_RGB)));
            

            //update the icon
            if (view.isVerticalScroll()) {
                view.getJbtn_toTop().setIcon(new ImageIcon(Utils.resizeImage(
                        view.getJbtn_toTop().getWidth(), 
                        view.getJbtn_toTop().getWidth(), 
                        Constants.VIEW_SB_VERT1)));
    
            } else {
                view.getJbtn_toTop().setIcon(new ImageIcon(Utils.resizeImage(
                        view.getJbtn_toTop().getWidth(), 
                        view.getJbtn_toTop().getWidth(),
                        Constants.VIEW_SB_HORIZ1)));
    
            }
        } else if (_event.getSource().equals(view.getJbtn_toBottom())) {
            
            //update the border
            view.getJbtn_toBottom().setBorder(BorderFactory.createEtchedBorder(
                    new Color(BORDER_PRESSED_HL_RGB, 
                            BORDER_PRESSED_HL_RGB, 
                            BORDER_PRESSED_HL_RGB), 
                            new Color(BORDER_PRESSED_BG_RGB,
                                    BORDER_PRESSED_BG_RGB,
                                    BORDER_PRESSED_BG_RGB)));
            
            //update the icon
            if (view.isVerticalScroll()) {
                view.getJbtn_toBottom().setIcon(new ImageIcon(
                        Utils.resizeImage(view.getJbtn_toBottom().getWidth(),
                                view.getJbtn_toBottom().getWidth(),
                                Constants.VIEW_SB_VERT2)));
            } else {
                view.getJbtn_toBottom().setIcon(new ImageIcon(
                        Utils.resizeImage(view.getJbtn_toBottom().getWidth(), 
                                view.getJbtn_toBottom().getWidth(), 
                                Constants.VIEW_SB_HORIZ2)));
            }
        }
            
        
        //if center save drag start and point of old center position
        //if top or button, let the panel scroll.
        if (_event.getSource().equals(view.getJbtn_center())) {
            
            //set click start point and center start Point
            pnt_dragStartOnScreen = new Point(_event.getXOnScreen(), 
                    _event.getYOnScreen());
            pnt_centerStartLocation = view.getJbtn_center().getLocation();
            
        } else if (_event.getSource().equals(view.getJbtn_toBottom()) 
                || _event.getSource().equals(view.getJbtn_toTop())) {
            
            //set up down pressed
            upDownPressed = true;
            
            //start a 'change - location' Thread.
            new Thread() {
                public void run() {
                    try {
                        
                        //change location while upDown is pressed.
                        while (upDownPressed) {
                            Thread.sleep(1);
                            clickEvent(_event);
                        }
                    } catch (InterruptedException exception) {
                        Error.printError(getClass().getSimpleName(), 
                                "mouse pressed", "interrupted.", exception, 
                                Error.ERROR_MESSAGE_PRINT_LESS);
                    }
                }
            } .start();
        }
        
        if (activityListener != null) {

            activityListener.activityOccurred(_event);
        }
    }


    /**
     * Mouse dragged for the center button.
     * @param _event the drag event.
     */
    public final void mouseDragged(final MouseEvent _event) {
        
        //drag center button
        if (_event.getSource().equals(view.getJbtn_center())) {
            
            //vertical scroll versus horizontal scroll
            if (view.isVerticalScroll()) {

                if (State.isNormalRotation()) {
                    int y = (int) (
                            pnt_centerStartLocation.getY() 
                            - pnt_dragStartOnScreen.getY() 
                            + _event.getYOnScreen());
               
                    if (y - view.getJbtn_toTop().getHeight() 
                            > view.getJbtn_toBottom().getY() 
                            - view.getJbtn_toTop().getHeight() 
                            - view.getJbtn_center().getHeight()) {
                        
                        y = view.getJbtn_toBottom().getY() 
                                - view.getJbtn_toTop().getHeight() 
                                - view.getJbtn_center().getHeight() 
                                + view.getJbtn_toTop().getHeight();
                    } else if (y < view.getJbtn_toTop().getHeight()) {
                        y = view.getJbtn_toTop().getHeight();
                    }

                    view.getJbtn_center().setLocation(
                            view.getJbtn_center().getX(), y);
                } else {
                    int y = (int) 
                            (pnt_centerStartLocation.getY() 
                            - pnt_dragStartOnScreen.getY() 
                            + _event.getYOnScreen());

                    //thus the scrollPane does never quit its borders.
                    y = Math.min(y, -view.getJbtn_center().getHeight() 
                            + view.getJbtn_toTop().getY());
                    y = Math.max(y, view.getJbtn_toBottom().getHeight() 
                            + view.getJbtn_toBottom().getY());

                    //set location of the center button.
                    view.getJbtn_center().setLocation(
                            view.getJbtn_center().getX(), y);
                }
            } else {

                if (State.isNormalRotation()) {
                    int x = (int) (pnt_centerStartLocation.getX() 
                            - pnt_dragStartOnScreen.getX() 
                            + _event.getXOnScreen());
                    
                    if (x - view.getJbtn_toTop().getWidth()
                            > view.getJbtn_toBottom().getX()
                            - view.getJbtn_toTop().getWidth()
                            - view.getJbtn_center().getWidth()) {
                        x = view.getJbtn_toBottom().getX() 
                                - view.getJbtn_toTop().getWidth()
                                - view.getJbtn_center().getWidth() 
                                + view.getJbtn_toTop().getWidth();
                    } else if (x < view.getJbtn_toTop().getWidth()) {
                        x = view.getJbtn_toTop().getWidth();
                    }
                    
                    view.getJbtn_center().setLocation(
                            x, view.getJbtn_center().getY());
                } else {
                    int x = (int) (pnt_centerStartLocation.getX() 
                            - pnt_dragStartOnScreen.getX() 
                            + _event.getXOnScreen());

                    //thus the scrollPane does never quit its borders.
                    x = Math.min(x, -view.getJbtn_center().getWidth() 
                            + view.getJbtn_toTop().getX());
                    x = Math.max(x, view.getJbtn_toBottom().getWidth() 
                            + view.getJbtn_toBottom().getX());

                    //set location of the center button.
                    view.getJbtn_center().setLocation(x, 
                            view.getJbtn_center().getY());
                }
                
                
            }
            refreshA();     
        }
    }



    /**
     * if clicked at at or down button.
     * 
     * @param _event the mouseEvent
     */
    public final void mouseClicked(final MouseEvent _event) {

        //if to button or the top button do click event once.
        if (_event.getSource().equals(view.getJbtn_toBottom()) 
                || _event.getSource().equals(view.getJbtn_toTop())) {
            clickEvent(_event);
        }
    }



    /**
     * reset pressed graphics to normal ones.
     * 
     * @param _event the MouseEvent
     */
    public final void mouseReleased(final MouseEvent _event) {
        
        //reset the center value.
        if (_event.getSource().equals(view.getJbtn_center())) {
            
            //reset border
            view.getJbtn_center().setBorder(
                    BorderFactory.createEtchedBorder(
                            new Color(BORDER_RELEASED_HL_RGB, 
                                    BORDER_RELEASED_HL_RGB, 
                                    BORDER_RELEASED_HL_RGB),
                            new Color(BORDER_RELEASED_BG_RGB, 
                                    BORDER_RELEASED_BG_RGB, 
                                    BORDER_RELEASED_BG_RGB)));
            view.getJbtn_center().setBackground(Color.white);
            
        } else if (_event.getSource().equals(view.getJbtn_toTop())) {

            //reset border
            view.getJbtn_toTop().setBorder(
                    BorderFactory.createEtchedBorder(
                            new Color(BORDER_RELEASED_HL_RGB, 
                                    BORDER_RELEASED_HL_RGB, 
                                    BORDER_RELEASED_HL_RGB),
                            new Color(BORDER_RELEASED_BG_RGB, 
                                    BORDER_RELEASED_BG_RGB, 
                                    BORDER_RELEASED_BG_RGB)));
            
            //reset icon
            if (view.isVerticalScroll()) {
                view.getJbtn_toTop().setIcon(new ImageIcon(Utils.resizeImage(
                        view.getJbtn_toTop().getWidth(), 
                        view.getJbtn_toTop().getWidth(), 
                        Constants.SP_PATH_UP)));
            } else {
                view.getJbtn_toTop().setIcon(new ImageIcon(Utils.resizeImage(
                        view.getJbtn_toTop().getWidth(), 
                        view.getJbtn_toTop().getWidth(),
                        Constants.SP_PATH_LEFT)));
            }
        } else if (_event.getSource().equals(view.getJbtn_toBottom())) {
            
            //reset border
            view.getJbtn_toTop().setBorder(
                    BorderFactory.createEtchedBorder(
                            new Color(BORDER_RELEASED_HL_RGB, 
                                    BORDER_RELEASED_HL_RGB, 
                                    BORDER_RELEASED_HL_RGB),
                            new Color(BORDER_RELEASED_BG_RGB, 
                                    BORDER_RELEASED_BG_RGB, 
                                    BORDER_RELEASED_BG_RGB)));
            
            if (view.isVerticalScroll()) {
                view.getJbtn_toBottom().setIcon(new ImageIcon(
                        Utils.resizeImage(view.getJbtn_toBottom().getWidth(), 
                                view.getJbtn_toBottom().getWidth(), 
                                Constants.SP_PATH_DOWN)));
    
            } else {
                view.getJbtn_toBottom().setIcon(new ImageIcon(
                        Utils.resizeImage(view.getJbtn_toBottom().getWidth(), 
                                view.getJbtn_toBottom().getWidth(), 
                                Constants.SP_PATH_RIGHT)));
            }
        }
        if (_event.getSource().equals(view.getJbtn_toBottom()) 
                || _event.getSource().equals(view.getJbtn_toTop())) {
            
            upDownPressed = false;
        }
        
        if (activityListener != null) {

            activityListener.activityOccurred(_event);
        }
    }


    /**
     * {@inheritDoc}
     */
    public final void mouseEntered(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public final void mouseExited(final MouseEvent _event) { }


    /**
     * {@inheritDoc}
     */
    public final void mouseMoved(final MouseEvent _event) {

//        if (_event.getSource().equals(view.getJpnl_toLocate())) {
//            view.requestFocus();
//        }
    }

    /**
     * {@inheritDoc}
     */
    public final void keyPressed(final KeyEvent _event) {

        keyPressed = true;
        new Thread() {
            public void run() {
                while (view.getJbtn_toBottom().isVisible() && keyPressed) {
                    try {
                        
                        final int keyCodeUp = 38,
                                keyCodeDown = 40,
                                keyCodeLeft = 37,
                                keyCodeRight = 39;
                        Thread.sleep((2 + 2) * 2);
                        
                        //up button and vertical scroll
                        if (_event.getKeyCode() == keyCodeUp
                                && view.isVerticalScroll()) {
                            addYLocation();
                        } else if (_event.getKeyCode() == keyCodeDown
                                && view.isVerticalScroll()) {
                            removeYLocation();
                        } else if (_event.getKeyCode() == keyCodeLeft
                                && !view.isVerticalScroll()) {
                                addXLocation();   
                        } else if (_event.getKeyCode() == keyCodeRight
                                && !view.isVerticalScroll()) {
                            removeXLocation();
                        }
                    } catch (InterruptedException exception) {

                        Error.printError(getClass().getSimpleName(), 
                                "key pressed", "interrupted.", exception, 
                                Error.ERROR_MESSAGE_PRINT_LESS);
                    }
                }
            }
        } .start();
    }



    /**
     * reset key pressed.
     * @param _event the KeyEvent.
     */
    public final void keyReleased(final KeyEvent _event) {
        keyPressed = false;     
    }

    
    /**
     * {@inheritDoc}
     */
    public final void keyTyped(final KeyEvent _event) { }

    
    /**
     * Click event.
     * @param _event the event.
     */
    private void clickEvent(final MouseEvent _event) {
    
        //
        if (_event.getSource().equals(view.getJbtn_toBottom())) {
          
            if (view.isVerticalScroll()) {
    
//                if (Status.isNormalRotation()) {

                    removeYLocation();
//                } else {
//                    addYLocation();
//                }
            } else {

//                if (Status.isNormalRotation()) {

                    removeXLocation();
//                } else {
//                    addXLocation();
//                }
            }
        } else if (_event.getSource().equals(view.getJbtn_toTop())) {
    
            if (view.isVerticalScroll()) {

//                if (Status.isNormalRotation()) {

                    addYLocation();
//                } else {
//                    removeYLocation();
//                }
            } else {

//                if (Status.isNormalRotation()) {

                    addXLocation();
//                } else {
//                    removeXLocation();
//                }
            }
        }
    }

    
    /**
     * remove x location.
     */
    private void removeXLocation() {
        

            int x = view.getJpnl_toLocate().getLocation().x - moveStep;
            if (x < -view.getJpnl_toLocate().getWidth()
                    + view.getJpnl_owner().getWidth()) {
                x = -view.getJpnl_toLocate().getWidth()
                        + view.getJpnl_owner().getWidth();
            }
            
            view.getJpnl_toLocate().setLocation(x, 
                    view.getJpnl_toLocate().getLocation().y);
            view.recalculateCenterBounds();
    
    }
    
    
    /**
     * increase x location.
     */
    private void addXLocation() {
        
        
        

            int x = view.getJpnl_toLocate().getLocation().x + moveStep;
            if (x > 0) {
                x = 0;
            }
            view.getJpnl_toLocate().setLocation(x, 
                    view.getJpnl_toLocate().getLocation().y);
            view.recalculateCenterBounds();
    }
    
    
    
    /**
     * decrease y location.
     */
    private void removeYLocation() {

        int y = view.getJpnl_toLocate().getLocation().y - moveStep;
            if (y < -view.getJpnl_toLocate().getHeight()
                    + view.getJpnl_owner().getHeight()) {
                y = -view.getJpnl_toLocate().getHeight()
                        + view.getJpnl_owner().getHeight();
            }
            view.getJpnl_toLocate().setLocation(
                    view.getJpnl_toLocate().getLocation().x, y);

            view.recalculateCenterBounds();

    }


    /**
     * increase y location.
     */
    private void addYLocation() {
        int y = view.getJpnl_toLocate().getLocation().y + moveStep;
        

            if (y > 0) {
                y = 0;
            }
            view.getJpnl_toLocate().setLocation(
                    view.getJpnl_toLocate().getLocation().x, y);
            view.recalculateCenterBounds();
    }

    
    /**
     * refresh because of center.
     */
    private void refreshA() {
        
        final int cent = 100;
        if (view.isVerticalScroll()) {
            int bar100Percent = view.getJbtn_toBottom().getY() 
                    - view.getJbtn_toTop().getHeight()
                    - view.getJbtn_center().getHeight();
            int percentage = Constants.MAX_PERCENTAGE 
                    * (view.getJbtn_center().getY()
                            - view.getJbtn_toTop().getHeight()) 
                            / bar100Percent;
            if (!State.isNormalRotation()) {

                bar100Percent = (-view.getJbtn_center().getHeight() 
                        + view.getJbtn_toTop().getY()) 
                        - (view.getJbtn_toBottom().getHeight() 
                        + view.getJbtn_toBottom().getY());

                
                percentage = Constants.MAX_PERCENTAGE 
                        - Constants.MAX_PERCENTAGE 
                        * (view.getJbtn_center().getY() 
                                -  view.getJbtn_toBottom().getY()
                                -  view.getJbtn_toBottom().getHeight()) 
                                / bar100Percent;
            }
            
            view.getJpnl_toLocate().setLocation(
                    view.getJpnl_toLocate().getLocation().x, 
                    -percentage * (view.getJpnl_toLocate().getHeight() 
                            - view.getJpnl_owner().getHeight()) / cent);
        } else {

            int bar100Percent = view.getJbtn_toBottom().getX() 
                    - view.getJbtn_toTop().getWidth()
                    - view.getJbtn_center().getWidth();
            int percentage = Constants.MAX_PERCENTAGE 
                    * (view.getJbtn_center().getX() 
                            - view.getJbtn_toTop().getWidth()) 
                            / bar100Percent;
            
            if (!State.isNormalRotation()) {

                bar100Percent = (-view.getJbtn_center().getWidth() 
                        + view.getJbtn_toTop().getX()) 
                        - (view.getJbtn_toBottom().getWidth() 
                        + view.getJbtn_toBottom().getX());

                
                percentage = Constants.MAX_PERCENTAGE 
                        - Constants.MAX_PERCENTAGE 
                        * (view.getJbtn_center().getX() 
                                -  view.getJbtn_toBottom().getX()
                                -  view.getJbtn_toBottom().getWidth()) 
                                / bar100Percent;
            }
            view.getJpnl_toLocate().setLocation(-percentage 
                    * (view.getJpnl_toLocate().getWidth() 
                            - view.getJpnl_owner().getWidth()) / cent, 
                            view.getJpnl_toLocate().getLocation().y);
        }
    }
}
