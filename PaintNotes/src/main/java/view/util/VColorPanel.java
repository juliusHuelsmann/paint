//package declaration
package view.util;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//import declarations
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

import view.util.mega.MButton;
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import view.util.mega.MTextField;
import model.settings.TextFactory;
import model.settings.ViewSettings;
import control.interfaces.MenuListener;
import control.util.CColorPanel;


/**
 * Panel for the selection of a new color.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial") public class VColorPanel extends MPanel {

	/**
	 * MTextFields for entering special color.
	 */
	private MTextField jtf_r, jtf_g, jtf_b;
	
	/**
	 * JLabel for selected color and RGB - titles.
	 */
	private MLabel jlbl_selectedColor, 
	jlbl_r , jlbl_g, jlbl_b;

	/**
	 * MButton for applying the changes.
	 */
	private MButton jbtn_applyCanges;
	
	/**
	 * Array of buttons which 
	 *     are already there and have to be updated
	 *     have to be added to the colorPanel.
	 */
	private MButton [] jbtn_colorsUpdate, jbtn_colorsPanel;
	
	
	/**
	 * the controller class.
	 */
	private CColorPanel control;

	/*
	 * final values used for computing the size of stuff
	 */
	
	/**
	 * distance between single components in this class.
	 */
	private final int distanceItems = 10;

	/**
	 * amount of items in row in MButton array for colors.
	 */
    private final int amountInRows = 7;
    
    /**
     * The size of the big color.
     */
    private final int sizeBigColor = 50;
    
	/**
	 * Constructor: initializes things.
	 * 
	 * @param _jbtn_toUpdate the MButtons which are being updated.
	 * @param _ml the menuListener for all item1- and item2- menus
	 */
	public VColorPanel(final MButton[] _jbtn_toUpdate, 
			final MenuListener _ml, 
			final MouseListener _controlPaintStatus) {

		//initialize JPanel and alter settings
		super();
		super.setOpaque(false);
		super.setLayout(null);
		super.setBackground(ViewSettings.GENERAL_CLR_BACKGROUND);
		super.setVisible(false);

        //save the MButton 
        this.jbtn_colorsPanel = _jbtn_toUpdate;
        control = new CColorPanel(this);

        //add components
		int height = addComponents(_ml, _controlPaintStatus);
		
		jlbl_selectedColor.setBounds(
		        jbtn_colorsUpdate[jbtn_colorsUpdate.length - 1].getX()
		        + distanceItems / 2
		        + jbtn_colorsUpdate[jbtn_colorsUpdate.length - 1].getWidth(), 
		        
		        jbtn_colorsUpdate[amountInRows - 1].getY(), 
		        sizeBigColor + (1 + 2) * (2 + 2), 
		        height * (2) + distanceItems / 2);
		
		jbtn_applyCanges.setBounds(jlbl_selectedColor.getX(), 
		        jlbl_selectedColor.getY() + jlbl_selectedColor.getHeight() 
		        + distanceItems / 2,
		        jlbl_selectedColor.getWidth(), (2 + 2 + 2) * (2 + 2 + 1));
		
		/*
		 * add listeners
		 */
		//keyListener for changing current color by changing the RGB values
		jtf_r.addKeyListener(control);
		jtf_g.addKeyListener(control);
		jtf_b.addKeyListener(control);
		
		//mouse and -motionListener for changing by clicking at color area
		for (int currentButton = 0; 
		        currentButton < getJbtn_colorsPanel().length;
		        currentButton++) {

		    jbtn_colorsUpdate[currentButton]
		            .addMouseMotionListener(control); 
		    jbtn_colorsUpdate[currentButton]
		            .addMouseListener(control);
		}
	        
		//MButton for applying changes
		getJbtn_applyCanges().addMouseListener(control);

		//set location
		jlbl_r.setLocation(distanceItems,
		        jbtn_colorsUpdate[jbtn_colorsUpdate.length - 1].getY() 
		        + jbtn_colorsUpdate[0].getHeight() + distanceItems / 2);
		jtf_r.setLocation(jlbl_r.getX() + jlbl_r.getWidth(), jlbl_r.getY());
	        
	        
		jlbl_g.setLocation(jtf_r.getX() + jtf_r.getWidth() 
		        + distanceItems, jlbl_r.getY());

		jtf_g.setLocation(jlbl_g.getX() + jlbl_g.getWidth(), jlbl_g.getY());
		jlbl_b.setLocation(jtf_g.getX() + jtf_g.getWidth() 
		        + distanceItems, jlbl_g.getY());
		jtf_b.setLocation(jlbl_b.getX() + jlbl_b.getWidth(), jlbl_b.getY());

		super.setSize(ViewSettings.getSIZE_PNL_CLR());
		super.setVisible(true);
	}
	
	
	/**
	 * add components to JPanel and return height.
	 * @param _ml the menuListener for all item1- and item2- menus
	 * @return the height
	 */
	private int addComponents(final MenuListener _ml,
			final MouseListener _controlPaintStatus) {

        //save final values that are rather unimportant.
        final Dimension dim_JLabel = new Dimension(40, 16);
        final Dimension dim_MTextField = new Dimension(30, 16);
        final int widthTotal = 500;
        final int currentHeight = 150;


        //JLabel for the selected Color
        jlbl_selectedColor = new MLabel();
        jlbl_selectedColor.setSize(sizeBigColor, sizeBigColor);
        jlbl_selectedColor.setBackground(Color.pink);
        jlbl_selectedColor.setOpaque(true);
        jlbl_selectedColor.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.black), new LineBorder(Color.white)));
        super.add(jlbl_selectedColor);

        /*
         * JLabels and MTextFields for color RGB Values
         */
        jlbl_r = new MLabel(TextFactory.getInstance().getTextColorPanel_red());
        jlbl_r.setSize(dim_JLabel);
        jlbl_r.setOpaque(false);
        jlbl_r.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jlbl_r.setBorder(null);
        super.add(jlbl_r);

        jtf_r  = new MTextField();
        jtf_r.setOpaque(false);
        jtf_r.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jtf_r.setSize(dim_MTextField);
        jtf_r.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        super.add(jtf_r);

        jlbl_g = new MLabel(
                TextFactory.getInstance().getTextColorPanel_green());
        jlbl_g.setSize(dim_JLabel);
        jlbl_g.setOpaque(false);
        jlbl_g.setBorder(null);
        jlbl_g.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jlbl_g);

        jtf_g  = new MTextField();
        jtf_g.setOpaque(false);
        jtf_g.setSize(dim_MTextField);
        jtf_g.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jtf_g.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        super.add(jtf_g);
        
        jlbl_b = new MLabel(
                TextFactory.getInstance().getTextColorPanel_blue());
        jlbl_b.setSize(dim_JLabel);
        jlbl_b.setOpaque(false);
        jlbl_b.setBorder(null);
        jlbl_b.setFont(ViewSettings.GENERAL_FONT_ITEM);
        super.add(jlbl_b);

        jtf_b  = new MTextField();
        jtf_b.setOpaque(false);
        jtf_b.setSize(dim_MTextField);
        jtf_b.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jtf_b.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        jtf_b.setFocusable(true);
        super.add(jtf_b);

        /*
         * MButton for inserting.
         */
        jbtn_applyCanges = new MButton(
                TextFactory.getInstance().getTextColorPanel_submit());
        jbtn_applyCanges.setContentAreaFilled(false);
        jbtn_applyCanges.setFont(ViewSettings.GENERAL_FONT_ITEM);
        jbtn_applyCanges.setOpaque(false);
        jbtn_applyCanges.setFocusable(false);
        super.add(jbtn_applyCanges);

        //add controller for this class.

        this.insertColorPanel(widthTotal, currentHeight, _ml, 
        		_controlPaintStatus);
        
        int width = sizeBigColor / 2;
        int height = sizeBigColor / (2 + 1);
        
        jbtn_colorsUpdate = new MButton[jbtn_colorsPanel.length];
        for (int i = 0; i < jbtn_colorsUpdate.length; i++) {
            jbtn_colorsUpdate[i] = new MButton();
            jbtn_colorsUpdate[i].setBounds(distanceItems 
                    + (i % amountInRows) * (width + 2), -distanceItems 
                    + currentHeight + (i / amountInRows) * (height + 2),
                    width, height);
            jbtn_colorsUpdate[i].setBackground(
                    jbtn_colorsPanel[i].getBackground());
            jbtn_colorsUpdate[i].setOpaque(true);
            
            if (i == control.getSelectedPosition()) {
                jbtn_colorsUpdate[i].setBorder(BorderFactory.createLineBorder(
                        Color.red, 2));
            } else {
                jbtn_colorsUpdate[i].setBorder(BorderFactory
                        .createCompoundBorder(
                                new LineBorder(Color.black), 
                                new LineBorder(Color.white)));
            }
            super.add(jbtn_colorsUpdate[i]);
        }
        
        //return the current height
        return height;
	}
	
	/**
	 * insert the color panel to the ColorPanel.
	 * 
	 * @param _width the width of the panel
	 * @param _height the height of the panel
	 * @param _ml the menuListener for all item1- and item2- menus
	 * 
	 * @return the height of the ColorPanel.
	 */
	private int insertColorPanel(
			final int _width, 
			final int _height,
			final MenuListener _ml,
			final MouseListener _controlPaintStatus) {

        /**
         * first and second colorPanels
         */
        final byte itemsInRow = (byte) 32;
        final int abstand = 8, abstand2 = 8, maxRGB = 255;
        
        


        MLabel jlbl_pickColor = new MLabel();
        //width = #px * #differentFields * #sizePX / #distance
        int newWidth = 256 * 4 * 2 / abstand2;
        BufferedImage bi = new BufferedImage(
        		newWidth,
        		itemsInRow * 4,
        		BufferedImage.TYPE_INT_RGB);
        jlbl_pickColor.setSize(bi.getWidth(), bi.getHeight());
        /*
         * gray (to red)
         */
        int currentItem = 0;
        for (int gb = maxRGB; gb >= 0; gb -= abstand2) {
            for (int r = maxRGB; r >= 0; r -= abstand) {

                //update.
                int tempGb = gb;
                if (tempGb > r) {
                    tempGb = r;
                }
                int y = (currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, new Color(r, tempGb, tempGb).getRGB());
                bi.setRGB(x, y + 1, new Color(r, tempGb, tempGb).getRGB());
                bi.setRGB(x + 1, y, new Color(r, tempGb, tempGb).getRGB());
                bi.setRGB(x + 1, y + 1, new Color(r, tempGb, tempGb).getRGB());
                currentItem++;
            }
        }   
        /*
         * red (to green)
         */
        for (int g = 0; g <= maxRGB; g += abstand2) {
            for (int r = maxRGB; r >= 0; r -= abstand) {

                //update.
                int tempGb = g;
                if (tempGb > r) {
                    tempGb = r;   
                }
                int rgb = new Color(r, tempGb, 0).getRGB();
                int y = (currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, rgb);
                bi.setRGB(x, y + 1, rgb);
                bi.setRGB(x + 1, y, rgb);
                bi.setRGB(x + 1, y + 1, rgb);
                currentItem++;
            }
        }   
        for (int i = maxRGB; i >= 0; i -= abstand2) {
            for (int r = maxRGB; r >= 0; r -= abstand) {

                //update.
                int tempi = i;
                if (tempi > r) {
                    tempi = r;
                }

                int rgb = new Color(tempi, r, 0).getRGB();
                int y = (currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, rgb);
                bi.setRGB(x, y + 1, rgb);
                bi.setRGB(x + 1, y, rgb);
                bi.setRGB(x + 1, y + 1, rgb);
                currentItem++;
            }
        }
        
        /*
         * green (to blue)
         */
        for (int blue = 0; blue <= maxRGB; blue += abstand2) {
            for (int r = maxRGB; r >= 0; r -= abstand) {

                //update.
                int tempGb = blue;
                if (tempGb > r) {
                    tempGb = r;
                }

                int rgb = new Color(0, r, tempGb).getRGB();
                int y = (currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, rgb);
                bi.setRGB(x, y + 1, rgb);
                bi.setRGB(x + 1, y, rgb);
                bi.setRGB(x + 1, y + 1, rgb);
                currentItem++;
            }
        }   
        currentItem = 0;
        for (int blue = 0; blue <= maxRGB; blue += abstand2) {
            for (int r = 0; r <= maxRGB; r += abstand) {

                //update.
                int tempGb = blue;
                if (tempGb > r) {
                    tempGb = r;
                }
                int rgb = new Color(0, r, tempGb).getRGB();
                int y = (itemsInRow + currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, rgb);
                bi.setRGB(x, y + 1, rgb);
                bi.setRGB(x + 1, y, rgb);
                bi.setRGB(x + 1, y + 1, rgb);
                currentItem++;
                //insert color to panel
            }
        }   
        for (int i = maxRGB; i >= 0; i -= abstand2) {
            for (int r = 0; r <= maxRGB; r += abstand) {

                //update.
                int tempi = i;
                if (tempi > r) {
                    tempi = r;
                }
                int rgb = new Color(0, tempi, r).getRGB();
                int y = (itemsInRow + currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, rgb);
                bi.setRGB(x, y + 1, rgb);
                bi.setRGB(x + 1, y, rgb);
                bi.setRGB(x + 1, y + 1, rgb);
                currentItem++;
                //insert color to panel
            }
        }
        /*
         * blue (to red)
         */
        for (int blue = 0; blue <= maxRGB; blue += abstand2) {
            for (int r = 0; r <= maxRGB; r += abstand) {

                //update .
                int tempGb = blue;
                if (tempGb > r) {
                    tempGb = r;
                }
                int rgb = new Color(tempGb, 0, r).getRGB();
                int y = (itemsInRow + currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, rgb);
                bi.setRGB(x, y + 1, rgb);
                bi.setRGB(x + 1, y, rgb);
                bi.setRGB(x + 1, y + 1, rgb);
                currentItem++;
            }
        }   
        for (int i = maxRGB; i >= 0; i -= abstand2) {
            for (int r = 0; r <= maxRGB; r += abstand) {

                //update.
                int tempi = i;
                if (tempi > r) {
                    tempi = r;
                }
                int rgb = new Color(r, 0, tempi).getRGB();
                int y = (itemsInRow + currentItem % itemsInRow) * 2;
                int x = (int) (currentItem / itemsInRow) * 2;
                bi.setRGB(x, y, rgb);
                bi.setRGB(x, y + 1, rgb);
                bi.setRGB(x + 1, y, rgb);
                bi.setRGB(x + 1, y + 1, rgb);
                currentItem++;
            }
        }

        //set the bufferedimage inside the controller class and add
        // listener to the JLabel for being able to pick colors
        control.setBufferedImage(bi);
        jlbl_pickColor.addMouseListener(control);
        jlbl_pickColor.addMouseMotionListener(control);
        
        jlbl_pickColor.setLocation(distanceItems, distanceItems);
        jlbl_pickColor.setIcon(new ImageIcon(bi));
        super.add(jlbl_pickColor);

        return jlbl_pickColor.getHeight();
	}

	
	/*
	 * getter and setter methods.
	 */

	/**
	 * @return the jtf_r
	 */
	public final MTextField getJtf_r() {
		return jtf_r;
	}


	/**
	 * @return the jtf_g
	 */
	public final MTextField getJtf_g() {
		return jtf_g;
	}


	/**
	 * @return the jtf_b
	 */
	public final MTextField getJtf_b() {
		return jtf_b;
	}


	/**
	 * @return the jlbl_selectedColor
	 */
	public final MLabel getJlbl_selectedColor() {
		return jlbl_selectedColor;
	}


	/**
	 * @return the jbtn_schonDa
	 */
	public final MButton [] getJbtn_colorsUpdate() {
		return jbtn_colorsUpdate;
	}


	/**
	 * @return the jbtn_copy
	 */
	public final MButton [] getJbtn_colorsPanel() {
		return jbtn_colorsPanel;
	}


	/**
	 * @return the jbtn_applyCanges
	 */
	public final MButton getJbtn_applyCanges() {
		return jbtn_applyCanges;
	}
}
