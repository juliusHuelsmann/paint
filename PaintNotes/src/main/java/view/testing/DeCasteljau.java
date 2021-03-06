package view.testing;


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


import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import model.objects.painting.po.PaintObjectWriting;
import model.util.adt.list.SecureList;
import model.util.adt.list.SecureListSort;
import view.util.mega.MLabel;



/**
 * View class which illustrates the DeCastlejau algorithm.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class DeCasteljau extends JFrame {

    
    /**
     * The MLabel for the painting.
     */
    private MLabel jlbl_painting;
    
	public DeCasteljau() {
        super();
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final int size = 100;
        super.setSize(size, size);
        super.setLayout(null);
        jlbl_painting = new MLabel();
        jlbl_painting.setSize(getSize());
    	final SecureList<Point> pnt = new SecureList<Point>();

        jlbl_painting.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				System.out.println(e.getButton());
				
				if (e.getButton() == 3) {
					pnt.toFirst(SecureListSort.ID_NO_PREDECESSOR, SecureListSort.ID_NO_PREDECESSOR);
					while(!pnt.isEmpty()) {

						pnt.remove(SecureListSort.ID_NO_PREDECESSOR);
					}
				} else {
					
				pnt.insertAtTheEnd(e.getPoint(), SecureListSort.ID_NO_PREDECESSOR);
				show(PaintObjectWriting.deCasteljau(pnt.toPntArray(), false));
			}
			}
		});
        jlbl_painting.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseDragged(MouseEvent e) {
				pnt.insertAtTheEnd(e.getPoint(), SecureListSort.ID_NO_PREDECESSOR);
//				pnt.insertAtTheEnd(e.getPoint(), SecureListSort.ID_NO_PREDECESSOR);
//				pnt.insertAtTheEnd(e.getPoint(), SecureListSort.ID_NO_PREDECESSOR);
				show(PaintObjectWriting.deCasteljau(pnt.toPntArray(), false));
			}
		});
        super.add(jlbl_painting);
        
        
        super.setVisible(true);
    }
	
	

    /**
     * Show new bufferedImage.
     * @param _bi the bufferedImage.
     */
    public synchronized void show(final BufferedImage _bi) {
    	
    	final boolean showEnabled = !false;
    	if (showEnabled) {
        	

            	if (_bi.getWidth() > 0
            			&& _bi.getHeight() > 0) {

            		BufferedImage bi = _bi;
                    setSize(bi.getWidth(), bi.getHeight());
                    jlbl_painting.setSize(bi.getWidth(),
                    		bi.getHeight());
                    jlbl_painting.setIcon(new ImageIcon(bi));
    	}
    }
    }
    public static void main(String[] args){
    	new DeCasteljau();
    }
}
