package model.util.paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import start.Start;
import model.objects.painting.PaintBI;
import model.settings.Constants;
import model.settings.StateStandard;
import model.settings.State;
import model.settings.ViewSettings;

/**
 * Class which contains utility methods.
 * @author Julius huelsmann
 * @version %I%, %U%
 */
public final class Utils {

    
    /**
     * Empty utility class constructor.
     */
    private Utils() { }

    
    //create raster
    
    /**
     * Color for painting background for painting.
     */
    private static final Color RASTAR_COLOR = Color.black;
    

    /**
     * paint a raster background with .
     * @param _bi_background the graphics at which is painted.
     * @param _clr the Color
     * @param _r the rectangle which is to be painted directly at graphics
     * 
     * @return the altered bufferedImage 
     */
    public static BufferedImage paintRastarBlock(
            final BufferedImage _bi_background, 
            final Color [] _clr, final Rectangle _r) {
        return paintRastarBlock(_bi_background, _clr, _r, 
                ViewSettings.SELECTION_BORDER_BLOCK_SIZE);
    }



    /**
     * paint a raster background with .
     * @param _bi_background the graphics at which is painted.
     * @param _clr the Color
     * @param _r the rectangle which is to be painted directly at graphics
     * @param _blocksize the size of each block.
     * 
     * @return the altered bufferedImage 
     */
    public static BufferedImage paintRastarBlock(
            final BufferedImage _bi_background, 
            final Color [] _clr, final Rectangle _r, 
            final int _blocksize) {

        //go through the whole buffered image with block size raster
        for (int x = _r.x / _blocksize;
                x < (_r.width + _r.x) / _blocksize + 1; x++) {
            for (int y = _r.y / _blocksize;
                    y < (_r.y + _r.height) / _blocksize + 1; y++) {

                int rectX = x * _blocksize;
                int rectY = y * _blocksize;
                
                int rectWidth = _blocksize;
                int rectHeight = rectWidth;

                //fit the size to the borders.
                if (rectX + rectWidth > _r.width + _r.x) {
                    rectWidth = _r.x + _r.width - rectX;
                }
                if (rectY + rectHeight > _r.height + _r.y) {
                    rectHeight = _r.y + _r.height - rectY;
                }
                
                //fit the start coordinates to the borders
                if (rectX < _r.x) {
                    rectWidth -= _r.x - rectX; 
                    rectX = _r.x;
                }
                if (rectY < _r.y) {
                    rectHeight -= _r.y - rectY; 
                    rectY = _r.y;
                }
                
                PaintBI.fillRectangleQuick(_bi_background, 
                        _clr[(y + x) % _clr.length], new Rectangle(
                                rectX, rectY, rectWidth, rectHeight));
            }
        }
        
        return _bi_background;
    }
    
    
    
    public static String convertString(final String _input) {
    	

    	return _input.replaceAll("%20"," ")
    	.replaceAll("%c3%bc","ü")
    	.replaceAll("%c3%a4","ä")
    	.replaceAll("%c3%b6","ö");
    }


    /**
     * resizes a BufferedImage (input BufferedImage, output BufferedImage).
     * 
     * @param _width the width of the new image
     * @param _height the height of the new image
     * @param _path the path  of the buffered image to be transformed
     * @return the transformed bufferedImage 
     */
    private static BufferedImage normalResizeImage(final int _width, 
            final int _height, final String _path) {

        //if path is null return null
        if (_path == "") {
            return null;
        }
        
        String myPath = Start.class.getResource(_path).getPath();
        myPath = convertString(myPath);
        File inputFile = new File(myPath);
        BufferedImage bi;
        try {
            bi = ImageIO.read(inputFile);
            java.awt.Image scaledImage = bi.getScaledInstance(
            		Math.max(_width, 1),
                    Math.max(_height, 1), java.awt.Image.SCALE_SMOOTH);
            BufferedImage outImg = new BufferedImage(
            		Math.max(_width, 1),
                    Math.max(_height, 1), BufferedImage.TYPE_INT_ARGB);
            
            
            Graphics g = outImg.getGraphics();
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();
            return outImg;
        } catch (IOException e) {
        	System.out.println(e);
        	System.out.println("FEHLER:::");
        	
        	if (!_path.contains(StateStandard.ALTERNATIVE_FILE_START)) {
        		State.getLogger().severe(
        				"Other location used for loading images."
        				+ " May be due to an error.");
        		return normalResizeImage(
        				_width, _height, 
        				StateStandard.ALTERNATIVE_FILE_START + _path);
        	} else {

        		System.out.println(_path);
                e.printStackTrace();
                return null;
        	}
        	
        }
    }
    
    
    /**
     * returns a resized image (rotated or not).
     * 
     * @param _width the width of the new image
     * @param _height the height of the new image
     * @param _path the path  of the buffered image to be transformed
     * @return the transformed bufferedImage 
     */
    public static BufferedImage resizeImage(final int _width, 
            final int _height, final String _path) {
        
        if (State.isNormalRotation()) {
            return normalResizeImage(_width, _height, _path);
        } else {
            return flipImage(_width, _height, _path);
        }
    }

    /**
     * resizes a BufferedImage (input BufferedImage, output BufferedImage).
     * 
     * @param _width the width of the new image
     * @param _height the height of the new image
     * @param _path the path  of the buffered image to be transformed
     * @return the transformed bufferedImage 
     */
    public static BufferedImage flipImage(final int _width, 
            final int _height, final String _path) {

        final int degree = 180;
        BufferedImage src = normalResizeImage(_width, _height, _path);

        BufferedImage newImage = 
                new BufferedImage(_width, _height, src.getType());

        Graphics2D graphic = newImage.createGraphics();
        
        //rotate graphic at center position
        graphic.rotate(Math.toRadians(degree), _width / 2, _height / 2);

        graphic.drawImage(src, null, 0, 0);
        return newImage;
    }

    

    /**
     * resizes a BufferedImage (input BufferedImage, output BufferedImage).
     * 
     * @param _width the width of the buffered image
     * @param _height the height of the buffered image
     * @param _bi the first buffered image
     * @return the new bufferedImage.
     */
    public static BufferedImage resizeImage(final int _width,
            final int _height, final BufferedImage _bi) {
        
        java.awt.Image scaledImage = 
                _bi.getScaledInstance(_width, _height, 
                        java.awt.Image.SCALE_SMOOTH);
        
        BufferedImage outImg = new BufferedImage(_width,
                _height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = outImg.getGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return outImg;
    }

    /**
     * resizes a BufferedImage (input BufferedImage, output BufferedImage).
     * 
     * @param _width the width of the buffered image
     * @param _height the height of the buffered image
     * @param _bi the first buffered image
     * @return the new bufferedImage.
     */
    public static BufferedImage resizeImageQuick(final int _width,
            final int _height, final BufferedImage _bi) {
        
        java.awt.Image scaledImage = 
                _bi.getScaledInstance(_width, _height, 
                        java.awt.Image.SCALE_FAST);
        
        BufferedImage outImg = new BufferedImage(_width,
                _height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = outImg.getGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return outImg;
    }
    
    

    
    
    
    /**
     * Print background.
     * @param _f the bufferedImage
     * 
     * @param _fromX the start point
     * @param _fromY the start point
     * @param _untilX the end point
     * @param _untilY the end point
     * 
     * @param _graphiX the location at the component where the painting 
     *                  process starts.
     * @param _graphiY the location at the component where the painting 
     *                  process starts.
     *                  
     * @return the transformed BufferedImage
     */
    public static synchronized BufferedImage getBackground(
            final BufferedImage _f, final int _fromX, 
            final int _fromY, final int _untilX, final int _untilY, 
            final int _graphiX, final int _graphiY) {
        
        switch (State.getIndexPageBackground()) {
        case Constants.CONTROL_PAGE_BACKGROUND_LINES:
            return getLinedImage(_f, _fromX, _fromY, _untilX, _untilY, 
                    _graphiX, _graphiY);

        case Constants.CONTROL_PAGE_BACKGROUND_NONE:
            return getWhiteBackground(_f, _fromX, _fromY, _untilX, _untilY, 
                    _graphiX, _graphiY);
            
        case Constants.CONTROL_PAGE_BACKGROUND_RASTAR:
            return getRastarImage(_f, _fromX, _fromY, _untilX, _untilY, 
                    _graphiX, _graphiY);
            
        default:
            State.getLogger().warning("unknown background type.");
            return null;
        }
        
    }

    
    /**
     * Print background.
     * @param _f the bufferedImage
     * 
     * @param _fromX the start point
     * @param _fromY the start point
     * @param _untilX the end point
     * @param _untilY the end point
     * 
     * @param _graphiX the location at the component where the painting 
     *                  process starts.
     * @param _graphiY the location at the component where the painting 
     *                  process starts.
     *                  
     * @return the transformed BufferedImage
     */
    public static BufferedImage getBackgroundExport(
            final BufferedImage _f, final int _fromX, 
            final int _fromY, final int _untilX, final int _untilY, 
            final int _graphiX, final int _graphiY) {
        
        Dimension d = new Dimension(State.getImageShowSize());
        State.setImageShowSize(new Dimension(State.getImageSize()));
        
        //fetch show percentages; thus able to reset them because the 
        //called methods work with show - percentages; thus save the export
        //percentages inside the show percentages.
        int borderLShow = State.getBorderLeftPercent();
        int borderRShow = State.getBorderRightPercent();
        int borderTShow = State.getBorderTopPercent();
        int borderBShow = State.getBorderBottomPercent();

        //set percentages
        State.setBorderLeftPercent(State.getBorderLeftPercentExport());
        State.setBorderRightPercent(State.getBorderRightPercentExport());
        State.setBorderTopPercent(State.getBorderTopPercentExport());
        State.setBorderBottomPercent(State.getBorderBottomPercentExport());

        BufferedImage bi = null;
        switch (State.getIndexPageBackgroundExport()) {
        case Constants.CONTROL_PAGE_BACKGROUND_LINES:
            bi = getLinedImage(_f, _fromX, _fromY, _untilX, _untilY, 
                    _graphiX, _graphiY);
            break;

        case Constants.CONTROL_PAGE_BACKGROUND_NONE:
            bi = getWhiteBackground(_f, _fromX, _fromY, _untilX, _untilY, 
                    _graphiX, _graphiY);
            break;
            
        case Constants.CONTROL_PAGE_BACKGROUND_RASTAR:
            bi = getRastarImage(_f, _fromX, _fromY, _untilX, _untilY, 
                    _graphiX, _graphiY);
            break;
        default:
            State.getLogger().warning("unknown background type.");
            bi = null;
            break;
        }

        //reset percentages
        State.setBorderLeftPercentExport(borderLShow);
        State.setBorderRightPercentExport(borderRShow);
        State.setBorderTopPercentExport(borderTShow);
        State.setBorderBottomPercentExport(borderBShow);

        State.setImageShowSize(d);
        return bi;
    }

    /**
     * returns a raster BufferedImage as background.
     * 
     * @param _width the width
     * @param _height the height
     * @return the changed bufferedImage
     */
    public static BufferedImage getRastarImage(
            final int _width, final int _height) {
        
        BufferedImage bi = new BufferedImage(
                _width, _height, BufferedImage.TYPE_INT_ARGB);
        
        //initialize with transparency
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
            }
        }
        
        //create raster
        final int size = 15;
        final int margeFront = 150;
        final int margeLast = (2 + 1) * (2 + 2) * (2 + 2 + 2 + 2 + 2)
                + bi.getWidth() % size - 1;
        
        Color rastarColor = Color.lightGray;
        
        for (int x = margeFront; x < bi.getWidth() - margeLast; x += size) {
            for (int y = 0; y < bi.getHeight() -  (bi.getHeight() % size); 
                    y += 2 + 1) {
                bi.setRGB(x, y, rastarColor.getRGB());
                if (x == margeFront || x == bi.getWidth() - margeLast - 1) {
                    
                    if (y + 1 < bi.getHeight()) {
                        bi.setRGB(x, y + 1, rastarColor.getRGB());
                    }
                    if (y + 2 < bi.getHeight()) {
                        bi.setRGB(x, y + 2, rastarColor.getRGB());
                    }
                }
            }
        }
        
        
        //horizontale linien
        for (int y = size - 1; y < bi.getHeight(); y += size) {
            for (int x = margeFront; x < bi.getWidth() - margeLast; 
                    x += 2 + 1) {
                bi.setRGB(x, y, rastarColor.getRGB());

                if (y / size % (2 + 1) == 0) {
                    bi.setRGB(x, y, Color.gray.getRGB());
                }
                if (y == (bi.getHeight() - bi.getHeight() % size - 1)) {
                    if (x + 1 < bi.getWidth()) {
                        bi.setRGB(x + 1, y, rastarColor.getRGB());
                    }
                    if (x + 2 < bi.getWidth()) {
                        bi.setRGB(x + 2, y, rastarColor.getRGB());
                    }
                }
            }
        }
        return bi;
    }
    

    /**
     * returns a raster BufferedImage as background.
     * 
     * @param _g the BufferedImage to which is painted
     * @return _g
     */
    public static BufferedImage getRastarImage(final BufferedImage _g,
    		final int _x_paintingLocation, 
    		final int _y_paintingLocation,
    		final int _width_page, 
    		final int _height_page) {
        
        //the start and end values of the image rectangle which has
        //to be printed.
        int fromX = -_x_paintingLocation;
        int fromY = -_y_paintingLocation;
        int untilX = fromX + _width_page;
        int untilY = fromY + _height_page;

        return getRastarImage(_g, fromX, fromY, untilX, untilY, 0, 0);
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * return part of the raster image.
     * 
     * @param _f the bufferedImage
     * 
     * @param _fromX the start point
     * @param _fromY the start point
     * @param _untilX the end point
     * @param _untilY the end point
     * 
     * @param _graphiX the location at the component where the painting 
     *                  process starts.
     * @param _graphiY the location at the component where the painting 
     *                  process starts.
     *                  
     * @return the transformed BufferedImage
     */
    private static synchronized BufferedImage getWhiteBackground(
            final BufferedImage _f, final int _fromX, 
            final int _fromY, final int _untilX, final int _untilY, 
            final int _graphiX, final int _graphiY) {
        
        //the width and the height of the entire image, of which the parts
        //are painted.
        int width  = State.getImageShowSize().width;
        int height = State.getImageShowSize().height;

        //the merge of the page which is not filled with raster but entirely
        //white. distancePoints is not the right expression. distance between 
        //points = distancePoints - 1
        final int distancePoints;
        if (State.getRasterSize() <= (2 + 2 + 1) * 2) {
           distancePoints = 2 + 1;
        } else {
           distancePoints = 2 + 1;
        }
        
        
        
        

        
        
        //calculate the border pixel location adapted to the size of the
        //raster
        final int topBorder = 
        		(int) (State.getRasterBorderTop() / State.getRasterSize())
        		* State.getRasterSize();
        
        final int bottomBorder = 
        		(int) ((height - State.getRasterBorderBottom())
        				/ State.getRasterSize())
        				* State.getRasterSize();

        final int leftBorder = 
        		(int) (State.getRasterBorderFront() / State.getRasterSize())
        		* State.getRasterSize();
        
        final int rightBorder = 
        		(int) ((width - State.getRasterBorderEnd())
        				/ State.getRasterSize())
        				* State.getRasterSize();

        final int fromX_raster = (int) (_fromX / State.getRasterSize())
                * State.getRasterSize();

        final int fromY_raster = (int) (_fromY / State.getRasterSize())
                * State.getRasterSize();

        final int fromX_points = (int) (_fromX / distancePoints)
        		* distancePoints;
        
        final int fromY_points = (int) (_fromY / distancePoints)
        		* distancePoints;

        final int untilX_raster = (int) (_untilX / State.getRasterSize())
        		* State.getRasterSize();

        final int untilY_raster = (int) (_untilY / State.getRasterSize())
        		* State.getRasterSize();

        final int untilX_points = (int) (_untilX / distancePoints)
        		* distancePoints;
        
        final int untilY_points = (int) (_untilY / distancePoints)
        		* distancePoints;
        
        
        
        //vertical lines    |  |  |  |  |  |  |  |  |
        //                  |  |  |  |  |  |  |  |  |
        for (int x = 
                //either the starting coordinate (_fromX) matched at the
                //raster size or the merge front if 
                Math.max(
                		
                		//the first (black) line
                		leftBorder, 
                		
                		//the coordinates of the section of the raster that
                		//is printed.
                		fromX_raster);
        		
        		
                //coordinate smaller than the last merge or the until x
                //value
                x <= Math.min(
                		
                		//the last (black) line
                		rightBorder, 
                		
                		//the last position in image
                		untilX_raster);
        		
        		
                //proceeds in size steps
                x += State.getRasterSize()) {

            
            for (int y = 
                    //the fromX (the window from x)
                    Math.max(
                    		
                    		
                    		//the first point beneath the first (black) line
                    		((int) (topBorder / distancePoints) + 1)
                    		* distancePoints , 
                    
                    		//the first position in image
                    		fromY_points);
            		
            		
            		//either the height merge (the height minus the height
                    //modulo the distance of the different points or until 
                    //x coordinate
                    y <= Math.min(
                    		
                    		//the last (black) line
                    		bottomBorder, 
                    		
                    		//the last position in image
                    		untilY_points); 

                    //proceeds with the speed of distancePoints
                    y += distancePoints) {

                //calculate the values of the coordinates which are painted 
                //at the graphics.
                final int coordinateX = x - _fromX + _graphiX;
                final int coordinateY = y - _fromY + _graphiY;
                
                if (coordinateX >= 0 && coordinateY >= 0
                		&& coordinateX < _f.getWidth()
                		&& coordinateY < _f.getHeight()) {
                	
                	
                    
                    //if the loop has reached the last values paint a line.
                    if (x == leftBorder || x == rightBorder) {

                  	  //paint the point
                      PaintBI.paintScilentPoint(_f, coordinateX, coordinateY,
                     		RASTAR_COLOR.getRGB());
                    	if (coordinateY + 1 < height) {
                    		PaintBI.paintScilentPoint(_f, coordinateX, 
                    				coordinateY + 1, RASTAR_COLOR.getRGB());
                    	}	
                    	if (coordinateY + 2 < height) {
                    		PaintBI.paintScilentPoint(_f, coordinateX, 
                    				coordinateY + 2, RASTAR_COLOR.getRGB());
                    	}	
                    }
                }
            }
        }
        //horizontal lines  _______________________
        //                  _______________________
        //                  _______________________
        //                  _______________________
        for (int y = Math.max(

        		topBorder,  
               
        		
        		fromY_raster);

                y <= Math.min(
                		
                		bottomBorder,
                		
                		untilY_raster); 

        		y += State.getRasterSize()) {
            
            for (int x =  Math.max(
            		
            		
              		((int) (leftBorder / distancePoints) + 1)
              			* distancePoints, 
            		
              		fromX_points);
            		
                    x <= Math.min(
                    		
                    		rightBorder, 
                    		untilX_points); 
            		
                    x += distancePoints) {

                //calculate correct coordinate values for the graphics
                final int newX = x - _fromX + _graphiX;
                final int newY = y - _fromY + _graphiY;

                
                if (newX >= 0 && newY >= 0
                		&& newX < _f.getWidth() && newY < _f.getHeight()) {
                	
                	
                      //if the loop has reached the last values paint a line.
                      if (y == topBorder || y == bottomBorder) {

                     	 PaintBI.paintScilentPoint(
                                 _f, newX, newY, RASTAR_COLOR.getRGB());
                           if (newX + 1 < height) {
                               PaintBI.paintScilentPoint(
                                       _f, newX + 1, newY, 
                                       RASTAR_COLOR.getRGB());
                           }
                           if (newX + 2 < height) {
                               PaintBI.paintScilentPoint(
                                       _f, newX + 2, newY,
                                       RASTAR_COLOR.getRGB());
                           }
                      }
                }
            }
        }
        
        //paint the non image and the border of the page.
        if (width < _untilX || height < _untilY) {
            paintNonImage(_f, _untilX, _untilY, width, height);
        }
        return _f;
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * return part of the raster image.
     * 
     * @param _f the bufferedImage
     * 
     * @param _fromX the start point
     * @param _fromY the start point
     * @param _untilX the end point
     * @param _untilY the end point
     * 
     * @param _graphiX the location at the component where the painting 
     *                  process starts.
     * @param _graphiY the location at the component where the painting 
     *                  process starts.
     *                  
     * @return the transformed BufferedImage
     */
    private static synchronized BufferedImage getRastarImage(
            final BufferedImage _f, final int _fromX, 
            final int _fromY, final int _untilX, final int _untilY, 
            final int _graphiX, final int _graphiY) {
        
        //the width and the height of the entire image, of which the parts
        //are painted.
        int width  = State.getImageShowSize().width;
        int height = State.getImageShowSize().height;

        //the merge of the page which is not filled with raster but entirely
        //white. distancePoints is not the right expression. distance between 
        //points = distancePoints - 1
        final int distancePoints;
        if (State.getRasterSize() <= (2 + 2 + 1) * 2) {
           distancePoints = 2 + 1;
        } else {
           distancePoints = 2 + 1;
        }
        
        
        
        
        
        
        
        
        
        
        
        

        
        
        //calculate the border pixel location adapted to the size of the
        //raster
        final int topBorder = 
        		(int) (State.getRasterBorderTop() / State.getRasterSize())
        		* State.getRasterSize();
        
        final int bottomBorder = 
        		(int) ((height - State.getRasterBorderBottom())
        				/ State.getRasterSize())
        				* State.getRasterSize();

        final int leftBorder = 
        		(int) (State.getRasterBorderFront() / State.getRasterSize())
        		* State.getRasterSize();
        
        final int rightBorder = 
        		(int) ((width - State.getRasterBorderEnd())
        				/ State.getRasterSize())
        				* State.getRasterSize();

        final int fromX_raster = (int) (_fromX / State.getRasterSize())
                * State.getRasterSize();

        final int fromY_raster = (int) (_fromY / State.getRasterSize())
                * State.getRasterSize();

        final int fromX_points = (int) (_fromX / distancePoints)
        		* distancePoints;
        
        final int fromY_points = (int) (_fromY / distancePoints)
        		* distancePoints;

        final int untilX_raster = (int) (_untilX / State.getRasterSize())
        		* State.getRasterSize();

        final int untilY_raster = (int) (_untilY / State.getRasterSize())
        		* State.getRasterSize();

        final int untilX_points = (int) (_untilX / distancePoints)
        		* distancePoints;
        
        final int untilY_points = (int) (_untilY / distancePoints)
        		* distancePoints;
        
        
        
        //vertical lines    |  |  |  |  |  |  |  |  |
        //                  |  |  |  |  |  |  |  |  |
        for (int x = 
                //either the starting coordinate (_fromX) matched at the
                //raster size or the merge front if 
                Math.max(
                		
                		//the first (black) line
                		leftBorder, 
                		
                		//the coordinates of the section of the raster that
                		//is printed.
                		fromX_raster);
        		
        		
                //coordinate smaller than the last merge or the until x
                //value
                x <= Math.min(
                		
                		//the last (black) line
                		rightBorder, 
                		
                		//the last position in image
                		untilX_raster);
        		
        		
                //proceeds in size steps
                x += State.getRasterSize()) {

            
            for (int y = 
                    //the fromX (the window from x)
                    Math.max(
                    		
                    		
                    		//the first point beneath the first (black) line
                    		((int) (topBorder / distancePoints) + 1)
                    		* distancePoints , 
                    
                    		//the first position in image
                    		fromY_points);
            		
            		
            		//either the height merge (the height minus the height
                    //modulo the distance of the different points or until 
                    //x coordinate
                    y <= Math.min(
                    		
                    		//the last (black) line
                    		bottomBorder, 
                    		
                    		//the last position in image
                    		untilY_points); 

                    //proceeds with the speed of distancePoints
                    y += distancePoints) {

                //calculate the values of the coordinates which are painted 
                //at the graphics.
                final int coordinateX = x - _fromX + _graphiX;
                final int coordinateY = y - _fromY + _graphiY;
                
                if (coordinateX >= 0 && coordinateY >= 0
                		&& coordinateX < _f.getWidth()
                		&& coordinateY < _f.getHeight()) {
                	
                	
                	  //paint the point
                    PaintBI.paintScilentPoint(_f, coordinateX, coordinateY,
                   		RASTAR_COLOR.getRGB());
                    
                    //if the loop has reached the last values paint a line.
                    if (x == leftBorder || x == rightBorder) {
                    	
                    	if (coordinateY + 1 < height) {
                    		PaintBI.paintScilentPoint(_f, coordinateX, 
                    				coordinateY + 1, RASTAR_COLOR.getRGB());
                    	}	
                    	if (coordinateY + 2 < height) {
                    		PaintBI.paintScilentPoint(_f, coordinateX, 
                    				coordinateY + 2, RASTAR_COLOR.getRGB());
                    	}	
                    }
                }
            }
        }
        //horizontal lines  _______________________
        //                  _______________________
        //                  _______________________
        //                  _______________________
        for (int y = Math.max(

        		topBorder,  
               
        		
        		fromY_raster);

                y <= Math.min(
                		
                		bottomBorder,
                		
                		untilY_raster); 

        		y += State.getRasterSize()) {
            
            for (int x =  Math.max(
            		
            		
              		((int) (leftBorder / distancePoints) + 1)
              			* distancePoints, 
            		
              		fromX_points);
            		
                    x <= Math.min(
                    		
                    		rightBorder, 
                    		untilX_points); 
            		
                    x += distancePoints) {

                //calculate correct coordinate values for the graphics
                final int newX = x - _fromX + _graphiX;
                final int newY = y - _fromY + _graphiY;

                
                if (newX >= 0 && newY >= 0
                		&& newX < _f.getWidth() && newY < _f.getHeight()) {
                	
                	 PaintBI.paintScilentPoint(
                            _f, newX, newY, RASTAR_COLOR.getRGB());
                	
                      //if the loop has reached the last values paint a line.
                      if (y == topBorder || y == bottomBorder) {
                           
                           if (newX + 1 < height) {
                               PaintBI.paintScilentPoint(
                                       _f, newX + 1, newY, RASTAR_COLOR
                                       .getRGB());
                           }
                           if (newX + 2 < height) {
                               PaintBI.paintScilentPoint(
                                       _f, newX + 2, newY, RASTAR_COLOR
                                       .getRGB());
                           }
                      }
                }
            }
        }
        
        //paint the non image and the border of the page.
        if (width < _untilX || height < _untilY) {
            paintNonImage(_f, _untilX, _untilY, width, height);
        }
        return _f;
    }

    /**
     * return part of the raster image.
     * 
     * @param _f the bufferedImage
     * 
     * @param _fromX the start point
     * @param _fromY the start point
     * @param _untilX the end point
     * @param _untilY the end point
     * 
     * @param _graphiX the location at the component where the painting 
     *                  process starts.
     * @param _graphiY the location at the component where the painting 
     *                  process starts.
     *                  
     * @return the transformed BufferedImage
     */
    @SuppressWarnings("unused")
    private static BufferedImage getLinedImage(
            final BufferedImage _f, final int _fromX, 
            final int _fromY, final int _untilX, final int _untilY, 
            final int _graphiX, final int _graphiY) {
        
        //the width and the height of the entire image, of which the parts
        //are painted.
        int width = State.getImageShowSize().width;
        int height = State.getImageShowSize().height;

        //the merge of the page which is not filled with raster but entirely
        //white. distancePoints is not the right expression. distance between 
        //points = distancePoints - 1
        final int distancePoints;
        if (State.getRasterSize() <= (2 + 2 + 1) * 2) {
           distancePoints = 2;
        } else {
           distancePoints = 2 + 1;
        }
        
        
        
        
        
        


        
        
        //calculate the border pixel location adapted to the size of the
        //raster
        final int topBorder = 
        		(int) (State.getRasterBorderTop() / State.getRasterSize())
        		* State.getRasterSize();
        
        final int bottomBorder = 
        		(int) ((height - State.getRasterBorderBottom())
        				/ State.getRasterSize())
        				* State.getRasterSize();

        final int leftBorder = 
        		(int) (State.getRasterBorderFront() / State.getRasterSize())
        		* State.getRasterSize();
        
        final int rightBorder = 
        		(int) ((width - State.getRasterBorderEnd())
        				/ State.getRasterSize())
        				* State.getRasterSize();

        final int fromX_raster = (int) (_fromX / State.getRasterSize())
                * State.getRasterSize();

        final int fromY_raster = (int) (_fromY / State.getRasterSize())
                * State.getRasterSize();

        final int fromX_points = (int) (_fromX / distancePoints)
        		* distancePoints;
        
        final int fromY_points = (int) (_fromY / distancePoints)
        		* distancePoints;

        final int untilX_raster = (int) (_untilX / State.getRasterSize())
        		* State.getRasterSize();

        final int untilY_raster = (int) (_untilY / State.getRasterSize())
        		* State.getRasterSize();

        final int untilX_points = (int) (_untilX / distancePoints)
        		* distancePoints;
        
        final int untilY_points = (int) (_untilY / distancePoints)
        		* distancePoints;
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        //value because of which is decided whether to print a line
        //or to print a text for debugging purpose.
        boolean print = true;
        //set a small font
//        _g.setFont(new Font("Courier New", Font.PLAIN, (2 + 1) * (2 + 1)));
        //vertical lines    |                       |
        //                  |                       |
        //                  |                       |
        //                  |                       |
        for (int x : new int[]{
        		leftBorder, 
            	rightBorder}) {
        	
        	
            for (int y = Math.max(

            		topBorder,  
                   
            		
            		fromY_raster);

                    y <= Math.min(
                    		
                    		bottomBorder,
                    		
                    		untilY_raster); 
                    //proceeds with the speed of distancePoints
                    y++) {
    
                //calculate the values of the coordinates which are painted 
                //at the graphics.
                final int coordinateX = x - _fromX + _graphiX;
                final int coordinateY = y - _fromY + _graphiY;
                

                if (coordinateX >= 0 && coordinateY >= 0
                		&& coordinateX < _f.getWidth()
                		&& coordinateY < _f.getHeight()) {
                	
                    //paint the point
                    PaintBI.paintScilentPoint(
                            _f, coordinateX, coordinateY,
                            RASTAR_COLOR.getRGB());
    
                   //if the loop has reached the last values paint a line.
                   if (x == State.getRasterBorderFront() 
                           || x >= width - State.getRasterBorderEnd() 
                           - State.getRasterSize() + 1) {
                        
                        if (y + 1 < height) {
                            PaintBI.paintScilentPoint(
                                    _f, coordinateX, coordinateY + 1,
                                    RASTAR_COLOR.getRGB());
                        }
                        if (y + 2 < height) {
                            PaintBI.paintScilentPoint(
                                    _f, coordinateX, coordinateY + 2,
                                    RASTAR_COLOR.getRGB());
                        }
                    }
                }
            }
        }
        //horizontal lines  _______________________
        //                  _______________________
        //                  _______________________
        //                  _______________________
        for (int y = Math.max(

        		topBorder,  
               
        		
        		fromY_raster);

                y <= Math.min(
                		
                		bottomBorder,
                		
                		untilY_raster); 

        		y += State.getRasterSize()) {
            
            for (int x =  Math.max(
            		
            		
              		((int) (leftBorder / distancePoints) + 1)
              			* distancePoints, 
            		
              		fromX_points);
            		
                    x <= Math.min(
                    		
                    		rightBorder, 
                    		untilX_points); 
            		
                    x += distancePoints) {

                //calculate correct coordinate values for the graphics
                final int newX = x - _fromX + _graphiX;
                final int newY = y - _fromY + _graphiY;

                
                if (newX >= 0 && newY >= 0
                		&& newX < _f.getWidth() && newY < _f.getHeight()) {
                	
                	 PaintBI.paintScilentPoint(
                            _f, newX, newY, RASTAR_COLOR.getRGB());
                	
                      //if the loop has reached the last values paint a line.
                      if (y == topBorder || y == bottomBorder) {
                           
                           if (newX + 1 < height) {
                               PaintBI.paintScilentPoint(
                                       _f, newX + 1, newY, RASTAR_COLOR
                                       .getRGB());
                           }
                           if (newX + 2 < height) {
                               PaintBI.paintScilentPoint(
                                       _f, newX + 2, newY, RASTAR_COLOR
                                       .getRGB());
                           }
                      }
                }
            }
        }
        
        //paint the non image and the border of the page.
        if (width < _untilX || height < _untilY) {
            paintNonImage(_f, _untilX, _untilY, width, height);
        }
        return _f;
    }

    
    /**
     * Print the white empty Background.
     * 
     * @param _f the bufferedImage
     * 
     * @param _fromX the start point
     * @param _fromY the start point
     * @param _untilX the end point
     * @param _untilY the end point
     * 
     * @param _graphiX the location at the component where the painting 
     *                  process starts.
     * @param _graphiY the location at the component where the painting 
     *                  process starts.
     *                  
     * @return the transformed BufferedImage
     */
    private static BufferedImage printWhiteBackgroundOld(
            final BufferedImage _f, final int _fromX, 
            final int _fromY, final int _untilX, final int _untilY, 
            final int _graphiX, final int _graphiY) {
    	

        //the width and the height of the entire image, of which the parts
        //are painted.
        int width = State.getImageShowSize().width;
        int height = State.getImageShowSize().height;

        //the merge of the page which is not filled with raster but entirely
        //white. distancePoints is not the right expression. distance between 
        //points = distancePoints - 1
        final int distancePoints;
        if (State.getRasterSize() <= (2 + 2 + 1) * 2) {
           distancePoints = 2;
        } else {
           distancePoints = 2 + 1;
        }
        //value because of which is decided whether to print a line
        //or to print a text for debugging purpose.
        boolean print = true;
        //set a small font
//        _g.setFont(new Font("Courier New", Font.PLAIN, (2 + 1) * (2 + 1)));
        //vertical lines    |                       |
        //                  |                       |
        //                  |                       |
        //                  |                       |

    	if (State.getRasterBorderFront() != 0 
    			|| State.getBorderRightPercentShow() != 0) {
    	
        for (int x : new int[]{State.getRasterBorderFront(), 
            width - State.getRasterBorderEnd()}) {
            for (int y = 
                    //the fromX (the window from x)
                    Math.max((_fromY / distancePoints) * distancePoints,
                            State.getRasterBorderTop()); 
                    
                    //either the height merge (the height minus the height
                    //modulo the distance of the different points or until 
                    //x coordinate
                    y < Math.min(Math.min(height -  (height % distancePoints), 
                            _untilY), height - State.getRasterBorderBottom()); 
                    
                    //proceeds with the speed of distancePoints
                    y += distancePoints) {
    
                //calculate the values of the coordinates which are painted 
                //at the graphics.
                final int coordinateX = x - _fromX + _graphiX;
                final int coordinateY = y - _fromY + _graphiY;
                
                if (print) {
                    //paint the point
                    PaintBI.paintScilentPoint(
                            _f, coordinateX, coordinateY,
                            RASTAR_COLOR.getRGB());
    
                   //if the loop has reached the last values paint a line.
                   if (x == State.getRasterBorderFront() 
                           || x >= width - State.getRasterBorderEnd() 
                           - State.getRasterSize() + 1) {
                        
                        if (y + 1 < height) {
                            PaintBI.paintScilentPoint(
                                    _f, coordinateX, coordinateY + 1,
                                    RASTAR_COLOR.getRGB());
                        }
                        if (y + 2 < height) {
                            PaintBI.paintScilentPoint(
                                    _f, coordinateX, coordinateY + 2,
                                    RASTAR_COLOR.getRGB());
                        }
                    }
                } 
            }
        }	
    	}
        
        
        

    	if (State.getBorderBottomPercentShow() != 0 
    			|| State.getRasterBorderTop() != 0) {
    	
        //horizontal lines  _______________________
        //
        //
        //
        //                  _______________________
        for (int y = Math.max(State.getRasterBorderTop(),  
                (_fromY / State.getRasterSize()) * State.getRasterSize());
                y < Math.min(Math.min(height -  (height % distancePoints), 
                        _untilY), height - State.getRasterBorderBottom()); 
                y += State.getRasterSize()) {
            
            for (int x =  Math.max(State.getRasterBorderFront(), 
                    _fromX / distancePoints * distancePoints); 
                    x < Math.min(width - State.getRasterBorderEnd(), _untilX); 
                    x += distancePoints) {

                //calculate correct coordinate values for the graphics
                final int newX = x - _fromX + _graphiX;
                final int newY = y - _fromY + _graphiY;

                //if the loop has reached the last values paint a line.
                if (y == State.getRasterBorderTop() 
                        || y >= height - State.getRasterBorderBottom() 
                        - State.getRasterSize() + 1) {
                     
                     if (y + 1 < height) {
                         PaintBI.paintScilentPoint(
                                 _f, newX + 1, newY, RASTAR_COLOR.getRGB());
                     }
                     if (y + 2 < height) {
                         PaintBI.paintScilentPoint(
                                 _f, newX + 2, newY, RASTAR_COLOR.getRGB());
                     }
                 }
            }
        }
    	}
        
        //paint the non image and the border of the page.
        if (width < _untilX || height < _untilY) {
            paintNonImage(_f, _untilX, _untilY, width, height);
        }
        return _f;
    }
    
    /**
     * paint non image.
     * @param _bi the BufferedImage
     * @param _untilX the second x coordinate
     * @param _untilY the second y coordinate
     * @param _width the first x coordinate
     * @param _height the first y coordinate
     */
    public static void paintNonImage(final BufferedImage _bi, 
            final int _untilX,
            final int _untilY, final int _width, final int _height) {
        
        /*
         * print rectangles
         */
//        final Color c_nonImageBG = new Color(230, 240, 255);
        final Color c_nonImageBG = new Color(180, 190, 200);
        
        PaintBI.fillRectangleQuick(_bi, c_nonImageBG, 
                new Rectangle(_width, 0, _untilX, _untilY));
        PaintBI.fillRectangleQuick(_bi, c_nonImageBG, 
                new Rectangle(0, _height, _untilX, _untilY));
        PaintBI.fillRectangleQuick(_bi, c_nonImageBG, 
                new Rectangle(_width, _height, _untilX, _untilY));
        
        
        /*
         * print lines
         */
        PaintBI.fillRectangleQuick(_bi, Color.black,
                new Rectangle(_width, 0, 1, _height));


        PaintBI.fillRectangleQuick(_bi, Color.black,
                new Rectangle(0, _height, _width, 1));
        
    }
}
