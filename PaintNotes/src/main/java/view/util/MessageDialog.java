package view.util;


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
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import view.util.mega.MScrollPane;
import control.util.WindowFocusListener;

@SuppressWarnings("serial")
public class MessageDialog extends JFrame  {
	
	private JTextArea jta_text;
	private final Component cmp_root;
	private WindowFocusListener wfl;
	
	
	
	private MessageDialog(final String _message, final Component _cmp_root) {

		// save root component which will be disabled and re-enabled when
		// this window disposes.
		this.cmp_root = _cmp_root;
		
		jta_text = new JTextArea(_message);
		
		MScrollPane sp = new MScrollPane(jta_text);
		super.add(sp);
		
		super.setSize(
				(int) Math.max(jta_text.getPreferredSize().getWidth() + 10, 50),
				Math.max(_cmp_root.getHeight(), 50));
		super.setLocationRelativeTo(_cmp_root);
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		cmp_root.setEnabled(false);
		super.setVisible(true);

		if(cmp_root instanceof Window) {
			wfl = new WindowFocusListener(this);
			Window jf = (Window) cmp_root;
			jf.addWindowListener(wfl);
		}
	}
	
	public void dispose() {
		super.dispose();
		cmp_root.setEnabled(true);
		if (wfl != null) {

			if(cmp_root instanceof Window) {
				Window jf = (Window) cmp_root;
				jf.removeWindowListener(wfl);
			}
			
		}
	}
	
	public static void showMessage(final String _message, final Component _cmp_root) {
		
		new MessageDialog(_message, _cmp_root);
	}
}
