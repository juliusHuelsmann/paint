package view.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import control.forms.tabs.CTabSelection;
import model.settings.ViewSettings;
import view.util.mega.MButton;
import view.util.mega.MLabel;
import view.util.mega.MPanel;
import view.util.mega.MTextField;


/**
 * This extension of MPanel is displayed, if the user hits the MButton
 * {@link view.tabs.Selection.#getTb_changeSize()}. It contains the MTextFields
 * {@link #mtf_height} and {@link #mtf_width} for inserting the new 
 * size information.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
@SuppressWarnings("serial")
public class PopupChangeSize extends MPanel {

	
	/**
	 * The only instance of the singleton class {@link #PopupChangeSize()}.
	 */
	private static PopupChangeSize instance;
	
	/**
	 * The JLabels which contain the tile for
	 * height and width JTextfields {@link #mtf_height} and
	 * {@link #mtf_width}. 
	 */
	private MLabel mlbl_width, mlbl_height;
	
	/**
	 * The MTextFields the user enters information on the new size of the
	 * selection.
	 */
	private MTextField mtf_width, mtf_height;
	
	
	/**
	 * The description for the user.
	 */
	private JTextArea jta_description;
	
	
	/**
	 * MButtons for applying the changes or for setting the old values into the
	 * {@link #mtf_height} and {@link #mtf_width} and MButton {@link #mbtn_exit}
	 * for disposing the {@link #PopupChangeSize()}
	 */
	private MButton mbtn_reset, mbtn_apply, mbtn_exit;

	
	/**
	 * The owner of the instance of this class. Is used for calculation of the
	 * {@link #PopupChangeSize()}'s location.
	 */
	private Component owner;
	
	/**
	 *  Private Constructor. (singleton)
	 */
	private PopupChangeSize() {}
	
	/**
	 * Initializes the components of the {@link #PopupChangeSize()}
	 * @param _cts ActionListener for the MButtons.
	 */
	public void initialize(final ActionListener _cts) {

		super.setBorder(new LineBorder(Color.gray));
		super.setLayout(null);
		super.setBackground(ViewSettings.GENERAL_CLR_BACKGROUND_DARK_X);
		super.setVisible(false);
		
		final int height = 20;
		final int distanceToBorder = 5;
		final int distanceBetweenItems = 2;

		mbtn_exit = new MButton("x");
		mbtn_exit.setSize(height, height);
		mbtn_exit.addActionListener(_cts);
		super.add(mbtn_exit);
		
		jta_description = new JTextArea("Change size of selection:\n\n"
				+ "Insert the current selection's new size."
				+ "\nHit \"apply\" to apply the changes and \"reset\""
				+ "\nfor inserting the old size into the TextFields.\n");
		super.setSize(jta_description.getPreferredSize().width + distanceToBorder * 2, 
				jta_description.getPreferredSize().height + (2 + 1) * (height + distanceBetweenItems) 
				+ 2 * distanceToBorder);
		mbtn_exit.setLocation(getWidth() - distanceToBorder - height, distanceToBorder);
		jta_description.setSize(jta_description.getPreferredSize());
		jta_description.setLocation(distanceToBorder, distanceToBorder);
		jta_description.setEditable(false);
		jta_description.setFocusable(false);
		jta_description.setOpaque(false);
		super.add(jta_description);
		
		mlbl_width = new MLabel("Width");
		mlbl_width.setSize(getWidth() / 2 - distanceToBorder, height);
		mlbl_width.setLocation(distanceToBorder, jta_description.getY() + jta_description.getHeight());
		super.add(mlbl_width);

		mtf_width = new MTextField();
		mtf_width.setOpaque(false);
		mtf_width.setSize(mlbl_width.getSize());
		mtf_width.setLocation(getWidth() / 2, mlbl_width.getY());
		super.add(mtf_width);
		

		mlbl_height = new MLabel("Height");
		mlbl_height.setSize(mlbl_width.getSize());
		mlbl_height.setLocation(distanceToBorder, mlbl_width.getY() + mlbl_width.getHeight()
				+ distanceBetweenItems);
		super.add(mlbl_height);

		mtf_height = new MTextField();
		mtf_height.setOpaque(false);
		mtf_height.setSize(mlbl_height.getSize());
		mtf_height.setLocation(getWidth() / 2, mlbl_height.getY());
		super.add(mtf_height);

		mbtn_reset = new MButton("Reset");
		mbtn_reset.addActionListener(_cts);
		mbtn_reset.setSize(mlbl_height.getWidth() - distanceBetweenItems,
				mlbl_height.getHeight());
		mbtn_reset.setLocation(distanceToBorder, mlbl_height.getY() + mlbl_height.getHeight()
				+ distanceBetweenItems);
		super.add(mbtn_reset);

		mbtn_apply = new MButton("Apply");
		mbtn_apply.addActionListener(_cts);
		mbtn_apply.setSize(mbtn_reset.getSize());
		mbtn_apply.setLocation(getWidth() / 2 
				+ distanceBetweenItems, mbtn_reset.getY());
		super.add(mbtn_apply);
		
	}
	
	/**
	 * Return the only instance of the {@link #PopupChangeSize}
	 * @param _cts 			ActionListener for the MButtons.
	 * @param _owner		the view element that is the owner of the 
	 * 						component {@link #PopupChangeSize()}.
	 */
	public static PopupChangeSize getInstance(final CTabSelection _cts, 
			final Component _owner) {
		 if (instance == null) {
			 instance = new PopupChangeSize();
			 instance.initialize(_cts);
			 instance.owner = _owner;
		 }
		 return instance;
	}
	
	
	/**
	 * Display.
	 * @param _origWidth	the original width which is inserted into the
	 * 						{@link #mlbl_width}
	 * @param _origHeight	the original height which is inserted into the
	 * 						{@link #mlbl_height}
	 */
	public static void show(final int _origWidth, final int _origHeight) {

		if (instance != null) {
			instance.setLocation(
					instance.owner.getWidth() / 2 - instance.getWidth() / 2, 
					instance.owner.getHeight() / 2 - instance.getHeight() / 2);
			
			instance.setVisible(true);
			instance.mtf_width.setText(_origWidth + "");
			instance.mtf_height.setText(_origHeight + "");
			instance.setVisible(true);
		}
	}

	/**
	 * @return the mbtn_reset
	 */
	public MButton getMbtn_reset() {
		return mbtn_reset;
	}

	/**
	 * @return the mbtn_apply
	 */
	public MButton getMbtn_apply() {
		return mbtn_apply;
	}


	/**
	 * @return the mbtn_exit
	 */
	public MButton getMbtn_exit() {
		return mbtn_exit;
	}

	/**
	 * @return the mtf_width
	 */
	public MTextField getMtf_width() {
		return mtf_width;
	}


	/**
	 * @return the mtf_height
	 */
	public MTextField getMtf_height() {
		return mtf_height;
	}

	/**
	 * Abbreviation for {@link #getInstance(CTabSelection, Component)} with
	 * arguments null, null.
	 * @return the only instance of this class.
	 */
	public static PopupChangeSize getInstance() {
		return instance;
	}

}
