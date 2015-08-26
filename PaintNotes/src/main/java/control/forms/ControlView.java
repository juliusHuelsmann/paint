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

//import declarations
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import control.forms.tabs.CTabTools;
import control.interfaces.ActivityListener;
import model.settings.Constants;
import model.settings.State;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import model.util.paint.Utils;
import view.View;


/**
 * Controller class for events handled for components directly added to instance
 * of JFrame View.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ControlView implements ActionListener,
ActivityListener, MouseListener {

	
	/**
	 * The saved instance of the view class.
	 */
	private final View view;
	
	
	/**
	 * Instance of the controller class controlTabPainting.
	 */
	private CTabTools ctp;
	
	
	
	/**
	 * Constructor: initializes the view class and reads settings.
	 * @param _view instance of view
	 * @param _ctp instance of ControlTabPainting
	 */
	public ControlView(final View _view, final CTabTools _ctp) {
		
		this.view = _view;
		this.ctp = _ctp;
	}
	
	
	/**
	 * ActionListener for items that are directly added to the JFrame.
	 * 
	 * @param _event the actionEvent
	 */
	public final void actionPerformed(final ActionEvent _event) {
		if (_event.getSource().equals(view.getJbtn_exit())) {


			//if there are changes to be saved
			if (State.isUncommittedChanges()) {

				final int answer = JOptionPane.showConfirmDialog(
						
						//the window which is disabled while the user has not
						//answered the confirm dialog
						view, 
						
						//The text contained inside the dialog
						TextFactory.getInstance()
						.getTextViewPopupSaveChangesText(), 
						
						//The title of the dialog
						TextFactory.getInstance()
						.getTextViewPopupSaveChangesTitle(),
						
						//options which are 
						JOptionPane.YES_NO_CANCEL_OPTION);
				
				switch (answer) {
				case JOptionPane.YES_OPTION:

					ctp.mr_save();
					break;
					
					
				case JOptionPane.NO_OPTION:

					//exit
					System.exit(1);
					break;
					
					
				case JOptionPane.CANCEL_OPTION:
				case JOptionPane.CLOSED_OPTION:
					
					//do nothing
					break;
					
					
				default:
					
					//default can not occur.
					break;
				}

			} else {
				System.exit(1);
			}
		}
	}

	
	/**
	 * MouseListener.
	 * @param _event the mouseEvent
	 */
	public final void mouseClicked(final MouseEvent _event) {
		
	}

	
	/**
	 * MouseListener.
	 * @param _event the mouseEvent
	 */
	public final void mouseEntered(final MouseEvent _event) {

		// source: exit button at the top of the window
		if (_event.getSource().equals(view.getJbtn_exit())) {

			view.getJbtn_exit().setIcon(
					new ImageIcon(Utils.resizeImage(view
							.getJbtn_exit().getWidth(), view
							.getJbtn_exit().getHeight(),
							Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
		} else if (_event.getSource().equals(
				view.getJbtn_fullscreen())) {
			view.getJbtn_fullscreen().setIcon(new ImageIcon(Utils
					.resizeImage(view.getJbtn_exit().getWidth(), 
							view.getJbtn_exit().getHeight(),
							Constants
							.VIEW_JBTN_FULLSCREEN_MOUSEOVER_PATH)));
		} else if (_event.getSource().equals(
				view.getJbtn_minimize())) {
			view.getJbtn_minimize().setIcon(new ImageIcon(Utils
					.resizeImage(view.getJbtn_exit().getWidth(), 
							view.getJbtn_exit().getHeight(),
							Constants
							.VIEW_JBTN_MINIMIZE_MOUSEOVER_PATH)));
		}
	}


	/**
	 * MouseListener.
	 * @param _event the mouseEvent
	 */
	public final void mouseExited(final MouseEvent _event) {
	
		// source: exit button at the top of the window
		if (_event.getSource().equals(view.getJbtn_exit())) {
			view.getJbtn_exit().setIcon(new ImageIcon(Utils
					.resizeImage(view.getJbtn_exit().getWidth(), 
							view.getJbtn_exit().getHeight(),
							Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));
			} else if (_event.getSource().equals(
					view.getJbtn_fullscreen())) {
				view.getJbtn_fullscreen().setIcon(new ImageIcon(Utils
						.resizeImage(view.getJbtn_exit().getWidth(), 
								view.getJbtn_exit().getHeight(),
								Constants
								.VIEW_JBTN_FULLSCREEN_NORMAL_PATH)));
			} else if (_event.getSource().equals(
					view.getJbtn_minimize())) {
				view.getJbtn_minimize().setIcon(new ImageIcon(Utils
						.resizeImage(view.getJbtn_exit().getWidth(), 
								view.getJbtn_exit().getHeight(),
								Constants
								.VIEW_JBTN_MINIMIZE_NORMAL_PATH)));
			}	
	}


	/**
	 * MouseListener which changes the layout of the buttons contained in
	 * view class while the mouse is pressed.
	 * 
	 * @param _event the mouseEvent
	 */
	public final void mousePressed(final MouseEvent _event) {

		if (_event.getSource().equals(view.getJbtn_exit())) {
			
			//reset the icon of the exit button.
			view.getJbtn_exit().setIcon(new ImageIcon(Utils.resizeImage(
					view.getJbtn_exit().getWidth(), 
					view.getJbtn_exit().getHeight(),
					Constants.VIEW_JBTN_EXIT_PRESSED_PATH)));
			
			
		} else if (_event.getSource().equals(view.getJbtn_fullscreen())) {
			
			//reset the icon of the fullscreen button.
			view.getJbtn_fullscreen().setIcon(new ImageIcon(Utils.resizeImage(
					view.getJbtn_exit().getWidth(), 
					view.getJbtn_exit().getHeight(),
					Constants.VIEW_JBTN_FULLSCREEN_PRESSED_PATH)));
		} else if (_event.getSource().equals(
				view.getJbtn_minimize())) {
			view.getJbtn_minimize().setIcon(new ImageIcon(Utils
					.resizeImage(view.getJbtn_exit().getWidth(), 
							view.getJbtn_exit().getHeight(),
							Constants
							.VIEW_JBTN_MINIMIZE_PRESSED_PATH)));
		}	
	}


	/**
	 * MouseListener.
	 * @param _event the mouseEvent
	 */
	public final void mouseReleased(final MouseEvent _event) {

		// source: exit button at the top of the window
		if (_event.getSource().equals(view.getJbtn_exit())) {

			//reset icon of the exit button
			view.getJbtn_exit().setIcon(new ImageIcon(Utils.resizeImage(
					view.getJbtn_exit().getWidth(), 
					view.getJbtn_exit().getHeight(), 
					Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));
		 
		
		} else if (_event.getSource().equals(view.getJbtn_fullscreen())) {

			//change the boolean which contains whether the current session
			//is a fullscreen or a normal window session
			ViewSettings.setFULLSCREEN(!ViewSettings.isFullscreen());
		 
			
			//if the new window state is fullscreen save the window size to
			//the size of the screen and save it inside the ViewSettings.
			//The setter method applies the size to the instance of View.
			if (ViewSettings.isFullscreen()) {
				
				//compute the new size of the 
				ViewSettings.setSize_jframe(
						ViewSettings.getSizeViewFullscreen());
			view.setFullscreen();
		 } else {

			//if the new window state is not fullscreen, apply custom size
			//of JFrame.
			ViewSettings.setSize_jframe(
					ViewSettings.getSizeViewWindow());
				view.setNotFullscreen();
		 }
			sizeChanged();
		
		} else if (_event.getSource().equals(view.getJbtn_minimize())) {

			 view.setState(JFrame.ICONIFIED);
		 } 
	}

	
	
	/**
	 * 
	 */
	private void sizeChanged() {

		view.setVisible(true);
		view.flip();
		view.repaint();
		view.getTabs().flipSons();
		view.getTabs().flip();
		view.getTabs().openTab(0);
		view.getTabs().reApplySize(view);
		view.getTabs().repaint();
		

		ctp.getControlPaint().setBi_preprint(
				new BufferedImage(
						Math.max(ViewSettings.getView_bounds_page()
								.getSize().width, 1),
						Math.max(ViewSettings.getView_bounds_page()
								.getSize().height,
								1),
						BufferedImage.TYPE_INT_ARGB));
		ctp.getControlPaint().getControlPic().setBi_background(
				new BufferedImage(
						Math.max(ViewSettings.getView_bounds_page()
								.getSize().width, 1),
						Math.max(ViewSettings.getView_bounds_page()
								.getSize().height,
								1),
						BufferedImage.TYPE_INT_ARGB));
				ctp.getControlPaint().getControlPic().setBi(new BufferedImage(
						Math.max(ViewSettings.getView_bounds_page()
								.getSize().width, 1),
						Math.max(ViewSettings.getView_bounds_page()
								.getSize().height, 
								1),
						BufferedImage.TYPE_INT_ARGB));
		view.getPage().setSize(
				(int) ViewSettings.getView_bounds_page().getWidth(),
		 (int) ViewSettings.getView_bounds_page().getHeight());
	}

	
	/**
	 * {@inheritDoc}
	 */
	public final void activityOccurred(final MouseEvent _event) {

		ViewSettings.setSize_jframe(view.getSize());
		sizeChanged();
		
	}
}
