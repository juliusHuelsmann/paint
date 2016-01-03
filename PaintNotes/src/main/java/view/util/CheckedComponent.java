package view.util;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.mail.search.FromStringTerm;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import view.util.mega.MPanel;

public class CheckedComponent extends MPanel {

	private JCheckBox jcb_check;
	private MPanel jpnl_contents;
	
	/**
	 * overwrites add method.
	 */
	public Component add(final Component _c) {
		if (jpnl_contents == null) {
			return null;
		}
		return jpnl_contents.add(_c);
	}
	
	
	public boolean isSelected() {
		return jcb_check.isSelected();
	}
	/**
	 * Constructor: initializes componentes.
	 */
	public CheckedComponent(final String _d) {
//		super.setBorder(new LineBorder(Color.black));
		super.setLayout(null);
		jpnl_contents = new MPanel();
		jpnl_contents.setLayout(null);
		super.add(jpnl_contents);
		
		jcb_check = new JCheckBox(_d);
		super.add(jcb_check);
	}
	
	
	public int getResultingWidth() {
		return jpnl_contents.getWidth();
	}
	

	
	public static void main(String[]_args){
		CheckedComponent cp = new CheckedComponent("hi");
		cp.setSize(300, 40);
		
		
		MultipleBar mb = new MultipleBar(2, 10, 200);
		mb.setSize(cp.getResultingWidth(), cp.getHeight());
		cp.setLocation(10, 50);
		cp.add(mb);
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(400, 400);
		jf.setLocationRelativeTo(null);
		jf.setLayout(null);
		jf.add(cp);
		jf.setVisible(true);
		
	}
	/**
	 * Set size.
	 */
	public void setSize(int _width, int _height) {
		super.setSize(_width, _height);

		final int width = (int) jcb_check.getPreferredSize().getWidth();
		jcb_check.setBounds(5, 5, width, getHeight() - 10);

		jpnl_contents.setBounds(jcb_check.getX() + jcb_check.getWidth(),
				jcb_check.getY(), 
				Math.max(1, getWidth() - jcb_check.getWidth()),
				jcb_check.getHeight());
		for (Component j : jpnl_contents.getComponents()) {
			j.setSize(Math.max(1, jpnl_contents.getWidth() - 5),
					jpnl_contents.getHeight());
		}
		
		
	}
	
	
	public void setSize(final Dimension _d) {
		this.setSize((int) _d.getWidth(), (int) _d.getHeight());
	}
}
