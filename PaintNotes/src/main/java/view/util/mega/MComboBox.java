package view.util.mega;

import javax.swing.JComboBox;

public class MComboBox extends JComboBox {

	
	public MComboBox() {
		super.setFocusable(false);
	}
	
	
	public MComboBox(String [] _string) {
		super(_string);
		super.setFocusable(false);
	}
	
}
