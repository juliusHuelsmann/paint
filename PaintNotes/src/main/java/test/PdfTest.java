package test;



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




import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


/**
 * Example from source library of apache pdf box, authored by Ben Litchfield. 
 * Will be removed. For testing purpose.
 *
 * @author Ben Litchfield
 * @author Julius Huelsmann
 */
public class PdfTest {

	
	    /**
	     * Test: Create pdf from image and pdf file.
	     *
	     * @param _inputFile 	pdf input
	     * @param _imagePath 	image input
	     * @param _outputFile 	pdf output
	     *
	     * @throws IOException occurs if reading the data fails.
	     */
	    public void createPDFFromImage(
	    		String _inputFile, String _imagePath, String _outputFile )
	            throws IOException
	    {

	    	PDDocument doc = null;
	        try
	        {
	        	
	        	doc = new PDDocument();
	            //doc = PDDocument.load( new File(_inputFile) );

	            //we will add the image to the first page.
	            PDPage page = doc.getPage(0);

	            // createFromFile is the easiest way with an image file
	            // if you already have the image in a BufferedImage, 
	            // call LosslessFactory.createFromImage() instead
	            PDImageXObject pdImage = PDImageXObject.createFromFile(_imagePath, doc);
	            PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true);

	            // contentStream.drawImage(ximage, 20, 20 );
	            // better method inspired by http://stackoverflow.com/a/22318681/535646
	            // reduce this value if the image is too large
	            float scale = 1f;
	            contentStream.drawImage(pdImage, 20, 20, pdImage.getWidth()*scale, pdImage.getHeight()*scale);

	            contentStream.close();
	            doc.save( _outputFile );
	        }
	        finally
	        {
	            if( doc != null )
	            {
	                doc.close();
	            }
	        }
	    }

	    /**
	     * This will load a PDF document and add a single image on it.
	     * <br />
	     * see usage() for commandline
	     *
	     * @param args Command line arguments.
	     */
	    public static void main(String[] args) throws IOException
	    {
	    	
	    	
	        PdfTest app = new PdfTest();
	        app.createPDFFromImage("input.pdf", "cursor.png", "output.pdf");
	        
	    }


}
