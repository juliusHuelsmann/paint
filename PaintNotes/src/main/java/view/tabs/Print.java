//package declaration
package view.tabs;

//import declarations
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.objects.pen.normal.Marker;
import model.objects.pen.normal.Pencil;
import model.settings.Constants;
import model.settings.Status;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import control.ControlPainting;
import control.tabs.CPaintStatus;
import control.tabs.CPaintVisualEffects;
import control.tabs.CPaintSelection;
import view.util.Item1Menu;
import view.util.Item1PenSelection;
import view.util.VColorPanel;
import view.util.Item1Button;
import view.util.mega.MButton;


/**
 * Paint tab.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Print extends Tab {

	
	private static Print instance;
	
	/**
	 * 
	 */
	 private Item1Button tb_print ;
	
	private Print(int _amount) {
		super(_amount);
		super.setOpaque(false);
		super.setLayout(null);
		

        //cut
        tb_print = new Item1Button(null);

        tb_print.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
        tb_print.setLocation(ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
        tb_print.setBorder(false);
        tb_print.addMouseListener(ControlPainting.getInstance());
        Paint.initializeTextButtonOhneAdd(tb_print,
                "Druckauftrag",
                Constants.VIEW_TB_NEW_PATH);
        tb_print.setActivable(false);
        super.add(tb_print);

	}
	
	
	
	public static Print getInstance(int _amount) {
		if (instance == null) {
			
			if (_amount != -1) {

				instance = new Print(_amount);
			} else {
				Status.getLogger().severe("Error initializing");
			}
		}
		return instance;
	}

	/**
	 * @return the tb_new
	 */
	public Item1Button getTb_print() {
		return tb_print;
	}

	/**
	 * @param tb_new the tb_new to set
	 */
	public void setTb_print(Item1Button tb_new) {
		this.tb_print = tb_new;
	}

	
}
