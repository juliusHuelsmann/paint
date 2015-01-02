//package declaration
package view.util;

//import declarations
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import view.forms.Page;
import view.forms.Tabs;
import view.util.mega.MButton;
import view.util.mega.MPanel;
import control.tabs.CPaintStatus;
import control.util.CItem;
import model.objects.painting.Picture;
import model.settings.ViewSettings;
import model.util.adt.list.List;
import model.util.paint.Utils;

/**
 * 
 * @author Julius Huelsmann 
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Item1Menu extends MPanel {

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
	 * MButton for selection and for selecting.
	 */
	private Item1Button tb_select;
	
	
	/**
	 * MButton for selection and for selecting and opening.
	 */
	private VButtonWrapper tb_open;

	/**
	 * MPanel which contains stuff.
	 */
	private MPanel jpnl_stuff, jpnl_container, jpnl_subContainer;
	
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
	private int closedWidth = ViewSettings.getItemMenu1Width(), 
	        closedHeight = ViewSettings.getItemMenu1Height();
	
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
	 * @param _openSelectOneButton whether to open and to select at the same 
	 * button
	 */
	public Item1Menu(final boolean _openSelectOneButton) {
		
		//initialize MPanel and alter settings
		super();
		super.setLayout(null);
		super.setOpaque(false);
		super.setBorder(null);
		super.setBackground(ViewSettings.GENERAL_CLR_BACKGROUND_LIGHT);
		super.setFocusable(false);

		jpnl_stuff = new MPanel();
		ls_item = new List<Component>();
		jpnl_container = new MPanel();
		jpnl_subContainer = new MPanel();

		sp_scroll = new VScrollPane(jpnl_stuff, jpnl_container, true);
		sp_scroll.setOpaque(true);
		super.add(sp_scroll);


		//initialize MButton
		tb_select = new Item1Button(this);
		tb_select.setActivable(false);
		tb_select.addMouseListener(CPaintStatus.getInstance());
		tb_select.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		tb_select.setFocusable(false);
		if (_openSelectOneButton) {

		    tb_select.addMouseListener(CItem.getInstance());
		}
		tb_select.setOpaque(true);
		super.add(tb_select);

		if (!_openSelectOneButton) {

	        //initialize MButton
	        tb_open = new VButtonWrapper(this);
	        tb_open.setContentAreaFilled(false);
	        tb_open.addMouseListener(CItem.getInstance());
	        tb_open.setBorder(null);
	        tb_open.setFocusable(false);
	        tb_open.setOpaque(false);
	        super.add(tb_open);
		}
		
		jpnl_container.setLayout(null);
		jpnl_container.setOpaque(false);
		jpnl_container.setBorder(null);
		jpnl_container.setFocusable(false);
		super.add(jpnl_container);

        
        jpnl_subContainer.setLayout(null);
        jpnl_subContainer.setOpaque(true);
        jpnl_subContainer.setBorder(null);
        jpnl_subContainer.setBackground(
                ViewSettings.CLR_BACKGROUND_MENU_1_OPEN);
        jpnl_subContainer.setFocusable(false);
        jpnl_container.add(jpnl_subContainer);
        
		jpnl_stuff.setLayout(null);
		jpnl_stuff.setOpaque(true);
		jpnl_stuff.setBorder(null);
		jpnl_stuff.setBackground(ViewSettings.CLR_BACKGROUND_MENU_1_OPEN);
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

    	
    	if (open != _open) {

	    	//release selected because of display bug otherwise.
	    	if (Picture.getInstance().isSelected()) {
		    	Picture.getInstance().releaseSelected();
		    	Page.getInstance().releaseSelected();
	    	}
	    	
        	//open or close
            open = _open;
            if (_open) {
                setSize(openedWidth, openedHeight);

                jpnl_container.setBorder(BorderFactory.createLineBorder(
                        ViewSettings.GENERAL_CLR_BORDER));

                jpnl_container.requestFocus();
                
            } else {
                setSize(closedWidth, closedHeight);
                jpnl_container.setBorder(null);
                
                //when closed repaint.
                Page.getInstance().getJlbl_painting().repaint();
                Tabs.getInstance().repaint();
            }
    	}
    }
    
    /**
     * closes without repaint of paintLabel.
     */
    public final void closeSilently() {

        open = false;
        
        setSize(closedWidth, closedHeight);
        jpnl_container.setBorder(null);
    }

	/**
	 * set border.
	 * @param _border whether has border or not.
	 */
	public final void setBorder(final boolean _border) {
		tb_select.setBorder(null);
		tb_select.setBorder(_border);
        jpnl_stuff.setBorder(null);
        jpnl_container.setBorder(null);
	}
	
	
    
    /**
     * add a MPanel to item1menu.
     * @param _jpnl the MPanel
     * @return the MPanel
     */
	public final Component add(final MPanel _jpnl) {

		_jpnl.setOpaque(false);
		return addd(_jpnl);
	}
    
    /**
     * add a MButton to item1menu.
     * @param _jbtn the MButton
     * @return the MButton
     */
	public final Component add(final MButton _jbtn) {

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
	 * resize.
	 */
	public final void flip() {
	    open = false;
	    closedWidth = ViewSettings.getItemMenu1Width();
	    closedHeight = ViewSettings.getItemMenu1Height();
	    setSize(closedWidth, closedHeight);
	    tb_select.flip();
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
           
            
            if (tb_open != null) {
                tb_select.setSize(closedWidth, closedHeight * (2 + 2) 
                        / (2 + 2 + 1));
                tb_open.setLocation(tb_select.getX(), 
                        tb_select.getY() + tb_select.getHeight());
                tb_open.setSize(closedWidth, closedHeight / (2 + 2 + 1));
                
                final int height = tb_open.getHeight() / (2 + 1);
                final int imageWidth = 60;
                final int imageHeight = 42;
                tb_open.setIcon(new ImageIcon(Utils.resizeImage(
                        height * imageWidth / imageHeight, 
                        height, "open.png")));
            } else {

                tb_select.setSize(closedWidth, closedHeight);
            }
            
			
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
				int size = getWidth() / itemsInRow;
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
							        size - x , sizeHeight);
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
        tb_select.setIcon(_text);
        imagePath = _text;
    }
    
    /**
     * set activable.
     */
    public final void setActivable() {

        tb_select.setActivable(true);
    }
    
    /**
     * set text of item opener.
     * @param _text the text
     */
    public final void setText(final String _text) {
        tb_select.setText(_text);
    }
    
    /**
     * disable icon.
     */
    public final void setNullIcon() {
        imagePath = "";
        tb_select.setNullIcon();
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
     * @return the MPanel jpnl_stuff.
     */
    public final MPanel getMPanel() {
        return jpnl_stuff;
    }
	


	/**
	 * getter.
	 * @return the tb_open
	 */
	public final Item1Button getTb_open() {
		return tb_select;
	}


	
	/**
	 * setter.
	 * @param _tb_open the ITem1Button
	 */
	public final void setTb_open(final Item1Button _tb_open) {
		this.tb_select = _tb_open;
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


    /**
     * Apply stroke.
     */
	public final void stroke() {
		tb_select.stroke();		
	}
}
