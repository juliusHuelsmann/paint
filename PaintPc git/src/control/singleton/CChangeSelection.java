//package declaration
package control.singleton;

//import declarations
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.objects.painting.PaintObject;
import model.objects.painting.PaintObjectWriting;
import model.objects.painting.Picture;
import settings.Constants;
import view.forms.tabs.Selection;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CChangeSelection implements ActionListener {

    /**
     * The only instance of this class.
     */
    private static CChangeSelection instance;
    
    /**
     * Empty utility class constructor.
     */
    private CChangeSelection() { }
    
    /**
     * {@inheritDoc}
     */
    @Override public void actionPerformed(final ActionEvent _event) {

        Selection s = Selection.getInstance();
        if (_event.getSource().equals(s.getJcb_points())) {
            selectPenOp(Constants.PEN_ID_POINT);
            
        } else if (_event.getSource().equals(s.getJcb_line())) {
            selectPenOp(Constants.PEN_ID_LINES);
            
        } else if (_event.getSource().equals(s.getJcb_maths())) {
            selectPenOp(Constants.PEN_ID_MATHS);
        } else {
            for (int i = 0; i < s.getJbtn_colors().length; i++) {

                if (_event.getSource().equals(s.getJbtn_colors()[i])) {
                    setColor(s.getJbtn_colors()[i].getBackground());
                    Picture.getInstance().paintSelected();
                }
            }
        }
    }
    
    
    
    /**
     * select penOperation.
     * @param _id_operation the pen operation to select.
     */
    public static synchronized void selectPenOp(final int _id_operation) {
        Selection s = Selection.getInstance();
        switch (_id_operation) {
        case Constants.PEN_ID_POINT:

            s.getJcb_points().setSelected(true);
            s.getJcb_maths().setSelected(false);
            s.getJcb_line().setSelected(false);
            setPen(Constants.PEN_ID_POINT);
            Picture.getInstance().paintSelected();
        
            break;
        case Constants.PEN_ID_LINES:
            
            s.getJcb_line().setSelected(true);
            s.getJcb_maths().setSelected(false);
            s.getJcb_points().setSelected(false);
            setPen(Constants.PEN_ID_LINES);
            Picture.getInstance().paintSelected();
        
            break;
        case Constants.PEN_ID_MATHS:
            
            s.getJcb_maths().setSelected(true);
            s.getJcb_line().setSelected(false);
            s.getJcb_points().setSelected(false);
            setPen(Constants.PEN_ID_MATHS);
            Picture.getInstance().paintSelected();
        
            break;
        default:

            s.getJcb_maths().setSelected(false);
            s.getJcb_line().setSelected(false);
            s.getJcb_points().setSelected(false);
            
            break;
        }
    }
    

    
    /**
     * Set id for selected paintObject's pens.
     * @param _id_operation the id
     */
    private static synchronized void setPen(final int _id_operation) {
        Picture.getInstance().getLs_poSelected().toFirst();
        while (!Picture.getInstance().getLs_poSelected().isBehind() 
                && !Picture.getInstance().getLs_poSelected().isEmpty()) {
            PaintObject o = Picture.getInstance().getLs_poSelected().getItem();
            if (o instanceof PaintObjectWriting) {
                PaintObjectWriting pow = (PaintObjectWriting) o;
                pow.getPen().setId_operation(_id_operation);
            }
            Picture.getInstance().getLs_poSelected().next();
        }
    }
    
    /**
     * Set selected paintObject's color.
     * @param _clr the Color
     */
    private static synchronized void setColor(final Color _clr) {
        Picture.getInstance().getLs_poSelected().toFirst();
        while (!Picture.getInstance().getLs_poSelected().isBehind() 
                && !Picture.getInstance().getLs_poSelected().isEmpty()) {
            PaintObject o = Picture.getInstance().getLs_poSelected().getItem();
            if (o instanceof PaintObjectWriting) {
                PaintObjectWriting pow = (PaintObjectWriting) o;
                pow.getPen().setClr_foreground(new Color(_clr.getRGB()));
            }
            Picture.getInstance().getLs_poSelected().next();
        }
    }

    
    /**
     * Deactivate to perform operation on selected items (e.g. because there
     * is no item that exists or there are no suitable operations for special
     * kind of PaintItem)
     */
    public static synchronized void deactivateOp() {

        Selection s = Selection.getInstance();
        s.getJcb_maths().setEnabled(false);
        s.getJcb_line().setEnabled(false);
        s.getJcb_points().setEnabled(false);
    }
    
    
    /**
     * Activate to perform operation on selected items.
     */
    public static synchronized void activateOp() {

        Selection s = Selection.getInstance();
        s.getJcb_maths().setEnabled(true);
        s.getJcb_line().setEnabled(true);
        s.getJcb_points().setEnabled(true);
    }
    
    /**
     * getter method for only instance of this class.
     * @return the only instance of CChangeSelection
     */
    public static CChangeSelection getInstance() {
        
        if (instance == null) {
            instance = new CChangeSelection();
        }
        
        return instance;
        
    }

    
    
}
