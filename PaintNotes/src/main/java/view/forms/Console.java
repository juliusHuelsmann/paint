package view.forms;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JScrollBar;
import javax.swing.JTextArea;

import control.util.MousePositionTracker;
import model.settings.ViewSettings;
import view.util.mega.MPanel;
import view.util.mega.MScrollPane;


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
	private MScrollPane scrollPane;
	
	/**
	 * The only instance of this singleton class.
	 */
	private static Console instance;

	/**
	 * Different IDs for different console outputs.
	 */
	public static final int ID_ERROR = 0, 
			ID_WARNING = 1, 
			ID_INFO_IMPORTANT = 2,
			ID_INFO_MEDIUM = 3,
			ID_INFO_UNIMPORTANT = 4; 

	/**
	 * Different IDs for different console outputs.
	 */
	public static final String STRG_ID_ERROR = "Error:\t", 
			STRG_ID_WARNING = "Warning:\t", 
			STRG_ID_INFO_IMPORTANT = "Info:\t",
			STRG_ID_INFO_MEDIUM = "Info: ",
			STRG_ID_INFO_UNIMPORTANT = "Info:\t",
					STRG_ID_POSITION = "Pos:\t"; 
			
	
	/**
	 * The id up to which the messages are printed (for not having an overfull
	 * output but only the necessary informations on screen).
	 */
	private final int id_lastPrinted = ID_INFO_UNIMPORTANT;
	
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

		scrollPane = new MScrollPane(jta_console);
		scrollPane.setVerticalScrollBarPolicy(
				MScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jpnl_container.add(scrollPane, BorderLayout.CENTER);
		jpnl_container.setSize(getSize());

		
		//set the messageID to the starting value (0)
		this.messageID = 0;
	}
	
	/**
	 * Log message with additional information on the kind of information.
	 * 
	 * @param message		The message that is printed
	 * @param _messageType	the type of the message; whether it is an error 
	 * 						message, a warning or only additional information
	 * @param _callClass	the class calling the log method
	 * @param _methodName	the name of the method calling the log method.
	 */
	public static synchronized void log(
			final String _message, final int _messageType, 
			@SuppressWarnings("rawtypes") final Class _callClass, 
			final String _methodName) {
		final String message = _message.replaceAll("\t", " ");
		
		//if the message is important enough for the current configuration
		//to be printed to screen
		if (getInstance().id_lastPrinted <= _messageType) {

			String timestamp = "" + getInstance().messageID;
			
			
			switch (_messageType) {
			case ID_ERROR:
				getInstance().jta_console.append(STRG_ID_ERROR
						+ timestamp + ":\t" + message 
						+ "\n@Class" + _callClass.getSimpleName() 
						+ "." + _methodName + "\n");
				break;
			case ID_WARNING:
				getInstance().jta_console.append(STRG_ID_WARNING
						+ timestamp + ":\t" + message
						+ "\n@Class" + _callClass.getSimpleName()
						+ "." + _methodName + "\n");
				break;
			case ID_INFO_IMPORTANT: 
				getInstance().jta_console.append(STRG_ID_INFO_IMPORTANT
						+ timestamp + ":\t" + message
						+ "\n@Class" + _callClass.getSimpleName()
						+ "." + _methodName + "\n");
				break;
			case ID_INFO_MEDIUM: 
				getInstance().jta_console.append(STRG_ID_INFO_MEDIUM
						+ timestamp + ":\t" + message
						+ "\n@Class" + _callClass.getSimpleName()
						+ "." + _methodName + "\n");
				break;
			case ID_INFO_UNIMPORTANT: 
				getInstance().jta_console.append(STRG_ID_INFO_UNIMPORTANT
						+ timestamp + ":\t" + message
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

	
	/**
	 * Log location of mouse in image. Not done in the general log method
	 * because this kinds of logging have to be deleted (thus updated if 
	 * new one appears).
	 * @param _x		x location in image
	 * @param _y		y location in image
	 */
	public void logPosition(final int _x, final int _y) {
		String text = jta_console.getText();
		
		int id_mouse = text.lastIndexOf(STRG_ID_POSITION);
		int id_others = Math.max(text.lastIndexOf(STRG_ID_INFO_MEDIUM), 
				Math.max(text.lastIndexOf(STRG_ID_INFO_IMPORTANT), 
						Math.max(text.lastIndexOf(STRG_ID_INFO_UNIMPORTANT), 
								Math.max(text.lastIndexOf(STRG_ID_WARNING), 
										text.lastIndexOf(STRG_ID_ERROR)))));
		
		// if the last logged stuff is mouse text, remove it
		if (id_mouse > id_others) {
			text = text.substring(0, id_mouse );
		}
		
		jta_console.setText(text + STRG_ID_POSITION + "IMAGE"+ "\n" + _x + "." + _y + "\n");
	}
}
