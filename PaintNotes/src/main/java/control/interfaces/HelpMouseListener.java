package control.interfaces;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import model.settings.Constants;
import model.util.paint.Utils;
import view.forms.Help;

public class HelpMouseListener implements MouseListener {

	/**
	 * Identifiers for help id.
	 */
	public static final int HELP_ID_LOW = 0,
	HELP_ID_MEDIUM = 1, HELP_ID_HIGH = 2, HELP_ID_ALWAYS = 3;
	
	
	/**
	 * The help id.
	 */
	private static int visibleHelpId = HELP_ID_LOW;
	
	/**
	 * The help id of the component which is initializes the helpMouseListener.
	 */
	private final int help_id;
	
	/**
	 * Instance of the main view-class view.
	 */
	private final Help help;
	
	/**
	 * Instance of the containing JFrame.
	 */
	private final JFrame jf_owner;
	
	/**
	 * The text.
	 */
	private final String text;
	
	private Cursor c;
	
	private Component cmp;
	
	/**
	 * 
	 * @param _text
	 * @param _help_id
	 * @param _jf
	 * @param _help
	 * @param _c
	 */
	public HelpMouseListener(
			final String _text, 
			final int _help_id,
			
			final JFrame _jf,
			final Help _help,
			
			final Component _cmp_helped,
			
			final Cursor _c) {
		
		this.text = _text;
		this.help_id = _help_id;
		
		this.jf_owner = _jf;
		this.help = _help;
		
		if (c == null) {
		    c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		} else {
			this.c = _c;
		}
		
		this.cmp = _cmp_helped;
	}

	
	
	/**
	 * @return the visibleHelpId
	 */
	public static int getVisibleHelpId() {
		return visibleHelpId;
	}

	/**
	 * @param _visibleHelpId the visibleHelpId to set
	 */
	public static void setVisibleHelpId(final int _visibleHelpId) {
		HelpMouseListener.visibleHelpId = _visibleHelpId;
	}

	private int messageID = -1;

	public void mouseEntered(final MouseEvent _event) { 
		
		final int messageSize = 150;
		// show help message
		messageID = help.showInformation(text, 
				new Point(
						cmp.getLocationOnScreen().x + cmp.getWidth() / 2,
						cmp.getLocationOnScreen().y + cmp.getHeight()), 
				messageSize, messageSize);
		
		//show cursor
		jf_owner.setCursor(c);
		
	}

	public final void mouseExited(final MouseEvent _event) { 
		
		help.hideInformation(messageID);
		

		jf_owner.setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	/**
	 * set the cursor.
	 * 
	 * @param _path the path of the cursor image.
	 * @param _name the name of the cursor
	 */
	public void setCursor(final String _path, final String _name) {

	    jf_owner.setCursor(Toolkit.getDefaultToolkit()
	            .createCustomCursor(new ImageIcon(Utils.resizeImage(
	                    Constants.MOUSE_ICON_SIZE, 
                        Constants.MOUSE_ICON_SIZE, 
                        _path)).getImage(), 
                        new Point(Constants.MOUSE_ICON_SIZE / 2, 
                                Constants.MOUSE_ICON_SIZE / 2), 
                                _name));	    
	}
	

	/**
	 * {@inheritDoc}
	 */
	public final void mousePressed(final MouseEvent _event) { }

	/**
	 * {@inheritDoc}
	 */
	public final void mouseReleased(final MouseEvent _event) { }

	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(final MouseEvent _event) { }


}
