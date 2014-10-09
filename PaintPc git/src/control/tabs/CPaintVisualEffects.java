package control.tabs;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;

import model.settings.Status;
import model.settings.ViewSettings;
import view.View;
import view.forms.tabs.Paint;
import view.util.Item1PenSelection;
import view.util.VButtonWrapper;

/**
 * Controller class which takes care of the visual effects caused
 * by buttons (e.g. mouseOver)
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CPaintVisualEffects implements MouseListener {

	/**
	 * the only instance of this class.
	 */
	private static CPaintVisualEffects instance = null;

	/**
	 * boolean which indicates, whether the necessary instances of View and
	 * Picture have been set by now.
	 */
	private boolean startPerform;

	/**
	 * The StiftAuswahl which currently is selected for both groups.
	 */
	private Item1PenSelection[] lastSelected = null;
	
	
	/**
	 * Constructor: initializes startPerfrom (which indicates the listeners 
	 * to not perform an action until necessary variables have been set.
	 */
	private CPaintVisualEffects() {
		
		//initialize start perform because the necessary variables have not
		//been set yet.
		this.startPerform = false;
		
		//initialize the last selected array
		this.lastSelected = new Item1PenSelection[2];
	}
	
	
	/**
	 * Checks whether an Object is a StiftAuswahl wrapper button or not.
	 * 
	 * @param _obj the object
	 * @return the boolean
	 */
	public static boolean isAStiftAuswahl(final Object _obj) {
		try {
			
			((Item1PenSelection) ((VButtonWrapper) _obj).wrapObject())
			.toString();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * initialize the controller and start listening.
	 * @param _true whether to start to listen or not
	 */
	public void enable(final boolean _true) {
		
		
		//set startPerfrom to true if both values are acceptable
		if (View.getInstance() != null) {
			startPerform = _true;
		} else {
		    Status.getLogger().warning("initialize. Es liegt "
					+ "moeglicherweise ein Fehler bei der initializsierung "
					+ "vor. Deshalb kann es sein, dass Desing User "
					+ "Interaktionen nicht registriert werden.");
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseEntered(final MouseEvent _event) {

		//if the ActionListener is not ready to listen to events because 
		//of the lack of necessary links to instances of classes return
		//each time an action is performed.
		if (!startPerform) {
			return;
		}

		//if mouse is over a pen
		if (isAStiftAuswahl(_event.getSource())) {
			
			//if is not selected
			Item1PenSelection selected = (Item1PenSelection) 
			        ((VButtonWrapper) _event.getSource()).wrapObject();
			if (!selected.isSelected()) {
			    applyFocus(selected);
			}
			return; 
		} else {

	        //for loop for identifying the cause of the event
	        for (int j = 0; j < Paint.getInstance().getJbtn_colors().length; 
	                j++) {
	            if (_event.getSource().equals(
	                    Paint.getInstance().getJbtn_colors()[j])) {
	                
	                //highlight the border of the icon with mouse-over
	                Paint.getInstance().getJbtn_colors()[j].setBorder(
	                        BorderFactory.createCompoundBorder(
	                                new LineBorder(Color.black),
	                                new LineBorder(Color.black)));
	                
	                //return because only one item is performing action at one 
	                //time
	                return;
	            }
	        }
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseExited(final MouseEvent _event) {

		//if the ActionListener is not ready to listen to events because 
		//of the lack of necessary links to instances of classes return
		//each time an action is performed.
		if (!startPerform) {
			return;
		}
		
		//outfit of StiftAuswahl
		if (isAStiftAuswahl(_event.getSource())) {

			//reset the instance of Pen if this pen is not selected
			Item1PenSelection selected = (Item1PenSelection)
			        ((VButtonWrapper) _event.getSource()).wrapObject();
			if (!selected.isSelected()) {
				selected.setOpaque(false);
			}
			return;
		}
		
		//for loop for identifying the cause of the event
		for (int j = 0; j < Paint.getInstance().getJbtn_colors().length; j++) {

			if (_event.getSource().equals(
			        Paint.getInstance().getJbtn_colors()[j])) {

				//reset the border of the icon which had mouse-over
				Paint.getInstance().getJbtn_colors()[j].setBorder(BorderFactory
						.createCompoundBorder(new LineBorder(Color.black),
								new LineBorder(Color.white)));
				
				//return because only one item is performing action at one time
				return;
			}
		}
	}
	
	/**
	 * apply focus to selected item.
	 * @param _selected the selected item
	 */
	public static void applyFocus(final Item1PenSelection _selected) {

        _selected.setOpaque(true);
        _selected.setBackground(ViewSettings.CLR_BORDER);
    
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mousePressed(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseReleased(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseClicked(final MouseEvent _event) {

		//if mouse is over a pen
		if (isAStiftAuswahl(_event.getSource())) {

			Item1PenSelection stift_event = (Item1PenSelection)
					((VButtonWrapper) _event.getSource()).wrapObject();
			
			//if pen is not selected yet
			if (lastSelected[stift_event.getPenSelection() - 1] != null 
			        && lastSelected[stift_event.getPenSelection() - 1]
			                != stift_event) {
				lastSelected[stift_event.getPenSelection() - 1]
				        .setSelected(false);
				lastSelected[stift_event.getPenSelection() - 1]
				        .setOpaque(false);
				lastSelected[stift_event.getPenSelection() - 1]
				        .setBackground(Color.green);
			} 
			
			//select the current pen
			stift_event.setSelected(true);
			stift_event.setOpaque(true);
			stift_event.setBackground(ViewSettings.CLR_BORDER);
			
			//set the last selected
			lastSelected[stift_event.getPenSelection() - 1] = stift_event;
		}
	}

	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static CPaintVisualEffects getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
			instance = new CPaintVisualEffects();
		}
		
		//return the only instance of this class.
		return instance;
	}
}
