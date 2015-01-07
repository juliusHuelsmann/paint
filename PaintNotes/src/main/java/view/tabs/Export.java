//package declarations
package view.tabs;

//import declarations
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import view.util.mega.MLabel;
import model.settings.Constants;
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
public final class Export extends Tab {


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
    private MLabel jlbl_backgroundTitle, jlbl_borderTitle, 
    jlbl_subtitle_borderTop, jlbl_subtitle_borderBottom, 
    jlbl_subtitle_borderLeft, jlbl_subtitle_borderRight;
    

    /**
     * Titles for JCombobox by which the user ins able to set the exported
     * alpha color from alpha to white.
     */
    private MLabel jlbl_displayAlphaTitle, jlbl_subtitle_alpha;
    

    /**
     * JCombobox by which the user ins able to set the exported alpha color 
     * from alpha to white.
     */
    private JComboBox jcb_displayAlpha;

    /*
     * Save as
     */
    /**
     * Contains the different saveFormats.
     */
    private JComboBox jcb_saveFormats;
    
    /**
     * Titles for save as.
     */
    private MLabel jlbl_subtitle_saveOptions, jlbl_saveOptionsTitle;
    
    
    /**
     * Final values for the size and location.
     */
    private final int buttonHeight = 20, buttonWidth = 200;
    
    /**
     * Empty utility class constructor.
     */
    public Export(CExport _cex) {
        super(2);
        //initialize JPanel and alter settings
        super.setOpaque(false);
        super.setLayout(null);
        super.setVisible(true);

        //initialize the content for the background
        initializeColumn1(_cex);
    }
    

    /**
     * Initialize the first column.
     */
    private void initializeColumn1(CExport _cex) {
        
        //initialize the first part of the first column containing the 
        //possibility to choose an image background for the export and
        //to set its border sizes.
        initializeColumn1BG(_cex);
        
        //initializes the second part of the first column which contains
        //the components for changing the displaying of alpha values.
        //Choices: Save pure alpha as alpha value or save it as white pixel.
        initializeColumn1Alpha(_cex);


        //initializes the second part of the first column which contains
        //the components for changing the export format of the image.
        initializeColumn1SaveOptions(_cex);

        //apply the settings set in the Status class to the view items 
        //initialized above; Thus the user settings can be saved and restored
        applySettings();
        
        //insert the section border and the subtitle
        insertSectionStuff("Image Background", 0, 
                ViewSettings.getDistanceBetweenItems() + jcb_displayAlpha
                .getX() + jcb_displayAlpha.getWidth(), 0, true);
    }
    


    /**
     * Initialize the first part of the first column containing the 
     * possibility to choose an image background for the export and
     * to set its border sizes.
     */
    private void initializeColumn1BG(CExport _cex) {

        jlbl_backgroundTitle = new MLabel("Background:");
        jlbl_backgroundTitle.setBounds(ViewSettings.getDistanceBetweenItems(),
                ViewSettings.getDistanceBetweenItems(), 
                buttonWidth, buttonHeight);
        jlbl_backgroundTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_backgroundTitle.setFocusable(false);
        super.add(jlbl_backgroundTitle);

        jcb_raster = new JCheckBox("Raster");
        jcb_raster.setFocusable(false);
        jcb_raster.setOpaque(false);
        jcb_raster.addActionListener(_cex);
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
        jcb_lines.addActionListener(_cex);
        jcb_lines.setLocation(jcb_raster.getWidth() + jcb_raster.getX() 
                + ViewSettings.getDistanceBetweenItems(), jcb_raster.getY());
        jcb_lines.setSize(jcb_raster.getSize());
        jcb_lines.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_lines);
        
        jcb_nothing = new JCheckBox("Nothing");
        jcb_nothing.setFocusable(false);
        jcb_nothing.setOpaque(false);
        jcb_nothing.addActionListener(_cex);
        jcb_nothing.setLocation(jcb_lines.getWidth() + jcb_lines.getX() 
                + ViewSettings.getDistanceBetweenItems(), jcb_raster.getY());
        jcb_nothing.setSize(jcb_raster.getSize());
        jcb_nothing.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_nothing);

        jlbl_borderTitle = new MLabel("Border [percent]:");
        jlbl_borderTitle.setBounds(ViewSettings.getDistanceBetweenItems(), 
                jcb_nothing.getY() + jcb_nothing.getHeight() 
                + ViewSettings.getDistanceBetweenItems(), 
                buttonWidth, buttonHeight);
        jlbl_borderTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_borderTitle.setFocusable(false);
        super.add(jlbl_borderTitle);

        //top
        jlbl_subtitle_borderTop = new MLabel("Top");
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
        jcb_margeTop.addActionListener(_cex);
        super.add(jcb_margeTop);
        
        //left
        jlbl_subtitle_borderLeft = new MLabel("Left");
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
        jcb_margeLeft.addActionListener(_cex);
        super.add(jcb_margeLeft);

        //bottom
        jlbl_subtitle_borderBottom = new MLabel("Bottom");
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
        jcb_margeBottom.addActionListener(_cex);
        super.add(jcb_margeBottom);

        //right
        jlbl_subtitle_borderRight = new MLabel("Right");
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
        jcb_margeRight.addActionListener(_cex);
        super.add(jcb_margeRight);
    }

    
    /**
     * Initializes the second part of the first column which contains
     * the components for changing the displaying of alpha values.
     * Choices: Save pure alpha as alpha value or save it as white pixel.
     */
    private void initializeColumn1Alpha(CExport _cex) {

        /*
         * Alpha
         */
        //right
        jlbl_displayAlphaTitle = new MLabel("Transpareny");
        jlbl_displayAlphaTitle.setSize(buttonWidth, buttonHeight);
        jlbl_displayAlphaTitle.setLocation(
                ViewSettings.getDistanceBetweenItems() 
                + jcb_margeBottom.getX() + jcb_margeBottom.getWidth()
                + ViewSettings.getDistanceBetweenItems(),
                jlbl_backgroundTitle.getY());
        jlbl_displayAlphaTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_displayAlphaTitle.setFocusable(false);
        super.add(jlbl_displayAlphaTitle);
        
        jlbl_subtitle_alpha = new MLabel("alpha values ...");
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
        jcb_displayAlpha.addActionListener(_cex);
        super.add(jcb_displayAlpha);
    }
    

    
    /**
     * Initializes the second part of the first column which contains
     * the components for changing the export format of the image.
     */
    private void initializeColumn1SaveOptions(CExport _cex) {

        /*
         * Alpha
         */
        //right
        jlbl_saveOptionsTitle = new MLabel("Save options");
        jlbl_saveOptionsTitle.setSize(buttonWidth, buttonHeight);
        jlbl_saveOptionsTitle.setLocation(
                jlbl_displayAlphaTitle.getX(),
                jcb_displayAlpha.getY() + jcb_displayAlpha.getHeight()
                + ViewSettings.getDistanceBetweenItems());
        jlbl_saveOptionsTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_saveOptionsTitle.setFocusable(false);
        super.add(jlbl_saveOptionsTitle);
        
        jlbl_subtitle_saveOptions = new MLabel("save file as");
        jlbl_subtitle_saveOptions.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_saveOptions.setLocation(jlbl_saveOptionsTitle.getX(),
                jlbl_saveOptionsTitle.getHeight() 
                + jlbl_saveOptionsTitle.getY()
                + ViewSettings.getDistanceBetweenItems());
        jlbl_subtitle_saveOptions.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_subtitle_saveOptions.setFocusable(false);
        super.add(jlbl_subtitle_saveOptions);
        
        jcb_saveFormats = new JComboBox(Constants.SAVE_FORMATS);
        jcb_saveFormats.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_saveFormats.setLocation(jlbl_subtitle_saveOptions.getX() 
                + jlbl_subtitle_saveOptions.getWidth() 
                + ViewSettings.getDistanceBetweenItems(),
                jlbl_subtitle_saveOptions.getY());
        jcb_saveFormats.addActionListener(_cex);
        super.add(jcb_saveFormats);
    }
    
    
    /**
     * Apply the values set in Settings to the components.
     */
    private void applySettings() {

        //apply the values set in Settings to the components:
        jcb_margeLeft.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                Status.getBorderLeftPercent()));
        jcb_margeRight.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                Status.getBorderRightPercent()));
        jcb_margeTop.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                Status.getBorderTopPercent()));
        jcb_margeBottom.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                Status.getBorderBottomPercent()));
        
        switch (Status.getIndexPageBackgroundExport()) {
        case Constants.CONTROL_PAGE_BACKGROUND_RASTAR:
            jcb_raster.setSelected(true);
            jcb_lines.setSelected(false);
            jcb_nothing.setSelected(false);
            break;
        case Constants.CONTROL_PAGE_BACKGROUND_LINES:
            jcb_raster.setSelected(false);
            jcb_lines.setSelected(true);
            jcb_nothing.setSelected(false);
            break;
        case Constants.CONTROL_PAGE_BACKGROUND_NONE:
            jcb_raster.setSelected(false);
            jcb_lines.setSelected(false);
            jcb_nothing.setSelected(true);
            break;
        default:
            Status.getLogger().severe("error: unknown type of background");
            break;
        }
        
        if (Status.isExportAlpha()) {

            jcb_displayAlpha.setSelectedIndex(1);
        } else {

            jcb_displayAlpha.setSelectedIndex(0);
        }
        
        jcb_saveFormats.setSelectedItem(Status.getSaveFormat());
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


    /**
     * @return the jcb_saveFormats
     */
    public JComboBox getJcb_saveFormats() {
        return jcb_saveFormats;
    }

}
