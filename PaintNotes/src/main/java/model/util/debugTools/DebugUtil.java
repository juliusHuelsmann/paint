package model.util.debugTools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.settings.Status;



/**
 * Utility class for debug purpose.
 * Contains the method for performing a graphical-user-interface-elements-check
 * on the code. 
 * This revealed the solution of an error (comparison method violates general 
 * constraints) which was thrown only by the official java runtime environment.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class DebugUtil {
    
    /*
     * Utility tests for checking whether program works properly.
     */

	
	/**
	 * Debug values which indicate whether the check - method displays the
	 * results of the check inside the console or whether an image is painted
	 * that displays the element - tree.
	 */
    public static final byte 
    CHECK_OP_CONSOLE = 0,
    CHECK_OP_IMAGE = 1;
    
    
    /**
     * The dimension of one item in the picture (only necessary if the specified
     * operation is the CHECK_OP_IMAGE operation).
     */
    public static final Dimension D_ITEM = new Dimension(70, 10);
    
    /**
     * The distance of two items in the picture (only necessary if the specified
     * operation is the CHECK_OP_IMAGE operation).
     */
    public static final Dimension D_DISTANCE = new Dimension(12, 2);

    
    /**
     * Colors for displaying view.
     */
    private static final Color CLR_CORRECT = new Color(130, 131, 133), 
    		CLR_ERROR = new Color(255, 120, 120);
	
	/**
	 * Font which is used for printing names inside the return image of
	 * function printImageFormComponents().
	 */
    private static final Font FONT_IMAGE = new Font("", Font.PLAIN, 10);
    
    /**
     * Empty utility class constructor.
     */
    private DebugUtil() { }
    
    
    /**
     * Starts a size-component-check and prints an image.
     * @param _rootComponent	the root component (the first level which 
     * 							contains all the sub-elements).
     * @param _operation 		the currently specified operation
     */
	public static void performCheckComponents(
			final Component _rootComponent,
			final byte _operation) {
		
		switch(_operation) {
		case CHECK_OP_IMAGE:

			//the output path and the image that is presented
			final String outputPath = "analyse.png";

			
			//fetch the amount of components and the depth
			//of the tree
			Rectangle pnt_result = checkComponents(
					_rootComponent, "", 0, 0);
			int itemsCols = pnt_result.width + 1;
			int itemsRows = pnt_result.y + 1;
			
			
			//create image and fill it with background color
			BufferedImage bi_analyze = new BufferedImage(
					itemsCols * (D_ITEM.width + D_DISTANCE.width), 
					itemsRows * (D_ITEM.height + D_DISTANCE.height), 
					BufferedImage.TYPE_INT_RGB);
			for (int w = 0; w < bi_analyze.getWidth(); w++) {
				for (int h = 0; h < bi_analyze.getHeight(); h++) {
					bi_analyze.setRGB(w, h, Color.white.getRGB());
				}
			}
			
			
			//for being able to both count the current entry and to
			//return the BufferedImage by the function, it is necessary
			//to use a class variable for counting the current entry
			currentEntry = 0;
			
			//check components will call itself recursively and
			//check all components that are added by the root components
			//or its children.
			printImageFromComponents(_rootComponent,  0, bi_analyze);
			
			//write image to hard drive
			try {
				ImageIO.write(bi_analyze, "png", new File(outputPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			break;
		default:
			Status.getLogger().warning("Debug tool: unknown debug operator:"
					+ _operation);
			break;
		}
	}


	
	
	/**
	 * Check the components.
	 * Recursive method which returns the total amount of rows and columns.
	 * 
	 * @param _rootComponent 	the root component
	 * @param _print			whether to print the result or not
	 * @param _currentColumn	the current column number
	 * @param _currentRow		the current row number
	 * @return					the total amount of rows and columns of the
	 * 							current graphical-user-interface-configuration
	 */
	private static Rectangle checkComponents(
			final Component _rootComponent, 
			final String _print,
			final int _currentColumn,
			final int _currentRow) {
		
		//save given values.
		int depth = _currentColumn;
		int amount = _currentRow;
		int maxDepth = _currentColumn;
		
		
		
		


		if (_rootComponent.getWidth() <= 0 || _rootComponent.getHeight() <= 0) {
			System.err.println(_print 
					+ _rootComponent.getClass().getSimpleName()
					+ _rootComponent.getSize()
					);
		} else {
			System.out.println(_print 
					+ _rootComponent.getClass().getSimpleName()
//					+ _c.getSize() 
					);
		}
		
		
		
		
		
		if (_rootComponent instanceof JPanel) {
			for (Component x : ((JPanel) _rootComponent).getComponents()) {
				
				Rectangle pnt_recursiv = checkComponents(
						x, _print + "\t", depth + 1, 1);
				
				//update max depth and amount
				amount += pnt_recursiv.y;
				maxDepth = Math.max(maxDepth, pnt_recursiv.width);
			}

		} else if (_rootComponent instanceof JFrame) {

			for (Component x : ((JFrame) _rootComponent).getContentPane()
					.getComponents()) {


				Rectangle pnt_recursiv = checkComponents(
						x, _print + "\t", depth + 1, 1);

				//update max depth
				amount += pnt_recursiv.y;
				maxDepth = Math.max(maxDepth, pnt_recursiv.width);
			}
			
		} else if (_rootComponent instanceof Panel) {

			for (Component x : ((Panel) _rootComponent).getComponents()) {


				Rectangle pnt_recursiv = checkComponents(
						x, _print + "\t", depth + 1, 1);

				//update max depth
				amount += pnt_recursiv.y;
				maxDepth = Math.max(maxDepth, pnt_recursiv.width);
			}
			
		} else if (_rootComponent instanceof Window) {

			for (Component x : ((Window) _rootComponent).getComponents()) {


				Rectangle pnt_recursiv = checkComponents(
						x, _print +  "\t", depth + 1, 1);

				//update max depth
				amount += pnt_recursiv.y;
				maxDepth = Math.max(maxDepth, pnt_recursiv.width);
			}

		} 
		
		
		Rectangle pnt_res = new Rectangle(depth, amount, maxDepth, 0);
		return pnt_res;
	}

	
	/**
	 * Current entry (has to be outside the function because 
	 * it is only allowed to have one return value. Thus either
	 * the BufferedImage or the currentEntry have to be externalized.
	 */
	private static int currentEntry = 1;

	/**
	 * Print image from components.
	 * Recursive method.
	 * 
	 * @param _rootComponent 	the root component
	 * @param _currentColumn	the column of the current item
	 * @param _bi				the bufferedImage which is altered and 
	 * 							afterwards returned
	 * 							
	 * @return					the BufferedImage which displays the complete
	 * 							graphical user interface landscape.
	 */
	private static BufferedImage printImageFromComponents(
			final Component _rootComponent, 
			final int _currentColumn,
			final BufferedImage _bi) {
		
		
		//save given values.
		BufferedImage bi_ret = _bi;



		int adjC = 0 + _currentColumn * (D_ITEM.width + D_DISTANCE.width);
		int adjR = 0 + currentEntry * (D_ITEM.height + D_DISTANCE.height);
		
		Graphics g = bi_ret.getGraphics();


		if (_rootComponent.getWidth() <= 0 || _rootComponent.getHeight() <= 0) {

			g.setColor(CLR_ERROR);
		} else {

			g.setColor(CLR_CORRECT);
		}

		g.drawRect(adjC, adjR, D_ITEM.width, D_ITEM.height);
		g.setFont(FONT_IMAGE);
		g.drawString(_rootComponent.getClass().getSimpleName(), adjC, 
				adjR + D_ITEM.height);

			
			
		currentEntry++;
		
		if (_rootComponent instanceof JPanel) {
			for (Component x : ((JPanel) _rootComponent).getComponents()) {

				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = printImageFromComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
				
			}

		} else if (_rootComponent instanceof JFrame) {

			for (Component x : ((JFrame) _rootComponent)
					.getContentPane().getComponents()) {

				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = printImageFromComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
				
			}
			
		} else if (_rootComponent instanceof Panel) {

			for (Component x : ((Panel) _rootComponent).getComponents()) {


				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = printImageFromComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
			}
			
		} else if (_rootComponent instanceof Window) {

			for (Component x : ((Window) _rootComponent).getComponents()) {

				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = printImageFromComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
				
			}
		} 
		
			
			
		
		return bi_ret;
	}
}
