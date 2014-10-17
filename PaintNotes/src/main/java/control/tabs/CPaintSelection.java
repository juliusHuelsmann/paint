package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;

import model.settings.Constants;
import model.settings.Status;
import view.util.Item1Button;

/**
 *
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CPaintSelection implements MouseListener, ActionListener {

	/**
	 * the only instance of this class.
	 */
	private static CPaintSelection instance = null;

	/*
	 * saved values
	 */
	
	/**
	 * Buttons.
	 */
	private Item1Button tb_line, tb_curve, tb_magic;
	
	/**
	 * JCheckBoxes.
	 */
	private JCheckBox jcb_image, jcb_separated, jcb_whole;
	
	/**
	 * private constructor which can only be called out of
	 * this class. Realized by getInstance.
	 * 
     * @param _tb_line button
     * @param _tb_curve button
     * @param _tb_magic image button
     * @param _jcb_whole JCheckBox
     * @param _jcb_image JCheckBox
     * @param _jcb_separated JCheckBox
	 */
	private CPaintSelection(
            final JCheckBox _jcb_whole, 
            final JCheckBox _jcb_separated, final JCheckBox _jcb_image,
            final Item1Button _tb_line, final Item1Button _tb_curve,
            final Item1Button _tb_magic) {
		
	    //save values
	    this.tb_line = _tb_line;
		this.tb_curve = _tb_curve;
		this.tb_magic = _tb_magic;
		
		this.jcb_whole = _jcb_whole;
		this.jcb_separated = _jcb_separated;
		this.jcb_image = _jcb_image;

		tb_line.addActionListener(this);
		tb_curve.addActionListener(this);
		tb_magic.addActionListener(this);
		
		jcb_image.addMouseListener(this);
		jcb_separated.addMouseListener(this);
		jcb_whole.addMouseListener(this);
	}

	
	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
     * @param _tb_line button
     * @param _tb_curve button
     * @param _tb_magic image button
     * @param _jcb_whole JCheckBox
     * @param _jcb_image JCheckBox
     * @param _jcb_separated JCheckBox
	 * @return the only instance of this class.
	 */
	public static CPaintSelection getInstance(
			final JCheckBox _jcb_whole, 
			final JCheckBox _jcb_separated, final JCheckBox _jcb_image,
			final Item1Button _tb_line, final Item1Button _tb_curve,
			final Item1Button _tb_magic) {

	    //if class is not instanced yet instantiate
		if (instance == null) {
			instance = new CPaintSelection(
					_jcb_whole, _jcb_separated, _jcb_image,
					_tb_line, _tb_curve, _tb_magic);
		}
		
		//return the only instance of this class.
		return instance;
	}



	
	public void mouseClicked(final MouseEvent _event) {

		if (_event.getSource().equals(jcb_whole)) {

			//set index selection
			Status.setIndexSelection(Constants
			        .CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM);
			
			//deactivate others and activate itself
			deactivate();
			jcb_whole.setSelected(true);
			
		} else if (_event.getSource().equals(jcb_separated)) {

			//set index selection
		    Status.setIndexSelection(
			        Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM);
			
			//deactivate others and activate itself
			deactivate();
			jcb_separated.setSelected(true);
			
		} else if (_event.getSource().equals(jcb_image)) {

			//set index selection
		    Status.setIndexSelection(
			        Constants.CONTROL_PAINTING_SELECTION_INDEX_IMAGE);
			
			//deactivate others and activate itself
			deactivate();
			jcb_image.setSelected(true);
		}
	}

	
	/**
	 * Deactivate each JChecBbox.
	 */
	private void deactivate() {
		jcb_whole.setSelected(false);
		jcb_separated.setSelected(false);
		jcb_image.setSelected(false);
	}


	public void mouseEntered(final MouseEvent _event) { }


	public void mouseExited(final MouseEvent _event) { }

	public void mousePressed(final MouseEvent _event) { }

	public void mouseReleased(final MouseEvent _event) { }

    public void actionPerformed(final ActionEvent _event) {
        if (_event.getSource().equals(tb_curve.getActionCause())) {

            //set index
            Status.setIndexOperation(
                    Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE);
            
        } else if (_event.getSource().equals(tb_line.getActionCause())) {
 
            //set index
            Status.setIndexOperation(
                    Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE);
            
        } else if (_event.getSource().equals(tb_magic.getActionCause())) {

            //set index
            Status.setIndexOperation(
                    Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC);
        }      
    }
}
