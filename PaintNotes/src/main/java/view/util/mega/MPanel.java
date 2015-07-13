//package declaration
package view.util.mega;

//import declarations
import java.awt.Component;

import javax.swing.JPanel;

/**
 * View class which add the possibility to rotate the components 
 * (by overwriting paintComponents(Graphics g) in container class).
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class MPanel extends JPanel implements Mega {

	public MPanel() {
		super.setFocusable(false);
	}
    
    /**
     * Turn the owned components.
     */
    public final void turn() {

        //go through the list of contained components and change location
        //and e.g. call turn method of components.
        for (Component c : getComponents()) {
            c.setLocation(getWidth() - c.getX() - c.getWidth(),
                    getHeight() - c.getY() - c.getHeight());
            if (c instanceof Mega) {
                ((Mega) c).turn();
            }
        }
    }
}
