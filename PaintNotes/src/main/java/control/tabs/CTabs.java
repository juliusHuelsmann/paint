package control.tabs;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import view.forms.Page;
import model.settings.ViewSettings;
import control.ControlPaint;
import control.interfaces.MoveEvent;
import control.interfaces.TabbedListener;

/**
 * Controller class for Tabs. Deals with the opening and the closing
 * of the entire tab pane (not opening another tab).
 * 
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public class CTabs implements TabbedListener {

	
	/**
	 * Instance of the main controller.
	 */
	private ControlPaint cp;
	
	/**
	 * Constructor: saves the instance of the main controller
	 * 				class which offers accessibility to other
	 * 				controller, model and view classes.
	 * 
	 * @param _cp 	instance of the main controller class which
	 * 				is saved
	 */
	public CTabs(final ControlPaint _cp) {
		this.cp = _cp;
	}
	

	/**
	 * 
	 */
	public void closeListener() {
		
		double timetoclose0 = System.currentTimeMillis();
		Page page = cp.getView().getPage();
		
		
		//set new bounds in settings
		ViewSettings.setView_bounds_page(
				new Rectangle(ViewSettings.getView_bounds_page()));

		
		//re-initialize the image with the correct size

		cp.getControlPic().setBi_background(new BufferedImage(
				ViewSettings.getView_bounds_page().getSize().width,
				ViewSettings.getView_bounds_page().getSize().height,
				BufferedImage.TYPE_INT_ARGB));
		cp.getControlPic().setBi(new BufferedImage(
				ViewSettings.getView_bounds_page().getSize().width,
				ViewSettings.getView_bounds_page().getSize().height,
				BufferedImage.TYPE_INT_ARGB));
        

		//apply new size in view
        //takes some time and repaints the image.
		//TODO: better algorithm for opening. 
		//save the previously displayed image (out of BufferedImage)
		//and write it into the new (greater one). Afterwards (re)paint
		//the sub image that is new.
        page.setSize(
        		(int) ViewSettings.getView_bounds_page().getWidth(),
                (int) ViewSettings.getView_bounds_page().getHeight());
        
       //time used ca. 0.8 sec

        
        // output
		double timetoclose1 = System.currentTimeMillis();
		final int divisorToSeconds = 100;
		System.out.println(getClass() 
				+ "time passed" + (timetoclose1 - timetoclose0) 
				/ divisorToSeconds);
	}

	
	
	/**
	 * {@inheritDoc}
	 */
	public final void moveListener(final MoveEvent _event) {
        cp.getView().getPage().setLocation(
        		_event.getPnt_bottomLocation());
	}

	

	/**
	 * {@inheritDoc}
	 */
	public final void openListener() {

		Page page = cp.getView().getPage();

		//set new bounds in settings
		ViewSettings.setView_bounds_page(
				new Rectangle(ViewSettings.getView_bounds_page_open()));

		//re-initialize the image with the correct size
		cp.getControlPic().setBi_background(new BufferedImage(
				ViewSettings.getView_bounds_page().getSize().width,
				ViewSettings.getView_bounds_page().getSize().height,
				BufferedImage.TYPE_INT_ARGB));
		cp.getControlPic().setBi(new BufferedImage(
				ViewSettings.getView_bounds_page().getSize().width,
				ViewSettings.getView_bounds_page().getSize().height,
				BufferedImage.TYPE_INT_ARGB));

        
		//apply new size in view
        //takes some time and repaints the image.
		//TODO: better algorithm for opening. 
		//save the previously displayed image (out of BufferedImage)
		//and write the necessary stuff it into the new (smaller one)
        page.setSize(
        		(int) ViewSettings.getView_bounds_page().getWidth(),
                (int) ViewSettings.getView_bounds_page().getHeight());
	}
}
