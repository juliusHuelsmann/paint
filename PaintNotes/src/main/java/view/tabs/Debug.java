//package declaration
package view.tabs;

//import declarations
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.objects.PictureOverview;
import model.settings.Constants;
import model.settings.Status;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import control.forms.tabs.CTabDebug;
import view.util.VScrollPane;
import view.util.Item1Button;
import view.util.mega.MLabel;
import view.util.mega.MPanel;

/**
 * JPanel which shows the list of existing non-selected PaintObjects and 
 * details about them. Is created mainly for testing purpose.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Debug extends Tab implements Observer {

	//identifier for 
	/**
	 * id for reloading everything; clears the JPanel, receives as object in 
	 * method update a list of Items.
	 */
	public static final int ID_RELOAD = 0;
	
	
	/**
	 * id for adding one item; clears JPanel, receives.
	 */
	public static final int ID_ADD_ITEM = 1;

    
    /**
     * ID for removing one item from normal list and thus from view.
     */
    public static final int ID_REMOVE_ITEM = 2;
    
    
    /**
     * ID for adding one item to selected list after it has been removed from
     * normal list. Thus is is re included to the PaintObjects view (this)
     * until it loses focus and may be re-added to the normal list of items.
     */
    public static final int ID_ADD_ITEM_SELECTED = 3;
    
    /**
     * ID for removing one item from selected list and thus temporarily from
     * PaintObjects until it is re added to the normal list of items.
     */
    public static final int ID_REMOVE_ITEM_SELECTED = 4;
    
	/**
	 * Contains the amount of items contained in JPanel, title and the 
	 * detailed position in an image of the view.
	 */
	private MLabel jlbl_amountOfItems, jlbl_title, jlbl_detailedPosition;
	
	//Items shown in in ScrollPanel
	
	/**
	 * Contains the items .
	 */
	private final MPanel jpnl_items, jpnl_owner;
	
	/**
	 * ScrollPanel for items.
	 */
	private final VScrollPane sp_up;
	
	/**
	 * JPanel which contains the JScrollPane of the JTextArea.
	 */
	private final MPanel jpnl_container;

	/**
	 * JTextArea containing information on the selected item.
	 */
	private final JTextArea jta_infoSelectedPanel;

    //value for functionality of adding something
    
	
	/**
	 * The controller for the debug-tab.
	 */
	private CTabDebug cps;
	
	
	/**
	 * JButton for to enable and disable console.
	 */
	private Item1Button i1b_console;
	
	
	/**
	 * Button for generating a view diagram which is saved as a PNG image 
	 * called analyze.png. inside the current working directory.
	 */
	private Item1Button i1b_diagramView;
	
	/**
	 * Generates a text-file which contains the logging information on the 
	 * current session.
	 */
	private Item1Button i1b_generateLog;
	
	
	/**
	 * Is opening a view form where the user can insert information on the 
	 * bug that is to be reported.
	 * Afterwards, a bug message is sent, containing log information on the 
	 * current session and the bug description provided by the user.
	 */
	private Item1Button i1b_reportBug;
	
	/**
	 * Constructor: initialize instances of Components and add special 
	 * MouseMotionListener.
	 * @param 	_cps		the controller class for the tab debug.
	 */
	public Debug(final CTabDebug _cps) {
		
		//initialize JPanel and alter settings
		super(0);
		super.setLayout(null);
		super.setOpaque(false);

		//final rather unimportant values
		final int tabSize = 10;
		this.cps = _cps;
		
		//title
		jlbl_title = new MLabel(TextFactory.getInstance()
				.getView_debug_headline());
		jlbl_title.setFocusable(false);
		jlbl_title.setBorder(null);
		jlbl_title.setFont(ViewSettings.GENERAL_FONT_HEADLINE_1);
		super.add(jlbl_title);
		
		//owner of items panel and items panel
		jpnl_owner = new MPanel();
		jpnl_owner.setLayout(null);
		jpnl_owner.setVisible(true);
		jpnl_owner.setBorder(null);
		jpnl_owner.setOpaque(false);
		super.add(jpnl_owner);

		jpnl_items = new MPanel();
		jpnl_items.setLayout(null);
		jpnl_items.setVisible(true);
		jpnl_items.setBorder(null);
		jpnl_items.setOpaque(false);
		jpnl_owner.add(jpnl_items);

		//JLabel for amount of items
		jlbl_amountOfItems = new MLabel();
		jlbl_amountOfItems.setFocusable(false);
		jlbl_amountOfItems.setBorder(null);
		jlbl_amountOfItems.setFont(ViewSettings.GENERAL_FONT_ITEM);
		super.add(jlbl_amountOfItems);

        //
        sp_up = new VScrollPane(jpnl_items, jpnl_owner, true);
        sp_up.setOpaque(false);
        jpnl_owner.add(sp_up);
    
		//JTextArea, container and ScrollPane
		jpnl_container = new MPanel();
		jpnl_container.setLayout(new BorderLayout());
		super.add(jpnl_container);
		
		jta_infoSelectedPanel = new JTextArea();
		jta_infoSelectedPanel.setEditable(false);
		jta_infoSelectedPanel.setOpaque(false);
		jta_infoSelectedPanel.setFocusable(false);
		jta_infoSelectedPanel.setBorder(null);
		jta_infoSelectedPanel.setTabSize(tabSize);
		jta_infoSelectedPanel.setLineWrap(true);
		jta_infoSelectedPanel.setFont(ViewSettings.GENERAL_FONT_ITEM_SMALL);

		JScrollPane scrollPane = new JScrollPane(jta_infoSelectedPanel);
		scrollPane.setVerticalScrollBarPolicy(
		        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jpnl_container.add(scrollPane, BorderLayout.CENTER);
		
		jlbl_detailedPosition = new MLabel();
		jlbl_detailedPosition.setFocusable(false);
		jlbl_detailedPosition.setBorder(
		        BorderFactory.createLineBorder(Color.black));
		super.add(jlbl_detailedPosition);


		i1b_console = new Item1Button(null);
		i1b_console.setBorder(false);
        i1b_console.addActionListener(null);
        i1b_console.addActionListener(_cps);
        i1b_console.setActivable(false);
		super.add(i1b_console);


		i1b_diagramView = new Item1Button(null);
		i1b_diagramView.setBorder(false);
		i1b_diagramView.addActionListener(null);
		i1b_diagramView.addActionListener(_cps);
		i1b_diagramView.setActivable(false);
		super.add(i1b_diagramView);
		


		i1b_generateLog = new Item1Button(null);
		i1b_generateLog.setBorder(false);
		i1b_generateLog.addActionListener(null);
		i1b_generateLog.addActionListener(_cps);
		i1b_generateLog.setActivable(false);
		super.add(i1b_generateLog);


		i1b_reportBug = new Item1Button(null);
		i1b_reportBug.setBorder(false);
		i1b_reportBug.addActionListener(null);
		i1b_reportBug.addActionListener(_cps);
		i1b_reportBug.setActivable(false);
		super.add(i1b_reportBug);
	}
	
	
	/**
	 * deactivate all the components.
	 */
	public void deactivate() {
		for (int i = 0; i < jpnl_items.getComponents().length; i++) {
		    if (jpnl_items.getComponent(i) instanceof Item1Button) {
	            ((Item1Button) jpnl_items.getComponent(i)).setActivated(false);
		    }
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public Component add(final Component _component) {

	    final int distance = 5;
	    
		//update the rectangle
		cps.getRec_old().y += 
		        cps.getRec_old().getHeight();
		
		//set bounds of c
		_component.setSize(cps.getRec_old().width, 
		        cps.getRec_old().height);
		_component.setLocation(cps.getRec_old().x, 
		        cps.getRec_old().y);
		
		//update size of JPanel for items.
		jpnl_items.setSize(
		        cps.getRec_old().width + distance,
		        cps.getRec_old().height 
		        + cps.getRec_old().y + distance);

		sp_up.reload();
		
		
		//resize container.
		return jpnl_items.add(_component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void setSize(final int _width, final int _height) {

	    
		final int heightJLabel2 = 20;
		final int heightJLabel = 10;
		final int heightNewComponent = 50;
		final int distance = 5;
		
		//set size of super and owner
		final int iconSize = 16;
		final int twentyFife = 25;
		
		
        sp_up.setIcon_size(iconSize);
        
        
        final int availableHeight = ViewSettings.getView_heightTB_visible()
        		- ViewSettings.VIEW_LOCATION_TB.y
        		- 2 * (2 + 2) * ViewSettings.getDistanceBetweenItems();
        
        
        
        final int amountCols = 3;
        final int amountOfItemsC1 = 7;
        
        //first column
        jlbl_title.setBounds(
                distance, 
                distance, 
                _width / (amountCols) / 2, 
                heightJLabel2);

        jpnl_owner.setLocation(
                jlbl_title.getX(), 
                jlbl_title.getY() + jlbl_title.getHeight() + distance);
        jpnl_owner.setSize(
        		_width / (amountCols) / amountOfItemsC1, 
                availableHeight - jpnl_owner.getY());

        jpnl_items.setLocation(0, 0);
        jpnl_items.setSize(
                jpnl_owner.getWidth() - distance * 2 - twentyFife,
                jpnl_owner.getHeight());

        
        
        //second column
        jlbl_amountOfItems.setBounds(
                jlbl_title.getX() + jlbl_title.getWidth() + distance,
                jlbl_title.getY(), 
                _width / amountOfItemsC1, 
                heightJLabel);
		

		jlbl_detailedPosition.setLocation(jpnl_owner.getX() + distance
		        + jpnl_owner.getWidth(), jpnl_owner.getY());
		jlbl_detailedPosition.setSize(
		        (availableHeight - jlbl_detailedPosition.getY()) 
		        * Status.getImageSize().width / Status.getImageSize().height, 
		        availableHeight - jlbl_detailedPosition.getY());

        sp_up.setLocation(jpnl_owner.getWidth() - twentyFife, 0);
        sp_up.setSize(twentyFife,
        		availableHeight - sp_up.getY());
        
				
		jpnl_container.setLocation(
		        jlbl_detailedPosition.getWidth() 
		        + jlbl_detailedPosition.getX() + distance, 
		        jpnl_owner.getY());
		jpnl_container.setSize(
		        2 * _width / amountOfItemsC1, 
		        availableHeight
		        - jpnl_container.getY());
		   
		//initialize values
		cps.setRec_old(new Rectangle(
		        distance, -heightNewComponent + 1,
		        jpnl_items.getWidth(), 
		        heightNewComponent));
		
		
		i1b_console.setLocation(jpnl_container.getX() 
				+ jpnl_container.getWidth()
				+ ViewSettings.getDistanceBeforeLine(),
				ViewSettings.getDistanceBeforeLine());
		i1b_console.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());

		i1b_diagramView.setLocation(i1b_console.getX() 
				+ i1b_console.getWidth()
				+ ViewSettings.getDistanceBeforeLine(),
				ViewSettings.getDistanceBeforeLine());
		i1b_diagramView.setSize(i1b_console.getWidth(), 
				i1b_console.getHeight());
		
		i1b_generateLog.setLocation(i1b_diagramView.getX() 
				+ i1b_diagramView.getWidth()
				+ ViewSettings.getDistanceBeforeLine(),
				ViewSettings.getDistanceBeforeLine());
		i1b_generateLog.setSize(i1b_console.getWidth(), 
				i1b_console.getHeight());
		
		i1b_reportBug.setLocation(i1b_generateLog.getX() 
				+ i1b_generateLog.getWidth()
				+ ViewSettings.getDistanceBeforeLine(),
				ViewSettings.getDistanceBeforeLine());
		i1b_reportBug.setSize(i1b_console.getWidth(), 
				i1b_console.getHeight());
		
        Print.initializeTextButtonOhneAdd(i1b_console,
        		TextFactory.getInstance().getView_debug_console(),
                Constants.VIEW_TB_NEW_PATH);

        Print.initializeTextButtonOhneAdd(i1b_diagramView,
        		TextFactory.getInstance().getView_debug_generateViewDiagram(),
                Constants.VIEW_TB_NEW_PATH);
        Print.initializeTextButtonOhneAdd(i1b_generateLog,
        		TextFactory.getInstance().getView_debug_generateLog(),
                Constants.VIEW_TB_NEW_PATH);
        Print.initializeTextButtonOhneAdd(i1b_reportBug,
        		TextFactory.getInstance().getView_debug_reportBug(),
                Constants.VIEW_TB_NEW_PATH);

        i1b_console.setActivable(false);
        i1b_diagramView.setActivable(false);
        i1b_generateLog.setActivable(false);
        i1b_reportBug.setActivable(false);
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized void update(final Observable _obs, final Object _obj) {
		
	    
	    if (!(_obs instanceof PictureOverview)) {
	        new Exception("error: update called with wrong parameters")
	        .printStackTrace();
	        return;
	    }
	    
	    
	    
	    switch (Integer.parseInt(_obj + "")) {
	    
	    case ID_ADD_ITEM:
	    	System.out.println(getClass() + "update add " 
	    			+  ((PictureOverview) _obs).getCurrentPO().getElementId());
            cps.updateAdd((PictureOverview) _obs);
	        break;
	    case ID_REMOVE_ITEM:
	    	System.out.println(getClass() + "update remove " 
	    			+  ((PictureOverview) _obs).getCurrentPO().getElementId());
            cps.updateRemove((PictureOverview) _obs);
	        break;
	    case ID_ADD_ITEM_SELECTED:

	    	//TODO:
	    	System.out.println(getClass() + "update add selected " 
	    			+  ((PictureOverview) _obs).getCurrentPO().getElementId());
            cps.updateAddSelected(
                    (PictureOverview) _obs);
	        break;
	    case ID_REMOVE_ITEM_SELECTED:

	    	if (((PictureOverview) _obs).getCurrentPO() == null) {

		    	System.out.println(getClass() 
		    			+ "update remove selected id null, group selected?");
	    	} else {
		    	System.out.println(getClass() + "update remove selected " 
		    			+  ((PictureOverview) _obs)
		    			.getCurrentPO().getElementId());
	    	}
            cps.updateRemoveSelected(
                    (PictureOverview) _obs);
	        break;
        default:
            Status.getLogger().warning("unknown case!");
            break;
	        
	    }
	    //print list for testing purpose.
//	    Picture.getInstance().getLs_po_sortedByX().printAddcounter();
//	    System.out.println(getClass() + "sel");
//	    if (Picture.getInstance().getLs_poSelected() != null)
//	    Picture.getInstance().getLs_poSelected().printAddcounter();
	    repaint();
	}
	
	


    /**
     * @return the jta_infoSelectedPanel
     */
    public JTextArea getJta_infoSelectedPanel() {
        return jta_infoSelectedPanel;
    }


    /**
     * @return the jlbl_detailedPosition
     */
    public MLabel getJlbl_detailedPosition() {
        return jlbl_detailedPosition;
    }


    /**
     * @return the jlbl_title
     */
    public MLabel getJlbl_title() {
        return jlbl_title;
    }


    /**
     * @return the jlbl_amountOfItems
     */
    public MLabel getJlbl_amountOfItems() {
        return jlbl_amountOfItems;
    }


    /**
     * @return the jpnl_items
     */
    public MPanel getJpnl_items() {
        return jpnl_items;
    }


	/**
	 * @return the i1b_console
	 */
	public Item1Button getI1b_console() {
		return i1b_console;
	}


	/**
	 * @param _i1b_console the i1b_console to set
	 */
	public void setI1b_console(final Item1Button _i1b_console) {
		this.i1b_console = _i1b_console;
	}
}
