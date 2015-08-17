package model.objects;


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


import java.util.Observable;

import model.objects.painting.po.PaintObject;
import view.tabs.Debug;

/**
 * The class corresponding to the view PaintObjects class. 
 * Lists amount of PaintObjects.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class PictureOverview extends Observable {


	/**
	 * the current index.
	 */
	private static int number;
	
	/**
	 * the current PaintObject.
	 */
	private PaintObject currentPO;

    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void add(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number++;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(Debug.ID_ADD_ITEM);
    }
    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void remove(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number--;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(Debug.ID_REMOVE_ITEM);
    }
    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void addSelected(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number++;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(Debug.ID_ADD_ITEM_SELECTED);
    }
    
    /**
     * add paintObject.
     * @param _po the paintObject to be added.
     */
    public final synchronized void removeSelected(final PaintObject _po) { 
        
        //increase number and save current PaintObject
        number--;
        this.currentPO = _po;
        
        //notify observer
        setChanged();
        notifyObservers(Debug.ID_REMOVE_ITEM_SELECTED);
    }

	
	/**
	 * Constructor: adds observer.
	 */
	public PictureOverview(final Debug _po) {
		super.addObserver(_po);
	}


    /**
     * @return the number
     */
    public final int getNumber() {
        return number;
    }


    /**
     * @param _number the number to set
     */
    public final void setNumber(final int _number) {
        PictureOverview.number = _number;
    }


    /**
     * @return the currentPO
     */
    public final PaintObject getCurrentPO() {
        return currentPO;
    }


    /**
     * @param _currentPO the currentPO to set
     */
    public final void setCurrentPO(final PaintObject _currentPO) {
        this.currentPO = _currentPO;
    }
}
