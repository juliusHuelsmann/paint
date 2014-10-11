package view.forms.tabs;

import java.awt.Toolkit;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.settings.ViewSettings;
import control.tabs.COutlook;


/**
 * View class for tab.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Outlook extends JPanel {

    
    /**
     * The only instance of this class.
     */
    private static Outlook instance;

    /**
     * The JCheckBox for background of new Image.
     */
    private JCheckBox jcb_raster, jcb_lines, jcb_nothing;

    /**
     * Title JLabel.
     */
    private JLabel jlbl_backgroundTitle;
    
    /**
     * Empty utility class constructor.
     */
    private Outlook() { }
    
    
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

        
        final int distance = 5, buttonHeight = 20, buttonWidth = 200;

        jlbl_backgroundTitle = new JLabel("Background:");
        jlbl_backgroundTitle.setBounds(distance, distance, 
                buttonWidth, buttonHeight);
        jlbl_backgroundTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_backgroundTitle.setFocusable(false);
        super.add(jlbl_backgroundTitle);

        jcb_raster = new JCheckBox("Raster");
        jcb_raster.setFocusable(false);
        jcb_raster.setOpaque(false);
        jcb_raster.addActionListener(COutlook.getInstance());
        jcb_raster.setLocation(jlbl_backgroundTitle.getX(), 
                distance + jlbl_backgroundTitle.getY()
                + jlbl_backgroundTitle.getHeight());
        jcb_raster.setSize(buttonWidth, buttonHeight);
        jcb_raster.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jcb_raster.setSelected(true);
        super.add(jcb_raster);
        
        jcb_lines = new JCheckBox("Lines");
        jcb_lines.setFocusable(false);
        jcb_lines.setOpaque(false);
        jcb_lines.addActionListener(COutlook.getInstance());
        jcb_lines.setLocation(jcb_raster.getWidth() + jcb_raster.getX() 
                + distance, jcb_raster.getY());
        jcb_lines.setSize(buttonWidth, buttonHeight);
        jcb_lines.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_lines);
        
        jcb_nothing = new JCheckBox("Nothing");
        jcb_nothing.setFocusable(false);
        jcb_nothing.setOpaque(false);
        jcb_nothing.addActionListener(COutlook.getInstance());
        jcb_nothing.setLocation(jcb_lines.getWidth() + jcb_lines.getX() 
                + distance, jcb_raster.getY());
        jcb_nothing.setSize(buttonWidth, buttonHeight);
        jcb_nothing.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jcb_nothing);

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
    public static Outlook getInstance() {
        
        if (instance == null) {
            instance = new Outlook();
            instance.initialize();
        }
        return instance;
    }


}
