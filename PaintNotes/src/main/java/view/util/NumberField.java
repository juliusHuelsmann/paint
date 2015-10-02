package view.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import view.util.mega.MTextField;

/**
 * MTextField that only accepts positive numbers.
 */
@SuppressWarnings("serial")
public class NumberField extends MTextField {

	/**
	 * MTextField that only accepts positive numbers.
	 */
	public NumberField() {
		super();
		super.addKeyListener(new KeyListener() {
			
			
			@Override
			public void keyTyped(KeyEvent e) { }
			
			@Override
			public void keyReleased(KeyEvent e) {
				String origText = getText();
				String resultingText = "";
				
				for (int i = 0; i < origText.length(); i++) {

					String currentChar;
					try {
						final int j = Integer.parseInt("" + origText.charAt(i));
						if (!(j >= 0 && j <= 9)) {
							currentChar = "";
						} else {
							currentChar = "" + j;
						}
					} catch (Exception ex) {
						currentChar = "";
					}
					resultingText += currentChar;
				}
				setText(resultingText);
			}
			
			@Override
			public void keyPressed(KeyEvent e) { }
		});
	}
}
