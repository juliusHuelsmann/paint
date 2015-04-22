package control.tabs;

import java.awt.event.MouseEvent;

import model.settings.Status;
import view.util.Item2;
import control.ControlPaint;
import control.interfaces.ActivityListener;


/**
 * 
 * @author juli
 *
 */
public class CTabInsert implements ActivityListener {

	//TODO: isnt it possible to move the entire content the insert-tab -
	//listener?
	
	/**
	 * 
	 */
	private ControlPaint cp;
	
	
	/**
	 * 
	 * @param _cp
	 */
	public CTabInsert(final ControlPaint _cp) {
		this.cp = _cp;
	}
	
	
	/**
	 * 
	 */
	public final void activityOccurred(final MouseEvent _event) {

        if (_event.getSource() instanceof Item2) {

            Item2 i2 = (Item2) _event.getSource();
    		cp.getView().getTabs().getTab_insert().getTb_selected()
    		.setIcon(i2.getIconPath());
    		cp.getView().getTabs().getTab_insert().getTb_selected().setText(
    				i2.getTitle());
        } else {
        	Status.getLogger().severe("wrong listener added");
        }
	}

}
