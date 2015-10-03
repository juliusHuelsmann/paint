//package declaration
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


//import declarations
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import control.forms.tabs.CTabAboutPaint;
import control.interfaces.HelpMouseListener;
import view.forms.Help;
import view.util.Item1Button;
import model.settings.Constants;
import model.settings.ViewSettings;

/**
 * JPanel which shows the list of existing non-selected PaintObjects and 
 * details about them. Is created mainly for testing purpose.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class AboutPaint extends Tab {

	
	/**
	 * JTextArea which contains information on the paint program.
	 */
	private JTextArea jta_about;
	
	/**
	 * Button for checking for program updates.
	 */
	private Item1Button i1b_checkForUpdates, i1b_settings, i1b_reinstall, 
	i1b_uninstall;
	
	/**
	 * Constructor: initialize instances of Components and add special 
	 * MouseMotionListener.
	 * @param _controlTabAbout instance of controller class for this view item
	 * @param _cmp_toRepaint the component which is to repaint if the
	 * 			scrolling is performed. (is necessary if the
	 * 			{@link #ScrollablePanel(Component)} is inserted above
	 * 			some opaque component.
	 */
	public AboutPaint(final CTabAboutPaint _controlTabAbout, 
			final Component _cmp_toRepaint) {
		
		//initialize JPanel and alter settings
		super(0, _cmp_toRepaint);
		super.setLayout(null);
		super.setOpaque(false);

		jta_about = new JTextArea(CTabAboutPaint.demandInformation());
		jta_about.setFocusable(false);
		jta_about.setBorder(null);
		jta_about.setOpaque(false);
		jta_about.setEditable(false);
		super.add(jta_about);
		
		

		i1b_checkForUpdates = new Item1Button(null);
		i1b_checkForUpdates.setLocation(
				ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
		i1b_checkForUpdates.setBorder(false);
        i1b_checkForUpdates.addActionListener(_controlTabAbout);
        i1b_checkForUpdates.setActivable(false);
		super.add(i1b_checkForUpdates);
		
		


		i1b_settings = new Item1Button(null);
		i1b_settings.setLocation(
				i1b_checkForUpdates.getX() 
				+ i1b_checkForUpdates.getWidth()
				+ ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
		i1b_settings.setBorder(false);
        i1b_settings.addActionListener(_controlTabAbout);
        i1b_settings.setActivable(false);
		super.add(i1b_settings);

		i1b_uninstall = new Item1Button(null);
		i1b_uninstall.setLocation(
				i1b_settings.getX() 
				+ i1b_settings.getWidth()
				+ ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
		i1b_uninstall.setBorder(false);
		i1b_uninstall.addActionListener(_controlTabAbout);
		i1b_uninstall.setActivable(false);
		super.add(i1b_uninstall);

		i1b_reinstall = new Item1Button(null);
		i1b_reinstall.setLocation(
				i1b_uninstall.getX() 
				+ i1b_uninstall.getWidth()
				+ ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
		i1b_reinstall.setBorder(false);
		i1b_reinstall.addActionListener(_controlTabAbout);
		i1b_reinstall.setActivable(false);
		super.add(i1b_reinstall);
		
		
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(final int _width, final int _height) {
		
		//call super function
		super.setSize(_width, _height);
		
		//change contents
		i1b_checkForUpdates.setLocation(ViewSettings.getDistanceBetweenItems(),
				ViewSettings.getDistanceBetweenItems());
		i1b_checkForUpdates.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());

		i1b_settings.setLocation(
				i1b_checkForUpdates.getX() 
				+ i1b_checkForUpdates.getWidth()
				+ ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
		i1b_settings.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());

        Print.initializeTextButtonOhneAdd(i1b_checkForUpdates,
                "check for updates",
                Constants.VIEW_TB_NEW_PATH);

        Print.initializeTextButtonOhneAdd(i1b_settings,
                "Settings",
                Constants.VIEW_TB_NEW_PATH);
                
                
		i1b_checkForUpdates.setActivable(false);

		i1b_uninstall.setActivable(false);
		i1b_uninstall.setLocation(i1b_settings.getX() 
        		+ i1b_settings.getWidth() 
        		+ ViewSettings.getDistanceBetweenItems() , 
        		ViewSettings.getDistanceBetweenItems());
		i1b_uninstall.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
		

		i1b_reinstall.setActivable(false);
		i1b_reinstall.setLocation(i1b_uninstall.getX() 
        		+ i1b_uninstall.getWidth() 
        		+ ViewSettings.getDistanceBetweenItems() , 
        		ViewSettings.getDistanceBetweenItems());
		i1b_reinstall.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
		

        Print.initializeTextButtonOhneAdd(i1b_uninstall,
                "Uninstall",
                Constants.VIEW_TB_NEW_PATH);

        Print.initializeTextButtonOhneAdd(i1b_reinstall,
                "Reinstall",
                Constants.VIEW_TB_NEW_PATH);

        jta_about.setLocation(i1b_reinstall.getX() 
        		+ i1b_reinstall.getWidth() 
        		+ ViewSettings.getDistanceBetweenItems() , 
        		ViewSettings.getDistanceBetweenItems());
        
        jta_about.setSize(350, ViewSettings.getView_heightTB_visible() 
        		- 5 * 2 - 40);
        
        
        
	}

	/**
	 * @return the i1b_checkForUpdates
	 */
	public Item1Button getI1b_checkForUpdates() {
		return i1b_checkForUpdates;
	}

	/**
	 * @param _i1b_checkForUpdates the i1b_checkForUpdates to set
	 */
	public void setI1b_checkForUpdates(final Item1Button _i1b_checkForUpdates) {
		this.i1b_checkForUpdates = _i1b_checkForUpdates;
	}

	/**
	 * @return the jta_about
	 */
	public JTextArea getJta_about() {
		return jta_about;
	}

	/**
	 * @param _jta_about the jta_about to set
	 */
	public void setJta_about(final JTextArea _jta_about) {
		this.jta_about = _jta_about;
	}


	/**
	 * Initialize the helpListeners
	 * 
	 * @param _jf	the JFrame
	 * @param _c	the Help class
	 */
	@Override
	public void initializeHelpListeners(final JFrame _jf, final Help _c) {
		i1b_checkForUpdates.addMouseListener(new HelpMouseListener(
				"Check for program updates.",
				HelpMouseListener.HELP_ID_MEDIUM, _jf, 
				_c, i1b_checkForUpdates, null));
	}


	/**
	 * Returns the {@link #i1b_settings}.
	 * @return the i1b_settings
	 */
	public Item1Button getI1b_settings() {
		return i1b_settings;
	}


	/**
	 * Returns the {@link #i1b_reinstall}.
	 * @return the i1b_reinstall
	 */
	public Item1Button getI1b_reinstall() {
		return i1b_reinstall;
	}


	/**
	 * Returns the {@link #i1b_uninstall}.
	 * @return the i1b_uninstall
	 */
	public Item1Button getI1b_uninstall() {
		return i1b_uninstall;
	}

}
