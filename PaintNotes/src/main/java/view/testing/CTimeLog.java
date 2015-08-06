package view.testing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 * Controller class for small time-logging tool.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class CTimeLog implements ActionListener {
	
	
	/**
	 * 
	 */
	private MTimeLog model;
	
	/**
	 * 
	 */
	private VTimeLog view;
	
	
	/**
	 * The Thread.
	 */
	private Thread t_time;
	
	
	/**
	 * Constructor.
	 * @param _vlt the view class for time-logging.
	 */
	public CTimeLog(final VTimeLog _vlt) {
		this.view = _vlt;
		this.model = new MTimeLog();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final void actionPerformed(final ActionEvent _event) {

		if (_event.getSource().equals(view.getJbtn_save())) {
			
			JFileChooser jf = new JFileChooser();
			int returnVal = jf.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				model.save(jf.getSelectedFile().getAbsolutePath());
			} else {
				System.out.println("error saveing");
			}
			
		} else if (_event.getSource().equals(view.getJbtn_load())) {

			JFileChooser jf = new JFileChooser();
			int returnVal = jf.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				MTimeLog mtls = model.load(
						jf.getSelectedFile().getAbsolutePath());
				if (mtls != null) {
					model = mtls;
					view.updateCombobox(model.getTitles());
					view.updateTable(model.getTable());
				}
			}
			
		} else if (_event.getSource().equals(view.getJbtn_addItem())) {
			String s = JOptionPane.showInputDialog("Please insert the name "
					+ "of the new activity", "activity");
			model.addTimeItem(s);
			view.updateCombobox(model.getTitles());
			view.updateTable(model.getTable());
		} else if (_event.getSource().equals(view.getJbtn_startLog())) {

			String s = (String) view.getJcb_currentOperation()
					.getSelectedItem();
			int index = view.getJcb_currentOperation().getSelectedIndex();
			if (s == null) {
				JOptionPane.showMessageDialog(view, "no activity selected.");
				
			} else {
				final MTime selectedMTime = model.getPerIndex(index);
				if (t_time == null) {
					t_time  = new Thread() {
						
						
						@SuppressWarnings("deprecation")
						public void run() {

							view.getJbtn_startLog().setText("stop log");
							view.getJbtn_addItem().setEnabled(false);
							view.getJbtn_load().setEnabled(false);
							view.getJbtn_save().setEnabled(false);
							view.getJcb_currentOperation().setEnabled(false);
							view.getJlbl_start().setText("Activity" 
							+ selectedMTime.getStrg_title() + ": Start: " 
									+ new Date().getHours() + ":"
									+ new Date().getMinutes() + ":" 
									+ new Date().getSeconds());
							double startTime = System.currentTimeMillis();
							while (!isInterrupted()) {

								int workingTime = (int) (System
										.currentTimeMillis() - startTime) 
										/ 1000;
								view.getJlbl_time().setText("Time: " 
										+ workingTime);
								selectedMTime.setWorkingTime(workingTime);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									interrupt();
								}
								
							}
							t_time = null;
						}
					};
					t_time.start();
					
				} else {

					selectedMTime.applyWorkingTime();
					t_time.interrupt();
					view.getJbtn_startLog().setText("start log");
					view.getJbtn_addItem().setEnabled(true);
					view.getJbtn_load().setEnabled(true);
					view.getJbtn_save().setEnabled(true);
					view.getJcb_currentOperation().setEnabled(true);
					view.getJlbl_start().setText("no activty");
					view.getJlbl_time().setText(" ");
					view.updateTable(model.getTable());
				}
			}
		}
	}
	

}
