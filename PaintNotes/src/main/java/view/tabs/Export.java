//package declarations
package view.tabs;

//import declarations
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import view.forms.Help;
import view.util.mega.MComboBox;
import view.util.mega.MLabel;
import model.settings.Constants;
import model.settings.State;
import model.settings.ViewSettings;
import control.forms.tabs.CTabExport;


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
    private JCheckBox jcb_raster, jcb_lines, jcb_nothing, jcb_borderEnabled;
    
    /**
     * The JCombo boxes for the size of the border.
     */
    private MComboBox jcb_margeTop, jcb_margeLeft, jcb_margeRight,
    jcb_margeBottom;
    

    /**
     * Title MLabel.
     */
    private MLabel jlbl_backgroundTitle, jlbl_borderTitle, 
    jlbl_subtitle_borderTop, jlbl_subtitle_borderBottom, 
    jlbl_subtitle_borderLeft, jlbl_subtitle_borderRight;
    

    /**
     * Titles for MComboBox by which the user ins able to set the exported
     * alpha color from alpha to white.
     */
    private MLabel jlbl_displayAlphaTitle, jlbl_subtitle_alpha;
    

    /**
     * MComboBox by which the user ins able to set the exported alpha color 
     * from alpha to white.
     */
    private MComboBox jcb_displayAlpha;

    /*
     * Save as
     */
    /**
     * Contains the different saveFormats.
     */
    private MComboBox jcb_saveFormats;
    
    /**
     * Titles for save as.
     */
    private MLabel jlbl_subtitle_saveOptions;
    
    
    /**
     * Final values for the size and location.
     */
    private final int buttonWidth = 275;
    
    /**
     * Empty utility class constructor.
     */
    public Export() {
        super(2);
        //initialize JPanel and alter settings
        super.setOpaque(false);
        super.setLayout(null);
        super.setVisible(true);

    }
    
    /**
     * Initializes the content for the background.
     * @param _cex the controller class of the export-tab.
     */
    public void initialize(final CTabExport _cex) {

        //initialize the content for the background
        initializeColumn1(_cex);    	
    }
    

    /**
     * Initialize the first column.
     * @param _cex the controller class of the export-tab.
     */
    private void initializeColumn1(final CTabExport _cex) {
        
        //initialize the first part of the first column containing the 
        //possibility to choose an image background for the export and
        //to set its border sizes.
        int x = initializeColumn1BG(_cex, true);
        
        //initializes the second part of the first column which contains
        //the components for changing the displaying of alpha values.
        //Choices: Save pure alpha as alpha value or save it as white pixel.
        //initializes the second part of the first column which contains
        //the components for changing the export format of the image.
        x = initializeColumn1Alpha(_cex, true, x);

        //apply the settings set in the Status class to the view items 
        //initialized above; Thus the user settings can be saved and restored
        applySettings();
    }
    


    /**
     * Initialize the first part of the first column containing the 
     * possibility to choose an image background for the export and
     * to set its border sizes.
     * @param _cex the controller class of the export-tab.
     */
    private int initializeColumn1BG(final CTabExport _cex,
    		final boolean _paint
    		) {

    	final int buttonHeight = 
    			(ViewSettings.getView_heightTB_visible()
    	                -  ViewSettings.getDistanceBetweenItems() 
    	                - ViewSettings.getView_heightTB()
    	                / ViewSettings.TABBED_PANE_TITLE_PROPORTION_HEIGHT)
    			/ (2 + 1)
    			- 2 * ViewSettings.getDistanceBetweenItems();
    	
    	if (_paint) {

            jlbl_backgroundTitle = new MLabel("Background:");
    	}
        jlbl_backgroundTitle.setBounds(
        		ViewSettings.getDistanceBetweenItems(),
                ViewSettings.getDistanceBetweenItems(), 
                buttonWidth / 2, buttonHeight);
        if (_paint) {
        	jlbl_backgroundTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
            jlbl_backgroundTitle.setFocusable(false);
            super.add(jlbl_backgroundTitle);

            jcb_raster = new JCheckBox("Raster");
            jcb_raster.setFocusable(false);
            jcb_raster.setOpaque(false);
            jcb_raster.addActionListener(_cex);	
        }
        
        jcb_raster.setLocation(
        		jlbl_backgroundTitle.getX() 
        		+ ViewSettings.getDistanceBetweenItems()
        		+ jlbl_backgroundTitle.getWidth(),
                jlbl_backgroundTitle.getY()
//                + ViewSettings.getDistanceBetweenItems()
//                + jlbl_backgroundTitle.getHeight()
                );
        jcb_raster.setSize(buttonWidth * 2 / (2 + 2), buttonHeight);
        
        if (_paint) {

            jcb_raster.setFont(ViewSettings.GENERAL_FONT_ITEM);
            jcb_raster.setSelected(true);
            super.add(jcb_raster);
            
            jcb_lines = new JCheckBox("Lines");
            jcb_lines.setFocusable(false);
            jcb_lines.setOpaque(false);
            jcb_lines.addActionListener(_cex);
        }
        jcb_lines.setLocation(jcb_raster.getWidth() + jcb_raster.getX() 
                + ViewSettings.getDistanceBetweenItems(), jcb_raster.getY());
        jcb_lines.setSize(jcb_raster.getSize());
        
        if (_paint) {

            jcb_lines.setFont(ViewSettings.GENERAL_FONT_ITEM);
            super.add(jcb_lines);
            
            jcb_nothing = new JCheckBox("Nothing");
            jcb_nothing.setFocusable(false);
            jcb_nothing.setOpaque(false);
            jcb_nothing.addActionListener(_cex);
        }
        jcb_nothing.setLocation(jcb_lines.getWidth() + jcb_lines.getX() 
                + ViewSettings.getDistanceBetweenItems(), jcb_raster.getY());
        jcb_nothing.setSize(jcb_raster.getSize());
        
        if (_paint) {

            jcb_nothing.setFont(ViewSettings.GENERAL_FONT_ITEM);
            super.add(jcb_nothing);


            jlbl_borderTitle = new MLabel("Border:");
        }
        jlbl_borderTitle.setBounds(ViewSettings.getDistanceBetweenItems(), 
                jcb_nothing.getY() + jcb_nothing.getHeight() 
                + ViewSettings.getDistanceBetweenItems(), 
                buttonWidth / 2, buttonHeight);
        
        if (_paint) {

            jlbl_borderTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
            jlbl_borderTitle.setFocusable(false);
            super.add(jlbl_borderTitle);
            //top
            jlbl_subtitle_borderTop = new MLabel("Top");

            jcb_borderEnabled = new JCheckBox("enabled");
            jcb_borderEnabled.setFocusable(false);
            jcb_borderEnabled.setOpaque(false);
            jcb_borderEnabled.addActionListener(_cex);	

            jcb_borderEnabled.setFont(ViewSettings.GENERAL_FONT_ITEM);
            jcb_borderEnabled.setSelected(true);
            super.add(jcb_borderEnabled);
        }
        jcb_borderEnabled.setLocation(
        		jlbl_borderTitle.getX() 
        		+ ViewSettings.getDistanceBetweenItems()
        		+ jlbl_borderTitle.getWidth(),
        		jlbl_borderTitle.getY()
//                + ViewSettings.getDistanceBetweenItems()
//                + jlbl_backgroundTitle.getHeight()
                );
        jcb_borderEnabled.setSize(buttonWidth * 2 / (2 + 2), buttonHeight);
        
        
        
        
        jlbl_subtitle_borderTop.setSize(buttonWidth / (2 + 2), buttonHeight);
        jlbl_subtitle_borderTop.setLocation(jlbl_borderTitle.getX(), 
                ViewSettings.getDistanceBetweenItems() + jlbl_borderTitle.getY()
                + jlbl_borderTitle.getHeight());
        
        if (_paint) {

            jlbl_subtitle_borderTop.setFont(
            		ViewSettings.GENERAL_FONT_ITEM_PLAIN);
            jlbl_subtitle_borderTop.setFocusable(false);
            super.add(jlbl_subtitle_borderTop);
            
            jcb_margeTop = new MComboBox(Constants.getBorderPercentagesTitle());
        }
        jcb_margeTop.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeTop.setLocation(jlbl_subtitle_borderTop.getX() 
                + jlbl_subtitle_borderTop.getWidth()
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderTop.getY());
        
        if (_paint) {

            jcb_margeTop.addActionListener(_cex);
            super.add(jcb_margeTop);

            jlbl_subtitle_borderBottom = new MLabel("Bottom");
        }
        jlbl_subtitle_borderBottom.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderBottom.setLocation(jcb_margeTop.getX()
                + jcb_margeTop.getWidth() 
                + ViewSettings.getDistanceBetweenItems(),
                jcb_margeTop.getY());
        
        if (_paint) {

            jlbl_subtitle_borderBottom.setFont(
                    ViewSettings.GENERAL_FONT_ITEM_PLAIN);
            jlbl_subtitle_borderBottom.setFocusable(false);
            super.add(jlbl_subtitle_borderBottom);
          
            //bottom
            jcb_margeBottom = new MComboBox(
            		Constants.getBorderPercentagesTitle());
        }
        jcb_margeBottom.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeBottom.setLocation(jlbl_subtitle_borderBottom.getX() 
                + jlbl_subtitle_borderBottom.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderBottom.getY());
        if (_paint) {

            jcb_margeBottom.addActionListener(_cex);
            super.add(jcb_margeBottom);

            //left
            jlbl_subtitle_borderLeft = new MLabel("Left");
        }
        jlbl_subtitle_borderLeft.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderLeft.setLocation(jcb_margeBottom.getX()
        		+ jcb_margeBottom.getWidth()
        		+ ViewSettings.getDistanceBetweenItems(), 
                + jlbl_subtitle_borderBottom.getY());
        
        if (_paint) {

            jlbl_subtitle_borderLeft.setFont(
            		ViewSettings.GENERAL_FONT_ITEM_PLAIN);
            jlbl_subtitle_borderLeft.setFocusable(false);
            super.add(jlbl_subtitle_borderLeft);

            jcb_margeLeft = new MComboBox(
            		Constants.getBorderPercentagesTitle());
        }
        
        jcb_margeLeft.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeLeft.setLocation(jlbl_subtitle_borderLeft.getX() 
                + jlbl_subtitle_borderLeft.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jlbl_subtitle_borderLeft.getY());
        
        
        if (_paint) {

            jcb_margeLeft.addActionListener(_cex);
            super.add(jcb_margeLeft);
            
            //right
            jlbl_subtitle_borderRight = new MLabel("Right");
        }
        
        
        jlbl_subtitle_borderRight.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_borderRight.setLocation(jcb_margeLeft.getX() 
                + jcb_margeLeft.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jcb_margeLeft.getY());
        
        
        if (_paint) {
        	 jlbl_subtitle_borderRight.setFont(
        			 ViewSettings.GENERAL_FONT_ITEM_PLAIN);
             jlbl_subtitle_borderRight.setFocusable(false);
             super.add(jlbl_subtitle_borderRight);
             
             jcb_margeRight = new MComboBox(
            		 Constants.getBorderPercentagesTitle());
        }
       
        jcb_margeRight.setSize(jlbl_subtitle_borderTop.getSize());
        jcb_margeRight.setLocation(jlbl_subtitle_borderRight.getX() 
                + jlbl_subtitle_borderRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 
                jcb_margeLeft.getY());
        
        if (_paint) {

            jcb_margeRight.addActionListener(_cex);
            super.add(jcb_margeRight);
            
            
        }
        insertSectionStuff("Image Background", 0, jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 0, _paint);
        return jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems();
    }

    
    /**
     * Initializes the second part of the first column which contains
     * the components for changing the displaying of alpha values.
     * Choices: Save pure alpha as alpha value or save it as white pixel.
     * @param _cex the controller class of the export-tab.
     */
    private int initializeColumn1Alpha(
    		final CTabExport _cex,
    		final boolean _paint,
    		final int _x) {
        /*
         * Alpha
         */

    	final int buttonHeight = 
    			(ViewSettings.getView_heightTB_visible()
    	                -  ViewSettings.getDistanceBetweenItems() 
    	                - ViewSettings.getView_heightTB()
    	                / ViewSettings.TABBED_PANE_TITLE_PROPORTION_HEIGHT)
    			/ (2 + 2)
    			- ViewSettings.getDistanceBetweenItems();
    	
    	if (_paint) {

            //right
            jlbl_displayAlphaTitle = new MLabel("Ohter:");	
    	}
        jlbl_displayAlphaTitle.setSize(buttonWidth * 3 / 4, buttonHeight);
        jlbl_displayAlphaTitle.setLocation(_x
                + ViewSettings.getDistanceBetweenItems(),
                jlbl_backgroundTitle.getY());
        if (_paint) {

            jlbl_displayAlphaTitle.setFont(
            		ViewSettings.GENERAL_FONT_HEADLINE_2);
            jlbl_displayAlphaTitle.setFocusable(false);
            super.add(jlbl_displayAlphaTitle);
            
            jlbl_subtitle_alpha = new MLabel("alpha");
        }
        jlbl_subtitle_alpha.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_alpha.setLocation(jlbl_displayAlphaTitle.getX(),
                jlbl_displayAlphaTitle.getHeight() 
                + jlbl_displayAlphaTitle.getY()
                + ViewSettings.getDistanceBetweenItems());
        
        if (_paint) {

            jlbl_subtitle_alpha.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
            jlbl_subtitle_alpha.setFocusable(false);
            super.add(jlbl_subtitle_alpha);
            
            jcb_displayAlpha = new MComboBox(new String[]{Constants.ID_WHITE,
                    Constants.ID_DISPLAY_ALPHA});
        }
        jcb_displayAlpha.setSize(jlbl_displayAlphaTitle.getWidth()
        		- jlbl_subtitle_borderTop.getWidth()
        		- ViewSettings.getDistanceBetweenItems(),
        		buttonHeight);
        jcb_displayAlpha.setLocation(jlbl_subtitle_alpha.getX() 
                + jlbl_subtitle_alpha.getWidth() 
                + ViewSettings.getDistanceBetweenItems(),
                jlbl_subtitle_alpha.getY());
        
        if (_paint) {

            jcb_displayAlpha.addActionListener(_cex);
            super.add(jcb_displayAlpha);

            jlbl_subtitle_saveOptions = new MLabel("format");
        }
        
        

        jlbl_subtitle_saveOptions.setSize(jlbl_subtitle_borderTop.getSize());
        jlbl_subtitle_saveOptions.setLocation(jlbl_subtitle_alpha.getX(),
        		jlbl_subtitle_alpha.getHeight() 
                + jlbl_subtitle_alpha.getY()
                + ViewSettings.getDistanceBetweenItems());
        
        if (_paint) {

            jlbl_subtitle_saveOptions.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
            jlbl_subtitle_saveOptions.setFocusable(false);
            super.add(jlbl_subtitle_saveOptions);
            
            jcb_saveFormats = new MComboBox(Constants.SAVE_FORMATS);
        }
        jcb_saveFormats.setSize(jcb_displayAlpha.getSize());
        jcb_saveFormats.setLocation(jlbl_subtitle_saveOptions.getX() 
                + jlbl_subtitle_saveOptions.getWidth() 
                + ViewSettings.getDistanceBetweenItems(),
                jlbl_subtitle_saveOptions.getY());
        
        if (_paint) {

            jcb_saveFormats.addActionListener(_cex);
            super.add(jcb_saveFormats);
            

        }
        insertSectionStuff("Visualization", jlbl_displayAlphaTitle.getX(), 
                jcb_displayAlpha.getX() + jcb_displayAlpha.getWidth()
                + ViewSettings.getDistanceBetweenItems(), 1, _paint);
        
        return jcb_displayAlpha.getX() + jcb_displayAlpha.getWidth()
                + ViewSettings.getDistanceBetweenItems();
        
    
    
    
    }
    

    @Override public void applySize() {
    	super.applySize();
        //initialize the first part of the first column containing the 
        //possibility to choose an image background for the export and
        //to set its border sizes.
        int x = initializeColumn1BG(null, false);
        
        //initializes the second part of the first column which contains
        //the components for changing the displaying of alpha values.
        //Choices: Save pure alpha as alpha value or save it as white pixel.
        //initializes the second part of the first column which contains
        //the components for changing the export format of the image.
        x = initializeColumn1Alpha(null, false, x);

    	
    }
    
    
    
    /**
     * Apply the values set in Settings to the components.
     */
    private void applySettings() {

        //apply the values set in Settings to the components:
        jcb_margeLeft.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                State.getBorderLeftPercent()));
        jcb_margeRight.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                State.getBorderRightPercent()));
        jcb_margeTop.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                State.getBorderTopPercent()));
        jcb_margeBottom.setSelectedIndex(Look.getBorderPercentagesSettingsIndex(
                State.getBorderBottomPercent()));
        
        switch (State.getIndexPageBackgroundExport()) {
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
            State.getLogger().severe("error: unknown type of background");
            break;
        }
        
        if (State.isExportAlpha()) {

            jcb_displayAlpha.setSelectedIndex(1);
        } else {

            jcb_displayAlpha.setSelectedIndex(0);
        }
        
        jcb_saveFormats.setSelectedItem(State.getSaveFormat());
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
    public MComboBox getJcb_margeTop() {
        return jcb_margeTop;
    }



    /**
     * @return the jcb_margeLeft
     */
    public MComboBox getJcb_margeLeft() {
        return jcb_margeLeft;
    }

    
    /**
     * @return the jcb_margeRight
     */
    public MComboBox getJcb_margeRight() {
        return jcb_margeRight;
    }


    /**
     * @return the jcb_margeBottom
     */
    public MComboBox getJcb_margeBottom() {
        return jcb_margeBottom;
    }


    /**
     * @return the jcb_displayAlpha
     */
    public MComboBox getJcb_displayAlpha() {
        return jcb_displayAlpha;
    }


    /**
     * @return the jcb_saveFormats
     */
    public MComboBox getJcb_saveFormats() {
        return jcb_saveFormats;
    }

	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}

}
