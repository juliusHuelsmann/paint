package view.forms.tabs;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import control.tabs.CPaintStatus;
import model.settings.ViewSettings;
import view.util.Item2;
import view.util.Item2Menu;
import view.util.Item1Button;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Write extends Tab {

    /**
     * The item2menus.
     */
    private Item1Button tb_bemerkung, tb_satz, tb_beweis, tb_beispiel;
    
    /**
     * The only instance of this class.
     */
    private static Write instance;


    
    /**
     * Empty utility class constructor.
     */
	private Write() { 
	    super(1);
	}
	
	/**
	 * initializes Panel.
     * @param _height the height of the panel.
	 */
	private void initialize(final int _height) {

		//initialize JPanel and alter settings
		super.setOpaque(false);
		super.setLayout(null);

		
        /*
         * 
         * 
         * 
         * 
         * 
         */
		

        tb_satz = new Item1Button(null);
        tb_satz.setOpaque(true);
        tb_satz.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_satz.setLocation(ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_satz.setText("Viereck");
        tb_satz.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_satz.setActivable(false);
        tb_satz.setIcon("paint/test.png");
        super.add(tb_satz);

        tb_beweis = new Item1Button(null);
        tb_beweis.setOpaque(true);
        tb_beweis.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_beweis.setLocation(ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_beweis.setText("Viereck");
        tb_beweis.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_beweis.setActivable(false);
        tb_beweis.setIcon("paint/test.png");
        super.add(tb_beweis);

        tb_beispiel = new Item1Button(null);
        tb_beispiel.setOpaque(true);
        tb_beispiel.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_beispiel.setLocation(ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_beispiel.setText("Viereck");
        tb_beispiel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_beispiel.setActivable(false);
        tb_beispiel.setIcon("paint/test.png");
        super.add(tb_beispiel);

		
		tb_bemerkung = new Item1Button(null);
		tb_bemerkung.setOpaque(true);
		tb_bemerkung.setSize(ViewSettings.getItemMenu1Width(),
		        ViewSettings.getItemMenu1Height());
		tb_bemerkung.setLocation(ViewSettings.getDistanceBetweenItems(), 
		        ViewSettings.getDistanceBetweenItems());
		tb_bemerkung.setText("Viereck");
		tb_bemerkung.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(Color.black),
				new LineBorder(Color.white)));
		tb_bemerkung.setActivable(false);
		tb_bemerkung.setIcon("paint/test.png");
		super.add(tb_bemerkung);
        
		
		
		

        insertSectionStuff("fertige Stifte", tb_bemerkung.getX(), 
                tb_bemerkung.getX() + tb_bemerkung.getWidth(), 0, true);


        
        super.setSize((int) Toolkit.getDefaultToolkit()
                .getScreenSize().getWidth(), _height);
	}

	
	
	
	
	/**
	 * Return the only instance of this class.
	 * @return the only instance of this singleton class
	 */
	public static Write getInstance() {
	    
	    if (instance == null) {
	        instance = new Write();
	        instance.initialize(ViewSettings.getView_heightTB());
	    }
	    
	    return instance;
	}
}
