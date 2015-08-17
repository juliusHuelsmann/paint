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


import java.util.Date;

import model.util.adt.stack.Stack;



/**
 * The status class contains the method for saving the performed user actions 
 * and program answers.
 * That is done because if an error occurs, it can be reconstructed.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class ActionManager {

	
	/**
	 * Stack of actions.
	 */
	private static Stack<Action> lsAction;
	
	/**
	 * Empty utility class constructor.
	 */
	private ActionManager() { }
	
	
	
	/**
	 * Register, that the user performed an action for debugging purpose.
	 * If an error occurs, the user actions and the auto-generated program
	 * answers are saved and the error can be reconstructed.
	 * 
	 * 
	 * @param _actionTitle		the title of the action
	 * @param _start			whether the action is started or ended.
	 */
	public static void addUserAction(
			final String _actionTitle,
			final boolean _start) {
		getLs_action().insert(new UserAction(_actionTitle, _start, getCurrentTime()));
	}
	
	
	
	private static long getCurrentTime() {
		return new Date().getTime();
	}
	
	
	/**
	 * Register a program answer to a user action. This is done for debugging.
	 * 
	 * If an error occurs, the user actions and the auto-generated program
	 * answers are saved and the error can be reconstructed.
	 * 
	 * @param _title			the answer of the program which contains
	 * 							information on how the action has been 
	 * 							computed.
	 */
	public static void addAnswer(
			final String _title) {
		getLs_action().insert(new ProgramAction(_title, getCurrentTime()));
	}
	
	
	
	/**
	 * This function generates a string which contains information on all the 
	 * actions that have been performed.
	 * Therefore, the saved actionStack is emptied.
	 * 
	 * @return 		a string which contains information on all the actions 
	 * 				that have been performed.
	 */
	public static String externalizeAction() {

		//for not throwing away the log after it has been externalized,
		// the log information are written into the following new-
		// created stack which replaces the old one.
		Stack<Action> ls_action2 = new Stack<Action>();
		
		//the string that is returned at the end and into which the information
		//on the actions are inserted.
		String content = "";
		while (!getLs_action().isEmpty()) {
			content = getLs_action().getElem_last().getContent().getString()
					+ "\n" + content;
			ls_action2.insert(getLs_action().getElem_last().getContent());
			getLs_action().remove();
		}
		
		//invert.
		while (!ls_action2.isEmpty()) {
			getLs_action().insert(ls_action2.getElem_last().getContent());
			ls_action2.remove();
		}

		//remove the temporary stack
		ls_action2 = null;
		
		//return the content.
		return content;
	}



	/**
	 * @return the ls_action
	 */
	public static Stack<Action> getLs_action() {
		
		// if the stack has not been not instantiated yet create it:
		if (lsAction == null) {
			lsAction = new Stack<Action>();
		}
		
		// return the stack.
		return lsAction;
	}
}
