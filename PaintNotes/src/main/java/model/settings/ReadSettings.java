package model.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.CodeSource;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.util.Util;
import model.util.paint.Utils;
import start.Start;

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
	
	public static final String ID_PROGRAM_LOCATION = "workspace location";
	
	/**
	 * root directory of program. Contains all settings.
	 */
	private static final String PROGRAM_LOCATION = System
			.getProperty("user.home") + System.getProperty("file.separator")
			+ ".PaintUni/";

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
		while (!line.equals(ID_PROGRAM_LOCATION) && line != null);
		
		//read line
		line = br.readLine();
		
		//close reader
		br.close();
		fr.close();

		//return return value: in case of correct values
		return line;
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
	public static void changeOption(
			final String _operation,
			final String _newValue)
			throws IOException {
		
		//create Reader
		FileReader fr = new FileReader(PROGRAM_SETTINGS_LOCATION);
		BufferedReader br = new BufferedReader(fr);
		    
		String sumLine = "";
		String currentLine = "";
		boolean found = false;
		currentLine = br.readLine();
		while (currentLine != null){
				
			
			if (!found) {
				sumLine += currentLine + "\n";
			} else {
				found = !found;
				sumLine += _newValue + "\n";
			}
			
			// if the current line is the identifier of the current
			// operation that has to be changed.
			if (currentLine != null && currentLine.equals(_operation)) {
				found = true;
			}
			currentLine = br.readLine();
			
		}

		//close reader
		br.close();
		fr.close();
		
		FileWriter fw = new FileWriter(PROGRAM_SETTINGS_LOCATION);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(sumLine);
		bw.flush();
		bw.close();
		fw.close();
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
				System.out.println(wsLocation);
				
				installed = new File(wsLocation).exists();
				if (!installed) {
                    wsLocation = "";
				}

			} catch (FileNotFoundException e) {
				System.out.println("file not found " + e);
				showErrorMessage();
			} catch (IOException e) {
				System.out.println("io" + e);
				showErrorMessage();
			}
		}
		
		//if settings not found.
		if (!installed) {
		
			new File(PROGRAM_LOCATION).mkdir();
			try {
				
				//create new file chooser
				JFileChooser jc = new JFileChooser();

				//sets the text and language of all the components in JFileChooser
				UIManager.put("FileChooser.openDialogTitleText",
						"Open");
				UIManager.put("FileChooser.lookInLabelText",
						"LookIn");
				UIManager.put("FileChooser.openButtonText", 
						"Open");
				UIManager.put("FileChooser.cancelButtonText",
						"Cancel");
				UIManager.put("FileChooser.fileNameLabelText",
						"FileName");
				UIManager.put("FileChooser.filesOfTypeLabelText",
						"TypeFiles");
				UIManager.put("FileChooser.openButtonToolTipText",
						"OpenSelectedFile");
				UIManager.put("FileChooser.cancelButtonToolTipText",
						"Cancel");
				UIManager.put("FileChooser.fileNameHeaderText",
						"FileName");
				UIManager.put("FileChooser.upFolderToolTipText",
						"UpOneLevel");
				UIManager.put("FileChooser.homeFolderToolTipText",
						"Desktop");
				UIManager.put("FileChooser.newFolderToolTipText",
						"CreateNewFolder");
				UIManager.put("FileChooser.listViewButtonToolTipText",
						"List");
				UIManager.put("FileChooser.newFolderButtonText",
						"CreateNewFolder");
				UIManager.put("FileChooser.renameFileButtonText",
						"RenameFile");
				UIManager.put("FileChooser.deleteFileButtonText",
						"DeleteFile");
				UIManager.put("FileChooser.filterLabelText",
						"TypeFiles");
				UIManager.put("FileChooser.detailsViewButtonToolTipText",
						"Details");
				UIManager.put("FileChooser.fileSizeHeaderText",
						"Size");
				UIManager.put("FileChooser.fileDateHeaderText",
						"DateModified");
				SwingUtilities.updateComponentTreeUI(jc);
				
				

				jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jc.setMultiSelectionEnabled(false);
				
				final String informationMsg 
				= "Please select the workspace folder. \n"
						+ "By default, the new files are saved in there. \n\n"
						+ "Is changed simply by using the Save-As option.\n"
						+ "If no folder is specified, the default worspace folder \n"
						+ "is the home directory of the current user.";
				 final int response = JOptionPane.showConfirmDialog(jc, informationMsg, 
							"Select workspace folder", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.INFORMATION_MESSAGE);
				 
				final String defaultSaveLocation = 
						System
						.getProperty("user.home") + System.getProperty("file.separator");
				File f;
				if (response != JOptionPane.NO_OPTION) {

					//fetch selected file
					f = jc.getSelectedFile();
					jc.showDialog(null, "select");
				} else {
					f = new File(defaultSaveLocation); 
				}
				
				printInformation();
				
				
				if (f == null) {
					f = new File(defaultSaveLocation); 
				}
				//if file selected
				if (f != null) {
					
					//if the file exists
					if (f.exists()) {
						writeToFile(PROGRAM_SETTINGS_LOCATION, 
								ID_PROGRAM_LOCATION + "\n" + f.getAbsolutePath());
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
				System.out.println(e);
				JOptionPane.showConfirmDialog(null, "An exception occured."
						+ " Try again later.",
						"Error creating workspace", JOptionPane.DEFAULT_OPTION, 
						JOptionPane.ERROR_MESSAGE, null);
			}
		}
		
		installOSX();
		return wsLocation;
	}
	
	public static void main(String[] _args) {
		installOSX();
	}
	
	
	private static void installOSX() {
		
		
		try {

			final String name = "paint";
			final String app_name = "Paint.app";
			/*
			 * STEP 1:	Create files and folders for the info.plist
			 */
			final String filePath = "/Applications/" + app_name + "/Contents/";
			final String fileName = "Info.plist";
			final String fileContent = ""
					+ "<dict>\n"
					+ "    <key>CFBundleTypeExtensions</key>\n"
					+ "    <array>\n"
					+ "        <string>jpeg</string>\n"
					+ "        <string>png</string>\n"
					+ "        <string>gif</string>\n"
					+ "        <string>pic</string>\n"
					+ "    </array>\n"
					+ "</dict>";
			

			FileWriter fw;
			// create necessary directories
			File p = new File(filePath);
			p.mkdirs();

			//TODO: this is just a debug paraemter
			boolean overwrite = false;
			if (overwrite) {

				// Create info.plist
				fw = new FileWriter(filePath + fileName);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(fileContent);
				bw.flush();
				bw.close();
				fw.close();
			}

			
			
			/*
			 * STEP 2:	Create (executable) file which is called if the 
			 * 			application is run.
			 */
			final String app_folder_path = filePath + "MacOS/";
			final String app_file_path = app_folder_path + name;
			new File(app_folder_path).mkdirs();
			String orig_jar_file = "";
			orig_jar_file = URLDecoder.decode(
					ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");
			
			// if Eclipse on-the-fly-compiling
			final String eclipseSubstring = "target/classes/";
			if (orig_jar_file.endsWith(eclipseSubstring)) {
				orig_jar_file = orig_jar_file.substring(
						0,
						orig_jar_file.length() - eclipseSubstring.length());
				
			}
			orig_jar_file += "paint.jar";
			
			final String jar_file_path =  app_file_path + ".jar";
			final String content = "#!/bin/bash\n"
					+ "echo $1\n"
					+ "echo $2\n"
					+ "echo $3\n"
					+ "java -jar " + jar_file_path + " $@$0$1";

			// Create application file
			FileWriter fw2 = new FileWriter(app_file_path);
			BufferedWriter bw_2 = new BufferedWriter(fw2);
			bw_2.write(content);
			bw_2.flush();
			bw_2.close();
			fw2.close();


			// Make the file executable

			final String command0 = 
					"chmod a+x " + app_file_path;
			String ret = Util.executeCommandLinux(command0);
			
			

			/*
			 * Step 3:	Copy .jar file to the destination.
			 */

			final String command1 = 
					"cp " + orig_jar_file + " " + jar_file_path;
			String ret1 = Util.executeCommandLinux(command1);
			
			
			final String command2 = 
					"/System/Library/Frameworks/CoreServices.framework/Versions/"
					+ "A/Frameworks/LaunchServices.framework/Versions/A/Support/"
					+ "lsregister -f /Applications/" + app_name + "/";
			String ret2 = Util.executeCommandLinux(command1);
			
			
			final String command3 = "killall Finder";
			String ret3 = Util.executeCommandLinux(command3);

			String s = "Operation log:\n";
			System.out.println(s);
			System.out.println(ret + "\n" + ret1 +"\n" + ret2 +"\n" + ret3);
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	
	
	private void updateOSX() {
		
	}
	
	private static void printInformation() {
		
		final String os_propertyName = "os.name";
		final String propertyLinux = "Linux";

		if (System.getProperties().getProperty(os_propertyName).equals(
				propertyLinux)) {
			
			System.out.println("here i am");
		}
		
		System.out.println("ReadSettings.linkProgram()");
		System.out.println(System.getProperties().getProperty(os_propertyName));
	}


	/**
	 * prints error message to screen.
	 */
	private static void showErrorMessage() {

		//print message to screen.
		JOptionPane.showConfirmDialog(null, "An exception occured." 
				+ " Try again later.", "Error loading workspace", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
	
	}
	
}
