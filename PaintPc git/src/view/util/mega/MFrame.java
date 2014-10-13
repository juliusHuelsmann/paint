package view.util.mega;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MFrame extends JFrame implements Mega {

    
    /**
     * flip.
     */
    public void turn() {

        
        
        for (Component c : getContentPane().getComponents()) {

                c.setLocation(getWidth() - c.getX() - c.getWidth(),
                        getHeight() - c.getY() - c.getHeight());
                if (c instanceof Mega) {
                    ((Mega) c).turn();
                }
        }
        
    }
}
