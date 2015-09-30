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


import java.awt.Component;

import javax.swing.JFrame;

import view.forms.Help;

/**
 * the workspace tab.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Workspace extends Tab {

    
    
    /**
     * Constructor: calls super-constructor with the amount of sections that
     * are contained in tab Workspace.
	 * @param _cmp_toRepaint the component which is to repaint if the
	 * 			scrolling is performed. (is necessary if the
	 * 			{@link #ScrollablePanel(Component)} is inserted above
	 * 			some opaque component.
     */
    public Workspace(final Component _cmp_toRepaint) {
        super(0, _cmp_toRepaint);
    }
	//change workspace location
	//default values
		//page
		//hours
		//numbers
		//sizes

	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}
}
