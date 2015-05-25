package control.forms.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.ContorlPicture;
import control.ControlPaint;
import view.forms.Page;
import view.tabs.Look;
import model.settings.Constants;
import model.settings.State;



/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CTabLook implements ActionListener {

	
	/**
	 * Instance of the root-controller class (saved in constructor).
	 */
	private final ControlPaint cp;
	
	
    /**
     * Empty utility class constructor.
     * 
     * @param _cp 	instance of the root-controller class.
     */
    public CTabLook(final ControlPaint _cp) { 
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
		            State.setIndexPageBackground(backgroundID);
		        
		        } else if (_event.getSource().equals(
		                getLook().getJcb_lines())) {
		            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
		            getLook().getJcb_lines().setSelected(true);
		            getLook().getJcb_nothing().setSelected(false);
		            getLook().getJcb_raster().setSelected(false);
		            State.setIndexPageBackground(backgroundID);
		        
		        } else if (_event.getSource().equals(
		                getLook().getJcb_nothing())) {
		        	setBackgroundNone();
		        } else if (_event.getSource().equals(
		                getLook().getJcb_margeBottom())) {
		        	applyMargeBottom();
		        } else if (_event.getSource().equals(
		                getLook().getJcb_margeLeft())) {
		        	applyMargeLeft();
		        } else if (_event.getSource().equals(
		                getLook().getJcb_margeTop())) {
		        	applyMargeTop();
		        } else if (_event.getSource().equals(
		                getLook().getJcb_margeRight())) {
		        	applyMargeRight();
		        } else if (_event.getSource().equals(getLook()
		                .getJcb_displayAlpha())) {
	
		            State.setShowAlpha(getLook().getJcb_displayAlpha()
		                    .getSelectedItem().equals(
		                    		Constants.ID_DISPLAY_ALPHA));
		            
		            if (getPage().getWidth() > 0) {
	
		                
		            	getPage().flip();
		            }
		            
		        } else if (_event.getSource().equals(getLook()
		        		.getJcb_borderEnabled())) {
		        	if (!getLook().getJcb_borderEnabled().isSelected()) {
		        		
		        		//do not display the explicit border settings anymore.
		        		setBorderVisible(false);
		        		
		        		//apply zero-border
		                State.setBorderRightPercent(0);
		                State.setBorderLeftPercent(0);
		                State.setBorderTopPercent(0);
		                State.setBorderBottomPercent(0);
		        	} else {
		        		
		        		//(re-) display the border settings.
		        		setBorderVisible(true);
		        		
		        		//apply the settings that are enabled by the buttons that 
		        		// have just been set visible.
		        		applyMargeRight();
		        		applyMargeLeft();
		        		applyMargeTop();
		        		applyMargeBottom();
		        	}
		        }
		        getControlPicture().refreshPaint();
		        
		    }
	}
	
	
	private void setBorderVisible(boolean _visible) {


		getLook().getJcb_margeBottom().setVisible(_visible);
		getLook().getJcb_margeTop().setVisible(_visible);
		getLook().getJcb_margeLeft().setVisible(_visible);
		getLook().getJcb_margeRight().setVisible(_visible);

		getLook().getJlbl_subtitle_borderBottom().setVisible(_visible);
		getLook().getJlbl_subtitle_borderTop().setVisible(_visible);
		getLook().getJlbl_subtitle_borderLeft().setVisible(_visible);
		getLook().getJlbl_subtitle_borderRight().setVisible(_visible);
	
	}
	

	
	/**
	 * Set Background to none.
	 */
	public void setBackgroundNone() {

        int backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
        getLook().getJcb_nothing().setSelected(true);
        getLook().getJcb_lines().setSelected(false);
        getLook().getJcb_raster().setSelected(false);
        State.setIndexPageBackground(backgroundID);


	}
	

	/**
	 * Set page borders to none.
	 */
	public void setMargeNone() {

        State.setBorderRightPercent(0);
        State.setBorderLeftPercent(0);
        State.setBorderTopPercent(0);
        State.setBorderBottomPercent(0);
        
        getLook().getJcb_margeRight().setSelectedIndex(0);
        getLook().getJcb_margeLeft().setSelectedIndex(0);
        getLook().getJcb_margeTop().setSelectedIndex(0);
        getLook().getJcb_margeBottom().setSelectedIndex(0);
	}
	
	

	public void applyMargeRight() {

        String str_selected = getLook().getJcb_margeRight()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderRightPercent(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
	}
	
	
	public void applyMargeLeft() {

        
		
        String str_selected = getLook().getJcb_margeLeft()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderLeftPercent(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
    
	}
	
	
	public void applyMargeTop() {

		
        String str_selected = getLook().getJcb_margeTop()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderTopPercent(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
    
	}
	
	public void applyMargeBottom() {

        
        String str_selected = getLook().getJcb_margeBottom()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderBottomPercent(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
    
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
    	    		State.getLogger().severe("cp.getView().getPage() is null");
    	    	}
        	} else {
	    		State.getLogger().severe("cp.getView() is null");
	    	}
    	} else {
    		State.getLogger().severe("cp is null");
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
        				State.getLogger().severe("cp.getView().getTabs()"
        						+ ".getTab_look() is null");
        			}
    	    	} else {
    	    		State.getLogger().severe("cp.getView().getTabs() is null");
    	    	}
        	} else {
	    		State.getLogger().severe("cp.getView() is null");
	    	}
    	} else {
    		State.getLogger().severe("cp is null");
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
        		State.getLogger().severe("cp.getControlPic() is null");
    		}
    	} else {
    		State.getLogger().severe("cp is null");
    	}
    	return null;
    
    }
      
}
