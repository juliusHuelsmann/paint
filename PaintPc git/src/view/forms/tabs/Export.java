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

import view.util.VLabel;
import model.settings.Status;
import model.settings.ViewSettings;
import control.tabs.CExport;


/**
 * View class for class Look. It contains items for changing the displaying of
 * the page such as the background and its border and the display of the alpha 
 * values.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Export extends JPanel {

    /**
     * JLabels for the separation, linked with information.
     */
    private JLabel [] jlbl_separation;
       /**
     * JLabels for the information text, linked with separation line.
     */
    private VLabel [] jlbl_information;
    /**
     * The only instance of this class.
     */
    private static Export instance;

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
     * Title JLabel.
     */
    private JLabel jlbl_backgroundTitle, jlbl_borderTitle, 
    jlbl_subtitle_borderTop, jlbl_subtitle_borderBottom, 
    jlbl_subtitle_borderLeft, jlbl_subtitle_borderRight;
    

    /**
     * Final values for the size and location.
     */
    private final int buttonHeight = 20, buttonWidth = 200;
    
    /**
     * Empty utility class constructor.
     */
    private Export() { }
    
    
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

        jlbl_information = new VLabel[2];
        jlbl_separation = new JLabel[2];
        
        //initialize the content for the background
        initializeFirstColumn();
    }
    
    

    /**
     * Initialize the first column.
     */
    private void initializeFirstColumn() {

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
        jcb_raster.addActionListener(CExport.getInstance());
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
        jcb_lines.addActionListener(CExport.getInstance());
        jcb_lines.setLocation(jcb_raster.getWidth() + jcb_raster.getX() 
                + ViewSettings.getDistanceBetweenItems(), jcb_raster.getY());
        jcb_lines.setSize(jcb_raster.getSize());
        jcb_lines.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_lines);
        
        jcb_nothing = new JCheckBox("Nothing");
        jcb_nothing.setFocusable(false);
        jcb_nothing.setOpaque(false);
        jcb_nothing.addActionListener(CExport.getInstance());
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

        String [] s_amount = new String[] {"0%", "4%", "8%", "12%", 
                "16%", "24%"};

        //top
        jlbl_subtitle_borderTop = new JLabel("Top");
        jlbl_subtitle_borderTop.setSize(buttonWidth / 2, buttonHeight);
        jlbl_subtitle_borderTop.setLocation(jlbl_borderTitle.getX(), 
                ViewSettings.getDistanceBetweenItems() + jlbl_borderTitle.getY()
                + jlbl_borderTitle.getHeight());
        jlbl_subtitle_borderTop.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_borderTop.setFocusable(false);
        super.add(jlbl_subtitle_borderTop);
        
        jcb_margeTop = new JComboBox(s_amount);
        jcb_margeTop.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeTop.setLocation(jlbl_subtitle_borderTop.getX() 
                + jlbl_subtitle_borderTop.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderTop.getY());
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
        
        jcb_margeLeft = new JComboBox(s_amount);
        jcb_margeLeft.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeLeft.setLocation(jlbl_subtitle_borderLeft.getX() 
                + jlbl_subtitle_borderLeft.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderLeft.getY());
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
        
        jcb_margeBottom = new JComboBox(s_amount);
        jcb_margeBottom.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeBottom.setLocation(jlbl_subtitle_borderBottom.getX() 
                + jlbl_subtitle_borderBottom.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderBottom.getY());
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
        
        jcb_margeRight = new JComboBox(s_amount);
        jcb_margeRight.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeRight.setLocation(jlbl_subtitle_borderRight.getX() 
                + jlbl_subtitle_borderRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderRight.getY());
        super.add(jcb_margeRight);
        
        insertTrennung(jcb_nothing.getX() + jcb_nothing.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 0, true);
        insertInformation("Image Background", 0, jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 0, true);
    }
    
    

    /**
     * @return the jcb_raster
     */
    public JCheckBox getJcb_raster() {
        return jcb_raster;
    }

    /**
     * 
     * @param _x the x coordinate in pX
     * @param _locInArray the location in array
     * @param _add whether to add or not
     */
    private void insertTrennung(final int _x, 
            final int _locInArray, final boolean _add) {
        
        //if new initialization is demanded
        if (_add) {
            this.jlbl_separation[_locInArray] = new JLabel();
            this.jlbl_separation[_locInArray].setBorder(
                    BorderFactory.createLineBorder(
                            ViewSettings.GENERAL_CLR_BACKGROUND_DARK_XX));
            super.add(this.jlbl_separation[_locInArray]);
            
        }
        this.jlbl_separation[_locInArray].setBounds(
                _x, 
                ViewSettings.getDistanceBetweenItems(), 
                1, 
                ViewSettings.getView_heightTB_visible()
                - ViewSettings.getDistanceBetweenItems() 
                - ViewSettings.getView_heightTB()
                / ViewSettings.TABBED_PANE_TITLE_PROPORTION_HEIGHT);
    }
    
    /**
     * insert information text.
     * @param _text the printed text
     * @param _x1 first x coordinate (is between first and second coordinate)
     * @param _x2 second x coordinate (is between first and second coordinate)
     * @param _locationInArray the location in information array
     * @param _insert whether to insert the item or just to change bounds
     */
    private void insertInformation(final String _text, 
            final int _x1, final int _x2, final int _locationInArray, 
            final boolean _insert) {

        if (_insert) {

            //final value for foreground for JLabel
            final int rgb = 190;
            
            jlbl_information[_locationInArray] = new VLabel();
            jlbl_information[_locationInArray].setFont(
                    ViewSettings.GENERAL_TP_FONT_INFORMATION);
            jlbl_information[_locationInArray].setForeground(
                    new Color(rgb, rgb, rgb));
            jlbl_information[_locationInArray].setHorizontalAlignment(
                    SwingConstants.CENTER);
            jlbl_information[_locationInArray].setText(_text);
            super.add(jlbl_information[_locationInArray]);
            
        }

        if (Status.isNormalRotation()) {

            final int number = 
                    ViewSettings.getItemMenu1Height()
                    + ViewSettings.getDistanceBetweenItems();
            final int number2 = 15;
            jlbl_information[_locationInArray].setBounds(
                    _x1, number, _x2 - _x1, number2);
        } else {
            jlbl_information[_locationInArray].setOpaque(true);
            jlbl_information[_locationInArray].setBounds(-1, -1, -1, -1);
        }

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
    public static Export getInstance() {
        
        if (instance == null) {
            instance = new Export();
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

}
