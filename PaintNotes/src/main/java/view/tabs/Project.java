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


import java.awt.event.ActionListener;

import javax.swing.JFrame;

import model.settings.Constants;
import model.settings.ViewSettings;
import view.forms.Help;
import view.util.Item1Button;

/**
 * Panel for the Tab.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Project extends Tab {


	/**
	 * 
	 */
	 private Item1Button tb_next_page, tb_previous_page;
    
    
    /**
     * Constructor. Call super-constructor with the amount of sections the 
     * project tab contains.
     */
    public Project(final ActionListener _cp) {
        super(0);
		super.setOpaque(false);
		super.setLayout(null);
		
        this.initialize(true, _cp);


    }
	/*
	 * Tab
	 */
	//name
	//default page values
	//backup settings

	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	private void initialize(final boolean _print, final ActionListener _cp) {



		if (_print) {

	        //cut
	        tb_previous_page = new Item1Button(null);
		}

        tb_previous_page.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
        tb_previous_page.setLocation(ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
        
        if (_print) {

            tb_previous_page.setBorder(false);
            tb_previous_page.addActionListener(_cp);
        }
        Print.initializeTextButtonOhneAdd(tb_previous_page,
                "Previous Page",
                Constants.VIEW_TB_NEW_PATH);
        
        if (_print) {

            tb_previous_page.setActivable(false);
            super.add(tb_previous_page);
        }
	

		if (_print) {

	        //cut
	        tb_next_page = new Item1Button(null);
		}

        tb_next_page.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
        tb_next_page.setLocation(
        		tb_previous_page.getWidth() + tb_previous_page.getX() 
        		+ ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
        
        if (_print) {

            tb_next_page.setBorder(false);
            tb_next_page.addActionListener(_cp);
        }
        Print.initializeTextButtonOhneAdd(tb_next_page,
                "Next Page",
                Constants.VIEW_TB_NEW_PATH);
        
        if (_print) {

            tb_next_page.setActivable(false);
            super.add(tb_next_page);
        }
	}

	/**
	 * @return the tb_next_page
	 */
	public Item1Button getTb_next_page() {
		return tb_next_page;
	}


	/**
	 * @return the tb_previous_page
	 */
	public Item1Button getTb_previous_page() {
		return tb_previous_page;
	}

	
	//pencil selection | pencil size selection | pencil color selection | 
	//select somethings | special items like graphc etc | copy-paste | 
	//change color of seleciton?
}
