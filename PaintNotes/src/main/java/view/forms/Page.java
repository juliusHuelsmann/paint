//package declaration
package view.forms;

//import declarations
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import model.settings.Constants;
import model.settings.Status;
import model.settings.ViewSettings;
import model.util.paint.Utils;
import view.util.VScrollPane;
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import control.ControlPaint;
import view.util.mega.MButton;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public final class Page extends MPanel {

    
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
    private QuickAccess quickAccess;
	/**
	 * empty utility class constructor. 
	 */
	public Page(final ControlPaint _cv) {
		initialize(_cv);
	}

	
	/**
	 * initializes graphical user interface components
     * and thus creates view. Called directly after creation.
	 */
	private void initialize(final ControlPaint controlPaint) {

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
        jpnl_new = new New(controlPaint.getControlnew());
        super.add(jpnl_new);
        super.add(Console.getInstance());
        
        quickAccess = new QuickAccess(controlPaint.getControlQuickAccess());
        super.add(quickAccess);
        
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
        sp_ub.setActivityListener(controlPaint.getUtilityControlScrollPane());
        controlPaint.getView().add(sp_ub);

        sp_lr = new VScrollPane(jpnl_toMove, this, false);
        sp_lr.setActivityListener(controlPaint.getUtilityControlScrollPane());
        controlPaint.getView().add(sp_lr);
        
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
                jbtn_resize[x][y].addMouseListener(
                		controlPaint.getControlPaintSelection());
             //   jbtn_resize[x][y].addMouseListener(controlPaint);
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
        jlbl_painting.setPaintListener(controlPaint.getControlPic());
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

		sp_ub.setSize(ViewSettings.VIEW_SIZE_SP, 
				ViewSettings.getSizeJFrame().height 
				- ViewSettings.VIEW_SIZE_SP);
        sp_lr.setSize(ViewSettings.getSizeJFrame().width, 
                ViewSettings.VIEW_SIZE_SP);

        jlbl_background.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_background2.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_selectionBG.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
        jlbl_selectionPainting.setBounds(0, 0, getWidth() - 1, getHeight() - 1);

        jlbl_background.setIcon(new ImageIcon(Status.getBi_transparency()));



		//the order of painting is important! It is necessary that the 
        //paintinglabel's bounds and the locations of the ScrollPanes are 
        //set here
        jlbl_painting.setBounds(0, 0, getWidth() - 1, getHeight() - 1);
		sp_ub.setLocation(ViewSettings.getSizeJFrame().width 
				- sp_ub.getWidth(), ViewSettings.VIEW_SIZE_SP);
		sp_lr.setLocation(0, ViewSettings.getSizeJFrame().height 
				- sp_lr.getHeight());
            	
		

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


	public QuickAccess getQuickAccess() {
		return quickAccess;
	}


	public void setQuickAccess(QuickAccess quickAccess) {
		this.quickAccess = quickAccess;
	}


	public MPanel getJpnl_toMove() {
		return jpnl_toMove;
	}


	public void setJpnl_toMove(MPanel jpnl_toMove) {
		this.jpnl_toMove = jpnl_toMove;
	}
}

