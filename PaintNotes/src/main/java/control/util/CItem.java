//package declaration
package control.util;


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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import view.util.Item1Menu;
import view.util.VButtonWrapper;


/**
 * Controller class for Item.
 * 
 * @author Julius Huelsmann
 * @version %I, %U%
 */
public final class CItem implements MouseListener {

    /**
     * the only instance of this class.
     */
	private static CItem instance;
	
	/**
	 * the last enabled item.
	 */
	private Item1Menu i_lastActionItem;

    /**
     * private constructor which can only be called out of
     * this class. Realized by getInstance.
     */
	private CItem() { }

    /**
     * {@inheritDoc}
     */
	public void mouseClicked(final MouseEvent _event) {

        //fetch current source of action
        final Item1Menu i_currentActionItem = (Item1Menu) 
                ((VButtonWrapper) _event.getSource()).wrapObject();
        
        
	    //if this is NOT the first time an ActionEvent is thrown.
	    //disable the last item
		if (i_lastActionItem != null) {

		    //disable and close Item
		    i_lastActionItem.setOpen(false);
		}
		
		

		//if the current item is a new item open the new item. Set last item
		//if action performed twice with the same source, do not reopen it
		//but reset the last Action element
		if (i_currentActionItem != i_lastActionItem) {

		    
		    new Thread() {
		        public void run() {
		            
		            //ugly solution: if opened without closing another menu
		            if (i_currentActionItem != i_lastActionItem) {

		                final int sleepTime = 150;
		                
	                    try {
	                        Thread.sleep(sleepTime);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
		            }

		            //set open flag
		            i_currentActionItem.setOpen(!i_currentActionItem.isOpen());
		        }
		    } .start();
			
			//set last element
			i_lastActionItem = i_currentActionItem;
			
		} else { 
		    
		    //set last element
			i_lastActionItem = null;
		}

	}
	
	
	/**
	 * Reset the last open item.
	 * Used if each ItemMenu is closed.
	 */
	public void reset() {
	    i_lastActionItem = null;
	}
	
	
	/**
	 * simple getter method.
	 * @return the instance
	 */
	public static CItem getInstance() {

		//if not yet instanced call the constructor of FetchColor.
		if (instance == null) {
			instance = new CItem();
		}
		return instance;
	}

    /**
     * {@inheritDoc}
     */
     public void mouseEntered(final MouseEvent _event) { }

     /**
      * {@inheritDoc}
      */
    public void mouseExited(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public void mousePressed(final MouseEvent _event) { }

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(final MouseEvent _event) { }
}
