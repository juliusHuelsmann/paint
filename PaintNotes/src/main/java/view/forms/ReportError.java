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


import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import model.settings.Constants;
import model.settings.State;
import model.util.html.HtmlDoc;


/**
 * View class for reporting error.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class ReportError extends JPanel {
	
	
	/**
	 * The JEditorPane which is the base of this class.
	 */
	private final JEditorPane myEditorPane;
	
	
	/**
	 * 
	 */
	public ReportError() {

		super();
		super.setLayout(null);
		final int width = 450, height = 975;
		super.setSize(width, height);
		myEditorPane = new JEditorPane();
		myEditorPane.setEditable(true);
		myEditorPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(final HyperlinkEvent _e) {
		        if (_e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
		            myEditorPane.setToolTipText(_e.getDescription());
		        } else if (_e.getEventType() 
		        		== HyperlinkEvent.EventType.EXITED) {
		            myEditorPane.setToolTipText(null);
		        } else if (_e.getEventType() 
		        		== HyperlinkEvent.EventType.ACTIVATED) {
		            try {
		                myEditorPane.setPage(_e.getURL());
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		        }
		    }
		});
		myEditorPane.setSize(getSize());
		myEditorPane.setOpaque(false);
		myEditorPane.setVisible(true);
		super.add(myEditorPane);
		super.setVisible(false);
	}
	
	
	/**
	 * The html document.
	 */
	private HtmlDoc doc;
	
	
	
	/**
	 * Open report bug window with message in background inserted to HTML
	 * page by using the function load().
	 * @param _message	the message that is going to be reported.
	 */
	public void reportBug(final String _message) {

		try {
			
			//open the specified web-page
			myEditorPane.setPage(Constants.URL_BUG_PAGE);
			
			//wait for the page to have loaded.
			new Thread() {
				public void run() {
					
					doc = new HtmlDoc(
							(HTMLDocument) myEditorPane.getDocument());
					
					final int maxSeconds = 20;
					boolean found = false;
					
					for (int secondsPassed = 0; 
							secondsPassed < maxSeconds
							&& !found; secondsPassed++) {

						//check whether a certain element exists
						found = (doc.getElement("colophon") != null);
						
						//sleep for a second.
						try {
							final int speelpTime = 500;
							Thread.sleep(speelpTime);
						} catch (InterruptedException e) {
							//do nothing
							model.settings.State.getLogger().severe(
									"interrupted bug reporting.");
						}
					}
					
					// only continue if the page has been loaded 
					//(that means that the
					// element has been found. 
					// otherwise give severe warning to console.
					if (found) {
						System.out.println("lodaa");

						//if the element has been found, insert the error-
						//elements.
						load(_message);
					} else {
						model.settings.State.getLogger().severe("Unable to communicate with"
								+ " the bug server.");
					}
				}
			} .start();
			
        } catch (IOException ex) {
        	//do nothing.
        	State.getLogger().severe("io exception while trying "
        			+ "to report bug...");
        	ex.printStackTrace();
        }
	}
	
	
	/**
	 * Function for loading the page with a certain message.
	 * 
	 * @param _message the message.
	 */
	@SuppressWarnings("unused")
	private void load(final String _message) {
		
		//load element and offset.
		int offset = myEditorPane.getSelectionStart();


		try {
	        	
			// insert our special tag (if the tag is not bounded with 
			// non-whitespace character, nothing happens)
	        	
			String name = "namenlos",
					email = "ich@techfak.uni-paderborn.de", 
					content = "indirect reply";
			
	        	
	        	String messageTag = "<textarea rows=\"10\" id=\"comment\" "
	        			+ "name=\"comment\" title=\"Enter your comment"
	        			+ " here...\">" 
	        			+ content + "</textarea>";
	        	String emailTag = 
	        			"<input id=\"email\" name=\"email\" type=\"text\""
	        			+ " value=\"" + email + "\" />";
	        	String nameTag = 
	        			"<input id=\"author\" name=\"author\" type=\"text\" "
	        			+ "value=\"" + name + "\" /> ";

	        	doc.insertHTML("comment-form-comment", messageTag);
	        	doc.insertHTML("emailSrc", emailTag);
	        	doc.insertHTML("nameSrc", nameTag);
	        	
	        	//remove leading and trailing minuses
//	        	doc.remove(offset, 1);
	        	//at the current position is the minus before tag inserted
//	        	doc.remove(offset + 1, 1); 
	        	//the next sign is minus after new tag (the tag is nowhere)
	        	
	        	final HTMLEditorKit hek = 
	        			((HTMLEditorKit) myEditorPane
	        					.getEditorKitForContentType("text/html"));
	    		hek.setAutoFormSubmission(false);
	    		myEditorPane.addHyperlinkListener(new HyperlinkListener() {
					
					public void hyperlinkUpdate(final HyperlinkEvent _e) {

						setVisible(false);
						myEditorPane.setVisible(false);
						repaint();
					}
				});
	        } catch (BadLocationException ble) {
	            throw new Error(ble);
	        } catch (IOException ioe) {
	            throw new Error(ioe);
	        }

		setVisible(true);

	}
	
	
	/**
	 * main function.
	 * @param _args the arguments
	 */
	public static void main(final String[] _args) {
		
		JFrame jf = new JFrame("hey");
		final int defaultJFSize = 300;
		jf.setSize(defaultJFSize, defaultJFSize);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(null);
		ReportError rer = new ReportError();
		jf.add(rer);
		rer.reportBug("Die erste formelle fehlermeldung");
		jf.repaint();
	}
	
	
	
	/**
	 * Build a tree for the HTML elements of the page.
	 * Implementation is recursive.
	 * 
	 * @param _t	text string for printing the hierarchy
	 * @param _ele	the current root element
	 */
	public static void buildTree(final String _t, final Element _ele) {
		
		
		String parentName = "";
		if (_ele.getParentElement() != null) {

			parentName = _ele.getParentElement().getName();	
		}
		System.out.println(_t + _ele.getName() + "from " + parentName);
		for (int i = 0; i < _ele.getElementCount(); i++) {
			buildTree(_t + "\t", _ele.getElement(i));
		}
		
		
	}

}
