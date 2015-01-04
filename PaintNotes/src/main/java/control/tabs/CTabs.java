package control.tabs;

import view.forms.Page;
import model.settings.ViewSettings;
import control.ControlPaint;
import control.interfaces.MoveEvent;
import control.interfaces.TabbedListener;

public class CTabs implements TabbedListener{

	ControlPaint cp;
	/**
	 * 
	 */
	public CTabs(final ControlPaint _cp) {
		
	}
	
	public void closeListener() {
		
		Page page = cp.getView().getPage();

        page.setSize(
                ViewSettings.getView_bounds_page_closed().getSize());
        page.getJlbl_painting().setSize(
                ViewSettings.getView_bounds_page_closed().getSize()
                .width, ViewSettings.getView_bounds_page_closed()
                .getSize().height 
                - 1);
        page.getJlbl_selectionBG().setSize(
                ViewSettings.getView_bounds_page_closed().getSize());
        page.getJlbl_selectionPainting().setSize(
                ViewSettings.getView_bounds_page_closed().getSize());
        page.getJlbl_painting().refreshPaint();
	}

	public void moveListener(MoveEvent _event) {

        cp.getView().getPage().setLocation(_event.getPnt_bottomLocation());
	}

	public void openListener() {

		Page page = cp.getView().getPage();

        //adapt image size.
        page.setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        page.getJlbl_painting().setSize(
                ViewSettings.getView_bounds_page_closed().getSize()
                .width, ViewSettings.getView_bounds_page_closed()
                .getSize().height 
                - 1);
        page.getJlbl_selectionBG().setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        page.getJlbl_selectionPainting().setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        page.getJlbl_painting().refreshPaint(); 		
	}
}
