package control.util.implementations;


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


import java.awt.event.MouseEvent;

import view.forms.Page;
import view.forms.Tabs;
import control.ControlPaint;
import control.interfaces.ActivityListener;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ScrollPaneActivityListener implements ActivityListener {

	
	/**
	 * Instance of ControlPaint.
	 */
	private ControlPaint controlPaint;
	
	
	/**
	 * Constructor: saves the ControlPaint instance.
	 * @param _controlPaint instance of ControlPaint
	 */
	public ScrollPaneActivityListener(final ControlPaint _controlPaint) {
		this.controlPaint = _controlPaint;
	}
	

	/**
	 * simple private getter method catching errors.
	 * @return instance of page fetched by ControlPaint
	 */
	private Page getPage() {
		return controlPaint.getView().getPage();
	}

	
	/**
	 * simple private getter method catching errors.
	 * @return instance of tabs fetched by ControlPaint
	 */
	private Tabs getTabs() {
		return controlPaint.getView().getTabs();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void activityOccurred(final MouseEvent _event) {


        for (int a = 0; a < getPage().getJbtn_resize().length; a++) {
            for (int b = 0; b < getPage().getJbtn_resize()[a].length; 
                    b++) {
                getPage().getJbtn_resize()[a][b].repaint();
            }
        }
        getPage().getJlbl_selectionBG().repaint();
        getPage().getJlbl_selectionPainting().repaint();
        //close each open menu
        getTabs().closeMenues();		
	}
}
