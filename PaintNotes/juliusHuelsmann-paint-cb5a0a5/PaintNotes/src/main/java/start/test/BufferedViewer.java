package start.test;

import java.awt.image.BufferedImage;

import javax.naming.TimeLimitExceededException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.settings.Status;
import model.util.Util;


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
        super.setSize(100,100);
        
        jlbl_painting = new JLabel();
        jlbl_painting.setSize(getSize());
        super.add(jlbl_painting);
        
        
        super.setVisible(true);
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

    
    static double timeLast = 0;
    /**
     * Show new bufferedImage.
     * @param _bi the bufferedImage.
     */
    public static synchronized void show(final BufferedImage _bi) {
    	
    	/*

    	Status.getLogger().warning("TEST" + "BufferdViewer.show");
    	System.out.println(_bi.getWidth() + ".." + _bi.getHeight());
    	final double time = System.currentTimeMillis();
//
    	if (time - timeLast > 5000) {
    		timeLast = time;
        	if (_bi.getWidth() > 0
        			&&_bi.getHeight() > 0) {

        		int width = _bi.getWidth();
        		int height = _bi.getHeight();
        		if (_bi.getWidth() > 300) {
        			width = 300;
        		}
        		if (_bi.getHeight() > 300) {
        			height = 300;
        		}
        		
        		BufferedImage bi = Util.resize(_bi, width, height);
        		
                getInstance().setSize(bi.getWidth(), bi.getHeight());
                getInstance().jlbl_painting.setSize(bi.getWidth(), bi.getHeight());
                getInstance().jlbl_painting.setIcon(new ImageIcon(bi));
                System.out.println("passed");
        	}
    	} else {
    		System.out.println("hier");
    	}*/
    }
    
    
    
}
