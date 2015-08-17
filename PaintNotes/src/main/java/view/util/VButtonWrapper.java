//package declaration
package view.util;


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


//import declaration

import view.util.mega.MButton;
import model.settings.Error;


/**
 * Wrapper class which delivers a certain object to ActionListener.
 * 
 * @author Julius Huelsmann
 * @version %U%, %I%
 */
@SuppressWarnings("serial")
public class VButtonWrapper extends MButton {

	/**
	 * The object that is to be delivered to the actionListener.
	 */
	private Object o_delivered;
	
	
	/**
	 * Constructor: save the object.
	 * @param _o_delivered the object to be delivered.
	 */
	public VButtonWrapper(final Object _o_delivered) {
	    
	    //if delivered item is null
	    if (_o_delivered == null) {

            Error.printError(getClass().getSimpleName(), 
                    "paintComponent(...)", "exception occured", 
                    new Exception(), Error.ERROR_MESSAGE_INTERRUPT); 
	    }
	    
	    //set delivered item
		this.o_delivered = _o_delivered;
	}
	
	
	
	
	
	/**
	 * simple getter method which returns the delivered object.
	 * @return the delivered object.
	 */
	public final Object wrapObject() {
		return o_delivered;
	}
}
