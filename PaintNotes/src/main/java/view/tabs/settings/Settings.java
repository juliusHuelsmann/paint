package view.tabs.settings;


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


import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import view.util.mega.MLabel;

@SuppressWarnings("serial")
public class Settings extends JPanel {

	private MLabel jlbl_headline;
	private JTextArea jta_info;
	
	public Settings(final String _title) {
		super();
		super.setOpaque(false);
		super.setLayout(null);
		
		jlbl_headline = new MLabel("Settings: " + _title);
		jlbl_headline.setFont(new Font("", Font.BOLD, 18));
		jlbl_headline.setFocusable(false);
		super.add(jlbl_headline);
		
		jta_info = new JTextArea("This will be the space for the settings of the current tab.\n"
				+ "Has not been implemented yet.");
		jta_info.setFocusable(false);
		jta_info.setOpaque(false);
		jta_info.setEditable(false);
		super.add(jta_info);
	}
	
	public void setSize(int _width, int _height) {
		super.setSize(_width, _height);

		jlbl_headline.setLocation(getWidth() / 3, 10);
		jlbl_headline.setSize(getWidth() / 3, 23);

		jta_info.setSize(400, 60);
		jta_info.setLocation((getWidth() - jta_info.getWidth()) / 2, 
				(getHeight() - jta_info.getHeight()) / 2);
	}
}
