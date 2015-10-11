//package declaration
package control.forms;
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

//import declarations
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import control.ControlPaint;
import model.settings.Constants;
import model.settings.State;
import model.util.paint.Utils;
import view.View;
import view.forms.Message;
import view.forms.New;

/**
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CNew implements ActionListener, MouseListener, KeyListener {

	
	/**
	 * Instance of the main controller.
	 */
	private ControlPaint cp;
	
	
	/**
	 * 
	 * @param _cp the main controller
	 */
	public CNew(final ControlPaint _cp) {
		this.cp = _cp;
	}
	

	/**
	 * 
	 * @return the new
	 */
	private New getNew() {
		return cp.getView().getPage().getJpnl_new();
	}
	
	/**
	 * 
	 * @return the view
	 */
	private View getView() {
		return cp.getView();
	}
	
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent _event) {
        if (_event.getSource().equals(getNew().getI1b_a4()
                .getActionCause())) {
            deactivate();
            getNew().getJlbl_bg1().setVisible(false);
            getNew().hideCustomInformation();
        } else if (_event.getSource().equals(getNew().getI1b_a5()
                .getActionCause())) {
            deactivate();
            getNew().getJlbl_bg2().setVisible(false);
            getNew().hideCustomInformation();
        } else if (_event.getSource().equals(getNew().getI1b_a6()
                .getActionCause())) {
            deactivate(); 
            getNew().getJlbl_bg3().setVisible(false);
            getNew().hideCustomInformation();
        } else if (_event.getSource().equals(getNew().getI1b_a7()
                .getActionCause())) {
            deactivate();
            getNew().getJlbl_bg4().setVisible(false);
            getNew().hideCustomInformation();
        } else if (_event.getSource().equals(
                getNew().getI1b_custom().getActionCause())) {
            deactivate();
            getNew().getJlbl_bg5().setVisible(false);
            getNew().showCustomInformation();
        } else if (_event.getSource().equals(
                getNew().getJbtn_enter())) {
            
            int width, height, backgroundID;
            String project = getNew().getJcb_project()
                    .getSelectedItem().toString();
            
            //check whether settings are correct:
            if (getNew().getI1b_custom().isActivated()) {
                

                int checkWidth = checkText(
                        getNew().getJtf_customWidth().getText());
                int checkHeight = checkText(
                        getNew().getJtf_customHeight().getText());
                if (checkHeight != -1 && checkWidth != -1) {
                    width = checkWidth;
                    height = checkHeight;
                } else {
                    
                    Message.showMessage(Message.MESSAGE_ID_ERROR, 
                            "Size of Image:\nDo only enter integer values"
                            + " greater than zero.");
                    return;
                }
            } else if (getNew().getI1b_a4().isActivated()) {
                width = Constants.SIZE_A4.width;
                height = Constants.SIZE_A4.height;
            } else if (getNew().getI1b_a5().isActivated()) {
                width = Constants.SIZE_A5.width;
                height = Constants.SIZE_A5.height;
            } else if (getNew().getI1b_a6().isActivated()) {
                width = Constants.SIZE_A6.width;
                height = Constants.SIZE_A6.height;
            } else if (getNew().getI1b_a7().isActivated()) {
                width = Constants.SIZE_A7.width;
                height = Constants.SIZE_A7.height;
            } else {
                State.getLogger().warning("no page activated.");
                width = 2;
                height = 2;
            }
            
            if (getNew().getJcb_raster().isSelected()) {
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
            } else if (getNew().getJcb_lines().isSelected()) {
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
            } else if (getNew().getJcb_nothing().isSelected()) {
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            } else {
                State.getLogger().warning("no background selected.");
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            }
            
            

            State.setIndexPageBackground(backgroundID);
            cp.getProject().initialize(new Dimension(width, height));
            State.setOpenProject(project);
            cp.getView().getPage().getJpnl_new().setVisible(false);
            
            cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(
                getNew().getJcb_lines())) {
            getNew().getJcb_lines().setSelected(true);
            getNew().getJcb_nothing().setSelected(false);
            getNew().getJcb_raster().setSelected(false);
        } else if (_event.getSource().equals(
                getNew().getJcb_nothing())) {
            getNew().getJcb_nothing().setSelected(true);
            getNew().getJcb_lines().setSelected(false);
            getNew().getJcb_raster().setSelected(false);
        } else if (_event.getSource().equals(
                getNew().getJcb_raster())) {
            getNew().getJcb_raster().setSelected(true);
            getNew().getJcb_nothing().setSelected(false);
            getNew().getJcb_lines().setSelected(false);
        }
    }
    
    
    
    
    /**
     * Check whether the text is a valid integer size greater than zero.
     * If that's the case return the integer, otherwise -1.
     * 
     * @param _text the text that is checked.
     * @return if the text is valid the integer it consists of, otherwise -1.
     */
    private int checkText(final String _text) {
        
        try {
            int returnValue =  Integer.parseInt(_text);
            if (returnValue <= 0) {
                return -1;
            } 
            return returnValue;
        } catch (Exception e) {
            return -1;
        }
    }
    
    
    /**
     * Deactivate all the items.
     */
    private void deactivate() {

        getNew().getI1b_a4().setActivated(false);
        getNew().getI1b_a5().setActivated(false);
        getNew().getI1b_a6().setActivated(false);
        getNew().getI1b_a7().setActivated(false);
        getNew().getI1b_custom().setActivated(false);

        getNew().getJlbl_bg1().setVisible(true);
        getNew().getJlbl_bg2().setVisible(true);
        getNew().getJlbl_bg3().setVisible(true);
        getNew().getJlbl_bg4().setVisible(true);
        getNew().getJlbl_bg5().setVisible(true);
    }
    
    
    



    /**
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent _event) {

        

        // source: exit button at the top of the window
        if (_event.getSource().equals(getNew().getJbtn_exit())) {
            getNew().setVisible(false);
        } 
    }



    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent _event) {

        // source: exit button at the top of the window
        if (_event.getSource().equals(getNew().getJbtn_exit())) {
            getNew().getJbtn_exit().setIcon(new ImageIcon(
                    Utils.resizeImage(getView().getJbtn_exit()
                            .getWidth(), getNew().getJbtn_exit()
                            .getHeight(),
                            Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
        }
    }



    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent _event) {

        // source: exit button at the top of the window
        if (_event.getSource().equals(getNew().getJbtn_exit())) {
            getNew().getJbtn_exit().setIcon(new ImageIcon(
                    Utils.resizeImage(getView().getJbtn_exit()
                            .getWidth(), getNew().getJbtn_exit()
                            .getHeight(),
                            Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
        }
    }



    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent _event) {


        // source: exit button at the top of the window
        if (_event.getSource().equals(getNew().getJbtn_exit())) {
            getNew().getJbtn_exit().setIcon(new ImageIcon(
                    Utils.resizeImage(getView().getJbtn_exit()
                            .getWidth(), getNew().getJbtn_exit()
                            .getHeight(),
                            Constants.VIEW_JBTN_EXIT_PRESSED_PATH)));
        }         
    }


    /**
     * {@inheritDoc}
     */
    public void mouseReleased(final MouseEvent _event) {
    }




    /**
     * Old Strings for resetting if the user did something wrong.
     */
    private String oldWidth, oldHeight;

    /**
     * {@inheritDoc}
     */
    public void keyPressed(final KeyEvent _event) {
        int checkWidth = checkText(
                getNew().getJtf_customWidth().getText());
        int checkHeight = checkText(
                getNew().getJtf_customHeight().getText());
        if (checkWidth != -1) {

            oldWidth = getNew().getJtf_customWidth().getText();
        } 
        if (checkHeight != -1) {

            oldHeight = getNew().getJtf_customHeight().getText();
        }
    }



    /**
     * {@inheritDoc}
     */
    public void keyReleased(final KeyEvent _event) {

        int checkWidth = checkText(
                getNew().getJtf_customWidth().getText());
        int checkHeight = checkText(
                getNew().getJtf_customHeight().getText());
        if (checkWidth == -1 
                && !getNew().getJtf_customWidth()
                .getText().equals("")
                ) {
            getNew().getJtf_customWidth().setText(oldWidth);
        } 
        if ((checkHeight == -1) 
                && 
                !getNew().getJtf_customHeight()
                .getText().equals("")
                ) {
        
            getNew().getJtf_customHeight().setText(oldHeight);
        }        
    }




    /**
     * {@inheritDoc}
     */
    public void keyTyped(final KeyEvent _event) {

    }
}
