package view.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.AbstractBorder;

class MyRoundedBorder extends AbstractBorder {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color color;
    private int thickness = 4;
    private int radii = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    private int pointerPad = 4;
    RenderingHints hints;

    

    /**
     * Constructor: save values and initialize variables.
     * 
     * @param _color
     * @param _thickness
     * @param _radii
     * @param _pointerSize
     */
    public MyRoundedBorder(
            Color _color, int _thickness, int _radii, int _pointerSize) {
    	
    	// save values
        this.thickness = _thickness;
        this.radii = _radii;
        this.pointerSize = _pointerSize;
        this.color = _color;

        stroke = new BasicStroke(_thickness);
        strokePad = _thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = _radii + strokePad;
        int bottomPad = pad + _pointerSize + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getBorderInsets(final Component c) {
        return insets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getBorderInsets(final Component c, final Insets insets) {
        return getBorderInsets(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;
//        g2.setColor(new Color(0, 0, 0, 0));

        int bottomLineY = height - thickness - pointerSize;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + strokePad,
                0 + strokePad,
                width - thickness,
                bottomLineY,
                radii,
                radii);

        Polygon pointer = new Polygon();

        // left point
        pointer.addPoint(
                strokePad + radii + pointerPad,
                bottomLineY);
        // right point
        pointer.addPoint(
                strokePad + radii + pointerPad + pointerSize,
                bottomLineY);
        // bottom point
        pointer.addPoint(
                strokePad + radii + pointerPad + (pointerSize / 2),
                height - strokePad);

        Area area = new Area(bubble);
        area.add(new Area(pointer));
        

        g2.setRenderingHints(hints);

//        Area spaceSpace2 = new Area(new Rectangle(0, 0, width, height));
//        spaceSpace2.subtract(area);
        Area spareSpace = new Area(new Rectangle(0, 0, width, height));
//        spareSpace.subtract(spaceSpace2);
        spareSpace.subtract(area);
        g2.setClip(spareSpace);
        

        
        g2.clearRect(0, 0, width, height);
        g2.setClip(null);

        
        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}

