//package declaration
package view.util;

//import declarations
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import settings.ViewSettings;
import control.util.CItemAufklappen;



/**
 * a Menu which shows as much of the content as possible even if
 * it is closed.
 * 
 * @author Julius Huelsmann 
 * @version %I%, %U%
 *
 */
@SuppressWarnings("serial")
public class Item2Menu extends JPanel {

    /**
     * JLabel for the border.
     */
	private JLabel jlbl_border;
	
	/**
	 * JButton for opening the Panel.
	 */
	private VButtonWrapper jbtn_open;
	
	/**
	 * distance to the border of the JPanel of each component.
	 */
	private final int distance = 5;
	
	/**
	 * whether opened or not.
	 */
	private boolean opened;

	/**
	 * the point of the previously inserted item at JPanel jpnl_border.
	 */
	private Point pnt_previousItem;

	/**
	 * the amount of items which are added in one row to JPanel.
	 */
	private int itemsInRow;
	
	/**
	 * Constructor: shows closed item.
	 */
	public Item2Menu() {
		
		//initialize JPanel and alter settings
		super();
		super.setOpaque(false);
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
		jlbl_border = new JLabel();
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
	    
	    if (_open == !opened) {

	        this.opened = _open;

	        //if is not opened set border.
	        if (opened) {
	            final Color clr_highlight = new Color(165, 165, 190);
	            setBorder(clr_highlight, Color.LIGHT_GRAY);  
	        }


	        
	        if (opened) {
	            setSize(getWidth(), getHeight() * 2);   
	        } else {

	            setSize(getWidth(), 
	                    getHeight() + jbtn_open.getHeight() * 2 - 2 - 1);
	        }
	    } //otherwise nothing to do
	}
	
	
	/**
	 * set border of JLabel and JButton.
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
		
		final int realheight;
		final int heightJButton = 15;
		if (opened) {
			realheight = _height - heightJButton * 2 + (2 + 1);
		} else {
			realheight = _height / 2;
		}
		super.setSize(_width, realheight);
		jlbl_border.setBounds(distance, distance, _width - distance * 2,
		        realheight - distance * 2 - heightJButton);
		jbtn_open.setBounds(jlbl_border.getX(), jlbl_border.getY() 
		        + jlbl_border.getHeight(), jlbl_border.getWidth(), 
		        heightJButton);
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
	
}