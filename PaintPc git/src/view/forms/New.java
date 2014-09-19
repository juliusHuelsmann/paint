package view.forms;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import control.ControlPainting;
import control.singleton.MousePositionTracker;
import settings.Constants;
import settings.ViewSettings;
import view.View;
import view.util.Item1Button;

/**
 * This class is the user interface for creating a new page.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class New extends JPanel {

    /*
     * singleton
     */
    
    /**
     * The only instance of this class.
     */
    private static New instance;

    /*
     * components
     */
    
    /**
     * item Buttons for different page sizes.
     */
    private Item1Button i1b_a4, i1b_a5, i1b_a6, i1b_a7;
    
    /**
     * The JPanel which contains for instance the itemButtons.
     */
    private JPanel jpnl_stuff;
    
    /**
     * JLabel for background and darken.
     */
    private JLabel jlbl_background, jlbl_dimm;
    
    /*
     * final values
     */

    /**
     * final values for presentation.
     */
    private final int 
    distanceTop = 100,
    distanceBetweenItems = 5,
    distanceLeftRight = 5,
    width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()
    / (2 + 1) + (2 + 1) * distanceBetweenItems, 
    height = (int) (width / Math.sqrt(2) + distanceTop
            + 2 * distanceBetweenItems);
    
    /**
     * Empty utility class Constructor.
     */
    public New() { }

    
    /**
     * Initializes graphical user interface stuff.
     */
    private void initialize() {
        
        super.setSize(View.getInstance().getSize());
        super.setLocation(0, 0);
        super.setOpaque(true);
        super.setLayout(null);
        super.setVisible(false);

        jpnl_stuff = new JPanel();
        jpnl_stuff.setSize(width, height);
        MousePositionTracker mpt =  new MousePositionTracker(jpnl_stuff);
        jpnl_stuff.addMouseMotionListener(mpt);
        jpnl_stuff.addMouseListener(mpt);
        super.addMouseMotionListener(mpt);
        super.addMouseListener(mpt);
        
        jpnl_stuff.setLocation((int) (getWidth() - jpnl_stuff.getWidth()) / 2, 
                (int) (getHeight() - jpnl_stuff.getHeight()) / 2);
        jpnl_stuff.setBackground(
                ViewSettings.CLR_BACKGROUND_DARK);
        jpnl_stuff.setLayout(null);
        jpnl_stuff.setBorder(BorderFactory.createLineBorder(Color.black));
        super.add(jpnl_stuff);

        JLabel jlbl_title = new JLabel("Create new Page");
        jlbl_title.setFont(ViewSettings.GENERAL_FONT_HEADLINE);
        jlbl_title.setOpaque(false);
        jlbl_title.setBorder(null);
        jlbl_title.setLocation(distanceLeftRight, distanceLeftRight);
        jlbl_title.setSize(width, (2 + 2) * (2 + 2 + 1));
        jpnl_stuff.add(jlbl_title);
        
        /*
         * the page items
         */
        i1b_a4 = new Item1Button(null);
        i1b_a4.setImageHeight((((height - (2 + 2) * distanceBetweenItems 
                - distanceTop) / 2 - 2 * distanceBetweenItems)));
        i1b_a4.setImageWidth((int)
                (i1b_a4.getImageHeight() / Math.sqrt(2)));
        
        //initialize
        initialize(i1b_a4, "Din A4 (" + Constants.SIZE_A4.width + "x" 
                + Constants.SIZE_A4.height + ")", 0, 0);

        i1b_a5 = new Item1Button(null);
        i1b_a5.setImageHeight(i1b_a4.getImageHeight() / 2);
        i1b_a5.setImageWidth(i1b_a4.getImageWidth());
        initialize(i1b_a5, "Din A5 (" + Constants.SIZE_A5.width + "x" 
                + Constants.SIZE_A5.height + ")",
                i1b_a4.getWidth() + distanceBetweenItems, 0);

        i1b_a6 = new Item1Button(null);
        i1b_a6.setImageHeight(i1b_a5.getImageHeight());
        i1b_a6.setImageWidth(i1b_a5.getImageWidth() / 2);
        initialize(i1b_a6, "Din A6 (" + Constants.SIZE_A6.width + "x" 
                + Constants.SIZE_A6.height + ")", 
                0, i1b_a5.getHeight() + distanceBetweenItems);
        
        i1b_a7 = new Item1Button(null);
        i1b_a7.setImageHeight(i1b_a6.getImageHeight() / 2);
        i1b_a7.setImageWidth(i1b_a6.getImageWidth());
        initialize(i1b_a7, "Din A7 (" + Constants.SIZE_A7.width + "x" 
                + Constants.SIZE_A7.height + ")", 
                i1b_a5.getWidth() + distanceBetweenItems, 
                i1b_a5.getHeight() + distanceBetweenItems);

        jlbl_dimm = new JLabel();
        jlbl_dimm.setSize(getSize());
        jlbl_dimm.setOpaque(true);
        super.add(jlbl_dimm);
        
        jlbl_background = new JLabel();
        jlbl_background.setSize(getSize());
        super.add(jlbl_background);

        
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override public void setVisible(final boolean _visible) {
        
        
        if (_visible) {

            jlbl_background.setIcon(new ImageIcon(ControlPainting.getRobot()
                    .createScreenCapture(
                            new Rectangle(0, 0, getWidth(), getHeight()))));
            new Thread() {
                public void run() {

                    final int maxAlpha = 200;
                    final int rgb = 20;
                    for (int i = 0; i <= maxAlpha; i += 2) {

                        jlbl_dimm.setBackground(new Color(rgb, rgb, rgb, i));
                        repaint();
                        
                        try {
                            Thread.sleep(2 * (2 + 2 + 1));
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    
                    }
                }
            } .start();
        }

        super.setVisible(_visible);
        super.requestFocus();
    }
    
    /**
     * Initialize one page - item.
     * @param _i1b the page item
     * @param _text the text 
     * @param _plusXLocation the amount of pixel which is added to the
     * x coordinate of the component
     * @param _plusYLocation the amount of pixel which is added to the
     * x coordinate of the component
     */
    private void initialize(final Item1Button _i1b, final String _text, 
            final int _plusXLocation, final int _plusYLocation) {

        _i1b.setOpaque(true);
        _i1b.setBackground(Color.white);
        _i1b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black), new LineBorder(Color.lightGray)));
        _i1b.setText(_text);
        
        _i1b.setSize((width - (2 + 1) * distanceBetweenItems) / 2, 
                (height - 2 * distanceBetweenItems - distanceTop) / 2 
                - distanceBetweenItems);
        _i1b.setActivable(true);
        _i1b.setLocation(distanceBetweenItems + _plusXLocation, 
                distanceTop  + _plusYLocation);
        _i1b.setIconLabelY((_i1b.getHeight() 
                - _i1b.getImageHeight()) / 2);
        _i1b.setBorder(true);
        _i1b.setOwnBorder(BorderFactory.createLineBorder(Color.gray));
        jpnl_stuff.add(_i1b);
    }
    
    
    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * @return the only instance of this class.
     */ 
    public static New getInstance() {
        
        //create instance if necessary.
        if (instance == null) {
            instance = new New();
            instance.initialize();
        }
        
        //return the only instance
        return instance;
    }
}
