package test;

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
				"http://juliushuelsmann.github.io/paint/issue2.html");
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
				
				
				 HTMLDocument doc = (HTMLDocument) myEditorPane.getDocument();
			        HTMLEditorKit ekit = (HTMLEditorKit) myEditorPane.getEditorKit();
			        int offset = myEditorPane.getSelectionStart();

			        if (2.7 == 1) {

				        buildTree("", doc.getRootElements()[0]);
			            buildTree("", doc.getRootElements()[1]);
			        }

			        try {
			            ekit.insertHTML(doc, offset, "<span>ahoj</span>", 0, 0, HTML.Tag.SPAN);
			            Element ele = doc.getRootElements()[0];
			            System.out.println();
			            System.out.println(doc.getRootElements()[0].getElement(0).getElement(0).getElement(1));
			            ele = ele.getElement(1);
			            String ihtml = "<bar medium=\"#DEFAULT\" type=\"packaged\" source=\"identifier\" />";
			            ihtml="<h1> asdf </h1>";
			            String txt = 
								"<label for=\"email\" content=\"testing@gmail.com\">Email <span class=\"required\">(required)</span> <span class=\"nopublish\">(Address never made public)</span></label>";
			            doc.setInnerHTML(doc.getElement("email"), txt);
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
