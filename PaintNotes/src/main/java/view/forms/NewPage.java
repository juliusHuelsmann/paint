package view.forms;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import view.util.mega.MButton;
import view.util.mega.MPanel;


/**
 * Insert new page between PDF pages.
 * 
 * @author Julius Huelsmann
 */
@SuppressWarnings("serial")
public class NewPage extends MButton {
	
	private static NewPage instance;
	
	private NewPage() {
		super("+");
		super.setLayout(null);
		super.setVisible(true);
		super.setContentAreaFilled(false);
		super.setOpaque(true);
		super.setFocusable(false);
		super.setBorder(BorderFactory.createLineBorder(Color.gray));
		super.setBackground(new Color(160, 170, 165, 150));
		super.setSize(50, 50);
	}
	
	
	public static NewPage getInstance() {
		
		if (instance == null) {
			instance = new NewPage();
		}
		
		return instance;
	}
	
	
	
	public static void main(String[] _args) {
		JFrame jf = new JFrame();
		jf.setBackground(Color.pink);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(500, 500);
		jf.setLayout(null);
		
		NewPage np = new NewPage();
		jf.add(np);

		jf.setVisible(true);;
		jf.setResizable(false);
	}
	
	

}
