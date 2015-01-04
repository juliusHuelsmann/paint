//package declaration
package view.forms;

//import declarations
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import model.settings.Constants;
import model.settings.Status;
import model.settings.ViewSettings;
import model.util.paint.Utils;
import view.View;
import view.util.VScrollPane;
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import control.ControlPaintSelectin;
import control.ControlPaint;
import control.ControlView;
import control.tabs.ControlTabPainting;
import view.util.mega.MButton;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public final class Page extends MPanel {

    /*
     * Only instance of this class (singleton)
     */
    
    /**
     * The only instance of this page.
     */
    private static Page instance;
    
    /*
     * JLabel which is being painted
     */
    
	/**
	 * JLabel on which is painted and JLabel for background (e.g. raster).
	 */
	private PaintLabel jlbl_painting;
	
	/*
	 * Stuff for scrolling the JLabel
	 */

    /**
     * Internal panel which is not added to graphical user interface.
     * The panel serves to be able to scroll and handles the location change 
     * further to the Painting JLabel
     */
    private MPanel jpnl_toMove;
    
	/**
	 * two ScrollPanels for both dimensions.
	 */
	private VScrollPane sp_ub, sp_lr;

	/*
	 * Stuff for selection
	 */
	
    /**
     * MButtons for resizing the selection.
     */
    private MButton[][] jbtn_resize;

    /**
     * Contains the size of a selection while resizing.
     */
    private MLabel jlbl_resizeSelectionSize;

    /**
     * Selection JLabel.
     */
    private MLabel jlbl_selectionBG;

    /**
     * Selection JLabel.
     */
    private MLabel jlbl_selectionPainting;

    /**
     * The border JLabel.
     */
    private MLabel jlbl_border;
	
    /**
     * The background JLabel (which is shown if image is transparent).
     */
    private MLabel jlbl_background, jlbl_background2;
    
    /**
     * 
     */
    private New jpnl_new;
    
    /**
     * 
     */
    private ControlPaint controlPaint;
    
	/**
	 * empty utility class constructor. 
	 */
	public Page(final ControlPaint _cv) {
		instance = this;
		this.controlPaint = _cv;
		initialize();
	}

	
	/**
	 * initializes graphical user interface components
     * and thus creates view. Called directly after creation.
	 */
	private void initialize() {

        //alter settings
        super.setOpaque(true);
        super.setBackground(Color.white);
        super.setLayout(null);
        super.setFocusable(false);
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 0, 1, ViewSettings.GENERAL_CLR_BORDER), 

                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, ViewSettings.GENERAL_CLR_BORDER),
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 1, Color.white))));
        super.setVisible(false);

        //form for creating new page.
        jpnl_new = new New();
        super.add(jpnl_new);
        super.add(Console.getInstance());
        super.add(QuickAccess.getInstance());
        
        //initialize JPanel jpnl_toMove
        jpnl_toMove = new MPanel() {
            
            @Override public void setLocation(final int _x, final int _y) {

            	//in here the ScrollPane is changing the location of the 
            	//painting label
                jlbl_painting.setLocation(_x, _y);
            }
        };

        //ScrollPanel for up and down
        sp_ub = new VScrollPane(jpnl_toMove, this, true);
        View.getInstance().add(sp_ub);

        sp_lr = new VScrollPane(jpnl_toMove, this, false);
        View.getInstance().add(sp_lr);
        
        jlbl_resizeSelectionSize = new MLabel();
        jlbl_resizeSelectionSize.setOpaque(true);
        jlbl_resizeSelectionSize.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_resizeSelectionSize.setBackground(Color.white);
        jlbl_resizeSelectionSize.setForeground(Color.black);
        jlbl_resizeSelectionSize.setSize((int) Math.pow(2, 2 + 2 + 2 + 1), 
                (int) Math.pow(2, 2 + 2 + 1));
        jlbl_resizeSelectionSize.setLocation(
                -jlbl_resizeSelectionSize.getWidth(), 
                -jlbl_resizeSelectionSize.getHeight());
        jlbl_resizeSelectionSize.setBorder(BorderFactory.createLineBorder(
                Color.black));
        super.add(jlbl_resizeSelectionSize);
        
        //initialize resize MButton
        jbtn_resize = new MButton[2 + 1][2 + 1];
        for (int x = 0; x < jbtn_resize.length; x++) {
            for (int y = 0; y < jbtn_resize[x].length; y++) {
        
                final int buttonSize = 10;
                
                jbtn_resize[x][y] = new MButton();
                jbtn_resize[x][y].setSize(buttonSize, buttonSize);
                jbtn_resize[x][y].setContentAreaFilled(false);
                jbtn_resize[x][y].setBorder(
                        BorderFactory.createLineBorder(Color.gray));
                jbtn_resize[x][y].setBackground(Color.white);
                jbtn_resize[x][y].setOpaque(true);
                jbtn_resize[x][y].addMouseMotionListener(
                		controlPaint.getControlPaintSelection());
                jbtn_resize[x][y].addMouseListener(controlPaint);
                super.add(jbtn_resize[x][y]);
            }
        }
        
        
        //the center button
        jbtn_resize[1][1].setBorder(null);
        jbtn_resize[1][1].setSize((2 + 2 + 2) * (2 + 2 + 1),
                (2 + 2 + 2) * (2 + 2 + 1));
        jbtn_resize[1][1].setIcon(new ImageIcon(Utils.resizeImage(
                jbtn_resize[1][1].getWidth(), jbtn_resize[1][1].getHeight(),
                Constants.VIEW_JBTN_RESIZE_PATH)));
        jbtn_resize[1][1].setOpaque(false);
        
        jlbl_selectionBG = new MLabel();
        jlbl_selectionBG.setOpaque(false);
        jlbl_selectionBG.setFocusable(false);
        super.add(jlbl_selectionBG);
        
        jlbl_selectionPainting = new MLabel();
        jlbl_selectionPainting.setOpaque(false);
        jlbl_selectionPainting.setFocusable(false);
        super.add(jlbl_selectionPainting);

        //Border MLabel added to 
        jlbl_border = new MLabel();
        jlbl_border.setOpaque(true);
        jlbl_border.setBackground(new Color(0, 0, 0, 2 * 2 * 2 * 2 * 2));
        jlbl_border.setBorder(BorderFactory.createLineBorder(Color.black));
        super.add(jlbl_border);

        jlbl_background2 = new MLabel();
        jlbl_background2.setBackground(Color.white);
        jlbl_background2.setFocusable(false);
        jlbl_background2.setBorder(null);
        jlbl_background2.setOpaque(false);
        super.add(jlbl_background2); 
        
        //JLabel for the painting and the raster
        jlbl_painting = new PaintLabel(jpnl_toMove);
        jlbl_painting.setFocusable(false);
        jlbl_painting.setBorder(null);
        jlbl_painting.addMouseMotionListener(controlPaint);
        jlbl_painting.addMouseListener(controlPaint);
        jlbl_painting.setOpaque(false);
        super.add(jlbl_painting);


        jlbl_background = new MLabel();
        jlbl_background.setBackground(Color.white);
        jlbl_background.setFocusable(false);
        jlbl_background.setBorder(null);
        jlbl_background.setOpaque(true);
        super.add(jlbl_background); 

        
        removeButtons();

	}
	

    
    
    /**
     * recalculate center bounds of ScrollPanes (if location has changed 
     * without using the ScrollPanes).
     */
    public void refrehsSps() {
        sp_ub.recalculateCenterBounds();
        sp_lr.recalculateCenterBounds();
    }
    
    
    
    /**
     * hide the buttons.
     */
    public void removeButtons() { 
        final int newLocation = -100;
        for (int a = 0; a < jbtn_resize.length; a++) {
            for (int b = 0; b < jbtn_resize[a].length; b++) {
                jbtn_resize[a][b].setLocation(newLocation, newLocation);
            }
        }
    }
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public void setSize(final int _width, final int _height) {
		
	    //set standard size
	    super.setSize(_width, _height);
	    
	    flip();
	}
	

	
	
	/**
	 * .
	 */
	public void flip() {

            jpnl_toMove.setBounds(0, 0,
                    Status.getImageShowSize().width,
                    Status.getImageShowSize().height);

	        sp_ub.setLocation(ViewSettings.getSizeJFrame().width 
	                - sp_ub.getWidth(), ViewSettings.VIEW_SIZE_SP);

	        sp_lr.setLocation(0, ViewSettings.getSizeJFrame().height 
	                - sp_lr.getHeight());
            

        sp_ub.setSize(ViewSettings.VIEW_SIZE_SP, 
                ViewSettings.getSizeJFrame().height 
                - ViewSettings.VIEW_SIZE_SP);
        sp_lr.setSize(ViewSettings.getSizeJFrame().width, 
                ViewSettings.VIEW_SIZE_SP);

        jlbl_painting.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_background.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_background2.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_selectionBG.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_selectionPainting.setBounds(0, 0, getWidth() - 1, getHeight() - 1);

        jlbl_background.setIcon(new ImageIcon(Status.getBi_transparency()));
	}
	
	

    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
	public static synchronized Page getInstance() {
	    
	    //if not initialized yet initialize
	    if (instance == null) {

	        //create instance and initialize
	    }
	    
	    //return the only instance of this class
	    return instance;
	}
	
	

    /**
     * return fully transparent BufferedImage.
     * 
     * @return the BufferedImage
     */
    public  BufferedImage getEmptyBISelection() {
        BufferedImage bi = new BufferedImage(jlbl_selectionBG.getWidth(), 
                jlbl_selectionBG.getHeight(), BufferedImage.TYPE_INT_ARGB);
        final int maxRGB = 255;
        int rgba = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, rgba);
            }
        }
        
        return bi;
    }
    /**
     * return fully transparent BufferedImage.
     * 
     * @return the BufferedImage
     */
    public  BufferedImage getEmptyBITransparent() {
        BufferedImage bi = new BufferedImage(
                Status.getImageSize().width, 
                Status.getImageSize().height, BufferedImage.TYPE_INT_ARGB);
        final int maxRGB = 255;
        
        int rgba = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, rgba);
            }
        }
        
        return bi;
    }
    
    /**
     * return fully transparent BufferedImage.
     * 
     * @return the BufferedImage
     */
    public  BufferedImage getEmptyBIWhite() {
        BufferedImage bi = new BufferedImage(
                Status.getImageSize().width, 
                Status.getImageSize().height, BufferedImage.TYPE_INT_RGB);
        
        final int max = 255;
        int rgb = new Color(max, max, max).getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, rgb);
            }
        }
        
        return bi;
    }
	
	
	/**
	 * Release selected items and add them to normal list.
	 */
	public void releaseSelected() {

	    for (int i = 0; i < jbtn_resize.length; i++) {
	        for (int j = 0; j < jbtn_resize[i].length; j++) {
	            int width = jbtn_resize[i][j].getWidth();

	            jbtn_resize[i][j].setLocation(-width - 1, -1);
	        }
	    }
	    //method for setting the MButtons to the size of the entire image.
	    ControlTabPainting.getInstance().updateResizeLocation();
	    
	    jlbl_painting.stopBorderThread();
	    
        BufferedImage emptyBI = getEmptyBISelection();
        jlbl_selectionBG.setIcon(new ImageIcon(emptyBI));
        jlbl_selectionPainting.setIcon(new ImageIcon(emptyBI));
        jlbl_selectionPainting.repaint();

        jlbl_border.setBounds(0, 0, 0, 0);
        jlbl_selectionBG.setLocation(0, 0);
        jlbl_selectionPainting.setLocation(0, 0);
        
        jlbl_painting.refreshPaint();
	}



	
	/*
	 * getter methods
	 */
	

    /**
     * @return the jlbl_painting
     */
    public PaintLabel getJlbl_painting() {
        return jlbl_painting;
    }


    /**
     * @return the jlbl_selection
     */
    public MLabel getJlbl_selectionBG() {
        return jlbl_selectionBG;
    }
    

    /**
     * @return the jlbl_selectionPainting
     */
    public MLabel getJlbl_selectionPainting() {
        return jlbl_selectionPainting;
    }


    /**
     * @return the jbtn_resize
     */
    public MButton[][] getJbtn_resize() {
        return jbtn_resize;
    }


    /**
     * @return the jlbl_border
     */
    public MLabel getJlbl_border() {
        return jlbl_border;
    }


    /**
     * @return the jlbl_background
     */
    public MLabel getJlbl_background() {
        return jlbl_background;
    }



    /**
     * @return the jlbl_background
     */
    public MLabel getJlbl_background2() {
        return jlbl_background2;
    }


    /**
     * @return the jlbl_resizeSelectionSize
     */
    public MLabel getJlbl_resizeSelectionSize() {
        return jlbl_resizeSelectionSize;
    }


	/**
	 * @return the jpnl_new
	 */
	public New getJpnl_new() {
		return jpnl_new;
	}


	/**
	 * @param jpnl_new the jpnl_new to set
	 */
	public void setJpnl_new(New jpnl_new) {
		this.jpnl_new = jpnl_new;
	}
}

