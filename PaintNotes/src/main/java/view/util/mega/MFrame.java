//package declaration
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


//import declarations
import java.awt.Component;
import javax.swing.JFrame;

/**
 * View class which add the possibility to rotate the components 
 * (by overwriting paintComponents(Graphics g) in container class).
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class MFrame extends JFrame implements Mega {

	public MFrame() {
		super.setFocusable(false);
	}
    /**
     * Turn the owned components.
     */
    public final void turn() {

        //go through the list of contained components and change location
        //and e.g. call turn method of components.
        for (Component c : getContentPane().getComponents()) {

            c.setLocation(getWidth() - c.getX() - c.getWidth(),
                    getHeight() - c.getY() - c.getHeight());
            if (c instanceof Mega) {
                ((Mega) c).turn();
            }
        }
    }
}
