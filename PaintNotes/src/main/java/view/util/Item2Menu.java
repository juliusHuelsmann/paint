//package declaration
package view.util;

//import declarations
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import view.forms.Page;
import view.forms.Tabs;
import view.util.mega.MButton;
import view.util.mega.MPanel;
import model.objects.painting.Picture;
import model.settings.ViewSettings;
import control.util.CItemAufklappen;

/**
 * A Menu which shows as much of the content as possible even if
 * it is closed.
 * 
 * @author Julius Huelsmann 
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Item2Menu extends MPanel {

    /**
     * MButton for the border.
     */
	private MButton jlbl_border;
	
	/**
	 * MButton for opening the Panel.
	 */
	private VButtonWrapper jbtn_open;
	
	/**
	 * distance to the border of the MPanel of each component.
	 */
	private final int distance = 5;
	
	/**
	 * whether opened or not.
	 */
	private boolean opened;

	/**
	 * the point of the previously inserted item at MPanel jpnl_border.
	 */
	private Point pnt_previousItem;

	/**
	 * the amount of items which are added in one row to MPanel.
	 */
	private int itemsInRow;

    /**
     * The set height of the Item2Menu if it is closed. Has to be saved because
     * if the Item2Menu is opened the component height is overwritten.
     */
    private int height;
    
    
	/**
	 * Constructor: shows closed item.
	 */
	public Item2Menu() {
		
		//initialize MPanel and alter settings
		super();
		super.setOpaque(true);
		super.setLayout(null);
		
		//initialize items in row
		this.itemsInRow = 2 + 2 + 1;
		this.opened = false;

		final Color clr_background = 
		        ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT_FOCUS;
		
		//for opening and closing
		jbtn_open = new VButtonWrapper(this);
		jbtn_open.setText("v");
		jbtn_open.addMouseListener(CItemAufklappen.getInstance());
		jbtn_open.setContentAreaFilled(false);
		jbtn_open.setOpaque(true);
		jbtn_open.setBackground(clr_background);
		jbtn_open.setFocusable(false);
		super.add(jbtn_open);
		
		//contains stuff and is the border of the item
		jlbl_border = new MButton();
		jlbl_border.setContentAreaFilled(false);
		jlbl_border.setOpaque(false);
		jlbl_border.setLayout(null);
		jlbl_border.setOpaque(true);
		jlbl_border.setBackground(clr_background);
		super.add(jlbl_border);
		
		//apply borders
		applyMouseExited();
		
		
	}
	
	/**
	 * disable set border.
	 * @param _border 
	 */
	@Override public void setBorder(final Border _border) { }

	/**
	 * apply mouseEntered.
	 */
	public final void applyMouseEntered() {
	
	    //if is not opened set border.
	    if (!opened) {
	        final Color clr_1 = new Color(190, 190, 240), 
	                clr_2 = new Color(200, 200, 200);
            setBorder(clr_1, clr_2);
		}
	}
	
	/**
	 * apply mouseExited.
	 */
	public final void applyMouseExited() {
    
        //if is not opened set border.
		if (!opened) {
	        setBorder(ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT,
	                Color.LIGHT_GRAY);        
		}
	}
    
    /**
     * apply mouseClicked.
     */
	public final void applyMouseClicked() {

        setOpen(!opened);
        
	}
	
	/**
	 * open or close the item2 menu from outside.
	 * @param _open wheter open or not
	 */
	public final void setOpen(final boolean _open) {
	    

    	//open or close the menu
	    if (_open != opened) {


	    	//release selected because of display bug otherwise.
	    	if (Picture.getInstance().isSelected()) {

		    	Picture.getInstance().releaseSelected();
		    	Page.getInstance().releaseSelected();
	    	}
	    	
	    	//open or close
	    	
	        this.opened = _open;
	        final int heightMButton = 15;

	        //if is not opened set border.
	        if (opened) {
	            final Color clr_highlight = new Color(165, 165, 190);
	            setBorder(clr_highlight, Color.LIGHT_GRAY);  
	        }


	        
	        if (opened) {

	                final int borderSpace = 2;
	                final int totalWidth = jlbl_border.getWidth() 
	                        - 2 * borderSpace;
	                final int itemSize = totalWidth / itemsInRow;

	                if (pnt_previousItem == null) {

                        super.setSize(getWidth(), height); 
	                } else {

                        int realheight = pnt_previousItem.y + itemSize;
	                    if (realheight < height) {
	                        realheight = height;
	                    }
                        super.setSize(getWidth(), realheight);  
	                } 
	        } else {

	            super.setSize(getWidth(), height);
	        }

	        jlbl_border.setBounds(
	                distance, 
	                distance, 
	                getWidth() - distance * 2,
	                getHeight() - distance * 2 - heightMButton);
	        
	        jbtn_open.setBounds(
	                jlbl_border.getX(),
	                jlbl_border.getY() + jlbl_border.getHeight(), 
	                jlbl_border.getWidth(), 
	                heightMButton);
	        if (!opened) {

                //when closed repaint.
                Page.getInstance().getJlbl_painting().repaint();
                Tabs.getInstance().repaint();
	        }
	        
	    } //otherwise nothing to do
	    repaint();
	}
	
	
	/**
	 * set border of JLabel and MButton.
	 * @param _c1 the first color
	 * @param _c2 the second color.
	 */
	private void setBorder(final Color _c1, final Color _c2) {
		jlbl_border.setBorder(
		        BorderFactory.createCompoundBorder(
		                BorderFactory.createMatteBorder(1, 1, 0, 1, _c1), 
		                BorderFactory.createMatteBorder(1, 1, 0, 1, _c2)));
		jbtn_open.setBorder(
		        BorderFactory.createCompoundBorder(
		                BorderFactory.createMatteBorder(0, 1, 1, 1, _c1),
		                BorderFactory.createMatteBorder(0, 1, 1, 1, _c2)));
		
	}
	
	/**
	 * set size of Menu and location and size of its components.
	 * @param _width the width
	 * @param _height the height
	 */
	@Override public final void setSize(final int _width, final int _height) {
		
	    this.height = _height;
		final int realheight;
		final int heightMButton = 15;
		if (opened) {

	        final int borderSpace = 2;
		    final int totalWidth = jlbl_border.getWidth() - 2 * borderSpace;
	        final int itemSize = totalWidth / itemsInRow;

	        realheight = pnt_previousItem.y + itemSize;
		} else {
			realheight = _height;
		}
		super.setSize(_width, realheight);
		jlbl_border.setBounds(
		        distance, 
		        distance, 
		        _width - distance * 2,
		        realheight - distance * 2 - heightMButton);
		
		jbtn_open.setBounds(
		        jlbl_border.getX(),
		        jlbl_border.getY() + jlbl_border.getHeight(), 
		        jlbl_border.getWidth(), 
		        heightMButton);
	}
	
	
	@Override public final Component add(final Component _c) {
		

		final int borderSpace = 2;
		final int totalWidth = jlbl_border.getWidth() - 2 * borderSpace;
		final int itemSize = totalWidth / itemsInRow;

		if (pnt_previousItem == null) {

			this.pnt_previousItem = new Point(-itemSize, 0);
		}
		int newline = (pnt_previousItem.x + itemSize) 
		        / (totalWidth - itemSize);
		pnt_previousItem.y =  newline * itemSize + pnt_previousItem.y;
		if (newline == 1) {
			pnt_previousItem.x = 0;
		} else {
			pnt_previousItem.x = (pnt_previousItem.x + itemSize) 
			        % (totalWidth - itemSize);
		}
		_c.setBounds(borderSpace + pnt_previousItem.x, borderSpace 
		        + pnt_previousItem.y, itemSize, itemSize);
		
		return jlbl_border.add(_c);
	}

    /**
     * @return the itemsInRow
     */
    public final int getItemsInRow() {
        return itemsInRow;
    }

    /**
     * @param _itemsInRow the itemsInRow to set
     */
    public final void setItemsInRow(final int _itemsInRow) {
        this.itemsInRow = _itemsInRow;
    }

    
    /**
     * Return whether is open or not.
     * @return whether is open
     */
	public final  boolean isOpen() {
		return opened;
	}
	
}
