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
	
	/**
	 * 
	 */
	private Cursor c;
	
	/**
	 * 
	 */
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

	
	/**
	 * Identifier of the message that is currently shown by current instance
	 * of this class.
	 * For being able to hide specified messages (by age).
	 */
	private int messageID = -1;

	private Thread thread_wait_for_help;
	
	/**
	 * Sets visible the instance of Help and changes the cursor.
	 * @param _event		the MouseEvent.
	 */
	public final void mouseEntered(final MouseEvent _event) { 
		
		thread_wait_for_help = new Thread() {
			public void run() {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					interrupt();
				}
				if (!interrupted()) {
					final int messageSize = 200;
					
					
					
					// show help message
					messageID = help.showInformation(
							adaptTextToSize(messageSize, text), new Point(
									cmp.getLocationOnScreen().x 
									+ cmp.getWidth() / 2,
									cmp.getLocationOnScreen().y
									+ cmp.getHeight()), 
									messageSize);
				}
			}
		};
		thread_wait_for_help.start();
		//show cursor
		jf_owner.setCursor(c);
		
	}
	
	public static void main(String[]args){
		System.out.println(
				adaptTextToSize(200, "Speichert das aktuelle Bild. "
        		+ "Wenn noch kein Speicherpfad angegeben wurde, öffnet sich"
        		+ " ein Dateibrowser. Andernfalls wird die Datei unter dem "
        		+ "zuletzt angegebenen Pfad gespeichert.\n\n"
        		+ "Die Speichereinstellungen können im Tab \"Export\""
        		+ " geändert werden."));
	}
	
	
	/**
	 * Adapt text to certain view - size.
	 */
	public final static String adaptTextToSize(
			final int _width,
			final String _text) {

		// 21 chars each 150 px.
		// 20 / 150 = #chars / px.
		final int amountCharsPerLine = 21 * _width / 150;
		String text = "";
		
		int lastSpaceIndex = -1;
		String word = "";
		int charsPerRow = 0;
		for (int index = 0; index < _text.length(); index++) {

			if (charsPerRow >= amountCharsPerLine) {
				
				// if no separation character has been found
				if (lastSpaceIndex == -1) {
					text += word + "-\n";
					
				} else {

					text += "\n";
					index = lastSpaceIndex;
				}
				lastSpaceIndex = -1;
				word = "";
				charsPerRow = 0;
			} else {

				if (_text.charAt(index) == ' '
						|| _text.charAt(index) == '\t') {
					lastSpaceIndex = index;
					text += word + _text.charAt(index);
					word = "";
				} else if (_text.charAt(index) == '\n'){

					text += word + _text.charAt(index);
					word = "";
					lastSpaceIndex = -1;
					charsPerRow = 0;
				} else {
					word += _text.charAt(index);
				}
				charsPerRow++;
			}
		}
		text += word;
		
		
		return text;
		
	}

	/**
	 * Hides the instance of Help (if it is not newer than the help-message 
	 * with message-id messageID.
	 * @param _event		the MouseEvent.
	 */
	public final void mouseExited(final MouseEvent _event) { 
		
		if (thread_wait_for_help != null) {

			
			thread_wait_for_help.interrupt();
			help.hideInformation(messageID);
			thread_wait_for_help = null;
		}
		

		jf_owner.setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	/**
	 * set the cursor.
	 * 
	 * @param _path the path of the cursor image.
	 * @param _name the name of the cursor
	 */
	public final void setCursor(final String _path, final String _name) {

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
