package view.util.mega;

import java.awt.Component;

import javax.swing.JPanel;

public class MPanel extends JPanel {

    private boolean first = true;
    
    /**
     * flip.
     */
    public void flip() {

        
        
        for (Component c : getComponents()) {

            if (!first) {

                c.setLocation(getWidth() - c.getX() - c.getWidth(),
                        getHeight() - c.getY() - c.getHeight());
            }
        }
        
        first = false;
    }
}
