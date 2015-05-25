//package declaration
package view.util;

//import declaration
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.Util;
import model.util.paint.Utils;
import control.util.VisualTextButton;

/**
 * TextButton which displays both text and icon.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class Item1Button extends MPanel {

	/**
	 * wrapper button which delivers this TextButton to the ActionListener.
	 */
	private VButtonWrapper jbtn_touch;

	/**
	 * TextEnabled contains whether to paint text if image is displayed.
	 * Otherwise (imageWidth == -1 || imageHeight == -1) text is painted
	 * even though textEnabled == false.
	 */
	private boolean textEnabled = false;
	/**
	 * JLabel for text.
	 */
	private MLabel jlbl_title;

    /**
     * JLabel for color or image.
     */
	private MLabel jlbl_color;

	/**
	 * boolean which indicate whether activated or it is possible to activate.
	 */
	private boolean activated, activable, border, disabled;

    /**
     * image width and height.
     */
    private int imageWidth = -1, imageHeight = -1;
    
    
    /**
     * If it's necessary to store additional information in this button
     * (done in PaintObjects view class) this can be done using getter and
     * setter of this object.
     */
    private Object additionalInformation;
	
    
    /**
     * JLabel containing stroke.
     */
    private JLabel jlbl_stroke;
    
	/**
	 * Constructor: save Object.
	 * 
	 * @param _wrap the object which is to be wrapped
	 */
	public Item1Button(final Object _wrap) {
		
		//initialize JPanel and alter settings
		super();
		super.setLayout(null);
		super.setBackground(
				ViewSettings.GENERAL_CLR_ITEM1BUTTON_BACKGROUND_NORMAL);
		super.setOpaque(true);
		
		//set values
		this.activated = false;
		this.activable = true;
		this.border = true;
		this.disabled = false;
		
		//initialize title JLabel
		jlbl_title = new MLabel();
		jlbl_title.setBorder(null);
		jlbl_title.setFocusable(false);
		jlbl_title.setOpaque(false);
		jlbl_title.setFont(ViewSettings.GENERAL_FONT_ITEM1_BUTTON);
		jlbl_title.setForeground(Color.gray);
		jlbl_title.setHorizontalAlignment(SwingConstants.CENTER);
		super.add(jlbl_title);
		
		//initialize JButton

        //if _wrap is null set _wrap to this
        if (_wrap == null) {
            jbtn_touch = new VButtonWrapper(this);
        } else {
            jbtn_touch = new VButtonWrapper(_wrap);
        }
		jbtn_touch.setFocusable(false);
		jbtn_touch.setContentAreaFilled(false);
		jbtn_touch.setOpaque(false);
		jbtn_touch.addMouseListener(VisualTextButton.getInstance());
		jbtn_touch.setBorder(null);
		super.add(jbtn_touch);

		//initialize color JLabel
		jlbl_color = new MLabel();
		jlbl_color.setBorder(null);
		jlbl_color.setOpaque(true);
		jlbl_color.setFocusable(false);
		super.add(jlbl_color);
		
		jlbl_stroke = new JLabel();
		jlbl_stroke.setBorder(null);
		jlbl_stroke.setFocusable(false);
		super.add(jlbl_stroke);
	}
	
	/**
	 * set activated.
	 * @param _activate whether activated or not.
	 */
	public final void setActivated(final boolean _activate) {

		//if able to activate
		if (activable) {

			//update activated flag.
			this.activated = _activate;
			
			//if not activated
			if (!_activate) {
                super.setBackground(
                		ViewSettings.GENERAL_CLR_ITEM1BUTTON_BACKGROUND_NORMAL);
				setOwnBorder(null);
			} else {

				//if activated
				setOwnBackground(
				        ViewSettings.GENERAL_CLR_ITEM1BUTTON_BACKGROUND);
				setOwnBorder(ViewSettings.BRD_ITEM1BUTTON);
				super.repaint();
			}

		}
	}
	
	/**
	 * set whether able to activate.
	 * @param _activable whether able to activate
	 */
	public final void setActivable(final boolean _activable) {
		
		//set whether able to activate
		this.activable = _activable;

		//set deactivated if is not able to activate
		if (!activable) {
			activated = false;
		}
	}
	
	
	
	/**
	 * Highlight the button in special color for informing the user that this
	 * item has not been enabled.
	 */
	public final void disable() {
		this.disabled = true;
		setOwnBackground(
		        ViewSettings.GENERAL_CLR_BACKGROUND_DISABLED);

	}
	
	
	/**
	 * Return whether this instance is disabled.
	 * @return whether this instance is disabled.
	 */
	public final boolean isDisabled() {
		return disabled;
	}
	
	
	
	/**
	 * set null icon.
	 */
	public final void setNullIcon() {
		jlbl_color.setIcon(new ImageIcon(""));
	}
	

	/**
	 * set icon of the JLabel jlbl_color.
	 * @param _i the path of the Icon.
	 */
	public final void setIcon(final String _i) {
		jlbl_color.setOpaque(false);
		jlbl_color.setIcon(new ImageIcon(Utils.resizeImage(
				jlbl_color.getWidth(), jlbl_color.getHeight(), _i)));
	}
	
	
	/**
	 * set icon of the JLabel jlbl_color.
	 * @param _i the path of the Icon.
	 */
	public final void setIcon(final BufferedImage _i) {
		jlbl_color.setOpaque(false);
		if (jlbl_color.getWidth() > 0 && jlbl_color.getHeight() > 0) {

	        jlbl_color.setIcon(new ImageIcon(Utils.resizeImage(
	                jlbl_color.getWidth(), jlbl_color.getHeight(), _i)));
		} 
	}

	/**
	 * return the cause of ActionEvent (jbtn_touch).
	 * @return the jbtn_touch
	 */
	public final VButtonWrapper getActionCause() {
		return jbtn_touch;
	}
	
	/**
	 * set the text of JLabel.
	 * @param _text the text
	 */
	public final void setText(final String _text) {
		jlbl_title.setText(_text);
	}

	/**
	 * return the icon of the JLabel.
	 * @return the icon.
	 */
	public final Icon getIcon() {
		return jlbl_color.getIcon();
	}
    
    /**
     * add actionListener.
     * @param _al the ActionListener.
     */
    public final void addActionListener(final ActionListener _al) {
        jbtn_touch.addActionListener(_al);
    }
    
    /**
     * add mouseListener.
     * @param _ml the mouseListener.
     */
    public final void addMouseListener(final MouseListener _ml) {
        jbtn_touch.addMouseListener(_ml);
    }
	
	/**
	 * set the background of the Color JLabel.
	 * @param _c the color
	 */
	@Override public final void setBackground(final Color _c) {

		//if the JLabel is initialized
		if (jlbl_color != null) {
			jlbl_color.setBackground(_c);
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override public final void setBorder(final Border _border) {
		if (jlbl_color != null && border) {
            jlbl_color.setBorder(_border);   
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public final void setSize(final int _width, final int _height) {
		
		//set super size and the size of the JButton for ActionEvent
		super.setSize(_width, _height);
		jbtn_touch.setSize(getSize());
		jlbl_stroke.setSize(getSize());
		
		flip();
	}
	

	/**
	 * set the y coordinate of Item1Button-Icon JLabel.
	 * @param _y the new coordinate
	 */
	public final void setIconLabelY(final int _y) {
	    jlbl_color.setLocation(jlbl_color.getX(), _y);
	}
	/**
	 * set the x coordinate of Item1Button-Icon JLabel.
	 * @param _x the new coordinate
	 */
	public final void setIconLabelX(final int _x) {
	    jlbl_color.setLocation(_x, jlbl_color.getY());
	}
	
	/**
	 * flip the object.
	 */
	public final void flip() {
	    
	    final int distance = 5;
	    final int colorSize = 30;
	    

	    jlbl_title.setFont(ViewSettings.GENERAL_FONT_ITEM1_BUTTON);
	    //set size of color - and title JLabel
	    if (imageWidth == -1 || imageHeight == -1) {
	    	jlbl_color.setBounds(distance * (2 + 1), distance, 
	    			getWidth() - colorSize, getWidth() - colorSize);

	    	jlbl_title.setBounds(0, jlbl_color.getHeight() 
	    			+ jlbl_color.getY(), getWidth(), getHeight() 
	    			- jlbl_color.getHeight() - jlbl_color.getY());
	    } else if (textEnabled) {

	    	jlbl_title.setBounds(0, (jlbl_color.getHeight() 
	    			+ jlbl_color.getY()) / 2, getWidth(), getHeight() 
	    			- jlbl_color.getHeight() - jlbl_color.getY());
	    }
	}
	
	
	
	/**
	 * enable text.
	 */
	public final void enableText() {
		textEnabled = true;
	}
	
	/**
	 * set the own border.
	 * @param _border the border.
	 */
	public final void setOwnBorder(final Border _border) {
		super.setBorder(_border);
	}
	
	/**
	 * set the background color of this.
	 * @param _c the color.
	 */
	public final void setOwnBackground(final Color _c) {
		super.setBackground(_c);
	}

	/**
	 * simple getter method.
	 * @return whether is activated or not.
	 */
	public final boolean isActivated() {
		return activated;
	}
	
	/**
	 * simple getter method.
	 * @return whether is able to activate or not.
	 */
	public final boolean isActivable() {
		return activable;
	}

	/**
	 * @return the border
	 */
	public final boolean isBorder() {
		return border;
	}

	/**
	 * @param _border the border to set
	 */
	public final void setBorder(final boolean _border) {
		this.border = _border;
	}

    /**
     * @return the imageWidth
     */
    public final int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param _imageWidth the imageWidth to set
     */
    public final void setImageWidth(final int _imageWidth) {
    	if (jlbl_color != null) {

        	jlbl_color.setSize(_imageWidth, jlbl_color.getHeight());	
    	} else {
    		State.getLogger().warning("element does not exist which is "
    				+ "supposed to exist.");
    	}
        this.imageWidth = _imageWidth;
    }

    /**
     * @return the imageHeight
     */
    public final int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param _imageHeight the imageHeight to set
     */
    public final void setImageHeight(final int _imageHeight) {

    if (jlbl_color != null) {
    	jlbl_color.setSize(jlbl_color.getWidth(), _imageHeight);	
	} else {
		State.getLogger().warning("element does not exist which is "
				+ "supposed to exist.");
	}
        this.imageHeight = _imageHeight;
    }

    /**
     * @return the additionalInformation
     */
    public final Object getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * @param _additionalInformation the additionalInformation to set
     */
    public final void setAdditionalInformation(
            final Object _additionalInformation) {
        this.additionalInformation = _additionalInformation;
    }

    
    /**
     * Apply stroke.
     */
	public final synchronized void stroke() {
        Util.getStroke(jlbl_stroke); 
	}
}
