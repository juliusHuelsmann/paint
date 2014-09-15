//package declaration
package model.objects.painting;

//import declarations
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import settings.Error;
import settings.Status;
import view.ViewVorschau;
import view.forms.Page;
import model.objects.PictureOverview;
import model.objects.pen.Pen;
import model.objects.pen.special.PenSelection;
import model.util.list.List;

/**
 * 
 * @author Julius Huelsmann
 *
 */
public final class Picture extends Observable {
	
	/**
	 * the only instance of this class.
	 */
	private static Picture instance = null;

	/**
	 * list of PaintObjects.
	 */
	private List<PaintObject> ls_po_sortedByX, ls_poSelected;

	/**
	 * buffered image with dimension of imageSize. not resized if the user 
	 * zooms in or out, exists just for the purpose of being able to handle
	 * actions like PIPETTE or FILL. It is possible to save this BufferedImage
	 * at save operation directly to hard drive.
	 */
	private BufferedImage bi_normalSize;
	
	/**
	 * current PaintObject which can be altered.
	 */
	private PaintObjectWriting po_current;
	
	/**
	 * the pen which is given to new PaintObject if created.
	 */
	private Pen pen_current;
	
	/**
	 * current id which is given to new PaintObject and then increased.
	 */
	private int currentId;

    
	
	/**
	 * Empty utility class constructor.
	 */
	private Picture() { }
	
	/**
	 * creates new instance of list.
	 */
	private void initialize() {

	    reload();
	    
        //add observer which prints preview.
        super.addObserver(ViewVorschau.getInstance());
	}
	
	
	/**
	 * reload the image.
	 */
	public void reload() {

        //initialize the BufferedImage
        bi_normalSize = new BufferedImage(Status.getImageSize().width, 
                Status.getImageSize().height, BufferedImage.TYPE_INT_ARGB);
        
        //initialize with colors.
        for (int x = 0; x < bi_normalSize.getWidth(); x++) {
            for (int y = 0; y < bi_normalSize.getHeight(); y++) {
                bi_normalSize.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
            }
        }

        //initialize both lists ordered by 
        this.ls_po_sortedByX = new List<PaintObject>();
        
        //save current id
        this.currentId = 0;
        
	}
	
	/**
     * adds a new PaintObject to list.
     */
    public void addPaintObjectWrinting() {
        
        if (po_current != null) {
            
            //throw error message and kill program.
            System.err.println("Fehler in Klasse " + getClass().getName() 
                    + "Es soll ein neues objekt geadded werden, obwohl das "
                    + "alte nicht null ist also nicht gefinished wurde.\n"
                    + "Programm wird beendet.");
            
            System.exit(1);
        }
        
        //create new PaintObject and insert it into list of 
        po_current = new PaintObjectWriting(currentId, pen_current);

        //increase current id
        currentId++;
        
        //set uncommitted changes.
        Status.setUncommittedChanges(true);
    }
    
    /**
     * adds a new PaintObject to list.
     * @param _bi the BufferedImage which is to be transformed into ImagePO.
     */
    public void addPaintObjectImage(final BufferedImage _bi) {
        
        if (po_current != null) {
            
            //throw error message and kill program.
            System.err.println("Fehler in Klasse " + getClass().getName() 
                    + "Es soll ein neues objekt geadded werden, obwohl das "
                    + "alte nicht null ist also nicht gefinished wurde.\n"
                    + "Programm wird beendet.");
            
            System.exit(1);
        }
        
        //create new PaintObject and insert it into list of 
        ls_po_sortedByX.insertAfterHead(new PaintObjectImage(currentId, _bi));

        //increase current id
        currentId++;
        
        //set uncommitted changes.
        Status.setUncommittedChanges(true);
    }
	
	
	/**
	 * repaint a rectangle. For selection purpose.
	 * @param _r the rectangle
	 */
	public void repaintRectangle(final Rectangle _r) {
        
        //set rectangle alpha
        for (int x = _r.x; x < _r.width + _r.x; x++) {
            for (int y = 0; y < _r.height + _r.y; y++) {
                bi_normalSize.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
            }
        }
    }
	
	/**
	 * clear.
	 */
    public void clear() {
        
        //set rectangle alpha
        for (int x = 0; x < bi_normalSize.getWidth(); x++) {
            for (int y = 0; y < bi_normalSize.getHeight(); y++) {
                bi_normalSize.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
            }
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
        return bi_normalSize.getRGB(_pxX, _pxY);
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

        double time0 = System.currentTimeMillis();
        BufferedImage ret = emptyRectangle(_x, _y, _width, _height, 
                _graphicX, _graphiY, _bi);
        Page.getInstance().getJlbl_painting().setBi(ret);
        Page.getInstance().getJlbl_painting().setIcon(new ImageIcon(ret));

        Page.getInstance().getJlbl_painting().repaint();

        double time1 = System.currentTimeMillis();
        System.out.println("empty time" + (time1 - time0));
      
        ret =  repaintRectangle(_x, _y, _width, _height, 
                _graphicX, _graphiY, ret);
        
        double time2 = System.currentTimeMillis();
        System.out.println("repaint time" + (time2 - time1));
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
        
        PaintBI.fillRectangleQuick(_bi, Color.white, 
                new Rectangle(_graphicX, _graphiY, rectWidth, rectHeight));
        
        return _bi;

    }
    
    
    /**
     * Repaint a rectangle without clearing the screen.
     * 
     * @param _x the x coordinate
     * @param _y the y coordinate
     * @param _width the width 
     * @param _height the height
     * @param _graphicX the graphics x
     * @param _graphiY the graphics y.
     * 
     * @param _bi the BufferedImage
     * @return the graphics
     */
    public synchronized BufferedImage repaintRectangle(final int _x,
            final int _y, final int _width, final int _height,
            final int _graphicX, final int _graphiY,
            final BufferedImage _bi) {

        //alle die in Frage kommen neu laden.
        if (ls_po_sortedByX == null
                || _bi == null) {
            return _bi;
        }
        Status.setCounter_paintedPoints(0);
        boolean behindRectangle = false;
        ls_po_sortedByX.toFirst();

        while (!ls_po_sortedByX.isEmpty() && !ls_po_sortedByX.isBehind() 
                && !behindRectangle) {
            
            if (ls_po_sortedByX.getItem() != null) {
                
                System.out.println("inner loop");
                //check whether the current PaintObject is in the given 
                //rectangle
                if (ls_po_sortedByX.getItem().getSnapshotBounds().x 
                        <= -Page.getInstance().getJlbl_painting()
                        .getLocation().getX()
                        + Page.getInstance().getJlbl_painting().getWidth()) {
                    
                    //paint the object.
                    ls_po_sortedByX.getItem().paint(
                            bi_normalSize, false, _bi,
                            Page.getInstance().getJlbl_painting().getLocation().x,
                            Page.getInstance().getJlbl_painting().getLocation().y);
                } else {
                   behindRectangle = true; 
                }
            }
            ls_po_sortedByX.next();
        }
        ls_po_sortedByX.toFirst();

        System.out.println("Painted " 
                    + Status.getCounter_paintedPoints() 
                    + "pixel points for this operation.");
        
//      g_imageWork.translate(0, 0);
      
      

      //notify preview-observer
      setChanged();
      notifyObservers(bi_normalSize);
      
      
      return _bi;
    }
	
	
	
	
	
	/**
	 * adds a Point to current PaintObject.
	 * @param _pnt the point which is to be added.
	 */
	public void changePaintObject(final Point _pnt) {

		
		
		if (po_current == null) {
			
			//throw error message and kill program.
			System.err.println("Fehler in Klasse " + getClass().getName() 
					+ "Es soll ein nicht existentes Objekt veraendert werden.\n"
					+ "Programm wird beendet.");
			
			System.exit(1);
		}
		if (_pnt.getX() > bi_normalSize.getWidth() || _pnt.getX() < 0 
				|| _pnt.getY() > bi_normalSize.getHeight() || _pnt.getY() < 0) {
			return;
		}


		po_current.getPoints().toLast();
		if (po_current.getPoints().isEmpty()) {

	      //add point to PaintObject
	      po_current.addPoint(_pnt);
		} else {

		    final int minimalDistance = 1;
	        int dx = po_current.getPoints().getItem().x - _pnt.x;
	        int dy = po_current.getPoints().getItem().y - _pnt.y;
	        if (Math.sqrt(dx * dx + dy * dy) > minimalDistance) {
	            po_current.addPoint(_pnt);

	            try {

	                ((PenSelection) po_current.getPen())
	                .resetCurrentBorderValue();
	            } catch (Exception e) {
	                Error.printError(getClass().getSimpleName() , 
	                        "changePaintObject", "Class cast", e, 
	                        Error.ERROR_MESSAGE_DO_NOTHING);
	            }
	        }
		}
        BufferedImage bi_transformed = po_current.paint(
                Page.getInstance().getJlbl_painting().getBi(), 
                false, 
                Page.getInstance().getJlbl_painting().getBi(), 
                Page.getInstance().getJpnl_toMove().getX(), 
                Page.getInstance().getJpnl_toMove().getY());

        Page.getInstance().getJlbl_painting().setBi(bi_transformed);
        Page.getInstance().getJlbl_painting().setIcon(
                new javax.swing.ImageIcon(bi_transformed));
        
        //set uncommitted changes.
        Status.setUncommittedChanges(true);
	}
	
	
	/**
	 * 
	 */
	public void abortPaintObject() {

		pen_current.abort();
		po_current = null;
	}
	
	
	/**
	 * Finishes current PaintObject.
	 */
	public void finish() {

		if (po_current == null) {
			
			//throw error message and kill program.
			System.err.println("Fehler in Klasse " + getClass().getName() 
					+ "Es soll ein nicht existentes Objekt beendet werden.\n"
					+ "Programm wird beendet.");
			
			System.exit(1);
		}
		
		
//		//update the current instance of BufferedImage and paint to 
//		//graphics as well. Here, it is not possible to check whether
//		//items are in range, because the current PaintObject shell be
//		//entirely painted to both bufferedImage and screen.
		bi_normalSize = po_current.paint(bi_normalSize, true, 
		        Page.getInstance().getJlbl_painting().getBi(),
                Page.getInstance().getJpnl_toMove().getX(),
                Page.getInstance().getJpnl_toMove().getY());
		PictureOverview.getInstance().add(po_current);

        BufferedImage bi_transformed = po_current.paint(
                Page.getInstance().getJlbl_painting().getBi(), 
                true, 
                Page.getInstance().getJlbl_painting().getBi(), 
                Page.getInstance().getJpnl_toMove().getX(), 
                Page.getInstance().getJpnl_toMove().getY());

        Page.getInstance().getJlbl_painting().setBi(bi_transformed);

		//insert into sorted lists sorted by x and y positions.
		final Rectangle b = po_current.getSnapshotBounds();
        ls_po_sortedByX.insertSorted(po_current, b.x);
        
        
		//reset current instance of PaintObject
		po_current = null;

		//notify preview-observer
        setChanged();
        notifyObservers(bi_normalSize);
        
        //set uncommitted changes.
        Status.setUncommittedChanges(true);
	}
	

    /**
     * sets the pen the first time a picture is created (thus do not notify the 
     * history of uncommitted changes).
     * 
     * @param _pen the new pen.
     */
    public void initializePen(final Pen _pen) {

        //set in picture
        this.pen_current = _pen;
        
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
		if (po_current != null) {
		    po_current.setPen(pen_current);
		}
        
        //set uncommitted changes.
        Status.setUncommittedChanges(true);
	}
    
    
    
    /**
     * change color of current pen.
     * @param _clr the new color.
     */
    public void changeColor(final Color _clr) {
        
        //set in picture
        pen_current.setClr_foreground(
                new Color(_clr.getRed(), _clr.getGreen(), _clr.getBlue()));
        
        //set in current paint object
        if (po_current != null) {
            po_current.changeColor(
                    new Color(_clr.getRed(), _clr.getGreen(), 
                            _clr.getBlue()));   
        }

        //set uncommitted changes.
        Status.setUncommittedChanges(true);
    }
	
	

    /**
     * save the picture.
     * @param _wsLoc the path of the location.
     */
    public void savePNG(final String _wsLoc) {
        try {
            ImageIO.write(bi_normalSize, "png", new File(_wsLoc));
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	
	/**
	 * darken image.
	 */
	public void darken() {
		for (int i = 0; i < bi_normalSize.getWidth(); i++) {
			for (int j = 0; j < bi_normalSize.getHeight(); j++) {
				
				final int minus = 30;
				Color c = new Color(bi_normalSize.getRGB(i, j));
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
				
				bi_normalSize.setRGB(i, j, 
				        new Color(red, green, blue).getRGB());
			}
		}
	}

	
	/**
	 * transform white to alpha.
	 */
	public void transformToAlpha() {
		for (int i = 0; i < bi_normalSize.getWidth(); i++) {
			for (int j = 0; j < bi_normalSize.getHeight(); j++) {
				
				Color c = new Color(bi_normalSize.getRGB(i, j));
			
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
				
				bi_normalSize.setRGB(i, j, new Color(
				        red, green, blue, gesamt).getRGB());
			}
		}
	}
	
	
	/**
	 * transform white pixel to alpha pixel.
	 */
	public void whiteToAlpha() {
		for (int i = 0; i < bi_normalSize.getWidth(); i++) {
			for (int j = 0; j < bi_normalSize.getHeight(); j++) {
				
				Color c = new Color(bi_normalSize.getRGB(i, j));
			
				int red = c.getRed();
				int blue =  c.getBlue();
				int green =  c.getGreen();

				int alpha;
				final int upTo = 240;
				if (red + green + blue >= (2 + 1) * upTo) {
					alpha = 0;
				} else {
				    final int maxAlpha = 255;
					alpha = maxAlpha;
				}
				
				bi_normalSize.setRGB(i, j, new Color(
				        red, green, blue, alpha).getRGB());
			}
		}
	}
	
	
	/**
	 * transform image to gray image.
	 */
	public void blackWhite() {

		for (int i = 0; i < bi_normalSize.getWidth(); i++) {
			for (int j = 0; j < bi_normalSize.getHeight(); j++) {
				
				Color c = new Color(bi_normalSize.getRGB(i, j));
			
				int red = c.getRed();
				int blue =  c.getBlue();
				int green =  c.getGreen();
				
				
				int gesamt = (red + green + blue) / (2 + 1);
				
				bi_normalSize.setRGB(i, j, new Color(
				        gesamt, gesamt, gesamt).getRGB());
			}
		}
	}
	
	
	
	
	/**
	 * create selected List.
	 */
	public void createSelected() {
	    if (ls_poSelected == null) {
	        ls_poSelected = new List<PaintObject>();
	    } else {
	        if (!ls_poSelected.isEmpty()) {
	            System.err.println("error:" + getClass() 
	                    + "creating new selection list but list is not empty.");
	        }
	    }
	}
	
	
	/**
	 * insert into selected list.
	 * @param _po the paintObject to be inserted.
	 */
	public void insertIntoSelected(final PaintObject _po) {

	    if (ls_poSelected == null) {
	        System.err.println("error. insert into null list" + getClass());
	        System.exit(1);
	    } else {
	        
	        ls_poSelected.insertSorted(_po, _po.getSnapshotBounds().x);   
	    }
	}
	
	
	/**
	 * load an image to the picture. and write it into bi_resized.
	 * 
	 * @param _wsLoc the location of the image
	 * @return the size of the image
	 */
	public Point load(final String _wsLoc) {
		try {
			bi_normalSize = ImageIO.read(new File(_wsLoc));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new Point(bi_normalSize.getWidth(), bi_normalSize.getHeight());
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
     * @return the bi_normalSize
     */
    public BufferedImage getBi_normalSize() {
        return bi_normalSize;
    }

    /**
     * @param _bi_normalSize the bi_normalSize to set
     */
    public void setBi_normalSize(final BufferedImage _bi_normalSize) {
        this.bi_normalSize = _bi_normalSize;
    }
}
