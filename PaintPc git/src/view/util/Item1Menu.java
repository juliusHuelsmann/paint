//package declaration
package view.util;

//import declarations
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import settings.ViewSettings;
import view.forms.Page;
import control.singleton.CItem;
import control.singleton.ControlSelectionColorPen;
import model.util.list.List;

/**
 * 
 * @author Julius Huelsmann 
 * @version %I%, %U%
 *
 */
@SuppressWarnings("serial")
public class Item1Menu extends JPanel {

	/**
	 * final value for items placed in one row.
	 */
	private byte itemsInRow = 2 + 1;
	/**
	 * list of contents.
	 */
	private List<Component> ls_item;

	/**
	 * boolean for whether inserting line by line or spalte by spalte.
	 */
	private boolean orderHeight = false;
	
	/**
	 * JButton for opening stuff.
	 */
	private Item1Button tb_open;

	/**
	 * JPanel which contains stuff.
	 */
	private JPanel jpnl_stuff, jpnl_container, jpnl_subContainer;
	
	/**
	 * the size.
	 */
	private int sizeHeight = -1;
	
	/**
	 * size if opened.
	 */
	private int openedWidth, openedHeight;
	
	/**
	 * size if closed.
	 */
	private int closedWidth = ViewSettings.ITEM_MENU1_WIDTH, 
	        closedHeight = ViewSettings.ITEM_MENU1_HEIGHT;
	
	/**
	 * The ScrollPane for the menu items.
	 */
	private VScrollPane sp_scroll;
	
	/**
	 * the path of the image.
	 */
	private String imagePath = "st2.png";
	
	/**
	 * whether the menu is opened or not.
	 */
	private boolean open = false;
	
	/**
	 * Constructor: shows closed item.
	 */
	public Item1Menu() {
		
		//initialize JPanel and alter settings
		super();
		super.setLayout(null);
		super.setOpaque(false);
		super.setBorder(null);
		super.setBackground(ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT);
		super.setFocusable(false);

		jpnl_stuff = new JPanel();
		ls_item = new List<Component>();
		jpnl_container = new JPanel();
		jpnl_subContainer = new JPanel();

		sp_scroll = new VScrollPane(jpnl_stuff, jpnl_container, true);
		sp_scroll.setOpaque(true);
		super.add(sp_scroll);


		//initialize JButton
		tb_open = new Item1Button(this);
		tb_open.setActivable(false);
		tb_open.addMouseListener(ControlSelectionColorPen.getInstance());
        tb_open.addMouseListener(CItem.getInstance());
		tb_open.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		tb_open.setFocusable(false);
		tb_open.setOpaque(true);
		super.add(tb_open);

        

		
		jpnl_container.setLayout(null);
		jpnl_container.setOpaque(false);
		jpnl_container.setForeground(Color.green);
		jpnl_container.setBorder(null);
		jpnl_container.setBackground(ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT);
		jpnl_container.setFocusable(false);
		super.add(jpnl_container);

        
        jpnl_subContainer.setLayout(null);
        jpnl_subContainer.setOpaque(true);
        jpnl_subContainer.setForeground(Color.green);
        jpnl_subContainer.setBorder(null);
        jpnl_subContainer.setBackground(
                ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT);
        jpnl_subContainer.setFocusable(false);
        jpnl_container.add(jpnl_subContainer);
        
		jpnl_stuff.setLayout(null);
		jpnl_stuff.setOpaque(true);
		jpnl_stuff.setForeground(Color.green);
		jpnl_stuff.setBorder(null);
		jpnl_stuff.setBackground(ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT);
		jpnl_stuff.setFocusable(false);
		jpnl_subContainer.add(jpnl_stuff);
        
	}
	
	
	/**
	 * remove scrollPanel if not necessary.
	 */
	public final void removeScroll() {
	    super.remove(sp_scroll);
	}
	
	/**
	 * getter method.
	 * @return whether open or not.
	 */
	public final boolean isOpen() {
		return open;
	}
	
	/**
	 * set open.
	 * @param _open whether open or  not.
	 */
	public final void setOpen(final boolean _open) {

	    open = _open;
        if (_open) {
            setSize(openedWidth, openedHeight);

            jpnl_container.setBorder(BorderFactory.createLineBorder(
                    ViewSettings.CLR_BORDER));

            jpnl_container.requestFocus();
            
        } else {
            setSize(closedWidth, closedHeight);
            jpnl_container.setBorder(null);
            
            //when closed repaint.
            Page.getInstance().getJlbl_painting().repaint();
        }
	}

	/**
	 * set border.
	 * @param _border whether has border or not.
	 */
	public final void setBorder(final boolean _border) {
		tb_open.setBorder(null);
		tb_open.setBorder(_border);
        jpnl_stuff.setBorder(null);
        jpnl_container.setBorder(null);
	}
	
	
    
    /**
     * add a JPanel to item1menu.
     * @param _jpnl the JPanel
     * @return the JPanel
     */
	public final Component add(final JPanel _jpnl) {

		_jpnl.setOpaque(false);
		return addd(_jpnl);
	}
    
    /**
     * add a JButton to item1menu.
     * @param _jbtn the JButton
     * @return the JButton
     */
	public final Component add(final JButton _jbtn) {

		_jbtn.setContentAreaFilled(false);
		_jbtn.setOpaque(false);
		_jbtn.setBorder(BorderFactory.createLineBorder(Color.gray));
		return addd(_jbtn);
	}
	
	/**
	 * add a check box to item1menu.
	 * @param _jcb the checkBox
	 * @return the checkBox
	 */
	public final Component add(final JCheckBox _jcb) {

		_jcb.setContentAreaFilled(false);
		_jcb.setOpaque(false);
		_jcb.setBorder(BorderFactory.createLineBorder(Color.gray));
		return addd(_jcb);
	}
	
	
	
	/**
	 * add a component.
	 * @param _cmp the component which is to be added
	 * @return the Component.
	 */
	private Component addd(final Component _cmp) {

		int size = openedWidth / itemsInRow;
		
		if (orderHeight) {
			size = openedHeight / itemsInRow;
		}
		ls_item.toLast();
		int lastPosition;
		if (ls_item.isEmpty()) {
			lastPosition = -1;
		} else {
			int height = sizeHeight;
			if (height < 0) {
				height = size;
			}
			if (orderHeight) {
				lastPosition = ls_item.getItem().getY() / size 
				        + ls_item.getItem().getX() / height * itemsInRow;
			} else {
				lastPosition = ls_item.getItem().getX() / size 
				        + ls_item.getItem().getY() / height * itemsInRow;
			}
		}
		int currentPosition = lastPosition + 1;
		
		int x = currentPosition % itemsInRow;
		int y = currentPosition / itemsInRow;
		
		if (orderHeight) {
			int swap = x;
			x = y;
			y = swap;
		}
		//calculate 
		
		if (sizeHeight != -1) {
			_cmp.setSize(size, sizeHeight);
		} else {
		    _cmp.setSize(size, size);
		}

		if (sizeHeight != -1) {
            _cmp.setLocation(x * size, y * sizeHeight);
		    
		} else {
	        _cmp.setLocation(x * size, y * size);
		}
		
		jpnl_stuff.add(_cmp);
		

		ls_item.insertAtTheEnd(_cmp);
		
		return _cmp;
	}

    @Override public final void setSize(final Dimension _d) {

        openedHeight = _d.height;
        openedWidth = _d.width;
        
        setSize(_d.width, _d.height);
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override public final void setSize(final int _width, final int _height) {
		
		
		if (!isOpen()) {

			
			super.setSize(closedWidth, closedHeight);
			jpnl_stuff.setSize(closedWidth, jpnl_stuff.getHeight());
			tb_open.setSize(closedWidth, closedHeight);
			
		} else {
			
			//save openWidth, openHeight
			openedWidth = _width;
			openedHeight = _height;


				
				/*
				 * set size of stuff
				 */
				super.setSize(_width, _height);
				jpnl_stuff.setBounds(0, 0, 
				        _width - 2, _height - closedHeight - 2);
				jpnl_container.setBounds(0, 0 + closedHeight, 
				        _width, _height - closedHeight - 0);
				jpnl_subContainer.setBounds(1, 1, 
				        _width - 2, _height - closedHeight - 2);
				int size = getWidth() / itemsInRow + 2;
				int currentItem = 0;

				//size of contained stuff
				ls_item.toFirst();
				
				int lastHeight = 0, lastY = 0;
				int x = sp_scroll.getIcon_size();
				while (!ls_item.isBehind() && !ls_item.isEmpty()) {
					
					if (itemsInRow != 1) {
						x = 0;
					}
					
					if (!orderHeight) {
						if (sizeHeight != -1) {
						    
							ls_item.getItem().setLocation(size 
							        * (currentItem % itemsInRow), 1 
							        + sizeHeight * (currentItem / itemsInRow));

                            ls_item.getItem().setSize(
							        size - x, sizeHeight);
						} else {
							ls_item.getItem().setLocation(size * (currentItem 
							        % itemsInRow),
							        size * (currentItem / itemsInRow)); 
							ls_item.getItem().setSize(
							        size - x, size);
						}
					} else {
						if (sizeHeight == -1) {

							ls_item.getItem().setBounds(size
							        * (currentItem / itemsInRow),
							        sizeHeight
							        * (currentItem % itemsInRow), 
							        size - x, sizeHeight);
						}
					}
					
					//fetch old values
					lastHeight = ls_item.getItem().getHeight();
					lastY = ls_item.getItem().getY();
					currentItem++;
					
					//proceed one step
					ls_item.next();
				}
				if (!ls_item.isEmpty()) {

					jpnl_stuff.setSize(_width - x - 2, lastHeight + lastY);

				}
				
				if (_height > 0 && _width > 0) {
					if (jpnl_stuff.getWidth() > 0) {

						ls_item.toFirst();
						sp_scroll.setLocation(_width - sp_scroll.getIcon_size(),
						        closedHeight);
						sp_scroll.setSize(sp_scroll.getIcon_size(), _height 
						        + sp_scroll.getIcon_size() - sp_scroll.getY());
					}
				}
		} 
//        if (imagePath != "") {
//
//            tb_open.setIcon(imagePath);   
//        }
	}

    
	/**
	 * set the icon.
	 * @param _text the path of the icon
	 */
    public final void setIcon(final String _text) {
        tb_open.setIcon(_text);
        imagePath = _text;
    }
    
    /**
     * set activable.
     */
    public final void setActivable() {

        tb_open.setActivable(true);
    }
    
    /**
     * set text of item opener.
     * @param _text the text
     */
    public final void setText(final String _text) {
        tb_open.setText(_text);
    }
    
    /**
     * disable icon.
     */
    public final void setNullIcon() {
        imagePath = "";
        tb_open.setNullIcon();
    }
    
    /**
     * set the amount of items in row.
     * @param _items the amount of items in row.
     */
    public final void setItemsInRow(final byte _items) {
        this.itemsInRow = _items;
    }
    
    /**
     * getter method.
     * @param _orderh the order height
     */
    public final void setOrderHeight(final boolean _orderh) {
        this.orderHeight = _orderh;
    }
    
    /**
     * 
     * getter method.
     * @return the JPanel jpnl_stuff.
     */
    public final JPanel getJPanel() {
        return jpnl_stuff;
    }
	


	/**
	 * getter.
	 * @return the tb_open
	 */
	public final Item1Button getTb_open() {
		return tb_open;
	}


	
	/**
	 * setter.
	 * @param _tb_open the ITem1Button
	 */
	public final void setTb_open(final Item1Button _tb_open) {
		this.tb_open = _tb_open;
	}

    /**
     * @return the imagePath
     */
    public final String getImagePath() {
        return imagePath;
    }

    /**
     * @param _imagePath the imagePath to set
     */
    public final void setImagePath(final String _imagePath) {
        this.imagePath = _imagePath;
    }

    /**
     * @return the sizeHeight
     */
    public final int getSizeHeight() {
        return sizeHeight;
    }

    /**
     * @param _sizeHeight the sizeHeight to set
     */
    public final void setSizeHeight(final int _sizeHeight) {
        this.sizeHeight = _sizeHeight;
    }
}