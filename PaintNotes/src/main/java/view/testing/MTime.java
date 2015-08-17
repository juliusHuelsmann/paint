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


import java.io.Serializable;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class MTime implements Serializable {

	
	/**
	 * The default serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Title of the time item.
	 */
	private String strg_title;
	
	/**
	 * (unique) identifier of the time item.
	 */
	private final int identifier;
	
	
	/**
	 * The spent time.
	 */
	private int time;
	
	/**
	 * static identifier for guaranteeing that the identifier is unique.
	 */
	private static int currentIdentifier = 0;
	
	/**
	 * Working Time.
	 */
	private int workingTime = 0;
	
	/**
	 * Constructor: saves parameters and updates the currentIdentifier.
	 * 	
	 * @param _strg_title 	the title of the Time item.
	 */
	public MTime(final String _strg_title) {
		
		//save parameter
		this.strg_title = _strg_title;
		this.identifier = increaseCurrentIdentifier();
	}
	
	
	
	/**
	 * 
	 */
	public final synchronized void applyWorkingTime() {
		time += workingTime;
		workingTime = 0;
	}
	
	
	/**
	 * Increase method for current identifier which returns the identifier
	 * which is to be used for the new Time item.
	 * 
	 * @return the identifier which is to be used for the new Time item.
	 */
	private synchronized int increaseCurrentIdentifier() {
		return currentIdentifier++;
	}


	/**
	 * @return the strg_title
	 */
	public final String getStrg_title() {
		return strg_title;
	}


	/**
	 * @param _strg_title the strg_title to set
	 */
	public final void setStrg_title(final String _strg_title) {
		this.strg_title = _strg_title;
	}


	/**
	 * @return the time
	 */
	public final int getTime() {
		return time;
	}


	/**
	 * @param _time the time to set
	 */
	public final void setTime(final int _time) {
		this.time = _time;
	}


	/**
	 * @return the identifier
	 */
	public final int getIdentifier() {
		return identifier;
	}


	/**
	 * @return the workingTime
	 */
	public final int getWorkingTime() {
		return workingTime;
	}


	/**
	 * @param _workingTime the workingTime to set
	 */
	public final void setWorkingTime(final int _workingTime) {
		this.workingTime = _workingTime;
	}
}
