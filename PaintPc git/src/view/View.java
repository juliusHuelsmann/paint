//package declaration
package view;

//import declarations
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import settings.Constants;
import settings.ViewSettings;
import start.utils.Utils;
import view.forms.New;
import view.forms.Page;
import view.forms.PaintObjects;
import view.forms.Tabs;
import view.forms.tabs.Paint;
import control.ControlPainting;

/**
 * singleton view class.
 * Contains all sub graphical user interface items.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public final class View extends JFrame {

	/**
	 * the only instance of this class.
	 */
	private static View instance = null;

	/**
	 * close button.
	 */
	private JButton jbtn_exit;
	
	
	/**
	 * JLabel which contains the program title (for start fade in).
	 * Only question of design (no real usage).
	 */
	private JLabel jlbl_title;
	
	/**
	 * Constructor: initialize JFrame, alter settings and initialize items.
	 */
	private View() { }
	
	/**
	 * initialize MainJFrame (add content).
	 */
	private void initialize() {
	    
        //initialize JFrame and alter settings
        super.setAlwaysOnTop(false);
        super.setFocusable(false);
        super.setLayout(null);
        super.setUndecorated(true);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setFullscreen();
        
        //fade in and show text.
        fadeIn();
        
        //form for creating new page.
        super.add(New.getInstance());

        //exit
        jbtn_exit = new JButton();
        jbtn_exit.setContentAreaFilled(false);
        jbtn_exit.setOpaque(false);
        jbtn_exit.addMouseListener(ControlPainting.getInstance());
        jbtn_exit.setBorder(null);
        jbtn_exit.setFocusable(false);
        super.add(jbtn_exit);

        //fade out
        fadeOut();
        
        //set some things visible and repaint the whole window.
        flip(true);
        repaint();

        /*
         * add tab, overview about paintObjects and Page
         */
        super.add(Tabs.getInstance());
        //super.add(PaintObjects.getInstance());
        super.add(Page.getInstance());

        //display tabs and page.
        Tabs.getInstance().setVisible(true);
        Page.getInstance().setVisible(true);
	}
	
	/**
	 * The maxcounter for design. Is used for fade in and out
	 */
	private final int dsgn_maxFadeIn = 200, dsgn_max_moveTitle = 200;
	
	
	private void fadeIn(){

        jlbl_title = new JLabel("Paint!");
        jlbl_title.setBounds(-350, 
                getHeight() / 2 - 75, 350, 150);
        jlbl_title.setOpaque(false);
        jlbl_title.setFont(new Font("Purisa", Font.BOLD + Font.ITALIC, 100));
        super.add(jlbl_title);
        

        super.setVisible(true);
        
        new Thread(){
            public void run(){
                for(int i = 0; i < dsgn_max_moveTitle; i ++){

                    jlbl_title.setBounds((int)(-350 + (getWidth() + 350)/2 * ((i)) / dsgn_max_moveTitle),
                            getHeight() / 2 - 75, 350, 150);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int max = 200;
                int maxSteps = 2;
                int lastPosition = -1;
                for(int anzSteps = 1; anzSteps <= maxSteps; anzSteps ++){
                for(int i = 0; i < max; i ++){

                    lastPosition = (int)(getWidth() / 2 - 175 + (90 / (anzSteps )/ Math.sqrt(anzSteps) * maxSteps * Math.sqrt(maxSteps))*Math.sin(2 * Math.PI* i / max ));
                    
                    jlbl_title.setBounds((int)(getWidth() / 2 - 175 + (60 / (anzSteps )/ Math.sqrt(anzSteps) * maxSteps * Math.sqrt(maxSteps))*Math.sin(2 * Math.PI* i / max )), 
                            getHeight() / 2 - 75, 350, 150);
                    
                    
                    
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                }
                

//                jlbl_title.setBounds(getWidth() / 2 - 175, 
//                        getHeight() / 2 - 75, 350, 150);
                for(int i = 0; i < dsgn_max_moveTitle; i ++){

                    jlbl_title.setBounds(lastPosition + (getWidth() - lastPosition) * ((i)) / dsgn_max_moveTitle,
                            getHeight() / 2 - 75, 350, 150);
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } .start();
                
        
        //fade in
        for(int i = 0; i < dsgn_maxFadeIn; i ++){

            super.getContentPane().setBackground(
                    new Color(255 - (255 - 75) * i / dsgn_maxFadeIn,
                            255 - (255 - 175) * i / dsgn_maxFadeIn, 
                            255 - (255 - 125) * i / dsgn_maxFadeIn));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        super.getContentPane().setBackground(new Color(75,175,125));
	}
	
	
	private void fadeOut(){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        
        //fade out
        for(int i = 0; i < dsgn_maxFadeIn; i ++){

            super.getContentPane().setBackground(
                    new Color(75 + (255 - 75) * i / dsgn_maxFadeIn,
                            175 + (255 - 175) * i / dsgn_maxFadeIn, 
                            125 + (255 - 125) * i / dsgn_maxFadeIn));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        jlbl_title.setVisible(false);
        super.getContentPane().setBackground(Color.white);
	}
	
	
	
	/**
	 * set FullscreenMode.
	 */
	private void setFullscreen() {

		//initialize instances
        GraphicsEnvironment ge 
        = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device 
        = ge.getDefaultScreenDevice();
 
        //if fullscreen is supported
        if (device.isFullScreenSupported()) {
            setUndecorated(true);
            device.setFullScreenWindow(this);
        } else {
            device.setFullScreenWindow(null);
        }
        
        repaint();
        setVisible(false);
        dispose();
        setUndecorated(false);
        repaint();
    
        if (isDisplayable()) {
            setVisible(false);
            dispose();
        }
        setUndecorated(true);
        repaint();
        
        device.setFullScreenWindow(this);
        setVisible(false);
	}



    
	
	/**
	 * apply the sizes of the items.
	 * @param _normalFlip the real size.
	 */
	public void flip(final boolean _normalFlip) {

        super.setSize(ViewSettings.VIEW_SIZE_JFRAME);
        super.setLocation(0, 0);

        Tabs.getInstance().setSize(
                ViewSettings.VIEW_WIDTH_TB, 
                ViewSettings.VIEW_HEIGHT_TB,
                ViewSettings.VIEW_HEIGHT_TB_VISIBLE);
        PaintObjects.getInstance().setSize(ViewSettings.VIEW_BOUNDS_PO.width, 
                ViewSettings.VIEW_BOUNDS_PO.height);
        Page.getInstance().setSize(ViewSettings.VIEW_BOUNDS_PAGE.width, 
                ViewSettings.VIEW_BOUNDS_PAGE.height);
	    
        //if not flipped
	    if (_normalFlip) {

	        
	        jbtn_exit.setBounds(ViewSettings.VIEW_BOUNDS_JBTN_EXIT);
	        PaintObjects.getInstance().setLocation(
	                ViewSettings.VIEW_BOUNDS_PO.getLocation());
	        Page.getInstance().setLocation(
	                ViewSettings.VIEW_BOUNDS_PAGE.getLocation());

	        jbtn_exit.setIcon(new ImageIcon(Utils.resizeImage(
	                jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
	                Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));
	        Tabs.getInstance().setLocation(ViewSettings.VIEW_LOCATION_TB);
	        
	    } else {
	        jbtn_exit.setLocation(
                    ViewSettings.VIEW_SIZE_JFRAME.width
                    - ViewSettings.VIEW_BOUNDS_JBTN_EXIT.x
                    - ViewSettings.VIEW_BOUNDS_JBTN_EXIT.height, 
	                ViewSettings.VIEW_SIZE_JFRAME.height 
	                - ViewSettings.VIEW_BOUNDS_JBTN_EXIT.height 
	                - ViewSettings.VIEW_BOUNDS_JBTN_EXIT.y);

	        jbtn_exit.setIcon(new ImageIcon(Utils.flipImage(
	                jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
	                Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));

	        PaintObjects.getInstance().setLocation(
	                ViewSettings.VIEW_BOUNDS_PO.x, 
                    ViewSettings.VIEW_SIZE_JFRAME.height 
                    - ViewSettings.VIEW_BOUNDS_PO.y 
                    - ViewSettings.VIEW_BOUNDS_PO.height);

            PaintObjects.getInstance().setVisible(false);
            Page.getInstance().setLocation(
                    ViewSettings.VIEW_SIZE_JFRAME.width
                    - ViewSettings.VIEW_BOUNDS_PAGE.x
                    - ViewSettings.VIEW_BOUNDS_PAGE.width,
                    ViewSettings.VIEW_SIZE_JFRAME.height
                    - ViewSettings.VIEW_BOUNDS_PAGE.y 
                    - ViewSettings.VIEW_BOUNDS_PAGE.height);
            Tabs.getInstance().setLocation(ViewSettings.VIEW_SIZE_JFRAME.width 
                    - ViewSettings.VIEW_WIDTH_TB, 0);
            
	    }

        Page.getInstance().flip(_normalFlip);
        Paint.getInstance().flip();
        Tabs.getInstance().flip(_normalFlip);
	}
	
	
    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static View getInstance() {
        
        //if class is not instanced yet instantiate
        if (instance == null) {
            instance = new View();
            instance.initialize();
        }
        
        //return the only instance of this class.
        return instance;
    }
    
    
    /*
     * getter methods
     */
    
	/**
	 * @return the jbtn_exit
	 */
	public JButton getJbtn_exit() {
		return jbtn_exit;
	}
}
