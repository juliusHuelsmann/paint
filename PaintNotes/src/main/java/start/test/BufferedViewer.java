package start.test;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.settings.State;
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
        final int size = 100;
        super.setSize(size, size);
        
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

    
    /**
     * The last time.
     */
    private static double timeLast = 0;
    
    /**
     * Show new bufferedImage.
     * @param _bi the bufferedImage.
     */
    public static synchronized void show(final BufferedImage _bi) {
    	
    	final boolean showEnabled = false;
    	if (showEnabled) {
        	
    		final int minTimeDiff = 5000;
    		final int maxWindowSize = 700;
    		
    		State.getLogger().warning("TEST" + "BufferdViewer.show");

        	final double time = System.currentTimeMillis();

        	if (time - timeLast > minTimeDiff) {
        		timeLast = time;
            	if (_bi.getWidth() > 0
            			&& _bi.getHeight() > 0) {

            		int width = _bi.getWidth();
            		int height = _bi.getHeight();
            		
            		if (_bi.getWidth() > maxWindowSize) {
            			width = maxWindowSize;
            		}
            		if (_bi.getHeight() > maxWindowSize) {
            			height = maxWindowSize;
            		}
            		
            		BufferedImage bi = Util.resize(_bi, width, height);
            		
                    getInstance().setSize(bi.getWidth(), bi.getHeight());
                    getInstance().jlbl_painting.setSize(bi.getWidth(),
                    		bi.getHeight());
                    getInstance().jlbl_painting.setIcon(new ImageIcon(bi));
                    System.out.println("passed");
            	}
        	}
    	}
    }
}
