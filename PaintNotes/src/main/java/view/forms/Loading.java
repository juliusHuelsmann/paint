package view.forms;

import java.awt.Color;
import java.lang.management.ClassLoadingMXBean;

import javax.rmi.CORBA.Util;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import control.forms.CLoading;

import view.util.mega.MLabel;
import view.util.mega.MPanel;

public class Loading extends MPanel {

	private JLabel jlbl_background;
	private ImageIcon[] iic;
	private String[] path = new String[] {
			"img/icn0.png", 
			"img/icn1.png", 
			"img/icn2.png", 
			"img/icn3.png",
			"img/icn4.png", 
			"img/icn5.png", 
			"img/icn6.png"};
	
	public Loading() {
		super();
		super.setBorder(new LineBorder(Color.black));
		
		jlbl_background = new MLabel();
		jlbl_background.setOpaque(false);
		super.add(jlbl_background);
		
		
	}
	
	public void setSize(int _width, int _height) {
		super.setSize(_width, _height);
		iic = new ImageIcon[path.length];
		for (int i = 0; i < iic.length; i++) {
			try{
//				iic[i] = new ImageIcon(model.util.Util.resize(path[i], _width, _height));
					
			} catch(Exception e) {
//				e.printStackTrace();
			}
		}
	}


	public void nextIcon(int _next) {
		if (iic != null && iic[_next % iic.length] != null)
		jlbl_background.setIcon(iic[_next % iic.length]);
	}
}
