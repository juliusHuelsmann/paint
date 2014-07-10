package model.objects.painting;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


/**
 * Utility class for painting stuff to a buffered image.
 * @author Julius Huelsmann
 *
 */
public final class PaintBI {

    /**
     * Empty utility class constructor.
     */
    private PaintBI() {
        
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
     * @return length of the curve.
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
     *           
     *       p4
     * 
     */
    public static void fillPolygonN(
            final BufferedImage _bi, 
            final Color _clr,
            final Point[] _p) {
        PolygonReturn polygonReturn = paintPolygonN(_bi, _clr, 1, _p, true);
        
        //das hier so oft wie keine center points mehr gefunden werden.
        Point[] p = polygonReturn.findCenterPoints();
        if(p.length != 2){

            return;
        } else {

            boolean [][] umgebung1 = getUmgebung(p[0], polygonReturn.getField());
            if(umgebung1 == null) {

                boolean [][] umgebung2 = getUmgebung(p[1], polygonReturn.getField());
                if(umgebung2 == null){
                    System.out.println("error");
                    return;
                } else {
                    for (int y = 0; y < umgebung2.length; y++){

                        for (int x = 0; x < umgebung2[y].length; x++){
                            if(umgebung2[x][y])
                                paintPoint(_bi, null, _clr, 1, new Point(x,y));
                        }
                    }
                }
            } else {

                for (int y = 0; y < umgebung1.length; y++) {

                    for (int x = 0; x < umgebung1[y].length; x++) {
                        if (umgebung1[x][y]) {
                            paintPoint(_bi, null, _clr, 1, new Point(x, y));
                        }
                    }
                }
            }
        }
        
        
    }
    
    
    private static boolean[][] getUmgebung(Point _p, boolean[][] _b){
        
        boolean [][] b = _b;
        
        for (int x = _p.x - 1; x <= _p.x + 1; x++) {
            for (int y = _p.x - 1; y <= _p.x + 1; y++) {
                
                //out of image range -> return null
                if (y > b.length || x > b[0].length) {
                    return null;
                }
                
                //not out of image range, can check b
                if (!b[x][y] && (y > b.length - 1 || x > b[0].length - 1
                        || y <= 0 || x <= 0)) {
                    return null;
                } else if (!b[x][y]) {
                    b = getUmgebung(new Point(x, y), b);
                }
                
            }
        }
        return b;
    }

    
    
    /**
     * Paints a line in a special color to a bufferedImage.
     * 
     * @param _bi the buffered image on which is painted
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
            final boolean [][] _field,
            final Color _clr,
            final int _thickness,
            final Point _pStart,
            final Point _pEnd,
            final boolean _paint) {

        int dX = _pStart.x - _pEnd.x;
        int dY = _pStart.y - _pEnd.y;
        int maxDistance = Math.max(Math.max(dX, dY), 1);
        
        //the field, inside true if painted, otherwise false.
        boolean[][] field = _field;
        
        //f: [0, maxDistance]-> (x                     ,                     y)
        //        (t)       |-> (startX + dX * t / max , startY + dY * t / max)
        for (int t = 0; t < maxDistance && _paint; t++) {
            field = paintPoint(_bi, field, _clr, _thickness, 
                    new Point(_pStart.x + dX * t / maxDistance,
                              _pStart.y + dY * t / maxDistance));
        }
        
        return new PolygonReturn(maxDistance + 1, field);
    }
    
    

    /**
     * Paints a point in a special color to a bufferedImage.
     * The coordinates of the point are in the middle of the
     * point which is finally printed (thickness)
     * 
     * @param _bi the buffered image on which is painted
     * @param _clr the color in which is painted.
     * 
     * @param _thickness the thickness of the point
     * 
     * @param _p the painted point.
     */
    private static boolean [][] paintPoint(
            final BufferedImage _bi, 
            final boolean [][] _field,
            final Color _clr,
            final int _thickness,
            final Point _p) {
        
        //interrupt if error in field size
        if (_field.length != _bi.getWidth() 
                || _field.length != _bi.getHeight()) {
            
            throw new ThreadDeath();
            //("Fehlerhafte arrayuebergabe, " +
            	//	"programmierfehler");
        }

        //the thickness
        for (int x = _p.x - _thickness / 2 - _thickness % 2; 
                x < _p.x + _thickness / 2; x++) {

            for (int y = _p.y - _thickness / 2 - _thickness % 2; 
                    y < _p.y + _thickness / 2; y++) {
                
                if(x == _p.x && y == _p.y)
                    continue;
                
                //if in range paint to bufferedImage and fill
                //the boolean array.
                if (y < _bi.getHeight() && y >= 0
                        && x < _bi.getWidth() && x >= 0) {
                    
                    _bi.setRGB(x, y, _clr.getRGB());
                    
                    if (_field != null)
                    _field[x][y] = true;
                }
            }
        }
        
        return _field;
    }
    
    
}
class PolygonReturn {

    private int lengthOrigin;
    private boolean [][] field;
    
    public Point[] findCenterPoints(){

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (field[x][y]) {
                    

                    for (int sy = -1; sy <= 0; sy++) {
                        for (int sx = -1; sx <= 0; sx++) {
                            if(sy == 0 && sx == 0)
                                continue;
                            
                            if(x - sx >= field.length || y - sy >= field.length
                                    || x - sx < 0 || y - sy < 0) {
                                continue;
                            }
                            if (!field [x + sx][y+sy] && !field [x - sx][y-sy]) {
                                Point[] p = new Point[2];
                                p[0] = new Point (x+sx, y+sy);
                                p[1] = new Point (x-sx, y-sy);
                                return p;
                            }
                        }
                    }
                }
            }
        }
        

            return null;
        
    }
    public void actualize(PolygonReturn _pi){
        this.lengthOrigin += _pi.getLengthOrigin() - 1;
        this.field = _pi.getField();
    }
    public PolygonReturn (int _length, boolean [][] _field){
        this.lengthOrigin = _length;
        this.field = _field;
    }
    
    public PolygonReturn (int _width, int _height){
        this.lengthOrigin = 0;
        this.field = new boolean[_width][_height];
        for (int x = 0; x < _width; x ++){
            for (int y = 0; y < _height; y++) {
                field[x][y] = false;
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
     * @param lengthOrigin the lengthOrigin to set
     */
    public void setLengthOrigin(int lengthOrigin) {
        this.lengthOrigin = lengthOrigin;
    }
    /**
     * @return the field
     */
    public boolean [][] getField() {
        return field;
    }
    /**
     * @param field the field to set
     */
    public void setField(boolean [][] field) {
        this.field = field;
    }
}


