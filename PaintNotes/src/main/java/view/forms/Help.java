package view.forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import model.settings.ViewSettings;
import view.util.mega.MPanel;


/**
 * .
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Help extends MPanel {

	
	/**
	 * The identifier of the current message which is used because if 
	 * the location of the mouse is moved from one component to another,
	 * two events are thrown: 
	 * 		mouse_exited
	 * 		mouse_entered
	 * 
	 * if first mouse_entered is called and afterwards mouse_exited, the 
	 * new message (which is to be maintained) is made invisible.
	 * 
	 * Therefore- for making information invisible, an identifier is used
	 * and returned whenever information are shown. This identifier is needed
	 * for hiding information.
	 * 
	 * For avoiding display bugs, all messages with messageID lower or equal
	 * than the given messageID are removed.
	 */
	private static int messageID = 0;
	
	/**
	 * The JFrame that owns the element about which the information are 
	 * displayed.
	 */
	private JFrame jf_owner;
	

	/**
	 * JTextArea which contains information on the graphical user interface 
	 * component.
	 */
	private JTextArea jta_content;
	
	
	/**
	 * JLabel which contains background image (auto generated if displayed 
	 * (and size is changed).
	 */
	private JLabel jlbl_background, jlbl_headline;
	
	
	/**
	 * 
	 * @param _view
	 */
	public Help(final JFrame _view) {
		super();
		super.setOpaque(false);
		super.setLayout(null);
//		super.setBackground(Color.green);
		
		this.jf_owner = _view;
		
		jlbl_headline = new JLabel("Hilfe");
		jlbl_headline.setFocusable(false);
		jlbl_headline.setOpaque(false);
		jlbl_headline.setForeground(Color.gray);
		jlbl_headline.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 14));
		super.add(jlbl_headline);

		jta_content = new JTextArea();
		jta_content.setEditable(false);
		jta_content.setOpaque(false);
		jta_content.setForeground(Color.gray);
		jta_content.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
		jta_content.setFocusable(false);
		super.add(jta_content);
		
		jlbl_background = new JLabel();
		jlbl_background.setOpaque(false);
		jlbl_background.setFocusable(false);
		super.add(jlbl_background);
		
		setVisible(false);
	}
	
	/**
	 * Show information on an element which is (directly or indirectly) added
	 * to the JFrame jf_owner.
	 * 
	 * @param _information		the information
	 * 
	 * @param _pnt_locOnScreen	the location of the element about which 
	 * 							the information are printed
	 * 	
	 * @param _width			the approximate width of the message
	 * @param _height   		the approximate height of the message
	 * 
	 * @return					the identifier of the message is created for 
	 * 							begin able to control which message is to be
	 * 							hidden (if method hhideInformation is called)
	 */
	public final synchronized int showInformation(
			final String _information,
			final Point _pnt_locOnScreen,
			final int _width, final int _height) {
	
		//calculate the location in window
		int locInWindowX = _pnt_locOnScreen.x - jf_owner.getX() - _width / 2;
		int locInWindowY = _pnt_locOnScreen.y - jf_owner.getY(); 
		
		final int shiftX;
		if (locInWindowX < 0 + ViewSettings.DISTANCE_TO_WINDOW) {
			shiftX = locInWindowX - ViewSettings.DISTANCE_TO_WINDOW;
		} else if (locInWindowX + _width 
				> jf_owner.getWidth() - ViewSettings.DISTANCE_TO_WINDOW) {
			shiftX = locInWindowX + _width - jf_owner.getWidth()
					+ ViewSettings.DISTANCE_TO_WINDOW;
		} else {
			shiftX = 0;
		}
		final int shiftY;
		if (locInWindowY < ViewSettings.DISTANCE_TO_WINDOW) {
			shiftY = locInWindowY - ViewSettings.DISTANCE_TO_WINDOW;
		} else if (locInWindowY + _height 
				> jf_owner.getHeight() - ViewSettings.DISTANCE_TO_WINDOW) {
			shiftY = locInWindowY + _height - jf_owner.getHeight()
					+ ViewSettings.DISTANCE_TO_WINDOW;
		} else {
			shiftY = 0;
		}
		
		super.setLocation(locInWindowX - shiftX, locInWindowY - shiftY);
		
		
		
		//generate BufferedImage
		final BufferedImage bi_background = new BufferedImage(_width, _height, 
				BufferedImage.TYPE_INT_ARGB);
		
		final int heightArrow = 15;
		final int rgbClrBackground 
//		= new Color(230, 250, 255).getRGB();
//		= new Color(255, 230, 250).getRGB();
		= new Color(240, 255, 240).getRGB();
		final int rgbClrAlpha = new Color(0, 0, 0, 0).getRGB();
		final int rgbClrBorder = Color.lightGray.getRGB();

		//visible scope
		for (int x = 0; x < bi_background.getWidth(); x++) {

			for (int y = heightArrow; y < bi_background.getHeight(); y++) {
				bi_background.setRGB(x, y, rgbClrBackground);
			}
		}

		//length of one border-line.
		final int borderLength = 8;
		//upper border
		int cBorderPX = 0;
		for (int x = 0; x < bi_background.getWidth(); x++) {
			final int xRel = x - bi_background.getWidth() / 2
					- shiftX;
			int y = heightArrow;
			if (Math.abs(xRel) >= y) {

				if ((cBorderPX / borderLength % 2) == 0) {

					bi_background.setRGB(x, y, rgbClrBorder);
					cBorderPX++;
				} else {
					x += borderLength - 1;
					cBorderPX += borderLength;
				}
			}
		}

		for (int y = heightArrow; y < bi_background.getHeight(); y++) {
			int x = bi_background.getWidth() - 1;
			if ((cBorderPX / borderLength % 2) == 0) {
	
				bi_background.setRGB(x, y, rgbClrBorder);
				cBorderPX++;
			} else {
				y += borderLength - 1;
				cBorderPX += borderLength;
			}
		}

		for (int x = bi_background.getWidth() - 1; x >= 0; x--) {
			int y = bi_background.getHeight() - 1;
			if ((cBorderPX / borderLength % 2) == 0) {
	
				bi_background.setRGB(x, y, rgbClrBorder);
				cBorderPX++;
			} else {
				x -= borderLength - 1;
				cBorderPX += borderLength;
			}
		}

		for (int y = bi_background.getHeight() - 1; y >= 0; y--) {
			int x = 0;
			if ((cBorderPX / borderLength % 2) == 0) {
	
				bi_background.setRGB(x, y, rgbClrBorder);
				cBorderPX++;
			} else {
				y -= borderLength - 1;
				cBorderPX += borderLength;
			}
		}

		//alpha scope
		for (int x = 0; x < bi_background.getWidth(); x++) {
				
			//coordinates relative to center:
			final int xRel = x - bi_background.getWidth() / 2
					- shiftX;

				for (int y = 0; y < heightArrow; y++) {
				
				//border
				if (Math.abs(xRel) == y) {
					cBorderPX = (int) ((heightArrow + xRel) * Math.sqrt(2));

					if ((cBorderPX / borderLength % 2) == 0) {

						bi_background.setRGB(x, y, rgbClrBorder);
					} else {

						bi_background.setRGB(x, y, rgbClrBackground);
					}
				} else if (Math.abs(xRel) < y) {
					bi_background.setRGB(x, y, rgbClrBackground);
				} else {
					bi_background.setRGB(x, y, rgbClrAlpha);
				}
			}
		}
		jlbl_background.setIcon(new ImageIcon(bi_background));
		
final int heightHeadline = 20;
		//set size 
		super.setSize(_width, _height);
		jlbl_background.setSize(_width, _height);
		jlbl_headline.setSize(_width, heightHeadline);
		jlbl_headline.setLocation(
				(_width - 2 * heightHeadline) / 2, 
				ViewSettings.getDistanceBetweenItems() + heightArrow);
		jta_content.setLocation(
				ViewSettings.getDistanceBetweenItems(), 
				ViewSettings.getDistanceBetweenItems() 
				+ jlbl_headline.getHeight()
				+ jlbl_headline.getY());
		jta_content.setSize(_width - 2 * ViewSettings.getDistanceBetweenItems(),
				_height - heightArrow 
				- 2 * ViewSettings.getDistanceBetweenItems());

		//set text
		jta_content.setText(_information);
		
		
		setVisible(true);
		messageID++;
		return messageID;
		
		
		//
		//			   /\
		// ___________/  \______________
	}
	
	/**
	 * Hide information.
	 * @param _messageID 	the identifier of the last message that is to be 
	 * 						hidden.
	 */
	public final void hideInformation(final int _messageID) {
		
		if (_messageID >= messageID) {
			setVisible(false);
		}
	}
}
