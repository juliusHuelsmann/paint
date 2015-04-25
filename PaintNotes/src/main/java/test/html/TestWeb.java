package test.html;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class TestWeb {
	
	public static void main(String[]args) {
		
		JFrame jf = new JFrame("hey");
		jf.setSize(500, 500);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
		
		JScrollPane jsp = new JScrollPane();
		
		final JEditorPane myEditorPane = new JEditorPane();
		myEditorPane.setEditable(false); // to allow it to generate HyperlinkEvents
		myEditorPane.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent e) {
		        if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
		            myEditorPane.setToolTipText(e.getDescription());
		        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
		            myEditorPane.setToolTipText(null);
		        } else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            try {
		                myEditorPane.setPage(e.getURL());
		            } catch (IOException ex) {
		                // handle error
		                ex.printStackTrace();
		            }
		        }
		    }
		});
		
		final String pageName = (
				"http://juliushuelsmann.github.io/paint/issue3.html");
		try {
			
			myEditorPane.setPage(pageName);
			System.out.println(myEditorPane.getActions());
			
        } catch (IOException ex) {
            // handle error
            ex.printStackTrace();
        }
		myEditorPane.setSize(100, 100);
		myEditorPane.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane editorScrollPane = new JScrollPane(myEditorPane);
		editorScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setPreferredSize(new Dimension(250, 145));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		jf.add(editorScrollPane);
		jf.repaint();
		
		
		
		
		
		
		new Thread() {
			public void run() {
				
				
				//wait until loaded.
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
					HtmlDoc doc = new HtmlDoc((HTMLDocument) myEditorPane.getDocument());
//			        HTMLEditorKit ekit = (HTMLEditorKit) myEditorPane.getEditorKit();
			        int offset = myEditorPane.getSelectionStart();

			        if (2.7 == 1) {

				        buildTree("", doc.getRootElements()[0]);
			            buildTree("", doc.getRootElements()[1]);
			        }

			        try {
			        	
			        	// insert our special tag (if the tag is not bounded with 
			        	// non-whitespace character, nothing happens)
			        	
			        	
			        	String name = "jup", email = "julius@freenet.de", content = "error:\n\n1:\tAsdf\n2:\t jkl!";
			        	
			        	String messageTag = "<textarea rows=\"10\" id=\"comment\" name=\"comment\" title=\"Enter your comment here...\">" 
			        			+ content + "</textarea>";
			        	String emailTag = 
			        			"<input id=\"email\" name=\"email\" type=\"text\" value=\"" + email + "\" />";
			        	String nameTag = 
			        			"<input id=\"author\" name=\"author\" type=\"text\" value=\"" + name + "\" /> ";

			        	doc.insertHTML("comment-form-comment", messageTag);
			        	doc.insertHTML("emailSrc", emailTag);
			        	doc.insertHTML("nameSrc", nameTag);
			        	System.out.println(doc.getElement("email").getElementIndex(0));
			        	System.out.println(doc.getHl().getText(0, doc.getHl().getLength()));
			        	
			        	//remove leading and trailing minuses
			        	doc.remove(offset, 1); //at the current position is the minus before tag inserted
			        	doc.remove(offset + 1, 1); //the next sign is minus after new tag (the tag is nowhere)
			        	
			        	System.out.println();
//			            ekit.insertHTML(doc, offset, "<span>ahoj</span>", 0, 0, HTML.Tag.SPAN);
//			            Element ele = doc.getRootElements()[0];
//			            ele = ele.getElement(1);
//			            String ihtml = "<bar medium=\"#DEFAULT\" type=\"packaged\" source=\"identifier\" />";
//			            ihtml="<h1> asdf </h1>";
//			            String txt = 
//								"<label for=\"email\" content=\"testing@gmail.com\">Email <span class=\"required\">(required)</span> <span class=\"nopublish\">(Address never made public)</span></label>";
//			            doc.setOuterHTML(doc.getElement("emailDiv"), txt);
//			            doc.setInnerHTML();
			        }
			        catch (BadLocationException ble) {
			            throw new Error(ble);
			        }
			        catch (IOException ioe) {
			            throw new Error(ioe);
			        }
			}
		} .start();
		
		
		
		
		
		

       
	}
	
	private static void buildTree(String _t, Element ele) {
		
		
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
