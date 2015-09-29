package view.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

import model.settings.Constants;
import model.settings.ViewSettings;
import model.util.paint.Utils;
import view.util.mega.MPanel;


/**
 * Panel to which items can be added. If the <code>ScrollablePanel</code> is 
 * too small to display each of its contents, two buttons for scrolling are 
 * inserted at the left or right edge of the MPanel. 
 *  
 * @author Julius Huelsmann
 * @version %I%, %U%
 *
 */
@SuppressWarnings("serial")
public class ScrollablePanel extends MPanel {

	
	/**
	 * The height of the scrollButtons {@link #jbtn_scrollLeft} and 
	 * {@link #jbtn_scrollRight}. By default (if equal to 
	 * <code>-1</code>), it is set as the height of the 
	 * {@link #ScrollablePanel()}.
	 */
	private int heightScrollButtons = -1;
	
	/**
	 * For not being forced to recalculate the size of contents each time it
	 * is used (e.g. for scrolling) it is calculated each time something is
	 * added to the {@link #ScrollablePanel()} or its size is changed.
	 * Does not listen the size of the components. If they are changed, this
	 * value has to be updated manually by calling 
	 * {@link #calculateSizeOfContent()}.
	 */
	private Dimension lastCalculatedSizeOfContents;
	
	/**
	 * The components that are added to <code>ScrollablePanel</code> are not 
	 * directly added, but added to the contained <code>JPanel 
	 * {@link #jpnl_content}</code>. <br>
	 * <br>
	 * That is done because like this, it is much easier to move the components
	 * by just moving the jpnl_content.
	 * <br>
	 * The <code>JPanel {@link #jpnl_content} </code> contains in its location the
	 * scrolling shift inside the ScrollPanel, subtracted by the size and location
	 * of the "left" scroll button.
	 */
	private JPanel jpnl_content;
	
	/**
	 * The container of {@link #jpnl_content}.
	 */
	private JPanel jpnl_container;
	
	
	/**
	 * The scrollButtons that are set visible if the 
	 * <code>{@link #jpnl_content}</code>'s size is greater than the size of the 
	 * <code>JPanel {@link #ScrollablePanel} </code>.<br>
	 * 
	 * The bounds of the JButtons {@link #jbtn_scrollLeft} and 
	 * {@link #jbtn_scrollRight} determines the size of the 
	 * {@link #jpnl_content}.
	 * 
	 */
	private JButton jbtn_scrollRight, jbtn_scrollLeft;
	
	
	/**
	 * Constructor: calls super constructor and initializes values.
	 */
	public ScrollablePanel() {
		super();
		super.setLayout(null);
		super.setOpaque(false);
		
		this.lastCalculatedSizeOfContents = new Dimension(0, 0);
		
		jpnl_container = new JPanel();
		jpnl_container.setLayout(null);
		jpnl_container.setOpaque(false);
		super.add(jpnl_container);
		
		final int speed = 30;
		
		jpnl_content = new JPanel();
		jpnl_content.setLayout(null);
		jpnl_content.setOpaque(false);
		jpnl_container.add(jpnl_content);

		jbtn_scrollLeft = new JButton("<");
		jbtn_scrollLeft.setFont(new Font("", Font.PLAIN, 9));
		jbtn_scrollLeft.setFocusable(false);
		jbtn_scrollLeft.setContentAreaFilled(false);
		jbtn_scrollLeft.addMouseListener(new MouseListener() {
			
			private boolean pressed = false;
			@Override
			public void mouseReleased(MouseEvent e) {
				pressed = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				pressed = true;
				new Thread() {
					public void run() {
						while(pressed) {

							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							scroll(speed);
						}
					}
				}.start();				

			}
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		});
		jbtn_scrollLeft.setOpaque(false);
		super.add(jbtn_scrollLeft);

		jbtn_scrollRight = new JButton(">");
		jbtn_scrollRight.setFont(new Font("", Font.PLAIN, 9));
		jbtn_scrollRight.setFocusable(false);
		jbtn_scrollRight.setContentAreaFilled(false);
		
        AbstractBorder brdr = new MyRoundedBorder(Color.lightGray,1,15,0);

        jbtn_scrollRight.setBorder(brdr);
        jbtn_scrollLeft.setBorder(brdr);
		jbtn_scrollRight.setOpaque(false);
		jbtn_scrollRight.addMouseListener(new MouseListener() {
			
			private boolean pressed = false;
			@Override
			public void mouseReleased(MouseEvent e) {
				pressed = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				pressed = true;
				new Thread() {
					public void run() {
						while(pressed) {

							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							scroll(-speed);
						}
					}
				}.start();				

			}
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		});
		super.add(jbtn_scrollRight);
		
	}
	
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component add(Component _c) {
		Component r_comp =  jpnl_content.add(_c);
		onSizeChange();
		return r_comp;
	}
	
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(final Dimension _d) {
		super.setSize(_d);

		onSizeChange();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(final int _width, final int _height) {
		super.setSize(_width, _height);

		onSizeChange();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBounds(final int _x, final int _y, final int _width, final int _height) {
		super.setBounds(_x, _y, _width, _height);

		onSizeChange();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBounds(final Rectangle _r) {
		super.setBounds(_r);
		
		onSizeChange();
	}
	
	
	
	
	/**
	 * Computes the size of a rectangle that would include every content.
	 * @return 
	 */
	public Dimension calculateSizeOfContent() {
		
		int maxWidth = 0, maxHeight = 0;
		
		for (Component c : jpnl_content.getComponents()) {
			maxWidth = Math.max(maxWidth, c.getWidth() + c.getX());
			maxHeight = Math.max(maxHeight, c.getHeight() + c.getY());
		}
		lastCalculatedSizeOfContents = new Dimension(maxWidth, maxHeight); 
		return new Dimension(maxWidth, maxHeight);
	}
	
	
	
	private void onSizeChange() {
		
		Dimension size_wanted = calculateSizeOfContent();
		// if scrolling is necessary
		if (size_wanted.width > getWidth()) {

			
			final int widthScroll = 37;
			final int heightScroll;
			if(heightScrollButtons == -1) {
				heightScroll = getHeight();
			} else {
				heightScroll = heightScrollButtons;
			}
			
			jpnl_content.setSize(size_wanted);
			
			jbtn_scrollLeft.setBounds(0, 0, widthScroll, heightScroll);
			jbtn_scrollRight.setBounds(getWidth() - widthScroll, 0, widthScroll, heightScroll);
			jbtn_scrollLeft.setVisible(true);
			jbtn_scrollRight.setVisible(true);
			
			jpnl_container.setBounds(jbtn_scrollLeft.getX() + jbtn_scrollLeft.getWidth(), 
					0, getWidth() - jbtn_scrollLeft.getX() - jbtn_scrollLeft.getWidth()
					- jbtn_scrollRight.getWidth(),
					getHeight());
			
		} else {

			jpnl_container.setBounds(0, 0, getWidth(), getHeight());
			jpnl_content.setBounds(0, 0, getWidth(), getHeight());
			jbtn_scrollLeft.setVisible(false);
			jbtn_scrollRight.setVisible(false);
			

		}
	}
	
	
	/**
	 * Perform scrolling.
	 * @param _unit
	 */
	public void scroll(final int _unit) {
		 
		int newX = jpnl_content.getX() + _unit;
		if (newX > 0) {
			newX = 0;
		}
		final int min = -lastCalculatedSizeOfContents.width + jpnl_container.getWidth();
		if (newX < min) {
			newX = min;
		}
		
		jpnl_content.setLocation(newX, jpnl_content.getY());
	}
	
	
	
	/**
	 * Main method for testing.
	 * @param _args
	 */
	public static void mains(final String[]_args) {
		JFrame jf = new JFrame();
		jf.setSize(400, 150);
		jf.setLocationRelativeTo(null);
		jf.setLayout(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final ScrollablePanel sp = new ScrollablePanel();
		sp.setSize(300, 120);
		sp.setOpaque(true);
		sp.setBackground(Color.darkGray);
		jf.add(sp);

		JButton jbtn_add = new JButton("add");
		jbtn_add.setBounds(300, 5, 50, 50);
		jbtn_add.addActionListener(new ActionListener() {
			
			private int id = 0;
			@Override
			public void actionPerformed(ActionEvent e) {

				final int size = 100;
				JButton jbtn_add2 = new JButton("id" + id);
				jbtn_add2.setBounds(id * size, 5, size, size);
				sp.add(jbtn_add2);

				id++;
				sp.repaint();
			}
		});
		jf.add(jbtn_add);
		jf.setVisible(true);
	}





	/**
	 * @param _heightScrollButtons the heightScrollButtons to set
	 */
	public void setHeightScrollButtons(final int _heightScrollButtons) {
		this.heightScrollButtons = _heightScrollButtons;
	}
}
