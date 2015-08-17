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


/**
 * The menuListener provides an interface for listening for menu - closing
 * and menu - opening events.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public interface MenuListener {

	
	/**
	 * Event which is fired before the MenuItem is opened.
	 */
	void beforeOpen();
	

	/**
	 * Event which is fired before the MenuItem is closed.
	 */
	void beforeClose();
	

	/**
	 * Event which is fired after the MenuItem is opened.
	 */
	void afterOpen();


	/**
	 * Event which is fired after the MenuItem is closed.
	 */
	void afterClose();
}
