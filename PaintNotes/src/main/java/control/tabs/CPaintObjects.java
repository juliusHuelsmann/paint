package control.tabs;

//import declarations
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import control.ContorlPicture;
import control.ControlPaint;
import control.ControlPaintSelectin;
import model.objects.PictureOverview;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.Pen;
import model.settings.Constants;
import model.settings.Status;
import model.util.DPoint;
import model.util.adt.list.List;
import model.util.adt.list.SecureList;
import view.forms.Page;
import view.tabs.PaintObjects;
import view.util.Item1Button;


/**
 * Controller class for PaintObjects panel which shows the list of existing
 * PaintObjects.
 * @author Julius Huelsmann
 * @version %I%,%U%
 */
public final class CPaintObjects implements ActionListener {


    /**
     * Point contains the coordinates of the last inserted item in graphical
     * user interface. Thus it is possible to insert the next item right after
     * its predecessor.
     */
    private Rectangle rec_old;
    
    private ControlPaint cp;
    
    /**
     * Private utility class constructor.
     */
    public CPaintObjects(ControlPaint _cp) {
    	this.cp = _cp;
    }
    

    private PaintObjects getPaintObjects() {
    	return cp.getView().getTabs().getTab_pos();
    }
    private Page getPage() {
    	return cp.getView().getPage();
    }
    private ContorlPicture getControlPicture() {
    	return cp.getControlPic();
    }
    
    /**
     * ActionListener deals with the action performed by the buttons containing
     * a PaintObject of the PaintObjects' view.
     * @param _event the event that is thrown.
     */
    public void actionPerformed(final ActionEvent _event) {
        
        Component[] c 
        = getPaintObjects().getJpnl_items().getComponents();
        
        
        for (int i = 0; i < c.length; i++) {

            if (c[i] instanceof Item1Button
                    && ((Item1Button) c[i]).getAdditionalInformation() 
                    instanceof PaintObject) {

                Item1Button i1b = (Item1Button) c[i];
                final PaintObject po_cu = (PaintObject) i1b
                        .getAdditionalInformation();
                if (_event.getSource().equals(i1b.getActionCause())) {


                    i1b.setActivated(false);
                    showPaintObjectInformation(po_cu);
                    
                    Picture.getInstance().releaseSelected(
                			cp.getControlPaintSelection(),
                			cp.getcTabSelection(),
                			cp.getView().getTabs().getTab_pos(),
                			cp.getView().getPage().getJlbl_painting().getLocation().x,
                			cp.getView().getPage().getJlbl_painting().getLocation().y);
                    getControlPicture().releaseSelected();



                    Status.setIndexOperation(
                            Constants.CONTROL_PAINTING_INDEX_MOVE);
                    
                    //decativate other menuitems and activate the current one
                    //(move)
                    Picture.getInstance().createSelected();
                    getPaintObjects().deactivate();
                    Picture.getInstance().insertIntoSelected(po_cu, cp.getView().getTabs().getTab_pos());
                    new PictureOverview(getPaintObjects()).remove(po_cu);
                    Picture.getInstance().getLs_po_sortedByX().remove(
                    		SecureList.ID_NO_PREDECESSOR);
                    
                }
            } else {
                Status.getLogger().severe("Error in ActionListener: "
                        + "wrong kind of element. "
                        + "This error should never occure");
            }
        }
        
        //finish insertion into selected.
        Picture.getInstance().finishSelection(cp.getcTabSelection());
        
        Picture.getInstance().paintSelected(getPage(),
    			cp.getControlPic(),
    			cp.getControlPaintSelection());
        getControlPicture().refreshPaint();
        getPaintObjects().repaint();
        getPage().getJlbl_background2().repaint();
       
    }

    
    
    
    /**
     * Add a new PaintObject to the graphical user interface.
     * @param _pov the PictureOverview
     * @return the inserted Item1Button for being able to edit it afterwards.
     */
    public synchronized Item1Button updateAdd(final PictureOverview _pov) {

        //the size of each item
        final int itemSize = 40;
        
        //update the amount of items
        getPaintObjects().getJlbl_amountOfItems().setText("amount = " 
        + _pov.getNumber());

        //create new button for the item
        Item1Button jbtn_new = new Item1Button(null);
        jbtn_new.setAdditionalInformation(_pov.getCurrentPO());
        jbtn_new.setImageWidth(itemSize);
        jbtn_new.setImageHeight(itemSize);
        jbtn_new.setBackground(Color.pink);
        jbtn_new.setBorder(true);
        jbtn_new.addActionListener(new CPaintObjects(cp));
        jbtn_new.setText("ID " + _pov.getCurrentPO().getElementId());
        getPaintObjects().add(jbtn_new);
        jbtn_new.setIcon((_pov.getCurrentPO().getSnapshot()));
        
        
        //repaint view
         getPaintObjects().repaint();
        
        //return the Item1Button for later use (for example @updateAddSelected)
        return jbtn_new;
    
    }
    

    /**
     * Remove a PaintObject from the graphical user interface and eliminate
     * gaps between items that may occur because of removal.
     * 
     *
     * 
     * @param _pov  the PictureOverview that contains the item which is going
     *				to be deleted in current item.
     */
    public synchronized void updateRemove(final PictureOverview _pov) {

        Component [] comp = getPaintObjects().getJpnl_items()
                .getComponents();
        int removalY = -1;
        
        for (int i = 0; i < comp.length; i++) {
        	
        	//if the current component is instance of the correct class.
            if (comp[i] instanceof Item1Button) {
                

            	//fetch the button as instance of Item1Button for better 
            	//readability and then check whether there are additional
            	//information stored in it (in updateAdd (also called by 
            	//update add selected) the current 
            	//paintObject is stored there).
                Item1Button i1b = (Item1Button) comp[i];
                if (i1b.getAdditionalInformation() != null
                        && i1b.getAdditionalInformation() 
                        instanceof PaintObject) {
                    
                    //fetch the paintObject and check whether it is the
                	//one to be removed.
                    PaintObject po = 
                            (PaintObject) i1b.getAdditionalInformation();
                    if (po.equals(_pov.getCurrentPO())) {

                    	//do only remove not-activated fields (because there 
                    	//is a special method for removing activated fields)
                    	if (!i1b.isActivated()) {


                        	//save the removal y coordinate. if it equals -1 set
                        	//it to be zero (i don't think that may occur but 
                        	//who knows ;) )
                            removalY = i1b.getY();
                            if (removalY == -1) {
                            	removalY = 0;
                            }
                            
                            //decrease the rectangle y location for further
                            //adding
                            rec_old.y = rec_old.y - i1b.getHeight();

                            //remove and reset rectangle height.
                            getPaintObjects().getJpnl_items()
                            .remove(comp[i]);

                    	} 
                    	
                    	
                    } else if (removalY != -1) {
                    	
                    	//reset location and then update removalY.
                    	i1b.setLocation(i1b.getX(), removalY);
                    	removalY = i1b.getY() + i1b.getHeight();
                    }
                }
            }
        }
    }
    
    
    /**
     * 
     * @param _pov the PictureOverview serving (temporarily) as container
     * of information on the current instance of PaintObject
     */
    public synchronized void updateAddSelected(final PictureOverview _pov) {
        Item1Button i1b = updateAdd(_pov);
        i1b.setActivated(true);
        showPaintObjectInformation(_pov.getCurrentPO());
        
    }
    
    
    /**
     * @param _pov the PictureOverview serving (temporarily) as container
     * of information on the current instance of PaintObject
     */
    public void updateRemoveSelected(final PictureOverview _pov) {


        Component [] comp = getPaintObjects().getJpnl_items()
                .getComponents();
        int removalY = -1;
        
        for (int i = 0; i < comp.length; i++) {
        	
        	//if the current component is instance of the correct class.
            if (comp[i] instanceof Item1Button) {
                

            	//fetch the button as instance of Item1Button for better 
            	//readability and then check whether there are additional
            	//information stored in it (in updateAdd (also called by 
            	//update add selected) the current 
            	//paintObject is stored there).
                Item1Button i1b = (Item1Button) comp[i];
                if (i1b.getAdditionalInformation() != null
                        && i1b.getAdditionalInformation() 
                        instanceof PaintObject) {
                    
                    //fetch the paintObject and check whether it is the
                	//one to be removed.
                    PaintObject po = 
                            (PaintObject) i1b.getAdditionalInformation();
                    if (po.equals(_pov.getCurrentPO())) {
                    
                    	//for only deleting selected ones
                    	if (i1b.isActivated()) {


                        	//save the removal y coordinate. if it equals -1 set
                        	//it to be zero (i don't think that may occur but 
                        	//who knows ;) )
                            removalY = i1b.getY();
                            if (removalY == -1) {
                            	removalY = 0;
                            }
                            
                            //decrease the rectangle y location for further
                            //adding
                            rec_old.y = rec_old.y - i1b.getHeight();

                            //remove and reset rectangle height.
                            getPaintObjects().getJpnl_items()
                            .remove(comp[i]);

                    	} 
                    	
                    	
                    } else if (removalY != -1) {
                    	
                    	//reset location and then update removalY.
                    	i1b.setLocation(i1b.getX(), removalY);
                    	removalY = i1b.getY() + i1b.getHeight();
                    }
                }
            }
        }
    }
    
    
    /**
     * show information on PaintObject at graphical user interface.
     * @param _po_cu the current PaintObject
     */
    private void showPaintObjectInformation(final PaintObject _po_cu) {

        final Rectangle r = _po_cu.getSnapshotBounds();
        String text = "no information found.";
        
        //stuff for paintObjectWriting
        if (_po_cu instanceof PaintObjectWriting) {
            
            PaintObjectWriting pow = (PaintObjectWriting) _po_cu;
            Pen pe = pow.getPen();
            final List<DPoint> ls_point = pow.getPoints();
            text = "Stift  " + pe.getClass().getSimpleName()
                    + " \nArt   " + pe.getID()
                    + "\nStaerke    " + pe.getThickness()
                    + "\nFarbe  (" + pe.getClr_foreground().getRed()
                    + ", " + pe.getClr_foreground().getGreen()
                    + ", " + pe.getClr_foreground().getBlue()
                    + ")\nBounds    " + r.x + "." + r.y + ";" 
                    + r.width + "." + r.height + "\nimageSize  "
                    + Status.getImageSize().width + "." 
                    + Status.getImageSize().height 
                    + "\nPoints";
            ls_point.toFirst();
            int currentLine = 0;
            final int newLine = 10;
            while (!pow.getPoints().isBehind()) {
                
                 currentLine++;
                 
                 //each second line a line break;
                 if (currentLine % newLine == 1) {
                     text += "\n";
                 }
                 
                 text += ls_point.getItem().getX() 
                         + " "
                         + ls_point.getItem().getY() + " | ";
                ls_point.next();
             }
        }
        
        getPaintObjects().getJta_infoSelectedPanel()
        .setText(text);
        
        //create bufferedImage
        BufferedImage bi = new BufferedImage(getPaintObjects().getJlbl_detailedPosition()
                .getWidth(), getPaintObjects()
                .getJlbl_detailedPosition().getHeight(), 
                BufferedImage.TYPE_INT_ARGB);

        
        //fetch rectangle
        int x = r.x * bi.getWidth() 
                / Status.getImageSize().width;
        int y = r.y * bi.getHeight() 
                / Status.getImageSize().height;
        int width = r.width * bi.getWidth() 
                / Status.getImageSize().width;
        int height = r.height * bi.getHeight() 
                / Status.getImageSize().height;

        int border = 2;
        int highlightX = x - border;
        int highlightY = y - border;
        int highlightWidth = width + 2 * border;
        int highlightHeight = height + 2 * border;

        //paint rectangle and initialize with alpha
        for (int coorX = 0; coorX < bi.getWidth(); coorX++) {
            for (int coorY = 0; coorY < bi.getHeight(); coorY++) {
                
                if (coorX >= x && coorY >= y && x + width >= coorX
                        && y + height >= coorY) {
                    bi.setRGB(coorX, coorY, Color.black.getRGB());
                } else if (coorX >= highlightX 
                        && coorY >= highlightY 
                        && highlightX + highlightWidth >= coorX
                        && highlightY + highlightHeight >= coorY) {

                    bi.setRGB(coorX, coorY, Color.gray.getRGB());
                } else {

                    bi.setRGB(coorX, coorY, 
                            new Color(0, 0, 0, 0).getRGB());    
                }
            }
        }
        getPaintObjects().getJlbl_detailedPosition()
        .setIcon(new ImageIcon(bi));
    }
    

    public CPaintObjects getInstance() {
    	return null;
    }

    /**
     * @return the rec_old
     */
    public Rectangle getRec_old() {
        return rec_old;
    }


    /**
     * @param _rec_old the rec_old to set
     */
    public void setRec_old(final Rectangle _rec_old) {
        this.rec_old = _rec_old;
    }
}
