//package declaration
package view.util;

//import declarations
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import settings.Status;


/**
 * JLabel which can be flipped.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public class VLabel extends JLabel {

    
    /**
     * paintComponent which paints the component. Flips if Status is 
     * normalRotation.
     * 
     * @param _graphics the graphics which are painted.
     */
    @Override public final void paintComponent(final Graphics _graphics) {
                
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
        if (!Status.isNormalRotation()) {
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
