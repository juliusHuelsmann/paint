package control.forms.minorImportance;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.ControlPaint;
import view.util.mega.MButton;

public class InfoSelection implements ActionListener {

	
	private MButton jbtn_delete, jbtn_resize;
	
	private ControlPaint cp;
	
	public InfoSelection(final ControlPaint _cp) {
		jbtn_delete = new MButton("Delete");
		jbtn_delete.setOpaque(false);
		jbtn_delete.addActionListener(this);
		jbtn_delete.setContentAreaFilled(false);
		jbtn_resize = new MButton("Resize");
		jbtn_resize.setOpaque(false);
		jbtn_resize.addActionListener(this);
		jbtn_resize.setContentAreaFilled(false);
		this.cp = _cp;
	}

	
	public void show(final Point _pnt_location) {
		cp.getView().getInfo_form().applyButtons(
				new MButton[] {jbtn_delete,  jbtn_resize},
				_pnt_location);
	}
	
	
	public void actionPerformed(final ActionEvent _event) {
		if (_event.getSource().equals(jbtn_delete)) {

			cp.getPicture().deleteSelected(cp.getView().getTabs().getTab_debug(),
					cp.getcTabSelection());
			cp.getControlPic().releaseSelected();
			cp.getView().getInfo_form().setVisible(false);
		} else if (_event.getSource().equals(jbtn_resize)) {
			
		}
	}
}
