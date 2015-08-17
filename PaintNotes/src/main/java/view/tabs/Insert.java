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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import control.ControlPaint;
import control.forms.CPaintStatus;
import model.settings.Constants;
import model.settings.ViewSettings;
import view.forms.Help;
import view.util.Item2;
import view.util.Item2Menu;
import view.util.Item1Button;
import view.util.mega.MLabel;
import view.util.mega.MTextField;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class Insert extends Tab {

    /**
     * The item2menus.
     */
    private Item2Menu ia_geo, ia_maths, ia_diagram;
    
    /**
     * ia_geo Item2 menus.
     */
    private Item2 i2_g_line, i2_g_rect, i2_g_rect_round, 
    i2_g_triangle, i2_g_arch, i2_g_curve, i2_g_curve2,
    i2_g_round, i2_g_archFilled, i2_g_rectFilled, i2_g_triangleFilled, 
    i2_g_roundFilled, i2_g_rect_roundFilled, i2_d_diagramm;
    
    
    /**
     * The item1button which shows the currently selected form.
     */
    private Item1Button tb_selected;
    
    
    /**
     * Titles for the amount of lines and rows of diagrams.
     */
    private MLabel jlbl_amountLines, jlbl_amountRows;
    
    /**
     * Input fields for the amount of lines and rows of diagrams.
     */
    private MTextField jtf_amountLines, jtf_amountRows;

    /**
     * Constants.
     */
    private final int distance = 5, itemButtonSize = 128, locationX = 285;

    
    /**
     * Empty utility class constructor.
     */
	public Insert(final CPaintStatus _cps, 
			final ControlPaint _cp) { 
	    super(2 + 2);


		//initialize JPanel and alter settings
		super.setOpaque(false);
		super.setLayout(null);

		
        /*
         * 
         * 
         * 
         * 
         * 
         */
		tb_selected = new Item1Button(null);
		tb_selected.setOpaque(true);
		tb_selected.setSize(itemButtonSize, ViewSettings.getItemMenu1Height());
		tb_selected.setLocation(distance, distance);
		tb_selected.setText("Viereck");
		tb_selected.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(Color.black),
				new LineBorder(Color.white)));
		tb_selected.setActivable(false);
		tb_selected.setIcon(Constants.VIEW_TAB_INSRT_SELECT);
		super.add(tb_selected);
		
		insertSectionStuff("ausgewaehlt", tb_selected.getX(), 
		        tb_selected.getX() + tb_selected.getWidth(), 0, true);
        /*
         * 
         * 
         * 
         * 
         * 
         */
        initializeGeo(getJlbl_separation()[0].getX(), _cp, _cps);

        insertSectionStuff("geometrische Formen", 
                ia_geo.getX(), ia_geo.getX() + ia_geo.getWidth(), 1, true);
        /*
         * 
         * 
         * 
         * 
         * 
         */
        ia_maths = new Item2Menu();
        ia_maths.setMenuListener(_cp);
        ia_maths.setLocation(getJlbl_separation()[1].getX() 
                + distance, distance);
        ia_maths.setSize(ia_geo.getWidth(), ViewSettings.getItemMenu1Height());
		super.add(ia_maths);
		
        insertSectionStuff("mathematische Formen", ia_maths.getX(), 
                ia_maths.getX() + ia_maths.getWidth(), 2, true);
        /*
         * 
         * 
         * 
         * 
         * 
         */
        ia_diagram = new Item2Menu();
        ia_diagram.setMenuListener(_cp);
     	ia_diagram.setSize(ia_geo.getWidth(), 
     	        ViewSettings.getItemMenu1Height());
    	ia_diagram.setLocation(getJlbl_separation()[2].getX() 
    	        + distance, distance);
		super.add(ia_diagram);

        i2_d_diagramm = new Item2();
        i2_d_diagramm.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_d_diagramm.addMouseListener(_cps);
        i2_d_diagramm.setTitle("line");
        ia_diagram.add(i2_d_diagramm);
        i2_d_diagramm.setIcon(Constants.VIEW_TAB_INSRT_PAINT_LINE);

        final int widthLabel = 125, heightLabel = 20;
        jlbl_amountLines = new MLabel("# lines");
        jlbl_amountLines.setLocation(distance 
                + ia_diagram.getX() + ia_diagram.getWidth(),
                ia_diagram.getY());
        jlbl_amountLines.setBorder(null);
        jlbl_amountLines.setOpaque(false);
        jlbl_amountLines.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_amountLines.setSize(widthLabel, heightLabel);
        super.add(jlbl_amountLines);
        
        jtf_amountLines = new MTextField();
        jtf_amountLines.setLocation(distance 
                + jlbl_amountLines.getX() + jlbl_amountLines.getWidth(),
                ia_diagram.getY());
        jtf_amountLines.setOpaque(false);
        jtf_amountLines.setFont(ViewSettings.GENERAL_FONT_ITEM_SMALL);
        jtf_amountLines.setSize(widthLabel, heightLabel);
        super.add(jtf_amountLines);
        
        jlbl_amountRows = new MLabel("# rows");
        jlbl_amountRows.setLocation(distance 
                + ia_diagram.getX() + ia_diagram.getWidth(),
                jlbl_amountLines.getY() + jlbl_amountLines.getHeight() 
                + distance);
        jlbl_amountRows.setBorder(null);
        jlbl_amountRows.setOpaque(false);
        jlbl_amountRows.setFont(ViewSettings.GENERAL_FONT_ITEM_PLAIN);
        jlbl_amountRows.setSize(widthLabel, heightLabel);
        super.add(jlbl_amountRows);

        jtf_amountRows = new MTextField();
        jtf_amountRows.setLocation(distance 
                + jlbl_amountRows.getX() + jlbl_amountRows.getWidth(),
                jlbl_amountRows.getY());
        jtf_amountRows.setOpaque(false);
        jtf_amountRows.setFont(ViewSettings.GENERAL_FONT_ITEM_SMALL);
        jtf_amountRows.setSize(widthLabel, heightLabel);
        super.add(jtf_amountRows);
        

        insertSectionStuff("Diagramme", ia_diagram.getX(), 
                jtf_amountRows.getX() + jtf_amountRows.getWidth(), 2 + 1, true);
        
	
	}

	/**
	 * Initialize geometric objects.
	 * @param _x the start x coordinate
	 */
	private void initializeGeo(
			final int _x, 
			final ControlPaint _cp,
			final CPaintStatus _cps) {

        ia_geo = new Item2Menu();
        ia_geo.setMenuListener(_cp);
        ia_geo.setLocation(_x , tb_selected.getY());
        ia_geo.setSize(locationX, ViewSettings.getItemMenu1Height());
        ia_geo.setItemsInRow(2 + 2);
        super.add(ia_geo);

        i2_g_line = new Item2();
        i2_g_line.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_line.addMouseListener(_cps);
        i2_g_line.setTitle("line");
        ia_geo.add(i2_g_line);
        i2_g_line.setIcon(Constants.VIEW_TAB_INSRT_PAINT_LINE);

        i2_g_curve = new Item2();
        i2_g_curve.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_curve.addMouseListener(_cps);
        i2_g_curve.setTitle("curve");
        ia_geo.add(i2_g_curve);
        i2_g_curve.setIcon(Constants.VIEW_TAB_INSRT_PAINT_CURVE);

        i2_g_curve2 = new Item2();
        i2_g_curve2.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_curve2.addMouseListener(_cps);
        i2_g_curve2.setTitle("curve");
        ia_geo.add(i2_g_curve2);
        i2_g_curve2.setIcon(Constants.VIEW_TAB_INSRT_PAINT_CURVE);

        i2_g_arch = new Item2();
        i2_g_arch.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_arch.addMouseListener(_cps);
        i2_g_arch.setTitle("arch");
        ia_geo.add(i2_g_arch);
        i2_g_arch.setIcon(Constants.VIEW_TAB_INSRT_PAINT_ARCH);

        i2_g_round = new Item2();
        i2_g_round.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_round.addMouseListener(_cps);
        i2_g_round.setTitle("round");
        ia_geo.add(i2_g_round);
        i2_g_round.setIcon(Constants.VIEW_TAB_INSRT_PAINT_CIRCLE);

        i2_g_rect = new Item2();
        i2_g_rect.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_rect.addMouseListener(_cps);
        i2_g_rect.setTitle("rect");
        ia_geo.add(i2_g_rect);
        i2_g_rect.setIcon(Constants.VIEW_TAB_INSRT_PAINT_RECT);

        i2_g_rect_round = new Item2();
        i2_g_rect_round.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_rect_round.addMouseListener(_cps);
        i2_g_rect_round.setTitle("rect round");
        ia_geo.add(i2_g_rect_round);
        i2_g_rect_round.setIcon(Constants.VIEW_TAB_INSRT_PAINT_RECTROUND);
        
        i2_g_triangle = new Item2();
        i2_g_triangle.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_triangle.addMouseListener(_cps);
        i2_g_triangle.setTitle("triangle");
        ia_geo.add(i2_g_triangle);
        i2_g_triangle.setIcon(Constants.VIEW_TAB_INSRT_PAINT_TRIANGLE);

        i2_g_roundFilled = new Item2();
        i2_g_roundFilled.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_roundFilled.addMouseListener(_cps);
        i2_g_roundFilled.setTitle("round filled");
        ia_geo.add(i2_g_roundFilled);
        i2_g_roundFilled.setIcon(Constants.VIEW_TAB_INSRT_PAINT_CIRCLE_F);

        i2_g_rectFilled = new Item2();
        i2_g_rectFilled.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_rectFilled.addMouseListener(_cps);
        i2_g_rectFilled.setTitle("rectangle filled");
        ia_geo.add(i2_g_rectFilled);
        i2_g_rectFilled.setIcon(
        		Constants.VIEW_TAB_INSRT_PAINT_RECT_F);

        i2_g_rect_roundFilled = new Item2();
        i2_g_rect_roundFilled.setItemActivityListener(
        		_cp.getUtilityControlItem2());
        i2_g_rect_roundFilled.addMouseListener(_cps);
        i2_g_rect_roundFilled.setTitle("rectangle round filled");
        ia_geo.add(i2_g_rect_roundFilled);
        i2_g_rect_roundFilled.setIcon(
        		Constants.VIEW_TAB_INSRT_PAINT_RECTROUND_F);
        
        i2_g_triangleFilled = new Item2();
        i2_g_triangleFilled.setItemActivityListener(
        		_cp.getUtilityControlItem2());
        i2_g_triangleFilled.addMouseListener(_cps);
        i2_g_triangleFilled.setTitle("triangle filled");
        ia_geo.add(i2_g_triangleFilled);
        i2_g_triangleFilled.setIcon(Constants.VIEW_TAB_INSRT_PAINT_TRIANGLE_F);

        i2_g_archFilled = new Item2();
        i2_g_archFilled.setItemActivityListener(_cp.getUtilityControlItem2());
        i2_g_archFilled.addMouseListener(_cps);
        i2_g_archFilled.setTitle("arch filled");
        ia_geo.add(i2_g_archFilled);
        i2_g_archFilled.setIcon(Constants.VIEW_TAB_INSRT_PAINT_ARCH_F);

	}

	
	
	

    /**
     * @return the ia_maths
     */
    public Item2Menu getIa_maths() {
        return ia_maths;
    }

    /**
     * @return the ia_geo
     */
    public Item2Menu getIa_geo() {
        return ia_geo;
    }

    /**
     * @return the ia_diagram
     */
    public Item2Menu getIa_diagram() {
        return ia_diagram;
    }

    /**
     * @return the i2_g_line
     */
    public Item2 getI2_g_line() {
        return i2_g_line;
    }

    /**
     * @return the i2_g_rect
     */
    public Item2 getI2_g_rect() {
        return i2_g_rect;
    }

    /**
     * @return the i2_g_triangle
     */
    public Item2 getI2_g_triangle() {
        return i2_g_triangle;
    }

    /**
     * @return the i2_g_arch
     */
    public Item2 getI2_g_arch() {
        return i2_g_arch;
    }

    /**
     * @return the i2_g_curve
     */
    public Item2 getI2_g_curve() {
        return i2_g_curve;
    }

    /**
     * @return the i2_g_archFilled
     */
    public Item2 getI2_g_archFilled() {
        return i2_g_archFilled;
    }

    /**
     * @return the tb_selected
     */
    public Item1Button getTb_selected() {
        return tb_selected;
    }

    /**
     * @return the i2_d_diagramm
     */
    public Item2 getI2_d_diagramm() {
        return i2_d_diagramm;
    }

    /**
     * @return the jtf_amountLines
     */
    public MTextField getJtf_amountLines() {
        return jtf_amountLines;
    }


    /**
     * @return the jtf_amountRows
     */
    public MTextField getJtf_amountRows() {
        return jtf_amountRows;
    }

    /**
     * @return the i2_g_rectFilled
     */
    public Item2 getI2_g_rectFilled() {
        return i2_g_rectFilled;
    }


    /**
     * @return the i2_g_triangleFilled
     */
    public Item2 getI2_g_triangleFilled() {
        return i2_g_triangleFilled;
    }


    /**
     * @return the i2_g_roundFilled
     */
    public Item2 getI2_g_roundFilled() {
        return i2_g_roundFilled;
    }


    /**
     * @return the i2_g_rect_roundFilled
     */
    public Item2 getI2_g_rect_roundFilled() {
        return i2_g_rect_roundFilled;
    }

    /**
     * @return the i2_g_curve2
     */
    public Item2 getI2_g_curve2() {
        return i2_g_curve2;
    }

	@Override
	public void initializeHelpListeners(JFrame _jf, Help _c) {
		// TODO Auto-generated method stub
		
	}
}
