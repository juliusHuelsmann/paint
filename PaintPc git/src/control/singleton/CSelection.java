package control.singleton;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

import model.objects.painting.Picture;
import settings.Status;
import view.forms.Page;


/**
 * Handles actions like resizing Selection and moving selection.
 * @author Julius Huelsmann
 * @version %I%,%U%
 */
public class CSelection implements MouseMotionListener, MouseListener {

    /**
     * the only instance of this class.
     */
    private static CSelection instance;
    
    /**
     * the start location of the JLabels (page) for selection background and
     * selection. For moving operation.
     */
    private Point pnt_startLocationLabel;
    
    /**
     * Start location of Buttons for resizing and moving.
     * For moving operation.
     */
    private Point[][] pnt_startLocationButton;

    /**
     * Start point.
     */
    private Point pnt_start;
    
    
    /**
     * Constructor: initialize Point array.
     */
    public CSelection() {
        pnt_startLocationButton = new Point
                [Page.getInstance().getJbtn_resize().length]
                        [Page.getInstance().getJbtn_resize()[0].length];
    }
    
    /**
     * 
     * 
     * @param _event The actionEvent
     */
    @Override public final void mouseDragged(final MouseEvent _event) {
        
        if (_event.getSource().equals(
                Page.getInstance().getJbtn_resize()[1][1])) {
            
            int dX = _event.getXOnScreen() - pnt_start.x, 
                    dY = _event.getYOnScreen() - pnt_start.y;
            Page.getInstance().getJlbl_selectionBG().setLocation(
                    pnt_startLocationLabel.x + dX,
                    pnt_startLocationLabel.y + dY);
            Page.getInstance().getJlbl_selectionPainting().setLocation(
                    pnt_startLocationLabel.x + dX,
                    pnt_startLocationLabel.y + dY);

            for (int x = 0; x < pnt_startLocationButton.length; x++) {

                for (int y = 0; y < pnt_startLocationButton.length; y++) {
                     
                    Page.getInstance().getJbtn_resize()[x][y].setLocation(
                            pnt_startLocationButton[x][y].x + dX,
                            pnt_startLocationButton[x][y].y + dY);
                }
            }
        } else {
            
            int distanceX = _event.getXOnScreen() - pnt_start.x;
            int distanceY = _event.getYOnScreen() - pnt_start.y;
            int distanceXY;
            if (Math.abs(distanceX) < Math.abs(distanceY)) {
                distanceXY = distanceX;
            } else {

                distanceXY = distanceY;
            }
            
            if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[0][0])) {
                
                Page.getInstance().getJbtn_resize()[0][0].setLocation(
                        pnt_startLocationButton[0][0].x + distanceXY,
                        pnt_startLocationButton[0][0].y + distanceXY);


                Page.getInstance().getJbtn_resize()[0][1].setLocation(
                        pnt_startLocationButton[0][1].x + distanceXY,
                        Page.getInstance().getJbtn_resize()[0][0].getY() 
                        + (Page.getInstance().getJbtn_resize()[0][2].getY()
                        - Page.getInstance().getJbtn_resize()[0][0].getY())
                        / 2);

                Page.getInstance().getJbtn_resize()[0][2].setLocation(
                        pnt_startLocationButton[0][2].x + distanceXY,
                        pnt_startLocationButton[0][2].y);

                Page.getInstance().getJbtn_resize()[1][0].setLocation(
                        Page.getInstance().getJbtn_resize()[0][0].getX()
                        + (Page.getInstance().getJbtn_resize()[2][0].getX()
                        - Page.getInstance().getJbtn_resize()[0][0].getX())
                        / 2,
                        pnt_startLocationButton[1][0].y + distanceXY);

                Page.getInstance().getJbtn_resize()[2][0].setLocation(
                        pnt_startLocationButton[2][0].x,
                        pnt_startLocationButton[2][0].y + distanceXY);

                Page.getInstance().getJbtn_resize()[1][2].setLocation(
                        Page.getInstance().getJbtn_resize()[0][0].getX()
                        + (Page.getInstance().getJbtn_resize()[2][0].getX()
                        - Page.getInstance().getJbtn_resize()[0][0].getX())
                        / 2,
                        pnt_startLocationButton[1][2].y);
                
                final int size = Page.getInstance().getJbtn_resize()[0][0]
                        .getWidth();
                

//                Page.getInstance().getJlbl_painting().removeOldRectangle();
                Page.getInstance().getJlbl_painting().paintSelection(
                        Page.getInstance().getJbtn_resize()[0][0].getX()
                        - size,
                        Page.getInstance().getJbtn_resize()[0][0].getY()
                        - size,
                        Page.getInstance().getJbtn_resize()[0][2].getX()
                        - Page.getInstance().getJbtn_resize()[0][0].getX(),
                        Page.getInstance().getJbtn_resize()[2][0].getY()
                        - Page.getInstance().getJbtn_resize()[0][0].getY());
                
            } else if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[1][0])) {
                Page.getInstance().getJbtn_resize()[1][0].setLocation(
                        pnt_startLocationButton[1][0].x,
                        pnt_startLocationButton[1][0].y + distanceY);
                Page.getInstance().getJbtn_resize()[0][0].setLocation(
                        pnt_startLocationButton[0][0].x,
                        pnt_startLocationButton[0][0].y + distanceY);
                Page.getInstance().getJbtn_resize()[2][0].setLocation(
                        pnt_startLocationButton[2][0].x,
                        pnt_startLocationButton[2][0].y + distanceY);
                
            } else if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[2][0])) {
                Page.getInstance().getJbtn_resize()[2][0].setLocation(
                        pnt_startLocationButton[2][0].x + distanceXY,
                        pnt_startLocationButton[2][0].y + distanceXY);
                
            } else if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[1][0])) {
                
            } else if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[1][2])) {
                
            } else if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[2][1])) {
                
            } else if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[2][2])) {
                
            } else if (_event.getSource().equals(
                    Page.getInstance().getJbtn_resize()[2][3])) {
                
            } 
            
                
            System.out.println("resizing not done yet");
            //resize
        }
    }

    @Override
    public void mouseMoved(final MouseEvent _event) {
        
    }

    
    /**
     * simple getter method.
     * @return the instance
     */
    public static CSelection getInstance() {

        //if not yet instanced call the constructor of FetchColor.
        if (instance == null) {
            instance = new CSelection();
        }
        return instance;
    }

    @Override
    public void mouseClicked(final MouseEvent _event) { }

    @Override
    public void mouseEntered(final MouseEvent _event) { }

    @Override
    public void mouseExited(final MouseEvent _event) { }

    @Override public final void mousePressed(final MouseEvent _event) {
        pnt_start = _event.getLocationOnScreen();
        pnt_startLocationLabel = Page.getInstance().getJlbl_selectionBG()
                .getLocation();

        for (int x = 0; x < pnt_startLocationButton.length; x++) {

            for (int y = 0; y < pnt_startLocationButton.length; y++) {
                pnt_startLocationButton[x][y] = 
                        Page.getInstance().getJbtn_resize()[x][y].getLocation();
            }
        }
        
        if (!_event.getSource().equals(
                Page.getInstance().getJbtn_resize()[1][1])) {
            Page.getInstance().getJlbl_painting().stopBorderThread();
        }
    }

    /**
     * @param _event the MouseEvent
     */
    @Override public final void mouseReleased(final MouseEvent _event) {

        
        
        
        
        
        
        
        
        
        
        

        if (_event.getSource().equals(
                Page.getInstance().getJbtn_resize()[1][1])) {
            
            int dX = _event.getXOnScreen() - pnt_start.x, 
                    dY = _event.getYOnScreen() - pnt_start.y;
            Picture.getInstance().moveSelected(dX, dY);
        } else {

            Picture.getInstance().getLs_poSelected().toFirst();
            
            //fetch points from which the vectory may start

            if (_event.getSource() instanceof JButton) {
                JButton jbtn = (JButton) _event.getSource();
                Point from = new Point(jbtn.getX() - jbtn.getWidth() / 2,
                        jbtn.getY() - jbtn.getHeight() / 2);
            } else {
                Status.getLogger().warning("Wrong action source? "
                        + "This warning should never occure.");
            }
        }
        
        
        pnt_start = null;
    }
    

}
