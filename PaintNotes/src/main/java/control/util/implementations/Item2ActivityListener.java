package control.util.implementations;

import java.awt.event.MouseEvent;

import model.settings.Status;
import view.util.Item2;
import control.ControlPaint;
import control.interfaces.ActivityListener;

public class Item2ActivityListener implements ActivityListener{

	private ControlPaint cp;
	
	public Item2ActivityListener (ControlPaint _cp) {
		this.cp = _cp;
	}
	public void activityOccurred(final MouseEvent _event) {

        if (_event.getSource() instanceof Item2) {

            Item2 i2 = (Item2) _event.getSource();
    		cp.getView().getTabs().getTab_insert().getTb_selected().setIcon(i2.getIconPath());
    		cp.getView().getTabs().getTab_insert().getTb_selected().setText(i2.getTitle());
        } else {
        	Status.getLogger().severe("wrong listener added");
        }
	}

}
