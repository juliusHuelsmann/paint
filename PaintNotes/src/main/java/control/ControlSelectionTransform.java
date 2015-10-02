package control;


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


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import model.settings.State;
import model.util.DPoint;
import model.util.adt.list.SecureList;
import view.forms.Page;
import view.util.mega.MButton;


/**
 * Handles actions like resizing Selection and moving selection.
 * @author Julius Huelsmann
 * @version %I%,%U%
 */
public class ControlSelectionTransform implements 
MouseMotionListener, MouseListener {

    
    /**
     * the start location of the JLabels (getPage()) for selection 
     * background and
     * selection. For moving operation.
     */
    private DPoint pnt_startLocationLabel;
    
    /**
     * Start location of Buttons for resizing and moving.
     * For moving operation.
     */
    private DPoint[][] pnt_startLocationButton;

    /**
     * The location on screen which is saved on mouseClick at the button
     * which allows the user to move the selected area.
     */
    private DPoint pnt_start;

    /**
     * 
     */
    private DPoint pnt_rSelectionStart;
    
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
    public ControlSelectionTransform(final ControlPaint _cv) {
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
        	
        	// update values after drag
        	updateSelectionLocation(_event, false);
        	
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
    public void mouseClicked(final MouseEvent _event) { 

        if (_event.getSource().equals(
                getPage().getJbtn_resize()[1][1])) {
        	
        	if (_event.getButton() == MouseEvent.BUTTON3) {
        		cv.getInfo_selection().show(new Point(
        				_event.getXOnScreen() - cv.getView().getX() - 1,
        				_event.getYOnScreen() - cv.getView().getY() - 1));
                cv.getControlPic().stopBorderThread();
        	}
        }
    }

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

            dim_imageSizeOld = new Dimension(State.getImageSize());
            factorW = 1.0 * State.getImageSize().width 
                    / State.getImageShowSize().width;
            factorH = 1.0 * State.getImageSize().height 
                    / State.getImageShowSize().height;
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
            
        	updateSelectionLocation(_event, true);

            
            
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
                State.getLogger().warning("Wrong action source? "
                        + "This warning should never occure.");
            }
            
            pnt_start = null;
            pnt_rSelectionStart = null;
        }
        
    }
    

    /**
	 * 
	 * @param _event
	 * @param _applyChanges 	whether to apply the changes to the selection
	 * 							and to reset the values.
	 */
	private void updateSelectionLocation(final MouseEvent _event,
			final boolean _applyChanges) {
		
		if (pnt_start == null || _event == null) {
			
			//print error message.
			State.getLogger().severe("the pnt_start or the _event is null"
					+ "\n\tpnt_start: \t" + pnt_start
					+ "\n\t_event: \t" + _event);
			return;
		}
		
		// compute the difference between the location of the mouse press
		// event (on screen) and the current mouse location (on screen).
	    int dX = (int) (_event.getXOnScreen() - pnt_start.getX()), 
	    		dY = (int) (_event.getYOnScreen() - pnt_start.getY());
	    
	    
	    // shift the selection - background JLabel and the label which 
	    // contains the painted selection by the above-calculated values.
	    if (pnt_startLocationLabel != null) {
	
	        getPage().getJlbl_selectionBG().setLocation(
	                (int) pnt_startLocationLabel.getX() + dX,
	                (int) pnt_startLocationLabel.getY() + dY);
	        getPage().getJlbl_selectionPainting().setLocation(
	                (int) pnt_startLocationLabel.getX() + dX,
	                (int) pnt_startLocationLabel.getY() + dY);
	
	    } else {
	    	
	    	// otherwise print an error message.
	    	State.getLogger().severe("The pnt_startLocationLabel is null");
	    }
		
		// Shift the Buttons for moving and stretching.
	    if (pnt_startLocationButton != null) {
	        for (int x = 0; x < pnt_startLocationButton.length; x++) {
	            for (int y = 0; y < pnt_startLocationButton.length; y++) {
	                 
	            	
	            	// if either 
	            	//		(1)	there is no selection but x + y >== 2 (meaning
	            	//			they are visible) or
	            	// 
	            	//		(2) there is a selection (then each button is 
	            	//			visible)
	            	// shift the buttons. 
	            	//
	            	// Case (1) should never occur because if the entire image
	            	// is selected, there is nothing to shift.
	                if (x + y >= 2 || !wholeImageSelected) {
	
	                	getPage().getJbtn_resize()[x][y].setLocation(
	                			(int) pnt_startLocationButton[x][y].getX() 
	                            + dX,
	                            (int) pnt_startLocationButton[x][y].getY() 
	                            + dY);
	                }
	            }
	        }
	    } else {
	    	// otherwise print error.
	    	State.getLogger().severe(
	    			"The pnt_startLocationButton is null.");
	    }
	            
	    
	    // Shift the rectangle which contains the current location of the
	    // selection.
	    r_selection.x = (int) pnt_rSelectionStart.getX() + dX;
	    r_selection.y =  (int) pnt_rSelectionStart.getY() + dY;
	    
	    // Adapt the border to the selection's new calculated location
	    getPage().getJlbl_border().setBounds(r_selection);
	    
	    // if "apply changes" is selected, the shifting is applied to the 
	    // paintObjects.
	    if (_applyChanges) {
	
	    	//compute the current zoom - factor
	        final double cZoomFactorWidth = 1.0 * State.getImageSize().width
	                / State.getImageShowSize().width;
	        final double cZoomFactorHeight = 1.0 * State.getImageSize().height
	                / State.getImageShowSize().height;
	        
	        cv.getPicture().moveSelected(
	                (int) (1.0 * dX * cZoomFactorWidth), 
	                (int) (1.0 * dY * cZoomFactorHeight));
	        
	        cv.getControlPic().paintEntireSelectionRect(
	                r_selection);
	        getPage().getJlbl_selectionPainting().repaint();
	
	        // reset the pnt_start and the point for which contains the start 
	        // of the selection.
	        pnt_start = null;
	        pnt_rSelectionStart = null;
	        
	        // change the point which is maintained inside the controlPaintgit 
	        // which is used for (not) shifting the selected stuff while
	        // the user scrolls.
	        cv.getPnt_startLocation().x -= dX;
	        cv.getPnt_startLocation().y -= dY;
	    }
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
        State.setImageSize(newDim);
        State.setImageShowSize(new Dimension(
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
    public void mr_stretchImage(final MouseEvent _event) {

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
        
        final int height = j[2][2].getHeight() / 2;
        
        DPoint pnt_stretchFrom = null, pnt_size, pnt_totalStretch = null;
        if (_event.getSource().equals(j[0][0])) {
            
            pnt_stretchFrom = new DPoint(j[2][2].getX() + height, j[2][2].getY() + height);
            pnt_totalStretch = new DPoint(distanceXY, distanceXY);
        } else if (_event.getSource().equals(j[1][0])) {
            pnt_stretchFrom = new DPoint(j[1][2].getX() + height, j[1][2].getY() + height);
            pnt_totalStretch = new DPoint(0, distanceY);

        } else if (_event.getSource().equals(j[2][0])) {
            pnt_stretchFrom = new DPoint(j[0][2].getX() + height, j[0][2].getY() + height);
            pnt_totalStretch = new DPoint(distanceXY2, distanceXY2);
        

        } else if (_event.getSource().equals(j[0][1])) {
            pnt_stretchFrom = new DPoint(j[2][1].getX() + height, j[2][1].getY() + height);
            pnt_totalStretch = new DPoint(distanceX, 0);

        } else if (_event.getSource().equals(j[1][2])) {
            pnt_stretchFrom = new DPoint(j[1][0].getX() + height, j[1][0].getY() + height);
            pnt_totalStretch = new DPoint(0, -distanceY);

        } else if (_event.getSource().equals(j[0][2])) {
            pnt_stretchFrom = new DPoint(j[2][0].getX() + height, j[2][0].getY() + height);
            pnt_totalStretch = new DPoint(-distanceXY2, -distanceXY2);

        } else if (_event.getSource().equals(j[2][1])) {
            pnt_stretchFrom = new DPoint(j[0][1].getX() + height, j[0][1].getY() + height);
            pnt_totalStretch = new DPoint(-distanceX, 0);

        } else if (_event.getSource().equals(j[2][2])) {
            pnt_stretchFrom = new DPoint(j[0][0].getX() + height, j[0][0].getY() + height);
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
        
        stretchImage(pnt_stretchFrom, pnt_totalStretch, pnt_size);

    }
    
    
    private void stretchImage(final DPoint _pnt_stretchFrom,
    		final DPoint _pnt_totalStretch,
    		final DPoint _pnt_size) {

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
                        _pnt_stretchFrom, _pnt_totalStretch, _pnt_size);
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
    
    
    public void mr_stretchImage(final int distanceX, final int distanceY) {

        MButton[][] j = getPage().getJbtn_resize();
        
        final int height = j[2][2].getHeight() / 2;
        final DPoint pnt_stretchFrom1, pnt_totalStretch1;
        final DPoint pnt_size1 = new DPoint(
        		j[2][2].getX() - j[0][0].getX(), 
        		j[2][2].getY() - j[0][0].getY());

        pnt_stretchFrom1 = new DPoint(j[1][0].getX() + height, j[1][0].getY() + height);
        pnt_totalStretch1 = new DPoint(0, -distanceY);
        stretchImage(pnt_stretchFrom1, pnt_totalStretch1, pnt_size1);

        final DPoint pnt_stretchFrom2, pnt_totalStretch2;
        pnt_stretchFrom2 = new DPoint(j[0][1].getX() + height, j[0][1].getY() + height);
        pnt_totalStretch2 = new DPoint(-distanceX, 0);
        stretchImage(pnt_stretchFrom2, pnt_totalStretch2, pnt_size1);
        
        

    }

    /**
     * @return the r_selection
     */
    public final Rectangle getR_selection() {
        return r_selection;
    }

    
    
    
    /**
     * Reset the start points after some movement is applied. <br>
     * This is necessary because of the following scenario:<br>
     * 		1) 	The user selects PaintObjects<br>
     * 		2)	The user moves the selection<br>
     * 			-> 	Inside this controller class the movement is saved.<br>
     * 				The displayed graphical movement is performed by moving<br>
     * 				the container of the selection image and not by repainting<br>
     * 				the selection each time it is moved (for better speed)<br>
     * 		3)  The user demands for a repaint (e.g. because of a change of <br>
     * 			color)<br>
     * 			->	The paintObjects are repainted using the new position<br>
     * 				(resulting from movement). thus the start locations and the <br>
     * 				location of the selection background have to be reset to <br>
     * 				zero. Otherwise there will be an error in the next step:<br>
     * 		4) 	The user moves the PaintObjects<br>
     * 			-> 	Otherwise:
     * 					The selected PaintObjects are painted double-shifted:<br>
     * 					Once by the location of the PaintLabel, once by <br>
     * 					the shift that is saved in model value<br>
     */
    public final void resetPntStartLocationLabel() {
    	this.pnt_startLocationLabel = new DPoint(0, 0);
    }

    /**
     * @param _r_selection the r_selection to set
     */
    public final void setR_selection(final Rectangle _r_selection) {
        this.r_selection = _r_selection;
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
        			State.getLogger().severe("cv.getView().getPage() is null");
    			}
    		} else {
    			State.getLogger().severe("cv.getView() is null");	
    		}
    	} else {
			State.getLogger().severe("cv is null");	
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
