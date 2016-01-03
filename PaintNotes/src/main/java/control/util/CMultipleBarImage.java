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

public class CMultipleBarImage implements MouseMotionListener, MouseListener {

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
	private int min, max;

	
	
	/**
	 * 
	 * @param _mb
	 */
	public CMultipleBarImage(MultipleBar _mb, 
			final int _amount,
			final int _min, final int _max) {
		this.mb = _mb;
		this.min = _min;
		this.max = _max;

		location = new int[_amount];
		
		
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
	
	
	
	private BufferedImage bi_first, bi_center, bi_last;
	private int scrollerWidth, scrollerHeigth;
	private int[] rgbA_Scroller;
	
	
	private void paintScroller(final int _x) {
		
		// repaint only center
		bi.setRGB(_x-scrollerWidth/2 , scrollerHeigth, 
				scrollerWidth, scrollerHeigth, rgbA_Scroller,
				0, bi.getWidth());
		
		
		
	}
	
	private void repaintBackgroundScope(
			final int _x, final int _y, 
			final int _width, final int _height) {
		
		if (_x <= bi_first.getWidth()) {
			if (_width + _x <= bi_first.getWidth()) {
				
				// repaint first area
				int[] rgbA = null;
				rgbA = bi_first.getRGB(_x, _y, _width, _height, rgbA, 0, bi_center.getWidth());
				bi.setRGB(_x, _y, _width, _height, rgbA, 0, bi.getWidth());
			} else {
				repaintBackgroundScope(_x, _y, bi_first.getWidth()-_x, _height);
				repaintBackgroundScope(bi_first.getWidth() + 1, _y, _width - (bi_first.getWidth()-_x) , _height);
			}
		} else if (_x + _width >= bi.getWidth() - bi_first.getWidth()) {
			
			if (_x >= bi.getWidth() - bi_first.getWidth() ) {

				// repaint last
				int[] rgbA = null;
				rgbA = bi_last.getRGB(_x, _y, _width, _height, rgbA, 0, bi_center.getWidth());
				bi.setRGB(_x, _y, _width, _height, rgbA, 0, bi.getWidth());
			} else {
				repaintBackgroundScope(_x, _y,  bi.getWidth() - bi_first.getWidth() - 1, _height);
				repaintBackgroundScope(bi_first.getWidth(), _y, _width - ( bi.getWidth() - bi_first.getWidth() - 1) , _height);
			}
		} else {
			// repaint only center
			int[] rgbA = null;
			rgbA = bi_center.getRGB(_x, _y, _width, _height, rgbA, 0, bi_center.getWidth());
			bi.setRGB(_x, _y, _width, _height, rgbA, 0, bi.getWidth());
		}
		
		
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = -1;
		int[] cl = getComputedLocations();
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
		bi = new BufferedImage(mb.getWidth(), mb.getHeight(), BufferedImage.TYPE_INT_RGB);

		final int white = Color.white.getRGB();
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				bi.setRGB(i, j, white);
			}
		}
		for (int i = 0; i < location.length; i++) {
			location[i] = bi.getWidth() * i / location.length;
		}
		
		
		// read images
		
		
		
		
		
		
		
		
		paintBar();
	}
	
	public void paintBar() {

	
		repaintBackgroundScope(0, 0, bi.getWidth(), bi.getHeight());
		
		for (int i = 0; i < getLocation().length; i++) {
			final int x = getLocation()[i];
			paintScroller(x);
		}
		mb.setIcon(new ImageIcon(bi));
	}
	
	
	public void changeLocationBar(final int _prevX, final int _nextX) {

		Graphics g = bi.getGraphics();
		g.setColor(Color.white);
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
	public int[] getComputedLocations() {
		final int[] realLocation = new int[getLocation().length];
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
	public int getMin() {
		return min;
	}



	/**
	 * @param min the min to set
	 */
	public void setMin(int min) {
		this.min = min;
	}



	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}



	/**
	 * @param max the max to set
	 */
	public void setMax(int max) {
		this.max = max;
	}
}
