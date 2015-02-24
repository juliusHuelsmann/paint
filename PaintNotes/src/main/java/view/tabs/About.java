//package declaration
package view.tabs;

//import declarations
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.omg.CORBA._IDLTypeStub;

import model.objects.PictureOverview;
import model.settings.Constants;
import model.settings.Status;
import model.settings.ViewSettings;
import control.tabs.CPaintObjects;
import view.util.VScrollPane;
import view.util.Item1Button;
import view.util.mega.MLabel;
import view.util.mega.MPanel;

/**
 * JPanel which shows the list of existing non-selected PaintObjects and 
 * details about them. Is created mainly for testing purpose.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public final class About extends Tab {

	private JTextArea jta_about;
	private Item1Button i1b_checkForUpdates;
	
	/**
	 * Constructor: initialize instances of Components and add special 
	 * MouseMotionListener.
	 */
	public About() {
		
		//initialize JPanel and alter settings
		super(0);
		super.setLayout(null);
		super.setOpaque(false);

		jta_about = new JTextArea("Paint Program maintained by "
				+ "Julius Hülsmann.\n\n"
				+ "Visit https://github.com/juliusHuelsmann/paint");
		jta_about.setFocusable(false);
		jta_about.setBorder(null);
		jta_about.setOpaque(false);
		jta_about.setEditable(false);
		super.add(jta_about);
		
		
		

		i1b_checkForUpdates = new Item1Button(null);

		i1b_checkForUpdates.setLocation(ViewSettings.getDistanceBetweenItems(), 
        		ViewSettings.getDistanceBetweenItems());
		i1b_checkForUpdates.setBorder(false);
        i1b_checkForUpdates.addActionListener(null);
        i1b_checkForUpdates.setActivable(false);
		super.add(i1b_checkForUpdates);
		
		
	}

	public void setSize(int _width, int _height) {
		super.setSize(_width, _height);;
		

		i1b_checkForUpdates.setLocation(5, 5);
		i1b_checkForUpdates.setSize(ViewSettings.getItemMenu1Width(), 
                ViewSettings.getItemMenu1Height());

        Print.initializeTextButtonOhneAdd(i1b_checkForUpdates,
                "check for updates",
                Constants.VIEW_TB_NEW_PATH);
		i1b_checkForUpdates.setActivable(false);
        jta_about.setLocation(i1b_checkForUpdates.getX() + i1b_checkForUpdates.getWidth() + 5, 5);
        jta_about.setSize(350, ViewSettings.getView_heightTB_visible() - 5 * 2 - 40);
	}
	
	
}
