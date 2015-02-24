package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.ContorlPicture;
import control.ControlPaint;
import view.forms.Page;
import view.tabs.Look;
import model.settings.Constants;
import model.settings.Status;



/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CLook implements ActionListener {

	private ControlPaint cp;
    /**
     * Empty utility class constructor.
     */
    public CLook(ControlPaint _cp) { 
    	this.cp = _cp;
    }

    
    private Page getPage() {
    	return cp.getView().getPage();
    }

    public Look getLook() {
    	return cp.getView().getTabs().getTab_look();
    }
    public ContorlPicture getControlPicture() {
    	return cp.getControlPic();
    }
    

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent _event) {

    	if (getLook() != null) {
    		  int backgroundID;
    	        if (_event.getSource().equals(getLook().getJcb_raster())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
    	            getLook().getJcb_raster().setSelected(true);
    	            getLook().getJcb_nothing().setSelected(false);
    	            getLook().getJcb_lines().setSelected(false);
    	            Status.setIndexPageBackground(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getLook().getJcb_lines())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
    	            getLook().getJcb_lines().setSelected(true);
    	            getLook().getJcb_nothing().setSelected(false);
    	            getLook().getJcb_raster().setSelected(false);
    	            Status.setIndexPageBackground(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getLook().getJcb_nothing())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
    	            getLook().getJcb_nothing().setSelected(true);
    	            getLook().getJcb_lines().setSelected(false);
    	            getLook().getJcb_raster().setSelected(false);
    	            Status.setIndexPageBackground(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getLook().getJcb_margeBottom())) {
    	            
    	            String str_selected = getLook().getJcb_margeBottom()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderBottomPercent(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getLook().getJcb_margeLeft())) {
    	            

    	            String str_selected = getLook().getJcb_margeLeft()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderLeftPercent(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getLook().getJcb_margeTop())) {

    	            String str_selected = getLook().getJcb_margeTop()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderTopPercent(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getLook().getJcb_margeRight())) {

    	            String str_selected = getLook().getJcb_margeRight()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                Status.setBorderRightPercent(int_selected);
    	            } catch (Exception e) {
    	                Status.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(getLook()
    	                .getJcb_displayAlpha())) {

    	            Status.setShowAlpha(getLook().getJcb_displayAlpha()
    	                    .getSelectedItem().equals(Constants.ID_DISPLAY_ALPHA));
    	            
    	            if (getPage().getWidth() > 0) {

    	                
    	            	getPage().flip();
    	            }
    	            
    	        }
    	        getControlPicture().refreshPaint();
    	        
    	    }
    }
      
}
