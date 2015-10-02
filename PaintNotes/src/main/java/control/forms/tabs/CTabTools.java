//package declaration
package control.forms.tabs;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import control.ContorlPicture;
import control.ControlPaint;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectDrawImage;
import model.objects.painting.po.PaintObjectWriting;
import model.settings.Constants;
import model.settings.State;
import model.settings.StateStandard;
import model.settings.ViewSettings;
import model.util.DPoint;
import model.util.DRect;
import model.util.DragAndDrop;
import model.util.Util;
import model.util.adt.list.List;
import model.util.paint.MyClipboard;
import model.util.paint.Utils;
import view.View;
import view.forms.Message;
import view.forms.Page;
import view.tabs.Tools;
import view.util.Item1PenSelection;
import view.util.VButtonWrapper;

/**
 * Controller class dealing with main paint stuff.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CTabTools extends DragAndDrop implements ActionListener, MouseListener {


	/**
	 * The StiftAuswahl which currently is selected for both groups.
	 */
	private Item1PenSelection[] lastSelected = null;
	
    
    /**
     * Instance of ControlPaint.
     */
    private ControlPaint controlPaint;
    
    /**
     * empty utility class Constructor.
     * @param _cp the instance of ControlPaint
     */
    public CTabTools(final ControlPaint  _cp) {

    	this.controlPaint = _cp;
        // initialize and start action

		//initialize the last selected array
		this.lastSelected = new Item1PenSelection[2];
    }

    
    /**
     * Error-checked getter method.
     * @return the controlPicture.
     */
    private ContorlPicture getControlPicture() {
    	return controlPaint.getControlPic();
    }
    
    
    /**
     * Fetch the instance of tab paint.
     * @return the tab paint.
     */
    public Tools getTabPaint() {
    	
    	if (controlPaint != null
    			&& controlPaint.getView() != null
    			&& controlPaint.getView().getTabs() != null
    			&& controlPaint.getView().getTabs().getTab_paint() != null) {
    		return controlPaint.getView().getTabs().getTab_paint();
    	} else {
    		
    		State.getLogger().severe("Tab does not exist!");
    		return null;
    	}
    }


    /**
     * Action-listener which handles the action of the following .
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent _event) {

    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Tools paint = getTabPaint();

    	
    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {
    		
    		if (
    				//turn inverted
    				_event.getSource().equals(paint.getTb_turnNormal()
    				.getActionCause())) {


    			/**
    			 * The command which is executed in terminal for rotating the
    			 * screen.
    			 */
	        	final String commandNormal = "xrandr -o normal";
	        	
	        	/**
	        	 * The result of the command's execution in terminal.
	        	 * If the response tells that the command has been executed
	        	 * successfully, there is nothing to do. Otherwise 
	        	 * perform rotation done by program and print a warning.
	        	 */
	        	final String result = Util.executeCommandLinux(commandNormal);
	        	if (result.startsWith(Util.EXECUTION_SUCCESS)) {
	        		
	        		//print success information
	        		State.getLogger().info("Rotation normal success");
	        	} else if (result.startsWith(Util.EXECUTION_FAILED)) {
	        		
	        		
	        		//if the window has not been inverted yet invert it by
	        		//using the implemented methods turn. Otherwise there
	        		//is nothing to do.
	        		if (State.isNormalRotation()) {
	
	        			
	            		//print a warning and turn the instance of view 
	        			//afterwards
	            		State.getLogger().warning("beta rotation");
	                    getView().turn();
	                    
	                    //set the new rotation value.
	                    State.setNormalRotation(false);
	            	}
	            }
	        } else if (
	        		
	        		//turn normal
	        		_event.getSource().equals(
	        				paint.getTb_turnMirror().getActionCause())) {


    			/**
    			 * The command which is executed in terminal for rotating the
    			 * screen.
    			 */
	        	final String commandInverted = "xrandr -o inverted";

	        	/**
	        	 * The result of the command's execution in terminal.
	        	 * If the response tells that the command has been executed
	        	 * successfully, there is nothing to do. Otherwise 
	        	 * perform rotation done by program and print a warning.
	        	 */
	        	final String result = Util.executeCommandLinux(commandInverted);
	        	if (result.startsWith(Util.EXECUTION_SUCCESS)) {

	        		//print success information
	        		State.getLogger().info("Rotation normal success");
	        	} else if (result.startsWith(Util.EXECUTION_FAILED)) {
	        
	        		//if the window has been inverted yet invert it by
	        		//using the implemented methods turn. Otherwise there
	        		//is nothing to do.
	        		if (!State.isNormalRotation()) {
	
	        			
	            		//print a warning and turn the instance of view 
	        			//afterwards
	            		State.getLogger().warning("beta rotation");
	                    getView().turn();

	                    //set the new rotation value.
	                    State.setNormalRotation(true);
	            	}
	            }
	        } else if (_event.getSource().equals(
                    paint.getTb_new().getActionCause())) {
                mr_new();
            } else if (_event.getSource().equals(
            		paint.getTb_save().getActionCause())) {
                mr_save();
            } else if (_event.getSource().equals(
            		paint.getTb_saveAs().getActionCause())) {
                mr_saveAs();
            } else if (_event.getSource().equals(
            		paint.getTb_load().getActionCause())) {
                mr_load();
            } else if (_event.getSource().equals(
            		paint.getTb_zoomOut().getActionCause())) {
                mr_zoomOut();
            } else if (_event.getSource().equals(
            		paint.getTb_copy().getActionCause())) {
                mr_copy();
            } else if (_event.getSource().equals(
            		paint.getTb_paste().getActionCause())) {
                mr_paste();
            } else if (_event.getSource().equals(
            		paint.getTb_cut().getActionCause())) {
                mr_cut();
            } else if (_event.getSource().equals(
            		paint.getTb_eraseAll().getActionCause())) {
            	State.setEraseIndex(Constants.ERASE_ALL);
            	getView().getTabs().getTab_paint().getTb_erase().setOpen(false);
            	
            } else if (_event.getSource().equals(
            		paint.getTb_eraseDestroy().getActionCause())) {

            	State.setEraseIndex(Constants.ERASE_DESTROY);
            	getView().getTabs().getTab_paint().getTb_erase().setOpen(false);
            } else if (_event.getSource().equals(
            		paint.getTb_prev().getActionCause())) {
            	
            	controlPaint.getPicture().getHistory().applyPrevious();
            	controlPaint.getControlPic().refreshPaint();
            	
            } else if (_event.getSource().equals(
            		paint.getTb_next().getActionCause())) {

            	controlPaint.getPicture().getHistory().applyNext();
            	controlPaint.getControlPic().refreshPaint();
            	
            } else if (_event.getSource().equals(
            		paint.getTb_insertImage().getActionCause())) {
            	JFileChooser jfc = new JFileChooser();
            	jfc.showOpenDialog(controlPaint.getView());
            	jfc.setVisible(true);
            	File s = jfc.getSelectedFile();
            	
            	if (s != null) {
                	controlPaint.getProject().getCurrentPicture().addPaintObjectImage(Utils.readImageFromOutiseJar(s.getPath()));
                	controlPaint.getControlPic().refreshPaint();
            	}
            }
    	}
    }
    

    /**
     * Error-checked getter method for getting instance of the view class
     * page.
     * @return	instance of the view class page.
     */
    private Page getPage() {
    	
    	if (controlPaint != null) {
    		if (controlPaint.getView() != null) {

    	    	return controlPaint.getView().getPage();
    		} else {
    			State.getLogger().severe("controlPaint.getView() == null");
    		}
    	} else {
			State.getLogger().severe("controlPaint == null");
    	}
    	return null;
    	
    }

    
    /**
     * Error-checked getter method for getting instance of the view class view.
     * 
     * @return instance of the view class View.
     */
    private View getView() {
    	return controlPaint.getView();
    }

    



	/*
     * 
     * 
     * clipboard
     * 
     * 
     */
    
    /**
     * MouseReleased method for button press at button cut.
     */
    public void mr_cut() {

        MyClipboard.getInstance().copyPaintObjects(
        		controlPaint.getPicture(),
                controlPaint.getPicture().getLs_poSelected(), 
                controlPaint.getPicture().paintSelectedBI(new DRect(controlPaint
                		.getControlPaintSelection().getR_selection())));
        
        controlPaint.getPicture().deleteSelected(
        		controlPaint.getView().getTabs().getTab_debug(),
        		controlPaint.getcTabSelection());
        getControlPicture().releaseSelected();
        getControlPicture().refreshPaint();
    
    }
    

    /**
     * MouseReleased method for button press at button paste.
     */
    public void mr_paste() {

    	getControlPicture().releaseSelected();
        controlPaint.getPicture().releaseSelected(
    			controlPaint.getControlPaintSelection(),
    			controlPaint.getcTabSelection(),
    			controlPaint.getView().getTabs().getTab_debug(),
    			controlPaint.getView().getPage().getJlbl_painting()
    			.getLocation().x,
    			controlPaint.getView().getPage().getJlbl_painting()
    			.getLocation().y);
        controlPaint.getPicture().createSelected();
        
        Object o = MyClipboard.getInstance().paste();
        if (o instanceof BufferedImage) {
            PaintObjectDrawImage poi = controlPaint.getPicture().createPOI(
                    (BufferedImage) o);
            controlPaint.getPicture().insertIntoSelected(
            		poi, controlPaint.getView().getTabs().getTab_debug());
            //finish insertion into selected.
            controlPaint.getPicture().finishSelection(
            		controlPaint.getcTabSelection());
            
            controlPaint.getPicture().paintSelected(getPage(),
        			controlPaint.getControlPic(),
        			controlPaint.getControlPaintSelection());
            getPage().getJlbl_backgroundStructure().repaint();

        } else if (o instanceof List) {
            @SuppressWarnings("unchecked")
            List<PaintObject> ls = (List<PaintObject>) o;
            ls.toFirst();
            /*
             * Calculate the center of the entire selection
             * because that center is to be placed at the image's center.
             */
            Point pnt_centerInImage  = new Point();
            int amount = 0;
            
            //calculate the shift
            while (!ls.isEmpty() && !ls.isBehind()) {
            	int cX = ls.getItem().getSnapshotBounds().width / 2
            			+ ls.getItem().getSnapshotBounds().x;
            	int cY = ls.getItem().getSnapshotBounds().height / 2 
            			+ ls.getItem().getSnapshotBounds().y;
            	pnt_centerInImage.x += cX;
            	pnt_centerInImage.y += cY;
            	amount++;
            	ls.next();
            }
            
            //divide the sum of the image bounds by the total amount of items
            pnt_centerInImage.x /= amount;
            pnt_centerInImage.y /= amount;
            
            final double stretchWidth = 1.0 * State.getImageSize().getWidth()
            		/ State.getImageShowSize().getWidth(),
            		stretchHeight = 1.0 * State.getImageSize().getHeight()
            		/ State.getImageShowSize().getHeight();
            // calculate the wanted result for the center, thus the coordinates
            // of the currently displayed image-scope's center.
            Point pnt_wanted = new Point(
            		(int) ((-getPage().getJlbl_painting().getLocation().getX()
            				+ getPage().getJlbl_painting().getWidth() / 2)
            				* stretchWidth),
            		(int) ((-getPage().getJlbl_painting().getLocation().getY()
            				+ getPage().getJlbl_painting().getHeight() / 2)
            				* stretchHeight)
            			);
           Point pnt_move = new Point(
        		   -pnt_centerInImage.x + pnt_wanted.x,
        		   -pnt_centerInImage.y + pnt_wanted.y);
            
            ls.toFirst();
            while (!ls.isEmpty() && !ls.isBehind()) {
                PaintObject po = ls.getItem();
                
                if (po instanceof PaintObjectDrawImage) {
                    PaintObjectDrawImage poi = (PaintObjectDrawImage) po;
                    PaintObjectDrawImage poi_new = controlPaint.getPicture()
                    		.createPOI(poi.getSnapshot());
                    controlPaint.getPicture().insertIntoSelected(
                    		poi_new, controlPaint.getView().getTabs()
                    		.getTab_debug());

                    //finish insertion into selected.
                    controlPaint.getPicture().finishSelection(
                    		controlPaint.getcTabSelection());
                    
                } else if (po instanceof PaintObjectWriting) {
                    PaintObjectWriting pow = (PaintObjectWriting) po;
                    PaintObjectWriting pow_new 
                    = controlPaint.getPicture().createPOW(pow.getPen());
                    
                    pow.getPoints().toFirst();
                    while (!pow.getPoints().isEmpty() 
                            && !pow.getPoints().isBehind()) {
                        pow_new.addPoint(new DPoint(
                                pow.getPoints().getItem()));
                        pow.getPoints().next();
                    }
                    controlPaint.getPicture().insertIntoSelected(pow_new, 
                    		controlPaint.getView().getTabs().getTab_debug());

                
                } else  if (po != null) {
                    State.getLogger().warning("unknown kind of "
                            + "PaintObject; element = " + po);
                }
                ls.next();
            }
            //finish insertion into selected.
            controlPaint.getPicture().finishSelection(
            		controlPaint.getcTabSelection());
            controlPaint.getPicture().moveSelected(pnt_move.x, pnt_move.y);
            
        } else if (o instanceof PaintObjectWriting) {
        	
        	//theoretically unused because everything is stored
        	//inside lists.
            controlPaint.getPicture().insertIntoSelected(
                    (PaintObjectWriting) o, 
                    controlPaint.getView().getTabs().getTab_debug());

            //finish insertion into selected.
            controlPaint.getPicture().finishSelection(
            		controlPaint.getcTabSelection());
        } else if (o instanceof PaintObjectDrawImage) {

        	//theoretically unused because everything is stored
        	//inside lists.
            controlPaint.getPicture().insertIntoSelected(
                    (PaintObjectDrawImage) o, 
                    controlPaint.getView().getTabs().getTab_debug());

            //finish insertion into selected.
            controlPaint.getPicture().finishSelection(
            		controlPaint.getcTabSelection());
            new Exception("hier").printStackTrace();
        } else {
            State.getLogger().warning("unknown return type of clipboard"
            		+ "\ncontent: " + o);
        }
        controlPaint.getPicture().paintSelected(getPage(),
    			controlPaint.getControlPic(),
    			controlPaint.getControlPaintSelection());
        getPage().getJlbl_backgroundStructure().repaint();
        getControlPicture().refreshPaint();
    }

    /**
     * MouseReleased method for button press at button copy.
     */
    public void mr_copy() {

        MyClipboard.getInstance().copyPaintObjects(
        		controlPaint.getPicture(),
                controlPaint.getPicture().getLs_poSelected(), 
                controlPaint.getPicture().paintSelectedBI(new DRect(
                		controlPaint
                		.getControlPaintSelection().getR_selection())));
            
    }
    
    
    /*
     * 
     * 
     * exit
     * 
     * 
     */



    /*
     * 
     * 
     * File operations
     * 
     * 
     */
    
    
    
    /**
     * the save action.
     */
    public void mr_save() {

    	
        // if not saved yet. Otherwise use the saved save path.
        if (State.getSavePath() == null) {
        	JOptionPane.showMessageDialog(getView(), "Sorry, this file"
        			 + "has not been saved yet.\n\nSpecify save location!");
        	mr_saveAs();
        } else {
        	final String fileEnding = "." + State.getSaveFormat();
        	final int lengthFileEndig = fileEnding.length();
            int d = State.getSavePath().toCharArray().length - lengthFileEndig;
            String firstPath = State.getSavePath().substring(0, d);
            if (!fileEnding.equals(".pic")) {

            	if (fileEnding.equals(".pdf")) {

                    try {
						controlPaint.getProject().savePDF(firstPath + fileEnding);
					} catch (IOException e) {
						e.printStackTrace();
					}
            	} else  {

                    controlPaint.getPicture().saveIMAGE(firstPath, 0, 0, fileEnding);
            	}
            } 
            controlPaint.getProject().saveProject(firstPath + ".pic");

            State.setUncommittedChanges(false);
            controlPaint.getView().getPage().repaint();
        }
    }
    

    
    
    /**
     * the save action.
     */
    public void mr_saveAs() {

    	final String fileEnding;
    	

            // choose a file
            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new java.io.File(StateStandard.getWsLocation()));
            jfc.setDialogTitle("Select save location");
            int retval = jfc.showSaveDialog(getView());

            // if selected a file.
            if (retval == JFileChooser.APPROVE_OPTION) {

                // fetch the selected file.
                File file = jfc.getSelectedFile();

                // edit file ending
                if (!file.getName().toLowerCase().contains(".")) {
                	fileEnding = "." + State.getSaveFormat();
                    file = new File(file.getAbsolutePath() + ".pic");
                } else if (!Constants.endsWithSaveFormat(file.getName())) {
                	
                	fileEnding = "";
                	String formatList = "(";
                	for (String format : Constants.SAVE_FORMATS) {
                		formatList += format + ", ";
                	}
                	formatList = formatList.subSequence(0, 
                			formatList.length() - 2) + ")";
                	
                    JOptionPane.showMessageDialog(getView(),
                            "Error saving file:\nFile extension \"" 
                    + Constants.getFileExtension(file.getName())
                    + "\" not supported! Supported formats:\n\t"
                    + formatList + ".", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    mr_saveAs();
                    return;
                } else {

                	fileEnding = "." 
                	+ Constants.getFileExtension(file.getName());
                }

                // if file already exists
                if (file.exists()) {
                	
                    int result = JOptionPane.showConfirmDialog(
                            getView(), "File already exists. "
                                    + "Owerwrite?", "Owerwrite file?",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.NO_OPTION) {
                        mr_save();
                        return;
                    } else if (result == JOptionPane.CANCEL_OPTION) {
                        // interrupt
                        return;
                    }
                    // overwrite
                }
                if (file != null 
                		&& file.getParentFile() != null
                		&& file.getParentFile().exists() ) {

                	// save file to settings.
                	StateStandard.setWsLocation(file.getParentFile().getAbsolutePath(), true);

                }
                State.setSavePath(file.getAbsolutePath());
            } else {
            	fileEnding = "";
            	return;
            }

        // generate path without the file ending.
        if (State.getSavePath() != null) {

        	final int lengthFileEndig = fileEnding.length();
            int d = State.getSavePath().toCharArray().length - lengthFileEndig;
            String firstPath = State.getSavePath().substring(0, d);
            
            // save images in both formats.
//            controlPaint.getPicture().saveIMAGE(
//            		firstPath, getPage().getJlbl_painting().getLocation().x,
//            		getPage().getJlbl_painting().getLocation().y);
            if (!fileEnding.equals(".pic")) {

            	if (fileEnding.equals(".pdf")) {

                    try {
						controlPaint.getProject().savePDF(firstPath + fileEnding);
					} catch (IOException e) {
						e.printStackTrace();
					}
            	} else  {

                    controlPaint.getPicture().saveIMAGE(firstPath, 0, 0, fileEnding);
            	}
            } 
            controlPaint.getProject().saveProject(firstPath + ".pic");


            State.setUncommittedChanges(false);
            controlPaint.getView().getPage().repaint();
        }
    }
    

    
    
    /**
     * MouseReleased method for button press at button load.
     */
    public void mr_load() {

    	int i;
    	
    	if (State.isUncommittedChanges()) {

            i = JOptionPane.showConfirmDialog(getView(),
                    "Do you want to save the committed changes? ",
                    "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
    	} else {
    		i = 1;
    	}
        // no
        if (i == 1) {


            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new java.io.File(StateStandard.getWsLocation()));
            jfc.setDialogTitle("Select load location");
            int retval = jfc.showOpenDialog(getView());
            if (retval == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();

                if (file.getName().toLowerCase().endsWith(".pic")) {
                    controlPaint.loadProject(
                    		file.getAbsolutePath());
                    State.setUncommittedChanges(false);
                    getControlPicture().refreshPaint();

                    State.setSavePath(file.getAbsolutePath());
                    
                } else if (Constants.endsWithSaveFormat(file.getName())) {
                    
//                    try {
//                        BufferedImage bi_imageBG = ImageIO.read(file);
//                        State.setImageSize(new Dimension(
//                                bi_imageBG.getWidth(), 
//                                bi_imageBG.getHeight()));
//                        State.setImageShowSize(State.getImageSize());
                        controlPaint.getPicture().emptyImage();
//                        controlPaint.getPicture().addPaintObjectImage(
//                        		bi_imageBG);
                    	controlPaint.getProject().load(file.getAbsolutePath());
                        getControlPicture().refreshPaint();
                        
//                    } catch (IOException e) {
//                        e.printStackTrace();
//
//                        new Error("not supported yet to load pictures "
//                                + "because there are no paintObjects for "
//                                + "pictures"
//                                + "but only those for lines.")
//                        .printStackTrace();
//                    }
                    
                    

                } else {

                    JOptionPane.showMessageDialog(getView(),
                            "Select a .png file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    mr_save();
                    State.setUncommittedChanges(false);
                    return;
                }

    			
            }
        
        
        
        
        
        } else if (i == 0) {
            // yes
            mr_save();
        }        
    }



    /**
     * the action which is performed if new image is released.
     */
    public void mr_new() {

        if (State.isUncommittedChanges()) {
            int i = JOptionPane.showConfirmDialog(getView(),
                    "Do you want to save the committed changes? ",
                    "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
            if (i == 0) {
                mr_save();
                mr_new();
            } 
            
            //if the user does not want to interrupt recall actionNew
            if (i == 1) {

            	controlPaint.getView().getPage().getJpnl_new().setVisible(true);
                controlPaint.getPicture().reload();
                State.setUncommittedChanges(false);

    			
            }
        } else {

        	controlPaint.getView().getPage().getJpnl_new().setVisible(true);
            controlPaint.getPicture().reload();
            State.setUncommittedChanges(false);

			

        }
    }

    
    
    /*
     * 
     * 
     * Zoom
     * 
     * 
     */
    
    
    /**
     * MouseReleased method for button press at button zoom out.
     * 
     * @see ControlPaint.#mr_zoomIn()
     */
    public void mr_zoomOut() {

    	// if allowed to zoom out (has not already reached the max zoom out 
    	// level
        if (-State.getZoomState() + 1 <  ViewSettings.MAX_ZOOM_OUT) {
        	
        	// Change the value which is used for computing the current 
        	// <code> imageShowSize</code>.
        	State.zoomStateZoomOut();
        	
            int newWidth = (int) (State.getImageSize().width
            		* Math.pow(ViewSettings.ZOOM_MULITPLICATOR, State.getZoomState()));
            int newHeight = (int) (State.getImageSize().height
            		* Math.pow(ViewSettings.ZOOM_MULITPLICATOR, State.getZoomState()));

            // contains the not shifted new location of the paint-JLabel.
            // the formula is okay.
            Point oldLocation = new Point(
            		
            		// location of the JLabel (negative)
            		(int) (getPage().getJlbl_painting().getLocation().x 
            		+ getPage().getJlbl_painting().getWidth() 
            		* (ViewSettings.ZOOM_MULITPLICATOR / 2.0 - 1.0/2.0)),
            				
            		(int) (getPage().getJlbl_painting().getLocation().y 
            		+ getPage().getJlbl_painting().getHeight() 
            		* (ViewSettings.ZOOM_MULITPLICATOR / 2.0 - 1.0/2.0)));

            
            // not smaller than the negative image size.
            oldLocation.x = Math.max(oldLocation.x,
            		-(State.getImageShowSize().width
            				- getPage().getWidth()
            				* ViewSettings.ZOOM_MULITPLICATOR));
            oldLocation.y = Math.max(oldLocation.y,
                    -(State.getImageShowSize().height
                    		- getPage().getHeight() 
                    		* ViewSettings.ZOOM_MULITPLICATOR));
            
            // not greater than 0, these calculations prevent the zoom-out 
            // location to be at the upper left of the page.
            oldLocation.x = Math.min(oldLocation.x, 0);
            oldLocation.y = Math.min(oldLocation.y, 0); 
     
            // apply the new image size.
            State.setImageShowSize(new Dimension(newWidth, newHeight));

            getPage().flip();
            getPage().getJlbl_painting().setBounds(
            		
            		// adapt the shifted location to the new image size.
            		// The rounding at entire pixels is performed inside the
            		// setBounds-method. Thus we don't have to cope with it
            		// in here.
            		(oldLocation.x) / ViewSettings.ZOOM_MULITPLICATOR , 
            		(oldLocation.y) / ViewSettings.ZOOM_MULITPLICATOR,
            		
            		// the old width and height.
            		getPage().getJlbl_painting().getWidth(),
            		getPage().getJlbl_painting().getHeight());
            
            // refresh scrollPanes which have changed because of the zooming
            // process.
            getPage().refrehsSps();

            
            // release all selected items. If this is not done, the
            // painted selection size is too big.
            if (controlPaint.getPicture().isSelected()) {

                getControlPicture().releaseSelected();
                controlPaint.getPicture().releaseSelected(
            			controlPaint.getControlPaintSelection(),
            			controlPaint.getcTabSelection(),
            			controlPaint.getView().getTabs().getTab_debug(),
            			controlPaint.getView().getPage().getJlbl_painting()
            			.getLocation().x,
            			controlPaint.getView().getPage().getJlbl_painting()
            			.getLocation().y);
            }
            
            // update the location of the resize buttons for resizing the entire
            // page.
            updateResizeLocation();
        } else {
        	
        	// if the max zoom in level has been reached, print an information
        	// message and update the resize buttons.
            Message.showMessage(Message.MESSAGE_ID_INFO, 
                    "max zoom out reached");
            updateResizeLocation();
        }
    }
    

    
	/**
     * Update the location of the JButtons for resizing. Thus the user is
     * able to resize the whole image.
     */
    public void updateResizeLocation() {

    	// only perform this action if nothing is selected.
		if (!controlPaint.getPicture().isSelected()) {

			// change the location of the resize - buttons. Now that the selection
			// is released, the entire page can be resized.
	        for (int h = 0; h <= 2; h++) {
	            for (int w = 0; w <= 2; w++) {
	                //that are not necessary buttons
	                if (h + w <= 2) {
	
	                    getPage().getJbtn_resize()[h][w].setLocation(
	                    		-getPage().getJbtn_resize()[h][w].getWidth() - 1,
	                    		-getPage().getJbtn_resize()[h][w].getHeight() - 1);
	                } else {
	
	                	//necessary buttons
	                    getPage().getJbtn_resize()[h][w].setLocation(
	                    		+ State.getImageShowSize().width * (h) / 2
	                    		+ getPage().getJlbl_painting().getLocation().x
	                    		- getPage().getJbtn_resize()[h][w].getWidth() / 2,
	                    		+ State.getImageShowSize().height * (w) / 2
	                    		+ getPage().getJlbl_painting().getLocation().y
	                    		- getPage().getJbtn_resize()[h][w].getWidth() / 2);
	                }
				}
			}
		}
    }


	
	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(final MouseEvent _event) {

    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Tools paint = getTabPaint();

    	
    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {
    		

			//if mouse is over a pen
			if (isAStiftAuswahl(_event.getSource())) {
				
				//if is not selected
				Item1PenSelection selected = (Item1PenSelection) 
				        ((VButtonWrapper) _event.getSource()).wrapObject();
				if (!selected.isSelected()) {
				    applyFocus(selected);
				}
				return; 
			} else {
	
		        //for loop for identifying the cause of the event
		        for (int j = 0; j < paint.getJbtn_colors().length; 
		                j++) {
		            if (_event.getSource().equals(
		                    paint.getJbtn_colors()[j])) {
		                
		                //highlight the border of the icon with mouse-over
		                paint.getJbtn_colors()[j].setBorder(
		                        BorderFactory.createCompoundBorder(
		                                new LineBorder(Color.black),
		                                new LineBorder(Color.black)));
		                
		                //return because only one item is performing action 
		                //at one time
		                return;
		            }
		        }
			}
    	}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(final MouseEvent _event) {

    	/**
    	 * Instance of paint fetched out of instance of the main controller
    	 * class.
    	 * The getter method handles the printing of an error message if the 
    	 * instance of Paint is null.
    	 */
    	final Tools paint = getTabPaint();

    	
    	//if the initialization process has terminated without errors
    	//the instance of Paint is not equal to null, thus it is possible to
    	//check the source of the ActionEvent.
    	if (paint != null) {
    		
		
		//outfit of StiftAuswahl
			if (isAStiftAuswahl(_event.getSource())) {
	
				//reset the instance of Pen if this pen is not selected
				Item1PenSelection selected = (Item1PenSelection)
				        ((VButtonWrapper) _event.getSource()).wrapObject();
				if (!selected.isSelected()) {
					selected.setOpaque(false);
				}
				return;
			}
			
			//for loop for identifying the cause of the event
			for (int j = 0; j < paint.getJbtn_colors().length; j++) {
	
				if (_event.getSource().equals(
				        paint.getJbtn_colors()[j])) {
	
					//reset the border of the icon which had mouse-over
					paint.getJbtn_colors()[j].setBorder(BorderFactory
							.createCompoundBorder(new LineBorder(Color.black),
									new LineBorder(Color.white)));
					
					//return because only one item is performing action at one 
					//time
					return;
				}
			}
    	}
	}
	
	/**
	 * apply focus to selected item.
	 * @param _selected the selected item
	 */
	public static void applyFocus(final Item1PenSelection _selected) {

        _selected.setOpaque(true);
        _selected.setBackground(ViewSettings.GENERAL_CLR_BORDER);
    
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(final MouseEvent _event) { }

	
	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(final MouseEvent _event) {

		//if mouse is over a pen
		if (isAStiftAuswahl(_event.getSource())) {

			Item1PenSelection stift_event = (Item1PenSelection)
					((VButtonWrapper) _event.getSource()).wrapObject();
			
			//if pen is not selected yet
			if (lastSelected[stift_event.getPenSelection() - 1] != null 
			        && lastSelected[stift_event.getPenSelection() - 1]
			                != stift_event) {
				lastSelected[stift_event.getPenSelection() - 1]
				        .setSelected(false);
				lastSelected[stift_event.getPenSelection() - 1]
				        .setOpaque(false);
				lastSelected[stift_event.getPenSelection() - 1]
				        .setBackground(Color.green);
			} 
			
			//select the current pen
			stift_event.setSelected(true);
			stift_event.setOpaque(true);
			stift_event.setBackground(ViewSettings.GENERAL_CLR_BORDER);
			
			//set the last selected
			lastSelected[stift_event.getPenSelection() - 1] = stift_event;
		}
	}
	
	/**
	 * Checks whether an Object is a StiftAuswahl wrapper button or not.
	 * 
	 * @param _obj the object
	 * @return the boolean
	 */
	public static boolean isAStiftAuswahl(final Object _obj) {
		try {
			
			((Item1PenSelection) ((VButtonWrapper) _obj).wrapObject())
			.toString();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	
	
	
	

	/**
	 * @return the controlPaint
	 */
	public ControlPaint getControlPaint() {
		return controlPaint;
	}


	/**
	 * @param _controlPaint to set.
	 */
	public void setControlPaint(final ControlPaint _controlPaint) {
		this.controlPaint = _controlPaint;
	}


	@Override
	public void dropHandler(final java.util.List<File> _files) {

    	int i;
    	
    	if (State.isUncommittedChanges()) {

            i = JOptionPane.showConfirmDialog(getView(),
                    "Do you want to save the committed changes? ",
                    "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
    	} else {
    		i = 1;
    	}
    	
        // no uncommitted changes
        if (i == 1) {

                File file = _files.get(0);

                if (file.getName().toLowerCase().endsWith(".pic")) {
                    controlPaint.loadProject(
                    		file.getAbsolutePath());
                    State.setUncommittedChanges(false);
                    getControlPicture().refreshPaint();

                    State.setSavePath(file.getAbsolutePath());
                    
                } else if (Constants.endsWithSaveFormat(file.getName())) {
                    
                        controlPaint.getPicture().emptyImage();
                    	controlPaint.getProject().load(file.getAbsolutePath());
                        getControlPicture().refreshPaint();
                        

                } else {

                    JOptionPane.showMessageDialog(getView(),
                            "Select a .png file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
        } else if (i == 0) {
            // yes
            mr_save();
        }        
    }

}
