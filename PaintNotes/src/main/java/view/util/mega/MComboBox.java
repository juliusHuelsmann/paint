package view.util.mega;

import javax.swing.JComboBox;

@SuppressWarnings({ "serial", "rawtypes" })
public class MComboBox extends JComboBox {

	
	public MComboBox() {
		super.setFocusable(false);
	}
	
	
	@SuppressWarnings("unchecked")
	public MComboBox(String [] _string) {
		super(_string);
		super.setFocusable(false);
	}
	
}
