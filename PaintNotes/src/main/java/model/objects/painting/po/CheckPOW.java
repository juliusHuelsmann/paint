package model.objects.painting.po;

import java.awt.Color;
import java.awt.Rectangle;

import model.objects.pen.normal.Pencil;
import model.util.DPoint;



/**
 * Class for checking errors in POW destroy selection.
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
public class CheckPOW {

	
	
	/**
	 * Whether an error occurred or not.
	 */
	private static boolean errorOccurred = false;
    
    
    
    
    /**
     * Create a new PaintObjectWriting for testing purpose which contains
     * two points.
     * @param _p1 the first point
     * @param _p2 the second point.
     * @return the PaintObjectwriting.
     */
    private static final PaintObjectWriting createPOW(
    		final DPoint _p1, final DPoint _p2) {
    	
    	//create PaintObjectWriting.
    	PaintObjectWriting pow = new PaintObjectWriting(
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
    public static final PaintObjectWriting createPOW(
    		final DPoint _p1, final DPoint _p2, 
    		final DPoint _p3, final DPoint _p4) {
    	
    	//create PaintObjectWriting.
    	PaintObjectWriting pow = new PaintObjectWriting(
    			0, new Pencil(1, 1, Color.black));
    	
    	//insert points.
    	pow.addPoint(_p1);
    	pow.addPoint(_p2);
    	pow.addPoint(_p3);
    	pow.addPoint(_p4);
    	return pow;
    }
    
    
   
    
    
    private static String comparePaintObjects(PaintObjectWriting[] _pow1, 
    		PaintObjectWriting[] _pow2) {

    	
    	String s = (_pow1.length + "\t\t\t" + _pow2.length + "\t\t" 
    	+  (_pow1.length == _pow2.length) + "\n");

    	for (int i = 0; i < Math.max(_pow1.length, _pow2.length); i ++) {
    		
    		if (i < _pow1.length && i < _pow2.length) {

    			s += compareArrays(_pow1[i].getPoints().toArray(), 
    					_pow2[i].getPoints().toArray()) ;
    		} else if (i < _pow1.length) {

    			DPoint[] dp = new DPoint[0];
    			s += compareArrays(_pow1[i].getPoints().toArray(), 
    					dp) ;
    		} else if (i < _pow2.length) {

    			DPoint[] dp = new DPoint[0];
    			s += compareArrays(dp, 
    					_pow2[i].getPoints().toArray());
    		}
    	}
    	return s;
    	
    }

    
    private static String compareArrays(DPoint[] _pow1, 
    		DPoint[] _pow2) {

    	
    	String s = "";
    	for (int i = 0; i < Math.max(_pow1.length, _pow2.length); i ++) {
    		
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
        				+ "\t\t\t\t"+ "FALSE" + "\n";
                		errorOccurred = true;
    		} else if (i < _pow2.length) {

        		s += "\t\t\t" +  _pow2[i].getX() + " " + _pow2[i].getY() + ";  "
        				+ "\t"+ "FALSE" + "\n";
        		errorOccurred = true;
    		}
    	}
    	return s;
    	
    	
    }
    
    public static String printArray(DPoint[] arr) {
    	
    	String s = "";
    	for (int i = 0; i < arr.length; i ++) {
    		s += arr[i].getX() + " " + arr[i].getY() + ";  ";
    	}
    	return s;
    	
    }
    

	public static void main(String[]args){
    	errorOccurred = false;
    	
    	boolean check1_1 = false, check1_2 = false, check1_3 = false, 
    			check1_4 = false,
    			check2_1 = false, check2_2 = true, check2_3 = false, 
    			check2_4 = false;
    	
    	
    	if (check1_1) {
    		checkBothOutside1_1();
    	}
    	if (check1_2) {
    		checkBothOutside1_2();
    	}
    	if (check1_3) {
    		checkBothOutside1_3();
    	}
    	if (check1_4) {
    		checkBothOutside1_4();
    	}
    	

    	if (check2_1) {
    		checkBothOutside2_1();
    	}
    	if (check2_2) {
    		checkBothOutside2_2();
    	}
    	if (check2_3) {
//    		checkBothOutside2_3();
    	}
    	if (check2_4) {
    	}
    	
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
    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(60, 56, 5, 5);
    	
    	
    	/*
    	 * first scenario
    	 */
    	System.out.println("\n \n"
    	+ "			1	_____________\n"
    	+ "			*	|            |\n"
    	+ "			*	|            |\n"
    	+ "			*	|____________|\n"
    	+ "			2");
    	PaintObjectWriting[][] result = cv_bothOutside.p_top2Bottom.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_top2Bottom},
    			
    			//result
    			result[0]));
    			
    	/*
    	 * second scenario
    	 */
    	System.out.println("\n \n"
    	+ "			2	_____________\n"
    	+ "			*	|            |\n"
    	+ "			*	|            |\n"
    	+ "			*	|____________|\n"
    	+ "			1");
    	result = cv_bothOutside.p_bottom2Top.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_bottom2Top},
    			
    			//result
    			result[0]));
    	

    	/*
    	 * 3rd scenario
    	 */
    	System.out.println("\n \n"
    	+ "			2	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(55, 61, 5, 5);
    	result = cv_bothOutside.p_bottom2Top.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_bottom2Top},
    			
    			//result
    			result[0]));
    	
    	
    	
    	

    	System.out.println("\n\n"
    	+ "			1	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			*	\n"
    	+ "			2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(55, 61, 5, 5);
    	result = cv_bothOutside.p_top2Bottom.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_top2Bottom},
    			
    			//result
    			result[0]));
    	
    	
    	
    	
    	
    	
    	
    	

    	/*
    	 * 3rd scenario
    	 */
    	System.out.println("\n \n"
    	+ "			    2	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(50, 61, 5, 5);
    	result = cv_bothOutside.p_bottom2Top.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_bottom2Top},
    			
    			//result
    			result[0]));
    	
    	
    	
    	

    	System.out.println("\n\n"
    	+ "			    1	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    *	\n"
    	+ "			    2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(50, 61, 5, 5);
    	result = cv_bothOutside.p_top2Bottom.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_top2Bottom},
    			
    			//result
    			result[0]));
    	


    	/*
    	 * 3rd scenario
    	 */
    	System.out.println("\n \n"
    	+ "			          2	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(60, 61, 5, 5);
    	result = cv_bothOutside.p_bottom2Top.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_bottom2Top},
    			
    			//result
    			result[0]));
    	
    	
    	
    	

    	System.out.println("\n\n"
    	+ "			          1	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          *	\n"
    	+ "			          2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(60, 61, 5, 5);
    	result = cv_bothOutside.p_top2Bottom.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_top2Bottom},
    			
    			//result
    			result[0]));
    	



    	/*
    	 * 3rd scenario
    	 */
    	System.out.println("\n \n"
    	+ "			               2	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               1 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(65, 61, 5, 5);
    	result = cv_bothOutside.p_bottom2Top.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_bottom2Top},
    			
    			//result
    			result[0]));
    	
    	
    	
    	

    	System.out.println("\n\n"
    	+ "			               1	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               *	\n"
    	+ "			               2 \n"
    	+ "			___________\n"
    	+ "			|         |\n"
    	+ "			|         |\n"
    	+ "			|_________|\n");
    	r = new Rectangle(65, 61, 5, 5);
    	result = cv_bothOutside.p_top2Bottom.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result 
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_top2Bottom},
    			
    			//result
    			result[0]));
    	System.out.println("both outside TB, BT error occurred: \t" + errorOccurred);
    }
	
	
	
	
	
	

    /**
     * Check for top bottom and vice versa.
     */
    private static synchronized void checkBothOutside1_2() {
    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(56, 56, 5, 5);
    	
    	
    	/*
    	 * first scenario ((55 to 60)
    	 */
    	System.out.println("\n \n"
    	+ "			1*************************2"
    	+ "			    _____________\n"
    	+ "				|            |\n"
    	+ "				|            |\n"
    	+ "				|____________|\n");
    	PaintObjectWriting[][] result = cv_bothOutside.p_left2Right.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_left2Right},
    			
    			//result
    			result[0]));
    			
    	/*
    	 * second scenario
    	 */
    	System.out.println("\n \n"
    	    	+ "			2*************************1"
    	    	+ "			    _____________\n"
    	    	+ "				|            |\n"
    	    	+ "				|            |\n"
    	    	+ "				|____________|\n");
    	result = cv_bothOutside.p_right2Left.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_right2Left},
    			
    			//result
    			result[0]));


    	/*
    	 * 3rd scenario
    	 */
    	System.out.println("\n \n"
    	    	+ "  1*****2    _____________\n"
    	    	+ "             |            |\n"
    	    	+ "             |            |\n"
    	    	+ "             |____________|\n");
    	r = new Rectangle(61, 55, 5, 5);
    	result = cv_bothOutside.p_left2Right.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_left2Right},
    			
    			//result
    			result[0]));
    	

    	
    	

    	System.out.println("\n \n"
    	    	+ " 2*****1     _____________\n"
    	    	+ "             |            |\n"
    	    	+ "             |            |\n"
    	    	+ "             |____________|\n");
    	r = new Rectangle(61, 55, 5, 5);
    	result = cv_bothOutside.p_right2Left.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_right2Left},
    			
    			//result
    			result[0]));
    	
    	
    	
    	
    	

    	


    	/*
    	 * 3rd scenario
    	 */
    	System.out.println("\n \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
    	    	+ " 1*****2     |            |\n"
    	    	+ "             |____________|\n");
    	r = new Rectangle(61, 56, 5, 5);
    	result = cv_bothOutside.p_left2Right.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_left2Right},
    			
    			//result
    			result[0]));
    	

    	
    	

    	System.out.println("\n \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
    	    	+ " 2*****1     |            |\n"
    	    	+ "             |____________|\n");
    	r = new Rectangle(61, 56, 5, 5);
    	result = cv_bothOutside.p_right2Left.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_right2Left},
    			
    			//result
    			result[0]));
    	
    	
    	
    	

		/*
		 * 3rd scenario
		 */
		System.out.println("\n \n"
		    	+ "              _____________\n"
		    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "   1*****2   |____________|\n");
		r = new Rectangle(61, 50, 5, 5);
		result = cv_bothOutside.p_left2Right.separate(r);
		System.out.println("EXPECTED\t\tRESULT\t\t");
		System.out.println("insideList:");
		System.out.print(comparePaintObjects(
				
				//expected
				new PaintObjectWriting[0], 
				
				//result
				result[1]));
		
		System.out.println("outsidelist:");
		
		System.out.print(comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{cv_bothOutside.p_left2Right},
				
				//result
				result[0]));
		
		
		
		
		
		System.out.println("\n \n"
		    	+ "              _____________\n"
		    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ " 2*****1     |____________|\n");
		r = new Rectangle(61, 50, 5, 5);
		result = cv_bothOutside.p_right2Left.separate(r);
		System.out.println("EXPECTED\t\tRESULT\t\t");
		System.out.println("insideList:");
		System.out.print(comparePaintObjects(
				
				//expected
				new PaintObjectWriting[0], 
				
				//result
				result[1]));
		
		System.out.println("outsidelist:");
		
		System.out.print(comparePaintObjects(
				
				//expected
				new PaintObjectWriting[]{cv_bothOutside.p_right2Left},
				
				//result
				result[0]));

    	
    	System.out.println("both outside LR, RL error occurred: \t"
    			+ errorOccurred);
    }
 
    
    
    
    
    
    
    
    
    
    
    
    
    


    /**
     * Check for top bottom and vice versa.
     */
    private static synchronized void checkBothOutside1_3() {
    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(50, 61, 5, 5);
    	
    	
    	System.out.println("\n \n"
    	    	+ "                                    1    \n"
    	    	+ "                                 2  \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n");
    	PaintObjectWriting[][] result = cv_bothOutside.p_mixed_LB2RT.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixed_LB2RT},
    			
    			//result
    			result[0]));

    	
    	System.out.println("\n \n"
    	    	+ "                                    2    \n"
    	    	+ "                                 1  \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n");
    	result = cv_bothOutside.p_mixed_RT2LB.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixed_RT2LB},
    			
    			//result
    			result[0]));

    	
    	
    	

    	r = new Rectangle(55, 61, 5, 5);
    	System.out.println("\n \n"
    	    	+ "                      1                  \n"
    	    	+ "                    2               \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n");
    	result = cv_bothOutside.p_mixed_LB2RT.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixed_LB2RT},
    			
    			//result
    			result[0]));

    	
    	System.out.println("\n \n"
    	    	+ "                      2                  \n"
    	    	+ "                    1               \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n");
    	result = cv_bothOutside.p_mixed_RT2LB.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixed_RT2LB},
    			
    			//result
    			result[0]));
    	
    	System.out.println("both outside LR, RL error occurred: \t"
    			+ errorOccurred);
    }
    
    
    
    



    /**
     * Check for top bottom and vice versa.
     */
    private static synchronized void checkBothOutside1_4() {
    	CheckEnvironment 
    	cv_bothOutside = new CheckEnvironment();
    	Rectangle r = new Rectangle(50, 61, 5, 5);
    	
    	
    	System.out.println("\n \n"
    	    	+ "       1                                \n"
    	    	+ "          2                        \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n");
    	PaintObjectWriting[][] result = cv_bothOutside.p_mixedLT2BR.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixedLT2BR},
    			
    			//result
    			result[0]));

    	
    	System.out.println("\n \n"
    	    	+ "       1                                \n"
    	    	+ "          2                         \n"
    	    	+ "             _____________\n"
    	    	+ "             |            |\n"
		    	+ "             |            |\n"
		    	+ "             |____________|\n");
    	result = cv_bothOutside.p_mixedBR2LT.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixedBR2LT},
    			
    			//result
    			result[0]));

    	
    	
    	

    	r = new Rectangle(61, 55, 10, 10);
    	System.out.println("\n \n"
    	    	+ "             _____________\n"
    	    	+ "      1      |            |\n"
		    	+ "          2  |            |\n"
		    	+ "             |____________|\n");
    	result = cv_bothOutside.p_mixedLT2BR.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixedLT2BR},
    			
    			//result
    			result[0]));

    	
    	System.out.println("\n \n"
    	    	+ "             _____________\n"
    	    	+ "       2     |            |\n"
		    	+ "          1  |            |\n"
		    	+ "             |____________|\n");
    	result = cv_bothOutside.p_mixedBR2LT.separate(r);
    	System.out.println("EXPECTED\t\tRESULT\t\t");
    	System.out.println("insideList:");
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[0], 
    			
    			//result
    			result[1]));
    	
    	System.out.println("outsidelist:");
    	
    	System.out.print(comparePaintObjects(
    			
    			//expected
    			new PaintObjectWriting[]{cv_bothOutside.p_mixedBR2LT},
    			
    			//result
    			result[0]));
    	
    	System.out.println("both outside LR, RL error occurred: \t"
    			+ errorOccurred);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    

	 /**
   * Check for top bottom and vice versa.
   */
  private static synchronized void checkBothOutside2_1() {
  	CheckEnvironment 
  	cv_bothOutside = new CheckEnvironment();
  	Rectangle r;
  	
  	
  	System.out.println("\n \n"
  	+ "			2	\n"
  	+ "			*	\n"
  	+ "			*	\n"
  	+ "			*__________\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			*	\n"
  	+ "			1 \n");
  	r = new Rectangle(55, 56, 5, 2);
  	PaintObjectWriting[][] result = cv_bothOutside.p_bottom2Top.separate(r);
  	System.out.println("EXPECTED\t\tRESULT\t\t");
  	System.out.println("insideList:");
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  		   					//intersection at the bottom of the rectangle.
  							new DPoint(55,58), 
  							
  							//intersection at top of rectangle.
  							new DPoint(55, 56))
  			}, 
  			
  			//result
  			result[1]));
  	
  	System.out.println("outsidelist:");
  	
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(55,60), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(55,58)),

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(55, 56),
  		   							
  		   					//point 2
  		   					new DPoint(55, 55))
  			}, 
  			//result
  			result[0]));
  	
  	
  	

  	System.out.println("\n \n"
  	+ "			1	\n"
  	+ "			*	\n"
  	+ "			*	\n"
  	+ "			*__________\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			*	\n"
  	+ "			2 \n");
  	r = new Rectangle(55, 56, 5, 2);
  	result = cv_bothOutside.p_top2Bottom.separate(r);
  	System.out.println("EXPECTED\t\tRESULT\t\t");
  	System.out.println("insideList:");
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  							//intersection at top of rectangle.
  							new DPoint(55, 56),
  		   					
  							//intersection at the bottom of the rectangle.
  							new DPoint(55,58))
  			}, 
  			
  			//result
  			result[1]));
  	
  	System.out.println("outsidelist:");
  	
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(

  		   					//point 1
  		   					new DPoint(55, 55),
  		   					
  		   					//intersection at top of rectangle.
  		   					new DPoint(55, 56)),
  		   							
  		   					
  		   			//first line: from first point to first intersection (
  		   			//the one at the bottom of the rectangle)
  		   			createPOW(

  		   		   			//intersection at the bottom of the rectangle.
  		   					new DPoint(55,58),

  		   		   			//point 2.
  		   					new DPoint(55,60))
  			}, 
  			//result
  			result[0]));
  	

  	
  	
  	
  	
  	
  	

  	
  	System.out.println("\n \n"
  	+ "			          2	\n"
  	+ "		              *	\n"
  	+ "			          *	\n"
  	+ "			__________*\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			          *	\n"
  	+ "			          1 \n");
  	r = new Rectangle(50, 56, 5, 2);
  	result = cv_bothOutside.p_bottom2Top.separate(r);
  	System.out.println("EXPECTED\t\tRESULT\t\t");
  	System.out.println("insideList:");
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  		   					//intersection at the bottom of the rectangle.
  							new DPoint(55,58), 
  							
  							//intersection at top of rectangle.
  							new DPoint(55, 56))
  			}, 
  			
  			//result
  			result[1]));
  	
  	System.out.println("outsidelist:");
  	
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(55,60), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(55,58)),

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(55, 56),
  		   							
  		   					//point 2
  		   					new DPoint(55, 55))
  			}, 
  			//result
  			result[0]));
  	
  	
  	

  	System.out.println("\n \n"
  	+ "			          1	\n"
  	+ "			          *	\n"
  	+ "			          *	\n"
  	+ "			__________*\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			          *	\n"
  	+ "			          2 \n");
  	r = new Rectangle(50, 56, 5, 2);
  	result = cv_bothOutside.p_top2Bottom.separate(r);
  	System.out.println("EXPECTED\t\tRESULT\t\t");
  	System.out.println("insideList:");
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  							//intersection at top of rectangle.
  							new DPoint(55, 56),
  		   					
  							//intersection at the bottom of the rectangle.
  							new DPoint(55,58))
  			}, 
  			
  			//result
  			result[1]));
  	
  	System.out.println("outsidelist:");
  	
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(

  		   					//point 1
  		   					new DPoint(55, 55),
  		   					
  		   					//intersection at top of rectangle.
  		   					new DPoint(55, 56)),
  		   							
  		   					
  		   			//first line: from first point to first intersection (
  		   			//the one at the bottom of the rectangle)
  		   			createPOW(

  		   		   			//intersection at the bottom of the rectangle.
  		   					new DPoint(55,58),

  		   		   			//point 2.
  		   					new DPoint(55,60))
  			}, 
  			//result
  			result[0]));
  	


  	
  	System.out.println("\n \n"
  	+ "			     2	\n"
  	+ "	             *	\n"
  	+ "			     *	\n"
  	+ "			_____*____\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			     *	\n"
  	+ "			     1 \n");
  	r = new Rectangle(52, 56, 5, 2);
  	result = cv_bothOutside.p_bottom2Top.separate(r);
  	System.out.println("EXPECTED\t\tRESULT\t\t");
  	System.out.println("insideList:");
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  		   					//intersection at the bottom of the rectangle.
  							new DPoint(55,58), 
  							
  							//intersection at top of rectangle.
  							new DPoint(55, 56))
  			}, 
  			
  			//result
  			result[1]));
  	
  	System.out.println("outsidelist:");
  	
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					
  					//first line: from first point to first intersection (
  					//the one at the bottom of the rectangle)
  					createPOW(

  		   					//point 1.
  							new DPoint(55,60), 
  							
  		   					//intersection at the bottom of the rectangle.
  							new DPoint(55,58)),

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(
  		   							
  		   					//intersection at top of rectangle.
  		   					new DPoint(55, 56),
  		   							
  		   					//point 2
  		   					new DPoint(55, 55))
  			}, 
  			//result
  			result[0]));
  	
  	
  	

  	System.out.println("\n \n"
  	+ "			    1	\n"
  	+ "			    *	\n"
  	+ "			    *	\n"
  	+ "			____*_____\n"
  	+ "			|         |\n"
  	+ "			|         |\n"
  	+ "			|_________|\n"
  	+ "			    *	\n"
  	+ "			    2 \n");
  	r = new Rectangle(52, 56, 5, 2);
  	result = cv_bothOutside.p_top2Bottom.separate(r);
  	System.out.println("EXPECTED\t\tRESULT\t\t");
  	System.out.println("insideList:");
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					createPOW(

  							//intersection at top of rectangle.
  							new DPoint(55, 56),
  		   					
  							//intersection at the bottom of the rectangle.
  							new DPoint(55,58))
  			}, 
  			
  			//result
  			result[1]));
  	
  	System.out.println("outsidelist:");
  	
  	System.out.print(comparePaintObjects(
  			
  			//expected
  			new PaintObjectWriting[]{
  					

  		   			//second line: from second intersection (the one at the 
  					//top of the rectangle) to the second point
  					createPOW(

  		   					//point 1
  		   					new DPoint(55, 55),
  		   					
  		   					//intersection at top of rectangle.
  		   					new DPoint(55, 56)),
  		   							
  		   					
  		   			//first line: from first point to first intersection (
  		   			//the one at the bottom of the rectangle)
  		   			createPOW(

  		   		   			//intersection at the bottom of the rectangle.
  		   					new DPoint(55,58),

  		   		   			//point 2.
  		   					new DPoint(55,60))
  			}, 
  			//result
  			result[0]));
  	
  	
  	
  	System.out.println("error occurred: \t\t\t" + errorOccurred);
  }

  

	 /**
 * Check for top bottom and vice versa.
 */
private static synchronized void checkBothOutside2_2() {
	CheckEnvironment 
	cv_bothOutside = new CheckEnvironment();
	Rectangle r;
	

	System.out.println("\n \n"
	+ "2 * * * *__________* * * 1\n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "         |_________|\n"
	+ "         *	\n");
	r = new Rectangle(57, 55, 2, 5);
	PaintObjectWriting[][] result = cv_bothOutside.p_right2Left.separate(r);
	System.out.println("EXPECTED\t\tRESULT\t\t");
	System.out.println("insideList:");
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(59,55), 
							
							new DPoint(57, 55))
			}, 
			
			//result
			result[1]));
	
	System.out.println("outsidelist:");
	
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(60,55), 
							
		   					//intersection 
							new DPoint(59,55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(57, 55),
		   							
		   					//point 2
		   					new DPoint(55, 55))
			}, 
			//result
			result[0]));
	

	System.out.println("\n \n"
	+ "1 * * * *__________* * * 2\n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "         |_________|\n"
	+ "         *	\n");
	r = new Rectangle(57, 55, 2, 5);
	result = cv_bothOutside.p_left2Right.separate(r);
	System.out.println("EXPECTED\t\tRESULT\t\t");
	System.out.println("insideList:");
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(57,55), 
							
							new DPoint(59, 55))
			}, 
			
			//result
			result[1]));
	
	System.out.println("outsidelist:");
	
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(55,55), 
							
		   					//intersection 
							new DPoint(57,55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(59, 55),
		   							
		   					//point 2
		   					new DPoint(60, 55))
			}, 
			//result
			result[0]));
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	


	System.out.println("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "2 * * * *|_________|* * * 1\n" 
	+ "         *	\n");
	r = new Rectangle(57, 50, 2, 5);
	result = cv_bothOutside.p_right2Left.separate(r);
	System.out.println("EXPECTED\t\tRESULT\t\t");
	System.out.println("insideList:");
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(59,55), 
							
							new DPoint(57, 55))
			}, 
			
			//result
			result[1]));
	
	System.out.println("outsidelist:");
	
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(60,55), 
							
		   					//intersection 
							new DPoint(59,55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(57, 55),
		   							
		   					//point 2
		   					new DPoint(55, 55))
			}, 
			//result
			result[0]));
	

	System.out.println("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "         |         |\n"
	+ "1 * * * *|_________|* * * 2\n" 
	+ "         *	\n");
	r = new Rectangle(57, 50, 2, 5);
	result = cv_bothOutside.p_left2Right.separate(r);
	System.out.println("EXPECTED\t\tRESULT\t\t");
	System.out.println("insideList:");
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(57,55), 
							
							new DPoint(59, 55))
			}, 
			
			//result
			result[1]));
	
	System.out.println("outsidelist:");
	
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(55,55), 
							
		   					//intersection 
							new DPoint(57,55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(59, 55),
		   							
		   					//point 2
		   					new DPoint(60, 55))
			}, 
			//result
			result[0]));
	
	
	
	
	
	
	
	
	
	
	
	

	


	System.out.println("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "2 * * * *|         |* * * 1\n"
	+ "         |_________|\n" 
	+ "         *	\n");
	r = new Rectangle(57, 52, 2, 5);
	result = cv_bothOutside.p_right2Left.separate(r);
	System.out.println("EXPECTED\t\tRESULT\t\t");
	System.out.println("insideList:");
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(59,55), 
							
							new DPoint(57, 55))
			}, 
			
			//result
			result[1]));
	
	System.out.println("outsidelist:");
	
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(60,55), 
							
		   					//intersection 
							new DPoint(59,55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(57, 55),
		   							
		   					//point 2
		   					new DPoint(55, 55))
			}, 
			//result
			result[0]));
	

	System.out.println("\n \n"
	+ "         __________      \n" 
	+ "         |         |\n"
	+ "1 * * * *|         |* * * 2\n"
	+ "         |_________|\n" 
	+ "         *	\n");
	r = new Rectangle(57, 52, 2, 5);
	result = cv_bothOutside.p_left2Right.separate(r);
	System.out.println("EXPECTED\t\tRESULT\t\t");
	System.out.println("insideList:");
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					createPOW(

							new DPoint(57,55), 
							
							new DPoint(59, 55))
			}, 
			
			//result
			result[1]));
	
	System.out.println("outsidelist:");
	
	System.out.print(comparePaintObjects(
			
			//expected
			new PaintObjectWriting[]{
					
					//first line: from first point to first intersection
					createPOW(

		   					//point 1.
							new DPoint(55,55), 
							
		   					//intersection 
							new DPoint(57,55)),

		   			//second line: from second intersection to the second point
					createPOW(
		   							
		   					//intersection
		   					new DPoint(59, 55),
		   							
		   					//point 2
		   					new DPoint(60, 55))
			}, 
			//result
			result[0]));
	
	
	
	
	
	
	System.out.println("error occurred: \t\t\t" + errorOccurred);
	}
}





class CheckEnvironment {
	
	/**
	 * The possible combinations of paintObjects.
	 * 	
	 * 
	 */
	protected PaintObjectWriting
	
	//single direction
	p_left2Right, p_right2Left, 
	p_top2Bottom, p_bottom2Top,
	
	//mixed directions
	p_mixedLT2BR, p_mixedBR2LT, 
	p_mixed_LB2RT, p_mixed_RT2LB;
	
	public CheckEnvironment() {


		//simple direction.
    	p_left2Right = new PaintObjectWriting(0, new Pencil(1, 1, Color.black));
    	p_left2Right.addPoint(new DPoint (55 , 55));
    	p_left2Right.addPoint(new DPoint (60 , 55));
    	
    	p_right2Left = new PaintObjectWriting(0, new Pencil(1, 1, Color.black));
    	p_right2Left.addPoint(new DPoint (60 , 55));
    	p_right2Left.addPoint(new DPoint (55 , 55));

    	p_top2Bottom = new PaintObjectWriting(1, new Pencil(1, 1, Color.black));
    	p_top2Bottom.addPoint(new DPoint (55 , 55));
    	p_top2Bottom.addPoint(new DPoint (55 , 60));
    	
    	p_bottom2Top = new PaintObjectWriting(1, new Pencil(1, 1, Color.black));
    	p_bottom2Top.addPoint(new DPoint (55 , 60));
    	p_bottom2Top.addPoint(new DPoint (55 , 55));
    	

    	//mixed directions.
    	p_mixedLT2BR = new PaintObjectWriting(0, new Pencil(1, 1, Color.black));
    	p_mixedLT2BR.addPoint(new DPoint (55 , 55));
    	p_mixedLT2BR.addPoint(new DPoint (60 , 60));
    	
    	p_mixedBR2LT = new PaintObjectWriting(0, new Pencil(1, 1, Color.black));
    	p_mixedBR2LT.addPoint(new DPoint (55 , 55));
    	p_mixedBR2LT.addPoint(new DPoint (60 , 60));

    	p_mixed_LB2RT = new PaintObjectWriting(1, new Pencil(1, 1, Color.black));
    	p_mixed_LB2RT.addPoint(new DPoint (55 , 60));
    	p_mixed_LB2RT.addPoint(new DPoint (60 , 55));
    	
    	p_mixed_RT2LB = new PaintObjectWriting(1, new Pencil(1, 1, Color.black));
    	p_mixed_RT2LB.addPoint(new DPoint (60 , 55));
    	p_mixed_RT2LB.addPoint(new DPoint (55 , 60));
	}
	
}

