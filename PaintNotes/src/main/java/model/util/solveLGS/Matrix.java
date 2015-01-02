package model.util.solveLGS;

import java.awt.Point;
import java.awt.Rectangle;

import model.settings.Status;
import model.util.adt.list.List;


/**
 * the matrix for mathematical computations.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class Matrix {

    
    /**
     * The matrix.
     */
	private double [][] content;
	

	/**
	 * Constructor: initialized with width and height.
	 * @param _width the width
	 * @param _height the height
	 */
	public Matrix(final int _width, final int _height) {
		
		this.content = new double[_width][_height];
	}
	
	
	/**
	 * set the value of a special field in matrix.
	 * @param _x the x coordinate
	 * @param _y the y coordinate
	 * @param _value the value to be set
	 */
	public final void setValue(final int _x, final int _y, 
	        final double _value) {
		this.content[_x][_y] = _value;
	}
	
	
	/**
	 * Try to compute a solution of the matrix.
	 * @return the solved matrix
	 */
	@Deprecated
	public final double[] solveUnexact() {
		
		if (content.length + 1 != content[0].length) {
		    Status.getLogger().severe(
			        "unable to solve matrix: not quadratic!");
		}
		
		
		
		for (int line = 0; line < content.length; line++) {
			
			//divide current line by line[content][content]; thus make
		    //the pivot element 1
			content[line] = divideLine(content[line], content[line][line]);
		
			//sub the first line from the following ones.
			for (int lineTwo = line + 1; lineTwo < content.length; lineTwo++) {
				substractMult(content[lineTwo], content[line], 
				        content[lineTwo][line]);
			}
		}
		

		for (int line = content.length - 1; line >= 0; line--) {
			for (int lineTwo = line - 1; lineTwo >= 0; lineTwo--) {
				substractMult(content[lineTwo], content[line], 
				        content[lineTwo][line]);
			}
		}
		
		double [] solution = new double [content.length];
		String x = "";
		for (int i = 0; i < solution.length; i++) {
			solution[i] = content[i][solution.length];
			x += "	+ " + solution[i] + " * x ^ " + (solution.length - 1 - i);
		}
		x = x.replaceFirst("	+", "f(x) = ");
		return solution;
	}
	

    /**
     * Try to compute a solution of the matrix.
     * @return the solved matrix
     */
    public final double[] solve() {
        
        
        //check whether the matrix is quadratic. Whether its determinant is non 
        //- zero will be checked on the fly during the calculation.
        if (content.length + 1 != content[0].length) {
            Status.getLogger().severe("unable to solve matrix: not quadratic!");
            return null;
        } 
        String s = ("anfang" + printMatrix());
        
        for (int line = 0; line < content.length; line++) {

            /*
             * pivot transformation
             */
            //find pivot element in each line. Only simple pivot research.
            int maxId = line;
            for (int lineF = line; lineF < content.length; lineF++) {
                
                if (Math.abs(content[lineF][line]) 
                        >= Math.abs(content[maxId][maxId])) {
                    maxId = lineF;
                }
            }
            
            //swap the current line with the line containing the pivot element
            double[] swap = content[maxId];
            content[maxId] = content[line];
            content[line] = swap;

            s += ("geordnet" + printMatrix());
        
            /*
             * Subtract current line from following ones.
             */
            for (int lineF = line + 1; lineF < content.length; lineF++) {

                substractMult(content[lineF], content[line], 
                        1.0 * content[lineF][line] / content[line][line]);
            }
        
        }

        s += ("obere dreieck" + printMatrix());

        for (int line = content.length - 1; line >= 0; line--) {
            for (int lineTwo = line - 1; lineTwo >= 0; lineTwo--) {
                substractMult(content[lineTwo], content[line], 
                        content[lineTwo][line] / content[line][line]);
            }
        }

        s += ("diagonalmatrix" + printMatrix());

        
        for (int line = 0; line < content.length; line++) {
            
            //divide current line by line[content][content]; thus make
            //the pivot element 1
            content[line] = divideLine(content[line], content[line][line]);
        }
        s += ("solution1" + printMatrix());
        
        //generate solution.
        double [] solution = new double [content.length];
        for (int i = 0; i < solution.length; i++) {
            solution[i] = content[i][solution.length];
            s += "    + " + solution[i] + " * x ^ " + (solution.length - 1 - i);
        }
        s = s.replaceFirst("    + ", "f(x) = ");
        
//        System.out.println(s + x);
        return solution;
    }
	
	/**
	 * Berechnent funktionswert von f an der stelle x.
	 * 
	 * @param _function function
	 * @param _xValue x value
	 * @return the value of the function at x.
	 */
	public static final int calculateFVonX(final double[] _function, 
	        final int _xValue) {
		
		double result = 0;
		for (int i = 0; i < _function.length; i++) {
			result += (Math.pow(_xValue, _function.length - 1 - i) 
			        * _function[i]);
		}
		return (int) result;
	}
	

	/**
	 * divide a single line by a value and return the result.
	 * @param _content the line
	 * @param _divisor the value
	 * @return the new line
	 */
	private double[] divideLine(final double [] _content, 
	        final double _divisor) {
		for (int i = 0; i < _content.length; i++) {
			_content[i] = 1.0 * _content[i] / _divisor;
		}
		return _content;
	}
	
	
	/**
	 * Subtract one line * factor from another and return the result.
	 * @param _content the one line
	 * @param _toSubstract the other line which is subtracted
	 * @param _factor the factor
	 * @return the new line
	 */
	private double[] substractMult(final double [] _content, 
	        final double [] _toSubstract, final double _factor) {
		
		for (int i = 0; i < _content.length; i++) {
			_content[i] -= (1.0 * _toSubstract[i] * _factor);
		}
		return _content;
		
	}
    
    
    /**
     * print current matrix.
     */
    public final void printOutMatrix() {
        
        System.out.println("\nPRINT MATRIX\n");
        
        for (int i = 0; i < content.length; i++) {
            for (int j = 0; j < content[i].length; j++) {
                System.out.print("  " + content[i][j] + "   ");
            }
            System.out.println("");
        }
    }
    
    
    /**
     * print current matrix.
     * @return the printed matrix
     */
    public final String printMatrix() {
        
        String s = "\nPRINT MATRIX\n\n";
        
        for (int i = 0; i < content.length; i++) {
            for (int j = 0; j < content[i].length; j++) {
                s += ("\t" + content[i][j] + "\t");
            }
            s += ("\n");
        }
        s += ("\n\n");
        
        return s;
    }

	/**
	 * return the ableitung.
	 * @param _funct the function
	 * @param _x the x value 
	 * @return the value
	 */
	public static Point getAbleitungen(final double[] _funct, final int _x) {

		return getAbleitung(_funct, _x);
	}

	
	/**
	 * compute ableitung at position.
	 * @param _funct the function
	 * @param _x the x value
	 * @return the ableitung
	 */
	private static Point getAbleitung(final double[] _funct, 
	        final int _x) {
		
		double ersteAbl = 0;
		double zweiteAbl = 0;
		for (int i = 0; i < _funct.length - 1; i++) {
			ersteAbl += _funct[i] * (_funct.length - i - 1) 
			        * Math.pow(_x, _funct.length - i - 2);
		}
		for (int i = 0; i < _funct.length - 2; i++) {
			zweiteAbl += _funct[i] * (_funct.length - i - 1)
			        * (_funct.length - i - 2) * Math.pow(
			                _x, _funct.length - i - 2 - 1);
		}
		return new Point((int) ersteAbl, (int) zweiteAbl);
	}
	
	
	/**
	 * calc best function.
	 * 
	 * @param _ls the list
	 * @param _rect the rectangle
	 * @return the best function
	 */
	@SuppressWarnings("null")
    public final double[] calculateBestFunction(final List<Point> _ls, 
	        final Rectangle _rect) {
		Matrix[] matrizen = null;

		double[] abweichung = new double [matrizen.length];
		
		double[] [] functions = null;
		for (int i = 0; i < matrizen.length; i++) {
			functions [i] = matrizen[i].solve();
			abweichung[i] = abweichungswertBestimmen(functions[i], _ls);
		}
		
		//gib die funktion mit dem niedrigsten abweichung zurueck
		
		return null;
		
		
	}
	
	
	/**
	 * calculate abweichung.
	 * @param _funct fucntion
	 * @param _lsPointsOnFunct list fo points
	 * @return the value for the abweichung
	 */
	private static double abweichungswertBestimmen(
	        final double[] _funct, final List<Point> _lsPointsOnFunct) {
		return 0;
		
	}
	
	/**
	 * load functions.
	 * @param _ls the list of points
	 * @param _ableitungen the ableitungen
	 * @return the function
	 */
	public static Matrix loadFunctions(final List<Point> _ls, 
	        final Rectangle _ableitungen) {
		int amount = 2;
		_ls.toFirst();
		while (!_ls.isBehind()) {
			
			amount++;
			_ls.next();
		}
		
		Matrix m = new Matrix(amount , amount + 1);
		
		_ls.toFirst();
		for (int line = 0; line < amount - 2; line++) {

			int x = _ls.getItem().x;
			int y = _ls.getItem().y;
			for (int j = 0; j < amount; j++) {

				m.setValue(line , j, Math.pow(x, amount - 1 - j));	
			}
			m.setValue(line, amount, y);
			_ls.next();
		}
		m.printOutMatrix();
		/*
		 * set value of ableitung
		 */
		//ax		 + 	bx 	+	c		= 0
		//2*XWERT*a	 + 	b*1	+	0		= y 
		for (int j = 0; j < amount; j++) {

			m.setValue(amount - 2 , j, (amount - 1 - j) 
			        * Math.pow(_ableitungen.x, amount - 2 - j));	
		}
		m.setValue(amount - 2, amount, _ableitungen.y);
		
//		zweite abl
		for (int j = 0; j < amount - 1; j++) {

			m.setValue(amount - 1 , j, (amount - 1 - j) * (amount - 2 - j)
			        * Math.pow(_ableitungen.width, amount - 2 - 1 - j));	
		}
		m.setValue(amount - 1, amount, _ableitungen.height);
		
		m.printOutMatrix();
		
		return m;
	}

	/**
	 * load a function from list of points.
	 * @param _ls the list of points
	 * @return the matrix.
	 */
	public static final Matrix loadFunctions(final List<Point> _ls) {
		int amount = 0;
		_ls.toFirst();
		while (!_ls.isBehind()) {
			
			amount++;
			_ls.next();
		}
		
		Matrix m = new Matrix(amount , amount + 1);
		
		_ls.toFirst();
		for (int line = 0; line < amount; line++) {

			int x = _ls.getItem().x;
			int y = _ls.getItem().y;
			for (int j = 0; j < amount; j++) {

				m.setValue(line , j, Math.pow(x, amount - 1 - j));	
			}
			m.setValue(line, amount, y);
			_ls.next();
		}
		
		return m;
	}
}
