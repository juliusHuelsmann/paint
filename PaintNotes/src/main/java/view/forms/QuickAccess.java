package view.forms;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.settings.ViewSettings;
import model.util.Util;
import control.tabs.CQuickAccess;
import control.util.MousePositionTracker;
import view.util.mega.MLabel;
import view.util.mega.MPanel;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class QuickAccess extends MPanel {

	
	/**
	 * MLabel.
	 */
	private MLabel jlbl_background, jlbl_move, jlbl_pen, jlbl_erase, 
	jlbl_select, jlbl_outsideTop, jlbl_outsideRight,  jlbl_outsideLeft, 
	jlbl_outsideBottom;
	
	
	/**
	 * 
	 */
	private final int sizeItem = 40;
	
	/**
	 * 
	 */
	private static QuickAccess instance;
	
	/**
	 * 
	 */
	public QuickAccess() {
		super();
		super.setLayout(null);
		super.setOpaque(false);
		MousePositionTracker mpt = new MousePositionTracker(this);
		super.addMouseListener(mpt);
		super.addMouseMotionListener(mpt);
		super.setSize(200, 200);

		jlbl_move = new MLabel();
		jlbl_move.setSize(sizeItem, sizeItem);
		jlbl_move.setOpaque(false);
		jlbl_move.setIcon(
				new ImageIcon(
						Util.resize("centerResize.png", sizeItem, sizeItem)));
		jlbl_move.setLocation(getWidth() / 2- sizeItem / 2, getHeight() / 5);
		super.add(jlbl_move);
		

		jlbl_pen = new MLabel();
		jlbl_pen.setSize(sizeItem, sizeItem);
		jlbl_pen.setOpaque(false);
		jlbl_pen.setIcon(
				new ImageIcon(
						Util.resize("centerResize.png", sizeItem, sizeItem)));
		jlbl_pen.setLocation(getWidth() / 2- sizeItem / 2, 
				 getHeight() - getHeight() / 5 - sizeItem);
		super.add(jlbl_pen);

		jlbl_erase = new MLabel();
		jlbl_erase.setSize(sizeItem, sizeItem);
		jlbl_erase.setOpaque(false);
		jlbl_erase.setIcon(
				new ImageIcon(
						Util.resize("icon/save.png", sizeItem, sizeItem)));
		jlbl_erase.setLocation(getWidth() / 5 , getHeight() / 2 - sizeItem / 2);
		super.add(jlbl_erase);

		jlbl_select = new MLabel();
		jlbl_select.setSize(sizeItem, sizeItem);
		jlbl_select.setOpaque(false);
		jlbl_select.setIcon(
				new ImageIcon(
						Util.resize("centerResize.png", sizeItem, sizeItem)));
		jlbl_select.setLocation(getWidth() - getWidth() / 5 - sizeItem, getHeight() / 2 - sizeItem / 2);
		super.add(jlbl_select);
		

		jlbl_outsideTop = new MLabel();
		jlbl_outsideTop.setSize(getSize());
		jlbl_outsideTop.setOpaque(false);
		super.add(jlbl_outsideTop);

		jlbl_outsideLeft= new MLabel();
		jlbl_outsideLeft.setSize(getSize());
		jlbl_outsideLeft.setOpaque(false);
		jlbl_outsideLeft.addMouseListener(CQuickAccess.getInstance());
		super.add(jlbl_outsideLeft);

		jlbl_outsideRight = new MLabel();
		jlbl_outsideRight.setSize(getSize());
		jlbl_outsideRight.setOpaque(false);
		super.add(jlbl_outsideRight);

		jlbl_outsideBottom = new MLabel();
		jlbl_outsideBottom.setSize(getSize());
		jlbl_outsideBottom.setOpaque(false);
		super.add(jlbl_outsideBottom);
		
		jlbl_background = new MLabel();
		jlbl_background.setSize(getSize());
		jlbl_background.setOpaque(false);
		super.add(jlbl_background);
		
		drawCircle(jlbl_background);
		
	}
	
	
	
	public static final QuickAccess getInstance() {
		if (instance == null){
			instance = new QuickAccess();
		}
		return instance;
	}
	
	
	/**
	 * 
	 * @param _jlbl
	 */
	private final void drawCircle(JLabel _jlbl) {
		
		//the bufferedImage for the center circle.
		BufferedImage bi = new BufferedImage(
				_jlbl.getWidth(), 
				_jlbl.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);

		//the bufferedImages for the outside stuff.
		BufferedImage bi_outsideTop = new BufferedImage(
				_jlbl.getWidth(), 
				_jlbl.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);
		
		BufferedImage bi_outsideBottom= new BufferedImage(
				_jlbl.getWidth(), 
				_jlbl.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);
		
		BufferedImage bi_outsideLeft = new BufferedImage(
				_jlbl.getWidth(), 
				_jlbl.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);
		
		BufferedImage bi_outsideRight = new BufferedImage(
				_jlbl.getWidth(), 
				_jlbl.getHeight(), 
				BufferedImage.TYPE_INT_ARGB);
	
		final int strokeDistance = 10;
		
		
		//go through the width and the height values and calculate the 
		//coordinates with origin at center.
		for (int h = 0; h < bi.getWidth(); h ++) {
			double y = 1.0 *  (bi.getHeight() / 2 - h);
			for (int w = 0; w < bi.getWidth(); w ++) {
				double x = 1.0 *  (bi.getWidth() / 2 - w);

				// the radius of the circles
				double r1 = bi.getWidth() / 2;
				double r2 = bi.getWidth() / 2 - 20;
				double r3 = bi.getWidth() / 2 - 30;
				
				
				//if inside the outer circle (thus there is the possibility
				//to be inside the other circles)
				if (Math.pow(x, 2) - Math.pow(r1, 2) <= -Math.pow(y, 2)) {

					//if in the ring outside
					if (Math.pow(x, 2) - Math.pow(r2, 2) >= -Math.pow(y, 2)) {
						
						
						
						//division into four pieces
						if (w == h || w == bi.getWidth() - h) {

							bi.setRGB(w, h, Color.lightGray.getRGB());
						} else {
						

							if (w > h && w < bi.getWidth() - h) {

								if ( (w + h) % strokeDistance == 0
				            			||  (w - h) % strokeDistance == 0) {

									// lines
									
									bi_outsideTop.setRGB(w, h, new Color(220,230,250).getRGB());
				            	} else 
			            		//top quadrant
			                	bi_outsideTop.setRGB(w, h,ViewSettings
			                			.GENERAL_CLR_BACKGROUND
			                			.getRGB());	
							} else  if (w < h) {
								
								if ( h < bi.getWidth() - w) {

									if ( (w + h) % strokeDistance == 0
					            			||  (w - h) % strokeDistance == 0) {

										// lines
										
										bi_outsideLeft.setRGB(w, h, new Color(220,230,250).getRGB());
					            	} else 
									//left
									bi_outsideLeft.setRGB(w, h,ViewSettings
				                			.GENERAL_CLR_BACKGROUND
				                			.getRGB());	
								} else {

									if ( (w + h) % strokeDistance == 0
					            			||  (w - h) % strokeDistance == 0) {

										// lines
										
										bi_outsideBottom.setRGB(w, h, new Color(220,230,250).getRGB());
					            	} else 
									//bottom
									bi_outsideBottom.setRGB(w, h,ViewSettings
				                			.GENERAL_CLR_BACKGROUND
				                			.getRGB());	
								}
							} else  if (w > bi.getWidth() - h) {
								

								if ( (w + h) % strokeDistance == 0
				            			||  (w - h) % strokeDistance == 0) {

									// lines
									
									bi_outsideRight.setRGB(w, h, new Color(220,230,250).getRGB());
				            	} else 
								//right
								bi_outsideRight.setRGB(w, h,ViewSettings
			                			.GENERAL_CLR_BACKGROUND
			                			.getRGB());	
							} 
						
						}
		            			
						
//	                	bi.setRGB(w, h, ViewSettings.GENERAL_CLR_BACKGROUND.getRGB());
					} else if (Math.pow(x, 2) - Math.pow(r3, 2) 
							<= -Math.pow(y, 2)) {
						

						if (w == h || w == bi.getWidth()-h) {

							bi.setRGB(w, h, Color.lightGray.getRGB());
						} else if ( (w + h) 
								% strokeDistance == 0
								||  (w - h) % strokeDistance == 0) {

							bi.setRGB(w, h, new Color(220,230,250).getRGB());
						} else {
							final Color clr_hightlighted = new Color(
									ViewSettings.GENERAL_CLR_BACKGROUND_DARK
		            				.getRed() - 11, 
		            				ViewSettings.GENERAL_CLR_BACKGROUND_DARK
		            				.getGreen() - 11, 
		            				ViewSettings.GENERAL_CLR_BACKGROUND_DARK
		            				.getBlue() - 12);
		            		
							if (w > h && w < bi.getWidth() - h) {

			            		//top quadrant
			                	bi.setRGB(w, h,clr_hightlighted.getRGB());	
							} else  if (w < h) {
								
								if ( h < bi.getWidth() - w) {

									//left
				                	bi.setRGB(w, h,ViewSettings
				                			.GENERAL_CLR_BACKGROUND_DARK
				                			.getRGB());	
								} else {

									//bottom
				                	bi.setRGB(w, h,ViewSettings
				                			.GENERAL_CLR_BACKGROUND_DARK
				                			.getRGB());	
								}
							} else  if (w > bi.getWidth() - h) {
								
								//rigth
			                	bi.setRGB(w, h,ViewSettings
			                			.GENERAL_CLR_BACKGROUND_DARK
			                			.getRGB());	
							} 
		            	}
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
			double myX1 = Math.sqrt(Math.abs(myY * myY - Math.pow(bi.getWidth() / 2, 2) - bi.getWidth() / 2));

			myX1 += bi.getHeight() / 2;
			myY += bi.getHeight() / 2;
				//x² + y² <= r²

			myX1 -= 1;
			while (myY >= bi.getHeight()){
				myY --;
			}
			while (myY < 0){
				myY ++;
			}
			if (myX1 >= 0 && myY >= 0 && myX1 < bi.getWidth() && myY < bi.getHeight())
          	bi.setRGB((int) myX1, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
			
			myX1 +=1;
			int myX2 = -(int) (myX1 -bi.getHeight());

			if (myX2 >= 0 && myY >= 0 && myX2 < bi.getWidth() && myY < bi.getHeight())
          	bi.setRGB((int) myX2, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
		}
		
		
		
		
		
		

		for (double Xh = 0; Xh < bi.getWidth(); Xh += 0.1) {

			double rad2 = bi.getWidth() / 2 - 20;
			double myY = 1.0 *  (bi.getHeight() / 2 - Xh);
			double myX1 = Math.sqrt(Math.abs(myY * myY - Math.pow(rad2, 2) - bi.getWidth() / 2));

			myX1 += bi.getHeight() / 2;
			myY += bi.getHeight() / 2;
				//x² + y² <= r²

			myX1 -= 1;
			if (myX1 >= 0 && myY >= 0 && myX1 < bi.getWidth() && myY < bi.getHeight()) {

				if (myY > 20 && myY < bi.getHeight() - 20){

		          	bi.setRGB((int) myX1, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
				}
			}
			
			myX1 +=1;
			int myX2 = -(int) (myX1 -bi.getHeight());

			if (myX2 >= 0 && myY >= 0 && myX2 < bi.getWidth() && myY < bi.getHeight()) {

				if (myY > 20 && myY < bi.getHeight() - 20){

		          	bi.setRGB((int) myX2, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
				}
			}
		}
		
		

		for (double Xh = 0; Xh < bi.getWidth(); Xh += 0.1) {

			double rad3 = bi.getWidth() / 2 - 30;
			double myY = 1.0 *  (bi.getHeight() / 2 - Xh);
			double myX1 = Math.sqrt(Math.abs(myY * myY - Math.pow(rad3, 2) - bi.getWidth() / 2));

			myX1 += bi.getHeight() / 2;
			myY += bi.getHeight() / 2;
				//x² + y² <= r²

			myX1 -= 1;
			if (myX1 >= 0 && myY >= 0 && myX1 < bi.getWidth() && myY < bi.getHeight()) {

				if (myY < 30 || myY > bi.getHeight() - 30){
					
				} else {

	    				bi.setRGB((int) myX1, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
					}
			}
			
			myX1 +=1;
			int myX2 = -(int) (myX1 -bi.getHeight());

			if (myX2 >= 0 && myY >= 0 && myX2 < bi.getWidth() && myY < bi.getHeight())
			{
				if (myY < 30 || myY > bi.getHeight() - 30){
					
				} else

	          	bi.setRGB((int) myX2, (int) myY, ViewSettings.GENERAL_CLR_BORDER.getRGB());
			}
		}
		
		

		_jlbl.setIcon(new ImageIcon(bi));
		jlbl_outsideBottom.setIcon(new ImageIcon(bi_outsideBottom));
		jlbl_outsideTop.setIcon(new ImageIcon(bi_outsideTop));
		jlbl_outsideRight.setIcon(new ImageIcon(bi_outsideRight));
		jlbl_outsideLeft.setIcon(new ImageIcon(bi_outsideLeft));
	}

	
	public void openLeft() {
		for (int i = 0; i < 25; i++) {

			jlbl_outsideLeft.setLocation(-i, 0);
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	/**
	 * @return the jlbl_outsideBottom
	 */
	public MLabel getJlbl_outsideBottom() {
		return jlbl_outsideBottom;
	}



	/**
	 * @param jlbl_outsideBottom the jlbl_outsideBottom to set
	 */
	public void setJlbl_outsideBottom(MLabel jlbl_outsideBottom) {
		this.jlbl_outsideBottom = jlbl_outsideBottom;
	}



	/**
	 * @return the jlbl_outsideRight
	 */
	public MLabel getJlbl_outsideRight() {
		return jlbl_outsideRight;
	}



	/**
	 * @param jlbl_outsideRight the jlbl_outsideRight to set
	 */
	public void setJlbl_outsideRight(MLabel jlbl_outsideRight) {
		this.jlbl_outsideRight = jlbl_outsideRight;
	}



	/**
	 * @return the jlbl_outsideLeft
	 */
	public MLabel getJlbl_outsideLeft() {
		return jlbl_outsideLeft;
	}



	/**
	 * @param jlbl_outsideLeft the jlbl_outsideLeft to set
	 */
	public void setJlbl_outsideLeft(MLabel jlbl_outsideLeft) {
		this.jlbl_outsideLeft = jlbl_outsideLeft;
	}



	/**
	 * @return the jlbl_outsideTop
	 */
	public MLabel getJlbl_outsideTop() {
		return jlbl_outsideTop;
	}



	/**
	 * @param jlbl_outsideTop the jlbl_outsideTop to set
	 */
	public void setJlbl_outsideTop(MLabel jlbl_outsideTop) {
		this.jlbl_outsideTop = jlbl_outsideTop;
	}
}
