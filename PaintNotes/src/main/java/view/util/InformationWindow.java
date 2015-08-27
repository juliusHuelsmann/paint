package view.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * View class for showing text-information.
 * 
 * @author Julius Hülsmann
 * @version %U%, %I%
 */
@SuppressWarnings("serial")
public class InformationWindow extends JFrame {

	
	
	/**
	 * JTextArea which displays information.
	 */
	private JTextArea jta;
	
	
	
	/**
	 * JScrollPane for JTextArea.
	 */
	private JScrollPane jsp;
	
	
	
	/**
	 * The thread.
	 */
	private Thread t;
	
	
	
	/**
	 * Constructor: initializes view.
	 */
	public InformationWindow() {
		super();
		init();
	}
	
	
	
	/**
	 * Constructor: initializes view with title.
	 * @param _title the title of the window
	 */
	public InformationWindow(final String _title) {
		super(_title);
		init();
	}
	
	
	
	/**
	 * Initialization method which is called from out of the different
	 * constructors.
	 */
	private final void init() {

		// JFrame - Settings
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.setSize(50, 350);
		
		// JText-Area
		jta = new JTextArea();
		jta.setEditable(false);
		jta.setFocusable(false);
		jta.setOpaque(true);
		jta.setTabSize(3);
		jta.setBackground(new Color(95, 100, 120));
		jta.setForeground(Color.white);
		jta.setFont(new Font("American Typewriter", Font.PLAIN, 15));
		jta.setBorder(null);

		// JScrollPane
		jsp = new JScrollPane (jta, 
		   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
		   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setOpaque(false);
		jsp.setAutoscrolls(true);
		super.add(jsp);
		
		// final settings.
		this.adaptSize();
		super.setVisible(true);
	}
	
	
	
	/**
	 * Set the text of the JTextArea.
	 * @param _t the text.
	 */
	public final void setText(final String _t) {

		final boolean waitingEnabled = t != null;
		
		if (waitingEnabled) {
			stopWaiting();
		}
		jta.setText(_t);
		adaptSize();
		if (waitingEnabled) {
			startWaiting(true);
		}
	}
	
	
	
	/**
	 * Append text to the JTextArea.
	 * @param _t the text.
	 */
	private final void appendText(final String _t) {

		final boolean waitingEnabled = t != null;
		
		if (waitingEnabled) {
			stopWaiting();

		}
		jta.append(_t);
		adaptSize();
		if (waitingEnabled) {
			startWaiting(true);
		}
	}
	
	private int operationID = 0;
	public final void appendNewOperation(final String _t) {
		operationID++;
		appendText("> " + operationID + "\t" + _t + "\n");
	}
	
	
	public final void appendNewResult(final String _t) {
		appendText("\t\t" + _t + "\n");
	}
	
	
	
	/**
	 * Adapt the size.
	 */
	public final void adaptSize() {

		final int width = 50;
		super.setSize(
				(int) jta.getPreferredSize().getWidth() + width,
				(int) getHeight());
		super.setLocationRelativeTo(null);
	}
	
	
	
	/**
	 * Start waiting visualization.
	 */
	public final synchronized void startWaiting(final boolean _method1) {
		
		if (t != null) {
			stopWaiting();
		}
		t = new Thread() {
			
			
			
			public void run() {
				
				if (_method1) {
					method1();
				} else {
					method2();
				}
			}
			
			
			/**
			 * 
			 */
			@Override
			public final void interrupt() {
				setName(Thread.State.TERMINATED.toString());
			}
			
			
			public final boolean isInterrupted() {
				return getName().equals(Thread.State.TERMINATED.toString());
			}
			
			private void method2() {

				
				while (!isInterrupted()) {
					final String strg_n = "loading\t------------";
					final int startInt = 8;
					jta.append("loading\t");
					for (int i = startInt; 
							i < strg_n.length(); i++) {

						jta.append("" + strg_n.charAt(i));
						
						final int time = 100;
						try {
							Thread.sleep(time);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (jta.getText().endsWith(strg_n)) {
						jta.setText(jta.getText().substring(0, 
								jta.getText().length() - strg_n.length()));
					}
				}
				super.interrupt();
			
			}
			
			
			private void method1() {


				final String strg_basic = "loading\t";
				final int length_pendel = 4 * 15;
				jta.append(strg_basic);
				char pendelChar = '-';
				String pendel = "";
				
				while (!isInterrupted()) {
					
					for (int j = 0; j < 2; j++) {
						for (int i = 0; 
								i < length_pendel; i++) {

							if (j == 0) {
								jta.append("" + pendel + pendelChar);
								if (i % 4 == 1) {

									pendel += "-";
								} else {

									pendel += " ";
								}
							} else {
								pendel = pendel.substring(0,  pendel.length() - 1);
								jta.append(pendel + pendelChar);
							}
							
							final int time = 40;
							try {
								if (!isInterrupted()) {

									Thread.sleep(time - time * (Math.abs(i - length_pendel / 2) ) * 2 / length_pendel * 2 / 3);
								} else {
									interrupt();
								}
							} catch (InterruptedException e) {
							}
							
							if (j == 0) {

								jta.setText(jta.getText().substring(0, jta.getText().length() - pendel.length()));
							} else {

								jta.setText(jta.getText().substring(0, jta.getText().length() - pendel.length() - 1));
							}
							
						}
					}
				}
				
				if (jta.getText().endsWith(strg_basic)) {
					jta.setText(jta.getText().substring(0, 
							jta.getText().length() -(strg_basic).length()));
				} else {
					System.out.println(jta.getText());
				}
				
				super.interrupt();
			
			}
		};

		t.setName(Thread.State.NEW.toString());
		t.start();
	}
	

	
	
	
	
	
	/**
	 * Stop waiting visualization.
	 */
	public final synchronized void stopWaiting() {
		t.interrupt();

		while (!t.getState().equals(Thread.State.TERMINATED)) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		t = null;
	}
	
	
	
	/**
	 * The main method for testing purpose.
	 * @param _args the arguments of main.
	 * @throws InterruptedException 
	 */
	public final static void main(final String[] _args) {
		InformationWindow ifo = new InformationWindow();
		ifo.setText("hier ist eine neue Message\n\t>die wird dargestellt.\n");
		ifo.startWaiting(true);
		ifo.appendNewOperation("hullo");
		
		try{

			Thread.sleep(1000);
		} catch(Exception e) {
			e.printStackTrace();
		}

		
		ifo.appendNewOperation("hallo ich bin hier nicht mehr da");
		ifo.appendNewOperation("1");
		ifo.appendNewOperation("2 ");
		
	}
}
