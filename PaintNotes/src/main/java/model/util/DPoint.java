package model.util;


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
import java.io.Serializable;

/**
 * Double Point class.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class DPoint implements Serializable {

    /**
     * Generated Serial version.
     */
    private static final long serialVersionUID = -6695180495580749849L;
    
    /**
     * The two double points.
     */
    private double x, y;

    
    /**
     * Constructor.
     * @param _x coordiante
     * @param _y coordiante
     */
    public DPoint(final double _x, final double _y) {
        x = _x;
        y = _y;
    }
    
    /**
     * 
     * Constructor.
     * @param _x coordiante
     * @param _y coordiante
     */
    public DPoint(final int _x, final int _y) {
        x = _x;
        y = _y;
    }
    
    /**
     * Constructor.
     * @param _dp coordinates
     */
    public DPoint(final DPoint _dp) {
    	
    	if (_dp != null) {

            x = _dp.x;
            y = _dp.y;
    	}
    }

    
    /**
     * Constructor.
     * @param _dp coordinates
     */
    public DPoint(final Point _dp) {
        x = _dp.x;
        y = _dp.y;
    }
    

    
    /**
     * Constructor.
     */
    public DPoint() {
        x = 0;
        y = 0;
    }

    /**
     * @return the x
     */
    public final double getX() {
        return x;
    }

    /**
     * @param _x the x to set
     */
    public final void setX(final double _x) {
        this.x = _x;
    }

    /**
     * @return the y
     */
    public final double getY() {
        return y;
    }

    /**
     * @param _y the y to set
     */
    public final void setY(final double _y) {
        this.y = _y;
    }
}
