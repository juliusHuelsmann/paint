package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.forms.Page;
import view.forms.tabs.Look;
import model.settings.Constants;
import model.settings.Status;



/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class COutlook implements ActionListener {

    /**
     * Empty utility class constructor.
     */
    private COutlook() { }


    
    /**
     * The only instance of this class.
     */
    private static COutlook instance;
    
    
    @Override public void actionPerformed(final ActionEvent _event) {

        int backgroundID;
        if (_event.getSource().equals(Look.getInstance().getJcb_raster())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
            Look.getInstance().getJcb_raster().setSelected(true);
            Look.getInstance().getJcb_nothing().setSelected(false);
            Look.getInstance().getJcb_lines().setSelected(false);
        
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_lines())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
            Look.getInstance().getJcb_lines().setSelected(true);
            Look.getInstance().getJcb_nothing().setSelected(false);
            Look.getInstance().getJcb_raster().setSelected(false);
        
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_nothing())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            Look.getInstance().getJcb_nothing().setSelected(true);
            Look.getInstance().getJcb_lines().setSelected(false);
            Look.getInstance().getJcb_raster().setSelected(false);
        
        } else {
            Status.getLogger().warning("no background selected.");
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
        } 
        Status.setIndexPageBackground(backgroundID);
        Page.getInstance().getJlbl_painting().refreshPaint();
        
    }
    
    
    /**
     * Return the only instance of this class.
     * @return the only instance of this class.
     */
    public static COutlook getInstance() {
        if (instance == null) {
            instance = new COutlook();
        }
        return instance;
    }

}
