package control.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.tabs.Paint;
import view.util.Item1Menu;
import view.util.Item1PenSelection;
import view.util.VButtonWrapper;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.State;
import control.ControlPaint;
import control.forms.CPaintStatus;
import control.forms.tabs.CTabPainting;


/**
 * Controller class for pen?.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class CPen implements ChangeListener, MouseListener {


	/**
	 * 
	 */
	private CTabPainting cp;
	
	
	/**
	 * 
	 */
	private Item1PenSelection i1ps;

	/**
	 * The pen which is replaced by this. 2 or 1.
	 */
	private int penSelection = 0;
	
	/**
	 * 
	 */
	private Item1Menu i1m_toSet;
	
	/**
	 * 
	 */
	private CPaintStatus cps;
	
	/**
	 * 
	 */
	private ControlPaint controlPaint;
	
	/**
	 * The pen.
	 */
	private Pen pen;
	
	
	/**
	 * Constructor.
	 * @param _controlPaint
	 * @param _cp
	 * @param _i1ps
	 * @param _i1m_toSet
	 * @param _pen
	 * @param _penSelection
	 * @param _cps
	 */
	public CPen(
			final ControlPaint _controlPaint,
			final CTabPainting _cp, 
			final Item1PenSelection _i1ps,
			final Item1Menu _i1m_toSet,
			final Pen _pen,
			final int _penSelection,
			final CPaintStatus _cps) {
		this. controlPaint = _controlPaint;
		this.penSelection = _penSelection;
		this.cp = _cp;
		this.i1ps = _i1ps;
		this.pen = _pen;
		this.i1m_toSet = _i1m_toSet;
		this.cps = _cps;
	}

	
	/**
	 * 
	 */
	public final void stateChanged(final ChangeEvent _event) {

		if (_event.getSource() instanceof JSlider) {

			//fetch the thickness from JSlider
			final int thickness = ((JSlider) _event.getSource()).getValue();
			
			//set text and apply the thickness inside the model pen
			i1ps.getJlbl_thickness().setText(thickness + "px");
			pen.setThickness(thickness);
			
			
			controlPaint.getPicture().userSetPen(pen, penSelection);
	        //set the image of the current pen, close the menu and
	        //reset the last open menu; thus no menu has to be closed the 
			//next time another menu is opened

			i1m_toSet.setIcon(i1ps.getImagePath());
	            
	        CTabPainting.applyFocus(i1ps);
		}
	}
	
	
    
    /**
     * mouseReleased action for changing pen.
     * @param _event the ActionEvent
     * @return whether the _event is suitable for this operation
     */
    private boolean mouseReleasedPenChange(final MouseEvent _event) {


    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Paint paint = cp.getTabPaint();

    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {
    		/*
             * the different pens in open pen menu
             */
            if (CTabPainting.isAStiftAuswahl((_event.getSource()))) {
                Item1PenSelection sa = (Item1PenSelection) ((VButtonWrapper) 
                        _event.getSource()).wrapObject();
                
                //set the image of the current pen, close the menu and
                //reset the last open menu; thus no menu has to be closed the 
                //next time another menu is opened
                if (sa.getPenSelection() == 1) {
                    
                    paint.getIt_stift1().setIcon(sa.getImagePath());
                    paint.getIt_stift1().setOpen(false);
                    State.setIndexOperation(
                            Constants.CONTROL_PAINTING_INDEX_PAINT_1);
                    cps.deactivate();
                    paint.getIt_stift1().getTb_open()
                    .setActivated(true);
                    paint.getTb_color1()
                    .setActivated(true);
                    
                    
                } else if (sa.getPenSelection() == 2) {
                    
                    paint.getIt_stift2().setIcon(sa.getImagePath());
                    paint.getIt_stift2().setOpen(false);
                    State.setIndexOperation(
                            Constants.CONTROL_PAINTING_INDEX_PAINT_2);

                    cps.deactivate();
                    paint.getIt_stift2().getTb_open()
                    .setActivated(true);
                    paint.getTb_color2()
                    .setActivated(true);
                }
                CTabPainting.applyFocus(sa);
                controlPaint.getPicture().userSetPen(pen, penSelection);
                CItem.getInstance().reset();
                
                //return that this operation has been performed
                return true;
            } else {
                
                //return that the operation has not been found yet.
                return false;
            }
    	} else {
            
            //return that the operation has not been found yet.
    		return false;
    	}
    }


	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(final MouseEvent _event) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(final MouseEvent _event) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(final MouseEvent _event) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(final MouseEvent _event) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public final void mouseReleased(final MouseEvent _event) {
		mouseReleasedPenChange(_event);		
	}
}
