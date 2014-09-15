//package declaration
package view.forms.tabs;

//import declarations
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import view.util.Item1Menu;
import view.util.Item1Button;

/**
 * The Selection Tab.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Selection extends JPanel {

    
    /**
     * Constructor.
     * @param _height the height
     */
	public Selection(final int _height) {

		//initialize JPanel and alter settings
		super();
		super.setOpaque(false);
		super.setLayout(null);

		final int distance = 5;
		final int htf = 135;
		final int twoHundred = 200;
		
		Item1Button tb = new Item1Button(null);
		tb.setOpaque(true);
		tb.setSize(htf, htf);
		tb.setLocation(distance, distance);
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

		Item1Button tb_changeColor = new Item1Button(null);
		tb_changeColor.setOpaque(true);
		tb_changeColor.setSize(htf, htf);
		tb_changeColor.setLocation(tb_changePen.getX()
		        + tb_changePen.getWidth() 
		        + distance, tb_changePen.getY());
		tb_changeColor.setText("Farbe aendern");
		tb_changeColor.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(Color.black),
				new LineBorder(Color.white)));
		tb_changeColor.setActivable(false);
		tb_changeColor.setIcon("paint/test.png");
		super.add(tb_changeColor);

		Item1Button tb_cutSelection = new Item1Button(null);
		tb_cutSelection.setOpaque(true);
		tb_cutSelection.setSize(htf, htf);
		tb_cutSelection.setLocation(tb_changeColor.getX() 
		        + tb_changeColor.getWidth()
		        + distance, tb_changeColor.getY());
		tb_cutSelection.setText("Zurechtschneiden");
		tb_cutSelection.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(Color.black),
				new LineBorder(Color.white)));
		tb_cutSelection.setActivable(false);
		tb_cutSelection.setIcon("paint/test.png");
		super.add(tb_cutSelection);

		//pen 1
		Item1Menu it_stift1 = new Item1Menu();
		it_stift1.setBorder(null);
		it_stift1.setBorder(false);
		it_stift1.setText("Drehen/Spiegeln");
		it_stift1.setLocation(tb_cutSelection.getX() 
		        + tb_cutSelection.getWidth() + distance, 
		        tb_changeColor.getY());
		it_stift1.setSize(twoHundred, twoHundred + twoHundred / 2);
		it_stift1.setActivable();
		it_stift1.setItemsInRow((byte) 1);
		it_stift1.setBorder(false);
		super.add(it_stift1);

		super.setSize(
		        (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
		        _height);
	}
}
