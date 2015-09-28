package model.objects.painting.po;


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


import model.objects.Project;
import model.objects.painting.Picture;
import model.util.pdf.PDFUtils;


/**
 * Paint object PDF for the .
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PaintObjectPdf extends PaintObjectImage {
	
	
	
	
	/**
	 * The page number inside the XDocument.
	 */
	final int pageNumber;
	
	/**
	 * Link to the Project for being able to fetch the XDocument. 
	 * The XDocument is not saved directly because it is not serializable.
	 * 
	 * For saving, the XDocument is removed out of the project class and
	 * restored afterwards.
	 */
	final Project pro;
	
	
	/**
	 * Constructor: calls super-constructor and saves instances of important 
	 * classes.
	 * 
	 * @param _elementId
	 * @param _project
	 * @param _pageNr
	 * @param _picture
	 */
	public PaintObjectPdf(
			final int _elementId, final Project _project, final int _pageNr, 
			final Picture _picture) {
		
		// call super constructor and save variables.
		super(_elementId, null, _picture);
		this.pro = _project;
		this.pageNumber = _pageNr;
		
	}

	public void remember() {

		setImage(PDFUtils.pdf2image(
				pro.getDocument().getPDDocument(), pageNumber));
	}

	public void forget() {

		setImage(null);
	}
	
}
