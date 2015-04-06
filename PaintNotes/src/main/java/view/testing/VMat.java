package view.testing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.util.VScrollPane;

public class VMat extends JPanel {

	private JTextField[][] jlbl_matrix;
	private JTextField jlbl_amountRows, jlbl_amountCols;
	
	private JButton jbtn_spline;
	
	
	private final int addY = 50;
	
	private KeyListener kl = new KeyListener() {
		
		public void keyTyped(KeyEvent _event) {
			System.out.println("t2");
				
//			}
		
			
		}
		
		public void keyReleased(KeyEvent _event) {

//			if (_event.getSource().equals(jlbl_amountRows)) {
				int rows = 0;
				int cols = 0;
				
				try{
					rows = Integer.parseInt(jlbl_amountRows.getText());
					cols = Integer.parseInt(jlbl_amountCols.getText());
				}catch(Exception e) {
					
				}
				if (cols > 0 && rows > 0)
				initializeJLabel(cols, rows);
				repaint();
			System.out.println("rl");
			
		}
		
		public void keyPressed(KeyEvent _event) {
			System.out.println("t");
		}
	};
	
	private void initializeJLabel( int _h, int _w) {
		//rm old

		for (int wid = 0; jlbl_matrix != null && wid < jlbl_matrix.length; wid++) {
			for (int heig = 0; heig < jlbl_matrix[wid].length; heig++) {
				remove(jlbl_matrix[wid][heig]);
			}
		}
		
		jlbl_matrix = new JTextField[_h][_w];
		for (int wid = 0; wid < _h; wid++) {
			for (int heig = 0; heig < _w; heig++) {
				jlbl_matrix[wid][heig] = new JTextField();
				double itW = 1.0 * (getWidth()) / _w;
				double itH = 1.0 * (getHeight() - jlbl_amountCols.getHeight() - jlbl_amountCols.getY()- 50) / _h;
				jlbl_matrix[wid][heig].setBounds((int)(heig * itW) ,(int) (wid * itH+ jlbl_amountCols.getHeight() +jlbl_amountCols.getY() + 5), (int)(itW), (int)itH);
				super.add(jlbl_matrix[wid][heig]);
			}
		}
	}
	
	private void resetSize() {

		if (jlbl_matrix != null) {

			for (int heig = 0; heig < jlbl_matrix.length; heig++) {
				for (int widt = 0; widt < jlbl_matrix[heig].length; widt++) {
					double itW = 1.0 * (getWidth()) / jlbl_matrix[widt].length;
					double itH = 1.0 * (getHeight() - jlbl_amountCols.getHeight() - jlbl_amountCols.getY()- 50) / jlbl_matrix.length;
					jlbl_matrix[widt][heig].setBounds((int)(heig * itW) ,(int) (widt * itH+ jlbl_amountCols.getHeight() +jlbl_amountCols.getY() + 5), (int)(itW), (int)itH);
					super.add(jlbl_matrix[widt][heig]);
				}
			}
		}
	}

	public VMat() {
		super();
		super.setLayout(null);
		super.setSize(300, 300);
		
		jlbl_amountCols = new JTextField("0");
		jlbl_amountCols.setBounds(0 * getWidth() / 2, 5, getWidth() / 2, 20);
		jlbl_amountCols.addKeyListener(kl);
		super.add(jlbl_amountCols);
		
		jlbl_amountRows = new JTextField("0");
		jlbl_amountRows.addKeyListener(kl);
		jlbl_amountRows.setBounds(1 * getWidth() / 2, 5, getWidth() / 2, 20);
		super.add(jlbl_amountRows);
	}
	

	public VMat(double[][] _vals) {
		super();
		super.setLayout(null);
		super.setSize(300, 300);

		int _height = _vals.length;
		int _width = _vals[0].length;
		
		jlbl_amountCols = new JTextField(_width);
		jlbl_amountCols.setBounds(0 * getWidth() / 2, 5, getWidth() / 2, 20);
		jlbl_amountCols.setEditable(false);
		super.add(jlbl_amountCols);
		
		jlbl_amountRows = new JTextField(_height);
		jlbl_amountRows.setBounds(1 * getWidth() / 2, 5, getWidth() / 2, 20);
		jlbl_amountCols.setEditable(false);
		super.add(jlbl_amountRows);
		initializeJLabel( _width,  _height);
		
		for(int i = 0; i < _width; i ++) {
			for(int j = 0; j < _height; j ++) {
				jlbl_matrix[i][j].setEditable(false);
				jlbl_matrix[i][j].setText("" + _vals[j][i]);
				
			}	
		}
	}
	
	
	public void setSize(int _x, int _y) {
		super.setSize(_x, _y);
		jlbl_amountCols.setBounds(0 * getWidth() / 2, 5, getWidth() / 2, 20);
		jlbl_amountRows.setBounds(1 * getWidth() / 2, 5, getWidth() / 2, 20);
		resetSize();
	}
	
	public static void main(String[]args) {
		
		
		final VMat vsp = new VMat();
		JFrame jf = new JFrame() {
			public void setSize(int _x, int _y) {
				vsp.setSize(_x, _y);
				super.setSize(_x, _y);
			}
			
			public void validate() {
				super.validate();
				vsp.setSize(getWidth(), getHeight());
			}
		};
		jf.setSize(300, 300);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.setLayout(null);
		jf.setResizable(true);
		
		
		jf.add(vsp);
	}
}
