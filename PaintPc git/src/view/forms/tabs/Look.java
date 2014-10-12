//package declarations
package view.forms.tabs;

//import declarations
import java.awt.Toolkit;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import model.settings.Constants;
import model.settings.Status;
import model.settings.ViewSettings;
import control.tabs.CLook;


/**
 * View class for class Look. It contains items for changing the displaying of
 * the page such as the background and its border and the display of the alpha 
 * values.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Look extends Tab {

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
    private Look() { 
        super(2);
    }
    
    
    /**
     * Initialize the view class.
     */
    private void initialize() {

        //initialize JPanel and alter settings
        super.setOpaque(false);
        super.setLayout(null);
        super.setSize((int) Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth(), ViewSettings.getView_heightTB());
        super.setVisible(true);

        //initialize the content for the background
        int x = initializeFirstColumn(0);
        
        //initialize the content for the visualization
        initializeSecondColumn(x);
    }
    
    

    /**
     * Initialize the first column.
     * @param _x the x start coordinate
     * @return the x coordinate of the separation element.
     */
    private int initializeFirstColumn(final int _x) {

        jlbl_backgroundTitle = new JLabel("Background:");
        jlbl_backgroundTitle.setBounds(ViewSettings.getDistanceBetweenItems(),
                ViewSettings.getDistanceBetweenItems(), 
                buttonWidth, buttonHeight);
        jlbl_backgroundTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_backgroundTitle.setFocusable(false);
        super.add(jlbl_backgroundTitle);

        jcb_raster = new JCheckBox("Raster");
        jcb_raster.setFocusable(false);
        jcb_raster.setOpaque(false);
        jcb_raster.addActionListener(CLook.getInstance());
        jcb_raster.setLocation(jlbl_backgroundTitle.getX(), 
                ViewSettings.getDistanceBetweenItems()
                + jlbl_backgroundTitle.getY()
                + jlbl_backgroundTitle.getHeight());
        jcb_raster.setSize(buttonWidth * 2 / (2 + 1), buttonHeight);
        jcb_raster.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jcb_raster.setSelected(true);
        super.add(jcb_raster);
        
        jcb_lines = new JCheckBox("Lines");
        jcb_lines.setFocusable(false);
        jcb_lines.setOpaque(false);
        jcb_lines.addActionListener(CLook.getInstance());
        jcb_lines.setLocation(jcb_raster.getWidth() + jcb_raster.getX() 
                + ViewSettings.getDistanceBetweenItems(), jcb_raster.getY());
        jcb_lines.setSize(jcb_raster.getSize());
        jcb_lines.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_lines);
        
        jcb_nothing = new JCheckBox("Nothing");
        jcb_nothing.setFocusable(false);
        jcb_nothing.setOpaque(false);
        jcb_nothing.addActionListener(CLook.getInstance());
        jcb_nothing.setLocation(jcb_lines.getWidth() + jcb_lines.getX() 
                + ViewSettings.getDistanceBetweenItems(), jcb_raster.getY());
        jcb_nothing.setSize(jcb_raster.getSize());
        jcb_nothing.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_nothing);


        jlbl_borderTitle = new JLabel("Border [percent]:");
        jlbl_borderTitle.setBounds(ViewSettings.getDistanceBetweenItems(), 
                jcb_nothing.getY() + jcb_nothing.getHeight() 
                + ViewSettings.getDistanceBetweenItems(), 
                buttonWidth, buttonHeight);
        jlbl_borderTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_borderTitle.setFocusable(false);
        super.add(jlbl_borderTitle);
        //top
        jlbl_subtitle_borderTop = new JLabel("Top");
        jlbl_subtitle_borderTop.setSize(buttonWidth / 2, buttonHeight);
        jlbl_subtitle_borderTop.setLocation(jlbl_borderTitle.getX(), 
                ViewSettings.getDistanceBetweenItems() + jlbl_borderTitle.getY()
                + jlbl_borderTitle.getHeight());
        jlbl_subtitle_borderTop.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderTop.setFocusable(false);
        super.add(jlbl_subtitle_borderTop);
        
        jcb_margeTop = new JComboBox(Constants.getBorderPercentagesTitle());
        jcb_margeTop.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeTop.setLocation(jlbl_subtitle_borderTop.getX() 
                + jlbl_subtitle_borderTop.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderTop.getY());
        jcb_margeTop.addActionListener(CLook.getInstance());
        super.add(jcb_margeTop);
        //left
        jlbl_subtitle_borderLeft = new JLabel("Left");
        jlbl_subtitle_borderLeft.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderLeft.setLocation(jlbl_subtitle_borderTop.getX(), 
                ViewSettings.getDistanceBetweenItems() 
                + jlbl_subtitle_borderTop.getY()
                + jlbl_subtitle_borderTop.getHeight());
        jlbl_subtitle_borderLeft.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderLeft.setFocusable(false);
        super.add(jlbl_subtitle_borderLeft);
        
        jcb_margeLeft = new JComboBox(Constants.getBorderPercentagesTitle());
        jcb_margeLeft.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeLeft.setLocation(jlbl_subtitle_borderLeft.getX() 
                + jlbl_subtitle_borderLeft.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderLeft.getY());
        jcb_margeLeft.addActionListener(CLook.getInstance());
        super.add(jcb_margeLeft);
        //bottom
        jlbl_subtitle_borderBottom = new JLabel("Bottom");
        jlbl_subtitle_borderBottom.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderBottom.setLocation(jcb_margeTop.getX()
                + jcb_margeTop.getWidth() 
                + ViewSettings.getDistanceBetweenItems(),
                jcb_margeTop.getY());
        jlbl_subtitle_borderBottom.setFont(
                ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderBottom.setFocusable(false);
        super.add(jlbl_subtitle_borderBottom);
        
        jcb_margeBottom = new JComboBox(Constants.getBorderPercentagesTitle());
        jcb_margeBottom.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeBottom.setLocation(jlbl_subtitle_borderBottom.getX() 
                + jlbl_subtitle_borderBottom.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderBottom.getY());
        jcb_margeBottom.addActionListener(CLook.getInstance());
        super.add(jcb_margeBottom);

        //right
        jlbl_subtitle_borderRight = new JLabel("Right");
        jlbl_subtitle_borderRight.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderRight.setLocation(jcb_margeLeft.getX() 
                + jcb_margeLeft.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems()
                + jlbl_subtitle_borderTop.getY()
                + jlbl_subtitle_borderTop.getHeight());
        jlbl_subtitle_borderRight.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderRight.setFocusable(false);
        super.add(jlbl_subtitle_borderRight);
        
        jcb_margeRight = new JComboBox(Constants.getBorderPercentagesTitle());
        jcb_margeRight.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeRight.setLocation(jlbl_subtitle_borderRight.getX() 
                + jlbl_subtitle_borderRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderRight.getY());
        jcb_margeRight.addActionListener(CLook.getInstance());
        super.add(jcb_margeRight);
        
        //apply the values set in Settings to the components:
        jcb_margeLeft.setSelectedIndex(getBorderPercentagesSettingsIndex(
                Status.getBorderLeftPercent()));
        jcb_margeRight.setSelectedIndex(getBorderPercentagesSettingsIndex(
                Status.getBorderRightPercent()));
        jcb_margeTop.setSelectedIndex(getBorderPercentagesSettingsIndex(
                Status.getBorderTopPercent()));
        jcb_margeBottom.setSelectedIndex(getBorderPercentagesSettingsIndex(
                Status.getBorderBottomPercent()));
        
        insertSectionStuff("Image Background", 0, jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 0, true);
        return jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems();
    }
    
    
    
    /**
     * Return the selected index.
     * @param _percentage the percentage set in Status.
     * @return the selected index in string array.
     */
    public static int getBorderPercentagesSettingsIndex(final int _percentage) {
        int selected = -1;
        for (int i = 0; i < Constants.BORDER_PRERCENTAGES.length; i++) {
            if (_percentage == Constants.BORDER_PRERCENTAGES[i]) {
                selected = i;
            }
        }
        
        if (selected == -1) {
            Status.getLogger().severe("Fatal error: Value set in settings"
                    + " that is not in Constants. Value = " + _percentage);
            selected = 0;
        }
        
        
        return selected;
    }
    
    
    /**
     * Initialize the second column.
     * @param _x the start x coorindate.
     * @return the x location of the separation element
     */
    private int initializeSecondColumn(final int _x) {

        /*
         * Alpha
         */

        //right
        jlbl_displayAlphaTitle = new JLabel("Visualization");
        jlbl_displayAlphaTitle.setSize(buttonWidth, buttonHeight);
        jlbl_displayAlphaTitle.setLocation(_x
                + ViewSettings.getDistanceBetweenItems(),
                jlbl_backgroundTitle.getY());
        jlbl_displayAlphaTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_displayAlphaTitle.setFocusable(false);
        super.add(jlbl_displayAlphaTitle);
        
        jlbl_subtitle_alpha = new JLabel("of pure alpha");
        jlbl_subtitle_alpha.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_alpha.setLocation(jlbl_displayAlphaTitle.getX(),
                jlbl_displayAlphaTitle.getHeight() 
                + jlbl_displayAlphaTitle.getY()
                + ViewSettings.getDistanceBetweenItems());
        jlbl_subtitle_alpha.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_alpha.setFocusable(false);
        super.add(jlbl_subtitle_alpha);
        
        jcb_displayAlpha = new JComboBox(new String[]{Constants.ID_WHITE,
                Constants.ID_DISPLAY_ALPHA});
        jcb_displayAlpha.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_displayAlpha.setLocation(jlbl_subtitle_alpha.getX() 
                + jlbl_subtitle_alpha.getWidth() 
                + ViewSettings.getDistanceBetweenItems(),
                jlbl_subtitle_alpha.getY());
        jcb_displayAlpha.addActionListener(CLook.getInstance());
        super.add(jcb_displayAlpha);

        insertSectionStuff("Visualization", jlbl_displayAlphaTitle.getX(), 
                jcb_displayAlpha.getX() + jcb_displayAlpha.getWidth()
                + ViewSettings.getDistanceBetweenItems(), 1, true);
        
        return jcb_displayAlpha.getX() + jcb_displayAlpha.getWidth()
                + ViewSettings.getDistanceBetweenItems();
    }

    /**
     * @return the jcb_raster
     */
    public JCheckBox getJcb_raster() {
        return jcb_raster;
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
