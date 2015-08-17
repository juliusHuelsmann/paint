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

import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

import model.settings.Constants;
import view.util.mega.MLabel;
import view.util.mega.MPanel;


/**
 * Loading graphical user interface.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Loading extends MPanel {

	
	/**
	 * MLabel for the background.
	 */
	private MLabel jlbl_background;
	
	/**
	 * ImageIcons for the loading image.
	 */
	private ImageIcon[] iic;
	
	
	
	/**
	 * Constructor: initialize.
	 */
	public Loading() {
		super();
		super.setBorder(new LineBorder(Color.black));
		
		jlbl_background = new MLabel();
		jlbl_background.setOpaque(false);
		super.add(jlbl_background);
		
		
	}
	
	
	/**
	 * Apply the size to the main component and its contents.
	 * @param _width the new width
	 * @param _height the new height
	 */
	public final void setSize(final int _width, final int _height) {
		super.setSize(_width, _height);
		iic = new ImageIcon[Constants.str_loading.length];
//		for (int i = 0; i < iic.length; i++) {
//			try {
//				iic[i] = new ImageIcon(model.util.Util.resize(
//						path[i], _width, _height));
//					
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}


	/**
	 * 
	 * @param _next the integer which 
	 */
	public final void nextIcon(final int _next) {
		if (iic != null && iic[_next % iic.length] != null) {
			jlbl_background.setIcon(iic[_next % iic.length]);
		}
	}
}
