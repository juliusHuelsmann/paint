package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            Status.setIndexPageBackgroundExport(backgroundID);
        
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_lines())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
            Export.getInstance().getJcb_lines().setSelected(true);
            Export.getInstance().getJcb_nothing().setSelected(false);
            Export.getInstance().getJcb_raster().setSelected(false);
            Status.setIndexPageBackgroundExport(backgroundID);
        
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_nothing())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            Export.getInstance().getJcb_nothing().setSelected(true);
            Export.getInstance().getJcb_lines().setSelected(false);
            Export.getInstance().getJcb_raster().setSelected(false);
            Status.setIndexPageBackgroundExport(backgroundID);
        
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_margeBottom())) {
            
            String str_selected = Export.getInstance().getJcb_margeBottom()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderBottomPercentExport(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_margeLeft())) {
            

            String str_selected = Export.getInstance().getJcb_margeLeft()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderLeftPercentExport(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_margeTop())) {

            String str_selected = Export.getInstance().getJcb_margeTop()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderTopPercentExport(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } else if (_event.getSource().equals(
                Export.getInstance().getJcb_margeRight())) {

            String str_selected = Export.getInstance().getJcb_margeRight()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderRightPercentExport(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } else if (_event.getSource().equals(Export.getInstance()
                .getJcb_displayAlpha())) {
            Status.setExportAlpha(Export.getInstance().getJcb_displayAlpha()
                    .getSelectedItem().equals(Constants.ID_DISPLAY_ALPHA));
        }
        
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
