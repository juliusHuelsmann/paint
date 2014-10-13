package view.util.mega;

import java.awt.Component;

import javax.swing.JPanel;

public class MPanel extends JPanel implements Mega{

    
    /**
     * flip.
     */
    public void turn() {

        
        
        for (Component c : getComponents()) {


                c.setLocation(getWidth() - c.getX() - c.getWidth(),
                        getHeight() - c.getY() - c.getHeight());
                if (c instanceof Mega) {
                    ((Mega) c).turn();
                }
            }
        
    }
}
