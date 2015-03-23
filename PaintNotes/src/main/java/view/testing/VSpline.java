package view.testing;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.objects.pen.Pen;
import model.util.DPoint;
import model.util.adt.list.List;

public class VSpline extends JPanel{

	private JTextField[][] jlbl_matrix;
	private JTextField jlbl_amountPoints;
	private JButton jbtn_spline;
	private Container c;
	
	private final int addY = 50;
	
	private KeyListener kl = new KeyListener() {
		
		public void keyTyped(KeyEvent _event) { }
		
		public void keyReleased(KeyEvent _event) {

				int cols = 0;
				
				try{
					cols = Integer.parseInt(jlbl_amountPoints.getText());
				}catch(Exception e) {
					
				}
				if (cols > 0)
				initializeJLabel(cols);
				repaint();
			
		}
		
		public void keyPressed(KeyEvent _event) { }
	};
	
	private ActionListener al = new ActionListener() {
		
		public void actionPerformed(ActionEvent _event) {
			
			/*
			 * generate list of points
			 */
			List<DPoint> ls_point = new List<DPoint>();
			boolean error = jlbl_matrix == null;
			for (int i = 0; !error && i < jlbl_matrix.length; i++) {

				double x  = 0, y = 0;
				try{
					x = Double.parseDouble(jlbl_matrix[i][0].getText());
					y = Double.parseDouble(jlbl_matrix[i][1].getText());
				}catch(Exception e) {
					error = true;
				}
				ls_point.insertAtTheEnd(new DPoint(x, y));
			}
			
			if (!error) {

				double [][] a = Pen.spline(ls_point, true);
				
				setVisible(false);
				if (a.length > 0) {

					JOptionPane.showMessageDialog(getIt(), "success.");
					c.add(new VMat(a));
				}
				
			} else {
				JOptionPane.showMessageDialog(getIt(), "Error: illegal values.");
			}
		}
	};
	
	private JPanel getIt() {
		return this;
	}
	
	private void initializeJLabel(int _amountPoints) {
		//rm old
		int amountCoordinates = 2;

		for (int h = 0; jlbl_matrix != null && h < jlbl_matrix.length; h++) {
			for (int w = 0; w < jlbl_matrix[h].length; w++) {
				remove(jlbl_matrix[h][w]);
			}
		}
		
		jlbl_matrix = new JTextField[_amountPoints][amountCoordinates];
		for (int h = 0; h < _amountPoints; h++) {
			for (int w = 0; w < amountCoordinates; w++) {
				jlbl_matrix[h][w] = new JTextField();
				double itW = 1.0 * (getWidth()) / amountCoordinates;
				double itH = 1.0 * (getHeight() - jlbl_amountPoints.getHeight() - jlbl_amountPoints.getY()- 50) / _amountPoints;
				jlbl_matrix[h][w].setBounds((int)(w * itW) ,(int) (h * itH+ jlbl_amountPoints.getHeight() +jlbl_amountPoints.getY() + 5), (int)(itW), (int)itH);
				super.add(jlbl_matrix[h][w]);
			}
		}
	}
	
	private void resetSize() {

		for (int h = 0; jlbl_matrix != null && h < jlbl_matrix.length; h++) {
			for (int w = 0; w < jlbl_matrix[h].length; w++) {
				double itW = 1.0 * (getWidth()) / jlbl_matrix[h].length;
				double itH = 1.0 * (getHeight() - jlbl_amountPoints.getHeight() - jlbl_amountPoints.getY()- 50) / jlbl_matrix.length;
				jlbl_matrix[h][w].setBounds((int)(w * itW) ,(int) (h * itH+ jlbl_amountPoints.getHeight() +jlbl_amountPoints.getY() + 5), (int)(itW), (int)itH);
				super.add(jlbl_matrix[h][w]);
			}
		}
	}
	
	public VSpline(Container _c) {
		super();
		super.setLayout(null);
		super.setSize(300, 300);
		this.c = _c;
		jlbl_amountPoints = new JTextField("0");
		jlbl_amountPoints.setBounds(0 * getWidth(), 5, getWidth() / 2, 20);
		jlbl_amountPoints.addKeyListener(kl);
		super.add(jlbl_amountPoints);
		
		jbtn_spline = new JButton("spline!");
		jbtn_spline.setBounds(0 * getWidth(), 5, getWidth() / 2, 20);
		jbtn_spline.addActionListener(al);
		super.add(jbtn_spline);
	}
	
	
	public void setSize(int _x, int _y) {
		super.setSize(_x, _y);
		jlbl_amountPoints.setBounds(0 * getWidth() / 2, 5, getWidth() / 2, 20);
		jbtn_spline.setBounds(1 * getWidth() / 2, 5, getWidth() / 2, 20);
		resetSize();
	}
	
	public static void main(String[]args) {
		
		
		JFrame jf = new JFrame() {
			
			Component vsp;

			public Component add(Component _c) {
				vsp = _c;
				return super.add(_c);
			}
			
			public void setSize(int _x, int _y) {
				if (vsp != null) {

					vsp.setSize(_x, _y);
				}
				super.setSize(_x, _y);
			}
			
			public void validate() {
				super.validate();
				if (vsp != null) {

					vsp.setSize(getWidth(), getHeight());
				}
			}
		};

		final VSpline vsp;
		vsp = new VSpline(jf);
		jf.setSize(300, 300);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.setLayout(null);
		jf.setResizable(true);
		
		
		jf.add(vsp);
	}
}
