//package declaration
package view.util;

//import java.awt components
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;


//import java.swing components
import javax.swing.border.Border;

//import rotatatble buttons and panels
import view.util.mega.MPanel;

//import utility class
import model.util.Util;
import view.util.mega.MLabel;


/**
 * Wrapper class which delivers a certain object to ActionListener.
 * 
 * @author Julius Huelsmann
 * @version %U%, %I%
 */
@SuppressWarnings("serial")
public class VTabButton extends MPanel {

	
	/**
	 * ButtonWrapper of which the Tab - Button mainly consists.
	 */
	private VButtonWrapper vbw;
	
	
	/**
	 * MLabel for a stroke background.
	 */
	private MLabel jlbl_stroke;
	
	
	/**
	 * Constructor: Initializes the fundamental components of this tab
	 * button.
	 * 
	 * @param _o_delivered the object passed to the button - wrapper owned
	 * by this instance of MPanel
	 */
	public VTabButton(final Object _o_delivered) {
		
		super.setLayout(null);
		super.setBorder(null);
		super.setOpaque(true);
		super.setFocusable(false);
		
		jlbl_stroke = new MLabel();
		jlbl_stroke.setOpaque(false);
		jlbl_stroke.setFocusable(false);
		super.add(jlbl_stroke);
		
		vbw = new VButtonWrapper(_o_delivered);

		vbw.setVisible(true);
		vbw.setBackground(Color.white);
		vbw.setBorder(null);
		vbw.setFocusable(false);
		vbw.setContentAreaFilled(false);
		vbw.setOpaque(true);
		super.add(vbw);
	}
	
	
	/*
	 * design methods
	 */

	/**
	 * 
	 */
	public final void stroke() {
		jlbl_stroke.setVisible(true);
		Util.getStroke(jlbl_stroke);
	}
	
	
	/**
	 * 
	 */
	public final void unstroke() {
		jlbl_stroke.setVisible(false);
	}
	
	
	
	/*
	 * overwritten methods or methods written for passing attributes directly
	 * to owned instance of VButtonWrapper
	 */
	
	
	/**
	 * set Border of VButtonWrapper.
	 * @param _b the Border
	 */
	public final void setBorder(final Border _b) {
		if (vbw != null) {
			vbw.setBorder(_b);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public final void setSize(final int _width, final int _height) {
		super.setSize(_width, _height);
		vbw.setSize(_width, _height);
		jlbl_stroke.setSize(_width, _height);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public final void setSize(final Dimension _d) {
		super.setSize(_d);
		vbw.setSize(_d);
		jlbl_stroke.setSize(_d);
		
	}
	
	/**
	 * set settings of owned VButtonWrapper.
	 * @param _control the ActionListener.
	 */
	public final void addActionListener(final ActionListener _control) {
        vbw.addActionListener(_control);
	}
	
	
	/**
	 * set settings of owned VButtonWrapper.
	 * @param _text the text
	 */
	public final void setText(final String _text) {
		vbw.setText(_text);
	}
	
	
	/**
	 * set settings of owned VButtonWrapper.
	 * @param _control the MouseListener.
	 */
	public final void addMouseListener(final MouseListener _control) {

        vbw.addMouseListener(_control);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public final void setFocusable(final boolean _focusable) {
		vbw.setFocusable(_focusable);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public final void setBackground(final Color _c) {
		if (vbw != null) {
			vbw.setBackground(_c);
		}
		super.setBackground(_c);
	}
	


	/**
	 * set settings of owned VButtonWrapper.
	 * @param _b whether content area is filled.
	 */
	public final void setContentAreaFilled(final boolean _b) {
		vbw.setContentAreaFilled(_b);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final void setOpaque(final boolean _opaque) {
		super.setOpaque(_opaque);
		if (vbw != null) {
			vbw.setOpaque(_opaque);
			
		}
	}
	
}
