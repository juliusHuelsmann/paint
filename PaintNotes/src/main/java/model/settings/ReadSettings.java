package model.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * reads the settings from the settings directory while program
 * launches.
 * 
 * @author Julius Huelsmann
 */
public final class ReadSettings {

	
	/**
	 * Utility class constructor.
	 */
	private ReadSettings() {
		
	}
	
	/**
	 * root directory of program. Contains all settings.
	 */
	private static final String PROGRAM_LOCATION = System
			.getProperty("user.home") + System.getProperty("file.separator")
			+ "PaintUni";

	/**
	 * location of program where the current workspace and other settings 
	 * are saved.
	 */
	private static final String PROGRAM_SETTINGS_LOCATION = PROGRAM_LOCATION 
			+ System.getProperty("file.separator") + "settings";
	

	/**
	 * Writes text _text to path _path. 
	 * @param _path location of file.
	 * @param _text text to be written into the file.
	 * 
	 * @throws IOException is thrown in case of error.
	 */
	private static void writeToFile(final String _path, final String _text)
			throws IOException {
		
		//create writer
		PrintWriter pWriter = new PrintWriter(new FileWriter(_path)); 
		
		//print text
		pWriter.println(_text); 
		pWriter.flush(); 
		
		//close writer
		pWriter.close();
	}
	
	
	/**
	 * reads text from a configuration file; if is configuration file 
	 * is not valid, return null; otherwise return configuration line.
	 * 
	 * @param _path from which _path is red.
	 * @return the text read form file @ _path.
	 * 
	 * @throws IOException is thrown in case of error.
	 */
	private static String readFromFile(final String _path)
			throws IOException {
		
		//create Reader
		FileReader fr = new FileReader(_path);
		BufferedReader br = new BufferedReader(fr);
		    
		//create string for line
		String line = "start value not null.";

		do {
			
			//read line
			line = br.readLine();
		}
		while (!line.equals("workspace location") && line != null);
		
		//read line
		line = br.readLine();
		
		//close reader
		br.close();
		fr.close();

		//return return value: in case of correct values
		return line;
	}
	
	
	/**
	 * checks whether program information on current workspace exist.
	 * @return if workspace is now set.
	 */
	public static String install() {
		boolean installed = false;
		String wsLocation = "";
		/*
		 * does program root directory exist?
		 */
		if (new File(PROGRAM_LOCATION).exists()) {
			
			//try to read 
			try {
				wsLocation = readFromFile(PROGRAM_SETTINGS_LOCATION);
				installed = new File(wsLocation).exists();
				if (!installed) {
                    wsLocation = "";
				}

			} catch (FileNotFoundException e) {
				showErrorMessage();
			} catch (IOException e) {
				showErrorMessage();
			}
		}
		
		//if settings not found.
		if (!installed) {
		
			new File(PROGRAM_LOCATION).mkdir();
			try {
				
				//create new file chooser
				JFileChooser jc = new JFileChooser();
				jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jc.setMultiSelectionEnabled(false);
				jc.showDialog(null, "Select Workspace Folder");
				
				//fetch selected file
				File f = jc.getSelectedFile();
				
				//if file selected
				if (f != null) {
					
					//if the file exists
					if (f.exists()) {
						writeToFile(PROGRAM_SETTINGS_LOCATION, 
								"workspace location\n" + f.getAbsolutePath());
					} else {

						//open message dialog
						JOptionPane.showConfirmDialog(null, "Folder does "
						+ "not exist", "Error creating workspace", JOptionPane
						.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
					}
					
					//recall install
					return install();
				}
				
			} catch (IOException e) {
				JOptionPane.showConfirmDialog(null, "Exception occured."
						+ " Try again later.",
						"Error creating workspace", JOptionPane.DEFAULT_OPTION, 
						JOptionPane.ERROR_MESSAGE, null);
			}
		}
		
		return wsLocation;
	}
	
	
	/**
	 * prints error message to screen.
	 */
	private static void showErrorMessage() {

		//print message to screen.
		JOptionPane.showConfirmDialog(null, "Exception occured." 
				+ " Try again later.", "Error loading workspace", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
	
	}
	
}
