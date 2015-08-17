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


import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import model.settings.ViewSettings;
import view.util.mega.MButton;
import view.util.mega.MPanel;

/**
 * Information tab form which can be added to view.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class InfoForm extends MPanel {

	private final int heightPerLine = 15;
	public InfoForm() {
		
		super();
		super.setSize(ViewSettings.getWidthInfoForm(), 0);
		super.setLayout(null);
		super.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.gray));
		super.setBackground(Color.lightGray);
		super.setOpaque(true);
		super.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {}
			
			public void mousePressed(MouseEvent e) {}
			
			public void mouseExited(MouseEvent e) {
				setVisible(false);
			}
			
			public void mouseEntered(MouseEvent e) {}
			
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	
	/**
	 * 
	 * @param jbtn
	 */
	public void applyButtons(
			final MButton [] jbtn,
			final Point pnt_location) {
		if (jbtn != null) {
			for (int i = 0; i < jbtn.length; i++) {
				if (jbtn[i] != null) {
					jbtn[i].setBounds(2, heightPerLine * i + 2, getWidth() - 4, heightPerLine);
					super.add(jbtn[i]);
				}
			}
			
			super.setSize(getWidth(), heightPerLine * jbtn.length + 4);
			super.setLocation(pnt_location.x, pnt_location.y);
			super.setVisible(true);

			
		} else {
			super.setVisible(false);
		}
	}
}
