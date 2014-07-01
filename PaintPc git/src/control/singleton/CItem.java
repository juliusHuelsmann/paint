//package declaration
package control.singleton;

//import declarations
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.forms.Page;
import view.util.Item1Menu;
import view.util.VButtonWrapper;


/**
 * Controller class for Item.
 * 
 * @author Julius Huelsmann
 * @version %I, %U%
 */
public final class CItem implements MouseListener {

    /**
     * the only instance of this class.
     */
	private static CItem instance;
	
	/**
	 * the last enabled item.
	 */
	private Item1Menu i_lastActionItem;

    /**
     * private constructor which can only be called out of
     * this class. Realized by getInstance.
     */
	private CItem() { }

	@Override
	public void mouseClicked(final MouseEvent _event) {
		
	    //if this is NOT the first time an ActionEvent is thrown.
	    //disable the last item
		if (i_lastActionItem != null) {

		    //disable and close Item
			i_lastActionItem.setOpen(false);
			i_lastActionItem.close();

		}

        Page.getInstance().getJlbl_painting().refreshPaint();
		
		//fetch current source of action
		Item1Menu i_currentActionItem = (Item1Menu) 
		        ((VButtonWrapper) _event.getSource()).wrapObject();
		
		//if the current item is a new item open the new item. Set last item
		//if action performed twice with the same source, do not reopen it
		//but reset the last Action element
		if (i_currentActionItem != i_lastActionItem) {

		    
		    //set open flag
			i_currentActionItem.setOpen(!i_currentActionItem.isOpen());
			
			//perform open/close action
			if (i_currentActionItem.isOpen()) {
				i_currentActionItem.open();
			} else  {
				i_currentActionItem.close();
			}
			
			//set last element
			i_lastActionItem = i_currentActionItem;
		} else { 
		    
		    //set last element
			i_lastActionItem = null;
		}

	}
	
	
	/**
	 * simple getter method.
	 * @return the instance
	 */
	public static CItem getInstance() {

		//if not yet instanced call the constructor of FetchColor.
		if (instance == null) {
			instance = new CItem();
		}
		return instance;
	}

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
