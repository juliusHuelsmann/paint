package view.forms.tabs;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

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
public class Insert extends JPanel {

    /**
     * The item2menus.
     */
    private Item2Menu ia_geo, ia_maths, it_pfeileUAE, ia_diagram;
    
    /**
     * Constructor initializes Panel.
     * @param _height the height of the panel.
     */
	public Insert(final int _height) {

		//initialize JPanel and alter settings
		super();
		super.setOpaque(false);
		super.setLayout(null);
		
		final int distance = 5;
		final int amountOfItemsAdded = 20;
		final int itemButtonSize = 135;
		final int locationX = 285;

		Item1Button tb = new Item1Button(null);
		tb.setOpaque(true);
		tb.setSize(itemButtonSize, itemButtonSize);
		tb.setLocation(distance, distance);
		tb.setText("Viereck");
		tb.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(Color.black),
				new LineBorder(Color.white)));
		tb.setActivable(false);
		tb.setIcon("paint/test.png");
		super.add(tb);
		
		
		JLabel jlbl_trennung = insertTrennung(
		        tb.getX() + tb.getWidth(), tb.getY());
        insertInformation("ausgewaehlt", tb.getX(), jlbl_trennung.getX());
        //

        ia_geo = new Item2Menu();
        ia_geo.setLocation(jlbl_trennung.getX() , tb.getY());
        ia_geo.setSize(locationX, itemButtonSize * 2);
		super.add(ia_geo);
		
		
		jlbl_trennung = insertTrennung(ia_geo.getX() 
		        + ia_geo.getWidth(), ia_geo.getY());
        insertInformation("geometrische Formen", 
                ia_geo.getX(), jlbl_trennung.getX());
        //

        ia_maths = new Item2Menu();
        ia_maths.setLocation(jlbl_trennung.getX() + distance, distance);
        ia_maths.setSize(ia_geo.getWidth(), ia_geo.getHeight() * 2);
		super.add(ia_maths);
		
		jlbl_trennung = insertTrennung(ia_maths.getX() 
		        + ia_maths.getWidth(), ia_maths.getY());
        insertInformation("mathematische Formen",
                ia_maths.getX(), jlbl_trennung.getX());
        
        it_pfeileUAE = new Item2Menu();
     	it_pfeileUAE.setSize(ia_geo.getWidth(), ia_geo.getHeight() * 2);
    	it_pfeileUAE.setLocation(jlbl_trennung.getX() + distance, distance);
		super.add(it_pfeileUAE);
		//20 items
		for (int i = 0; i < amountOfItemsAdded; i++) {
		    it_pfeileUAE.add(new Item2());
		}
		
		jlbl_trennung = insertTrennung(it_pfeileUAE.getX() 
		        + it_pfeileUAE.getWidth(), it_pfeileUAE.getY());
        insertInformation("Pfeile u.a", it_pfeileUAE.getX(), 
                jlbl_trennung.getX());

        ia_diagram = new Item2Menu();
     	ia_diagram.setSize(ia_geo.getWidth(), ia_geo.getHeight() * 2);
    	ia_diagram.setLocation(jlbl_trennung.getX() + distance, distance);
		super.add(ia_diagram);
		
		jlbl_trennung = insertTrennung(ia_diagram.getX() 
		        + ia_diagram.getWidth(), ia_diagram.getY());
        insertInformation("Diagramme", ia_diagram.getX(),
                jlbl_trennung.getX());
        super.setSize((int) Toolkit.getDefaultToolkit()
                .getScreenSize().getWidth(), _height);
	}

	/**
	 * insert a separation line.
	 * @param _x the x start location
	 * @param _y the y start location.
	 * @return the JLabel
	 */
	private JLabel insertTrennung(final int _x, final int _y) {

	    final int number = 145;
	    
		JLabel jlbl_trennung = new JLabel();
		jlbl_trennung.setBorder(BorderFactory.createLineBorder(
		       ViewSettings.CLR_BACKGROUND_DARK_X));
		jlbl_trennung.setBounds(_x, _y, 1, number);
		super.add(jlbl_trennung);
		
		return jlbl_trennung;
	}
	
	
	/**
	 * insert information text as title for an area.
	 * @param _text the information text
	 * @param _x the lower x coordinate 
	 * @param _upper the upper x coordinate
	 */
	private void insertInformation(final String _text, final int _x, 
	        final int _upper) {

		final int rgb = 190;
		final int number = 135;
		final int number2 = 20;
		
		JLabel jlbl_informationColor = new JLabel(_text);
		jlbl_informationColor.setFont(ViewSettings.GENERAL_FONT_ITEM);
		jlbl_informationColor.setBounds(_x , number, _upper - _x, number2);
		jlbl_informationColor.setForeground(new Color(rgb, rgb, rgb));
		jlbl_informationColor.setHorizontalAlignment(SwingConstants.CENTER);
		super.add(jlbl_informationColor);

	}

    /**
     * @return the ia_maths
     */
    public final Item2Menu getIa_maths() {
        return ia_maths;
    }

    /**
     * @return the ia_geo
     */
    public final Item2Menu getIa_geo() {
        return ia_geo;
    }

    /**
     * @return the ia_diagram
     */
    public final Item2Menu getIa_diagram() {
        return ia_diagram;
    }

    /**
     * @return the it_pfeileUAE
     */
    public final Item2Menu getIt_pfeileUAE() {
        return it_pfeileUAE;
    }
	
}
