package view.util;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InformationWindow extends JFrame {

	private JTextArea jta;
	private JScrollPane jsp;
	
	public InformationWindow() {
		super();
		init();
	}
	
	public InformationWindow(final String _title) {
		super(_title);
		init();
	}
	
	private void init() {

		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.setSize(50, 150);
		jta = new JTextArea();
		jta.setEditable(false);
		jta.setFocusable(false);
		jta.setOpaque(false);
		jta.setBorder(null);

		jsp = new JScrollPane (jta, 
		   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		jsp.setAutoscrolls(true);
		super.add(jsp);
		super.setVisible(true);
	}
	
	
	public void setText(final String _t) {
		jta.setText(_t);
		adaptSize();
	}
	
	
	
	public void appendText(final String _t) {
		jta.append(_t + "\n");
		adaptSize();
	}
	public void adaptSize() {

		final int width = 50;
		super.setSize(
				(int) jta.getPreferredSize().getWidth() + width,
				(int) getHeight());
		super.setLocationRelativeTo(null);
	}
	
	
}
