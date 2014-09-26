package control.singleton;

import java.awt.Rectangle;
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
    private DPoint pnt_start, pnt_rSelectionStart;;

    /**
     * This rectangle displays the selectino.
     */
    private Rectangle r_selection;
    
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
            
            int dX = (int) (_event.getXOnScreen() - pnt_start.getX()), 
                    dY = (int) (_event.getYOnScreen() - pnt_start.getY());
            Page.getInstance().getJlbl_selectionBG().setLocation(
                    (int) pnt_startLocationLabel.getX() + dX,
                    (int) pnt_startLocationLabel.getY() + dY);
            Page.getInstance().getJlbl_selectionPainting().setLocation(
                    (int) pnt_startLocationLabel.getX() + dX,
                    (int) pnt_startLocationLabel.getY() + dY);

            for (int x = 0; x < pnt_startLocationButton.length; x++) {

                for (int y = 0; y < pnt_startLocationButton.length; y++) {
                     
                    Page.getInstance().getJbtn_resize()[x][y].setLocation(
                            (int) pnt_startLocationButton[x][y].getX() + dX,
                            (int) pnt_startLocationButton[x][y].getY() + dY);

                }
            }
            
            r_selection.x = (int) pnt_rSelectionStart.getX() + dX;
            r_selection.y =  (int) pnt_rSelectionStart.getY() + dY;
            
            Page.getInstance().getJlbl_border().setBounds(r_selection);
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
        pnt_start = new DPoint(_event.getLocationOnScreen());
        pnt_rSelectionStart = new DPoint(r_selection.getLocation());
        pnt_startLocationLabel = new DPoint(
                Page.getInstance().getJlbl_selectionBG().getLocation());

        for (int x = 0; x < pnt_startLocationButton.length; x++) {

            for (int y = 0; y < pnt_startLocationButton.length; y++) {
                pnt_startLocationButton[x][y] = new DPoint(Page.getInstance()
                        .getJbtn_resize()[x][y].getLocation());
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
            
            int dX = (int) (_event.getXOnScreen() - pnt_start.getX()), 
                    dY = (int) (_event.getYOnScreen() - pnt_start.getY());

            final double cZoomFactorWidth = 1.0 * Status.getImageSize().width
                    / Status.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * Status.getImageSize().height
                    / Status.getImageShowSize().height;
            
            Picture.getInstance().moveSelected(
                    (int) (1.0 * dX * cZoomFactorWidth), 
                    (int) (1.0 * dY * cZoomFactorHeight));
        } else {

            Picture.getInstance().getLs_poSelected().toFirst();
            
            //fetch DPoints from which the vectory may start

            if (_event.getSource() instanceof JButton) {
                mr_stretchImage(_event);
            } else {
                Status.getLogger().warning("Wrong action source? "
                        + "This warning should never occure.");
            }
        }
        
        
        pnt_start = null;
        pnt_rSelectionStart = null;
    }
    
    
    
    
    /**
     * Moves the buttons and the selection to the right location.
     * @param _event the MouseEvent (from mouseDragged)
     */
    private void md_buttonLocation(final MouseEvent _event) {

        double distanceX = _event.getXOnScreen() - pnt_start.getX();
        double distanceY = _event.getYOnScreen() - pnt_start.getY();
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
        
        if (_event.getSource().equals(j[0][0])) {
            //items at the same edges.
            j[0][0].setLocation((int) (p[0][0].getX() + distanceXY), 
                    (int) (p[0][0].getY() + distanceXY));
            j[0][2].setLocation((int) (p[0][2].getX() + distanceXY), 
                    (int) p[0][2].getY());
            j[2][0].setLocation((int) p[2][0].getX(), 
                    (int) (p[2][0].getY() + distanceXY));
            j[1][0].setLocation((int) p[1][0].getX(), 
                    (int) (p[1][0].getY() + distanceXY));
            j[0][1].setLocation((int) (p[0][1].getX() + distanceXY), 
                    (int) p[0][1].getY());
            
            
        } else if (_event.getSource().equals(j[1][0])) {
            //the items at the same margin as the moved one
            j[1][0].setLocation((int) p[1][0].getX(), 
                    (int) (p[1][0].getY() + distanceY));
            j[0][0].setLocation((int) p[0][0].getX(),
                    (int) (p[0][0].getY() + distanceY));
            j[2][0].setLocation((int) p[2][0].getX(),
                    (int) (p[2][0].getY() + distanceY));
            
            
        } else if (_event.getSource().equals(j[2][0])) {
            //the items at the same margin as the moved one
            j[2][0].setLocation((int) (p[2][0].getX() - distanceXY2), 
                    (int) (p[2][0].getY() + distanceXY2));
            j[1][0].setLocation(j[0][0].getX() - (j[0][0].getX()
                    - j[2][0].getX()) / 2, j[2][0].getY());
            j[0][0].setLocation((int) p[0][0].getX(), j[2][0].getY());
            j[2][2].setLocation(j[2][0].getX(), (int) p[2][2].getY());
            j[2][1].setLocation(j[2][0].getX(), j[2][0].getY()
                    + (j[2][2].getY() - j[2][0].getY()) / 2);
            
            
        } else if (_event.getSource().equals(j[0][1])) {
            j[0][1].setLocation(
                    (int) (p[0][1].getX() + distanceX), (int) (p[0][1].getY()));
            j[0][0].setLocation(
                    (int) (p[0][0].getX() + distanceX), (int) (p[0][0].getY()));
            j[0][2].setLocation(
                    (int) (p[0][2].getX() + distanceX), (int) (p[0][2].getY()));
            
            
        } else if (_event.getSource().equals(j[1][2])) {
            j[1][2].setLocation(
                    (int) (p[1][2].getX()), (int) (p[1][2].getY() + distanceY));
            j[0][2].setLocation(
                    (int) (p[0][2].getX()), (int) (p[0][2].getY() + distanceY));
            j[2][2].setLocation(
                    (int) (p[2][2].getX()), (int) (p[2][2].getY() + distanceY));
            
            
        } else if (_event.getSource().equals(j[0][2])) {
            //the items at the same margin as the moved one
            j[0][2].setLocation((int) (p[0][2].getX() - distanceXY2),
                    (int) (p[0][2].getY() + distanceXY2));
            j[1][2].setLocation((int) (j[2][2].getX() - (j[2][2].getX()
                    - j[0][2].getX()) / 2), j[0][2].getY());
            j[2][2].setLocation((int) (p[2][2].getX()), j[0][2].getY());
            j[0][0].setLocation((int) (j[0][2].getX()), (int) (p[0][0].getY()));
            j[0][1].setLocation(j[0][2].getX(), j[0][2].getY()
                    + (j[0][0].getY() - j[0][2].getY()) / 2);
            
            
        } else if (_event.getSource().equals(j[2][1])) {
            j[2][1].setLocation((int) (p[2][1].getX() + distanceX),
                    (int) (p[2][1].getY()));
            j[2][0].setLocation((int) (p[2][0].getX() + distanceX), 
                    (int) (p[2][0].getY()));
            j[2][2].setLocation((int) (p[2][2].getX() + distanceX), 
                    (int) (p[2][2].getY()));
            
        } else if (_event.getSource().equals(j[2][2])) {
            //items at the same edges.
            j[2][2].setLocation(
                    (int) (p[2][2].getX() + distanceXY), 
                    (int) (p[2][2].getY() + distanceXY));
            j[2][0].setLocation((int) (p[2][0].getX() + distanceXY), 
                    (int) (p[2][0].getY()));
            j[0][2].setLocation((int) (p[0][2].getX()),
                    (int) (p[0][2].getY() + distanceXY));
            j[1][2].setLocation((int) (p[1][2].getX()),
                    (int) (p[1][2].getY() + distanceXY));
            j[2][1].setLocation((int) (p[2][1].getX() + distanceXY),
                    (int) (p[2][1].getY()));
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
        r_selection = new Rectangle(j[0][0].getX() + size,
                j[0][0].getY() + size, j[2][0].getX() - j[0][0].getX(),
                j[0][2].getY() - j[0][0].getY());
    
    }
    

    /**
     * Method for stretching image.
     * @param _event the passed MouseEvent
     */
    private void mr_stretchImage(final MouseEvent _event) {

        double distanceX = _event.getXOnScreen() - pnt_start.getX();
        double distanceY = _event.getYOnScreen() - pnt_start.getY();
        JButton[][] j = Page.getInstance().getJbtn_resize();
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
            pnt_totalStretch = new DPoint(0, -distanceY);

        } else if (_event.getSource().equals(j[0][2])) {
            pnt_stretchFrom = new DPoint(j[2][0].getLocation());
            pnt_totalStretch = new DPoint(-distanceXY2, -distanceXY2);

        } else if (_event.getSource().equals(j[2][1])) {
            pnt_stretchFrom = new DPoint(j[0][1].getLocation());
            pnt_totalStretch = new DPoint(-distanceX, 0);

        } else if (_event.getSource().equals(j[2][2])) {
            pnt_stretchFrom = new DPoint(j[0][0].getLocation());
            pnt_totalStretch = new DPoint(-distanceXY, -distanceXY);

        } 
        pnt_size = new DPoint(j[2][2].getX() - j[0][0].getX(), 
                j[2][2].getY() - j[0][0].getY());
        

        final int size = j[0][0].getWidth() / 2;
        final int sizeButton = j[1][1].getWidth() / 2;

        final int minSize = sizeButton * 2 + size * (2 + 1);
        if (pnt_size.getX() < minSize) {
            pnt_size.setX(minSize);
        }        
        if (pnt_size.getY() < minSize) {
            pnt_size.setY(minSize);
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

    /**
     * @return the r_selection
     */
    public final Rectangle getR_selection() {
        return r_selection;
    }

    /**
     * @param _r_selection the r_selection to set
     */
    public final void setR_selection(final Rectangle _r_selection) {
        this.r_selection = _r_selection;
    }

        

}
