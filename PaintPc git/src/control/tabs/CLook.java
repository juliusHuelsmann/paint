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
public final class CLook implements ActionListener {

    /**
     * Empty utility class constructor.
     */
    private CLook() { }


    
    /**
     * The only instance of this class.
     */
    private static CLook instance;
    
    
    @Override public void actionPerformed(final ActionEvent _event) {

        int backgroundID;
        if (_event.getSource().equals(Look.getInstance().getJcb_raster())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
            Look.getInstance().getJcb_raster().setSelected(true);
            Look.getInstance().getJcb_nothing().setSelected(false);
            Look.getInstance().getJcb_lines().setSelected(false);
            Status.setIndexPageBackground(backgroundID);
        
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_lines())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
            Look.getInstance().getJcb_lines().setSelected(true);
            Look.getInstance().getJcb_nothing().setSelected(false);
            Look.getInstance().getJcb_raster().setSelected(false);
            Status.setIndexPageBackground(backgroundID);
        
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_nothing())) {
            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            Look.getInstance().getJcb_nothing().setSelected(true);
            Look.getInstance().getJcb_lines().setSelected(false);
            Look.getInstance().getJcb_raster().setSelected(false);
            Status.setIndexPageBackground(backgroundID);
        
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_margeBottom())) {
            
            String str_selected = Look.getInstance().getJcb_margeBottom()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderBottomPercent(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_margeLeft())) {
            

            String str_selected = Look.getInstance().getJcb_margeLeft()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderLeftPercent(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_margeTop())) {

            String str_selected = Look.getInstance().getJcb_margeTop()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderTopPercent(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } else if (_event.getSource().equals(
                Look.getInstance().getJcb_margeRight())) {

            String str_selected = Look.getInstance().getJcb_margeRight()
                    .getSelectedItem().toString().replace("%", "");
            
            try {

                int int_selected = Integer.parseInt(str_selected);
                Status.setBorderRightPercent(int_selected);
            } catch (Exception e) {
                Status.getLogger().severe(
                        "error: change border size: wrong input");
            }
        } 
        Page.getInstance().getJlbl_painting().refreshPaint();
        
    }
    
    
    /**
     * Return the only instance of this class.
     * @return the only instance of this class.
     */
    public static CLook getInstance() {
        if (instance == null) {
            instance = new CLook();
        }
        return instance;
    }

}
