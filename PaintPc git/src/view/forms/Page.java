//package declaration
package view.forms;

//import declarations
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.util.paint.Utils;
import settings.Status;
import settings.ViewSettings;
import view.View;
import view.util.VScrollPane;
import control.CSelection;
import control.ControlPainting;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public final class Page extends JPanel {

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
    private JPanel jpnl_toMove;
    
	/**
	 * two ScrollPanels for both dimensions.
	 */
	private VScrollPane sp_ub, sp_lr;

	/*
	 * Stuff for selection
	 */
	
    /**
     * JButtons for resizing the selection.
     */
    private JButton[][] jbtn_resize;


    /**
     * Selection JLabel.
     */
    private JLabel jlbl_selectionBG;

    /**
     * Selection JLabel.
     */
    private JLabel jlbl_selectionPainting;

    /**
     * The border JLabel.
     */
    private JLabel jlbl_border;
	
	/**
	 * empty utility class constructor. 
	 */
	private Page() { }

	
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
                        0, 0, 0, 1, ViewSettings.CLR_BORDER), 

                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, ViewSettings.CLR_BORDER),
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 1, Color.white))));
        super.setVisible(false);
        
        //initialize JPanel jpnl_toMove
        jpnl_toMove = new JPanel() {
            
            @Override public void setLocation(final int _x, final int _y) {

                jlbl_painting.setLocation(_x, _y);
            }
        };

        //ScrollPanel for up and down
        sp_ub = new VScrollPane(jpnl_toMove, this, true);
        View.getInstance().add(sp_ub);

        sp_lr = new VScrollPane(jpnl_toMove, this, false);
        View.getInstance().add(sp_lr);
        
        //initialize resize JButton
        jbtn_resize = new JButton[2 + 1][2 + 1];
        for (int x = 0; x < jbtn_resize.length; x++) {
            for (int y = 0; y < jbtn_resize[x].length; y++) {
        
                final int buttonSize = 10;
                
                jbtn_resize[x][y] = new JButton();
                jbtn_resize[x][y].setSize(buttonSize, buttonSize);
                jbtn_resize[x][y].setContentAreaFilled(false);
                jbtn_resize[x][y].setBorder(
                        BorderFactory.createLineBorder(Color.gray));
                jbtn_resize[x][y].setBackground(Color.white);
                jbtn_resize[x][y].setOpaque(true);
                jbtn_resize[x][y].addMouseMotionListener(
                        CSelection.getInstance());
                jbtn_resize[x][y].addMouseListener(
                        CSelection.getInstance());
                super.add(jbtn_resize[x][y]);
            }
        }
        
        
        //the center button
        jbtn_resize[1][1].setBorder(null);
        jbtn_resize[1][1].setSize((2 + 2 + 2) * (2 + 2 + 1),
                (2 + 2 + 2) * (2 + 2 + 1));
        jbtn_resize[1][1].setIcon(new ImageIcon(Utils.resizeImage(
                jbtn_resize[1][1].getWidth(), jbtn_resize[1][1].getHeight(),
                "centerResize.png")));
        
        jlbl_selectionBG = new JLabel();
        jlbl_selectionBG.setOpaque(false);
        jlbl_selectionBG.setFocusable(false);
        super.add(jlbl_selectionBG);
        
        jlbl_selectionPainting = new JLabel();
        jlbl_selectionPainting.setOpaque(false);
        jlbl_selectionPainting.setFocusable(false);
        super.add(jlbl_selectionPainting);

        jlbl_border = new JLabel();
        jlbl_border.setOpaque(true);
        jlbl_border.setBackground(new Color(0, 0, 0, 2 * 2 * 2 * 2 * 2));
        jlbl_border.setBorder(BorderFactory.createLineBorder(Color.black));
        super.add(jlbl_border);
        
        
        //JLabel for the painting and the raster
        jlbl_painting = new PaintLabel(jpnl_toMove);
        jlbl_painting.setBackground(Color.white);
        jlbl_painting.setFocusable(false);
        jlbl_painting.setBorder(null);
        jlbl_painting.addMouseMotionListener(ControlPainting.getInstance());
        jlbl_painting.addMouseListener(ControlPainting.getInstance());
        jlbl_painting.setOpaque(true);
        super.add(jlbl_painting);

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
	    
	    flip(true);
	}
	

	
	
	/**
	 * .
	 * @param _normalSize whether flipped or not
	 */
	public void flip(final boolean _normalSize) {

	    if (_normalSize) {

            jpnl_toMove.setBounds(0, 0,
                    Status.getImageShowSize().width,
                    Status.getImageShowSize().height);

	        sp_ub.setLocation(ViewSettings.VIEW_SIZE_JFRAME.width 
	                - sp_ub.getWidth(), ViewSettings.VIEW_SIZE_SP);

	        sp_lr.setLocation(0, ViewSettings.VIEW_SIZE_JFRAME.height 
	                - sp_lr.getHeight());
            

	    } else {

            sp_ub.setLocation(0, ViewSettings.VIEW_SIZE_SP);
            sp_lr.setLocation(ViewSettings.VIEW_SIZE_SP, 0);
 
	    }
        sp_ub.setSize(ViewSettings.VIEW_SIZE_SP, 
                ViewSettings.VIEW_SIZE_JFRAME.height 
                - ViewSettings.VIEW_SIZE_SP);
        sp_lr.setSize(ViewSettings.VIEW_SIZE_JFRAME.width, 
                ViewSettings.VIEW_SIZE_SP);

        jlbl_painting.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_selectionBG.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_selectionPainting.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
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
	        instance = new Page();
	        instance.initialize();
	    }
	    
	    //return the only instance of this class
	    return instance;
	}
	
	
	
	/**
	 * return fully transparent BufferedImage.
	 * 
	 * @return the BufferedImage
	 */
	public  BufferedImage getEmptyBI() {
	    BufferedImage bi = new BufferedImage(jlbl_selectionBG.getWidth(), 
	            jlbl_selectionBG.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    
	    int rgba = new Color(0, 0, 0, 0).getRGB();
	    for (int x = 0; x < bi.getWidth(); x++) {
	        for (int y = 0; y < bi.getHeight(); y++) {
	            bi.setRGB(x, y, rgba);
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
	    
	    jlbl_painting.stopBorderThread();
	    
        BufferedImage emptyBI = getEmptyBI();
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
    public JLabel getJlbl_selectionBG() {
        return jlbl_selectionBG;
    }
    

    /**
     * @return the jlbl_selectionPainting
     */
    public JLabel getJlbl_selectionPainting() {
        return jlbl_selectionPainting;
    }


    /**
     * @return the jbtn_resize
     */
    public JButton[][] getJbtn_resize() {
        return jbtn_resize;
    }


    /**
     * @return the jlbl_border
     */
    public JLabel getJlbl_border() {
        return jlbl_border;
    }
}

