package view.tabs;

//import declarations
import java.awt.Color;
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
    private Item1Button tb_pen_comment, tb_pen_theorem, tb_pen_proof, 
    tb_pen_example, tb_pen_headline1, tb_pen_headline2, tb_pen_headline3;
    
    
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
        tb_pen_theorem = new Item1Button(null);
        tb_pen_theorem.setOpaque(true);
        tb_pen_theorem.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_pen_theorem.setLocation(ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_pen_theorem.setText("Satz");
        tb_pen_theorem.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_pen_theorem.setActivable(true);
        tb_pen_theorem.addActionListener(CWrite.getInstance());
        tb_pen_theorem.setIcon("paint/test.png");
        super.add(tb_pen_theorem);

        tb_pen_proof = new Item1Button(null);
        tb_pen_proof.setOpaque(true);
        tb_pen_proof.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_pen_proof.setLocation(tb_pen_theorem.getWidth() 
                + tb_pen_theorem.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_pen_proof.setText("Beweis");
        tb_pen_proof.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_pen_proof.setActivable(true);
        tb_pen_proof.addActionListener(CWrite.getInstance());
        tb_pen_proof.setIcon("paint/test.png");
        super.add(tb_pen_proof);

        tb_pen_example = new Item1Button(null);
        tb_pen_example.setOpaque(true);
        tb_pen_example.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_pen_example.setLocation(tb_pen_proof.getWidth() 
                + tb_pen_proof.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_pen_example.setText("Beispiel");
        tb_pen_example.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_pen_example.setActivable(true);
        tb_pen_example.addActionListener(CWrite.getInstance());
        tb_pen_example.setIcon("paint/test.png");
        super.add(tb_pen_example);


        tb_pen_comment = new Item1Button(null);
        tb_pen_comment.setOpaque(true);
        tb_pen_comment.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_pen_comment.setLocation(tb_pen_example.getWidth() 
                + tb_pen_example.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_pen_comment.setText("Bemerkung");
        tb_pen_comment.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_pen_comment.setActivable(true);
        tb_pen_comment.setIcon("paint/test.png");
        tb_pen_comment.addActionListener(CWrite.getInstance());
        super.add(tb_pen_comment);

		

        insertSectionStuff("pen settings content", 0, 
                tb_pen_comment.getX() + tb_pen_comment.getWidth()
                + ViewSettings.getDistanceBeforeLine(), 0, true);

        tb_pen_headline1 = new Item1Button(null);
        tb_pen_headline1.setOpaque(true);
        tb_pen_headline1.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_pen_headline1.setLocation(tb_pen_comment.getWidth()
                + tb_pen_comment.getX() 
                + ViewSettings.getDistanceBeforeLine() * 2, 
                ViewSettings.getDistanceBetweenItems());
        tb_pen_headline1.setText("Headline 1");
        tb_pen_headline1.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_pen_headline1.setActivable(true);
        tb_pen_headline1.setIcon("paint/test.png");
        tb_pen_headline1.addActionListener(CWrite.getInstance());
        super.add(tb_pen_headline1);

        tb_pen_headline2 = new Item1Button(null);
        tb_pen_headline2.setOpaque(true);
        tb_pen_headline2.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_pen_headline2.setLocation(tb_pen_headline1.getWidth() 
                + tb_pen_headline1.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_pen_headline2.setText("Headline 2");
        tb_pen_headline2.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_pen_headline2.setActivable(true);
        tb_pen_headline2.setIcon("paint/test.png");
        tb_pen_headline2.addActionListener(CWrite.getInstance());
        super.add(tb_pen_headline2);
        
        tb_pen_headline3 = new Item1Button(null);
        tb_pen_headline3.setOpaque(true);
        tb_pen_headline3.setSize(ViewSettings.getItemMenu1Width(),
                ViewSettings.getItemMenu1Height());
        tb_pen_headline3.setLocation(tb_pen_headline2.getWidth() 
                + tb_pen_headline2.getX() 
                + ViewSettings.getDistanceBetweenItems(), 
                ViewSettings.getDistanceBetweenItems());
        tb_pen_headline3.setText("Headline 3");
        tb_pen_headline3.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_pen_headline3.setActivable(true);
        tb_pen_headline3.setIcon("paint/test.png");
        tb_pen_headline3.addActionListener(CWrite.getInstance());
        super.add(tb_pen_headline3);


        insertSectionStuff("pen settings headline", 
                tb_pen_comment.getX() + tb_pen_comment.getWidth()
                + ViewSettings.getDistanceBeforeLine(),
                tb_pen_headline3.getX() + tb_pen_headline3.getWidth() 
                + ViewSettings.getDistanceBeforeLine(), 0, true);

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
        return tb_pen_comment;
    }

    /**
     * @return the tb_satz
     */
    public Item1Button getTb_satz() {
        return tb_pen_theorem;
    }

    /**
     * @return the tb_beweis
     */
    public Item1Button getTb_beweis() {
        return tb_pen_proof;
    }

    /**
     * @return the tb_beispiel
     */
    public Item1Button getTb_beispiel() {
        return tb_pen_example;
    }

    /**
     * @return the tb_headline1
     */
    public Item1Button getTb_headline1() {
        return tb_pen_headline1;
    }

    /**
     * @return the tb_headline2
     */
    public Item1Button getTb_headline2() {
        return tb_pen_headline2;
    }

    /**
     * @return the tb_headline3
     */
    public Item1Button getTb_headline3() {
        return tb_pen_headline3;
    }
}
