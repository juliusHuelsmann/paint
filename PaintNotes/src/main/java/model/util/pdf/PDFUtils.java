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

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

/**
 * 
 * This file and its methods are based on the source code of the file
 * PDFToImage of the apache pdfbox document, version 2.0.0.
 * The project's download page can be found at 
 * https://pdfbox.apache.org/download.cgi#scm.
 * The original file is edited by Julius Huelsmann.
 * 
 * 
 * Convert a PDF document to an image.
 *
 * @author Ben Litchfield
 * @author Julius Huelsmann
 */
public final class PDFUtils {
	
	/**
	 * Identifiers that serve as parameters for the method call.
	 */
    private static final String PASSWORD = "-password", 
    		START_PAGE = "-startPage", END_PAGE = "-endPage",
    		PAGE = "-page", IMAGE_TYPE = "-imageType", 
    		FORMAT = "-format", OUTPUT_PREFIX = "-outputPrefix", 
    		PREFIX = "-prefix", COLOR = "-color", 
    		RESOLUTION = "-resolution", DPI = "-dpi", CROPBOX = "-cropbox", 
    		TIME = "-time";

    /**
     * Private utility class constructor
     */
    private PDFUtils() { }

    
    
    /**
     * Method for directly extracting image information from inside 
     * a specified PDF document. 
     * @param _path	the location of the PDF document on disk.
     * @return	the extracted BufferedImage.
     */
    public static BufferedImage pdf2image(final String _path) {
    	try {
			return pdf2image(new String[]{_path});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * Infamous main method.
     * Adapted by Julius Huelsmann.
     * 
     * @author Ben Litchfield
     *
     * @param args Command line arguments, should be one and a reference to a file.
     *
     * @throws IOException If there is an error parsing the document.
     */
    public static BufferedImage pdf2image(final String[] _parameters) 
    		throws IOException {
    	
    	// suppress the Dock icon on OS X if called from outside
    	// the paint - project.
        System.setProperty("apple.awt.UIElement", "true");

        String password = "";
        String pdfFile = null;
        String outputPrefix = null;
        String imageFormat = "jpg";
        int startPage = 1;
        int endPage = Integer.MAX_VALUE;
        String color = "rgb";
        int dpi;
        float cropBoxLowerLeftX = 0;
        float cropBoxLowerLeftY = 0;
        float cropBoxUpperRightX = 0;
        float cropBoxUpperRightY = 0;
        boolean showTime = false;
        try {
            dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        } catch( HeadlessException e ) {
            dpi = 96;
        }
        for( int i = 0; i < _parameters.length; i++ ) {
            if( _parameters[i].equals( PASSWORD ) ) {
                i++;
                if( i >= _parameters.length )
                {
                    usage();
                }
                password = _parameters[i];
            }
            else if( _parameters[i].equals( START_PAGE ) )
            {
                i++;
                if( i >= _parameters.length )
                {
                    usage();
                }
                startPage = Integer.parseInt( _parameters[i] );
            }
            else if( _parameters[i].equals( END_PAGE ) )
            {
                i++;
                if( i >= _parameters.length )
                {
                    usage();
                }
                endPage = Integer.parseInt( _parameters[i] );
            }
            else if( _parameters[i].equals( PAGE ) )
            {
                i++;
                if( i >= _parameters.length )
                {
                    usage();
                }
                startPage = Integer.parseInt( _parameters[i] );
                endPage = Integer.parseInt( _parameters[i] );
            }
            else if( _parameters[i].equals(IMAGE_TYPE) || _parameters[i].equals(FORMAT) )
            {
                i++;
                imageFormat = _parameters[i];
            }
            else if( _parameters[i].equals( OUTPUT_PREFIX ) || _parameters[i].equals( PREFIX ) )
            {
                i++;
                outputPrefix = _parameters[i];
            }
            else if( _parameters[i].equals( COLOR ) )
            {
                i++;
                color = _parameters[i];
            }
            else if( _parameters[i].equals( RESOLUTION ) || _parameters[i].equals( DPI ) )
            {
                i++;
                dpi = Integer.parseInt(_parameters[i]);
            }
            else if( _parameters[i].equals( CROPBOX ) )
            {
                i++;
                cropBoxLowerLeftX = Float.valueOf(_parameters[i]);
                i++;
                cropBoxLowerLeftY = Float.valueOf(_parameters[i]);
                i++;
                cropBoxUpperRightX = Float.valueOf(_parameters[i]);
                i++;
                cropBoxUpperRightY = Float.valueOf(_parameters[i]);
            }
            else if( _parameters[i].equals( TIME ) )
            {
                showTime = true;
            }
            else
            {
                if( pdfFile == null )
                {
                    pdfFile = _parameters[i];
                }
            }
        }
        if( pdfFile == null )
        {
            usage();
        }
        else
        {
            if(outputPrefix == null)
            {
                outputPrefix = pdfFile.substring( 0, pdfFile.lastIndexOf( '.' ));
            }

            PDDocument document = null;
            try
            {
                document = PDDocument.load(new File(pdfFile), password);

                ImageType imageType = null;
                if ("bilevel".equalsIgnoreCase(color))
                {
                    imageType = ImageType.BINARY;
                }
                else if ("gray".equalsIgnoreCase(color))
                {
                    imageType = ImageType.GRAY;
                }
                else if ("rgb".equalsIgnoreCase(color))
                {
                    imageType = ImageType.RGB;
                }
                else if ("rgba".equalsIgnoreCase(color))
                {
                    imageType = ImageType.ARGB;
                }
                
                if (imageType == null)
                {
                    System.err.println( "Error: Invalid color." );
                    System.exit( 2 );
                }

                //if a CropBox has been specified, update the CropBox:
                //changeCropBoxes(PDDocument document,float a, float b, float c,float d)
                if ( cropBoxLowerLeftX!=0 || cropBoxLowerLeftY!=0
                        || cropBoxUpperRightX!=0 || cropBoxUpperRightY!=0 )
                {
                    changeCropBox(document,
                            cropBoxLowerLeftX, cropBoxLowerLeftY,
                            cropBoxUpperRightX, cropBoxUpperRightY);
                }

                long startTime = System.nanoTime();

                // render the pages
                boolean success = true;
                endPage = Math.min(endPage, document.getNumberOfPages());
                PDFRenderer renderer = new PDFRenderer(document);
                for (int i = startPage - 1; i < endPage; i++)
                {
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi, imageType);
                    String fileName = outputPrefix + (i + 1) + "." + imageFormat;
                    success &= ImageIOUtil.writeImage(image, fileName, dpi);
                    return image;
                }

                // performance stats
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                int count = 1 + endPage - startPage;
                if (showTime)
                {
                    System.err.printf("Rendered %d page%s in %dms\n", count, count == 1 ? "" : "s",
                                      duration / 1000000);
                }

                if (!success)
                {
                    System.err.println( "Error: no writer found for image format '"
                            + imageFormat + "'" );
                    System.exit(1);
                }
            }
            finally
            {
                if( document != null )
                {
                    document.close();
                }
            }
            return null;
        }
		return null;
    }
    
    
    
    public static BufferedImage pdf2image(PDDocument _document, int _pageNumber) {

    	int dpi = 0;
        try {
            dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        } catch( HeadlessException e ) {
            dpi = 96;
            dpi = 106;
        }
    	PDFRenderer renderer = new PDFRenderer(_document);
    	BufferedImage image;
		try {
			image = renderer.renderImageWithDPI(_pageNumber, dpi, ImageType.RGB);
	    	return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			return null;
		}
    
    }

    /**
     * This will print the usage requirements and exit.
     */
    private static void usage()
    {
        System.err.println( "Usage: java -jar pdfbox-app-x.y.z.jar PDFToImage [OPTIONS] <PDF file>\n" +
            "  -password  <password>          Password to decrypt document\n" +
            "  -format <string>               Image format: " + getImageFormats() + "\n" +
            "  -prefix <string>               Filename prefix for image files\n" +
            "  -page <number>                 The only page to extract (1-based)\n" +
            "  -startPage <number>            The first page to start extraction (1-based)\n" +
            "  -endPage <number>              The last page to extract(inclusive)\n" +
            "  -color <string>                The color depth (valid: bilevel, indexed, gray, rgb, rgba)\n" +
            "  -dpi <number>                  The DPI of the output image\n" +
            "  -cropbox <number> <number> <number> <number> The page area to export\n" +
            "  -time                          Prints timing information to stdout\n" +
            "  <PDF file>                     The PDF document to use\n"
            );
        System.exit( 1 );
    }

    private static String getImageFormats()
    {
        StringBuilder retval = new StringBuilder();
        String[] formats = ImageIO.getReaderFormatNames();
        for( int i = 0; i < formats.length; i++ )
        {
           if (formats[i].equalsIgnoreCase(formats[i]))
           {
               retval.append( formats[i] );
               if( i + 1 < formats.length )
               {
                   retval.append( ", " );
               }
           }
        }
        return retval.toString();
    }

    private static void changeCropBox(PDDocument document, float a, float b, float c, float d)
    {
        for (PDPage page : document.getPages())
        {
            System.out.println("resizing page");
            PDRectangle rectangle = new PDRectangle();
            rectangle.setLowerLeftX(a);
            rectangle.setLowerLeftY(b);
            rectangle.setUpperRightX(c);
            rectangle.setUpperRightY(d);
            page.setCropBox(rectangle);

        }
    }
    
    
    
    
    
    
    
    
    

	
    /**
     * Test: Create pdf from image and pdf file.
     *
     * @param _inputFile 	pdf input
     * @param _imagePath 	image input
     * @param _outputFile 	pdf output
     *
     * @throws IOException occurs if reading the data fails.
     */
    public void saveAsPdf(
    		String _inputFile, String _imagePath, String _outputFile )
            throws IOException{

    	
    	
    	PDDocument doc = null;
        try
        {
        	
        	// create new document and insert empty page to the document.
        	doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);

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
}
