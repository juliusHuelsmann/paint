package control.interfaces;
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

import java.awt.event.MouseEvent;


/**
 * Activity Listener interface is a general Listener-interface for a bunch
 * of activities that may occur.
 * 
 * The only constraint for the applicability is, that the event that is passed
 * to the activityListener is a MouseEvent.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public interface ActivityListener {

	/**
	 * Is called each time a certain activity occurs.
	 * @param _event 	the passed MouseEvent.
	 */
	void activityOccurred(final MouseEvent _event);
}
