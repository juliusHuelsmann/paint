package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.util.Util;
import control.ControlPaint;


/**
 * Controller class for Print Tab.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class CTabPrint implements ActionListener {


	/**
	 * Instance of the main controller.
	 */
	private ControlPaint cp;
	
	
	/**
	 * Constructor: save main controller.
	 * @param _cp instance of main controller 
	 */
	public CTabPrint(final ControlPaint _cp) {
		this.cp = _cp;
	}
	


	/**
	 * {@inheritDoc}
	 */
	public final void actionPerformed(final ActionEvent _event) {
		if (_event.getSource().equals(
				cp.getView().getTabs().getTab_print().getTb_print()
				.getActionCause())) {
        	Util.print(cp.getPicture());
		}
	}

}
