package control.tabs;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import view.forms.Page;
import model.settings.ViewSettings;
import control.ContorlPicture;
import control.ControlPaint;
import control.interfaces.MoveEvent;
import control.interfaces.TabbedListener;

public class CTabs implements TabbedListener{

	ControlPaint cp;
	/**
	 * 
	 */
	public CTabs(final ControlPaint _cp) {
		this.cp = _cp;
	}
	
	private ContorlPicture getControlPic() {
		return cp.getControlPic();
	}
	
	public void closeListener() {
		
		Page page = cp.getView().getPage();
		ViewSettings.setView_bounds_page(
				new Rectangle(ViewSettings.getView_bounds_page()));

		
		//das bufferedimage ist nicht gross genug!
        page.getJlbl_painting().setLoc(0, 0);
        
        page.getJlbl_painting().setSize(
        		ViewSettings.getView_bounds_page().getSize().width, 
        		ViewSettings.getView_bounds_page().getSize().height - 1);

		cp.getControlPic().setBi(new BufferedImage(
				ViewSettings.getView_bounds_page().getSize().width,
				ViewSettings.getView_bounds_page().getSize().height,
				BufferedImage.TYPE_INT_ARGB));
        
        page.getJlbl_selectionBG().setSize(
                ViewSettings.getView_bounds_page().getSize());   
        page.getJpnl_toMove().setLocation(
                ViewSettings.getView_bounds_page().getLocation());
        page.getJlbl_painting().setSize(
                        ViewSettings.getView_bounds_page().getSize());
        page.getJlbl_selectionPainting().setSize(
                ViewSettings.getView_bounds_page().getSize());
        page.setSize(
                ViewSettings.getView_bounds_page().getSize());
        
        getControlPic().refreshPaint();
	}

	public void moveListener(MoveEvent _event) {

        cp.getView().getPage().setLocation(_event.getPnt_bottomLocation());
	}

	public void openListener() {

		Page page = cp.getView().getPage();

		ViewSettings.setView_bounds_page(
				new Rectangle(ViewSettings.getView_bounds_page_open()));
		
		

		cp.getControlPic().setBi(new BufferedImage(
				ViewSettings.getView_bounds_page().getSize().width,
				ViewSettings.getView_bounds_page().getSize().height,
				BufferedImage.TYPE_INT_ARGB));
        
        //adapt image size.
        page.setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        page.getJlbl_painting().setSize(
                ViewSettings.getView_bounds_page_open().getSize()
                .width, ViewSettings.getView_bounds_page_open()
                .getSize().height 
                - 1);
        page.getJlbl_selectionBG().setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        page.getJpnl_toMove().setLocation(
                ViewSettings.getView_bounds_page_open().getLocation());
        page.getJlbl_painting().setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        page.getJlbl_painting().setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        page.getJlbl_selectionPainting().setSize(
                ViewSettings.getView_bounds_page_open().getSize());
        getControlPic().refreshPaint(); 		
	}
}
