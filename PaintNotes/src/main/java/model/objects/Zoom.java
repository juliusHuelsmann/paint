package model.objects;


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


import control.ContorlPicture;
import model.settings.State;
import model.settings.ViewSettings;
import view.forms.Page;


/**
 * JLabel which shows where to zoom in.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Zoom {

	
	/**
	 * Controller class which handles the painting.
	 */
	private ContorlPicture controlPicture;
    
    /**
     * Initialize instance.<br><br>
     * 
     * @param _cp 		instance of the controller class which handles the 
     * 					painting.
     */
    public Zoom(final ContorlPicture _cp) { 
    	this.controlPicture = _cp;
    }
    
    /**
     * Location of zoom.
     */
    private int x, y; 
    
    
    /**
     * return the x location.
     * @return the x location
     */
    public int getX() {
       return x; 
    }
    /**
     * 
     * return the y location.
     * @return the y location
     */
    public int getY() {
        return y;
    }
    /**
     * .
     * @param _x the x coordinate
     * @param _y the y coordinate.
     * @param _page
     */
    public synchronized void setLocation(final int _x, final int _y,
    		final Page _page) {

        //the image pixel size in pixel for painting.
        //for example if zoomed in once, an image pixel has
        //got the size of two painted pixel in each dimension.
        //thus it is necessary to paint 4 pixel. These pixel
        //are painted under the previous pixel. Example:
        //      [x] [a] [ ]         (x is the pixel which is 
        //      [a] [a] [ ]         already printed, a are those
        //      [ ] [ ] [ ]         which are added to avoid gaps.
        int imagePixelSize = (int) State.getZoomFactorToShowSize();

        int xNewAligned, yNewAligned;
        
        if (imagePixelSize != 0 && imagePixelSize != 0) {
            int shiftAlinedX = -_page.getJlbl_painting()
                    .getLocation().x % imagePixelSize,
                    shiftAlinedY = -_page.getJlbl_painting()
                    .getLocation().y % imagePixelSize;

            xNewAligned = _x + shiftAlinedX;
            yNewAligned = _y + shiftAlinedY;
            
            xNewAligned = xNewAligned - (xNewAligned % imagePixelSize);
            yNewAligned = yNewAligned - (yNewAligned % imagePixelSize);
        } else {
            xNewAligned = _x;
            yNewAligned = _y;
        }
       
        
        //if the zoom has moved essentially
        if (xNewAligned != x || yNewAligned != y) {
            
            this.x = xNewAligned;
            this.y = yNewAligned;
            
            controlPicture.setZoomBox(
                    x, y, 
                    ViewSettings.ZOOM_SIZE.width,
                    ViewSettings.ZOOM_SIZE.height);
        }
    }
}
