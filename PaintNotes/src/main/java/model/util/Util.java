package model.util;


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


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Point;
import java.awt.PrintJob;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import start.Start;
import model.objects.painting.Picture;
import model.objects.painting.po.PaintObject;
import model.objects.painting.po.PaintObjectWriting;
import model.settings.StateStandard;
import model.settings.State;
import model.settings.ViewSettings;
import model.util.adt.list.List;
import model.util.paint.Utils;


/**
 * Very unspecific utility methods.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Util {

	
	/**
	 * Constants that indicate whether an execution (of a command inside the
	 * terminal) was successful or not.
	 */
	public static final String EXECUTION_SUCCESS = "Success",
			EXECUTION_FAILED = "Failure";
	
    
    /**
     * Empty utility class constructor.
     */
    private Util() { }
    
    
    
    /**
     * Function for getting stack trace out of an exception and printiing a 
     * message to the user.
     * 
     * @param _title				the title of the exception
     * @param _additionalMessage	the additional message to the exception 
     * 								message
     * @param _e					the exception
     * @param _cmpOwner				the view-owner of the component that
     * 								caused the error.
     */
    public static void handleException(
    		final String _title,
    		final String _additionalMessage,
    		final Exception _e,
    		final Component _cmpOwner) {


		int result = JOptionPane.showConfirmDialog(
				null, 
				"An error occured while executing paint.\n" 
				+ "This error will not stop the program from working properly."
				+ "\n\n"
				+ "If you agree, the error message (which contains no "
				+ "user-specific information\n"
				+ "will be used for eliminating the bug.  \n"
				+ "If the error is not caused by this program, please select "
				+ "\"don't aggree \""
				+ "\n\nError message:\n"
				+ _additionalMessage + ".\n"
				+ "\n" 
				+ "More detailed information:\n"
				+ Util.stackTraceToString(_e),
				_title,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.ERROR_MESSAGE);
		
		if (result == JOptionPane.YES_OPTION) {
			System.out.println("TODO: sending information");
		} else {
			System.out.println("TODO: not sending information");
		}
    }
    
    /**
     * Writes a stack trace to string.
     * 
     * @param _t the exception
     * @return the stackTrace.
     */
    public static String stackTraceToString(final Exception _t) {
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	_t.printStackTrace(pw);
    	return sw.toString(); 
    }

    /**
     * Function for printing image.
     * 
     * @param _pic instance of Picture that is printed.
     */
    public static void print(final Picture _pic) {

    	BufferedImage bi_pic = _pic.calculateImage();
  		JFrame jf = new JFrame();
    	jf.setSize(
    			(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()
    			* bi_pic.getWidth()  / bi_pic.getHeight(),
    			(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    	jf.setVisible(false);
    	PrintJob pjob = Toolkit.getDefaultToolkit().getPrintJob(
    			jf, "paintPrint", null);
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
    
    
    /**
     * Function for resizing a bufferedImage.
     * 
     * @param _bi 		the bufferedImage that is to be resized
     * 
     * @param _width 	its new width 
     * 
     * @param _height 	its new height
     * 
     * @return			the resized BufferedImage.
     */
    public static BufferedImage resize(
    		final BufferedImage _bi, final int _width, final int _height) { 
	   
    	
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

    
    /**
     * Read an image from file and resize it afterwards.
     * 
     * @param _width	The new width of the image.
     * 
     * @param _height	The new height of the image.
     * 
     * @return			The resized BufferedImage.
     */
    public static BufferedImage resize(
    		final String _path, final int _width, final int _height) { 
	   
    	BufferedImage img_scaled;
		try {

	        String myPath = Start.class.getResource(_path).getPath();
	        myPath = Utils.convertString(myPath);

	        img_scaled = ImageIO.read(Start.class.getResourceAsStream(_path));
			//img_scaled = ImageIO.read(new File(myPath));
		    return resize(img_scaled, _width, _height);
		} catch (IOException e) {

        	if (!_path.contains(StateStandard.ALTERNATIVE_FILE_START)) {
        		State.getLogger().severe("other location used for "
        				+ "loading images. May be due to an error.");
        		return resize(StateStandard.ALTERNATIVE_FILE_START + _path, 
        				_width, _height);
        	} else {

        		System.out.println(_path);
                e.printStackTrace();
                return null;
        	}
		}
	}  
    
    
    /**
     * Export resource from out of jar file.
     * 
     *      
     * @param _path_jar		the path of the file inside the jar file.
     * @param _path_out		the export destination
     * @return 				the path of the exported file.
     * @throws Exception	
     */
    public static void exportResource(
    		final String _path_jar, 
    		final String _path_out)  {
    	
        try {

        	//initialize streams and values
            
            final InputStream stream = Start.class.getResourceAsStream(
            		_path_jar);
            if(stream == null) {
                State.getLogger().warning("Error (1) exporting \""
                		+ _path_jar 
                		+ "\".");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            final OutputStream resStreamOut = new FileOutputStream(_path_out);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            stream.close();
            resStreamOut.close();
        } catch (Exception ex) {

            State.getLogger().warning("Error (2) exporting \""
            		+ _path_jar 
            		+ "\":\n" + ex);
        } 

    }
    
    
    
    
    public static String requestSudoRights(final String _sudoCmd) {
    	JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');
        passwordField.requestFocus();
        int d = JOptionPane.showConfirmDialog(
                null,
                new Object[] {
                		"The os demands sudo privileges for the installation "
                		+ "of paint.\n"
                		+ "Either relaunch the program by calling \n",
                		new JTextField("sudo java -jar paint.jar"),
                		 "or enter the sudoer's password.", 
                		passwordField,

    			},
    			"Request sudo privileges",
                JOptionPane.OK_OPTION);
        
        if (d == JOptionPane.YES_OPTION) {

        	char[] passwd = passwordField.getPassword();

        	String pw = "";
        	for (int i = 0; i < passwd.length; i++) {
    			pw += passwd[i];
    		}
        	execSudoCommand(_sudoCmd, pw);
    		return pw;
        } else {
        	return "";
        }
    }
    
    

    public static void execSudoCommand(final String _sudoCmd, 
    		final String _pw) {


        	String[] cmd = {"/bin/bash","-c", "echo " + _pw + "| sudo -S "
            		+ _sudoCmd};
    		try {
    			Runtime.getRuntime().exec(cmd);
    		} catch (IOException e) {
    			e.printStackTrace();
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
        String answer = "", s;
        try {
        	
        	//execute command
            p = Runtime.getRuntime().exec(_command);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            
            //fetch answer
            while ((s = br.readLine()) != null) {
            	answer += s;
            }
            p.waitFor();
            
            //if normal termination
            if (p.exitValue() == 0) {
            	answer = EXECUTION_SUCCESS  + ": " + answer;
            } else {
            	answer = EXECUTION_FAILED   + ": " + answer;
            }
            
            //destroy execution process and return the result
            p.destroy();
        	return answer;
        	
        } catch (Exception e) {
        	
        	//print stack trace and return the failure message.
//        	e.printStackTrace();
        	return EXECUTION_FAILED;
        }
    }
    
    
    
    /**
     * return list of strings from a text file.
     * 
     * @param _path the path of the text file
     * @return list of lines in text file
     */
    public static List<String> loadTextFile(final String _path) {

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
     * @param _jlbl_stroke the background carrying item.
     * @return the stroked image.
     */
	public static BufferedImage getStroke(final JLabel _jlbl_stroke) {
		
		
		final int strokeDistance = 10;
		if (_jlbl_stroke.getWidth() > 0 
				&& _jlbl_stroke.getHeight() > 0
//				&& jlbl_stroke.isShowing()
				) {
			BufferedImage bi_stroke = new BufferedImage(_jlbl_stroke.getWidth(),
	        		_jlbl_stroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y++) {
	            	

//	            	if ( (_addX + x + y +  _addY) % 20 == 0) {
	            	
	            	try {
	            		if ((x + _jlbl_stroke.getLocationOnScreen().x
		            			+ y + _jlbl_stroke.getLocationOnScreen().y) 
		            			% strokeDistance == 0) {
	            			final int clr = 10;
		                	bi_stroke.setRGB(x, y, 
		                			new Color(clr, clr, clr, clr).getRGB());
		            	} else {

	            			final int clr = 0;
		                	bi_stroke.setRGB(x, y, 
		                			new Color(clr, clr, clr, clr).getRGB());
		            	}
	            	} catch (IllegalComponentStateException e) {
	            		x = bi_stroke.getWidth();
	            		y = bi_stroke.getHeight();
	            	}
	            
	            }	
	        }
	        _jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
			return bi_stroke;
		}
		return null;
		
	}   
	
	
	
	
	
	
	
	
	  /**
     * Apply stroke on background.
     * @param _jlbl_stroke the background carrying item.
     * @return the stroked image.
     */
	public static BufferedImage getStrokeMove(
			final BufferedImage _bi, final JLabel _jlbl_stroke, 
			final int _dX, final int _dY) {
		
		if (_dY >= _bi.getHeight() || _dX >= _bi.getWidth()) {
			return getStroke(_jlbl_stroke);
		} 
		
		
		final int strokeDistance = 10;
		if (_bi.getWidth() > 0 
				&& _bi.getHeight() > 0) {
			

			if (Math.abs(_dX) > _bi.getWidth() / 2
					|| Math.abs(_dY) > _bi.getHeight() / 2) {

				return getStroke(_jlbl_stroke);
			} else {
				Rectangle rec_maintainStart = new Rectangle();
				Rectangle rec_maintainDest = new Rectangle();
				Rectangle rec_new1 = new Rectangle();
				Rectangle rec_new2 = new Rectangle();

				if (_dX > 0) {

					rec_maintainStart.x = _dX;
					rec_maintainDest.x = 0;
					rec_maintainDest.width = _bi.getWidth() - _dX;

					rec_new1.x = rec_maintainDest.width;
					rec_new1.width = _dX;
					rec_new1.height = _bi.getHeight();
					rec_new1.y = 0;
				} else {

					rec_maintainStart.x = 0;
					rec_maintainDest.x = -_dX;
					rec_maintainDest.width = _bi.getWidth() + _dX;

					rec_new1.x = 0;
					rec_new1.width = -_dX;
					rec_new1.height = _bi.getHeight();
					rec_new1.y = 0;
				}

				if (_dY > 0) {
					

					rec_maintainStart.y = _dY;
					rec_maintainDest.y = 0;
					rec_maintainDest.height = _bi.getHeight() - _dY;

					rec_new2.y = _bi.getHeight() - _dY;
					rec_new2.height = _dY;
					rec_new2.x = 0;
					rec_new2.width = _bi.getWidth();
				} else {
					rec_maintainStart.y = 0;
					rec_maintainDest.y = -_dY;
					rec_maintainDest.height = _bi.getHeight() + _dY;

					rec_new2.y = 0;
					rec_new2.height = -_dY;
					rec_new2.x = 0;
					rec_new2.width = _bi.getWidth();
				}
//				rec_new1.height = rec_new2.y + rec_new2.height - rec_new1.y;
				rec_maintainStart.width = rec_maintainDest.width;
				rec_maintainStart.height = rec_maintainDest.height;

		        
				int[] bt = new int[rec_maintainDest.width 
				                   * rec_maintainDest.height];
				bt = _bi.getRGB(rec_maintainStart.x, rec_maintainStart.y, 
						rec_maintainStart.width, rec_maintainStart.height, 
						bt, 0, rec_maintainStart.width - 1);
				
				_bi.setRGB(rec_maintainDest.x,
						rec_maintainDest.y,
						rec_maintainDest.width, 
						rec_maintainDest.height, 
						bt, 
						0, 
						rec_maintainDest.width - 1);


		        for (int x = rec_new1.x; x < rec_new1.x + rec_new1.width; x++) {
		            for (int y = rec_new1.y;
		            		y < rec_new1.y + rec_new1.height; y++) {
		            	
		            	try {
		            		if ((x + _jlbl_stroke.getLocationOnScreen().x
			            			+ y + _jlbl_stroke.getLocationOnScreen().y) 
			            			% strokeDistance == 0) {

		            			final int rgba = 10;
		            			_bi.setRGB(x, y, 
		            					new Color(rgba, rgba, rgba, 
		            							rgba).getRGB());
			            	} else {

			            		_bi.setRGB(x, y, 
			            				new Color(0, 0, 0, 0).getRGB());
			            	}
		            	} catch (IllegalComponentStateException e) {
		            		
		            		//interrupt
		            		x = rec_new1.width;
		            		y = rec_new1.height;
		            	}
		            }	
		        }
		        for (int x = rec_new2.x; x < rec_new2.x + rec_new2.width; x++) {
		            for (int y = rec_new2.y;
		            		y < rec_new2.y + rec_new2.height; y++) {
		            	
		            	try {
		            		if ((x + _jlbl_stroke.getLocationOnScreen().x
			            			+ y + _jlbl_stroke.getLocationOnScreen().y) 
			            			% strokeDistance == 0) {

		            			final int colorRGB = 10;
		            			_bi.setRGB(x, y, new Color(colorRGB, colorRGB,
		            					colorRGB, colorRGB)
		            			.getRGB());
			            	} else {

			            		_bi.setRGB(x, y, 
			            				new Color(0, 0, 0, 0).getRGB());
			            	}
		            	} catch (IllegalComponentStateException e) {
		            		
		            		//interrupt
		            		x = rec_new2.width;
		            		y = rec_new2.height;
		            	}
		            }	
		        }
		        _jlbl_stroke.setIcon(new ImageIcon(_bi));
		        return _bi;
			}
		}
		return getStroke(_jlbl_stroke);
	}   
	
	
	
	
	
	
	
	
	
	
	
	
	/**
     * Apply stroke on background.
     * @param _jlbl_stroke the background carrying item.
     */
	public static void getStrokeRec(
			final JLabel _jlbl_stroke) {
		
		
		final int strokeDistance = 10;
		if (_jlbl_stroke.getWidth() > 0 
				&& _jlbl_stroke.getHeight() > 0
//				&& jlbl_stroke.isShowing()
				) {
			BufferedImage bi_stroke = new BufferedImage(
					_jlbl_stroke.getWidth(),
	        		_jlbl_stroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y++) {
	            	

//	            	if ( (_addX + x + y +  _addY) % 20 == 0) {
	            	
	            	try {
	            		if ((x + _jlbl_stroke.getLocationOnScreen().x
		            			- y - _jlbl_stroke.getLocationOnScreen().y) 
		            			% strokeDistance == 0
		            			||  (x + _jlbl_stroke.getLocationOnScreen().x
				            			+ y 
				            			+ _jlbl_stroke.getLocationOnScreen().y) 
				            			% strokeDistance == 0) {

	            			final int clr = 10;
		                	bi_stroke.setRGB(x, y, 
		                			new Color(clr, clr, clr, clr).getRGB());
		            	} else {

		            		final int clr = 0;
		                	bi_stroke.setRGB(x, y, 
		                			new Color(clr, clr, clr, clr).getRGB());
		            	}
	            	} catch (IllegalComponentStateException e) {
	            		x = bi_stroke.getWidth();
	            		y = bi_stroke.getHeight();
	            	}
	            
	            }	
	        }
	        _jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
		}
		
	}

	
	/**
     * Apply stroke on background.
     * @param _jlbl_stroke the background carrying item.
     */
	public static void getScrollStroke(final JButton _jlbl_stroke) {
		
		
		final int strokeDistance = 10;
		if (_jlbl_stroke.getWidth() > 0 
				&& _jlbl_stroke.getHeight() > 0
				) {
			BufferedImage bi_stroke = new BufferedImage(
					_jlbl_stroke.getWidth(), 
	        		_jlbl_stroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y++) {
	            	

	            	
	            	try {
	            		if (
	            				(x + _jlbl_stroke.getLocationOnScreen().x
		            			- y - _jlbl_stroke.getLocationOnScreen().y) 
		            			% strokeDistance == 0
//		            			||  (x + jlbl_stroke.getLocationOnScreen().x
//				            		+ y + jlbl_stroke.getLocationOnScreen().y) 
//				            			% strokeDistance == 0
				            			) {

		                	bi_stroke.setRGB(x, y, Color.lightGray.getRGB());
		            	} else {

		            		final int rb = 255;
		            		final int g = 250;
		                	bi_stroke.setRGB(
		                			x, y, new Color(rb, g, rb).getRGB());
		            	}
	            	} catch (IllegalComponentStateException e) {
	            		x = bi_stroke.getWidth();
	            		y = bi_stroke.getHeight();
	            	}
	            
	            }	
	        }
	        _jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
		}
		
	}

    /**
     * Apply stroke on background.
     * @param _jlbl_stroke the background carrying item.
     */
	public static void getRoughStroke(final JLabel _jlbl_stroke) {
		
		
		final int strokeDistance = 20;
		if (_jlbl_stroke.getWidth() > 0 
				&& _jlbl_stroke.getHeight() > 0
//				&& jlbl_stroke.isShowing()
				) {
			BufferedImage bi_stroke = new BufferedImage(
					_jlbl_stroke.getWidth(), 
	        		_jlbl_stroke.getHeight(),
	        		BufferedImage.TYPE_INT_ARGB);
	        
	        for (int x = 0; x < bi_stroke.getWidth(); x++) {

	            for (int y = 0; y < bi_stroke.getHeight(); y++) {
	            	

//	            	if ( (_addX + x + y +  _addY) % 20 == 0) {
	            	if ((x + y) 
	            			% strokeDistance == 0) {

	            		final int r = 40, g = 50, alpha = 90;
	                	bi_stroke.setRGB(x, y,
	                			new Color(r, g, g, alpha).getRGB());
	            	} else {

	                	bi_stroke.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
	            	}
	            }	
	        }
	        _jlbl_stroke.setIcon(new ImageIcon(bi_stroke));
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
                State.getImageSize().width, 
                State.getImageSize().height, BufferedImage.TYPE_INT_ARGB);
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
                State.getImageSize().width, 
                State.getImageSize().height, BufferedImage.TYPE_INT_RGB);
        
        final int max = 255;
        int rgb = new Color(max, max, max).getRGB();
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                bi.setRGB(x, y, rgb);
            }
        }
        
        return bi;
    }
	
	
    
    /**
     * concatenates string array and string.
     * 
     * @param _stringArray	the string array to which the string is 
     * 						added (at the end)
     * @param _stringAdd	the new string.
     * @return				the new string array [_stringArray, _stringAdd]
     */
    public static String [] concatenate(final String[] _stringArray, 
    		final String _stringAdd) {
    
    	
    	// error - catching
    	if (_stringArray == null || _stringAdd == null) {
    		
    		return null;
    	}
    	
    	// create new, empty string array.
    	final String[] newString = new String[_stringArray.length + 1];

    	/*
    	 * concatenates string array and string
    	 */
    	for (int i = 0; i < _stringArray.length; i++) {
			newString[i] = _stringArray[i];
		}
    	newString[_stringArray.length] = _stringAdd;
    	
    	// return string - array.
    	return newString;
    	
    }

    
    

    
}
