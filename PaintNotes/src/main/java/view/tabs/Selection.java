//package declaration
package view.tabs;


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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.State;
import model.settings.ViewSettings;
import control.ControlPaint;
import control.forms.CPaintStatus;
import control.forms.tabs.CTabTools;
import control.forms.tabs.CTabSelection;
import control.interfaces.MenuListener;
import control.util.CPen;
import view.forms.Help;
import view.util.CheckedComponent;
import view.util.Item1Menu;
import view.util.Item1Button;
import view.util.Item1PenSelection;
import view.util.MultipleBar;
import view.util.VColorPanel;
import view.util.mega.MButton;

/**
 * The Selection Tab.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Selection extends Tab {


    /**
     * array of colors to change the first or the second color.
     */
    private MButton [] jbtn_colors;

    /**
     * Buttons for the second and the first color.
     */
    private Item1Button tb_color, tb_erase, tb_changeSize,
    i1b_rotateClockwise, i1b_rotateCounterclockwise, i1b_mirrorHoriz, 
    i1b_rotateFree, i1b_mirrorVert;
    
    /**
     * Color fetcher.
     */
    private Item1Menu it_color, im_changePen, im_scanEdit;

    /**
     * integer values.
     */
    private final int distance = 5, heightLabel = 20, hf = 150;

    /**
     * The JCheckBox for changing the pen.
     */
    private JCheckBox jcb_points, jcb_line, jcb_maths;

    
    /**
     * Empty Utility class Constructor.
	 * @param _cmp_toRepaint the component which is to repaint if the
	 * 			scrolling is performed. (is necessary if the
	 * 			{@link #ScrollablePanel(Component)} is inserted above
	 * 			some opaque component.
     */
	public Selection(final Component _cmp_toRepaint) {
		
	    super(2, _cmp_toRepaint);
        super.setOpaque(false);
        super.setLayout(null);

        
	}
	
	
	
	/**
	 * Apply size for the entire panel and its contents.
	 */
	public void applySize() {
		super.applySize();


        int x = initCololrs(distance, false, null, null, null, null);
        x = initPen(x, false, null);
        initOthers(x, false, null, null, null, null);
	}
	
	/**
	 * 
     * @param _cPaint 				instance of the controller class which 
     * 								deals with painting / erasing / zooming
     * 								at the picture.
     * 
     * @param _cts 					instance of the controller class of this
     * 								very tab.
     * 
     * @param _ml 					instance of MenuListenern
     * 
     * @param _controlPaintStatus 	the controller class which copes with 
     * 								the different paint status like:	
     * 									(1) 	paint
     * 									(2)		erase
     * 									(3)		(...)
     * 								etc.
	 */
	public void initialize(
			final CTabTools _cPaint, 
			final CTabSelection _cts,
			final ControlPaint _cp,
			final CPaintStatus _controlPaintStatus) {

        int x = initCololrs(distance, true, _cPaint, _cts, _cp, 
        		_controlPaintStatus);
        x = initPen(x, true, _cts);
        x = initOthers(x, true, _cp, _controlPaintStatus, _cts, _cp);
        initBIOperations(x, true, _cp, _controlPaintStatus, _cts, _cp);
	}
	
	
	
	/**
	 * 
	 * @param _x the x coordinate
	 * @param _paint the boolean.
	 * 
	 * 
	 * @param _controlTabPainting 
	 * 					instance of ControlTabPainting handling the 
	 * 					MouseEvents for the color selection in color
	 * 					pane
	 * 
	 * @param _cTabSelection 
	 * 					instance of ControlTabSelection handling the 
	 * 					ActionEvents for the color button selection
	 * 
	 * @param _ml
	 * 					implements MenuListener _ml which handles menu
	 * 					opening and closing events for being able to repaint
	 * 					or to do other necessary stuff.
	 * 
	 * @return the new x coordinate
	 */
	private int initCololrs(final int _x, final boolean _paint,
			final MouseListener _controlTabPainting,
			final ActionListener _cTabSelection, 
			final MenuListener _ml,
			final CPaintStatus _controlPaintStatus) {

		if (_paint) {

	        //the first color for the first pen
	        tb_color = new Item1Button(null);
	        tb_color.setOpaque(true);
//	        tb_color.addMouseListener(CPaintStatus.getInstance());
	        tb_color.setBorder(BorderFactory.createCompoundBorder(
	                new LineBorder(Color.black), new LineBorder(Color.white)));
		}

	        tb_color.setLocation(_x, ViewSettings.getDistanceBetweenItems());
	        tb_color.setSize(ViewSettings.getItemMenu1Width(), 
	                ViewSettings.getItemMenu1Height());
		if (_paint) {

	        tb_color.setText("Farbe 1");
	        tb_color.setActivable(false);
	        super.add(tb_color);
		}

    	final int distanceBetweenColors = 2;
    	final int width = (2 + 2 + 1) * (2 + 2 + 1) - 2 - 2;
    	final int amountOfItems = 4;
    	final int height = ViewSettings.getItemMenu1Height() / amountOfItems
    	        -  distanceBetweenColors;
    	final int anzInR = 7;
    	

		if (_paint) {

			jbtn_colors = new MButton[anzInR * (2 + 2)];
		}
        for (int i = 0; i < jbtn_colors.length; i++) {

    		if (_paint) {
    	
            jbtn_colors[i] = new MButton();
    		}
            jbtn_colors[i].setBounds(tb_color.getX() + tb_color.getWidth() 
    		        + distanceBetweenColors 
    		        + (i % anzInR) * (width + distanceBetweenColors),
    		        distanceBetweenColors + (i / anzInR) 
    		        * (height + distanceBetweenColors), width, height);

    		if (_paint) {

            jbtn_colors[i].setOpaque(true);
            jbtn_colors[i].addActionListener(_cTabSelection);
            jbtn_colors[i].addMouseListener(_controlPaintStatus);
            jbtn_colors[i].addMouseListener(_controlTabPainting);
            jbtn_colors[i].setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.black), new LineBorder(Color.white)));
            super.add(jbtn_colors[i]);
    		}
        }

		if (_paint) {

        int i = 0;
        final int three = 3;
        final Color 
                c1n0 = Color.black,
                c1n1 = new Color(80, 80, 80),
                c1n2 = new Color(160, 160, 160),
                c1n3 = Color.white,
                
                c2n0 = new Color(23, 32, 164),
                c2n1 = new Color(63, 72, 204), 
                c2n2 = new Color(103, 112, 244),
                c2n3 = new Color(153, 162, 255), 

                c3n0 = new Color(180, 10, 10),
                c3n1 = new Color(200, 20, 20), 
                c3n2 = new Color(250, 75, 75),
                c3n3 = new Color(255, 100, 100), 

                c4n0 = new Color(24, 157, 45),
                c4n1 = new Color(34, 177, 67), 
                c4n2 = new Color(64, 197, 97),
                c4n3 = new Color(104, 255, 147), 
                        
                c5n0 = new Color(235, 107, 73),
                c5n1 = new Color(255, 127, 93), 
                c5n2 = new Color(255, 147, 113),
                c5n3 = new Color(255, 187, 153), 

                c6n0 = new Color(133, 33, 134),
                c6n1 = new Color(163, 73, 164), 
                c6n2 = new Color(193, 103, 194),
                c6n3 = new Color(255, 153, 254),
                        
                c7n0 = new Color(112, 146, 190),
                c7n1 = new Color(200, 191, 231), 
                c7n2 = new Color(255, 201, 14),
                c7n3 = new Color(120, 74, 50);
    
        //schwarz bis grau
        jbtn_colors[i + anzInR * 0].setBackground(c1n0);
        jbtn_colors[i + anzInR * 1].setBackground(c1n1);
        jbtn_colors[i + anzInR * 2].setBackground(c1n2);
        jbtn_colors[i + anzInR * three].setBackground(c1n3);
        //blue
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c2n0);
        jbtn_colors[i + anzInR * 1].setBackground(c2n1);
        jbtn_colors[i + anzInR * 2].setBackground(c2n2);
        jbtn_colors[i + anzInR * three].setBackground(c2n3);
    
        //red
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c3n0);
        jbtn_colors[i + anzInR * 1].setBackground(c3n1);
        jbtn_colors[i + anzInR * 2].setBackground(c3n2);
        jbtn_colors[i + anzInR * three].setBackground(c3n3);
    
        //green
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c4n0);
        jbtn_colors[i + anzInR * 1].setBackground(c4n1);
        jbtn_colors[i + anzInR * 2].setBackground(c4n2);
        jbtn_colors[i + anzInR * three].setBackground(c4n3);
    
        //orange
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c5n0);
        jbtn_colors[i + anzInR * 1].setBackground(c5n1);
        jbtn_colors[i + anzInR * 2].setBackground(c5n2);
        jbtn_colors[i + anzInR * three].setBackground(c5n3);
    
        //pink
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c6n0);
        jbtn_colors[i + anzInR * 1].setBackground(c6n1);
        jbtn_colors[i + anzInR * 2].setBackground(c6n2);
        jbtn_colors[i + anzInR * three].setBackground(c6n3);
    
        i++;
        jbtn_colors[i + anzInR * 0].setBackground(c7n0);
        jbtn_colors[i + anzInR * 1].setBackground(c7n1);
        jbtn_colors[i + anzInR * 2].setBackground(c7n2);
        jbtn_colors[i + anzInR * three].setBackground(c7n3);
		}
		

		if (_paint) {

        //
        it_color = new Item1Menu(true);
        it_color.setMenuListener(_ml);
        it_color.addMouseListener(_controlPaintStatus);
		}
        it_color.setSize(ViewSettings.getSIZE_PNL_CLR());

		if (_paint) {

        it_color.setBorder(false);
        it_color.setText("+ Farben");
		}
        it_color.setLocation(jbtn_colors[jbtn_colors.length - 1].getX() 
                + ViewSettings.getDistanceBetweenItems() 
                + jbtn_colors[jbtn_colors.length - 1].getWidth(), 
                ViewSettings.getDistanceBetweenItems());

		if (_paint) {

	        it_color.getMPanel().add(new VColorPanel(jbtn_colors, _ml,
	        		_controlPaintStatus));
	        it_color.setBorder(false);
		}
        it_color.setIcon(Constants.VIEW_TAB_PAINT_PALETTE);

		if (_paint) {

			super.add(it_color);
		}
        
        int xLocationSeparation = it_color.getWidth() + it_color.getX() 
                + ViewSettings.getDistanceBeforeLine();
        insertSectionStuff("Farben", _x, xLocationSeparation, 0, _paint);

        
        return xLocationSeparation + ViewSettings.getDistanceBetweenItems();
	}
	
	
	/**
	 * initialize the pen.
	 * @param _x the x coordinate
	 * @param _paint whether to paint the items
	 * 
	 * @param _cTabSelection 
	 * 					instance of ControlTabSelection handling the 
	 * 					ActionEvents for all buttons inside this tab
	 * 
	 * @return the new x coordinate
	 */
	private int initPen(final int _x, final boolean _paint,
			final CTabSelection _cTabSelection) {

		if (_paint) {

	        jcb_points = new JCheckBox("points");
	        jcb_points.setSelected(true);
	        jcb_points.setOpaque(false);
		}
        jcb_points.setBounds(_x, distance,  hf, heightLabel);
		if (_paint) {

	        jcb_points.setVerticalAlignment(SwingConstants.TOP);
	        jcb_points.setFocusable(false);
	        jcb_points.addActionListener(_cTabSelection);
	        super.add(jcb_points);
	    
	        jcb_line = new JCheckBox("line");
	        jcb_line.setVerticalAlignment(SwingConstants.TOP);
	        jcb_line.setFocusable(false);
	        jcb_line.setOpaque(false);
		}
        jcb_line.setBounds(_x, jcb_points.getHeight() + jcb_points.getY() 
                + distance,  hf, heightLabel);

		if (_paint) {
	
	        jcb_line.addActionListener(_cTabSelection);
	        super.add(jcb_line);
	    
	        jcb_maths = new JCheckBox("maths");
	        jcb_maths.setFocusable(false);
	        jcb_maths.setOpaque(false);
	        jcb_maths.setVerticalAlignment(SwingConstants.TOP);
		}

        jcb_maths.setBounds(_x, jcb_line.getHeight() + jcb_line.getY() 
                + distance,  hf, heightLabel);

		if (_paint) {

	        jcb_maths.addActionListener(_cTabSelection);
	        super.add(jcb_maths);
	        
	        //deactivate the items because at the beginning there is no item 
	        //selected.
	        _cTabSelection.deactivateOp();
		}
    

        int xLocationSeparation = jcb_maths.getWidth() + jcb_maths.getX() 
                + ViewSettings.getDistanceBeforeLine();
        insertSectionStuff("Pen", _x, xLocationSeparation, 1, _paint);

        return xLocationSeparation + ViewSettings.getDistanceBetweenItems();
        
	}


	private CheckedComponent cp_value, cp_saturation, cp_threshold;
	private MultipleBar mb_value, mb_threshold, mb_saturation;
	/**
	 * The item1Menu for the first pen.
	 */
	private Item1Menu it_rotate;

	/**
	 * The item1Menu for the first pen.
	 */
	private Item1Menu im_changeColors;

	/**
	 * The item1Menu for the first pen.
	 */
	private Item1Menu im_grayifyColors;
	
	
	/**
	 * 
	 */
	private Item1Button i1b_red2yellow, i1b_red2green, i1b_red2lightBlue,
	i1b_red2darkBlue, i1b_red2pink,

	i1b_yellow2red, i1b_yellow2green, i1b_yellow2lightBlue,
	i1b_yellow2darkBlue, i1b_yellow2pink,

	i1b_green2yellow, i1b_green2red, i1b_green2lightBlue, i1b_green2darkBlue, 
	i1b_green2pink,

	i1b_lightBlue2yellow, i1b_lightBlue2green, i1b_lightBlue2red,
	i1b_lightBlue2darkBlue, i1b_lightBlue2pink,
	
	i1b_darkBlue2yellow, i1b_darkBlue2green, i1b_darkBlue2lightBlue, 
	i1b_darkBlue2red, i1b_darkBlue2pink,
	
	i1b_pink2yellow, i1b_pink2green, i1b_pink2lightBlue, i1b_pink2darkBlue, 
	i1b_pink2red;
	

	/**
	 * 
	 */
	private Item1Button i1b_grayifyRed, i1b_grayifyYellow, i1b_grayifyGreen,
	i1b_grayifyLightBlue, i1b_grayifyDarkBlue, i1b_grayifyPink, i1b_scanOK;
	
	/**
	 * initialize other items.
	 * @param _x the x coordinate
	 * @param _paint whether to paint or not
	 * @param _ml
	 * 					implements MenuListener _ml which handles menu
	 * 					opening and closing events for being able to repaint
	 * 					or to do other necessary stuff.
	 * 
	 */
	private int initOthers(final int _x, final boolean _paint,
			final MenuListener _ml,
			final CPaintStatus _controlPaintStatus,
			final ActionListener _cts,
			final ControlPaint _cp) {
		

		
		
		if (_paint) {

	        tb_erase = new Item1Button(null);
	        tb_erase.setOpaque(true);
		}
		tb_erase.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
		tb_erase.setLocation(_x + distance, distance);
		if (_paint) {

			tb_erase.setText("Delete");
			tb_erase.setActivable(false);
			tb_erase.addActionListener(_cts);
			tb_erase.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
			tb_erase.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.black),
                    new LineBorder(Color.white)));
            super.add(tb_erase);
        }
		
		
		
		if (_paint) {

	        tb_changeSize = new Item1Button(null);
	        tb_changeSize.setOpaque(true);
		}
        tb_changeSize.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());
        tb_changeSize.setLocation(tb_erase.getWidth() + tb_erase.getX() + distance, distance);
		if (_paint) {

	        tb_changeSize.setText("Groesse aendern");
	        tb_changeSize.setBorder(BorderFactory.createCompoundBorder(
	                new LineBorder(Color.black),
	                new LineBorder(Color.white)));
	        tb_changeSize.setActivable(false);
	        tb_changeSize.addActionListener(_cts);
	        tb_changeSize.setIcon(Constants.VIEW_TAB_INSRT_SELECT);
	        super.add(tb_changeSize);
	
            im_changePen = new Item1Menu(false);
            im_changePen.removeScroll();
            im_changePen.setMenuListener(_ml);
            im_changePen.addMouseListener(_controlPaintStatus);

		}


        final Dimension sizeIT = new Dimension(550, 550);
		im_changePen.setSize(sizeIT);
		im_changePen.setSizeHeight(50);
		im_changePen.setLocation(tb_changeSize.getX() + tb_changeSize.getWidth() + distance, 
                tb_changeSize.getY());
		if (_paint) {

			im_changePen.setBorder(null);
			im_changePen.setText("Change pen");
			im_changePen.setItemsInRow((byte) 1);
			im_changePen.setActivable();
			im_changePen.setBorder(false);
		}
		im_changePen.setIcon(Constants.VIEW_TAB_INSRT_SELECT);
		if (_paint) {
			addPens(_cp, _cp.getcTabTools(), _controlPaintStatus);
        	super.add(im_changePen);

	
	        //pen 1
	        it_rotate = new Item1Menu(false);
	        it_rotate.removeScroll();
	        it_rotate.setMenuListener(_ml);
	        it_rotate.addMouseListener(_controlPaintStatus);
	        it_rotate.setBorder(null);
	        it_rotate.setBorder(false);
	        it_rotate.setText("Drehen/Spiegeln");

	        im_scanEdit = new Item1Menu(false);
            im_scanEdit.removeScroll();
            im_scanEdit.setMenuListener(_ml);
            im_scanEdit.addMouseListener(_controlPaintStatus);
	        im_scanEdit.setBorder(null);
	        im_scanEdit.setBorder(false);
            im_scanEdit.setText("Edit scan");

	        i1b_scanOK = new Item1Button(null);
	        i1b_scanOK.addActionListener(_cts);
	        i1b_scanOK.setBorder(false);
	        i1b_scanOK.setShifted();
	        i1b_scanOK.setText("Apply");
	        i1b_scanOK.setActivable(true);
	        i1b_scanOK.setOpaque(false);
	        
	        
            i1b_rotateClockwise = new Item1Button(null);
            i1b_rotateClockwise.addActionListener(_cts);
            i1b_rotateClockwise.setBorder(false);
            i1b_rotateClockwise.setShifted();
            i1b_rotateClockwise.setText("Rotate 45° Clockwise");
            i1b_rotateClockwise.setActivable(true);
            i1b_rotateClockwise.setOpaque(false);

	        
            i1b_rotateCounterclockwise = new Item1Button(null);
            i1b_rotateCounterclockwise.addActionListener(_cts);
            i1b_rotateCounterclockwise.setBorder(false);
            i1b_rotateCounterclockwise.setShifted();
            i1b_rotateCounterclockwise.setText("Rotate 45° Counter-clockwise");
            i1b_rotateCounterclockwise.setActivable(true);
            i1b_rotateCounterclockwise.setOpaque(false);

            i1b_rotateFree = new Item1Button(null);
            i1b_rotateFree.addActionListener(_cts);
            i1b_rotateFree.setBorder(false);
            i1b_rotateFree.setShifted();
            i1b_rotateFree.setText("Rotation custom degree");
            i1b_rotateFree.setActivable(true);
            i1b_rotateFree.setOpaque(false);
	        
            i1b_mirrorVert = new Item1Button(null);
            i1b_mirrorVert.addActionListener(_cts);
            i1b_mirrorVert.setBorder(false);
            i1b_mirrorVert.setShifted();
            i1b_mirrorVert.setText("Mirror vertically");
            i1b_mirrorVert.setActivable(true);
            i1b_mirrorVert.setOpaque(false);

            i1b_mirrorHoriz = new Item1Button(null);
            i1b_mirrorHoriz.addActionListener(_cts);
            i1b_mirrorHoriz.setBorder(false);
            i1b_mirrorHoriz.setShifted();
            i1b_mirrorHoriz.setText("Mirror horizontally");
            i1b_mirrorHoriz.setActivable(true);
            i1b_mirrorHoriz.setOpaque(false);

            

//            im_scanEdit.addActionListener(_cts);

		}
		final int distance = 5;
        final int contentHeight = ViewSettings.getItemMenu1Height() - 2 * distance;
        final Dimension sizeOpen = new Dimension(
        		ViewSettings.getItemMenu1Width() * (2 + 2), 
        		 contentHeight * (2 + 2) + 5);
        final Dimension sizeOpenScan = new Dimension(
        		ViewSettings.getItemMenu1Width() * (2 + 2 + 1), 
        		 contentHeight * (2 + 2) + 5);
        
        it_rotate.changeClosedSizes(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());

        it_rotate.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        it_rotate.setSize(sizeOpen);
        it_rotate.setSizeHeight(ViewSettings.getItemWidth());
        it_rotate.setLocation(im_changePen.getX() 
                + im_changePen.getWidth() + distance, 
                im_changePen.getY());

        im_scanEdit.changeClosedSizes(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());

        im_scanEdit.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        im_scanEdit.setSize(sizeOpenScan);
        im_scanEdit.setSizeHeight(ViewSettings.getItemWidth());

        i1b_rotateClockwise.setSize(
        		it_rotate.getWidth(), i1b_rotateClockwise.getHeight());
        i1b_scanOK.setSize(
        		it_rotate.getWidth(), i1b_rotateClockwise.getHeight());
        i1b_rotateCounterclockwise.setSize(i1b_rotateClockwise.getSize());
        i1b_rotateFree.setSize(i1b_rotateClockwise.getSize());
        i1b_mirrorHoriz.setSize(i1b_rotateClockwise.getSize());
        i1b_mirrorVert.setSize(i1b_rotateClockwise.getSize());
//        im_scanEdit.setSize(ViewSettings.getItemMenu1Width(), 
//                ViewSettings.getItemMenu1Height());

        i1b_rotateClockwise.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_rotateCounterclockwise.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_rotateFree.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_mirrorHoriz.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_mirrorVert.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        im_scanEdit.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
		if (_paint) {

            it_rotate.add(i1b_rotateClockwise);
            it_rotate.add(i1b_rotateCounterclockwise);
            it_rotate.add(i1b_rotateFree);
            it_rotate.add(i1b_mirrorHoriz);
            it_rotate.add(i1b_mirrorVert);
	        it_rotate.setActivable();
	        it_rotate.setItemsInRow((byte) 1);
	        it_rotate.setBorder(false);
	        super.add(it_rotate);
	        
	        cp_threshold = new CheckedComponent("threshold");
	        mb_threshold = new MultipleBar(2, 0, 255*3, im_scanEdit.getBackground());
	        cp_threshold.add(mb_threshold);
	    	cp_threshold.setSize(im_scanEdit.getWidth(), im_scanEdit.getHeight() / 3);

	        im_scanEdit.add(cp_threshold);
	        

	        cp_saturation = new CheckedComponent("saturation");
	        mb_saturation = new MultipleBar(1, 0.01, 1.0, im_scanEdit.getBackground());
	        cp_saturation.add(mb_saturation);
	    	cp_saturation.setSize(im_scanEdit.getWidth(), im_scanEdit.getHeight() / 3);
	    	im_scanEdit.add(cp_saturation);

	        cp_value = new CheckedComponent("value");
	        mb_value = new MultipleBar(1, 0.01, 1.0, 
	        		im_scanEdit.getBackground());
	        cp_value.add(mb_value);
	    	cp_value.setSize(im_scanEdit.getWidth(), im_scanEdit.getHeight() / 3);
	    	im_scanEdit.add(cp_value);

	    	im_scanEdit.add(i1b_scanOK);
	        mb_value.setSize(cp_value.getResultingWidth(), cp_value.getHeight());
	        im_scanEdit.setActivable();
	        im_scanEdit.setItemsInRow((byte) 1);
	        im_scanEdit.setBorder(false);
	        im_scanEdit.removeScroll();
            super.add(im_scanEdit);
		}

        im_scanEdit.setLocation(it_rotate.getX() + it_rotate.getWidth() + 5, it_rotate.getY());

        int xLocationSeparation = im_scanEdit.getWidth() + im_scanEdit.getX()
                + ViewSettings.getDistanceBeforeLine();
        insertSectionStuff("Farben", _x, xLocationSeparation, 1, _paint);
        return xLocationSeparation;
//        return xLocationSeparation + ViewSettings.getDistanceBetweenItems();
	}
	
	
	

	/**
	 * initialize other items.
	 * @param _x the x coordinate
	 * @param _paint whether to paint or not
	 * @param _ml
	 * 					implements MenuListener _ml which handles menu
	 * 					opening and closing events for being able to repaint
	 * 					or to do other necessary stuff.
	 * 
	 */
	private void initBIOperations(final int _x, final boolean _paint,
			final MenuListener _ml,
			final CPaintStatus _controlPaintStatus,
			final ActionListener _cts,
			final ControlPaint _cp) {

		final int distance = 5;
        final int contentHeight = ViewSettings.getItemMenu1Height() - 2 * distance;
        final Dimension sizeOpen = new Dimension(
        		ViewSettings.getItemMenu1Width() * (2 + 2), 
        		 contentHeight * (2 + 2) + 5);
		
		

        //pen 1
        
        if (_paint) {
        	

            im_changeColors = new Item1Menu(false);
            im_changeColors.setMenuListener(_ml);
            im_changeColors.addMouseListener(_controlPaintStatus);
            im_changeColors.setBorder(null);
            im_changeColors.setBorder(false);
            im_changeColors.setText("switch clr");
            im_changeColors.changeClosedSizes(ViewSettings.getItemMenu1Width(), 
                    ViewSettings.getItemMenu1Height());

            /*
             * red
             */
            i1b_red2yellow = new Item1Button(null);
            i1b_red2yellow.addActionListener(_cts);
            i1b_red2yellow.setBorder(false);
            i1b_red2yellow.setShifted();
            i1b_red2yellow.setText("Red to yellow");
            i1b_red2yellow.setActivable(true);
            i1b_red2yellow.setOpaque(false);

            i1b_red2green = new Item1Button(null);
            i1b_red2green.addActionListener(_cts);
            i1b_red2green.setBorder(false);
            i1b_red2green.setShifted();
            i1b_red2green.setText("Red to green");
            i1b_red2green.setActivable(true);
            i1b_red2green.setOpaque(false);

            i1b_red2lightBlue = new Item1Button(null);
            i1b_red2lightBlue.addActionListener(_cts);
            i1b_red2lightBlue.setBorder(false);
            i1b_red2lightBlue.setShifted();
            i1b_red2lightBlue.setText("Red to lightBlue");
            i1b_red2lightBlue.setActivable(true);
            i1b_red2lightBlue.setOpaque(false);

            i1b_red2darkBlue = new Item1Button(null);
            i1b_red2darkBlue.addActionListener(_cts);
            i1b_red2darkBlue.setBorder(false);
            i1b_red2darkBlue.setShifted();
            i1b_red2darkBlue.setText("Red to darkBlue");
            i1b_red2darkBlue.setActivable(true);
            i1b_red2darkBlue.setOpaque(false);

            i1b_red2pink = new Item1Button(null);
            i1b_red2pink.addActionListener(_cts);
            i1b_red2pink.setBorder(false);
            i1b_red2pink.setShifted();
            i1b_red2pink.setText("Red to pink");
            i1b_red2pink.setActivable(true);
            i1b_red2pink.setOpaque(false);


            /*
             * yellow
             */
            i1b_yellow2red = new Item1Button(null);
            i1b_yellow2red.addActionListener(_cts);
            i1b_yellow2red.setBorder(false);
            i1b_yellow2red.setShifted();
            i1b_yellow2red.setText("yellow to red");
            i1b_yellow2red.setActivable(true);
            i1b_yellow2red.setOpaque(false);

            i1b_yellow2green = new Item1Button(null);
            i1b_yellow2green.addActionListener(_cts);
            i1b_yellow2green.setBorder(false);
            i1b_yellow2green.setShifted();
            i1b_yellow2green.setText("yellow to green");
            i1b_yellow2green.setActivable(true);
            i1b_yellow2green.setOpaque(false);

            i1b_yellow2lightBlue = new Item1Button(null);
            i1b_yellow2lightBlue.addActionListener(_cts);
            i1b_yellow2lightBlue.setBorder(false);
            i1b_yellow2lightBlue.setShifted();
            i1b_yellow2lightBlue.setText("yellow to lightBlue");
            i1b_yellow2lightBlue.setActivable(true);
            i1b_yellow2lightBlue.setOpaque(false);

            i1b_yellow2darkBlue = new Item1Button(null);
            i1b_yellow2darkBlue.addActionListener(_cts);
            i1b_yellow2darkBlue.setBorder(false);
            i1b_yellow2darkBlue.setShifted();
            i1b_yellow2darkBlue.setText("yellow to darkBlue");
            i1b_yellow2darkBlue.setActivable(true);
            i1b_yellow2darkBlue.setOpaque(false);

            i1b_yellow2pink = new Item1Button(null);
            i1b_yellow2pink.addActionListener(_cts);
            i1b_yellow2pink.setBorder(false);
            i1b_yellow2pink.setShifted();
            i1b_yellow2pink.setText("yellow to pink");
            i1b_yellow2pink.setActivable(true);
            i1b_yellow2pink.setOpaque(false);

            /*
             * red
             */
            i1b_green2yellow = new Item1Button(null);
            i1b_green2yellow.addActionListener(_cts);
            i1b_green2yellow.setBorder(false);
            i1b_green2yellow.setShifted();
            i1b_green2yellow.setText("green to yellow");
            i1b_green2yellow.setActivable(true);
            i1b_green2yellow.setOpaque(false);

            i1b_green2red = new Item1Button(null);
            i1b_green2red.addActionListener(_cts);
            i1b_green2red.setBorder(false);
            i1b_green2red.setShifted();
            i1b_green2red.setText("green to red");
            i1b_green2red.setActivable(true);
            i1b_green2red.setOpaque(false);

            i1b_green2lightBlue = new Item1Button(null);
            i1b_green2lightBlue.addActionListener(_cts);
            i1b_green2lightBlue.setBorder(false);
            i1b_green2lightBlue.setShifted();
            i1b_green2lightBlue.setText("green to lightBlue");
            i1b_green2lightBlue.setActivable(true);
            i1b_green2lightBlue.setOpaque(false);

            i1b_green2darkBlue = new Item1Button(null);
            i1b_green2darkBlue.addActionListener(_cts);
            i1b_green2darkBlue.setBorder(false);
            i1b_green2darkBlue.setShifted();
            i1b_green2darkBlue.setText("green to darkBlue");
            i1b_green2darkBlue.setActivable(true);
            i1b_green2darkBlue.setOpaque(false);

            i1b_green2pink = new Item1Button(null);
            i1b_green2pink.addActionListener(_cts);
            i1b_green2pink.setBorder(false);
            i1b_green2pink.setShifted();
            i1b_green2pink.setText("green to pink");
            i1b_green2pink.setActivable(true);
            i1b_green2pink.setOpaque(false);

            /*
             * lightBlue
             */
            i1b_lightBlue2yellow = new Item1Button(null);
            i1b_lightBlue2yellow.addActionListener(_cts);
            i1b_lightBlue2yellow.setBorder(false);
            i1b_lightBlue2yellow.setShifted();
            i1b_lightBlue2yellow.setText("lightBlue to yellow");
            i1b_lightBlue2yellow.setActivable(true);
            i1b_lightBlue2yellow.setOpaque(false);

            i1b_lightBlue2green = new Item1Button(null);
            i1b_lightBlue2green.addActionListener(_cts);
            i1b_lightBlue2green.setBorder(false);
            i1b_lightBlue2green.setShifted();
            i1b_lightBlue2green.setText("lightBlue to green");
            i1b_lightBlue2green.setActivable(true);
            i1b_lightBlue2green.setOpaque(false);

            i1b_lightBlue2red = new Item1Button(null);
            i1b_lightBlue2red.addActionListener(_cts);
            i1b_lightBlue2red.setBorder(false);
            i1b_lightBlue2red.setShifted();
            i1b_lightBlue2red.setText("lightBlue to red");
            i1b_lightBlue2red.setActivable(true);
            i1b_lightBlue2red.setOpaque(false);

            i1b_lightBlue2darkBlue = new Item1Button(null);
            i1b_lightBlue2darkBlue.addActionListener(_cts);
            i1b_lightBlue2darkBlue.setBorder(false);
            i1b_lightBlue2darkBlue.setShifted();
            i1b_lightBlue2darkBlue.setText("lightBlue to darkBlue");
            i1b_lightBlue2darkBlue.setActivable(true);
            i1b_lightBlue2darkBlue.setOpaque(false);

            i1b_lightBlue2pink = new Item1Button(null);
            i1b_lightBlue2pink.addActionListener(_cts);
            i1b_lightBlue2pink.setBorder(false);
            i1b_lightBlue2pink.setShifted();
            i1b_lightBlue2pink.setText("lightBlue to pink");
            i1b_lightBlue2pink.setActivable(true);
            i1b_lightBlue2pink.setOpaque(false);

            /*
             * darkBlue
             */
            i1b_darkBlue2yellow = new Item1Button(null);
            i1b_darkBlue2yellow.addActionListener(_cts);
            i1b_darkBlue2yellow.setBorder(false);
            i1b_darkBlue2yellow.setShifted();
            i1b_darkBlue2yellow.setText("darkBlue to yellow");
            i1b_darkBlue2yellow.setActivable(true);
            i1b_darkBlue2yellow.setOpaque(false);

            i1b_darkBlue2green = new Item1Button(null);
            i1b_darkBlue2green.addActionListener(_cts);
            i1b_darkBlue2green.setBorder(false);
            i1b_darkBlue2green.setShifted();
            i1b_darkBlue2green.setText("darkBlue to green");
            i1b_darkBlue2green.setActivable(true);
            i1b_darkBlue2green.setOpaque(false);

            i1b_darkBlue2lightBlue = new Item1Button(null);
            i1b_darkBlue2lightBlue.addActionListener(_cts);
            i1b_darkBlue2lightBlue.setBorder(false);
            i1b_darkBlue2lightBlue.setShifted();
            i1b_darkBlue2lightBlue.setText("darkBlue to lightBlue");
            i1b_darkBlue2lightBlue.setActivable(true);
            i1b_darkBlue2lightBlue.setOpaque(false);

            i1b_darkBlue2red = new Item1Button(null);
            i1b_darkBlue2red.addActionListener(_cts);
            i1b_darkBlue2red.setBorder(false);
            i1b_darkBlue2red.setShifted();
            i1b_darkBlue2red.setText("darkBlue to red");
            i1b_darkBlue2red.setActivable(true);
            i1b_darkBlue2red.setOpaque(false);

            i1b_darkBlue2pink = new Item1Button(null);
            i1b_darkBlue2pink.addActionListener(_cts);
            i1b_darkBlue2pink.setBorder(false);
            i1b_darkBlue2pink.setShifted();
            i1b_darkBlue2pink.setText("darkBlue to pink");
            i1b_darkBlue2pink.setActivable(true);
            i1b_darkBlue2pink.setOpaque(false);


            /*
             * pink
             */
            i1b_pink2yellow = new Item1Button(null);
            i1b_pink2yellow.addActionListener(_cts);
            i1b_pink2yellow.setBorder(false);
            i1b_pink2yellow.setShifted();
            i1b_pink2yellow.setText("pink to yellow");
            i1b_pink2yellow.setActivable(true);
            i1b_pink2yellow.setOpaque(false);

            i1b_pink2green = new Item1Button(null);
            i1b_pink2green.addActionListener(_cts);
            i1b_pink2green.setBorder(false);
            i1b_pink2green.setShifted();
            i1b_pink2green.setText("pink to green");
            i1b_pink2green.setActivable(true);
            i1b_pink2green.setOpaque(false);

            i1b_pink2lightBlue = new Item1Button(null);
            i1b_pink2lightBlue.addActionListener(_cts);
            i1b_pink2lightBlue.setBorder(false);
            i1b_pink2lightBlue.setShifted();
            i1b_pink2lightBlue.setText("pink to lightBlue");
            i1b_pink2lightBlue.setActivable(true);
            i1b_pink2lightBlue.setOpaque(false);

            i1b_pink2darkBlue = new Item1Button(null);
            i1b_pink2darkBlue.addActionListener(_cts);
            i1b_pink2darkBlue.setBorder(false);
            i1b_pink2darkBlue.setShifted();
            i1b_pink2darkBlue.setText("pink to darkBlue");
            i1b_pink2darkBlue.setActivable(true);
            i1b_pink2darkBlue.setOpaque(false);

            i1b_pink2red = new Item1Button(null);
            i1b_pink2red.addActionListener(_cts);
            i1b_pink2red.setBorder(false);
            i1b_pink2red.setShifted();
            i1b_pink2red.setText("pink to red");
            i1b_pink2red.setActivable(true);
            i1b_pink2red.setOpaque(false);

        }
        
        
        

        im_changeColors.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        im_changeColors.setSize(sizeOpen);
        im_changeColors.setSizeHeight(ViewSettings.getItemWidth());
        im_changeColors.setLocation(_x +  distance, 
                it_rotate.getY());
        
        
        



        i1b_red2yellow.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_red2yellow.setSize(
        		im_changeColors.getWidth(), i1b_red2yellow.getHeight());

        i1b_red2green.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_red2green.setSize(i1b_red2yellow.getSize());

        i1b_red2lightBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_red2lightBlue.setSize(i1b_red2yellow.getSize());

        i1b_red2darkBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_red2darkBlue.setSize(i1b_red2yellow.getSize());

        i1b_red2pink.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_red2pink.setSize(i1b_red2yellow.getSize());
        



        i1b_yellow2red.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_yellow2red.setSize(
        		im_changeColors.getWidth(), i1b_yellow2red.getHeight());

        i1b_yellow2green.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_yellow2green.setSize(i1b_yellow2red.getSize());

        i1b_yellow2lightBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_yellow2lightBlue.setSize(i1b_yellow2red.getSize());

        i1b_yellow2darkBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_yellow2darkBlue.setSize(i1b_yellow2red.getSize());

        i1b_yellow2pink.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_yellow2pink.setSize(i1b_yellow2red.getSize());
        



        i1b_green2yellow.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_green2yellow.setSize(
        		im_changeColors.getWidth(), i1b_green2yellow.getHeight());

        i1b_green2red.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_green2red.setSize(i1b_green2yellow.getSize());

        i1b_green2lightBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_green2lightBlue.setSize(i1b_green2yellow.getSize());

        i1b_green2darkBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_green2darkBlue.setSize(i1b_green2yellow.getSize());

        i1b_green2pink.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_green2pink.setSize(i1b_green2yellow.getSize());
        



        i1b_lightBlue2yellow.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_lightBlue2yellow.setSize(
        		im_changeColors.getWidth(), i1b_lightBlue2yellow.getHeight());

        i1b_lightBlue2green.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_lightBlue2green.setSize(i1b_lightBlue2yellow.getSize());

        i1b_lightBlue2red.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_lightBlue2red.setSize(i1b_lightBlue2yellow.getSize());

        i1b_lightBlue2darkBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_lightBlue2darkBlue.setSize(i1b_lightBlue2yellow.getSize());

        i1b_lightBlue2pink.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_lightBlue2pink.setSize(i1b_lightBlue2yellow.getSize());
        



        i1b_darkBlue2yellow.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_darkBlue2yellow.setSize(
        		im_changeColors.getWidth(), i1b_darkBlue2yellow.getHeight());

        i1b_darkBlue2green.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_darkBlue2green.setSize(i1b_darkBlue2yellow.getSize());

        i1b_darkBlue2lightBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_darkBlue2lightBlue.setSize(i1b_darkBlue2yellow.getSize());

        i1b_darkBlue2red.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_darkBlue2red.setSize(i1b_darkBlue2yellow.getSize());

        i1b_darkBlue2pink.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_darkBlue2pink.setSize(i1b_darkBlue2yellow.getSize());
        



        i1b_pink2yellow.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_pink2yellow.setSize(
        		im_changeColors.getWidth(), i1b_pink2yellow.getHeight());

        i1b_pink2green.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_pink2green.setSize(i1b_pink2yellow.getSize());

        i1b_pink2lightBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_pink2lightBlue.setSize(i1b_pink2yellow.getSize());

        i1b_pink2darkBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_pink2darkBlue.setSize(i1b_pink2yellow.getSize());

        i1b_pink2red.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_pink2red.setSize(i1b_pink2yellow.getSize());
        

        
        
		if (_paint) {

			im_changeColors.add(i1b_red2yellow);
			im_changeColors.add(i1b_red2green);
			im_changeColors.add(i1b_red2lightBlue);
			im_changeColors.add(i1b_red2darkBlue);
			im_changeColors.add(i1b_red2pink);

			im_changeColors.add(i1b_yellow2red);
			im_changeColors.add(i1b_yellow2green);
			im_changeColors.add(i1b_yellow2lightBlue);
			im_changeColors.add(i1b_yellow2darkBlue);
			im_changeColors.add(i1b_yellow2pink);

			im_changeColors.add(i1b_green2yellow);
			im_changeColors.add(i1b_green2red);
			im_changeColors.add(i1b_green2lightBlue);
			im_changeColors.add(i1b_green2darkBlue);
			im_changeColors.add(i1b_green2pink);

			im_changeColors.add(i1b_lightBlue2yellow);
			im_changeColors.add(i1b_lightBlue2green);
			im_changeColors.add(i1b_lightBlue2red);
			im_changeColors.add(i1b_lightBlue2darkBlue);
			im_changeColors.add(i1b_lightBlue2pink);

			im_changeColors.add(i1b_darkBlue2yellow);
			im_changeColors.add(i1b_darkBlue2green);
			im_changeColors.add(i1b_darkBlue2lightBlue);
			im_changeColors.add(i1b_darkBlue2red);
			im_changeColors.add(i1b_darkBlue2pink);

			im_changeColors.add(i1b_pink2yellow);
			im_changeColors.add(i1b_pink2green);
			im_changeColors.add(i1b_pink2lightBlue);
			im_changeColors.add(i1b_pink2darkBlue);
			im_changeColors.add(i1b_pink2red);


			im_changeColors.setActivable();
			im_changeColors.setItemsInRow((byte) 1);
			im_changeColors.setBorder(false);
	        super.add(im_changeColors);
	        //it_changeColors.removeScroll();
		}
        

        
        
        
        
		
		
		
		

		if (_paint) {
			

	        //pen 1
	        im_grayifyColors = new Item1Menu(false);
	        im_grayifyColors.setMenuListener(_ml);
	        im_grayifyColors.addMouseListener(_controlPaintStatus);
	        im_grayifyColors.setBorder(null);
	        im_grayifyColors.setBorder(false);
	        im_grayifyColors.setText("remove clr");
	        im_grayifyColors.changeClosedSizes(ViewSettings.getItemMenu1Width(), 
	                ViewSettings.getItemMenu1Height());
	        
			
            i1b_grayifyRed = new Item1Button(null);
            i1b_grayifyRed.addActionListener(_cts);
            i1b_grayifyRed.setBorder(false);
            i1b_grayifyRed.setShifted();
            i1b_grayifyRed.setText("red2gray");
            i1b_grayifyRed.setActivable(true);
            i1b_grayifyRed.setOpaque(false); 
            
            i1b_grayifyYellow = new Item1Button(null);
            i1b_grayifyYellow.addActionListener(_cts);
            i1b_grayifyYellow.setBorder(false);
            i1b_grayifyYellow.setShifted();
            i1b_grayifyYellow.setText("yellow2gray");
            i1b_grayifyYellow.setActivable(true);
            i1b_grayifyYellow.setOpaque(false);

            i1b_grayifyGreen = new Item1Button(null);
            i1b_grayifyGreen.addActionListener(_cts);
            i1b_grayifyGreen.setBorder(false);
            i1b_grayifyGreen.setShifted();
            i1b_grayifyGreen.setText("green2gray");
            i1b_grayifyGreen.setActivable(true);
            i1b_grayifyGreen.setOpaque(false);

            i1b_grayifyLightBlue = new Item1Button(null);
            i1b_grayifyLightBlue.addActionListener(_cts);
            i1b_grayifyLightBlue.setBorder(false);
            i1b_grayifyLightBlue.setShifted();
            i1b_grayifyLightBlue.setText("lightBlue2gray");
            i1b_grayifyLightBlue.setActivable(true);
            i1b_grayifyLightBlue.setOpaque(false);

            i1b_grayifyDarkBlue = new Item1Button(null);
            i1b_grayifyDarkBlue.addActionListener(_cts);
            i1b_grayifyDarkBlue.setBorder(false);
            i1b_grayifyDarkBlue.setShifted();
            i1b_grayifyDarkBlue.setText("darkBlue2gray");
            i1b_grayifyDarkBlue.setActivable(true);
            i1b_grayifyDarkBlue.setOpaque(false);

            i1b_grayifyPink = new Item1Button(null);
            i1b_grayifyPink.addActionListener(_cts);
            i1b_grayifyPink.setBorder(false);
            i1b_grayifyPink.setShifted();
            i1b_grayifyPink.setText("pink2gray");
            i1b_grayifyPink.setActivable(true);
            i1b_grayifyPink.setOpaque(false);


		}
        
        

        im_grayifyColors.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        im_grayifyColors.setSize(sizeOpen);
        im_grayifyColors.setSizeHeight(ViewSettings.getItemWidth());
        im_grayifyColors.setLocation(im_changeColors.getX() 
                + im_changeColors.getWidth() + distance, 
                im_changeColors.getY());
        
        i1b_rotateClockwise.setSize(
        		im_grayifyColors.getWidth(), i1b_rotateClockwise.getHeight());

        
		
		
		

        i1b_grayifyRed.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_grayifyRed.setSize(
        		im_changeColors.getWidth(), i1b_grayifyRed.getHeight());
		

        i1b_grayifyYellow.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_grayifyYellow.setSize(
        		im_changeColors.getWidth(), i1b_grayifyYellow.getHeight());

        i1b_grayifyGreen.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_grayifyGreen.setSize(i1b_grayifyYellow.getSize());

        i1b_grayifyLightBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_grayifyLightBlue.setSize(i1b_grayifyYellow.getSize());

        i1b_grayifyDarkBlue.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_grayifyDarkBlue.setSize(i1b_grayifyYellow.getSize());

        i1b_grayifyPink.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_grayifyPink.setSize(i1b_grayifyYellow.getSize());
        

		
		if (_paint) {

			im_grayifyColors.add(i1b_grayifyRed);
			im_grayifyColors.add(i1b_grayifyYellow);
			im_grayifyColors.add(i1b_grayifyGreen);
			im_grayifyColors.add(i1b_grayifyLightBlue);
			im_grayifyColors.add(i1b_grayifyDarkBlue);
			im_grayifyColors.add(i1b_grayifyPink);
			
			im_grayifyColors.setActivable();
			im_grayifyColors.setItemsInRow((byte) 1);
			im_grayifyColors.setBorder(false);
	        super.add(im_grayifyColors);
	        //it_changeColors.removeScroll();
		}
		
		
        
        
        
        
		
	}
	
	
	
	
	
	

	/**
	 * add pens .
	 */
	private void addPens(final ControlPaint _controlPaint, 
			
			// for focus animation
			final CTabTools _cp,
			
			
			final CPaintStatus _controlPaintStatus) {


		
		for (Pen pen_available : State.getPen_available()) {
			
			/*
			 * The pen which is inserted into the first penMenu
			 */
			Item1PenSelection i1 = new Item1PenSelection(
					pen_available.getName(),
					pen_available.getIconPath(),
					pen_available,
					1);
			
			//mouse listener and changeListener
			i1.addMouseListener(_cp);
			CPen cpen = new CPen(_controlPaint, _cp, i1, im_changePen, 
					Pen.clonePen(pen_available), 1, _controlPaintStatus, true);
			i1.addChangeListener(cpen);
			i1.addMouseListener(cpen);

			//add to first pen
			im_changePen.add(i1);
		}
    
	}


    /**
     * @return the jcb_points
     */
    public JCheckBox getJcb_points() {
        return jcb_points;
    }


    /**
     * @return the jcb_line
     */
    public JCheckBox getJcb_line() {
        return jcb_line;
    }


    /**
     * @return the jcb_maths
     */
    public JCheckBox getJcb_maths() {
        return jcb_maths;
    }


    /**
     * @return the jbtn_colors
     */
    public MButton [] getJbtn_colors() {
        return jbtn_colors;
    }


    /**
     * @return the tb_color
     */
    public Item1Button getTb_color() {
        return tb_color;
    }



	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}



	/**
	 * @return the tb_erase
	 */
	public Item1Button getTb_erase() {
		return tb_erase;
	}







	/**
	 * @return the tb_changeSize
	 */
	public Item1Button getTb_changeSize() {
		return tb_changeSize;
	}



	/**
	 * @return the it_rotate
	 */
	public Item1Menu getIt_rotate() {
		return it_rotate;
	}



	/**
	 * @return the i1b_red2yellow
	 */
	public Item1Button getI1b_red2yellow() {
		return i1b_red2yellow;
	}



	/**
	 * @return the i1b_red2green
	 */
	public Item1Button getI1b_red2green() {
		return i1b_red2green;
	}



	/**
	 * @return the i1b_red2lightBlue
	 */
	public Item1Button getI1b_red2lightBlue() {
		return i1b_red2lightBlue;
	}



	/**
	 * @return the i1b_red2darkBlue
	 */
	public Item1Button getI1b_red2darkBlue() {
		return i1b_red2darkBlue;
	}



	/**
	 * @return the i1b_red2pink
	 */
	public Item1Button getI1b_red2pink() {
		return i1b_red2pink;
	}



	/**
	 * @return the i1b_yellow2red
	 */
	public Item1Button getI1b_yellow2red() {
		return i1b_yellow2red;
	}



	/**
	 * @return the i1b_yellow2green
	 */
	public Item1Button getI1b_yellow2green() {
		return i1b_yellow2green;
	}



	/**
	 * @return the i1b_yellow2lightBlue
	 */
	public Item1Button getI1b_yellow2lightBlue() {
		return i1b_yellow2lightBlue;
	}



	/**
	 * @return the i1b_yellow2darkBlue
	 */
	public Item1Button getI1b_yellow2darkBlue() {
		return i1b_yellow2darkBlue;
	}



	/**
	 * @return the i1b_yellow2pink
	 */
	public Item1Button getI1b_yellow2pink() {
		return i1b_yellow2pink;
	}



	/**
	 * @return the i1b_green2yellow
	 */
	public Item1Button getI1b_green2yellow() {
		return i1b_green2yellow;
	}



	/**
	 * @return the i1b_green2red
	 */
	public Item1Button getI1b_green2red() {
		return i1b_green2red;
	}



	/**
	 * @return the i1b_green2lightBlue
	 */
	public Item1Button getI1b_green2lightBlue() {
		return i1b_green2lightBlue;
	}



	/**
	 * @return the i1b_green2darkBlue
	 */
	public Item1Button getI1b_green2darkBlue() {
		return i1b_green2darkBlue;
	}



	/**
	 * @return the i1b_green2pink
	 */
	public Item1Button getI1b_green2pink() {
		return i1b_green2pink;
	}



	/**
	 * @return the i1b_lightBlue2yellow
	 */
	public Item1Button getI1b_lightBlue2yellow() {
		return i1b_lightBlue2yellow;
	}



	/**
	 * @return the i1b_lightBlue2green
	 */
	public Item1Button getI1b_lightBlue2green() {
		return i1b_lightBlue2green;
	}



	/**
	 * @return the i1b_lightBlue2red
	 */
	public Item1Button getI1b_lightBlue2red() {
		return i1b_lightBlue2red;
	}



	/**
	 * @return the i1b_lightBlue2darkBlue
	 */
	public Item1Button getI1b_lightBlue2darkBlue() {
		return i1b_lightBlue2darkBlue;
	}



	/**
	 * @return the i1b_lightBlue2pink
	 */
	public Item1Button getI1b_lightBlue2pink() {
		return i1b_lightBlue2pink;
	}



	/**
	 * @return the i1b_darkBlue2yellow
	 */
	public Item1Button getI1b_darkBlue2yellow() {
		return i1b_darkBlue2yellow;
	}



	/**
	 * @return the i1b_darkBlue2green
	 */
	public Item1Button getI1b_darkBlue2green() {
		return i1b_darkBlue2green;
	}



	/**
	 * @return the i1b_darkBlue2lightBlue
	 */
	public Item1Button getI1b_darkBlue2lightBlue() {
		return i1b_darkBlue2lightBlue;
	}



	/**
	 * @return the i1b_darkBlue2red
	 */
	public Item1Button getI1b_darkBlue2red() {
		return i1b_darkBlue2red;
	}



	/**
	 * @return the i1b_darkBlue2pink
	 */
	public Item1Button getI1b_darkBlue2pink() {
		return i1b_darkBlue2pink;
	}



	/**
	 * @return the i1b_pink2yellow
	 */
	public Item1Button getI1b_pink2yellow() {
		return i1b_pink2yellow;
	}



	/**
	 * @return the i1b_pink2green
	 */
	public Item1Button getI1b_pink2green() {
		return i1b_pink2green;
	}



	/**
	 * @return the i1b_pink2lightBlue
	 */
	public Item1Button getI1b_pink2lightBlue() {
		return i1b_pink2lightBlue;
	}



	/**
	 * @return the i1b_pink2darkBlue
	 */
	public Item1Button getI1b_pink2darkBlue() {
		return i1b_pink2darkBlue;
	}



	/**
	 * @return the i1b_pink2red
	 */
	public Item1Button getI1b_pink2red() {
		return i1b_pink2red;
	}



	/**
	 * @return the i1b_grayifyRed
	 */
	public Item1Button getI1b_grayifyRed() {
		return i1b_grayifyRed;
	}



	/**
	 * @return the i1b_grayifyYellow
	 */
	public Item1Button getI1b_grayifyYellow() {
		return i1b_grayifyYellow;
	}



	/**
	 * @return the i1b_grayifyGreen
	 */
	public Item1Button getI1b_grayifyGreen() {
		return i1b_grayifyGreen;
	}



	/**
	 * @return the i1b_grayifyLightBlue
	 */
	public Item1Button getI1b_grayifyLightBlue() {
		return i1b_grayifyLightBlue;
	}



	/**
	 * @return the i1b_grayifyDarkBlue
	 */
	public Item1Button getI1b_grayifyDarkBlue() {
		return i1b_grayifyDarkBlue;
	}



	/**
	 * @return the i1b_grayifyPink
	 */
	public Item1Button getI1b_grayifyPink() {
		return i1b_grayifyPink;
	}



	/**
	 * @return the i1b_scanEdit
	 */
	public Item1Menu getIm_scanEdit() {
		return im_scanEdit;
	}







	/**
	 * @return the i1b_scanEdit
	 */
	public Item1Button geti1b_scanOk() {
		return i1b_scanOK;
	}



	/**
	 * @return the im_changePen
	 */
	public Item1Menu getIm_changePen() {
		return im_changePen;
	}



	/**
	 * @param im_changePen the im_changePen to set
	 */
	public void setIm_changePen(Item1Menu im_changePen) {
		this.im_changePen = im_changePen;
	}



	/**
	 * @return the im_changeColors
	 */
	public Item1Menu getIm_changeColors() {
		return im_changeColors;
	}



	/**
	 * @param im_changeColors the im_changeColors to set
	 */
	public void setIm_changeColors(Item1Menu im_changeColors) {
		this.im_changeColors = im_changeColors;
	}



	/**
	 * @return the im_grayifyColors
	 */
	public Item1Menu getIm_grayifyColors() {
		return im_grayifyColors;
	}



	/**
	 * @param im_grayifyColors the im_grayifyColors to set
	 */
	public void setIm_grayifyColors(Item1Menu im_grayifyColors) {
		this.im_grayifyColors = im_grayifyColors;
	}



	/**
	 * @return the mb_saturation
	 */
	public MultipleBar getMb_saturation() {
		return mb_saturation;
	}



	/**
	 * @param mb_saturation the mb_saturation to set
	 */
	public void setMb_saturation(MultipleBar mb_saturation) {
		this.mb_saturation = mb_saturation;
	}



	/**
	 * @return the cp_saturation
	 */
	public CheckedComponent getCp_saturation() {
		return cp_saturation;
	}



	/**
	 * @param cp_saturation the cp_saturation to set
	 */
	public void setCp_saturation(CheckedComponent cp_saturation) {
		this.cp_saturation = cp_saturation;
	}



	/**
	 * @return the cp_threshold
	 */
	public CheckedComponent getCp_threshold() {
		return cp_threshold;
	}



	/**
	 * @param cp_threshold the cp_threshold to set
	 */
	public void setCp_threshold(CheckedComponent cp_threshold) {
		this.cp_threshold = cp_threshold;
	}



	/**
	 * @return the mb_threshold
	 */
	public MultipleBar getMb_threshold() {
		return mb_threshold;
	}



	/**
	 * @param mb_threshold the mb_threshold to set
	 */
	public void setMb_threshold(MultipleBar mb_threshold) {
		this.mb_threshold = mb_threshold;
	}



	/**
	 * @return the cp_value
	 */
	public CheckedComponent getCp_value() {
		return cp_value;
	}



	/**
	 * @param cp_value the cp_value to set
	 */
	public void setCp_value(CheckedComponent cp_value) {
		this.cp_value = cp_value;
	}



	/**
	 * @return the mb_value
	 */
	public MultipleBar getMb_value() {
		return mb_value;
	}



	/**
	 * @param mb_value the mb_value to set
	 */
	public void setMb_value(MultipleBar mb_value) {
		this.mb_value = mb_value;
	}



}
