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
				"http://juliushuelsmann.github.io/paint/issue.html");
		try {
			
			myEditorPane.setPage(pageName);
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
	}

}
