package model.settings;


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
 * Version class contains the information on the current program version and
 * a function which generates a human-readable string for informing about the
 * current program version.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Version {
	
	
	/**
	 * This final string contains the name of the program.
	 */
	public static final String PROGRAM_NAME = "Paint";
	
	
	/**
	 * This final static string contains the release-date of the current 
	 * version.
	 */
	public static final String RELEASE_DATE = "2015 04 25";
	
	/**
	 * This final, static string contains information on the state of the 
	 * current program version. <br>
	 * It may contain: <br><br>
	 * 
	 * 		(1)		PRE RELEASE <br>
	 * 				If the current version is not a released version but 
	 * 				does still contain some bugs.
	 * 				<br><br>
	 * 		(2)		RELEASE <br>
	 *				If the current version is a release without big known bugs.
	 *				<br><br> 
	 */
	public static final String STATUS = "PRE RELEASE";

	
	/**
	 * This final, static string contains the current program version.
	 */
	public static final String VERSION = "v00.1";
	
	
	
	/**
	 * Function which generates the version information out of the hard-coded
	 * information declared in version class.
	 * 
	 * @return		human readable information on the program name, the 
	 * 				current version, status and the release date of the
	 * 				currently used version.
	 * 				
	 * 				For more detailed information on the contents of the 
	 * 				generate string see the java documentation of the variables 
	 * 				contained inside the class called Version.
	 */ 
	public static String generateVersionInformation() {
		return PROGRAM_NAME + " " + VERSION + " " + STATUS + " " + RELEASE_DATE;
	}
	
	
	
	/**
	 * Empty utility class constructor.
	 */
	private Version() {
		
	}
}
