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
import control.tabs.CAbout;
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
import model.Project;
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
	private CAbout cTabAbout;
	
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
	
	private Project project;

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

            project = new Project();
            picture = project.getPicture();
            
            Status.setControlPaint(this);

            utilityControlScrollPane = new ScrollPaneActivityListener(this);
            //initialize the model class picture.
            //TODO: not null
            controlnew = new CNew(this);
            cTabPrint = new CPrint(this);
            cTabLook = new CLook(this);
            cTabExport = new CExport(this);
            cTabWrite = new CWrite(this);
            cTabAbout = new CAbout(this);
            controlPic = new ContorlPicture(this);
            cTabSelection = new CTabSelection(this);
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
            view.getTabs().getTab_paint().getIt_stift1()
            .getTb_open().setActivated(true);
           
            
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
	public void mouseClicked(final MouseEvent _event) { }

	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(final MouseEvent _event) { }

	/**
	 * {@inheritDoc}
	 */
	public final void mouseExited(final MouseEvent _event) {



	       if (_event.getSource().equals(
	        		getPage().getJlbl_painting())) { 
	        	
	        	
	        	//remove old pen - position - indication - point
	        	if (!picture.isSelected()) {
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


		            	//do only preprint if there is no menu open (because
		            	//that would cause a repaint of the view and close
		            	//the menu.
		            	if (!getTabs().isMenuOpen()) {

			            	BufferedImage bi = Util.getEmptyBISelection();
			            	getPage().getJlbl_selectionBG().setIcon(
			            			new ImageIcon(bi));
			            	break;
		            	}
	                default:
	                	break;
	        		}
	        	}
	        }		
	}

	/**
	 * {@inheritDoc}
	 */
	public final void mousePressed(final MouseEvent _event) {

        if (_event.getSource().equals(
                getPage().getJlbl_painting())) {

        	if (getTabs().isMenuOpen()) {

                getTabs().closeMenues();
        	}
            
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
                        || picture.isPaintObjectReady()) {
                    

                    // set currently selected pen. Differs if
                    if (_event.getButton() == MouseEvent.BUTTON1) {

                        if (Status.getIndexOperation() 
                                == Constants.CONTROL_PAINTING_INDEX_PAINT_1) {
                            picture.changePen(Pen.clonePen(
                                    Status.getPenSelected1()));

                        } else {

                            picture.changePen(Pen.clonePen(
                                    Status.getPenSelected2()));
                        }
                    } else if (_event.getButton() == MouseEvent.BUTTON3) {

                        if (Status.getIndexOperation() 
                                == Constants.CONTROL_PAINTING_INDEX_PAINT_1) {
                            picture.changePen(Pen.clonePen(
                                    Status.getPenSelected2()));
                        } else {


                            picture.changePen(Pen.clonePen(
                                    Status.getPenSelected1()));

                        }
                    }
                }
                    
                // add paintObject and point to Picture
                picture.addPaintObject(getTabs().getTab_insert());
                changePO(_event);
                break;
            
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

                // abort old paint object
                picture.abortPaintObject(controlPic);

                // change pen and add new paint object
                picture.changePen(new PenSelection());
                picture.addPaintObjectWrinting();
                break;

            case Constants.CONTROL_PAINTING_INDEX_ERASE:
            	
            	mr_erase(_event.getPoint());
            	final boolean oldStuff = false;
            	if (oldStuff) {
            		/*
                	 * Initialize the field for erasing. 
                	 * Therefore calculate the 
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
                			Status.getImageSize().width
                			/ Status.getEraseRadius());
                	
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
                			Status.getImageSize().height 
                			/ Status.getEraseRadius());
                	
                	//initialize the field.
                	field_erase = new byte[width][height];
            	}
            	
            	
            	break;
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                picture.abortPaintObject(controlPic);
                picture.changePen(new PenSelection());
                picture.addPaintObjectWrinting();
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

	/**
	 * {@inheritDoc}
	 */
	public final void mouseReleased(final MouseEvent _event) {
		if (_event.getSource().equals(
                getPage().getJlbl_painting())) {
			mr_painting(_event);
		} 
	}



	/**
	 * @return the view
	 */
	public final  View getView() {
		return view;
	}




	/**
	 * {@inheritDoc}
	 */
	public final void mouseDragged(final MouseEvent _event) {


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
                        picture.abortPaintObject(controlPic);
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

                    picture.abortPaintObject(controlPic);
                    picture.changePen(new PenSelection());
                    picture.addPaintObjectWrinting();
                    break;
                }
            case Constants.CONTROL_PAINTING_INDEX_MOVE:
                if (_event.getModifiersEx() == leftMouse) {
                    if (pnt_start == null) {
                        pnt_start = _event.getPoint();
                        pnt_startLocation = getPage()
                                .getJlbl_painting().getLocation();
                        picture.abortPaintObject(controlPic);
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
                    
                    if (x < -Status.getImageShowSize().width 
                    		+ getPage().getJlbl_painting().getWidth()) {
                    	
                        x = -Status.getImageShowSize().width 
                        		+ getPage().getJlbl_painting().getWidth();
                    }
                    if (x > 0) {
                        x = 0;
                    }
                    if (y < -Status.getImageShowSize().height 
                    		+ getPage().getJlbl_painting().getHeight()) {
                        y = -Status.getImageShowSize().height 
                        		+ getPage().getJlbl_painting().getHeight();
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



	
	/**
	 * {@inheritDoc}
	 */
	public final void mouseMoved(final MouseEvent _event) {


        if (_event.getSource().equals(getPage().getJlbl_painting())) {

            switch (Status.getIndexOperation()) {
            
            case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:

            	//do only display zoom box if there is no menu open (because
            	//that would cause a repaint of the view and close
            	//the menu.
            	if (!getTabs().isMenuOpen()) {
	                // save x and y location
	                int xLocation = 
	                _event.getX() - ViewSettings.ZOOM_SIZE.width / 2;
	                int yLocation = 
	                        _event.getY() - ViewSettings.ZOOM_SIZE.height / 2;
	
	                // check whether values are in range
	
	                // not smaller than zero
	                xLocation = Math.max(0, xLocation);
	                yLocation = Math.max(0, yLocation);
	
	                // not greater than the entire shown image - 
	                // the width of zoom
	                xLocation = Math.min(Status.getImageShowSize().width
	                        - ViewSettings.ZOOM_SIZE.width, xLocation);
	                yLocation = Math.min(Status.getImageShowSize().height
	                        - ViewSettings.ZOOM_SIZE.height, yLocation);
	
	                // apply new location
	                zoom.setLocation(xLocation, yLocation, getPage());
            	}
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

            	//do only preprint if there is no menu open (because
            	//that would cause a repaint of the view and close
            	//the menu.
            	if (!getTabs().isMenuOpen()) {


                    if (Status.isNormalRotation()) {

                        picture.getPen_current().preprint(
                                _event.getX(), _event.getY(),
                                Util.getEmptyBISelection(),
                                getPage().getJlbl_selectionBG());

                    } else {

                        picture.getPen_current().preprint(
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
	
	        picture.changePaintObject(new DPoint((_event.getX() 
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
	
	        picture.changePaintObject(
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
            picture.finish(view.getTabs().getTab_debug());
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
            if (_event.getButton() == 1) {

                //remove old rectangle.
                getPage().getJlbl_border().setBounds(-1, -1, 0, 0);
                switch (Status.getIndexSelection()) {
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:
                    
                        PaintObjectWriting pow 
                        = picture.abortPaintObject(controlPic);
                        mr_sel_curve_complete(_event, pow);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:

                    PaintObjectWriting pow2 
                    = picture.abortPaintObject(controlPic);
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
                    mr_sel_line_complete(r);
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
                picture.abortPaintObject(controlPic);
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
                if (picture.isSelected()) {

                    picture.releaseSelected(
                			getControlPaintSelection(),
                			getcTabSelection(),
                			getView().getTabs().getTab_debug(),
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
    	final int transaction = picture.getLs_po_sortedByX()
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
        picture.getLs_po_sortedByX().toFirst(
        		transaction, SecureList.ID_NO_PREDECESSOR);
        picture.createSelected();
        
        // create and initialize current values (current PO and its coordinates)
        PaintObject po_current = picture.getLs_po_sortedByX()
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
                picture.insertIntoSelected(po_current, getView().getTabs().getTab_debug());
                picture.getLs_po_sortedByX().remove(
                		transaction);
                //remove item out of PictureOverview and paint and refresh paint
                //otherwise it is not possible to select more than one item
                 new PictureOverview(view.getTabs().getTab_debug()).remove(po_current);
                picture.getLs_po_sortedByX().toFirst(
                		transaction, SecureList.ID_NO_PREDECESSOR);
            } else {
                // next; in if clause the next is realized by remove()
                picture.getLs_po_sortedByX().next(
                		transaction, SecureList.ID_NO_PREDECESSOR);
            }
            // update current values
            po_current = picture.getLs_po_sortedByX().getItem();
        }


    	//finish transaction 
    	picture.getLs_po_sortedByX().finishTransaction(
    			transaction);
    	
        //finish insertion into selected.
        picture.finishSelection(getcTabSelection());
        
        //paint the selected item and refresh entire painting.
        picture.paintSelected(getPage(),
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
        picture.createSelected();

        // go to the beginning of the list
        if (!picture.getLs_po_sortedByX().isEmpty()) {


        	//start transaction 
        	final int transaction = picture.getLs_po_sortedByX()
        			.startTransaction("Selection curve destroy", 
        					SecureList.ID_NO_PREDECESSOR);
        	
            // go to the beginning of the list
            picture.getLs_po_sortedByX().toFirst(
            		transaction, SecureList.ID_NO_PREDECESSOR);
            
            // create and initialize current values
            PaintObject po_current = picture.getLs_po_sortedByX()
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
                    new PictureOverview(view.getTabs().getTab_debug()).remove(picture
                            .getLs_po_sortedByX().getItem());
                    picture.getLs_po_sortedByX().remove(
                    		transaction);
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {

                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            picture.insertIntoSelected(
                                    separatedPO[1][current], getView().getTabs().getTab_debug());
                        } else {
                            
                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    
                    //finish insertion into selected.
                    picture.finishSelection(getcTabSelection());
                    
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview(view.getTabs().getTab_debug()).add(
                                    separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    picture.getLs_po_sortedByX().toFirst(
                    		transaction, SecureList.ID_NO_PREDECESSOR);
                } else {
                    
                    // next
                    picture.getLs_po_sortedByX().next(
                    		transaction, SecureList.ID_NO_PREDECESSOR);
                }


                // update current values
                po_current = picture.getLs_po_sortedByX()
                        .getItem();
            }

            
            //insert the to insert items to graphical user interface.
            ls_toInsert.toFirst();
            while (!ls_toInsert.isBehind() && !ls_toInsert.isEmpty()) {

                picture.getLs_po_sortedByX().insertSorted(
                        ls_toInsert.getItem(), 
                        ls_toInsert.getItem().getSnapshotBounds().x,
                        transaction);
                ls_toInsert.next();
            }

        	//finish transaction 
        	picture.getLs_po_sortedByX().finishTransaction(
        			transaction);
        	
            if (picture.paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {
            	controlPic.refreshPaint();
            }

        }


        picture.paintSelected(getPage(),
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
    private synchronized void mr_sel_line_complete(
            final Rectangle _r_size) {

    	
    	//start transaction 
    	final int transaction = picture.getLs_po_sortedByX()
    			.startTransaction("Selection line complete", 
    					SecureList.ID_NO_PREDECESSOR);
        /*
         * SELECT ALL ITEMS INSIDE RECTANGLE    
         */
        //case: there can't be items inside rectangle because list is empty
        if (picture.getLs_po_sortedByX().isEmpty()) {

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
        picture.getLs_po_sortedByX().toFirst(
        		transaction, SecureList.ID_NO_PREDECESSOR);
        picture.createSelected();
        
        // create and initialize current values (current PO and its coordinates)
        PaintObject po_current = picture.getLs_po_sortedByX()
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
                 new PictureOverview(view.getTabs().getTab_debug()).remove(po_current);
                
                //move current item from normal list into selected list 
                picture.insertIntoSelected(po_current, getView().getTabs().getTab_debug());
                             
                picture.getLs_po_sortedByX().remove(
                		transaction);
            }            

            picture.getLs_po_sortedByX().next(
            		transaction, SecureList.ID_NO_PREDECESSOR);

            // update current values
            currentX = po_current.getSnapshotBounds().x;
            po_current = picture.getLs_po_sortedByX().getItem();

        }

    	//finish transaction 
    	picture.getLs_po_sortedByX().finishTransaction(
    			transaction);

        //finish insertion into selected.
        picture.finishSelection(getcTabSelection());
        
        controlPic.refreshPaint();

        if (!picture.paintSelected(getPage(),
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
    	final int transaction = picture.getLs_po_sortedByX()
    			.startTransaction("Selection line destroy", 
    					SecureList.ID_NO_PREDECESSOR);
        /*
         * whole item selection.
         */
        // initialize selection list
        picture.createSelected();

        // go to the beginning of the list
        picture.getLs_po_sortedByX().toFirst(transaction, 
        		SecureList.ID_NO_PREDECESSOR);
        if (!picture.getLs_po_sortedByX().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = picture.getLs_po_sortedByX()
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
                     new PictureOverview(view.getTabs().getTab_debug()).remove(picture
                            .getLs_po_sortedByX().getItem());
                    picture.getLs_po_sortedByX().remove(
                    		transaction);
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {

                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            picture.insertIntoSelected(
                                    separatedPO[1][current],
                                    getView().getTabs().getTab_debug()
                            		);
                        } else {
                            
                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                        
                    }

                    //finish insertion into selected.
                    picture.finishSelection(getcTabSelection());
                    
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview(view.getTabs().getTab_debug()).add(
                                    separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                } 
                // next
                picture.getLs_po_sortedByX().next(transaction,
                		SecureList.ID_NO_PREDECESSOR);


                // update current values
                currentX = po_current.getSnapshotBounds().x;
                po_current = picture.getLs_po_sortedByX()
                        .getItem();
            }

            
            //insert the to insert items to graphical user interface.
            ls_toInsert.toFirst();
            while (!ls_toInsert.isBehind() && !ls_toInsert.isEmpty()) {

                picture.getLs_po_sortedByX().insertSorted(
                        ls_toInsert.getItem(), 
                        ls_toInsert.getItem().getSnapshotBounds().x,
                        transaction);
                ls_toInsert.next();
            }
            if (picture.paintSelected(getPage(),
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
    	picture.getLs_po_sortedByX().finishTransaction(
    			transaction);
    }
    
    
    
    /**
     * Erase functionality at mouseReleased.
     * @param _p the Point.
     */
    public synchronized void mr_erase(final Point _p) {

    	
    	
    	//start transaction 
    	final int transaction = picture.getLs_po_sortedByX()
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
        picture.createSelected();

        // go to the beginning of the list
        picture.getLs_po_sortedByX().toFirst(transaction,
        		SecureList.ID_NO_PREDECESSOR);
        if (!picture.getLs_po_sortedByX().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = picture.getLs_po_sortedByX()
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
            
            
            switch (Status.getEraseIndex()) {
            case Status.ERASE_ALL:

                
                // go through list. until either list is empty or it is
                // impossible for the paintSelection to paint inside the
                // selected area
                while (po_current != null
                        && currentX 
                        <= (r_sizeField.x + r_sizeField.width)) {


                    //The y condition has to be in here because the items are just 
                    //sorted by x coordinate; thus it is possible that one PaintObject 
                    //is not suitable for the specified rectangle but some of its 
                    //predecessors in sorted list do.
                    if (po_current.isInSelectionImage(r_sizeField)) {


                        //remove item out of PictureOverview and paint and refresh paint
                        //otherwise it is not possible to select more than one item
                         new PictureOverview(view.getTabs().getTab_debug()).remove(po_current);
                        
                        picture.getLs_po_sortedByX().remove(
                        		transaction);
                    }            

                    picture.getLs_po_sortedByX().next(
                    		transaction, SecureList.ID_NO_PREDECESSOR);

                    // update current values
                    currentX = po_current.getSnapshotBounds().x;
                    po_current = picture.getLs_po_sortedByX().getItem();

                }

            	//finish transaction 
            	picture.getLs_po_sortedByX().finishTransaction(
            			transaction);

                //finish insertion into selected.
                picture.finishSelection(getcTabSelection());
                
                controlPic.refreshPaint();
            	
            	
            	
            	
            	
            	
            	
            	picture.deleteSelected(getView().getTabs().getTab_debug(), cTabSelection);
            	break;
            case Status.ERASE_DESTROY:

            
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
	
	                            new PictureOverview(view.getTabs().getTab_debug()).remove(picture
	                                    .getLs_po_sortedByX().getItem());
	                    	}
	                        picture.getLs_po_sortedByX().remove(
	                        		transaction);
	                	}
	                } 
	                // next
	                picture.getLs_po_sortedByX().next(transaction,
	                		SecureList.ID_NO_PREDECESSOR);
	
	
	                // update current values
	                currentX = po_current.getSnapshotBounds().x;
	                po_current = picture.getLs_po_sortedByX()
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
	
	                        picture.getLs_po_sortedByX().insertSorted(
	                        		ls_separatedPO.getItem(), 
	                        		ls_separatedPO.getItem().getSnapshotBounds().x,
	                        		transaction);
	
	                    	if (debug_update_paintObjects_view) {
	
	                            new PictureOverview(view.getTabs().getTab_debug()).add(
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
	        	picture.getLs_po_sortedByX().finishTransaction(
	        			transaction);
	        
	            
	            if (picture.paintSelected(getPage(),
	        			getControlPic(),
	        			getControlPaintSelection())) {
	            	controlPic.refreshPaint();
	            }
	
	            r_sizeField.x /= cZoomFactorWidth;
	            r_sizeField.width /= cZoomFactorWidth;
	            r_sizeField.y /= cZoomFactorHeight;
	            r_sizeField.height /= cZoomFactorHeight;
	
	        	break;
	        }
        } else {

        	//finish transaction
        	picture.getLs_po_sortedByX().finishTransaction(
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
        picture.createSelected();

    	//start transaction 
    	final int transaction = picture.getLs_po_sortedByX()
    			.startTransaction("erase", 
    					SecureList.ID_NO_PREDECESSOR);
        // go to the beginning of the list
        picture.getLs_po_sortedByX().toFirst(transaction,
        		SecureList.ID_NO_PREDECESSOR);
        if (!picture.getLs_po_sortedByX().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = picture.getLs_po_sortedByX()
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

                            new PictureOverview(view.getTabs().getTab_debug()).remove(picture
                                    .getLs_po_sortedByX().getItem());
                    	}
                        picture.getLs_po_sortedByX().remove(
                        		transaction);
                	}
                } 
                // next
                picture.getLs_po_sortedByX().next(transaction,
                		SecureList.ID_NO_PREDECESSOR);


                // update current values
                currentX = po_current.getSnapshotBounds().x;
                po_current = picture.getLs_po_sortedByX()
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

                        picture.getLs_po_sortedByX().insertSorted(
                        		ls_separatedPO.getItem(), 
                        		ls_separatedPO.getItem().getSnapshotBounds().x,
                        		transaction);

                    	if (debug_update_paintObjects_view) {

                            new PictureOverview(view.getTabs().getTab_debug())
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
        	picture.getLs_po_sortedByX().finishTransaction(
        			transaction);
            
            if (picture.paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {
            	controlPic.refreshPaint();
            }
        } else {

        	//finish transaction
        	picture.getLs_po_sortedByX().finishTransaction(
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


        int color = picture.getColorPX(_event.getX(), 
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
	public final ControlPaintSelectin getControlPaintSelection() {
		return controlPaintSelection;
	}




	/**
	 * @return the cTabPaint
	 */
	public final ControlTabPainting getcTabPaint() {
		return cTabPaint;
	}






	/**
	 * @return the cTabPrint
	 */
	public final CPrint getcTabPrint() {
		return cTabPrint;
	}





	/**
	 * @return the cTabWrite
	 */
	public final CWrite getcTabWrite() {
		return cTabWrite;
	}








	/**
	 * @return the cTabPaintStatus
	 */
	public final CPaintStatus getcTabPaintStatus() {
		return cTabPaintStatus;
	}




	/**
	 * {@inheritDoc}
	 */
	public final void beforeOpen() {

    	//release selected because of display bug otherwise.
    	if (picture.isSelected()) {
	    	picture.releaseSelected(
        			getControlPaintSelection(),
        			getcTabSelection(),
        			getView().getTabs().getTab_debug(),
        			getView().getPage().getJlbl_painting().getLocation().x,
        			getView().getPage().getJlbl_painting().getLocation().y);
	    	controlPic.releaseSelected();
    	}		
	}




	/**
	 *  {@inheritDoc}
	 */
	public final void beforeClose() {

    	//release selected because of display bug otherwise.
    	if (picture.isSelected()) {
	    	picture.releaseSelected(
        			getControlPaintSelection(),
        			getcTabSelection(),
        			getView().getTabs().getTab_debug(),
        			getView().getPage().getJlbl_painting().getLocation().x,
        			getView().getPage().getJlbl_painting().getLocation().y);
	    	controlPic.releaseSelected();
    	}		
	}



	/**
	 * {@inheritDoc}
	 */
	public void afterOpen() { }




	/**
	 * {@inheritDoc}
	 */
	public final void afterClose() {

        //when closed repaint.
        getPage().getJlbl_painting().repaint();
        getTabs().repaint();		
	}


    
    /**
     * Error checked getter method for page.
     * 
     * @return 	instance of Page fetched out of the saved view class.
     */
    private Page getPage() {
    	
    	if (view != null) {
    		if (view.getPage() != null) {
    	    	return view.getPage();
    		} else {
    			Status.getLogger().severe("view.getPage() is null");
    		}
    	} else {
			Status.getLogger().severe("view is null");
    	}
    	return null;
    }

    
    /**
     * Error checked getter method for tabs.
     * 
     * @return 	instance of Tabs fetched out of the saved view class.
     */
    private Tabs getTabs() {
    	if (view != null) {
    		if (view.getTabs() != null) {
    	    	return view.getTabs();
    		} else {
    			Status.getLogger().severe("view.getTabs() is null");
    		}
    	} else {
			Status.getLogger().severe("view is null");
    	}
    	return null;
    }


	/**
	 * @return the cTabs
	 */
	public final CTabs getcTabs() {
		return cTabs;
	}




	/**
	 * @param _cTabs the cTabs to set
	 */
	public final void setcTabs(final CTabs _cTabs) {
		this.cTabs = _cTabs;
	}




	/**
	 * @return the cTabSelection
	 */
	public final CTabSelection getcTabSelection() {
		return cTabSelection;
	}




	/**
	 * @param _cTabSelection the cTabSelection to set
	 */
	public final void setcTabSelection(final CTabSelection _cTabSelection) {
		this.cTabSelection = _cTabSelection;
	}




	/**
	 * @return the cTabLook
	 */
	public final CLook getcTabLook() {
		return cTabLook;
	}







	/**
	 * @return the cTabExport
	 */
	public final CExport getcTabExport() {
		return cTabExport;
	}






	/**
	 * @return the cTabPaintObjects
	 */
	public final CPaintObjects getcTabPaintObjects() {
		return cTabPaintObjects;
	}





	/**
	 * @return the controlPic
	 */
	public final ContorlPicture getControlPic() {
		return controlPic;
	}





	/**
	 * @return the controlnew
	 */
	public final CNew getControlnew() {
		return controlnew;
	}




	/**
	 * 
	 * @return the controlQuickAccess
	 */
	public final CQuickAccess getControlQuickAccess() {
		return controlQuickAccess;
	}


	/**
	 * 
	 * @return the utilityControlScrollPane
	 */
	public final ScrollPaneActivityListener getUtilityControlScrollPane() {
		return utilityControlScrollPane;
	}


	/**
	 * 
	 * @return the utilityControlItem2
	 */
	public final Item2ActivityListener getUtilityControlItem2() {
		return utilityControlItem2;
	}

	/**
	 * @return the picture
	 */
	public final Picture getPicture() {
		return picture;
	}

	/**
	 * @return the cTabAbout
	 */
	public final CAbout getcTabAbout() {
		return cTabAbout;
	}




	public Project getProject() {
		return project;
	}




	public void setProject(Project project) {
		this.project = project;
	}
    
}
