//package declaration
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


//import declarations
import java.io.Serializable;

/**
 * Element class contains a certain content of type <Type>, a predecessor and a
 * successor element.
 *
 * Is one item of a list (List).
 *
 * Contains getter and setter methods for content, predecessor, successor and
 * implements Serializable.
 *
 * @author Julius Huelsmann
 * @version Milestone 2
 * @param <Type>
 */
public class Element<Type> implements Serializable {

	/*
	 * variable for saving list:
	 */
	
    /**
     * Default serial version UID for being able to identify the list's 
     * version if saved to the disk and check whether it is possible to 
     * load it or whether important features have been added so that the
     * saved file is out-dated.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Content of the ELement.
     */
    private Type content;

    /**
     * Element which comes next.
     */
    private Element<Type> elemSuccessor;

    /**
     * Predecessor element.
     */
    private Element<Type> elemPredecessor;
    
    /**
     * index for sort. Sort ASC.
     */
    private double sortedIndex = 0;

    /**
     * Constructor: Saves values such as content, successor, predecessor.
     *
     * @param _content the content we want to save.
     * @param _elemSuccessor the successor element.
     * @param _elemPredecessor the predecessor element.
     */
    public Element(final Type _content,
            final Element<Type> _elemSuccessor,
            final Element<Type> _elemPredecessor) {
        // save values
        this.content = _content;
        this.elemSuccessor = _elemSuccessor;
        this.elemPredecessor = _elemPredecessor;
    }
    /**
     * Constructor: Saves values such as content, successor, predecessor.
     *
     * @param _content the content we want to save.
     * @param _elemSuccessor the successor element.
     * @param _elemPredecessor the predecessor element.
     * @param _sortIndex the index for inserting in a sorted way
     */
    public Element(final Type _content,
            final Element<Type> _elemSuccessor,
            final Element<Type> _elemPredecessor,
            final int _sortIndex) {
        // save values
        this.content = _content;
        this.elemSuccessor = _elemSuccessor;
        this.elemPredecessor = _elemPredecessor;
        this.sortedIndex = _sortIndex;
    }
    /**
     * Sets the content.
     *
     * @param _content content for current element.
     */
    public final void setContent(final Type _content) {
        this.content = _content;
    }

    /**
     * Returns the content.
     *
     * @return content
     */
    public final Type getContent() {
        return content;
    }

    /**
     * Sets the successor - element.
     *
     * @param _elemSuccessor the successing element.
     */
    public final void setElemSuccessor(final Element<Type> _elemSuccessor) {
        elemSuccessor = _elemSuccessor;
    }

    /**
     * Returns the successor - element.
     *
     * @return elemSuccessor.
     */
    public final Element<Type> getElemSuccessor() {
        return elemSuccessor;
    }

    /**
     * Returns the predecessor - element.
     *
     * @return elemPredecessor.
     */
    public final Element<Type> getElemPredecessor() {
        return elemPredecessor;
    }

    /**
     * Sets the predecessor - element.
     *
     * @param _elemPredecessor .
     */
    public final void setElemPredecessor(
            final Element<Type> _elemPredecessor) {
        this.elemPredecessor = _elemPredecessor;
    }
    /**
     * @return the sortedIndex
     */
    public final double getSortedIndex() {
        return sortedIndex;
    }
    /**
     * @param _sortedIndex the sortedIndex to set
     */
    public final void setSortedIndex(final double _sortedIndex) {
        this.sortedIndex = _sortedIndex;
    }
}
