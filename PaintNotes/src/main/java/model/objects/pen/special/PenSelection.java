//package declaration
package model.objects.pen.special;


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


//import declarations
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.State;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import model.util.DPoint;


/**
 * .
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class PenSelection extends Pen implements Serializable {

    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * values for moving the border.
     */
    private int currentBorderValue = 0, 
            currentBorderStart = 0;

    /**
     * The last printed point.
     */
    private DPoint pnt_lastPrinted;

    /**
     * Constructor calls super constructor.
     */
	public PenSelection() {
		super(Constants.PEN_ID_POINT, 1, Color.black, 
		        Constants.PATH_PEN_FILLER_LINE);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override protected final void paintPoint(final DPoint _p, 
	        final BufferedImage _bi, final boolean _final, 
	        final DPoint _pnt_shift, final BufferedImage _g,
	        final Rectangle _r_visibleScope) {
		
		//TODO: apply the final Rectangle _r_visibleScope update!

		final double factorWidth = 1.0 * State.getImageSize().width
				/ State.getImageShowSize().width;
		final double factorHeight = 1.0 * State.getImageSize().height
				/ State.getImageShowSize().height;

		final int x = (int) (_pnt_shift.getX() + _p.getX() / factorWidth);
		final int y = (int) (_pnt_shift.getY() + _p.getY() / factorHeight);
		
	    //first printed point
	    if (pnt_lastPrinted == null) {


			if (x >= 0 && y >= 0 && x < _bi.getWidth() && y < _bi.getHeight()) {

		        _bi.setRGB(x, y,
		                ViewSettings.SELECTION_BORDER_CLR_BORDER[0].getRGB()); 
			}
	        pnt_lastPrinted = new DPoint(x, y);

	    } else {
	        

	    	
	        int distanceX =  (int) Math.abs(x - pnt_lastPrinted.getX());
	        int distanceY = (int) Math.abs(y - pnt_lastPrinted.getY());
	        
	        
	        int distance = (int) (Math.max(distanceX, distanceY));
	        
	        if (distance >= ViewSettings.SELECTION_BORDER_BLOCK_SIZE) {


	            //generate vector from pnt_lastPrinted towards the new one
	            //and norm it afterwards to the length of the maximal selection
	            //border size
	            DPoint vector = new DPoint(x - pnt_lastPrinted.getX(), 
	                    y - pnt_lastPrinted.getY());
	            double length = Math.sqrt(Math.pow(vector.getX(), 2)
	                    + Math.pow(vector.getY(), 2));
	            vector.setX((int) (vector.getX() 
	                    * ViewSettings.SELECTION_BORDER_BLOCK_SIZE / length));
	            vector.setY((int) (vector.getY()
                        * ViewSettings.SELECTION_BORDER_BLOCK_SIZE / length));
	            
	            
	            for (int i = 1; 
	                    i <= ViewSettings.SELECTION_BORDER_BLOCK_SIZE; i++) {

	                //calculate the color
	                int s = (currentBorderValue 
	                        / ViewSettings.SELECTION_BORDER_BLOCK_SIZE)
	                        % ViewSettings.SELECTION_BORDER_CLR_BORDER.length;
	                
	                //if in range paint BufferedImage
	                int x1 = (int) (pnt_lastPrinted.getX() + vector.getX() 
                            * i / ViewSettings.SELECTION_BORDER_BLOCK_SIZE);
	                int y1 = (int) (pnt_lastPrinted.getY() + vector.getY() 
                            * i / ViewSettings.SELECTION_BORDER_BLOCK_SIZE);
	                
	                if (x1 >= _bi.getWidth()) {
	                    x1 = _bi.getWidth() - 1;
	                }
                    if (x1 < 0) {
                        x1 = 0;
                    } 
	                
	                if (y1 >= _bi.getHeight()) {
                        y1 = _bi.getHeight() - 1;
                    }
	                
	                if (y1 < 0) {
	                    y1 = 0;
	                }
	                
	                _bi.setRGB(x1, y1, 
	                		ViewSettings.SELECTION_BORDER_CLR_BORDER[s]
	                        .getRGB()); 
	                currentBorderValue++;
	            }
	            
	            pnt_lastPrinted.setX(pnt_lastPrinted.getX() + vector.getX());
	            pnt_lastPrinted.setY(pnt_lastPrinted.getY() + vector.getY());
	            
	            //paint point for reaching the place of the current Point.
	            paintPoint(_p, _bi, _final, _pnt_shift, _g, _r_visibleScope);
	        }
	    }

	}
	
	
	/**
	 * reset the current border value.
	 */
	public final void resetCurrentBorderValue() {
	    this.currentBorderStart += 1;
//	    ViewSettings.SELECTION_BORDER_MOVE_SPEED_PX;
	    this.currentBorderValue = currentBorderStart;
	}

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return TextFactory.getInstance().getName_selection();
	}

}
