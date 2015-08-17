package control.util;


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


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import control.interfaces.ActivityListener;
import view.util.Item2;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CItem2 implements MouseListener {

    /**
     * The colors.
     */
    private final Color clr_pressed = new Color(230, 160, 170), 
    clr_mouseOver = new Color(160, 230, 170), clr_nothing = Color.lightGray;
    
    /**
     * The only instance of this class.
     */
    private static CItem2 instance;

    
    /**
     * Instance of Activity listener which can be added to the CItem2.
     * Once added, the activityListener informs about each click performed
     * on the instance of Item2 by calling the activityOccurred function.
     */
    private ActivityListener activityListener;
    
    
    /**
     * Method for adding activity listener.
     * 
     * @param _activityListener	implementation of ActivityListener
     */
    public void addItemActivityListener(
    		final ActivityListener _activityListener) {
    	activityListener = _activityListener;
    }
    
    /**
     * Empty utility class constructor.
     */
    private CItem2() { }


    /**
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent _event) {
        if (_event.getSource() instanceof Item2) {

            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_mouseOver));
            
            activityListener.activityOccurred(_event);
        }
    }
    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent _event) {
        if (_event.getSource() instanceof Item2) {
            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_pressed));
        }
    }
    /**
     * {@inheritDoc}
     */
    public void mouseReleased(final MouseEvent _event) { }
    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent _event) {
        if (_event.getSource() instanceof Item2) {
            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_mouseOver));
        }
    }
    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent _event) { 
        if (_event.getSource() instanceof Item2) {
            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_nothing));
        }
    }
    
    
    /**
     * Return the only instance of this class.
     * @return the only instance
     */
    public static CItem2 getInstance() {
        
        if (instance == null) {
            instance = new CItem2();
        }
        return instance;
    }
}
