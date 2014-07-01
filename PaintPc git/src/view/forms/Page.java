//package declaration
package view.forms;

//import declarations
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
@SuppressWarnings("serial") public final class Page extends JPanel 
implements Observer {

	/**
	 * JLabel on which is painted and JLabel for background (e.g. raster).
	 */
	private SPaintLabel jlbl_painting;
	
	/**
	 * two ScrollPanels for both dimensions.
	 */
	private VScrollPane sp_ub, sp_lr;
	
	/**
	 * Panel to be moved.
	 */
	private JPanel jpnl_toMove;

	
	/**
	 * The only instance of this page.
	 */
	private static Page instance;
	
	
	
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
        
        //initialize JPanel jpnl_toMove
        jpnl_toMove = new JPanel() {
            
            @Override public void setLocation(final int _x, final int _y) {

                super.setLocation(_x, _y);
                jlbl_painting.setLocation(_x, _y);
//                jlbl_paintBG.setLocation(_x, _y);
            }
        };
        jpnl_toMove.setOpaque(true);
        jpnl_toMove.setBackground(Color.white);
        jpnl_toMove.setLayout(null);
        jpnl_toMove.setBorder(BorderFactory.createEtchedBorder(
                Color.darkGray, Color.lightGray));
        jpnl_toMove.setBorder(null);
        jpnl_toMove.setIgnoreRepaint(true);
        jpnl_toMove.setFocusable(false);
        
        //add selection JPanels
//        super.add(Zoom.getInstance());

        //JLabel for the painting and the raster
        jlbl_painting = new SPaintLabel();
        jlbl_painting.setBackground(Color.white);
        jlbl_painting.setFocusable(false);
        jlbl_painting.setBorder(null);
        jlbl_painting.addMouseMotionListener(ControlPainting.getInstance());
        jlbl_painting.addMouseListener(ControlPainting.getInstance());
        super.add(jlbl_painting);


        //ScrollPanel for up and down
        sp_ub = new VScrollPane(jpnl_toMove, this, true);
        View.getInstance().add(sp_ub);

        sp_lr = new VScrollPane(jpnl_toMove, this, false);
        View.getInstance().add(sp_lr);
        
//        super.add(jpnl_toMove);
	}
	
	
	public void screenLock(boolean _b){
	    
//	    if(_b){
//	        jlbl_painting.setVisible(false);
//	        Rectangle screenSize = new Rectangle( Toolkit.getDefaultToolkit().getScreenSize() );
//	        jlbl_screenLock.setIcon(new ImageIcon(ControlPainting.getRobot().createScreenCapture( screenSize )));
//	    } 
//        jlbl_screenLock.setVisible(_b);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public void setSize(final int _width, final int _height) {
		
	    //set standard size
	    super.setSize(_width, _height);
		
		//flip
		flip(true);
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
	@Override public void repaint() {
	    
	    //repaint this and JFrame
		super.repaint();
	}

	/**
	 * update method, prints new icon to screen.
	 * @param _o the observable
	 * @param _arg the argument
	 */
	@Override public void update(final Observable _o, final Object _arg) {
		
//		BufferedImage ls = (BufferedImage) _arg;
//		jlbl_painting.setIcon(new ImageIcon(ls));
//		View.getInstance().repaint();
	}
	
	
	/**
	 * .
	 * @param _normalSize whether flipped or not
	 */
	public void flip(final boolean _normalSize) {

        jlbl_painting.setSize(getWidth() - 1, getHeight() - 1);

	    if (_normalSize) {

            jpnl_toMove.setBounds(0, 0,
                    Status.getImageShowSize().width,
                    Status.getImageShowSize().height);
	        
	        jlbl_painting.setLocation(0, 0);
	     
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

        //change size of components
//        jlbl_paintBG.setIcon(new ImageIcon(Utils.getRastarImage(
//                        jlbl_paintBG.getWidth(), jlbl_paintBG.getHeight())));
//
//        sp_ub.reload();
//        sp_lr.reload();
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
     * @return the jlbl_painting
     */
    public SPaintLabel getJlbl_painting() {
        return jlbl_painting;
    }


    /**
     * @param _jlbl_painting the jlbl_painting to set
     */
    public void setJlbl_painting(final SPaintLabel _jlbl_painting) {
        this.jlbl_painting = _jlbl_painting;
    }


    /**
     * @return the jpnl_toMove
     */
    public JPanel getJpnl_toMove() {
        return jpnl_toMove;
    }


    /**
     * @param _jpnl_toMove the jpnl_toMove to set
     */
    public void setJpnl_toMove(final JPanel _jpnl_toMove) {
        this.jpnl_toMove = _jpnl_toMove;
    }
}

