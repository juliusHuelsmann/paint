//package declaration
package view.tabs;

//import declarations
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import model.settings.Constants;
import model.settings.ViewSettings;
import view.forms.Help;
import view.util.Item1Button;


/**
 * Paint tab.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Print extends Tab {
	
	/**
	 * 
	 */
	 private Item1Button tb_print;
	
	 
	 /**
	  * Constructor: initializes view.
	  * @param _cp the control paint.
	  */
	public Print(final ActionListener _cp) {
		super(0);
		super.setOpaque(false);
		super.setLayout(null);

		//initialize the first tab.
		initialize00(_cp, true);
	}
	
	
	
	@Override
	public void applySize() {
		super.applySize();
		initialize00(null, false);
	}
	
	/**
	 * Initialize function which can also be used for dynamic resize operation.
	 * 
	 * 
	 * @param _cp 		instance of the ActionListener - controller class
	 * 					which is only necessary at first initialization
	 * 					(if the second parameter (_print) is true)
	 * 
	 * @param _print	whether to insert the elements or not. Should be only
	 * 					true in first initialization.
	 */
	private void initialize00(
			final ActionListener _cp,
			final boolean _print) {

		
		if (_print) {

	        //cut
	        tb_print = new Item1Button(null);
		}

        tb_print.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
        tb_print.setLocation(ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
        
        if (_print) {

            tb_print.setBorder(false);
            tb_print.addActionListener(_cp);
        }
        initializeTextButtonOhneAdd(tb_print,
                "Druckauftrag",
                Constants.VIEW_TB_NEW_PATH);
        
        if (_print) {

            tb_print.setActivable(false);
            super.add(tb_print);
        }
	}
    
    /**
     * initialize a text button.
     * 
     * @param _tb the textButton
     * @param _text the text of the button
     * @param _path the path of the image
     */
    public static void initializeTextButtonOhneAdd(
            final Item1Button _tb, 
            final String _text, final String _path) {
        
        //alter settings of TextButton
        _tb.setOpaque(true);
        _tb.setText(_text);
        _tb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        _tb.setActivable(true);
        _tb.setIcon(_path);
    }
	

	/**
	 * @return the tb_new
	 */
	public Item1Button getTb_print() {
		return tb_print;
	}

	/**
	 * @param _tb_new the tb_new to set
	 */
	public void setTb_print(final Item1Button _tb_new) {
		this.tb_print = _tb_new;
	}




	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}
}
