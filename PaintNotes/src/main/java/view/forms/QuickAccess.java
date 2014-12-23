package view.forms;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.management.MXBean;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.settings.ViewSettings;
import control.util.MousePositionTracker;
import view.util.mega.MLabel;
import view.util.mega.MPanel;

public class QuickAccess extends MPanel {

	private MLabel jlbl_background;
	
	private static QuickAccess instance;
	public QuickAccess() {
		super();
		super.setLayout(null);
		super.setOpaque(false);
		MousePositionTracker mpt = new MousePositionTracker(this);
		super.addMouseListener(mpt);
		super.addMouseMotionListener(mpt);
		super.setSize(200, 200);
		
		jlbl_background = new MLabel();
		jlbl_background.setSize(getSize());
		jlbl_background.setOpaque(false);;
		super.add(jlbl_background);
		
		drawCircle(jlbl_background);
		
	}
	
	
	
	public static final QuickAccess getInstance() {
		if (instance == null){
			instance = new QuickAccess();
		}
		return instance;
	}
	private static final void drawCircle(JLabel _jlbl) {
		BufferedImage bi = new BufferedImage(
				_jlbl.getWidth(), 
				_jlbl.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);
	
		int topLastVal = 0;
		for (int h = 0; h < bi.getWidth(); h ++) {

			double y = 1.0 *  (bi.getHeight() / 2 - h);
			for (int w = 0; w < bi.getWidth(); w ++) {

				double x = 1.0 *  (bi.getWidth() / 2 - w);

				
				//x² + y² <= r²
		if ((x * x - bi.getWidth()  *  bi.getWidth() / 4) <= - (y * y)){


//			double val = 10;
//
//			double divisor = (int)(_jlbl.getWidth() * 2);
//			int v1 = (int) ((1.0 * x * x - bi.getWidth()  *  bi.getWidth() / 4.0) / divisor);
//			int v2 =  - (int)  (1.0 * y * y / divisor);
//			if (v1 == v2){
//
//				if (w % 10 == 0 && h <= bi.getHeight() / 2) {
//					
//					//draw line from previous topLastVal to current one
//					int dy = h -topLastVal;
//					for (int cX = 1; cX <= 10; cX ++) {
//
//						double cY = 1.0 * dy * cX / 10.0;
//
//						int newX = -cX + w + 10;
//						int newY =  (int) cY + topLastVal;
//						if (newX >= 0 && newY >= 0 && newX < bi.getWidth() && newY < bi.getHeight()) {
//
//							bi.setRGB(newX, newY, Color.black.getRGB());
//						}
//						
//					}
//					
//					topLastVal = h;
//					bi.setRGB(w, h, Color.red.getRGB());
//				}
//			} else {

				final int strokeDistance = 10;
	            		if ( (w + h) 
		            			% strokeDistance == 0
		            			||  (w - h) % strokeDistance == 0) {

		                	bi.setRGB(w, h, new Color(220,230,250).getRGB());
		            	} else {

		                	bi.setRGB(w, h, ViewSettings.GENERAL_CLR_BACKGROUND_DARK.getRGB());
		            	}
//			}
	            		
	            		
	            		
	            		
//	          for (int iX = 0; iX < bi.getWidth(); iX ++) {
//	        	  int nix = Math.abs(iX - bi.getWidth());
//	        	  int iy = nix * nix - bi.getWidth() * bi.getWidth() / 4;
//
//              	bi.setRGB(iX,(int) Math.sqrt(Math.abs(iy)) , Color.black.getRGB());
//	          }
//			

	                	
		}else {

			bi.setRGB(w, h, new Color(0, 0, 0, 0).getRGB());
		}
			}}
		
		


		for (double Xh = 0; Xh < bi.getWidth(); Xh += 0.01) {

			double myY = 1.0 *  (bi.getHeight() / 2 - Xh);
//			double myXold = 1.0 *  (bi.getWidth() / 2 - w);
//			(bi.getWidth() / 2 - w) = sqrt(y² - (bi.getwi / 2)²)
//			w =  sqrt(y² - (bi.getwi / 2)²) - bi.getwi / 2
			double myX = Math.sqrt(Math.abs(myY * myY - Math.pow(bi.getWidth() / 2, 2) - bi.getWidth() / 2));

			myX += bi.getHeight() / 2;
			myY += bi.getHeight() / 2;
				//x² + y² <= r²

			myX -= 1;
			while (myY >= bi.getHeight()){
				myY --;
			}
			while (myY < 0){
				myY ++;
			}
			if (myX >= 0 && myY >= 0 && myX < bi.getWidth() && myY < bi.getHeight())
          	bi.setRGB((int) myX, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
			
			myX +=1;
			int myX2 = -(int) (myX -bi.getHeight());

			if (myX2 >= 0 && myY >= 0 && myX2 < bi.getWidth() && myY < bi.getHeight())
          	bi.setRGB((int) myX2, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
		}
		
		
		_jlbl.setIcon(new ImageIcon(bi));
	}
}
