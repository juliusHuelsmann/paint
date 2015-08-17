package control.forms.tabs;
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

import model.settings.State;
import view.util.Item2;
import control.ControlPaint;
import control.interfaces.ActivityListener;


/**
 * 
 * @author juli
 *
 */
public class CTabInsert implements ActivityListener {

	//TODO: isnt it possible to move the entire content the insert-tab -
	//listener?
	
	/**
	 * 
	 */
	private ControlPaint cp;
	
	
	/**
	 * 
	 * @param _cp
	 */
	public CTabInsert(final ControlPaint _cp) {
		this.cp = _cp;
	}
	
	
	/**
	 * 
	 */
	public final void activityOccurred(final MouseEvent _event) {

        if (_event.getSource() instanceof Item2) {

            Item2 i2 = (Item2) _event.getSource();
    		cp.getView().getTabs().getTab_insert().getTb_selected()
    		.setIcon(i2.getIconPath());
    		cp.getView().getTabs().getTab_insert().getTb_selected().setText(
    				i2.getTitle());
        } else {
        	State.getLogger().severe("wrong listener added");
        }
	}

}
