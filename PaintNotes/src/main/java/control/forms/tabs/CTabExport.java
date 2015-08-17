package control.forms.tabs;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.ControlPaint;
import view.tabs.Export;
import model.settings.Constants;
import model.settings.State;



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
    	            State.setIndexPageBackgroundExport(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_lines())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
    	            getExport().getJcb_lines().setSelected(true);
    	            getExport().getJcb_nothing().setSelected(false);
    	            getExport().getJcb_raster().setSelected(false);
    	            State.setIndexPageBackgroundExport(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_nothing())) {
    	            backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
    	            getExport().getJcb_nothing().setSelected(true);
    	            getExport().getJcb_lines().setSelected(false);
    	            getExport().getJcb_raster().setSelected(false);
    	            State.setIndexPageBackgroundExport(backgroundID);
    	        
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeBottom())) {
    	            
    	            String str_selected = getExport().getJcb_margeBottom()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                State.setBorderBottomPercentExport(int_selected);
    	            } catch (Exception e) {
    	                State.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeLeft())) {
    	            

    	            String str_selected = getExport().getJcb_margeLeft()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                State.setBorderLeftPercentExport(int_selected);
    	            } catch (Exception e) {
    	                State.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeTop())) {

    	            String str_selected = getExport().getJcb_margeTop()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                State.setBorderTopPercentExport(int_selected);
    	            } catch (Exception e) {
    	                State.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(
    	                getExport().getJcb_margeRight())) {

    	            String str_selected = getExport().getJcb_margeRight()
    	                    .getSelectedItem().toString().replace("%", "");
    	            
    	            try {

    	                int int_selected = Integer.parseInt(str_selected);
    	                State.setBorderRightPercentExport(int_selected);
    	            } catch (Exception e) {
    	                State.getLogger().severe(
    	                        "error: change border size: wrong input");
    	            }
    	        } else if (_event.getSource().equals(getExport()
    	                .getJcb_displayAlpha())) {
    	            State.setExportAlpha(getExport().getJcb_displayAlpha()
    	                    .getSelectedItem().equals(
    	                    		Constants.ID_DISPLAY_ALPHA));
    	        } else if (_event.getSource().equals(getExport()
    	                .getJcb_saveFormats())) {
    	            State.setSaveFormat(getExport().getJcb_saveFormats()
    	                    .getSelectedItem().toString());
    	        } else if (_event.getSource().equals(getExport()
		        		.getJcb_borderEnabled())) {
    	        	
		        	if (!getExport().getJcb_borderEnabled().isSelected()) {
		        		
		        		//do not display the explicit border settings anymore.
		        		setBorderVisible(false);
		        		
		        		//apply zero-border
		                State.setBorderRightPercentExport(0);
		                State.setBorderLeftPercentExport(0);
		                State.setBorderTopPercentExport(0);
		                State.setBorderBottomPercentExport(0);
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
    	        
    	    }
    	}


    
    
    
    
    
    


	private void setBorderVisible(boolean _visible) {
	
	
		getExport().getJcb_margeBottom().setVisible(_visible);
		getExport().getJcb_margeTop().setVisible(_visible);
		getExport().getJcb_margeLeft().setVisible(_visible);
		getExport().getJcb_margeRight().setVisible(_visible);
	
		getExport().getJlbl_subtitle_borderBottom().setVisible(_visible);
		getExport().getJlbl_subtitle_borderTop().setVisible(_visible);
		getExport().getJlbl_subtitle_borderLeft().setVisible(_visible);
		getExport().getJlbl_subtitle_borderRight().setVisible(_visible);
	
	}

	public void applyMargeRight() {

        String str_selected = getExport().getJcb_margeRight()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderRightPercentExport(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
	}
	
	
	public void applyMargeLeft() {

        
		
        String str_selected = getExport().getJcb_margeLeft()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderLeftPercentExport(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
    
	}
	
	
	public void applyMargeTop() {

		
        String str_selected = getExport().getJcb_margeTop()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderTopPercentExport(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
    
	}
	
	public void applyMargeBottom() {

        
        String str_selected = getExport().getJcb_margeBottom()
                .getSelectedItem().toString().replace("%", "");
        
        try {

            int int_selected = Integer.parseInt(str_selected);
            State.setBorderBottomPercentExport(int_selected);
        } catch (Exception e) {
            State.getLogger().severe(
                    "error: change border size: wrong input");
        }
    
	}

    
    
    
    
    /*
     * getter
     */


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
						State.getLogger().severe("cp.getView().getTabs()"
								+ ".getTab_export() is null");
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
       

}
