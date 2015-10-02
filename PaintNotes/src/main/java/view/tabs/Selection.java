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
import view.util.Item1Menu;
import view.util.Item1Button;
import view.util.Item1PenSelection;
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
    private Item1Menu it_color, it_changePen;

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
        initOthers(x, true, _cp, _controlPaintStatus, _cts, _cp);
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
	
	
	/**
	 * The item1Menu for the first pen.
	 */
	private Item1Menu it_rotate;
	
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
	private void initOthers(final int _x, final boolean _paint,
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
	
            it_changePen = new Item1Menu(false);
            it_changePen.removeScroll();
            it_changePen.setMenuListener(_ml);
            it_changePen.addMouseListener(_controlPaintStatus);

		}


        final Dimension sizeIT = new Dimension(550, 550);
		it_changePen.setSize(sizeIT);
		it_changePen.setSizeHeight(50);
		it_changePen.setLocation(tb_changeSize.getX() + tb_changeSize.getWidth() + distance, 
                tb_changeSize.getY());
		if (_paint) {

			it_changePen.setBorder(null);
			it_changePen.setText("Change pen");
			it_changePen.setItemsInRow((byte) 1);
			it_changePen.setActivable();
			it_changePen.setBorder(false);
		}
		it_changePen.setIcon(Constants.VIEW_TAB_INSRT_SELECT);
		if (_paint) {
			addPens(_cp, _cp.getcTabPaint(), _controlPaintStatus);
        	super.add(it_changePen);

	
	        //pen 1
	        it_rotate = new Item1Menu(false);
	        it_rotate.setMenuListener(_ml);
	        it_rotate.addMouseListener(_controlPaintStatus);
	        it_rotate.setBorder(null);
	        it_rotate.setBorder(false);
	        it_rotate.setText("Drehen/Spiegeln");
	        
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
		}
		final int distance = 5;
        final int contentHeight = ViewSettings.getItemMenu1Height() - 2 * distance;
        final Dimension sizeOpen = new Dimension(
        		ViewSettings.getItemMenu1Width() * (2 + 2), 
        		 contentHeight * (2 + 2) + 5);
        
        it_rotate.changeClosedSizes(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());

        it_rotate.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        it_rotate.setSize(sizeOpen);
        it_rotate.setSizeHeight(ViewSettings.getItemWidth());
        it_rotate.setLocation(it_changePen.getX() 
                + it_changePen.getWidth() + distance, 
                it_changePen.getY());
        
        i1b_rotateClockwise.setSize(
        		it_rotate.getWidth(), i1b_rotateClockwise.getHeight());
        i1b_rotateCounterclockwise.setSize(i1b_rotateClockwise.getSize());
        i1b_rotateFree.setSize(i1b_rotateClockwise.getSize());
        i1b_mirrorHoriz.setSize(i1b_rotateClockwise.getSize());
        i1b_mirrorVert.setSize(i1b_rotateClockwise.getSize());

        i1b_rotateClockwise.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_rotateCounterclockwise.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_rotateFree.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_mirrorHoriz.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
        i1b_mirrorVert.setIcon(Constants.VIEW_TB_PIPETTE_PATH);
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
	        it_rotate.removeScroll();
		}
        

        int xLocationSeparation = it_rotate.getWidth() + it_rotate.getX()
                + ViewSettings.getDistanceBeforeLine();
        insertSectionStuff("Farben", _x, xLocationSeparation, 1, _paint);
//        return xLocationSeparation + ViewSettings.getDistanceBetweenItems();
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
			CPen cpen = new CPen(_controlPaint, _cp, i1, it_changePen, 
					Pen.clonePen(pen_available), 1, _controlPaintStatus, true);
			i1.addChangeListener(cpen);
			i1.addMouseListener(cpen);

			//add to first pen
			it_changePen.add(i1);
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





}
