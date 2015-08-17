package model.debug;


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



/**
 * This Implementation of Action contains all the actions that are performed
 * directly by the user interacting with the graphical user interface.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class UserAction extends Action {

	
	/**
	 * The title of the userAction.
	 */
	private String title;
	
	/**
	 * Whether the userAction is started or terminated.
	 */
	private boolean start;
	
	
	
	/**
	 * Constructor: saves title and whether the action is started or terminated.
	 * @param _title	the title that is saved.
	 * @param _start	whether the action is started or terminated.
	 */
	public UserAction(
			final String _title, 
			final boolean _start, 
			final long _time) {
		super(_time);
		this.title = _title;
		this.start = _start;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final String getString() {
		if (start) {
			return getTime() + "\tUA\tStart:     \t" + title;
		} else {
			return getTime() + "\tUA\tFinished: \t" + title;
		}
	}
}
