//package declaration
package view.util.mega;

//import declarations
import java.awt.Component;
import javax.swing.JFrame;

/**
 * View class which add the possibility to rotate the components 
 * (by overwriting paintComponents(Graphics g) in container class).
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class MFrame extends JFrame implements Mega {

    /**
     * Turn the owned components.
     */
    @Override public final void turn() {

        //go through the list of contained components and change location
        //and e.g. call turn method of components.
        for (Component c : getContentPane().getComponents()) {

            c.setLocation(getWidth() - c.getX() - c.getWidth(),
                    getHeight() - c.getY() - c.getHeight());
            if (c instanceof Mega) {
                ((Mega) c).turn();
            }
        }
    }
}
