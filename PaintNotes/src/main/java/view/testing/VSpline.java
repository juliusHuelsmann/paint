package view.testing;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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

import view.util.mega.MTextField;
import model.objects.pen.Pen;
import model.util.DPoint;
import model.util.adt.list.List;


/**
 * View class for testing spline.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class VSpline extends JPanel {

	
	/**
	 * MTextField that displays the matrix.
	 */
	private MTextField[][] jlbl_matrix;
	
	/**
	 * MTextField for setting the amount of points.
	 */
	private MTextField jlbl_amountPoints;
	
	/**
	 * JButton which for enabling spline.
	 */
	private JButton jbtn_spline;
	
	/**
	 * Container which .
	 */
	private Container c;

	
	/**
	 * KeyListener for parsing string to integer.
	 */
	private KeyListener kl = new KeyListener() {
		
		public void keyTyped(final KeyEvent _event) { }
		
		public void keyReleased(final KeyEvent _event) {

				int cols = 0;
				
				try {
					cols = Integer.parseInt(jlbl_amountPoints.getText());
				} catch (Exception e) {
					
				}
				if (cols > 0) {
					initializeJLabel(cols);
				}
				repaint();
		}
		
		public void keyPressed(final KeyEvent _event) { }
	};
	
	
	
	/**
	 * ActionListener for starting spline interpolation.
	 */
	private ActionListener al = new ActionListener() {
		
		
		/**
		 * {@inheritDoc}
		 */
		public void actionPerformed(final ActionEvent _event) {
			
			/*
			 * generate list of points
			 */
			List<DPoint> ls_point = new List<DPoint>();
			boolean error = jlbl_matrix == null;
			for (int i = 0; !error && i < jlbl_matrix.length; i++) {

				double x  = 0, y = 0;
				try {
					x = Double.parseDouble(jlbl_matrix[i][0].getText());
					y = Double.parseDouble(jlbl_matrix[i][1].getText());
				} catch (Exception e) {
					error = true;
				}
				ls_point.insertAtTheEnd(new DPoint(x, y));
			}
			
			if (!error) {

				double [][] a = Pen.spline(ls_point, true, null, null, null);
				
				setVisible(false);
				if (a.length > 0) {

					JOptionPane.showMessageDialog(getIt(), "success.");
					c.add(new VMat(a));
				}
				
			} else {
				JOptionPane.showMessageDialog(
						getIt(), "Error: illegal values.");
			}
		}
	};
	
	
	/**
	 * @return the vspline.
	 */
	private JPanel getIt() {
		return this;
	}
	
	
	/**
	 * Initialize the labels with specified amount of points.
	 * @param _amountPoints		the specified amount of points in spline.
	 */
	private void initializeJLabel(final int _amountPoints) {
		//rm old
		int amountCoordinates = 2;

		for (int h = 0; jlbl_matrix != null && h < jlbl_matrix.length; h++) {
			for (int w = 0; w < jlbl_matrix[h].length; w++) {
				remove(jlbl_matrix[h][w]);
			}
		}
		
		jlbl_matrix = new MTextField[_amountPoints][amountCoordinates];
		for (int h = 0; h < _amountPoints; h++) {
			for (int w = 0; w < amountCoordinates; w++) {
				jlbl_matrix[h][w] = new MTextField();
				double itW = 1.0 * (getWidth()) / amountCoordinates;
				double itH = 1.0 * (getHeight() - jlbl_amountPoints.getHeight()
						- jlbl_amountPoints.getY() - 50) / _amountPoints;
				jlbl_matrix[h][w].setBounds((int) (w * itW),
						(int) (h * itH
						+ jlbl_amountPoints.getHeight() 
						+ jlbl_amountPoints.getY() + 5), 
						(int) (itW), (int) itH);
				super.add(jlbl_matrix[h][w]);
			}
		}
	}
	
	
	
	/**
	 * Recalculate and apply the size and location of the matrix-JLabels.
	 * Add the JLabels to view afterwards.
	 * This function does not remove the JLabels.
	 */
	private void resetSize() {

		for (int h = 0; jlbl_matrix != null && h < jlbl_matrix.length; h++) {
			for (int w = 0; w < jlbl_matrix[h].length; w++) {
				double itW = 1.0 * (getWidth()) / jlbl_matrix[h].length;
				double itH = 1.0 * (getHeight() - jlbl_amountPoints.getHeight() 
						- jlbl_amountPoints.getY() - 50) / jlbl_matrix.length;
				jlbl_matrix[h][w].setBounds((int) (w * itW),
						(int) (h * itH + jlbl_amountPoints.getHeight() 
								+ jlbl_amountPoints.getY() + 5), 
								(int) (itW), (int) itH);
				super.add(jlbl_matrix[h][w]);
			}
		}
	}
	
	
	/**
	 * Constructor of the view - spline class.
	 * @param _c	the Component which contains this class.
	 */
	public VSpline(final Container _c) {
		super();
		
		//hard coded size
		final int size = 300;
		super.setLayout(null);
		super.setSize(size, size);
		this.c = _c;
		jlbl_amountPoints = new MTextField("0");
		jlbl_amountPoints.setBounds(0 * getWidth(), 5, getWidth() / 2, 20);
		jlbl_amountPoints.addKeyListener(kl);
		super.add(jlbl_amountPoints);
		
		jbtn_spline = new JButton("spline!");
		jbtn_spline.setBounds(0 * getWidth(), 5, getWidth() / 2, 20);
		jbtn_spline.addActionListener(al);
		super.add(jbtn_spline);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final void setSize(final int _x, final int _y) {
		
		final int fife = 5;
		super.setSize(_x, _y);
		jlbl_amountPoints.setBounds(0 * getWidth() / 2, fife, getWidth() / 2,
				fife * (2 + 2));
		jbtn_spline.setBounds(1 * getWidth() / 2, fife, 
				getWidth() / 2, fife * (2 + 2));
		resetSize();
	}
	
	
	/**
	 * 
	 * @param _args
	 */
	public static final void main(final String[] _args) {
		
		
		JFrame jf = new JFrame() {
			
			
			/**
			 * 
			 */
			private Component vsp;


			/**
			 * Add a content and save it as root component.
			 */
			public Component add(final Component _c) {
				vsp = _c;
				return super.add(_c);
			}
			
			
			/**
			 * set the size of the JFrame and adapt the size of its
			 * content.
			 * 
			 * @param _x the new width
			 * @param _y the new height
			 */
			public void setSize(
					final int _x, final int _y) {
				if (vsp != null) {

					vsp.setSize(_x, _y);
				}
				super.setSize(_x, _y);
			}
			
			
			/**
			 * call the set size function.
			 */
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
