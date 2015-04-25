package test.html;

import java.awt.Component;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTMLDocument;

public class HtmlDoc {
	
	private HTMLDocument hl;
	
	public HtmlDoc(HTMLDocument  _hl) {
		this.hl = _hl;
	}

	
	
	public void insertHTML(int offset, String htmlText) throws BadLocationException, IOException {
	    if (hl.getParser() == null)
	        throw new IllegalStateException("No HTMLEditorKit.Parser");

	    Element elem = getCurrentElement(offset);

	    //the method insertHTML is not visible
	    try {
	        Method insertHTML = javax.swing.text.html.HTMLDocument.class.getDeclaredMethod("insertHTML",
	                new Class[] {Element.class, int.class, String.class, boolean.class});
	        insertHTML.setAccessible(true);
	        insertHTML.invoke(hl, new Object[] {elem, offset, htmlText, false});
	    }
	    catch (Exception e) {
	        throw new IOException("The method insertHTML() could not be invoked", e);
	    }
	}
	
	
	

	public void insertHTML(String _parent, String htmlText) throws BadLocationException, IOException {
	    if (hl.getParser() == null)
	        throw new IllegalStateException("No HTMLEditorKit.Parser");

	    Element elem = getElement(_parent);
	    int offset = elem.getStartOffset();

	    //the method insertHTML is not visible
	    try {
	        Method insertHTML = javax.swing.text.html.HTMLDocument.class.getDeclaredMethod("insertHTML",
	                new Class[] {Element.class, int.class, String.class, boolean.class});
	        insertHTML.setAccessible(true);
	        insertHTML.invoke(hl, new Object[] {elem, offset, htmlText, false});
	    }
	    catch (Exception e) {
	        throw new IOException("The method insertHTML() could not be invoked", e);
	    }
	}
	
	
	public Element getCurrentElement(int offset) {
	    ElementIterator ei = new ElementIterator(hl);
	    Element elem, currentElem = null;
	    int elemLength = Integer.MAX_VALUE;

	    while ((elem = ei.next()) != null) { //looking for closest element
	        int start = elem.getStartOffset(), end = elem.getEndOffset(), len = end - start;
	        if (elem.isLeaf() || elem.getName().equals("html"))
	            continue;
	        if (start <= offset && offset < end && len <= elemLength) {
	            currentElem = elem;
	            elemLength = len;
	        }
	    }

	    return currentElem;
	}



	/**
	 * {@inheritDoc}
	 */
	public Element[] getRootElements() {
		return hl.getRootElements();
	}



	/**
	 * {@inheritDoc}
	 */
	public void remove(int offs, int len) throws BadLocationException {
		hl.remove(offs, len);
	}



	/**
	 * {@inheritDoc}
	 */
	public Element getElement(String string) {
		return hl.getElement(string);
	}



	/**
	 * {@inheritDoc}
	 */
	public HTMLDocument getHl() {
		return hl;
	}



	/**
	 * {@inheritDoc}
	 */
	public void setHl(HTMLDocument hl) {
		this.hl = hl;
	}

}
