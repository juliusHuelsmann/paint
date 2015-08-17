//package declaration
package model.objects.history;


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

import model.objects.painting.Picture;
import model.settings.State;
import model.util.adt.list.List;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class History extends Observable {

    /**
     * the list of history items.
     */
    private static List<String> lsHistory;
    
    /**
     * The Picture.
     */
    private Picture pic;
    
    /**
     * Utility class constructor.
     * @param _pic the picture.
     */
    public History(final Picture _pic) {
    	this.pic = _pic;
    }

    /**
     * add new history.
     * @param _hist the history to add
     */
    public void addHistory(final String _hist) {
        lsHistory.insertAtTheEnd(_hist);
        
        setChanged();
        notifyObservers("add " + _hist);
    }    
    
    
    /**
     * remove history.
     * @param _hist the history to be removed
     */
    public void removeHistory(final String _hist) {
        if (lsHistory.find(_hist)) {
            lsHistory.remove();
        } else {
            State.getLogger().warning("History" + "error");
        }
        setChanged();
        notifyObservers("remove " + _hist);
    }

	/**
	 * @return the pic
	 */
	public Picture getPic() {
		return pic;
	}

    
    
}
