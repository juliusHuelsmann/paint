package control.debug;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import control.ControlPainting;
import start.Start;
import model.objects.painting.Picture;
import model.objects.pen.normal.Pencil;
import model.settings.Constants;
import model.settings.Status;
import model.util.DPoint;
import model.util.Util;
import model.util.list.List;

/**
 * Class Bug contains methods for solving bugs that occur by being able to 
 * reconstruct them and debugging each action step-by step using the 
 * IDE debugger.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Bug {
	
	
	
	/**
	 * Bug constructor.
	 * @param _loadingPath the path of the bug file that is loaded.
	 */
	private Bug(final String _loadingPath) {
		
		//open graphical user interface by calling the original main method.
		Start.main(new String[0]);
		
		//load bug and display it at loaded interface.
		loadBug(_loadingPath);
	}
	
	
	/**
	 * Load Bug from hard drive.
	 * @param _loadingPath the path of the bug file that is loaded.
	 */
	private void loadBug(String _loadingPath) {
		List <String> ls_strg = Util.loadTextFile(_loadingPath);
		
		ls_strg.toFirst();
		while (!ls_strg.isBehind() && !ls_strg.isEmpty()) {
			
			String cString = ls_strg.getItem();
			if (cString.equals("Line")){

				Picture.getInstance().changePen(new Pencil(
						Constants.PEN_ID_LINES, 1, Color.blue));
				Picture.getInstance().addPaintObject();

				DPoint p = new DPoint();
				while (!ls_strg.isBehind() 
						&& !ls_strg.isEmpty()
						&& p != null) {

					ls_strg.next();

					if (ls_strg.getItem() == null
							|| ls_strg.getItem().equals("")
							|| ls_strg.getItem().startsWith("//")) {
						ls_strg.next();
						continue;
					} else {
						
						p = isPoint(ls_strg.getItem());
						if (p != null) {

							Picture.getInstance().changePaintObject(p);
						} else {
							ls_strg.previous();
						}
					}
				}
				
				Picture.getInstance().finish();
			} else if (cString.contains("Rectangle")){



				Rectangle p = new Rectangle();
				while (!ls_strg.isBehind() 
						&& !ls_strg.isEmpty()
						&& p != null) {

					ls_strg.next();
					System.out.println("ne");

					if (ls_strg.getItem() == null
							|| ls_strg.getItem().equals("")
							|| ls_strg.getItem().startsWith("//")) {
						ls_strg.next();
						System.out.println("hier");
						continue;
					} else {
						
						p = isRect(ls_strg.getItem());
						System.out.println(p);
						if (p != null) {

							ControlPainting.getInstance().mr_sel_line_destroy(p);
						}
					}
				}
				
			} else if (
					!cString.equals("")
					&& !cString.startsWith("//")) {
				
				Status.getLogger().severe("error reading bug file! Line: " 
				+ cString);
				ls_strg.next();
				
			} else {
				ls_strg.next();
			}
			
		}
		
	}

	private DPoint isPoint(String _line) {
		
		DPoint r = new DPoint();
		for (int i = 0; i < _line.length(); i++) {
			
			if (_line.charAt(i) == ' ') {
				try{
					r.setX(Double.parseDouble(_line.substring(0, i)));
					r.setY(Double.parseDouble(_line.substring(i)));
					return r;
				} catch(Exception e) {
					return null;
				}
			} 
		}
		return null;
	}

	private Rectangle isRect(String _line) {
		
		int i1 = -1;
		int i2 = -1;
		int i3 = -1;
		Rectangle r = new Rectangle();
		for (int i = 0; i < _line.length(); i++) {
			
			if (_line.charAt(i) == ' ') {
				if (i1 == -1) {
					i1 = i;
				} else if (i2 == -1) {
					i2 = i;
				} else if (i3 == -1) {
					i3 = i;
				} else {
					Status.getLogger().warning("Trailing spaces?");
				}
			} 
		}
		
		if (i1 != -1 && i2 != -1 && i3 != -1) {

			try{
				r.x = (int) (Double.parseDouble(_line.substring(0, i1)));
				r.y = (int) (Double.parseDouble(_line.substring(i1, i2)));
				r.width = (int) (Double.parseDouble(_line.substring(i2, i3)));
				r.height = (int) (Double.parseDouble(_line.substring(i3)));
				return r;
			} catch(Exception e) {
				return null;
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Alternative main method.
	 * @param _args the main arguments
	 */
	public static void main(String [] _args) {
		final String bugFile = "2014_12_23.bug";
		new Bug(bugFile);
	}
}