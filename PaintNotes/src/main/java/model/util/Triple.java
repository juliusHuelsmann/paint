package model.util;


/**
 * Triple class which contains three double values that can be changed.
 * Used for color transformation from RGB to HSV and vice versa at
 * {@link model.objects.painting.po.PaintObjectDrawImage} insidte the 
 * methods
 *  - {@link model.objects.painting.po.PaintObjectDrawImage.rgbToHsv()}
 *  - {@link model.objects.painting.po.PaintObjectDrawImage.hsvToRgb()}
 * 
 * @author Julius Huelsmann
 * @version %I%; %U%
 */
public class Triple {

	private double a, b, c;

	
	public Triple() {
		
	}
	
	
	/**
	 * 
	 * @param _a
	 * @param _b
	 * @param _c
	 */
	public Triple(final double _a, final double _b, final double _c) {
		this.a = _a;
		this.b = _b;
		this.c = _c;
	}
	
	
	public double getA() {
		return a;
	}

	public void setA(final double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(final double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	
	
	/**
	 * 
	 * @param c
	 */
	public void setC(final double c) {
		this.c = c;
	}
	
	
}
