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
import control.forms.tabs.CTabLook;


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
     * The JCheckBox for background of new Image.
     */
    private JCheckBox jcb_raster, jcb_lines, jcb_nothing, jcb_borderEnabled;
    
    /**
     * The JCombo boxes for the size of the border.
     */
    private MComboBox jcb_margeTop, jcb_margeLeft, jcb_margeRight,
    jcb_margeBottom;
    
    /**
     * The JCombo box for displaying of alpha.
     */
    private MComboBox jcb_displayAlpha;

    /**
     * Title JLabel.
     */
    private MLabel jlbl_backgroundTitle, jlbl_borderTitle, 
    jlbl_subtitle_borderTop, jlbl_subtitle_borderBottom, 
    jlbl_subtitle_borderLeft, jlbl_subtitle_borderRight,
    jlbl_displayAlphaTitle, jlbl_subtitle_alpha;
    

    /**
     * Final values for the size and location.
     */
    private final int buttonWidth = 275;
    
    /**
     * Constructor: Initializes and uses the controller class for this tab for
     * 				adding listeners to components.
     * 
     * @param _clook		the controller class for this class that is used
     * 						for adding listeners to componetns.
     */
    public Look(final CTabLook _clook) { 
        super(2);
        //initialize JPanel and alter settings
        super.setOpaque(false);
        super.setLayout(null);
        super.setVisible(true);

        //initialize the content for the background
        int x = initializeFirstColumn(0, _clook, true);
        
        //initialize the content for the visualization
        initializeSecondColumn(x, _clook, true);
        
        //apply the right settings to components (out of Status class)
        applySettings();
    }
    
    
    

    /**
     * Initialize the first column.
     * @param _x 		the x start coordinate (the first added graphical user
     * 					interface element that is added by the following method
     * 					is added right at position _x.
     * 
     * @param _paint 	this value is true if the components have to be created
     * 					and added to this tab (thus it has to be true the first
     * 					time it is called).<br>
     * 
     * 					If it is false, only the size of each component (that 
     * 					in general is dependent on the current window size)
     * 					is re-computed and applied.
     * 
     * @param _clook	the controller class for this tab.
     * 
     * @return			the x coordinate of the (last) separation element which
     * 					terminates the first column.
     */
    private int initializeFirstColumn(final int _x, 
    		final CTabLook _clook,
    		final boolean _paint) {

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
            jcb_raster.addActionListener(_clook);	
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
            jcb_lines.addActionListener(_clook);
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
            jcb_nothing.addActionListener(_clook);
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
            jcb_borderEnabled.addActionListener(_clook);	

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

            jcb_margeTop.addActionListener(_clook);
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

            jcb_margeBottom.addActionListener(_clook);
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

            jcb_margeLeft.addActionListener(_clook);
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

            jcb_margeRight.addActionListener(_clook);
            super.add(jcb_margeRight);
            
            
        }
        insertSectionStuff("Image Background", 0, jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems(), 0, _paint);
        return jcb_margeRight.getX()
                + jcb_margeRight.getWidth() 
                + ViewSettings.getDistanceBetweenItems();
    }
    
    

    
    @Override public void applySize() {
    	super.applySize();
    	int x = initializeFirstColumn(0, null, false);
    	initializeSecondColumn(x, null, false);
    	
    }
    
    /**
     * Apply the values set in Settings to the components.
     */
    private void applySettings() {

        //apply the values set in Settings to the components:
        jcb_margeLeft.setSelectedIndex(getBorderPercentagesSettingsIndex(
                State.getBorderLeftPercent()));
        jcb_margeRight.setSelectedIndex(getBorderPercentagesSettingsIndex(
                State.getBorderRightPercent()));
        jcb_margeTop.setSelectedIndex(getBorderPercentagesSettingsIndex(
                State.getBorderTopPercent()));
        jcb_margeBottom.setSelectedIndex(getBorderPercentagesSettingsIndex(
                State.getBorderBottomPercent()));
        
        
        switch (State.getIndexPageBackground()) {
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
        
        if (State.isShowAlpha()) {

            jcb_displayAlpha.setSelectedIndex(1);
        } else {

            jcb_displayAlpha.setSelectedIndex(0);
        }
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
            State.getLogger().severe("Fatal error: Value set in settings"
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
    private int initializeSecondColumn(final int _x, final CTabLook _clook,
    		final boolean _paint) {

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

            jcb_displayAlpha.addActionListener(_clook);
            super.add(jcb_displayAlpha);
        }

        insertSectionStuff("Visualization", jlbl_displayAlphaTitle.getX(), 
                jcb_displayAlpha.getX() + jcb_displayAlpha.getWidth()
                + ViewSettings.getDistanceBetweenItems(), 1, _paint);
        
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




	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}




	public JCheckBox getJcb_borderEnabled() {
		return jcb_borderEnabled;
	}




	/**
	 * @return the jlbl_subtitle_borderTop
	 */
	public MLabel getJlbl_subtitle_borderTop() {
		return jlbl_subtitle_borderTop;
	}




	/**
	 * @return the jlbl_subtitle_borderBottom
	 */
	public MLabel getJlbl_subtitle_borderBottom() {
		return jlbl_subtitle_borderBottom;
	}




	/**
	 * @return the jlbl_subtitle_borderLeft
	 */
	public MLabel getJlbl_subtitle_borderLeft() {
		return jlbl_subtitle_borderLeft;
	}




	/**
	 * @return the jlbl_subtitle_borderRight
	 */
	public MLabel getJlbl_subtitle_borderRight() {
		return jlbl_subtitle_borderRight;
	}
}
