package control.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import model.settings.Constants;
import model.util.paint.Utils;
import view.View;
import view.forms.New;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CNew implements ActionListener, MouseListener {

    /**
     * The only instance of this class.
     */
    private static CNew instance;
    
    
    /**
     * {@inheritDoc}
     */
    @Override public void actionPerformed(final ActionEvent _event) {
        if (_event.getSource().equals(New.getInstance().getI1b_a4()
                .getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg1().setVisible(false);
        } else if (_event.getSource().equals(New.getInstance().getI1b_a5()
                .getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg2().setVisible(false);
        } else if (_event.getSource().equals(New.getInstance().getI1b_a6()
                .getActionCause())) {
            deactivate(); 
            New.getInstance().getJlbl_bg3().setVisible(false);
        } else if (_event.getSource().equals(New.getInstance().getI1b_a7()
                .getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg4().setVisible(false);
        } else if (_event.getSource().equals(
                New.getInstance().getI1b_custom().getActionCause())) {
            deactivate();
            New.getInstance().getJlbl_bg5().setVisible(false);
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


    @Override
    public void mouseClicked(final MouseEvent _event) {

        

        // source: exit button at the top of the window
        if (_event.getSource().equals(New.getInstance().getJbtn_exit())) {
            New.getInstance().setVisible(false);
        } 
    }


    @Override
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


    @Override
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


    @Override
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


    @Override
    public void mouseReleased(final MouseEvent _event) {
    }
}
