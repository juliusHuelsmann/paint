//package declaration
package view;

//import declarations
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import model.settings.Constants;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.Util;
import model.util.paint.Utils;
import view.forms.Help;
import view.forms.InfoForm;
import view.forms.Loading;
import view.forms.Message;
import view.forms.Page;
import view.forms.Tabs;
import view.util.mega.MButton;
import view.util.mega.MFrame;
import view.util.mega.MLabel;
import control.ControlPaint;
import control.forms.ControlView;
import control.forms.minorImportance.InfoSelection;
import control.util.WindowMover;


/**
 * View class.
 * Contains all sub graphical user interface items; consists of MFrame.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public final class View extends MFrame {


	/**
	 * close button.
	 */
	private MButton jbtn_exit, jbtn_fullscreen;

    /**
     * The maximum counter for design. Is used for fade in and out
     */
    private final int dsgn_maxFadeIn = 150, dsgn_max_moveTitle = 100;
	
    
	/**
	 * MLabel which contains the program title (for start fade in) and MLabel
	 * for painting border of JFrame if non-fullscreen mode is enabled.
	 * Only question of design (no real usage).
	 */
	private MLabel jlbl_title, jlbl_border;
	
	/**
	 * The tab.
	 */
	private Tabs tabs;
	
	
	/**
	 * The loading view class.
	 */
	private Loading loading;
	/**
	 * The Page.
	 */
	private Page page;
	
	
	/**
	 * A help form which is made visible on mouseOver.
	 */
	private  Help help;
	
	private ControlView cv;
	
	/**
	 * Info form which delivers.
	 */
	private InfoForm info_form;
	
	/**
	 */
	public View() {
		
	}
	
	/**
	 * 
	 * Constructor: initialize JFrame, alter settings and initialize items.
	 * @param _cp instance of ControlView.
	 */
	public void initialize(final ControlPaint _cp) {
	    
        //initialize JFrame and alter settings
        super.setAlwaysOnTop(false);
        super.setFocusable(false);
        super.setLayout(null);
        super.setUndecorated(true);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);

        cv = new ControlView(this, _cp.getcTabPaint());
        
        
        MLabel jlbl_backgroundStroke = new MLabel();
        jlbl_backgroundStroke.setSize(getSize());
        jlbl_backgroundStroke.setVisible(true);
        super.add(jlbl_backgroundStroke);

        help = new Help(this);
        super.add(help);

        jlbl_border = new MLabel();
        jlbl_border.setOpaque(false);
        jlbl_border.setBorder(BorderFactory.createLineBorder(Color.gray));
        jlbl_border.setFocusable(false);
        super.add(jlbl_border);
        
        Thread t = null;
        if (ViewSettings.isFullscreen()) {

            this.setFullscreen();
            jlbl_backgroundStroke.setSize(getSize());
            Util.getRoughStroke(jlbl_backgroundStroke);
            
            //fade in and show text.
            t =  fadeIn();

        } else {
//            MousePositionTracker mpt = new MousePositionTracker(this);
//            super.addMouseListener(mpt);
//            super.addMouseMotionListener(mpt);

            WindowMover wmv = new WindowMover(this);
            wmv.setActivityListener(cv);
            super.addMouseListener(wmv);
            super.addMouseMotionListener(wmv);
            
        }

        
        //exit
        jbtn_exit = new MButton();
        jbtn_exit.setContentAreaFilled(false);
        jbtn_exit.setOpaque(false);
        jbtn_exit.addMouseListener(cv);
        jbtn_exit.addActionListener(cv);
        jbtn_exit.setBorder(null);
        jbtn_exit.setFocusable(false);
        super.add(jbtn_exit);

        //exit
        jbtn_fullscreen = new MButton();
        jbtn_fullscreen.setContentAreaFilled(false);
        jbtn_fullscreen.setOpaque(false);
        jbtn_fullscreen.addMouseListener(cv);
        jbtn_fullscreen.addActionListener(cv);
        jbtn_fullscreen.setBorder(null);
        jbtn_fullscreen.setFocusable(false);
        super.add(jbtn_fullscreen);

//        loading = new Loading();
//        super.add(loading);
        page = new Page(_cp);
        tabs = new Tabs(this);
        

        flip();
        super.setVisible(true);
        if (!ViewSettings.isFullscreen()) {
            super.getContentPane().setBackground(Color.white);
            super.setLocationRelativeTo(null);
        }
        
        info_form = new InfoForm();
        super.add(info_form);
        
        
        tabs.initialize(this, _cp);
        

        super.remove(jlbl_backgroundStroke);
        if (t != null) {

            if (ViewSettings.isFullscreen()) {

                //fade out
                fadeOut(t);
            }

        }
        //set some things visible and repaint the whole window.
        flip();
        repaint();

        /*
         * add Message form, tab, overview about paintObjects and Page
         */
        super.add(new Message());
        
        super.add(tabs);
        super.add(page);

        tabs.setVisible(true);
        page.setVisible(true);
        
//        setResizable(false);
        setResizable(true);
        
        //debug for testing view components.
        //writes analyse file to analyse.png
//        DebugUtil.performCheckComponents(
//        		 this, DebugUtil.CHECK_OP_IMAGE);

	}

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate() {

		if (cv != null
				&& (getWidth() != sizeChecked.width
				|| getHeight() != sizeChecked.height)) {
			
			setSize(getWidth(), getHeight());
			cv.activityOccurred(null);
		}
		super.validate();
	}
	



    @Override
	public void setLocation(final Point _pnt) {
    	int x = _pnt.x;
    	int y = _pnt.y;
//		System.out.println(x + "d..d" + y);
		super.setLocation(x, y);
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
	 * @return the thread of fading in.
	 */
	private Thread fadeIn() {

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
         * Whether to show animation or not.
         */
        final boolean fade = false;
        
        /**
         * The maximum mount of movements the MLabel performs.
         */
        final int maxAmountMovement = 2;
        
        /**
         * With this factor, the movement is increased.
         */
        final int movementEnforce = 60;
        
        if (!fade) {

        	return null;
        }
        //initialize the MLabel and set view visible
        jlbl_title = new MLabel("Paint!");
        jlbl_title.setBounds(title_start_x, title_start_y,
                title_start_width, title_start_height);
        jlbl_title.setOpaque(false);
        jlbl_title.setFont(new Font("Purisa", 
                Font.BOLD + Font.ITALIC, font_size));
        super.add(jlbl_title);
        super.setVisible(true);
        
        Thread t_waitFor = new Thread() {
        	public void run() {

        		//fade in
                for (int i = 0; i < dsgn_maxFadeIn; i++) {
                    getContentPane().setBackground(new Color(
                    		Color.white.getRed() - (Color.white.getRed() 
                    				- clr_bg.getRed()) * i / dsgn_maxFadeIn,
                    				Color.white.getGreen() 
                    				- (Color.white.getGreen() 
                    						- clr_bg.getGreen()) 
                    						* i / dsgn_maxFadeIn, 
                    						Color.white.getBlue() 
                    						- (Color.white.getBlue()
                                            - clr_bg.getBlue()) 
                                            * i / dsgn_maxFadeIn));
                    try {
                        Thread.sleep(2 * (2 + 2 + 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //set background color.
                getContentPane().setBackground(clr_bg);
        	}
        };

        t_waitFor.start();
        //move MLabel into the graphical user interface.
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
         * The last position of the title-MLabel. Used for 
         */
        int lastPosition = -1;

        //let MLabel swing.
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
                            Thread.sleep(2 + 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                //move MLabel out of the graphical user interface.
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
        return t_waitFor;
	}
	
	
	
	/**
	 * fade the gui elements out.
	 * @param _t_waintFor the thread for which the view waits.
	 */
	private void fadeOut(final Thread _t_waintFor) {
        
		new Thread() {
			public void run() {
				final int time = 200;
			    
			    
			    while (_t_waintFor.isAlive()) {
			        //sleep for a while
			        try {
			            Thread.sleep(time);
			        } catch (InterruptedException e) {
			            e.printStackTrace();
			        }
			    }
		        
		        //fade out
		        for (int i = 0; i < dsgn_maxFadeIn; i++) {

		            getContentPane().setBackground(
		                    new Color(clr_bg.getRed() + (Color.white.getRed() 
		                            - clr_bg.getRed()) * i / dsgn_maxFadeIn,
		                            clr_bg.getGreen() + (Color.white.getGreen() 
		                                    - clr_bg.getGreen()) 
		                                    * i / dsgn_maxFadeIn, 
		                            clr_bg.getBlue() + (Color.white.getBlue() 
		                                    - clr_bg.getBlue()) 
		                                    * i / dsgn_maxFadeIn));
		            try {
		                Thread.sleep(2 * (2 + 2 + 1));
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }

			    while (!model.settings.State.isInitializationFinished()) {
			        //sleep for a while
			        try {
			            Thread.sleep(time * 2);
			        } catch (InterruptedException e) {
			            e.printStackTrace();
			        }
			    }
		        jlbl_title.setVisible(false);
		        getContentPane().setBackground(Color.white);
		        requestFocus();
			}
		} .start();
	    
        
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
	 * set FullscreenMode.
	 */
	public void setNotFullscreen() {

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
            device.setFullScreenWindow(null);
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
        
        device.setFullScreenWindow(null);
        setVisible(false);
	}


	/**
	 * apply the sizes of the items.
	 */
	public void flip() {

	    //set gui bounds
		sizeChecked = new Dimension(ViewSettings.getSizeJFrame());
        super.setSize(ViewSettings.getSizeJFrame());

        if (loading != null) {
            loading.setBounds(ViewSettings.getView_bounds_loading());
        }
        
        //initialize tabs
        State.getLogger().info("   initialize Tabs\n");
        
        if (tabs != null) {
        	
        	tabs.reApplySize();
        	tabs.setSize(
                    ViewSettings.getView_widthTb(), 
                    ViewSettings.getView_heightTB(),
                    ViewSettings.getView_heightTB_visible());
            tabs.setLocation(ViewSettings.VIEW_LOCATION_TB);
            
        } else {
        	State.getLogger().severe("tabs at flip null. Debugging?");
        }
        
        //initialize PaintObjects
        State.getLogger().info("   initialize PaintObjects\n");

        //initialize PaintObjects
        State.getLogger().info("   initialize Page\n");
        
	    
	        	
        jbtn_exit.setBounds(ViewSettings.getViewBoundsJbtnExit());
        jbtn_exit.setIcon(new ImageIcon(Utils.resizeImage(
        		jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
        		Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));
        
        jbtn_fullscreen.setBounds(
        		ViewSettings.getView_bounds_jbtn_fullscreen());
        jbtn_fullscreen.setIcon(new ImageIcon(Utils.resizeImage(
        		jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
        		Constants.VIEW_JBTN_FULLSCREEN_NORMAL_PATH)));
	        	
	        	
	        //the page flip is done directly inside the set size method
//	        Remove
        

        if (page != null) {

            page.setLocation(
                    ViewSettings.getView_bounds_page().getLocation());
            page.setSize(ViewSettings.getView_bounds_page().width, 
                    ViewSettings.getView_bounds_page().height);
        } else {
        	State.getLogger().severe("page at flip null. Debugging?");
        }

        if (tabs != null) {

            tabs.flip();	
        }
        jlbl_border.setBounds(0, 0, getWidth(), getHeight());
	}
	
	
	/**
	 * Dimension for checking which size has been reported.
	 */
	private Dimension sizeChecked = new Dimension(0, 0);
	/**
	 * Method for setting size of view and its content.
	 * 
	 * @param _width the widht
	 * @param _height the height
	 */
	public void setSize(final int _width, final int _height) {

		sizeChecked = new Dimension(_width, _height);
		ViewSettings.setSize_jframe(sizeChecked);
		
		//this is done because viewSettings decides whether to accept _width and
		//_height or not.
		super.setSize(ViewSettings.getSize_jframe().width, 
				ViewSettings.getSize_jframe().height);
	}
	
    
    /*
     * getter methods
     */
    
	/**
	 * @return the jbtn_exit
	 */
	public MButton getJbtn_exit() {
		return jbtn_exit;
	}

    /**
     * @return the jbtn_fullscreen
     */
    public MButton getJbtn_fullscreen() {
        return jbtn_fullscreen;
    }

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @return the tabs
	 */
	public Tabs getTabs() {
		return tabs;
	}

	
	/**
	 * @return the Loading.
	 */
	public Loading getLoading() {
		return loading;
	}

	/**
	 * @return the help
	 */
	public Help getHelp() {
		return help;
	}

	/**
	 * @return the info_form
	 */
	public InfoForm getInfo_form() {
		return info_form;
	}

}
