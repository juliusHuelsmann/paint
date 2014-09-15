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
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import control.singleton.ControlSelectionColorPen;
import control.singleton.ControlVisualEffects;
import model.objects.Zoom;
import model.objects.painting.PaintObject;
import model.objects.painting.Picture;
import model.objects.pen.normal.PenKuli;
import model.objects.pen.special.PenSelection;
import settings.Constants;
import settings.ReadSettings;
import settings.Settings;
import settings.Status;
import settings.ViewSettings;
import start.utils.MyClipboard;
import start.utils.Utils;
import view.View;
import view.forms.New;
import view.forms.Page;
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
    private Point pnt_start;

    /**
     * the robot.
     */
    private static Robot robot;

    /**
     * empty utility class Constructor.
     */
    private ControlPainting() {
    }

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
            ControlSelectionColorPen.getInstance().initialize();
            ControlVisualEffects.getInstance().enable(true);

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

            System.out.println(getClass() + " painting pressed");
            
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
                        new Point((_event.getX() 
//                                + Page.getInstance()
//                                .getJlbl_painting().getLocation().x
                                )
                                * Status.getImageSize().width
                                / Status.getImageShowSize().width, (_event
                                .getY() 
//                                + Page.getInstance().getJlbl_painting()
//                                .getLocation().y
                                )
                                * Status.getImageSize().height
                                / Status.getImageShowSize().height));

                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

                // abort old paint object
                Picture.getInstance().abortPaintObject();

                // change pen and add new paint object
                Picture.getInstance()
                        .changePen(
                                new PenSelection(Constants.PEN_ID_LINES, 1,
                                        Color.gray));
                Picture.getInstance().addPaintObjectWrinting();
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                Picture.getInstance().abortPaintObject();
                Picture.getInstance()
                        .changePen(
                                new PenSelection(Constants.PEN_ID_LINES, 1,
                                        Color.gray));
                Picture.getInstance().addPaintObjectWrinting();
                break;
            case Constants.CONTROL_PAINTING_INDEX_PIPETTE:

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

            System.out.println(getClass() + " painting pressed");
            switch (Status.getIndexOperation()) {

            // it is not important for mousePressed whether
            // clicked right or left because the currently
            // selected pen is set in mousePressed
            case Constants.CONTROL_PATINING_INDEX_PAINT_2:
            case Constants.CONTROL_PATINING_INDEX_PAINT_1:

                // hier kann ich das noch gar nicht entscheiden.
                // doch.
                Picture.getInstance().changePaintObject(
                        new Point((_event.getX() 
//                                + Page.getInstance()
//                                .getJlbl_painting().getLocation().x
                                )
                                * Status.getImageSize().width
                                / Status.getImageShowSize().width, (_event
                                .getY() 
//                                + Page.getInstance()
//                                .getJlbl_painting().getLocation().y
                                )
                                * Status.getImageSize().height
                                / Status.getImageShowSize().height));
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

                Picture.getInstance().changePaintObject(
                        new Point(_event.getX(), _event.getY()));
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:

                if (pnt_start == null) {
                    pnt_start = _event.getPoint();
                    Picture.getInstance().abortPaintObject();
                }

                if (pnt_start != null) {

                    Page.getInstance().getJlbl_painting().removeOldRectangle();
                    Page.getInstance()
                            .getJlbl_painting()
                            .paintSelection(
                                    Math.min(pnt_start.x, _event.getX()),
                                    Math.min(pnt_start.y, _event.getY()),
                                    Math.abs(pnt_start.x - _event.getX()),
                                    Math.abs(pnt_start.y - _event.getY()));
                } else {
                    System.out.println("ist null.");
                }
                break;

            case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:

                Picture.getInstance().abortPaintObject();
                Picture.getInstance()
                        .changePen(
                                new PenSelection(Constants.PEN_ID_LINES, 1,
                                        Color.gray));
                Picture.getInstance().addPaintObjectWrinting();
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
                        .getJpnl_toMove().getX(), 
                        Page.getInstance().getJpnl_toMove().getY());

                Page.getInstance().getJpnl_toMove()
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

            // TODO: // copy to clipboard (ENTIRE normal image)
            MyClipboard.getInstance().copyImage(
                    Picture.getInstance().getBi_normalSize());

        } else if (_event.getSource().equals(
                Paint.getInstance().getTb_paste().getActionCause())) {

            // TODO: error if not image copy to clipboard (ENTIRE normal image)
            Picture.getInstance().addPaintObjectImage(
                    (BufferedImage) MyClipboard.getInstance().paste());

        } else if (_event.getButton() == 1
                && _event.getSource().equals(
                        Page.getInstance().getJlbl_painting())) {
            mouseReleasedPainting(_event);
        }
    }
    
    /**
     * the mouse released event of painting JLabel.
     * @param _event the event given to mouseListener.
     */
    private void mouseReleasedPainting(final MouseEvent _event) {

        System.out.println(getClass() + " painting released");
        // switch index of operation
        switch (Status.getIndexOperation()) {

        case Constants.CONTROL_PATINING_INDEX_PAINT_2:
        case Constants.CONTROL_PATINING_INDEX_PAINT_1:

            System.out.println("finiesh");
            // write the current working picture into the global picture.
            Picture.getInstance().finish();
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_CURVE:

            Picture.getInstance().abortPaintObject();
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_LINE:

            actionSelectionLine(_event);
            break;

        case Constants.CONTROL_PAINTING_INDEX_SELECTION_MAGIC:
            Picture.getInstance().abortPaintObject();
            break;
        case Constants.CONTROL_PAINTING_INDEX_ZOOM:

            if (Status.getImageShowSize().width
                    / Status.getImageSize().width 
                    < Math.pow(ViewSettings.ZOOM_MULITPLICATOR, 
                            ViewSettings.MAX_ZOOM_IN)) {
                
                int newWidth = Status.getImageShowSize().width
                        * ViewSettings.ZOOM_MULITPLICATOR, 
                        newHeight = Status.getImageShowSize().height
                        * ViewSettings.ZOOM_MULITPLICATOR;

                Point oldLocation = new Point(Page.getInstance()
                        .getJpnl_toMove().getX(), Page.getInstance()
                        .getJpnl_toMove().getY());

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
                Page.getInstance().getJpnl_toMove().setLocation(newX, newY);

                // apply the new location at ScrollPane
                Page.getInstance().refrehsSps();
                System.out.println("done");
            } else {
                
                //TODO: das hier soltle in einer popup text message stehen
                //die nur als info da ist (nicht als fenster sondern nur
                //text schoen @ gui.
                JOptionPane.showMessageDialog(
                        View.getInstance(), "max zoom in reached");
            }
            break;

        case Constants.CONTROL_PAINTING_INDEX_PIPETTE:
            actionPipette(_event);
            break;
        default:
            System.out.println(getClass()
                    + "switch in mouseReleased default");
            System.exit(1);
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

        final int hundred = 100;
        final double dX = hundred * (0.000 + Status.getImageSize().width)
                / Status.getImageShowSize().width;
        final double dY = hundred * (0.000 + Status.getImageSize().height)
                / Status.getImageShowSize().height;

        int color = Picture.getInstance().getColorPX(
                (int) ((_event.getX() - Page.getInstance().getJlbl_painting()
                        .getLocation().x)
                        * hundred / dX),
                (int) ((_event.getY() - Page.getInstance().getJlbl_painting()
                        .getLocation().y)
                        * hundred / dY));

        System.out.println(color);
    }

    /**
     * The event which is performed after performed a mouseReleased with id
     * selection line.
     * 
     * @param _event
     *            the mouseEvent.
     */
    private void actionSelectionLine(final MouseEvent _event) {

        // TODO: die items finden, die im gesamt - viereck liegen und
        // die teile aus dem gesamt - viereck repainten.

        // fetch rectangle
        Rectangle r_sizeField = new Rectangle(Math.min(pnt_start.x,
                _event.getX()), Math.min(pnt_start.y, _event.getY()),
                Math.abs(pnt_start.x - _event.getX()), Math.abs(pnt_start.y
                        - _event.getY()));

        // the maximal bounds which cover all other items
        Rectangle r_sizeMax = null;

        // print selection WITH ALL VISUAL EFFECTS
        Page.getInstance().getJlbl_painting().paintEntireSelection(r_sizeField);
        /*
         * whole item selection.
         */
        // find paintObjects and move them from image to Selection
        Picture.getInstance().getLs_po_sortedByX().toFirst();

        // initialize selection list
        Picture.getInstance().createSelected();

        // go to the beginning of the list
        Picture.getInstance().getLs_po_sortedByX().toFirst();
        if (Picture.getInstance().getLs_po_sortedByX().isEmpty()) {
            pnt_start = null;
            // TODO selektierte list of items loeschen.
            System.out.println("returning");
            return;
        }
        // create and initialize current values
        PaintObject po_current = Picture.getInstance().getLs_po_sortedByX()
                .getItem();
        int currentX = po_current.getSnapshotBounds().x;
        int currentY = po_current.getSnapshotBounds().y;
        BufferedImage bi_selection = new BufferedImage(
                Status.getImageSize().width, Status.getImageSize().height,
                BufferedImage.TYPE_INT_ARGB);

        final int rgb_alpha = new Color(0, 0, 0, 0).getRGB();
        for (int i = 0; i < bi_selection.getWidth(); i++) {
            for (int j = 0; j < bi_selection.getHeight(); j++) {
                bi_selection.setRGB(i, j, rgb_alpha);
            }
        }

        // go through list. until either list is empty or it is
        // impossible for the paintSelection to paint inside the
        // selected area
        while (po_current != null
                && currentX <= r_sizeField.x + r_sizeField.width
                && currentY <= r_sizeField.y + r_sizeField.height) {

            if (po_current.isInSelectionImage(r_sizeField)) {

                // update maximal size
                if (r_sizeMax == null) {
                    Rectangle r = po_current.getSnapshotBounds();
                    r_sizeMax = new Rectangle(r.x, r.y, r.width + r.x, r.height
                            + r.y);
                } else {

                    Rectangle r = po_current.getSnapshotBounds();

                    r_sizeMax = new Rectangle(Math.min(r.x, r_sizeMax.x),
                            Math.min(r.y, r_sizeMax.y), Math.max(r.width + r.x,
                                    r_sizeMax.width), Math.max(r.height + r.y,
                                    r_sizeMax.height));
                }
                // get item; remove it out of lists and add it to
                // selection list
                Picture.getInstance().getLs_po_sortedByX().remove();
                Picture.getInstance().insertIntoSelected(po_current);

                // //add it to bufferedImage of SelectionPane and
                // //highlight it as selected
                // bi_selection = po_current.paint(null, true,
                // _graphicsSelection,
                // Page.getInstance().getJlbl_painting().getX(),
                // Page.getInstance().getJlbl_painting().getY(),
                // Page.getInstance().getJlbl_painting()
                // .getWidth(), Page.getInstance()
                // .getJlbl_painting().getHeight());
            }

            // next
            Picture.getInstance().getLs_po_sortedByX().next();

            // update current values
            currentX = po_current.getSnapshotBounds().x;
            currentY = po_current.getSnapshotBounds().y;
            po_current = Picture.getInstance().getLs_po_sortedByX().getItem();
        }

        // paint to selected pane
        if (r_sizeMax != null) {

            Rectangle realRect = new Rectangle(r_sizeMax.x, r_sizeMax.y,
                    r_sizeMax.width - r_sizeMax.x, r_sizeMax.height
                            - r_sizeMax.y);
            Picture.getInstance().repaintRectangle(realRect);
            // Selection.getInstance().showSelection(realRect);
        }

        // reset values
        pnt_start = null;

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
                System.out.println("not supported yet to load pictures "
                        + "because there are no paintObjects for pictures"
                        + "but only those for lines.");

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
