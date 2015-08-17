package view.util.mega;


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


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.BoundedRangeModel;
import javax.swing.JSlider;
import model.settings.State;

/**
 * View class for which add the possibility to rotate the components 
 * by overwriting paintComponents(Graphics g) and by overwriting the method 
 * paintComponent(Graphics g) for rotating the images and the icons.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class MSlider extends JSlider implements Mega {

    /**
     * Constructor. Call super - constructor.
     */
    public MSlider() {
        super();
        super.setFocusable(false);
    }
    

    /**
     * Constructor. Call super - constructor.
    /**
     * Creates a horizontal slider using the specified min, max and value.
     * <p>
     * The <code>BoundedRangeModel</code> that holds the slider's data
     * handles any issues that may arise from improperly setting the
     * minimum, initial, and maximum values on the slider.  See the
     * {@code BoundedRangeModel} documentation for details.
     *
     * @param min  the minimum value of the slider
     * @param max  the maximum value of the slider
     * @param value  the initial value of the slider
     *
     * @see BoundedRangeModel
     * @see #setMinimum
     * @see #setMaximum
     * @see #setValue
     */
    public MSlider(final int _min, final int _max, final int _value) {
        super(_min, _max, _value);
        super.setFocusable(false);
    }
    

    /**
     * Turn the owned components.
     */
    public final void turn() {

        //go through the list of contained components and change location
        //and e.g. call turn method of components.
        for (Component c : getComponents()) {
            c.setLocation(getWidth() - c.getX() - c.getWidth(),
                    getHeight() - c.getY() - c.getHeight());
            if (c instanceof Mega) {
                ((Mega) c).turn();
            }
        }
    }
    

    /**
     * paintComponent which paints the component. Flips if Status is 
     * normalRotation.
     * 
     * @param _graphics the graphics which are painted.
     */
    @Override public final void paintComponent(final Graphics _graphics) {
                
        //initialize values
        Graphics2D g2d = (Graphics2D) _graphics;
        AffineTransform origXform = g2d.getTransform();
        AffineTransform newXform = (AffineTransform) origXform.clone();
        
        //center of rotation is center of the panel
        int xRot = this.getWidth() / 2;
        int yRot = this.getHeight() / 2;
        
        //fetch rotation from Status
        double rotation = 0;
        
        //if not normal rotation
        if (!State.isNormalRotation()) {
            final int filpRotation = 180;
            rotation = filpRotation;
        }
        
        //rotate the image and draw the image to panel
        newXform.rotate(Math.toRadians(rotation), xRot, yRot);
        g2d.setTransform(newXform);
        super.paintComponent(g2d);
        g2d.setTransform(origXform);
    }
}
