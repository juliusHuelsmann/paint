package view.testing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

import view.util.mega.MTextField;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class VMat extends JPanel {

	
	/**
	 * 
	 */
	private MTextField[][] jlbl_matrix;
	
	/**
	 * 
	 */
	private MTextField jlbl_amountRows, jlbl_amountCols;
	
	
	
	/**
	 * 
	 */
	private KeyListener kl = new KeyListener() {
		
		public void keyTyped(final KeyEvent _event) { }
		
		public void keyReleased(final KeyEvent _event) {

//			if (_event.getSource().equals(jlbl_amountRows)) {
				int rows = 0;
				int cols = 0;
				
				try {
					rows = Integer.parseInt(jlbl_amountRows.getText());
					cols = Integer.parseInt(jlbl_amountCols.getText());
				} catch (Exception e) {
					//do nothing.
					cols = 0;
				}
				if (cols > 0 && rows > 0) {
					initializeJLabel(cols, rows);
				}
				repaint();
		}
		
		public void keyPressed(final KeyEvent _event) { }
	};
	
	
	/**
	 * 
	 * @param _h
	 * @param _w
	 */
	private void initializeJLabel(final int _h, final int _w) {
		//rm old

		for (int wid = 0; jlbl_matrix != null
				&& wid < jlbl_matrix.length; wid++) {
			for (int heig = 0; heig < jlbl_matrix[wid].length; heig++) {
				remove(jlbl_matrix[wid][heig]);
			}
		}
		
		jlbl_matrix = new MTextField[_h][_w];
		for (int wid = 0; wid < _h; wid++) {
			for (int heig = 0; heig < _w; heig++) {
				jlbl_matrix[wid][heig] = new MTextField();
				double itW = 1.0 * (getWidth()) / _w;
				double itH = 1.0 * (getHeight() - jlbl_amountCols.getHeight() 
						- jlbl_amountCols.getY() - 50) / _h;
				jlbl_matrix[wid][heig].setBounds((int) (heig * itW),
						(int) (wid * itH + jlbl_amountCols.getHeight() 
								+ jlbl_amountCols.getY() + 5),
								(int) (itW), (int) itH);
				super.add(jlbl_matrix[wid][heig]);
			}
		}
	}
	
	
	/**
	 * 
	 */
	private void resetSize() {

		if (jlbl_matrix != null) {

			for (int heig = 0; heig < jlbl_matrix.length; heig++) {
				for (int widt = 0; widt < jlbl_matrix[heig].length; widt++) {
					double itW = 1.0 * (getWidth()) / jlbl_matrix[widt].length;
					double itH = 1.0 * (getHeight() 
							- jlbl_amountCols.getHeight()
							- jlbl_amountCols.getY() - 50) / jlbl_matrix.length;
					jlbl_matrix[widt][heig].setBounds((int) (heig * itW),
							(int) (widt * itH + jlbl_amountCols.getHeight() 
									+ jlbl_amountCols.getY() + 5), 
									(int) (itW), (int) itH);
					super.add(jlbl_matrix[widt][heig]);
				}
			}
		}
	}

	
	/**
	 * Constructor of the view matrix class.
	 */
	public VMat() {
		super();
		super.setLayout(null);
		super.setSize(300, 300);
		
		jlbl_amountCols = new MTextField("0");
		jlbl_amountCols.setBounds(0 * getWidth() / 2, 5, getWidth() / 2, 20);
		jlbl_amountCols.addKeyListener(kl);
		super.add(jlbl_amountCols);
		
		jlbl_amountRows = new MTextField("0");
		jlbl_amountRows.addKeyListener(kl);
		jlbl_amountRows.setBounds(1 * getWidth() / 2, 5, getWidth() / 2, 20);
		super.add(jlbl_amountRows);
	}
	

	/**
	 * Constructor of the view matrix class, for already displaying a matrix
	 * containing double values.
	 * 
	 * @param _vals	the matrix that will be displayed.
	 */
	public VMat(final double[][] _vals) {
		super();
		//hard coded size
		final int size = 300;
		super.setLayout(null);
		super.setSize(size, size);

		int height = _vals.length;
		int width = _vals[0].length;
		
		jlbl_amountCols = new MTextField(width);
		jlbl_amountCols.setBounds(0 * getWidth() / 2, 5, getWidth() / 2, 20);
		jlbl_amountCols.setEditable(false);
		super.add(jlbl_amountCols);
		
		jlbl_amountRows = new MTextField(height);
		jlbl_amountRows.setBounds(1 * getWidth() / 2, 5, getWidth() / 2, 20);
		jlbl_amountCols.setEditable(false);
		super.add(jlbl_amountRows);
		initializeJLabel(width,  height);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				jlbl_matrix[i][j].setEditable(false);
				jlbl_matrix[i][j].setText("" + _vals[j][i]);
				
			}	
		}
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public final void setSize(final int _x, final int _y) {
		super.setSize(_x, _y);
		final int twenty = 20;
		final int five = 5;
		
		jlbl_amountCols.setBounds(0 * getWidth() / 2, 
				five, getWidth() / 2, twenty);
		jlbl_amountRows.setBounds(1 * getWidth() / 2, 
				five, getWidth() / 2, twenty);
		resetSize();
	}
	
	
	/**
	 * the main function.
	 * @param _args the arguments
	 */
	public static final void main(final String[] _args) {
		
		final int viewSize = 300;
		
		final VMat vsp = new VMat();
		
		JFrame jf = new JFrame() {

			public void setSize(final int _x, final int _y) {
				vsp.setSize(_x, _y);
				super.setSize(_x, _y);
			}
			
			public void validate() {
				super.validate();
				vsp.setSize(getWidth(), getHeight());
			}
		};
		jf.setSize(viewSize, viewSize);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.setLayout(null);
		jf.setResizable(true);
		
		
		jf.add(vsp);
	}
}
