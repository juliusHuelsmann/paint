//package declaration
package model.objects.painting.po.geo;


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
import java.awt.Rectangle;

import model.objects.painting.Picture;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.State;
import model.util.DPoint;

/**
 * The paintObject corresponds to one item that has been painted. It consists
 * of a list ofDPoints which are added by the user by dragging the mouse and 
 * thus painting an entity to the screen.<br><br>
 * 
 * The paint object takes care of painting the image both to the screen and
 * to the not resized BufferedImage which will be saved as an image (e.g. as
 * .PNG file).
 * 
 * @author Julius Huelsmann
 * @version %U%,%I%
 */
public class POCurve extends PaintObjectWriting {

	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.saved.
     */
    private static final long serialVersionUID = -3730582547146097485L;

    
    
    /**
     * Counts the amount of points.
     */
    private boolean ready;
	

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 * @param _pen the pen which is painted
	 * @param _casus whether to paint mathematical2 or mathematical3.
	 */
	public POCurve(final int _elementId, final Pen _pen, 
			final int _casus, final Picture _pic) {
		
	    //call super constructor
	    super(_pic, _casus, _pen);

        if (_casus != Constants.PEN_ID_MATHS_SILENT 
                && _casus != Constants.PEN_ID_MATHS_SILENT_2) {
            State.getLogger().severe("wrong case,.");
        }
        
	    this.ready = true;
	}
	

	/**
	 * Set the PaintObject ready to receive a new point.
	 */
	public final void setReady() {
	    this.ready = true;
	}
    

    
    /**
     * 
     * @param _p1 the first DPoint
     * @param _p2 the secondDPoint
     * @param _toleranceRad the tolerance radius
     * @param _pnt_to_find the point to be found
     * 
     * @return true or false
     */
    public static boolean pruefeLine(final DPoint _p1, final DPoint _p2, 
            final int _toleranceRad, final DPoint _pnt_to_find) {

        //compute delta values
        int dX = (int) (_p1.getX() - _p2.getX());
        int dY = (int) (_p1.getY() - _p2.getY());

        //print the line between the twoDPoints
        for (int a = 0; a < Math.max(Math.abs(dX), Math.abs(dY)); a++) {
            int plusX = a * dX /  Math.max(Math.abs(dX), Math.abs(dY));
            int plusY = a * dY /  Math.max(Math.abs(dX), Math.abs(dY));
            

            if (isInSelectionPoint(new Rectangle(
                    (int) (_pnt_to_find.getX() - _toleranceRad),
                    (int) (_pnt_to_find.getY() - _toleranceRad),
                    (int) (_pnt_to_find.getX() + _toleranceRad),
                    (int) (_pnt_to_find.getY() + _toleranceRad)),
                    new DPoint(_p1.getX() - plusX, _p1.getY() - plusY))) {
                return true;
            }
            
        }
        
        return false;
    }
	/**
	 * {@inheritDoc}
	 */
    @Override
    public final void addPoint(final DPoint _pnt) {

        if (ready) {
            
            boolean empty = getPoints().isEmpty();
            
            if (!empty) {
                
                final int tolerance = 6;
                getPoints().toFirst();
                DPoint pnt_previous = getPoints().getItem();
                getPoints().next();
                boolean found = false;
                
                while (!found && !getPoints().isEmpty() 
                        && !getPoints().isBehind()) {
                    found = pruefeLine(pnt_previous, getPoints().getItem(), 
                            tolerance,
                            _pnt);
                
                    if (!found) {

                        getPoints().next();
                    }
                }
                
                if (!found) {
                    return;
                }
                getPoints().previous();

                
                
                //find the point next to
            } 

            //update MIN values
            setMinX((int) Math.min(_pnt.getX(), getMinX()));
            setMinY((int) Math.min(_pnt.getY(), getMinY()));

            //update MAX values
            setMaxX((int) Math.max(_pnt.getX(), getMaxX()));
            setMaxY((int) Math.max(_pnt.getY(), getMaxY()));
        
            getPoints().insertBehind(new DPoint(_pnt));
            if (empty) {
                getPoints().insertBehind(new DPoint(_pnt));
            }
            
            
            ready = false;
        } else {
            getPoints().replace(new DPoint(_pnt));
        }
    }

}
