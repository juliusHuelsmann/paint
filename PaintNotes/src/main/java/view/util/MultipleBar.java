package view.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import control.util.CMultipleBarImage;
import control.util.CMultipleBarSimple;


/**
 * 
 * @author juli
 *
 */
public class MultipleBar extends JLabel {

	
	/**
	 * The controller class of this utility class.
	 */
	private CMultipleBarSimple controller;
	
	
	
	/**
	 * Constructor initializes the gui contents.
	 * @param _amount the amount of bars to move.
	 * @param _min the minimal value of bar
	 * @param _max the max value of bar.
	 */
	public MultipleBar(final int _amount,
			final double _min,
			final double _max,
			final Color _clr_background) {
		super();
		super.setOpaque(false);
		super.setLayout(null);
		this.controller = new CMultipleBarSimple(this, _amount, _min, _max, _clr_background);
		super.addMouseMotionListener(controller);
		super.addMouseListener(controller);
		
	}

	
	/**
	 * Constructor initializes the gui contents.
	 * @param _amount the amount of bars to move.
	 * @param _min the minimal value of bar
	 * @param _max the max value of bar.
	 */
	public MultipleBar(final int _amount,
			final double _min,
			final double _max) {
		super();
		super.setOpaque(false);
		super.setLayout(null);
		this.controller = new CMultipleBarSimple(this, _amount, _min, _max);
		super.addMouseMotionListener(controller);
		super.addMouseListener(controller);
	}
	
	/**
	 * Set size.
	 */
	public void setSize(final int _width, final int _height) {
		super.setSize(_width, _height);
		if (controller != null) {

			controller.afterSizeChange();	
		}
		

	}
	
	
	
	
	
	/**
	 * get the real locations.
	 * @return
	 */
	public double[] getComputedLocations() {
		return controller.getComputedLocations();
	}
	
	



	/**
	 * @return the min
	 */
	public double getMin() {
		return controller.getMin();
	}



	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		controller.setMin(min);
	}



	/**
	 * @return the max
	 */
	public double getMax() {
		return controller.getMax();
	}



	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		controller.setMax(max);
	}
	
}
