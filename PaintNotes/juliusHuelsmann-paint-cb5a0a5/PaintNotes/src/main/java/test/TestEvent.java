package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TestEvent extends JFrame implements MouseListener, MouseMotionListener,  ActionListener {

	private JButton jbtn;
	
	public TestEvent() {
		super();
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setSize(700, 700);
		super.setLocationRelativeTo(null);
		
		jbtn = new JButton();
		jbtn.setSize(getSize());
		super.add(jbtn);
		
		jbtn.addMouseListener(this);
		jbtn.addMouseMotionListener(this);
		jbtn.addActionListener(this);
		
		super.setResizable(false);
		super.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0);
	}

	public void mouseClicked(MouseEvent arg0) {

		System.out.println(arg0);		
	}

	public void mouseEntered(MouseEvent arg0) {

		System.out.println(arg0);		
	}

	public void mouseExited(MouseEvent arg0) {

		System.out.println(arg0);		
	}

	public void mousePressed(MouseEvent arg0) {

		System.out.println(arg0);
	}

	public void mouseReleased(MouseEvent arg0) {

		System.out.println(arg0);		
	}

	public void mouseDragged(MouseEvent arg0) {

		System.out.println(arg0);		
	}

	public void mouseMoved(MouseEvent arg0) {

	}
	public static void main(String[]args){
		new TestEvent();
	}

}
