package control.util;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import control.interfaces.ActivityListener;
import view.util.Item2;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CItem2 implements MouseListener {

    /**
     * The colors.
     */
    private final Color clr_pressed = new Color(230, 160, 170), 
    clr_mouseOver = new Color(160, 230, 170), clr_nothing = Color.lightGray;
    
    /**
     * The only instance of this class.
     */
    private static CItem2 instance;

    private ActivityListener activityListener;
    
    public void addItemActivityListener(ActivityListener _activityListener) {
    	activityListener = _activityListener;
    }
    
    /**
     * Empty utility class constructor.
     */
    private CItem2() { }


    /**
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent _event) {
        if (_event.getSource() instanceof Item2) {

            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_mouseOver));
            
            activityListener.activityOccurred(_event);
        }
    }
    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent _event) {
        if (_event.getSource() instanceof Item2) {
            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_pressed));
        }
    }
    /**
     * {@inheritDoc}
     */
    public void mouseReleased(final MouseEvent _event) { }
    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent _event) {
        if (_event.getSource() instanceof Item2) {
            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_mouseOver));
        }
    }
    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent _event) { 
        if (_event.getSource() instanceof Item2) {
            Item2 i2 = (Item2) _event.getSource();
            i2.setBorder(BorderFactory.createLineBorder(clr_nothing));
        }
    }
    
    
    /**
     * Return the only instance of this class.
     * @return the only instance
     */
    public static CItem2 getInstance() {
        
        if (instance == null) {
            instance = new CItem2();
        }
        return instance;
    }
}
