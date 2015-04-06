package view.testing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

public class VTimeLog extends JPanel {

	private JButton jbtn_startLog, jbtn_addItem, jbtn_save, jbtn_load;
	
	private ActionListener al = new CTimeLog(this);
	
	private JComboBox jcb_currentOperation;
	
	private JLabel jlbl_start, jlbl_time;
	

	private JTable jtbl_content;
	
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

		jlbl_start = new JLabel("");
		jlbl_start.setOpaque(false);
		jlbl_time = new JLabel("");
		jlbl_time.setOpaque(false);
		jtbl_content = new JTable(new String[][]{}, new String[]{"Identifier", "Name", "Log"});
		updateCombobox(new String[]{});
		updateTable(new String[][]{});

		super.add(jlbl_start);
		super.add(jlbl_time);
		
	}
	
	public void updateTable(String[][] _headlines) {

		
		if (jtbl_content != null) {
			remove(jtbl_content);
		}

		jtbl_content = new JTable(_headlines, new String[]{"Identifier", "Name", "Log"});
		jtbl_content.setEnabled(false);
		super.add(jtbl_content);
		setSize(getWidth(), getHeight());
		
	}
	
	
	public void updateCombobox(String[] _headlines) {
		
		if (jcb_currentOperation != null) {

			remove(jcb_currentOperation);
		}

		jcb_currentOperation = new JComboBox(_headlines);
		jcb_currentOperation.addActionListener(al);
		super.add(jcb_currentOperation);
		setSize(getWidth(), getHeight());
	}

	
	
	
	public void setSize(int _width, int _height) {
		super.setSize(_width, _height);

		jbtn_save.setBounds(0 * getWidth() / 2, 0, getWidth() / 2, 20);
		jbtn_load.setBounds(1 * getWidth() / 2, 0, getWidth() / 2, 20);
		
		jbtn_addItem.setBounds(0 * getWidth() / 2, jbtn_save.getHeight() + jbtn_save.getY(), getWidth() / 2, 20);
		jbtn_startLog.setBounds(1 * getWidth() / 2, jbtn_save.getHeight() + jbtn_save.getY(), getWidth() / 2, 20);
		
		jcb_currentOperation.setBounds(0, jbtn_addItem.getHeight() + jbtn_addItem.getY(), getWidth(), 20);
		
		jlbl_start.setBounds(0 * getWidth() / 2, jcb_currentOperation.getHeight() + jcb_currentOperation.getY(), getWidth() / 2, 20);
		jlbl_time.setBounds(1 * getWidth() / 2, jcb_currentOperation.getHeight() + jcb_currentOperation.getY(), getWidth() / 2, 20);
		
		jtbl_content.setBounds(0, jlbl_time.getHeight() + jlbl_time.getY(), getWidth(), getHeight() - (jlbl_time.getHeight() + jlbl_time.getY()));
		
	}
	public static void main(String[]args) {
		

		@SuppressWarnings("serial")
		JFrame jf = new JFrame("Time logger") {
			
			Component vsp;

			public Component add(Component _c) {
				vsp = _c;
				return super.add(_c);
			}
			
			public void setSize(int _x, int _y) {
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

		final VTimeLog vsp;
		vsp = new VTimeLog();
		jf.setSize(500, 300);
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
	public JButton getJbtn_startLog() {
		return jbtn_startLog;
	}




	/**
	 * @param jbtn_startLog the jbtn_startLog to set
	 */
	public void setJbtn_startLog(JButton jbtn_startLog) {
		this.jbtn_startLog = jbtn_startLog;
	}




	/**
	 * @return the jbtn_addItem
	 */
	public JButton getJbtn_addItem() {
		return jbtn_addItem;
	}




	/**
	 * @param jbtn_addItem the jbtn_addItem to set
	 */
	public void setJbtn_addItem(JButton jbtn_addItem) {
		this.jbtn_addItem = jbtn_addItem;
	}


	/**
	 * @return the jcb_currentOperation
	 */
	public JComboBox getJcb_currentOperation() {
		return jcb_currentOperation;
	}


	/**
	 * @param jcb_currentOperation the jcb_currentOperation to set
	 */
	public void setJcb_currentOperation(JComboBox jcb_currentOperation) {
		this.jcb_currentOperation = jcb_currentOperation;
	}


	/**
	 * @return the jlbl_start
	 */
	public JLabel getJlbl_start() {
		return jlbl_start;
	}


	/**
	 * @param jlbl_start the jlbl_start to set
	 */
	public void setJlbl_start(JLabel jlbl_start) {
		this.jlbl_start = jlbl_start;
	}


	/**
	 * @return the jlbl_time
	 */
	public JLabel getJlbl_time() {
		return jlbl_time;
	}


	/**
	 * @param jlbl_time the jlbl_time to set
	 */
	public void setJlbl_time(JLabel jlbl_time) {
		this.jlbl_time = jlbl_time;
	}

	/**
	 * @return the jbtn_save
	 */
	public JButton getJbtn_save() {
		return jbtn_save;
	}

	/**
	 * @param jbtn_save the jbtn_save to set
	 */
	public void setJbtn_save(JButton jbtn_save) {
		this.jbtn_save = jbtn_save;
	}

	/**
	 * @return the jbtn_load
	 */
	public JButton getJbtn_load() {
		return jbtn_load;
	}

	/**
	 * @param jbtn_load the jbtn_load to set
	 */
	public void setJbtn_load(JButton jbtn_load) {
		this.jbtn_load = jbtn_load;
	}
}
