//package declaration
package view.util;

//import declaration

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.border.Border;

import view.util.mega.MButton;
import view.util.mega.MPanel;
import model.settings.Error;
import model.util.Util;


/**
 * Wrapper class which delivers a certain object to ActionListener.
 * 
 * @author Julius Huelsmann
 * @version %U%, %I%
 */
@SuppressWarnings("serial")
public class VTabButton extends MPanel {

	
	private VButtonWrapper vbw;
	
	private JLabel jlbl_stroke;
	/**
	 * 
	 */
	public VTabButton(final Object _o_delivered) {
		
		super.setLayout(null);
		super.setBorder(null);
		super.setOpaque(true);
		super.setFocusable(false);
		
		jlbl_stroke = new JLabel();
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
	

	
	public void setBorder(Border _b) {
		if (vbw != null)
		vbw.setBorder(_b);
	}
	/**
	 * 
	 */
	@Override
	public void setSize(int _width, int _height){
		super.setSize(_width, _height);
		vbw.setSize(_width, _height);
		jlbl_stroke.setSize(_width, _height);
	}
	/**
	 * 
	 * @param _p
	 */
	@Override
	public void setSize(Dimension _d){
		super.setSize(_d);
		vbw.setSize(_d);
		jlbl_stroke.setSize(_d);
		
	}
	
	
	
	public VButtonWrapper getButtonWrapper() {
		return vbw;
	}
	
	public void addActionListener(ActionListener _control) {
        vbw.addActionListener(_control);
	}
	
	public void setText(String _text) {
		vbw.setText(_text);
	}
	
	public void addMouseListener(MouseListener _control) {

        vbw.addMouseListener(_control);
	}
	
	public void setFocusable(boolean _focusable) {
		vbw.setFocusable(_focusable);
	}
	
	
	public void setBackground(Color _c) {
		if (vbw != null)
		vbw.setBackground(_c);
		super.setBackground(_c);
	}
	
	public void stroke() {
		jlbl_stroke.setVisible(true);
		Util.getStroke(jlbl_stroke, 0, 0);
	}
	
	
	public void unstroke() {
		jlbl_stroke.setVisible(false);
	}


	public void setContentAreaFilled(boolean b) {
		vbw.setContentAreaFilled(b);
	}
	
	
	public void setOpaque(boolean _opaque){
		super.setOpaque(_opaque);
		if (vbw != null) {
			vbw.setOpaque(_opaque);
			
		}
	}
	
}
