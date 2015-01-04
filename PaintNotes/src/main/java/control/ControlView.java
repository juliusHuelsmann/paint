package control;

//import declarations
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import control.tabs.ControlTabPainting;
import model.objects.painting.Picture;
import model.settings.Constants;
import model.settings.ReadSettings;
import model.settings.Settings;
import model.settings.Status;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import model.util.paint.Utils;
import view.View;
import view.forms.Tabs;
import view.tabs.Paint;


/**
 * Controller class for events handled for components directly added to instance
 * of JFrame View.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ControlView implements ActionListener, MouseListener {

	
	/**
	 * The saved instance of the view class.
	 */
	private final View view;
	
	
	
	/**
	 * Constructor: initializes the view class and reads settings.
	 */
	public ControlView(final View _view) {
		
		this.view = _view;
	}
	
	
	/**
	 * ActionListener for items that are directly added to the JFrame.
	 * 
	 * @param _event the actionEvent
	 */
	public final void actionPerformed(final ActionEvent _event) {
		if (_event.getSource().equals(view.getJbtn_exit())) {


			//if there are changes to be saved
			if (Status.isUncommittedChanges()) {

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

					ControlTabPainting.getInstance().mr_save();
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
                            .getJbtn_exit().getWidth(), View
                            .getInstance().getJbtn_exit().getHeight(),
                            Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
        } else if (_event.getSource().equals(
                view.getJbtn_fullscreen())) {
            view.getJbtn_fullscreen().setIcon(new ImageIcon(Utils
                    .resizeImage(view.getJbtn_exit().getWidth(), 
                            view.getJbtn_exit().getHeight(),
                                    Constants
                                    .VIEW_JBTN_FULLSCREEN_MOUSEOVER_PATH)));
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
          } else {

        	  //if the new window state is not fullscreen, apply custom size
        	  //of JFrame.
        	  ViewSettings.setSize_jframe(
        			  ViewSettings.getSizeViewWindow());
          }
          
          view.flip();
          view.repaint();
          Tabs.getInstance().repaint();
          Tabs.getInstance().openTab(0);
          
      
        
        } 
	}


}