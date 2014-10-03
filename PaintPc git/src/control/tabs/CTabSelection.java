//package declaration
package control.tabs;

//import declarations
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.objects.painting.PaintObject;
import model.objects.painting.PaintObjectWriting;
import model.objects.painting.Picture;
import model.settings.Constants;
import view.forms.tabs.Selection;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CTabSelection implements ActionListener {

    /**
     * The only instance of this class.
     */
    private static CTabSelection instance;
    
    /**
     * The penID and the color of the currently selected item.
     */
    private int selectionPenID = -1, selectionColor = -1;
    
    /**
     * Empty utility class constructor.
     */
    private CTabSelection() { }
    
    /**
     * {@inheritDoc}
     */
    @Override public void actionPerformed(final ActionEvent _event) {

        Selection s = Selection.getInstance();
        if (_event.getSource().equals(s.getJcb_points())) {

            this.selectionPenID = Constants.PEN_ID_POINT;
            selectPenOp(Constants.PEN_ID_POINT);
            
        } else if (_event.getSource().equals(s.getJcb_line())) {

            this.selectionPenID = Constants.PEN_ID_LINES;
            selectPenOp(Constants.PEN_ID_LINES);
            
        } else if (_event.getSource().equals(s.getJcb_maths())) {

            this.selectionPenID = Constants.PEN_ID_MATHS;
            selectPenOp(Constants.PEN_ID_MATHS);
        } else {
            for (int i = 0; i < s.getJbtn_colors().length; i++) {

                if (_event.getSource().equals(s.getJbtn_colors()[i])) {
                    
                    this.selectionColor = s.getJbtn_colors()[i].getBackground()
                            .getRGB();
                    setColor(new Color(selectionColor));
                    Selection.getInstance().getTb_color().setBackground(
                            new Color(selectionColor));
                    Picture.getInstance().paintSelected();
                    activateColor();
                }
            }
        }
    }
    
    
    
    /**
     * select penOperation.
     * @param _id_operation the pen operation to select.
     */
    private static synchronized void selectPenOp(final int _id_operation) {
        Selection s = Selection.getInstance();
        switch (_id_operation) {
        case Constants.PEN_ID_POINT:

            s.getJcb_points().setSelected(true);
            s.getJcb_maths().setSelected(false);
            s.getJcb_line().setSelected(false);
            setPen(Constants.PEN_ID_POINT);
            Picture.getInstance().paintSelected();
            activatePen();
        
            break;
        case Constants.PEN_ID_LINES:
            
            s.getJcb_line().setSelected(true);
            s.getJcb_maths().setSelected(false);
            s.getJcb_points().setSelected(false);
            setPen(Constants.PEN_ID_LINES);
            Picture.getInstance().paintSelected();
            activatePen();
        
            break;
        case Constants.PEN_ID_MATHS:
            
            s.getJcb_maths().setSelected(true);
            s.getJcb_line().setSelected(false);
            s.getJcb_points().setSelected(false);
            setPen(Constants.PEN_ID_MATHS);
            Picture.getInstance().paintSelected();
            activatePen();
        
            break;
        default:

            s.getJcb_maths().setSelected(false);
            s.getJcb_line().setSelected(false);
            s.getJcb_points().setSelected(false);
            
            break;
        }
    }

    /**
     * sets operation and adapts graphical user interface but does not
     * set the operation to each selected item.
     * @param _id_operation the pen operation to select.
     */
    private static synchronized void showPenOp(final int _id_operation) {
        Selection s = Selection.getInstance();
        switch (_id_operation) {
        case Constants.PEN_ID_POINT:

            s.getJcb_points().setSelected(true);
            s.getJcb_maths().setSelected(false);
            s.getJcb_line().setSelected(false);
            Picture.getInstance().paintSelected();
        
            break;
        case Constants.PEN_ID_LINES:
            
            s.getJcb_line().setSelected(true);
            s.getJcb_maths().setSelected(false);
            s.getJcb_points().setSelected(false);
            Picture.getInstance().paintSelected();
        
            break;
        case Constants.PEN_ID_MATHS:
            
            s.getJcb_maths().setSelected(true);
            s.getJcb_line().setSelected(false);
            s.getJcb_points().setSelected(false);
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
     * Change the selection painting settings.
     * @param _hardChange whether it is the first element that is inserted 
     * into the selected list and thus the method does not have to check whether
     * all previous items had the same settings.
     * @param _penId the id of the pen
     * @param _color the color
     */
    public void change(final boolean _hardChange, 
            final int _penId, final int _color) {
        
        if (_hardChange) {

            activatePen();
            activateColor();
            
            //save values
            this.selectionPenID = _penId;
            this.selectionColor = _color;
            
            //show pen and color at graphical user interface.
            showPenOp(_penId);
            Selection.getInstance().getTb_color().setBackground(
                    new Color(_color));
        } else {
            
            if (selectionPenID != _penId) {
                selectionPenID = -1;
            } else {

                activatePen();
            }
            showPenOp(selectionPenID);

            if (selectionColor != _color) {
                selectionColor = -1;
                deactivateColor();
            } else {

                activateColor();
                Selection.getInstance().getTb_color().setBackground(
                        new Color(selectionColor));
            }
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
        getInstance().selectionPenID = -1;
        getInstance().selectionColor = -1;
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
     * Deactivate to perform operation on selected items (e.g. because there
     * is no item that exists or there are no suitable operations for special
     * kind of PaintItem)
     */
    private static synchronized void deactivatePen() {

        Selection s = Selection.getInstance();
        s.getJcb_maths().setEnabled(false);
        s.getJcb_line().setEnabled(false);
        s.getJcb_points().setEnabled(false);
        getInstance().selectionPenID = -1;
    }
    
    
    /**
     * Activate to perform operation on selected items.
     */
    private static synchronized void activatePen() {

        Selection s = Selection.getInstance();
        s.getJcb_maths().setEnabled(true);
        s.getJcb_line().setEnabled(true);
        s.getJcb_points().setEnabled(true);
    }

    /**
     * Deactivate to perform operation on selected items (e.g. because there
     * is no item that exists or there are no suitable operations for special
     * kind of PaintItem)
     */
    private static synchronized void deactivateColor() {

        Selection s = Selection.getInstance();
        s.getTb_color().setVisible(false);
    }
    
    
    /**
     * Activate to perform operation on selected items.
     */
    private static synchronized void activateColor() {

        Selection s = Selection.getInstance();
        s.getTb_color().setVisible(true);
    }
    
    /**
     * getter method for only instance of this class.
     * @return the only instance of CChangeSelection
     */
    public static CTabSelection getInstance() {
        
        if (instance == null) {
            instance = new CTabSelection();
        }
        
        return instance;
        
    }

    /**
     * @return the selectionPenID
     */
    public static int getSelectionPenID() {
        return getInstance().selectionPenID;
    }

    /**
     * @return the selectionColor
     */
    public static int getSelectionColor() {
        return getInstance().selectionColor;
    }

    
    
}
