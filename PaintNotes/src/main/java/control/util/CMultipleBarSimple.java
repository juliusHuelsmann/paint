package control.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import view.util.MultipleBar;

public class CMultipleBarSimple implements MouseMotionListener, MouseListener {

	/**
	 * 
	 */
	private BufferedImage bi;
	
	/**
	 * The location of the Bar items
	 */
	private int [] location;
	
	/**
	 * the view class.
	 */
	private MultipleBar mb;

	/**
	 * The minimal and maximal values of bar.
	 */
	private double min, max;

	private final Color clr_background;
	
	/**
	 * 
	 * @param _mb
	 */
	public CMultipleBarSimple(
			final MultipleBar _mb,
			final int _amount,
			final double _min, final double _max) {
		this.mb = _mb;
		this.min = _min;
		this.max = _max;

		location = new int[_amount];
		clr_background = Color.white;
		
	}
	/**
	 * 
	 * @param _mb
	 */
	public CMultipleBarSimple(
			final MultipleBar _mb,
			final int _amount,
			final double _min, final double _max,
			final Color _clr_background) {
		this.mb = _mb;
		this.min = _min;
		this.max = _max;

		location = new int[_amount];
		clr_background = _clr_background;
		
	}
	
	private int pressed = -1;

	@Override
	public void mousePressed(final MouseEvent _event) {
		
		int minDistance = bi.getWidth() + 1;
		int minIndex = -1;
		for (int i = 0; i < location.length; i++) {
			final int distance = Math.abs(_event.getX() - location[i]);
			if (distance <= minDistance) {
				minDistance = distance;
				minIndex = i;
			} else {
				i = location.length;
			}
		}
		
		if (minDistance < 5) {
			pressed = minIndex;
		} else {
			pressed = -1;
		}
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = -1;
		double[] cl = getComputedLocations();
		for (int i = 0; i < location.length; i++) {

			System.out.println(cl[i]);	
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }
	@Override
	public void mouseDragged(MouseEvent e) { 
		if (pressed != -1) {
			
			
			// update the location in view and control
			final int nextX = e.getX();
			final int prevX = location[pressed];
			
			if (pressed + 1 >= location.length 
					|| nextX < location[pressed + 1]){

				location[pressed] = nextX;

				
				changeLocationBar(prevX, nextX);
			}
	
		}
		
	}
	@Override
	public void mouseMoved(MouseEvent e) { }




	public void afterSizeChange() {
		bi = new BufferedImage(mb.getWidth(), mb.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);

		final int white = clr_background.getRGB();
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				bi.setRGB(i, j, white);
			}
		}
		for (int i = 0; i < location.length; i++) {
			location[i] = bi.getWidth() * i / location.length;
		}
		paintBar();
	}
	
	public void paintBar() {

	
		Graphics g = bi.getGraphics();
		g.setColor(Color.black);
		g.drawLine(0, 5, bi.getWidth(), 5);
		g.drawString("" + getMin(), 0, mb.getHeight() - 10);
		g.drawString("" + getMax(), mb.getWidth() - 20, mb.getHeight() - 10);
		
		for (int i = 0; i < getLocation().length; i++) {
			final int x = getLocation()[i];
			g.drawLine(x, 2, x, 9);
		}
		mb.setIcon(new ImageIcon(bi));
	}
	
	
	public void changeLocationBar(final int _prevX, final int _nextX) {

		Graphics g = bi.getGraphics();
		g.setColor(clr_background);
		g.drawLine(_prevX, 2, _prevX, 9);
		g.setColor(Color.black);
		g.drawLine(_nextX, 2, _nextX, 9);
		g.drawLine(_prevX, 5, _prevX, 5);
		
		mb.setIcon(new ImageIcon(bi));
	}
	
	/**
	 * get the real locations.
	 * @return
	 */
	public double[] getComputedLocations() {
		final double[] realLocation = new double[getLocation().length];
		for (int i = 0; i < getLocation().length; i++) {
			realLocation[i] = getMin() + (int) (1.0 * getLocation()[i] * (getMax() - getMin())/ bi.getWidth() );
		}
		return realLocation;
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[]_args){
		MultipleBar mb = new MultipleBar(2, 10, 200);
		mb.setSize(300, 40);
		mb.setLocation(10, 50);
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(400, 400);
		jf.setLocationRelativeTo(null);
		jf.setLayout(null);
		jf.add(mb);
		jf.setVisible(true);
		
	}
	
	
	
	

	/**
	 * @return the location
	 */
	public int [] getLocation() {
		return location;
	}




	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}



	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}



	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}



	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}
}
