//package declaration
package view.tabs;

//import declarations
import model.settings.Constants;
import model.settings.ViewSettings;
import control.tabs.CPrint;
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
	  */
	public Print(final CPrint _cp) {
		super(0);
		super.setOpaque(false);
		super.setLayout(null);
		

        //cut
        tb_print = new Item1Button(null);

        tb_print.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
        tb_print.setLocation(ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
        tb_print.setBorder(false);
        tb_print.addActionListener(_cp);
        Paint.initializeTextButtonOhneAdd(tb_print,
                "Druckauftrag",
                Constants.VIEW_TB_NEW_PATH);
        tb_print.setActivable(false);
        super.add(tb_print);

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
}
