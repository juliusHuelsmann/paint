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
    private Item2Menu ia_geo, ia_maths, ia_diagram;
    
    /**
     * ia_geo Item2 menus.
     */
    private Item2 i2_g_line, i2_g_rect, i2_g_rect_round, 
    i2_g_triangle, i2_g_arch, i2_g_curve,
    i2_g_round, i2_g_archFilled, i2_g_rectFilled, i2_g_triangleFilled, 
    i2_g_roundFilled, i2_g_rect_roundFilled, i2_d_diagramm;
    
    
    /**
     * The item1button which shows the currently selected form.
     */
    private Item1Button tb_selected;
    
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
		final int itemButtonSize = 128;
		final int locationX = 285;

		tb_selected = new Item1Button(null);
		tb_selected.setOpaque(true);
		tb_selected.setSize(itemButtonSize, itemButtonSize);
		tb_selected.setLocation(distance, distance);
		tb_selected.setText("Viereck");
		tb_selected.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(Color.black),
				new LineBorder(Color.white)));
		tb_selected.setActivable(false);
		tb_selected.setIcon("paint/test.png");
		super.add(tb_selected);
		
		
		JLabel jlbl_trennung = insertTrennung(tb_selected.getX() 
		        + tb_selected.getWidth(), tb_selected.getY());
        insertInformation("ausgewaehlt", 
                tb_selected.getX(), jlbl_trennung.getX());
        //

        ia_geo = new Item2Menu();
        ia_geo.setLocation(jlbl_trennung.getX() , tb_selected.getY());
        ia_geo.setSize(locationX, itemButtonSize * 2);
        ia_geo.setItemsInRow(2 + 2);
        super.add(ia_geo);

        i2_g_line = new Item2();
        i2_g_line.addMouseListener(CPaintStatus.getInstance());
        i2_g_line.setTitle("line");
        ia_geo.add(i2_g_line);
        i2_g_line.setIcon("icon/geoForm/line.png");

        i2_g_curve = new Item2();
        i2_g_curve.addMouseListener(CPaintStatus.getInstance());
        i2_g_curve.setTitle("curve");
        ia_geo.add(i2_g_curve);
        i2_g_curve.setIcon("icon/geoForm/curve.png");

        i2_g_arch = new Item2();
        i2_g_arch.addMouseListener(CPaintStatus.getInstance());
        i2_g_arch.setTitle("arch");
        ia_geo.add(i2_g_arch);
        i2_g_arch.setIcon("icon/geoForm/arch.png");

        i2_g_archFilled = new Item2();
        i2_g_archFilled.addMouseListener(CPaintStatus.getInstance());
        i2_g_archFilled.setTitle("arch filled");
        ia_geo.add(i2_g_archFilled);
        i2_g_archFilled.setIcon("icon/geoForm/pfeilopen.png");

        i2_g_round = new Item2();
        i2_g_round.addMouseListener(CPaintStatus.getInstance());
        i2_g_round.setTitle("round");
        ia_geo.add(i2_g_round);
        i2_g_round.setIcon("icon/geoForm/circle.png");

        i2_g_rect = new Item2();
        i2_g_rect.addMouseListener(CPaintStatus.getInstance());
        i2_g_rect.setTitle("rect");
        ia_geo.add(i2_g_rect);
        i2_g_rect.setIcon("icon/geoForm/rectangle.png");

        i2_g_rect_round = new Item2();
        i2_g_rect_round.addMouseListener(CPaintStatus.getInstance());
        i2_g_rect.setTitle("rect round");
        ia_geo.add(i2_g_rect_round);
        i2_g_rect_round.setIcon("icon/geoForm/rectangleRound.png");
        
        i2_g_triangle = new Item2();
        i2_g_triangle.addMouseListener(CPaintStatus.getInstance());
        i2_g_triangle.setTitle("triangle");
        ia_geo.add(i2_g_triangle);
        i2_g_triangle.setIcon("icon/geoForm/triangle.png");

        i2_g_roundFilled = new Item2();
        i2_g_roundFilled.addMouseListener(CPaintStatus.getInstance());
        i2_g_roundFilled.setTitle("round filled");
        ia_geo.add(i2_g_roundFilled);
        i2_g_roundFilled.setIcon("icon/geoForm/circleFilled.png");

        i2_g_rectFilled = new Item2();
        i2_g_rectFilled.addMouseListener(CPaintStatus.getInstance());
        i2_g_rectFilled.setTitle("rectangle filled");
        ia_geo.add(i2_g_rectFilled);
        i2_g_rectFilled.setIcon("icon/geoForm/rectangleFilled.png");

        i2_g_rect_roundFilled = new Item2();
        i2_g_rect_roundFilled.addMouseListener(CPaintStatus.getInstance());
        i2_g_rect_roundFilled.setTitle("rectangle round filled");
        ia_geo.add(i2_g_rect_roundFilled);
        i2_g_rect_roundFilled.setIcon("icon/geoForm/rectangleRoundFilled.png");
        
        i2_g_triangleFilled = new Item2();
        i2_g_triangleFilled.addMouseListener(CPaintStatus.getInstance());
        i2_g_triangleFilled.setTitle("triangle filled");
        ia_geo.add(i2_g_triangleFilled);
        i2_g_triangleFilled.setIcon("icon/geoForm/triangleFilled.png");

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
		

        ia_diagram = new Item2Menu();
     	ia_diagram.setSize(ia_geo.getWidth(), ia_geo.getHeight() * 2);
    	ia_diagram.setLocation(jlbl_trennung.getX() + distance, distance);
		super.add(ia_diagram);

        i2_d_diagramm = new Item2();
        i2_d_diagramm.addMouseListener(CPaintStatus.getInstance());
        i2_d_diagramm.setTitle("line");
        ia_diagram.add(i2_d_diagramm);
        i2_d_diagramm.setIcon("icon/geoForm/line.png");

		
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

    /**
     * @return the i2_g_archFilled
     */
    public Item2 getI2_g_archFilled() {
        return i2_g_archFilled;
    }

    /**
     * @return the tb_selected
     */
    public Item1Button getTb_selected() {
        return tb_selected;
    }

    /**
     * @return the i2_d_diagramm
     */
    public Item2 getI2_d_diagramm() {
        return i2_d_diagramm;
    }
}
