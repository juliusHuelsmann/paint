package view.forms.tabs;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
public final class Insert extends JPanel {

    /**
     * The item2menus.
     */
    private Item2Menu ia_geo, ia_maths, it_pfeileUAE, ia_diagram;
    
    /**
     * ia_geo Item2 menus.
     */
    private Item2 i2_g_line, i2_g_rect, i2_g_triangle, i2_g_arch, i2_g_curve;
    
    
    /**
     * The only instance of this class.
     */
    private static Insert instance;
    
    /**
     * Empty utility class constructor.
     */
	private Insert() { }
	
	/**
	 * initializes Panel.
     * @param _height the height of the panel.
	 */
	private void initialize(final int _height) {

		//initialize JPanel and alter settings
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

        i2_g_line = new Item2();
        i2_g_line.addMouseListener(CPaintStatus.getInstance());
        ia_geo.add(i2_g_line);
        i2_g_line.setIcon("icon/geoForm/line.png");

        i2_g_rect = new Item2();
        i2_g_rect.addMouseListener(CPaintStatus.getInstance());
        ia_geo.add(i2_g_rect);
        i2_g_rect.setIcon("icon/geoForm/rectangle.png");

        i2_g_triangle = new Item2();
        i2_g_triangle.addMouseListener(CPaintStatus.getInstance());
        ia_geo.add(i2_g_triangle);
        i2_g_triangle.setIcon("icon/geoForm/triangle.png");

        i2_g_arch = new Item2();
        i2_g_arch.addMouseListener(CPaintStatus.getInstance());
        ia_geo.add(i2_g_arch);
        i2_g_arch.setIcon("icon/geoForm/arch.png");

        i2_g_curve = new Item2();
        i2_g_curve.addMouseListener(CPaintStatus.getInstance());
        ia_geo.add(i2_g_curve);
        i2_g_curve.setIcon("icon/geoForm/curve.png");
        
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
	 * Return the only instance of this class.
	 * @return the only instance of this singleton class
	 */
	public static Insert getInstance() {
	    
	    if (instance == null) {
	        instance = new Insert();
	        instance.initialize(ViewSettings.VIEW_HEIGHT_TB);
	    }
	    
	    return instance;
	}

    /**
     * @return the ia_maths
     */
    public Item2Menu getIa_maths() {
        return ia_maths;
    }

    /**
     * @return the ia_geo
     */
    public Item2Menu getIa_geo() {
        return ia_geo;
    }

    /**
     * @return the ia_diagram
     */
    public Item2Menu getIa_diagram() {
        return ia_diagram;
    }

    /**
     * @return the it_pfeileUAE
     */
    public Item2Menu getIt_pfeileUAE() {
        return it_pfeileUAE;
    }

    /**
     * @return the i2_g_line
     */
    public Item2 getI2_g_line() {
        return i2_g_line;
    }

    /**
     * @return the i2_g_rect
     */
    public Item2 getI2_g_rect() {
        return i2_g_rect;
    }

    /**
     * @return the i2_g_triangle
     */
    public Item2 getI2_g_triangle() {
        return i2_g_triangle;
    }

    /**
     * @return the i2_g_arch
     */
    public Item2 getI2_g_arch() {
        return i2_g_arch;
    }

    /**
     * @return the i2_g_curve
     */
    public Item2 getI2_g_curve() {
        return i2_g_curve;
    }
	
}
