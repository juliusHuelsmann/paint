package model.util;

import java.awt.Rectangle;

public class DRect {

	private double x, y, width, height;


	public DRect() {
		
	}
	public DRect(Rectangle _r) {
		x = _r.x;
		y = _r.y;
		width = _r.width;
		height = _r.height;
	}

	public DRect(double _x, double _y, double _width, double _height) {
		this.x = _x;
		this.y = _y;
		this.width = _width;
		this.height = _height;
		
	}
	public DRect(int _x, int _y, int _width, int _height) {
		this.x = _x;
		this.y = _y;
		this.width = _width;
		this.height = _height;
		
	}
	
	
	public Rectangle getRectangle() {
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}


	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	
	


	/**
	 * @return the x
	 */
	public int getIX() {
		return (int) x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getIY() {
		return (int) y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public int getIWidth() {
		return (int) width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getIHeight() {
		return (int) height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
