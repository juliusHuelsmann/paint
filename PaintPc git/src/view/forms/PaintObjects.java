//package declaration
package view.forms;

//import declarations
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.objects.PictureOverview;
import control.singleton.CPaintObjects;
import control.singleton.MousePositionTracker;
import settings.Status;
import settings.ViewSettings;
import view.util.VScrollPane;
import view.util.Item1Button;

/**
 * JPanel which shows the list of PaintObjects and details about them.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class PaintObjects extends JPanel implements Observer {

	/**
	 * the only instance of this class.
	 */
	private static PaintObjects instance = null;

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
     * until it loses focus and may be readded to the normal list of items.
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
	private JLabel jlbl_amountOfItems, jlbl_title, jlbl_detailedPosition;
	
	//Items shown in in ScrollPanel
	
	/**
	 * Contains the items .
	 */
	private final JPanel jpnl_items, jpnl_owner;
	
	/**
	 * ScrollPanel for items.
	 */
	private final VScrollPane sp_up;
	
	/**
	 * JPanel which contains the JScrollPane of the JTextArea.
	 */
	private final JPanel jpnl_container;

	/**
	 * JTextArea containing information on the selected item.
	 */
	private final JTextArea jta_infoSelectedPanel;

    //value for functionality of adding something
    
	
	/**
	 * Constructor: initialize instances of Components and add special 
	 * MouseMotionListener.
	 */
	private PaintObjects() {
		
		//initialize JPanel and alter settings
		super();
		super.setLayout(null);
		super.setOpaque(true);
		super.setBackground(ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT);
		MousePositionTracker mpt = new MousePositionTracker(this);
		super.addMouseListener(mpt);
		super.addMouseMotionListener(mpt);
		super.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		//final rather unimportant values
		final int tabSize = 10;
		
		//title
		jlbl_title = new JLabel("Paint Objects");
		jlbl_title.setFocusable(false);
		jlbl_title.setBorder(null);
		jlbl_title.setFont(ViewSettings.GENERAL_FONT_HEADLINE);
		super.add(jlbl_title);
		
		//owner of items panel and items panel
		jpnl_owner = new JPanel();
		jpnl_owner.setLayout(null);
		jpnl_owner.setVisible(true);
		jpnl_owner.setBorder(null);
		jpnl_owner.setOpaque(false);
		super.add(jpnl_owner);

		jpnl_items = new JPanel();
		jpnl_items.setLayout(null);
		jpnl_items.setVisible(true);
		jpnl_items.setBorder(null);
		jpnl_items.setOpaque(false);
		jpnl_owner.add(jpnl_items);

		//JLabel for amount of items
		jlbl_amountOfItems = new JLabel();
		jlbl_amountOfItems.setFocusable(false);
		jlbl_amountOfItems.setBorder(null);
		jlbl_amountOfItems.setFont(ViewSettings.GENERAL_FONT_ITEM);
		super.add(jlbl_amountOfItems);

        //
        sp_up = new VScrollPane(jpnl_items, jpnl_owner, true);
        sp_up.setOpaque(false);
        jpnl_owner.add(sp_up);
    
		//JTextArea, container and ScrollPane
		jpnl_container = new JPanel();
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
		
		jlbl_detailedPosition = new JLabel();
		jlbl_detailedPosition.setFocusable(false);
		jlbl_detailedPosition.setBorder(
		        BorderFactory.createLineBorder(Color.black));
		super.add(jlbl_detailedPosition);
		
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
		CPaintObjects.getInstance().getRec_old().y += 
		        CPaintObjects.getInstance().getRec_old().getHeight();
		
		//set bounds of c
		_component.setSize(CPaintObjects.getInstance().getRec_old().width, 
		        CPaintObjects.getInstance().getRec_old().height);
		_component.setLocation(CPaintObjects.getInstance().getRec_old().x, 
		        CPaintObjects.getInstance().getRec_old().y);
		
		//update size of JPanel for items.
		jpnl_items.setSize(
		        CPaintObjects.getInstance().getRec_old().width + distance,
		        CPaintObjects.getInstance().getRec_old().height 
		        + CPaintObjects.getInstance().getRec_old().y + distance);

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
		final int widthScrollPane = 15;
		final int distance = 5;
		final int thing = 300;
		final int widht_Map = _width / 2;
		
		//set size of super and owner
		final int iconSize = 16;
		final int twentyFife = 25;
		final int hundred = 100;
		
		super.setSize(_width, _height);
        sp_up.setIcon_size(iconSize);
		jlbl_title.setBounds(distance, distance, _width - 2 * distance, 
		        heightJLabel2);
		jpnl_owner.setLocation(distance, distance + jlbl_title.getY()
		        + jlbl_title.getHeight());
		jpnl_owner.setSize(_width, _height - heightJLabel2 - distance 
		        * (2 + 2) - thing);
		jpnl_items.setLocation(0, 0);
		jpnl_items.setSize(jpnl_owner.getWidth() - distance * 2, hundred);
		

		jlbl_detailedPosition.setBounds(jpnl_owner.getX(), jpnl_owner.getY() 
		        + jpnl_owner.getHeight() + distance, widht_Map , 
		        Status.getImageSize().height * widht_Map 
		        / Status.getImageSize().width);
		jlbl_amountOfItems.setBounds(distance, _height - distance 
		        - heightJLabel, _width - 2 * distance, heightJLabel);
		
		sp_up.setLocation(_width - widthScrollPane - 2 * distance, 2 + 2 + 1);
		sp_up.setSize(twentyFife, jpnl_owner.getHeight());
				
		jpnl_container.setLocation(distance, jlbl_detailedPosition.getHeight()
		        + jlbl_detailedPosition.getY() + distance);
		jpnl_container.setSize(_width - 2 * distance, _height 
		        - jpnl_container.getY() - 2 * distance - heightJLabel);
		   
		//initialize values
		CPaintObjects.getInstance().setRec_old(new Rectangle(
		        distance, -heightNewComponent + 1,
		        jpnl_owner.getWidth() - widthScrollPane - (2 + 1) * distance, 
		        heightNewComponent));
		
		
		
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public void update(final Observable _obs, final Object _obj) {
		
	    
	    if (!(_obs instanceof PictureOverview)) {
	        new Exception("error: update called with wrong parameters")
	        .printStackTrace();
	        return;
	    }
	    
	    
	    
	    switch (Integer.parseInt(_obj + "")) {
	    
	    case ID_ADD_ITEM:
            CPaintObjects.getInstance().updateAdd((PictureOverview) _obs);
	        break;
	    case ID_REMOVE_ITEM:
            CPaintObjects.getInstance().updateRemove((PictureOverview) _obs);
	        break;
	    case ID_ADD_ITEM_SELECTED:
            CPaintObjects.getInstance().updateAddSelected(
                    (PictureOverview) _obs);
	        break;
	    case ID_REMOVE_ITEM_SELECTED:
            CPaintObjects.getInstance().updateRemoveSelected(
                    (PictureOverview) _obs);
	        break;
        default:
            Status.getLogger().warning("unknown case!");
            break;
	        
	    }
	    
	}
	
	
	
	
	/**
	 * this method guarantees that only one instance of this
	 * class can be created ad runtime.
	 * 
	 * @return the only instance of this class.
	 */
	public static PaintObjects getInstance() {
		
		//if class is not instanced yet instantiate
		if (instance == null) {
			instance = new PaintObjects();
		}
		
		//return the only instance of this class.
		return instance;
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
    public JLabel getJlbl_detailedPosition() {
        return jlbl_detailedPosition;
    }


    /**
     * @return the jlbl_title
     */
    public JLabel getJlbl_title() {
        return jlbl_title;
    }


    /**
     * @return the jlbl_amountOfItems
     */
    public JLabel getJlbl_amountOfItems() {
        return jlbl_amountOfItems;
    }


    /**
     * @return the jpnl_items
     */
    public JPanel getJpnl_items() {
        return jpnl_items;
    }
}
