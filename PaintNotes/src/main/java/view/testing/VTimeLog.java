package view.testing;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import view.util.mega.MComboBox;
import view.util.mega.MLabel;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class VTimeLog extends JPanel {

	
	/**
	 * JButtons for the time-log.
	 */
	private JButton jbtn_startLog, jbtn_addItem, jbtn_save, jbtn_load;
	
	
	/**
	 * The time-log actionListener.
	 */
	private ActionListener al = new CTimeLog(this);
	
	
	/**
	 * 
	 */
	private MComboBox jcb_currentOperation;
	
	
	/**
	 * 
	 */
	private MLabel jlbl_start, jlbl_time;
	

	/**
	 * 
	 */
	private JTable jtbl_content;
	
	
	/**
	 * Constructor.
	 */
	public VTimeLog() {
		super();
		super.setLayout(null);


		jbtn_save = new JButton("Save");
		jbtn_save.addActionListener(al);
		super.add(jbtn_save);
		
		jbtn_load = new JButton("Load");
		jbtn_load.addActionListener(al);
		super.add(jbtn_load);

		jbtn_addItem = new JButton("Add Item");
		jbtn_addItem.addActionListener(al);
		super.add(jbtn_addItem);
		
		jbtn_startLog = new JButton("Start Log");
		jbtn_startLog.addActionListener(al);
		super.add(jbtn_startLog);

		jlbl_start = new MLabel("");
		jlbl_start.setOpaque(false);
		jlbl_time = new MLabel("");
		jlbl_time.setOpaque(false);
		jtbl_content = new JTable(
				new String[][]{},
				new String[]{"Identifier", "Name", "Log"});
		updateCombobox(new String[]{});
		updateTable(new String[][]{});

		super.add(jlbl_start);
		super.add(jlbl_time);
		
	}
	
	
	/**
	 * Update the table and its dimensions.
	 * @param _headlines the new headlines for the table
	 */
	public final void updateTable(final String[][] _headlines) {

		
		if (jtbl_content != null) {
			remove(jtbl_content);
		}

		jtbl_content = new JTable(
				_headlines, new String[]{"Identifier", "Name", "Log"});
		jtbl_content.setEnabled(false);
		super.add(jtbl_content);
		setSize(getWidth(), getHeight());
		
	}
	
	
	/**
	 * Update the JCombobox.
	 * @param _headlines the headlines.
	 */
	public final void updateCombobox(final String[] _headlines) {
		
		if (jcb_currentOperation != null) {

			remove(jcb_currentOperation);
		}

		jcb_currentOperation = new MComboBox(_headlines);
		jcb_currentOperation.addActionListener(al);
		super.add(jcb_currentOperation);
		setSize(getWidth(), getHeight());
	}

	
	
	
	/**
	 * Apply new size to the class and its components.
	 * @param _width the width
	 * @param _height the height
	 */
	public final void setSize(final int _width, final int _height) {
		
		//set size
		super.setSize(_width, _height);

		//the height
		final int height = 20;
		
		jbtn_save.setBounds(0 * getWidth() / 2, 0, getWidth() / 2, height);
		jbtn_load.setBounds(1 * getWidth() / 2, 0, getWidth() / 2, height);
		
		jbtn_addItem.setBounds(0 * getWidth() / 2, 
				jbtn_save.getHeight() + jbtn_save.getY(),
				getWidth() / 2, height);
		jbtn_startLog.setBounds(1 * getWidth() / 2,
				jbtn_save.getHeight() + jbtn_save.getY(),
				getWidth() / 2, height);
		
		jcb_currentOperation.setBounds(0,
				jbtn_addItem.getHeight() + jbtn_addItem.getY(),
				getWidth(), height);
		
		jlbl_start.setBounds(0 * getWidth() / 2, 
				jcb_currentOperation.getHeight() 
				+ jcb_currentOperation.getY(), getWidth() / 2, height);
		jlbl_time.setBounds(1 * getWidth() / 2, 
				jcb_currentOperation.getHeight()
				+ jcb_currentOperation.getY(), getWidth() / 2, height);
		
		jtbl_content.setBounds(0, jlbl_time.getHeight() 
				+ jlbl_time.getY(), getWidth(), getHeight()
				- (jlbl_time.getHeight() + jlbl_time.getY()));
		
	}
	
	/**
	 * Main method for starting.
	 * @param _args the arguments
	 */
	public static void main(final String[] _args) {
		

		@SuppressWarnings("serial")
		JFrame jf = new JFrame("Time logger") {
			
			/**
			 * The root component of which the size is adapted.
			 */
			private Component vsp;

			public Component add(final Component _c) {
				vsp = _c;
				return super.add(_c);
			}
			
			public void setSize(final int _x, final int _y) {
				if (vsp != null) {

					vsp.setSize(_x, _y);
				}
				super.setSize(_x, _y);
			}
			
			public void validate() {
				super.validate();
				if (vsp != null) {

					vsp.setSize(getWidth(), getHeight());
				}
			}
		};

		
		final int widht = 500;
		final int height = 300;
		final VTimeLog vsp;
		vsp = new VTimeLog();
		jf.setSize(widht, height);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.setLayout(null);
		jf.setResizable(true);
		
		
		jf.add(vsp);
	}




	/**
	 * @return the jbtn_startLog
	 */
	public final JButton getJbtn_startLog() {
		return jbtn_startLog;
	}




	/**
	 * @return the jbtn_addItem
	 */
	public final JButton getJbtn_addItem() {
		return jbtn_addItem;
	}





	/**
	 * @return the jcb_currentOperation
	 */
	public final MComboBox getJcb_currentOperation() {
		return jcb_currentOperation;
	}



	/**
	 * @return the jlbl_start
	 */
	public final MLabel getJlbl_start() {
		return jlbl_start;
	}



	/**
	 * @return the jlbl_time
	 */
	public final MLabel getJlbl_time() {
		return jlbl_time;
	}



	/**
	 * @return the jbtn_save
	 */
	public final JButton getJbtn_save() {
		return jbtn_save;
	}


	/**
	 * @return the jbtn_load
	 */
	public final JButton getJbtn_load() {
		return jbtn_load;
	}
}
