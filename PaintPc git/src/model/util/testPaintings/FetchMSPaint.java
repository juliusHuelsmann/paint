package model.util.testPaintings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import model.settings.Status;

/**
 * 
 * @author Julius Huelsmann
 *
 */
@SuppressWarnings("serial")
public class FetchMSPaint extends JFrame {

	/**
	 * Robot.
	 */
	private Robot r;

	
	/**
	 * 
	 */
	public FetchMSPaint() {
		
		//set size of view
		super();
		final int hundred = 100;
		super.setSize(hundred, hundred);
		super.setLayout(null);
		super.setLocationRelativeTo(null);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//initialize new robot
		try {
			r = new Robot();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		//button
		JButton jbtn_proceed = new JButton("proceed");
		jbtn_proceed.setBounds(0, 0, getWidth(), getHeight());
		jbtn_proceed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent _event) {
				
				
				int index = InputEvent.BUTTON1_MASK;
				final int x = 250;
				final int fife = 5;
				final int fifety = 50;
				
				
				
				for (int anzahl = 0; anzahl < fife; anzahl++) {
					
				    final int sleepTime1 = 5000;
				    final int sleepTime = 1000;
				    final int hundredFifeteen = 115;
					sleep(sleepTime1);

					r.mouseMove(hundredFifeteen + x * anzahl,
					        fifety * (2 + 2));
					r.mousePress(index);
					r.mouseRelease(index);

					sleep(sleepTime);
					
					r.mousePress(index);

					r.mouseMove(hundredFifeteen + x * anzahl, 
					        fifety * (2 + 2));
					sleep(sleepTime);
					r.mouseMove(hundredFifeteen + x * anzahl + fifety * 2, 
					        fifety * (2 + 2 + 1));
					sleep(sleepTime);
					r.mouseMove(hundredFifeteen + x * anzahl,
					        fifety * (2 + 2) +  fifety * 2);
					sleep(sleepTime);
					r.mouseMove(hundredFifeteen + x * anzahl - fifety * 2,
					        fifety * (2 + 2 + 1));
					sleep(sleepTime);
					r.mouseMove(hundredFifeteen + x * anzahl, 
					        fifety * (2 + 2));
					sleep(sleepTime);
					
					r.mouseRelease(index);
				}
			}
		});
		super.add(jbtn_proceed);

		super.setVisible(true);
		super.setResizable(false);
	}
	
	
	
	
	

	/**
	 * Rotate an image.
	 * @param _src the source.
	 * @param _degrees the degrees
	 * @return the bufferdImage
	 */
    private static BufferedImage rotateImageX(final BufferedImage _src, 
            final double _degrees) {
        AffineTransform affineTransform = AffineTransform.getRotateInstance(
                Math.toRadians(_degrees),
                _src.getWidth() / 2,
                _src.getHeight() / 2);
        BufferedImage rotatedImage = new BufferedImage(_src.getWidth(), _src
                .getHeight(), _src.getType());
        Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
        g.setTransform(affineTransform);
        g.drawImage(_src, 0, 0, null);
        return rotatedImage;
    }
    
    
    /**
     * rotate an image.
     * @param _src the source BufferedImage
     * @param _degrees the degrees to rotate
     * @param _width the width 
     * @param _height the height
     * @return the rotated bufferdImage
     */
    private static BufferedImage rotateImage(final BufferedImage _src, 
            final double _degrees, final int _width, final int _height) {
        AffineTransform affineTransform = AffineTransform.getRotateInstance(
                Math.toRadians(_degrees),
                _src.getWidth() / 2,
                _src.getHeight() / 2);
        
        BufferedImage rotatedImage = new BufferedImage(
                _width, _height, _src.getType());
        Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
        g.setTransform(affineTransform);
        g.drawImage(_src, 0, 0, null);
        return rotatedImage;
    }
	

    /**
     * sleep for a time.
     * @param _time the time
     */
	public final void sleep(final int _time) {
		try {
			Thread.sleep(_time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * the main method.
	 * @param _args the arguments of the main method.
	 */
	public static final void main(final String[] _args) {

		boolean bln = false;
        final int hundred = 100;
		
		
		if (bln) {
		    new FetchMSPaint();
		} else {
			JFrame jf = new JFrame("testing environment");
			jf.setLayout(null);
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jf.getContentPane().setBackground(Color.white);
			final JLabel [] jlbl_one = new JLabel[2];
			
			jf.addMouseListener(new MouseListener() {
                
                private Point pnt_1 = null;
                private Point pnt_2 = null;
                
				
			    @Override public void mouseReleased(final MouseEvent _event) { }
				@Override public void mousePressed(final MouseEvent _event) { }
				@Override public void mouseExited(final MouseEvent _event) { }
				@Override public void mouseEntered(final MouseEvent _event) { }
				@Override public void mouseClicked(final MouseEvent _event) {
					if (pnt_1 != null && pnt_2 != null) {
						pnt_1 = null;
						pnt_2 = null;
					}
					
					if (pnt_1 == null) {
						pnt_1 = new Point(_event.getPoint());
					} else {
						pnt_2 = new Point(_event.getPoint());
						doIt();
					}
					
				}
				//laenge = 29 pixel
				//breite = 7 pixel
				private void doIt() {

					//initialize values
					int xLength = 0;
					int yLength = 0;
					int cLength = 0;
					int degree = 0;

					
					if (pnt_1.getX() >= pnt_2.getX() 
					        && pnt_1.getY() <= pnt_2.getY()) {
						xLength = (int) (pnt_1.getX() - pnt_2.getX());
						yLength = (int) (pnt_1.getY() - pnt_2.getY());
						cLength = (int) Math.sqrt(Math.pow(xLength, 2)
						        + Math.pow(yLength, 2));
					} else if (pnt_1.getX() >= pnt_2.getX() 
					        && pnt_1.getY() >= pnt_2.getY()) {
						xLength =  (int) (pnt_1.getX() - pnt_2.getX());
						yLength =  (int) (pnt_1.getY() - pnt_2.getY());
						cLength =  (int) Math.sqrt(
						        Math.pow(xLength, 2) + Math.pow(yLength, 2));
					} else if (pnt_1.getX() <= pnt_2.getX()
					        && pnt_1.getY() <= pnt_2.getY()) {
						xLength =  (int) (pnt_1.getX() - pnt_2.getX());
						yLength =  (int) (pnt_1.getY() - pnt_2.getY());
						cLength = -(int) Math.sqrt(
						        Math.pow(xLength, 2) + Math.pow(yLength, 2));
					} else if (pnt_1.getX() <= pnt_2.getX() 
					        && pnt_1.getY() >= pnt_2.getY()) {
						xLength = (int) (pnt_1.getX() - pnt_2.getX());
						yLength = (int) (pnt_1.getY() - pnt_2.getY());
						cLength = -(int) Math.sqrt(
						        Math.pow(xLength, 2) + Math.pow(yLength, 2));
					} else {
					    Status.getLogger().severe("error");
					}
				
					if (pnt_1.getX() == pnt_2.getX() 
					        && pnt_1.getY() == pnt_2.getY()) {
					    Status.getLogger().severe("gleicher punkt");
					} else {
						double thing = 0.0000 + ((hundred * yLength) / cLength);
						degree = (int) Math.toDegrees(Math.asin(
						        thing / hundred));
						Status.getLogger().info("xLength " + xLength 
						        + "	yLength " 
						        + yLength + "	cLength " + cLength + "	degree "
						        + degree);
				
						
						if (degree < 0) {
						    final int maxDegree = 360;
							degree = maxDegree + degree;
						}
						try {
							jlbl_one[0].setBounds(0, 0, 
							        hundred * 2, hundred * 2);
							jlbl_one[0].setIcon(new ImageIcon(rotateImageX(
							        ImageIO.read(new File("paint/test.png")),
							        degree)));
							
							jlbl_one[1].setBounds(
							        0, hundred * 2, hundred * 2, hundred * 2);
							jlbl_one[1].setIcon(new ImageIcon(rotateImage(
							        ImageIO.read(new File("paint/test.png")),
							        degree, hundred, hundred)));
						} catch (Exception e) {
							e.printStackTrace();
						}						
					}
				}
			});
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jf.setSize(hundred * (2 + 2), hundred * (2 + 2));
			
			for (int i = 0; i < jlbl_one.length; i++) {
				jlbl_one[i] = new JLabel();
				jlbl_one[i].setBorder(new LineBorder(Color.pink));
				jf.add(jlbl_one[i]);
			}
			jf.setVisible(true);
		}
	}
}
