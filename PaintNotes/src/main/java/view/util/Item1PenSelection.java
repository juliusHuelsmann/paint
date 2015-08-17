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


//import java.awt components
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;



//import java.swing components
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;



//import rotatatble buttons and panels
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import view.util.mega.MSlider;
import model.objects.pen.Pen;
//constants and viewSettings
import model.settings.Constants;
import model.util.paint.Utils;

/**
 * Class item pen selection, contains one pen which can be selected.
 * 
 * @author Julius Huelsmann
 * @version %I%,%U%
 */
@SuppressWarnings("serial")
public class Item1PenSelection extends MPanel {

	/**
	 * JLabel containing information on name and thickness and
	 * an image identifying the special type.
	 */
	private MLabel jlbl_image, jlbl_name, jlbl_thickness;
	
	/**
	 * JButton for performing an action.
	 */
	private JButton jbtn_select;
	
	/**
	 * Slider with which it is possible to change sickness.
	 */
	private MSlider jsl_thickness;
	
	/**
	 * contains whether this pen is the pen selected for the current working 
	 * pen (position 0 or 1).
	 */
	private boolean selected = false;
	
	/**
	 * Contains the path of the current image.
	 */
	private String imagePath;
	
	
	/**
	 * The selected pen id.
	 */
	private int pen_selection;
	
	
	/**
	 * @return the pen_selection.
	 */
	public final int getPenSelection() {
		return pen_selection;
	}
	
	/**
	 * The pen.
	 */
	private Pen pen;
	

	/**
	 * @return the pen.
	 */
	public final Pen getPen() {
		return pen;
	}
	/**
	 * Constructor: creates graphical user interface.
	 * @param _title the title of the item
	 * @param _imagePath the path of the image which is given to the itemMenu 
	 *         and painted.
	 * user clicked at this button
	 */
	public Item1PenSelection(final String _title, final String _imagePath,
			final Pen _pen, final int _penSelection) {
		
		//initialize JFrame and alter settings
		super();
		super.setLayout(null);
		super.setOpaque(false);
		super.setBorder(BorderFactory.createMatteBorder(
		        0, 0, 1, 0, Color.lightGray));

		//save current values
		this.imagePath = _imagePath;
		this.pen = _pen;
		this.pen_selection = _penSelection;
		
		//initialize components
		jlbl_image = new MLabel();
		jlbl_image.setBorder(new LineBorder(Color.black));
		jlbl_image.setOpaque(false);
		super.add(jlbl_image);

		jlbl_name = new MLabel(_title);
		jlbl_name.setBorder(null);
		jlbl_name.setOpaque(false);
		super.add(jlbl_name);
		
		jsl_thickness = new MSlider(1, Constants.MAX_PEN_THICKNESS, 2);
		jsl_thickness.setPaintTicks(true);
		jsl_thickness.setMajorTickSpacing(
		        Constants.MAX_PEN_THICKNESS / (2 + 2));
		jsl_thickness.setOpaque(false);
		jsl_thickness.setMinorTickSpacing(1);
		jsl_thickness.setPaintTrack(true);
		jlbl_thickness = new MLabel("1px");
		jlbl_thickness.setBorder(null);
		jlbl_thickness.setOpaque(false);
		super.add(jlbl_thickness);

        super.add(jsl_thickness);
	
		jbtn_select = new VButtonWrapper(this);
		jbtn_select.setContentAreaFilled(false);
		jbtn_select.setOpaque(false);
		jbtn_select.setBorder(null);
		jbtn_select.setFocusable(false);
		super.add(jbtn_select);
	}

	/**
	 * add ActionListener to JButton.
	 * @param _l the ActionListener.
	 */
	public final void addChangeListener(final ChangeListener _l) {
		jsl_thickness.addChangeListener(_l);
	}
	
	/**
	 * add ActionListener to JButton.
	 * @param _l the ActionListener.
	 */
	public final void addActionListener(final ActionListener _l) {
		jbtn_select.addActionListener(_l);
	}
	
	/**
	 * add MouseListener to JButton jbtn_select.
	 * @param _l the MouseListener
	 */
	public final void addMouseListener(final MouseListener _l) {
		jbtn_select.addMouseListener(_l);
	}
	
	
	/**
	 * return instance of JButton which may have caused an actionEvent.
	 * @return the jbtn_select
	 */
	public final JButton getActionCause() {
		return jbtn_select;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setSize(final int _width, final int _height) {
		
		//set size of JPanel and JButton for ActionEvents
		super.setSize(_width, _height);
		jbtn_select.setSize(_width, _height);
		
		//set bounds of the image
		final int distance = 5;
		final int widthThicknessLabel = distance * (distance + 2); 
		
		jlbl_image.setBounds(distance, distance, 
				_height - 2 * distance, _height - 2 * distance);
		jlbl_name.setBounds(jlbl_image.getX() + jlbl_image.getWidth() 
				+ distance, distance, _width / (2 + 1), _height - 2 * distance);
		jsl_thickness.setBounds(jlbl_name.getX() + jlbl_name.getWidth()
				+ distance, distance, 
				_width - distance * 2 - widthThicknessLabel
				- jlbl_name.getWidth() - jlbl_name.getX(), 
				_height - 2 * distance);
		
		
		jlbl_thickness.setBounds(
		        jsl_thickness.getX() + jsl_thickness.getWidth(),
		        jsl_thickness.getY(),
		        widthThicknessLabel,
                _height);
		
		//set image icon if path is set
		if (!imagePath.equals("")) {
			
			jlbl_image.setIcon(new ImageIcon(Utils.resizeImage(jlbl_image
					.getWidth(), jlbl_image.getHeight(), imagePath)));
		}
	}

	
	/**
	 * @return the selected
	 */
	public final boolean isSelected() {
		return selected;
	}


	/**
	 * @param _selected the selected to set
	 */
	public final void setSelected(final boolean _selected) {
		this.selected = _selected;
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
	 * @return the jlbl_thickness
	 */
	public final MLabel getJlbl_thickness() {
		return jlbl_thickness;
	}
}
