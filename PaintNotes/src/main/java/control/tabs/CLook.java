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

	
	/**
	 * Instance of the root-controller class (saved in constructor).
	 */
	private final ControlPaint cp;
	
	
    /**
     * Empty utility class constructor.
     * 
     * @param _cp 	instance of the root-controller class.
     */
    public CLook(final ControlPaint _cp) { 
    	this.cp = _cp;
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
		        	setBackgroundNone();
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
		                    .getSelectedItem().equals(
		                    		Constants.ID_DISPLAY_ALPHA));
		            
		            if (getPage().getWidth() > 0) {
	
		                
		            	getPage().flip();
		            }
		            
		        }
		        getControlPicture().refreshPaint();
		        
		    }
	}
	

	
	public void setBackgroundNone() {

        int backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
        getLook().getJcb_nothing().setSelected(true);
        getLook().getJcb_lines().setSelected(false);
        getLook().getJcb_raster().setSelected(false);
        Status.setIndexPageBackground(backgroundID);
        

	}
	

	
	public void setMargeNone() {

        Status.setBorderRightPercent(0);
        Status.setBorderLeftPercent(0);
        Status.setBorderTopPercent(0);
        Status.setBorderBottomPercent(0);
        
        getLook().getJcb_margeRight().setSelectedIndex(0);
        getLook().getJcb_margeLeft().setSelectedIndex(0);
        getLook().getJcb_margeTop().setSelectedIndex(0);
        getLook().getJcb_margeBottom().setSelectedIndex(0);
	}

	/**
     * Error checked getter method for Page.
     * 
     * @return 	instance of Page fetched out of the root controller class.
     */
    private Page getPage() {
    	
    	if (cp != null) {
    		if (cp.getView() != null) {
    			if (cp.getView().getPage() != null) {
    	    		return cp.getView().getPage();
    	    	} else {
    	    		Status.getLogger().severe("cp.getView().getPage() is null");
    	    	}
        	} else {
	    		Status.getLogger().severe("cp.getView() is null");
	    	}
    	} else {
    		Status.getLogger().severe("cp is null");
    	}
    	
    	return null;
    }

    /**
     * Error checked getter method for Look.
     * 
     * @return 	instance of Look fetched out of the root controller class.
     */
    private Look getLook() {
    	if (cp != null) {
    		if (cp.getView() != null) {
    			if (cp.getView().getTabs() != null) {

        			if (cp.getView().getTabs() != null) {

        	    		return cp.getView().getTabs().getTab_look();
        			} else {
        				Status.getLogger().severe("cp.getView().getTabs()"
        						+ ".getTab_look() is null");
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
    
    /**
     * Error checked getter method for ContorlPicture.
     * 
     * @return 	instance of ContorlPicture fetched out of the root 
     * 			controller class.
     */
    private ContorlPicture getControlPicture() {

    	if (cp != null) {
    		if (cp.getControlPic() != null) {
    	    	return cp.getControlPic();
    		} else {
        		Status.getLogger().severe("cp.getControlPic() is null");
    		}
    	} else {
    		Status.getLogger().severe("cp is null");
    	}
    	return null;
    
    }
      
}
