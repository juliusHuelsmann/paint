package model.util.adt.list;


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
 * Secure element which contains a string identifying the transaction and
 * its unique id. It is used inside the list of Transactions in SecureList.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <ClosedActionType>
 */
public class Transaction<ClosedActionType> implements Serializable {


    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = 1L;

	/**
     * The unique id of the current closed action.
     */
    private int id_secureList = 0;

    
    /**
     * The unique id of the current closed action.
     */
    private static int maxID = 0;

    
	/**
	 * The name identifying the transaction for debugging purpose and
	 * for being able to print the current list action.
	 */
	private String name;

	
	/**
	 * Constructor: (initializes) the content with start values.
	 */
	public Transaction() {

        //set title of transaction
        this.name = "";

        //set unique id and change the max id.
        id_secureList = maxID++;
	}
	
	
	/**
	 * Constructor: (initializes) the content with start values passed by 
	 * calling methods.
	 * @param _name 
	 * 					the name of the transaction
	 */
	public Transaction(final String _name) {

        //set title of transaction
        this.name = _name;
	}
	
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	
	
	/**
	 * @param _name the name to set
	 */
	public final void setName(final String _name) {
		this.name = _name;
	}


	/**
	 * @return the id_secureList
	 */
	public final int getId_secureList() {
		return id_secureList;
	}


}
