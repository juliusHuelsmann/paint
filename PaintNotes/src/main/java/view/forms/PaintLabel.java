package view.forms;


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

import java.util.Random;

//
import control.interfaces.MoveEvent;
import control.interfaces.PaintListener;

//
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import model.settings.State;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PaintLabel extends MLabel {
    
    /**
     * The location which can be changed and given.
     */
    private int x = 0, y = 0;    

	/**
	 * The menuListener provides an interface for listening for set of location
	 * and size events.
	 */
    private PaintListener paintListener;

    /**
     * JPanel which is to be updated if location changes in
     * PaintLabel.
     */
    private MPanel jpnl_toMove;
    
    /**
     * Constructor.
     * 
     * @param _jpnl_toMove JPanel which is to be updated if location changes in
     * PaintLabel.
     */
    public PaintLabel(final MPanel _jpnl_toMove) {
        this.jpnl_toMove = _jpnl_toMove;
    }

    /**
     * Set the PaintListener which provides an interface for  listening for set 
     * of location and size events outside this utility class.
     * 
     * It is only possible to set one MenuListener at a time.
     * 
     * @param _pl the PaintListener
     */
    public final void setPaintListener(final PaintListener _pl) {
    	this.paintListener = _pl;
    }
    
    
    /*
     * Location is not set directly because the paint methods shell be able 
     * to decide for themselves what to paint at which position.
     * 
     * Thus, the methods for changing the location and for getting it are
     * overwritten and alter / return the locally saved location.
     * 
     * methods for changing location:
     */
    
    
    /**
     * As it is only necessary to scroll in entire pixel units, the scroll
     * units that are received for example by the <code>VScrollPane</code> have
     * to be rounded. For respecting the gap between the rounded values and the
     * actual ones, this function is working with a random method.
     * 
     * @param _x the new x coordinate which is edited
     * @param _y the new y coordinate which is edited
     * 
     * @return 	the resulting scroll value which will be saved in the methods
     * 			which call this function.
     * 
     * @see #setLocation(int, int)
     * @see #getLocation(Point)
     * @see #setBounds(int, int, int, int)
     * 
     * @since paint version 01.02.24
     */
    private Point getShift(final int _x, final int _y) {

    	// The values have to be adapted to the grid which is given by
    	// the zoom size for not having to cope with partial pixel.
    	final double zoomWidth = 1.0 * State.getImageSize().width 
    			/ State.getImageShowSize().width;
    	final double zoomHeight = 1.0 * State.getImageSize().height 
    			/ State.getImageShowSize().height;
    	
    	int newX = (int) Math.round ((Math.round(_x * zoomWidth)) / zoomWidth);
    	int newY = (int) Math.round ((Math.round(_y * zoomHeight)) / zoomHeight);
    	
    	return new Point(newX, newY);
    }
    

    /**
     * This method saves the location in x and y coordinate for being able
     * to display the correct painting sub image.
     * 
     * for x and y location are saved. Thus, the painting methods
     * are able to calculate for themselves what to paint at what position.
     * @param _x the new x coordinate which is saved
     * @param _y the new y coordinate which is saved
     */
    @Override public final synchronized void setLocation(
    		final int _x, final int _y) {
    	
    	final Point p = getShift(_x, _y);
    	final int newX = p.x;
    	final int newY = p.y;
    	
        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.beforeLocationChange(
        			new MoveEvent(new Point(newX, newY)), 
        			new MoveEvent(new Point(x, y)));
        } else {
        	State.getLogger().severe("PaintListener not set.");
        }
        
        //save the old values for being able to pass them to "after location
        //changed" method
        final int lastX = x;
        final int lastY = y;
        
        //save the new location
        this.x = newX;
        this.y = newY;

        jpnl_toMove.setBounds(newX, newY, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());
        

        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.afterLocationChange(
        			new MoveEvent(new Point(newX, newY)),
        			new MoveEvent(new Point(lastX, lastY)));
        } else {
        	State.getLogger().severe("PaintListener not set.");
        }
    }
    

    /**
     * Really set the location.
     * 
     * E.g. called by controlling class in case of zooming in.
     * 
     * @param _x the new x coordinate which is saved
     * @param _y the new y coordinate which is saved
     */
    public final void setLoc(final int _x, final int _y) {
        
        //if something changed, repaint
        super.setBounds(_x, _y, getWidth(), getHeight());
    }
    

    /**
     * in here, the location is not set as usual, but just the values
     * for x and y location are saved. Thus, the painting methods
     * are able to calculate for themselves what to paint at what position.
     * @param _p the new coordinates which are saved
     */
    @Override public final void setLocation(final Point _p) {

    	final Point p = getShift(_p.x, _p.y);
    	
        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.beforeExternalLocationChange(new MoveEvent(p));
        } else {
        	State.getLogger().severe("PaintListener not set.");
        }
        
        //save the new location
        this.x = p.x;
        this.y = p.y;

        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(x, y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());
        

        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.afterExternalLocationChange(new MoveEvent(p));
        } else {
        	State.getLogger().severe("PaintListener not set.");
        }

    }
    
    
    /**
     * set the size of the MLabel and save the new location. Location is not 
     * set because the paint methods shell be able to decide for themselves
     * what to paint at which position.
     * 
     * @param _x the x coordinate which is saved
     * @param _y the y coordinate which is saved
     * @param _widht the width which is set
     * @param _height the height which is set
     */
    @Override public final void setBounds(final int _x, final int _y, 
            final int _widht, final int _height) {

    	final Point p = getShift(_x, _y);
    	
        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	paintListener.beforeExternalLocationChange(new MoveEvent(
        			new Point(p.x, p.y)));
        	paintListener.beforeExternalSizeChange(new MoveEvent(
        			new Point(_widht, _height)));
        } else {
        	State.getLogger().severe("PaintListener not set.");
        }
        
        //save the new location 
        this.x = p.x;
        this.y = p.y;

        //update the JPanel location because the ScrollPane fetches information
        //out of that panel
        jpnl_toMove.setBounds(p.x, p.y, jpnl_toMove.getWidth(), 
                jpnl_toMove.getHeight());
        
        //set width and height.
        super.setBounds(0, 0, _widht, _height);
        
        //Forward the set location event to the instance of paintListener
        //if it has been set.
        if (paintListener != null) {
        	
        	paintListener.afterExternalBoundsChange(
        			new MoveEvent(new Point(p.x, p.y)),
        			new MoveEvent(new Point(_widht, _height)));
        } else {
        	State.getLogger().severe("PaintListener not set.");
        }
    }

    
    
    
    /*
     * methods for getting location
     */

    
    /**
     * @return the real coordinate!
     */
    @Deprecated
    @Override public final int getX() {
        return super.getX();
    }

    /**
     * @return the real coordinate!
     */
    @Deprecated
    @Override public final int getY() {
        return super.getY();
    }
    
    /**
     * returns the saved but not applied x and y coordinates.
     * @return the saved but not applied x and y coordinates (point).
     */
    @Override public final Point getLocation() {
        return new Point(x, y);
    }
    
    /**
     * returns the saved but not applied x and y coordinates together with the
     * applied size in a rectangle. 
     * @return the saved but not applied x and y coordinates together with the
     * applied size in a rectangle. 
     */
    @Override public final Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}
