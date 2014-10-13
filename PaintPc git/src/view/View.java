//package declaration
package view;

//import declarations
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.settings.Constants;
import model.settings.Status;
import model.settings.ViewSettings;
import model.util.paint.Utils;
import view.forms.Message;
import view.forms.Page;
import view.forms.Tabs;
import view.forms.tabs.Paint;
import control.ControlPainting;
import control.util.MousePositionTracker;

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
	private JButton jbtn_exit, jbtn_fullscreen;

    /**
     * The maximum counter for design. Is used for fade in and out
     */
    private final int dsgn_maxFadeIn = 200, dsgn_max_moveTitle = 200;
	
	/**
	 * JLabel which contains the program title (for start fade in) and JLabel
	 * for painting border of JFrame if non-fullscreen mode is enabled.
	 * Only question of design (no real usage).
	 */
	private JLabel jlbl_title, jlbl_border;
	
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
        
        if (ViewSettings.isFullscreen()) {

            this.setFullscreen();
            //fade in and show text.
            fadeIn();

        } else {
            MousePositionTracker mpt = new MousePositionTracker(this);
            super.addMouseListener(mpt);
            super.addMouseMotionListener(mpt);
        }
        
        jlbl_border = new JLabel();
        jlbl_border.setOpaque(false);
        jlbl_border.setBorder(BorderFactory.createLineBorder(Color.gray));
        jlbl_border.setFocusable(false);
        super.add(jlbl_border);
        
        //exit
        jbtn_exit = new JButton();
        jbtn_exit.setContentAreaFilled(false);
        jbtn_exit.setOpaque(false);
        jbtn_exit.addMouseListener(ControlPainting.getInstance());
        jbtn_exit.setBorder(null);
        jbtn_exit.setFocusable(false);
        super.add(jbtn_exit);

        //exit
        jbtn_fullscreen = new JButton();
        jbtn_fullscreen.setContentAreaFilled(false);
        jbtn_fullscreen.setOpaque(false);
        jbtn_fullscreen.addMouseListener(ControlPainting.getInstance());
        jbtn_fullscreen.setBorder(null);
        jbtn_fullscreen.setFocusable(false);
        super.add(jbtn_fullscreen);

        if (ViewSettings.isFullscreen()) {

            //fade out
            fadeOut();
        }

        //set some things visible and repaint the whole window.
        flip(true);
        repaint();

        if (!ViewSettings.isFullscreen()) {
            super.getContentPane().setBackground(Color.white);
            super.setLocationRelativeTo(null);
        }
        /*
         * add Message form, tab, overview about paintObjects and Page
         */
        Message.addMyself();
        super.add(Tabs.getInstance());
        super.add(Page.getInstance());

        
        //display tabs and page.
        Tabs.getInstance().setVisible(true);
        Page.getInstance().setVisible(true);

	}
	

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
	public void setFullscreen() {

		//initialize instances
        GraphicsEnvironment ge 
        = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device 
        = ge.getDefaultScreenDevice();
 
        //if fullScreen modus is supported
        if (device.isFullScreenSupported()) {
            if (!isUndecorated()) {

                setUndecorated(true);
            }
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
        super.setSize(ViewSettings.getSizeJFrame());

        
        
        //initialize tabs
        Status.getLogger().info("   initialize Tabs\n");
        Tabs.getInstance().setSize(
                ViewSettings.getView_widthTb(), 
                ViewSettings.getView_heightTB(),
                ViewSettings.getView_heightTB_visible());
        
        //initialize PaintObjects
        Status.getLogger().info("   initialize PaintObjects\n");

        //initialize PaintObjects
        Status.getLogger().info("   initialize Page\n");
        Page.getInstance().setSize(ViewSettings.getView_bounds_page().width, 
                ViewSettings.getView_bounds_page().height);
        Page.getInstance().setLocation(
                ViewSettings.getView_bounds_page().getLocation());
	    
        //if not flipped
	    if (_normalFlip) {

            Page.getInstance().setLocation(
                    ViewSettings.getView_bounds_page().getLocation());

	        
	        jbtn_exit.setBounds(ViewSettings.getViewBoundsJbtnExit());
	        jbtn_exit.setIcon(new ImageIcon(Utils.resizeImage(
	                jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
	                Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));

            jbtn_fullscreen.setBounds(
                    ViewSettings.getView_bounds_jbtn_fullscreen());
	        jbtn_fullscreen.setIcon(new ImageIcon(Utils.resizeImage(
                    jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
                    Constants.VIEW_JBTN_FULLSCREEN_NORMAL_PATH)));
	        
	        Tabs.getInstance().setLocation(ViewSettings.VIEW_LOCATION_TB);
	        
	    } else {
	        jbtn_exit.setLocation(
                    ViewSettings.getSizeJFrame().width
                    - ViewSettings.getViewBoundsJbtnExit().x
                    - ViewSettings.getViewBoundsJbtnExit().height, 
	                ViewSettings.getSizeJFrame().height 
	                - ViewSettings.getViewBoundsJbtnExit().height 
	                - ViewSettings.getViewBoundsJbtnExit().y);

	        jbtn_exit.setIcon(new ImageIcon(Utils.flipImage(
	                jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
	                Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));

            Page.getInstance().setLocation(
                    ViewSettings.getSizeJFrame().width
                    - ViewSettings.getView_bounds_page().x
                    - ViewSettings.getView_bounds_page().width,
                    ViewSettings.getSizeJFrame().height
                    - ViewSettings.getView_bounds_page().y 
                    - ViewSettings.getView_bounds_page().height);
            Tabs.getInstance().setLocation(ViewSettings.getSizeJFrame().width 
                    - ViewSettings.getView_widthTb(), 0);
            
	    }

        Page.getInstance().flip();
        Paint.getInstance().flip();
        Tabs.getInstance().flip(_normalFlip);
        jlbl_border.setBounds(0, 0, getWidth(), getHeight());
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

    /**
     * @return the jbtn_fullscreen
     */
    public JButton getJbtn_fullscreen() {
        return jbtn_fullscreen;
    }
}
