//package declaration
package view.util.mega;

//import declarations
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JButton;
import model.settings.State;


/**
 * View class for which add the possibility to rotate the components 
 * by overwriting paintComponents(Graphics g) and by overwriting the method 
 * paintComponent(Graphics g) for rotating the images and the icons.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class MButton extends JButton implements Mega {

    /**
     * Constructor. Call super - constructor.
     */
    public MButton() {
        super();
        super.setFocusable(false);
    }
    

    /**
     * Constructor. Call super - constructor.
     * @param _t the text of the Label.
     */
    public MButton(final String _t) {
        super(_t);
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

    /**
     * paintComponent which paints the component. Flips if Status is 
     * normalRotation.
     * 
     * @param _graphics the graphics which are painted.
     */
    public final void paintComponent(final Graphics _graphics) {
                
        //initialize values
        Graphics2D g2d = (Graphics2D) _graphics;
        AffineTransform origXform = g2d.getTransform();
        AffineTransform newXform = (AffineTransform) origXform.clone();
        
        //center of rotation is center of the panel
        int xRot = this.getWidth() / 2;
        int yRot = this.getHeight() / 2;
        
        //fetch rotation from Status
        double rotation = 0;
        
        //if not normal rotation
        if (!State.isNormalRotation()) {
            final int filpRotation = 180;
            rotation = filpRotation;
        }
        
        //rotate the image and draw the image to panel
        newXform.rotate(Math.toRadians(rotation), xRot, yRot);
        g2d.setTransform(newXform);
        super.paintComponent(g2d);
        g2d.setTransform(origXform);
    }
}
