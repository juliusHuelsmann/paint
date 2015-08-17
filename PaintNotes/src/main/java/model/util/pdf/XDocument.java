package model.util.pdf;


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
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.objects.Project;
import model.objects.painting.po.PaintObjectPdf;
import model.settings.Constants;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.ResourceCache;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.tools.PDFToImage;
/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class XDocument {

	/**
	 * 
	 */
	private PDDocument document; 
	
	
	/**
	 * Array of the PDF pages.
	 */
	private PaintObjectPdf[] pdfPages;
	
	
	private Project project;
	

	/**
	 * Call super-class constructor and initialize extra-
	 * settings.
	 */
	public XDocument() {
		
		// call super constructor.
		super();
		this.document = new PDDocument();
	}

	/**
	 * Call super-class constructor and initialize extra-
	 * settings.
	 * @throws IOException 
	 */
	public XDocument(final String _pString, final Project _project) throws IOException {
		
		//
		// call super constructor and save values.
		//
		super();
		this.project = _project;

		//
		// load the document.
		//
		this.document = PDDocument.load(new File(_pString));
		
	}
	
	
	public Dimension initialize() {

		// initialize the paintObjectPdf array which is saved
		// for being able to modify them just after inserting
		// or removing page.
		this.pdfPages = new PaintObjectPdf[document.getNumberOfPages()];
		
		//
		BufferedImage bi_saved = null;
		
		//
		// initialize the pages
		//
		for (int i = 0; i < pdfPages.length; i++) {
			
			//TODO: hier muss das Bild als bfuferedimage exportiert werden.
			PDPage page = document.getPage(i);
//			pdfPages[i] = _project.getPicture(i).createPPF(); 

			if (document != null) {
				
				
				BufferedImage bi = PDFUtils.pdf2image(
						document, i);

				if (bi != null) {

					
					pdfPages[i] = project.getPicture(i).addPaintObjectPDF(bi);

					if (project.getCurrentPageNumber() == i) {
						bi_saved = bi;
					}
					
				}
			}
//			 BufferedImage buffImage = convertToImage(page, 8, 12);

//					new PaintObjectPdf(_elementId, _bi, _picture)
		}

		if (bi_saved == null) {
			return Constants.SIZE_A4;
		}
		return new Dimension(bi_saved.getWidth(), bi_saved.getHeight());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addPage(final PDPage _page) {
		document.addPage(_page);
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	public void addSignature(
			final PDSignature _sigObject,
			final SignatureInterface _signatureInterface) 
					throws IOException {
		document.addSignature(_sigObject, _signatureInterface);
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	public void addSignature(
			final PDSignature _sigObject,
			final SignatureInterface _signatureInterface,
			final SignatureOptions _options) 
					throws IOException {
		document.addSignature(_sigObject, 
				_signatureInterface, _options);
	}
	

	
	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	public void addSignatureField(
			final java.util.List<PDSignatureField> _sigFields,
			final SignatureInterface _signatureInterface,
			final SignatureOptions _options) throws IOException{
		
		document.addSignatureField(
				_sigFields, _signatureInterface, _options);
	}
	

	
	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	public void close() throws IOException {
		document.close();
	}
	
	

	
	/**
	 * {@inheritDoc}
	 * @return 
	 */
	public final AccessPermission getCurrentAccessPermission() {
		return document.getCurrentAccessPermission();
	}

	
	/**
	 * {@inheritDoc}
	 */
	public final COSDocument getDocument() {
		return document.getDocument();
	}

	
	/**
	 * {@inheritDoc}
	 */
	public PDDocumentCatalog getDocumentCatalog(){
		return document.getDocumentCatalog();
	}

	
	/**
	 * {@inheritDoc}
	 * @return 
	 */
	public Long getDocumentId(){
		return document.getDocumentId();
	}
	
	

	
	/**
	 * {@inheritDoc}
	 */
	public PDDocumentInformation getDocumentInformation(){
		return document.getDocumentInformation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public PDEncryption getEncryption(){
		return document.getEncryption();
		
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	public PDSignature getLastSignatureDictionary() throws IOException {
		return document.getLastSignatureDictionary();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getNumberOfPages() {
		return document.getNumberOfPages();
	}
	
	/**
	 * {@inheritDoc}
	 * @return 
	 */
	public PDPage getPage(final int _pageIndex) {
		return document.getPage(_pageIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public PDPageTree getPages() {
		return document.getPages();
	}
	

	/**
	 * {@inheritDoc}
	 */
	public ResourceCache getResourceCache() {
		return document.getResourceCache();
	}

	/**
	 * @return the pdfPages
	 */
	public PaintObjectPdf[] getPdfPages() {
		return pdfPages;
	}

	
}
