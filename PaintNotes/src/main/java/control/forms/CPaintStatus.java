//package declaration
package control.forms;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import control.ContorlPicture;
import control.ControlPaint;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.Status;
import model.util.paint.Utils;
import view.View;
import view.forms.Page;
import view.forms.Tabs;
import view.tabs.Insert;
import view.tabs.Paint;


/**
 * Controller class which handles the actions caused by
 * the selection of colors or pens.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CPaintStatus implements MouseListener {
	
	
	/**
	 * instance of the Paint controller class.
	 */
	private ControlPaint controlPaint;
	
	/**
	 * Constructor: initializes startPerfrom (which indicates the listeners 
	 * to not perform an action until necessary variables have been set.
	 */
	public CPaintStatus(final ControlPaint _cp) { 
		this.controlPaint = _cp;
		
	}


	private View getView() {
		return controlPaint.getView();
	}

	private Page getPage() {
		return controlPaint.getView().getPage();
	}
    
    
	private Insert getInsert() {
		return controlPaint.getView().getTabs().getTab_insert();
	}
	private Tabs getTabs() {
		return controlPaint.getView().getTabs();
	}
	private ContorlPicture getControlPicture() {
		return controlPaint.getControlPic();
	}
    /**
     * Fetch the instance of tab paint.
     * @return the tab paint.
     */
    private Paint getTabPaint() {
    	
    	if (controlPaint != null
    			&& controlPaint.getView() != null
    			&& controlPaint.getView().getTabs() != null
    			&& controlPaint.getView().getTabs().getTab_paint() != null) {
    		return controlPaint.getView().getTabs().getTab_paint();
    	} else {
    		
    		Status.getLogger().severe("Tab does not exist!");
    		return null;
    	}
    }
	
	
	/**
	 * set the cursor.
	 * 
	 * @param _path the path of the cursor image.
	 * @param _name the name of the cursor
	 */
	private void setCursor(final String _path, final String _name) {

	    getView().setCursor(Toolkit.getDefaultToolkit()
	            .createCustomCursor(new ImageIcon(Utils.resizeImage(
	                    Constants.MOUSE_ICON_SIZE, 
                        Constants.MOUSE_ICON_SIZE, 
                        _path)).getImage(), 
                        new Point(Constants.MOUSE_ICON_SIZE / 2, 
                                Constants.MOUSE_ICON_SIZE / 2), 
                                _name));	    
	}
	

	
	/**
	 * set the cursor.
	 * 
	 * @param _path the path of the cursor image.
	 * @param _name the name of the cursor
	 */
	private void setCursor(final BufferedImage _path, final String _name) {

	    getView().setCursor(Toolkit.getDefaultToolkit()
	            .createCustomCursor(_path, 
                        new Point(Status.getEraseRadius(), 
                        		Status.getEraseRadius()), 
                                _name));	    
	}
	
	
	/**
     * Deactivate each option.
     * 
     */
    public void deactivate() {
    

    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Paint paint = getTabPaint();

    	
    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {

    		
        	
            paint.getIt_stift1().getTb_open().setActivated(false);
            paint.getIt_stift2().getTb_open().setActivated(false);
            paint.getIt_selection().getTb_open().setActivated(false);
            paint.getTb_color1().setActivated(false);
            paint.getTb_color2().setActivated(false);
            paint.getTb_pipette().setActivated(false);
            paint.getTb_fill().setActivated(false);
            paint.getTb_zoomIn().setActivated(false);
            paint.getTb_move().setActivated(false);
            paint.getTb_erase().getTb_open().setActivated(false);
    	}
    }
    
    
    
    /**
     * Return the operation the current clickEvent will throw.
     * @param _event the Event.
     * @return the operation index.
     */
    private int getOperation(final MouseEvent _event) {


    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Paint paint = getTabPaint();

    	
    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {

            /*
             * colors.
             *  1) first and second color
             *  2) change first/second color button array
             */
            if (_event.getSource().equals(
                    paint.getJbtn_color1().getActionCause()) 
                    || _event.getSource().equals(
                            paint.getIt_stift1()
                            .getTb_open().getActionCause())) {
                
                return Constants.CONTROL_PAINTING_INDEX_PAINT_1;
            
            } else if (_event.getSource().equals(paint
                    .getJbtn_color2().getActionCause()) || _event.getSource()
                    .equals(paint.getIt_stift2()
                            .getTb_open().getActionCause())) {

                return Constants.CONTROL_PAINTING_INDEX_PAINT_2;
                
            } else if (_event.getSource().equals(paint
                    .getIt_selection().getTb_open().getActionCause())) {
            
                return Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE;
            
            }  else if (_event.getSource().equals(paint
                    .getTb_selectionCurve().getActionCause())) {
            
                return Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE;
            
            } else if (_event.getSource().equals(paint
                    .getTb_selectionLine().getActionCause())) {
            
                return Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE;
            
            } else if (_event.getSource().equals(paint
                    .getTb_selectionMagic().getActionCause())) {
            
                return Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC;
            } else if (_event.getSource().equals(paint
                    .getTb_fill().getActionCause())) {
            
                return Constants.CONTROL_PAINTING_INDEX_FILL;
            
            } else if (_event.getSource().equals(paint
                    .getTb_zoomIn().getActionCause())) {
            
                return Constants.CONTROL_PAINTING_INDEX_ZOOM_IN;
            
            } else if (_event.getSource().equals(paint
                    .getTb_pipette().getActionCause())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_PIPETTE);
            
            } else if (_event.getSource().equals(paint
                    .getTb_move().getActionCause())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_MOVE);
            
            } else if (_event.getSource().equals(paint
                    .getTb_erase().getTb_open().getActionCause())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_ERASE);
            
            } else if (_event.getSource().equals(getInsert()
                    .getI2_g_line())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_I_G_LINE);
            
            } else if (_event.getSource().equals(getInsert()
                    .getI2_g_rect())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE);
            
            } else if (_event.getSource().equals(getInsert()
                    .getI2_g_curve())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_I_G_CURVE);
            
            } else if (_event.getSource().equals(getInsert()
                    .getI2_g_triangle())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE);
            
            } else if (_event.getSource().equals(getInsert()
                    .getI2_g_arch())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_I_G_ARCH);
            
            } else if (_event.getSource().equals(getInsert()
                    .getI2_d_diagramm())) {
            
                return (Constants.CONTROL_PAINTING_INDEX_I_D_DIA);
            
            } else if (_event.getSource().equals(
                    getInsert().getI2_g_archFilled())) {
                return Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED;
            } else if (_event.getSource().equals(
                    getInsert().getI2_g_rectFilled())) {
                return Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED;
            } else if (_event.getSource().equals(
                    getInsert().getI2_g_triangleFilled())) {
                return Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED;
            }  else if (_event.getSource().equals(
                    getInsert().getI2_g_curve2())) {
                return Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2;
            } else {
                return -1;
            }
    	} else {

            return -1;
    	}
    }
    
    
    /**
     * perform action .
     * @param _event the event
     */
    private void mouseReleasedColorChange(final MouseEvent _event) {


    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Paint paint = getTabPaint();

    	
    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {
    		for (int j = 0; j < paint.getJbtn_colors().length; j++) {

                if (_event.getSource().equals(
                        paint.getJbtn_colors()[j])) {
                    
                    if (Status.getIndexOperation()
                            != Constants.CONTROL_PAINTING_INDEX_PAINT_2) {

                        Pen pen = Status.getPenSelected1();
                        
                        Color newBG = paint.getJbtn_colors()[j]
                                        .getBackground();
                        
                        paint.getJbtn_color1().setBackground(
                                new Color(newBG.getRGB(), true));
                        pen.setClr_foreground(
                                new Color(newBG.getRGB(), true));
                        
                        Status.setPenSelected1(pen);
                        
                    } else if (Status.getIndexOperation()
                            == Constants.CONTROL_PAINTING_INDEX_PAINT_2) {

                        Pen pen = Status.getPenSelected2();
                        
                        Color newBG = paint.getJbtn_colors()[j]
                                        .getBackground();
                        
                        paint.getJbtn_color2().setBackground(newBG);
                        pen.setClr_foreground(newBG);
                        
                        Status.setPenSelected2(pen);
                        
                    } 
                }
            }
    	}
    }
    


    /**
	 * {@inheritDoc}
	 */
	public void mouseReleased(final MouseEvent _event) {



    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Paint paint = getTabPaint();

    	
    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {

    			
    			//if previously zoomed remove zoom field.
    			if (Status.getIndexOperation()
    					== Constants.CONTROL_PAINTING_INDEX_ZOOM_IN) {

    				getControlPicture().removeZoomBox();
    			}
    			
    			
    		    int operationID = getOperation(_event);
    		    
    		    //if operation id is valid; thus operation has been found
    		    if (operationID != -1) {

    		        //set operation and deactivate older operation Buttons
                    Status.setIndexOperation(operationID);
                    deactivate();
                    
                    if (controlPaint.getPicture().isSelected()) {

                        //if there was selection before, release it to Picture
                        controlPaint.getPicture().releaseSelected(
                    			controlPaint.getControlPaintSelection(),
                    			controlPaint.getcTabSelection(),
                    			controlPaint.getView().getTabs().getTab_debug(),
                    			controlPaint.getView().getPage()
                    			.getJlbl_painting().getLocation().x,
                    			controlPaint.getView().getPage()
                    			.getJlbl_painting().getLocation().y);
                        getControlPicture().releaseSelected();
                        getPage().removeButtons();
                    }
                    
                    

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
//                        getInsert().getI2_g_line().
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_PAINT_1:

                        //enable buttons
                        paint.getIt_stift1().getTb_open()
                        .setActivated(true);
                        paint.getTb_color1().setActivated(true);
                        controlPaint.getPicture().changePen(
                        		Status.getPenSelected1());
                        
                        //set cursor
                        setCursor(paint.getIt_stift1().getImagePath(),
                                "p1");
                        setCursor("cursor.png",
                                "p1");
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_PAINT_2:

                        //enable buttons
                        paint.getIt_stift2().getTb_open()
                        .setActivated(true);
                        paint.getTb_color2().setActivated(true);
                        controlPaint.getPicture().changePen(
                        		Status.getPenSelected2());
                        
                        //set cursor
                        setCursor(paint.getIt_stift2().getImagePath(),
                                "p2");
                        setCursor("cursor.png",
                                "p1");
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
                        getTabs().closeMenues();
                        paint.getIt_selection().getTb_open()
                            .setActivated(true);
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:
                        getTabs().closeMenues();
                        paint.getIt_selection()
                        .getTb_open().setActivated(true); 
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:
                        getTabs().closeMenues();
                        paint.getIt_selection().getTb_open()
                        .setActivated(true);
                        break;	
                    case Constants.CONTROL_PAINTING_INDEX_FILL:
                        getTabs().closeMenues();
                        paint.getTb_fill().setActivated(true);
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:
                        getTabs().closeMenues();
                        paint.getTb_zoomIn().setActivated(true);
                        break;

                    case Constants.CONTROL_PAINTING_INDEX_PIPETTE:
                        getTabs().closeMenues();
                        paint.getTb_pipette().setActivated(true);
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_MOVE:
                        getTabs().closeMenues();
                        paint.getTb_move().setActivated(true);
                        break;
                    case Constants.CONTROL_PAINTING_INDEX_ERASE:

                        //enable buttons
                        paint.getTb_erase().getTb_open()
                        .setActivated(true);
                    	
                    	
                        getTabs().closeMenues();
                        paint.getTb_erase().getTb_open().setActivated(true);

                        BufferedImage bi_erase = new BufferedImage(
                        		Status.getEraseRadius() * 2,
                        		Status.getEraseRadius() * 2, 
                        		BufferedImage.TYPE_INT_RGB);
                        for (int y = 0; y < bi_erase.getHeight(); y++) {
                        	for (int x = 0; x < bi_erase.getWidth(); x++) {
                            
                        		if (y == 0 || y == bi_erase.getHeight() - 1
                        				|| x == 0 
                        				|| x == bi_erase.getWidth() - 1) {
                            		bi_erase.setRGB(x, y, Color.black.getRGB());
                        		} else {

                            		bi_erase.setRGB(x, y, Color.white.getRGB());
                        		}
                            
                        	
                        	}
                        	
                        }
                        
                        setCursor(bi_erase, "p1");
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
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent _event) { }
}
