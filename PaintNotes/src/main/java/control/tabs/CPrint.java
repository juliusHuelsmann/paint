package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.util.Util;
import view.tabs.Print;
import control.ControlPaint;

public class CPrint implements ActionListener {

	private ControlPaint cp;
	
	public CPrint(ControlPaint _cp) {
		this.cp = _cp;
	}
	
	
	public final void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(cp.getView().getTabs().getTab_print().getTb_print().getActionCause())) {
        	Util.print();
		}
	}

}
