package control.util.implementations;

import java.awt.event.MouseEvent;

import view.forms.Page;
import view.forms.Tabs;
import control.ControlPaint;
import control.interfaces.ActivityListener;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ScrollPaneActivityListener implements ActivityListener {

	
	/**
	 * Instance of ControlPaint.
	 */
	private ControlPaint controlPaint;
	
	
	/**
	 * Constructor: saves the ControlPaint instance.
	 * @param _controlPaint instance of ControlPaint
	 */
	public ScrollPaneActivityListener(final ControlPaint _controlPaint) {
		this.controlPaint = _controlPaint;
	}
	

	/**
	 * simple private getter method catching errors.
	 * @return instance of page fetched by ControlPaint
	 */
	private Page getPage() {
		return controlPaint.getView().getPage();
	}

	
	/**
	 * simple private getter method catching errors.
	 * @return instance of tabs fetched by ControlPaint
	 */
	private Tabs getTabs() {
		return controlPaint.getView().getTabs();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void activityOccurred(final MouseEvent _event) {


        for (int a = 0; a < getPage().getJbtn_resize().length; a++) {
            for (int b = 0; b < getPage().getJbtn_resize()[a].length; 
                    b++) {
                getPage().getJbtn_resize()[a][b].repaint();
            }
        }
        getPage().getJlbl_selectionBG().repaint();
        getPage().getJlbl_selectionPainting().repaint();
        //close each open menu
        getTabs().closeMenues();		
	}
}
