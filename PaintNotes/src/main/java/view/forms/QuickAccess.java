package view.forms;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import model.settings.ViewSettings;
import model.util.Util;
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
			jlbl_select, jlbl_outsideTop, jlbl_outsideRight, jlbl_outsideLeft,
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
	 * Constructor: initializes the graphical user interface.
	 */
	public QuickAccess(MouseListener _controlQuickAccess) {

		super();
		super.setLayout(null);
		super.setOpaque(false);
		super.setVisible(false);
		
		final MousePositionTracker mpt = new MousePositionTracker(this);
		final int size = 200;
		final int five = 5;

		super.addMouseListener(mpt);
		super.addMouseMotionListener(mpt);
		super.setSize(size, size);

		jlbl_move = new MLabel();
		jlbl_move.setSize(sizeItem, sizeItem);
		jlbl_move.setOpaque(false);
		jlbl_move.setIcon(new ImageIcon(Util.resize("centerResize.png",
				sizeItem, sizeItem)));
		jlbl_move
				.setLocation(getWidth() / 2 - sizeItem / 2, getHeight() / five);
		super.add(jlbl_move);

		jlbl_pen = new MLabel();
		jlbl_pen.setSize(sizeItem, sizeItem);
		jlbl_pen.setOpaque(false);
		jlbl_pen.setIcon(new ImageIcon(Util.resize("centerResize.png",
				sizeItem, sizeItem)));
		jlbl_pen.setLocation(getWidth() / 2 - sizeItem / 2, getHeight()
				- getHeight() / five - sizeItem);
		super.add(jlbl_pen);

		jlbl_erase = new MLabel();
		jlbl_erase.setSize(sizeItem, sizeItem);
		jlbl_erase.setOpaque(false);
		jlbl_erase.setIcon(new ImageIcon(Util.resize("icon/save.png", sizeItem,
				sizeItem)));
		jlbl_erase.setLocation(getWidth() / five, getHeight() / 2 - sizeItem
				/ 2);
		super.add(jlbl_erase);

		jlbl_select = new MLabel();
		jlbl_select.setSize(sizeItem, sizeItem);
		jlbl_select.setOpaque(false);
		jlbl_select.setIcon(new ImageIcon(Util.resize("centerResize.png",
				sizeItem, sizeItem)));
		jlbl_select.setLocation(getWidth() - getWidth() / five - sizeItem,
				getHeight() / 2 - sizeItem / 2);
		super.add(jlbl_select);

		jlbl_outsideTop = new MLabel();
		jlbl_outsideTop.setSize(getSize());
		jlbl_outsideTop.setOpaque(false);
		super.add(jlbl_outsideTop);

		jlbl_outsideLeft = new MLabel();
		jlbl_outsideLeft.setSize(getSize());
		jlbl_outsideLeft.setOpaque(false);
		jlbl_outsideLeft.addMouseListener(_controlQuickAccess);
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

		drawCircle();

		jlbl_background.addMouseListener(mpt);
		jlbl_background.addMouseMotionListener(mpt);
		jlbl_move.addMouseListener(mpt);
		jlbl_move.addMouseMotionListener(mpt);
		jlbl_pen.addMouseListener(mpt);
		jlbl_pen.addMouseMotionListener(mpt);
		jlbl_erase.addMouseListener(mpt);
		jlbl_erase.addMouseMotionListener(mpt);
		jlbl_select.addMouseListener(mpt);
		jlbl_select.addMouseMotionListener(mpt);

	}


	/**
	 * Paint components' background.
	 */
	private final void drawCircle() {

		// the bufferedImage for the center circle.
		BufferedImage bi = new BufferedImage(jlbl_background.getWidth(),
				jlbl_background.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// the bufferedImages for the outside stuff.
		BufferedImage bi_outsideTop = new BufferedImage(
				jlbl_background.getWidth(), jlbl_background.getHeight(),
				BufferedImage.TYPE_INT_ARGB),
				bi_outsideBottom = new BufferedImage(
				jlbl_background.getWidth(), jlbl_background.getHeight(),
				BufferedImage.TYPE_INT_ARGB),
				bi_outsideLeft = new BufferedImage(
				jlbl_background.getWidth(), jlbl_background.getHeight(),
				BufferedImage.TYPE_INT_ARGB),
				bi_outsideRight = new BufferedImage(
				jlbl_background.getWidth(), jlbl_background.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		final int strokeDistance = 10, r2Min = 20, r3Min = 30;

		// go through the width and the height values and calculate the
		// coordinates with origin at center.
		for (int h = 0; h < bi.getWidth(); h++) {
			double y = 1.0 * (bi.getHeight() / 2 - h);
			for (int w = 0; w < bi.getWidth(); w++) {
				double x = 1.0 * (bi.getWidth() / 2 - w);

				// the radius of the circles
				double r1 = bi.getWidth() / 2;
				double r2 = bi.getWidth() / 2 - r2Min;
				double r3 = bi.getWidth() / 2 - r3Min;
				final int i_background = new Color(220, 230, 250).getRGB();

				// if inside the outer circle (thus there is the possibility
				// to be inside the other circles)
				if (Math.pow(x, 2) - Math.pow(r1, 2) <= -Math.pow(y, 2)) {

					// if in the ring outside
					if (Math.pow(x, 2) - Math.pow(r2, 2) >= -Math.pow(y, 2)) {

						// division into four pieces
						if (w == h || w == bi.getWidth() - h) {

							bi.setRGB(w, h, Color.lightGray.getRGB());
						} else {
							if (w > h && w < bi.getWidth() - h) {
								if ((w + h) % strokeDistance == 0
										|| (w - h) % strokeDistance == 0) {
									bi_outsideTop.setRGB(w, h, i_background);
								} else {
									// top quadrant
									bi_outsideTop.setRGB(w, h, ViewSettings
											.GENERAL_CLR_BACKGROUND
											.getRGB());
								}
							} else if (w < h) {
								if (h < bi.getWidth() - w) {
									if ((w + h) % strokeDistance == 0
											|| (w - h) % strokeDistance == 0) {
										// lines
										bi_outsideLeft.setRGB(w, h,
												i_background);
									} else {

										// left
										bi_outsideLeft.setRGB(w, h, ViewSettings
												.GENERAL_CLR_BACKGROUND
												.getRGB());
									}
								} else {

									if ((w + h) % strokeDistance == 0
											|| (w - h) % strokeDistance == 0) {

										bi_outsideBottom.setRGB(
												w, h, ViewSettings
												.GENERAL_CLR_BACKGROUND
												.getRGB());
									} else {
										// bottom
										bi_outsideBottom.setRGB(w, h, 
												ViewSettings
												.GENERAL_CLR_BACKGROUND
												.getRGB());
									}
								}
							} else if (w > bi.getWidth() - h) {

								if ((w + h) % strokeDistance == 0
										|| (w - h) % strokeDistance == 0) {

									// lines	

									bi_outsideRight.setRGB(w, h, i_background);
								} else {
									
									// right
									bi_outsideRight.setRGB(w, h, ViewSettings
											.GENERAL_CLR_BACKGROUND.getRGB());
								}
							}
						}
					} else if (Math.pow(x, 2) - Math.pow(r3, 2) <= -Math.pow(y,
							2)) {

						if (w == h || w == bi.getWidth() - h) {

							bi.setRGB(w, h, Color.lightGray.getRGB());
						} else if ((w + h) % strokeDistance == 0
								|| (w - h) % strokeDistance == 0) {

							bi.setRGB(w, h, i_background);
						} else {
							final Color clr_hightlighted = new Color(
									ViewSettings.GENERAL_CLR_BACKGROUND_DARK
											.getRed() - 11,
									ViewSettings.GENERAL_CLR_BACKGROUND_DARK
											.getGreen() - 11,
									ViewSettings.GENERAL_CLR_BACKGROUND_DARK
											.getBlue() - 12);

							if (w > h && w < bi.getWidth() - h) {

								// top quadrant
								bi.setRGB(w, h, clr_hightlighted.getRGB());
							} else if (w < h) {

								if (h < bi.getWidth() - w) {

									// left
									bi.setRGB(
											w,
											h,
											ViewSettings.GENERAL_CLR_BACKGROUND_DARK
													.getRGB());
								} else {

									// bottom
									bi.setRGB(
											w,
											h,
											ViewSettings.GENERAL_CLR_BACKGROUND_DARK
													.getRGB());
								}
							} else if (w > bi.getWidth() - h) {

								// rigth
								bi.setRGB(
										w,
										h,
										ViewSettings.GENERAL_CLR_BACKGROUND_DARK
												.getRGB());
							}
						}
					}

				} else {

					bi.setRGB(w, h, new Color(0, 0, 0, 0).getRGB());
				}
			}
		}

		for (double Xh = 0; Xh < bi.getWidth(); Xh += 0.01) {

			double myY = 1.0 * (bi.getHeight() / 2 - Xh);
			// double myXold = 1.0 * (bi.getWidth() / 2 - w);
			// (bi.getWidth() / 2 - w) = sqrt(y² - (bi.getwi / 2)²)
			// w = sqrt(y² - (bi.getwi / 2)²) - bi.getwi / 2
			double myX1 = Math.sqrt(Math.abs(myY * myY
					- Math.pow(bi.getWidth() / 2, 2) - bi.getWidth() / 2));

			myX1 += bi.getHeight() / 2;
			myY += bi.getHeight() / 2;
			// x² + y² <= r²

			myX1 -= 1;
			while (myY >= bi.getHeight()) {
				myY--;
			}
			while (myY < 0) {
				myY++;
			}
			if (myX1 >= 0 && myY >= 0 && myX1 < bi.getWidth()
					&& myY < bi.getHeight())
				bi.setRGB((int) myX1, (int) myY,
						ViewSettings.GENERAL_CLR_BORDER.getRGB());

			myX1 += 1;
			int myX2 = -(int) (myX1 - bi.getHeight());

			if (myX2 >= 0 && myY >= 0 && myX2 < bi.getWidth()
					&& myY < bi.getHeight())
				bi.setRGB((int) myX2, (int) myY,
						ViewSettings.GENERAL_CLR_BORDER.getRGB());
		}

		for (double Xh = 0; Xh < bi.getWidth(); Xh += 0.1) {

			double rad2 = bi.getWidth() / 2 - 20;
			double myY = 1.0 * (bi.getHeight() / 2 - Xh);
			double myX1 = Math.sqrt(Math.abs(myY * myY - Math.pow(rad2, 2)
					- bi.getWidth() / 2));

			myX1 += bi.getHeight() / 2;
			myY += bi.getHeight() / 2;
			// x² + y² <= r²

			myX1 -= 1;
			if (myX1 >= 0 && myY >= 0 && myX1 < bi.getWidth()
					&& myY < bi.getHeight()) {

				if (myY > 20 && myY < bi.getHeight() - 20) {

					bi.setRGB((int) myX1, (int) myY,
							ViewSettings.GENERAL_CLR_BORDER.getRGB());
				}
			}

			myX1 += 1;
			int myX2 = -(int) (myX1 - bi.getHeight());

			if (myX2 >= 0 && myY >= 0 && myX2 < bi.getWidth()
					&& myY < bi.getHeight()) {

				if (myY > 20 && myY < bi.getHeight() - 20) {

					bi.setRGB((int) myX2, (int) myY,
							ViewSettings.GENERAL_CLR_BORDER.getRGB());
				}
			}
		}

		final double movespeed = 0.1;
		for (double x_h = 0; x_h < bi.getWidth(); x_h += movespeed) {

			double rad3 = bi.getWidth() / 2 - r3Min;
			double myY = 1.0 * (bi.getHeight() / 2 - x_h);
			double myX1 = Math.sqrt(Math.abs(myY * myY - Math.pow(rad3, 2)
					- bi.getWidth() / 2));

			myX1 += bi.getHeight() / 2;
			myY += bi.getHeight() / 2;
			// x² + y² <= r²

			myX1 -= 1;
			if (myX1 >= 0 && myY >= 0 && myX1 < bi.getWidth()
					&& myY < bi.getHeight()) {

				if (myY < r3Min || myY > bi.getHeight() - r3Min) {

				} else {

					bi.setRGB((int) myX1, (int) myY,
							ViewSettings.GENERAL_CLR_BORDER.getRGB());
				}
			}

			myX1 += 1;
			int myX2 = -(int) (myX1 - bi.getHeight());

			if (myX2 >= 0 && myY >= 0 && myX2 < bi.getWidth()
					&& myY < bi.getHeight()) {
				if (myY < r3Min || myY > bi.getHeight() - r3Min) {

				} else {
					bi.setRGB((int) myX2, (int) myY,
							ViewSettings.GENERAL_CLR_BORDER.getRGB());
				}
			}
		}

		jlbl_background.setIcon(new ImageIcon(bi));
		jlbl_outsideBottom.setIcon(new ImageIcon(bi_outsideBottom));
		jlbl_outsideTop.setIcon(new ImageIcon(bi_outsideTop));
		jlbl_outsideRight.setIcon(new ImageIcon(bi_outsideRight));
		jlbl_outsideLeft.setIcon(new ImageIcon(bi_outsideLeft));
	}

	/**
	 * GUI - open animation.
	 */
	public final void openLeft() {

		final int maxSteps = 25;

		for (int i = 0; i < maxSteps; i++) {

			jlbl_outsideLeft.setLocation(-i, 0);
			try {
				Thread.sleep(maxSteps);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the jlbl_outsideBottom
	 */
	public final MLabel getJlbl_outsideBottom() {
		return jlbl_outsideBottom;
	}

	/**
	 * @param _jlbl_outsideBottom
	 *            the jlbl_outsideBottom to set
	 */
	public final void setJlbl_outsideBottom(final MLabel _jlbl_outsideBottom) {
		this.jlbl_outsideBottom = _jlbl_outsideBottom;
	}

	/**
	 * @return the jlbl_outsideRight
	 */
	public final MLabel getJlbl_outsideRight() {
		return jlbl_outsideRight;
	}

	/**
	 * @param _jlbl_outsideRight
	 *            the jlbl_outsideRight to set
	 */
	public final void setJlbl_outsideRight(final MLabel _jlbl_outsideRight) {
		this.jlbl_outsideRight = _jlbl_outsideRight;
	}

	/**
	 * @return the jlbl_outsideLeft
	 */
	public final MLabel getJlbl_outsideLeft() {
		return jlbl_outsideLeft;
	}

	/**
	 * @param _jlbl_outsideLeft
	 *            the jlbl_outsideLeft to set
	 */
	public final void setJlbl_outsideLeft(final MLabel _jlbl_outsideLeft) {
		this.jlbl_outsideLeft = _jlbl_outsideLeft;
	}

	/**
	 * @return the jlbl_outsideTop
	 */
	public final MLabel getJlbl_outsideTop() {
		return jlbl_outsideTop;
	}

	/**
	 * @param _jlbl_outsideTop
	 *            the jlbl_outsideTop to set
	 */
	public final void setJlbl_outsideTop(final MLabel _jlbl_outsideTop) {
		this.jlbl_outsideTop = _jlbl_outsideTop;
	}
}
