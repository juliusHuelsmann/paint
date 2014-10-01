package start.test;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 * Shows BufferedImage for testing purpose.
 * @author Julius Huelsmann
 *
 */
@SuppressWarnings("serial")
public final class BufferedViewer extends JFrame {

    
    
    /**
     * The JLabel for the painting.
     */
    private JLabel jlbl_painting;
    
    
    /**
     * The only instance of this class.
     */
    private static BufferedViewer instance;
    
    
    
    /**
     * BufferedViewer constructor.
     * 
     */
    private BufferedViewer() {
        super();
        super.setLayout(null);
        
        jlbl_painting = new JLabel();
        super.add(jlbl_painting);
        
        
        
        super.setVisible(true);
        super.setResizable(false);
    }

    /**
     * @return the instance
     */
    public static BufferedViewer getInstance() {
        
        if (instance == null) {
            instance = new BufferedViewer();
        }
        return instance;
    }

    
    
    /**
     * Show new bufferedImage.
     * @param _bi the bufferedImage.
     */
    public static void show(final BufferedImage _bi) {

        getInstance().setSize(_bi.getWidth(), _bi.getHeight());
        getInstance().jlbl_painting.setSize(_bi.getWidth(), _bi.getHeight());
        getInstance().jlbl_painting.setIcon(new ImageIcon(_bi));
        getInstance().setVisible(true);
    }
    
    
    
}
