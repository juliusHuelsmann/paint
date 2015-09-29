package view.tabs;


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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import model.settings.ViewSettings;
import view.forms.Help;
import view.util.ScrollablePanel;
import view.util.mega.MLabel;
import view.util.mega.MPanel;


/**
 * the abstract tab class.
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
@SuppressWarnings("serial")
public abstract class Tab extends ScrollablePanel {

    /**
     * JLabels for the separation, linked with information.
     */
    private MLabel [] jlbl_separation;

    /**
     * JLabels for the information text, linked with separation line.
     */
    private MLabel [] jlbl_information;
    
    
    /**
     * Constructor: initializes the separation and information stuff.
     * @param _amount the amount of different sections separated by separation
     * and linked to information.
     */
    public Tab(final int _amount) {
        
        //initialize the Labels
        jlbl_information = new MLabel[_amount];
        jlbl_separation = new MLabel[_amount];
    	this.applyMySize();
    }
    

    
    /**
     * Function for setting size to the tab specified in ViewSettings.
     */
    private void applyMySize() {

        super.setSize(ViewSettings.getView_widthTb(), 
                ViewSettings.getView_heightTab());
        final int titleHeight = getHeight() / ViewSettings
                .TABBED_PANE_TITLE_PROPORTION_HEIGHT;
        super.setHeightScrollButtons(ViewSettings.getView_heightTB_visible()
        		- titleHeight - ViewSettings.getDistanceBetweenItems() * 2);
    }
    
    
    /**
     * call the apply size method.
     */
    public void applySize() {
    	this.applyMySize();
    }
    

    
    
    /**
     * Insert both separation and information.
     * @param _text the printed text
     * @param _x1 first x coordinate (is between first and second coordinate)
     * @param _x2 second x coordinate (is between first and second coordinate)
     * @param _locationInArray the location in information array
     * @param _insert whether to insert the item or just to change bounds
     */
    protected final void insertSectionStuff(final String _text, 
            final int _x1, final int _x2, final int _locationInArray, 
            final boolean _insert) {
        
        insertSeparation(_x2, _locationInArray, _insert);
        insertInformation(_text, _x1, _x2, _locationInArray, _insert);
    }
    
    

    /**
     * 
     * @param _x the x coordinate in pX
     * @param _locationInArray the location in array
     * @param _insert whether to add or not
     */
    protected final void insertSeparation(final int _x, 
            final int _locationInArray, final boolean _insert) {
        
        //if new initialization is demanded
        if (_insert) {
            this.getJlbl_separation()[_locationInArray] = new MLabel();
            this.getJlbl_separation()[_locationInArray].setBorder(
                    BorderFactory.createLineBorder(
                            ViewSettings.GENERAL_CLR_BACKGROUND_DARK_XX));
            super.add(this.getJlbl_separation()[_locationInArray]);
            
        }
        this.getJlbl_separation()[_locationInArray].setBounds(
                _x, 
                ViewSettings.getDistanceBetweenItems(), 
                1, 
                ViewSettings.getView_heightTB_visible()
                - ViewSettings.getDistanceBetweenItems() 
                - ViewSettings.getView_heightTB()
                / ViewSettings.TABBED_PANE_TITLE_PROPORTION_HEIGHT);
    }
    
    /**
     * insert information text.
     * @param _text the printed text
     * @param _x1 first x coordinate (is between first and second coordinate)
     * @param _x2 second x coordinate (is between first and second coordinate)
     * @param _locationInArray the location in information array
     * @param _insert whether to insert the item or just to change bounds
     */
    protected final void insertInformation(final String _text, 
            final int _x1, final int _x2, final int _locationInArray, 
            final boolean _insert) {

        if (_insert) {

            //final value for foreground for JLabel
            final int rgb = 190;
            
            jlbl_information[_locationInArray] = new MLabel();
            jlbl_information[_locationInArray].setFont(
                    ViewSettings.GENERAL_TP_FONT_INFORMATION);
            jlbl_information[_locationInArray].setForeground(
                    new Color(rgb, rgb, rgb));
            jlbl_information[_locationInArray].setHorizontalAlignment(
                    SwingConstants.CENTER);
            jlbl_information[_locationInArray].setText(_text);
            super.add(jlbl_information[_locationInArray]);
            
        }


            final int number = 
                    ViewSettings.getItemMenu1Height()
                    + ViewSettings.getDistanceBetweenItems();
            final int number2 = 15;
            jlbl_information[_locationInArray].setBounds(
                    _x1, number, _x2 - _x1, number2);
    }

    
    /**
     * Initialize the help listeners for components of implementations of tab.
     * @param _jf	the JFrame which contains everything
     * @param _c	the component which is explained inside the help-form.
     */
    public abstract void initializeHelpListeners(
    		final JFrame _jf, final Help _c);

    /**
     * @return the jlbl_separation
     */
    public final MLabel [] getJlbl_separation() {
        return jlbl_separation;
    }


    /**
     * @return the jlbl_information
     */
    public final MLabel [] getJlbl_information() {
        return jlbl_information;
    }
}
