package view.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import control.util.MousePositionTracker;
import model.settings.ViewSettings;
import view.util.mega.MPanel;


/**
 * Console class for printing output.
 * @author Julius Huelsmann
 *
 */
@SuppressWarnings("serial")
public final class Console extends MPanel {

	/**
	 * JTextArea which contains the logged stuff.
	 */
	private JTextArea jta_console;
	
	/**
	 * ScrollPane for textArea.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * The only instance of this singleton class.
	 */
	private static Console instance;
	
	/**
	 * Different IDs for different console outputs.
	 */
	public static final int ID_ERROR = 0, 
			ID_WARNING = 1, 
			ID_INFO_UNIMPORTANT = 2, 
			ID_INFO_MEDIUM = 3,
			ID_INFO_IMPORTANT = 4;
	
	
	/**
	 * The id up to which the messages are printed (for not having an overfull
	 * output but only the necessary informations on screen).
	 */
	private final int id_lastPrinted = ID_INFO_IMPORTANT;
	
	/**
	 * Unique identifier of message.
	 */
	private int messageID;
	
	/**
	 * Console constructor.
	 */
	private Console() {
		
		//initialize MPanel and add a MousePosition tracker.
		super();
		super.setLayout(null);
		final int width = 250, height = 300, fontSize = 12;
		final MousePositionTracker mpt = new MousePositionTracker(this);
		super.addMouseListener(mpt);
		super.addMouseMotionListener(mpt);
		super.setSize(width , height);
		super.setLocation(0, 0);
		super.setVisible(false);

		/*
		 * The text output.
		 */
		MPanel jpnl_container = new MPanel();
		jpnl_container.setLayout(new BorderLayout());
		jpnl_container.setFocusable(false);
		super.add(jpnl_container);
		
		jta_console = new JTextArea("Console output:");
		jta_console.setEditable(false);
		jta_console.setOpaque(true);
		jta_console.setFocusable(false);
		jta_console.setBorder(null);
		jta_console.addMouseListener(mpt);
		jta_console.addMouseMotionListener(mpt);
		jta_console.setForeground(Color.white);
		jta_console.setBackground(Color.black);
		jta_console.setFont(new Font("Droid Sans Mono", Font.ITALIC, fontSize));
		jta_console.setTabSize(2 + 2);
		jta_console.setLineWrap(true);
		jta_console.setFont(ViewSettings.GENERAL_FONT_ITEM_SMALL);

		scrollPane = new JScrollPane(jta_console);
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jpnl_container.add(scrollPane, BorderLayout.CENTER);
		jpnl_container.setSize(getSize());

		
		//set the messageID to the starting value (0)
		this.messageID = 0;

		
	}
	
	/**
	 * Log message with additional information on the kind of information.
	 * 
	 * @param _message		The message that is printed
	 * @param _messageType	the type of the message; whether it is an error 
	 * 						message, a warning or only additional information
	 * @param _callClass	the class calling the log method
	 * @param _methodName	the name of the method calling the log method.
	 */
	public static synchronized void log(
			final String _message, final int _messageType, 
			@SuppressWarnings("rawtypes") final Class _callClass, 
			final String _methodName) {
		
		//if the message is important enough for the current configuration
		//to be printed to screen
		if (getInstance().id_lastPrinted <= _messageType) {

			String timestamp = "" + getInstance().messageID;
			
			
			switch (_messageType) {
			case ID_ERROR:
				getInstance().jta_console.append("\nError:  " 
						+ timestamp + ":\t" + _message 
						+ "\n@Class" + _callClass.getSimpleName() 
						+ "." + _methodName + "\n");
				break;
			case ID_WARNING:
				getInstance().jta_console.append("\nWarning:" 
						+ timestamp + ":\t" + _message
						+ "\n@Class" + _callClass.getSimpleName()
						+ "." + _methodName + "\n");
				break;
			case ID_INFO_IMPORTANT: 
				getInstance().jta_console.append("\nInfo:   " 
						+ timestamp + ":\t" + _message
						+ "\n@Class" + _callClass.getSimpleName()
						+ "." + _methodName + "\n");
				break;
			case ID_INFO_MEDIUM: 
				getInstance().jta_console.append("\nInfo:   " 
						+ timestamp + ":\t" + _message
						+ "\n@Class" + _callClass.getSimpleName()
						+ "." + _methodName + "\n");
				break;
			case ID_INFO_UNIMPORTANT: 
				getInstance().jta_console.append("\nInfo:   " 
						+ timestamp + ":\t" + _message
						+ "\n@Class" + _callClass.getSimpleName()
						+ "." + _methodName + "\n");
				break;
			default:
				log("Warning: wrong identifier", 
						ID_ERROR, Console.class, "log");
				break;
			}

			//increase message ID
			getInstance().messageID++;
			
			//update location of scrollBar
			JScrollBar vertical = 
					getInstance().scrollPane.getVerticalScrollBar();
			vertical.setValue(
					vertical.getMaximum() + getInstance().getHeight());
		}
	}
	
	
	
	/**
	 * Return the only instance of this class.
	 * @return the only instance of this class
	 */
	public static Console getInstance() {
		if (instance == null) {
			instance = new Console();
		}
		
		return instance;
	}
}
