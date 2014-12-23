//package declaration
package control;

//import declarations
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import control.tabs.CPaintStatus;
import control.tabs.CPaintVisualEffects;
import model.objects.PictureOverview;
import model.objects.Zoom;
import model.objects.painting.PaintBI;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectImage;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.objects.pen.special.PenSelection;
import model.settings.Constants;
import model.settings.ReadSettings;
import model.settings.Settings;
import model.settings.Status;
import model.settings.ViewSettings;
import model.util.DPoint;
import model.util.Util;
import model.util.list.List;
import model.util.paint.MyClipboard;
import model.util.paint.Utils;
import view.View;
import view.forms.Message;
import view.forms.New;
import view.forms.Page;
import view.forms.Tabs;
import view.tabs.Paint;

/**
 * Controller class dealing with main paint stuff.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class ControlPainting implements MouseListener,
        MouseMotionListener, ActionListener {

    /**
     * the only instance of this.
     */
    private static ControlPainting instance = null;

    /**
     * Boolean which indicates, whether the necessary instances of View and
     * Picture have been set by now.
     */
    private boolean startPerform;

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
     * The thread for moving at the page.
     */
    private Thread thrd_move;
    
    /**
     * empty utility class Constructor.
     */
    private ControlPainting() { }

    /**
     * pseudo-constructor method. Used because of getInstance.
     */
    private void start() {

        // get location of current workspace
        Settings.setWsLocation(ReadSettings.install());

        // initialize startPerform
        this.startPerform = false;
        
        //set logger level to finest; thus every log message is shown.
        Status.getLogger().setLevel(Level.WARNING);

        // if installed
        if (!Settings.getWsLocation().equals("")) {
            
            Status.getLogger().info("installation found.\n");

            /*
             * initialize model
             */

            Status.getLogger().info(
                    "initialize model class Pen and current pen.\n");
            
            Picture.getInstance().initializePen(
                    new BallPen(Constants.PEN_ID_POINT, 1, Color.black));
            Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_PAINT_1);
            Paint.getInstance().getTb_color1().setActivated(true);
            Paint.getInstance().getIt_stift1().getTb_open().setActivated(true);
            


            /*
             * initialize view.
             */
            Status.getLogger().info(
                    "initialize view class and make visible.\n");
            
            View.getInstance().setVisible(true);


            Status.getLogger().info(
                    "Start handling actions and initialize listeners.\n");
            
            // tell all the controller classes to start to perform
            this.startPerform = true;
            CPaintStatus.getInstance().initialize();
            CPaintVisualEffects.getInstance().enable(true);

            Status.getLogger().info("initialization process completed.\n\n"
                    + "-------------------------------------------------\n");
        } else {

            // if not installed and no installation done
            System.exit(1);
        }
    }

    /**
     * Enable or disable listeners.
     * 
     * @param _what
     *            whether enabled or disabled.
     */
    public void enable(final boolean _what) {
        startPerform = _what;
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        if (_event.getSource().equals(
                Paint.getInstance().getTb_turnMirror().getActionCause())) {


        	String commandNormal = "xrandr -o normal";
        	String result = Util.executeCommandLinux(commandNormal);
        	if (result.startsWith(Util.EXECUTION_SUCCESS)){
        		
        		Status.getLogger().info("Rotation normal success");
        	} else if (result.startsWith(Util.EXECUTION_FAILED)) {
        		
        		if (Status.isNormalRotation()) {

            		
            		Status.getLogger().warning("beta rotation");
                    View.getInstance().turn();
                    Status.setNormalRotation(false);
            	}
            }
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_turnNormal().getActionCause())) {


        	String commandInverted = "xrandr -o inverted";
        	String result = Util.executeCommandLinux(commandInverted);
        	if (result.startsWith(Util.EXECUTION_SUCCESS)){
        		
        		Status.getLogger().info("Rotation normal success");
        	} else if (result.startsWith(Util.EXECUTION_FAILED)) {
        		
        		if (Status.isNormalRotation()) {

            		
            		Status.getLogger().warning("beta rotation");
                	
                    View.getInstance().turn();
                    Status.setNormalRotation(true);
            	}
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseClicked(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {

            if (Status.isUncommittedChanges()) {

                int i = JOptionPane.showConfirmDialog(View.getInstance(),
                        "Changes Uncommitted. Do you want to save them "
                                + "before exiting? ", "Save changes",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (i == 0) {
                    // okay
                    mr_save();
                } else if (i == 1) {

                    // no
                    System.exit(1);
                } 
                //i == 2 ^ interrupt
            } else {
                System.exit(1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is
        // performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {
            View.getInstance().getJbtn_exit().setIcon(
                    new ImageIcon(Utils.resizeImage(View.getInstance()
                            .getJbtn_exit().getWidth(), View
                            .getInstance().getJbtn_exit().getHeight(),
                            Constants.VIEW_JBTN_EXIT_PRESSED_PATH)));
        } else if (_event.getSource().equals(
                View.getInstance().getJbtn_fullscreen())) {
            View.getInstance().getJbtn_fullscreen().setIcon(new ImageIcon(Utils
                    .resizeImage(View.getInstance().getJbtn_exit().getWidth(), 
                            View.getInstance().getJbtn_exit().getHeight(),
                                    Constants
                                    .VIEW_JBTN_FULLSCREEN_PRESSED_PATH)));
        } else if (_event.getSource().equals(
                Page.getInstance().getJlbl_painting())) {

            Tabs.getInstance().closeMenues();
            
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
                Picture.getInstance().addPaintObject();
                changePO(_event);
                break;
            
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

                // abort old paint object
                Picture.getInstance().abortPaintObject();

                // change pen and add new paint object
                Picture.getInstance().changePen(new PenSelection());
                Picture.getInstance().addPaintObjectWrinting();
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                Picture.getInstance().abortPaintObject();
                Picture.getInstance().changePen(new PenSelection());
                Picture.getInstance().addPaintObjectWrinting();
                break;
            case Constants.CONTROL_PAINTING_INDEX_PIPETTE:

                break;
            case Constants.CONTROL_PAINTING_INDEX_MOVE:

                
                pnt_start = _event.getPoint();
                pnt_startLocation = Page.getInstance().getJlbl_painting()
                        .getLocation();
                Page.getInstance().getJlbl_painting().refreshPaint();
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

            Picture.getInstance().changePaintObject(
                    new DPoint((_event.getX() 
                            - Page.getInstance()
                            .getJlbl_painting().getLocation().x
                            )
                            * Status.getImageSize().width
                            / Status.getImageShowSize().width, (_event
                            .getY() 
                            - Page.getInstance().getJlbl_painting()
                            .getLocation().y
                            )
                            * Status.getImageSize().height
                            / Status.getImageShowSize().height));
        } else {

            Picture.getInstance().changePaintObject(
                    new DPoint(
                            Page.getInstance().getJlbl_painting().getWidth()
                            - (_event.getX() 
                            + Page.getInstance()
                            .getJlbl_painting().getLocation().x
                            )
                            * Status.getImageSize().width
                            / Status.getImageShowSize().width, 
                            Page.getInstance().getJlbl_painting().getHeight()
                            - (_event.getY() 
                            + Page.getInstance().getJlbl_painting()
                            .getLocation().y
                            )
                            * Status.getImageSize().height
                            / Status.getImageShowSize().height));
        }
    }
    /**
     * {@inheritDoc}
     */
    public void mouseDragged(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // left mouse pressed
        final int leftMouse = 1024;
        if ((_event.getSource().equals(Page.getInstance()
                .getJlbl_painting()))) {

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
                    Picture.getInstance().changePaintObject(
                            new DPoint(_event.getX(), _event.getY()));
                    break;
                }

            case Constants.CONTROL_PAINTING_INDEX_ERASE:
//                if (_event.getModifiersEx() == leftMouse) {
//
//
//                }
            	mr_erase(_event.getPoint());
                break;
                
            case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:

                if (_event.getModifiersEx() == leftMouse) {

                    if (pnt_start == null) {
                        pnt_start = _event.getPoint();
                        pnt_startLocation = Page.getInstance()
                                .getJlbl_painting().getLocation();
                        Picture.getInstance().abortPaintObject();
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

                        Page.getInstance().getJlbl_border().setBounds(xLocation,
                                yLocation, xSize, ySize);
                    } else {
                        
                        Status.getLogger().warning("Want to print border but"
                                + "the starting point is null.");
                    }
                    break;
                }

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                if (_event.getModifiersEx() == leftMouse) {

                    Picture.getInstance().abortPaintObject();
                    Picture.getInstance().changePen(new PenSelection());
                    Picture.getInstance().addPaintObjectWrinting();
                    break;
                }
            case Constants.CONTROL_PAINTING_INDEX_MOVE:
                if (_event.getModifiersEx() == leftMouse) {
                    if (pnt_start == null) {
                        pnt_start = _event.getPoint();
                        pnt_startLocation = Page.getInstance()
                                .getJlbl_painting().getLocation();
                        Picture.getInstance().abortPaintObject();
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
                    
                    if (x < -Status.getImageShowSize().width + Page
                            .getInstance().getJlbl_painting().getWidth()) {
                        x = -Status.getImageShowSize().width + Page
                                .getInstance().getJlbl_painting().getWidth();
                    }
                    if (x > 0) {
                        x = 0;
                    }
                    if (y < -Status.getImageShowSize().height + Page
                            .getInstance().getJlbl_painting().getHeight()) {
                        y = -Status.getImageShowSize().height + Page
                                .getInstance().getJlbl_painting().getHeight();
                    }
                    if (y >= 0) {
                        y = 0;
                    } 
                    Page.getInstance().getJlbl_painting().setLocation(x, y);
                    Page.getInstance().refrehsSps();
                    pnt_last = _event.getPoint();
                    break;
                }

            default:
                break;
            }
        }
        New.getInstance().repaint();
    }

    /*
     * mouse over
     */

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {
            mr_exit();
        } else if (_event.getSource().equals(
                View.getInstance().getJbtn_fullscreen())) {
            mr_fullscreen();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_new().getActionCause())) {
            mr_new();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_save().getActionCause())) {
            mr_save();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_load().getActionCause())) {
            mr_load();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_zoomOut().getActionCause())) {
            mr_zoomOut();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_copy().getActionCause())) {
            mr_copy();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_paste().getActionCause())) {
            mr_paste();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_cut().getActionCause())) {
            mr_cut();
        } else if (_event.getSource().equals(
                        Page.getInstance().getJlbl_painting())) {
            mr_painting(_event);
        }
    }
    


    
    /**
     * make fullscreen or normal size.
     */
    private void mr_fullscreen() {
//        View.getInstance().setFullscreen();
        ViewSettings.setFULLSCREEN(!ViewSettings.isFullscreen());
        
        if (ViewSettings.isFullscreen()) {

            ViewSettings.setSize_jframe(
                new Dimension(
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()), 
                (int) (Toolkit.getDefaultToolkit().getScreenSize()
                        .getHeight())));
        } else {

            ViewSettings.setSize_jframe(
                   new Dimension(
                   (int) (Toolkit.getDefaultToolkit().getScreenSize()
                           .getWidth() 
                           * 2 / (2 + 1)), 
                   (int) (Toolkit.getDefaultToolkit().getScreenSize()
                           .getHeight() 
                           * 2 / (2 + 1))));
        }
        
        View.getInstance().setVisible(true);
        View.getInstance().flip();
        View.getInstance().repaint();
        Tabs.getInstance().repaint();
        Tabs.getInstance().openTab(0);
        
    }

    /**
     * {@inheritDoc}
     */
    public void mouseExited(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {
            View.getInstance().getJbtn_exit().setIcon(new ImageIcon(Utils
                    .resizeImage(View.getInstance().getJbtn_exit().getWidth(), 
                            View.getInstance().getJbtn_exit().getHeight(),
                                    Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));
        } else if (_event.getSource().equals(
                View.getInstance().getJbtn_fullscreen())) {
            View.getInstance().getJbtn_fullscreen().setIcon(new ImageIcon(Utils
                    .resizeImage(View.getInstance().getJbtn_exit().getWidth(), 
                            View.getInstance().getJbtn_exit().getHeight(),
                                    Constants
                                    .VIEW_JBTN_FULLSCREEN_NORMAL_PATH)));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {

            View.getInstance().getJbtn_exit().setIcon(
                    new ImageIcon(Utils.resizeImage(View.getInstance()
                            .getJbtn_exit().getWidth(), View
                            .getInstance().getJbtn_exit().getHeight(),
                            Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
        } else if (_event.getSource().equals(
                View.getInstance().getJbtn_fullscreen())) {
            View.getInstance().getJbtn_fullscreen().setIcon(new ImageIcon(Utils
                    .resizeImage(View.getInstance().getJbtn_exit().getWidth(), 
                            View.getInstance().getJbtn_exit().getHeight(),
                                    Constants
                                    .VIEW_JBTN_FULLSCREEN_MOUSEOVER_PATH)));
        }
    }

    /*
     * mouseMoved
     */

    /**
     * {@inheritDoc}
     */
    public void mouseMoved(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        if (_event.getSource().equals(Page.getInstance().getJlbl_painting())) {

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
                Zoom.getInstance().setLocation(xLocation, yLocation);
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
                
                if (Status.isNormalRotation()) {

                    Picture.getInstance().getPen_current().preprint(
                            _event.getX(), _event.getY());
                } else {

                    Picture.getInstance().getPen_current().preprint(
                            Page.getInstance().getJlbl_painting().getWidth() 
                            - _event.getX(), 
                            Page.getInstance().getJlbl_painting().getHeight() 
                            - _event.getY());
                }
                break;
            default:
                
                break;
            }
                 
        }
    }

    /**
     * method returns only one instance of this.
     * 
     * @return the instance
     */
    public static ControlPainting getInstance() {

        // if instance is not yet created
        if (instance == null) {

            // initialize and start action
            instance = new ControlPainting();
            instance.start();
        }

        // return the only instance
        return instance;
    }

    
    /*
     * 
     * 
     * clipboard
     * 
     * 
     */
    
    /**
     * MouseReleased method for button press at button cut.
     */
    private void mr_cut() {

        MyClipboard.getInstance().copyPaintObjects(
                Picture.getInstance().getLs_poSelected(), 
                Picture.getInstance().paintSelectedBI());
        
        Picture.getInstance().deleteSelected();
        Page.getInstance().releaseSelected();
        Page.getInstance().getJlbl_painting().refreshPaint();
    
    }
    

    /**
     * MouseReleased method for button press at button paste.
     */
    private void mr_paste() {


        Page.getInstance().releaseSelected();
        Picture.getInstance().releaseSelected();
        Picture.getInstance().createSelected();
        
        Object o = MyClipboard.getInstance().paste();
        if (o instanceof BufferedImage) {

            PaintObjectImage poi = Picture.getInstance().createPOI(
                    (BufferedImage) o);
            Picture.getInstance().insertIntoSelected(poi);
            Picture.getInstance().paintSelected();
            Page.getInstance().getJlbl_background2().repaint();
        } else if (o instanceof List) {
            @SuppressWarnings("unchecked")
            List<PaintObject> ls = (List<PaintObject>) o;
            ls.toFirst();
            
            while (!ls.isEmpty() && !ls.isBehind()) {
                PaintObject po = ls.getItem();
                if (po instanceof PaintObjectImage) {
                    PaintObjectImage poi = (PaintObjectImage) po;
                    PaintObjectImage poi_new = Picture.getInstance()
                            .createPOI(poi.getSnapshot());
                    Picture.getInstance().insertIntoSelected(poi_new);
                    
                } else if (po instanceof PaintObjectWriting) {
                    PaintObjectWriting pow = (PaintObjectWriting) po;
                    PaintObjectWriting pow_new 
                    = Picture.getInstance().createPOW(
                            pow.getPen());
                    
                    pow.getPoints().toFirst();
                    while (!pow.getPoints().isEmpty() 
                            && !pow.getPoints().isBehind()) {
                        pow_new.addPoint(new DPoint(
                                pow.getPoints().getItem()));
                        pow.getPoints().next();
                    }
                    Picture.getInstance().insertIntoSelected(pow_new);
                
                } else  if (po != null) {
                    Status.getLogger().warning("unknown kind of "
                            + "PaintObject; element = " + po);
                }
                ls.next();
            }
            
        } else if (o instanceof PaintObjectWriting) {
            Picture.getInstance().insertIntoSelected(
                    (PaintObjectWriting) o);
        } else if (o instanceof PaintObjectImage) {
            Picture.getInstance().insertIntoSelected(
                    (PaintObjectImage) o);
            new Exception("hier").printStackTrace();
        } else {
            Status.getLogger().warning("unknown return type of clipboard");
        }
        Picture.getInstance().paintSelected();
        Page.getInstance().getJlbl_background2().repaint();
        Page.getInstance().getJlbl_painting().refreshPaint();
    
    }

    /**
     * MouseReleased method for button press at button copy.
     */
    private void mr_copy() {

        MyClipboard.getInstance().copyPaintObjects(
                Picture.getInstance().getLs_poSelected(), 
                Picture.getInstance().paintSelectedBI());
            
    }
    
    
    /*
     * 
     * 
     * exit
     * 
     * 
     */

    /**
     * MouseReleased method for button press at button exit.
     */
    private void mr_exit() {

        View.getInstance().getJbtn_exit().setIcon(new ImageIcon(
                Utils.resizeImage(View.getInstance().getJbtn_exit()
                        .getWidth(), View.getInstance().getJbtn_exit()
                        .getHeight(), 
                        Constants.VIEW_JBTN_EXIT_NORMAL_PATH)));
            
    }


    /*
     * 
     * 
     * File operations
     * 
     * 
     */
    
    
    
    /**
     * the save action.
     */
    private void mr_save() {

        // if not saved yet. Otherwise use the saved save path.
        if (Status.getSavePath() == null) {

            // choose a file
            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new java.io.File("."));
            jfc.setDialogTitle("Select save location");
            int retval = jfc.showOpenDialog(View.getInstance());

            // if selected a file.
            if (retval == JFileChooser.APPROVE_OPTION) {

                // fetch the selected file.
                File file = jfc.getSelectedFile();

                // edit file ending
                if (!file.getName().toLowerCase().contains(".")) {
                    file = new File(file.getAbsolutePath() + ".pic");
                } else if (!file.getName().toLowerCase().endsWith(".png")
                        && !file.getName().toLowerCase().endsWith(".pic")) {

                    JOptionPane.showMessageDialog(View.getInstance(),
                            "Select a .png or .pic file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    mr_save();
                    return;
                }

                // if file already exists
                if (file.exists()) {

                    int result = JOptionPane.showConfirmDialog(
                            View.getInstance(), "File already exists. "
                                    + "Owerwrite?", "Owerwrite file?",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == 1) {
                        // no
                        mr_save();
                        return;
                    } else if (result == 2) {
                        // interrupt
                        return;
                    }
                    // overwrite
                }
                Status.setSavePath(file.getAbsolutePath());
            }
        }

        // generate path without the file ending.
        if (Status.getSavePath() != null) {

            int d = Status.getSavePath().toCharArray().length - 2 - 1;
            String firstPath = Status.getSavePath().substring(0, d);
            
            // save images in both formats.
            Picture.getInstance().saveIMAGE(firstPath);
            Picture.getInstance().savePicture(firstPath + "pic");


            Status.setUncommittedChanges(false);
        }
    }
    
    /**
     * MouseReleased method for button press at button load.
     */
    private void mr_load() {

        int i = JOptionPane.showConfirmDialog(View.getInstance(),
                "Do you want to save the committed changes? ",
                "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
        // no
        if (i == 1) {



            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new java.io.File("."));
            jfc.setDialogTitle("Select load location");
            int retval = jfc.showOpenDialog(View.getInstance());

            if (retval == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();

                if (file.getName().toLowerCase().endsWith(".pic")) {
                    Picture.getInstance().loadPicture(file.getAbsolutePath());
                    Status.setUncommittedChanges(false);
                    Page.getInstance().getJlbl_painting().refreshPaint();
                } else if (file.getName().toLowerCase().endsWith(".png")) {
                    
                    try {
                        BufferedImage bi_imageBG = ImageIO.read(file);
                        Status.setImageSize(new Dimension(
                                bi_imageBG.getWidth(), 
                                bi_imageBG.getHeight()));
                        Status.setImageShowSize(Status.getImageSize());
                        Picture.getInstance().emptyImage();
                        Picture.getInstance().addPaintObjectImage(bi_imageBG);
                        Page.getInstance().getJlbl_painting().refreshPaint();
                        
                    } catch (IOException e) {
                        e.printStackTrace();

                        new Error("not supported yet to load pictures "
                                + "because there are no paintObjects for "
                                + "pictures"
                                + "but only those for lines.")
                        .printStackTrace();
                    }
                    
                    

                } else {

                    JOptionPane.showMessageDialog(View.getInstance(),
                            "Select a .png file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    mr_save();
                    Status.setUncommittedChanges(false);
                }
            }
        
        
        
        
        
        } else if (i == 0) {
            // yes
            mr_save();
        }        
    }



    /**
     * the action which is performed if new image is released.
     */
    private void mr_new() {

        if (Status.isUncommittedChanges()) {
            int i = JOptionPane.showConfirmDialog(View.getInstance(),
                    "Do you want to save the committed changes? ",
                    "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
            if (i == 0) {
                mr_save();
                mr_new();
            } 
            
            //if the user does not want to interrupt recall actionNew
            if (i == 1) {

                New.getInstance().setVisible(true);
                Picture.getInstance().reload();
                Status.setUncommittedChanges(false);
            }
        } else {

            New.getInstance().setVisible(true);
            Picture.getInstance().reload();
            Status.setUncommittedChanges(false);

        }
    }

    
    
    /*
     * 
     * 
     * Zoom
     * 
     * 
     */
    
    
    /**
     * MouseReleased method for button press at button zoom out.
     */
    private void mr_zoomOut() {

    	//TODO: adjust this one.
        //if able to zoom out
        if (Status.getImageSize().width
                / Status.getImageShowSize().width 
                < Math.pow(ViewSettings.ZOOM_MULITPLICATOR, 
                        ViewSettings.MAX_ZOOM_OUT)) {
        	
            int newWidth = Status.getImageShowSize().width
                    / ViewSettings.ZOOM_MULITPLICATOR, newHeight = Status
                    .getImageShowSize().height
                    / ViewSettings.ZOOM_MULITPLICATOR;

            int displayedWidth = 
            		Page.getInstance().getJlbl_painting().getWidth();
            int displayedHeight = 
            		Page.getInstance().getJlbl_painting().getHeight();
            
            
            Point oldLocation = new Point(Page.getInstance()
                    .getJlbl_painting().getLocation().x + displayedWidth / 2, 
                    Page.getInstance() 
                    .getJlbl_painting().getLocation().y + displayedHeight / 2);
            

            // not smaller than the
            oldLocation.x = Math.max(oldLocation.x,
            		-(Status.getImageShowSize().width 
                    - Page.getInstance().getJlbl_painting()
                    .getWidth()));
            oldLocation.y = Math.max(oldLocation.y,
                    -(Status.getImageShowSize().height - Page
                            .getInstance().getJlbl_painting()
                            .getHeight()));
            
            
            // not greater than 0
            oldLocation.x = Math.min(oldLocation.x, 0);
            oldLocation.y = Math.min(oldLocation.y, 0); 
     
            Status.setImageShowSize(new Dimension(newWidth, newHeight));
            Page.getInstance().flip();

            Page.getInstance().getJlbl_painting()
                    .setLoc((oldLocation.x) / 2, (oldLocation.y) / 2);
            Page.getInstance().getJlbl_painting().refreshPaint();
            Page.getInstance().refrehsSps();

            Page.getInstance().releaseSelected();
            Picture.getInstance().releaseSelected();
            updateResizeLocation();
        } else {
            Message.showMessage(Message.MESSAGE_ID_INFO, 
                    "max zoom out reached");
            updateResizeLocation();
        }

        // TODO: hier die location aktualisieren.
    
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

            Point oldLocation = new Point(Page.getInstance()
                    .getJlbl_painting().getLocation().x, Page.getInstance()
                    .getJlbl_painting().getLocation().y);

            Status.setImageShowSize(new Dimension(newWidth, newHeight));
            Page.getInstance().flip();

            /*
             * set the location of the panel.
             */
            // save new coordinates
            int newX = (oldLocation.x - Zoom.getInstance().getX())
                    * ViewSettings.ZOOM_MULITPLICATOR;
            int newY = (oldLocation.y - Zoom.getInstance().getY())
                    * ViewSettings.ZOOM_MULITPLICATOR;

            
            // check if the bounds are valid

            // not smaller than the
            newX = Math.max(newX, -(Status.getImageShowSize().width 
                    - Page.getInstance().getJlbl_painting()
                    .getWidth()));
            newY = Math.max(newY,
                    -(Status.getImageShowSize().height - Page
                            .getInstance().getJlbl_painting()
                            .getHeight()));
            
            
            // not greater than 0
            newX = Math.min(newX, 0);
            newY = Math.min(newY, 0);
            

            // apply the location at JLabel (with setLocation method that 
            //is not used for scrolling purpose [IMPORTANT]) and repaint the 
            //image afterwards.
            Page.getInstance().getJlbl_painting().setLoc(newX, newY);
            Page.getInstance().getJlbl_painting().refreshPaint();

            // apply the new location at ScrollPane
            Page.getInstance().refrehsSps();
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
        
        int xShift = _ldp.getSnapshotBounds().x,
                yShift = _ldp.getSnapshotBounds().y;
        
        
        //necessary because the points are painted directly to the buffered
        //image which starts at the _ldp snaphsot x.
        Picture.movePaintObjectWriting(_ldp, -_ldp.getSnapshotBounds().x, 
                -_ldp.getSnapshotBounds().y);
        BufferedImage transform = _ldp.getSnapshot();
        
        
        byte[][] field = PaintBI.printFillPolygonN(transform,
                Color.green, model.util.Util.dpntToPntArray(
                        model.util.Util.pntLsToArray(_ldp.getPoints())));
        
        
        // initialize selection list
        Picture.getInstance().getLs_po_sortedByX().toFirst();
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
            if (po_current.isInSelectionImage(field, xShift, yShift)) {

                //move current item from normal list into selected list 
                Picture.getInstance().insertIntoSelected(po_current);
                Picture.getInstance().getLs_po_sortedByX().remove();
                //remove item out of PictureOverview and paint and refresh paint
                //otherwise it is not possible to select more than one item
                 new PictureOverview().remove(po_current);
                Picture.getInstance().getLs_po_sortedByX().toFirst();
            } else {
                // next; in if clause the next is realized by remove()
                Picture.getInstance().getLs_po_sortedByX().next();
            }
            // update current values
            po_current = Picture.getInstance().getLs_po_sortedByX().getItem();
        }

        
        Picture.getInstance().paintSelected();

        Page.getInstance().getJlbl_painting().refreshPaint();
        
        
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

        int xShift = _ldp.getSnapshotBounds().x,
                yShift = _ldp.getSnapshotBounds().y;
        
        
        //necessary because the points are painted directly to the buffered
        //image which starts at the _ldp snaphsot x.
        Picture.movePaintObjectWriting(_ldp, -_ldp.getSnapshotBounds().x, 
                -_ldp.getSnapshotBounds().y);
        BufferedImage transform = _ldp.getSnapshot();
        
        
        byte[][] field = PaintBI.printFillPolygonN(transform,
                Color.green, model.util.Util.dpntToPntArray(
                        model.util.Util.pntLsToArray(_ldp.getPoints())));
        
        
        /*
         * whole item selection.
         */
        // find paintObjects and move them from image to Selection
        Picture.getInstance().getLs_po_sortedByX().toFirst();

        // initialize selection list
        Picture.getInstance().createSelected();

        // go to the beginning of the list
        Picture.getInstance().getLs_po_sortedByX().toFirst();
        if (!Picture.getInstance().getLs_po_sortedByX().isEmpty()) {

            // go to the beginning of the list
            Picture.getInstance().getLs_po_sortedByX().toFirst();
            
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
                if (po_current.isInSelectionImage(field, xShift, yShift)) {

                    // get item; remove it out of lists and add it to
                    // selection list

                    PaintObject [][] separatedPO = po_current.separate(
                            field, xShift, yShift);
//                    PaintObject [][] p2 = po_current.separate(
//                            new Rectangle(r_sizeField.x - 2,
//                    r_sizeField.y - 2,
//                            r_sizeField.width + 2 * 2, 
//                            r_sizeField.height + 2 * 2));
//                    
//                    PaintObject [][] separatedPO = 
//                    Util.mergeDoubleArray(p, p2);
                     new PictureOverview().remove(Picture.getInstance()
                            .getLs_po_sortedByX().getItem());
                    Picture.getInstance().getLs_po_sortedByX().remove();
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {

                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            Picture.getInstance().insertIntoSelected(
                                    separatedPO[1][current]);
                        } else {
                            
                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview().add(
                                    separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    Picture.getInstance().getLs_po_sortedByX().toFirst();
                } else {
                    
                    // next
                    Picture.getInstance().getLs_po_sortedByX().next();
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
                        ls_toInsert.getItem().getSnapshotBounds().x);
                ls_toInsert.next();
            }
            if (Picture.getInstance().paintSelected()) {
                Page.getInstance().getJlbl_painting().refreshPaint();
            }

        }
        

        Picture.getInstance().paintSelected();

        Page.getInstance().getJlbl_painting().refreshPaint();

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

        /*
         * SELECT ALL ITEMS INSIDE RECTANGLE    
         */
        //case: there can't be items inside rectangle because list is empty
        if (Picture.getInstance().getLs_po_sortedByX().isEmpty()) {

//            //adjust location of the field for painting to view
            _r_size.x += Page.getInstance().getJlbl_painting().getLocation()
                    .x;
            _r_size.y += Page.getInstance().getJlbl_painting().getLocation()
                    .y;
            //paint to view
            Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                    _r_size);
            pnt_start = null;
            return;
        }
        
        // find paintObjects and move them from image to Selection and 
        // initialize selection list
        Picture.getInstance().getLs_po_sortedByX().toFirst();
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
                 new PictureOverview().remove(po_current);
                
                //move current item from normal list into selected list 
                Picture.getInstance().insertIntoSelected(po_current);
                             
                Picture.getInstance().getLs_po_sortedByX().remove();
            }            

            Picture.getInstance().getLs_po_sortedByX().next();

            // update current values
            currentX = po_current.getSnapshotBounds().x;
            po_current = Picture.getInstance().getLs_po_sortedByX().getItem();

        }

        Page.getInstance().getJlbl_painting().refreshPaint();

        if (!Picture.getInstance().paintSelected()) {

          //transform the logical Rectangle to the painted one.
          _r_size.x = (int) (1.0 * _r_size.x / cZoomFactorWidth);
          _r_size.width = (int) 
                  (1.0 * _r_size.width / cZoomFactorWidth);
          _r_size.y = (int) (1.0 * _r_size.y / cZoomFactorHeight);
          _r_size.height = (int) 
                  (1.0 * _r_size.height / cZoomFactorHeight);
          
          _r_size.x 
          += Page.getInstance().getJlbl_painting().getLocation().x;
          _r_size.y 
          += Page.getInstance().getJlbl_painting().getLocation().y;
          
          
          CSelection.getInstance().setR_selection(_r_size,
                  Page.getInstance().getJlbl_painting().getLocation());
          Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                  _r_size);
        }
        Page.getInstance().getJlbl_background2().repaint();
        
    }
    
    

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _event the mouseEvent.
     * @param _r_sizeField the rectangle
     */
    public void mr_sel_line_destroy( 
            final Rectangle _r_sizeField) {

        /*
         * whole item selection.
         */
        // initialize selection list
        Picture.getInstance().createSelected();

        // go to the beginning of the list
        Picture.getInstance().getLs_po_sortedByX().toFirst();
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
                     new PictureOverview().remove(Picture.getInstance()
                            .getLs_po_sortedByX().getItem());
                    Picture.getInstance().getLs_po_sortedByX().remove();
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[1].length;
                            current++) {

                        if (separatedPO[1][current] != null) {

                            //recalculate snapshot bounds for being able to 
                            //insert the item into the sorted list.
                            separatedPO[1][current].recalculateSnapshotBounds();
                            Picture.getInstance().insertIntoSelected(
                                    separatedPO[1][current]);
                        } else {
                            
                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview().add(
                                    separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                } 
                // next
                Picture.getInstance().getLs_po_sortedByX().next();


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
                        ls_toInsert.getItem().getSnapshotBounds().x);
                ls_toInsert.next();
            }
            if (Picture.getInstance().paintSelected()) {
                Page.getInstance().getJlbl_painting().refreshPaint();
            }

            _r_sizeField.x /= cZoomFactorWidth;
            _r_sizeField.width /= cZoomFactorWidth;
            _r_sizeField.y /= cZoomFactorHeight;
            _r_sizeField.height /= cZoomFactorHeight;
        }
        

        Page.getInstance().getJlbl_painting().refreshRectangle(
                _r_sizeField.x, _r_sizeField.y, 
                _r_sizeField.width, _r_sizeField.height);

        
        Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                _r_sizeField);

    }
    
    
    
    
    private synchronized void mr_erase(Point _p) {


    	
    	final Rectangle _r_sizeField = new Rectangle(
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
        Picture.getInstance().getLs_po_sortedByX().toFirst();
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
                     new PictureOverview().remove(Picture.getInstance()
                            .getLs_po_sortedByX().getItem());
                    Picture.getInstance().getLs_po_sortedByX().remove();
                    
                    //go through the list of elements.
                    for (int current = 0; current < separatedPO[0].length;
                            current++) {

                        if (separatedPO[0][current] != null) {
                            //recalculate snapshot bounds for being able to
                            //insert the item into the sorted list.
                            separatedPO[0][current].recalculateSnapshotBounds();
                            ls_toInsert.insertBehind(separatedPO[0][current]);
    
                             new PictureOverview().add(
                                    separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                } 
                // next
                Picture.getInstance().getLs_po_sortedByX().next();


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
                        ls_toInsert.getItem().getSnapshotBounds().x);
                ls_toInsert.next();
            }
            if (Picture.getInstance().paintSelected()) {
                Page.getInstance().getJlbl_painting().refreshPaint();
            }

            _r_sizeField.x /= cZoomFactorWidth;
            _r_sizeField.width /= cZoomFactorWidth;
            _r_sizeField.y /= cZoomFactorHeight;
            _r_sizeField.height /= cZoomFactorHeight;
        }
        

        Page.getInstance().getJlbl_painting().clrRectangle(
                _r_sizeField.x, _r_sizeField.y, 
                _r_sizeField.width, _r_sizeField.height);

    
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
            Picture.getInstance().finish();
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:
            if (_event.getButton() == 1) {

//                PaintObjectWriting ldp 
//                = Picture.getInstance().abortPaintObject();
                //remove old rectangle.
                Page.getInstance().getJlbl_border().setBounds(-1, -1, 0, 0);
                switch (Status.getIndexSelection()) {
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:
                    

                        PaintObjectWriting pow 
                        = Picture.getInstance().abortPaintObject();
                        mr_sel_curve_complete(_event, pow);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:

                    PaintObjectWriting pow2 
                    = Picture.getInstance().abortPaintObject();
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
                CPaintStatus.getInstance().deactivate();
                Paint.getInstance().getTb_move().setActivated(true);
            }
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:
            if (_event.getButton() == 1) {

                Rectangle r = mr_paint_calcRectangleLocation(_event);
                Page.getInstance().getJlbl_border().setBounds(r);
                //remove old rectangle.
                switch (Status.getIndexSelection()) {
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:

                    mr_sel_line_complete(_event, r);
                    break;
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:

                  r.x += Page.getInstance().getJlbl_painting()
                          .getLocation().x;
                  r.y += Page.getInstance().getJlbl_painting()
                          .getLocation().y;
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
                CPaintStatus.getInstance().deactivate();
                Paint.getInstance().getTb_move().setActivated(true);
                Page.getInstance().getJlbl_background2().repaint();
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_ERASE:

//        	mr_erase(_event.getPoint());
//            mr_selection_line_destroy(_event);
//            Picture.getInstance().deleteSelected();
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

            if (_event.getButton() == 1) {
                Picture.getInstance().abortPaintObject();
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_ZOOM_IN:

            if (_event.getButton() == MouseEvent.BUTTON1) {
                mr_zoom(_event);
                updateResizeLocation();
            } else if (_event.getButton() == MouseEvent.BUTTON3) {
            	mr_zoomOut();
            	updateResizeLocation();
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
                Picture.getInstance().releaseSelected();
                Page.getInstance().releaseSelected();
                Page.getInstance().removeButtons();
                Page.getInstance().getJlbl_painting().refreshPaint();
                Page.getInstance().getJlbl_painting().repaint();
            }
            break;
           
        	
            
        default:
            Status.getLogger().warning("Switch in mouseReleased default");
            break;
        }
        New.getInstance().setVisible(false);
    }

    /**
     * The action which is performed if clicked while the current id is pipette.
     * 
     * @param _event
     *            the event.
     */
    private void mr_paint_pipette(final MouseEvent _event) {


        int color = Picture.getInstance().getColorPX(_event.getX(), 
                _event.getY());
        if (_event.getButton() == 1) {
            Paint.getInstance().getTb_color1().setBackground(new Color(color));
        } else {
            Paint.getInstance().getTb_color2().setBackground(new Color(color));
        }
    }
    
    
    
    
    
    
    
    /**
     * Initializes the movement thread.
     * @param _mmSP the movement speed
     */
    private void mr_paint_initializeMovementThread(final Point _mmSP) {
        thrd_move = new Thread() {
            @Override public void run() {
                final int max = 25;
                for (int i = max; i >= 0; i--) {

                    int x = Page.getInstance().getJlbl_painting()
                            .getLocation().x 
                            - _mmSP.x * i / max;
                    int y = Page.getInstance().getJlbl_painting()
                            .getLocation().y 
                            - _mmSP.y * i / max;

                    if (x < -Status.getImageShowSize().width 
                            + Page.getInstance().getJlbl_painting()
                            .getWidth()) {
                        x = -Status.getImageShowSize().width
                                + Page.getInstance()
                                .getJlbl_painting().getWidth();
                    }
                    if (x > 0) {
                        x = 0;
                    }
                    
                    if (y < -Status.getImageShowSize().height
                            + Page.getInstance().getJlbl_painting()
                            .getHeight()) {
                        y = -Status.getImageShowSize().height
                                + Page.getInstance()
                                .getJlbl_painting().getHeight();
                    }
                    if (y >= 0) {
                        y = 0;
                    } 
                    Page.getInstance().getJlbl_painting()
                    .setLocation(x, y);
                    Page.getInstance().refrehsSps();
                    
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

        xLocation -= Page.getInstance().getJlbl_painting()
                .getLocation().x;
        yLocation -= Page.getInstance().getJlbl_painting()
                .getLocation().y;

        
        return new Rectangle(
                xLocation,
                yLocation,
                xSize,
                ySize);

    }
    
    
    /**
     * Update the location of the JButtons for resizing. Thus the user is
     * able to resize the whole image.
     */
    public void updateResizeLocation() {

        //the width and the height
        int w = Status.getImageShowSize().width;
        int h = Status.getImageShowSize().height;

        //set location
        Page.getInstance().getJbtn_resize()[2][1].setLocation(w, h / 2);
        Page.getInstance().getJbtn_resize()[2][2].setLocation(w, h);
        Page.getInstance().getJbtn_resize()[1][2].setLocation(w / 2, h);
        
        //set visible
        Page.getInstance().getJbtn_resize()[1][2].setVisible(true);
        Page.getInstance().getJbtn_resize()[2][2].setVisible(true);
        Page.getInstance().getJbtn_resize()[2][1].setVisible(true);
    }
}
