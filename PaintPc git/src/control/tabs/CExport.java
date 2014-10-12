package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.forms.Page;
import view.forms.tabs.Export;
import model.settings.Constants;
import model.settings.Status;



/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CExport implements ActionListener {

    /**
     * Empty utility class constructor.
     */
    private CExport() { }


    
    /**
     * The only instance of this class.
     */
    private static CExport instance;
    
    
    @Override public void actionPerformed(final ActionEvent _event) {

        int backgroundID;
        if (_event.getSource().equals(Export.getInstance().getJcb_raster())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
            Export.getInstance().getJcb_raster().setSelected(true);
            Export.getInstance().getJcb_nothing().setSelected(false);
            Export.getInstance().getJcb_lines().setSelected(false);
        
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_lines())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
            Export.getInstance().getJcb_lines().setSelected(true);
            Export.getInstance().getJcb_nothing().setSelected(false);
            Export.getInstance().getJcb_raster().setSelected(false);
        
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_nothing())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            Export.getInstance().getJcb_nothing().setSelected(true);
            Export.getInstance().getJcb_lines().setSelected(false);
            Export.getInstance().getJcb_raster().setSelected(false);
        
        } else {
            Status.getLogger().warning("no background selected.");
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
        } 
        Status.setIndexPageBackgroundExport(backgroundID);
        Page.getInstance().getJlbl_painting().refreshPaint();
        
    }
    
    
    /**
     * Return the only instance of this class.
     * @return the only instance of this class.
     */
    public static CExport getInstance() {
        if (instance == null) {
            instance = new CExport();
        }
        return instance;
    }

}
