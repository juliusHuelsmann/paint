//package declaration
package control.util;

//import declarations
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.settings.ViewSettings;
import view.util.Item1Menu;
import view.util.Item1Button;
import view.util.VButtonWrapper;


/**
 * Handle the visual effects of TextButton.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class VisualTextButton implements MouseListener {
	
	/**
	 * the only instance of this class.
	 */
	private static VisualTextButton instance = null;

	/**
	 * private constructor which can only be called out of
	 * this class. Realized by getInstance.
	 */
	private VisualTextButton() { }

	/**
	 * {@inheritDoc}
	 */
	@Override public void mouseReleased(final MouseEvent _event) {  
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override public void mousePressed(final MouseEvent _event) { }

	/**
	 * {@inheritDoc}
	 */
	@Override public void mouseExited(final MouseEvent _event) {

		//get the current button and handle exceptions
		Item1Button currentButton = checkInput(_event);
		
		//if the button selection is enabled (isActivabe) activate the button
		if (!currentButton.isActivated()) {
			currentButton.setOwnBackground(ViewSettings.CLR_BACKGROUND_DARK);
			currentButton.setOwnBorder(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseEntered(final MouseEvent _event) {

		//get the current button and handle exceptions
		Item1Button currentButton = checkInput(_event);
		
		//if the button selection is enabled (isActivabe) activate the button
		if (!currentButton.isActivated()) {
			currentButton.setOwnBackground(ViewSettings.CLR_BACKGROUND_DARK_X);
			currentButton.setOwnBorder(ViewSettings.BRD_ITEM1BUTTON);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseClicked(final MouseEvent _event) {

		//get the current button and handle exceptions
		Item1Button currentButton = checkInput(_event);
		
		//if the button selection is enabled (isActivabe) activate the button
		if (currentButton.isActivable()) {
			
			currentButton.setActivated(true);
			currentButton.setOwnBackground(
			        ViewSettings.CLR_ITEM1BUTTON_BACKGROUND);	
			currentButton.setOwnBorder(ViewSettings.BRD_ITEM1BUTTON);
		}
		
	}


	/**
	 * check MouseEvent, handle errors and cast source to TextButton which
	 * is returned.
	 * 
	 * @param _event the event which is indirectly caused by TextButton if 
	 * 			everything works correctly.
	 * @return the TextButton which indirectly caused event.
	 */
	private Item1Button checkInput(final MouseEvent _event) {

		//try to cast the source
		try {

			VButtonWrapper wb = (VButtonWrapper) _event.getSource();
			Item1Menu it = (Item1Menu) wb.wrapObject();
			Item1Button tb = it.getTb_open();
			
			//return the successfully isolated TextButton
			return tb;
		} catch (Exception e) {
			
			try {

				VButtonWrapper wb = (VButtonWrapper) _event.getSource();
				Item1Button tb = (Item1Button) wb.wrapObject();
				
				//return the successfully isolated TextButton
				return tb;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			//handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static VisualTextButton getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
			instance = new VisualTextButton();
		}
		
		//return the only instance of this class.
		return instance;
	}
}

