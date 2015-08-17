package control.forms;
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

import javax.swing.JFrame;

import view.forms.Loading;


/**
 * The controller class for the loading.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class CLoading {

	
	/**
	 * The thread for loading.
	 */
	private Thread t_load;
	
	/**
	 * The loading graphical user interface.
	 */
	private Loading load;
	
	
	
	/**
	 * The constructor: saves the view-class.
	 * @param _load the view class.
	 */
	public CLoading(final Loading _load) {
		this.load = _load;
		load.setVisible(false);
	}
	
	
	
	/**
	 * Load.
	 * @param _jf the JFrame.
	 */
	public final void load(final JFrame _jf) {
		if (t_load != null) {
			t_load.interrupt();
			
		}
		t_load = new Thread() {
			public void run() {
				System.out.println("hier");
				int c = 0;
				load.setVisible(true);
				_jf.setEnabled(false);
				
				try {
					while (!isInterrupted()) {
						final int sleep_time = 100;
						Thread.sleep(sleep_time);
						load.nextIcon(c);
						c++;
						load.repaint();
							
					}
				} catch (Exception e) {
					System.out.println("interrupt");
					e.printStackTrace();
					interrupt();
				}
				load.setVisible(false);
				_jf.setEnabled(true);
				
			}
		};
		t_load.start();
	}
}
