package view.testing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;

import view.util.mega.MLabel;
import model.util.adt.list.List;
import model.util.adt.list.SecureList;
import model.util.adt.list.SecureListSort;

public class TestList extends JFrame {
	
	private SecureListSort<String> sls;
	private String [] s_current;
	
	private MLabel [] jlbl_listeEntry;
	private JButton jbtn_next;
	
	private final int buttonHeight = 20;
	
	
	
	public TestList(SecureListSort<String> _s) {
		super();
		final int wndWidth = 200;
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setLayout(null);
		super.setSize(wndWidth, wndWidth);
		super.setVisible(true);;
		super.setResizable(true);
		super.setLocationRelativeTo(null);
		run(_s);
	}
	
	private synchronized void run(SecureListSort<String> _s) {


		this.sls = _s;
		
		jbtn_next = new JButton();
		jbtn_next.setBounds(0, 0, getWidth(), buttonHeight);
		jbtn_next.addActionListener(new ActionListener() {
			
			public synchronized void actionPerformed(ActionEvent e) { 
				if(sls != null) {
				      synchronized(sls) {
				    	  sls.notify();
				      }
					
				}
			}
		});
		super.add(jbtn_next);
	}
	
	
	public void applyList(SecureListSort<String> _sl_new) {
		String[] s_new = _sl_new.toArrayString();

		//remove old stuff from gui.
		for (int i = 0; jlbl_listeEntry != null 
				&& i < jlbl_listeEntry.length; i++) {
			if (jlbl_listeEntry[i] != null) {
				super.remove(jlbl_listeEntry[i]);
			}
		}
		
		jlbl_listeEntry = new MLabel[s_new.length];
		for (int i = 0; i < s_new.length; i++) {
			jlbl_listeEntry[i] = new MLabel(i + ":   " + s_new[i]);
			jlbl_listeEntry[i].setOpaque(true);
			jlbl_listeEntry[i].setBounds(0,
					jbtn_next.getY() + jbtn_next.getHeight() + (i) * buttonHeight,
					jbtn_next.getWidth(), buttonHeight);
			super.add(jlbl_listeEntry[i]);
			
			if (s_current != null && i <  s_current.length) {
				if (s_current[i] == s_new[i]) {
					jlbl_listeEntry[i].setBackground(Color.green);
				} else {
					jlbl_listeEntry[i].setBackground(Color.red);
				}
			} else {
				jlbl_listeEntry[i].setBackground(Color.gray);
			}
			setSize(jbtn_next.getWidth(), buttonHeight * (s_new.length + 1));
			
		}
		repaint();
		s_current = s_new;
	}
	
	
	public static void main(String[] args) {
		

    	final SecureListSort<String> s = new SecureListSort<String>();
		final TestList tl = new TestList(s);
		new Thread() {
			public void run() {

		    	s.setSortDESC();
		    	s.testInitializeUnSortedList(30, s);
		    	tl.applyList(s);
		    	s.resort(null);
		    	tl.applyList(s);
			}
		}.start();
	}

}
