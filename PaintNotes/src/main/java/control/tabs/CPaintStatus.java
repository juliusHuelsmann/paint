//package declaration
package control.tabs;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import control.util.CItem;
import model.objects.painting.Picture;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.Status;
import model.util.paint.Utils;
import view.View;
import view.forms.Page;
import view.forms.Tabs;
import view.tabs.Insert;
import view.tabs.Paint;
import view.util.Item1PenSelection;
import view.util.VButtonWrapper;


/**
 * Controller class which handles the actions caused by
 * the selection of colors or pens.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CPaintStatus implements MouseListener {

	/**
	 * boolean which indicates, whether the necessary instances of View and
	 * Picture have been set by now.
	 */
	private boolean startPerform;
	
    /**
     * the only instance of this class.
     */
	private static CPaintStatus instance;
	
	/**
	 * Constructor: initializes startPerfrom (which indicates the listeners 
	 * to not perform an action until necessary variables have been set.
	 */
	private CPaintStatus() {
		
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

            Status.getLogger().warning("initialize. Es liegt "
					+ "moeglicherweise ein Fehler bei der initializsierung "
					+ "vor. Deshalb kann es sein, dass User Interaktionen"
					+ "nicht registriert werden.");
		}
	}
	
	
	/**
	 * set the cursor.
	 * 
	 * @param _path the path of the cursor image.
	 * @param _name the name of the cursor
	 */
	private void setCursor(final String _path, final String _name) {

	    View.getInstance().setCursor(Toolkit.getDefaultToolkit()
	            .createCustomCursor(new ImageIcon(Utils.resizeImage(
	                    Constants.MOUSE_ICON_SIZE, 
                        Constants.MOUSE_ICON_SIZE, 
                        _path)).getImage(), 
                        new Point(Constants.MOUSE_ICON_SIZE / 2, 
                                Constants.MOUSE_ICON_SIZE / 2), 
                                _name));	    
	}
	
	
	/**
     * Deactivate each option.
     * 
     */
    public void deactivate() {
    
        Paint.getInstance().getIt_stift1().getTb_open().setActivated(false);
        Paint.getInstance().getIt_stift2().getTb_open().setActivated(false);
        Paint.getInstance().getIt_selection().getTb_open().setActivated(false);
        Paint.getInstance().getTb_color1().setActivated(false);
        Paint.getInstance().getTb_color2().setActivated(false);
        Paint.getInstance().getTb_pipette().setActivated(false);
        Paint.getInstance().getTb_fill().setActivated(false);
        Paint.getInstance().getTb_zoomIn().setActivated(false);
        Paint.getInstance().getTb_move().setActivated(false);
        Paint.getInstance().getTb_erase().setActivated(false);
    }
    
    
    
    /**
     * Return the operation the current clickEvent will throw.
     * @param _event the Event.
     * @return the operation index.
     */
    private int getOperation(final MouseEvent _event) {

        /*
         * colors.
         *  1) first and second color
         *  2) change first/second color button array
         */
        if (_event.getSource().equals(
                Paint.getInstance().getJbtn_color1().getActionCause()) 
                || _event.getSource().equals(
                        Paint.getInstance().getIt_stift1()
                        .getTb_open().getActionCause())) {
            
            return Constants.CONTROL_PAINTING_INDEX_PAINT_1;
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getJbtn_color2().getActionCause()) || _event.getSource()
                .equals(Paint.getInstance().getIt_stift2()
                        .getTb_open().getActionCause())) {

            return Constants.CONTROL_PAINTING_INDEX_PAINT_2;
            
        } else if (_event.getSource().equals(Paint.getInstance()
                .getIt_selection().getTb_open().getActionCause())) {
        
            return Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE;
        
        }  else if (_event.getSource().equals(Paint.getInstance()
                .getTb_selectionCurve().getActionCause())) {
        
            return Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE;
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_selectionLine().getActionCause())) {
        
            return Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE;
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_selectionMagic().getActionCause())) {
        
            return Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC;
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_fill().getActionCause())) {
        
            return Constants.CONTROL_PAINTING_INDEX_FILL;
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_zoomIn().getActionCause())) {
        
            return Constants.CONTROL_PAINTING_INDEX_ZOOM_IN;
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_pipette().getActionCause())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_PIPETTE);
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_move().getActionCause())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_MOVE);
        
        } else if (_event.getSource().equals(Paint.getInstance()
                .getTb_erase().getActionCause())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_ERASE);
        
        } else if (_event.getSource().equals(Insert.getInstance()
                .getI2_g_line())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_I_G_LINE);
        
        } else if (_event.getSource().equals(Insert.getInstance()
                .getI2_g_rect())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE);
        
        } else if (_event.getSource().equals(Insert.getInstance()
                .getI2_g_curve())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_I_G_CURVE);
        
        } else if (_event.getSource().equals(Insert.getInstance()
                .getI2_g_triangle())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE);
        
        } else if (_event.getSource().equals(Insert.getInstance()
                .getI2_g_arch())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_I_G_ARCH);
        
        } else if (_event.getSource().equals(Insert.getInstance()
                .getI2_d_diagramm())) {
        
            return (Constants.CONTROL_PAINTING_INDEX_I_D_DIA);
        
        } else if (_event.getSource().equals(
                Insert.getInstance().getI2_g_archFilled())) {
            return Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED;
        } else if (_event.getSource().equals(
                Insert.getInstance().getI2_g_rectFilled())) {
            return Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED;
        } else if (_event.getSource().equals(
                Insert.getInstance().getI2_g_triangleFilled())) {
            return Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED;
        }  else if (_event.getSource().equals(
                Insert.getInstance().getI2_g_curve2())) {
            return Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2;
        } else {
            return -1;
        }
    }
    
    
    /**
     * perform action .
     * @param _event the event
     */
    private void mouseReleasedColorChange(final MouseEvent _event) {

        for (int j = 0; j < Paint.getInstance().getJbtn_colors().length; j++) {

            if (_event.getSource().equals(
                    Paint.getInstance().getJbtn_colors()[j])) {
                
                if (Status.getIndexOperation()
                        != Constants.CONTROL_PAINTING_INDEX_PAINT_2) {

                    Pen pen = Status.getPenSelected1();
                    
                    Color newBG = Paint.getInstance().getJbtn_colors()[j]
                                    .getBackground();
                    
                    Paint.getInstance().getJbtn_color1().setBackground(
                            new Color(newBG.getRGB(), true));
                    pen.setClr_foreground(
                            new Color(newBG.getRGB(), true));
                    
                    Status.setPenSelected1(pen);
                    
                } else if (Status.getIndexOperation()
                        == Constants.CONTROL_PAINTING_INDEX_PAINT_2) {

                    Pen pen = Status.getPenSelected2();
                    
                    Color newBG = Paint.getInstance().getJbtn_colors()[j]
                                    .getBackground();
                    
                    Paint.getInstance().getJbtn_color2().setBackground(newBG);
                    pen.setClr_foreground(newBG);
                    
                    Status.setPenSelected2(pen);
                    
                } 
            }
        }
    }
    
    
    /**
     * mouseReleased action for changing pen.
     * @param _event the ActionEvent
     * @return whether the _event is suitable for this operation
     */
    private boolean mouseReleasedPenChange(final MouseEvent _event) {

        /*
         * the different pens in open pen menu
         */
        if (CPaintVisualEffects.isAStiftAuswahl((_event.getSource()))) {
            Item1PenSelection sa = (Item1PenSelection) ((VButtonWrapper) 
                    _event.getSource()).wrapObject();
            
            //set the image of the current pen, close the menu and
            //reset the last open menu; thus no menu has to be closed the next
            //time another menu is opened
            if (sa.getPenSelection() == 1) {
                
                Paint.getInstance().getIt_stift1().setIcon(sa.getImagePath());
                Paint.getInstance().getIt_stift1().setOpen(false);
                Status.setIndexOperation(
                        Constants.CONTROL_PAINTING_INDEX_PAINT_1);
                deactivate();
                Paint.getInstance().getIt_stift1().getTb_open()
                .setActivated(true);
                Paint.getInstance().getTb_color1()
                .setActivated(true);
                
                
            } else if (sa.getPenSelection() == 2) {
                
                Paint.getInstance().getIt_stift2().setIcon(sa.getImagePath());
                Paint.getInstance().getIt_stift2().setOpen(false);
                Status.setIndexOperation(
                        Constants.CONTROL_PAINTING_INDEX_PAINT_2);

                deactivate();
                Paint.getInstance().getIt_stift2().getTb_open()
                .setActivated(true);
                Paint.getInstance().getTb_color2()
                .setActivated(true);
            }
            CPaintVisualEffects.applyFocus(sa);
            Picture.getInstance().userSetPen(sa.getPen(), sa.getPenSelection());
            CItem.getInstance().reset();
            
            //return that this operation has been performed
            return true;
        } else {
            
            //return that the operation has not been found yet.
            return false;
        }
        
    }


    /**
	 * {@inheritDoc}
	 */
	public void mouseReleased(final MouseEvent _event) {

		//if the ActionListener is not ready to listen to events because 
		//of the lack of necessary links to instances of classes return
		//each time an action is performed.
		if (!startPerform) {
			return;
		}
		
		if (!mouseReleasedPenChange(_event)) {

		    int operationID = getOperation(_event);
		    
		    //if operation id is valid; thus operation has been found
		    if (operationID != -1) {

		        //set operation and deactivate older operation Buttons
                Status.setIndexOperation(operationID);
                deactivate();
                
                //if there was selection before, release it to Picture
                Picture.getInstance().releaseSelected();
                Page.getInstance().releaseSelected();
                Page.getInstance().removeButtons();
                Page.getInstance().getJlbl_painting().refreshPaint();
                
                

                switch (operationID) {
                /*
                 * pens
                 */
                case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
                case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
                case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
                case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
                case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
                case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
                case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
                case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
                case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
                case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
//                    Insert.getInstance().getI2_g_line().
                    break;
                case Constants.CONTROL_PAINTING_INDEX_PAINT_1:

                    //enable buttons
                    Paint.getInstance().getIt_stift1().getTb_open()
                    .setActivated(true);
                    Paint.getInstance().getTb_color1().setActivated(true);
                    
                    //set cursor
                    setCursor(Paint.getInstance().getIt_stift1().getImagePath(),
                            "p1");
                    break;
                case Constants.CONTROL_PAINTING_INDEX_PAINT_2:

                    //enable buttons
                    Paint.getInstance().getIt_stift2().getTb_open()
                    .setActivated(true);
                    Paint.getInstance().getTb_color2().setActivated(true);
                    
                    //set cursor
                    setCursor(Paint.getInstance().getIt_stift2().getImagePath(),
                            "p2");
                    break;
                case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getIt_selection().getTb_open()
                        .setActivated(true);
                    break;
                case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getIt_selection()
                    .getTb_open().setActivated(true); 
                    break;
                case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getIt_selection().getTb_open()
                    .setActivated(true);
                    break;
                case Constants.CONTROL_PAINTING_INDEX_FILL:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getTb_fill().setActivated(true);
                    break;
                case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getTb_zoomIn().setActivated(true);
                    break;

                case Constants.CONTROL_PAINTING_INDEX_PIPETTE:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getTb_pipette().setActivated(true);
                    break;
                case Constants.CONTROL_PAINTING_INDEX_MOVE:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getTb_move().setActivated(true);
                    break;
                case Constants.CONTROL_PAINTING_INDEX_ERASE:
                    Tabs.getInstance().closeMenues();
                    Paint.getInstance().getTb_erase().setActivated(true);
                    break;
                default:
                    Status.getLogger().warning("falsche id");
                    break;
                }
		    } else {


	            mouseReleasedColorChange(_event);
		    }
		}
	}
	
	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static CPaintStatus getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
			instance = new CPaintStatus();
		}
		
		//return the only instance of this class.
		return instance;
	}


    public void mouseClicked(final MouseEvent _event) { }
    public void mouseEntered(final MouseEvent _event) { }
    public void mouseExited(final MouseEvent _event) { }
    public void mousePressed(final MouseEvent _event) { }
}
