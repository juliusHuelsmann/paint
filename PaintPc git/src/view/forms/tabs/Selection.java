//package declaration
package view.forms.tabs;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import control.singleton.CChangeSelection;
import control.singleton.CStatus;
import control.singleton.CVisualEffects;
import settings.Status;
import settings.ViewSettings;
import view.util.Item1Menu;
import view.util.Item1Button;
import view.util.VColorPanel;
import view.util.VLabel;

/**
 * The Selection Tab.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Selection extends JPanel {

    /**
     * JLabels for the separation, linked with information.
     */
    private JLabel [] jlbl_separation, jlbl_information;

    /**
     * array of colors to change the first or the second color.
     */
    private JButton [] jbtn_colors;

    /**
     * Buttons for the second and the first color.
     */
    private Item1Button tb_color;
    
    /**
     * Color fetcher.
     */
    private Item1Menu it_color;

    /**
     * integer values.
     */
    private final int distance = 5, heightLabel = 20, htf = 135, 
            twoHundred = 200;

    /**
     * The JCheckBox for changing the pen.
     */
    private JCheckBox jcb_points, jcb_line, jcb_maths;

    /**
     * The only instance of this class.
     */
    private static Selection instance;
    
    /**
     * Empty Utility class Constructor.
     */
	private Selection() {
	    
	}
	
	
	/**
	 * real constructor.
	 * @param _height the height
	 */
	private void init(final int _height) {
        
        //initialize JPanel and alter settings
        super.setOpaque(false);
        super.setLayout(null);


        //initialize items 
        final int amountOfSeparations = 2;
        jlbl_separation = new JLabel[amountOfSeparations];
        jlbl_information = new VLabel[amountOfSeparations];
        

        int x = initCololrs(distance, true);
        x = initPen(x, true);
        initOthers(x, true);
        
        super.setSize(
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                _height);
    
	}
	
	
	/**
	 * 
	 * @param _x the x coordinate
	 * @param _paint the boolean.
	 * @return the new x coordinate
	 */
	private int initCololrs(final int _x, final boolean _paint) {

        //the first color for the first pen
        tb_color = new Item1Button(null);
        tb_color.setOpaque(true);
        tb_color.addMouseListener(CStatus.getInstance());
        tb_color.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black), new LineBorder(Color.white)));
        tb_color.setLocation(_x, ViewSettings.DISTANCE_BETWEEN_ITEMS);
        tb_color.setSize(
                ViewSettings.ITEM_MENU1_WIDTH, ViewSettings.ITEM_MENU1_HEIGHT);
        tb_color.setText("Farbe 1");
        super.add(tb_color);
    
    
        final int distanceBetweenColors = 2;
        int width = (2 + 2 + 1) * (2 + 2 + 1) - 2 - 2;
        int height = width + 2 + 1 + 2 * (2 + 1);
        int anzInR = 2 + 2 + 2 + 1;
        jbtn_colors = new JButton[anzInR * (2 + 2)];
        for (int i = 0; i < jbtn_colors.length; i++) {
            jbtn_colors[i] = new JButton();
            jbtn_colors[i].setBounds(tb_color.getX() + tb_color.getWidth() 
                    + distanceBetweenColors + (i % anzInR) 
                    * (width + distanceBetweenColors), 
                    distanceBetweenColors + (i / anzInR)
                    * (height + distanceBetweenColors), width, height);
            jbtn_colors[i].setOpaque(true);
            jbtn_colors[i].addMouseListener(
                    CStatus.getInstance());
            jbtn_colors[i].addMouseListener(CVisualEffects.getInstance());
            jbtn_colors[i].setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.black), new LineBorder(Color.white)));
            super.add(jbtn_colors[i]);
        }
        int i = 0;
        final int three = 3;
        final Color 
                c1n0 = Color.black,
                c1n1 = new Color(80, 80, 80),
                c1n2 = new Color(160, 160, 160),
                c1n3 = Color.white,
                
                c2n0 = new Color(23, 32, 164),
                c2n1 = new Color(63, 72, 204), 
                c2n2 = new Color(103, 112, 244),
                c2n3 = new Color(153, 162, 255), 

                c3n0 = new Color(180, 10, 10),
                c3n1 = new Color(200, 20, 20), 
                c3n2 = new Color(250, 75, 75),
                c3n3 = new Color(255, 100, 100), 

                c4n0 = new Color(24, 157, 45),
                c4n1 = new Color(34, 177, 67), 
                c4n2 = new Color(64, 197, 97),
                c4n3 = new Color(104, 255, 147), 
                        
                c5n0 = new Color(235, 107, 73),
                c5n1 = new Color(255, 127, 93), 
                c5n2 = new Color(255, 147, 113),
                c5n3 = new Color(255, 187, 153), 

                c6n0 = new Color(133, 33, 134),
                c6n1 = new Color(163, 73, 164), 
                c6n2 = new Color(193, 103, 194),
                c6n3 = new Color(255, 153, 254),
                        
                c7n0 = new Color(112, 146, 190),
                c7n1 = new Color(200, 191, 231), 
                c7n2 = new Color(255, 201, 14),
                c7n3 = new Color(120, 74, 50);
    
        //schwarz bis grau
        jbtn_colors[i + anzInR * 0].setBackground(c1n0);
        jbtn_colors[i + anzInR * 1].setBackground(c1n1);
        jbtn_colors[i + anzInR * 2].setBackground(c1n2);
        jbtn_colors[i + anzInR * three].setBackground(c1n3);
        //blue
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c2n0);
        jbtn_colors[i + anzInR * 1].setBackground(c2n1);
        jbtn_colors[i + anzInR * 2].setBackground(c2n2);
        jbtn_colors[i + anzInR * three].setBackground(c2n3);
    
        //red
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c3n0);
        jbtn_colors[i + anzInR * 1].setBackground(c3n1);
        jbtn_colors[i + anzInR * 2].setBackground(c3n2);
        jbtn_colors[i + anzInR * three].setBackground(c3n3);
    
        //green
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c4n0);
        jbtn_colors[i + anzInR * 1].setBackground(c4n1);
        jbtn_colors[i + anzInR * 2].setBackground(c4n2);
        jbtn_colors[i + anzInR * three].setBackground(c4n3);
    
        //orange
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c5n0);
        jbtn_colors[i + anzInR * 1].setBackground(c5n1);
        jbtn_colors[i + anzInR * 2].setBackground(c5n2);
        jbtn_colors[i + anzInR * three].setBackground(c5n3);
    
        //pink
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c6n0);
        jbtn_colors[i + anzInR * 1].setBackground(c6n1);
        jbtn_colors[i + anzInR * 2].setBackground(c6n2);
        jbtn_colors[i + anzInR * three].setBackground(c6n3);
    
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c7n0);
        jbtn_colors[i + anzInR * 1].setBackground(c7n1);
        jbtn_colors[i + anzInR * 2].setBackground(c7n2);
        jbtn_colors[i + anzInR * three].setBackground(c7n3);
    
        //
        it_color = new Item1Menu();
        it_color.setSize(ViewSettings.PAINT_SIZE);
        it_color.setBorder(false);
        it_color.setText("+ Farben");
        it_color.setLocation(jbtn_colors[jbtn_colors.length - 1].getX() 
                + ViewSettings.DISTANCE_BETWEEN_ITEMS 
                + jbtn_colors[jbtn_colors.length - 1].getWidth(), 
                ViewSettings.DISTANCE_BETWEEN_ITEMS);
        it_color.getJPanel().add(new VColorPanel(jbtn_colors));
        it_color.setBorder(false);
        it_color.setIcon("icon/palette.png");
        super.add(it_color);
        
        insertTrennung(it_color.getWidth() + it_color.getX() + ViewSettings
                .DISTANCE_BEFORE_LINE, it_color.getY(), 0, _paint);
        insertInformation("Farben", _x, jlbl_separation[0].getX(), 0, 
                _paint);

        
        return jlbl_separation[0].getX();
	}
	
	
	/**
	 * initialize the pen.
	 * @param _x the x coordinate
	 * @param _paint whether to paint the items
	 * @return the new x coordinate
	 */
	private int initPen(final int _x, final boolean _paint) {
	    
        jcb_points = new JCheckBox("points");
        jcb_points.setSelected(true);
        jcb_points.setOpaque(false);
        jcb_points.setBounds(_x, distance, twoHundred, heightLabel);
        jcb_points.setVerticalAlignment(SwingConstants.TOP);
        jcb_points.setFocusable(false);
        jcb_points.addActionListener(CChangeSelection.getInstance());
        super.add(jcb_points);
    
        jcb_line = new JCheckBox("line");
        jcb_line.setVerticalAlignment(SwingConstants.TOP);
        jcb_line.setFocusable(false);
        jcb_line.setOpaque(false);
        jcb_line.setBounds(_x, jcb_points.getHeight() + jcb_points.getY() 
                + distance, twoHundred, heightLabel);
        jcb_line.addActionListener(CChangeSelection.getInstance());
        super.add(jcb_line);
    
        jcb_maths = new JCheckBox("maths");
        jcb_maths.setFocusable(false);
        jcb_maths.setOpaque(false);
        jcb_maths.setVerticalAlignment(SwingConstants.TOP);
        jcb_maths.setBounds(_x, jcb_line.getHeight() + jcb_line.getY() 
                + distance, twoHundred, heightLabel);
        jcb_maths.addActionListener(CChangeSelection.getInstance());
        super.add(jcb_maths);
        
        //deactivate the items because at the beginning there is no item 
        //selected.
        CChangeSelection.deactivateOp();
    

        insertTrennung(jcb_maths.getWidth() + jcb_maths.getX() 
                + ViewSettings.DISTANCE_BEFORE_LINE, 
                jcb_maths.getY(), 1, _paint);
        insertInformation("Pen", _x, jlbl_separation[1].getX(), 1, 
                _paint);

        return jlbl_separation[1].getX();
        
	}
	
	
	/**
	 * initialize other items.
	 * @param _x the x coordinate
	 * @param _paint whether to paint or not
	 */
	private void initOthers(final int _x, final boolean _paint) {

        Item1Button tb = new Item1Button(null);
        tb.setOpaque(true);
        tb.setSize(htf, htf);
        tb.setLocation(_x + distance, distance);
        tb.setText("Groesse aendern");
        tb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb.setActivable(false);
        tb.setIcon("paint/test.png");
        super.add(tb);

        Item1Button tb_changePen = new Item1Button(null);
        tb_changePen.setOpaque(true);
        tb_changePen.setSize(htf, htf);
        tb_changePen.setLocation(tb.getX() + tb.getWidth() + distance, 
                tb.getY());
        tb_changePen.setText("Stift aendern");
        tb_changePen.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black),
                new LineBorder(Color.white)));
        tb_changePen.setActivable(false);
        tb_changePen.setIcon("paint/test.png");
        super.add(tb_changePen);

        //pen 1
        Item1Menu it_stift1 = new Item1Menu();
        it_stift1.setBorder(null);
        it_stift1.setBorder(false);
        it_stift1.setText("Drehen/Spiegeln");
        it_stift1.setLocation(tb_changePen.getX() 
                + tb_changePen.getWidth() + distance, 
                tb_changePen.getY());
        it_stift1.setSize(twoHundred, twoHundred + twoHundred / 2);
        it_stift1.setActivable();
        it_stift1.setItemsInRow((byte) 1);
        it_stift1.setBorder(false);
        super.add(it_stift1);

        insertTrennung(it_stift1.getWidth() + it_stift1.getX() + ViewSettings
                .DISTANCE_BEFORE_LINE, it_stift1.getY(), 1, _paint);
        insertInformation("Farben", _x, jlbl_separation[1].getX(), 1, 
                _paint);
	}
	
	

    /**
     * 
     * @param _x the x coordinate in pX
     * @param _y the y coordinate in pX
     * @param _locInArray the location in array
     * @param _add whether to add or not
     */
    private void insertTrennung(final int _x, final int _y, 
            final int _locInArray, final boolean _add) {
        
        //if new initialization is demanded
        if (_add) {
            this.jlbl_separation[_locInArray] = new JLabel();
            this.jlbl_separation[_locInArray].setBorder(
                    BorderFactory.createLineBorder(
                            ViewSettings.CLR_BACKGROUND_DARK_XX));
            super.add(this.jlbl_separation[_locInArray]);
            
        }
        final int number = 145;
        this.jlbl_separation[_locInArray].setBounds(_x, _y, 1, number);
    }
    
    /**
     * insert information text.
     * @param _text the printed text
     * @param _x1 first x coordinate (is between first and second coordinate)
     * @param _x2 second x coordinate (is between first and second coordinate)
     * @param _locationInArray the location in information array
     * @param _insert whether to insert the item or just to change bounds
     */
    private void insertInformation(final String _text, 
            final int _x1, final int _x2, final int _locationInArray, 
            final boolean _insert) {

        if (_insert) {

            //final value for foreground for JLabel
            final int rgb = 190;
            
            jlbl_information[_locationInArray] = new VLabel();
            jlbl_information[_locationInArray].setFont(
                    ViewSettings.TP_FONT_INFORMATION);
            jlbl_information[_locationInArray].setForeground(
                    new Color(rgb, rgb, rgb));
            jlbl_information[_locationInArray].setHorizontalAlignment(
                    SwingConstants.CENTER);
            jlbl_information[_locationInArray].setText(_text);
            super.add(jlbl_information[_locationInArray]);
            
        }

        if (Status.isNormalRotation()) {

            final int number = 135;
            final int number2 = 15;
            jlbl_information[_locationInArray].setBounds(
                    _x1, number, _x2 - _x1, number2);
        } else {
            jlbl_information[_locationInArray].setOpaque(true);
            jlbl_information[_locationInArray].setBounds(-1, -1, -1, -1);
        }

    }
    

    /**
     * getter method for only instance of this class.
     * @param _height the height of the selection view.
     * @return the only instance of CChangeSelection
     */
    public static Selection getInstance(final int _height) {
        
        if (instance == null) {
            instance = new Selection();
            instance.init(_height);
        }
        
        return instance;
        
    }

    /**
     * getter method for only instance of this class.
     * @return the only instance of CChangeSelection
     */
    public static Selection getInstance() {
        
        return instance;
        
    }

    /**
     * @return the jcb_points
     */
    public JCheckBox getJcb_points() {
        return jcb_points;
    }


    /**
     * @return the jcb_line
     */
    public JCheckBox getJcb_line() {
        return jcb_line;
    }


    /**
     * @return the jcb_maths
     */
    public JCheckBox getJcb_maths() {
        return jcb_maths;
    }
}
