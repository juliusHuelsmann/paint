package control.singleton;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

import model.objects.painting.Picture;
import model.util.DPoint;
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
    private DPoint pnt_startLocationLabel;
    
    /**
     * Start location of Buttons for resizing and moving.
     * For moving operation.
     */
    private DPoint[][] pnt_startLocationButton;

    /**
     * Start DPoint.
     */
    private DPoint pnt_start;
    
    
    /**
     * Constructor: initialize DPoint array.
     */
    public CSelection() {
        pnt_startLocationButton = new DPoint
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
            md_buttonLocation(_event);
            
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
            
            //fetch DPoints from which the vectory may start

            if (_event.getSource() instanceof JButton) {
                md_stretchImage(_event);
            } else {
                Status.getLogger().warning("Wrong action source? "
                        + "This warning should never occure.");
            }
        }
        
        
        pnt_start = null;
    }
    
    
    
    
    /**
     * Moves the buttons and the selection to the right location.
     * @param _event the MouseEvent (from mouseDragged)
     */
    private void md_buttonLocation(final MouseEvent _event) {

        double distanceX = _event.getXOnScreen() - pnt_start.x;
        double distanceY = _event.getYOnScreen() - pnt_start.y;
        JButton[][] j = Page.getInstance().getJbtn_resize();
        DPoint[][] p = pnt_startLocationButton;
        int distanceXY, distanceXY2;
        if (Math.abs(distanceX) < Math.abs(distanceY)) {
            distanceXY = distanceX;
            distanceXY2 = -distanceX;
        } else {
            distanceXY = distanceY;
            distanceXY2 = distanceY;
        }
        
        if (_event.getSource().equals(j[0][0])) {
            //items at the same edges.
            j[0][0].setLocation(p[0][0].x + distanceXY, p[0][0].y + distanceXY);
            j[0][2].setLocation(p[0][2].x + distanceXY, p[0][2].y);
            j[2][0].setLocation(p[2][0].x, p[2][0].y + distanceXY);
            j[1][0].setLocation(p[1][0].x, p[1][0].y + distanceXY);
            j[0][1].setLocation(p[0][1].x + distanceXY, p[0][1].y);
            
            
        } else if (_event.getSource().equals(j[1][0])) {
            //the items at the same margin as the moved one
            j[1][0].setLocation(p[1][0].x, p[1][0].y + distanceY);
            j[0][0].setLocation(p[0][0].x, p[0][0].y + distanceY);
            j[2][0].setLocation(p[2][0].x, p[2][0].y + distanceY);
            
            
        } else if (_event.getSource().equals(j[2][0])) {
            //the items at the same margin as the moved one
            j[2][0].setLocation(p[2][0].x - distanceXY2, 
                    p[2][0].y + distanceXY2);
            j[1][0].setLocation(j[0][0].getX() - (j[0][0].getX()
                    - j[2][0].getX()) / 2, j[2][0].getY());
            j[0][0].setLocation(p[0][0].x, j[2][0].getY());
            j[2][2].setLocation(j[2][0].getX(), p[2][2].y);
            j[2][1].setLocation(j[2][0].getX(), j[2][0].getY()
                    + (j[2][2].getY() - j[2][0].getY()) / 2);
            
            
        } else if (_event.getSource().equals(j[0][1])) {
            j[0][1].setLocation(p[0][1].x + distanceX, p[0][1].y);
            j[0][0].setLocation(p[0][0].x + distanceX, p[0][0].y);
            j[0][2].setLocation(p[0][2].x + distanceX, p[0][2].y);
            
            
        } else if (_event.getSource().equals(j[1][2])) {
            j[1][2].setLocation(p[1][2].x, p[1][2].y + distanceY);
            j[0][2].setLocation(p[0][2].x, p[0][2].y + distanceY);
            j[2][2].setLocation(p[2][2].x, p[2][2].y + distanceY);
            
            
        } else if (_event.getSource().equals(j[0][2])) {
            //the items at the same margin as the moved one
            j[0][2].setLocation(p[0][2].x - distanceXY2,
                    p[0][2].y + distanceXY2);
            j[1][2].setLocation(j[2][2].getX() - (j[2][2].getX()
                    - j[0][2].getX()) / 2, j[0][2].getY());
            j[2][2].setLocation(p[2][2].x, j[0][2].getY());
            j[0][0].setLocation(j[0][2].getX(), p[0][0].y);
            j[0][1].setLocation(j[0][2].getX(), j[0][2].getY()
                    + (j[0][0].getY() - j[0][2].getY()) / 2);
            
            
        } else if (_event.getSource().equals(j[2][1])) {
            j[2][1].setLocation(p[2][1].x + distanceX, p[2][1].y);
            j[2][0].setLocation(p[2][0].x + distanceX, p[2][0].y);
            j[2][2].setLocation(p[2][2].x + distanceX, p[2][2].y);
            
        } else if (_event.getSource().equals(j[2][2])) {
            //items at the same edges.
            j[2][2].setLocation(p[2][2].x + distanceXY, p[2][2].y + distanceXY);
            j[2][0].setLocation(p[2][0].x + distanceXY, p[2][0].y);
            j[0][2].setLocation(p[0][2].x, p[0][2].y + distanceXY);
            j[1][2].setLocation(p[1][2].x, p[1][2].y + distanceXY);
            j[2][1].setLocation(p[2][1].x + distanceXY, p[2][1].y);
        } 

        final int size = j[0][0].getWidth() / 2;
        final int sizeButton = j[1][1].getWidth() / 2;

        int width = j[2][2].getX() - j[0][0].getX();
        int height = j[2][2].getY() - j[0][0].getY();

        final int minSize = sizeButton * 2 + size * (2 + 1);
        if (width < minSize) {
            j[2][0].setLocation(j[0][0].getX() + minSize, j[2][0].getY());
            j[2][1].setLocation(j[0][0].getX() + minSize, j[2][1].getY());
            j[2][2].setLocation(j[0][0].getX() + minSize, j[2][2].getY());
        }        
        if (height < minSize) {
            j[0][2].setLocation(j[0][2].getX(), j[0][0].getY() + minSize);
            j[1][2].setLocation(j[1][2].getX(), j[0][0].getY() + minSize);
            j[2][2].setLocation(j[2][2].getX(), j[0][0].getY() + minSize);
        }

        //center the buttons
        j[1][0].setLocation(j[0][0].getX() + (j[2][0].getX() 
                - j[0][0].getX()) / 2,  j[1][0].getY());
        j[0][1].setLocation(j[0][1].getX(), j[0][0].getY() 
                + (j[0][2].getY() - j[0][0].getY()) / 2);
        j[1][2].setLocation(j[0][0].getX() + (j[2][0].getX() 
                - j[0][0].getX()) / 2,  j[1][2].getY());
        j[2][1].setLocation(j[2][1].getX(), j[0][0].getY() 
                + (j[0][2].getY() - j[0][0].getY()) / 2);
        
        
        j[1][1].setLocation(j[1][0].getX() + size - sizeButton, 
                j[0][1].getY() + size - sizeButton);

        Page.getInstance().getJlbl_border().setBounds(j[0][0].getX() + size,
                j[0][0].getY() + size, j[2][0].getX() - j[0][0].getX(),
                j[0][2].getY() - j[0][0].getY());
    
    }
    

    private void md_stretchImage(final MouseEvent _event) {

        double distanceX = _event.getXOnScreen() - pnt_start.x;
        double distanceY = _event.getYOnScreen() - pnt_start.y;
        JButton[][] j = Page.getInstance().getJbtn_resize();
        DPoint[][] p = pnt_startLocationButton;
        double distanceXY, distanceXY2;
        if (Math.abs(distanceX) < Math.abs(distanceY)) {
            distanceXY = distanceX;
            distanceXY2 = -distanceX;
        } else {
            distanceXY = distanceY;
            distanceXY2 = distanceY;
        }
        DPoint pnt_stretchFrom = null, pnt_size, pnt_totalStretch = null;
        if (_event.getSource().equals(j[0][0])) {
            
            pnt_stretchFrom = new DPoint(j[2][2].getLocation());
            pnt_totalStretch = new DPoint(distanceXY, distanceXY);
        } else if (_event.getSource().equals(j[1][0])) {
            pnt_stretchFrom = new DPoint(j[1][2].getLocation());
            pnt_totalStretch = new DPoint(0, distanceY);

        } else if (_event.getSource().equals(j[2][0])) {
            pnt_stretchFrom = new DPoint(j[0][2].getLocation());
            pnt_totalStretch = new DPoint(distanceXY2, distanceXY2);

        } else if (_event.getSource().equals(j[0][1])) {
            pnt_stretchFrom = new DPoint(j[2][1].getLocation());
            pnt_totalStretch = new DPoint(distanceX, 0);

        } else if (_event.getSource().equals(j[1][2])) {
            pnt_stretchFrom = new DPoint(j[1][0].getLocation());
            pnt_totalStretch = new DPoint(0, distanceY);

        } else if (_event.getSource().equals(j[0][2])) {
            pnt_stretchFrom = new DPoint(j[2][0].getLocation());
            pnt_totalStretch = new DPoint(distanceXY2, distanceXY2);

        } else if (_event.getSource().equals(j[2][1])) {
            pnt_stretchFrom = new DPoint(j[0][1].getLocation());
            pnt_totalStretch = new DPoint(distanceX, 0);

        } else if (_event.getSource().equals(j[2][2])) {
            pnt_stretchFrom = new DPoint(j[0][0].getLocation());
            pnt_totalStretch = new DPoint(distanceXY, distanceXY);

        } 
        pnt_size = new DPoint(j[2][2].getX() - j[0][0].getX(), 
                j[2][2].getY() - j[0][0].getY());
        

        final int size = j[0][0].getWidth() / 2;
        final int sizeButton = j[1][1].getWidth() / 2;

        final int minSize = sizeButton * 2 + size * (2 + 1);
        if (pnt_size.x < minSize) {
            pnt_size.x = minSize;
        }        
        if (pnt_size.y < minSize) {
            pnt_size.y = minSize;
        }
        
        Picture.getInstance().getLs_poSelected().toFirst();
        while (!Picture.getInstance().getLs_poSelected().isBehind()) {


            Picture.getInstance().getLs_poSelected().getItem().stretch(
                    pnt_stretchFrom, pnt_totalStretch, pnt_size);
            Picture.getInstance().getLs_poSelected().next();
        }
        Page.getInstance().releaseSelected();
        Picture.getInstance().paintSelected();
    }

        

}
