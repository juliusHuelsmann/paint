package control;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import model.settings.Status;
import model.util.DPoint;
import model.util.adt.list.SecureList;
import view.forms.Page;
import view.util.mega.MButton;


/**
 * Handles actions like resizing Selection and moving selection.
 * @author Julius Huelsmann
 * @version %I%,%U%
 */
public class ControlPaintSelectin implements 
MouseMotionListener, MouseListener {

    
    /**
     * the start location of the JLabels (getPage()) for selection 
     * background and
     * selection. For moving operation.
     */
    private DPoint pnt_startLocationLabel;
    
    
    /**
     * the point which saves the paintLabel location at the beginning of 
     * selection. Thus it is possible to change the scroll position
     * while selecting something.
     */
    private Point pnt_startPaintLabelLocation;
    
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
     * This rectangle displays the selection.
     */
    private Rectangle r_selection;
    
    
    /**
     * Whether the whole image is selected or it is just a selection of several
     * PaintObjects.
     */
    private boolean wholeImageSelected = false;
    
    /*
     * Whole image selected
     */

    /**
     * The old image size  (if whole image selected).
     */
    private Dimension dim_imageSizeOld;
    
    /**
     * and the zoom factor (if whole image selected).
     */
    private double factorW, factorH;
    
    
    /**
     * Instance of ControlPaint.
     */
    private ControlPaint cv;
    
    /**
     * Constructor: initialize DPoint array.
     * @param _cv the cv
     */
    public ControlPaintSelectin(final ControlPaint _cv) {
    	this.cv = _cv;
        pnt_startLocationButton = new DPoint
                [2 + 1][2 + 1];
    }
    
    /**
     * 
     * 
     * @param _event The actionEvent
     */
    public final void mouseDragged(final MouseEvent _event) {
        
        if (_event.getSource().equals(
                getPage().getJbtn_resize()[1][1])) {
            
            int dX = (int) (_event.getXOnScreen() - pnt_start.getX()), 
                    dY = (int) (_event.getYOnScreen() - pnt_start.getY());
            
            getPage().getJlbl_selectionBG().setLocation(
                    (int) pnt_startLocationLabel.getX() + dX,
                    (int) pnt_startLocationLabel.getY() + dY);
            
            getPage().getJlbl_selectionPainting().setLocation(
                    (int) pnt_startLocationLabel.getX() + dX,
                    (int) pnt_startLocationLabel.getY() + dY);

            for (int x = 0; x < pnt_startLocationButton.length; x++) {

                for (int y = 0; y < pnt_startLocationButton.length; y++) {
                     
                    if ((x == 2 && y == 2)
                            || (x == 2 && y == 1)
                            || (x == 1 && y == 2)
                            || (x == 0 && y == 2)
                            || !wholeImageSelected) {

                    	getPage().getJbtn_resize()[x][y].setLocation(
                    			(int) pnt_startLocationButton[x][y].getX() 
                                + dX,
                                (int) pnt_startLocationButton[x][y].getY() 
                                + dY);
                    }

                }
            }
            
            r_selection.x = (int) pnt_rSelectionStart.getX() + dX;
            r_selection.y =  (int) pnt_rSelectionStart.getY() + dY;
            
            getPage().getJlbl_border().setBounds(r_selection);
        } else {
            if (wholeImageSelected) {
                md_buttonLocationWholeImage(_event);
            } else {
                md_buttonLocation(_event);
            }
            
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseMoved(final MouseEvent _event) {
        
    }

    /**
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public final void mousePressed(final MouseEvent _event) {
        pnt_start = new DPoint(_event.getLocationOnScreen());
        
        //nothing selected means resize whole image
        if (r_selection == null 
        		|| cv.getPicture().getLs_poSelected() == null
        		|| cv.getPicture().getLs_poSelected().isEmpty()) {

            dim_imageSizeOld = new Dimension(Status.getImageSize());
            factorW = 1.0 * Status.getImageSize().width 
                    / Status.getImageShowSize().width;
            factorH = 1.0 * Status.getImageSize().height 
                    / Status.getImageShowSize().height;
            wholeImageSelected = true;
            r_selection = null;
        } else {
            wholeImageSelected = false;
            pnt_rSelectionStart = new DPoint(r_selection.getLocation());
        }
        pnt_startLocationLabel = new DPoint(
                getPage().getJlbl_selectionBG().getLocation());

        for (int x = 0; x < pnt_startLocationButton.length; x++) {

            for (int y = 0; y < pnt_startLocationButton.length; y++) {
                pnt_startLocationButton[x][y] = new DPoint(cv.getView()
                		.getPage().getJbtn_resize()[x][y].getLocation());
            }
        }
        
        if (!_event.getSource().equals(
                getPage().getJbtn_resize()[1][1])) {
            cv.getControlPic().stopBorderThread();
        }
    }

    /**
     * @param _event the MouseEvent
     */
    public final void mouseReleased(final MouseEvent _event) {

        

        if (_event.getSource().equals(
                getPage().getJbtn_resize()[1][1])) {
            
            int dX = (int) (_event.getXOnScreen() - pnt_start.getX()), 
                    dY = (int) (_event.getYOnScreen() - pnt_start.getY());

            final double cZoomFactorWidth = 1.0 * Status.getImageSize().width
                    / Status.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * Status.getImageSize().height
                    / Status.getImageShowSize().height;
            
            cv.getPicture().moveSelected(
                    (int) (1.0 * dX * cZoomFactorWidth), 
                    (int) (1.0 * dY * cZoomFactorHeight));
            
            cv.getControlPic().paintEntireSelectionRect(
                    r_selection);
            getPage().getJlbl_selectionPainting().repaint();
            
//            cv.getPicture().paintSelected();
            
        } else {

//            if (!wholeImageSelected) {
//
//                cv.getPicture().getLs_poSelected().toFirst();
//            }
            
            //fetch DPoints from which the vectory may start

            if (_event.getSource() instanceof MButton) {
                

                final int jrssW = getPage()
                        .getJlbl_resizeSelectionSize().getWidth();
                final int jrssH = getPage()
                        .getJlbl_resizeSelectionSize().getWidth();
                getPage().getJlbl_resizeSelectionSize().setLocation(
                        (-jrssW), 
                        (-jrssH));
                if (wholeImageSelected) {
                    mr_stretchPicture(_event);
                } else {

                    mr_stretchImage(_event);
                }
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
    private void md_buttonLocationWholeImage(final MouseEvent _event) {

        double distanceX = _event.getXOnScreen() - pnt_start.getX();
        double distanceY = _event.getYOnScreen() - pnt_start.getY();
        MButton[][] j = getPage().getJbtn_resize();
        DPoint[][] p = pnt_startLocationButton;
        double distanceXY;
        if ((distanceX) < (distanceY)) {
            distanceXY = distanceX;
        } else {
            distanceXY = distanceY;
        }
        Dimension newDim = null;
        
            
        
        //bottom
        if (_event.getSource().equals(j[1][2])) {
            j[1][2].setLocation(
                    (int) (p[1][2].getX()), (int) (p[1][2].getY() + distanceY));
            j[2][2].setLocation(
                    (int) (p[2][2].getX()), (int) (p[2][2].getY() + distanceY));

            newDim = new Dimension(
                    (int) (dim_imageSizeOld.width), 
                    (int) (dim_imageSizeOld.getHeight() + distanceY * factorH));
            
        } else if (_event.getSource().equals(j[2][1])) {
          
            //right
            j[2][1].setLocation((int) (p[2][1].getX() + distanceX),
                    (int) (p[2][1].getY()));
            j[2][2].setLocation((int) (p[2][2].getX() + distanceX), 
                    (int) (p[2][2].getY()));
            newDim = new Dimension(
                    (int) (dim_imageSizeOld.width + distanceX * factorW), 
                    (int) (dim_imageSizeOld.getHeight()));
            
        } else if (_event.getSource().equals(j[2][2])) {
            //items at the same edges.
            j[2][2].setLocation(
                    (int) (p[2][2].getX() + distanceXY), 
                    (int) (p[2][2].getY() + distanceXY));
            j[1][2].setLocation((int) (p[1][2].getX()),
                    (int) (p[1][2].getY() + distanceXY));
            j[2][1].setLocation((int) (p[2][1].getX() + distanceXY),
                    (int) (p[2][1].getY()));
            
            //center 

            newDim = new Dimension(
                    (int) (dim_imageSizeOld.width + distanceXY * factorW), 
                    (int) (dim_imageSizeOld.height + distanceXY * factorH));
        } 

        j[1][2].setLocation(j[2][2].getX() / 2 - j[1][2].getWidth() / 2,
                j[1][2].getY());
        j[2][1].setLocation(j[2][1].getX(), 
                j[2][2].getY() / 2 - j[2][1].getHeight() / 2);
        Status.setImageSize(newDim);
        Status.setImageShowSize(new Dimension(
                (int) (newDim.width / factorW),
                (int) (newDim.height / factorH)));
        

        cv.getControlPic().refreshPaint();
        
        
        final int width = getPage().getJlbl_resizeSelectionSize()
                .getWidth();
        final int height = getPage().getJlbl_resizeSelectionSize()
                .getHeight();
        getPage().getJlbl_resizeSelectionSize().setLocation(
                (j[2][2].getX() - width) / 2, 
                (j[2][2].getY() - height) / 2);

        if (getPage().getJlbl_resizeSelectionSize().getX() 
                < 0) {
            getPage().getJlbl_resizeSelectionSize().setLocation(
                    j[2][2].getX(), getPage()
                    .getJlbl_resizeSelectionSize().getY());
        }
        if (getPage().getJlbl_resizeSelectionSize().getY() 
                < 0) {
            getPage().getJlbl_resizeSelectionSize().setLocation(
                    getPage().getJlbl_resizeSelectionSize().getX(),
                    j[2][2].getY());
        }
        
        getPage().getJlbl_resizeSelectionSize().setText(
                newDim.width + "x" + newDim.height + "");

        getPage().refrehsSps();
        
        
    }
    
    
    /**
     * Moves the buttons and the selection to the right location.
     * @param _event the MouseEvent (from mouseDragged)
     */
    private void md_buttonLocation(final MouseEvent _event) {

        double distanceX = _event.getXOnScreen() - pnt_start.getX();
        double distanceY = _event.getYOnScreen() - pnt_start.getY();
        MButton[][] j = getPage().getJbtn_resize();
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
        

        if (!wholeImageSelected) {
            j[1][1].setLocation(j[1][0].getX() + size - sizeButton, 
                    j[0][1].getY() + size - sizeButton);
        }

        getPage().getJlbl_border().setBounds(j[0][0].getX() + size,
                j[0][0].getY() + size, j[2][0].getX() - j[0][0].getX(),
                j[0][2].getY() - j[0][0].getY());
        r_selection = new Rectangle(j[0][0].getX() + size,
                j[0][0].getY() + size, j[2][0].getX() - j[0][0].getX(),
                j[0][2].getY() - j[0][0].getY());
    
        
    }
    

    /**
     * Method for stretching the whole picture.
     * @param _event the passed MouseEvent
     */
    private void mr_stretchPicture(final MouseEvent _event) {
        
    }

    /**
     * Method for stretching image.
     * @param _event the passed MouseEvent
     */
    private void mr_stretchImage(final MouseEvent _event) {

        double distanceX = _event.getXOnScreen() - pnt_start.getX();
        double distanceY = _event.getYOnScreen() - pnt_start.getY();
        MButton[][] j = getPage().getJbtn_resize();
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
        
        if (cv.getPicture().getLs_poSelected() != null 
        		&& !cv.getPicture().getLs_poSelected().isEmpty()) {

        	//start transaction and closed action.
        	final int transaction = cv.getPicture().getLs_poSelected()
        			.startTransaction("stretch image", 
        					SecureList.ID_NO_PREDECESSOR);
        	final int closedAction = cv.getPicture().getLs_poSelected()
        			.startClosedAction("stretch image", 
        					SecureList.ID_NO_PREDECESSOR);
            
            cv.getPicture().getLs_poSelected().toFirst(
            		transaction, closedAction);
            while (!cv.getPicture().getLs_poSelected().isBehind()) {


                cv.getPicture().getLs_poSelected().getItem().stretch(
                        pnt_stretchFrom, pnt_totalStretch, pnt_size);
                cv.getPicture().getLs_poSelected().next(
                		transaction, closedAction);
            }

        	//close transaction and closed action.
        	cv.getPicture().getLs_poSelected().finishTransaction(
        			transaction);
        	cv.getPicture().getLs_poSelected().finishClosedAction(
        			closedAction);
            
            //release selected and paint them
            cv.getControlPic().releaseSelected();
            cv.getPicture().paintSelected(getPage(),
            		cv.getControlPic(),
            		cv.getControlPaintSelection());        	
        }

    }

    /**
     * @return the r_selection
     */
    public final Rectangle getR_selection() {
        return r_selection;
    }

    /**
     * @return the r_selection
     */
    public final Point getOldPaintLabelLocation() {
        return pnt_startPaintLabelLocation;
    }
    
    /**
     * Simple setter method.
     * @param _pnt the point to set.
     */
    public final void setOldPaintLabelLocation(final Point _pnt) {
        
        if (_pnt == null) {
            this.pnt_startPaintLabelLocation = null;
//            this.pnt_startPaintBGLocation = null;
            
        } else {
//
//            if (pnt_startPaintLabelLocation != null) {
//
//                this.pnt_startPaintBGLocation = new Point(_pnt.x 
//                        - pnt_startPaintBGLocation.x 
//                        + pnt_startPaintLabelLocation.x,
//                        _pnt.y 
//                        - pnt_startPaintBGLocation.y 
//                        + pnt_startPaintLabelLocation.y);
//            } else {
//                pnt_startPaintBGLocation = _pnt;
//            }
            this.pnt_startPaintLabelLocation = _pnt;
        }
    }
    
    
    
    /**
     * Reset the start points after some movement is applied.
     * This is necessary because of the following scenario:
     * 		1) 	The user selects PaintObjects
     * 		2)	The user moves the selection
     * 			-> 	Inside this controller class the movement is saved.
     * 				The displayed graphical movement is performed by moving
     * 				the container of the selection image and not by repainting
     * 				the selection each time it is moved (for better speed)
     * 		3)  The user demands for a repaint (e.g. because of a change of 
     * 			color)
     * 			->	The paintObjects are repainted using the new position
     * 				(resulting from movement). thus the start locations and the 
     * 				location of the selection background have to be reset to 
     * 				zero. Otherwise there will be an error in the next step:
     * 		4) 	The user moves the PaintObjects
     * 			-> 	Otherwise:
     * 					The selected PaintObjects are painted double-shifted:
     * 					Once by the location of the PaintLabel, once by 
     * 					the shift that is saved in model value
     */
    public final void resetPntStartLocationLabel() {
    	this.pnt_startLocationLabel = new DPoint(0, 0);
    	pnt_startPaintLabelLocation = new Point(0, 0);
    	getPage().getJlbl_selectionBG().setLocation(
    			pnt_startPaintLabelLocation);
    }

    /**
     * @param _r_selection the r_selection to set
     * @param _pnt_startPaintLabelLocation the start label location.
     */
    public final void setR_selection(final Rectangle _r_selection,
            final Point _pnt_startPaintLabelLocation) {
        this.r_selection = _r_selection;
        this.pnt_startPaintLabelLocation = _pnt_startPaintLabelLocation;
    }
    

    
    
    
    /**
     * Error-checking getter method of page.
     * 
     * @return	instance of Page fetched out of the controller-class 
     * 			ControlPaint.
     */
    private Page getPage() {
    	if (cv != null) {
    		if (cv.getView() != null) {
    			if (cv.getView().getPage() != null) {
        			return cv.getView().getPage();	
    			} else {
        			Status.getLogger().severe("cv.getView().getPage() is null");
    			}
    		} else {
    			Status.getLogger().severe("cv.getView() is null");	
    		}
    	} else {
			Status.getLogger().severe("cv is null");	
    	}
    	return null;
    }

    /**
     * @return the pnt_start
     */
    public final DPoint getPnt_start() {
        return pnt_start;
    }
}
