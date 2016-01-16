package control.forms.minorImportance;
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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.ControlPaint;
import view.util.mega.MButton;

public class InfoSelection implements ActionListener {

	
	private MButton jbtn_delete, jbtn_resize;
	
	private ControlPaint cp;
	
	public InfoSelection(final ControlPaint _cp) {
		jbtn_delete = new MButton("Delete");
		jbtn_delete.setOpaque(false);
		jbtn_delete.addActionListener(this);
		jbtn_delete.setContentAreaFilled(false);
		jbtn_resize = new MButton("Resize");
		jbtn_resize.setOpaque(false);
		jbtn_resize.addActionListener(this);
		jbtn_resize.setContentAreaFilled(false);
		this.cp = _cp;
	}

	
	public void show(final Point _pnt_location) {
		cp.getView().getInfo_form().applyButtons(
				new MButton[] {jbtn_delete,  jbtn_resize},
				_pnt_location);
	}
	
	
	public void actionPerformed(final ActionEvent _event) {
		if (_event.getSource().equals(jbtn_delete)) {

			cp.getProject().deleteSelected(cp.getView().getTabs().getTab_debug(),
					cp.getcTabSelection());
			cp.getControlPic().releaseSelected();
			cp.getView().getInfo_form().setVisible(false);
		} else if (_event.getSource().equals(jbtn_resize)) {
			
		}
	}
}
