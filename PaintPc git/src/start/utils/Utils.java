package start.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.objects.painting.PaintBI;
import settings.Status;
import settings.ViewSettings;
import view.forms.Page;

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
    private static final Color RASTAR_COLOR = Color.lightGray;
    

    /**
     * paint a raster background with .
     * @param _bi_background the graphics at which is painted.
     * @param _clr the Color
     * @param _r the rectangle which is to be painted directly at graphics
     * 
     */
    public static void paintRastarBlock(final Graphics _bi_background, 
            final Color [] _clr, final Rectangle _r) {

        //go through the whole buffered image with block size raster
        for (int x = _r.x / ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
                x < (_r.width + _r.x) / ViewSettings
                .SELECTION_BORDER_BLOCK_SIZE + 1; x++) {
            for (int y = _r.y / ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
                    y < (_r.y + _r.height) / ViewSettings
                    .SELECTION_BORDER_BLOCK_SIZE + 1; y++) {

                _bi_background.setColor(_clr[(y + x) % _clr.length]);

                int rectX = x * ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
                int rectY = y * ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
                
                int rectWidth = ViewSettings.SELECTION_BORDER_BLOCK_SIZE;
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
                
                _bi_background.fillRect(rectX, rectY, rectWidth, rectHeight);
            }
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
    private static BufferedImage normalResizeImage(final int _width, 
            final int _height, final String _path) {

        //if path is null return null
        if (_path == "") {
            return null;
        }
        
        File inputFile = new File(_path);
        BufferedImage bi;
        try {
            bi = ImageIO.read(inputFile);
            java.awt.Image scaledImage = bi.getScaledInstance(_width,
                    _height, java.awt.Image.SCALE_SMOOTH);
            BufferedImage outImg = new BufferedImage(_width, 
                    _height, BufferedImage.TYPE_INT_ARGB);
            
            
            Graphics g = outImg.getGraphics();
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();
            return outImg;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
        
        if (Status.isNormalRotation()) {
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
     * return lined background.
     * 
     * @param _width the width
     * @param _height the height
     * @return the BufferedImage
     */
    public static BufferedImage getLinedImage(
            final int _width, final int _height) {
        
        BufferedImage bi = new BufferedImage(
                _width, _height, BufferedImage.TYPE_INT_ARGB);
        
        //initialize with transparency
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
//              bi.setRGB(x, y, (0 << 24) | (0 << 16) | (0 << 8) | 0);
            }
        }
        
        //create raster
        final int size = 15;
        final int margeFront = 150;
        final int margeLast = (2 + 1) * (2 + 2) * (2 + 2 + 2 + 2 + 2)
                + bi.getWidth() % size - 1;
        
        
        Color rastarColor = Color.lightGray;
        
        for (int x = margeFront; x < bi.getWidth() - margeLast; x += size) {
            for (int y = 0; y < bi.getHeight() - (bi.getHeight() % size); 
                    y += 2) {
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
            for (int x = margeFront; x < bi.getWidth() - margeLast; x += 2) {
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
    public static BufferedImage getRastarImage(final BufferedImage _g) {
        
        //the start and end values of the image rectangle which has
        //to be printed.
        int fromX = -Page.getInstance().getJlbl_painting().getLocation().x;
        int fromY = -Page.getInstance().getJlbl_painting().getLocation().y;
        int untilX = fromX + Page.getInstance().getWidth();
        int untilY = fromY + Page.getInstance().getHeight();

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
    @SuppressWarnings("unused")
    public static BufferedImage getRastarImage(
            final BufferedImage _f, final int _fromX, 
            final int _fromY, final int _untilX, final int _untilY, 
            final int _graphiX, final int _graphiY) {
        
        //the width and the height of the entire image, of which the parts
        //are painted.
        int width = Status.getImageShowSize().width;
        int height = Status.getImageShowSize().height;

        //the merge of the page which is not filled with raster but entirely
        //white. distancePoints is not the right expression. distance between 
        //points = distancePoints - 1
        final int distancePoints;
        if (Status.getRasterSize() <= (2 + 2 + 1) * 2) {
           distancePoints = 2;
        } else {
           distancePoints = 2 + 1;
        }
        
        //value because of which is decided whether to print a line
        //or to print a text for debugging purpose.
        boolean print = true;
        //set a small font
//        _g.setFont(new Font("Courier New", Font.PLAIN, (2 + 1) * (2 + 1)));
        //vertical lines    |  |  |  |  |  |  |  |  |
        //                  |  |  |  |  |  |  |  |  |
        //                  |  |  |  |  |  |  |  |  |
        for (int x = 
                //either the starting coordinate (_fromX) matched at the
                //raster size or the merge front if 
                Math.max(Status.getRasterBorderFront(), 
                        (_fromX / Status.getRasterSize()) 
                        * Status.getRasterSize());
                
                //coordinate smaller than the last merge or the until x
                //value
                x < Math.min(width - Status.getRasterBorderEnd(), _untilX); 
                
                //proceeds in size steps
                x += Math.max(Status.getRasterSize() / 2, 1)) {
            
            for (int y = 
                    //the fromX (the window from x)
                    (_fromY / distancePoints) * distancePoints; 
                    
                    //either the height merge (the height minus the height
                    //modulo the distance of the different points or until 
                    //x coordinate
                    y < Math.min(height -  (height % distancePoints), _untilY); 
                    
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
                   if (x == Status.getRasterBorderFront() 
                           || x >= width - Status.getRasterBorderEnd() 
                           - Status.getRasterSize() + 1) {
                        
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
                } else if (!print && y % Status.getRasterSize()
                        == Status.getRasterSize() / 2 && Status.isDebug()) {

                    //the identifier of the small box
                    int xBoxID = (x / Status.getRasterSize()), 
                            yBoxID = (y / Status.getRasterSize());
                    
                    //the location of the information text about the box id
                    int xBoxLoc = coordinateX - Status.getRasterSize() / 2 + 2, 
                            yBoxLoc = coordinateY;
                    
                    //shift the text every second x entity
                    if (xBoxID % 2 == 0) {
                        yBoxLoc += 2 * (2 + 1);
                    }
                    
//                    //draw the string with color black
//                    Color previousColor = _g.getColor();
//                    _g.setColor(Color.black);
//                    _g.drawString(xBoxID + "." +  yBoxID, 
//                           xBoxLoc, yBoxLoc);
//                    //reset color
//                    _g.setColor(previousColor);
                }
            }
            print = !print;
        }
        //horizontal lines  _______________________
        //                  _______________________
        //                  _______________________
        //                  _______________________
        for (int y = Math.max(0, 
                (_fromY / Status.getRasterSize()) * Status.getRasterSize());
                y < Math.min(height -  (height % distancePoints), _untilY); 
                y += Status.getRasterSize()) {
            
            for (int x =  Math.max(Status.getRasterBorderFront(), 
            		_fromX / distancePoints * distancePoints); 
                    x < Math.min(width - Status.getRasterBorderEnd(), _untilX); 
                    x += distancePoints) {

                //calculate correct coordinate values for the graphics
                final int newX = x - _fromX + _graphiX;
                final int newY = y - _fromY + _graphiY;

               PaintBI.paintScilentPoint(
                       _f, newX, newY, RASTAR_COLOR.getRGB());
               
                if (y / Status.getRasterSize() % (2 + 1) == 0) {
                    PaintBI.paintScilentPoint(
                            _f, newX, newY, RASTAR_COLOR.getRGB());
                }
                if (y == (height - height % Status.getRasterSize() - 1)) {
                    if (x + 1 < width) {

                        PaintBI.paintScilentPoint(
                                _f, newX + 1, newY, RASTAR_COLOR.getRGB());
                    }
                    if (x + 2 < width) {
                        PaintBI.paintScilentPoint(
                                _f, newX + 2, newY, RASTAR_COLOR.getRGB());
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
     * @param _g the graphics
     * @param _untilX the second x coordinate
     * @param _untilY the second y coordinate
     * @param _width the first x coordinate
     * @param _height the first y coordinate
     */
    public static void paintNonImage(final BufferedImage _bi, final int _untilX, 
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
