//package declaration
package control;

//import declarations
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;

import javax.activation.UnsupportedDataTypeException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import control.singleton.CSelection;
import control.singleton.CStatus;
import control.singleton.CVisualEffects;
import model.objects.PictureOverview;
import model.objects.Zoom;
import model.objects.painting.PaintObject;
import model.objects.painting.PaintObjectImage;
import model.objects.painting.PaintObjectWriting;
import model.objects.painting.Picture;
import model.objects.pen.normal.PenKuli;
import model.objects.pen.special.PenSelection;
import model.util.DPoint;
import model.util.list.List;
import model.util.paint.MyClipboard;
import model.util.paint.Utils;
import settings.Constants;
import settings.ReadSettings;
import settings.Settings;
import settings.Status;
import settings.ViewSettings;
import view.View;
import view.forms.New;
import view.forms.Page;
import view.forms.Tabs;
import view.forms.tabs.Paint;

/**
 * Controller class.
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
     * boolean which indicates, whether the necessary instances of View and
     * Picture have been set by now.
     */
    private boolean startPerform;

    /**
     * start point mouseDragged.
     */
    private Point pnt_start, pnt_last, pnt_movementSpeed;

    /**
     * The start location of movement of the JPaintLabel.
     */
    private Point pnt_startLocation;
    
    /**
     * the robot.
     */
    private static Robot robot;

    /**
     * empty utility class Constructor.
     */
    private ControlPainting() { }

    /**
     * pseudo-constructor method. Used because of getInstance
     */
    private void start() {

        // get location of current workspace
        Settings.setWsLocation(ReadSettings.install());

        // initialize startPerform
        this.startPerform = false;
        
        //set logger level to finest; thus every log message is shown.
        Status.getLogger().setLevel(Level.ALL);

        // if installed
        if (!Settings.getWsLocation().equals("")) {
            
            Status.getLogger().info("installation found.\n");

            // initialize the robot
            try {
                robot = new Robot();
            } catch (AWTException e1) {
                e1.printStackTrace();
            }

            /*
             * initialize model
             */

            Status.getLogger().info(
                    "initialize model class Pen and current pen.\n");
            
            Picture.getInstance().initializePen(
                    new PenKuli(Constants.PEN_ID_POINT, 1, Color.black));


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
            CStatus.getInstance().initialize();
            CVisualEffects.getInstance().enable(true);

            Status.getLogger().info("initialization process completed.\n\n"
                    + "-------------------------------------------------\n");

        } else {

            // if not installed and no installation done
            System.exit(1);
        }
    }

    /**
     * enable or disable listeners.
     * 
     * @param _what
     *            whether enabled or disabled.
     */
    public void enable(final boolean _what) {
        startPerform = _what;
    }

    @Override
    public void actionPerformed(final ActionEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        if (_event.getSource().equals(
                Paint.getInstance().getTb_turnMirror().getActionCause())) {

            Status.setNormalRotation(false);
            View.getInstance().flip(false);
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_turnNormal().getActionCause())) {

            Status.setNormalRotation(true);
            View.getInstance().flip(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
                    actionSave();
                } else if (i == 1) {

                    // no
                    System.exit(1);
                } else if (i == 2) {

                    System.out.println(getClass() + "not");

                    // interrupt

                }
            } else {
                System.exit(1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is
        // performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {
            View.getInstance()
                    .getJbtn_exit()
                    .setIcon(
                            new ImageIcon(Utils.resizeImage(View.getInstance()
                                    .getJbtn_exit().getWidth(), View
                                    .getInstance().getJbtn_exit().getHeight(),
                                    "pressed.png")));
        } else if (_event.getSource().equals(
                Page.getInstance().getJlbl_painting())) {

            Tabs.getInstance().closeMenues();
            
            // switch index of operation
            switch (Status.getIndexOperation()) {

            // if painting is selected
            case Constants.CONTROL_PATINING_INDEX_PAINT_2:
            case Constants.CONTROL_PATINING_INDEX_PAINT_1:

                // set currently selected pen. Differs if
                if (_event.getButton() == MouseEvent.BUTTON1) {

                    Picture.getInstance().changePen(Status.getPenSelected1());
                } else if (_event.getButton() == MouseEvent.BUTTON3) {

                    Picture.getInstance().changePen(Status.getPenSelected2());
                }

                // add paintObject and point to Picture
                Picture.getInstance().addPaintObjectWrinting();
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
                
                Page.getInstance().releaseSelected();
                pnt_start = _event.getPoint();
                pnt_startLocation = Page.getInstance().getJlbl_painting()
                        .getLocation();
                
                break;
            default:
                break;

            }

        }
        // System.out.println("save!");
        // save
        // System.out.println(wsLocation);
        // pic.save(wsLocation + "/sjftemp.png");

        // //change size
        // view.getPge_paint().changeSize(1000, 1400);
        // pic.changeSize(1000, 1400);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseDragged(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // left mouse pressed
        final int leftMouse = 1024;
        if (_event.getModifiersEx() == leftMouse
                && (_event.getSource().equals(Page.getInstance()
                        .getJlbl_painting()))) {

            switch (Status.getIndexOperation()) {

            // it is not important for mousePressed whether
            // clicked right or left because the currently
            // selected pen is set in mousePressed
            case Constants.CONTROL_PATINING_INDEX_PAINT_2:
            case Constants.CONTROL_PATINING_INDEX_PAINT_1:

                // hier kann ich das noch gar nicht entscheiden.
                // doch.
                Picture.getInstance().changePaintObject(
                        new DPoint((_event.getX() 
                                - Page.getInstance()
                                .getJlbl_painting().getLocation().x
                                )
                                * Status.getImageSize().width
                                / Status.getImageShowSize().width, (_event
                                .getY() 
                                - Page.getInstance()
                                .getJlbl_painting().getLocation().y
                                )
                                * Status.getImageSize().height
                                / Status.getImageShowSize().height));
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

                Picture.getInstance().changePaintObject(
                        new DPoint(_event.getX(), _event.getY()));
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:

                if (pnt_start == null) {
                    pnt_start = _event.getPoint();
                    Picture.getInstance().abortPaintObject();
                }

                if (pnt_start != null) {

                    Page.getInstance().getJlbl_border().setBounds(
                                    Math.min(pnt_start.x, _event.getX()),
                                    Math.min(pnt_start.y, _event.getY()),
                                    Math.abs(pnt_start.x - _event.getX()),
                                    Math.abs(pnt_start.y - _event.getY()));
                } else {
                    
                    Status.getLogger().warning("Want to print border but"
                            + "the starting point is null.");
                }
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                Picture.getInstance().abortPaintObject();
                Picture.getInstance().changePen(new PenSelection());
                Picture.getInstance().addPaintObjectWrinting();
                break;
            case Constants.CONTROL_PAINTING_INDEX_MOVE:
                
                
                if (pnt_last != null) {
                    pnt_movementSpeed = new Point(pnt_last.x - _event.getX(), 
                            pnt_last.y - _event.getY());
                }
                //Scroll
                
                int x = pnt_startLocation.x + _event.getX() - pnt_start.x;
                int y = pnt_startLocation.y +  _event.getY() - pnt_start.y;

                if (x < -Status.getImageShowSize().width 
                        + Page.getInstance().getJlbl_painting().getWidth()) {
                    x = -Status.getImageShowSize().width
                            + Page.getInstance().getJlbl_painting().getWidth();
                }

                if (x > 0) {
                    x = 0;
                }
                
                if (y < -Status.getImageShowSize().height
                        + Page.getInstance().getJlbl_painting().getHeight()) {
                    y = -Status.getImageShowSize().height
                            + Page.getInstance().getJlbl_painting().getHeight();
                }
                if (y >= 0) {
                    y = 0;
                } 
                Page.getInstance().getJlbl_painting().setLocation(x, y);
                Page.getInstance().refrehsSps();
                pnt_last = _event.getPoint();
                break;
            default:
                break;
            }
        }
    }

    /*
     * mouse over
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {
            View.getInstance().getJbtn_exit().setIcon(new ImageIcon(
                    Utils.resizeImage(View.getInstance().getJbtn_exit()
                            .getWidth(), View.getInstance().getJbtn_exit()
                            .getHeight(), "normal.png")));

        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_new().getActionCause())) {
            actionNew();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_save().getActionCause())) {

            actionSave();
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_load().getActionCause())) {

            int i = JOptionPane.showConfirmDialog(View.getInstance(),
                    "Do you want to save the committed changes? ",
                    "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);

            // no
            if (i == 1) {
                actionLoad();
            } else if (i == 0) {
                // yes
                actionSave();
            }
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_zoomOut().getActionCause())) {

            if (Status.getImageSize().width
                    / Status.getImageShowSize().width 
                    < Math.pow(ViewSettings.ZOOM_MULITPLICATOR, 
                            ViewSettings.MAX_ZOOM_OUT)) {
                int newWidth = Status.getImageShowSize().width
                        / ViewSettings.ZOOM_MULITPLICATOR, newHeight = Status
                        .getImageShowSize().height
                        / ViewSettings.ZOOM_MULITPLICATOR;

                Point oldLocation = new Point(Page.getInstance()
                        .getJlbl_painting().getLocation().x, 
                        Page.getInstance()
                        .getJlbl_painting().getLocation().y);

                Page.getInstance().getJlbl_painting()
                        .setLocation((oldLocation.x) / 2, (oldLocation.y) / 2);

                Status.setImageShowSize(new Dimension(newWidth, newHeight));
                Page.getInstance().flip(Status.isNormalRotation());
                Page.getInstance().refrehsSps();
            } else {
                //TODO: das hier soltle in einer popup text message stehen
                //die nur als info da ist (nicht als fenster sondern nur
                //text schoen @ gui.
                JOptionPane.showMessageDialog(
                        View.getInstance(), "max zoom out reached");
            }

            // TODO: hier die location aktualisieren.
        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_copy().getActionCause())) {

            
            MyClipboard.getInstance().copyPaintObjects(
                    Picture.getInstance().getLs_poSelected(), 
                    Picture.getInstance().paintSelectedBI());

        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_paste().getActionCause())) {

            Page.getInstance().releaseSelected();
            Picture.getInstance().releaseSelected();
            Picture.getInstance().createSelected();
            
            Object o = MyClipboard.getInstance().paste();
            if (o instanceof BufferedImage) {

                Picture.getInstance().addPaintObjectImage((BufferedImage) o);
            } else if (o instanceof List) {

                @SuppressWarnings("unchecked")
                List<PaintObject> ls = (List<PaintObject>) o;
                ls.toFirst();
                
                while (!ls.isEmpty() && !ls.isBehind()) {

                    Picture.getInstance().insertIntoSelected(ls.getItem());
                    ls.next();
                }
                
            } else if (o instanceof PaintObjectWriting) {
                Picture.getInstance().insertIntoSelected(
                        (PaintObjectWriting) o);
            } else if (o instanceof PaintObjectImage) {
                new UnsupportedDataTypeException("hier").printStackTrace();
            } else {
                Status.getLogger().warning("unknown return type of clipboard");
            }
            Page.getInstance().getJlbl_painting().refreshPaint();

        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_cut().getActionCause())) {

            MyClipboard.getInstance().copyPaintObjects(
                    Picture.getInstance().getLs_poSelected(), 
                    Picture.getInstance().paintSelectedBI());
            
            Picture.getInstance().deleteSelected();
            Page.getInstance().releaseSelected();
            

            Page.getInstance().getJlbl_painting().refreshPaint();
        } else if (_event.getSource().equals(
                        Page.getInstance().getJlbl_painting())) {
            mouseReleasedPainting(_event);
        }
    }
    
    /**
     * the mouse released event of painting JLabel.
     * @param _event the event given to mouseListener.
     */
    private void mouseReleasedPainting(final MouseEvent _event) {

        // switch index of operation
        switch (Status.getIndexOperation()) {

        case Constants.CONTROL_PATINING_INDEX_PAINT_2:
        case Constants.CONTROL_PATINING_INDEX_PAINT_1:

            if (_event.getButton() == 1) {
                // write the current working picture into the global picture.
                Picture.getInstance().finish();
            }
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

            if (_event.getButton() == 1) {

                Picture.getInstance().abortPaintObject();
            }
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:


            if (_event.getButton() == 1) {

                //remove old rectangle.
                Page.getInstance().getJlbl_border().setBounds(-1, -1, 0, 0);
                
                switch (Status.getIndexSelection()) {
                
                //select complete item.
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_COMPLETE_ITEM:
                    //paint stuff
                    mr_selection_line_complete(_event);
                    break;
                    
                //destroy items
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_DESTROY_ITEM:
                    //stuff
                    mr_selection_line_destroy(_event);
                    break;
                    
                case Constants.CONTROL_PAINTING_SELECTION_INDEX_IMAGE:
                    break;
                    
                default:
                    break;
                
                }

                //set index to moving
                Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_MOVE);
                CStatus.getInstance().deactivate();
                Paint.getInstance().getTb_move().setActivated(true);
            }
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

            if (_event.getButton() == 1) {


                Picture.getInstance().abortPaintObject();
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_ZOOM:

            if (_event.getButton() == 1) {

                mr_zoom(_event);
            }
            break;
        case Constants.CONTROL_PAINTING_INDEX_PIPETTE:
            actionPipette(_event);
            break;
        case Constants.CONTROL_PAINTING_INDEX_MOVE:


            if (_event.getButton() == 1) {

                final Point mmSP = pnt_movementSpeed;
                if (mmSP != null) {
                    new Thread() {
                        @Override public void run() {
                            final int max = 25;
                            for (int i = max; i >= 0; i--) {
        
                                int x = Page.getInstance().getJlbl_painting()
                                        .getLocation().x 
                                        - mmSP.x * i / max;
                                int y = Page.getInstance().getJlbl_painting()
                                        .getLocation().y 
                                        - mmSP.y * i / max;
        
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
                                    e.printStackTrace();
                                }
                            }
                        }
                    } .start();
                }
                
                //set points to null
                pnt_start = null;
                pnt_startLocation = null;
                pnt_last = null;
                pnt_movementSpeed = null;
                
                //release everything
                Page.getInstance().releaseSelected();
                Picture.getInstance().releaseSelected();
                Page.getInstance().removeButtons();
                Page.getInstance().getJlbl_painting().refreshPaint();
            }
            break;
        default:
            Status.getLogger().warning("Switch in mouseReleased default");
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {
            View.getInstance()
                    .getJbtn_exit()
                    .setIcon(
                            new ImageIcon(Utils.resizeImage(View.getInstance()
                                    .getJbtn_exit().getWidth(), View
                                    .getInstance().getJbtn_exit().getHeight(),
                                    "normal.png")));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        // source: exit button at the top of the window
        if (_event.getSource().equals(View.getInstance().getJbtn_exit())) {

            View.getInstance()
                    .getJbtn_exit()
                    .setIcon(
                            new ImageIcon(Utils.resizeImage(View.getInstance()
                                    .getJbtn_exit().getWidth(), View
                                    .getInstance().getJbtn_exit().getHeight(),
                                    "mouseover.png")));
        }
    }

    /*
     * mouseMoved
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved(final MouseEvent _event) {

        // if the ActionListener is not ready to listen to events because
        // the view is not visible yet return each time an action is performed.
        if (!startPerform) {
            return;
        }

        if (Status.getIndexOperation() 
                == Constants.CONTROL_PAINTING_INDEX_ZOOM) {

            // save x and y location
            int xLocation = _event.getX() - ViewSettings.ZOOM_SIZE.width / 2;
            int yLocation = _event.getY() - ViewSettings.ZOOM_SIZE.height / 2;

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

    /**
     * The action which is performed if clicked while the current id is pipette.
     * 
     * @param _event
     *            the event.
     */
    private void actionPipette(final MouseEvent _event) {


        int color = Picture.getInstance().getColorPX(_event.getX(), 
                _event.getY());
        if (_event.getButton() == 1) {
            Paint.getInstance().getTb_color1().setBackground(new Color(color));
        } else {
            Paint.getInstance().getTb_color2().setBackground(new Color(color));
        }
    }

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _event
     *            the mouseEvent.
     */
    private void mr_selection_line_complete(final MouseEvent _event) {

        /*
         * INITIALIZE 
         */
        //initialize rectangles; 
        //  1) the bounds of the selection rectangle painted by user
        //  2) the bounds of the rectangle which contains each PaintObject 
        //     which is inside 1)
        Rectangle r_sizeField = new Rectangle(
                -Page.getInstance().getJlbl_painting().getLocation().x
                + Math.min(pnt_start.x, _event.getX()), 
                -Page.getInstance().getJlbl_painting().getLocation().y 
                + Math.min(pnt_start.y, _event.getY()),
                Math.abs(pnt_start.x - _event.getX()), Math.abs(pnt_start.y
                        - _event.getY())), r_sizeMax = null;
        if (r_sizeField.x < 0) {
            r_sizeField.x = 0;
        }
        if (r_sizeField.y < 0) {
            r_sizeField.y = 0;
        }

        /*
         * SELECT ALL ITEMS INSIDE RECTANGLE    
         */
        //case: there can't be items inside rectangle because list is empty
        if (Picture.getInstance().getLs_po_sortedByX().isEmpty()) {

            //adjust location of the field for painting to view
            r_sizeField.x += Page.getInstance().getJlbl_painting().getLocation()
                    .x;
            r_sizeField.y += Page.getInstance().getJlbl_painting().getLocation()
                    .y;
            //paint to view
            Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                    r_sizeField);
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
        r_sizeField.x *= cZoomFactorWidth;
        r_sizeField.width *= cZoomFactorWidth;
        r_sizeField.y *= cZoomFactorHeight;
        r_sizeField.height *= cZoomFactorHeight;
        
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

                //fetch the size of the current element
                Rectangle r = po_current.getSnapshotBounds();
                
                // update the max size; if it hasn't been initialized yet
                //take the size of the currentPaintObject; otherwise make it 
                //greater
                if (r_sizeMax == null) {
                    r_sizeMax = new Rectangle(r.x, r.y, 
                            r.width + r.x, r.height + r.y);
                } else {
                    r_sizeMax = new Rectangle(Math.min(r.x, r_sizeMax.x),
                            Math.min(r.y, r_sizeMax.y), 
                            Math.max(r.width + r.x, r_sizeMax.width), 
                            Math.max(r.height + r.y, r_sizeMax.height));
                }
                
                //move current item from normal list into selected list 
                Picture.getInstance().insertIntoSelected(po_current);
                Picture.getInstance().getLs_po_sortedByX().remove();
                //remove item out of PictureOverview and paint and refresh paint
                //otherwise it is not possible to select more than one item
                PictureOverview.getInstance().remove(po_current);
                Picture.getInstance().paintSelected();
                Page.getInstance().getJlbl_painting().refreshPaint();
            } else {
                // next; in if clause the next is realized by remove()
                Picture.getInstance().getLs_po_sortedByX().next();
            }
            // update current values
            currentX = po_current.getSnapshotBounds().x;
            po_current = Picture.getInstance().getLs_po_sortedByX().getItem();
        }

        // paint to selected pane
        if (r_sizeMax != null) {

            Rectangle realRect = new Rectangle(r_sizeMax.x, r_sizeMax.y,
                    r_sizeMax.width - r_sizeMax.x, r_sizeMax.height
                            - r_sizeMax.y);

            realRect.x = (int) (1.0 * realRect.x / cZoomFactorWidth);
            realRect.width = (int) (1.0 * realRect.width / cZoomFactorWidth);
            realRect.y = (int) (1.0 * realRect.y / cZoomFactorHeight);
            realRect.height = (int) (1.0 * realRect.height / cZoomFactorHeight);
            
            realRect.x += Page.getInstance().getJlbl_painting().getLocation().x;
            realRect.y += Page.getInstance().getJlbl_painting().getLocation().y;

            Picture.getInstance().repaintRectangle(realRect);
            CSelection.getInstance().setR_selection(realRect);
            Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                    realRect);
        } else {
            
            //transform the logical Rectangle to the painted one.
            r_sizeField.x = (int) (1.0 * r_sizeField.x / cZoomFactorWidth);
            r_sizeField.width = (int) 
                    (1.0 * r_sizeField.width / cZoomFactorWidth);
            r_sizeField.y = (int) (1.0 * r_sizeField.y / cZoomFactorHeight);
            r_sizeField.height = (int) 
                    (1.0 * r_sizeField.height / cZoomFactorHeight);
            
            r_sizeField.x 
            += Page.getInstance().getJlbl_painting().getLocation().x;
            r_sizeField.y 
            += Page.getInstance().getJlbl_painting().getLocation().y;
            
            CSelection.getInstance().setR_selection(r_sizeField);
            Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                    r_sizeField);
        }
        // reset values
        pnt_start = null;
    }
    
    

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _event
     *            the mouseEvent.
     */
    private void mr_selection_line_destroy(final MouseEvent _event) {

        // fetch rectangle
        Rectangle r_sizeField = new Rectangle(
                -Page.getInstance().getJlbl_painting().getLocation().x
                + Math.min(pnt_start.x, _event.getX()), 
                -Page.getInstance().getJlbl_painting().getLocation().y 
                + Math.min(pnt_start.y, _event.getY()),
                Math.abs(pnt_start.x - _event.getX()), Math.abs(pnt_start.y
                        - _event.getY()));

        //paint selection
        r_sizeField.x += Page.getInstance().getJlbl_painting().getLocation().x;
        r_sizeField.y += Page.getInstance().getJlbl_painting().getLocation().y;
        
        Picture.getInstance().repaintRectangle(r_sizeField);
        Page.getInstance().getJlbl_painting().paintEntireSelectionRect(
                r_sizeField);

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
            int currentX = po_current.getSnapshotBounds().x;
            int currentY = po_current.getSnapshotBounds().y;

            
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
            while (po_current != null
                    && currentX <= r_sizeField.x + r_sizeField.width
                    && currentY <= r_sizeField.y + r_sizeField.height) {

                if (po_current.isInSelectionImage(r_sizeField)) {

                    // get item; remove it out of lists and add it to
                    // selection list

                    PaintObject [][] separatedPO = po_current.separate(
                            r_sizeField);
                    PictureOverview.getInstance().remove(Picture.getInstance()
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
                        //recalculate snapshot bounds for being able to insert
                        //the item into the sorted list.
                        separatedPO[0][current].recalculateSnapshotBounds();
                        ls_toInsert.insertBehind(separatedPO[0][current]);

                        PictureOverview.getInstance().add(
                                separatedPO[0][current]);
                        } else {

                            Status.getLogger().warning("separated paintObject "
                                    + "is null");
                        }
                    }
                } else {
                    
                    // next
                    Picture.getInstance().getLs_po_sortedByX().next();
                }


                // update current values
                currentX = po_current.getSnapshotBounds().x;
                currentY = po_current.getSnapshotBounds().y;
                po_current = Picture.getInstance().getLs_po_sortedByX()
                        .getItem();
            }

            
            //insert the to insert items to graphical user interface.
            ls_toInsert.toFirst();
            while (!ls_toInsert.isBehind()) {

                Picture.getInstance().getLs_po_sortedByX().insertSorted(
                        ls_toInsert.getItem(), 
                        ls_toInsert.getItem().getSnapshotBounds().x);
                ls_toInsert.next();
            }
            Picture.getInstance().paintSelected();
            Page.getInstance().getJlbl_painting().refreshPaint();
        }
        


        // reset values
        pnt_start = null;

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
            Page.getInstance().flip(Status.isNormalRotation());

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

            // apply the location at JLabel
            Page.getInstance().getJlbl_painting().setLocation(newX, newY);

            // apply the new location at ScrollPane
            Page.getInstance().refrehsSps();
        } else {
            
            //TODO: das hier soltle in einer popup text message stehen
            //die nur als info da ist (nicht als fenster sondern nur
            //text schoen @ gui.
            JOptionPane.showMessageDialog(
                    View.getInstance(), "max zoom in reached");
        }
    }

    /**
     * the save action.
     */
    private void actionSave() {

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
                    actionSave();
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
                        actionSave();
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
        int d = Status.getSavePath().toCharArray().length - 2 - 1;
        String firstPath = Status.getSavePath().substring(0, d);

        // save images in both formats.
        Picture.getInstance().savePNG(firstPath + "png");
        Picture.getInstance().savePicture(firstPath + "pic");

        Status.setUncommittedChanges(false);
    }

    /**
     * the load option.
     */
    private void actionLoad() {

        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new java.io.File("."));
        jfc.setDialogTitle("Select load location");
        int retval = jfc.showOpenDialog(View.getInstance());

        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();

            if (file.getName().toLowerCase().endsWith(".pic")) {
                Picture.getInstance().loadPicture(file.getAbsolutePath());
                Status.setUncommittedChanges(false);
            } else if (file.getName().toLowerCase().endsWith(".png")) {
                new Error("not supported yet to load pictures "
                        + "because there are no paintObjects for pictures"
                        + "but only those for lines.").printStackTrace();

            } else {

                JOptionPane.showMessageDialog(View.getInstance(),
                        "Select a .png file.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                actionSave();
                Status.setUncommittedChanges(false);
            }
        }
    }

    /**
     * the action which is performed if new image is released.
     */
    private void actionNew() {

        if (Status.isUncommittedChanges()) {
            int i = JOptionPane.showConfirmDialog(View.getInstance(),
                    "Do you want to save the committed changes? ",
                    "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
            if (i == 0) {
                actionSave();
            }
            if (i == -1) {
                New.getInstance().setVisible(true);
            }
        }

        Picture.getInstance().reload();
        Status.setUncommittedChanges(false);

    }

    /**
     * @return the robot
     */
    public static Robot getRobot() {
        return robot;
    }
}
