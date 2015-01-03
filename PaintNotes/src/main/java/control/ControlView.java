package control;

//import declarations
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import control.tabs.CPaintStatus;
import control.tabs.CPaintVisualEffects;
import model.objects.painting.Picture;
import model.objects.pen.normal.BallPen;
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
	public ControlView() {
		

        // get location of current workspace
        Settings.setWsLocation(ReadSettings.install());

        //set logger level to finest; thus every log message is shown.
        Status.getLogger().setLevel(Level.WARNING);

        // if installed
        if (!Settings.getWsLocation().equals("")) {
            
            Status.getLogger().info("installation found.\n");

            /*
             * initialize model
             */

            Status.getLogger().info(
                    "initialize model class Pen and current pen.\n");
            
            Picture.getInstance().initializePen(
                    new BallPen(Constants.PEN_ID_POINT, 1, Color.black));
            Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_PAINT_1);
            Paint.getInstance().getTb_color1().setActivated(true);
            Paint.getInstance().getIt_stift1().getTb_open().setActivated(true);
            


            /*
             * initialize view.
             */
            Status.getLogger().info("initialize view class and set visible.");
            view = View.getInstance();
            view.setVisible(true);

            
            /*
             * Initialize control
             */
            Status.getLogger().info("initialize controller class.");
            ControlPainting.getInstance();
            
            Status.getLogger().info(
                    "Start handling actions and initialize listeners.\n");

            Status.getLogger().info("initialization process completed.\n\n"
                    + "-------------------------------------------------\n");
        } else {

            // if not installed and no installation done
        	view = null;
            System.exit(1);
        }
	}
	
	
	/**
	 * ActionListener for items that are directly added to the JFrame.
	 * 
	 * @param _event the actionEvent
	 */
	public final void actionPerformed(final ActionEvent _event) {
		if (_event.getSource().equals(view.getJbtn_exit())) {


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
					
					System.out.println("yes" + answer);
					break;
				case JOptionPane.NO_OPTION:
					System.out.println("no" + answer);
					break;
				case JOptionPane.CANCEL_OPTION:
					System.out.println("cancel" + answer);
					break;
				default:
					break;
				}
				if (answer == 0) {
					// okay
					ControlPainting.getInstance().mr_save();
				} else if (answer == 1) {
					
					// no
					System.exit(1);
				} 
				//i == 2 ^ interrupt
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
