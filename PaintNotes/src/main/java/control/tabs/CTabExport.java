package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.ControlPaint;
import view.tabs.Export;
import model.settings.Constants;
import model.settings.Status;



/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CTabExport implements ActionListener {

	/**
	 * An instance of the root - controller - class for fetching the instances
	 * of important classes.
	 */
	private ControlPaint cp;
    
	/**
     * Empty utility class constructor.
     * @param _cp	an instance of the root-controller-class out of which the
     * 				instances of important classes can be fetched.
     */
    public CTabExport(final ControlPaint _cp) {
    	this.cp = _cp;
    }


    
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent _event) {

    	if (getExport() != null) {
    		 int backgroundID;
    	        if (_event.getSource().equals(getExport().getJcb_raster())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
    	            getExport().getJcb_raster().setSelected(true);
    	            getExport().getJcb_nothing().setSelected(false);
    	            getExport().getJcb_lines().setSelected(false);
    	            Status.setIndexPageBackgroundExport(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_lines())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
    	            getExport().getJcb_lines().setSelected(true);
    	            getExport().getJcb_nothing().setSelected(false);
    	            getExport().getJcb_raster().setSelected(false);
    	            Status.setIndexPageBackgroundExport(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_nothing())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
    	            getExport().getJcb_nothing().setSelected(true);
    	            getExport().getJcb_lines().setSelected(false);
    	            getExport().getJcb_raster().setSelected(false);
    	            Status.setIndexPageBackgroundExport(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeBottom())) {
    	            
    	            String str_selected = getExport().getJcb_margeBottom()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderBottomPercentExport(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeLeft())) {
    	            

    	            String str_selected = getExport().getJcb_margeLeft()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderLeftPercentExport(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeTop())) {

    	            String str_selected = getExport().getJcb_margeTop()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderTopPercentExport(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeRight())) {

    	            String str_selected = getExport().getJcb_margeRight()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderRightPercentExport(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(getExport()
    	                .getJcb_displayAlpha())) {
    	            Status.setExportAlpha(getExport().getJcb_displayAlpha()
    	                    .getSelectedItem().equals(
    	                    		Constants.ID_DISPLAY_ALPHA));
    	        } else if (_event.getSource().equals(getExport()
    	                .getJcb_saveFormats())) {
    	            Status.setSaveFormat(getExport().getJcb_saveFormats()
    	                    .getSelectedItem().toString());
    	        }
    	        
    	    }
    	}




	/**
	 * Error - checked getter method for getting the export-tab.
	 * 
	 * @return 	an instance of Export fetched out of the root controller-class.
	 */
	private Export getExport() {
		
		if (cp != null) {
			if (cp.getView() != null) {
				if (cp.getView().getTabs() != null) {
					if (cp.getView().getTabs().getTab_export() != null) {
	
				    	return cp.getView().getTabs().getTab_export();
					} else {
						Status.getLogger().severe("cp.getView().getTabs()"
								+ ".getTab_export() is null");
					}
				} else {
					Status.getLogger().severe("cp.getView().getTabs() is null");
				}
			} else {
				Status.getLogger().severe("cp.getView() is null");
			}
		} else {
			Status.getLogger().severe("cp is null");
		}
		return null;
	}
       

}
