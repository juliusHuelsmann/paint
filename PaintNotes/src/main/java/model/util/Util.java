package model.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Point;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectWriting;
import model.settings.Status;
import model.settings.ViewSettings;
import model.util.adt.list.List;


/**
 * Very unspecific utility methods.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Util {

	public static String EXECUTION_SUCCESS = "Success",
			EXECUTION_FAILED = "Failure";
	
    
    /**
     * Empty utility class constructor.
     */
    private Util() { }
    

    public static void print(){

    	BufferedImage bi_pic = Picture.getInstance().calculateImage();
  		JFrame jf = new JFrame();
    	jf.setSize(
    			(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()
    			* bi_pic.getWidth()  / bi_pic.getHeight(),
    			(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    	jf.setVisible(false);
    	PrintJob pjob = Toolkit.getDefaultToolkit().getPrintJob(jf,"paintPrint",null);
    	if (pjob != null) {
    	      Graphics pg = pjob.getGraphics();
    	      if (pg != null) {
    	    	  bi_pic = resize(bi_pic, jf.getWidth(), jf.getHeight());
    	    	  pg.drawImage(bi_pic, 0, 0, null);
    	    	  pg.dispose();
    	      }
    	      pjob.end();
    	    }
    	jf.dispose();
    }
    public static BufferedImage resize(
    		BufferedImage _bi, int _width, int _height) { 
	   
    	
    	Image img_scaled = _bi.getScaledInstance(
    			_width, 
    			_height, 
    			Image.SCALE_SMOOTH);
    	
	    BufferedImage bi = new BufferedImage(
	    		_width, 
	    		_height, 
	    		BufferedImage.TYPE_INT_ARGB);

	    Graphics2D
	    g2d = bi.createGraphics();
	    g2d.drawImage(img_scaled, 0, 0, null);
	    g2d.dispose();

	    return bi;
	}  

    public static BufferedImage resize(
    		String _bi, int _width, int _height) { 
	   
    	BufferedImage img_scaled;
		try {
			img_scaled = ImageIO.read(new File(_bi));
		    return resize(img_scaled, _width, _height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	

	}  
    
    
    /**
     * Execute linux command in terminal and return the result.
     * 
     * @param _command the command that will be executed.
     * @return the answer of the linux command
     */
    public static String executeCommandLinux(final String _command) {
    	
    	
        Process p;
        try {
        	
        	//execute command
            p = Runtime.getRuntime().exec(_command);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            
            //fetch answer
            String answer = "", s;
            while ((s = br.readLine()) != null) {
            	answer += s;
            }
            p.waitFor();
            
            //if normal termination
            if (p.exitValue() == 0) {
            	answer = EXECUTION_SUCCESS  + ": " + answer;
            } else {
            	answer = EXECUTION_FAILED + ": " + answer;
            }
            
            //destroy execution process and return the result
            p.destroy();
        	return answer;
        	
        } catch (Exception e) {
        	
        	//print stack trace and return the failure message.
        	e.printStackTrace();
        	return EXECUTION_FAILED;
        }
    }
    
    
    
    /**
     * return list of strings from a text file.
     * 
     * @param _path the path of the text file
     * @return list of lines in text file
     */
    public static List<String> loadTextFile(String _path) {

    	List<String> ls_strings = new List<String>();
        try {
	        BufferedReader br = new BufferedReader(new FileReader(_path));
	        String line = br.readLine();
	
	            while (line != null) {
	            	ls_strings.insertBehind(line);
	                line = br.readLine();
	            }
				br.close();
				return ls_strings;
		} catch (IOException e) {
			e.printStackTrace();
	        return null;
		}
    }
    
    
    
    /**
     * convert list of points into array.
     * 
     * @param _ld list of points
     * @return the array
     */
    public static PaintObjectWriting[] poLsToArray(
            final List<PaintObjectWriting> _ld) {

        /*
         * Transform lists to arrays
         */
        int length = 0;
        _ld.toFirst();
        
        //it is necessary to double check behind and empty because an empty
        //list returns the length of 1 otherwise.
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            _ld.next();
            length++;
        }
        
        PaintObjectWriting [] pow = new PaintObjectWriting[length];

        _ld.toFirst();
        int index = 0;
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            pow[index] = _ld.getItem();
            _ld.next();
            index++;
        }
        
        return pow;
    }

    
    /**
     * convert list of points into array.
     * 
     * @param _ld list of points
     * @return the array
     */
    public static DPoint[] pntLsToArray(
            final List<DPoint> _ld) {

        /*
         * Transform lists to arrays
         */
        int length = 0;
        _ld.toFirst();
        
        //it is necessary to double check behind and empty because an empty
        //list returns the length of 1 otherwise.
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            _ld.next();
            length++;
        }
        
        DPoint [] pow = new DPoint[length];

        _ld.toFirst();
        int index = 0;
        while (!_ld.isBehind() && !_ld.isEmpty()) {
            pow[index] = _ld.getItem();
            _ld.next();
            index++;
        }
        
        return pow;
    }
    
    
    /**
     * transform DPoint array to Point array.
     * @param _dp DPoint array
     * @return Point array
     */
    public static Point[] dpntToPntArray(final DPoint[] _dp) {
        Point[] pnt_return = new Point[_dp.length];
        for (int i = 0; i < _dp.length; i++) {
            pnt_return[i] = new Point((int) _dp[i].getX(), (int) _dp[i].getY());
        }
        return pnt_return;
    }
    
    

    /**
     * merge two arrays.
     * @param _p1 the first array
     * @param _p2 the second array
     * @return the merged array
     */
    public static PaintObject[][] mergeDoubleArray(
            final PaintObject[][] _p1, final PaintObject[][] _p2) {
        
        if (_p1.length != _p2.length) {
            return null;
        }
        
        PaintObject [][] p_return = new PaintObject[_p1.length][1];
        for (int i = 0; i < _p1.length; i++) {
            p_return[i] = mergeArray(_p1[i], _p2[i]);
        }
        
        return p_return;
    }
    
    /**
     * merge two arrays.
     * @param _p1 the first array
     * @param _p2 the second array
     * @return the merged array
     */
    public static PaintObject[] mergeArray(
            final PaintObject[] _p1, final PaintObject[] _p2) {

        PaintObject [] p_return = new PaintObject[_p1.length + _p2.length];
        for (int i = 0; i < _p1.length; i++) {
            p_return[i] = _p1[i];
        }
        for (int i = _p1.length; i < _p2.length; i++) {
            p_return[i] = _p2[i - _p1.length];
        }
        return p_return;
    }





    /**
     * Apply stroke on background.
     * @param jlbl_stroke the background carrying item.
     */
	public static void getStroke(JLabel jlbl_stroke, int _addX, int _addY) {
		
		
		final int strokeDistance = 10;
		if (jlbl_stroke.getWidth() > 0 
				&& jlbl_stroke.getHeight() > 0
//				&& jlbl_stroke.isShowing()
				) {
			BufferedImage bi_stroke = new BufferedImage(jlbl_stroke.getWidth(), 
	        		jlbl_stroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x ++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y ++) {
	            	

//	            	if ( (_addX + x + y +  _addY) % 20 == 0) {
	            	
	            	try{
	            		if ( (x + jlbl_stroke.getLocationOnScreen().x
		            			+ y + jlbl_stroke.getLocationOnScreen().y) 
		            			% strokeDistance == 0) {

		                	bi_stroke.setRGB(x, y, new Color(10,10,10, 10).getRGB());
		            	} else {

		                	bi_stroke.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
		            	}
	            	} catch (IllegalComponentStateException e) {
	            		x = bi_stroke.getWidth();
	            		y = bi_stroke.getHeight();
	            	}
	            
	            }	
	        }
	        jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
		}
		
	}   /**
     * Apply stroke on background.
     * @param jlbl_stroke the background carrying item.
     */
	public static void getStrokeRec(JLabel jlbl_stroke, int _addX, int _addY) {
		
		
		final int strokeDistance = 10;
		if (jlbl_stroke.getWidth() > 0 
				&& jlbl_stroke.getHeight() > 0
//				&& jlbl_stroke.isShowing()
				) {
			BufferedImage bi_stroke = new BufferedImage(jlbl_stroke.getWidth(), 
	        		jlbl_stroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x ++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y ++) {
	            	

//	            	if ( (_addX + x + y +  _addY) % 20 == 0) {
	            	
	            	try{
	            		if ( (x + jlbl_stroke.getLocationOnScreen().x
		            			- y - jlbl_stroke.getLocationOnScreen().y) 
		            			% strokeDistance == 0
		            			||  (x + jlbl_stroke.getLocationOnScreen().x
				            			+ y + jlbl_stroke.getLocationOnScreen().y) 
				            			% strokeDistance == 0) {

		                	bi_stroke.setRGB(x, y, new Color(10,10,10, 10).getRGB());
		            	} else {

		                	bi_stroke.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
		            	}
	            	} catch (IllegalComponentStateException e) {
	            		x = bi_stroke.getWidth();
	            		y = bi_stroke.getHeight();
	            	}
	            
	            }	
	        }
	        jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
		}
		
	}

	
	/**
     * Apply stroke on background.
     * @param jlbl_stroke the background carrying item.
     */
	public static void getScrollStroke(JButton jlbl_stroke) {
		
		
		final int strokeDistance = 10;
		if (jlbl_stroke.getWidth() > 0 
				&& jlbl_stroke.getHeight() > 0
				) {
			BufferedImage bi_stroke = new BufferedImage(jlbl_stroke.getWidth(), 
	        		jlbl_stroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x ++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y ++) {
	            	

	            	
	            	try{
	            		if ( 
	            				(x + jlbl_stroke.getLocationOnScreen().x
		            			- y - jlbl_stroke.getLocationOnScreen().y) 
		            			% strokeDistance == 0
//		            			||  (x + jlbl_stroke.getLocationOnScreen().x
//				            			+ y + jlbl_stroke.getLocationOnScreen().y) 
//				            			% strokeDistance == 0
				            			) {

		                	bi_stroke.setRGB(x, y, Color.lightGray.getRGB());
		            	} else {

		                	bi_stroke.setRGB(x, y, new Color(255, 250, 255).getRGB());
		            	}
	            	} catch (IllegalComponentStateException e) {
	            		x = bi_stroke.getWidth();
	            		y = bi_stroke.getHeight();
	            	}
	            
	            }	
	        }
	        jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
		}
		
	}

    /**
     * Apply stroke on background.
     * @param jlbl_stroke the background carrying item.
     */
	public static void getRoughStroke(JLabel jlbl_stroke) {
		
		
		final int strokeDistance = 20;
		if (jlbl_stroke.getWidth() > 0 
				&& jlbl_stroke.getHeight() > 0
//				&& jlbl_stroke.isShowing()
				) {
			BufferedImage bi_stroke = new BufferedImage(jlbl_stroke.getWidth(), 
	        		jlbl_stroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x ++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y ++) {
	            	

//	            	if ( (_addX + x + y +  _addY) % 20 == 0) {
	            	if ( (x + y) 
	            			% strokeDistance == 0) {

	                	bi_stroke.setRGB(x, y, new Color(40,50,50, 90).getRGB());
	            	} else {

	                	bi_stroke.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
	            	}
	            }	
	        }
	        jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
		}
		
	}
	
	
	
	
	
	
	
	

    /**
     * return fully transparent BufferedImage.
     * 
     * @return the BufferedImage
     */
    public static BufferedImage getEmptyBISelection() {
    	
    	
        BufferedImage bi = new BufferedImage(
        		ViewSettings.getView_bounds_page().width - 1,
        		ViewSettings.getView_bounds_page().height - 1,
        		
//        		jlbl_selectionBG.getWidth(), 
//                jlbl_selectionBG.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        final int maxRGB = 255;
        int rgba = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, rgba);
            }
        }
        
        return bi;
    }
    /**
     * return fully transparent BufferedImage.
     * 
     * @return the BufferedImage
     */
    public static BufferedImage getEmptyBITransparent() {
        BufferedImage bi = new BufferedImage(
                Status.getImageSize().width, 
                Status.getImageSize().height, BufferedImage.TYPE_INT_ARGB);
        final int maxRGB = 255;
        
        int rgba = new Color(maxRGB, maxRGB, maxRGB, 0).getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, rgba);
            }
        }
        
        return bi;
    }
    
    /**
     * return fully transparent BufferedImage.
     * 
     * @return the BufferedImage
     */
    public static BufferedImage getEmptyBIWhite() {
        BufferedImage bi = new BufferedImage(
                Status.getImageSize().width, 
                Status.getImageSize().height, BufferedImage.TYPE_INT_RGB);
        
        final int max = 255;
        int rgb = new Color(max, max, max).getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, rgb);
            }
        }
        
        return bi;
    }
	
	

}
