package view.util;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.util.paint.Utils;

/**
 * JButton for inserting a form into image.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Item2 extends JButton {

    
    /**
     * form for items that display a form which can be inserted into image.
     */
	public Item2() {
		super();
		super.setOpaque(false);
		super.setContentAreaFilled(false);
		super.setFocusable(false);
		super.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		super.setIcon(new ImageIcon("paint/test.png"));
	}
	
    /**
     * Set the icon of the JButton.
     * @param _s the path
     */
	public final void setIcon(final String _s) {

        super.setIcon(new ImageIcon(Utils.resizeImage(
                getWidth(), getHeight(), _s)));
	}
	
}
