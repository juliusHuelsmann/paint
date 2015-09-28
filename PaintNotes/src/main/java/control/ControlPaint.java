package control;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import control.forms.CLoading;
import control.forms.CNew;
import control.forms.CPaintStatus;
import control.forms.CQuickAccess;
import control.forms.minorImportance.InfoSelection;
import control.forms.tabs.CTabAboutPaint;
import control.forms.tabs.CTabDebug;
import control.forms.tabs.CTabExport;
import control.forms.tabs.CTabInsert;
import control.forms.tabs.CTabLook;
import control.forms.tabs.CTabProject;
import control.forms.tabs.CTabTools;
import control.forms.tabs.CTabPrint;
import control.forms.tabs.CTabSelection;
import control.forms.tabs.CTabWrite;
import control.forms.tabs.CTabs;
import control.interfaces.MenuListener;
import control.util.implementations.ScrollPaneActivityListener;
import model.debug.ActionManager;
import model.debug.debugTools.DebugUtil;
import model.objects.PictureOverview;
import model.objects.Project;
import model.objects.Zoom;
import model.objects.painting.PaintBI;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.Pen;
import model.objects.pen.special.PenSelection;
import model.settings.Constants;
import model.settings.ReadSettings;
import model.settings.StateStandard;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.DPoint;
import model.util.Util;
import model.util.adt.list.List;
import model.util.adt.list.SecureList;
import view.View;
import view.forms.Console;
import view.forms.Message;
import view.forms.Page;
import view.forms.Tabs;


/**
 * 
 * This is the main controller class. It contains all the important
 * sub-controllers and references to the central view class called
 * View and to the central model class Project. 
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ControlPaint implements MouseListener, MouseMotionListener, 
MenuListener {

	/**
	 * Central view class which displays all main components 
	 * that are important for the program.
	 */
	private View view;
	
	/*
	 * Controller classes that are essential for the execution of
	 * the program.
	 */

	
	/**
	 * This controller class is responsible for the entire painting.
	 */
	private ContorlPicture controlPic;

	
	/**
	 * Controller class which is handling the different states the program
	 * can be in. For example there are states for erasing, painting, etc.
	 */
	private CPaintStatus cTabPaintStatus;
	
	
	/*
	 * Controller class for special forms inside program
	 * that are none-utility forms.
	 */

	
	/**
	 * Controller class which handles the zooming JLabel placement.
	 */
	private Zoom zoom;
	
	
	/**
	 * Controller class for the form for creating a new page.
	 */
	private CNew controlnew = null;
	
	
	
	/**
	 * Controller class for loading frame which appears if the 
	 * user has to wait for the program to finish a certain operation.
	 */
	@SuppressWarnings("unused")
	private CLoading cl;

	
	/**
	 * Controller class for the quick access form.
	 */
	private CQuickAccess controlQuickAccess;
	
	
	
	
	/*
	 * Controller classes for TabbedPane
	 */
	
	
	/**
	 * Controller class for the general TabbedPane determines 
	 * the behavior of the program in case the TabbedPane is 
	 * closed or opened.
	 */
	private CTabs cTabs;
	
	
	/*
	 * Controller class of tabs of TabbedPane
	 */
	
	/**
	 * Controller class for selection paint at the same level as this class.
	 */
	private ControlSelectionTransform controlPaintSelect;
	
	/**
	 * Controller class for the painting tab.
	 */
	private CTabTools cTabPaint;
	
	/**
	 * Controller class for the tab Print.
	 */
	private CTabPrint cTabPrint; 
	
	/**
	 * Controller class for the tab write.
	 */
	private CTabWrite cTabWrite;
	
	/**
	 * Controller class for the tab about.
	 */
	private CTabAboutPaint cTabAbout;
	
	
	/**
	 * Controller class for the tab selection.
	 */
	private CTabSelection cTabSelection;
	
	/**
	 * Controller class for the tab export.
	 */
	private CTabExport cTabExport;
	
	/**
	 * Controller class for the tab look.
	 */
	private CTabLook cTabLook;
	
	/**
	 * Controller class for the tab project.
	 */
	private CTabProject cTabProject;
	
	/**
	 * Controller class for the tab debug.
	 */
	private CTabDebug cTabPaintObjects;

	/*
	 * Rather utility controller classes that adapt pure utility classes
	 * to the needs of the program. 
	 * 
	 * For example, if the scrollPane is moved, it is necessary to repaint
	 * some pixel out of the image. That cannot be done directly by the
	 * ScrollPane utility controller because that would cause a loss of
	 * universality of the utility controller class.
	 */
	

	/**
	 * Utility listener for the ScrollPane (closes menus and repaints
	 * the picture).
	 */
	private ScrollPaneActivityListener utilityControlScrollPane;
	
	/**
	 * 
	 */
	private CTabInsert utilityControlItem2;
	
	
	
	
	/*
	 * Model classes:
	 */
	
	
	/**
	 * The class project is the root model class which contains
	 * all the essential model classes.
	 */
	private Project project;

	
	
	/*
	 * Computation values.
	 * 
	 */
	
    //TODO: quadratic? movement function which interpolates a time interval
    //of x milliseconds. Computation possible. Speed? Maybe other solutions?
	/**
     * Start point mouseDragged and the speed of movement for continuous 
     * movement.
     */
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

    /*
     * Values that are necessary for the preprint.
     */
    
    /**
     * The BufferedImage which contains the preprint of a pen.
     * Is saved because in this way it is much quicker to display
     * a preprint and to remove the old one.
     */
    private BufferedImage bi_preprint;
    
    
    /**
     * This point saves the location of the last preprint relative
     * to the borders of the currently displayed picture scope.
     * 
     * Is null if there currently is no displayed preprint.
     */
    private Rectangle rect_preprintBounds;
    
    /**
     * Info selection.
     */
    private InfoSelection info_selection;
    
	/**
	 * Constructor of the main controller class.
	 */
	public ControlPaint() { }
	

	/**
	 * Initializes controller class for being able to change settings 
	 * inside the specified class even though the class has not been entirely
	 * initialized.
	 */
	public final void initialize() {


		double startTime = System.currentTimeMillis();

        //get location of current workspace and set logger level to finest; 
		//thus every log message is shown.
        StateStandard.setWsLocation(ReadSettings.install(), false);
        State.getLogger().setLevel(Level.WARNING);

        //if the installation has been found, initialize the whole program.
        //Otherwise print an error and exit program
        if (!StateStandard.getWsLocation().equals("")) {
            
        	//Print logger status to console.
            State.getLogger().info("Installation found.");
            State.getLogger().info("Initialize model class Page.\n");

            State.setControlPaint(this);
            project = new Project();
            project.initialize();
            

            utilityControlScrollPane = new ScrollPaneActivityListener(this);
            //initialize the model class picture.
            //TODO: not null
            controlnew = new CNew(this);
            cTabPrint = new CTabPrint(this);
            cTabLook = new CTabLook(this);
            cTabProject = new CTabProject(this);
            cTabExport = new CTabExport(this);
            cTabWrite = new CTabWrite(this);
            cTabAbout = new CTabAboutPaint(this);
            controlPic = new ContorlPicture(this);
            cTabSelection = new CTabSelection(this);
            controlPaintSelect = new ControlSelectionTransform(this);
            cTabPaint = new CTabTools(this);
            cTabPaintStatus = new CPaintStatus(this);
            cTabPaintObjects = new CTabDebug(this);
            zoom = new Zoom(controlPic);
            utilityControlItem2 = new CTabInsert(this);
            cTabs = new CTabs(this);
            info_selection = new InfoSelection(this);
            
            //initialize the preprint image.
            bi_preprint = Util.getEmptyBISelection();

            //initialize view class and log information on current 
            //initialization progress
            State.getLogger().info("initialize view class and set visible.");

            view = new View();
            System.err.println("h" + (System.currentTimeMillis() - startTime));
            view.initialize(this);	//1000MS
            System.err.println(System.currentTimeMillis() - startTime);
            view.setVisible(true);	// 400MS
            System.err.println(System.currentTimeMillis() - startTime);

//            cl = new CLoading(view.getLoading());
            
            
            //enable current operation
            view.getTabs().getTab_paint().getTb_color1().setActivated(true);
            view.getTabs().getTab_paint().getIt_stift1()
            .getTb_open().setActivated(true);

            //initialize help listeners
            view.getTabs().initializeHelpListeners(
            		view, view.getHelp());
            
            /*
             * Initialize control
             */
            State.getLogger().info("initialize controller class.");
            
            State.getLogger().info(
                    "Start handling actions and initialize listeners.\n");

            State.getLogger().info("initialization process completed.\n\n"
                    + "-------------------------------------------------\n");

            

    		/*
    		 * Call update procedure.
    		 */
    		

    		boolean updateOnStart = true;
    		if (updateOnStart) {
    			ReadSettings.update(view, false);
    		}

            
        } else {

            //if not installed and no installation done print error and write
        	//null values into final variables
        	State.getLogger().severe("Fatal error: no installation found");
        	this.view = null;
        	this.project = null;
        	
        	//exit program
            System.exit(1);
        }
        DebugUtil.checkComponentsFocusable(getView());
	
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
			if (!project.getCurrentPicture().isSelected()) {
				switch (State.getIndexOperation()) {
				
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
					if (getTabs() != null) {
						
						if (!getTabs().isMenuOpen()) {
							
							removePreprint();
							break;
						}
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

        	// the name of the action. Just used for debugging purpose.
        	// the name of the action which contains necessary information for debugging.
			// Just used for debugging purpose.
        	final Point imgCoord = new Point(
        			-(int) ((getPage().getJlbl_painting().getLocation().getX() 
        			- _event.getX())
        			* State.getImageSize().width 
        			/ State.getImageShowSize().width
        			),
        			-(int) ((getPage().getJlbl_painting().getLocation().getY() 
        			- _event.getY())
        			* State.getImageSize().height 
        			/ State.getImageShowSize().height));
			final String actionName = "mousePressed at Picture."
        			+ "\n\t\tActionName:\t\t"
        			+ State.getIndexName(State.getIndexOperation())
        			+ "\n\t\tMouse Key: \t\t" + _event.getButton()
        			+ "\n\t\tCmp-Coord: \t\t" + _event.getX() + "," + _event.getY()
        			+ "\n\t\tImg-Coord: \t\t" + imgCoord.getX() + "," + imgCoord.getY();
        	ActionManager.addUserAction(actionName, true);
        	
        	if (getTabs().isMenuOpen()) {
                getTabs().closeMenues();
        	}
            
            // switch index of operation
            switch (State.getIndexOperation()) {

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

                if ((State.getIndexOperation() 
                        != Constants.CONTROL_PAINTING_INDEX_I_G_CURVE
                        && State.getIndexOperation() 
                        != Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2)
                        || project.getCurrentPicture().isPaintObjectReady()) {
                    

                    // set currently selected pen. Differs if
                    if (_event.getButton() == MouseEvent.BUTTON1) {

                        if (State.getIndexOperation() 
                                == Constants.CONTROL_PAINTING_INDEX_PAINT_1) {
                            project.getCurrentPicture().changePen(Pen.clonePen(
                                    State.getPenSelected1()));

                        } else {

                            project.getCurrentPicture().changePen(Pen.clonePen(
                                    State.getPenSelected2()));
                        }
                    } else if (_event.getButton() == MouseEvent.BUTTON3) {

                        if (State.getIndexOperation() 
                                == Constants.CONTROL_PAINTING_INDEX_PAINT_1) {
                            project.getCurrentPicture().changePen(Pen.clonePen(
                                    State.getPenSelected2()));
                        } else {


                            project.getCurrentPicture().changePen(Pen.clonePen(
                                    State.getPenSelected1()));

                        }
                    }
                }
                    
                // add paintObject and point to Picture
                project.getCurrentPicture().addPaintObject(getTabs().getTab_insert());
                changePO(_event);
                break;
            
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

                // abort old paint object
                project.getCurrentPicture().abortPaintObject(controlPic);

                //set the old pen to replace the curve afterwards.
                State.setPen_selectedReplaced(Pen.clonePen(
                		State.getPenSelected1()));
                
                // change pen and add new paint object
                project.getCurrentPicture().changePen(new PenSelection());
                project.getCurrentPicture().addPaintObjectWrinting();
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
                						1.0 * State.getImageSize().width
                						/ State.getImageShowSize().width
                						/ State.getEraseRadius(),

                						//value if border is visible on screen; 
                						//zoomed out
                						1.0 * State.getImageSize().width
                						/ getPage().getJlbl_painting()
                						.getWidth()
                						/ State.getEraseRadius());
                		final double factorHeight = 
                				1.0 * Math.min(
                						
                						//the value if the border of the picture
                						//is not displayed (because not enough
                						//zoom out)
                						1.0 * State.getImageSize().height
                						/ State.getImageShowSize().height
                						/ State.getEraseRadius(),

                						//value if border is visible on screen; 
                						//zoomed out
                						1.0 * State.getImageSize().height
                						/ getPage().getJlbl_painting()
                						.getHeight()
                						/ State.getEraseRadius());
                		
                		field_erase [(int) (_event.getPoint().x * factorWidth)]
                					[(int) (_event.getPoint().y * factorHeight)]
                							= PaintBI.FREE;

                		final int displayHeight = State.getEraseRadius()
                				* State.getImageShowSize().height
                				/ State.getImageSize().height;
                		final int displayWidth = State.getEraseRadius()
                				* State.getImageShowSize().width
                				/ State.getImageSize().width;
                		
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
            	
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

//                project.getPicture().abortPaintObject(controlPic);
//                project.getPicture().changePen(new PenSelection());
//                project.getPicture().addPaintObjectWrinting();
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

        	// the name of the action which contains necessary information for debugging.
			// Just used for debugging purpose.
        	final Point imgCoord = new Point(
        			-(int) ((getPage().getJlbl_painting().getLocation().getX() 
        			- _event.getX())
        			* State.getImageSize().width 
        			/ State.getImageShowSize().width
        			),
        			-(int) ((getPage().getJlbl_painting().getLocation().getY() 
        			- _event.getY())
        			* State.getImageSize().height 
        			/ State.getImageShowSize().height));
			final String actionName = "mouseReleased at Picture."
        			+ "\n\t\tActionName:\t\t"
        			+ State.getIndexName(State.getIndexOperation())
        			+ "\n\t\tMouse Key: \t\t" + _event.getButton()
        			+ "\n\t\tCmp-Coord: \t\t" + _event.getX() + "," + _event.getY()
        			+ "\n\t\tImg-Coord: \t\t" + imgCoord.getX() + "," + imgCoord.getY();
        	
        	
            //add user action just for debugging purpose.
        	ActionManager.addUserAction(actionName, false);
		} 
	}



	/**
	 * {@inheritDoc}
	 */
	public final void mouseDragged(final MouseEvent _event) {


        // left mouse pressed
        final int leftMouse = 1024;
        if (_event.getSource().equals(getPage().getJlbl_painting())) {
            switch (State.getIndexOperation()) {
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
                						1.0 * State.getImageSize().width
                						/ State.getImageShowSize().width
                						/ State.getEraseRadius(),

                						//value if border is visible on screen; 
                						//zoomed out
                						1.0 * State.getImageSize().width
                						/ getPage().getJlbl_painting()
                						.getWidth()
                						/ State.getEraseRadius());
                		final double factorHeight = 
                				1.0 * Math.min(
                						
                						//the value if the border of the picture
                						//is not displayed (because not enough
                						//zoom out)
                						1.0 * State.getImageSize().height
                						/ State.getImageShowSize().height
                						/ State.getEraseRadius(),

                						//value if border is visible on screen; 
                						//zoomed out
                						1.0 * State.getImageSize().height
                						/ getPage().getJlbl_painting()
                						.getHeight()
                						/ State.getEraseRadius());
                		
                		field_erase [(int) (_event.getPoint().x * factorWidth)]
                					[(int) (_event.getPoint().y * factorHeight)]
                							= PaintBI.FREE;

                		final int displayHeight = State.getEraseRadius()
                				* State.getImageShowSize().height
                				/ State.getImageSize().height;
                		final int displayWidth = State.getEraseRadius()
                				* State.getImageShowSize().width
                				/ State.getImageSize().width;
                		
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
                        project.getCurrentPicture().abortPaintObject(controlPic);
                    }

                    if (pnt_start != null) {

                        int xLocation = Math.min(pnt_start.x, _event.getX());
                        int yLocation = Math.min(pnt_start.y, _event.getY());
                        // not smaller than zero
                        xLocation = Math.max(0, xLocation);
                        yLocation = Math.max(0, yLocation);

                        // not greater than the entire shown image - 
                        // the width of zoom
                        int xSize = Math.min(State.getImageShowSize().width
                                - xLocation, 
                                Math.abs(pnt_start.x - _event.getX()));
                        int ySize = Math.min(State.getImageShowSize().height
                                - yLocation, 
                                Math.abs(pnt_start.y - _event.getY()));

                        getPage().getJlbl_border().setBounds(xLocation,
                                yLocation, xSize, ySize);
                    } else {
                        
                        State.getLogger().warning("Want to print border but"
                                + "the starting point is null.");
                    }
                    break;
                }

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

//                if (_event.getModifiersEx() == leftMouse) {
//
//                    project.getPicture().abortPaintObject(controlPic);
//                    project.getPicture().changePen(new PenSelection());
//                    project.getPicture().addPaintObjectWrinting();
//                    break;
//                }
            case Constants.CONTROL_PAINTING_INDEX_MOVE:
                if (_event.getModifiersEx() == leftMouse) {
                	
                	double d0 = System.currentTimeMillis();
                    if (pnt_start == null) {
                        pnt_start = _event.getPoint();
                        pnt_startLocation = getPage()
                                .getJlbl_painting().getLocation();
                        project.getCurrentPicture().abortPaintObject(controlPic);
                    }
                    
                    if (pnt_last != null) {
                        pnt_movementSpeed = new Point(
                                pnt_last.x - _event.getX(), 
                                pnt_last.y - _event.getY());
                        if (!State.isNormalRotation()) {

                            pnt_movementSpeed = new Point(
                                    -pnt_last.x + _event.getX(), 
                                    -pnt_last.y + _event.getY());
                        }
                    }
                    //Scroll
                    int x = pnt_startLocation.x + _event.getX() - pnt_start.x;
                    int y = pnt_startLocation.y +  _event.getY() - pnt_start.y;

                    if (!State.isNormalRotation()) {

                        x = pnt_startLocation.x - _event.getX() + pnt_start.x;
                        y = pnt_startLocation.y -  _event.getY() + pnt_start.y;
                    }
                    
                    if (x < -State.getImageShowSize().width 
                    		+ getPage().getJlbl_painting().getWidth()) {
                    	
                        x = -State.getImageShowSize().width 
                        		+ getPage().getJlbl_painting().getWidth();
                    }
                    if (x > 0) {
                        x = 0;
                    }
                    if (y < -State.getImageShowSize().height 
                    		+ getPage().getJlbl_painting().getHeight()) {
                        y = -State.getImageShowSize().height 
                        		+ getPage().getJlbl_painting().getHeight();
                    }
                    if (y >= 0) {
                        y = 0;
                    } 
                    getPage().getJlbl_painting().setLocation(x, y);
                    
                    
                    
                    getPage().refrehsSps();
                    pnt_last = _event.getPoint();
                	double d1 = System.currentTimeMillis();
                	System.out.println(getClass() + " time used:\t" + (d1 - d0));
                    break;
                }

            default:
                break;
            }
        }
//        New.getInstance().repaint();
    		
	}
	
	

	
	public void changeLocationSelectionOnScroll(final int _x, final int _y) {

        //
        // change location of the current selection
        //
        if (getPicture().isSelected()) {
        	
        	//calculate the shift in here not directly because the
        	//check whether the new coordinates are in range is
        	//performed with values x and y.
        	int shiftX = _x - pnt_startLocation.x;
        	int shiftY = _y - pnt_startLocation.y;
        	
        	//stop border thread for movement
        	controlPic.stopBorderThread();

        	// the shift compared to the old location for 
        	// not applying a move after the selection has been released
        	// to the selected items which is due to image shift.
        	int cmpX = 
        			//location of picture after change
        			shiftX 
        			
        			//location of picture before change
        			- getPage().getJlbl_selectionBG().getLocation().x;
        	int cmpY = 
        			//location of picture afterwards
        			shiftY 
        			//location of picture before change
        			- getPage().getJlbl_selectionBG().getLocation().y;
        	
        	
            getPage().getJlbl_selectionPainting().setLocation(
            		shiftX, shiftY);
            getPage().getJlbl_selectionBG().setLocation(
            		shiftX, shiftY);
            
            
//            // update location of rectangle and startpaintlabellocation
//            getControlPaintSelection().setOldPaintLabelLocation(
//            		new Point(getControlPaintSelection().getOldPaintLabelLocation().x
//            				- cmpX,
//            				getControlPaintSelection().getOldPaintLabelLocation().y
//                    				- cmpY));
//            Rectangle rect = new Rectangle(
//            		getControlPaintSelection().getR_selection().x
//            		- cmpX,
//            		getControlPaintSelection().getR_selection().y
//            		- cmpY,
//            		getControlPaintSelection().getR_selection().width,
//            		getControlPaintSelection().getR_selection().height);
//            Point pnt = new Point(
//            		(int) getControlPaintSelection().getOldPaintLabelLocation().getX(),
////            		+ cmpX,
//            		(int) getControlPaintSelection().getOldPaintLabelLocation().getY());
////            		+ cmpY);
//            
//            getControlPaintSelection().setR_selection(rect, pnt);
            getControlPaintSelection().getR_selection().x += cmpX;
            getControlPaintSelection().getR_selection().y += cmpY;
            
            //the location of the selected area is different from
            //those of the selection background and painting.
            getPage().getJlbl_border().setLocation(
            		getControlPaintSelection().getR_selection().x,
//            		+ shiftX,
            		getControlPaintSelection().getR_selection().y
//            		+ shiftY
            		);

            for (int h = 0; h <= 2; h++) {
                for (int w = 0; w <= 2; w++) {

                    getPage().getJbtn_resize()[h][w].setLocation(
                    		getControlPaintSelection()
                    		.getR_selection().x
                    		+ getControlPaintSelection()
                    		.getR_selection().width * (h) / 2
//                    		+ shiftX
                    		- getPage()
                    		.getJbtn_resize()[h][w].getWidth() / 2,
                    		getControlPaintSelection()
                    		.getR_selection().y 
                    		+ getControlPaintSelection()
                    		.getR_selection().height * w / 2
//                    		+ shiftY
                    		- getPage()
                    		.getJbtn_resize()[h][w].getHeight()
                    		/ 2);

				}
			}
        }
	}


	/**
	 * {@inheritDoc}
	 */
	public final void mouseMoved(final MouseEvent _event) {


        if (_event.getSource().equals(getPage().getJlbl_painting())) {

            switch (State.getIndexOperation()) {
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
	                xLocation = Math.min(State.getImageShowSize().width
	                        - ViewSettings.ZOOM_SIZE.width, xLocation);
	                yLocation = Math.min(State.getImageShowSize().height
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
            	
            	if (getTabs() != null && !getTabs().isMenuOpen()) {


                    if (State.isNormalRotation()) {

                        performPreprint(_event.getX(), _event.getY());
                        
                    } else {


                        performPreprint(
                        		getPage().getJlbl_painting().getWidth() 
                        		- _event.getX(), 
                                getPage().getJlbl_painting().getHeight() 
                                - _event.getY());

                    }
            	}
                break;
            default:
                
            	
            	
                break;
            }
        }
        
        
        /*
         * Log location information.
         */
        final double zoomWidth = State.getImageSize().width 
        		/ State.getImageShowSize().width;
        final double zoomHeight = State.getImageSize().height 
        		/ State.getImageShowSize().height;
        Console.getInstance().logPosition(
        		(int) (zoomWidth * (_event.getX() 
        				- getPage().getJlbl_painting().getLocation().getX())),
        		(int) (zoomHeight * (_event.getY() 
        				- getPage().getJlbl_painting().getLocation().getY())));
    		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

    
    /**
	 * Change PaintObject.
	 * @param _event the event.
	 */
	private void changePO(final MouseEvent _event) {
	
	    if (State.isNormalRotation()) {
	
	        project.getCurrentPicture().changePaintObject(new DPoint((_event.getX() 
	        		- getPage().getJlbl_painting().getLocation().x)
	        		* State.getImageSize().width
	        		/ State.getImageShowSize().width, 
	        		(_event.getY() 
	        				- getPage().getJlbl_painting().getLocation().y)
	                        * State.getImageSize().height
	                        / State.getImageShowSize().height),
	                        getPage(),
	                        controlPic);
	    } else {
	
	        project.getCurrentPicture().changePaintObject(
	                new DPoint(
	                        getPage().getJlbl_painting().getWidth()
	                        - (_event.getX() 
	                        + getPage()
	                        .getJlbl_painting().getLocation().x
	                        )
	                        * State.getImageSize().width
	                        / State.getImageShowSize().width, 
	                        getPage().getJlbl_painting().getHeight()
	                        - (_event.getY() 
	                        + getPage().getJlbl_painting()
	                        .getLocation().y
	                        )
	                        * State.getImageSize().height
	                        / State.getImageShowSize().height),
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
        switch (State.getIndexOperation()) {
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
        	if (getTabs() != null) {

                project.getCurrentPicture().finish(view.getTabs().getTab_debug());
        	}
            break;
        case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
            if (_event.getButton() == 1) {

                //remove old rectangle.
                getPage().getJlbl_border().setBounds(-1, -1, 0, 0);
                switch (State.getIndexSelection()) {
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:
                    
                        PaintObjectWriting pow 
                        = project.getCurrentPicture().abortPaintObject(controlPic);
                        mr_sel_curve_complete(_event, pow);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:

                    PaintObjectWriting pow2 
                    = project.getCurrentPicture().abortPaintObject(controlPic);
                    mr_sel_curve_destroy(_event, pow2);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_IMAGE:

                	controlPic.stopBorderThread();
                	view.getPage().getJlbl_selectionBG().setIcon(
                			new ImageIcon(Util.getEmptyBISelection()));
                    break;
                default:
                    break;
                }
                pnt_start = null;
                
                //set index to moving
                State.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_MOVE);
                cTabPaintStatus.deactivate();
                view.getTabs().getTab_paint().getTb_move().setActivated(true);
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:
            if (_event.getButton() == 1) {

                Rectangle r = mr_paint_calcRectangleLocation(_event);
                getPage().getJlbl_border().setBounds(r);
                //remove old rectangle.
                switch (State.getIndexSelection()) {
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:
                    mr_sel_line_complete(r);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:
                    mr_sel_line_destroy(r);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_IMAGE:
            		getPage().getJlbl_border().setBounds(
            				new Rectangle(0, 0, 0, 0));
                    break;
                default:
                    break;
                }
                // reset values
                pnt_start = null;
                
                //set index to moving
                State.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_MOVE);
                cTabPaintStatus.deactivate();
                view.getTabs().getTab_paint().getTb_move().setActivated(true);
                getPage().getJlbl_backgroundStructure().repaint();
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_ERASE:
        	break;
        case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:
            if (_event.getButton() == 1) {
                project.getCurrentPicture().abortPaintObject(controlPic);
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:
        	double d = System.currentTimeMillis();
            if (_event.getButton() == MouseEvent.BUTTON1) {
                mr_zoom(_event);
                cTabPaint.updateResizeLocation();
            } else if (_event.getButton() == MouseEvent.BUTTON3) {
            	cTabPaint.mr_zoomOut();
            }
            State.getLogger().severe("brauchte" 
            		+ (System.currentTimeMillis() - d) + "ms");
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
                if (project.getCurrentPicture().isSelected()) {
                    project.getCurrentPicture().releaseSelected(
                			getControlPaintSelection(),
                			getcTabSelection(),
                			getView().getTabs().getTab_debug(),
                			getView().getPage().getJlbl_painting()
                			.getLocation().x,
                			getView().getPage().getJlbl_painting()
                			.getLocation().y);
                    controlPic.releaseSelected();
                }
            }
            break;
        default:
            State.getLogger().warning("Switch in mouseReleased default");
            break;
        }
        view.getPage().getJpnl_new().setVisible(false);
    }

    
    
    
    
    
    
    

    /**
     * The MouseReleased event for zoom.
     * @param _event the event.
     */
    private void mr_zoom(final MouseEvent _event) {

    	// If the maximal zoom in - size is not reached yet execute the zoom
    	// -in. 
    	// Otherwise the user is informed, that it is impossible to zoom-in.
        if (State.getImageShowSize().width
                / State.getImageSize().width 
                < Math.pow(ViewSettings.ZOOM_MULITPLICATOR, 
                        ViewSettings.MAX_ZOOM_IN)) {
            
        	
        	// Calculate the new size of the page.
            int newWidth = State.getImageShowSize().width
                    * ViewSettings.ZOOM_MULITPLICATOR, 
                    newHeight = State.getImageShowSize().height
                    * ViewSettings.ZOOM_MULITPLICATOR;

            Point oldLocation = new Point(getPage()
                    .getJlbl_painting().getLocation().x, getPage()
                    .getJlbl_painting().getLocation().y);

            State.setImageShowSize(new Dimension(newWidth, newHeight));
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

//            // not smaller than the
//            newX = Math.max(newX, -(Status.getImageShowSize().width 
//                    - getPage().getJlbl_painting()
//                    .getWidth()));
//            newY = Math.max(newY,
//                    -(Status.getImageShowSize().height 
//                    		- getPage().getJlbl_painting()
//                    		.getHeight()));
//            
//            
//            // not greater than 0
//            newX = Math.min(newX, 0);
//            newY = Math.min(newY, 0);

            // not smaller than the negative image size.
            newX = Math.max(newX,
            		(int) -(State.getImageShowSize().width
            				- getPage().getWidth()));
            newY = Math.max(newY,
                    -(State.getImageShowSize().height
                    		- getPage().getHeight()));
            
            // not greater than 0, these calculations prevent the zoom-out 
            // location to be at the upper left of the page.
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
    	final int transaction = project.getCurrentPicture().getLs_po_sortedByY()
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
        project.getCurrentPicture().getLs_po_sortedByY().toFirst(
        		transaction, SecureList.ID_NO_PREDECESSOR);
        project.getCurrentPicture().createSelected();
        
        // create and initialize current values (current PO and its coordinates)
        PaintObject po_current = project.getCurrentPicture().getLs_po_sortedByY()
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
                project.getCurrentPicture().insertIntoSelected(po_current,
                		getView().getTabs().getTab_debug());
                project.getCurrentPicture().getLs_po_sortedByY().remove(
                		transaction);
                //remove item out of PictureOverview and paint and refresh paint
                //otherwise it is not possible to select more than one item
                 new PictureOverview(view.getTabs().getTab_debug()).remove(
                		 po_current);
                project.getCurrentPicture().getLs_po_sortedByY().toFirst(
                		transaction, SecureList.ID_NO_PREDECESSOR);
            } else {
                // next; in if clause the next is realized by remove()
                project.getCurrentPicture().getLs_po_sortedByY().next(
                		transaction, SecureList.ID_NO_PREDECESSOR);
            }
            // update current values
            po_current = project.getCurrentPicture().getLs_po_sortedByY().getItem();
        }


    	//finish transaction 
    	project.getCurrentPicture().getLs_po_sortedByY().finishTransaction(
    			transaction);
    	
        //finish insertion into selected.
        project.getCurrentPicture().finishSelection(getcTabSelection());
        
        //paint the selected item and refresh entire painting.
        if (!project.getCurrentPicture().paintSelected(getPage(),
        		getControlPic(),
        		getControlPaintSelection())) {
        	controlPic.stopBorderThread();
        	view.getPage().getJlbl_selectionBG().setIcon(
        			new ImageIcon(Util.getEmptyBISelection()));
        }

        //set the old pen to replace the curve afterwards.
        if (State.getPen_selectedReplaced() != null) {

            State.setPenSelected1(State.getPen_selectedReplaced());
            State.setPen_selectedReplaced(null);
        }
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
        
        //necessary because the points are painted directly to the buffered
        //image which starts at the _ldp snapshot x.
        Picture.movePaintObjectWriting(_ldp, -_ldp.getSnapshotBounds().x, 
                -_ldp.getSnapshotBounds().y);
        BufferedImage transform = _ldp.getSnapshot();

        //Extract points out of PaintObjectWriting and create a byte array
        //containing all the necessary information for deciding whether
        //a point is inside the selected area or not.
        //Because of this the step above to move the paintObject is necessary.
        byte[][] field = PaintBI.printFillPolygonN(transform, Color.green, model
        		.util.Util.dpntToPntArray(model.util.Util.pntLsToArray(
        				_ldp.getPoints())));
        
        /*
         * whole item selection.
         */
        // initialize selection list
        project.getCurrentPicture().createSelected();

        // go to the beginning of the list
        if (!project.getCurrentPicture().getLs_po_sortedByY().isEmpty()) {

        	//start transaction 
        	final int transaction = project.getCurrentPicture().getLs_po_sortedByY()
        			.startTransaction("Selection curve destroy", 
        					SecureList.ID_NO_PREDECESSOR);
            // go to the beginning of the list
            project.getCurrentPicture().getLs_po_sortedByY().toFirst(
            		transaction, SecureList.ID_NO_PREDECESSOR);
            
            // create and initialize current values
            PaintObject po_current = project.getCurrentPicture().getLs_po_sortedByY()
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
                    new PictureOverview(view.getTabs().getTab_debug()).remove(
                    		project.getCurrentPicture()
                            .getLs_po_sortedByY().getItem());
                    project.getCurrentPicture().getLs_po_sortedByY().remove(
                    		transaction);
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {
                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            project.getCurrentPicture().insertIntoSelected(
                                    separatedPO[1][current], 
                                    getView().getTabs().getTab_debug());
                        } else {
                            State.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    
                    //finish insertion into selected.
                    project.getCurrentPicture().finishSelection(getcTabSelection());
                    
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {
                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
                             new PictureOverview(view.getTabs().getTab_debug())
                             .add(separatedPO[0][current]);
                        } else {
                            State.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    project.getCurrentPicture().getLs_po_sortedByY().toFirst(
                    		transaction, SecureList.ID_NO_PREDECESSOR);
                } else {
                    // next
                    project.getCurrentPicture().getLs_po_sortedByY().next(
                    		transaction, SecureList.ID_NO_PREDECESSOR);
                }
                // update current values
                po_current = project.getCurrentPicture().getLs_po_sortedByY()
                        .getItem();
            }
            //insert the to insert items to graphical user interface.
            ls_toInsert.toFirst();
            while (!ls_toInsert.isBehind() && !ls_toInsert.isEmpty()) {
                project.getCurrentPicture().getLs_po_sortedByY().insertSorted(
                        ls_toInsert.getItem(), 
                        ls_toInsert.getItem().getSnapshotBounds().y,
                        transaction);
                ls_toInsert.next();
            }
        	//finish transaction 
        	project.getCurrentPicture().getLs_po_sortedByY().finishTransaction(
        			transaction);
        	
            if (project.getCurrentPicture().paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {
            	controlPic.refreshPaint();
            } 
        }

        if (!project.getCurrentPicture().paintSelected(getPage(),
    			getControlPic(),
    			getControlPaintSelection())) {
        	controlPic.stopBorderThread();
        	view.getPage().getJlbl_selectionBG().setIcon(
        			new ImageIcon(Util.getEmptyBISelection()));
        }
        //set the old pen to replace the curve afterwards.
        if (State.getPen_selectedReplaced() != null) {

            State.setPenSelected1(State.getPen_selectedReplaced());
            State.setPen_selectedReplaced(null);
        }
        controlPic.refreshPaint();
    }
    
    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _r_size the rectangle.
     */
    private synchronized void mr_sel_line_complete(
            final Rectangle _r_size) {

    	
    	//start transaction 
    	final int transaction = project.getCurrentPicture().getLs_po_sortedByY()
    			.startTransaction("Selection line complete", 
    					SecureList.ID_NO_PREDECESSOR);
        /*
         * SELECT ALL ITEMS INSIDE RECTANGLE    
         */
        //case: there can't be items inside rectangle because list is empty
        if (project.getCurrentPicture().getLs_po_sortedByY().isEmpty()) {


    		getPage().getJlbl_border().setBounds(
    				new Rectangle(0, 0, 0, 0));
        	
    		//used to paint selection even though selection did not
    		//contain anything
            //adjust location of the field for painting to view
//            _r_size.x += getPage().getJlbl_painting().getLocation()
//                    .x;
//            _r_size.y += getPage().getJlbl_painting().getLocation()
//                    .y;
//            //paint to view
//            controlPic.paintEntireSelectionRect(
//                    _r_size);
//            pnt_start = null;
            return;
        }
        
        // find paintObjects and move them from image to Selection and 
        // initialize selection list
        project.getCurrentPicture().getLs_po_sortedByY().toFirst(
        		transaction, SecureList.ID_NO_PREDECESSOR);
        project.getCurrentPicture().createSelected();
        
        // create and initialize current values (current PO and its coordinates)
        PaintObject po_current = project.getCurrentPicture().getLs_po_sortedByY()
                .getItem();
        int currentY = po_current.getSnapshotBounds().y;

        //adapt the rectangle to the currently used zoom factor.
        final double cZoomFactorWidth = 1.0 * State.getImageSize().width
                / State.getImageShowSize().width;
        final double cZoomFactorHeight = 1.0 * State.getImageSize().height
                / State.getImageShowSize().height;
        _r_size.x *= cZoomFactorWidth;
        _r_size.width *= cZoomFactorWidth;
        _r_size.y *= cZoomFactorHeight;
        _r_size.height *= cZoomFactorHeight;
        
        // go through list. until either list is empty or it is
        // impossible for the paintSelection to paint inside the
        // selected area
        while (po_current != null
                && currentY 
                <= (_r_size.y + _r_size.height)) {


            //The y condition has to be in here because the items are just 
            //sorted by x coordinate; thus it is possible that one PaintObject 
            //is not suitable for the specified rectangle but some of its 
            //predecessors in sorted list do.
            if (po_current.isInSelectionImage(_r_size)
            		&& po_current.isEditable()) {


                //remove item out of PictureOverview and paint and refresh paint
                //otherwise it is not possible to select more than one item
                 new PictureOverview(view.getTabs().getTab_debug()).remove(
                		 po_current);
                
                //move current item from normal list into selected list 
                project.getCurrentPicture().insertIntoSelected(
                		po_current, getView().getTabs().getTab_debug());
                             
                project.getCurrentPicture().getLs_po_sortedByY().remove(
                		transaction);
            }            

            project.getCurrentPicture().getLs_po_sortedByY().next(
            		transaction, SecureList.ID_NO_PREDECESSOR);

            // update current values
            currentY = po_current.getSnapshotBounds().y;
            po_current = project.getCurrentPicture().getLs_po_sortedByY().getItem();

        }

    	//finish transaction 
    	project.getCurrentPicture().getLs_po_sortedByY().finishTransaction(
    			transaction);

        //finish insertion into selected.
        project.getCurrentPicture().finishSelection(getcTabSelection());
        
        controlPic.refreshPaint();

        if (!project.getCurrentPicture().paintSelected(getPage(),
    			getControlPic(),
    			getControlPaintSelection())) {

    		getPage().getJlbl_border().setBounds(
    				new Rectangle(0, 0, 0, 0));

//          //transform the logical Rectangle to the painted one.
//          _r_size.x = (int) (1.0 * _r_size.x / cZoomFactorWidth);
//          _r_size.width = (int) 
//                  (1.0 * _r_size.width / cZoomFactorWidth);
//          _r_size.y = (int) (1.0 * _r_size.y / cZoomFactorHeight);
//          _r_size.height = (int) 
//                  (1.0 * _r_size.height / cZoomFactorHeight);
//          
//          _r_size.x 
//          += getPage().getJlbl_painting().getLocation().x;
//          _r_size.y 
//          += getPage().getJlbl_painting().getLocation().y;
//          
//          
//          controlPaintSelection.setR_selection(_r_size,
//                  getPage().getJlbl_painting().getLocation());
//          controlPic.paintEntireSelectionRect(
//                  _r_size);
        }
        getPage().getJlbl_backgroundStructure().repaint();
        

    }
    
    

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _r_sizeField the rectangle
     */
    public final void mr_sel_line_destroy(final Rectangle _r_sizeField) {

    	//start transaction 
    	final int transaction = project.getCurrentPicture().getLs_po_sortedByY()
    			.startTransaction("Selection line destroy", 
    					SecureList.ID_NO_PREDECESSOR);
        /*
         * whole item selection.
         */
        // initialize selection list
        project.getCurrentPicture().createSelected();

        // go to the beginning of the list
        project.getCurrentPicture().getLs_po_sortedByY().toFirst(transaction, 
        		SecureList.ID_NO_PREDECESSOR);
        if (!project.getCurrentPicture().getLs_po_sortedByY().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = project.getCurrentPicture().getLs_po_sortedByY()
                    .getItem();
            int mycurrentY = po_current.getSnapshotBounds().y;

            
            /**
             * Because it is impossible to insert the new created items directly
             * to list (otherwise there would be an infinite loop because of 
             * sort order they reappear inside the while
             * loop and are destroyed once again and thus reappear etc.
             */
            List<PaintObject> ls_toInsert = new List<PaintObject>();

            //adapt the rectangle to the currently used zoom factor.
            final double cZoomFactorWidth = 1.0 * State.getImageSize().width
                    / State.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * State.getImageSize().height
                    / State.getImageShowSize().height;
            _r_sizeField.x *= cZoomFactorWidth;
            _r_sizeField.width *= cZoomFactorWidth;
            _r_sizeField.y *= cZoomFactorHeight;
            _r_sizeField.height *= cZoomFactorHeight;
            
            

            
            // go through list. until either list is empty or it is
            // impossible for the paintSelection to paint inside the
            // selected area
            while (po_current != null
                    && mycurrentY 
                    <= (_r_sizeField.y + _r_sizeField.height)) {

                //The y condition has to be in here because the items are just 
                //sorted by x coordinate; thus it is possible that one 
                //PaintObject is not suitable for the specified rectangle but 
                //some of its predecessors in sorted list do.
                if (po_current.isInSelectionImage(_r_sizeField)
                		&& po_current.isEditable()) {

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
                     new PictureOverview(view.getTabs().getTab_debug()).remove(
                    		 project.getCurrentPicture()
                            .getLs_po_sortedByY().getItem());
                    project.getCurrentPicture().getLs_po_sortedByY().remove(
                    		transaction);
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {

                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            project.getCurrentPicture().insertIntoSelected(
                                    separatedPO[1][current],
                                    getView().getTabs().getTab_debug()
                            		);
                        } else {
                            
                            State.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                        
                    }

                    //finish insertion into selected.
                    project.getCurrentPicture().finishSelection(getcTabSelection());
                    
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview(
                            		 view.getTabs().getTab_debug()).add(
                            				 separatedPO[0][current]);
                        } else {

                            State.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                } 
                // next
                project.getCurrentPicture().getLs_po_sortedByY().next(transaction,
                		SecureList.ID_NO_PREDECESSOR);


                // update current values
                mycurrentY = po_current.getSnapshotBounds().y;
                po_current = project.getCurrentPicture().getLs_po_sortedByY()
                        .getItem();
            }

            
            //insert the to insert items to graphical user interface.
            ls_toInsert.toFirst();
            while (!ls_toInsert.isBehind() && !ls_toInsert.isEmpty()) {

                project.getCurrentPicture().getLs_po_sortedByY().insertSorted(
                        ls_toInsert.getItem(), 
                        ls_toInsert.getItem().getSnapshotBounds().y,
                        transaction);
                ls_toInsert.next();
            }

        	//finish transaction
        	project.getCurrentPicture().getLs_po_sortedByY().finishTransaction(
        			transaction);
            if (project.getCurrentPicture().paintSelected(getPage(),
        			getControlPic(),
        			getControlPaintSelection())) {

            	controlPic.refreshPaint();
            } else {

            	//nothing painted
        		getPage().getJlbl_border().setBounds(
        				new Rectangle(0, 0, 0, 0));
            }


            _r_sizeField.x /= cZoomFactorWidth;
            _r_sizeField.width /= cZoomFactorWidth;
            _r_sizeField.y /= cZoomFactorHeight;
            _r_sizeField.height /= cZoomFactorHeight;

            _r_sizeField.x += getPage().getJlbl_painting()
            		.getLocation().getX();
            _r_sizeField.y += getPage().getJlbl_painting()
            		.getLocation().getY();
            
        } else {

        	//nothing painted
    		getPage().getJlbl_border().setBounds(
    				new Rectangle(0, 0, 0, 0));
        }
        

        controlPic.refreshRectangle(
                _r_sizeField.x, _r_sizeField.y, 
                _r_sizeField.width, _r_sizeField.height);


        getPage().getJlbl_backgroundStructure().repaint();

    }
    
    
    
    /**
     * Erase functionality at mouseReleased.
     * @param _p the Point.
     */
    public final synchronized void mr_erase(final Point _p) {

    	
    	
    	//start transaction 
    	final int transaction = project.getCurrentPicture().getLs_po_sortedByY()
    			.startTransaction("erase", 
    					SecureList.ID_NO_PREDECESSOR);
    	/**
    	 * Value for showing the new paintObjects in PaintObjectsView.
    	 */
    	final boolean debug_update_paintObjects_view = false;
    	
    	final Rectangle r_sizeField = new Rectangle(
    			_p.x - State.getEraseRadius(), 
    			_p.y - State.getEraseRadius(), 
    			State.getEraseRadius() * 2, 
    			State.getEraseRadius() * 2);
        /*
         * whole item selection.
         */
        // initialize selection list
        project.getCurrentPicture().createSelected();

        // go to the beginning of the list
        project.getCurrentPicture().getLs_po_sortedByY().toFirst(transaction,
        		SecureList.ID_NO_PREDECESSOR);
        if (!project.getCurrentPicture().getLs_po_sortedByY().isEmpty()) {

            // create and initialize current values
            PaintObject po_current = project.getCurrentPicture().getLs_po_sortedByY()
                    .getItem();
            int mycurrentY = po_current.getSnapshotBounds().y;

            

            //adapt the rectangle to the currently used zoom factor.
            final double cZoomFactorWidth = 1.0 * State.getImageSize().width
                    / State.getImageShowSize().width;
            final double cZoomFactorHeight = 1.0 * State.getImageSize().height
                    / State.getImageShowSize().height;
            r_sizeField.x *= cZoomFactorWidth;
            r_sizeField.width *= cZoomFactorWidth;
            r_sizeField.y *= cZoomFactorHeight;
            r_sizeField.height *= cZoomFactorHeight;
            List<PaintObjectWriting> ls_separatedPO = null;
            
            
            switch (State.getEraseIndex()) {
            case Constants.ERASE_ALL:

                
                // go through list. until either list is empty or it is
                // impossible for the paintSelection to paint inside the
                // selected area
                while (po_current != null
                        && mycurrentY 
                        <= (r_sizeField.y + r_sizeField.height)) {


                    //The y condition has to be in here 
                	//because the items are just 
                    //sorted by x coordinate; thus it is 
                	//possible that one PaintObject 
                    //is not suitable for the specified 
                	//rectangle but some of its 
                    //predecessors in sorted list do.
                    if (po_current.isInSelectionImage(r_sizeField)
                    		&& po_current.isEditable()) {


                        //remove item out of PictureOverview and 
                    	//paint and refresh paint
                        //otherwise it is not possible to select 
                    	//more than one item
                         new PictureOverview(view.getTabs()
                        		 .getTab_debug()).remove(po_current);
                        
                        project.getCurrentPicture().getLs_po_sortedByY().remove(
                        		transaction);
                    }            

                    project.getCurrentPicture().getLs_po_sortedByY().next(
                    		transaction, SecureList.ID_NO_PREDECESSOR);

                    // update current values
                    mycurrentY = po_current.getSnapshotBounds().y;
                    po_current = project.getCurrentPicture()
                    		.getLs_po_sortedByY().getItem();

                }

            	//finish transaction 
            	project.getCurrentPicture().getLs_po_sortedByY().finishTransaction(
            			transaction);

                //finish insertion into selected.
                project.getCurrentPicture().finishSelection(getcTabSelection());
                
                controlPic.refreshPaint();
            	
            	
            	
            	
            	
            	
            	
            	project.getCurrentPicture().deleteSelected(getView()
            			.getTabs().getTab_debug(), cTabSelection);
            	break;
            case Constants.ERASE_DESTROY:

            
	            // go through list. until either list is empty or it is
	            // impossible for the paintSelection to paint inside the
	            // selected area
	            while (po_current != null
	                    && mycurrentY 
	                    <= (r_sizeField.y + r_sizeField.height)) {
	
	                //The y condition has to be in here because the items 
	            	//are just 
	                //sorted by x coordinate; thus it is possible that one 
	                //PaintObject is not suitable for the specified 
	            	//rectangle but 
	                //some of its predecessors in sorted list do.
	                if (po_current.isInSelectionImage(r_sizeField)
	                		&& po_current.isEditable()) {
	
	                    // get item; remove it out of lists and add it to
	                    // selection list
	
	                	ls_separatedPO 
	                	= po_current.deleteRectangle(
	                			r_sizeField, ls_separatedPO);
	                	if (ls_separatedPO != null) {
	
	                    	if (debug_update_paintObjects_view) {
	
	                            new PictureOverview(view.getTabs()
	                            		.getTab_debug()).remove(
	                            				project.getCurrentPicture()
	                                    .getLs_po_sortedByY().getItem());
	                    	}
	                        project.getCurrentPicture().getLs_po_sortedByY().remove(
	                        		transaction);
	                	}
	                } 
	                // next
	                project.getCurrentPicture().getLs_po_sortedByY().next(transaction,
	                		SecureList.ID_NO_PREDECESSOR);
	
	
	                // update current values
	                mycurrentY = po_current.getSnapshotBounds().y;
	                po_current = project.getCurrentPicture().getLs_po_sortedByY()
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
	                    	ls_separatedPO.getItem()
	                    	.recalculateSnapshotBounds();
	
	                        project.getCurrentPicture().getLs_po_sortedByY()
	                        .insertSorted(
	                        		ls_separatedPO.getItem(), 
	                        		ls_separatedPO.getItem()
	                        		.getSnapshotBounds().y,
	                        		transaction);
	
	                    	if (debug_update_paintObjects_view) {
	
	                            new PictureOverview(
	                            		view.getTabs().getTab_debug()).add(
	                           		 ls_separatedPO.getItem());
	                    	}
	                    } else {
	
	                        State.getLogger().warning("separated paintObject "
	                                + "is null");
	                    }
	                    ls_separatedPO.next();
	                }
	            }
	        	//finish transaction
	        	project.getCurrentPicture().getLs_po_sortedByY().finishTransaction(
	        			transaction);
	        
	            
	            if (project.getCurrentPicture().paintSelected(getPage(),
	        			getControlPic(),
	        			getControlPaintSelection())) {
	            	controlPic.refreshPaint();
	            }
	
	            r_sizeField.x /= cZoomFactorWidth;
	            r_sizeField.width /= cZoomFactorWidth;
	            r_sizeField.y /= cZoomFactorHeight;
	            r_sizeField.height /= cZoomFactorHeight;
	
	        	break;
			default:
				break;
	        }
        } else {

        	//finish transaction
        	project.getCurrentPicture().getLs_po_sortedByY().finishTransaction(
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
     * The action which is performed if clicked while the current id is pipette.
     * 
     * @param _event
     *            the event.
     */
    private void mr_paint_pipette(final MouseEvent _event) {


        int color = project.getCurrentPicture().getColorPX(_event.getX(), 
                _event.getY(), getControlPic().getBi());
        if (_event.getButton() == 1) {
        	view.getTabs().getTab_paint().getTb_color1().setBackground(
        			new Color(color));
            Pen pen = State.getPenSelected1();
            pen.setClr_foreground(new Color(color));
            State.setPenSelected1(pen);
        } else {
        	view.getTabs().getTab_paint().getTb_color2().setBackground(
        			new Color(color));
            Pen pen = State.getPenSelected2();
            pen.setClr_foreground(new Color(color));
            State.setPenSelected2(pen);
        }
    }
    
    
    
    
    
    
    


    
    
    /**
     * Initializes the movement thread.
     * @param _mmSP the movement speed
     */
    public final void mr_paint_initializeMovementThread(
    		final Point _mmSP) {
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

                    if (x < -model.settings.State.getImageShowSize().width 
                            + getPage().getJlbl_painting()
                            .getWidth()) {
                        x = -model.settings.State.getImageShowSize().width
                                + getPage()
                                .getJlbl_painting().getWidth();
                    }
                    if (x > 0) {
                        x = 0;
                    }
                    
                    if (y < -model.settings.State.getImageShowSize().height
                            + getPage().getJlbl_painting()
                            .getHeight()) {
                        y = -model.settings.State.getImageShowSize().height
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

    	//pnt_start is equal to null if the selection is performed through 
    	//one click.
    	if (pnt_start != null) {
    		int xLocation = Math.min(pnt_start.x, _event.getX());
            int yLocation = Math.min(pnt_start.y, _event.getY());
            // not smaller than zero
            xLocation = Math.max(0, xLocation);
            yLocation = Math.max(0, yLocation);
            
            // not greater than the entire shown image - 
            // the width of zoom
            int xSize = Math.min(State.getImageShowSize().width
                    - xLocation, 
                    Math.abs(pnt_start.x - _event.getX()));
            int ySize = Math.min(State.getImageShowSize().height
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
        
    	return new Rectangle(_event.getX(), _event.getY(), 0, 0);

    }

	
	
	

	/**
	 * save the picture.
	 * 
	 * @param _wsLoc
	 *            the path of the location.
	 */
	public void loadProject(final String _wsLoc) {
		try {
			FileInputStream fos = new FileInputStream(new File(_wsLoc));
			ObjectInputStream oos = new ObjectInputStream(fos);
			
			Project p = (Project) oos.readObject();
			project = p;
			project.restoreFormSerializable();
			// reset transactions etc., resort.
			for (int i = 0; i < project.getAmountPages(); i++) {
				project.getPicture(i).loadPicture();
			}

			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


		for (int i = 0; i < project.getAmountPages(); i++) {
			project.getPicture(i).unpack();
		}
		
	}



	/**
	 * @return the controlPaintSelection
	 */
	public final ControlSelectionTransform getControlPaintSelection() {
		return controlPaintSelect;
	}




	/**
	 * @return the cTabPaint
	 */
	public final CTabTools getcTabPaint() {
		return cTabPaint;
	}






	/**
	 * @return the cTabPrint
	 */
	public final CTabPrint getcTabPrint() {
		return cTabPrint;
	}





	/**
	 * @return the cTabWrite
	 */
	public final CTabWrite getcTabWrite() {
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
    	if (project.getCurrentPicture().isSelected()) {
	    	project.getCurrentPicture().releaseSelected(
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
    	if (project.getCurrentPicture().isSelected()) {
	    	project.getCurrentPicture().releaseSelected(
        			getControlPaintSelection(),
        			getcTabSelection(),
        			getView().getTabs().getTab_debug(),
        			getView().getPage().getJlbl_painting().getLocation().x,
        			getView().getPage().getJlbl_painting().getLocation().y);
	    	controlPic.releaseSelected();
    	}		
	}

	
	/**
	 * Remove the previous preprint.
	 */
	private void removePreprint() {


		//if the bounds of the preprint is not equal to null
		//the preprint exists
        if (rect_preprintBounds != null) {
        	
        	//fetch the color which is written into the position
        	//where the preprint has been.
        	final int maxRGB = 255;
        	final int rgbWhiteAlpha = new Color(
        			10, maxRGB, maxRGB, 0).getRGB();
//        			255, 0, 0).getRGB();
        	
        	//write the preprint to the rgb.
        	for (int w = 0; w < rect_preprintBounds.width;
        			w++) {
        		for (int h = 0; h < rect_preprintBounds.height; 
            			h++) {

        			int shiftedX = rect_preprintBounds.x + w;
        			int shiftedY = rect_preprintBounds.y + h;

        			
        			//if the values are legal:
        			if (
        					shiftedX >= 0
        					&& shiftedX < bi_preprint.getWidth()
        					&& shiftedY >= 0
        					&& shiftedY < bi_preprint.getHeight()) {

                    	bi_preprint.setRGB(
                    			shiftedX, 
                    			shiftedY,
                    			rgbWhiteAlpha);
        			} else {
        				
        				//problem specification is printed in each case.
        				final String problemSpecification = "\n"
        						+ "bounds: " + rect_preprintBounds
        						+ "imagesize" + bi_preprint.getWidth() 
        						+ ".." + bi_preprint.getHeight();

            			if (shiftedX < 0) {
            				State.getLogger().info(
            						"preprint location wrong: x < 0"
            						+ problemSpecification);
            			}

            			if (shiftedX >= bi_preprint.getWidth()) {
            				State.getLogger().info(
            						"preprint location wrong: x >= "
            						+ "bi_preprint.getWidth()"
            						+ problemSpecification);
            			}

            			if (shiftedY < 0) {
            				State.getLogger().info(
            						"preprint location wrong: y < 0"
            						+ problemSpecification);
            			}

            			if (shiftedY >= bi_preprint.getHeight()) {
            				State.getLogger().info(
            						"preprint location wrong: y >= "
            						+ "bi_preprint.getHeight()"
            						+ problemSpecification);
            			}
        			}
				}
			}
        	rect_preprintBounds = null;
        } 
	}
	
	
	/**
	 * Perform a preprint of a point.
	 * @param _x
	 * @param _y
	 */
	private void performPreprint(final int _x, final int _y) {

		//call remove preprint which removes a preprint if it exists
		removePreprint();

		if (project.getCurrentPicture().getPen_current() == null) {
			return;
		}

        /*
         * Perform preprinting and update values.
         */
        
        // perform the preprinting
        project.getCurrentPicture().getPen_current().preprint(
                _x, _y,
                bi_preprint,
                getPage().getJlbl_selectionBG());
        
        
        //zoom factor
        double zoomFactorX = State.getImageShowSize().width
                / State.getImageSize().width, 
                zoomFactorY = State.getImageShowSize().height
                        / State.getImageSize().height; 
        
        //fetch the preprintSize and divide it by two.
        int preprintX =
        		Math.max(
        				(int) Math.ceil(
        						project.getCurrentPicture().getPen_current()
        						.getThickness()  * zoomFactorX / 2),
        						1);
        int preprintY =
        		Math.max(
        				(int) Math.ceil(
        						project.getCurrentPicture().getPen_current()
        						.getThickness()  * zoomFactorY / 2),
        						1);
        
        int recX = _x - preprintX * 2;
        int recY = _y - preprintY * 2;
        int recWidth = preprintX * (2 + 2);
        int recHeight = preprintY * (2 + 2);
        rect_preprintBounds = new Rectangle(0, 0, 0, 0);
        
        // if the rectangle is outside the legal scope set its bounds
        // to null.
        if (
        		recX < 0
				&& recX + recWidth < 0) {
        	rect_preprintBounds = null;
        	
        } else if (recX < 0) {
        	//here, recWidth \geq \|recX\|; thus the new recWidth is greater
        	//or equal to zero.
        	recWidth = recX + recWidth;
        	recX = 0;
        }
        
        if (recX >= bi_preprint.getWidth()) {
        	rect_preprintBounds = null;
        } else if (recX + recWidth >= bi_preprint.getWidth()) {
        	recWidth = bi_preprint.getWidth() - recX;
        	if (recWidth <= 0) {
        		rect_preprintBounds = null;
        	}
        }
        if (
        		recY < 0
				&& recY + recHeight < 0) {
        	rect_preprintBounds = null;
        	
        } else if (recY < 0) {
        	//here, recWidth \geq \|recX\|; thus the new recWidth is greater
        	//or equal to zero.
        	recHeight = recY + recHeight;
        	recY = 0;
        }
        
        if (recY >= bi_preprint.getHeight()) {
        	rect_preprintBounds = null;
        } else if (recY + recHeight >= bi_preprint.getHeight()) {
        	recHeight = bi_preprint.getHeight() - recY;
        	if (recHeight <= 0) {
        		rect_preprintBounds = null;
        	}
        }

        //this is only equal to null if the rectangle is outside the legal 
        //scope.
        if (rect_preprintBounds != null) {
            rect_preprintBounds = new Rectangle(
        			recX, recY, recWidth, recHeight);
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
    			State.getLogger().severe("view.getPage() is null");
    		}
    	} else {
			State.getLogger().severe("view is null");
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
    			State.getLogger().severe("view.getTabs() is null");
    		}
    	} else {
			State.getLogger().severe("view is null");
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
	public final CTabLook getcTabLook() {
		return cTabLook;
	}







	/**
	 * @return the cTabExport
	 */
	public final CTabExport getcTabExport() {
		return cTabExport;
	}






	/**
	 * @return the cTabPaintObjects
	 */
	public final CTabDebug getcTabPaintObjects() {
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
	public final CTabInsert getUtilityControlItem2() {
		return utilityControlItem2;
	}

	/**
	 * @return the picture
	 */
	public final Picture getPicture() {
		return project.getCurrentPicture();
	}

	/**
	 * @return the cTabAbout
	 */
	public final CTabAboutPaint getcTabAbout() {
		return cTabAbout;
	}

	/**
	 * @return the view
	 */
	public final  View getView() {
		return view;
	}


	/**
	 * @return the project
	 */
	public final Project getProject() {
		return project;
	}


	/**
	 * @return the bi_preprint
	 */
	public final BufferedImage getBi_preprint() {
		return bi_preprint;
	}


	/**
	 * @param _bi_preprint the bi_preprint to set
	 */
	public final void setBi_preprint(final BufferedImage _bi_preprint) {
		this.bi_preprint = _bi_preprint;
	}


	/**
	 * @return the pnt_startLocation
	 */
	public Point getPnt_startLocation() {
		return pnt_startLocation;
	}


	/**
	 * @return the info_selection
	 */
	public InfoSelection getInfo_selection() {
		return info_selection;
	}


	/**
	 * @return the cTabProject
	 */
	public CTabProject getcTabProject() {
		return cTabProject;
	}


}
