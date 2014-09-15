//package declaration
package view.forms;

//import declarations
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import settings.Status;
import settings.ViewSettings;
import view.View;
import view.util.VScrollPane;
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

        //JLabel for the painting and the raster
        jlbl_painting = new PaintLabel(jpnl_toMove);
        jlbl_painting.setBackground(Color.white);
        jlbl_painting.setFocusable(false);
        jlbl_painting.setBorder(null);
        jlbl_painting.addMouseMotionListener(ControlPainting.getInstance());
        jlbl_painting.addMouseListener(ControlPainting.getInstance());
        jlbl_painting.setOpaque(true);
        super.add(jlbl_painting);


        //ScrollPanel for up and down
        sp_ub = new VScrollPane(jpnl_toMove, this, true);
        View.getInstance().add(sp_ub);

        sp_lr = new VScrollPane(jpnl_toMove, this, false);
        View.getInstance().add(sp_lr);
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

	
	/*
	 * getter methods
	 */

    /**
     * @return the jlbl_painting
     */
    public PaintLabel getJlbl_painting() {
        return jlbl_painting;
    }
}

