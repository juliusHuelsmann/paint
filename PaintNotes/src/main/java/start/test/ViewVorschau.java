//package declaration
package start.test;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * singleton view class which shows the painted image.
 * This observer can be readded to Picture but only for debugging purpose.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public final class ViewVorschau extends JFrame 
implements Observer {

	/**
	 * the only instance of this class.
	 */
	private static ViewVorschau instance = null;

	/**
	 * The JLabel which contains a preview image.
	 */
	private JLabel jlbl_imagePreview;
	
	
	/**
	 * A Rectangle which shows the presented part of the image.
	 */
	private JLabel jlbl_clipping;
	
	/**
	 * Constructor: initialize JFrame, alter settings and initialize items.
	 */
	private ViewVorschau() { }
	
	/**
	 * initialize MainJFrame (add content).
	 */
	private void initialize() {
	    
        //initialize JFrame and alter settings
	    super.setTitle("preview");
        super.setAlwaysOnTop(false);
        super.setFocusable(false);
        super.setLayout(null);
        super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        super.getContentPane().setBackground(Color.white);
        final int width = 400;
        super.setSize(width, (int) (width * Math.sqrt(2)));
        super.setLocation((int)
                Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 1, 0);
        
        //the JLabel for the currently presented part of the image.
        jlbl_clipping = new JLabel();
        jlbl_clipping.setOpaque(true);
        final int hundred = 100;
        jlbl_clipping.setBounds(hundred, hundred, hundred, hundred);
        final Color clr_clipping = new Color(205, 255, 240, 230);
        jlbl_clipping.setBackground(clr_clipping);
        jlbl_clipping.setBorder(BorderFactory.createLineBorder(Color.black));
        super.add(jlbl_clipping);
        
        //add tab, overview about paintObjects and Page
        jlbl_imagePreview = new JLabel();
        jlbl_imagePreview.setSize(getSize());
        jlbl_imagePreview.setLocation(0, 0);
        super.add(jlbl_imagePreview);
      
        //set size and location
        super.setVisible(true);

        repaint();
	}
	
	
    
	
	
    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static ViewVorschau getInstance() {
        
        //if class is not instanced yet instantiate
        if (instance == null) {
            instance = new ViewVorschau();
            instance.initialize();
        }
        
        //return the only instance of this class.
        return instance;
    }
    

    /**
     * {@inheritDoc}
     */
    public void update(final Observable _picture, final Object _bufferedImage) {
        
        new Thread() {
            
            public void run() {

//                jlbl_imagePreview.setIcon(new ImageIcon(Utils.resizeImage(
//                        getWidth(), getHeight(), 
//                        (BufferedImage) _bufferedImage)));
            }
        } .start();
    }
}
