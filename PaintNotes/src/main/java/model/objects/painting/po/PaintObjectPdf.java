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


import java.awt.image.BufferedImage;

import model.objects.painting.Picture;
import model.util.pdf.PDFUtils;
import model.util.pdf.XDocument;


/**
 * Paint object PDF for the .
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PaintObjectPdf extends PaintObjectImage {
final int pageNumber;
	final XDocument xD;
	public PaintObjectPdf(
			final int _elementId, final XDocument _xd, final int _pageNr, 
			final Picture _picture) {
		super(_elementId, null, _picture);
		this.xD = _xd;
		this.pageNumber = _pageNr;
		
	}

	public void remember() {

		setImage(PDFUtils.pdf2image(
				xD.getPDDocument(), pageNumber));
	}

	public void forget() {

		setImage(null);
	}
	
}
