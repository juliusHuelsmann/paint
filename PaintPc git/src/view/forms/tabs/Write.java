package view.forms.tabs;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import control.tabs.CWrite;
import model.settings.ViewSettings;
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
    private Item1Button tb_bemerkung, tb_satz, tb_beweis, tb_beispiel, 
    tb_headline1, tb_headline2, tb_headline3;
    
    /**
     * The only instance of this class.
     */
    private static Write instance;


    
    /**
     * Empty utility class constructor.
     */
	private Write() { 
	    super(2);
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
        tb_satz.setText("Satz");
        tb_satz.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_satz.setActivable(true);
        tb_satz.addActionListener(CWrite.getInstance());
        tb_satz.setIcon("paint/test.png");
        super.add(tb_satz);

        tb_beweis = new Item1Button(null);
        tb_beweis.setOpaque(true);
        tb_beweis.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_beweis.setLocation(tb_satz.getWidth() + tb_satz.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_beweis.setText("Beweis");
        tb_beweis.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_beweis.setActivable(true);
        tb_beweis.addActionListener(CWrite.getInstance());
        tb_beweis.setIcon("paint/test.png");
        super.add(tb_beweis);

        tb_beispiel = new Item1Button(null);
        tb_beispiel.setOpaque(true);
        tb_beispiel.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_beispiel.setLocation(tb_beweis.getWidth() + tb_beweis.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_beispiel.setText("Beispiel");
        tb_beispiel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_beispiel.setActivable(true);
        tb_beispiel.addActionListener(CWrite.getInstance());
        tb_beispiel.setIcon("paint/test.png");
        super.add(tb_beispiel);


        tb_bemerkung = new Item1Button(null);
        tb_bemerkung.setOpaque(true);
        tb_bemerkung.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_bemerkung.setLocation(tb_beispiel.getWidth() + tb_beispiel.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_bemerkung.setText("Bemerkung");
        tb_bemerkung.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_bemerkung.setActivable(true);
        tb_bemerkung.setIcon("paint/test.png");
        tb_bemerkung.addActionListener(CWrite.getInstance());
        super.add(tb_bemerkung);

        
		
		
		

        insertSectionStuff("fertige Stifte", 0, 
                tb_bemerkung.getX() + tb_bemerkung.getWidth()
                + ViewSettings.getDistanceBeforeLine(), 0, true);

        tb_headline1 = new Item1Button(null);
        tb_headline1.setOpaque(true);
        tb_headline1.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_headline1.setLocation(tb_bemerkung.getWidth() + tb_bemerkung.getX() 
                + ViewSettings.getDistanceBeforeLine() * 2, 
                ViewSettings.getDistanceBetweenItems());
        tb_headline1.setText("Headline 1");
        tb_headline1.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_headline1.setActivable(false);
        tb_headline1.setIcon("paint/test.png");
        tb_headline1.addActionListener(CWrite.getInstance());
        super.add(tb_headline1);

        tb_headline2 = new Item1Button(null);
        tb_headline2.setOpaque(true);
        tb_headline2.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_headline2.setLocation(tb_headline1.getWidth() + tb_headline1.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_headline2.setText("Headline 2");
        tb_headline2.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_headline2.setActivable(false);
        tb_headline2.setIcon("paint/test.png");
        tb_headline2.addActionListener(CWrite.getInstance());
        super.add(tb_headline2);
        
        tb_headline3 = new Item1Button(null);
        tb_headline3.setOpaque(true);
        tb_headline3.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_headline3.setLocation(tb_headline2.getWidth() + tb_headline2.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_headline3.setText("Headline 3");
        tb_headline3.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_headline3.setActivable(false);
        tb_headline3.setIcon("paint/test.png");
        tb_headline3.addActionListener(CWrite.getInstance());
        super.add(tb_headline3);


        insertSectionStuff("Headlines", 
                tb_bemerkung.getX() + tb_bemerkung.getWidth()
                + ViewSettings.getDistanceBeforeLine(),
                tb_headline3.getX() + tb_headline3.getWidth() 
                + ViewSettings.getDistanceBeforeLine(), 0, true);

        
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

    /**
     * @return the tb_bemerkung
     */
    public Item1Button getTb_bemerkung() {
        return tb_bemerkung;
    }

    /**
     * @return the tb_satz
     */
    public Item1Button getTb_satz() {
        return tb_satz;
    }

    /**
     * @return the tb_beweis
     */
    public Item1Button getTb_beweis() {
        return tb_beweis;
    }

    /**
     * @return the tb_beispiel
     */
    public Item1Button getTb_beispiel() {
        return tb_beispiel;
    }

    /**
     * @return the tb_headline1
     */
    public Item1Button getTb_headline1() {
        return tb_headline1;
    }

    /**
     * @return the tb_headline2
     */
    public Item1Button getTb_headline2() {
        return tb_headline2;
    }

    /**
     * @return the tb_headline3
     */
    public Item1Button getTb_headline3() {
        return tb_headline3;
    }
}
