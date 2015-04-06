package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


/**
 * Test class for getting the right order of elements and for gathering the 
 * different touch options.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class TestEvent extends JFrame implements MouseListener, 
MouseMotionListener,  ActionListener {

	/**
	 * The JButton.
	 */
	private JButton jbtn;
	
	
	/**
	 * Constructror.
	 */
	public TestEvent() {
		
		super();
		final int size = 700;
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setSize(size, size);
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
	
	/**
	 * {@inheritDoc}
	 */
	public final void actionPerformed(final ActionEvent _event) {
		System.out.println(_event);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void mouseClicked(final MouseEvent _event) {
		System.out.println(_event);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void mouseEntered(final MouseEvent _event) {
		System.out.println(_event);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void mouseExited(final MouseEvent _event) {
		System.out.println(_event);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void mousePressed(final MouseEvent _event) {
		System.out.println(_event);
	}

	/**
	 * {@inheritDoc}
	 */

	public final void mouseReleased(final MouseEvent _event) {
		System.out.println(_event);
	}

	/**
	 * {@inheritDoc}
	 */

	public final void mouseDragged(final MouseEvent _event) {
		System.out.println(_event);
		}

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseMoved(final MouseEvent _event) {

	}
	
	/**
	 * main method.
	 * @param _args the arguments.
	 */
	public static void main(final String[] _args) {
		new TestEvent();
	}


}
