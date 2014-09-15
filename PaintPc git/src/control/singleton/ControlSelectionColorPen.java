//package declaration
package control.singleton;

//import declarations
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import model.objects.painting.Picture;
import settings.Constants;
import settings.Status;
import settings.TextFactory;
import start.utils.Utils;
import view.View;
import view.forms.tabs.Paint;
import view.util.Item1PenSelection;
import view.util.VButtonWrapper;


/**
 * Controller class which handles the actions caused by
 * the selection of colors or pens.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class ControlSelectionColorPen implements MouseListener {

	/**
	 * boolean which indicates, whether the necessary instances of View and
	 * Picture have been set by now.
	 */
	private boolean startPerform;
	
    /**
     * the only instance of this class.
     */
	private static ControlSelectionColorPen instance;
	
	/**
	 * Constructor: initializes startPerfrom (which indicates the listeners 
	 * to not perform an action until necessary variables have been set.
	 */
	private ControlSelectionColorPen() {
		
		//initialize start perform because the necessary variables have not
		//been set yet.
		this.startPerform = false;
	}
	
	
	/**
	 * initialize the controller and start listening.
	 */
	public void initialize() {
		
		
		//set startPerfrom to true if both values are acceptable
		if (View.getInstance() != null && Picture.getInstance() != null) {
			startPerform = true;
		} else {
			System.err.println(getClass() + "initialize. Es liegt "
					+ "moeglicherweise ein Fehler bei der initializsierung "
					+ "vor. Deshalb kann es sein, dass User Interaktionen"
					+ "nicht registriert werden.");
		}
	}
	
	
	/**
	 * set the cursor.
	 */
	public void setCursor() {

        View.getInstance().setCursor(Toolkit.getDefaultToolkit()
                .createCustomCursor(new ImageIcon(Utils.resizeImage(
                        Constants.MOUSE_ICON_SIZE, 
                        Constants.MOUSE_ICON_SIZE, 
                        Constants.VIEW_TB_ZOOM_IN_PATH)).getImage(), 
                        new Point(Constants.MOUSE_ICON_SIZE / 2, 
                                Constants.MOUSE_ICON_SIZE / 2), 
                                TextFactory.getInstance()
                                .getTextViewTb_zoomIn()));	    
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public void mouseReleased(final MouseEvent _event) {

		//if the ActionListener is not ready to listen to events because 
		//of the lack of necessary links to instances of classes return
		//each time an action is performed.
		if (!startPerform) {
			return;
		}
		
		/*
		 * the different pens in open pen menu
		 */
		if (ControlVisualEffects.isAStiftAuswahl((_event.getSource()))) {
			Item1PenSelection sa = (Item1PenSelection) ((VButtonWrapper) 
			        _event.getSource()).wrapObject();
			
			//set the image of the current pen
			if (sa.getPenSelection() == 1) {
				Paint.getInstance().getIt_stift1().setIcon(sa.getImagePath());
	            Paint.getInstance().getIt_stift1().setOpen(false);
	            CItem.getInstance().reset();
			} else if (sa.getPenSelection() == 2) {
				Paint.getInstance().getIt_stift2().setIcon(sa.getImagePath());
                Paint.getInstance().getIt_stift2().setOpen(false);
                CItem.getInstance().reset();
			}
		} else if (
		        !_event.getSource().equals(Paint.getInstance()
		                .getTb_zoomOut().getActionCause())
		        && !_event.getSource().equals(Paint.getInstance()
		                .getTb_new().getActionCause())
		        && !_event.getSource().equals(Paint.getInstance()
		                .getTb_save().getActionCause())
                && !_event.getSource().equals(Paint.getInstance()
                        .getTb_turnMirror().getActionCause())
                && !_event.getSource().equals(Paint.getInstance()
                        .getTb_prev().getActionCause())
                && !_event.getSource().equals(Paint.getInstance()
                        .getTb_next().getActionCause())
                && !_event.getSource().equals(Paint.getInstance()
                        .getTb_turnNormal().getActionCause())
                        && !_event.getSource().equals(Paint.getInstance()
                                .getIt_color().getTb_open().getActionCause())
		        && !_event.getSource().equals(Paint.getInstance()
		                .getTb_load().getActionCause())) {
		    deactivate();
		}

		/*
		 * colors.
		 * 	1) first and second color
		 *  2) change first/second color button array//TODO: 
		 */
		//1. in mouseReleased reinpacken denn wenn dragged macht der nix und 
		//das ist bloed 2. cursor entsprechend dem eingestellten icon aendern.
        if (_event.getSource().equals(Paint.getInstance()
		        .getJbtn_color1().getActionCause())) {
            
            //enable paint 1
            Status.setIndexOperation(Constants.CONTROL_PATINING_INDEX_PAINT_1);
            
            //set suitable icon
            Paint.getInstance().getIt_stift1().getTb_open().setActivated(true);
            Paint.getInstance().getTb_color1().setActivated(true);
            View.getInstance().setCursor(Toolkit.getDefaultToolkit()
                    .createCustomCursor(new ImageIcon(Utils.resizeImage(
                            Constants.MOUSE_ICON_SIZE,
                            Constants.MOUSE_ICON_SIZE, Paint.getInstance()
                            .getIt_stift1().getImagePath())).getImage(), 
                            new Point(0, Constants.MOUSE_ICON_SIZE - 1), "p1"));
			
		} else if (_event.getSource().equals(Paint.getInstance()
		        .getJbtn_color2().getActionCause())) {
            Status.setIndexOperation(Constants.CONTROL_PATINING_INDEX_PAINT_2);
            Paint.getInstance().getIt_stift2().getTb_open().setActivated(true);
            Paint.getInstance().getTb_color2().setActivated(true);
			
		} else if (_event.getSource().equals(Paint.getInstance().getIt_stift1()
		        .getTb_open().getActionCause())) {
		
			//set index of selected text and deactivate the pen
			Status.setIndexOperation(Constants.CONTROL_PATINING_INDEX_PAINT_1);
            Paint.getInstance().getIt_stift1().getTb_open().setActivated(true);
            Paint.getInstance().getTb_color1().setActivated(true);
		
		} else if (_event.getSource().equals(Paint.getInstance().getIt_stift2()
		        .getTb_open().getActionCause())) {
		
			//set index of selected text and deactivate the pen
            Status.setIndexOperation(Constants.CONTROL_PATINING_INDEX_PAINT_2);
            Paint.getInstance().getIt_stift2().getTb_open().setActivated(true);
            Paint.getInstance().getTb_color2().setActivated(true);
			
		} else if (_event.getSource().equals(Paint.getInstance()
                .getIt_selection().getTb_open().getActionCause())) {
        
            Status.setIndexOperation(
                    Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE);
        
        }  else if (_event.getSource().equals(Paint.getInstance()
                .getTb_selectionCurve().getActionCause())) {
        
            Status.setIndexOperation(
                    Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE);
            Paint.getInstance().getIt_selection().getTb_open()
            .setActivated(true);
            Paint.getInstance().getIt_selection().setOpen(true);
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_selectionLine().getActionCause())) {
        
            Status.setIndexOperation(
                    Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE);
            Paint.getInstance().getIt_selection()
            .getTb_open().setActivated(true);
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_selectionMagic().getActionCause())) {
        
            Status.setIndexOperation(
                    Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC);
            Paint.getInstance().getIt_selection().getTb_open()
            .setActivated(true);
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_fill().getActionCause())) {
        
            Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_FILL);
            Paint.getInstance().getTb_fill().setActivated(true);
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_zoomIn().getActionCause())) {
        
            Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_ZOOM);
            Paint.getInstance().getTb_zoomIn().setActivated(true);
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_pipette().getActionCause())) {
        
            Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_PIPETTE);
            Paint.getInstance().getTb_pipette().setActivated(true);
        
        } else if (!_event.getSource().equals(Paint.getInstance()
		        .getSa_bn1().getActionCause())) {
		    
			for (int j = 0; 
			        j < Paint.getInstance().getJbtn_colors().length; j++) {

				if (_event.getSource().equals(
				        Paint.getInstance().getJbtn_colors()[j])) {
				    
					if (Status.getIndexOperation()
					        == Constants.CONTROL_PATINING_INDEX_PAINT_1) {
					    Paint.getInstance().getJbtn_color1().setBackground(
						        Paint.getInstance().getJbtn_colors()[j]
						                .getBackground());

                        Picture.getInstance().changeColor(Paint.getInstance()
                                .getJbtn_colors()[j].getBackground());
					} else if (Status.getIndexOperation()
					        == Constants.CONTROL_PATINING_INDEX_PAINT_2) {
						Paint.getInstance().getJbtn_color2().setBackground(
						        Paint.getInstance().getJbtn_colors()[j]
						                .getBackground());
					}
				}
			}
		}
	}
	
	
	/**
	 * Deactivate each tab.
	 */
	private void deactivate() {

        Paint.getInstance().getIt_stift1().getTb_open().setActivated(false);
        Paint.getInstance().getIt_stift2().getTb_open().setActivated(false);
        Paint.getInstance().getIt_selection().getTb_open().setActivated(false);
        Paint.getInstance().getTb_color1().setActivated(false);
        Paint.getInstance().getTb_color2().setActivated(false);
        Paint.getInstance().getTb_pipette().setActivated(false);
        Paint.getInstance().getTb_fill().setActivated(false);
        Paint.getInstance().getTb_zoomIn().setActivated(false);

        
	}
	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static ControlSelectionColorPen getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
			instance = new ControlSelectionColorPen();
		}
		
		//return the only instance of this class.
		return instance;
	}


    @Override public void mouseClicked(final MouseEvent _event) { }

    @Override public void mouseEntered(final MouseEvent _event) { }

    @Override public void mouseExited(final MouseEvent _event) { }

    @Override public void mousePressed(final MouseEvent _event) { }
}
