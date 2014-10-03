package model.util;

import java.awt.Point;

import model.objects.painting.PaintObjectWriting;
import model.util.list.List;


/**
 * Very unspecific utility methods.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Util {

    
    /**
     * Empty utility class constructor.
     */
    private Util() { }
    

    
    /**
     * convert list of points into array.
     * 
     * @param _ld list of points
     * @return the array
     */
    public static PaintObjectWriting[] poLsToArray(
            final List<PaintObjectWriting> _ld) {

        /*
         * Transform lists to arrays
         */
        int length = 0;
        _ld.toFirst();
        
        //it is necessary to double check behind and empty because an empty
        //list returns the length of 1 otherwise.
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            _ld.next();
            length++;
        }
        
        PaintObjectWriting [] pow = new PaintObjectWriting[length];

        _ld.toFirst();
        int index = 0;
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            pow[index] = _ld.getItem();
            _ld.next();
            index++;
        }
        
        return pow;
    }

    
    /**
     * convert list of points into array.
     * 
     * @param _ld list of points
     * @return the array
     */
    public static DPoint[] pntLsToArray(
            final List<DPoint> _ld) {

        /*
         * Transform lists to arrays
         */
        int length = 0;
        _ld.toFirst();
        
        //it is necessary to double check behind and empty because an empty
        //list returns the length of 1 otherwise.
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            _ld.next();
            length++;
        }
        
        DPoint [] pow = new DPoint[length];

        _ld.toFirst();
        int index = 0;
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            pow[index] = _ld.getItem();
            _ld.next();
            index++;
        }
        
        return pow;
    }
    
    
    /**
     * transform DPoint array to Point array.
     * @param _dp DPoint array
     * @return Point array
     */
    public static Point[] dpntToPntArray(final DPoint[] _dp) {
        Point[] pnt_return = new Point[_dp.length];
        for (int i = 0; i < _dp.length; i++) {
            pnt_return[i] = new Point((int) _dp[i].getX(), (int) _dp[i].getY());
        }
        return pnt_return;
    }
    
    
    
    /**
     * convert list of points into array.
     * 
     * @param _ld list of points
     * @return the array
     */
    private static Object[] lsToArrayD(
            @SuppressWarnings("rawtypes") final List _ld) {

        /*
         * Transform lists to arrays
         */
        int length = 0;
        _ld.toFirst();
        
        //it is necessary to double check behind and empty because an empty
        //list returns the length of 1 otherwise.
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            _ld.next();
            length++;
        }
        
        Object [] pow = new Object[length];

        _ld.toFirst();
        int index = 0;
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            pow[index] = _ld.getItem();
            _ld.next();
            index++;
        }
        
        return pow;
    }
}
