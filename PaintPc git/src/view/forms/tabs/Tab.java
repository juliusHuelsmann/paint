package view.forms.tabs;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.settings.Status;
import model.settings.ViewSettings;
import view.util.VLabel;


/**
 * the abstract tab class.
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
@SuppressWarnings("serial")
public abstract class Tab extends JPanel {

    /**
     * JLabels for the separation, linked with information.
     */
    private JLabel [] jlbl_separation;

    /**
     * JLabels for the information text, linked with separation line.
     */
    private VLabel [] jlbl_information;
    
    
    /**
     * Constructor: initializes the separation and information stuff.
     * @param _amount the amount of different sections separated by separation
     * and linked to information.
     */
    public Tab(final int _amount) {
        
        //initialize the Labels
        jlbl_information = new VLabel[_amount];
        jlbl_separation = new JLabel[_amount];
    }
    
    
    
    
    /**
     * Insert both separation and information.
     * @param _text the printed text
     * @param _x1 first x coordinate (is between first and second coordinate)
     * @param _x2 second x coordinate (is between first and second coordinate)
     * @param _locationInArray the location in information array
     * @param _insert whether to insert the item or just to change bounds
     */
    protected final void insertSectionStuff(final String _text, 
            final int _x1, final int _x2, final int _locationInArray, 
            final boolean _insert) {
        
        insertSeparation(_x2, _locationInArray, _insert);
        insertInformation(_text, _x1, _x2, _locationInArray, _insert);
    }
    
    

    /**
     * 
     * @param _x the x coordinate in pX
     * @param _locationInArray the location in array
     * @param _insert whether to add or not
     */
    protected final void insertSeparation(final int _x, 
            final int _locationInArray, final boolean _insert) {
        
        //if new initialization is demanded
        if (_insert) {
            this.getJlbl_separation()[_locationInArray] = new JLabel();
            this.getJlbl_separation()[_locationInArray].setBorder(
                    BorderFactory.createLineBorder(
                            ViewSettings.GENERAL_CLR_BACKGROUND_DARK_XX));
            super.add(this.getJlbl_separation()[_locationInArray]);
            
        }
        this.getJlbl_separation()[_locationInArray].setBounds(
                _x, 
                ViewSettings.getDistanceBetweenItems(), 
                1, 
                ViewSettings.getView_heightTB_visible()
                - ViewSettings.getDistanceBetweenItems() 
                - ViewSettings.getView_heightTB()
                / ViewSettings.TABBED_PANE_TITLE_PROPORTION_HEIGHT);
    }
    
    /**
     * insert information text.
     * @param _text the printed text
     * @param _x1 first x coordinate (is between first and second coordinate)
     * @param _x2 second x coordinate (is between first and second coordinate)
     * @param _locationInArray the location in information array
     * @param _insert whether to insert the item or just to change bounds
     */
    protected final void insertInformation(final String _text, 
            final int _x1, final int _x2, final int _locationInArray, 
            final boolean _insert) {

        if (_insert) {

            //final value for foreground for JLabel
            final int rgb = 190;
            
            jlbl_information[_locationInArray] = new VLabel();
            jlbl_information[_locationInArray].setFont(
                    ViewSettings.GENERAL_TP_FONT_INFORMATION);
            jlbl_information[_locationInArray].setForeground(
                    new Color(rgb, rgb, rgb));
            jlbl_information[_locationInArray].setHorizontalAlignment(
                    SwingConstants.CENTER);
            jlbl_information[_locationInArray].setText(_text);
            super.add(jlbl_information[_locationInArray]);
            
        }

        if (Status.isNormalRotation()) {

            final int number = 
                    ViewSettings.getItemMenu1Height()
                    + ViewSettings.getDistanceBetweenItems();
            final int number2 = 15;
            jlbl_information[_locationInArray].setBounds(
                    _x1, number, _x2 - _x1, number2);
        } else {
            jlbl_information[_locationInArray].setOpaque(true);
            jlbl_information[_locationInArray].setBounds(-1, -1, -1, -1);
        }
    }


    /**
     * @return the jlbl_separation
     */
    public final JLabel [] getJlbl_separation() {
        return jlbl_separation;
    }


    /**
     * @return the jlbl_information
     */
    public final VLabel [] getJlbl_information() {
        return jlbl_information;
    }
}
