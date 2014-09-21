//package declaration
package view;

//import declarations
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import settings.Constants;
import settings.Status;
import settings.ViewSettings;
import start.utils.Utils;
import view.forms.New;
import view.forms.Page;
import view.forms.Tabs;
import view.forms.tabs.Paint;
import view.forms.tabs.PaintObjects;
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
        super.add(Page.getInstance());

        //display tabs and page.
        Tabs.getInstance().setVisible(true);
        Page.getInstance().setVisible(true);
	}
	
	/**
	 * The maximum counter for design. Is used for fade in and out
	 */
	private final int dsgn_maxFadeIn = 200, dsgn_max_moveTitle = 200;
	

    /**
     * Background color fading in at startup.
     */
    private final Color clr_bg 
//    = new Color(75, 175, 125);
//    = new Color(75, 115, 165);
    = new Color(55, 95, 135);
    
	
	/**
	 * fade in graphical user interface elements.
	 */
	private void fadeIn() {

	    //initialize final values
	    
	    /**
	     * The bounds of the title at the beginning
	     */
	    final int title_start_width = 350, 
	            title_start_height = 150,
	            title_start_x = -350, 
	            title_start_y = getHeight() / 2 - title_start_height / 2;
	    
	    /**
	     * Size of the font.
	     */
	    final int font_size = 100;

	    /**
	     * The maximum amount of loops.
	     */
        final int maxLoop = 200;
        
        /**
         * The maximum mount of movements the JLabel performs.
         */
        final int maxAmountMovement = 2;
        
        /**
         * With this factor, the movement is increased.
         */
        final int movementEnforce = 60;
        
        //initialize the JLabel and set view visible
        jlbl_title = new JLabel("Paint!");
        jlbl_title.setBounds(title_start_x, title_start_y,
                title_start_width, title_start_height);
        jlbl_title.setOpaque(false);
        jlbl_title.setFont(new Font("Purisa", 
                Font.BOLD + Font.ITALIC, font_size));
        super.add(jlbl_title);
        super.setVisible(true);
        
        /*
         * move title
         */
        new Thread() {
            public void run() {

                //move JLabel into the graphical user interface.
                for (int i = 0; i < dsgn_max_moveTitle; i++) {
                    jlbl_title.setBounds((int) (title_start_x 
                            + (getWidth() + title_start_width) / 2 * ((i))
                            / dsgn_max_moveTitle),
                            title_start_y,
                            title_start_width, title_start_height);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                
                /**
                 * The last position of the title-JLabel. Used for 
                 */
                int lastPosition = -1;

                //let JLabel swing.
                for (int anzSteps = 1; 
                        anzSteps <= maxAmountMovement; 
                        anzSteps++) {
                    
                    for (int i = 0; i < maxLoop; i++) {
    
                        lastPosition = 
                                (int) ((getWidth() - title_start_width) / 2 
                                + (movementEnforce / anzSteps 
                                        / Math.sqrt(anzSteps) 
                                        * maxAmountMovement 
                                        * Math.sqrt(maxAmountMovement))
                                        * Math.sin(2 * Math.PI * i / maxLoop));
                         
                        jlbl_title.setBounds(
                                (int) ((getWidth() - title_start_width) / 2 
                                        + (movementEnforce / (anzSteps) 
                                                / Math.sqrt(anzSteps)
                                                * maxAmountMovement 
                                                * Math.sqrt(maxAmountMovement))
                                                * Math.sin(2 * Math.PI
                                                        * i / maxLoop)),
                                                        title_start_y,
                                                        title_start_width, 
                                                        title_start_height);
                        
                        try {
                            Thread.sleep(2 + 2 + 1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                //move JLabel out of the graphical user interface.
                for (int i = 0; i < dsgn_max_moveTitle; i++) {

                    jlbl_title.setBounds(
                            lastPosition 
                            + (getWidth() - lastPosition) 
                            * ((i)) / dsgn_max_moveTitle,
                            title_start_y,
                            title_start_width, 
                            title_start_height);
                    try {
                        Thread.sleep(2 + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } .start();
                
        
        //fade in
        for (int i = 0; i < dsgn_maxFadeIn; i++) {

            super.getContentPane().setBackground(
                    new Color(Color.white.getRed() - (Color.white.getRed() 
                            - clr_bg.getRed()) * i / dsgn_maxFadeIn,
                            Color.white.getGreen() - (Color.white.getGreen() 
                                    - clr_bg.getGreen()) * i / dsgn_maxFadeIn, 
                            Color.white.getBlue() - (Color.white.getBlue()
                                    - clr_bg.getBlue()) * i / dsgn_maxFadeIn));
            try {
                Thread.sleep(2 * (2 + 2 + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //set background color.
        super.getContentPane().setBackground(clr_bg);
	}
	
	
	
	/**
	 * fade the gui elements out.
	 */
	private void fadeOut() {
        
	    final int time = 2000;
	    
        //sleep for a while
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        //fade out
        for (int i = 0; i < dsgn_maxFadeIn; i++) {

            super.getContentPane().setBackground(
                    new Color(clr_bg.getRed() + (Color.white.getRed() 
                            - clr_bg.getRed()) * i / dsgn_maxFadeIn,
                            clr_bg.getGreen() + (Color.white.getGreen() 
                                    - clr_bg.getGreen()) * i / dsgn_maxFadeIn, 
                            clr_bg.getBlue() + (Color.white.getBlue() 
                                    - clr_bg.getBlue()) * i / dsgn_maxFadeIn));
            try {
                Thread.sleep(2 * (2 + 2 + 1));
            } catch (InterruptedException e) {
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
 
        //if fullScreen modus is supported
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

	    //set gui bounds
        super.setSize(ViewSettings.VIEW_SIZE_JFRAME);
        super.setLocation(0, 0);

        
        
        //initialize tabs
        Status.getLogger().info("   initialize Tabs\n");
        Tabs.getInstance().setSize(
                ViewSettings.VIEW_WIDTH_TB, 
                ViewSettings.VIEW_HEIGHT_TB,
                ViewSettings.VIEW_HEIGHT_TB_VISIBLE);
        
        //initialize PaintObjects
        Status.getLogger().info("   initialize PaintObjects\n");

        //initialize PaintObjects
        Status.getLogger().info("   initialize Page\n");
        Page.getInstance().setSize(ViewSettings.VIEW_BOUNDS_PAGE.width, 
                ViewSettings.VIEW_BOUNDS_PAGE.height);
	    
        //if not flipped
	    if (_normalFlip) {

	        
	        jbtn_exit.setBounds(ViewSettings.VIEW_BOUNDS_JBTN_EXIT);
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
