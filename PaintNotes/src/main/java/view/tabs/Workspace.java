package view.tabs;

import javax.swing.JFrame;

import view.forms.Help;

/**
 * the workspace tab.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Workspace extends Tab {

    
    
    /**
     * Constructor: calls super-constructor with the amount of sections that
     * are contained in tab Workspace.
     */
    public Workspace() {
        super(0);
    }
	//change workspace location
	//default values
		//page
		//hours
		//numbers
		//sizes

	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}
}
