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
import java.awt.image.BufferedImage;

import model.objects.painting.Picture;
import model.objects.painting.po.POInsertion;
import model.objects.painting.po.PaintObject;
import model.objects.pen.Pen;
import model.settings.State;
import model.util.DPoint;
import model.util.DRect;

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
public class POLine extends POInsertion {

	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = -3730582547146097485L;

	
	/**
	 * {@inheritDoc}
	 */
	@Override public final boolean isInSelectionImage(final Rectangle _r) {

	    //check whether the PaintObject is completed
	    if (getPnt_first() == null || getPnt_last() == null) {
	        State.getLogger().warning("Paint object line not ready");
	        return false;
	    }

        //check whether is in rectangle.
        if (isInSelectionPoint(_r, getPnt_first())
                || isInSelectionPoint(_r, getPnt_last())
                || pruefeLine(getPnt_last(), getPnt_first(), _r)) {
            return true;
        }
        
        //if the item has not been found return false
        return false;
    }
    

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId the id of the element.
	 * @param _pen the pen which is painted
	 */
	public POLine(final int _elementId, final Pen _pen, final Picture _pic) {
		
	    //call super constructor
	    super(_elementId, _pen, _pic);
	}
	


    /**
     * {@inheritDoc}
     */
    @Override 
    public final BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x, 
            final int _y, final DRect _r) {
        

        if (getPnt_first() == null || getPnt_last() == null) {
            return _bi;
        }
        getPen().paintLine(
                getPnt_first(), getPnt_last(), _bi, 
                _final, _g, new DPoint(_x, _y));
        return _bi;
    }
    
    
    

    
    /**
     * {@inheritDoc}
     */
    @Override public final synchronized PaintObject[][] separate(
            final Rectangle _r) {
        State.getLogger().severe("not impklemented yet");
        return null;
    }
    
    

}
