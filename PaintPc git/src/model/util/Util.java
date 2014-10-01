package model.util;

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
    public static PaintObjectWriting[] lsToArray(
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
}
