package control.util;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.util.Item1Menu;
import view.util.Item1PenSelection;
import model.objects.painting.Picture;
import model.objects.pen.Pen;
import control.ControlPaint;
import control.tabs.ControlTabPainting;

public class CPen implements ChangeListener{


	private ControlTabPainting cp;
	private Item1PenSelection i1ps;

	/**
	 * The pen which is replaced by this. 2 or 1.
	 */
	private int penSelection = 0;
	
	private Item1Menu i1m_toSet;
	/**
	 * The pen.
	 */
	private Pen pen;
	
	public CPen(
			final ControlTabPainting _cp, 
			final Item1PenSelection _i1ps,
			final Item1Menu _i1m_toSet,
			final Pen _pen) {
		this.cp = _cp;
		this.i1ps = _i1ps;
		this.pen = _pen;
		this.i1m_toSet = _i1m_toSet;
	}

	public void stateChanged(final ChangeEvent _event) {

		if (_event.getSource() instanceof JSlider) {

			//fetch the thickness from JSlider
			final int thickness = ((JSlider) _event.getSource()).getValue();
			
			//set text and apply the thickness inside the model pen
			i1ps.getJlbl_thickness().setText(thickness + "px");
			pen.setThickness(thickness);
			
			Picture.getInstance().userSetPen(pen, penSelection);
	        //set the image of the current pen, close the menu and
	        //reset the last open menu; thus no menu has to be closed the 
			//next time another menu is opened

			i1m_toSet.setIcon(i1ps.getImagePath());
	            
	        ControlTabPainting.applyFocus(i1ps);
		}
	}
}
