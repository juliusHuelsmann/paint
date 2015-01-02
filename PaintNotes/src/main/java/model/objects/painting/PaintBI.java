package model.objects.painting;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import model.settings.Status;


/**
 * Utility class for painting stuff to a buffered image.
 * @author Julius Huelsmann
 *
 */
public final class PaintBI {

    
    /**
     * Final values for array.
     */
    public static final byte OCCUPIED = 1, FREE = -1, 
            START_BYTE = 3, BORDER = 2;
    
    /**
     * Empty utility class constructor.
     */
    private PaintBI() {
        
    }
    
    
    /**
     * fill rectangle quickly in a certain color.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * 
     * @param _r the rectangle which is to be filled.
     * 
     */
    public static void fillRectangleQuick(
            final BufferedImage _bi, 
            final Color _clr,
            final Rectangle _r) {
        
        //go through the coordinates of the rectangle and paint
        for (int x = 0; x < _r.width; x++) {
            for (int y = 0; y < _r.height; y++) {

                //painting points
                if (_r.y + y < _bi.getHeight() && _r.y + y >= 0
                        && _r.x + x < _bi.getWidth() && _r.x + x >= 0) {
                    
                    _bi.setRGB(_r.x + x, _r.y + y, _clr.getRGB());
                }
                 
            }
        }
    }
    
    
    /**
     * Paints the borders of a polygon in a special 
     * color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * 
     * @param _thickness the thickness of the line painted.
     * 
     * @param _p the points. In between which lines are painted
     * consecutively.
     *
     * 
     * If length is equal to 4 it shell look like
     * 
     *      p1          
     *                  
     *                   p2
     *           p3
     *           
     *       p4
     *       
     * @param _paint whether to paint or just to return the length
     * 
     * @return  a new PolygonReturn instance which contains both length of the
     *          field and the field (as a two dimensional boolean array)
     */
    public static PolygonReturn paintPolygonN(
            final BufferedImage _bi, 
            final Color _clr,
            final int _thickness,
            final Point[] _p,
            final boolean _paint) {
        
        
        //initialize the length counter and the array where the border of 
        //the polygon are saved.
        PolygonReturn polygonReturn = new PolygonReturn(
                _bi.getWidth(), _bi.getHeight());

        //go through the list of points and paint line between points.
        for (int currentPoint = 0; currentPoint < _p.length; currentPoint++) {
            
            //paint line between points. 
            //Special case n (= _p.length) = 1: 
            //  paint a line from point1 to point1 is painting just a point
            polygonReturn.actualize(paintLine(_bi, polygonReturn.getField(), 
                    _clr, _thickness, _p[currentPoint], 
                    _p[(currentPoint + 1) % _p.length], _paint));
        }
        
        
        
        //return the length of the curve in pixel.
        return polygonReturn;
            
    }

    /**
     * Fills a polygon in a special color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * 
     * @param _p the points. In between which lines are painted
     * consecutively.
     *
     * If length is equal to 4 it shell look like
     * 
     *      p1          
     *                  
     *                   p2
     *           p3
     *        x   
     *       p4
     * 
     * @return  a new PolygonReturn instance which contains both length of the
     *          field and the field (as a two dimensional boolean array)
     */
    public static byte[][] printFillPolygonN(
            final BufferedImage _bi, 
            final Color _clr,
            final Point[] _p) {
        PolygonReturn pr = paintPolygonN(_bi, _clr, 1, _p, true);
//        drawPaintBI(pr);
        pr.schwabbel(_bi);
//        drawPaintBI(pr);
        return pr.getField();
    }
    
    /**
     * Fills a polygon in a special color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * 
     * @param _p the points. In between which lines are painted
     * consecutively.
     *
     * If length is equal to 4 it shell look like
     * 
     *      p1          
     *                  
     *                   p2
     *           p3
     *           
     *       p4
     * 
     * @return  a new PolygonReturn instance which contains both length of the
     *          field and the field (as a two dimensional boolean array)
     */
    public static PolygonReturn fillPolygonN(
            final BufferedImage _bi, 
            final Color _clr,
            final Point[] _p) {
        
        
        //generate the polygon which is to be filled.
        PolygonReturn polygonReturn = paintPolygonN(_bi, _clr, 1, _p, true);
        
        //do this until no suitable points are found.
        for (int versuchID = 0;; versuchID++) {
            
            Point[] p = polygonReturn.findCenterPoints(versuchID);
            
            if (p == null) {

                for (int x = 0; x < polygonReturn.getField().length; x++) {
                    for (int y = 0; y < polygonReturn.getField()[x].length; 
                            y++) {
                        if (polygonReturn.getField()[x][y] == 1) {
                            _bi.setRGB(x, y, _clr.getRGB());
                        }
                    }
                }
                
                return polygonReturn;
            } else {
                
                switch (p.length) {
                
                case 0:
                    for (int x = 0; x < polygonReturn.getField().length; x++) {
                        for (int y = 0; y < polygonReturn.getField()[x].length;
                                y++) {
                            if (polygonReturn.getField()[x][y] == 1) {
                                _bi.setRGB(x, y, _clr.getRGB());
                            }
                        }
                    }
                    return polygonReturn;
                    
                case 1:
                    byte [][] umgebung1 = getUmgebung(
                            p[0], polygonReturn.getField());
                    if (umgebung1 == null) {
                        continue;
                    } else {

                        polygonReturn.setField(umgebung1);
                        continue;
                    }

                case 2:
                    

                    byte [][] umgebung11 = getUmgebung(
                            p[0], polygonReturn.getField());
                    if (umgebung11 == null) {

                        byte [][] umgebung2 = getUmgebung(
                                p[1], polygonReturn.getField());
                        
                        if (umgebung2 == null) {
                            
                            continue;
                        } else {
                            polygonReturn.setField(umgebung2);
                            continue;
                            
                        }
                    } else {

                        polygonReturn.setField(umgebung11);
                        continue;
                    }
                    
                    
                    
                default:
                    break;
                
                }


            }
        }
    }
    
    
    
    
    /**
     * fill the environment of a special point if the environment is not
     * neighbored by the border of the bufferedImage.
     * 
     * @param _p the point
     * @param _b the paint array
     * 
     * @return the two dimensional boolean painting map
     */
    private static byte[][] getUmgebung(
            final Point _p, final byte[][] _b) {
        
        byte [][] b = _b;

        
        //go through the entire environment of the point.
        for (int x = _p.x - 1; x <= _p.x + 1; x++) {
            for (int y = _p.y - 1; y <= _p.y + 1; y++) {
                
                //out of image range -> continue
                if (y < 0 || x < 0 || y >= b.length || x >= b[0].length) {
                    continue;
                }
                
                //if counted -1-1, 0,0, 1,1; -1, 1 etc. (diagonally or equal to
                //the calling field)
                if (Math.abs(x - _p.x) == Math.abs(y - _p.y)) {
                    continue;
                }
                
                //not out of image range, can check b
                if (b[y][x] == -1) {
                    
                    if ((y > b.length - 1 || x > b[0].length - 1
                        || y <= 0 || x <= 0)) {
                        return null;
                    } else {
                        b [y][x] = 1;
                        b = getUmgebung(new Point(x, y), b);
                        if (b == null) {
                            return null;
                        }
                    }
                }
            }
        }
        return b;
    }

    
    
    /**
     * Paints a line in a special color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
     * @param _field a two dimensional boolean painting map
     * @param _clr the color in which is painted.
     * 
     * @param _thickness the thickness of printing the line
     * 
     * @param _pStart the location of start pixel
     *           painted.
     * @param _pEnd the location of end pixel painted.
     * 
     * @param _paint whether to paint or just to return the length
     * 
     * @return length of the curve.
     */
    private static PolygonReturn paintLine(
            final BufferedImage _bi, 
            final byte [][] _field,
            final Color _clr,
            final int _thickness,
            final Point _pStart,
            final Point _pEnd,
            final boolean _paint) {

        int dX = _pStart.x - _pEnd.x;
        int dY = _pStart.y - _pEnd.y;
        int maxDistance = Math.max(Math.max(Math.abs(dX), Math.abs(dY)), 1);

       // System.out.println(dX + "x" + dY + "y" + maxDistance + "max");
        //the field, inside true if painted, otherwise false.
        byte[][] field = _field;
        
        //f: [0, maxDistance]-> (x                     ,                     y)
        //        (t)       |-> (startX + dX * t / max , startY + dY * t / max)
        for (int t = 0; t <= maxDistance && _paint; t++) {
            field = paintPoint(_bi, field, _clr, _thickness, 
                    new Point(_pStart.x - dX * t / maxDistance,
                              _pStart.y - dY * t / maxDistance));
        }
        
        return new PolygonReturn(maxDistance + 1, field);
    }
    
    

    /**
     * Paints a point in a special color to a bufferedImage.
     * The coordinates of the point are in the middle of the
     * point which is finally printed (thickness)
     * 
     * @param _bi the buffered image on which is painted
     * @param _field a two dimensional boolean painting map
     * @param _clr the color in which is painted.
     * 
     * @param _thickness the thickness of the point
     * 
     * @param _p the painted point.
     * @return the two dimensional boolean painting map
     */
    private static byte [][] paintPoint(
            final BufferedImage _bi, 
            final byte [][] _field,
            final Color _clr,
            final int _thickness,
            final Point _p) {
        
        //interrupt if error in field size
        if (_field.length != _bi.getWidth() 
                || _field[0].length != _bi.getHeight()) {
            Status.getLogger().warning("problem");
            throw new ThreadDeath();
        }
        
        //the thickness
        for (int y = _p.y - _thickness / 2;
                y < _p.y + _thickness / 2 + _thickness % 2; y++) {

            for (int x = _p.x - _thickness / 2;
                    x < _p.x + _thickness / 2 + _thickness % 2; x++) {
                
//                if (y == _p.y && x == _p.x) {
//                    continue;
//                }
                
                //if in range paint to bufferedImage and fill
                //the boolean array.
                if (y < _bi.getHeight() && y >= 0
                        && x < _bi.getWidth() && x >= 0) {
                    
                    _bi.setRGB(x, y, _clr.getRGB());
                    
                    if (_field != null) {
                        _field[x][y] = OCCUPIED;
                    } else {
                        System.out.println("problem2");
                    }
                } 
//                else
//                    System.out.println(x + ".." + y);
            }
        }
        
        return _field;
    }
    
    
    /**
     * Paint point.
     * @param _bi the bufferedImage
     * @param _x the x coordinate of point
     * @param _y the y coordinate of point
     * @param _rgb the color
     */
    public static void paintScilentPoint(final BufferedImage _bi, 
            final int _x, final int _y, final int _rgb) {

        //create integer values
        int x = _x;
        int y = _y;
        
        //transform x coordinate.
        if (x <= 0) {
            x = 0;
        } else if (x >= _bi.getWidth()) {
            x = _bi.getWidth() - 1;
        }
        
        //transform y coordinate
        if (y <= 0) {
            y = 0;
        } else if (y >= _bi.getHeight()) {
            y = _bi.getHeight() - 1;
        }
        
        //transform bufferedImage.
        _bi.setRGB(x, y, _rgb);
        
    }
    

    /**
     * Main method for testing purpose.
     * 
     * @param _args the standard arguments (ignored)
     */
    public static void main(final String[] _args) {

        /**
         * Constants.
         */
        final int one = 1, two = 2, three = 3, four = 4, five = 5;
        
        Point [] p = new Point[2 + 2 + 1];
        p[0] = new Point(two * five, 0);
        p[1] = new Point(one, four * five);
        p[2] = new Point(four * five, two * five);
        p[three] = new Point(one, two * five);
        p[four] = new Point(four * five, four * five);

        Point [] p2 = new Point[2 + 2];
        p2[0] = new Point(one, one);
        p2[one] = new Point(one, four * five);
        p2[2] = new Point(four * five * two, four * five);
        p2[three] = new Point(four * five * two, one);

        PolygonReturn pr = PaintBI.paintPolygonN(
                new BufferedImage(four * five, four * five * two,
                        BufferedImage.TYPE_INT_ARGB), 
                        Color.black, 1, p, true);
        drawPaintBI(pr.getField());
        
        System.out.println();
        
        pr = PaintBI.fillPolygonN(
                new BufferedImage(four * five, four * five * two,
                        BufferedImage.TYPE_INT_ARGB), 
                        Color.black, p);
        drawPaintBI(pr.getField());
        
        System.out.println();

//        pr = PaintBI.fillRectangleQuick(
//                new BufferedImage(four * five, four * five * two,
//                        BufferedImage.TYPE_INT_ARGB), 
//                        Color.black, new Rectangle(
//                                five, five, five * two, five));
        drawPaintBI(pr.getField());

        System.out.println();
        
        pr = PaintBI.paintPolygonN(
                new BufferedImage(four * five, four * five * two,
                        BufferedImage.TYPE_INT_ARGB), 
                        Color.black, 1, p2, true);
        drawPaintBI(pr.getField());
        
        System.out.println();
        
        pr = PaintBI.fillPolygonN(
                new BufferedImage(four * five, four * five * two,
                        BufferedImage.TYPE_INT_ARGB), 
                        Color.black, p2);
        drawPaintBI(pr.getField());
        
        System.out.println();


//        boolean [][] umgebung2 = getUmgebung(
//                new Point (14,5), pr.getField());
//      drawPaintBI(new PolygonReturn(0, umgebung2));
    }
    
    
    
    /**
     * paint map to console output for testing purpose.
     * 
     * @param _pbi the stuff returned from method.
     */
    public static void drawPaintBI(final byte[][] _pbi) {
        
        
        System.out.print("  \t");
        for (int x = 1; x <= _pbi[0].length; x++) {

            switch ((x + "").length()) {
            case 1:
                System.out.print("| " + x);
                break;
            case 2:
                System.out.print("|" + x);
                break;
            default:
                break;
            }
        }
        System.out.println("");
        for (int y = 0; y < _pbi.length; y++) {
            System.out.print("  \t");
            for (int x = 0; x < _pbi[0].length; x++) {
                System.out.print("+--");
            }
            System.out.print("+");
            System.out.println();
            System.out.print((y + 1) + "\t");
        for (int x = 0; x < _pbi[0].length; x++) {
                
            String str = "|t ";
            if (_pbi[y][x] == OCCUPIED
                    || _pbi[y][x] != BORDER) {

                str = "|t ";
            } else {

                str = "|  ";
            }
            if (_pbi[y][x] == 0) {
            	str = "|" + "  ";
            } else {

                str = "|" + _pbi[y][x] + " ";	
            }
                System.out.print(str);
        }
        System.out.print("|");
        System.out.println();
        }
    }
}


/**
 * Output-class from paint methods which contains the length
 * of the curve that is painted and the field (containing
 * booleans for being able to paint them to console or
 * for working with it.
 * 
 * @author Julius Huelsmann
 */
class PolygonReturn {

    
    /**
     * The length of the curve.
     */
    private int lengthOrigin;
    
    /**
     * A map of the fields that are painted in BufferedImage.
     * 1       -        occupied
     * 0       -        no occupied
     * -1      -        not visited
     */
    private byte [][] field;
    
    
    /**
     * Returns point array with length of two. Its elements are
     * used for filling the polygon. Usually one of the two
     * points is outside and the other is inside the form. 
     * 
     * Exceptions: 
     *      lines, there null is returned or two outside points
     *      loops: it may occur that both points are inside.
     *      
     * @param _current how many results should be jumped.
     * 
     * @return a point with the length of two.
     */
    public Point[] findCenterPoints(final int _current) {

        int found = 0;
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                
                
                if (field[y][x] == 1) {
                    

                    for (int sy = -1; sy <= 0; sy++) {
                        for (int sx = -1; sx <= 0; sx++) {
                            if (sy == 0 && sx == 0) {
                                continue;
                            }

                            if (x - sx >= field[0].length 
                                    || y - sy >= field.length
                                    || x - sx < 0 || y - sy < 0) {
                                continue;
                            }
                            if (x + sx >= field[0].length 
                                    || y + sy >= field.length
                                    || x + sx < 0 || y + sy < 0) {
                                continue;
                            }
                            if (field [y + sy]  [x + sx] != -1
                                    && field [y - sy][x - sx] != -1) {
                                Point[] p = new Point[2];
                                p[0] = new Point(x + sx, y + sy);
                                p[1] = new Point(x - sx, y - sy);
                                if (found >= _current) {
                                    return p;
                                }
                                
                                //increase the found counter
                                found++;
                            }
                        }
                    }
                }
            }
        }
        

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                
                
                if (field[y][x] != -1) {
                    Point[] p = new Point[1];
                    p[0] = new Point(x, y);
                    if (found >= _current) {
                        return p;
                    }
                }
            }
        }
        
        //if no two points are found return a free point. (it may be possible 
        //that the form is a rectangle filling the entire screen)
        return null;
        
    }
    
    
    /**
     * Whether something changed during the last time the process has been done.
     */
    private boolean startchange = false;
    
    /**
     * @param _bi the BufferedImage for painting test stuff.
     */
    public void schwabbel(final BufferedImage _bi) {
        startchange = true;
        while (startchange) {

            startchange = false;
            start(_bi);   
        }

//        Page.getInstance().getJlbl_background().setIcon(new ImageIcon(_bi));
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    
    }
    
    /**
     * Check whether value is outside selection.
     * @param _x coordinate
     * @param _y coordinate
     * @return whether value is outside selection.
     */
    private boolean check(final int _x, final int _y) {

        return (_x < 0 || _y < 0 
                || _x >= field.length || _y >= field[_x].length
                || field[_x][_y] == PaintBI.BORDER);
            
    }
        
    
    /**
     * Start one process.
     * @param _bi the BufferedImage for painting test stuff.
     */
    private void start(final BufferedImage _bi) {

        for (int x = 0; x < field.length; x++) { 
            for (int y = 0; y < field[x].length; y++) { 
                if (field[x][y] == PaintBI.FREE) {

                    if (check(x - 1, y)
                            || check(x + 1, y)
                            || check(x, y - 1)
                            || check(x, y + 1)) {
                        field[x][y] = PaintBI.BORDER;
//                        _bi.setRGB(x, y, new Color(150, 120, 10).getRGB());
                        startchange = true;
                    }
                }
            }
        }
    }


    /**
     * Update length and field in this object. Getting the
     * new stuff from other PolygonReturn instance.
     * 
     * @param _pi the instance of PolygonReturn.
     */
    public void actualize(final PolygonReturn _pi) {
        this.lengthOrigin += _pi.getLengthOrigin() - 1;
        this.field = _pi.getField();
    }
    
    /**
     * Constructor: initializes the PolygonReturn with existing boolean
     * map and length.
     * 
     * @param _length the new length
     * @param _field the field
     */
    public PolygonReturn(final int _length, final byte [][] _field) {
        this.lengthOrigin = _length;
        this.field = _field;
    }
    
    
    /**
     * Constructor: initializes a new PolygonReturn.
     * 
     * @param _width the size of the field
     * @param _height the size of the field
     */
    public PolygonReturn(final int _width, 
            final int _height) {
        this.lengthOrigin = 0;
        this.field = new byte[_width][_height];
        for (int x = 0; x < _width; x++) {
            for (int y = 0; y < _height; y++) {
                field[x][y] = -1;
            }
        }
    }
    /**
     * @return the lengthOrigin
     */
    public int getLengthOrigin() {
        return lengthOrigin;
    }
    /**
     * @param _lengthOrigin the lengthOrigin to set
     */
    public void setLengthOrigin(final int _lengthOrigin) {
        this.lengthOrigin = _lengthOrigin;
    }
    /**
     * @return the field
     */
    public byte [][] getField() {
        
        
        byte [][] f2 = new byte[field.length][field[0].length];
        for (int y = 0; y < field.length; y++) {

            for (int x = 0; x < field[0].length; x++) {
                f2[y][x] = field[y][x];
            }
        }
        
        
        return f2;
    }
    /**
     * @param _field the field to set
     */
    public void setField(final byte [][] _field) {
        this.field = _field;
    }
    

    
}


