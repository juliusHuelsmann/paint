//package declaration
package view.forms;

//import declarations
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.objects.PictureOverview;
import model.objects.painting.PaintObject;
import model.objects.painting.PaintObjectWriting;
import model.objects.painting.Picture;
import model.objects.pen.Pen;
import model.util.list.List;
import control.singleton.MousePositionTracker;
import settings.Constants;
import settings.Status;
import settings.ViewSettings;
import view.ViewVorschau;
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
	 * ID for remving one item.
	 */
	public static final int ID_REMOVE_ITEM = 2;
	/**
	 * Contains the amount of items contained in JPanel, title and the 
	 * detailed position in an image of the view.
	 */
	private JLabel jlbl_amountOfItems, jlbl_title, jlbl_detailedPosition;
	
	//Items shown in in ScrollPanel
	
	/**
	 * Contains the items .
	 */
	private JPanel jpnl_items, jpnl_owner;
	
	/**
	 * ScrollPanel for items.
	 */
	private VScrollPane sp_up;

	//value for functionality of adding something
	
	/**
	 * Point contains the coordinates of the last inserted item.
	 */
	private Rectangle rec_old;
	
	//JTextArea
	
	/**
	 * JPanel which contains the JScrollPane of the JTextArea.
	 */
	private JPanel jpnl_container;

	/**
	 * JTextArea containing information on the selected item.
	 */
	private JTextArea jta_infoSelectedPanel;

	
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
			((Item1Button) jpnl_items.getComponent(i)).setActivated(false);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public Component add(final Component _component) {

	    final int distance = 5;
	    
		//update the rectangle
		rec_old.y += rec_old.getHeight();
		
		//set bounds of c
		_component.setSize(rec_old.width, rec_old.height);
		_component.setLocation(rec_old.x, rec_old.y);
		
		//update size of JPanel for items.
		jpnl_items.setSize(rec_old.width + distance,
		        rec_old.height + rec_old.y + distance);

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
		this.rec_old = new Rectangle(distance, -heightNewComponent + 1,
		        jpnl_owner.getWidth() - widthScrollPane - (2 + 1) * distance, 
		        heightNewComponent);
		
		
		
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
	    
	    //if transmitted operation is to add an item
	    if (Integer.parseInt(_obj + "") == ID_ADD_ITEM) {
		    
	        this.updateAdd((PictureOverview) _obs);
		    
		} else if (Integer.parseInt(_obj + "") == ID_REMOVE_ITEM) {
		    updateRemove((PictureOverview) _obs);
		}
	}
	
	
	
	
	
	/**
	 * add a new PaintObject to the graphical user interface.
	 * @param _pov the PictureOverview
	 */
	private void updateAdd(final PictureOverview _pov) {

	    //TODO: externalize the ActionListener.

        final int itemSize = 40;
        
        //update the amount of items
        jlbl_amountOfItems.setText("amount = " 
        + _pov.getNumber());

        //TODO: list is not working if .
        final Rectangle r = _pov
                .getCurrentPO().getSnapshotBounds();
        final PaintObject po_cu = _pov
                .getCurrentPO();
        
        //create new button for the item
        Item1Button jbtn_new = new Item1Button(null);
        jbtn_new.setAdditionalInformation(po_cu);
        jbtn_new.setImageWidth(itemSize);
        jbtn_new.setImageHeight(itemSize);
        jbtn_new.setBorder(true);
        
        
        jbtn_new.addActionListener(new ActionListener() {
            
            /**
             * actionListener which selects the paintItem the user clicked 
             * on.
             * 
             * @param _event the actionEvent
             */
            @Override public void actionPerformed(
                    final ActionEvent _event) {

                
                Picture.getInstance().releaseSelected();
                Page.getInstance().releaseSelected();
                deactivate();

                String text = "no information found.";
                
                //stuff for paintObjectWriting
                if (po_cu instanceof PaintObjectWriting) {
                    
                    PaintObjectWriting pow = (PaintObjectWriting) po_cu;
                    Pen pe = pow.getPen();
                    final List<Point> ls_point = pow.getPoints();
                    text = "Stift  " + pe.getClass().getSimpleName()
                            + " \nArt   " + pe.getID()
                            + "\nStaerke    " + pe.getThickness()
                            + "\nFarbe  (" + pe.getClr_foreground().getRed()
                            + ", " + pe.getClr_foreground().getGreen()
                            + ", " + pe.getClr_foreground().getBlue()
                            + ")\nBounds    " + r.x + "." + r.y + ";" 
                            + r.width + "." + r.height + "\nimageSize  "
                            + Status.getImageSize().width + "." 
                            + Status.getImageSize().height 
                            + "\nPoints";
                    ls_point.toFirst();
                    int currentLine = 0;
                    while (!pow.getPoints().isBehind()) {
                        
                         currentLine++;
                         
                         //each second line a line break;
                         if (currentLine % 2 == 1) {
                             text += "\n";
                         }
                         
                         text += ls_point.getItem().x 
                                 + " "
                                 + ls_point.getItem().y + " | ";
                        ls_point.next();
                     }
                }
                
                jta_infoSelectedPanel.setText(text);
                
                //create bufferedImage
                BufferedImage bi = new BufferedImage(
                        jlbl_detailedPosition.getWidth(), 
                        jlbl_detailedPosition.getHeight(), 
                        BufferedImage.TYPE_INT_ARGB);

                
                //fetch rectangle
                int x = r.x * bi.getWidth() 
                        / Status.getImageSize().width;
                int y = r.y * bi.getHeight() 
                        / Status.getImageSize().height;
                int width = r.width * bi.getWidth() 
                        / Status.getImageSize().width;
                int height = r.height * bi.getHeight() 
                        / Status.getImageSize().height;

                int border = 2;
                int highlightX = x - border;
                int highlightY = y - border;
                int highlightWidth = width + 2 * border;
                int highlightHeight = height + 2 * border;

                //paint rectangle and initialize with alpha
                for (int coorX = 0; coorX < bi.getWidth(); coorX++) {
                    for (int coorY = 0; coorY < bi.getHeight(); coorY++) {
                        
                        if (coorX >= x && coorY >= y && x + width >= coorX
                                && y + height >= coorY) {
                            bi.setRGB(coorX, coorY, Color.black.getRGB());
                        } else if (coorX >= highlightX 
                                && coorY >= highlightY 
                                && highlightX + highlightWidth >= coorX
                                && highlightY + highlightHeight >= coorY) {

                            bi.setRGB(coorX, coorY, Color.gray.getRGB());
                        } else {

                            bi.setRGB(coorX, coorY, 
                                    new Color(0, 0, 0, 0).getRGB());    
                        }
                    }
                }
                jlbl_detailedPosition.setIcon(new ImageIcon(bi));


                Status.setIndexOperation(
                        Constants.CONTROL_PAINTING_INDEX_MOVE);
                
                //decativate other menuitems and activate the current one
                //(move)
                Picture.getInstance().createSelected();
                Picture.getInstance().insertIntoSelected(po_cu);
                PictureOverview.getInstance().remove(po_cu);
                Picture.getInstance().getLs_po_sortedByX().remove();
                
                Picture.getInstance().paintSelected();
                Page.getInstance().getJlbl_painting().refreshPaint();
                Page.getInstance().getJlbl_painting()
                .paintEntireSelectionRect(po_cu.getSnapshotBounds());
                
                repaint();
            }
        });
        
        jbtn_new.setText(
                "ID " + _pov.getCurrentPO()
                .getElementId());
        add(jbtn_new);
        jbtn_new.setIcon(
                (_pov.getCurrentPO().getSnapshot()));

        repaint();
    
	}
	

    /**
     * remove a PaintObject from the graphical user interface.
     * @param _pov the PictureOverview
     */
    private void updateRemove(final PictureOverview _pov) {

        Component [] comp = jpnl_items.getComponents();
        for (int i = 0; i < comp.length; i++) {
            if (comp[i] instanceof Item1Button) {
                
                Item1Button i1b = (Item1Button) comp[i];
                if (i1b.getAdditionalInformation() != null
                        && i1b.getAdditionalInformation() 
                        instanceof PaintObject) {
                    
                    
                    PaintObject po = 
                            (PaintObject) i1b.getAdditionalInformation();
                    
                    if (po.equals(_pov.getCurrentPO())) {
                        jpnl_items.remove(comp[i]);

                        rec_old.y -= rec_old.getHeight();
                    }
                    
                }
            }
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
}
