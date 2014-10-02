package view.forms;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import model.settings.Constants;
import model.settings.ViewSettings;
import model.util.paint.Utils;
import control.forms.CNew;
import control.util.MousePositionTracker;
import view.util.Item1Button;

/**
 * This class is the user interface for creating a new page.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class New extends JPanel {

    /*
     * singleton
     */
    
    /**
     * The only instance of this class.
     */
    private static New instance;

    /*
     * top elements.
     */

    /**
     * The JPanel which contains for instance the itemButtons.
     */
    private JPanel jpnl_stuff;

    /**
     * Exit button for this tab.
     */
    private JButton jbtn_exit;    
    
    /*
     * Rather unimportant title dummies
     */

    /**
     * Title JLabel.
     */
    private JLabel jlbl_sizeTitle, jlbl_backgroundTitle;
    
    /*
     * pages sizes to be choosen.
     */
    
    /**
     * item Buttons for different page sizes.
     */
    private Item1Button i1b_a4, i1b_a5, i1b_a6, i1b_a7, i1b_custom;

    /**
     * The JLabels which serve as border of Item1Button.
     */
    private JLabel jlbl_bg1, jlbl_bg2, jlbl_bg3, jlbl_bg4, jlbl_bg5;

    /*
     * Settings general
     */
    /**
     * The JCheckBox for background of new Image.
     */
    private JCheckBox jcb_raster, jcb_lines, jcb_nothing;

    /**
     * Where to insert the new page.
     */
    private JComboBox jcb_project;
    
    /**
     * JButton for selecting something and thus completing the creation of the
     * new item.
     */
    private JButton jbtn_enter;

    /*
     * Settings Custom
     */
    
    /**
     * JLabels showing the description for setting width and height manually.
     */
    private JLabel jlbl_customWidth, jlbl_customHeight;
    
    /**
     * JTextFields which will be filled manually by user with the custom
     * size of the new Page.
     */
    private JTextField jtf_customWidth, jtf_customHeight;
    
    
    /*
     * final values for component's bounds.
     */

    /**
     * final values for presentation.
     */
    private final int 
    distanceBetweenItems = 5,
    distanceLeftRight = 5,
    width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()
    / (2 + 1) + (2 + 1) * distanceBetweenItems, 
    height = (int) (width / Math.sqrt(2) 
            + 2 * distanceBetweenItems),
            amount = 6,
            heightImageButton = height / amount - distanceBetweenItems,
            distanceTop = heightImageButton + 2 * distanceBetweenItems,
            buttonHeight = 20;
    
    /**
     * Empty utility class Constructor.
     */
    public New() { }

    
    /**
     * Initializes graphical user interface stuff.
     */
    private void initialize() {
        
        /*
         * Initialize JPanel and its most important elements such as the
         * exit button, the headline JLabel and the JPanel which contains 
         * every other components.
         */
        super.setSize(width, height);
        super.setLocation(0, 0);
        super.setOpaque(true);
        super.setLayout(null);
        super.setVisible(false);
        MousePositionTracker mpt =  new MousePositionTracker(this);
        super.addMouseMotionListener(mpt);
        super.addMouseListener(mpt);

        //The container JPanel
        jpnl_stuff = new JPanel();
        jpnl_stuff.setSize(width, height);
        jpnl_stuff.setBackground(ViewSettings.CLR_BACKGROUND_DARK);
        jpnl_stuff.setLayout(null);
        jpnl_stuff.setBorder(BorderFactory.createLineBorder(Color.black));
        super.add(jpnl_stuff);

        //The exit button which makes the JPanel invisible
        jbtn_exit = new JButton();
        jbtn_exit.setContentAreaFilled(false);
        jbtn_exit.setOpaque(false);
        jbtn_exit.addMouseListener(CNew.getInstance());
        jbtn_exit.setBorder(null);
        jbtn_exit.setFocusable(false);
        jbtn_exit.setBounds(ViewSettings.VIEW_BOUNDS_JBTN_EXIT);
        jbtn_exit.setLocation(jpnl_stuff.getWidth() - jbtn_exit.getWidth() 
                - distanceLeftRight, 1);
        jbtn_exit.setIcon(new ImageIcon(Utils.resizeImage(
                jbtn_exit.getWidth(), jbtn_exit.getHeight(), 
                Constants.VIEW_JBTN_EXIT_MOUSEOVER_PATH)));
        jpnl_stuff.add(jbtn_exit);
        
        //The JLabel which shows the headline
        JLabel jlbl_title = new JLabel("Create new Page");
        jlbl_title.setFont(ViewSettings.GENERAL_FONT_HEADLINE_1);
        jlbl_title.setOpaque(false);
        jlbl_title.setBorder(null);
        jlbl_title.setAlignmentX(CENTER_ALIGNMENT);
        jlbl_title.setLocation(distanceLeftRight, distanceLeftRight);
        jlbl_title.setSize(width, (2 + 2) * (2 + 2 + 1));
        jpnl_stuff.add(jlbl_title);

        
        /*
         * initializes the left half of the graphical user interface containing
         * special page sizes.
         */
        initializeSamplePages();
        

        /*
         * Initializes the first part of the graphical user itnerface's right
         * half. This section contains the specification components that
         * are always shown.
         */
        initializeGeneralSettings();
        
        /*
         * Initializes the second part of the graphical user itnerface's right
         * half. This section contains the specification components that
         * are only shown if the user selected 'custom page' in the left half
         * of the graphical user interface.
         */
        initializeCustomSettings();
        
        
        //hide the custom information and set the Border invisible because the 
        //start configuration is to enable din a4
        hideCustomInformation();
        i1b_a4.setActivated(true);
        jlbl_bg1.setVisible(false);
    }
    
    
    
    /**
     * Initializes the left page of the graphical user interface.
     */
    private void initializeSamplePages() {

        /*
         * the page items
         */
        i1b_a4 = new Item1Button(null);
        i1b_a4.setImageHeight(heightImageButton -  2 * distanceLeftRight);
        i1b_a4.setImageWidth((int)
                (i1b_a4.getImageHeight() / Math.sqrt(2)));
        initialize(i1b_a4, "Din A4 (" + Constants.SIZE_A4.width + "x" 
                + Constants.SIZE_A4.height + ")", 0, distanceTop);
        
        jlbl_bg1 = new JLabel();
        jlbl_bg1.setFocusable(false);
        jlbl_bg1.setBounds(i1b_a4.getX() - 1, i1b_a4.getY() - 1, 
                i1b_a4.getWidth() + 2, i1b_a4.getHeight() + 2);
        jlbl_bg1.setBorder(new LineBorder(Color.gray));
        jlbl_bg1.setOpaque(false);
        jpnl_stuff.add(jlbl_bg1);

        i1b_a5 = new Item1Button(null);
        i1b_a5.setImageHeight(i1b_a4.getImageHeight() / 2);
        i1b_a5.setImageWidth(i1b_a4.getImageWidth());
        initialize(i1b_a5, "Din A5 (" + Constants.SIZE_A5.width + "x" 
                + Constants.SIZE_A5.height + ")",
                0, i1b_a4.getHeight() + i1b_a4.getY() + distanceBetweenItems);

        jlbl_bg2 = new JLabel();
        jlbl_bg2.setFocusable(false);
        jlbl_bg2.setBounds(i1b_a5.getX() - 1, i1b_a5.getY() - 1, 
                i1b_a5.getWidth() + 2, i1b_a5.getHeight() + 2);
        jlbl_bg2.setBorder(new LineBorder(Color.gray));
        jlbl_bg2.setOpaque(false);
        jpnl_stuff.add(jlbl_bg2);
        
        i1b_a6 = new Item1Button(null);
        i1b_a6.setImageHeight(i1b_a5.getImageHeight());
        i1b_a6.setImageWidth(i1b_a5.getImageWidth() / 2);
        initialize(i1b_a6, "Din A6 (" + Constants.SIZE_A6.width + "x" 
                + Constants.SIZE_A6.height + ")", 
                0, i1b_a5.getHeight() + i1b_a5.getY() + distanceBetweenItems);

        jlbl_bg3 = new JLabel();
        jlbl_bg3.setFocusable(false);
        jlbl_bg3.setBounds(i1b_a6.getX() - 1, i1b_a6.getY() - 1, 
                i1b_a6.getWidth() + 2, i1b_a6.getHeight() + 2);
        jlbl_bg3.setBorder(new LineBorder(Color.gray));
        jlbl_bg3.setOpaque(false);
        jpnl_stuff.add(jlbl_bg3);
        
        i1b_a7 = new Item1Button(null);
        i1b_a7.setImageHeight(i1b_a6.getImageHeight() / 2);
        i1b_a7.setImageWidth(i1b_a6.getImageWidth());
        initialize(i1b_a7, "Din A7 (" + Constants.SIZE_A7.width + "x" 
                + Constants.SIZE_A7.height + ")", 
                0, i1b_a6.getHeight() + i1b_a6.getY() + distanceBetweenItems);

        jlbl_bg4 = new JLabel();
        jlbl_bg4.setFocusable(false);
        jlbl_bg4.setBounds(i1b_a7.getX() - 1, i1b_a7.getY() - 1, 
                i1b_a7.getWidth() + 2, i1b_a7.getHeight() + 2);
        jlbl_bg4.setBorder(new LineBorder(Color.gray));
        jlbl_bg4.setOpaque(false);
        jpnl_stuff.add(jlbl_bg4);
        
        i1b_custom = new Item1Button(null);
        i1b_custom.setImageHeight(i1b_a6.getImageHeight() / 2);
        i1b_custom.setImageWidth(i1b_a6.getImageWidth());
        initialize(i1b_custom, "Custom ", 
                0, i1b_a7.getHeight() + i1b_a7.getY() + distanceBetweenItems);

        jlbl_bg5 = new JLabel();
        jlbl_bg5.setFocusable(false);
        jlbl_bg5.setBounds(i1b_custom.getX() - 1, i1b_custom.getY() - 1, 
                i1b_custom.getWidth() + 2, i1b_custom.getHeight() + 2);
        jlbl_bg5.setBorder(new LineBorder(Color.gray));
        jlbl_bg5.setOpaque(false);
        jpnl_stuff.add(jlbl_bg5);
        
    }

    /**
     * Initializes the first part of the graphical user itnerface's right
     * half. This section contains the specification components that
     * are always shown.
     */
    private void initializeGeneralSettings() {

        jlbl_backgroundTitle = new JLabel("Background:");
        jlbl_backgroundTitle.setBounds(getWidth() / 2 + distanceLeftRight, 
                distanceTop, getWidth() / 2 - 2 * distanceLeftRight, 
                buttonHeight);
        jlbl_backgroundTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_backgroundTitle.setFocusable(false);
        jpnl_stuff.add(jlbl_backgroundTitle);

        jcb_raster = new JCheckBox("Raster");
        jcb_raster.setFocusable(false);
        jcb_raster.setOpaque(false);
        jcb_raster.setLocation(jlbl_backgroundTitle.getX(), 
                distanceBetweenItems + jlbl_backgroundTitle.getY()
                + jlbl_backgroundTitle.getHeight());
        jcb_raster.setSize(jlbl_backgroundTitle.getWidth() / (2 + 1) 
                - (2 + 1) * distanceBetweenItems, buttonHeight);
        jcb_raster.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jpnl_stuff.add(jcb_raster);
        
        jcb_lines = new JCheckBox("Lines");
        jcb_lines.setFocusable(false);
        jcb_lines.setOpaque(false);
        jcb_lines.setLocation(jcb_raster.getWidth() + jcb_raster.getX() 
                + distanceBetweenItems, jcb_raster.getY());
        jcb_lines.setSize(jlbl_backgroundTitle.getWidth() / (2 + 1) 
                - (2 + 1) * distanceBetweenItems, buttonHeight);
        jcb_lines.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jpnl_stuff.add(jcb_lines);
        
        jcb_nothing = new JCheckBox("Nothing");
        jcb_nothing.setFocusable(false);
        jcb_nothing.setOpaque(false);
        jcb_nothing.setLocation(jcb_lines.getWidth() + jcb_lines.getX() 
                + distanceBetweenItems, jcb_raster.getY());
        jcb_nothing.setSize(jlbl_backgroundTitle.getWidth() / (2 + 1) 
                - (2 + 1) * distanceBetweenItems, buttonHeight);
        jcb_nothing.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jpnl_stuff.add(jcb_nothing);

        JLabel jlbl_sizeProject = new JLabel("Project");
        jlbl_sizeProject.setBounds(jlbl_backgroundTitle.getX(), 
                jcb_nothing.getHeight() + jcb_nothing.getY() 
                + distanceBetweenItems * 2, jlbl_backgroundTitle.getWidth() / 2
                - distanceBetweenItems,
                buttonHeight);
        jlbl_sizeProject.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_sizeProject.setFocusable(false);
        jpnl_stuff.add(jlbl_sizeProject);

        jcb_project = new JComboBox(new String[]{"none", "..."});
        jcb_project.setBounds(jlbl_sizeProject.getX(), 
                jlbl_sizeProject.getHeight() + jlbl_sizeProject.getY() 
                + distanceBetweenItems, jlbl_sizeProject.getWidth(),
                buttonHeight);
        jpnl_stuff.add(jcb_project);
        
    }
    
    
    /**
     * Initializes the second part of the graphical user itnerface's right
     * half. This section contains the specification components that
     * are only shown if the user selected 'custom page' in the left half
     * of the graphical user interface.
     */
    private void initializeCustomSettings() {

        jlbl_sizeTitle = new JLabel("Size");
        jlbl_sizeTitle.setBounds(jlbl_backgroundTitle.getX(), 
                jcb_project.getHeight() + jcb_project.getY() 
                + distanceBetweenItems * 2, jlbl_backgroundTitle.getWidth() / 2
                - distanceBetweenItems,
                buttonHeight);
        jlbl_sizeTitle.setFont(ViewSettings.GENERAL_FONT_HEADLINE_2);
        jlbl_sizeTitle.setFocusable(false);
        jpnl_stuff.add(jlbl_sizeTitle);

        jlbl_customHeight = new JLabel("Height: ");
        jlbl_customHeight.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jlbl_customHeight.setFocusable(false);
        jlbl_customHeight.setBounds(jlbl_sizeTitle.getX(), 
                jlbl_sizeTitle.getHeight() + jlbl_sizeTitle.getY() 
                + distanceBetweenItems, jlbl_sizeTitle.getWidth() / 2
                - distanceBetweenItems,
                buttonHeight);
        jpnl_stuff.add(jlbl_customHeight);
        
        jtf_customHeight = new JTextField();
        jtf_customHeight.setOpaque(false);
        jtf_customHeight.addKeyListener(CNew.getInstance());
        jtf_customHeight.setBorder(BorderFactory.createLineBorder(Color.gray));
        jtf_customHeight.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jtf_customHeight.setBounds(jlbl_customHeight.getX() 
                + jlbl_customHeight.getWidth() + distanceBetweenItems,
                jlbl_customHeight.getY(), jlbl_customHeight.getWidth(), 
                jlbl_customHeight.getHeight());
        jpnl_stuff.add(jtf_customHeight);
        
        jlbl_customWidth = new JLabel("Width: ");
        jlbl_customWidth.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jlbl_customWidth.setFocusable(false);
        jlbl_customWidth.setBounds(jlbl_customHeight.getX(), 
                distanceBetweenItems + jlbl_customHeight.getY()
                + jlbl_customHeight.getHeight(), 
                jlbl_customHeight.getWidth(),
                buttonHeight);
        jpnl_stuff.add(jlbl_customWidth);

        jtf_customWidth = new JTextField();
        jtf_customWidth.setOpaque(false);
        jtf_customWidth.addKeyListener(CNew.getInstance());
        jtf_customWidth.setBorder(BorderFactory.createLineBorder(Color.gray));
        jtf_customWidth.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jtf_customWidth.setBounds(jlbl_customWidth.getX() 
                + jlbl_customWidth.getWidth() + distanceBetweenItems,
                jlbl_customWidth.getY(), jlbl_customWidth.getWidth(), 
                jlbl_customWidth.getHeight());
        jpnl_stuff.add(jtf_customWidth);
        
        jbtn_enter = new JButton("Create");
        jbtn_enter.setBounds(getWidth() / 2 + distanceLeftRight, 
                getHeight() - distanceBetweenItems - buttonHeight,
                getWidth() / 2 - 2 * distanceLeftRight, buttonHeight);
        jbtn_enter.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jbtn_enter.setContentAreaFilled(false);
        jbtn_enter.addActionListener(CNew.getInstance());
        jpnl_stuff.add(jbtn_enter);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override public void setVisible(final boolean _visible) {
        super.setLocation((Page.getInstance().getWidth() - getWidth()) / 2,
                (Page.getInstance().getHeight() - getHeight()) / 2);
        super.setVisible(_visible);
        super.requestFocus();
    }
    
    /**
     * Initialize one page - item.
     * @param _i1b the page item
     * @param _text the text 
     * @param _plusXLocation the amount of pixel which is added to the
     * x coordinate of the component
     * @param _plusYLocation the amount of pixel which is added to the
     * x coordinate of the component
     */
    private void initialize(final Item1Button _i1b, final String _text, 
            final int _plusXLocation, final int _plusYLocation) {

        _i1b.setOpaque(true);
        _i1b.setBackground(Color.white);
        _i1b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black), new LineBorder(Color.lightGray)));
        _i1b.setText(_text);
        
        _i1b.setSize((width - (2 + 1) * distanceBetweenItems) / 2, 
                heightImageButton);
        _i1b.setActivable(true);
        _i1b.addActionListener(CNew.getInstance());
        _i1b.setLocation(distanceBetweenItems + _plusXLocation, 
                _plusYLocation);
        _i1b.setIconLabelY((_i1b.getHeight() 
                - _i1b.getImageHeight()) / 2);
        _i1b.setBorder(true);
        jpnl_stuff.add(_i1b);
    }
    
    
    /**
     * Set the custom elements visible or invisible.
     * @param _visible whether visible or invisible
     */
    private void setCustomVisible(final boolean _visible) {

        jtf_customHeight.setVisible(_visible);
        jtf_customWidth.setVisible(_visible);
        jlbl_customHeight.setVisible(_visible);
        jlbl_customWidth.setVisible(_visible);
        jlbl_sizeTitle.setVisible(_visible);
        
    }
    
    /**
     * Hide the custom information for changing size.
     */
    public void hideCustomInformation() {
        setCustomVisible(false);
    }
    

    /**
     * Show the custom information for changing size.
     */
    public void showCustomInformation() {

        setCustomVisible(true);
    }
    
    
    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * @return the only instance of this class.
     */ 
    public static New getInstance() {
        
        //create instance if necessary.
        if (instance == null) {
            instance = new New();
            instance.initialize();
        }
        
        //return the only instance
        return instance;
    }


    /**
     * @return the i1b_a4
     */
    public Item1Button getI1b_a4() {
        return i1b_a4;
    }


    /**
     * @return the i1b_a5
     */
    public Item1Button getI1b_a5() {
        return i1b_a5;
    }

    
    /**
     * @return the i1b_a6
     */
    public Item1Button getI1b_a6() {
        return i1b_a6;
    }


    /**
     * @return the i1b_a7
     */
    public Item1Button getI1b_a7() {
        return i1b_a7;
    }


    /**
     * @return the i1b_custom
     */
    public Item1Button getI1b_custom() {
        return i1b_custom;
    }


    /**
     * @return the jbtn_exit
     */
    public JButton getJbtn_exit() {
        return jbtn_exit;
    }


    /**
     * @return the jlbl_bg1
     */
    public JLabel getJlbl_bg1() {
        return jlbl_bg1;
    }


    /**
     * @return the jlbl_bg2
     */
    public JLabel getJlbl_bg2() {
        return jlbl_bg2;
    }


    /**
     * @return the jlbl_bg3
     */
    public JLabel getJlbl_bg3() {
        return jlbl_bg3;
    }


    /**
     * @return the jlbl_bg4
     */
    public JLabel getJlbl_bg4() {
        return jlbl_bg4;
    }


    /**
     * @return the jlbl_bg5
     */
    public JLabel getJlbl_bg5() {
        return jlbl_bg5;
    }



    /**
     * @return the jcb_raster
     */
    public JCheckBox getJcb_raster() {
        return jcb_raster;
    }



    /**
     * @return the jcb_lines
     */
    public JCheckBox getJcb_lines() {
        return jcb_lines;
    }



    /**
     * @return the jcb_nothing
     */
    public JCheckBox getJcb_nothing() {
        return jcb_nothing;
    }


   

    /**
     * @return the jcb_project
     */
    public JComboBox getJcb_project() {
        return jcb_project;
    }


    /**
     * @return the jbtn_enter
     */
    public JButton getJbtn_enter() {
        return jbtn_enter;
    }


    /**
     * @return the jtf_customWidth
     */
    public JTextField getJtf_customWidth() {
        return jtf_customWidth;
    }


    /**
     * @return the jtf_customHeight
     */
    public JTextField getJtf_customHeight() {
        return jtf_customHeight;
    }
}
