package model.util.paint;



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


import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectImage;
import model.objects.painting.po.PaintObjectWriting;
import model.settings.State;
import model.util.DPoint;
import model.util.adt.list.List;
import model.util.adt.list.SecureList;


/**
 * Class for clipboard transfer.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class MyClipboard implements ClipboardOwner {
    
    
    /**
     * Just for testing purpose.
     * @param _args the args.
     */
    public static void main(final String[]_args) {
        BufferedImage bi = (BufferedImage) getInstance().paste();

        if (!State.isDebug()) {

            final int startSize = 30;
            JFrame jf = new JFrame();
            jf.setSize(startSize + bi.getWidth(), startSize + bi.getHeight());
            jf.setLayout(null);
            jf.setLocationRelativeTo(null);
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
            
            JLabel jlbl = new JLabel();
            jlbl.setSize(bi.getWidth(), bi.getHeight());
            jlbl.setIcon(new ImageIcon(bi));
            jf.add(jlbl);
           
            jf.setVisible(true);
            jf.setResizable(false);
        }
    }
    
    /**
     * The only instance of this class.
     */
    private static MyClipboard instance;
    
    
    /**
     * contains whether the clipboard is owned or not.
     */
    private boolean own_clipboard;
    
    
    /**
     * the paintObjectWriting which can be saved / loaded.
     */
    private List<PaintObject> ls_po_selected;
    
    /**
     * Empty utility class Constructor.
     */
    public MyClipboard() { }
    
    
    
    /**
     * CopyImage.
     * @param _i the image.
     */
    private void copyImage(final Image _i) {

        //copy to clipboard
        TransferableImage trans = new TransferableImage(_i);
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(trans, this);
        

        //reset the last PaintObjectWriting if just an image is copied
        //in case of Writing copied, the pw is set after exporting Image;
        //thus, this command does not hinder that process.
        this.ls_po_selected = null;
    }

    
    /**
     * copy po writing.
     * @param _picture the picture.
     * @param _lsPoSelected the poWriting
     * @param _i its image.
     */
    public void copyPaintObjects(
    		final Picture _picture,
    		final SecureList<PaintObject> _lsPoSelected, 
            final Image _i) {

        //copy image
        copyImage(_i);

        //set that i own the clipboard
        this.own_clipboard = true;

        //save the paintObjectWriting
        List<PaintObject> ls_new = new List<PaintObject>();

        //start transaction
        final int transaction = _lsPoSelected.startTransaction(
        		"copyPaintObjects", 
        		SecureList.ID_NO_PREDECESSOR);
        _lsPoSelected.toFirst(transaction, SecureList.ID_NO_PREDECESSOR);
        while (!_lsPoSelected.isBehind() && !_lsPoSelected.isEmpty()) {
            
            PaintObject po = _lsPoSelected.getItem();
            if (po instanceof PaintObjectImage) {
                PaintObjectImage poi = (PaintObjectImage) po;
                PaintObjectImage poi_new = _picture.createPOI(
                		poi.getSnapshot());

                ls_new.insertBehind(poi_new);
                
            } else if (po instanceof PaintObjectWriting) {
                
                PaintObjectWriting pow = (PaintObjectWriting) po;
                PaintObjectWriting pow_new = _picture.createPOW(
                        pow.getPen());
                
                pow.getPoints().toFirst();
                while (!pow.getPoints().isEmpty() 
                        && !pow.getPoints().isBehind()) {
                    pow_new.addPoint(new DPoint(pow.getPoints().getItem()));
                    pow.getPoints().next();
                }
                ls_new.insertBehind(pow_new);
            
            } else {
                State.getLogger().warning("unknown kind of "
                        + "PaintObject; element = " + po);
            }
            
            _lsPoSelected.next(transaction, SecureList.ID_NO_PREDECESSOR);
        }
        
        this.ls_po_selected = ls_new;
        
    }  
    
    
    /**
     * Paste.
     * @return the pasted image (if existing).
     */
	public Object paste() {

        //save the clipboard content.
        Object o = null;
        try {
            o = Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.imageFlavor);
            
        } catch (HeadlessException e) {
            State.getLogger().info("clipboard headless exception thrown");
        } catch (UnsupportedFlavorException e) {
            State.getLogger().info("UnsupportedFlavorException thrown");
        } catch (IOException e) {
            State.getLogger().info("IOException thrown");
        }
        
        if (own_clipboard && ls_po_selected != null) {

            State.getLogger().warning("list owned: " + own_clipboard 
                    + ls_po_selected);
            return ls_po_selected;
        }
        if (o instanceof BufferedImage) {
            State.getLogger().warning("bi! owned: " + own_clipboard 
                    + ls_po_selected);
            
            
        } else if (o instanceof String) {
            State.getLogger().warning("string! owned:" + own_clipboard);
        } else {
            State.getLogger().warning("nothing on clipboard! owned: "
                    + own_clipboard + "obj=" + o);
            
            if (o == null) {

            	//TODO: the copy and pasting of java objects does not work.
//                Clipboard c = Toolkit.getDefaultToolkit()
//            			.getSystemClipboard();
//                o =  c.getContents(this);
//                if (o instanceof sun.awt.datatransfer.ClipboardTransferable) {
//                	@SuppressWarnings("restriction")
//					sun.awt.datatransfer.ClipboardTransferable d 
//            		= ((sun.awt.datatransfer.ClipboardTransferable) o);
//                	System.out.println(d);
//                	try {
//						System.out.println(d.getTransferData(null));
//					} catch (UnsupportedFlavorException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                }
//                
//                System.out.println(o);
            }
        }

        return o;
    }


    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static MyClipboard getInstance() {
        
        //if class is not instanced yet instantiate
        if (instance == null) {
            instance = new MyClipboard();
        }
        
        //return the only instance of this class.
        return instance;
    }
    
    /**
     *  {@inheritDoc}
     */
    public void lostOwnership(final Clipboard _clip, 
            final Transferable _trans) {
        
        //i lost ownership of the clipboard; that means that another 
        //application has copied something to clipboard.
        this.own_clipboard = false;
    }

    
    /**
     * The item that is copied to clipboard.
     * @author Julius Huelsmann
     * @version %I%, %U%
     *
     */
    @SuppressWarnings("serial")
	private class TransferableImage implements Transferable, Serializable {

        
        /**
         * The image that is to be transmitted.
         */
        private Image i;

        
        /**
         * Constructor: saves image.
         * @param _i the iamge.
         */
        public TransferableImage(final Image _i) {
            this.i = _i;
        }


        /**
         * 
         * get tranfer data.
         * @param _flavor the DataFlavor.
         * @return the transfer data.
         * 
         * @throws UnsupportedFlavorException uex
         * @throws IOException ioex
         * 
         */
        public Object getTransferData(final DataFlavor _flavor)
        throws UnsupportedFlavorException, IOException {
        	final boolean tryVal = false;
        	if (!tryVal) {
        		return i;
        	}
            if (_flavor.equals(DataFlavor.imageFlavor) && i != null) {
                return i;
            } else {
                throw new UnsupportedFlavorException(_flavor);
            }
        }

        /**
         * get tranfer data.
         * @return the DataFlavor
         */
        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        
        /**
         * is data supported.
         * @param _flavor the flavor
         * @return whether data flav supported.
         */
        public boolean isDataFlavorSupported(final DataFlavor _flavor) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for (int j = 0; j < flavors.length; j++) {
                if (_flavor.equals(flavors[ j ])) {
                    return true;
                }
            }

            return false;
        }
    }
}
