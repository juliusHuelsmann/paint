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

import javax.swing.text.AttributeSet;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

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
	private JTextPane jta_console;
	
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
		final int width = 600, height = 300, fontSize = 12;
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
		
		jta_console = new JTextPane();
		jta_console.setEditable(true);
		jta_console.setOpaque(true);
		jta_console.setFocusable(false);
		jta_console.setBorder(null);
		jta_console.addMouseListener(mpt);
		jta_console.addMouseMotionListener(mpt);
		jta_console.setForeground(Color.white);
		jta_console.setBackground(Color.black);
		jta_console.setFont(new Font("Droid Sans Mono", Font.ITALIC, fontSize));
//		jta_console.setTabSize(2 + 2);
//		jta_console.setLineWrap(true);
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
		
		// if the instance of console is not visible, do not log (this 
		// increases the painting speed.
		if (!getInstance().isVisible()) {
			return;
		}
		
		final String message = _message.replaceAll("\t", " ");
		
		//if the message is important enough for the current configuration
		//to be printed to screen
		if (getInstance().id_lastPrinted <= _messageType) {

			String timestamp = "" + getInstance().messageID;
			
			
			switch (_messageType) {
			case ID_ERROR:
				append(STRG_ID_ERROR, timestamp, _callClass.getSimpleName() , _methodName, message);
				break;
			case ID_WARNING:
				append(STRG_ID_WARNING, timestamp, _callClass.getSimpleName() , _methodName, message);
				break;
			case ID_INFO_IMPORTANT: 
				append(STRG_ID_INFO_IMPORTANT, timestamp, _callClass.getSimpleName() , _methodName, message);
				break;
			case ID_INFO_MEDIUM: 
				append(STRG_ID_INFO_MEDIUM, timestamp, _callClass.getSimpleName() , _methodName, message);
				break;
			case ID_INFO_UNIMPORTANT: 
				append(STRG_ID_INFO_UNIMPORTANT, timestamp, _callClass.getSimpleName() , _methodName, message);
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
	
	
	
	private static void append(final String _identifier,
			final String _timestamp, final String _classname, 
			final String _methodName, final String _message) {
		

		//
		// the colors for the different types of text
		//
		final Color clr_id = Color.red, clr_time = Color.gray, 
				clr_loc = Color.darkGray, clr_txt = Color.white;
		
		
		
		//
		// identifier which gives the importance of the message
		//
		insertColoredText(_identifier, clr_id);

		
		//
		// the time the message has been logged.
		//
		insertColoredText(_timestamp + ":\n", clr_time);
		
		
		//
		// The message which is logged.
		//
		insertColoredText(_message + "\n", clr_txt);
		
		
		//
		// The location in program code.
		//

		insertColoredText("@" + _classname
				+ "." + _methodName + "\n", clr_loc);
	}
	
	private static void insertColoredText(
			final String _text,
			final Color _c) {

        StyleContext sc = StyleContext.getDefaultStyleContext();

        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, _c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        
        
        int len = instance.jta_console.getDocument().getLength();
        instance.jta_console.setCaretPosition(len);
        instance.jta_console.setCharacterAttributes(aset, false);
        instance.jta_console.replaceSelection(_text);
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
	        instance.jta_console.setSelectionStart(id_mouse);
	        instance.jta_console.setSelectionEnd(text.length());
	        instance.jta_console.replaceSelection(STRG_ID_POSITION + "IMAGE"+ "\n" + _x + "." + _y + "\n");
		} else {

	        instance.jta_console.setCaretPosition(text.length());
	        instance.jta_console.replaceSelection(STRG_ID_POSITION + "IMAGE"+ "\n" + _x + "." + _y + "\n");
		}

	}
}
