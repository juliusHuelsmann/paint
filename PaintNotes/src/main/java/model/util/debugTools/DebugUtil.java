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

public class DebugUtil {
    
    /*
     * Utility tests for checking whether program works properly.
     */

    public static final byte 
    CHECK_OP_CONSOLE = 0,
    CHECK_OP_IMAGE = 1;
    
    public final static Dimension d_item = new Dimension(70, 10);
    public final static Dimension d_distance = new Dimension(12, 2);
    /*
     * Starts a size-component-check and prints an image
     * @param _rootComponent
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
					itemsCols * (d_item.width + d_distance.width), 
					itemsRows * (d_item.height + d_distance.height), 
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
			checkComponents(_rootComponent,  0, bi_analyze);
			
			//write image to hard drive
			try {
				ImageIO.write(bi_analyze, "png", new File(outputPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			break;
		}
	}


	
	
	/**
	 * 
	 * @param _c
	 * @param _print
	 * @param _currentColumn
	 * @param _currentRow
	 * @return
	 */
	private static Rectangle checkComponents(
			final Component _c, 
			final String _print,
			final int _currentColumn,
			final int _currentRow) {
		
		//save given values.
		int depth = _currentColumn;
		int amount = _currentRow;
		int maxDepth = _currentColumn;
		
		
		
		


		if (_c.getWidth() <= 0 || _c.getHeight() <= 0)
			System.err.println(_print 
					+ _c.getClass().getSimpleName()
					+ _c.getSize()
					);
		else {
			System.out.println(_print 
					+ _c.getClass().getSimpleName()
//					+ _c.getSize() 
					);
		}
		
		
		
		
		
		if (_c instanceof JPanel ) {
			for (Component x : ((JPanel)_c).getComponents()) {
				
				Rectangle pnt_recursiv = checkComponents(
						x, _print + "\t", depth + 1, 1);
				
				//update max depth and amount
				amount += pnt_recursiv.y;
				maxDepth = Math.max(maxDepth, pnt_recursiv.width);
			}

		} else if (_c instanceof JFrame) {

			for (Component x : ((JFrame)_c).getContentPane().getComponents()) {


				Rectangle pnt_recursiv = checkComponents(
						x, _print + "\t", depth + 1, 1);

				//update max depth
				amount += pnt_recursiv.y;
				maxDepth = Math.max(maxDepth, pnt_recursiv.width);
			}
			
		} else if (_c instanceof Panel) {

			for (Component x : ((Panel)_c).getComponents()) {


				Rectangle pnt_recursiv = checkComponents(
						x, _print + "\t", depth + 1, 1);

				//update max depth
				amount += pnt_recursiv.y;
				maxDepth = Math.max(maxDepth, pnt_recursiv.width);
			}
			
		} else if (_c instanceof Window) {

			for (Component x : ((Window)_c).getComponents()) {


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

	private static int currentEntry = 1;

	/**
	 * 
	 * @param _c
	 * @param _print
	 * @param _currentColumn
	 * @param _currentRow
	 * @return
	 */
	private static BufferedImage checkComponents(
			final Component _c, 
			final int _currentColumn,
			final BufferedImage _bi) {
		
		
		//save given values.
		BufferedImage bi_ret = _bi;

		Color clr_correct = new Color(130, 131, 133);
		Color clr_error = new Color(255, 120, 120);


		int adjC = 0 + _currentColumn * (d_item.width + d_distance.width);
		int adjR = 0 + currentEntry * (d_item.height + d_distance.height);
		
		Graphics g = bi_ret.getGraphics();


		if (_c.getWidth() <= 0 || _c.getHeight() <= 0) {

			g.setColor(clr_error);
		} else {

			g.setColor(clr_correct);
		}

		g.drawRect(adjC, adjR, d_item.width, d_item.height);
		g.setFont(new Font("", Font.PLAIN, 10));
		g.drawString(_c.getClass().getSimpleName(), adjC, 
				adjR + d_item.height);

			
			
		currentEntry++;
		
		if (_c instanceof JPanel ) {
			for (Component x : ((JPanel)_c).getComponents()) {

				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = checkComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
				
			}

		} else if (_c instanceof JFrame) {

			for (Component x : ((JFrame)_c).getContentPane().getComponents()) {

				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = checkComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
				
			}
			
		} else if (_c instanceof Panel) {

			for (Component x : ((Panel)_c).getComponents()) {


				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = checkComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
			}
			
		} else if (_c instanceof Window) {

			for (Component x : ((Window)_c).getComponents()) {

				final int vorher = currentEntry + 1;
				currentEntry--;
				bi_ret = checkComponents(
						x, _currentColumn + 1,
						_bi);
				currentEntry = Math.max(vorher, currentEntry);
				
			}
		} 
		
			
			
		
		return bi_ret;
	}
}
