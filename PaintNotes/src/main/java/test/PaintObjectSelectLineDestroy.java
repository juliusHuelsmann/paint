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


import java.awt.Color;
import java.awt.Rectangle;

import org.junit.Test;

import model.objects.painting.po.PaintObjectWriting;
import model.objects.pen.normal.Pencil;
import model.util.DPoint;
import junit.framework.TestCase;


/**
 * Class for checking errors in POW destroy selection.
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public class PaintObjectSelectLineDestroy extends TestCase {

	/**
	 * Constants.
	 */
	private static final int C50 = 50, C61 = 61, C52 = 52, C5 = 5, C59 = 59,
			C54 = 54, C60 = 60, C55 = 55, C57 = 57, C56 = 56, C58 = 58, 
			C10 = 10, C4 = 4, C65 = 65, C2 = 2, C3 = 3, C7 = 7, C49 = 49;
	

	/**
	 * Whether an error occurred or not.
	 */
	private static boolean errorOccurred = false;
    
    
	/**
	 * Empty utility class constructor.
	 */
	public PaintObjectSelectLineDestroy() {
		
	}
	
	

    
    /**
     * Main method for testing purpose.
     */
    @Test
	public static void testSelectionDistroy() {
    	errorOccurred = false;
    	
    	//both outside
    	boolean checkBo1_1 = true, checkBo1_2 = true, checkBo1_3 = true, 
    			checkBo1_4 = true,
    			checkBo2_1 = true, checkBo2_2 = true, checkBo2_3 = true, 
    			checkBo2_4 = true;

    	//both inside
    	boolean checkBi = true;
    	
    	//one inside and the other outside
    	boolean checkOIOO = true;
    	
    	
    	/*
    	 * both outside
    	 */
    	if (checkBo1_1) {
    		checkBothOutside1_1();
    	}
    	if (checkBo1_2) {
    		checkBothOutside1_2();
    	}
    	if (checkBo1_3) {
    		checkBothOutside1_3();
    	}
    	if (checkBo1_4) {
    		checkBothOutside1_4();
    	}
    	//second path
    	if (checkBo2_1) {
    		checkBothOutside2_1();
    	}
    	if (checkBo2_2) {
    		checkBothOutside2_2();
    	}
    	if (checkBo2_3) {
    		checkBothOutside2_3();
    	}
    	if (checkBo2_4) {
    		checkBothOutside2_4();
    	}
    	
    	
    	
    	/*
    	 * both inside
    	 */

    	if (checkBi) {
    		checkBothInside();
    	}
    	
    	
    	/*
    	 * one inside
    	 */
    	if (checkOIOO) {
        	checkOneInside1();
    	}
    	
    	checkSpecial();
    	assertTrue(!errorOccurred);
    	
	}    
	
    
    
    /**
     * Create a new PaintObjectWriting for testing purpose which contains
     * two points.
     * @param _p1 the first point
     * @param _p2 the second point.
     * @return the PaintObjectwriting.
     */
    private static PaintObjectWriting createPOW(
    		final DPoint _p1, final DPoint _p2) {
    	
    	//create PaintObjectWriting.
    	PaintObjectWriting pow = new PaintObjectWriting(null, 
    			0, new Pencil(1, 1, Color.black));
    	
    	//insert points.
    	pow.addPoint(_p1);
    	pow.addPoint(_p2);
    	return pow;
    }  
    
    
    /**
     * Create a new PaintObjectWriting for testing purpose which contains
     * four points.
     * @param _p1 the first point
     * @param _p2 the second point.
     * @param _p3 the third point.
     * @param _p4 the fourth point.
     * @return the PaintObjectwriting.
     */
    public static PaintObjectWriting createPOW(
    		final DPoint _p1, final DPoint _p2, 
    		final DPoint _p3, final DPoint _p4) {
    	
    	//create PaintObjectWriting.
    	PaintObjectWriting pow = new PaintObjectWriting(null, 
    			0, new Pencil(1, 1, Color.black));
    	
    	//insert points.
    	pow.addPoint(_p1);
    	pow.addPoint(_p2);
    	pow.addPoint(_p3);
    	pow.addPoint(_p4);
    	return pow;
    }
    
    
   
    
    /**
     * Method for printing the comparison between paintObjects.
     * The result is returned and printed if an error occurred.
     * @param _pow1 the first PaintObject to be compared
     * @param _pow2 the second PaintObject to be compared
     * @return the string which contains detailed comparison
     */
    private static String comparePaintObjects(
    		final PaintObjectWriting[] _pow1, 
    		final PaintObjectWriting[] _pow2) {

    	
    	String s = (_pow1.length + "\t\t\t" + _pow2.length + "\t\t" 
    	+  (_pow1.length == _pow2.length) + "\n");

    	for (int i = 0; i < Math.max(_pow1.length, _pow2.length); i++) {
    		
    		if (i < _pow1.length && i < _pow2.length) {

    			s += compareArrays(_pow1[i].getPoints().toArray(), 
    					_pow2[i].getPoints().toArray());
    		} else if (i < _pow1.length) {

    			DPoint[] dp = new DPoint[0];
    			s += compareArrays(_pow1[i].getPoints().toArray(), 
    					dp);
    		} else if (i < _pow2.length) {

    			DPoint[] dp = new DPoint[0];
    			s += compareArrays(dp, 
    					_pow2[i].getPoints().toArray());
    		}
    	}
    	return s;
    	
    }

    /**
     * Method for printing the comparison between Arrays of DPoints.
     * The result is returned and printed if an error occurred.
     * @param _pow1 the first DPoints - Array to be compared
     * @param _pow2 the second DPoints - Array to be compared
     * @return the string which contains detailed comparison
     */
    private static String compareArrays(
    		final DPoint[] _pow1, 
    		final DPoint[] _pow2) {

    	
    	String s = "";
    	for (int i = 0; i < Math.max(_pow1.length, _pow2.length); i++) {
    		
    		if (i < _pow1.length && i < _pow2.length) {

        		boolean tempErrorOccured = (_pow1[i].getX() == _pow2[i].getX() 
        				&& _pow1[i].getY() == _pow2[i].getY());
        		s += _pow1[i].getX() + " " + _pow1[i].getY() + ";  "
        				+ "\t\t"
        				+ _pow2[i].getX() + " " + _pow2[i].getY() + ";  \t"
        				+  tempErrorOccured + "\n";

        		errorOccurred = !tempErrorOccured || errorOccurred;
    		} else if (i < _pow1.length) {

        		s += _pow1[i].getX() + " " + _pow1[i].getY() + ";  "
        				+ "\t\t\t\t" + "FALSE" + "\n";
                		errorOccurred = true;
    		} else if (i < _pow2.length) {

        		s += "\t\t\t" +  _pow2[i].getX() + " " + _pow2[i].getY() + ";  "
        				+ "\t" + "FALSE" + "\n";
        		errorOccurred = true;
    		}
    	}
    	return s;
    	
    	
    }
    
    
    /**
     * Method for returning the content of an array as a text.
     * @param _arr the array 
     * @return the text
     */
    public static String printArray(final DPoint[] _arr) {
    	
    	String s = "";
    	for (int i = 0; i < _arr.length; i++) {
    		s += _arr[i].getX() + " " + _arr[i].getY() + ";  ";
    	}
    	return s;
    	
    }
    

	
	
	
	/*
	 * Easy stuff that should be correct.
	 * elements both outside and their connections is not crossing the 
	 * rectangle.
	 */
	
	
	 /**
     * Check for top bottom and vice versa.
     */
    private static synchronized void checkBothOutside1_1() {
    	
    	String returnString = checkBothOutside1_1_1();
    	returnString += checkBothOutside1_1_2();
    	returnString += checkBothOutside1_1_3();
    	

    	if (errorOccurred) {
        	System.out.println(returnString);
    	}
    	System.out.println("checkBothOutside1_1 error occured: \t" 
    	+ errorOccurred);
    }
	

    
    /**
     * Check for top bottom and vice versa.
     * 
     * @return the string
     */
	private static synchronized String checkBothOutside1_1_3() {

    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(C60, C56, C5, C5);
    	String returnString = "";

    	returnString += ("\n \n"
    	+ "			          2	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C60, C61, C5, C5);
    	PaintObjectWriting[][] result = cv_bothOutside.getP_bottom2Top()
    			.separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:" + "\n");

    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_bottom2Top()},
    			
    			//result
    			result[0]));
    	
    	

    	returnString += ("\n\n"
    	+ "			          1	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C60, C61, C5, C5);
    	result = cv_bothOutside.getP_top2Bottom().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]) + "");
    	
    	returnString += ("outsidelist:" + "\n");

    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_top2Bottom()},
    			
    			//result
    			result[0]) + "\n");
    	

    	/*
    	 * 3rd scenario
    	 */
    	returnString += ("\n \n"
    	+ "			               2	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C65, C61, C5, C5);
    	result = cv_bothOutside.getP_bottom2Top().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]) + "");
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_bottom2Top()},
    			
    			//result
    			result[0]));
    	
    	

    	returnString += ("\n\n"
    	+ "			               1	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C65, C61, C5, C5);
    	result = cv_bothOutside.getP_top2Bottom().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result 
    			result[1]) + "");
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_top2Bottom()},
    			
    			//result
    			result[0]) + "\n");
    	
    	return returnString;
	}

    
    /**
     * Check for top bottom and vice versa.
     * 
     * @return the string
     */
	private static synchronized String checkBothOutside1_1_2() {

    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(C60, C56, C5, C5);
    	String returnString = "";
    	

    	returnString = ("\n\n"
    	+ "			1	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C55, C61, C5, C5);
    	PaintObjectWriting[][] result = cv_bothOutside.getP_top2Bottom()
    			.separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_top2Bottom()},
    			
    			//result
    			result[0]) + "");
    	
    	
    	
    	
    	
    	
    	
    	

    	/*
    	 * 3rd scenario
    	 */
    	returnString += ("\n \n"
    	+ "			    2	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C50, C61, C5, C5);
    	result = cv_bothOutside.getP_bottom2Top().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]) + "");
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_bottom2Top()},
    			
    			//result
    			result[0]) + "");
    	
    	
    	
    	

    	returnString += ("\n\n"
    	+ "			    1	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C50, C61, C5, C5);
    	result = cv_bothOutside.getP_top2Bottom().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]) + "");
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_top2Bottom()},
    			
    			//result
    			result[0]) + "");
    	

    	return returnString;
	}

    
    /**
     * Check for top bottom and vice versa.
     * 
     * @return the string
     */
    private static synchronized String checkBothOutside1_1_1() {

    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(C60, C56, C5, C5);

    	String returnString = "";
    	/*
    	 * first scenario
    	 */
    	returnString += ("\n \n"
    	+ "			1	_____________\n"
    	+ "			*	|            |\n"
    	+ "			*	|            |\n"
    	+ "			*	|____________|\n"
    	+ "			2 \n");
    	PaintObjectWriting[][] result = cv_bothOutside.getP_top2Bottom()
    			.separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]) + "\n");
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_top2Bottom()},
    			
    			//result
    			result[0]) + "\n");
    			
    	/*
    	 * second scenario
    	 */
    	returnString += ("\n \n"
    	+ "			2	_____________\n"
    	+ "			*	|            |\n"
    	+ "			*	|            |\n"
    	+ "			*	|____________|\n"
    	+ "			1\n");
    	result = cv_bothOutside.getP_bottom2Top().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]) + "");
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_bottom2Top()},
    			
    			//result
    			result[0]) + "");
    	

    	/*
    	 * 3rd scenario
    	 */
    	returnString += ("\n \n"
    	+ "			2	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n" + "\n");
    	r = new Rectangle(C55, C61, C5, C5);
    	result = cv_bothOutside.getP_bottom2Top().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t" + "\n");
    	returnString += ("insideList:" + "\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]) + "");
    	
    	returnString += ("outsidelist:" + "\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_bottom2Top()},
    			
    			//result
    			result[0]) + "");
    	
    	return returnString;
    }
    
    

    /**
     * Check both outside part 1.
     * @return the string.
     */
    private static synchronized String checkBothOutside1_2_2() {


    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(C56, C56, C5, C5);
    	String returnString = "";


    	/*
    	 * 3rd scenario
    	 */
    	returnString += ("\n \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
    	    	+ " 1*****2     |            |\n"
    	    	+ "             |____________|\n\n");
    	r = new Rectangle(C61, C56, C5, C5);
    	PaintObjectWriting[][] result = cv_bothOutside.getP_left2Right()
    			.separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_left2Right()},
    			
    			//result
    			result[0]));

    	returnString += ("\n \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
    	    	+ " 2*****1     |            |\n"
    	    	+ "             |____________|\n\n");
    	r = new Rectangle(C61, C56, C5, C5);
    	result = cv_bothOutside.getP_right2Left().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_right2Left()},
    			
    			//result
    			result[0]));
    	
    	
    	
    	

		/*
		 * 3rd scenario
		 */
		returnString += ("\n \n"
		    	+ "              _____________\n"
		    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "   1*****2   |____________|\n\n");
		r = new Rectangle(C61, C50, C5, C5);
		result = cv_bothOutside.getP_left2Right().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[0], 
				
				//result
				result[1]));
		
		returnString += ("outsidelist:\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{cv_bothOutside.getP_left2Right()},
				
				//result
				result[0]));
		
		
		
		
		
		returnString += ("\n \n"
		    	+ "              _____________\n"
		    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ " 2*****1     |____________|\n\n");
		r = new Rectangle(C61, C50, C5, C5);
		result = cv_bothOutside.getP_right2Left().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[0], 
				
				//result
				result[1]));
		
		returnString += ("outsidelist:\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{cv_bothOutside.getP_right2Left()},
				
				//result
				result[0]));
    	return returnString;
    }
	
    
    /**
     * Check both outside part 1.
     * @return the string.
     */
    private static synchronized String checkBothOutside1_2_1() {

    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(C56, C56, C5, C5);
    	String returnString = "";
    	
    	/*
    	 * first scenario ((C55 to C60)
    	 */
    	returnString += ("\n \n"
    	+ "			1*************************2"
    	+ "			    _____________\n"
    	+ "				|            |\n"
    	+ "				|            |\n"
    	+ "				|____________|\n\n");
    	PaintObjectWriting[][] result = cv_bothOutside.getP_left2Right()
    			.separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_left2Right()},
    			
    			//result
    			result[0]));
    			
    	/*
    	 * second scenario
    	 */
    	returnString += ("\n \n"
    	    	+ "			2*************************1"
    	    	+ "			    _____________\n"
    	    	+ "				|            |\n"
    	    	+ "				|            |\n"
    	    	+ "				|____________|\n\n");
    	result = cv_bothOutside.getP_right2Left().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_right2Left()},
    			
    			//result
    			result[0]));


    	/*
    	 * 3rd scenario
    	 */
    	returnString += ("\n \n"
    	    	+ "  1*****2    _____________\n"
    	    	+ "             |            |\n"
    	    	+ "             |            |\n"
    	    	+ "             |____________|\n\n");
    	r = new Rectangle(C61, C55, C5, C5);
    	result = cv_bothOutside.getP_left2Right().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_left2Right()},
    			
    			//result
    			result[0]));
    	

    	
    	

    	returnString += ("\n \n"
    	    	+ " 2*****1     _____________\n"
    	    	+ "             |            |\n"
    	    	+ "             |            |\n"
    	    	+ "             |____________|\n\n");
    	r = new Rectangle(C61, C55, C5, C5);
    	result = cv_bothOutside.getP_right2Left().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_right2Left()},
    			
    			//result
    			result[0]));
    	
    	

    	return returnString;
    }
	

    /**
     * Check for top bottom and vice versa.
     */
    private static synchronized void checkBothOutside1_2() {
    	
    	String returnString = checkBothOutside1_2_1();
    	returnString += checkBothOutside1_2_2();


    	if (errorOccurred) {
	    	System.out.println(returnString);
    	}
    	System.out.println("checkBothOutside1_2 error occured: \t"
    	+ errorOccurred);
    }
 
    
    
    
    
    
    
    
    
    
    
    
    
    


    /**
     * Check for top bottom and vice versa.
     */
    private static synchronized void checkBothOutside1_3() {
    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(C50, C61, C5, C5);

    	String returnString = "";
    	
    	
    	returnString += ("\n \n"
    	    	+ "                                    1    \n"
    	    	+ "                                 2  \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n\n");
    	PaintObjectWriting[][] result 
    	= cv_bothOutside.getP_mixedLB2RT().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedLB2RT()},
    			
    			//result
    			result[0]));

    	
    	returnString += ("\n \n"
    	    	+ "                                    2    \n"
    	    	+ "                                 1  \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n\n");
    	result = cv_bothOutside.getP_mixedRT2LB().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedRT2LB()},
    			
    			//result
    			result[0]));

    	
    	
    	

    	r = new Rectangle(C55, C61, C5, C5);
    	returnString += ("\n \n"
    	    	+ "                      1                  \n"
    	    	+ "                    2               \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n\n");
    	result = cv_bothOutside.getP_mixedLB2RT().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedLB2RT()},
    			
    			//result
    			result[0]));

    	
    	returnString += ("\n \n"
    	    	+ "                      2                  \n"
    	    	+ "                    1               \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n\n");
    	result = cv_bothOutside.getP_mixedRT2LB().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedRT2LB()},
    			
    			//result
    			result[0]));

    	if (errorOccurred) {
	    	System.out.println(returnString);
    	}
    	System.out.println("checkBothOutside1_3 error occured: \t" 
    	+ errorOccurred);
    }
    
    
    
    



    /**
     * Check for top bottom and vice versa.
     */
    private static synchronized void checkBothOutside1_4() {
    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(C50, C61, C5, C5);

    	String returnString = "";
    	
    	
    	returnString += ("\n \n"
    	    	+ "       1                                \n"
    	    	+ "          2                        \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n\n");
    	PaintObjectWriting[][] result = cv_bothOutside.getP_mixedLT2BR()
    			.separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedLT2BR()},
    			
    			//result
    			result[0]));

    	
    	returnString += ("\n \n"
    	    	+ "       1                                \n"
    	    	+ "          2                         \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n\n");
    	result = cv_bothOutside.getP_mixedBR2LT().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedBR2LT()},
    			
    			//result
    			result[0]));

    	
    	
    	

    	r = new Rectangle(C61, C55, C10, C10);
    	returnString += ("\n \n"
    	    	+ "             _____________\n"
    	    	+ "      1      |            |\n"
		    	+ "          2  |            |\n"
		    	+ "             |____________|\n\n");
    	result = cv_bothOutside.getP_mixedLT2BR().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedLT2BR()},
    			
    			//result
    			result[0]));

    	
    	returnString += ("\n \n"
    	    	+ "             _____________\n"
    	    	+ "       2     |            |\n"
		    	+ "          1  |            |\n"
		    	+ "             |____________|\n\n");
    	result = cv_bothOutside.getP_mixedBR2LT().separate(r);
    	returnString += ("EXPECTED\t\tRESULT\t\t\n");
    	returnString += ("insideList:\n");
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	returnString += ("outsidelist:\n");
    	
    	returnString += (comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.getP_mixedBR2LT()},
    			
    			//result
    			result[0]));

    	if (errorOccurred) {
	    	System.out.println(returnString);
    	}
    	System.out.println("checkBothOutside1_4 error occured: \t" 
    	+ errorOccurred);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    

	 /**
   * Check for top bottom and vice versa.
   */
  private static synchronized void checkBothOutside2_1() {
  	CheckEnvironment 
  	cv_bothOutside = new CheckEnvironment();
  	Rectangle r;

	String returnString = "";
	
  	
  	returnString += ("\n \n"
  	+ "			2	\n"
  	+ "			*	\n"
  	+ "			*	\n"
  	+ "			*__________\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			*	\n"
  	+ "			1 \n\n");
  	r = new Rectangle(C55, C56, C5, C2);
  	PaintObjectWriting[][] result = cv_bothOutside.getP_bottom2Top()
  			.separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58), 
  							
  							//intersection at top of rectangle.
  							new DPoint(C55, C56))
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(C55, C60), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58)),

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C56),
  		   							
  		   					//point 2
  		   					new DPoint(C55, C55))
  			}, 
  			//result
  			result[0]));
  	
  	
  	

  	returnString += ("\n \n"
  	+ "			1	\n"
  	+ "			*	\n"
  	+ "			*	\n"
  	+ "			*__________\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			*	\n"
  	+ "			2 \n\n");
  	r = new Rectangle(C55, C56, C5, C2);
  	result = cv_bothOutside.getP_top2Bottom().separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  							//intersection at top of rectangle.
  							new DPoint(C55, C56),
  		   					
  							//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58))
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(

  		   					//point 1
  		   					new DPoint(C55, C55),
  		   					
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C56)),
  		   							
  		   					
  		   			//first line: from first point to first intersection (
  		   			//the one at the bottom of the rectangle)
  		   			createPOW(

  		   		   			//intersection at the bottom of the rectangle.
  		   					new DPoint(C55, C58),

  		   		   			//point 2.
  		   					new DPoint(C55, C60))
  			}, 
  			//result
  			result[0]));
  	

  	
  	
  	
  	
  	
  	

  	
  	returnString += ("\n \n"
  	+ "			          2	\n"
  	+ "		              *	\n"
  	+ "			          *	\n"
  	+ "			__________*\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			          *	\n"
  	+ "			          1 \n\n");
  	r = new Rectangle(C50, C56, C5, C2);
  	result = cv_bothOutside.getP_bottom2Top().separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58), 
  							
  							//intersection at top of rectangle.
  							new DPoint(C55, C56))
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(C55, C60), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58)),

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C56),
  		   							
  		   					//point 2
  		   					new DPoint(C55, C55))
  			}, 
  			//result
  			result[0]));
  	
  	
  	

  	returnString += ("\n \n"
  	+ "			          1	\n"
  	+ "			          *	\n"
  	+ "			          *	\n"
  	+ "			__________*\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			          *	\n"
  	+ "			          2 \n\n");
  	r = new Rectangle(C50, C56, C5, C2);
  	result = cv_bothOutside.getP_top2Bottom().separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  							//intersection at top of rectangle.
  							new DPoint(C55, C56),
  		   					
  							//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58))
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(

  		   					//point 1
  		   					new DPoint(C55, C55),
  		   					
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C56)),
  		   							
  		   					
  		   			//first line: from first point to first intersection (
  		   			//the one at the bottom of the rectangle)
  		   			createPOW(

  		   		   			//intersection at the bottom of the rectangle.
  		   					new DPoint(C55, C58),

  		   		   			//point 2.
  		   					new DPoint(C55, C60))
  			}, 
  			//result
  			result[0]));
  	


  	
  	returnString += ("\n \n"
  	+ "			     2	\n"
  	+ "	             *	\n"
  	+ "			     *	\n"
  	+ "			_____*____\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			     *	\n"
  	+ "			     1 \n\n");
  	r = new Rectangle(C52, C56, C5, C2);
  	result = cv_bothOutside.getP_bottom2Top().separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58), 
  							
  							//intersection at top of rectangle.
  							new DPoint(C55, C56))
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(C55, C60), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58)),

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C56),
  		   							
  		   					//point 2
  		   					new DPoint(C55, C55))
  			}, 
  			//result
  			result[0]));
  	
  	
  	

  	returnString += ("\n \n"
  	+ "			    1	\n"
  	+ "			    *	\n"
  	+ "			    *	\n"
  	+ "			____*_____\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			    *	\n"
  	+ "			    2 \n\n");
  	r = new Rectangle(C52, C56, C5, C2);
  	result = cv_bothOutside.getP_top2Bottom().separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  							//intersection at top of rectangle.
  							new DPoint(C55, C56),
  		   					
  							//intersection at the bottom of the rectangle.
  							new DPoint(C55, C58))
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(

  		   					//point 1
  		   					new DPoint(C55, C55),
  		   					
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C56)),
  		   							
  		   					
  		   			//first line: from first point to first intersection (
  		   			//the one at the bottom of the rectangle)
  		   			createPOW(

  		   		   			//intersection at the bottom of the rectangle.
  		   					new DPoint(C55, C58),

  		   		   			//point 2.
  		   					new DPoint(C55, C60))
  			}, 
  			//result
  			result[0]));
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	

  	returnString += ("\n \n"
  	+ "			     	\n"
  	+ "	             	\n"
  	+ "			     2	\n"
  	+ "			__________\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			     *	\n"
  	+ "			     1 \n\n");
  	r = new Rectangle(C50, C50, C10, C10);
  	PaintObjectWriting pow = new PaintObjectWriting(null, 0, 
  			new Pencil(1, 1, Color.black));
  	pow.addPoint(new DPoint(C55, C65));
  	pow.addPoint(new DPoint(C55, C49));
  	result = pow.separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C60), 
  							
  							//intersection at top of rectangle.
  							new DPoint(C55, C50))
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(C55, C65), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C60)),

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C50),
  		   							
  		   					//point 2
  		   					new DPoint(C55, C49))
  			}, 
  			//result
  			result[0]));
  	
  	

  	returnString += ("\n \n"
  	+ "			     	\n"
  	+ "	             	\n"
  	+ "			     2	\n"
  	+ "			__________\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			     *	\n"
  	+ "			     1 \n\n");
  	r = new Rectangle(C50, C50, C10, C10);
  	pow = new PaintObjectWriting(null, 0, new Pencil(1, 1, Color.black));
  	pow.addPoint(new DPoint(C55, C49));
  	pow.addPoint(new DPoint(C55, C65));
  	result = pow.separate(r);
  	returnString += ("EXPECTED\t\tRESULT\t\t\n");
  	returnString += ("insideList:\n");
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  							//intersection at top of rectangle.
  							new DPoint(C55, C50),
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C60))
  							
  			}, 
  			
  			//result
  			result[1]));
  	
  	returnString += ("outsidelist:\n");
  	
  	returnString += (comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(C55, C49),
  		   							
  		   					//point 2
  		   					new DPoint(C55, C50)),
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(C55, C60), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(C55, C65))

  			}, 
  			//result
  			result[0]));  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
  	
	
		if (errorOccurred) {
	    	System.out.println(returnString);
		}
		System.out.println("checkBothOutside2_1 error occured: \t" 
		+ errorOccurred);
  }

  

/**
 * Check for top bottom and vice versa.
 */
private static synchronized void checkBothOutside2_2() {
	CheckEnvironment 
	cv_bothOutside = new CheckEnvironment();
	Rectangle r;

	String returnString = "";
	

	returnString += ("\n \n"
	+ "2 * * * *__________* * * 1\n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "         |_________|\n"
	+ "         *	\n\n");
	r = new Rectangle(C57, C55, 2, C5);
	PaintObjectWriting[][] result = cv_bothOutside.getP_right2Left()
			.separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C59, C55), 
							
							new DPoint(C57, C55))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C55), 
							
		   					//intersection 
							new DPoint(C59, C55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C55),
		   							
		   					//point 2
		   					new DPoint(C55, C55))
			}, 
			//result
			result[0]));
	

	returnString += ("\n \n"
	+ "1 * * * *__________* * * 2\n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "         |_________|\n"
	+ "         *	\n\n");
	r = new Rectangle(C57, C55, 2, C5);
	result = cv_bothOutside.getP_left2Right().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C55), 
							
							new DPoint(C59, C55))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C55), 
							
		   					//intersection 
							new DPoint(C57, C55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C59, C55),
		   							
		   					//point 2
		   					new DPoint(C60, C55))
			}, 
			//result
			result[0]));
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	


	returnString += ("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "2 * * * *|_________|* * * 1\n" 
	+ "         *	\n\n");
	r = new Rectangle(C57, C50, 2, C5);
	result = cv_bothOutside.getP_right2Left().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C59, C55), 
							
							new DPoint(C57, C55))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C55), 
							
		   					//intersection 
							new DPoint(C59, C55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C55),
		   							
		   					//point 2
		   					new DPoint(C55, C55))
			}, 
			//result
			result[0]));
	

	returnString += ("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "1 * * * *|_________|* * * 2\n" 
	+ "         *	\n\n");
	r = new Rectangle(C57, C50, 2, C5);
	result = cv_bothOutside.getP_left2Right().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C55), 
							
							new DPoint(C59, C55))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C55), 
							
		   					//intersection 
							new DPoint(C57, C55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C59, C55),
		   							
		   					//point 2
		   					new DPoint(C60, C55))
			}, 
			//result
			result[0]));
	
	
	
	
	
	
	
	
	
	
	
	

	


	returnString += ("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "2 * * * *|         |* * * 1\n"
	+ "         |_________|\n" 
	+ "         *	\n\n");
	r = new Rectangle(C57, C52, 2, C5);
	result = cv_bothOutside.getP_right2Left().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C59, C55), 
							
							new DPoint(C57, C55))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C55), 
							
		   					//intersection 
							new DPoint(C59, C55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C55),
		   							
		   					//point 2
		   					new DPoint(C55, C55))
			}, 
			//result
			result[0]));
	

	returnString += ("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "1 * * * *|         |* * * 2\n"
	+ "         |_________|\n" 
	+ "         *	\n\n");
	r = new Rectangle(C57, C52, 2, C5);
	result = cv_bothOutside.getP_left2Right().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C55), 
							
							new DPoint(C59, C55))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C55), 
							
		   					//intersection 
							new DPoint(C57, C55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C59, C55),
		   							
		   					//point 2
		   					new DPoint(C60, C55))
			}, 
			//result
			result[0]));
	
	
	
	

	if (errorOccurred) {
    	System.out.println(returnString);
	}
	System.out.println("checkBothOutside2_2 error occured: \t" 
	+ errorOccurred);
	}



/**
 * Check method for both vectors outside.
 */
private static synchronized void checkBothOutside2_3() {
	CheckEnvironment 
	cv_bothOutside = new CheckEnvironment();
	Rectangle r;
	

	String returnString = "";
	

	returnString += ("\n \n"
	    	+ "                                    1    \n"
	    	+ "                                 *  \n"
	    	+ "             _____________  *\n"
	    	+ "             |            |\n"
	    	+ "             |            |\n"
	    	+ "             |____________|\n"
	    	+ "           *\n"
	    	+ "        *\n"
	    	+ "      2\n\n");
	r = new Rectangle(C56, C56, 2, 2);
	PaintObjectWriting[][] result = cv_bothOutside.getP_mixedRT2LB()
			.separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C58, C57), 
							
							new DPoint(C57, C58))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C55), 
							
		   					//intersection 
							new DPoint(C58, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C58),
		   							
		   					//point 2
		   					new DPoint(C55, C60))
			}, 
			//result
			result[0]));

	returnString += ("\n \n"
	    	+ "                                    2    \n"
	    	+ "                                 *  \n"
	    	+ "             _____________  *\n"
	    	+ "             |            |\n"
	    	+ "             |            |\n"
	    	+ "             |____________|\n"
	    	+ "           *\n"
	    	+ "        *\n"
	    	+ "      1\n\n");
	r = new Rectangle(C56, C56, 2, 2);
	result = cv_bothOutside.getP_mixedLB2RT().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C58), 
							
							new DPoint(C58, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C60), 
							
		   					//intersection 
							new DPoint(C57, C58)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C58, C57),
		   							
		   					//point 2
		   					new DPoint(C60, C55))
			}, 
			//result
			result[0]));
	
	
	
	
	
	
	
	
	
	
	
	

	
	

	returnString += ("\n \n"
	    	+ "                    1    \n"
	    	+ "                 *  \n"
	    	+ "             *____________\n"
	    	+ "          *  |            |\n"
	    	+ "        2    |            |\n"
	    	+ "             |____________|\n\n");
	
	r = new Rectangle(C58, C57, C10, C10);
	result = cv_bothOutside.getP_mixedRT2LB().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							//here we've got the point of intersection twice
							new DPoint(C58, C57), 
							new DPoint(C58, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C55), 
							
		   					//intersection 
							new DPoint(C58, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C58, C57),
		   							
		   					//point 2
		   					new DPoint(C55, C60))
			}, 
			//result
			result[0]));
	
	

	
	

	returnString += ("\n \n"
	    	+ "                    2    \n"
	    	+ "                 *  \n"
	    	+ "             *____________\n"
	    	+ "          *  |            |\n"
	    	+ "        1    |            |\n"
	    	+ "             |____________|\n\n");
	
	r = new Rectangle(C58, C57, C10, C10);
	result = cv_bothOutside.getP_mixedLB2RT().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							//here we've got the point of intersection twice
							new DPoint(C58, C57), 
							new DPoint(C58, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C60), 
							
		   					//intersection 
							new DPoint(C58, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C58, C57),
		   							
		   					//point 2
		   					new DPoint(C60, C55))
			}, 
			//result
			result[0]));


	
	
	
	
	
	
	
	
	

	
	

	returnString += ("\n \n"
	    	+ "                        \n"
	    	+ "                   \n"
	    	+ "              ____________\n"
	    	+ "             |            |    1\n"
	    	+ "             |            |  *\n"
	    	+ "             |____________* \n"
	    	+ "                       *\n"
	    	+ "                    2\n"
	    	+ "\n");
	
	r = new Rectangle(C55, C55, C3, C2);
	result = cv_bothOutside.getP_mixedRT2LB().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							//here we've got the point of intersection twice
							new DPoint(C58, C57), 
							new DPoint(C58, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C55), 
							
		   					//intersection 
							new DPoint(C58, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C58, C57),
		   							
		   					//point 2
		   					new DPoint(C55, C60))
			}, 
			//result
			result[0]));
	
	

	
	

	returnString += ("\n \n"
	    	+ "                        \n"
	    	+ "                   \n"
	    	+ "              ____________\n"
	    	+ "             |            |    2\n"
	    	+ "             |            |  *\n"
	    	+ "             |____________* \n"
	    	+ "                       *\n"
	    	+ "                    1\n"
	    	+ "\n");

	r = new Rectangle(C55, C55, C3, C2);
	result = cv_bothOutside.getP_mixedRT2LB().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							//here we've got the point of intersection twice
							new DPoint(C58, C57), 
							new DPoint(C58, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C55), 
							
		   					//intersection 
							new DPoint(C58, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C58, C57),
		   							
		   					//point 2
		   					new DPoint(C55, C60))
			}, 
			//result
			result[0]));
	
	
	
	

	if (errorOccurred) {
    	System.out.println(returnString);
	}
	System.out.println("checkBothOutside2_3 error occured: \t" 
	+ errorOccurred);
	}







/**
 */
private static synchronized void checkBothOutside2_4() {
	CheckEnvironment 
	cv_bothOutside = new CheckEnvironment();
	Rectangle r;
	
	
	String returnString = "";


	returnString += ("\n \n"
	    	+ "     1                     \n"
	    	+ "         *                 \n"
	    	+ "            *_____________ \n"
	    	+ "             |            |\n"
	    	+ "             |            |\n"
	    	+ "             |____________|\n"
	    	+ "                           *\n"
	    	+ "                                *\n"
	    	+ "                                   2\n\n");
	r = new Rectangle(C56, C56, 2, 2);
	PaintObjectWriting[][] result = cv_bothOutside.getP_mixedLT2BR()
			.separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C56, C56), 
							//found first bug, solved by now
							new DPoint(C58, C58))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C55), 
							
		   					//intersection 
							new DPoint(C56, C56)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C58, C58),
		   							
		   					//point 2
		   					new DPoint(C60, C60))
			}, 
			//result
			result[0]));
	
	


	returnString += ("\n \n"
	    	+ "     2                     \n"
	    	+ "         *                 \n"
	    	+ "            *_____________ \n"
	    	+ "             |            |\n"
	    	+ "             |            |\n"
	    	+ "             |____________|\n"
	    	+ "                           *\n"
	    	+ "                                *\n"
	    	+ "                                   1\n\n");
	r = new Rectangle(C56, C56, 2, 2);
	result = cv_bothOutside.getP_mixedBR2LT().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C58, C58), 
							new DPoint(C56, C56))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C60), 
							
		   					//intersection 
							new DPoint(C58, C58)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C56, C56),
		   							
		   					//point 2
		   					new DPoint(C55, C55))
			}, 
			//result
			result[0]));
	

	returnString += ("\n \n"
	    	+ "             _____________ \n"
	    	+ "     1       |            |\n"
	    	+ "         *   |            |\n"
	    	+ "             *____________|\n"
	    	+ "                 *         \n"
	    	+ "                     2     \n");
	r = new Rectangle(C57, C55, 2, 2);
	result = cv_bothOutside.getP_mixedLT2BR().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C57), 
							//found first bug, solved by now
							new DPoint(C57, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C55), 
							
		   					//intersection 
							new DPoint(C57, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C57),
		   							
		   					//point 2
		   					new DPoint(C60, C60))
			}, 
			//result
			result[0]));
	

	returnString += ("\n \n"
	    	+ "             _____________ \n"
	    	+ "     2       |            |\n"
	    	+ "         *   |            |\n"
	    	+ "             *____________|\n"
	    	+ "                 *         \n"
	    	+ "                     1     \n");
	r = new Rectangle(C57, C55, 2, 2);
	result = cv_bothOutside.getP_mixedBR2LT().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C57), 
							//found first bug, solved by now
							new DPoint(C57, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C60), 
							
		   					//intersection 
							new DPoint(C57, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C57),
		   							
		   					//point 2
		   					new DPoint(C55, C55))
			}, 
			//result
			result[0]));
	
	
	
	
	
	
	
	
	

	returnString += ("\n \n"
	    	+ "                               \n"
	    	+ "                      1          \n"
	    	+ "             _____________*     \n"
	    	+ "             |            |  2\n"
	    	+ "             |            |     \n"
	    	+ "             |____________|  \n"
	    	+ "                                \n"
	    	+ "                           \n");
	r = new Rectangle(C55, C57, 2, 2);
	result = cv_bothOutside.getP_mixedLT2BR().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C57), 
							//found first bug, solved by now
							new DPoint(C57, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C55, C55), 
							
		   					//intersection 
							new DPoint(C57, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C57),
		   							
		   					//point 2
		   					new DPoint(C60, C60))
			}, 
			//result
			result[0]));
	
	

	returnString += ("\n \n"
	    	+ "                               \n"
	    	+ "                      2          \n"
	    	+ "             _____________*     \n"
	    	+ "             |            |  1\n"
	    	+ "             |            |     \n"
	    	+ "             |____________|  \n"
	    	+ "                                \n"
	    	+ "                           \n");
	r = new Rectangle(C55, C57, 2, 2);
	result = cv_bothOutside.getP_mixedBR2LT().separate(r);
	returnString += ("EXPECTED\t\tRESULT\t\t\n");
	returnString += ("insideList:\n");
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(C57, C57), 
							//found first bug, solved by now
							new DPoint(C57, C57))
			}, 
			
			//result
			result[1]));
	
	returnString += ("outsidelist:\n");
	
	returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(C60, C60), 
							
		   					//intersection 
							new DPoint(C57, C57)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(C57, C57),
		   							
		   					//point 2
		   					new DPoint(C55, C55))
			}, 
			//result
			result[0]));

		if (errorOccurred) {
	    	System.out.println(returnString);
		}
		System.out.println("checkBothOutside2_4 error occured: \t" 
				+ errorOccurred);
	}




























	/**
	 * Check for top bottom and vice versa.
	 */
	private static synchronized void checkBothInside() {
		CheckEnvironment 
		cv_bothOutside = new CheckEnvironment();
		Rectangle r = new Rectangle(C55, C55, C10, C5);

		String returnString = "";

		returnString += ("\n \n"
		+ "		_______1_____\n"
		+ "		|      *     |\n"
		+ "		|      *     |\n"
		+ "		|______2_____|\n"
		+ "	 \n");
		PaintObjectWriting[][] result = cv_bothOutside.getP_top2Bottom()
				.separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{cv_bothOutside.getP_top2Bottom()},
			
			//result
			result[1]) + "\n");
	
		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[0], 
				
				//result
				result[0]) + "\n");
		
		
		
		returnString += ("\n \n"
		+ "		_______2_____\n"
		+ "		|      *     |\n"
		+ "		|      *     |\n"
		+ "		|______1_____|\n"
		+ "	 \n");
		result = cv_bothOutside.getP_bottom2Top().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
	
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{cv_bothOutside.getP_bottom2Top()},
				
				//result
				result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[0], 
			
			//result
			result[0]) + "\n");
		
		returnString += ("\n \n"
		+ "		1____________\n"
		+ "		|   *       |\n"
		+ "		|       *   |\n"
		+ "		|___________2\n"
		+ "	 \n");
		r = new Rectangle(C55, C55, C5, C5);
		result = cv_bothOutside.getP_mixedLT2BR().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{cv_bothOutside.getP_mixedLT2BR()},
			
			//result
			result[1]) + "\n");
	
		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[0], 
				
				//result
				result[0]) + "\n");
		
		

		returnString += ("\n \n"
		+ "		2____________\n"
		+ "		|   *       |\n"
		+ "		|       *   |\n"
		+ "		|___________1\n"
		+ "	 \n");
		result = cv_bothOutside.getP_mixedBR2LT().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
	
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{cv_bothOutside.getP_mixedBR2LT()},
				
				//result
				result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[0], 
			
			//result
			result[0]) + "\n");
		
		returnString += ("\n \n"
				+ "		____________1\n"
				+ "		|        *  |\n"
				+ "		|   *       |\n"
				+ "		2___________1\n"
				+ "	 \n");
		r = new Rectangle(C55, C55, C5, C5);
		result = cv_bothOutside.getP_mixedRT2LB().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{cv_bothOutside.getP_mixedRT2LB()},
			
			//result
			result[1]) + "\n");
	
		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[0], 
				
				//result
				result[0]) + "\n");
		
		

		returnString += ("\n \n"
		+ "		____________2\n"
		+ "		|        *  |\n"
		+ "		|   *       |\n"
		+ "		1___________1\n"
		+ "	 \n");
		result = cv_bothOutside.getP_mixedLB2RT().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
	
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{cv_bothOutside.getP_mixedLB2RT()},
				
				//result
				result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[0], 
			
			//result
			result[0]) + "\n");
		

		if (errorOccurred) {
				System.out.println(returnString);
		}
		System.out.println("checkBothInside1 error occured: \t" 
				+ errorOccurred);
	}





	/**
	 * Check for top bottom and vice versa.
	 */
	private static synchronized void checkOneInside1() {
		CheckEnvironment 
		cv_bothOutside = new CheckEnvironment();
		Rectangle r = new Rectangle(C50, C57, C10, C5);

		String returnString = "";

		returnString += ("\n \n"
				+ "            1\n"
				+ "     _______*_____\n"
				+ "     |      *     |\n"
				+ "     |      2     |\n"
				+ "     |____________|\n"
				+ "	 \n");
		PaintObjectWriting[][] result = cv_bothOutside.getP_top2Bottom()
				.separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
			
					createPOW(
							//intersection
							new DPoint(C55, C57), 
							
							//2nd point
							new DPoint(C55, C60))
			},
			
			//result
			result[1]) + "\n");
	
		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected//expected
				new PaintObjectWriting[]{
						
						createPOW(
								//intersection
								new DPoint(C55, C55), 
								
								//2nd point
								new DPoint(C55, C57))
				}, 
				
				//result
				result[0]) + "\n");

		returnString += ("\n \n"
				+ "            2\n"
				+ "     _______*_____\n"
				+ "     |      *     |\n"
				+ "     |      1     |\n"
				+ "     |____________|\n"
				+ "	 \n");
		result = cv_bothOutside.getP_bottom2Top().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
	
		returnString += (comparePaintObjects(

				//expected
				new PaintObjectWriting[]{
				
						createPOW(
								//1st point
								new DPoint(C55, C60),
								
								//intersection
								new DPoint(C55, C57)) 
								
				},
				
				//result
				result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		returnString += (comparePaintObjects(

				//expected//expected
				new PaintObjectWriting[]{
						
						createPOW(

								//2nd point
								new DPoint(C55, C57),
								
								//2nd point
								new DPoint(C55, C55))
				}, 
				
				//result
				result[0]) + "\n");
		
		
		

		
		
		
		
		
		

		returnString += ("\n \n"
		+ "  1\n"
		+ "	    *____________\n"
		+ "	    |   *       |\n"
		+ "	    |        2  |\n"
		+ "	    |___________|\n"
		+ "	 \n");
		r = new Rectangle(C57, C57, C4, C4);
		result = cv_bothOutside.getP_mixedLT2BR().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{

					createPOW(
							//intersection
							new DPoint(C57, C57), 
							
							//second point
							new DPoint(C60, C60))
			},
			
			//result
			result[1]) + "\n");
	
		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{
						createPOW(new DPoint(C55, C55), new DPoint(C57, C57))
				}, 
				
				//result
				result[0]) + "\n");

		returnString += ("\n \n"
		+ "  2\n"
		+ "	    *____________\n"
		+ "	    |   *       |\n"
		+ "	    |        1  |\n"
		+ "	    |___________|\n"
		+ "	 \n");
		r = new Rectangle(C57, C57, C4, C4);
		result = cv_bothOutside.getP_mixedBR2LT().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
	
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[] {

						createPOW(

								//1st point
								new DPoint(C60, C60),
								
								//intersection
								new DPoint(C57, C57)) 
								
				},
				
				//result
				result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[] {

					createPOW(new DPoint(C57, C57), new DPoint(C55, C55))
			}, 
			
			//result
			result[0]) + "\n");
		
		
		

		
		
		
		//einer draussen, es geht durch alle ecken.
		//TODO: einer draussen, es geht durch eine ecke aber der andere
		//ligt auf ihr drauf unten.
		returnString += ("\n \n"
				+ "		              1\n"
				+ "		____________*\n"
				+ "		|        *  |\n"
				+ "		|   *       |\n"
				+ "		2___________1\n"
				+ "	 \n");
		r = new Rectangle(C54, C56, C5, C4);
		result = cv_bothOutside.getP_mixedRT2LB().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[] {
					createPOW(new DPoint(C59, C56),
							new DPoint(C55, C60))
			
			},
			
			//result
			result[1]) + "\n");
	
		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[] {
						createPOW(new DPoint(C60, C55),
								new DPoint(C59, C56))
				
				},
				
				//result
				result[0]) + "\n");
		
		

		returnString += ("\n \n"
				+ "		              2\n"
				+ "		____________*\n"
				+ "		|        *  |\n"
				+ "		|   *       |\n"
				+ "		1___________1\n"
				+ "	 \n");
		r = new Rectangle(C54, C56, C5, C4);
		result = cv_bothOutside.getP_mixedLB2RT().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[] {
					createPOW(
							new DPoint(C55, C60),
							new DPoint(C59, C56))
			
			},
			
			//result
			result[1]) + "\n");
	
		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[] {
						createPOW(
								new DPoint(C59, C56),
								new DPoint(C60, C55))
				
				},
				
				//result
				result[0]) + "\n");
		

		

		if (errorOccurred) {
				System.out.println(returnString);
		}
		System.out.println("check1Inside error occured: \t\t" 
				+ errorOccurred);
	}
	
	
	
	
	

	/**
	 * Check for top bottom and vice versa.
	 */
	private static synchronized void checkSpecial() {
		CheckEnvironment 
		cv_bothOutside = new CheckEnvironment();
		Rectangle r = new Rectangle(C50, C57, C10, C5);

		String returnString = "";


		
		returnString += ("\n \n"
				+ "     1\n"
				+ "     *____________\n"
				+ "     *            |\n"
				+ "     2            |\n"
				+ "     |____________|\n"
				+ "	 \n");
		 r = new Rectangle(C55, C57, C5, C4);
		PaintObjectWriting[][] result = cv_bothOutside.getP_top2Bottom()
				.separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
			
					createPOW(
							//intersection
							new DPoint(C55, C57), 
							
							//2nd point
							new DPoint(C55, C60))
			},
			
			//result
			result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[] {
						createPOW(
								new DPoint(C55, C55),
								new DPoint(C55, C57))
				
				},
				
				//result
				result[0]) + "\n");
		
		

		
		returnString += ("\n \n"
				+ "     2\n"
				+ "     *____________\n"
				+ "     *            |\n"
				+ "     1            |\n"
				+ "     |____________|\n"
				+ "	 \n");
		 r = new Rectangle(C55, C57, C5, C4);
		result = cv_bothOutside.getP_bottom2Top().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
			
					createPOW(
							//intersection
							new DPoint(C55, C60), 
							
							//2nd point
							new DPoint(C55, C57))
			},
			
			//result
			result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[] {
						createPOW(
								new DPoint(C55, C57),
								new DPoint(C55, C55))
				
				},
				
				//result
				result[0]) + "\n");
		

		returnString += ("\n \n"
				+ "     \n"
				+ "     _____________\n"
				+ "     |            |\n"
				+ "     1            |\n"
				+ "     *____________|\n"
				+ "     *\n"
				+ "     2\n"
				+ "	 \n");
		 r = new Rectangle(C54, C50, C3, C7);
		result = cv_bothOutside.getP_top2Bottom().separate(r);
		returnString += ("EXPECTED\t\tRESULT\t\t\n");
		returnString += ("insideList:\n");
		returnString += (comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
			
					createPOW(
							//intersection
							new DPoint(C55, C55), 
							
							//2nd point
							new DPoint(C55, C57))
			},
			
			//result
			result[1]) + "\n");

		returnString += ("outsidelist:" + "\n");
		
		returnString += (comparePaintObjects(
				
				//expected
				new PaintObjectWriting[] {
						createPOW(
								new DPoint(C55, C57),
								new DPoint(C55, C60))
				
				},
				
				//result
				result[0]) + "\n");
		
		if (errorOccurred) {
				System.out.println(returnString);
		}
		System.out.println("check error occured: \t\t\t" 
				+ errorOccurred);
		
	}
}





/**
 * Check environment class contains the possible point compositions for
 * testing the separation method.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
class CheckEnvironment {
	
	/**
	 * Constants.
	 */
	private static final int C60 = 60, C55 = 55;
	/**
	 * The possible combinations of paintObjects.
	 * 	
	 * 
	 */
	private final PaintObjectWriting
	
	//single direction
	p_left2Right, p_right2Left, 
	p_top2Bottom, p_bottom2Top,
	
	//mixed directions
	p_mixedLT2BR, p_mixedBR2LT, 
	p_mixedLB2RT, p_mixedRT2LB;
	
	
	/**
	 * The constructor.
	 */
	public CheckEnvironment() {


		//simple direction.
    	p_left2Right = new PaintObjectWriting(
    			null, 0, new Pencil(1, 1, Color.black));

    	getP_left2Right().addPoint(new DPoint(C55 , C55));
    	getP_left2Right().addPoint(new DPoint(C60 , C55));
    	
    	p_right2Left = new PaintObjectWriting(
    			null, 0, new Pencil(1, 1, Color.black));
    	getP_right2Left().addPoint(new DPoint(C60 , C55));
    	getP_right2Left().addPoint(new DPoint(C55 , C55));

    	p_top2Bottom = new PaintObjectWriting(
    			null, 1, new Pencil(1, 1, Color.black));
    	getP_top2Bottom().addPoint(new DPoint(C55 , C55));
    	getP_top2Bottom().addPoint(new DPoint(C55 , C60));
    	
    	p_bottom2Top = new PaintObjectWriting(
    			null, 1, new Pencil(1, 1, Color.black));
    	p_bottom2Top.addPoint(new DPoint(C55 , C60));
    	p_bottom2Top.addPoint(new DPoint(C55 , C55));
    	

    	//mixed directions.
    	p_mixedLT2BR = new PaintObjectWriting(
    			null, 0, new Pencil(1, 1, Color.black));
    	getP_mixedLT2BR().addPoint(new DPoint(C55 , C55));
    	getP_mixedLT2BR().addPoint(new DPoint(C60 , C60));
    	
    	p_mixedBR2LT = new PaintObjectWriting(
    			null, 0, new Pencil(1, 1, Color.black));
    	getP_mixedBR2LT().addPoint(new DPoint(C60 , C60));
    	getP_mixedBR2LT().addPoint(new DPoint(C55 , C55));

    	p_mixedLB2RT = new PaintObjectWriting(
    			null, 1, new Pencil(1, 1, Color.black));
    	getP_mixedLB2RT().addPoint(new DPoint(C55 , C60));
    	getP_mixedLB2RT().addPoint(new DPoint(C60 , C55));
    	
    	p_mixedRT2LB = new PaintObjectWriting(
    			null, 1, new Pencil(1, 1, Color.black));
    	getP_mixedRT2LB().addPoint(new DPoint(C60 , C55));
    	getP_mixedRT2LB().addPoint(new DPoint(C55 , C60));
	}


	/**
	 * @return the p_top2Bottom
	 */
	public PaintObjectWriting getP_top2Bottom() {
		return p_top2Bottom;
	}


	/**
	 * @return the p_left2Right
	 */
	public PaintObjectWriting getP_left2Right() {
		return p_left2Right;
	}


	/**
	 * @return the p_mixedRT2LB
	 */
	public PaintObjectWriting getP_mixedRT2LB() {
		return p_mixedRT2LB;
	}


	/**
	 * @return the p_bottom2Top
	 */
	public PaintObjectWriting getP_bottom2Top() {
		return p_bottom2Top;
	}


	/**
	 * @return the p_right2Left
	 */
	public PaintObjectWriting getP_right2Left() {
		return p_right2Left;
	}


	/**
	 * @return the p_mixedLB2RT
	 */
	public PaintObjectWriting getP_mixedLB2RT() {
		return p_mixedLB2RT;
	}


	/**
	 * @return the p_mixedBR2LT
	 */
	public PaintObjectWriting getP_mixedBR2LT() {
		return p_mixedBR2LT;
	}


	/**
	 * @return the p_mixedLT2BR
	 */
	public PaintObjectWriting getP_mixedLT2BR() {
		return p_mixedLT2BR;
	}
}

