//package declaration
package model.objects.painting;

//import declarations
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import control.CSelection;
import control.tabs.CTabSelection;
import view.View;
import view.forms.Message;
import view.forms.Page;
import view.tabs.Insert;
import model.objects.PictureOverview;
import model.objects.painting.po.POInsertion;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectImage;
import model.objects.painting.po.PaintObjectPen;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.painting.po.diag.PODiagramm;
import model.objects.painting.po.geo.POArch;
import model.objects.painting.po.geo.POArchFilled;
import model.objects.painting.po.geo.POCurve;
import model.objects.painting.po.geo.POLine;
import model.objects.painting.po.geo.PORectangle;
import model.objects.painting.po.geo.POTriangle;
import model.objects.painting.po.geo.POTriangleFilled;
import model.objects.painting.po.geo.PoRectangleFilled;
import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.objects.pen.normal.Pencil;
import model.objects.pen.special.PenSelection;
import model.settings.Constants;
import model.settings.Status;
import model.util.DPoint;
import model.util.list.List;
import model.util.paint.Utils;

/**
 * Picture class which contains all the not selected and selected painted items 
 * contained in two lists of PaintObjects. 
 * 
 * It handles the creation, changing and removing of PaintObjects, 
 * selection methods, 
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Picture {
	
	/**
	 * the only instance of this class.
	 */
	private static Picture instance = null;

	/**
	 * list of PaintObjects. The first list is the one into which all 
	 * non-selected paintObjects are added sorted by their x coordinate. 
	 * 
	 * The second list contains all currently selected items. These items 
	 * have to be saved somewhere outside the first list because they 
	 * can be transformed or removed.
	 */
	private List<PaintObject> ls_po_sortedByX, ls_poSelected;

	/**
	 * Current PaintObjectPen which can be altered and afterwards inserted into
	 * the list of PaintObjects sorted by X. 
	 * 
	 * If an image is inserted from clipboard or an existing PaintObjectPen
	 * is transformed into an image, that can be done directly e.g. by using
	 * the addPaintObjectImage method. This can not be done with the 
	 * PaintObjectPens because their creation is a continued process (the
	 * user draws a line; first the PaintObjectPen has to be created, 
	 * afterwards points are added to the PaintObjectPen. When the user 
	 * released the mouse, the paintObject is added to list of paintObjects
	 * and the PaintObjectPen po_current is reset to null.
	 */
	private PaintObjectPen po_current;
	
	/**
	 * The currently selected pen with which the current PaintObjectPen is 
	 * painted to the screen. Because of this, pen_current is copied and 
	 * afterwards given to new created PaintObject.
	 */
	private Pen pen_current;
	
	/**
	 * Current id of following PaintObject. Is given to the PaintObjectPen
	 * and increased afterwards. If the user removes PaintObjects the 
	 * id is not decreased; there may occur gaps in id.
	 */
	private int currentId;
	
	/**
	 * Empty utility class constructor because the Picture instance has to exist
	 * in process of initialization of Picture attributes.
	 */
	private Picture() { }
	
	/**
	 * Initialization method of class Picture.
	 * Calls the reload() method which creates a new instance of sorted 
	 * PaintObject list and sets up the currentID.
	 */
	private void initialize() {
	    reload();
	}
	
	
	/**
	 * The reload method creates a new instance of sorted PaintObject list 
	 * and sets up the currentID. 
	 * This method is called in initialization process and if the user
	 * creates a new page or loads a file that does only consist of images
	 * (e.g. in png of pdf format). 
	 */
	public void reload() {

        //initialize both lists ordered by 
        this.ls_po_sortedByX = new List<PaintObject>();
        
        //save current id
        this.currentId = 0;
	}
	
	
	/**
	 * Increases id and returns the next id for being able to create 
	 * PaintObjects. From outside the Picture class without using the create.
	 * method. Is mainly used for testing purpose.
	 * 
	 * @return the next id.
	 */
	public synchronized int getIncreaseCID() {
		currentId++;
		return currentId - 1;
	}

	/**
     * Method for creating a new PaintObjectImage which is not directly added
     * to a list but returned to the demanding method.
     * 
     * @param _bi the BufferedImage of which the new created PaintObjectImage
     * consists
     * 
     * @return the new created PaintObjectImage.
     */
    public PaintObjectImage createPOI(final BufferedImage _bi) {
        return new PaintObjectImage(currentId, _bi);
        
    }

    /**
     * Method for creating a new PaintObjectWriting which is not directly added
     * to a list but returned to the demanding method.
     * 
     * 
     * @param _pen the pen which will print the new created PaintObjectWriting
     *
     * @return the new created PaintObjectWriting
     */
    public PaintObjectWriting createPOW(final Pen _pen) {
        return new PaintObjectWriting(currentId, _pen);
        
    }

    /**
     * adds a new PaintObject to list.
     */
    public void addPaintObjectWrinting() {
        
        if (po_current != null) {
            
            if (!(po_current instanceof POCurve)) {
                

                //throw error message and kill program.
                Status.getLogger().severe(
                        "Es soll ein neues pen objekt geadded werden, obwohl "
                        + "das Alte nicht null ist also nicht gefinished "
                        + "wurde.\n"
                        + "Programm wird beendet.");
                ls_po_sortedByX.insertBehind(po_current);
            }
            
        }
        if (!(po_current instanceof POCurve)) {

            //create new PaintObject and insert it into list of 
            po_current = new PaintObjectWriting(currentId, pen_current);

            //increase current id
            currentId++;
            
            //set uncommitted changes.
            Status.setUncommittedChanges(true);
        }
    }
    
    /**
     * adds a new PaintObject to list.
     * @param _bi the BufferedImage which is to be transformed into ImagePO.
     */
    public void addPaintObjectImage(final BufferedImage _bi) {
        
        if (po_current != null) {
            
            //throw error message and kill program.
            Status.getLogger().severe(
                    "Es soll ein neues image objekt geadded werden, obwohl das "
                    + "Alte nicht null ist also nicht gefinished wurde.\n"
                    + "Programm wird beendet.");
            ls_po_sortedByX.insertBehind(po_current);
            
        }
        
        if (_bi == null) {
            Status.getLogger().warning("nothing on clipboard.");
        } else {
            
            //create new PaintObject and insert it into list of 
            ls_po_sortedByX.insertAfterHead(new PaintObjectImage(
                    currentId, _bi));
    
            //increase current id
            currentId++;
            
            //set uncommitted changes.
            Status.setUncommittedChanges(true);
        }
    }

    /**
     * Add the paintObject.
     */
    public void addPaintObject() {

        int casus = -1;
        // switch index of operation
        switch (Status.getIndexOperation()) {

        case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
            addPaintObject(new POLine(currentId, pen_current));
            break;
        case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
            
            casus = Constants.PEN_ID_MATHS_SILENT;
        case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
            if (casus == -1) {

                casus = Constants.PEN_ID_MATHS_SILENT_2;
            } 


            Pen pen;
            if (pen_current instanceof BallPen) {

                pen = new BallPen(casus,
                        pen_current.getThickness(), 
                        pen_current.getClr_foreground());
            } else if (pen_current instanceof PenSelection) {

                pen = new PenSelection();
            } else if (pen_current instanceof Pencil) {

                pen = new Pencil(casus,
                        pen_current.getThickness(), 
                        pen_current.getClr_foreground());
            } else {
                

                Status.getLogger().warning("fehler stiftz noch nicht hinzu");
                
                //throw exception
                throw new Error("Fehler: stift noch nicht hinzugefuegt.");
            }
            addPaintObject(new POCurve(casus, pen, casus));
            break;
        case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
            addPaintObject(new PORectangle(currentId, pen_current));
            break;
        case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
            addPaintObject(new POTriangle(currentId, pen_current));
            break;

        case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
            addPaintObject(new POArchFilled(currentId, pen_current));
            break;
        case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
            addPaintObject(new PoRectangleFilled(currentId, pen_current));
            break;
        case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
            addPaintObject(new POTriangleFilled(currentId, pen_current));
            break;
        case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
            addPaintObject(new POArch(currentId, pen_current));
            break;

        case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
            
            String srows = Insert.getInstance().getJtf_amountRows().getText();
            int rows = 0;
            try {
                rows = Integer.parseInt(srows);
            } catch (Exception e) {
                Message.showMessage(Message.MESSAGE_ID_INFO, "enter valid row");
            }
            String slines = Insert.getInstance().getJtf_amountLines().getText();
            int lines = 0;
            try {
                lines = Integer.parseInt(slines);
            } catch (Exception e) {
                Message.showMessage(Message.MESSAGE_ID_INFO, 
                        "enter valid column");
            }
            addPaintObject(new PODiagramm(currentId, pen_current, lines, rows));
            break;
        case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
        case Constants.CONTROL_PAINTING_INDEX_PAINT_1:
            addPaintObject(new PaintObjectWriting(currentId, pen_current));
            break;
        default:
            Status.getLogger().warning("unknown index operation");
        }
    }
    
    /**
     * adds a new PaintObject to list.
     * @param _po the PaintObjectPen.
     */
    private void addPaintObject(final PaintObjectPen _po) {
        
        if (po_current != null) {
            
            if (!(po_current instanceof POCurve)) {

                
                //throw error message and kill program.
                Status.getLogger().severe(
                        "Es soll ein neues objekt geadded werden, obwohl das "
                        + "alte nicht null ist also nicht gefinished wurde.\n"
                        + "Programm wird beendet.");
                ls_po_sortedByX.insertBehind(po_current);
                
            }
        }
        

        if (!(po_current instanceof POCurve) || !(_po instanceof POCurve)) {

            //create new PaintObject and insert it into list of 
            po_current = _po;

        
            //increase current id
            currentId++;
                
            //set uncommitted changes.
            Status.setUncommittedChanges(true);
        }
    }
	
    
    
    
    /**
     * return the color of the pixel at given location. The location is
     * the location in the bufferedImage which is not adapted at the zoomed
     * click size.
     * 
     * @param _pxX coordinate of normal-sized BufferedImage.
     * @param _pxY coordinate of normal-sized BufferedImage.
     * 
     * @return the RGB integer of the color at given coordinate.
     */
    public int getColorPX(final int _pxX, final int _pxY) {
        return Page.getInstance().getJlbl_painting().getBi().getRGB(_pxX, _pxY);
    }
    
    
    /**
     * repaint the items that are in a rectangle to the (view) page (e.g. if
     * the JLabel is moved))..
     * 
     * @param _x the x coordinate
     * @param _y the y coordinate
     * @param _width the width 
     * @param _height the height
     * @param _graphicX the graphics x
     * @param _graphiY the graphics y.
     * @param _bi the bufferedImage
     * @return the graphics
     */
    public synchronized BufferedImage updateRectangle(final int _x, 
            final int _y, final int _width, final int _height,
            final int _graphicX, final int _graphiY,
            final BufferedImage _bi) {

        BufferedImage ret =  emptyRectangle(_x, _y, _width, _height, 
                _graphicX, _graphiY, _bi);
        
        //if the graphical user interface is not set up yet.
        if (Page.getInstance().getJlbl_painting() == null
                || ret == null) {
            return new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        }
        Page.getInstance().getJlbl_painting().setBi(ret);
        Page.getInstance().getJlbl_painting().setIcon(new ImageIcon(ret));
        Page.getInstance().getJlbl_painting().repaint();

      
        ret =  repaintRectangle(_x, _y, _width, _height, ret, false);
        
        return ret;
    }
    
    

    /**
     * repaint the items that are in a rectangle to the (view) page (e.g. if
     * the JLabel is moved))..
     * 
     * @param _x the x coordinate
     * @param _y the y coordinate
     * @param _width the width 
     * @param _height the height
     * @param _graphicX the graphics x
     * @param _graphiY the graphics y.
     * @param _bi the BufferedImage
     * @return the graphics
     */
    public synchronized BufferedImage emptyRectangle(final int _x, 
            final int _y, final int _width, final int _height,
            final int _graphicX, final int _graphiY, 
            final BufferedImage _bi) {

        
        //check whether the rectangle concerns the blue border of the
        //image which is not to be emptied and then repainted. 
        //If that's the case, the rectangle width or height are decreased.
        int rectWidth = _width, rectHeight = _height;
        if (_x + _width > Status.getImageShowSize().width) {
            rectWidth = Status.getImageShowSize().width - _x;
        }
        
        if (_y + _height > Status.getImageShowSize().height) {
            rectHeight = Status.getImageShowSize().height - _y;
            
        }
        
        //alle die in Frage kommen neu laden.
        if (ls_po_sortedByX == null
                || _bi == null) {
            return _bi;
        }
        
        PaintBI.fillRectangleQuick(_bi, new Color(0, 0, 0, 0), 
                new Rectangle(_graphicX, _graphiY, rectWidth, rectHeight));
        
        return _bi;

    }
    
    
    /**
     * Repaint a rectangle without clearing the screen.
     * 
     * The state of the list of paintObjects is not changed by this process.
     * 
     * @param _x 		the x coordinate
     * @param _y 		the y coordinate
     * @param _width 	the width 
     * @param _height 	the height
     * 
     * @param _bi 		the BufferedImage
     * @param _final 	whether to paint finally to BufferedImage or not.
     * 
     * @return the BufferedImage with painted PaintObjects on it.
     */
    public synchronized BufferedImage repaintRectangle(final int _x,
            final int _y, final int _width, final int _height,
            final BufferedImage _bi,
            final boolean _final) {

    	//If the sorted list of PaintObjects has not been initialized yet,
    	//the list is empty or the given bufferedImage is equal to NULL
    	//return the given BufferedImage because there is nothing to do
        if (ls_po_sortedByX == null
        		|| ls_po_sortedByX.isEmpty() 
                || _bi == null) {
            return _bi;
        }

    	//Start a transaction. That means that after the transaction has 
    	//been terminated, the current item of the list is reset.
    	ls_po_sortedByX.startTransaction("repaintRectangle");

        //Initialize new list into which the Items are inserted that are inside
        //the specified rectangle. List is sorted by id for painting the 
        //items chronologically.
        List<PaintObject> ls_poChronologic = new List<PaintObject>();
        
        //reset value for debugging and speed testing.
        Status.setCounter_paintedPoints(0);
        
        //initialize start value go to the list's beginning and calculate
        //values such as the stretch factors (which occur because of 
        //zooming in and out).
        boolean behindRectangle = false;
        ls_po_sortedByX.toFirst();
        
        /**
         * Stretch factor by width and height.
         */
        final double factorW = 1.0
        		* Status.getImageSize().width
                / Status.getImageShowSize().width, 
                factorH = 1.0 
                * Status.getImageSize().width
                / Status.getImageShowSize().width;
        
        /**
         * The location of the page end needed for checking roughly whether
         * a paintObject may be inside the repaint rectangle.
         */
        final int 
        xLocationPageEnd = (int) (factorW 
        		* (-Page.getInstance().getJlbl_painting().getLocation().getX()
        				+ Page.getInstance().getJlbl_painting().getWidth())), 
        yLocationPageEnd = (int) (factorH 
        		* (-Page.getInstance().getJlbl_painting().getLocation().getY() 
        				+ Page.getInstance().getJlbl_painting().getHeight()));
        
        /**
         * The repaint rectangle.
         */
        final Rectangle r_selection = new Rectangle(_x, _y, _width, _height);
        
        
        /*
         * Find out which items are inside the given repaint rectangle and
         * insert them into the list of paintObjects.
         */
        
        while(!ls_po_sortedByX.isEmpty() 
        		&& !ls_po_sortedByX.isBehind() 
                && !behindRectangle) {
            
        	// if the current item is not initialized only perform the 
        	//next operation.
            if (ls_po_sortedByX.getItem() != null) {

                
                //check whether the current PaintObject is in the given 
                //rectangle by the coordinate by which the list is sorted.
            	//if that is not the case the element is behind the specified
            	//rectangle.
                if (ls_po_sortedByX.getItem().getSnapshotBounds().x 
                        <= xLocationPageEnd) {

                	//Firstly 	check whether the current PaintObject may be 
                	//			inside the given rectangle by the other 
                	//			coordinate (by which the list is not sorted).
                	//Secondly,	if (1) is the case, perform a second
                	//			check which is exact; that means it 
                	//			only succeeds if there really are pixels
                	//			printed inside selected rectangle.
                	//			
                	//If one of the two above mentioned tests return false
                	//there may occur PaintObjects that are inside the rectangle 
                	//but are found behind the current item inside the list
                	//(because the list can only be sorted by one parameter)
                	//Thus it is necessary to use this second if clause.
                    if (ls_po_sortedByX.getItem().getSnapshotBounds().y
                    		<= yLocationPageEnd
//                    		&& ls_po_sortedByX.getItem()
//                    		.isInSelectionImage(r_selection)
                    		) {
                    	 

                        	ls_poChronologic.insertSorted(
                        			ls_po_sortedByX.getItem(), 
                        			ls_po_sortedByX.getItem().getElementId());
                    }
                } else {
                	
                	//if the current element starts behind the rectangle
                	//in coordinate by which the list is sorted, 
                	//no PaintObject inserted after this element inside the 
                	//can be printed.
                    behindRectangle = true; 
                }
            } else {
            	
            	//log severe error because of unnecessary PaintObject inside
            	//list.
            	Status.getLogger().severe("Error. Null PaintObject inside"
            			+ " the list of sorted paintObjects.");
            }
            ls_po_sortedByX.next();
        }
        
        
        /*
         * Go through the sorted list of items and paint them
         */
        ls_poChronologic.toFirst();
        while (!ls_poChronologic.isBehind() && !ls_poChronologic.isEmpty()) {

        	ls_poChronologic.getItem().paint(
                    _bi, _final, 
                    Page.getInstance().getJlbl_painting().getBi(),
                    Page.getInstance().getJlbl_painting()
                    .getLocation().x,
                    Page.getInstance().getJlbl_painting()
                    .getLocation().y);
//            //paint the object.
//            if (_final) {
//
//            	//TODO: does it matter in here?
//            } else {
//
//            	ls_poChronologic.getItem().paint(
//                        _bi, _final, _bi,
//                        Page.getInstance().getJlbl_painting()
//                        .getLocation().x,
//                        Page.getInstance().getJlbl_painting()
//                        .getLocation().y);
//            }
        	
        	ls_poChronologic.next();
        }

        
        //print logging method 
        Status.getLogger().info("Painted " 
                    + Status.getCounter_paintedPoints() 
                    + "pixel points for this operation.");

        //finish transaction; adjust the current element to its state
        //before the list transaction.
    	ls_po_sortedByX.finishTransaction();
      
      return _bi;
    }
	
	

    /**
	 * adds a Point to current PaintObject.
	 * @param _pnt the point which is to be added.
	 */
	public void changePaintObject(final DPoint _pnt) {

		
		
		if (po_current == null) {
			
			//throw error message and kill program.
		    Status.getLogger().warning(
					"Es soll ein nicht existentes Objekt veraendert werden.\n"
					+ "Programm wird beendet.");
		}
		if (_pnt.getX() > Status.getImageSize().getWidth() 
		        || _pnt.getX() < 0 
				|| _pnt.getY() > Status.getImageSize().getHeight()
				|| _pnt.getY() < 0) {
			return;
		}
		if (!(po_current instanceof POCurve) 
		        && po_current instanceof PaintObjectWriting) {
		    
		    PaintObjectWriting pow = (PaintObjectWriting) po_current;
		    pow.getPoints().toLast();
	        if (pow.getPoints().isEmpty()) {

	          //add point to PaintObject
	            pow.addPoint(_pnt);
	        } else {

	            pow.addPoint(_pnt);


	            if (pen_current instanceof PenSelection) {

                    ((PenSelection) pow.getPen())
                    .resetCurrentBorderValue();
	            }
	        }
		} else if (po_current instanceof POInsertion 
                || po_current instanceof POLine) {

            POInsertion pow = (POInsertion) po_current;
            if (pow.getPnt_first() != null && pow.getPnt_last() != null) {
                Page.getInstance().getJlbl_painting().refreshPaint();
            }
            
            po_current.addPoint(_pnt);
        } else if (po_current instanceof POCurve) {

            POCurve pow = (POCurve) po_current;
            pow.addPoint(_pnt);
            Page.getInstance().getJlbl_painting().refreshPaint();
        } 

        
        if (pen_current instanceof PenSelection) {

            BufferedImage bi_transformed = 
                    Page.getInstance().getEmptyBISelection();
            bi_transformed = po_current.paint(bi_transformed, false, 
                    bi_transformed, 
                    Page.getInstance().getJlbl_painting().getLocation().x, 
                    Page.getInstance().getJlbl_painting().getLocation().y);


            Page.getInstance().getJlbl_selectionBG().setIcon(
                    new javax.swing.ImageIcon(bi_transformed));
        } else {

            BufferedImage bi_transformed;
            if (po_current instanceof POCurve) {
                bi_transformed = ((PaintObjectWriting) po_current).paint(
                        Page.getInstance().getJlbl_painting().getBi(), 
                        false,
                        Page.getInstance().getJlbl_painting().getBi(), 
                        Page.getInstance().getJlbl_painting().getLocation().x, 
                        Page.getInstance().getJlbl_painting().getLocation().y);
            } else if (po_current instanceof PaintObjectWriting
                    && !(po_current instanceof POCurve)) {
                bi_transformed = ((PaintObjectWriting) po_current).paintLast(
                        Page.getInstance().getJlbl_painting().getBi(), 
                        Page.getInstance().getJlbl_painting().getLocation().x, 
                        Page.getInstance().getJlbl_painting().getLocation().y);
            } else {
                Page.getInstance().getJlbl_painting().refreshRectangle(
                        po_current.getSnapshotBounds().x,
                        po_current.getSnapshotBounds().y,
                        po_current.getSnapshotBounds().width,
                        po_current.getSnapshotBounds().height);

                bi_transformed = po_current.paint(
                        Page.getInstance().getJlbl_painting().getBi(), 
                        false,
                        Page.getInstance().getJlbl_painting().getBi(), 
                        Page.getInstance().getJlbl_painting().getLocation().x, 
                        Page.getInstance().getJlbl_painting().getLocation().y);
            }
            Page.getInstance().getJlbl_painting().setBi(bi_transformed);
            Page.getInstance().getJlbl_painting().setIcon(
                    new javax.swing.ImageIcon(bi_transformed));

            
        }
        
        //set uncommitted changes.
        Status.setUncommittedChanges(true);
	}
	
	
	/**
	 * @return the paintObject.
	 */
	public PaintObjectWriting  abortPaintObject() {
	    PaintObjectWriting pow = null;
//	    PaintObjectWriting pow = new PaintObjectWriting(-1, pen_current);
//	    List<DPoint> ls_points = null;
//	    pow.
        if (pen_current instanceof PenSelection) {

            Page.getInstance().getJlbl_painting().paintSelection(po_current, 
                    (PenSelection) pen_current);

        } 
		pen_current.abort();

        if (po_current instanceof PaintObjectWriting) {

            pow = (PaintObjectWriting) po_current;
        }
		po_current = null;
		return pow;
	}
	
	
	/**
	 * Finishes current PaintObject.
	 */
	public void finish() {

		if (po_current == null) {
			
			//throw error message and kill program.
		    Status.getLogger().severe(
					"Es soll ein nicht existentes Objekt beendet werden.\n"
					+ "Programm wird beendet.");
		}
		

		//insert into sorted lists sorted by x and y positions.
		final Rectangle b = po_current.getSnapshotBounds();
		
		if (po_current instanceof POCurve) {
		    POCurve pc = (POCurve) po_current;
		    pc.setReady();
		} else {

	        ls_po_sortedByX.insertSorted(po_current, b.x);
	        new PictureOverview().add(po_current);
	        
	        //reset current instance of PaintObject
	        po_current = null;
		}
		

//        setChanged();
//        notifyObservers(bi_normalSize);
        
        if (!(pen_current instanceof PenSelection)) {

            //set uncommitted changes.
            Status.setUncommittedChanges(true);
        }
	}
	

    /**
     * sets the pen the first time a picture is created (thus do not notify the 
     * history of uncommitted changes).
     * 
     * @param _pen the new pen.
     */
    public void initializePen(final Pen _pen) {

        if (_pen instanceof BallPen) {

            this.pen_current = new BallPen(_pen.getId_operation(),
//            this.pen = new PenKuli(Constants.PEN_ID_POINT,
                    _pen.getThickness(), _pen.getClr_foreground());
        } else if (_pen instanceof PenSelection) {

            this.pen_current = new PenSelection();
        } else if (_pen instanceof Pencil) {

            this.pen_current = new Pencil(_pen.getId_operation(),
                    _pen.getThickness(), _pen.getClr_foreground());
        } else {
            
            //alert user.
            JOptionPane.showMessageDialog(View.getInstance(), 
                    "PROGRAMMIERFEHLER @ paintobjectwriting: " 
                    + "Stift noch nicht hinzugefuegt.");
            
            
            //throw exception
            new java.lang.Error("Fehler: stift noch nicht hinzugefuegt.")
            .printStackTrace();
        }
        
    }
	
	
	/**
	 * sets the pen both to Picture (for the following PaintObjects) and to 
	 * the current PaintObject (which should be null).
	 * 
	 * @param _pen the new pen.
	 */
	public void changePen(final Pen _pen) {
	    
	    //set in picture
		this.pen_current = _pen;
		
		//set in current paint object.
		if (po_current != null && po_current instanceof PaintObjectWriting) {
		    po_current.setPen(pen_current);
		}
        
        //set uncommitted changes.
        Status.setUncommittedChanges(true);
	}
    
    
	
    
    
    /**
     * Paint the selected BufferedImages to a new BufferedImage which has got 
     * the size of the selection. This method is used for copying the 
     * paintObjects as images to clipboard.
     * @return the painted BufferedImage.
     */
    public BufferedImage paintSelectedBI() {
        
        if (CSelection.getInstance().getR_selection() == null) {
            
            Status.getLogger().warning("the selection square does not exist!");
            return null;
        }
        
        BufferedImage bi = new BufferedImage(
                CSelection.getInstance().getR_selection().width + 1, 
                CSelection.getInstance().getR_selection().height + 1, 
                BufferedImage.TYPE_INT_ARGB);

        ls_poSelected.toFirst();
        while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {
            
            PaintObject po = ls_poSelected.getItem();
            
            if (po instanceof PaintObjectWriting) {
                PaintObjectWriting pow = (PaintObjectWriting) po;

                //TODO: zoom, scroll adjust?
                pow.paint(bi, false, bi, 
                        -CSelection.getInstance().getR_selection().x, 
                        -CSelection.getInstance().getR_selection().y);

            } else if (po instanceof PaintObjectImage) {
                PaintObjectImage poi = (PaintObjectImage) po;
                poi.paint(bi, false, bi, 
                        -CSelection.getInstance().getR_selection().x, 
                        -CSelection.getInstance().getR_selection().y);


            } else {
                Status.getLogger().warning("unknown kind of PaintObject" + po);
            }
            ls_poSelected.next();


        }
        return bi;
    }
	

    /**
     * save the picture.
     * @param _wsLoc the path of the location.
     */
    public void saveIMAGE(final String _wsLoc) {
        
        
        BufferedImage bi;
        if (Status.isExportAlpha()) {

            bi = Page.getInstance().getEmptyBITransparent();
        } else {
            bi = Page.getInstance().getEmptyBIWhite();
        }
        

        bi = Utils.getBackgroundExport(bi, 0, 0, Status.getImageSize().width, 
                Status.getImageSize().height, 0, 0);
        
        
        
        
        bi = repaintRectangle(
                -Page.getInstance().getJlbl_painting().getLocation().x + 0, 
                -Page.getInstance().getJlbl_painting().getLocation().y + 0, 
                Status.getImageSize().width, Status.getImageSize().height, 
                bi, true);
                
                
        
        
        try {
            ImageIO.write(bi, Status.getSaveFormat(),
                    new File(_wsLoc + Status.getSaveFormat()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * save the picture.
     * @param _wsLoc the path of the location.
     */
    public BufferedImage calculateImage() {
        
        
        BufferedImage bi;
        if (Status.isExportAlpha()) {

            bi = Page.getInstance().getEmptyBITransparent();
        } else {
            bi = Page.getInstance().getEmptyBIWhite();
        }
        

        bi = Utils.getBackgroundExport(bi, 0, 0, Status.getImageSize().width, 
                Status.getImageSize().height, 0, 0);
        
        
        
        
        bi = repaintRectangle(
                -Page.getInstance().getJlbl_painting().getLocation().x + 0, 
                -Page.getInstance().getJlbl_painting().getLocation().y + 0, 
                Status.getImageSize().width, Status.getImageSize().height, 
                bi, true);
                
                return bi;
        
        
    }
    

    /**
     * save the picture.
     * @param _wsLoc the path of the location.
     */
    public void saveQuickPNG(final String _wsLoc) {
        
        
        ls_po_sortedByX.toFirst();
        Rectangle r = ls_po_sortedByX.getItem().getSnapshotBounds();
        BufferedImage bi = new BufferedImage(
                r.width, r.height, BufferedImage.TYPE_INT_ARGB);
        
        for (int i = 0; i < r.width; i++) {
            for (int j = 0; j < r.height; j++) {
                bi.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
            }
        }
        
        bi = ls_po_sortedByX.getItem().paint(bi, true, bi, 0, 0);
        
        
        try {
            ImageIO.write(bi, "png", new File(_wsLoc));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * save the picture.
     * @param _wsLoc the path of the location.
     */
    public void savePicture(final String _wsLoc) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(_wsLoc));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ls_po_sortedByX);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * save the picture.
     * @param _wsLoc the path of the location.
     */
    public void loadPicture(final String _wsLoc) {
        try {
            FileInputStream fos = new FileInputStream(new File(_wsLoc));
            ObjectInputStream oos = new ObjectInputStream(fos);
            @SuppressWarnings("unchecked")
            List<PaintObject> p = (List<PaintObject>) oos.readObject();
            instance.ls_po_sortedByX = p;
            
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Empty each paintObject.
     */
    public void emptyImage() {
        ls_po_sortedByX = new List<PaintObject>();
        ls_poSelected = new List<PaintObject>();
    }
	
	
	/**
	 * darken image.
	 * @param _poi the PaintObjectImage which is altered
	 */
	public void darken(final PaintObjectImage _poi) {

        BufferedImage bi_snapshot = _poi.getSnapshot();
		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {
				
				final int minus = 30;
				Color c = new Color(bi_snapshot.getRGB(i, j));
				int red = c.getRed() - minus;
				int blue =  c.getBlue() -  minus;
				int green =  c.getGreen() - minus;
				
				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}
				
				bi_snapshot.setRGB(i, j, 
				        new Color(red, green, blue).getRGB());
			}
		}
	}

	
	/**
	 * transform white to alpha.
     * @param _poi the PaintObjectImage which is altered
	 */
	public void transformToAlpha(final PaintObjectImage _poi) {

        BufferedImage bi_snapshot = _poi.getSnapshot();
		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {
				
				Color c = new Color(bi_snapshot.getRGB(i, j));
			
				int red = c.getRed();
				int blue =  c.getBlue();
				int green =  c.getGreen();
				
				final int maxRGB = 255;
				final double multiplyer = 1.5;
				final int five = 5;
				
				int gesamt = maxRGB - (red + green + blue) / (2 + 1);
				gesamt *= multiplyer;
				
				if (gesamt > maxRGB) {
					gesamt = maxRGB - 1;
				}
				red = red * 2 + five;
				green = green * 2 + five;
				blue = blue * 2 + five;

				if (red > maxRGB) {
					red = maxRGB;
				}
				if (green > maxRGB) {
					green = maxRGB;
				}
				if (blue > maxRGB) {
					blue = maxRGB;
				}
				
				bi_snapshot.setRGB(i, j, new Color(
				        red, green, blue, gesamt).getRGB());
			}
		}
	}
	
	
	/**
	 * Transform all PaintObjectImages to alpha.
	 */
	public void transformWhiteToAlpha() {
	    
	    if (ls_po_sortedByX != null) {

	        ls_po_sortedByX.toFirst();
	        while (!ls_po_sortedByX.isBehind() && !ls_po_sortedByX.isEmpty()) {
	            if (ls_po_sortedByX.getItem() instanceof PaintObjectImage) {
	                
	                whiteToAlpha((PaintObjectImage) ls_po_sortedByX.getItem());
	            }
	            ls_po_sortedByX.next();
	        }
	    }
	}
	
	/**
	 * transform white pixel to alpha pixel.
     * @param _poi the PaintObjectImage which is altered
	 */
	private void whiteToAlpha(final PaintObjectImage _poi) {
	    
	    BufferedImage bi_snapshot = _poi.getSnapshot();
	    
		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {
				
				Color c = new Color(bi_snapshot.getRGB(i, j), true);

				int red = c.getRed();
				int blue =  c.getBlue();
				int green =  c.getGreen();

				int alpha;
				final int upTo = 240;
				if (red + green + blue >= (2 + 1) * upTo) {
					alpha = 0;
	                System.out.println("hier");
				} else {
				    final int maxAlpha = 255;
					alpha = maxAlpha;
				}
				
				bi_snapshot.setRGB(i, j, new Color(
				        red, green, blue, alpha).getRGB());
			}
		}
		
		_poi.setImage(bi_snapshot);
	}
	
	
	/**
	 * transform image to gray image.
     * @param _poi the PaintObjectImage which is altered
	 */
	public void blackWhite(final PaintObjectImage _poi) {

        BufferedImage bi_snapshot = _poi.getSnapshot();
        
		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {
				
				Color c = new Color(bi_snapshot.getRGB(i, j));
			
				int red = c.getRed();
				int blue =  c.getBlue();
				int green =  c.getGreen();
				
				
				int gesamt = (red + green + blue) / (2 + 1);
				
				bi_snapshot.setRGB(i, j, new Color(
				        gesamt, gesamt, gesamt).getRGB());
			}
		}
	}
	
	
	/*
	 * Selection methods.
	 */
	
	
	/**
	 * create selected List.
	 */
	public void createSelected() {
	    if (ls_poSelected == null) {
	        ls_poSelected = new List<PaintObject>();
	    } else {
	        if (!ls_poSelected.isEmpty()) {
	            Status.getLogger().warning(
	                    "creating new selection list but list is not empty.");
	        }
	    }
	}
	
	
	/**
	 * insert into selected list.
	 * @param _po the paintObject to be inserted.
	 */
	public synchronized void insertIntoSelected(final PaintObject _po) {


        //deactivates to change operations of selected items
	    if (ls_poSelected == null) {
	        Status.getLogger().warning("insert into null list");
	    } else  if (_po != null) {
	        

	        CTabSelection.activateOp();
	        if (_po instanceof PaintObjectWriting) {
	            
	            PaintObjectWriting pow = (PaintObjectWriting) _po;
	            CTabSelection.getInstance().change(ls_poSelected.isEmpty(), 
	                    pow.getPen().getId_operation(),
	                    pow.getPen().getClr_foreground().getRGB());
	        }
	        
	        ls_poSelected.toFirst();
	        ls_poSelected.insertBehind(_po);   
	        new PictureOverview().addSelected(_po);
	        
	    }
	}
	
	
	/**
	 * Move selected items.
	 * @param _dX the x difference from current position
	 * @param _dY the y difference from current position
	 */
	public synchronized void moveSelected(final int _dX, final int _dY) {
	
	    if (ls_poSelected == null) {
	        return;
	    }
	    ls_poSelected.toFirst();
	    
	    while (!ls_poSelected.isBehind()) {
	        
	        if (ls_poSelected.getItem() instanceof PaintObjectWriting) {

	            PaintObjectWriting pow = (PaintObjectWriting)
	                    ls_poSelected.getItem();
	            pow = movePaintObjectWriting(pow, _dX, _dY);
	            
	        } else if (ls_poSelected.getItem() instanceof PaintObjectImage) {

                PaintObjectImage p = (PaintObjectImage) ls_poSelected.getItem();
                p.move(new Point(_dX, _dY));
            } else if (ls_poSelected.getItem() instanceof POLine) {

                POLine p = (POLine) ls_poSelected.getItem();
                moveLine(p, _dX, _dY);
            } else {
	            Status.getLogger().warning("unknown kind of PaintObject?");
	        }
            ls_poSelected.next();
	    }
	}
	
	/**
	 * 
     * Move PaintObject items.
	 * @param _pow PaintObjectWriting
     * @param _dX the x difference from current position
     * @param _dY the y difference from current position
     * @return the PaintObjectWriting
	 */
	public static PaintObjectWriting movePaintObjectWriting(
	        final PaintObjectWriting _pow, 
	        final int _dX, final int _dY) {

        _pow.getPoints().toFirst();
        _pow.adjustSnapshotBounds(_dX, _dY);
        while (!_pow.getPoints().isBehind()) {
            _pow.getPoints().getItem().setX(
                    _pow.getPoints().getItem().getX() + _dX);
            _pow.getPoints().getItem().setY(
                    _pow.getPoints().getItem().getY() + _dY);
            _pow.getPoints().next();
        }
        return _pow;
    }
	   /**
     * 
     * Move PaintObject items.
     * @param _pow PaintObjectWriting
     * @param _dX the x difference from current position
     * @param _dY the y difference from current position
     * @return the PaintObjectWriting
     */
    public static POLine moveLine(
            final POLine _pow, 
            final int _dX, final int _dY) {

        _pow.getPnt_first().setX(_pow.getPnt_first().getX() + _dX);
        _pow.getPnt_first().setY(_pow.getPnt_first().getY() + _dY);
        
        _pow.getPnt_last().setX(_pow.getPnt_last().getX() + _dX);
        _pow.getPnt_last().setY(_pow.getPnt_last().getY() + _dY);
        return _pow;
    }
    
	/**
	 * Paint the selected items to the selection JLabel.
	 * 
	 * @return whether there is something to be painted or not.
	 */
	public boolean paintSelected() {

	    Page.getInstance().getJlbl_selectionPainting().setLocation(0, 0);
        BufferedImage verbufft = Page.getInstance().getEmptyBISelection();
        BufferedImage verbufft2 = Page.getInstance().getEmptyBISelection();
	    ls_poSelected.toFirst();
	    Rectangle r_max = null;
        while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {
            
            if (ls_poSelected.getItem() != null) {

                //create new Rectangle consisting of the bounds of the current 
                //paitnObject otherwise adjust the existing bounds
                if (r_max == null) {
                    Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
                    r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height 
                            + b.y);
                } else {
                    Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
                    r_max.x = Math.min(r_max.x, b.x);
                    r_max.y = Math.min(r_max.y, b.y);
                    r_max.width = Math.max(r_max.width, b.x + b.width);
                    r_max.height = Math.max(r_max.height, b.y + b.height);
                }
                
                if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
                    PaintObjectWriting pow = 
                            (PaintObjectWriting) ls_poSelected.getItem();
                    pow.enableSelected();
                }
                //paint the object.
                ls_poSelected.getItem().paint(verbufft2, false, verbufft,
                        Page.getInstance().getJlbl_painting().getLocation().x,
                        Page.getInstance().getJlbl_painting().getLocation().y);

                if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
                    PaintObjectWriting pow = 
                            (PaintObjectWriting) ls_poSelected.getItem();
                    pow.disableSelected();
                }

            }
            ls_poSelected.next();
        }
        ls_poSelected.toFirst();
        Page.getInstance().getJlbl_selectionPainting().setIcon(
                new ImageIcon(verbufft));


        if (r_max != null) {
            
            Rectangle realRect = new Rectangle(r_max.x, r_max.y,
                    r_max.width - r_max.x, r_max.height - r_max.y);
    
            //adapt the rectangle to the currently used zoom factor.
            final double cZoomFactorWidth = 1.0 * Status.getImageSize().width
                    / Status.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * Status.getImageSize().height
                    / Status.getImageShowSize().height;
            realRect.x = (int) (1.0 * realRect.x / cZoomFactorWidth);
            realRect.width = (int) (1.0 * realRect.width / cZoomFactorWidth);
            realRect.y = (int) (1.0 * realRect.y / cZoomFactorHeight);
            realRect.height = (int) (1.0 * realRect.height / cZoomFactorHeight);
              
            realRect.x += Page.getInstance().getJlbl_painting().getLocation().x;
            realRect.y += Page.getInstance().getJlbl_painting().getLocation().y;
            
            Page.getInstance().getJlbl_painting().refreshRectangle(
                    realRect.x, realRect.y, realRect.width, realRect.height);
            CSelection.getInstance().setR_selection(realRect,
                    Page.getInstance().getJlbl_painting().getLocation());
            Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                    realRect);

            return true;
        }
        return false;
	}
	
	

    /**
     * Paint the selected items to the selection JLabel.
     * 
     * @return whether there is something to be painted or not.
     */
    public boolean paintSelectedInline() {

        //it occurred that the start point equal to 0. Why?
        int px, py;
    	if (CSelection.getInstance().getPnt_start() == null) {
    		px = 0;
    		py = 0;
    	} else {
    		 px = (int) (CSelection.getInstance().getPnt_start()
    	                .getX()
    	                - Page.getInstance().getJlbl_painting()
    	                .getLocation().getX());
    		 py = (int) (CSelection.getInstance().getPnt_start()
    				 .getY()
    				 - Page.getInstance().getJlbl_painting()
    				 .getLocation().getY());

    	}
               
        BufferedImage verbufft = Page.getInstance().getEmptyBISelection();
        BufferedImage verbufft2 = Page.getInstance().getEmptyBISelection();
        ls_poSelected.toFirst();
        Rectangle r_max = null;
        while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {
            
            if (ls_poSelected.getItem() != null) {

                //create new Rectangle consisting of the bounds of the current 
                //paitnObject otherwise adjust the existing bounds
                if (r_max == null) {
                    Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
                    r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height 
                            + b.y);
                } else {
                    Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
                    r_max.x = Math.min(r_max.x, b.x);
                    r_max.y = Math.min(r_max.y, b.y);
                    r_max.width = Math.max(r_max.width, b.x + b.width);
                    r_max.height = Math.max(r_max.height, b.y + b.height);
                }
                
                if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
                    PaintObjectWriting pow = 
                            (PaintObjectWriting) ls_poSelected.getItem();
                    pow.enableSelected();
                }
                //paint the object.
                ls_poSelected.getItem().paint(verbufft2, false, verbufft,
                        Page.getInstance().getJlbl_painting().getLocation().x
                        - px,
                        Page.getInstance().getJlbl_painting().getLocation().y
                        - py);

                if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
                    PaintObjectWriting pow = 
                            (PaintObjectWriting) ls_poSelected.getItem();
                    pow.disableSelected();
                }

            }
            ls_poSelected.next();
        }
        ls_poSelected.toFirst();
        Page.getInstance().getJlbl_selectionPainting().setIcon(
                new ImageIcon(verbufft));


        if (r_max != null) {
            
            Rectangle realRect = new Rectangle(r_max.x, r_max.y,
                    r_max.width - r_max.x, r_max.height - r_max.y);
    
            //adapt the rectangle to the currently used zoom factor.
            final double cZoomFactorWidth = 1.0 * Status.getImageSize().width
                    / Status.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * Status.getImageSize().height
                    / Status.getImageShowSize().height;
            realRect.x = (int) (1.0 * realRect.x / cZoomFactorWidth);
            realRect.width = (int) (1.0 * realRect.width / cZoomFactorWidth);
            realRect.y = (int) (1.0 * realRect.y / cZoomFactorHeight);
            realRect.height = (int) (1.0 * realRect.height / cZoomFactorHeight);
              
            realRect.x += Page.getInstance().getJlbl_painting().getLocation().x;
            realRect.y += Page.getInstance().getJlbl_painting().getLocation().y;
            
            Page.getInstance().getJlbl_painting().refreshRectangle(
                    realRect.x, realRect.y, realRect.width, realRect.height);
            CSelection.getInstance().setR_selection(realRect,
                    Page.getInstance().getJlbl_painting().getLocation());
            Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                    realRect);
            return true;
        }
        return false;
    }
	
	
	/**
	 * release selected elements to normal list.
	 */
	public synchronized void releaseSelected() {

        //adjust the bounds of the selected items to the performed
        //scrolling
	    if (CSelection.getInstance().getOldPaintLabelLocation() != null) {

            int oldX = (int) CSelection.getInstance().getOldPaintLabelLocation()
                    .getX();
            int oldY = (int) CSelection.getInstance().getOldPaintLabelLocation()
                    .getY();

            int newX = (int) Page.getInstance().getJlbl_painting().getLocation()
                    .getX();
            int newY = (int) Page.getInstance().getJlbl_painting().getLocation()
                    .getY();
            
	        Picture.getInstance().moveSelected(oldX - newX, oldY - newY);
	        CSelection.getInstance().setOldPaintLabelLocation(null);
	    }
        
	    //deactivates to change operations of selected items
	    CTabSelection.deactivateOp();
	    if (ls_poSelected == null) {
	        Status.getLogger().info("o selected elements");
	        return;
	    }
	    ls_poSelected.toFirst();
	    while (!ls_poSelected.isEmpty()) {
	        
	        PaintObject po = ls_poSelected.getItem();

        	if (po == null) {
        		Status.getLogger().warning("error: empty list item");
        	}
        	
	        new PictureOverview().removeSelected(po);
	        
	        
	        if (po instanceof PaintObjectWriting) {
	            PaintObjectWriting pow = (PaintObjectWriting) po;
                new PictureOverview().add(pow);
	            ls_po_sortedByX.insertSorted(pow, pow.getSnapshotBounds().x);
	        } else if (po instanceof PaintObjectImage) {
	            PaintObjectImage poi = (PaintObjectImage) po;
                new PictureOverview().add(poi);

                ls_po_sortedByX.insertSorted(poi, poi.getSnapshotBounds().x);
	        } else if (ls_poSelected.getItem() instanceof POLine) {
                
	            POLine p = (POLine) ls_poSelected.getItem();
                p.recalculateSnapshotBounds();
	            new PictureOverview().add(p);

                ls_po_sortedByX.insertSorted(p, p.getSnapshotBounds().x);
            } else if (po != null) {
	            Status.getLogger().warning("unknown kind of PaintObject"
	                    + po);
	        }
	        ls_poSelected.remove();
	        ls_poSelected.toFirst();
	    }
	    ls_poSelected = null;
	}
    /**
     * release selected elements to normal list.
     */
    public synchronized void deleteSelected() {
        
        if (ls_poSelected == null) {
            Status.getLogger().info("o selected elements");
            return;
        }
        ls_poSelected.toFirst();
        while (!ls_poSelected.isEmpty()) {
            
        	if (ls_poSelected.getItem() == null) {
        		Status.getLogger().warning("error: empty list item");
        	}
        	
            new PictureOverview().removeSelected(
                    ls_poSelected.getItem());
            ls_poSelected.remove();
            
            ls_poSelected.toFirst();
        }
        //deactivates to change operations of selected items
        CTabSelection.deactivateOp();
        ls_poSelected = null;
    }
	
	/*
	 * save and load
	 */
	
	
	/**
	 * load an image to the picture. and write it into bi_resized.
	 * 
	 * @param _wsLoc the location of the image
	 * @return the size of the image
	 */
	public DPoint load(final String _wsLoc) {
	    BufferedImage bi_normalSize;
		try {
			BufferedImage bi_unnormalSchaizz = ImageIO.read(new File(_wsLoc));
			bi_normalSize = new BufferedImage(bi_unnormalSchaizz.getWidth(), 
			        bi_unnormalSchaizz.getHeight(), 
			        BufferedImage.TYPE_INT_ARGB);
			
			for (int x = 0; x < bi_unnormalSchaizz.getWidth(); x++) {

	            for (int y = 0; y < bi_unnormalSchaizz.getHeight(); y++) {
	                bi_normalSize.setRGB(x, y, bi_unnormalSchaizz.getRGB(x, y));
	            }
			}
			
			Status.setImageSize(new Dimension(bi_normalSize.getWidth(), 
                    bi_normalSize.getHeight()));
			Status.setImageShowSize(new Dimension(bi_normalSize.getWidth(), 
			        bi_normalSize.getHeight()));
			
			if (ls_po_sortedByX == null) {
			    createSelected();
			}
			
			ls_po_sortedByX.toFirst();
			ls_po_sortedByX.insertBehind(createPOI(bi_normalSize));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new DPoint(Status.getImageSize().getWidth(), 
		        Status.getImageSize().getHeight());
	}
	
	
	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static Picture getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
		    
			instance = new Picture();
			instance.initialize();
		}
		
		//return the only instance of this class.
		return instance;
	}
	
	/*
	 * getter and setter methods
	 */

	/**
	 * @return the pen_current
	 */
	public Pen getPen_current() {
		return pen_current;
	}


    /**
     * @return the ls_po_sortedByX
     */
    public List<PaintObject> getLs_po_sortedByX() {
        return ls_po_sortedByX;
    }


    /**
     * @param _ls_po_sortedByX the ls_po_sortedByX to set
     */
    public void setLs_po_sortedByX(final List<PaintObject> _ls_po_sortedByX) {
        this.ls_po_sortedByX = _ls_po_sortedByX;
    }

    /**
     * @return the ls_poSelected
     */
    public List<PaintObject> getLs_poSelected() {
        return ls_poSelected;
    }

    /**
     * Returns whether the current paint object is equal to zero; thus the 
     * Picture is ready for creating a new PaintObject.
     * 
     * Used because there are PaintObjects (such as POCurve) that need multiple
     * mouse presses, and releases.
     * 
     * Thus the Controller class for the painting has to check whether there is 
     * a paintObejct ready if the 'new' PaintObject would be of one of the 
     * above mentioned types.
     * 
     * @return whether the current paintObject does not exist.
     */
    public boolean isPaintObjectReady() {

        return (po_current == null);
    }

    
    
    /**
     * Set the pen after the user clicked at a pen button.
     * Thus clone the pen and change its color afterwards to the last used one.
     * 
     * @param _pen the pen
     * @param _id whether pen 1 or pen 2.
     */
    public void userSetPen(final Pen _pen, final int _id) {

        Pen pen = Pen.clonePen(_pen);
        if (_id == 1) {
            pen.setClr_foreground(Status.getPenSelected1().getClr_foreground());
            Status.setPenSelected1(pen);
        } else if (_id == 2) {
            pen.setClr_foreground(Status.getPenSelected2().getClr_foreground());
            Status.setPenSelected2(pen);
        } else {
            Status.getLogger().severe("wrong identifier.");
        }
    }

}
