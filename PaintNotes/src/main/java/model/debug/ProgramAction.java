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
 * by model classes and not directly by the user.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class ProgramAction extends Action {

	
	/**
	 * The title of the userAction.
	 */
	private String title;
	
	
	/**
	 * Constructor: saves title of the action.
	 * @param _title	the title that is saved.
	 */
	public ProgramAction(final String _title,
			final long _time) {
		super(_time);
		this.title = _title;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final String getString() {
		return getTime() + "\tPA\t" + title;
	}
}
