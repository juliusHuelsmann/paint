package view.forms;

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
import model.settings.Status;
import model.settings.Version;

import test.html.HtmlDoc;


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
		super.setSize(150, 75);
		myEditorPane = new JEditorPane();
		myEditorPane.setEditable(false);
//		myEditorPane.addHyperlinkListener(new HyperlinkListener() {
//			public void hyperlinkUpdate(HyperlinkEvent e) {
//		        if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
//		            myEditorPane.setToolTipText(e.getDescription());
//		        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
//		            myEditorPane.setToolTipText(null);
//		        } else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
//		            try {
//		                myEditorPane.setPage(e.getURL());
//		            } catch (IOException ex) {
//		                ex.printStackTrace();
//		            }
//		        }
//		    }
//		});
		myEditorPane.setSize(150, 75);
		myEditorPane.setOpaque(false);
		myEditorPane.setVisible(true);
		super.add(myEditorPane);
		super.setVisible(false);
	}
	
	private HtmlDoc doc;
	
	
	public void reportBug(final String _message) {

		try {
			
			//open the specified web-page
			myEditorPane.setPage(Constants.URL_BUG_PAGE);
			
			
			//wait for the page to have loaded.
			new Thread() {
				public void run() {
					
					doc = new HtmlDoc((HTMLDocument) myEditorPane.getDocument());
					
					final int maxSeconds = 20;
					boolean found = false;
					
					for (int secondsPassed = 0; 
							secondsPassed < maxSeconds
							&& !found; secondsPassed++) {

						//check whether a certain element exists
						found = (doc.getElement("colophon") != null);
						
						//sleep for a second.
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							//do nothing
							Status.getLogger().severe("interrupted bug reporting.");
						}
					}
					
					// only continue if the page has been loaded (that means that the
					// element has been found. 
					// otherwise give severe warning to console.
					if (found) {

						//if the element has been found, insert the error-elements.
						load(_message);
					} else {
						Status.getLogger().severe("Unable to communicate with" +
								" the bug server.");
					}
				}
			} .start();
			
        } catch (IOException ex) {
        	//do nothing.
        	Status.getLogger().severe("io exception while trying to report bug...");
        }
	}
	
	private void load(String _message) {
		
		//load element and offset.
		int offset = myEditorPane.getSelectionStart();


		try {
	        	
			// insert our special tag (if the tag is not bounded with 
			// non-whitespace character, nothing happens)
	        	
			String name = "namenlos",
					email = "ich@techfak.uni-paderborn.de", 
					content = "indirect reply";
			
	        	
	        	String messageTag = "<textarea rows=\"10\" id=\"comment\" name=\"comment\" title=\"Enter your comment here...\">" 
	        			+ content + "</textarea>";
	        	String emailTag = 
	        			"<input id=\"email\" name=\"email\" type=\"text\" value=\"" + email + "\" />";
	        	String nameTag = 
	        			"<input id=\"author\" name=\"author\" type=\"text\" value=\"" + name + "\" /> ";

	        	doc.insertHTML("comment-form-comment", messageTag);
	        	doc.insertHTML("emailSrc", emailTag);
	        	doc.insertHTML("nameSrc", nameTag);
	        	
	        	//remove leading and trailing minuses
//	        	doc.remove(offset, 1); //at the current position is the minus before tag inserted
//	        	doc.remove(offset + 1, 1); //the next sign is minus after new tag (the tag is nowhere)
	        	
	        	final HTMLEditorKit hek = ((HTMLEditorKit)myEditorPane.getEditorKitForContentType("text/html"));
	    		hek.setAutoFormSubmission(false);
	    		myEditorPane.addHyperlinkListener(new HyperlinkListener() {
					
					public void hyperlinkUpdate(HyperlinkEvent e) {

						setVisible(false);
						myEditorPane.setVisible(false);
						repaint();
					}
				});
	        }
	        catch (BadLocationException ble) {
	            throw new Error(ble);
	        }
	        catch (IOException ioe) {
	            throw new Error(ioe);
	        }

		setVisible(true);

	}
	
	public static void main(String[]args) {
		
		JFrame jf = new JFrame("hey");
		jf.setSize(300, 300);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(null);
		ReportError rer = new ReportError();
		jf.add(rer);
		rer.reportBug("Die erste formelle fehlermeldung");
		jf.repaint();
	}
	
	public static void buildTree(String _t, Element ele) {
		
		
		String parentName = "";
		if (ele.getParentElement() != null) {

			parentName = ele.getParentElement().getName();	
		}
		System.out.println(_t + ele.getName() + "from " + parentName);
		for (int i = 0; i < ele.getElementCount(); i++) {
			buildTree(_t + "\t", ele.getElement(i));
		}
		
		
	}

}
