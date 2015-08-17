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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.settings.Error;
import model.settings.ViewSettings;
import view.util.VButtonWrapper;
import view.util.VTabbedPane;

/**
 * Action- and MouseListener for the TabbedPane.
 * Opens a Tab if clicked on it and highlights a Tab if mouseOver.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class CTabbedPane implements ActionListener, MouseListener {

    
    /**
     * The corresponding view class.
     */
    private VTabbedPane view;
    
    /**
     * Constructor, saves the view.
     * @param _view the corresponding view.
     */
    public CTabbedPane(final VTabbedPane _view) {
        this.view = _view;
    }

    /**
     * {@inheritDoc}
     */
    public final void actionPerformed(final ActionEvent _event) {
        
        try {

            //fetch current tab out of the wrapper button
            int currentTab = Integer.parseInt(
                    "" + ((VButtonWrapper) _event.getSource()).wrapObject());
            
            //open the tab in graphical user interface
            view.openTab(currentTab);
        } catch (ClassCastException exception) {
            
            //throw exception
            Error.printError(getClass().getSimpleName(), 
                    "actionPerformed(...)", "Wrong argument in ActionEvent " 
                            + "which is not a WrapperButton or does not "
                            + "contain an integer.", exception, 
                            Error.ERROR_MESSAGE_INTERRUPT);
        }
    }

    
    /**
     * {@inheritDoc}
     */
    public final void mouseEntered(final MouseEvent _event) {

        try {

            //fetch current tab out of the wrapper button
            int currentTab = Integer.parseInt(
                    "" + ((VButtonWrapper) _event.getSource()).wrapObject());
            
            //if the current button does not belong to the currently opened 
            //tab
            if (view.getOpenTab() != (currentTab)) {
                
                //
                ((VButtonWrapper) _event.getSource()).setBackground(
                        ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT_FOCUS);
            } 
        } catch (ClassCastException exception) {
            
            //throw exception
            Error.printError(getClass().getSimpleName(), 
                    "mouseEntered(...)", "Wrong argument in ActionEvent " 
                            + "which is not a WrapperButton or does not "
                            + "contain an integer.", exception, 
                            Error.ERROR_MESSAGE_INTERRUPT);
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void mouseExited(final MouseEvent _event) {

        try {

            //fetch current tab out of the wrapper button
            int currentTab = Integer.parseInt(
                    "" + ((VButtonWrapper) _event.getSource()).wrapObject());
            
            //if the current button does not belong to the currently opened 
            //tab
            if (view.getOpenTab() != (currentTab)) {
                ((VButtonWrapper) _event.getSource()).setBackground(
                        Color.white);
            }
        } catch (ClassCastException exception) {
            
            //throw exception
            Error.printError(getClass().getSimpleName(), 
                    "mouseExited(...)", "Wrong argument in ActionEvent " 
                            + "which is not a WrapperButton or does not "
                            + "contain an integer.", exception, 
                            Error.ERROR_MESSAGE_INTERRUPT);
        }
    }


    

    /**
     * {@inheritDoc}
     */
    public final void mouseClicked(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public final void mousePressed(final MouseEvent _event) { }


    /**
     * {@inheritDoc}
     */
    public final void mouseReleased(final MouseEvent _event) { }

}
