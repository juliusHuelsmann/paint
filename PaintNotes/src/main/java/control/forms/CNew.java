//package declaration
package control.forms;

//import declarations
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import model.objects.painting.Picture;
import model.settings.Constants;
import model.settings.Status;
import model.util.paint.Utils;
import view.View;
import view.forms.Message;
import view.forms.New;
import view.forms.Page;

/**
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CNew implements ActionListener, MouseListener, KeyListener {

    /**
     * The only instance of this class.
     */
    private static CNew instance;
    
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent _event) {
        if (_event.getSource().equals(New.getInstance().getI1b_a4()
                .getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg1().setVisible(false);
            New.getInstance().hideCustomInformation();
        } else if (_event.getSource().equals(New.getInstance().getI1b_a5()
                .getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg2().setVisible(false);
            New.getInstance().hideCustomInformation();
        } else if (_event.getSource().equals(New.getInstance().getI1b_a6()
                .getActionCause())) {
            deactivate(); 
            New.getInstance().getJlbl_bg3().setVisible(false);
            New.getInstance().hideCustomInformation();
        } else if (_event.getSource().equals(New.getInstance().getI1b_a7()
                .getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg4().setVisible(false);
            New.getInstance().hideCustomInformation();
        } else if (_event.getSource().equals(
                New.getInstance().getI1b_custom().getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg5().setVisible(false);
            New.getInstance().showCustomInformation();
        } else if (_event.getSource().equals(
                New.getInstance().getJbtn_enter())) {
            
            int width, height, backgroundID;
            String project = New.getInstance().getJcb_project()
                    .getSelectedItem().toString();
            
            //check whether settings are correct:
            if (New.getInstance().getI1b_custom().isActivated()) {
                

                int checkWidth = checkText(
                        New.getInstance().getJtf_customWidth().getText());
                int checkHeight = checkText(
                        New.getInstance().getJtf_customHeight().getText());
                if (checkHeight != -1 && checkWidth != -1) {
                    width = checkWidth;
                    height = checkHeight;
                } else {
                    
                    Message.showMessage(Message.MESSAGE_ID_ERROR, 
                            "Size of Image:\nDo only enter integer values"
                            + " greater than zero.");
                    return;
                }
            } else if (New.getInstance().getI1b_a4().isActivated()) {
                width = Constants.SIZE_A4.width;
                height = Constants.SIZE_A4.height;
            } else if (New.getInstance().getI1b_a5().isActivated()) {
                width = Constants.SIZE_A5.width;
                height = Constants.SIZE_A5.height;
            } else if (New.getInstance().getI1b_a6().isActivated()) {
                width = Constants.SIZE_A6.width;
                height = Constants.SIZE_A6.height;
            } else if (New.getInstance().getI1b_a7().isActivated()) {
                width = Constants.SIZE_A7.width;
                height = Constants.SIZE_A7.height;
            } else {
                Status.getLogger().warning("no page activated.");
                width = 2;
                height = 2;
            }
            
            if (New.getInstance().getJcb_raster().isSelected()) {
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_RASTAR;
            } else if (New.getInstance().getJcb_lines().isSelected()) {
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_LINES;
            } else if (New.getInstance().getJcb_nothing().isSelected()) {
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            } else {
                Status.getLogger().warning("no background selected.");
                backgroundID = Constants.CONTROL_PAGE_BACKGROUND_NONE;
            }

            Status.setImageShowSize(new Dimension(width, height));
            Status.setImageSize(new Dimension(width, height));
            Status.setIndexPageBackground(backgroundID);
            Picture.getInstance().reload();
            Status.setOpenProject(project);
            
            Page.getInstance().getJlbl_painting().refreshPaint();
        } else if (_event.getSource().equals(
                New.getInstance().getJcb_lines())) {
            New.getInstance().getJcb_lines().setSelected(true);
            New.getInstance().getJcb_nothing().setSelected(false);
            New.getInstance().getJcb_raster().setSelected(false);
        } else if (_event.getSource().equals(
                New.getInstance().getJcb_nothing())) {
            New.getInstance().getJcb_nothing().setSelected(true);
            New.getInstance().getJcb_lines().setSelected(false);
            New.getInstance().getJcb_raster().setSelected(false);
        } else if (_event.getSource().equals(
                New.getInstance().getJcb_raster())) {
            New.getInstance().getJcb_raster().setSelected(true);
            New.getInstance().getJcb_nothing().setSelected(false);
            New.getInstance().getJcb_lines().setSelected(false);
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

        New.getInstance().getI1b_a4().setActivated(false);
        New.getInstance().getI1b_a5().setActivated(false);
        New.getInstance().getI1b_a6().setActivated(false);
        New.getInstance().getI1b_a7().setActivated(false);
        New.getInstance().getI1b_custom().setActivated(false);

        New.getInstance().getJlbl_bg1().setVisible(true);
        New.getInstance().getJlbl_bg2().setVisible(true);
        New.getInstance().getJlbl_bg3().setVisible(true);
        New.getInstance().getJlbl_bg4().setVisible(true);
        New.getInstance().getJlbl_bg5().setVisible(true);
    }
    
    
    
    /**
     * return the only instance of singleton class.
     * @return The only instance of this class
     */
    public static CNew getInstance() {
        if (instance == null) {
            instance = new CNew();
        } 
        return instance;
    }



    /**
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent _event) {

        

        // source: exit button at the top of the window
        if (_event.getSource().equals(New.getInstance().getJbtn_exit())) {
            New.getInstance().setVisible(false);
        } 
    }



    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent _event) {

        // source: exit button at the top of the window
        if (_event.getSource().equals(New.getInstance().getJbtn_exit())) {
            New.getInstance().getJbtn_exit().setIcon(new ImageIcon(
                    Utils.resizeImage(View.getInstance().getJbtn_exit()
                            .getWidth(), New.getInstance().getJbtn_exit()
                            .getHeight(),
                            Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
        }
    }



    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent _event) {

        // source: exit button at the top of the window
        if (_event.getSource().equals(New.getInstance().getJbtn_exit())) {
            New.getInstance().getJbtn_exit().setIcon(new ImageIcon(
                    Utils.resizeImage(View.getInstance().getJbtn_exit()
                            .getWidth(), New.getInstance().getJbtn_exit()
                            .getHeight(),
                            Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
        }
    }



    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent _event) {


        // source: exit button at the top of the window
        if (_event.getSource().equals(New.getInstance().getJbtn_exit())) {
            New.getInstance().getJbtn_exit().setIcon(new ImageIcon(
                    Utils.resizeImage(View.getInstance().getJbtn_exit()
                            .getWidth(), New.getInstance().getJbtn_exit()
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
                New.getInstance().getJtf_customWidth().getText());
        int checkHeight = checkText(
                New.getInstance().getJtf_customHeight().getText());
        if (checkWidth != -1) {

            oldWidth = New.getInstance().getJtf_customWidth().getText();
        } 
        if (checkHeight != -1) {

            oldHeight = New.getInstance().getJtf_customHeight().getText();
        }
    }



    /**
     * {@inheritDoc}
     */
    public void keyReleased(final KeyEvent _event) {

        int checkWidth = checkText(
                New.getInstance().getJtf_customWidth().getText());
        int checkHeight = checkText(
                New.getInstance().getJtf_customHeight().getText());
        if (checkWidth == -1 
                && !New.getInstance().getJtf_customWidth()
                .getText().equals("")
                ) {
            New.getInstance().getJtf_customWidth().setText(oldWidth);
        } 
        if ((checkHeight == -1) 
                && 
                !New.getInstance().getJtf_customHeight()
                .getText().equals("")
                ) {
        
            New.getInstance().getJtf_customHeight().setText(oldHeight);
        }        
    }




    /**
     * {@inheritDoc}
     */
    public void keyTyped(final KeyEvent _event) {

    }
}
