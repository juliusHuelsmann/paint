package view.forms;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import model.settings.Constants;
import view.util.mega.MLabel;
import view.util.mega.MPanel;


/**
 * Loading graphical user interface.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Loading extends MPanel {

	
	/**
	 * JLabel for the background.
	 */
	private JLabel jlbl_background;
	
	/**
	 * ImageIcons for the loading image.
	 */
	private ImageIcon[] iic;
	
	
	
	/**
	 * Constructor: initialize.
	 */
	public Loading() {
		super();
		super.setBorder(new LineBorder(Color.black));
		
		jlbl_background = new MLabel();
		jlbl_background.setOpaque(false);
		super.add(jlbl_background);
		
		
	}
	
	
	/**
	 * Apply the size to the main component and its contents.
	 * @param _width the new width
	 * @param _height the new height
	 */
	public final void setSize(final int _width, final int _height) {
		super.setSize(_width, _height);
		iic = new ImageIcon[Constants.str_loading.length];
//		for (int i = 0; i < iic.length; i++) {
//			try {
//				iic[i] = new ImageIcon(model.util.Util.resize(
//						path[i], _width, _height));
//					
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}


	/**
	 * 
	 * @param _next the integer which 
	 */
	public final void nextIcon(final int _next) {
		if (iic != null && iic[_next % iic.length] != null) {
			jlbl_background.setIcon(iic[_next % iic.length]);
		}
	}
}
