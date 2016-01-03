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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.ControlPaint;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectPen;
import model.settings.Constants;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.adt.list.SecureList;
import view.forms.InfoForm;
import view.forms.Message;
import view.forms.PopupChangeSize;
import view.tabs.Selection;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CTabSelection implements ActionListener {

    /**
     */
    private ControlPaint cp;
    
    /**
     * The penID and the color of the currently selected item.
     */
    private int selectionPenID = -1, selectionColor = -1;
    
    /**
     * Empty utility class constructor.
     */
    public CTabSelection(final ControlPaint _selection) {
    	this.cp = _selection;
    	
    }
    
    
    /**
     *
     */
    private Selection getSelection() {
    	
    	if (
    			cp != null
    			&& cp.getView() != null
    			&& cp.getView().getTabs() != null
    			&& cp.getView().getTabs().getTab_selection() != null) {

        	return cp.getView().getTabs().getTab_selection();
    	} else {
    		
    		if (cp != null) {

    			if (cp.getView() != null) {
        			if (cp.getView().getTabs() != null) {
        				if (cp.getView().getTabs().getTab_selection() != null) {

        					System.out.println(
        							"cp.view.tabs.tabselection != null");
        				}
    					System.out.println("cp.view.tabs != null");
        			}
					System.out.println("cp.view != null");
    			}
				System.out.println("cp != null");
    		}
    		System.exit(1);
    		return null;
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent _event) {

    	
    	//
    	// Components from PopupChangeSize which is displayed by the selection
    	// tab.
    	// 
    	if (_event.getSource().equals(
    			PopupChangeSize.getInstance().getMbtn_reset())) {
    		
    		if (cp.getPicture().isSelected()) {

            	// fetch selection rectangle
    			Rectangle r = cp.getControlPaintSelection().getR_selection();
    			if (r != null) {

                	PopupChangeSize.show(r.width, r.height);
    			} else {

                	PopupChangeSize.show(0, 0);
    			}
    		} else {
    			PopupChangeSize.getInstance().setVisible(false);
    		}
    		
    	} else if (_event.getSource().equals(
    			PopupChangeSize.getInstance().getMbtn_exit())){
    		PopupChangeSize.getInstance().setVisible(false);
    		
    	} else if (_event.getSource().equals(
    			PopupChangeSize.getInstance().getMbtn_apply())){
    		applyPopupChangeSize();
    	} else if (
    			//
    			// Components from the View Selection tab.
    			//
    			_event.getSource().equals(getSelection().getJcb_points())) {

            this.selectionPenID = Constants.PEN_ID_POINT;
            selectPenOp(Constants.PEN_ID_POINT);
            
        } else if (_event.getSource().equals(getSelection().getJcb_line())) {

            this.selectionPenID = Constants.PEN_ID_LINES;
            selectPenOp(Constants.PEN_ID_LINES);
            
        } else if (_event.getSource().equals(getSelection().getJcb_maths())) {

            this.selectionPenID = Constants.PEN_ID_MATHS;
            selectPenOp(Constants.PEN_ID_MATHS);
        } else if (_event.getSource().equals(getSelection().getTb_erase()
        		.getActionCause())) {

        	final Picture pic = cp.getPicture();
        	if (pic.isSelected()) {

            	// remove model
            	cp.getControlPic().releaseSelected();
            	pic.deleteSelected(cp.getView().getTabs().getTab_debug(), this);
            	cp.getcTabTools().updateResizeLocation();
        	} else {
        		Message.showMessage(Message.MESSAGE_ID_INFO, "Nothing selected.");
        	}
        } else if (_event.getSource().equals(getSelection().getTb_changeSize()
        		.getActionCause())) {
        	
        	if (PopupChangeSize.getInstance().isVisible()) {
        		PopupChangeSize.getInstance().setVisible(false);
        	} else {

        		if (cp.getPicture().isSelected()) {

                	// fetch selection rectangle
        			Rectangle r = cp.getControlPaintSelection().getR_selection();
        			if (r != null) {

                    	PopupChangeSize.show(r.width, r.height);
        			} else {

                    	PopupChangeSize.show(0, 0);
        			}
        		} else {
        			
        			Picture pic = cp.getPicture();
        			// change size of the entire image
                	PopupChangeSize.show(
                			(int) pic.getSize().getWidth(),
                			(int) pic.getSize().getHeight());
        		}
        	}

        	
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2green().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2GREEN);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2lightBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2LIGHT_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2pink().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2PINK);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2red().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2RED);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2yellow().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2YELLOW);
        	cp.getControlPic().refreshPaint();
        	
        	
        	
        	
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2green().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2GREEN);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2lightBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2LIGHT_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2pink().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2PINK);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2red().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2RED);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_darkBlue2yellow().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_DARK_BLUE2YELLOW);
        	cp.getControlPic().refreshPaint();
        	
        	
        	
        	
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_green2darkBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_GREEN2DARK_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_green2lightBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_GREEN2LIGHT_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_green2pink().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_GREEN2PINK);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_green2red().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_GREEN2RED);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_green2yellow().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_GREEN2YELLOW);
        	cp.getControlPic().refreshPaint();
        	
        	
        	
        	
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_lightBlue2green().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_LIGHT_BLUE2GREEN);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_lightBlue2darkBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_LIGHT_BLUE2DARK_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_lightBlue2pink().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_LIGHT_BLUE2PINK);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_lightBlue2red().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_LIGHT_BLUE2RED);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_lightBlue2yellow().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_LIGHT_BLUE2YELLOW);
        	cp.getControlPic().refreshPaint();
        	
        	
        	
        	
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_pink2green().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_PINK2GREEN);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_pink2lightBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_PINK2LIGHT_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_pink2darkBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_PINK2DARK_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_pink2red().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_PINK2RED);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_pink2yellow().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_PINK2YELLOW);
        	cp.getControlPic().refreshPaint();
        	
        	
        	
        	
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_red2green().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_RED2GREEN);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_red2lightBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_RED2LIGHT_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_red2pink().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_RED2PINK);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_red2darkBlue().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_RED2DARK_BLUE);
        	cp.getControlPic().refreshPaint();
        } else if (_event.getSource().equals(getSelection()
        		.getI1b_red2yellow().getActionCause())) {
        	cp.getPicture().executeTraversionOperation(
        			Picture.ID_TR_RED2YELLOW);
        	cp.getControlPic().refreshPaint();
        	
        	
        	
        	
        
        } else if (_event.getSource().equals(getSelection()
	    		.getI1b_yellow2green().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_YELLOW2GREEN);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_yellow2lightBlue().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_YELLOW2LIGHT_BLUE);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_yellow2pink().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_YELLOW2PINK);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_yellow2darkBlue().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_YELLOW2DARK_BLUE);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_yellow2red().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_YELLOW2RED);
        	cp.getControlPic().refreshPaint();
    	
    	
    	
    	
    	
        
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_grayifyDarkBlue().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_GRAYIFY_DARK_BLUE);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_grayifyGreen().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_GRAYIFY_GREEN);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_grayifyLightBlue().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_GRAYIFY_LIGHT_BLUE);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_grayifyPink().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_GRAYIFY_PINK);
        	cp.getControlPic().refreshPaint();
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_grayifyRed().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_GRAYIFY_RED);
        	cp.getControlPic().refreshPaint();
		
	    } else if (_event.getSource().equals(getSelection()
	    		.getI1b_grayifyYellow().getActionCause())) {
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_GRAYIFY_YELLOW);
        	cp.getControlPic().refreshPaint();
		
		
		
		
	    } else if (_event.getSource().equals(getSelection()
	    		.geti1b_scanOk().getActionCause())) {
	    	
	    	double[] i, j, k;

	    	if (getSelection().getCp_threshold().isSelected()) {

		    	i = getSelection().getMb_threshold().getComputedLocations();
	    	} else {
	    		i = new double[]{0, 0};
	    	}

	    	if (getSelection().getCp_saturation().isSelected()) {

		    	j = getSelection().getMb_saturation().getComputedLocations();
	    	} else {
	    		j = new double[]{1};
	    	}
	    	if (getSelection().getCp_value().isSelected()) {

	    		k = getSelection().getMb_value().getComputedLocations();
	    	} else {
	    		k = new double[]{1};
	    	}
	    	
	    	cp.getPicture().executeTraversionOperation(
	    			Picture.ID_TR_SCAN, new double[]{i[0], i[1],
	    					j[0], k[0]});
        	getSelection().getIm_scanEdit().repaint();
        	cp.getControlPic().refreshPaint();
        	getSelection().getIm_scanEdit().repaint();
	    }
    
        
        
        
        
        
        
        
        
        
        else {
            for (int i = 0; i < getSelection().getJbtn_colors().length; i++) {

                if (_event.getSource().equals(getSelection()
                		.getJbtn_colors()[i])) {
                    
                	setSelectionColor(getSelection()
                			.getJbtn_colors()[i].getBackground()
                			.getRGB());
                }
            }
        }
    }
    
    
    public void applyPopupChangeSize() {


		
		if (PopupChangeSize.getInstance()
				.getMtf_width().getText().equals("")) {

			Message.showMessage(Message.MESSAGE_ID_ERROR, "Width has to be positive.");
		} else if (PopupChangeSize.getInstance()
				.getMtf_width().getText().equals("")) {
			
			Message.showMessage(Message.MESSAGE_ID_ERROR, "Height has to be positive.");
		} else {

			final int newWidth = Integer.parseInt(PopupChangeSize.getInstance()
					.getMtf_width().getText());
			final int newHeight = Integer.parseInt(PopupChangeSize.getInstance()
					.getMtf_height().getText());
			
			if (newWidth == 0) {

				Message.showMessage(Message.MESSAGE_ID_ERROR, "Width has to be positive.");
			} else if (newHeight == 0) {

				Message.showMessage(Message.MESSAGE_ID_ERROR, "Height has to be positive.");
			} else {

				if (cp.getPicture().isSelected()) {

        			Rectangle r = cp.getControlPaintSelection().getR_selection();
        			if (r == null) {
        				Message.showMessage(Message.MESSAGE_ID_ERROR, "No selection available.");
        			} 
				
    				
    				final int widthChange = newWidth - r.width;
    				final int heightChange = newHeight - r.height;
    				r.setSize(newWidth, newHeight);

        			cp.getControlPaintSelection().setR_selection(r);
        			

                    for (int h = 0; h <= 2; h++) {
                        for (int w = 0; w <= 2; w++) {

                        	cp.getView().getPage().getJbtn_resize()[h][w].setLocation(
                            		cp.getControlPaintSelection()
                            		.getR_selection().x
                            		+ cp.getControlPaintSelection()
                            		.getR_selection().width * (h) / 2
                            		- cp.getView().getPage()
                            		.getJbtn_resize()[h][w].getWidth() / 2,
                            		cp.getControlPaintSelection()
                            		.getR_selection().y 
                            		+ cp.getControlPaintSelection()
                            		.getR_selection().height * w / 2
                            		- cp.getView().getPage()
                            		.getJbtn_resize()[h][w].getHeight()
                            		/ 2);

        				}
                    }
        			cp.getControlPaintSelection().mr_stretchImage(widthChange, heightChange);
        			
    			} else {
    				cp.getControlPic().refreshPaint();
	
    				cp.getView().getPage().refrehsSps();
    				cp.getcTabTools().updateResizeLocation();
    			}
			}
		}
	}


	public void setSelectionColor(final int _selectionColor) {

		final Picture pic = cp.getPicture();
    	if (pic.isSelected()) {
    		this.selectionColor = _selectionColor;
            setColor(cp, new Color(selectionColor));
            getSelection().getTb_color().setBackground(
                    new Color(selectionColor));

            pic.paintSelectedInline(
        			cp.getControlPaintSelection(),
        			cp.getView().getPage(),
        			cp.getControlPic());
            activateColor();
    	}
    }
    
    
    /**
     * select penOperation.
     * @param _id_operation the pen operation to select.
     */
    private synchronized void selectPenOp(final int _id_operation) {

    	switch (_id_operation) {
        case Constants.PEN_ID_POINT:

            getSelection().getJcb_points().setSelected(true);
            getSelection().getJcb_maths().setSelected(false);
            getSelection().getJcb_line().setSelected(false);
            setPen(cp, Constants.PEN_ID_POINT);
            cp.getPicture().paintSelectedInline(
        			cp.getControlPaintSelection(),
        			cp.getView().getPage(),
        			cp.getControlPic());
            activatePen();
        
            break;
        case Constants.PEN_ID_LINES:
            
            getSelection().getJcb_line().setSelected(true);
            getSelection().getJcb_maths().setSelected(false);
            getSelection().getJcb_points().setSelected(false);
            setPen(cp, Constants.PEN_ID_LINES);
            cp.getPicture().paintSelectedInline(
        			cp.getControlPaintSelection(),
        			cp.getView().getPage(),
        			cp.getControlPic());
            activatePen();
        
            break;
        case Constants.PEN_ID_MATHS:
            
            getSelection().getJcb_maths().setSelected(true);
            getSelection().getJcb_line().setSelected(false);
            getSelection().getJcb_points().setSelected(false);
            setPen(cp, Constants.PEN_ID_MATHS);
            cp.getPicture().paintSelectedInline(
        			cp.getControlPaintSelection(),
        			cp.getView().getPage(),
        			cp.getControlPic());
            activatePen();
        
            break;
        default:

            getSelection().getJcb_maths().setSelected(false);
            getSelection().getJcb_line().setSelected(false);
            getSelection().getJcb_points().setSelected(false);
            
            break;
        }
    }

    /**
     * sets operation and adapts graphical user interface but does not
     * set the operation to each selected item.
     * @param _id_operation the pen operation to select.
     */
    private synchronized void showPenOp(final int _id_operation) {
        switch (_id_operation) {
        case Constants.PEN_ID_POINT:

            getSelection().getJcb_points().setSelected(true);
            getSelection().getJcb_maths().setSelected(false);
            getSelection().getJcb_line().setSelected(false);
            cp.getPicture().paintSelected(cp.getView().getPage(),
            		cp.getControlPic(),
            		cp.getControlPaintSelection());
        
            break;
        case Constants.PEN_ID_LINES:
            
            getSelection().getJcb_line().setSelected(true);
            getSelection().getJcb_maths().setSelected(false);
            getSelection().getJcb_points().setSelected(false);
            cp.getPicture().paintSelected(cp.getView().getPage(),
            		cp.getControlPic(),
            		cp.getControlPaintSelection());
        
            break;
        case Constants.PEN_ID_MATHS:
            
            getSelection().getJcb_maths().setSelected(true);
            getSelection().getJcb_line().setSelected(false);
            getSelection().getJcb_points().setSelected(false);
            cp.getPicture().paintSelected(cp.getView().getPage(),
            		cp.getControlPic(),
            		cp.getControlPaintSelection());
        
            break;
        default:

            getSelection().getJcb_maths().setSelected(false);
            getSelection().getJcb_line().setSelected(false);
            getSelection().getJcb_points().setSelected(false);
            
            break;
        }
    }
    
    /**
     * Change the selection painting settings.
     * @param _hardChange whether it is the first element that is inserted 
     * into the selected list and thus the method does not have to check whether
     * all previous items had the same settings.
     * @param _penId the id of the pen
     * @param _color the color
     */
    public void change(final boolean _hardChange, 
            final int _penId, final int _color) {
        
        if (_hardChange) {

            activatePen();
            activateColor();
            
            //save values
            this.selectionPenID = _penId;
            this.selectionColor = _color;
//            
            //show pen and color at graphical user interface.
            showPenOp(_penId);
            getSelection().getTb_color().setBackground(
                    new Color(_color));
        } else {
            
            if (selectionPenID != _penId) {
                selectionPenID = -1;
            } else {

                activatePen();
            }
            showPenOp(selectionPenID);

            if (selectionColor != _color) {
                selectionColor = -1;
                deactivateColor();
            } else {

                activateColor();
                getSelection().getTb_color().setBackground(
                        new Color(selectionColor));
            }
        }
    }
    

    
    /**
     * Set id for selected paintObject's pens.
     * @param _id_operation the id
     */
    private static synchronized void setPen(final ControlPaint _cp, 
    		final int _id_operation) {
    	
    	final Picture pic = _cp.getPicture();

    	//start transaction and closed action.
    	final int transaction = pic.getLs_poSelected()
    			.startTransaction("set pen", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = pic.getLs_poSelected()
    			.startClosedAction("set pen", 
    					SecureList.ID_NO_PREDECESSOR);
    	
        pic.getLs_poSelected().toFirst(
        		transaction, closedAction);
        while (!pic.getLs_poSelected().isBehind() 
                && !pic.getLs_poSelected().isEmpty()) {
            PaintObject o = pic.getLs_poSelected().getItem();
            if (o instanceof PaintObjectPen) {
                PaintObjectPen pow = (PaintObjectPen) o;
                pow.getPen().setId_operation(_id_operation);
            }
            pic.getLs_poSelected().next(
            		transaction, closedAction);
        }
        

    	//close transaction and closed action.
    	pic.getLs_poSelected().finishTransaction(
    			transaction);
    	pic.getLs_poSelected().finishClosedAction(
    			closedAction);
        
    }
    
    /**
     * Set selected paintObject's color.
     * @param _clr the Color
     */
    private static synchronized void setColor(
    		final ControlPaint _cp, final Color _clr) {

    	final Picture pic = _cp.getPicture();

    	//start transaction and closed action.
    	final int transaction = pic.getLs_poSelected()
    			.startTransaction("set color", 
    					SecureList.ID_NO_PREDECESSOR);
    	final int closedAction = pic.getLs_poSelected()
    			.startClosedAction("set color", 
    					SecureList.ID_NO_PREDECESSOR);
    	
        if (pic.getLs_poSelected() != null) {

            pic.getLs_poSelected().toFirst(
            		transaction, closedAction);
            while (!pic.getLs_poSelected().isBehind() 
                    && !pic.getLs_poSelected().isEmpty()) {
                PaintObject o = pic.getLs_poSelected()
                        .getItem();
                if (o instanceof PaintObjectPen) {
                    PaintObjectPen pow = (PaintObjectPen) o;
                    pow.getPen().setClr_foreground(new Color(_clr.getRGB()));
                }
                pic.getLs_poSelected().next(
                		transaction, closedAction);
            }
        }

    	//close transaction and closed action.
    	pic.getLs_poSelected().finishTransaction(
    			transaction);
    	pic.getLs_poSelected().finishClosedAction(
    			closedAction);
        
    }

    
    /**
     * Deactivate to perform operation on selected items (e.g. because there
     * is no item that exists or there are no suitable operations for special
     * kind of PaintItem)
     */
    public synchronized void deactivateOp() {

        Selection s = getSelection();
        s.getJcb_maths().setEnabled(false);
        s.getJcb_line().setEnabled(false);
        s.getJcb_points().setEnabled(false);
        selectionPenID = -1;
        selectionColor = -1;
    }
    
    
    /**
     * Activate to perform operation on selected items.
     */
    public synchronized void activateOp() {

        Selection s = getSelection();
        s.getJcb_maths().setEnabled(true);
        s.getJcb_line().setEnabled(true);
        s.getJcb_points().setEnabled(true);
    }
    
    
    /**
     * Activate to perform operation on selected items.
     */
    private synchronized void activatePen() {

        Selection s = getSelection();
        s.getJcb_maths().setEnabled(true);
        s.getJcb_line().setEnabled(true);
        s.getJcb_points().setEnabled(true);
    }

    /**
     * Deactivate to perform operation on selected items (e.g. because there
     * is no item that exists or there are no suitable operations for special
     * kind of PaintItem)
     */
    private synchronized void deactivateColor() {

        Selection s = getSelection();
        s.getTb_color().setVisible(false);
    }
    
    
    /**
     * Activate to perform operation on selected items.
     */
    private synchronized void activateColor() {

        Selection s = getSelection();
        s.getTb_color().setVisible(true);
    }
    

    /**
     * @return the selectionPenID
     */
    public int getSelectionPenID() {
        return selectionPenID;
    }

    /**
     * @return the selectionColor
     */
    public int getSelectionColor() {
        return selectionColor;
    }

    
    
}
