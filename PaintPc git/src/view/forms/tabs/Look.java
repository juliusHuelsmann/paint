//package declarations
package view.forms.tabs;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.settings.ViewSettings;
import control.tabs.COutlook;


/**
 * View class for class Look. It contains items for changing the displaying of
 * the page such as the background and its border and the display of the alpha 
 * values.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Look extends JPanel {
    
    /**
     * The only instance of this class.
     */
    private static Look instance;

    /**
     * The JCheckBox for background of new Image.
     */
    private JCheckBox jcb_raster, jcb_lines, jcb_nothing;
    
    /**
     * The JCombo boxes for the size of the border.
     */
    private JComboBox jcb_margeTop, jcb_margeLeft, jcb_margeRight,
    jcb_margeBottom;
    
    /**
     * The JCombo box for displaying of alpha.
     */
    private JComboBox jcb_displayAlpha;

    /**
     * Title JLabel.
     */
    private JLabel jlbl_backgroundTitle, jlbl_borderTitle, 
    jlbl_subtitle_borderTop, jlbl_subtitle_borderBottom, 
    jlbl_subtitle_borderLeft, jlbl_subtitle_borderRight,
    jlbl_displayAlphaTitle, jlbl_subtitle_alpha;
    

    /**
     * Final values for the size and location.
     */
    private final int buttonHeight = 20, buttonWidth = 200;
    
    /**
     * Empty utility class constructor.
     */
    private Look() { }
    
    
    /**
     * Initialize the view class.
     */
    private void initialize() {

        //initialize JPanel and alter settings
        super.setOpaque(false);
        super.setLayout(null);
        super.setSize((int) Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth(), ViewSettings.VIEW_HEIGHT_TB);
        super.setVisible(true);

        //initialize the content for the background
        initializeFirstColumn();
        
        //initialize the content for the visualization
        initializeSecondColumn();
    }
    
    

    /**
     * Initialize the first column.
     */
    private void initializeFirstColumn() {

        jlbl_backgroundTitle = new JLabel("Background:");
        jlbl_backgroundTitle.setBounds(ViewSettings.DISTANCE_BETWEEN_ITEMS,
                ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                buttonWidth, buttonHeight);
        jlbl_backgroundTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_backgroundTitle.setFocusable(false);
        super.add(jlbl_backgroundTitle);

        jcb_raster = new JCheckBox("Raster");
        jcb_raster.setFocusable(false);
        jcb_raster.setOpaque(false);
        jcb_raster.addActionListener(COutlook.getInstance());
        jcb_raster.setLocation(jlbl_backgroundTitle.getX(), 
                ViewSettings.DISTANCE_BETWEEN_ITEMS
                + jlbl_backgroundTitle.getY()
                + jlbl_backgroundTitle.getHeight());
        jcb_raster.setSize(buttonWidth * 2 / (2 + 1), buttonHeight);
        jcb_raster.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jcb_raster.setSelected(true);
        super.add(jcb_raster);
        
        jcb_lines = new JCheckBox("Lines");
        jcb_lines.setFocusable(false);
        jcb_lines.setOpaque(false);
        jcb_lines.addActionListener(COutlook.getInstance());
        jcb_lines.setLocation(jcb_raster.getWidth() + jcb_raster.getX() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, jcb_raster.getY());
        jcb_lines.setSize(jcb_raster.getSize());
        jcb_lines.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_lines);
        
        jcb_nothing = new JCheckBox("Nothing");
        jcb_nothing.setFocusable(false);
        jcb_nothing.setOpaque(false);
        jcb_nothing.addActionListener(COutlook.getInstance());
        jcb_nothing.setLocation(jcb_lines.getWidth() + jcb_lines.getX() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, jcb_raster.getY());
        jcb_nothing.setSize(jcb_raster.getSize());
        jcb_nothing.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_nothing);


        jlbl_borderTitle = new JLabel("Border [percent]:");
        jlbl_borderTitle.setBounds(ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                jcb_nothing.getY() + jcb_nothing.getHeight() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                buttonWidth, buttonHeight);
        jlbl_borderTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_borderTitle.setFocusable(false);
        super.add(jlbl_borderTitle);

        String [] s_amount = new String[] {"0%", "4%", "8%", "12%", 
                "16%", "24%"};

        //top
        jlbl_subtitle_borderTop = new JLabel("Top");
        jlbl_subtitle_borderTop.setSize(buttonWidth / 2, buttonHeight);
        jlbl_subtitle_borderTop.setLocation(jlbl_borderTitle.getX(), 
                ViewSettings.DISTANCE_BETWEEN_ITEMS + jlbl_borderTitle.getY()
                + jlbl_borderTitle.getHeight());
        jlbl_subtitle_borderTop.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderTop.setFocusable(false);
        super.add(jlbl_subtitle_borderTop);
        
        jcb_margeTop = new JComboBox(s_amount);
        jcb_margeTop.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeTop.setLocation(jlbl_subtitle_borderTop.getX() 
                + jlbl_subtitle_borderTop.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                jlbl_subtitle_borderTop.getY());
        super.add(jcb_margeTop);
        
        //left
        jlbl_subtitle_borderLeft = new JLabel("Left");
        jlbl_subtitle_borderLeft.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderLeft.setLocation(jlbl_subtitle_borderTop.getX(), 
                ViewSettings.DISTANCE_BETWEEN_ITEMS 
                + jlbl_subtitle_borderTop.getY()
                + jlbl_subtitle_borderTop.getHeight());
        jlbl_subtitle_borderLeft.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderLeft.setFocusable(false);
        super.add(jlbl_subtitle_borderLeft);
        
        jcb_margeLeft = new JComboBox(s_amount);
        jcb_margeLeft.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeLeft.setLocation(jlbl_subtitle_borderLeft.getX() 
                + jlbl_subtitle_borderLeft.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                jlbl_subtitle_borderLeft.getY());
        super.add(jcb_margeLeft);

        //bottom
        jlbl_subtitle_borderBottom = new JLabel("Bottom");
        jlbl_subtitle_borderBottom.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderBottom.setLocation(jcb_margeTop.getX()
                + jcb_margeTop.getWidth() + ViewSettings.DISTANCE_BETWEEN_ITEMS,
                jcb_margeTop.getY());
        jlbl_subtitle_borderBottom.setFont(
                ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderBottom.setFocusable(false);
        super.add(jlbl_subtitle_borderBottom);
        
        jcb_margeBottom = new JComboBox(s_amount);
        jcb_margeBottom.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeBottom.setLocation(jlbl_subtitle_borderBottom.getX() 
                + jlbl_subtitle_borderBottom.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                jlbl_subtitle_borderBottom.getY());
        super.add(jcb_margeBottom);

        //right
        jlbl_subtitle_borderRight = new JLabel("Right");
        jlbl_subtitle_borderRight.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderRight.setLocation(jcb_margeLeft.getX() 
                + jcb_margeLeft.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                ViewSettings.DISTANCE_BETWEEN_ITEMS
                + jlbl_subtitle_borderTop.getY()
                + jlbl_subtitle_borderTop.getHeight());
        jlbl_subtitle_borderRight.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderRight.setFocusable(false);
        super.add(jlbl_subtitle_borderRight);
        
        jcb_margeRight = new JComboBox(s_amount);
        jcb_margeRight.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeRight.setLocation(jlbl_subtitle_borderRight.getX() 
                + jlbl_subtitle_borderRight.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS, 
                jlbl_subtitle_borderRight.getY());
        super.add(jcb_margeRight);
        
        insertTrennung(jcb_nothing.getX() + jcb_nothing.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS,
                ViewSettings.DISTANCE_BETWEEN_ITEMS);
        insertInformation("Image Background", 0, jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS);
    }
    
    
    
    /**
     * Initialize the second column.
     */
    private void initializeSecondColumn() {

        /*
         * Alpha
         */

        //right
        jlbl_displayAlphaTitle = new JLabel("Visualization");
        jlbl_displayAlphaTitle.setSize(buttonWidth, buttonHeight);
        jlbl_displayAlphaTitle.setLocation(jcb_nothing.getX()
                + jcb_nothing.getWidth() + ViewSettings.DISTANCE_BETWEEN_ITEMS,
                jlbl_backgroundTitle.getY());
        jlbl_displayAlphaTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_displayAlphaTitle.setFocusable(false);
        super.add(jlbl_displayAlphaTitle);
        
        jlbl_subtitle_alpha = new JLabel("of pure alpha");
        jlbl_subtitle_alpha.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_alpha.setLocation(jlbl_displayAlphaTitle.getX(),
                jlbl_displayAlphaTitle.getHeight() 
                + jlbl_displayAlphaTitle.getY()
                + ViewSettings.DISTANCE_BETWEEN_ITEMS);
        jlbl_subtitle_alpha.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_alpha.setFocusable(false);
        super.add(jlbl_subtitle_alpha);
        
        jcb_displayAlpha = new JComboBox(new String[]{"white", "boxes"});
        jcb_displayAlpha.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_displayAlpha.setLocation(jlbl_subtitle_alpha.getX() 
                + jlbl_subtitle_alpha.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS,
                jlbl_subtitle_alpha.getY());
        super.add(jcb_displayAlpha);

        insertTrennung(jcb_nothing.getX() + jcb_nothing.getWidth() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS,
                ViewSettings.DISTANCE_BETWEEN_ITEMS);
        insertInformation("Visualization", jlbl_displayAlphaTitle.getX(), 
                jcb_displayAlpha.getX()
                + jcb_displayAlpha.getWidth()
                + ViewSettings.DISTANCE_BETWEEN_ITEMS);
    }

    /**
     * @return the jcb_raster
     */
    public JCheckBox getJcb_raster() {
        return jcb_raster;
    }

    /**
     * insert a separation line.
     * @param _x the x start location
     * @param _y the y start location.
     * @return the JLabel
     */
    private JLabel insertTrennung(final int _x, final int _y) {

        final int number = 145;
        
        JLabel jlbl_trennung = new JLabel();
        jlbl_trennung.setBorder(
                BorderFactory.createLineBorder(
                        ViewSettings.CLR_BACKGROUND_DARK_XX));
        jlbl_trennung.setBounds(_x, _y, 1, number);
        super.add(jlbl_trennung);
        
        return jlbl_trennung;
    }
    
    
    /**
     * insert information text as title for an area.
     * @param _text the information text
     * @param _x the lower x coordinate 
     * @param _upper the upper x coordinate
     */
    private void insertInformation(final String _text, final int _x, 
            final int _upper) {

        final int rgb = 190;
        final int number = 135;
        final int number2 = 20;
        
        JLabel jlbl_informationColor = new JLabel(_text);
        jlbl_informationColor.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jlbl_informationColor.setBounds(_x , number, _upper - _x, number2);
        jlbl_informationColor.setForeground(new Color(rgb, rgb, rgb));
        jlbl_informationColor.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(jlbl_informationColor);

    }
    


    /**
     * @return the jcb_lines
     */
    public JCheckBox getJcb_lines() {
        return jcb_lines;
    }

    /**
     * @return the jcb_nothing
     */
    public JCheckBox getJcb_nothing() {
        return jcb_nothing;
    }
    /**
     * @return the instance
     */
    public static Look getInstance() {
        
        if (instance == null) {
            instance = new Look();
            instance.initialize();
        }
        return instance;
    }


    /**
     * @return the jcb_margeTop
     */
    public JComboBox getJcb_margeTop() {
        return jcb_margeTop;
    }



    /**
     * @return the jcb_margeLeft
     */
    public JComboBox getJcb_margeLeft() {
        return jcb_margeLeft;
    }

    
    /**
     * @return the jcb_margeRight
     */
    public JComboBox getJcb_margeRight() {
        return jcb_margeRight;
    }


    /**
     * @return the jcb_margeBottom
     */
    public JComboBox getJcb_margeBottom() {
        return jcb_margeBottom;
    }


    /**
     * @return the jcb_displayAlpha
     */
    public JComboBox getJcb_displayAlpha() {
        return jcb_displayAlpha;
    }
}
