//package declaration
package model.objects.painting.po;


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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import model.objects.painting.Picture;
import model.util.DPoint;
import model.util.DRect;
import model.util.adt.list.List;


/**
 * The paintObject corresponds to one item that has been painted. It consists
 * of a list of points which are added by the user by dragging the mouse and 
 * thus painting an entity to the screen.<br><br>
 * 
 * The paint object takes care of painting the image both to the screen and
 * to the not resized BufferedImage which will be saved as an image (e.g. as
 * .PNG file).<br><br>
 * 
 * Implemented by PaintObjectImage and PaintObjectWriting.
 * 
 * @author Julius Huelsmann
 * @version %U%,%I%
 */
public abstract class PaintObject implements Serializable, Cloneable {

	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = -3730582547146097485L;

	/**
	 * identifier of the current PaintObject. Thus each paint object can be 
	 * identified (e.g. if it is deleted, changed or something).
	 */
	private int elementId;
	
	
    
    /**
     * This method checks whether this paintObject is inside the given 
     * selection rectangle _r. It checks every point in list of points
     * 
     * @param _r the rectangle.
     * @return whether is in rectangle.
     */
    public abstract boolean isInSelectionImage(final Rectangle _r);

    
    /**
     * This method checks whether this paintObject is inside the given 
     * selection rectangle _r. It checks every point in list of points
     * 
     * @param _field the field .
     * @param _pnt_shiftRectangle
     * 				the shifting of the rectangle in x and y direction (or in 
     * 				other words the location of the element _r [0][0] on screen)
     * 
     * @return whether is in rectangle.
     */
    public abstract boolean isInSelectionImage(final byte[][] _field, 
            final Point _pnt_shiftRectangle);

    
	
	
	/**
	 * Stretch the points of entire paintObject and recalculate bounds on the
	 * fly.
	 * 
	 * @param _pnt_from The Point from where to stretch
	 * @param _pnt_totalStretch the total delta stretch
	 * @param _pnt_size the new size of the rectangle which is indicating
	 * the new size of the stretched PaintObject.
	 */
	public abstract void stretch(final DPoint _pnt_from, 
	        final DPoint _pnt_totalStretch,
            final DPoint _pnt_size);
    

	/**
	 * Instance of picture for being able to clone.
	 */
    private Picture picture;

	/**
	 * Constructor creates new instance
	 * of list.
	 * 
	 * @param _elementId 	the id of the element.
	 * @param _picture 		instance of a high model class for being able to 
	 * 						create new PaintObjects.
	 * 					
	 */
	public PaintObject(final Picture _picture, final int _elementId) {
		
		this.picture = _picture;
		//save values
		this.elementId = _elementId;
	}

	
	/**
	 * paint the current list of points to the BufferedImage and to the 
	 * graphics.
	 * 
	 * At the graphics only those points are painted, which belong to 
	 * a paint object which is in the given rectangle (_x, _y, _width + _x,
	 * _height + _y). They are painted shifted by the point _p_shift.
	 *
     * Painting at the graphics is shifted by (-_x, -_y).
     * 
	 * @param _bi the bufferedImage to which is printed.
	 * 
	 * @param _final if _final is true, paint to the Graphics and directly
	 *         at the bufferedImage. If it is false, only paint to the graphics
	 *         of a temporary image which is repainted after each painting.
	 * 
	 * @param _g the Graphics at which is painted if the boolean _final is 
	 *         true
	 * 
     * @param _x the x position where the rectangle which is to be painted to
     *         the JLabel starts
     *         painting at the graphics is shifted by (-_x, -_y)
	 * 
	 * @param _y the y position where the rectangle which is to be painted to
	 *         the JLabel starts
	 *         painting at the graphics is shifted by (-_x, -_y)
	 * 
	 * @param _rectRepaint the rectangle which is to be repainted at graphical 
	 * 			user interface. The pixel of PaintObjects outside this 
	 *			rectangle do not have to be printed. If rectangle is null, each
	 *			point is printed.
	 *         
	 * @return the new BufferedImage.
	 */
    public abstract BufferedImage paint(final BufferedImage _bi, 
            final boolean _final, final BufferedImage _g, final int _x, 
            final int _y, final DRect _rectRepaint);
    
    
    /*
     * snapshot things, e.g. for selection.
     */

	
    /**
     * returns a snapshot bufferedImage of this PaintObject.
     * @return snapshot bufferedImage.
     */
	public abstract BufferedImage getSnapshot();
	
	/**
     * return the bounds of the rectangle of the great image for this 
     * paintObject snapshot.
     * 
     * @return the rectangle.
     */
    public abstract Rectangle getSnapshotBounds();


    /**
     * return the bounds of the smallest square rectangle which contains the 
     * whole PaintObject.
     * 
     * @return the bounds of the rectangle.
     */
    public abstract Rectangle getSnapshotSquareBounds();

    
    /**
     * Fully recalculate the snapshot bounds.
     */
    public abstract void recalculateSnapshotBounds();


    /**
     * Separates the PaintObject; thus there are parts that are inside the
     * given rectangle and ones that are outside.
     * @param _r the rectangle
     * @return the PaintObject array [0][x] outside, [1] [x] inside.s
     */
    public abstract PaintObject[][] separate(Rectangle _r);


    /**
     * Separates the PaintObject; thus there are parts that are inside the
     * given rectangle and ones that are outside. The parts inside are removed.
     * @param _r the rectangle
     * @return the PaintObject array of elements inside.
     * @param _l the list where to insert the new created items.
     */
    public abstract List<PaintObjectWriting> deleteRectangle(
            final Rectangle _r, List<PaintObjectWriting> _l);
    /**
     * Separates the PaintObject; thus there are parts that are inside the
     * given rectangle and ones that are outside.
     * @param _r the rectangle
     * @param _pnt_shiftRectangle 	
     * 				the shifting of the rectangle in x and y direction (or in 
     * 				other words the location of the element _r [0][0] on screen
     * 
     * @return the PaintObject array [0][x] outside, [1] [x] inside.s
     */
    public abstract PaintObject[][] separate(
    		byte[][] _r, Point _pnt_shiftRectangle);
    
    /**
     * Separates the PaintObject; thus there are parts that are inside the
     * given rectangle and ones that are outside.
     * @param _r the rectangle
     * @param _pnt_shiftRectangle 	
     * 				the shifting of the rectangle in x and y direction (or in 
     * 				other words the location of the element _r [0][0] on screen
     * @param _pnt_stretch how the byte array is stretched.
     * @param _l the list where to insert the new created items.
     * @return the PaintObject array of elements inside.
     */
    public abstract  List<PaintObjectWriting>  deleteCurve(
    		byte[][] _r, Point _pnt_shiftRectangle, 
			final DPoint _pnt_stretch, 
    		List<PaintObjectWriting> _l);
    
    
    public abstract boolean isEditable();
    
    

	/**
	 * Move PaintObject items.
	 * 
	 * @param _pow
	 *            PaintObjectWriting
	 * @param _dX
	 *            the x difference from current position
	 * @param _dY
	 *            the y difference from current position
	 * @return the PaintObjectWriting
	 */
	public abstract PaintObject movePaintObject(
			final int _dX, final int _dY);
    
    /**
     * Clone this paint object.
     * @return the cloned object
     */
    public final PaintObject clone() {
    	
    	try {
			return (PaintObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
  
    /*
     * getter methods.
     */
    
    /**
     * getter.
     * @return the elementId
     */
    public final int getElementId() {
        return elementId;
    }






	/**
	 * @return the instance of Picture for the implementations of PaintObject.
	 */
	public final Picture getPicture() {
		return this.picture;
	}


	/**
	 * @param picture the picture to set
	 */
	public void setPicture(Picture picture) {
		this.picture = picture;
	}
    
}
