package control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import control.forms.CNew;
import control.interfaces.MenuListener;
import control.tabs.CExport;
import control.tabs.CLook;
import control.tabs.CPaintObjects;
import control.tabs.CPaintStatus;
import control.tabs.CPrint;
import control.tabs.CQuickAccess;
import control.tabs.CTabSelection;
import control.tabs.CTabs;
import control.tabs.CWrite;
import control.tabs.ControlTabPainting;
import control.util.implementations.Item2ActivityListener;
import control.util.implementations.ScrollPaneActivityListener;
import model.objects.PictureOverview;
import model.objects.Zoom;
import model.objects.painting.PaintBI;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.Pen;
import model.objects.pen.special.PenSelection;
import model.settings.Constants;
import model.settings.ReadSettings;
import model.settings.Settings;
import model.settings.Status;
import model.settings.ViewSettings;
import model.util.DPoint;
import model.util.Util;
import model.util.adt.list.List;
import model.util.adt.list.SecureList;
import view.View;
import view.forms.Message;
import view.forms.Page;
import view.forms.Tabs;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ControlPaint implements MouseListener, MouseMotionListener, 
MenuListener {

	/**
	 * Central view class.
	 */
	private View view;
	
	
	/**
	 * Central model class.
	 */
	private Picture picture;
	
	private CTabs cTabs;
	
	
	
	
	/**
	 * Controller class for selection paint at the same level as this class.
	 */
	private ControlPaintSelectin controlPaintSelection;
	
	
	/**
	 * Controller class for the painting tab.
	 */
	private ControlTabPainting cTabPaint;
	
	private CPrint cTabPrint; 
	
	private CWrite cTabWrite;
	private CQuickAccess controlQuickAccess;
	
	private CPaintStatus cTabPaintStatus;
	
	private CTabSelection cTabSelection;
	private CExport cTabExport;
	private CLook cTabLook;
	
	private CPaintObjects cTabPaintObjects;
	
	private ContorlPicture controlPic;
	
	
	private Zoom zoom;
	private CNew controlnew = null;
	
	private ScrollPaneActivityListener utilityControlScrollPane;
	private Item2ActivityListener utilityControlItem2;
	

    /**
     * Start point mouseDragged and the speed of movement for continuous 
     * movement.
     */
    //TODO: quadratic? movement function which interpolates a time interval
    //of x milliseconds. Computation possible. Speed? Maybe other solutions?
    private Point pnt_start, pnt_last, pnt_movementSpeed;

    /**
     * The start location of movement of the JPaintLabel.
     */
    private Point pnt_startLocation;
    
    /**
     * Erase field which contains the indices.
     */
    private byte [][] field_erase;
    
    /**
     * The thread for moving at the page.
     */
    private Thread thrd_move;

    
    private Page getPage() {
    	return view.getPage();
    }
    
    private Tabs getTabs() {
    	return view.getTabs();
    }
	
	/**
	 * 
	 */
	public ControlPaint() {

		

        //get location of current workspace and set logger level to finest; 
		//thus every log message is shown.
        Settings.setWsLocation(ReadSettings.install());
        Status.getLogger().setLevel(Level.WARNING);

        //if the installation has been found, initialize the whole program.
        //Otherwise print an error and exit program
        if (!Settings.getWsLocation().equals("")) {
            
        	//Print logger status to console.
            Status.getLogger().info("Installation found.");
            Status.getLogger().info("Initialize model class Page.\n");
            
            Status.setControlPaint(this);

            utilityControlScrollPane = new ScrollPaneActivityListener(this);
            //initialize the model class picture.
            //TODO: not null
            cTabWrite = new CWrite(this);
            controlPic = new ContorlPicture(this);
            cTabSelection = new CTabSelection(this);
            picture = Picture.getInstance();
            controlPaintSelection = new ControlPaintSelectin(this);
            cTabPaint = new ControlTabPainting(this);
            cTabPaintStatus = new CPaintStatus(this);
            cTabPaintObjects = new CPaintObjects(this);
            zoom = new Zoom(controlPic);
            
            utilityControlItem2 = new Item2ActivityListener(this);
            cTabs = new CTabs(this);

            //initialize view class and log information on current 
            //initialization progress
            Status.getLogger().info("initialize view class and set visible.");
            view = new View();
            view.initialize(this);
            view.setVisible(true);
            
            
            //enable current operation
            view.getTabs().getTab_paint().getTb_color1().setActivated(true);
            view.getTabs().getTab_paint().getIt_stift1().getTb_open().setActivated(true);
           
            
            /*
             * Initialize control
             */
            Status.getLogger().info("initialize controller class.");
            
            Status.getLogger().info(
                    "Start handling actions and initialize listeners.\n");

            Status.getLogger().info("initialization process completed.\n\n"
                    + "-------------------------------------------------\n");
        } else {

            //if not installed and no installation done print error and write
        	//null values into final variables
        	Status.getLogger().severe("Fatal error: no installation found");
        	this.view = null;
        	this.picture = null;
        	
        	//exit program
            System.exit(1);
        }
	
	}
	
	
	

	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(final MouseEvent arg0) { }

	public void mouseEntered(MouseEvent arg0) { }

	public void mouseExited(MouseEvent _event) {



	       if (_event.getSource().equals(
	        		getPage().getJlbl_painting())) { 
	        	
	        	
	        	//remove old pen - position - indication - point
	        	if (!Picture.getInstance().isSelected()) {
	        		switch (Status.getIndexOperation()) {

		        	case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
		        	case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
		            case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
		            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
		            case Constants.CONTROL_PAINTING_INDEX_PAINT_1:

		            	BufferedImage bi = Util.getEmptyBISelection();
		            	getPage().getJlbl_selectionBG().setIcon(
		            			new ImageIcon(bi));
		            	break;
	                default:
	                	break;
	        		}
	        	}
	        }		
	}

	public void mousePressed(final MouseEvent _event) {

        if (_event.getSource().equals(
                getPage().getJlbl_painting())) {

            getTabs().closeMenues();
            
            // switch index of operation
            switch (Status.getIndexOperation()) {

            case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
            case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
            case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
            case Constants.CONTROL_PAINTING_INDEX_PAINT_1:

                if ((Status.getIndexOperation() 
                        != Constants.CONTROL_PAINTING_INDEX_I_G_CURVE
                        && Status.getIndexOperation() 
                        != Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2)
                        || Picture.getInstance().isPaintObjectReady()) {
                    

                    // set currently selected pen. Differs if
                    if (_event.getButton() == MouseEvent.BUTTON1) {

                        if (Status.getIndexOperation() 
                                == Constants.CONTROL_PAINTING_INDEX_PAINT_1) {
                            Picture.getInstance().changePen(Pen.clonePen(
                                    Status.getPenSelected1()));

                        } else {

                            Picture.getInstance().changePen(Pen.clonePen(
                                    Status.getPenSelected2()));
                        }
                    } else if (_event.getButton() == MouseEvent.BUTTON3) {

                        if (Status.getIndexOperation() 
                                == Constants.CONTROL_PAINTING_INDEX_PAINT_1) {
                            Picture.getInstance().changePen(Pen.clonePen(
                                    Status.getPenSelected2()));
                        } else {


                            Picture.getInstance().changePen(Pen.clonePen(
                                    Status.getPenSelected1()));

                        }
                    }
                }
                    
                // add paintObject and point to Picture
                Picture.getInstance().addPaintObject(getTabs().getTab_insert());
                changePO(_event);
                break;
            
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

                // abort old paint object
                Picture.getInstance().abortPaintObject(controlPic);

                // change pen and add new paint object
                Picture.getInstance().changePen(new PenSelection());
                Picture.getInstance().addPaintObjectWrinting();
                break;

            case Constants.CONTROL_PAINTING_INDEX_ERASE:
            	
            	mr_erase(_event.getPoint());
            	if (0 == -1) {
            		/*
                	 * Initialize the field for erasing. Therefore calculate the 
                	 * dimension of the array first.
                	 */
                	final int width = 
                	Math.min(
                			
                			//The theoretically displayed width of the image
                			//if the image would not terminate somewhere
                			//on screen.
                			getPage().getJlbl_painting().getWidth()
                			* Status.getImageSize().width
                			/ Status.getImageShowSize().width
                			/ Status.getEraseRadius(),
                			
                			//the maximal displayed image size adapted to the
                			//erase radius.
                			Status.getImageSize().width / Status.getEraseRadius());
                	
                	final int height = 
                	Math.min(
                			
                			//The theoretically displayed height of the image
                			//if the image would not terminate somewhere
                			//on screen.
                			getPage().getJlbl_painting().getHeight()
                			* Status.getImageSize().height
                			/ Status.getImageShowSize().height
                			/ Status.getEraseRadius(),

                			//the maximal displayed image size adapted to the
                			//erase radius.
                			Status.getImageSize().height / Status.getEraseRadius());
                	
                	//initialize the field.
                	field_erase = new byte[width][height];
                	
//                	//set default values
//                	for (int x = 0; x < field_erase.length; x++) {
//    					for (int y = 0; y < field_erase[x].length; y++) {
//    	            		field_erase[x][y] = PaintBI.OCCUPIED;
//    					}
//    				}
            	}
            	
            	
            	break;
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                Picture.getInstance().abortPaintObject(controlPic);
                Picture.getInstance().changePen(new PenSelection());
                Picture.getInstance().addPaintObjectWrinting();
                break;
            case Constants.CONTROL_PAINTING_INDEX_PIPETTE:

                break;
            case Constants.CONTROL_PAINTING_INDEX_MOVE:

                
                pnt_start = _event.getPoint();
                pnt_startLocation = getPage().getJlbl_painting()
                        .getLocation();
                break;
            default:
                break;

            }
        }
    }

	public void mouseReleased(MouseEvent _event) {
		if (_event.getSource().equals(
                getPage().getJlbl_painting())) {
			mr_painting(_event);
		} 
	}



	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}



	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}



	public void mouseDragged(MouseEvent _event) {


        // left mouse pressed
        final int leftMouse = 1024;
        if (_event.getSource().equals(getPage().getJlbl_painting())) {
            switch (Status.getIndexOperation()) {
            case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
            case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
            case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
            case Constants.CONTROL_PAINTING_INDEX_PAINT_1:
                // add paintObject and point to Picture
                changePO(_event);
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
                if (_event.getModifiersEx() == leftMouse) {
                    changePO(_event);
                } 
                break;

            case Constants.CONTROL_PAINTING_INDEX_ERASE:

            	final boolean rectangleOperation = true;
            	
            	if (!rectangleOperation) {
//            		mr_eraseNew();
            		mr_erase(_event.getPoint());
                				
                					
                	//(_event.getPoint());
                	if (field_erase != null) {

                		final double factorWidth = 
                				1.0 * Math.min(
                						
                						//the value if the border of the picture
                						//is not displayed (because not enough
                						//zoom out)
                						1.0 * Status.getImageSize().width
                						/ Status.getImageShowSize().width
                						/ Status.getEraseRadius(),

                						//value if border is visible on screen; 
                						//zoomed out
                						1.0 * Status.getImageSize().width
                						/ getPage().getJlbl_painting()
                						.getWidth()
                						/ Status.getEraseRadius());
                		final double factorHeight = 
                				1.0 * Math.min(
                						
                						//the value if the border of the picture
                						//is not displayed (because not enough
                						//zoom out)
                						1.0 * Status.getImageSize().height
                						/ Status.getImageShowSize().height
                						/ Status.getEraseRadius(),

                						//value if border is visible on screen; 
                						//zoomed out
                						1.0 * Status.getImageSize().height
                						/ getPage().getJlbl_painting()
                						.getHeight()
                						/ Status.getEraseRadius());
                		
                		field_erase [(int) (_event.getPoint().x * factorWidth)]
                					[(int) (_event.getPoint().y * factorHeight)]
                							= PaintBI.FREE;

                		final int displayHeight = Status.getEraseRadius()
                				* Status.getImageShowSize().height
                				/ Status.getImageSize().height;
                		final int displayWidth = Status.getEraseRadius()
                				* Status.getImageShowSize().width
                				/ Status.getImageSize().width;
                		
                		controlPic.clrRectangle(
                        		_event.getX() - displayWidth / 2, 
                        		_event.getY() - displayHeight / 2, 
                        		displayWidth,
                        		displayHeight);
                	}
            	}
            	
            	Point p = new Point(
            			(int) (_event.getPoint().x - getPage()
            					.getJlbl_painting().getLocation().getX()),
                    	(int) (_event.getPoint().y - getPage()
                    			.getJlbl_painting().getLocation().getY()));
            	mr_erase(p);
                break;
                
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:

                if (_event.getModifiersEx() == leftMouse) {

                    if (pnt_start == null) {
                        pnt_start = _event.getPoint();
                        pnt_startLocation = getPage()
                                .getJlbl_painting().getLocation();
                        Picture.getInstance().abortPaintObject(controlPic);
                    }

                    if (pnt_start != null) {

                        int xLocation = Math.min(pnt_start.x, _event.getX());
                        int yLocation = Math.min(pnt_start.y, _event.getY());
                        // not smaller than zero
                        xLocation = Math.max(0, xLocation);
                        yLocation = Math.max(0, yLocation);

                        // not greater than the entire shown image - 
                        // the width of zoom
                        int xSize = Math.min(Status.getImageShowSize().width
                                - xLocation, 
                                Math.abs(pnt_start.x - _event.getX()));
                        int ySize = Math.min(Status.getImageShowSize().height
                                - yLocation, 
                                Math.abs(pnt_start.y - _event.getY()));

                        getPage().getJlbl_border().setBounds(xLocation,
                                yLocation, xSize, ySize);
                    } else {
                        
                        Status.getLogger().warning("Want to print border but"
                                + "the starting point is null.");
                    }
                    break;
                }

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                if (_event.getModifiersEx() == leftMouse) {

                    Picture.getInstance().abortPaintObject(controlPic);
                    Picture.getInstance().changePen(new PenSelection());
                    Picture.getInstance().addPaintObjectWrinting();
                    break;
                }
            case Constants.CONTROL_PAINTING_INDEX_MOVE:
                if (_event.getModifiersEx() == leftMouse) {
                    if (pnt_start == null) {
                        pnt_start = _event.getPoint();
                        pnt_startLocation = getPage()
                                .getJlbl_painting().getLocation();
                        Picture.getInstance().abortPaintObject(controlPic);
                    }
                    
                    if (pnt_last != null) {
                        pnt_movementSpeed = new Point(
                                pnt_last.x - _event.getX(), 
                                pnt_last.y - _event.getY());
                        if (!Status.isNormalRotation()) {

                            pnt_movementSpeed = new Point(
                                    -pnt_last.x + _event.getX(), 
                                    -pnt_last.y + _event.getY());
                        }
                    }
                    //Scroll
                    int x = pnt_startLocation.x + _event.getX() - pnt_start.x;
                    int y = pnt_startLocation.y +  _event.getY() - pnt_start.y;

                    if (!Status.isNormalRotation()) {

                        x = pnt_startLocation.x - _event.getX() + pnt_start.x;
                        y = pnt_startLocation.y -  _event.getY() + pnt_start.y;
                    }
                    
                    if (x < -Status.getImageShowSize().width + getPage().getJlbl_painting().getWidth()) {
                        x = -Status.getImageShowSize().width + getPage().getJlbl_painting().getWidth();
                    }
                    if (x > 0) {
                        x = 0;
                    }
                    if (y < -Status.getImageShowSize().height + getPage().getJlbl_painting().getHeight()) {
                        y = -Status.getImageShowSize().height + getPage().getJlbl_painting().getHeight();
                    }
                    if (y >= 0) {
                        y = 0;
                    } 
                    getPage().getJlbl_painting().setLocation(x, y);
                    getPage().refrehsSps();
                    pnt_last = _event.getPoint();
                    break;
                }

            default:
                break;
            }
        }
//        New.getInstance().repaint();
    		
	}



	public void mouseMoved(MouseEvent _event) {



        if (_event.getSource().equals(getPage().getJlbl_painting())) {

            switch (Status.getIndexOperation()) {
            
            case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:

                // save x and y location
                int xLocation = 
                _event.getX() - ViewSettings.ZOOM_SIZE.width / 2;
                int yLocation = 
                        _event.getY() - ViewSettings.ZOOM_SIZE.height / 2;

                // check whether values are in range

                // not smaller than zero
                xLocation = Math.max(0, xLocation);
                yLocation = Math.max(0, yLocation);

                // not greater than the entire shown image - the width of zoom
                xLocation = Math.min(Status.getImageShowSize().width
                        - ViewSettings.ZOOM_SIZE.width, xLocation);
                yLocation = Math.min(Status.getImageShowSize().height
                        - ViewSettings.ZOOM_SIZE.height, yLocation);

                // apply new location
                zoom.setLocation(xLocation, yLocation, getPage());
                break;

            case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
            case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
            case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
            case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
            case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
            case Constants.CONTROL_PAINTING_INDEX_PAINT_1:
                
            	if (!getTabs().isMenuOpen()) {

                    if (Status.isNormalRotation()) {

                        Picture.getInstance().getPen_current().preprint(
                                _event.getX(), _event.getY(),
                                Util.getEmptyBISelection(),
                                getPage().getJlbl_selectionBG());

                    } else {

                        Picture.getInstance().getPen_current().preprint(
                        		getPage().getJlbl_painting()
                        		.getWidth() 
                                - _event.getX(), 
                                getPage().getJlbl_painting()
                                .getHeight() 
                                - _event.getY(),
                                Util.getEmptyBISelection(),
                                getPage().getJlbl_selectionBG());
                    }
            	}
                break;
            default:
                
                break;
            }
        }
    		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

    
    /**
	 * Change PaintObject.
	 * @param _event the event.
	 */
	private void changePO(final MouseEvent _event) {
	
	    if (Status.isNormalRotation()) {
	
	        Picture.getInstance().changePaintObject(new DPoint((_event.getX() 
	        		- getPage().getJlbl_painting().getLocation().x)
	        		* Status.getImageSize().width
	        		/ Status.getImageShowSize().width, 
	        		(_event.getY() 
	        				- getPage().getJlbl_painting().getLocation().y)
	                        * Status.getImageSize().height
	                        / Status.getImageShowSize().height),
	                        getPage(),
	                        controlPic);
	    } else {
	
	        Picture.getInstance().changePaintObject(
	                new DPoint(
	                        getPage().getJlbl_painting().getWidth()
	                        - (_event.getX() 
	                        + getPage()
	                        .getJlbl_painting().getLocation().x
	                        )
	                        * Status.getImageSize().width
	                        / Status.getImageShowSize().width, 
	                        getPage().getJlbl_painting().getHeight()
	                        - (_event.getY() 
	                        + getPage().getJlbl_painting()
	                        .getLocation().y
	                        )
	                        * Status.getImageSize().height
	                        / Status.getImageShowSize().height),
	                        getPage(),
	                        controlPic
	                        );
	    }
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	


    /**
     * the mouse released event of painting JLabel.
     * @param _event the event given to mouseListener.
     */
    private void mr_painting(final MouseEvent _event) {

        // switch index of operation
        switch (Status.getIndexOperation()) {
        case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
        case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:
        case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
        case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
        case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
        case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
        case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
        case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
        case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:
        case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
        case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
        case Constants.CONTROL_PAINTING_INDEX_PAINT_1:
         
            // write the current working picture into the global picture.
            Picture.getInstance().finish(view.getTabs().getTab_pos());
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
            if (_event.getButton() == 1) {

                //remove old rectangle.
                getPage().getJlbl_border().setBounds(-1, -1, 0, 0);
                switch (Status.getIndexSelection()) {
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:
                    
                        PaintObjectWriting pow 
                        = Picture.getInstance().abortPaintObject(controlPic);
                        mr_sel_curve_complete(_event, pow);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:

                    PaintObjectWriting pow2 
                    = Picture.getInstance().abortPaintObject(controlPic);
                    mr_sel_curve_destroy(_event, pow2);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_IMAGE:
                    break;
                default:
                    break;
                }
                pnt_start = null;
                
                //set index to moving
                Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_MOVE);
                cTabPaintStatus.deactivate();
                view.getTabs().getTab_paint().getTb_move().setActivated(true);
            }
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:
            if (_event.getButton() == 1) {

                Rectangle r = mr_paint_calcRectangleLocation(_event);
                getPage().getJlbl_border().setBounds(r);
                //remove old rectangle.
                switch (Status.getIndexSelection()) {
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:
                    mr_sel_line_complete(_event, r);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:
                    mr_sel_line_destroy(r);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_IMAGE:
                    break;
                default:
                    break;
                }
                // reset values
                pnt_start = null;
                
                //set index to moving
                Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_MOVE);
                cTabPaintStatus.deactivate();
                view.getTabs().getTab_paint().getTb_move().setActivated(true);
                getPage().getJlbl_background2().repaint();
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_ERASE:

//        	mr_erase();
        	
        	break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

            if (_event.getButton() == 1) {
                Picture.getInstance().abortPaintObject(controlPic);
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:

            if (_event.getButton() == MouseEvent.BUTTON1) {
                mr_zoom(_event);
                cTabPaint.updateResizeLocation();
            } else if (_event.getButton() == MouseEvent.BUTTON3) {
            	cTabPaint.mr_zoomOut();
            	cTabPaint.updateResizeLocation();
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_PIPETTE:
            mr_paint_pipette(_event);
            break;
        case Constants.CONTROL_PAINTING_INDEX_MOVE:

            if (_event.getButton() == 1) {

                final Point mmSP = pnt_movementSpeed;
                if (mmSP != null) {
                    if (thrd_move != null) {
                        thrd_move.interrupt();
                    }
//                    mr_paint_initializeMovementThread(mmSP);
//                    thrd_move.start();
                }
                
                //set points to null
                pnt_start = null;
                pnt_startLocation = null;
                pnt_last = null;
                pnt_movementSpeed = null;
                
                //release everything
                if (Picture.getInstance().isSelected()) {

                    Picture.getInstance().releaseSelected(
                			getControlPaintSelection(),
                			getcTabSelection(),
                			getView().getTabs().getTab_pos(),
                			getView().getPage().getJlbl_painting().getLocation().x,
                			getView().getPage().getJlbl_painting().getLocation().y);
                    controlPic.releaseSelected();
                    getPage().removeButtons();
                }
            }
            break;
           
        default:
            Status.getLogger().warning("Switch in mouseReleased default");
            break;
        }
        view.getPage().getJpnl_new().setVisible(false);
    }

    
    
    
    
    
    
    

    /**
     * The MouseReleased event for zoom.
     * @param _event the event.
     */
    private void mr_zoom(final MouseEvent _event) {

        if (Status.getImageShowSize().width
                / Status.getImageSize().width 
                < Math.pow(ViewSettings.ZOOM_MULITPLICATOR, 
                        ViewSettings.MAX_ZOOM_IN)) {
            
            int newWidth = Status.getImageShowSize().width
                    * ViewSettings.ZOOM_MULITPLICATOR, 
                    newHeight = Status.getImageShowSize().height
                    * ViewSettings.ZOOM_MULITPLICATOR;

            Point oldLocation = new Point(getPage()
                    .getJlbl_painting().getLocation().x, getPage()
                    .getJlbl_painting().getLocation().y);

            Status.setImageShowSize(new Dimension(newWidth, newHeight));
            getPage().flip();
           
            /*
             * set the location of the panel.
             */
            // save new coordinates
            int newX = (oldLocation.x - zoom.getX())
                    * ViewSettings.ZOOM_MULITPLICATOR;
            int newY = (oldLocation.y - zoom.getY())
                    * ViewSettings.ZOOM_MULITPLICATOR;

            
            // check if the bounds are valid

            // not smaller than the
            newX = Math.max(newX, -(Status.getImageShowSize().width 
                    - getPage().getJlbl_painting()
                    .getWidth()));
            newY = Math.max(newY,
                    -(Status.getImageShowSize().height - getPage().getJlbl_painting()
                            .getHeight()));
            
            
            // not greater than 0
            newX = Math.min(newX, 0);
            newY = Math.min(newY, 0);
            

            // apply the location at JLabel (with setLocation method that 
            //is not used for scrolling purpose [IMPORTANT]) and repaint the 
            //image afterwards.
            getPage().getJlbl_painting().setBounds(newX, newY,
            		getPage().getJlbl_painting().getWidth(),
            		getPage().getJlbl_painting().getHeight());
            

            // apply the new location at ScrollPane
            getPage().refrehsSps();
        } else {
            Message.showMessage(Message.MESSAGE_ID_INFO, "max zoom in reached");
        }
    }

    
    
    
    /*
     * 
     * 
     * Selection
     * 
     * 
     */

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _event the mouseEvent.
     * @param _ldp the list of DPoints
     */
    private void mr_sel_curve_complete(final MouseEvent _event, 
            final PaintObjectWriting _ldp) {

    	
    	//start transaction
    	final int transaction = Picture.getInstance().getLs_po_sortedByX()
    			.startTransaction("Selection curve Complete", 
    					SecureList.ID_NO_PREDECESSOR);

    	//
    	Rectangle snapBounds = _ldp.getSnapshotBounds();
        int xShift = snapBounds.x, yShift = snapBounds.y;

        
        //stretch ldp!

        //necessary because the points are painted directly to the buffered
        //image which starts at the _ldp snapshot x.
        Picture.movePaintObjectWriting(_ldp, -_ldp.getSnapshotBounds().x, 
                -_ldp.getSnapshotBounds().y);

        BufferedImage transform = _ldp.getSnapshot();
        
        
        byte[][] field = PaintBI.printFillPolygonN(transform,
                Color.green, model.util.Util.dpntToPntArray(
                        model.util.Util.pntLsToArray(_ldp.getPoints())));
        
        
        // initialize selection list
        Picture.getInstance().getLs_po_sortedByX().toFirst(
        		transaction, SecureList.ID_NO_PREDECESSOR);
        Picture.getInstance().createSelected();
        
        // create and initialize current values (current PO and its coordinates)
        PaintObject po_current = Picture.getInstance().getLs_po_sortedByX()
                .getItem();
        
        // go through list. until either list is empty or it is
        // impossible for the paintSelection to paint inside the
        // selected area
        while (po_current != null) {

            //The y condition has to be in here because the items are just 
            //sorted by x coordinate; thus it is possible that one PaintObject 
            //is not suitable for the specified rectangle but some of its 
            //predecessors in sorted list do.
            if (po_current.isInSelectionImage(
            		field, new Point(xShift, yShift))) {

                //move current item from normal list into selected list 
                Picture.getInstance().insertIntoSelected(po_current, getView().getTabs().getTab_pos());
                Picture.getInstance().getLs_po_sortedByX().remove(
                		transaction);
                //remove item out of PictureOverview and paint and refresh paint
                //otherwise it is not possible to select more than one item
                 new PictureOverview(view.getTabs().getTab_pos()).remove(po_current);
                Picture.getInstance().getLs_po_sortedByX().toFirst(
                		transaction, SecureList.ID_NO_PREDECESSOR);
            } else {
                // next; in if clause the next is realized by remove()
                Picture.getInstance().getLs_po_sortedByX().next(
                		transaction, SecureList.ID_NO_PREDECESSOR);
            }
            // update current values
            po_current = Picture.getInstance().getLs_po_sortedByX().getItem();
        }


    	//finish transaction 
    	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
    			transaction);
    	
        //finish insertion into selected.
        Picture.getInstance().finishSelection(getcTabSelection());
        
        //paint the selected item and refresh entire painting.
        Picture.getInstance().paintSelected(getPage(),
    			getControlPic(),
    			getControlPaintSelection());
        controlPic.refreshPaint();


        
    }

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _ldp the paintObjectWriting of the selection.
     * @param _event
     *            the mouseEvent.
     */
    private void mr_sel_curve_destroy(final MouseEvent _event, 
            final PaintObjectWriting _ldp) {

    	
    	//
    	Rectangle snapBounds = _ldp.getSnapshotBounds();
        int xShift = snapBounds.x, yShift = snapBounds.y;

        
        //stretch ldp!

        //necessary because the points are painted directly to the buffered
        //image which starts at the _ldp snapshot x.
        Picture.movePaintObjectWriting(_ldp, -_ldp.getSnapshotBounds().x, 
                -_ldp.getSnapshotBounds().y);

        BufferedImage transform = _ldp.getSnapshot();
        

        //Extract points out of PaintObjectWriting and create a byte array
        //containing all the necessary information for deciding whether
        //a point is inside the selected area or not.
        //Because of this the step above to move the paintObject is necessary.
        byte[][] field = PaintBI.printFillPolygonN(transform,
                Color.green, model.util.Util.dpntToPntArray(
                        model.util.Util.pntLsToArray(_ldp.getPoints())));
        
        
        
        /*
         * whole item selection.
         */
        // initialize selection list
        Picture.getInstance().createSelected();

        // go to the beginning of the list
        if (!Picture.getInstance().getLs_po_sortedByX().isEmpty()) {


        	//start transaction 
        	final int transaction = Picture.getInstance().getLs_po_sortedByX()
        			.startTransaction("Selection curve destroy", 
        					SecureList.ID_NO_PREDECESSOR);
        	
            // go to the beginning of the list
            Picture.getInstance().getLs_po_sortedByX().toFirst(
            		transaction, SecureList.ID_NO_PREDECESSOR);
            
            // create and initialize current values
            PaintObject po_current = Picture.getInstance().getLs_po_sortedByX()
                    .getItem();

            
            /**
             * Because it is impossible to insert the new created items directly
             * to list (otherwise there would be an infinite loop because of 
             * sort order they reappear inside the while
             * loop and are destroyed once again and thus reappear etc.
             */
            List<PaintObject> ls_toInsert = new List<PaintObject>();

            
            // go through list. until either list is empty or it is
            // impossible for the paintSelection to paint inside the
            // selected area
            while (po_current != null) {

                //The y condition has to be in here because the items are just 
                //sorted by x coordinate; thus it is possible that one 
                //PaintObject 
                //is not suitable for the specified rectangle but some of its 
                //predecessors in sorted list do.
                if (po_current.isInSelectionImage(field, 
                		new Point(xShift, yShift))) {

                    // get item; remove it out of lists and add it to
                    // selection list

                    PaintObject [][] separatedPO = po_current.separate(
                    		field, new Point(xShift, yShift));
                    new PictureOverview(view.getTabs().getTab_pos()).remove(Picture.getInstance()
                            .getLs_po_sortedByX().getItem());
                    Picture.getInstance().getLs_po_sortedByX().remove(
                    		transaction);
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {

                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            Picture.getInstance().insertIntoSelected(
                                    separatedPO[1][current], getView().getTabs().getTab_pos());
                        } else {
                            
                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    
                    //finish insertion into selected.
                    Picture.getInstance().finishSelection(getcTabSelection());
                    
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview(view.getTabs().getTab_pos()).add(
                                    separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    Picture.getInstance().getLs_po_sortedByX().toFirst(
                    		transaction, SecureList.ID_NO_PREDECESSOR);
                } else {
                    
                    // next
                    Picture.getInstance().getLs_po_sortedByX().next(
                    		transaction, SecureList.ID_NO_PREDECESSOR);
                }


                // update current values
                po_current = Picture.getInstance().getLs_po_sortedByX()
                        .getItem();
            }

            
            //insert the to insert items to graphical user interface.
            ls_toInsert.toFirst();
            while (!ls_toInsert.isBehind() && !ls_toInsert.isEmpty()) {

                Picture.getInstance().getLs_po_sortedByX().insertSorted(
                        ls_toInsert.getItem(), 
                        ls_toInsert.getItem().getSnapshotBounds().x,
                        transaction);
                ls_toInsert.next();
            }

        	//finish transaction 
        	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
        			transaction);
        	
            if (Picture.getInstance().paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {
            	controlPic.refreshPaint();
            }

        }


        Picture.getInstance().paintSelected(getPage(),
    			getControlPic(),
    			getControlPaintSelection());
        controlPic.refreshPaint();

    }
    
    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _r_size the rectangle.
     * @param _event
     *            the mouseEvent.
     */
    private synchronized void mr_sel_line_complete(final MouseEvent _event,
            final Rectangle _r_size) {

    	
    	//start transaction 
    	final int transaction = Picture.getInstance().getLs_po_sortedByX()
    			.startTransaction("Selection line complete", 
    					SecureList.ID_NO_PREDECESSOR);
        /*
         * SELECT ALL ITEMS INSIDE RECTANGLE    
         */
        //case: there can't be items inside rectangle because list is empty
        if (Picture.getInstance().getLs_po_sortedByX().isEmpty()) {

//            //adjust location of the field for painting to view
            _r_size.x += getPage().getJlbl_painting().getLocation()
                    .x;
            _r_size.y += getPage().getJlbl_painting().getLocation()
                    .y;
            //paint to view
            controlPic.paintEntireSelectionRect(
                    _r_size);
            pnt_start = null;
            return;
        }
        
        // find paintObjects and move them from image to Selection and 
        // initialize selection list
        Picture.getInstance().getLs_po_sortedByX().toFirst(
        		transaction, SecureList.ID_NO_PREDECESSOR);
        Picture.getInstance().createSelected();
        
        // create and initialize current values (current PO and its coordinates)
        PaintObject po_current = Picture.getInstance().getLs_po_sortedByX()
                .getItem();
        int currentX = po_current.getSnapshotBounds().x;

        //adapt the rectangle to the currently used zoom factor.
        final double cZoomFactorWidth = 1.0 * Status.getImageSize().width
                / Status.getImageShowSize().width;
        final double cZoomFactorHeight = 1.0 * Status.getImageSize().height
                / Status.getImageShowSize().height;
        _r_size.x *= cZoomFactorWidth;
        _r_size.width *= cZoomFactorWidth;
        _r_size.y *= cZoomFactorHeight;
        _r_size.height *= cZoomFactorHeight;
        
        // go through list. until either list is empty or it is
        // impossible for the paintSelection to paint inside the
        // selected area
        while (po_current != null
                && currentX 
                <= (_r_size.x + _r_size.width)) {


            //The y condition has to be in here because the items are just 
            //sorted by x coordinate; thus it is possible that one PaintObject 
            //is not suitable for the specified rectangle but some of its 
            //predecessors in sorted list do.
            if (po_current.isInSelectionImage(_r_size)) {


                //remove item out of PictureOverview and paint and refresh paint
                //otherwise it is not possible to select more than one item
                 new PictureOverview(view.getTabs().getTab_pos()).remove(po_current);
                
                //move current item from normal list into selected list 
                Picture.getInstance().insertIntoSelected(po_current, getView().getTabs().getTab_pos());
                             
                Picture.getInstance().getLs_po_sortedByX().remove(
                		transaction);
            }            

            Picture.getInstance().getLs_po_sortedByX().next(
            		transaction, SecureList.ID_NO_PREDECESSOR);

            // update current values
            currentX = po_current.getSnapshotBounds().x;
            po_current = Picture.getInstance().getLs_po_sortedByX().getItem();

        }

    	//finish transaction 
    	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
    			transaction);

        //finish insertion into selected.
        Picture.getInstance().finishSelection(getcTabSelection());
        
        controlPic.refreshPaint();

        if (!Picture.getInstance().paintSelected(getPage(),
    			getControlPic(),
    			getControlPaintSelection())) {

          //transform the logical Rectangle to the painted one.
          _r_size.x = (int) (1.0 * _r_size.x / cZoomFactorWidth);
          _r_size.width = (int) 
                  (1.0 * _r_size.width / cZoomFactorWidth);
          _r_size.y = (int) (1.0 * _r_size.y / cZoomFactorHeight);
          _r_size.height = (int) 
                  (1.0 * _r_size.height / cZoomFactorHeight);
          
          _r_size.x 
          += getPage().getJlbl_painting().getLocation().x;
          _r_size.y 
          += getPage().getJlbl_painting().getLocation().y;
          
          
          controlPaintSelection.setR_selection(_r_size,
                  getPage().getJlbl_painting().getLocation());
          controlPic.paintEntireSelectionRect(
                  _r_size);
        }
        getPage().getJlbl_background2().repaint();
        

    }
    
    

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _r_sizeField the rectangle
     */
    public void mr_sel_line_destroy(final Rectangle _r_sizeField) {

    	//start transaction 
    	final int transaction = Picture.getInstance().getLs_po_sortedByX()
    			.startTransaction("Selection line destroy", 
    					SecureList.ID_NO_PREDECESSOR);
        /*
         * whole item selection.
         */
        // initialize selection list
        Picture.getInstance().createSelected();

        // go to the beginning of the list
        Picture.getInstance().getLs_po_sortedByX().toFirst(transaction, 
        		SecureList.ID_NO_PREDECESSOR);
        if (!Picture.getInstance().getLs_po_sortedByX().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = Picture.getInstance().getLs_po_sortedByX()
                    .getItem();
            int currentX = po_current.getSnapshotBounds().x;

            
            /**
             * Because it is impossible to insert the new created items directly
             * to list (otherwise there would be an infinite loop because of 
             * sort order they reappear inside the while
             * loop and are destroyed once again and thus reappear etc.
             */
            List<PaintObject> ls_toInsert = new List<PaintObject>();

            //adapt the rectangle to the currently used zoom factor.
            final double cZoomFactorWidth = 1.0 * Status.getImageSize().width
                    / Status.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * Status.getImageSize().height
                    / Status.getImageShowSize().height;
            _r_sizeField.x *= cZoomFactorWidth;
            _r_sizeField.width *= cZoomFactorWidth;
            _r_sizeField.y *= cZoomFactorHeight;
            _r_sizeField.height *= cZoomFactorHeight;
            
            

            
            // go through list. until either list is empty or it is
            // impossible for the paintSelection to paint inside the
            // selected area
            while (po_current != null
                    && currentX 
                    <= (_r_sizeField.x + _r_sizeField.width)) {

                //The y condition has to be in here because the items are just 
                //sorted by x coordinate; thus it is possible that one 
                //PaintObject is not suitable for the specified rectangle but 
                //some of its predecessors in sorted list do.
                if (po_current.isInSelectionImage(_r_sizeField)) {

                    // get item; remove it out of lists and add it to
                    // selection list

                    PaintObject [][] separatedPO = po_current.separate(
                            _r_sizeField);
//                    PaintObject [][] p2 = po_current.separate(
//                            new Rectangle(r_sizeField.x - 2,
//                    r_sizeField.y - 2,
//                            r_sizeField.width + 2 * 2, 
//                            r_sizeField.height + 2 * 2));
//                    
//                    PaintObject [][] separatedPO = 
//                    Util.mergeDoubleArray(p, p2);
                     new PictureOverview(view.getTabs().getTab_pos()).remove(Picture.getInstance()
                            .getLs_po_sortedByX().getItem());
                    Picture.getInstance().getLs_po_sortedByX().remove(
                    		transaction);
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {

                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            Picture.getInstance().insertIntoSelected(
                                    separatedPO[1][current],
                                    getView().getTabs().getTab_pos()
                            		);
                        } else {
                            
                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                        
                    }

                    //finish insertion into selected.
                    Picture.getInstance().finishSelection(getcTabSelection());
                    
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview(view.getTabs().getTab_pos()).add(
                                    separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                } 
                // next
                Picture.getInstance().getLs_po_sortedByX().next(transaction,
                		SecureList.ID_NO_PREDECESSOR);


                // update current values
                currentX = po_current.getSnapshotBounds().x;
                po_current = Picture.getInstance().getLs_po_sortedByX()
                        .getItem();
            }

            
            //insert the to insert items to graphical user interface.
            ls_toInsert.toFirst();
            while (!ls_toInsert.isBehind() && !ls_toInsert.isEmpty()) {

                Picture.getInstance().getLs_po_sortedByX().insertSorted(
                        ls_toInsert.getItem(), 
                        ls_toInsert.getItem().getSnapshotBounds().x,
                        transaction);
                ls_toInsert.next();
            }
            if (Picture.getInstance().paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {
            	controlPic.refreshPaint();
            }


            _r_sizeField.x /= cZoomFactorWidth;
            _r_sizeField.width /= cZoomFactorWidth;
            _r_sizeField.y /= cZoomFactorHeight;
            _r_sizeField.height /= cZoomFactorHeight;

            _r_sizeField.x += getPage().getJlbl_painting()
            		.getLocation().getX();
            _r_sizeField.y += getPage().getJlbl_painting()
            		.getLocation().getY();
            
        }
        

        controlPic.refreshRectangle(
                _r_sizeField.x, _r_sizeField.y, 
                _r_sizeField.width, _r_sizeField.height);


        getPage().getJlbl_background2().repaint();

    	//finish transaction
    	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
    			transaction);
    }
    
    
    
    /**
     * Erase functionality at mouseReleased.
     * @param _p the Point.
     */
    public synchronized void mr_erase(final Point _p) {

    	//start transaction 
    	final int transaction = Picture.getInstance().getLs_po_sortedByX()
    			.startTransaction("erase", 
    					SecureList.ID_NO_PREDECESSOR);
    	/**
    	 * Value for showing the new paintObjects in PaintObjectsView.
    	 */
    	final boolean debug_update_paintObjects_view = false;
    	
    	final Rectangle r_sizeField = new Rectangle(
    			_p.x - Status.getEraseRadius(), 
    			_p.y - Status.getEraseRadius(), 
    			Status.getEraseRadius() * 2, 
    			Status.getEraseRadius() * 2);
        /*
         * whole item selection.
         */
        // initialize selection list
        Picture.getInstance().createSelected();

        // go to the beginning of the list
        Picture.getInstance().getLs_po_sortedByX().toFirst(transaction,
        		SecureList.ID_NO_PREDECESSOR);
        if (!Picture.getInstance().getLs_po_sortedByX().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = Picture.getInstance().getLs_po_sortedByX()
                    .getItem();
            int currentX = po_current.getSnapshotBounds().x;

            

            //adapt the rectangle to the currently used zoom factor.
            final double cZoomFactorWidth = 1.0 * Status.getImageSize().width
                    / Status.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * Status.getImageSize().height
                    / Status.getImageShowSize().height;
            r_sizeField.x *= cZoomFactorWidth;
            r_sizeField.width *= cZoomFactorWidth;
            r_sizeField.y *= cZoomFactorHeight;
            r_sizeField.height *= cZoomFactorHeight;
            List<PaintObjectWriting> ls_separatedPO = null;
            
            // go through list. until either list is empty or it is
            // impossible for the paintSelection to paint inside the
            // selected area
            while (po_current != null
                    && currentX 
                    <= (r_sizeField.x + r_sizeField.width)) {

                //The y condition has to be in here because the items are just 
                //sorted by x coordinate; thus it is possible that one 
                //PaintObject is not suitable for the specified rectangle but 
                //some of its predecessors in sorted list do.
                if (po_current.isInSelectionImage(r_sizeField)) {

                    // get item; remove it out of lists and add it to
                    // selection list

                	ls_separatedPO 
                	= po_current.deleteRectangle(r_sizeField, ls_separatedPO);
                	if (ls_separatedPO != null) {

                    	if (debug_update_paintObjects_view) {

                            new PictureOverview(view.getTabs().getTab_pos()).remove(Picture.getInstance()
                                    .getLs_po_sortedByX().getItem());
                    	}
                        Picture.getInstance().getLs_po_sortedByX().remove(
                        		transaction);
                	}
                } 
                // next
                Picture.getInstance().getLs_po_sortedByX().next(transaction,
                		SecureList.ID_NO_PREDECESSOR);


                // update current values
                currentX = po_current.getSnapshotBounds().x;
                po_current = Picture.getInstance().getLs_po_sortedByX()
                        .getItem();
            }

            

            if (ls_separatedPO != null) {

            	ls_separatedPO.toFirst();
                while (ls_separatedPO != null
                		&& !ls_separatedPO.isEmpty()
                		&& !ls_separatedPO.isBehind()) {

                    if (ls_separatedPO.getItem() != null) {
                        //recalculate snapshot bounds for being able to
                        //insert the item into the sorted list.
                    	ls_separatedPO.getItem().recalculateSnapshotBounds();

                        Picture.getInstance().getLs_po_sortedByX().insertSorted(
                        		ls_separatedPO.getItem(), 
                        		ls_separatedPO.getItem().getSnapshotBounds().x,
                        		transaction);

                    	if (debug_update_paintObjects_view) {

                            new PictureOverview(view.getTabs().getTab_pos()).add(
                           		 ls_separatedPO.getItem());
                    	}
                    } else {

                        Status.getLogger().warning("separated paintObject "
                                + "is null");
                    }
                    ls_separatedPO.next();
                }
            }
        	//finish transaction
        	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
        			transaction);
        
            
            if (Picture.getInstance().paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {
            	controlPic.refreshPaint();
            }

            r_sizeField.x /= cZoomFactorWidth;
            r_sizeField.width /= cZoomFactorWidth;
            r_sizeField.y /= cZoomFactorHeight;
            r_sizeField.height /= cZoomFactorHeight;
        } else {

        	//finish transaction
        	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
        			transaction);
        
        }
        


        controlPic.clrRectangle(
                r_sizeField.x + (int) getPage()
				.getJlbl_painting().getLocation().getX(),
				r_sizeField.y + (int) getPage()
				.getJlbl_painting().getLocation().getY(),
                r_sizeField.width, r_sizeField.height);

    }


    /**
     * Erase functionality at mouseReleased.
     */
    public synchronized void mr_eraseNew() {

    	/**
    	 * Value for showing the new paintObjects in PaintObjectsView.
    	 */
    	final boolean debug_update_paintObjects_view = false;
    	
    	//TODO: 

		final double factorWidth = 
				Math.min(
						
						//the value if the border of the picture
						//is not displayed (because not enough
						//zoom out)
						1.0 * Status.getImageSize().width
						/ Status.getImageShowSize().width
						/ Status.getEraseRadius(),

						//value if border is visible on screen; 
						//zoomed out
						1.0 * Status.getImageSize().width
						/ getPage().getJlbl_painting()
						.getWidth()
						/ Status.getEraseRadius());
		final double factorHeight = 
				Math.min(
						
						//the value if the border of the picture
						//is not displayed (because not enough
						//zoom out)
						1.0 * Status.getImageSize().height
						/ Status.getImageShowSize().height
						/ Status.getEraseRadius(),

						//value if border is visible on screen; 
						//zoomed out
						1.0 * Status.getImageSize().height
						/ getPage().getJlbl_painting()
						.getHeight()
						/ Status.getEraseRadius());
    	final DPoint pnt_stretch = new DPoint(factorWidth, factorHeight);
        
    	/*
         * whole item selection.
         */
        // initialize selection list
        Picture.getInstance().createSelected();

    	//start transaction 
    	final int transaction = Picture.getInstance().getLs_po_sortedByX()
    			.startTransaction("erase", 
    					SecureList.ID_NO_PREDECESSOR);
        // go to the beginning of the list
        Picture.getInstance().getLs_po_sortedByX().toFirst(transaction,
        		SecureList.ID_NO_PREDECESSOR);
        if (!Picture.getInstance().getLs_po_sortedByX().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = Picture.getInstance().getLs_po_sortedByX()
                    .getItem();
            int currentX = po_current.getSnapshotBounds().x;

            List<PaintObjectWriting> ls_separatedPO = new List<PaintObjectWriting>();
            
            // go through list. until either list is empty or it is
            // impossible for the paintSelection to paint inside the
            // selected area
            while (po_current != null
                    && currentX <=  (getPage().getJlbl_painting()
                    		.getLocation().getX()
                    		+ getPage().getJlbl_painting()
                    		.getWidth())) {

                //The y condition has to be in here because the items are just 
                //sorted by x coordinate; thus it is possible that one 
                //PaintObject is not suitable for the specified rectangle but 
                //some of its predecessors in sorted list do.
                if (po_current.isInSelectionImageStretched(
                		field_erase, 
                		getPage().getJlbl_painting().getLocation(),
                		pnt_stretch)) {
                	

                    // get item; remove it out of lists and add it to
                    // selection list

                	ls_separatedPO 
                	= po_current.deleteCurve(field_erase, 
                    		getPage().getJlbl_painting().getLocation(),
                			pnt_stretch,
                			ls_separatedPO);
                	if (ls_separatedPO != null) {

                    	if (debug_update_paintObjects_view) {

                            new PictureOverview(view.getTabs().getTab_pos()).remove(Picture.getInstance()
                                    .getLs_po_sortedByX().getItem());
                    	}
                        Picture.getInstance().getLs_po_sortedByX().remove(
                        		transaction);
                	}
                } 
                // next
                Picture.getInstance().getLs_po_sortedByX().next(transaction,
                		SecureList.ID_NO_PREDECESSOR);


                // update current values
                currentX = po_current.getSnapshotBounds().x;
                po_current = Picture.getInstance().getLs_po_sortedByX()
                        .getItem();
            }

            

            if (ls_separatedPO != null) {

            	ls_separatedPO.toFirst();
                while (ls_separatedPO != null
                		&& !ls_separatedPO.isEmpty()
                		&& !ls_separatedPO.isBehind()) {

                    if (ls_separatedPO.getItem() != null) {
                        //recalculate snapshot bounds for being able to
                        //insert the item into the sorted list.
                    	ls_separatedPO.getItem().recalculateSnapshotBounds();

                        Picture.getInstance().getLs_po_sortedByX().insertSorted(
                        		ls_separatedPO.getItem(), 
                        		ls_separatedPO.getItem().getSnapshotBounds().x,
                        		transaction);

                    	if (debug_update_paintObjects_view) {

                            new PictureOverview(view.getTabs().getTab_pos())
                            .add(ls_separatedPO.getItem());
                    	}
                    } else {

                        Status.getLogger().warning("separated paintObject "
                                + "is null");
                    }
                    ls_separatedPO.next();
                }
            }
        	//finish transaction
        	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
        			transaction);
            
            if (Picture.getInstance().paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {
            	controlPic.refreshPaint();
            }
        } else {

        	//finish transaction
        	Picture.getInstance().getLs_po_sortedByX().finishTransaction(
        			transaction);
        }
    
    }

    /**
     * The action which is performed if clicked while the current id is pipette.
     * 
     * @param _event
     *            the event.
     */
    private void mr_paint_pipette(final MouseEvent _event) {


        int color = Picture.getInstance().getColorPX(_event.getX(), 
                _event.getY(), getControlPic().getBi());
        if (_event.getButton() == 1) {
        	view.getTabs().getTab_paint().getTb_color1().setBackground(new Color(color));
        } else {
        	view.getTabs().getTab_paint().getTb_color2().setBackground(new Color(color));
        }
    }
    
    
    
    
    
    
    


    
    
    /**
     * Initializes the movement thread.
     * @param _mmSP the movement speed
     */
    public void mr_paint_initializeMovementThread(final Point _mmSP) {
        thrd_move = new Thread() {
            @Override public void run() {
                final int max = 25;
                for (int i = max; i >= 0; i--) {

                    int x = getPage().getJlbl_painting()
                            .getLocation().x 
                            - _mmSP.x * i / max;
                    int y = getPage().getJlbl_painting()
                            .getLocation().y 
                            - _mmSP.y * i / max;

                    if (x < -Status.getImageShowSize().width 
                            + getPage().getJlbl_painting()
                            .getWidth()) {
                        x = -Status.getImageShowSize().width
                                + getPage()
                                .getJlbl_painting().getWidth();
                    }
                    if (x > 0) {
                        x = 0;
                    }
                    
                    if (y < -Status.getImageShowSize().height
                            + getPage().getJlbl_painting()
                            .getHeight()) {
                        y = -Status.getImageShowSize().height
                                + getPage()
                                .getJlbl_painting().getHeight();
                    }
                    if (y >= 0) {
                        y = 0;
                    } 
                    getPage().getJlbl_painting()
                    .setLocation(x, y);
                    getPage().refrehsSps();
                    
                    try {
                        final int sleepTime = 20;
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                }
            }
        };
    }
    

    /**
     * Method used for getting the location of the selection rectangle box.
     * @param _event the MouseEvent.
     * @return the location
     */
    private Rectangle mr_paint_calcRectangleLocation(final MouseEvent _event) {

        int xLocation = Math.min(pnt_start.x, _event.getX());
        int yLocation = Math.min(pnt_start.y, _event.getY());
        // not smaller than zero
        xLocation = Math.max(0, xLocation);
        yLocation = Math.max(0, yLocation);
        
        // not greater than the entire shown image - 
        // the width of zoom
        int xSize = Math.min(Status.getImageShowSize().width
                - xLocation, 
                Math.abs(pnt_start.x - _event.getX()));
        int ySize = Math.min(Status.getImageShowSize().height
                - yLocation, 
                Math.abs(pnt_start.y - _event.getY()));

        xLocation -= getPage().getJlbl_painting()
                .getLocation().x;
        yLocation -= getPage().getJlbl_painting()
                .getLocation().y;

        
        return new Rectangle(
                xLocation,
                yLocation,
                xSize,
                ySize);

    }




	/**
	 * @return the controlPaintSelection
	 */
	public ControlPaintSelectin getControlPaintSelection() {
		return controlPaintSelection;
	}




	/**
	 * @return the cTabPaint
	 */
	public ControlTabPainting getcTabPaint() {
		return cTabPaint;
	}




	/**
	 * @param cTabPaint the cTabPaint to set
	 */
	public void setcTabPaint(ControlTabPainting cTabPaint) {
		this.cTabPaint = cTabPaint;
	}




	/**
	 * @return the cTabPrint
	 */
	public CPrint getcTabPrint() {
		return cTabPrint;
	}




	/**
	 * @param cTabPrint the cTabPrint to set
	 */
	public void setcTabPrint(CPrint cTabPrint) {
		this.cTabPrint = cTabPrint;
	}




	/**
	 * @return the cTabWrite
	 */
	public CWrite getcTabWrite() {
		return cTabWrite;
	}




	/**
	 * @param cTabWrite the cTabWrite to set
	 */
	public void setcTabWrite(CWrite cTabWrite) {
		this.cTabWrite = cTabWrite;
	}




	/**
	 * @return the cTabPaintStatus
	 */
	public CPaintStatus getcTabPaintStatus() {
		return cTabPaintStatus;
	}




	/**
	 * @param cTabPaintStatus the cTabPaintStatus to set
	 */
	public void setcTabPaintStatus(CPaintStatus cTabPaintStatus) {
		this.cTabPaintStatus = cTabPaintStatus;
	}




	public void beforeOpen() {

    	//release selected because of display bug otherwise.
    	if (Picture.getInstance().isSelected()) {
	    	Picture.getInstance().releaseSelected(
        			getControlPaintSelection(),
        			getcTabSelection(),
        			getView().getTabs().getTab_pos(),
        			getView().getPage().getJlbl_painting().getLocation().x,
        			getView().getPage().getJlbl_painting().getLocation().y);
	    	controlPic.releaseSelected();
    	}		
	}




	public void beforeClose() {

    	//release selected because of display bug otherwise.
    	if (Picture.getInstance().isSelected()) {
	    	Picture.getInstance().releaseSelected(
        			getControlPaintSelection(),
        			getcTabSelection(),
        			getView().getTabs().getTab_pos(),
        			getView().getPage().getJlbl_painting().getLocation().x,
        			getView().getPage().getJlbl_painting().getLocation().y);
	    	controlPic.releaseSelected();
    	}		
	}



	/**
	 * {@inheritDoc}
	 */
	public void afterOpen() { }




	public void afterClose() {

        //when closed repaint.
        getPage().getJlbl_painting().repaint();
        getTabs().repaint();		
	}




	/**
	 * @return the cTabs
	 */
	public CTabs getcTabs() {
		return cTabs;
	}




	/**
	 * @param cTabs the cTabs to set
	 */
	public void setcTabs(CTabs cTabs) {
		this.cTabs = cTabs;
	}




	/**
	 * @return the cTabSelection
	 */
	public CTabSelection getcTabSelection() {
		return cTabSelection;
	}




	/**
	 * @param cTabSelection the cTabSelection to set
	 */
	public void setcTabSelection(CTabSelection cTabSelection) {
		this.cTabSelection = cTabSelection;
	}




	/**
	 * @return the cTabLook
	 */
	public CLook getcTabLook() {
		return cTabLook;
	}




	/**
	 * @param cTabLook the cTabLook to set
	 */
	public void setcTabLook(CLook cTabLook) {
		this.cTabLook = cTabLook;
	}




	/**
	 * @return the cTabExport
	 */
	public CExport getcTabExport() {
		return cTabExport;
	}




	/**
	 * @param cTabExport the cTabExport to set
	 */
	public void setcTabExport(CExport cTabExport) {
		this.cTabExport = cTabExport;
	}




	/**
	 * @return the cTabPaintObjects
	 */
	public CPaintObjects getcTabPaintObjects() {
		return cTabPaintObjects;
	}




	/**
	 * @param cTabPaintObjects the cTabPaintObjects to set
	 */
	public void setcTabPaintObjects(CPaintObjects cTabPaintObjects) {
		this.cTabPaintObjects = cTabPaintObjects;
	}




	/**
	 * @return the controlPic
	 */
	public ContorlPicture getControlPic() {
		return controlPic;
	}




	/**
	 * @param controlPic the controlPic to set
	 */
	public void setControlPic(ContorlPicture controlPic) {
		this.controlPic = controlPic;
	}




	/**
	 * @return the controlnew
	 */
	public CNew getControlnew() {
		return controlnew;
	}




	/**
	 * @param controlnew the controlnew to set
	 */
	public void setControlnew(CNew controlnew) {
		this.controlnew = controlnew;
	}

	public CQuickAccess getControlQuickAccess() {
		return controlQuickAccess;
	}

	public void setControlQuickAccess(CQuickAccess controlQuickAccess) {
		this.controlQuickAccess = controlQuickAccess;
	}

	public ScrollPaneActivityListener getUtilityControlScrollPane() {
		return utilityControlScrollPane;
	}

	public void setUtilityControlScrollPane(ScrollPaneActivityListener utilityControlScrollPane) {
		this.utilityControlScrollPane = utilityControlScrollPane;
	}

	public Item2ActivityListener getUtilityControlItem2() {
		return utilityControlItem2;
	}

	public void setUtilityControlItem2(Item2ActivityListener utilityControlItem2) {
		this.utilityControlItem2 = utilityControlItem2;
	}
    
}
