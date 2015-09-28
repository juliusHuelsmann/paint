//package declaration
package model.objects.painting;


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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.xml.ws.Dispatch;

import control.ContorlPicture;
import control.ControlSelectionTransform;
import control.forms.tabs.CTabSelection;
import view.forms.Console;
import view.forms.Message;
import view.forms.Page;
import view.tabs.Insert;
import view.tabs.Debug;
import model.objects.PictureOverview;
import model.objects.Project;
import model.objects.history.HistorySession;
import model.objects.painting.po.POInsertion;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectImage;
import model.objects.painting.po.PaintObjectPdf;
import model.objects.painting.po.PaintObjectPen;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.painting.po.diag.PODiagramm;
import model.objects.painting.po.geo.POArch;
import model.objects.painting.po.geo.POArchFilled;
import model.objects.painting.po.geo.POCurve;
import model.objects.painting.po.geo.POLine;
import model.objects.painting.po.geo.PORectangle;
import model.objects.painting.po.geo.POTriangle;
import model.objects.painting.po.geo.POTriangleFilled;
import model.objects.painting.po.geo.PoRectangleFilled;
import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.objects.pen.normal.Pencil;
import model.objects.pen.special.PenSelection;
import model.settings.Constants;
import model.settings.State;
import model.util.DPoint;
import model.util.DRect;
import model.util.Util;
import model.util.adt.list.SecureList;
import model.util.adt.list.SecureListSort;
import model.util.paint.Utils;
import model.util.pdf.XDocument;

/**
 * Picture class which contains all the not selected and selected painted items
 * contained in two lists of PaintObjects.
 * 
 * It handles the creation, changing and removing of PaintObjects, selection
 * methods,
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Picture implements Serializable {

	/**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
	 */
	private static final long serialVersionUID = 29882073145378042L;


	/**
	 * List of PaintObjects into which all non-selected paintObjects are added 
	 * sorted by their y coordinate.
	 */
	private SecureListSort<PaintObject> ls_po_sortedByY;
	
	
	/**
	 * List of PaintObjects which contains all currently selected items.
	 * These items have to be saved somewhere outside the first list because 
	 * they can be transformed or removed.
	 */
	private SecureList<PaintObject> ls_poSelected;

	/**
	 * Current PaintObjectPen which can be altered and afterwards inserted into
	 * the list of PaintObjects sorted by X.
	 * 
	 * If an image is inserted from clipboard or an existing PaintObjectPen is
	 * transformed into an image, that can be done directly e.g. by using the
	 * addPaintObjectImage method. This can not be done with the PaintObjectPens
	 * because their creation is a continued process (the user draws a line;
	 * first the PaintObjectPen has to be created, afterwards points are added
	 * to the PaintObjectPen. When the user released the mouse, the paintObject
	 * is added to list of paintObjects and the PaintObjectPen po_current is
	 * reset to null.
	 */
	private PaintObjectPen po_current;

	/**
	 * The currently selected pen with which the current PaintObjectPen is
	 * painted to the screen. Because of this, pen_current is copied and
	 * afterwards given to new created PaintObject.
	 */
	private Pen pen_current;

	/**
	 * Current id of following PaintObject. Is given to the PaintObjectPen and
	 * increased afterwards. If the user removes PaintObjects the id is not
	 * decreased; there may occur gaps in id.
	 */
	private int currentId;

	/**
	 * The history.
	 */
	private HistorySession history; 
	
	/**
	 * Empty utility class constructor because the Picture instance has to exist
	 * in process of initialization of Picture attributes.
	 */
	public Picture() { }

	/**
	 * Initialization method of class Picture. Calls the reload() method which
	 * creates a new instance of sorted PaintObject list and sets up the
	 * currentID.
	 */
	public void initialize(final HistorySession _history) {
		this.history = _history;
		reload();
	}
	

	/**
	 * The reload method creates a new instance of sorted PaintObject list and
	 * sets up the currentID. This method is called in initialization process
	 * and if the user creates a new page or loads a file that does only consist
	 * of images (e.g. in PNG of PDF format).
	 */
	public void reload() {

		// initialize both lists ordered by
		this.ls_po_sortedByY = new SecureListSort<PaintObject>();

		// save current id
		this.currentId = 0;
		
		//initialize the standard pen and the standard operation id in 
		//controller class for pen1 selected.
		State.applyStandardPen(this);
	}

	/**
	 * Increases id and returns the next id for being able to create
	 * PaintObjects. From outside the Picture class without using the create.
	 * method. Is mainly used for testing purpose.
	 * 
	 * @return the next id.
	 */
	public synchronized int getIncreaseCID() {
		currentId++;
		return currentId - 1;
	}

	/**
	 * Method for creating a new PaintObjectImage which is not directly added to
	 * a list but returned to the demanding method.
	 * 
	 * @param _bi
	 *            the BufferedImage of which the new created PaintObjectImage
	 *            consists
	 * 
	 * @return the new created PaintObjectImage.
	 */
	public PaintObjectImage createPOI(final BufferedImage _bi) {
		return new PaintObjectImage(getIncreaseCID(), _bi, this);
	}

	/**
	 * Method for creating a new PaintObjectImage which is not directly added to
	 * a list but returned to the demanding method.
	 * 
	 * @param _bi
	 *            the BufferedImage of which the new created PaintObjectImage
	 *            consists
	 * 
	 * @return the new created PaintObjectImage.
	 */
	public PaintObjectPdf createPDF(final Project _pro, final int _pageNr) {
		return new PaintObjectPdf(getIncreaseCID(), _pro, _pageNr, this);
	}

	/**
	 * Method for creating a new PaintObjectWriting which is not directly added
	 * to a list but returned to the demanding method.
	 * 
	 * 
	 * @param _pen
	 *            the pen which will print the new created PaintObjectWriting
	 *
	 * @return the new created PaintObjectWriting
	 */
	public PaintObjectWriting createPOW(final Pen _pen) {
		return new PaintObjectWriting(this, getIncreaseCID(), _pen);
	}

	/**
	 * adds a new PaintObject to list.
	 */
	public void addPaintObjectWrinting() {

		if (po_current != null) {

			if (!(po_current instanceof POCurve)) {

				// throw error message and kill program.
				State.getLogger().severe("Es soll ein neues pen objekt"
						+ " geadded werden, obwohl das Alte nicht null ist "
						+ "also nicht gefinished wurde.\n" 
						+ "Programm wird beendet.");
				ls_po_sortedByY.insertSorted(po_current,
						po_current.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);
			}

		}
		if (!(po_current instanceof POCurve)) {

			// create new PaintObject and insert it into list of
			po_current = new PaintObjectWriting(this, currentId, pen_current);

			// increase current id
			currentId++;

			// set uncommitted changes.
			State.setUncommittedChanges(true);
		}
	}

	/**
	 * adds a new PaintObject to list.
	 * 
	 * @param _bi
	 *            the BufferedImage which is to be transformed into ImagePO.
	 */
	public void addPaintObjectImage(final BufferedImage _bi) {

		if (po_current != null) {

			// throw error message and kill program.
			State.getLogger().severe("Es soll ein neues pen objekt"
					+ " geadded werden, obwohl das Alte nicht null ist "
					+ "also nicht gefinished wurde.\n" 
					+ "Programm wird beendet.");
			ls_po_sortedByY.insertSorted(po_current,
					po_current.getSnapshotBounds().y,
					SecureList.ID_NO_PREDECESSOR);

		}

		if (_bi == null) {
			State.getLogger().warning("nothing on clipboard.");
		} else {

			// create new PaintObject and insert it into list of
			PaintObjectImage poi = new PaintObjectImage(currentId, _bi, this);
			ls_po_sortedByY.insertSorted(poi, poi.getSnapshotBounds().y,
					SecureList.ID_NO_PREDECESSOR);

			// increase current id
			currentId++;

			// set uncommitted changes.
			State.setUncommittedChanges(true);
		}
	}
	
	
	
	/**
	 * Set whether this page currently is displayed (is necessary
	 * for projects consisting of more than one single page).
	 * 
	 * In a pdf, it is only necessary to load the pdf- background image
	 * from pdf file, if the current page is selected. That saves
	 * time and space in the RAM - drive.
	 * 
	 * @param _displayed	whether the current page is displayed.
	 */
	public void setDisplayed(final boolean _displayed) {
		
		if (_displayed) {
			pdf_background.remember();
		} else {
			pdf_background.forget();
		}
	}
	
	
	
	/**
	 * The background - pdf image of the pdf document the current picture
	 * is part of.
	 */
	private PaintObjectPdf pdf_background = null;
	

	/**
	 * adds a new PaintObject to list.
	 * 
	 * @param _bi
	 *            the BufferedImage which is to be transformed into ImagePO.
	 */
	public PaintObjectPdf addPaintObjectPDF(final Project _pro, final int _pageNr) {

		if (po_current != null) {

			// throw error message and kill program.
			State.getLogger().severe("Es soll ein neues pen objekt"
					+ " geadded werden, obwohl das Alte nicht null ist "
					+ "also nicht gefinished wurde.\n" 
					+ "Programm wird beendet.");
			ls_po_sortedByY.insertSorted(po_current,
					po_current.getSnapshotBounds().y,
					SecureList.ID_NO_PREDECESSOR);

		}

		if (_pro == null) {
			State.getLogger().warning("pdf is null.");
		} else {

			// create new PaintObject and insert it into list of
			PaintObjectPdf poi = createPDF(_pro, _pageNr);
			ls_po_sortedByY.insertSorted(poi, 0,
					SecureList.ID_NO_PREDECESSOR);
			
			if (pdf_background != null) {
				State.getLogger().severe("Overwriting pdf background image.");
			}
			pdf_background = poi;
			
			return poi;

		}
		return null;
	}

	/**
	 * Add the paintObject.
	 */
	public void addPaintObject(final Insert _tab_insert) {

		int casus = -1;
		// switch index of operation
		switch (State.getIndexOperation()) {

		case Constants.CONTROL_PAINTING_INDEX_I_G_LINE:
			addPaintObject(new POLine(currentId, pen_current, this));
			break;
		case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE:

			casus = Constants.PEN_ID_MATHS_SILENT;
		case Constants.CONTROL_PAINTING_INDEX_I_G_CURVE_2:
			if (casus == -1) {

				casus = Constants.PEN_ID_MATHS_SILENT_2;
			}

			Pen pen;
			if (pen_current instanceof BallPen) {

				pen = new BallPen(casus, pen_current.getThickness(),
						pen_current.getClr_foreground());
			} else if (pen_current instanceof PenSelection) {

				pen = new PenSelection();
			} else if (pen_current instanceof Pencil) {

				pen = new Pencil(casus, pen_current.getThickness(),
						pen_current.getClr_foreground());
			} else {

				State.getLogger().warning("fehler stiftz noch nicht hinzu");

				// throw exception
				throw new Error("Fehler: stift noch nicht hinzugefuegt.");
			}
			addPaintObject(new POCurve(casus, pen, casus, this));
			break;
		case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE:
			addPaintObject(new PORectangle(currentId, pen_current, this));
			break;
		case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE:
			addPaintObject(new POTriangle(currentId, pen_current, this));
			break;

		case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH_FILLED:
			addPaintObject(new POArchFilled(currentId, pen_current, this));
			break;
		case Constants.CONTROL_PAINTING_INDEX_I_G_RECTANGLE_FILLED:
			addPaintObject(new PoRectangleFilled(currentId, pen_current, this));
			break;
		case Constants.CONTROL_PAINTING_INDEX_I_G_TRIANGLE_FILLED:
			addPaintObject(new POTriangleFilled(currentId, pen_current, this));
			break;
		case Constants.CONTROL_PAINTING_INDEX_I_G_ARCH:
			addPaintObject(new POArch(currentId, pen_current, this));
			break;

		case Constants.CONTROL_PAINTING_INDEX_I_D_DIA:

			String srows = _tab_insert.getJtf_amountRows().getText();
			int rows = 0;
			try {
				rows = Integer.parseInt(srows);
			} catch (Exception e) {
				Message.showMessage(Message.MESSAGE_ID_INFO, "enter valid row");
			}
			String slines = _tab_insert.getJtf_amountLines().getText();
			int lines = 0;
			try {
				lines = Integer.parseInt(slines);
			} catch (Exception e) {
				Message.showMessage(Message.MESSAGE_ID_INFO,
						"enter valid column");
			}
			addPaintObject(new PODiagramm(currentId, pen_current, lines, 
					rows, this));
			break;
		case Constants.CONTROL_PAINTING_INDEX_PAINT_2:
		case Constants.CONTROL_PAINTING_INDEX_PAINT_1:
			addPaintObject(new PaintObjectWriting(this, currentId, 
					pen_current));
			break;
		default:
			State.getLogger().warning("unknown index operation");
		}
	}

	/**
	 * adds a new PaintObject to list.
	 * 
	 * @param _po
	 *            the PaintObjectPen.
	 */
	private void addPaintObject(final PaintObjectPen _po) {

		if (po_current != null) {

			if (!(po_current instanceof POCurve)) {

				// throw error message and kill program.
				State.getLogger().severe("Es soll ein neues pen objekt"
						+ " geadded werden, obwohl das Alte nicht null ist "
						+ "also nicht gefinished wurde.\n" 
						+ "Programm wird beendet.");
				ls_po_sortedByY.insertSorted(po_current,
						po_current.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);

			}
		}

		if (!(po_current instanceof POCurve) || !(_po instanceof POCurve)) {

			// create new PaintObject and insert it into list of
			po_current = _po;

			// increase current id
			currentId++;

			// set uncommitted changes.
			State.setUncommittedChanges(true);
		}
	}

	/**
	 * return the color of the pixel at given location. The location is the
	 * location in the bufferedImage which is not adapted at the zoomed click
	 * size.
	 * 
	 * @param _pxX
	 *            coordinate of normal-sized BufferedImage.
	 * @param _pxY
	 *            coordinate of normal-sized BufferedImage.
	 *            
	 * @param _bi 
	 * 				The BufferedImage which pixel is checked.
	 * 
	 * @return the RGB integer of the color at given coordinate.
	 */
	public int getColorPX(final int _pxX, final int _pxY, 
			final BufferedImage _bi) {
		return _bi.getRGB(_pxX, _pxY);
	}

	/**
	 * repaint the items that are in a rectangle to the (view) page (e.g. if the
	 * JLabel is moved))..
	 * 
	 * @param _x
	 *            the x coordinate
	 * @param _y
	 *            the y coordinate
	 * @param _width
	 *            the width
	 * @param _height
	 *            the height
	 * @param _graphicX
	 *            the graphics x
	 * @param _graphiY
	 *            the graphics y.
	 * @param _bi
	 *            the bufferedImage
	 * @return the graphics
	 */
	public synchronized BufferedImage updateRectangle(
			final int _x, final int _y, 
			
			final int _width, final int _height,
			
			final int _graphicX, final int _graphiY, 
			
			final BufferedImage _bi,
			final ContorlPicture _controlPicture) {
		
		
		BufferedImage ret = emptyRectangle(
				_x, _y, _width, _height, _graphicX,
				_graphiY, _bi);

		// if the graphical user interface is not set up yet.
		if (ret == null) {
			return new BufferedImage(_width, _height,
					BufferedImage.TYPE_INT_ARGB);
		}
		
		
		ret = repaintRectangle(_x, _y, _width, _height, _graphicX,
				_graphiY, ret, false);

		return ret;
	}

	/**
	 * repaint the items that are in a rectangle to the (view) page (e.g. if the
	 * JLabel is moved))..
	 * 
	 * @param _x
	 *            the x coordinate
	 * @param _y
	 *            the y coordinate
	 * @param _width
	 *            the width
	 * @param _height
	 *            the height
	 * @param _graphicX
	 *            the graphics x
	 * @param _graphiY
	 *            the graphics y.
	 * @param _bi
	 *            the BufferedImage
	 * @return the graphics
	 */
	public synchronized BufferedImage emptyRectangle(final int _x,
			final int _y, final int _width, final int _height,
			final int _graphicX, final int _graphiY, final BufferedImage _bi) {

		// check whether the rectangle concerns the blue border of the
		// image which is not to be emptied and then repainted.
		// If that's the case, the rectangle width or height are decreased.
		int rectWidth = _width, rectHeight = _height;
		if (_x + _width > State.getImageShowSize().width) {
			rectWidth = State.getImageShowSize().width - _x;
		}

		if (_y + _height > State.getImageShowSize().height) {
			rectHeight = State.getImageShowSize().height - _y;

		}

		BufferedImage bi = _bi;
		// alle die in Frage kommen neu laden.
		if (ls_po_sortedByY == null || bi == null) {
			bi = new BufferedImage(_width, _height, BufferedImage
					.TYPE_INT_ARGB);
//			return _bi;
		}

		final int maxRGB = 255;
		PaintBI.fillRectangleQuick(bi, new Color(maxRGB, maxRGB, maxRGB, 0), 
				new Rectangle(_graphicX, _graphiY, rectWidth, rectHeight));

		return bi;

	}

	/**
	 * Repaint a rectangle without clearing the screen. The state of the list of
	 * paintObjects is not changed by this process.
	 * 
	 * @param _x
	 *            the x coordinate of the repainted rectangle
	 * @param _y
	 *            the y coordinate
	 * @param _width
	 *            the width
	 * @param _height
	 *            the height
	 * 
	 * @param _bi
	 *            the BufferedImage
	 * @param _final
	 *            whether to paint finally to BufferedImage or not.
	 * 
	 * @return the BufferedImage with painted PaintObjects on it.
	 */
	public synchronized BufferedImage repaintRectangle(
			
			final int _x,
			final int _y,
			
			final int _width, 
			final int _height,
			
			final int _xBi,
			final int _yBi,
			
			final BufferedImage _bi, final boolean _final) {

		// If the sorted list of PaintObjects has not been initialized yet,
		// the list is empty or the given bufferedImage is equal to NULL
		// return the given BufferedImage because there is nothing to do
		if (ls_po_sortedByY == null 
				|| ls_po_sortedByY.isEmpty() 
				|| _bi == null) {
			return _bi;
		}
		
		// Start a transaction. That means that after the transaction has
		// been terminated, the current item of the list is reset.
		final int id_closedAction = 
				ls_po_sortedByY.startClosedAction("repaintRectangle",
						SecureList.ID_NO_PREDECESSOR);
		final int id_transaction = 
				ls_po_sortedByY.startTransaction("repaintRectangle",
						SecureList.ID_NO_PREDECESSOR);

		// Initialize new list into which the Items are inserted that are inside
		// the specified rectangle. List is sorted by id for painting the
		// items chronologically.
		SecureListSort<PaintObject> ls_poChronologic 
		= new SecureListSort<PaintObject>();

		// reset value for debugging and speed testing.
		State.setCounter_paintedPoints(0);

		// initialize start value go to the list's beginning and calculate
		// values such as the stretch factors (which occur because of
		// zooming in and out).
		boolean behindRectangle = false;
		ls_po_sortedByY.toFirst(id_closedAction, id_transaction);

		/**
		 * Stretch factor by width and height.
		 */
		final double factorW, factorH;
		if (_final) {
			factorW = 1;
			factorH = 1;
		} else {
			factorW = 1.0 * State.getImageSize().width
					/ State.getImageShowSize().width;
			factorH = 1.0 * State.getImageSize().width 
					/ State.getImageShowSize().width;

		}
		
		/**
		 * The location of the page end needed for checking roughly whether a
		 * paintObject may be inside the repaint rectangle.
		 */
		final int myYLocationRepaintEnd = (int) (factorH * (_y + _height)), 
				myXLocationRepaintEnd = (int) (factorW * (_x + _width));
		/**
		 * The repaint rectangle.
		 */
		final DRect r_selection = new DRect(
				(factorW * _x), (factorH * _y), 
				(factorW * _width), (factorH * _height));

		/*
		 * Find out which items are inside the given repaint rectangle and
		 * insert them into the list of paintObjects.
		 */
		while (!ls_po_sortedByY.isEmpty() 
				&& !ls_po_sortedByY.isBehind()
				&& !behindRectangle) {

			// if the current item is not initialized only perform the
			// next operation.
			if (ls_po_sortedByY.getItem() != null) {

				// check whether the current PaintObject is in the given
				// rectangle by the coordinate by which the list is sorted.
				// if that is not the case the element is behind the specified
				// rectangle.
				if (ls_po_sortedByY.getItem().getSnapshotBounds().y
						<= myYLocationRepaintEnd) {

					// Firstly check whether the current PaintObject may be
					// inside the given rectangle by the other
					// coordinate (by which the list is not sorted).
					// Secondly, if (1) is the case, perform a second
					// check which is exact; that means it
					// only succeeds if there really are pixels
					// printed inside selected rectangle.
					//
					// If one of the two above mentioned tests return false
					// there may occur PaintObjects that are inside the
					// rectangle
					// but are found behind the current item inside the list
					// (because the list can only be sorted by one parameter)
					// Thus it is necessary to use this second if clause.
					if (ls_po_sortedByY.getItem().getSnapshotBounds().x
							<= myXLocationRepaintEnd
							&& ls_po_sortedByY.getItem().isInSelectionImage(
									r_selection.getRectangle())) {
						
						
						// if the current paint object is a "normal" paint object
						// or it is not final.
						if (!(_final && ls_po_sortedByY.getItem() 
								instanceof PaintObjectPdf)) {

							ls_poChronologic.insertSorted(ls_po_sortedByY.getItem(),
									ls_po_sortedByY.getItem().getElementId(),
									SecureList.ID_NO_PREDECESSOR);
						}

					}
				} else {

					// if the current element starts behind the rectangle
					// in coordinate by which the list is sorted,
					// no PaintObject inserted after this element inside the
					// can be printed.
					behindRectangle = true;
				}
			} else {

				// log severe error because of unnecessary PaintObject inside
				// list.
				State.getLogger().severe(
						"Error. Null PaintObject inside"
								+ " the list of sorted paintObjects.");
			}
			ls_po_sortedByY.next(id_closedAction, id_transaction);
		}

		/*
		 * Go through the sorted list of items and paint them
		 */
		ls_poChronologic.toFirst(SecureList.ID_NO_PREDECESSOR, 
				SecureList.ID_NO_PREDECESSOR);
		int counter = 0;
		
		try {

			while (!ls_poChronologic.isBehind()
					&& !ls_poChronologic.isEmpty()) {

//				if (!_final || !(ls_poChronologic.getItem() instanceof PaintObjectPdf)) {

					ls_poChronologic.getItem().paint(_bi, _final,
							_bi, 
//							_x, _y,
//							_paintLocationX,
//							_paintLocationY,
							-_x + _xBi, -_y + _yBi,
//							Page.getInstance().getJlbl_painting().getBi(),
//							Page.getInstance().getJlbl_painting().getLocation().x,
//							Page.getInstance().getJlbl_painting().getLocation().y,
							r_selection);
					counter++;
//				}
					ls_poChronologic.next(SecureList.ID_NO_PREDECESSOR, 
							SecureList.ID_NO_PREDECESSOR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//log repainting action in console.
		if (counter > 0) {
			Console.log(counter
					+ " Item painted in rectanlge (" + _x + ", " + _y + ", "
					+ _width + ", " + _height + "). Final: " + _final, 
					Console.ID_INFO_UNIMPORTANT, 
					getClass(), "repaintRectangle");
		}
		
		// print logging method
		State.getLogger().info("Painted " + State.getCounter_paintedPoints()
						+ "pixel points for this operation.");
		//finish transaction and finish closed action; adjust the current
		//element to its state before the list transaction.
		ls_po_sortedByY.finishTransaction(id_transaction);
		ls_po_sortedByY.finishClosedAction(id_closedAction);

		return _bi;
	}

	/**
	 * adds a Point to current PaintObject.
	 * 
	 * @param _pnt
	 *            the point which is to be added.
	 */
	public void changePaintObject(final DPoint _pnt,
			final Page _page,
			final ContorlPicture _cPicture) {

		if (po_current == null) {

			// throw error message and kill program.
			State.getLogger().warning(
					"Es soll ein nicht existentes Objekt veraendert werden.\n"
							+ "Programm wird beendet.");
		}
		if (_pnt.getX() > State.getImageSize().getWidth() || _pnt.getX() < 0
				|| _pnt.getY() > State.getImageSize().getHeight()
				|| _pnt.getY() < 0) {
			return;
		}
		if (!(po_current instanceof POCurve)
				&& po_current instanceof PaintObjectWriting) {

			PaintObjectWriting pow = (PaintObjectWriting) po_current;
			pow.getPoints().toLast();
			if (pow.getPoints().isEmpty()) {

				// add point to PaintObject
				pow.addPoint(_pnt);
			} else {

				pow.addPoint(_pnt);

				if (pen_current instanceof PenSelection) {

					((PenSelection) pow.getPen()).resetCurrentBorderValue();
				}
			}
		} else if (po_current instanceof POInsertion
				|| po_current instanceof POLine) {

			POInsertion pow = (POInsertion) po_current;
			if (pow.getPnt_first() != null && pow.getPnt_last() != null) {
				_cPicture.refreshPaint();
			}

			po_current.addPoint(_pnt);
		} else if (po_current instanceof POCurve) {

			POCurve pow = (POCurve) po_current;
			pow.addPoint(_pnt);
			_cPicture.refreshPaint();
		}

		if (pen_current instanceof PenSelection) {

			BufferedImage bi_transformed = Util
					.getEmptyBISelection();
			bi_transformed = po_current.paint(
					bi_transformed, false,
					bi_transformed, 
					_page.getJlbl_painting().getLocation().x,
					_page.getJlbl_painting().getLocation().y, 
					null);

			_page.getJlbl_selectionBG()
					.setIcon(new javax.swing.ImageIcon(bi_transformed));
		} else {

			BufferedImage bi_transformed = _cPicture.getBi();
			if (po_current instanceof POCurve) {
				bi_transformed = ((PaintObjectWriting) po_current).paint(
						_cPicture.getBi(), false, _cPicture.getBi(), 
						_page.getJlbl_painting().getLocation().x, _page
						.getJlbl_painting().getLocation().y,
						null);
			} else if (po_current instanceof PaintObjectWriting
					&& !(po_current instanceof POCurve)) {
				
				bi_transformed = ((PaintObjectWriting) po_current).paintLast(
						bi_transformed,
						_page.getJlbl_painting().getLocation().x,
						_page.getJlbl_painting().getLocation().y);
			} else {
				_cPicture
						.refreshRectangle(
								po_current.getSnapshotBounds().x,
								po_current.getSnapshotBounds().y,
								po_current.getSnapshotBounds().width,
								po_current.getSnapshotBounds().height);

				bi_transformed = po_current.paint(_cPicture.getBi(), 
						false, _cPicture.getBi(), _page
						.getJlbl_painting().getLocation().x, _page
						.getJlbl_painting().getLocation().y, null);
			}
			_cPicture.setBi(bi_transformed);

		}

		// set uncommitted changes.
		State.setUncommittedChanges(true);
	}

	/**
	 * @return the paintObject.
	 */
	public PaintObjectWriting abortPaintObject(
			final ContorlPicture _controlPic) {
		PaintObjectWriting pow = null;
		// PaintObjectWriting pow = new PaintObjectWriting(-1, pen_current);
		// List<DPoint> ls_points = null;
		// pow.
		if (pen_current instanceof PenSelection) {

			_controlPic
					.paintSelection(po_current, (PenSelection) pen_current);

		}
		pen_current.abort();

		if (po_current instanceof PaintObjectWriting) {

			pow = (PaintObjectWriting) po_current;
		}
		po_current = null;
		return pow;
	}

	/**
	 * Finishes current PaintObject.
	 * @param _po the debug view panel.
	 */
	public void finish(final Debug _po) {

		if (po_current == null) {

			// throw error message and kill program.
			State.getLogger().severe(
					"Es soll ein nicht existentes Objekt beendet werden.\n"
							+ "Programm wird beendet.");
		} else {

			//add a new history item that indicates an add operation.
			history.addHistoryItem(
					history.createAddItem(po_current.clone(),
							po_current.clone()));

			// insert into sorted lists sorted by x and y positions.
			final Rectangle b = po_current.getSnapshotBounds();

			if (po_current instanceof POCurve) {
				POCurve pc = (POCurve) po_current;
				pc.setReady();
			} else {

				ls_po_sortedByY.insertSorted(po_current, b.y, 
						SecureList.ID_NO_PREDECESSOR);
				new PictureOverview(_po).add(po_current);

				// reset current instance of PaintObject
				po_current = null;
			}

			// setChanged();
			// notifyObservers(bi_normalSize);

			if (!(pen_current instanceof PenSelection)) {

				// set uncommitted changes.
				State.setUncommittedChanges(true);
			}
		}
	}

	/**
	 * sets the pen the first time a picture is created (thus do not notify the
	 * history of uncommitted changes).
	 * 
	 * @param _pen
	 *            the new pen.
	 */
	public void initializePen(final Pen _pen) {

		if (_pen instanceof BallPen) {

			this.pen_current = new BallPen(_pen.getId_operation(),
			// this.pen = new PenKuli(Constants.PEN_ID_POINT,
					_pen.getThickness(), _pen.getClr_foreground());
		} else if (_pen instanceof PenSelection) {

			this.pen_current = new PenSelection();
		} else if (_pen instanceof Pencil) {

			this.pen_current = new Pencil(_pen.getId_operation(),
					_pen.getThickness(), _pen.getClr_foreground());
		} else {

			// alert user.
			State.showMessageDialog(
					"PROGRAMMIERFEHLER @ paintobjectwriting: "
							+ "Stift noch nicht hinzugefuegt.");

			// throw exception
			new java.lang.Error("Fehler: stift noch nicht hinzugefuegt.")
					.printStackTrace();
		}

	}

	/**
	 * sets the pen both to Picture (for the following PaintObjects) and to the
	 * current PaintObject (which should be null).
	 * 
	 * @param _pen
	 *            the new pen.
	 */
	public void changePen(final Pen _pen) {

		// set in picture
		this.pen_current = _pen;

		// set in current paint object.
		if (po_current != null && po_current instanceof PaintObjectWriting) {
			po_current.setPen(pen_current);
		}

		// set uncommitted changes.
		State.setUncommittedChanges(true);
	}

	/**
	 * Paint the selected BufferedImages to a new BufferedImage which has got
	 * the size of the selection. This method is used for copying the
	 * paintObjects as images to clipboard.
	 * 
	 * @return the painted BufferedImage.
	 */
	public BufferedImage paintSelectedBI(final DRect _recSelection) {

		if (_recSelection == null) {

			State.getLogger().warning("the selection square does not exist!");
			return null;
		}

		BufferedImage bi = new BufferedImage(_recSelection.getIWidth() + 1, 
				_recSelection.getIHeight() + 1, BufferedImage.TYPE_INT_ARGB);


    	//start transaction and closed action.
    	final int transaction = ls_po_sortedByY
    			.startTransaction("paint selected bi", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = ls_po_sortedByY
    			.startClosedAction("paint selected bi", 
    					SecureList.ID_NO_PREDECESSOR);
        
		ls_poSelected.toFirst(transaction, closedAction);
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {

			PaintObject po = ls_poSelected.getItem();

			if (po instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) po;

				// TODO: zoom, scroll adjust?
				pow.paint(bi, false, bi, -_recSelection.getIX(), 
						-_recSelection.getIY(), null);

			} else if (po instanceof PaintObjectImage) {
				PaintObjectImage poi = (PaintObjectImage) po;
				poi.paint(bi, false, bi, -_recSelection.getIX(), 
						-_recSelection.getIY(), null);

			} else {
				State.getLogger().warning("unknown kind of PaintObject" + po);
			}
			ls_poSelected.next(transaction, closedAction);

		}

    	//close transaction and closed action.
    	ls_po_sortedByY.finishTransaction(
    			transaction);
    	ls_po_sortedByY.finishClosedAction(
    			closedAction);
        
		return bi;
	}

	/**
	 * save the picture.
	 * 
	 * @param _wsLoc
	 *            the path of the location.
	 *            
	 *            @param _x the location of the PaintLabel.
	 *            @param _y the location of the PaintLabel.
	 */
	public void saveIMAGE(final String _wsLoc,
			final int _x,
			final int _y,
			final String _type) {

		BufferedImage bi = getBufferedImage(_x, _y);
		try {
			if (_type == "") {

				ImageIO.write(bi, State.getSaveFormat(),
						new File(_wsLoc + State.getSaveFormat()));
			} else {

				ImageIO.write(bi, State.getSaveFormat(),
						new File(_wsLoc + _type));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * save the picture.
	 * 
	 *            
	 *            @param _x the location of the PaintLabel.
	 *            @param _y the location of the PaintLabel.
	 */
	public BufferedImage getBufferedImage(
			final int _x,
			final int _y) {

		BufferedImage bi;
		if (State.isExportAlpha()) {

			bi = Util.getEmptyBITransparent();
		} else {
			bi = Util.getEmptyBIWhite();
		}

		bi = Utils.getBackgroundExport(bi, 0, 0, State.getImageSize().width,
				State.getImageSize().height, 0, 0);

		bi = repaintRectangle(-_x + 0, -_y + 0, State.getImageSize().width,
				State.getImageSize().height, -_x + 0, -_y + 0, bi, true);

		return bi;
	}


	/**
	 * save BufferedImage as png file.
	 * 
	 * @param _wsLoc
	 *            the path of the location.
	 *            
	 *            @param _bi the BufferedImage
	 */
	public static void saveBufferedImage(final String _wsLoc,
			final BufferedImage bi) {

		try {

				ImageIO.write(bi, "png",
						new File(_wsLoc + "png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * save the picture.
	 * @return the BufferedImage.
	 */
	public BufferedImage calculateImage() {

		BufferedImage bi;
		if (State.isExportAlpha()) {

			bi = Util.getEmptyBITransparent();
		} else {
			bi = Util.getEmptyBIWhite();
		}

		bi = Utils.getBackgroundExport(bi, 0, 0, State.getImageSize().width,
				State.getImageSize().height, 0, 0);

		bi = repaintRectangle(
				
				0, 0,
//				-Page.getInstance().getJlbl_painting().getLocation().x + 0,
//				-Page.getInstance().getJlbl_painting().getLocation().y + 0,
				State.getImageSize().width,
				State.getImageSize().height,
				0, 0,
				bi, true);

		return bi;

	}

	/**
	 * save the picture. Method for different use of the software. Only used
	 * for transforming image by using main method parameters in start class.
	 * 
	 * @param _wsLoc
	 *            the path of the location.
	 */
	public void saveQuickPNG(final String _wsLoc) {

		ls_po_sortedByY.toFirst(SecureList.ID_NO_PREDECESSOR,
				SecureList.ID_NO_PREDECESSOR);
		Rectangle r = ls_po_sortedByY.getItem().getSnapshotBounds();
		BufferedImage bi = new BufferedImage(r.width, r.height,
				BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < r.width; i++) {
			for (int j = 0; j < r.height; j++) {
				bi.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
			}
		}

		bi = ls_po_sortedByY.getItem().paint(bi, true, bi, 0, 0, null);

		try {
			ImageIO.write(bi, "png", new File(_wsLoc));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pack for saving the picture.
	 * 
	 * @param _wsLoc
	 *            the path of the location.
	 */
	public void pack() {
		// store the content of the BufferedImages elsewhere and delete 
		// it afterwards because it is not serializable. After saving 
		// operation has been completed, the bufferedImages are 
		// automatically loaded.
		if (ls_po_sortedByY != null) {
			ls_po_sortedByY.toFirst(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			while (!ls_po_sortedByY.isBehind()) {
				if (ls_po_sortedByY.getItem() instanceof PaintObjectImage) {
					((PaintObjectImage) ls_po_sortedByY.getItem())
					.prepareForSaving();
				}
				ls_po_sortedByY.next(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			}
		}

	}
	
	
	public final void unpack() {

		// re - load the bufferedImage to file (which is not serializable, thus
		// it had to be packed for saving.
		if (ls_po_sortedByY != null) {
			ls_po_sortedByY.toFirst(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			while (!ls_po_sortedByY.isBehind()) {
				if (ls_po_sortedByY.getItem() instanceof PaintObjectImage) {
					((PaintObjectImage) ls_po_sortedByY.getItem()).restore();
				}
				ls_po_sortedByY.next(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			}
		}
	}

	
	
	

	/**
	 * Resets closedAction and Transactions after loading and resorts the 
	 * list's elements.
	 */
	public final void loadPicture() {

		//reset current action
		ls_po_sortedByY.resetTransaction();
		ls_po_sortedByY.resetClosedAction();

		//convert: is done for old version of .pic because
		// sorted by x coordinate.
		ls_po_sortedByY.toFirst(SecureListSort.ID_NO_PREDECESSOR, 
				SecureListSort.ID_NO_PREDECESSOR);
		while(!ls_po_sortedByY.isBehind()) {
			if (ls_po_sortedByY.getItem() != null 
					&& ls_po_sortedByY.getItem().getSnapshotBounds() != null) {

				
				ls_po_sortedByY.getElement().setSortedIndex(
						ls_po_sortedByY.getItem().getSnapshotBounds().y);
			}
			ls_po_sortedByY.next(SecureListSort.ID_NO_PREDECESSOR, 
					SecureListSort.ID_NO_PREDECESSOR);
		}

		ls_po_sortedByY.resort(null);
	}
	/**
	 * save the picture.
	 * 
	 * @param _wsLoc
	 *            the path of the location.
	 */
	@Deprecated
	public void loadPictureOld(final String _wsLoc) {
		try {
			FileInputStream fos = new FileInputStream(new File(_wsLoc));
			ObjectInputStream oos = new ObjectInputStream(fos);
			@SuppressWarnings("unchecked")
			SecureListSort<PaintObject> p = 
			(SecureListSort<PaintObject>) oos.readObject();
			ls_po_sortedByY = p;
			
			//reset current action
			ls_po_sortedByY.resetTransaction();
			ls_po_sortedByY.resetClosedAction();

			//convert: is done for old version of .pic because
			// sorted by x coordinate.
			ls_po_sortedByY.toFirst(SecureListSort.ID_NO_PREDECESSOR, 
					SecureListSort.ID_NO_PREDECESSOR);
			while(!ls_po_sortedByY.isBehind()) {
				ls_po_sortedByY.getElement().setSortedIndex(
						ls_po_sortedByY.getItem().getSnapshotBounds().y);
				ls_po_sortedByY.next(SecureListSort.ID_NO_PREDECESSOR, 
						SecureListSort.ID_NO_PREDECESSOR);
			}

			ls_po_sortedByY.resort(null);

			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		

		// re - load the bufferedImage to file (which is not serializable, thus
		// it had to be packed for saving.
		if (ls_po_sortedByY != null) {
			ls_po_sortedByY.toFirst(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			while (!ls_po_sortedByY.isBehind()) {
				if (ls_po_sortedByY.getItem() instanceof PaintObjectImage) {
					((PaintObjectImage) ls_po_sortedByY.getItem()).restore();
				}
				ls_po_sortedByY.next(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			}
		}
	}

	/**
	 * Empty each paintObject.
	 */
	public void emptyImage() {
		ls_po_sortedByY = new SecureListSort<PaintObject>();
		ls_poSelected = new SecureList<PaintObject>();
	}

	/**
	 * darken image.
	 * 
	 * @param _poi
	 *            the PaintObjectImage which is altered
	 */
	public void darken(final PaintObjectImage _poi) {

		BufferedImage bi_snapshot = _poi.getSnapshot();
		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {

				final int minus = 30;
				Color c = new Color(bi_snapshot.getRGB(i, j));
				int red = c.getRed() - minus;
				int blue = c.getBlue() - minus;
				int green = c.getGreen() - minus;

				if (red < 0) {
					red = 0;
				}
				if (green < 0) {
					green = 0;
				}
				if (blue < 0) {
					blue = 0;
				}

				bi_snapshot.setRGB(i, j, new Color(red, green, blue).getRGB());
			}
		}
	}

	/**
	 * transform white to alpha.
	 * 
	 * @param _poi
	 *            the PaintObjectImage which is altered
	 */
	public void transformToAlpha(final PaintObjectImage _poi) {

		BufferedImage bi_snapshot = _poi.getSnapshot();
		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {

				Color c = new Color(bi_snapshot.getRGB(i, j));

				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();

				final int maxRGB = 255;
				final double multiplyer = 1.5;
				final int five = 5;

				int gesamt = maxRGB - (red + green + blue) / (2 + 1);
				gesamt *= multiplyer;

				if (gesamt > maxRGB) {
					gesamt = maxRGB - 1;
				}
				red = red * 2 + five;
				green = green * 2 + five;
				blue = blue * 2 + five;

				if (red > maxRGB) {
					red = maxRGB;
				}
				if (green > maxRGB) {
					green = maxRGB;
				}
				if (blue > maxRGB) {
					blue = maxRGB;
				}

				bi_snapshot.setRGB(i, j,
						new Color(red, green, blue, gesamt).getRGB());
			}
		}
	}

	/**
	 * Transform all PaintObjectImages to alpha.
	 */
	public void transformWhiteToAlpha() {

		if (ls_po_sortedByY != null) {



	    	//start transaction and closed action.
	    	final int transaction = ls_po_sortedByY
	    			.startTransaction("transformWhiteToAlpha", 
	    					SecureList.ID_NO_PREDECESSOR);
	    	final int closedAction = ls_po_sortedByY
	    			.startClosedAction("transformWhiteToAlpha", 
	    					SecureList.ID_NO_PREDECESSOR);
	        
			
			ls_po_sortedByY.toFirst(transaction, closedAction);
			while (!ls_po_sortedByY.isBehind() && !ls_po_sortedByY.isEmpty()) {
				if (ls_po_sortedByY.getItem() instanceof PaintObjectImage) {

					whiteToAlpha((PaintObjectImage) ls_po_sortedByY.getItem());
				}
				ls_po_sortedByY.next(transaction, closedAction);
			}

	    	//close transaction and closed action.
	    	ls_po_sortedByY.finishTransaction(
	    			transaction);
	    	ls_po_sortedByY.finishClosedAction(
	    			closedAction);
	        
		}
	}

	/**
	 * transform white pixel to alpha pixel.
	 * 
	 * @param _poi
	 *            the PaintObjectImage which is altered
	 */
	private void whiteToAlpha(final PaintObjectImage _poi) {

		BufferedImage bi_snapshot = _poi.getSnapshot();

		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {

				Color c = new Color(bi_snapshot.getRGB(i, j), true);

				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();

				int alpha;
				final int upTo = 240;
				if (red + green + blue >= (2 + 1) * upTo) {
					alpha = 0;
					System.out.println("hier");
				} else {
					final int maxAlpha = 255;
					alpha = maxAlpha;
				}

				bi_snapshot.setRGB(i, j,
						new Color(red, green, blue, alpha).getRGB());
			}
		}

		_poi.setImage(bi_snapshot);
	}

	/**
	 * transform image to gray image.
	 * 
	 * @param _poi
	 *            the PaintObjectImage which is altered
	 */
	public void blackWhite(final PaintObjectImage _poi) {

		BufferedImage bi_snapshot = _poi.getSnapshot();

		for (int i = 0; i < bi_snapshot.getWidth(); i++) {
			for (int j = 0; j < bi_snapshot.getHeight(); j++) {

				Color c = new Color(bi_snapshot.getRGB(i, j));

				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();

				int gesamt = (red + green + blue) / (2 + 1);

				bi_snapshot.setRGB(i, j,
						new Color(gesamt, gesamt, gesamt).getRGB());
			}
		}
	}

	/*
	 * Selection methods.
	 */

	/**
	 * create selected List.
	 */
	public void createSelected() {
		if (ls_poSelected == null) {
			ls_poSelected = new SecureList<PaintObject>();
		} else {
			if (!ls_poSelected.isEmpty()) {
				State.getLogger().warning(
						"creating new selection list but list is not empty.");
			}
		}
	}

	/**
	 * insert into selected list.
	 * 
	 * @param _po
	 *            the paintObject to be inserted.
	 */
	public synchronized void insertIntoSelected(final PaintObject _po,
			final Debug _paintObjects) {

		// deactivates to change operations of selected items
		if (ls_poSelected == null) {
			State.getLogger().warning("insert into null list");
		} else if (_po != null) {

//			CTabSelection.activateOp();
//			if (_po instanceof PaintObjectWriting) {
//
//				PaintObjectWriting pow = (PaintObjectWriting) _po;
//				CTabSelection.getInstance().change(ls_poSelected.isEmpty(),
//						pow.getPen().getId_operation(),
//						pow.getPen().getClr_foreground().getRGB());
//			}

			ls_poSelected.insertAfterHead(_po, SecureList.ID_NO_PREDECESSOR);
			new PictureOverview(_paintObjects).addSelected(_po);

		}
	}
	/**
	 * Finish paintObjectSelection: go through the list of selected paint
	 * objects and tell the paintObjects selection interface controller
	 * the color and the pen.
	 */
	public synchronized void finishSelection(
			final CTabSelection _ctabSelection) {

		// deactivates to change operations of selected items
		if (ls_poSelected == null) {
			State.getLogger().warning("finish selection list which is"
					+ " null.");
		} else {

			//start transaction and closedAction.
			final int transaction = ls_poSelected.startTransaction(
					"finishSelection", 
					SecureList.ID_NO_PREDECESSOR);
			final int closedAction = ls_poSelected.startClosedAction(
					"finishSelection", 
					SecureList.ID_NO_PREDECESSOR);
			
			ls_poSelected.toFirst(transaction, closedAction);
			_ctabSelection.activateOp();

			while (!ls_poSelected.isBehind()) {
				

				if (ls_poSelected.getItem() instanceof PaintObjectWriting) {

					PaintObjectWriting pow = 
							(PaintObjectWriting) ls_poSelected.getItem();
					_ctabSelection.change(ls_poSelected.isEmpty(),
							pow.getPen().getId_operation(),
							pow.getPen().getClr_foreground().getRGB());
				}
				
				ls_poSelected.next(transaction, closedAction);
			}

			//finish actions.
			ls_poSelected.finishClosedAction(closedAction);
			ls_poSelected.finishTransaction(transaction);
		}
	}
	
	

	/**
	 * 
	 * @param _slpo
	 * @return
	 */
	public SecureList<PaintObject> cloneSecureListPaintObject(
			final SecureList<PaintObject> _slpo) {
		
		SecureList<PaintObject> sl_new = new SecureList<PaintObject>();
		
		if (_slpo != null) {

			_slpo.toFirst(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
			while (!_slpo.isBehind() && !_slpo.isEmpty()) {
				
				sl_new.insertBehind(_slpo.getItem().clone(), 
						SecureList.ID_NO_PREDECESSOR);
				_slpo.next(SecureList.ID_NO_PREDECESSOR,
						SecureList.ID_NO_PREDECESSOR);
			}
		}
		return sl_new;
		
	}

	/**
	 * Move selected items.
	 * 
	 * @param _dX
	 *            the x difference from current position
	 * @param _dY
	 *            the y difference from current position
	 */
	public synchronized void moveSelected(final int _dX, final int _dY) {

		if (ls_poSelected == null) {
			return;
		}
		

		SecureList<PaintObject> sl_oldMove 
		= cloneSecureListPaintObject(ls_poSelected);
//		add a new history item that indicates an add operation.


    	//start transaction and closed action.
    	final int transaction = getLs_po_sortedByY()
    			.startTransaction("stretch image", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = getLs_po_sortedByY()
    			.startClosedAction("stretch image", 
    					SecureList.ID_NO_PREDECESSOR);
        
		ls_poSelected.toFirst(transaction, closedAction);

		while (!ls_poSelected.isBehind()) {

			if (ls_poSelected.getItem() instanceof PaintObjectWriting) {

				PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
						.getItem();
				pow = movePaintObjectWriting(pow, _dX, _dY);

			} else if (ls_poSelected.getItem() instanceof PaintObjectImage) {

				PaintObjectImage p = (PaintObjectImage) ls_poSelected.getItem();
				p.move(new Point(_dX, _dY));
			} else if (ls_poSelected.getItem() instanceof POLine) {

				POLine p = (POLine) ls_poSelected.getItem();
				moveLine(p, _dX, _dY);
			} else {
				State.getLogger().warning("unknown kind of PaintObject?");
			}
			ls_poSelected.next(transaction, closedAction);
		}


		SecureList<PaintObject> sl_newMoved 
		= cloneSecureListPaintObject(ls_poSelected);

		history.addHistoryItem(history.createMoveItem(
				sl_oldMove, sl_newMoved));
		
    	//close transaction and closed action.
    	getLs_po_sortedByY().finishTransaction(
    			transaction);
    	getLs_po_sortedByY().finishClosedAction(
    			closedAction);
        
	}

	/**
	 * 
	 * Move PaintObject items.
	 * 
	 * @param _pow
	 *            PaintObjectWriting
	 * @param _dX
	 *            the x difference from current position
	 * @param _dY
	 *            the y difference from current position
	 * @return the PaintObjectWriting
	 */
	public static PaintObjectWriting movePaintObjectWriting(
			final PaintObjectWriting _pow, final int _dX, final int _dY) {

		_pow.getPoints().toFirst();
		_pow.adjustSnapshotBounds(_dX, _dY);
		while (!_pow.getPoints().isBehind() && !_pow.getPoints().isEmpty()) {
			_pow.getPoints().getItem()
					.setX(_pow.getPoints().getItem().getX() + _dX);
			_pow.getPoints().getItem()
					.setY(_pow.getPoints().getItem().getY() + _dY);
			_pow.getPoints().next();
		}
		return _pow;
	}

	/**
	 * 
	 * Move PaintObject items.
	 * 
	 * @param _pow
	 *            PaintObjectWriting
	 * @param _dX
	 *            the x difference from current position
	 * @param _dY
	 *            the y difference from current position
	 * @return the PaintObjectWriting
	 */
	public static POLine moveLine(final POLine _pow, final int _dX,
			final int _dY) {

		_pow.getPnt_first().setX(_pow.getPnt_first().getX() + _dX);
		_pow.getPnt_first().setY(_pow.getPnt_first().getY() + _dY);

		_pow.getPnt_last().setX(_pow.getPnt_last().getX() + _dX);
		_pow.getPnt_last().setY(_pow.getPnt_last().getY() + _dY);
		return _pow;
	}

	
	
	
	/**
	 * Paint the selected items to the selection JLabel.
	 * 
	 * @return whether there is something to be painted or not.
	 */
	public boolean paintSelected(
			final Page _page,
			final ContorlPicture _cp,
			final ControlSelectionTransform _cps) {


		// If the list of PaintObjects has not been initialized yet or
		// the list is empty return that there is nothing to do
		if (ls_poSelected == null 
				|| ls_poSelected.isEmpty()) {
			return false;
		}
		
		// Start a transaction. That means that after the transaction has
		// been terminated, the current item of the list is reset.
    	final int id_transaction = ls_po_sortedByY
    			.startTransaction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int id_closedAction = ls_po_sortedByY
    			.startClosedAction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);

		// Initialize new list into which the Items are inserted that are inside
		// the specified rectangle. List is sorted by id for painting the
		// items chronologically.
		SecureListSort<PaintObject> ls_poChronologic 
		= new SecureListSort<PaintObject>();

		// reset value for debugging and speed testing.
		State.setCounter_paintedPoints(0);

		
		ls_poSelected.toFirst(id_transaction, id_closedAction);
		Rectangle r_max = null;
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {

			// if the current item is not initialized only perform the
			// next operation.
			if (ls_poSelected.getItem() != null) {

				// create new Rectangle consisting of the bounds of the current
				// paitnObject otherwise adjust the existing bounds
				if (r_max == null) {
					Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
					r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height
							+ b.y);
				} else {
					Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
					r_max.x = Math.min(r_max.x, b.x);
					r_max.y = Math.min(r_max.y, b.y);
					r_max.width = Math.max(r_max.width, b.x + b.width);
					r_max.height = Math.max(r_max.height, b.y + b.height);
				}


				//insert the current element into the list containing 
				//the selected items by sorted by time of insertion 
				//indicated by their index.
				ls_poChronologic.insertSorted(ls_poSelected.getItem(),
						ls_poSelected.getItem().getElementId(),
						SecureList.ID_NO_PREDECESSOR);
			} else {

				// log severe error because of unnecessary PaintObject inside
				// list.
				State.getLogger().severe(
						"Error. Null PaintObject inside"
								+ " the list of sorted paintObjects.");
			}
			ls_poSelected.next(id_closedAction, id_transaction);
		}

		/*
		 * Go through the sorted list of items and paint them
		 */
		ls_poChronologic.toFirst(SecureList.ID_NO_PREDECESSOR, 
				SecureList.ID_NO_PREDECESSOR);
		BufferedImage verbufft = Util.getEmptyBISelection();
		BufferedImage verbufft2 = Util.getEmptyBISelection();
		int counter = 0;
		while (!ls_poChronologic.isBehind() && !ls_poChronologic.isEmpty()) {

			if (ls_poChronologic.getItem() instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) ls_poChronologic
						.getItem();
				pow.enableSelected();
			}
			// paint the object.
			ls_poChronologic.getItem().paint(verbufft2, false, verbufft,
					_page.getJlbl_painting().getLocation().x,
					_page.getJlbl_painting().getLocation().y, 
					null);

			if (ls_poChronologic.getItem() instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) ls_poChronologic
						.getItem();
				pow.disableSelected();
			}
			
			counter++;
			ls_poChronologic.next(SecureList.ID_NO_PREDECESSOR, 
					SecureList.ID_NO_PREDECESSOR);
		}
		//log repainting action in console.
		if (counter > 0) {
			Console.log(counter
					+ " Item painted in rectanlge (" + r_max + ").",
					Console.ID_INFO_UNIMPORTANT, 
					getClass(), "repaintRectangle");
		}
		
		// print logging method
		State.getLogger().info("Painted " + State.getCounter_paintedPoints()
						+ "pixel points for this operation. (paint selected)");

		//finish transaction and finish closed action; adjust the current
		//element to its state before the list transaction.
		ls_po_sortedByY.finishTransaction(id_transaction);
		ls_po_sortedByY.finishClosedAction(id_closedAction);

		_page.getJlbl_selectionPainting()
				.setIcon(new ImageIcon(verbufft));

		if (r_max != null) {

			Rectangle realRect = new Rectangle(r_max.x, r_max.y, r_max.width
					- r_max.x, r_max.height - r_max.y);

			// adapt the rectangle to the currently used zoom factor.
			final double cZoomFactorWidth = 1.0 * State.getImageSize().width
					/ State.getImageShowSize().width;
			final double cZoomFactorHeight = 1.0 * State.getImageSize().height
					/ State.getImageShowSize().height;
			realRect.x = (int) (1.0 * realRect.x / cZoomFactorWidth);
			realRect.width = (int) (1.0 * realRect.width / cZoomFactorWidth);
			realRect.y = (int) (1.0 * realRect.y / cZoomFactorHeight);
			realRect.height = (int) (1.0 * realRect.height / cZoomFactorHeight);

			realRect.x += _page.getJlbl_painting().getLocation().x;
			realRect.y += _page.getJlbl_painting().getLocation().y;

			_cp.refreshRectangle(realRect.x, realRect.y, realRect.width,
							realRect.height);
			_cps.setR_selection(realRect);
			_cp.paintEntireSelectionRect(realRect);

			return true;
		}
		return false;	
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Paint the selected items to the selection JLabel.
	 * NOT USED ANYMORE
	 * 
	 * @return whether there is something to be painted or not.
	 */
	public boolean paintSelectedOld(
			final Page _page,
			final ContorlPicture _cp,
			final ControlSelectionTransform _cps) {

		_page.getJlbl_selectionPainting().setLocation(0, 0);
		BufferedImage verbufft = Util.getEmptyBISelection();
		BufferedImage verbufft2 = Util.getEmptyBISelection();

    	//start transaction and closed action.
    	final int transaction = ls_po_sortedByY
    			.startTransaction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = ls_po_sortedByY
    			.startClosedAction("paintSelected", 
    					SecureList.ID_NO_PREDECESSOR);
        
		
		ls_poSelected.toFirst(transaction, closedAction);
		Rectangle r_max = null;
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {
			if (ls_poSelected.getItem() != null) {

				// create new Rectangle consisting of the bounds of the current
				// paitnObject otherwise adjust the existing bounds
				if (r_max == null) {
					Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
					r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height
							+ b.y);
				} else {
					Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
					r_max.x = Math.min(r_max.x, b.x);
					r_max.y = Math.min(r_max.y, b.y);
					r_max.width = Math.max(r_max.width, b.x + b.width);
					r_max.height = Math.max(r_max.height, b.y + b.height);
				}

				if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem();
					pow.enableSelected();
				}
				// paint the object.
				ls_poSelected.getItem().paint(verbufft2, false, verbufft,
						_page.getJlbl_painting().getLocation().x,
						_page.getJlbl_painting().getLocation().y, 
						null);

				if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem();
					pow.disableSelected();
				}

			}
			ls_poSelected.next(transaction, closedAction);
		}


    	//close transaction and closed action.
    	ls_po_sortedByY.finishTransaction(
    			transaction);
    	ls_po_sortedByY.finishClosedAction(
    			closedAction);
    	
		_page.getJlbl_selectionPainting()
				.setIcon(new ImageIcon(verbufft));

		if (r_max != null) {

			Rectangle realRect = new Rectangle(r_max.x, r_max.y, r_max.width
					- r_max.x, r_max.height - r_max.y);

			// adapt the rectangle to the currently used zoom factor.
			final double cZoomFactorWidth = 1.0 * State.getImageSize().width
					/ State.getImageShowSize().width;
			final double cZoomFactorHeight = 1.0 * State.getImageSize().height
					/ State.getImageShowSize().height;
			realRect.x = (int) (1.0 * realRect.x / cZoomFactorWidth);
			realRect.width = (int) (1.0 * realRect.width / cZoomFactorWidth);
			realRect.y = (int) (1.0 * realRect.y / cZoomFactorHeight);
			realRect.height = (int) (1.0 * realRect.height / cZoomFactorHeight);

			realRect.x += _page.getJlbl_painting().getLocation().x;
			realRect.y += _page.getJlbl_painting().getLocation().y;

			_cp.refreshRectangle(realRect.x, realRect.y, realRect.width,
							realRect.height);
			_cps.setR_selection(realRect);
			_cp.paintEntireSelectionRect(realRect);

			return true;
		}
		return false;
	}

	/**
	 * Paint the selected items to the selection JLabel.
	 * 
	 * @return whether there is something to be painted or not.
	 */
	public boolean paintSelectedInline(
			final ControlSelectionTransform _controlPaintSelection,
			final Page _page,
			final ContorlPicture _controlPic) {

		// it occurred that the start point equal to 0. Why?
		int px, py;
		if (_controlPaintSelection.getPnt_start() == null) {
			px = 0;
			py = 0;
		} else {
			px = (int) (_controlPaintSelection.getPnt_start().getX() 
					- _page.getJlbl_painting().getLocation().getX());
			py = (int) (_controlPaintSelection.getPnt_start().getY()
					- _page.getJlbl_painting().getLocation().getY());

		}
		
		
		//reset the point which shaves the shift of the selection
		//because now the shift that has been done is applied.
		_controlPaintSelection.resetPntStartLocationLabel();
		_page.getJlbl_selectionPainting().setLocation(0, 0);
		
		

		BufferedImage verbufft = Util.getEmptyBISelection();
		BufferedImage verbufft2 = Util.getEmptyBISelection();
		

    	//start transaction and closed action.
    	final int transaction = ls_po_sortedByY
    			.startTransaction("paintSelectedInline", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = ls_po_sortedByY
    			.startClosedAction("paintSelectedInline", 
    					SecureList.ID_NO_PREDECESSOR);
        
		ls_poSelected.toFirst(transaction, closedAction);
		Rectangle r_max = null;
		while (!ls_poSelected.isEmpty() && !ls_poSelected.isBehind()) {

			if (ls_poSelected.getItem() != null) {

				// create new Rectangle consisting of the bounds of the current
				// paitnObject otherwise adjust the existing bounds
				if (r_max == null) {
					Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
					r_max = new Rectangle(b.x, b.y, b.width + b.x, b.height
							+ b.y);
				} else {
					Rectangle b = ls_poSelected.getItem().getSnapshotBounds();
					r_max.x = Math.min(r_max.x, b.x);
					r_max.y = Math.min(r_max.y, b.y);
					r_max.width = Math.max(r_max.width, b.x + b.width);
					r_max.height = Math.max(r_max.height, b.y + b.height);
				}

				if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem();
					pow.enableSelected();
				}
				// paint the object.
				ls_poSelected.getItem().paint(
						verbufft2,
						false,
						verbufft,
						_page.getJlbl_painting().getLocation().x
								- px,
								_page.getJlbl_painting().getLocation().y
								- py, null);

				if (ls_poSelected.getItem() instanceof PaintObjectWriting) {
					PaintObjectWriting pow = (PaintObjectWriting) ls_poSelected
							.getItem();
					pow.disableSelected();
				}

			}
			ls_poSelected.next(transaction, closedAction);
		}

    	//close transaction and closed action.
    	ls_po_sortedByY.finishTransaction(
    			transaction);
    	ls_po_sortedByY.finishClosedAction(
    			closedAction);
		
		_page.getJlbl_selectionPainting()
				.setIcon(new ImageIcon(verbufft));

		if (r_max != null) {

			Rectangle realRect = new Rectangle(r_max.x, r_max.y, r_max.width
					- r_max.x, r_max.height - r_max.y);

			// adapt the rectangle to the currently used zoom factor.
			final double cZoomFactorWidth = 1.0 * State.getImageSize().width
					/ State.getImageShowSize().width;
			final double cZoomFactorHeight = 1.0 * State.getImageSize().height
					/ State.getImageShowSize().height;
			realRect.x = (int) (1.0 * realRect.x / cZoomFactorWidth);
			realRect.width = (int) (1.0 * realRect.width / cZoomFactorWidth);
			realRect.y = (int) (1.0 * realRect.y / cZoomFactorHeight);
			realRect.height = (int) (1.0 * realRect.height / cZoomFactorHeight);

			realRect.x += _page.getJlbl_painting().getLocation().x;
			realRect.y += _page.getJlbl_painting().getLocation().y;

			_controlPic
					.refreshRectangle(realRect.x, realRect.y, realRect.width,
							realRect.height);
			_controlPaintSelection.setR_selection(realRect);
			_controlPic
					.paintEntireSelectionRect(realRect);
			return true;
		}
		
		return false;
	}

	/**
	 * release selected elements to normal list.
	 */
	public synchronized void releaseSelected(
			final ControlSelectionTransform _controlPaintSelection,
			final CTabSelection _controlTabSelection,
			final Debug _paintObjects,
			final int _xLocationPaint, final int _yLocationPaint) {


		// deactivates to change operations of selected items
		_controlTabSelection.deactivateOp();
		if (ls_poSelected == null) {
			State.getLogger().info("o selected elements");
			return;
		}
		
		//create new transaction
		int transaction = ls_poSelected.startTransaction(
				"picture release selected", 
				SecureList.ID_NO_PREDECESSOR);
		
		//pass the list.
		ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		while (!ls_poSelected.isEmpty()) {

			PaintObject po = ls_poSelected.getItem();

			if (po == null) {
				State.getLogger().warning("error: empty list item");
			}

			new PictureOverview(_paintObjects).removeSelected(po);

			if (po instanceof PaintObjectWriting) {
				PaintObjectWriting pow = (PaintObjectWriting) po;
				new PictureOverview(_paintObjects).add(pow);
				ls_po_sortedByY.insertSorted(pow, pow.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);
			} else if (po instanceof PaintObjectImage) {
				PaintObjectImage poi = (PaintObjectImage) po;
				new PictureOverview(_paintObjects).add(poi);

				ls_po_sortedByY.insertSorted(poi, poi.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);
			} else if (ls_poSelected.getItem() instanceof POLine) {

				POLine p = (POLine) ls_poSelected.getItem();
				p.recalculateSnapshotBounds();
				new PictureOverview(_paintObjects).add(p);

				ls_po_sortedByY.insertSorted(p, p.getSnapshotBounds().y,
						SecureList.ID_NO_PREDECESSOR);
			} else if (po != null) {
				State.getLogger().warning("unknown kind of PaintObject" + po);
			}
			ls_poSelected.remove(transaction);
			ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		}
		
		//finish transaction and destroy list of selected items.
		ls_poSelected.finishTransaction(transaction);
		ls_poSelected = null;
	}

	/**
	 * release selected elements to normal list.
	 */
	public synchronized void deleteSelected(
			final Debug _pos,
			final CTabSelection _cts) {

		if (ls_poSelected == null) {
			State.getLogger().info("o selected elements");
			return;
		}
		//create new transaction
		int transaction = ls_poSelected.startTransaction(
				"picture delete selected", 
				SecureList.ID_NO_PREDECESSOR);
		
		//pass the list.
		ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		while (!ls_poSelected.isEmpty()) {

			if (ls_poSelected.getItem() == null) {
				State.getLogger().warning("error: empty list item");
			}

			new PictureOverview(_pos).removeSelected(ls_poSelected.getItem());
			ls_poSelected.remove(transaction);
			ls_poSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
		}
		// deactivates to change operations of selected items
		_cts.deactivateOp();

		//finish transaction and destroy list of selected items.
		ls_poSelected.finishTransaction(transaction);
		ls_poSelected = null;
	}

	/*
	 * save and load
	 */

	/**
	 * load an image to the picture. and write it into bi_resized.
	 * 
	 * @param _wsLoc
	 *            the location of the image
	 * @return the size of the image
	 */
	public DPoint load(final String _wsLoc) {
		BufferedImage bi_normalSize;
		try {
			
			if (_wsLoc.endsWith(".pdf")) {
				State.getLogger().severe("Wrong calling method load. Have"
						+ " to call Project.load instead of picutre.load.");

			} else {

				
				
				BufferedImage bi_unnormalSchaizz = ImageIO.read(new File(_wsLoc));
				bi_normalSize = new BufferedImage(bi_unnormalSchaizz.getWidth(),
						bi_unnormalSchaizz.getHeight(), 
						BufferedImage.TYPE_INT_ARGB);

				for (int x = 0; x < bi_unnormalSchaizz.getWidth(); x++) {

					for (int y = 0; y < bi_unnormalSchaizz.getHeight(); y++) {
						bi_normalSize.setRGB(x, y, bi_unnormalSchaizz.getRGB(x, y));
					}
				}

				State.setImageSize(new Dimension(bi_normalSize.getWidth(),
						bi_normalSize.getHeight()));
				State.setImageShowSize(new Dimension(bi_normalSize.getWidth(),
						bi_normalSize.getHeight()));

				if (ls_po_sortedByY == null) {
					createSelected();
				}

				//create new transaction
				int transaction = ls_po_sortedByY.startTransaction(
						"load", 
						SecureList.ID_NO_PREDECESSOR);
				ls_po_sortedByY.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
				PaintObjectImage poi_current = createPOI(bi_normalSize);
				ls_po_sortedByY.insertSorted(poi_current,
						poi_current.getSnapshotBounds().y, transaction);

				//finish transaction and destroy list of selected items.
				ls_po_sortedByY.finishTransaction(transaction);

			} 
		} catch (IOException e) {
			Util.handleException(
					"Error opening image: File not found", 
					"Error opening image file " + _wsLoc 
					+ ".\nThe input file can not be opened.",
					e, null);
			
			
		}
		return new DPoint(State.getImageSize().getWidth(), State
				.getImageSize().getHeight());
	}




	/*
	 * getter and setter methods
	 */

	/**
	 * @return the pen_current
	 */
	public Pen getPen_current() {
		return pen_current;
	}

	/**
	 * @return the ls_po_sortedByX
	 */
	public SecureListSort<PaintObject> getLs_po_sortedByY() {
		return ls_po_sortedByY;
	}


	/**
	 * @return the ls_poSelected
	 */
	public SecureList<PaintObject> getLs_poSelected() {
		return ls_poSelected;
	}

	/**
	 * Returns whether the current paint object is equal to zero; thus the
	 * Picture is ready for creating a new PaintObject.
	 * 
	 * Used because there are PaintObjects (such as POCurve) that need multiple
	 * mouse presses, and releases.
	 * 
	 * Thus the Controller class for the painting has to check whether there is
	 * a paintObejct ready if the 'new' PaintObject would be of one of the above
	 * mentioned types.
	 * 
	 * @return whether the current paintObject does not exist.
	 */
	public boolean isPaintObjectReady() {

		return (po_current == null);
	}

	/**
	 * Set the pen after the user clicked at a pen button. Thus clone the pen
	 * and change its color afterwards to the last used one.
	 * 
	 * @param _pen
	 *            the pen
	 * @param _id
	 *            whether pen 1 or pen 2.
	 */
	public void userSetPen(final Pen _pen, final int _id) {

		Pen pen = Pen.clonePen(_pen);
		if (_id == 1) {
			pen.setClr_foreground(State.getPenSelected1().getClr_foreground());
			State.setPenSelected1(pen);
		} else if (_id == 2) {
			pen.setClr_foreground(State.getPenSelected2().getClr_foreground());
			State.setPenSelected2(pen);
		} else {
			State.getLogger().severe("wrong identifier: " + _id);
		}
	}


	/**
	 * Return whether some PaintObjects are selected or not.
	 * @return whether there is something selected or not.
	 */
	public boolean isSelected() {
		return !(ls_poSelected == null || ls_poSelected.isEmpty());
	}

	/**
	 * @return the history
	 */
	public HistorySession getHistory() {
		return history;
	}

	/**
	 * @param _history the history to set
	 */
	public void setHistory(final HistorySession _history) {
		this.history = _history;
	}

}
