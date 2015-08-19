package model.settings;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.View;
import view.util.InformationWindow;
import model.util.Util;

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
		
		
		
		
		if (System.getProperty("os.name").equals("Mac OS X")) {

			installOSX();
		} else if (System.getProperty("os.name").equals("Linux")) {
			installLinux();
		}
	
		
		
		
		
		

		
		
		
		return wsLocation;

	}
	
//	public static void main(String[] _args) {
//		installOSX();
//	}

	/**
	 * Set up paint as program in OSX.
	 */
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
			

			
			//TODO: this is just a debug parameter
			boolean installed = new File(filePath + fileName).exists();
			if (!installed) {

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
			
			
			if (!installed) {

				final String command2 = 
						"/System/Library/Frameworks/CoreServices.framework/Versions/"
						+ "A/Frameworks/LaunchServices.framework/Versions/A/Support/"
						+ "lsregister -f /Applications/" + app_name + "/";
				String ret2 = Util.executeCommandLinux(command2);
				
				
				final String command3 = "killall Finder";
				String ret3 = Util.executeCommandLinux(command3);

				String s = "Operation log:\n";
				System.out.println(s);
				System.out.println(ret + "\n" + ret1 +"\n" + ret2 +"\n" + ret3);
				

			}			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Set up paint as program in OSX.
	 */
	private static void installLinux() {
		
		
		try {

			final String name = "paint";
			final String app_name = "Paint";
			/*
			 * STEP 1:	Create files and folders for the info.plist
			 */
			final String filePath = "/usr/share/" + app_name;
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
			

			
			//TODO: this is just a debug parameter
			boolean installed = new File(filePath + fileName).exists();
			if (!installed) {

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
			
			
			if (!installed) {

				final String command2 = 
						"/System/Library/Frameworks/CoreServices.framework/Versions/"
						+ "A/Frameworks/LaunchServices.framework/Versions/A/Support/"
						+ "lsregister -f /Applications/" + app_name + "/";
				String ret2 = Util.executeCommandLinux(command2);
				
				
				final String command3 = "killall Finder";
				String ret3 = Util.executeCommandLinux(command3);

				String s = "Operation log:\n";
				System.out.println(s);
				System.out.println(ret + "\n" + ret1 +"\n" + ret2 +"\n" + ret3);
				

			}			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private static boolean checkForUpdate(final View _view, final boolean _showNoUpdateMSG) {
		try {
			 // Create a URL for the desired page
	        URL url = new URL("http://juliushuelsmann.github.io/paint/currentRelease");       

	        // Read all the text returned by the server
	        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	        String version = in.readLine();
			in.close();
			
			final String[] result = Version.getMilestonePercentageDone(version);
			if (result == null) {
				// error
				return false;
			}

			final String MS = result[0];
			final String perc = result[1];

			try {
				
				// parse information to integer.
				int new_milestone  = Integer.parseInt(MS);
				int new_percentage = Integer.parseInt(perc);

				int crrnt_milestone  = Integer.parseInt(Version.MILESTONE);
				int crrnt_percentage = Integer.parseInt(
						Version.PERCENTAGE_DONE);
				
				if (new_milestone > crrnt_milestone
						|| (new_percentage > crrnt_percentage 
								&& new_milestone == crrnt_milestone)) {
					

					int d = JOptionPane.showConfirmDialog(_view, 
							"A new version of paint has been found:\n"
							+ "Used Version:\t"
							+ "" + crrnt_milestone + "."
							+ "" + crrnt_percentage+ "\n" 
							+ "New  Version:\t"
							+ "" + new_milestone + "."
							+ "" + new_percentage+ "\n\n" 
							+ "Do you want to download it right now? \n"
							+ "This operation will close paint and restart \n"
							+ "the new version in about one minute.",
							"Update",
							JOptionPane.YES_NO_OPTION);
					
					return d == JOptionPane.YES_OPTION;
				} else {

					if (_showNoUpdateMSG) {

						JOptionPane.showMessageDialog(_view, 
								"No updates found.",
								"Update", 
								JOptionPane.INFORMATION_MESSAGE);
						
					}
					
					return false;
				}
				
			} catch(NumberFormatException _nex) {
				
				//error
				model.settings.State.getLogger()
				.severe("Failed to update: version number currupted.");
				return false;
			}
			
			
       } catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	
	/**
	 * Update the entire program.
	 */
	public static void update(final View _view, final boolean _showNoUpdateMSG) {
		
		
		new Thread() {
			
			public void run() {
		        
				final boolean newReleaseAccepted = checkForUpdate( _view, _showNoUpdateMSG);
				
				if (newReleaseAccepted) {

					_view.dispose();
					
					/*
					 * dispose current version of paint that is currently running.
					 */
					
					InformationWindow iw = new InformationWindow("Update Process.");
					
					
					/*
					 * Property name for getting operating system identifier.
					 */
					final String os_propertyName = "os.name";
					
					/*
					 * Different property content values for deciding which
					 * operating system is used.
					 */
					final String propertyLinux = "Linux";
					final String propertyOSX = "Mac OS X";
					final String propertyWindows = "Windows";

					/*
					 * The identifier for the currently used operating system.
					 */
					final String propertyContent = System.getProperties()
							.getProperty(os_propertyName);
					
					
					/*
					 * Compute TEMP directory path depending on the currently 
					 * used operating system.
					 */
					iw.appendText("Checking operating system:");
					final String temp;
					if (propertyContent.equals(propertyLinux)) {
						temp = "";
						iw.appendText("\tLinux");
						throw new UnsupportedOperationException("not impl. yet.");
					} else if (propertyContent.equals(propertyWindows)) {
						temp = "";
						iw.appendText("\tWindows");
						throw new UnsupportedOperationException("not impl. yet.");
					} else if (propertyContent.equals(propertyOSX)) {
						temp = System.getenv().get("TMPDIR");
						iw.appendText("\tOS X");
					} else {
						temp = "";
						iw.appendText("\t Not found!");
						throw new UnsupportedOperationException("not impl. yet.");
					}

					
					/*
					 * Create sub-TEMP directory
					 */
					final String tempDirPaint = temp + "paint/";
					final String ret0_a, command0 = "mkdir " + tempDirPaint;
					if (new File(tempDirPaint).exists()) {

						
						ret0_a = "The file already exists: " + tempDirPaint + "." ;
						iw.appendText("\t" + ret0_a);
						
						
						// remove file
						iw.appendText("Remove old temp file.");
						final String ret0_b, command0_b = "rm -r -f " + tempDirPaint ;
						ret0_b = Util.executeCommandLinux(command0_b);

						iw.appendText("\t" + ret0_b);
						
					} 

					iw.appendText("Create sub-TEMP directory");
					final String ret0 = Util.executeCommandLinux(command0);
					iw.appendText("\t" + ret0);

					
					/*
					 * Check whether git is installted at the machine.
					 */

					iw.appendText("Clone Project into TEMP directory.");
					iw.appendText("Check whether git is installed.");
					final String command1 = "git version";
					String ret1 = Util.executeCommandLinux(command1);
					boolean installed = ret1.contains(Util.EXECUTION_SUCCESS);
					
					if (installed) {
						
						iw.appendText("\t Git installed." );

						/*
						 * Clone project into TEMP directory
						 */
						iw.appendText("Clone Project into TEMP directory.");
						final String command1a = "git clone "
								+ "https://github.com/juliusHuelsmann/paint.git "
								+ tempDirPaint;
						String ret1a = Util.executeCommandLinux(command1a);
						iw.appendText("\t" + ret1a);

						/*
						 * Start jar file which copies itself into the program directory.
						 */
						iw.appendText("Launching new project file");
						final String command2a = "java -jar "
								+ tempDirPaint + "PaintNotes/paint.jar";
						String ret2a = Util.executeCommandLinux(command2a);
						iw.appendText("\t" + ret2a);

					} else {

						iw.appendText("\t Git not installed. Manual download." );
						
						/*
						 * Download Program from repository URL
						 */
						final String repoURL = 
								"https://github.com/juliusHuelsmann/paint/archive/master.zip" ;
						final String zipPath = tempDirPaint + "master.zip";

						
						try {
							iw.appendText("Download zip");
							URL website = new URL(repoURL);
							ReadableByteChannel rbc = Channels.newChannel(
									website.openStream());
							FileOutputStream fos;
							fos = new FileOutputStream(zipPath);
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);	
							fos.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							iw.appendText("Failed download. exit.");
							
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.exit(1);
						} catch (IOException e) {
							e.printStackTrace();
							iw.appendText("Failed download. exit.");
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.exit(1);
						}
						
						
						
						/*
						 * Unzip the program
						 */
						iw.appendText("unzip");
						/**
						 * The command which is executed for unzipping the program.
						 */
				    	final String commandUnzip = "unzip " + zipPath + " -d " 
				    			+ tempDirPaint;
				    	
				    	/**
				    	 * The result of the command's execution in terminal.
				    	 * If the response tells that the command has been executed
				    	 * successfully, there is nothing to do. Otherwise 
				    	 * perform rotation done by program and print a warning.
				    	 */
				    	final String resultUnzip = Util.executeCommandLinux(commandUnzip);
						iw.appendText("\t " + resultUnzip);
				    	if (resultUnzip.startsWith(Util.EXECUTION_SUCCESS)) {
				    		
				    		//print success information
				    		model.settings.State.getLogger().info("Download and execution successfull");
				    	} else if (resultUnzip.startsWith(Util.EXECUTION_FAILED)) {
			//
				    		model.settings.State.getLogger().severe("Download and execution failed" 
				    				+ resultUnzip);
				    		try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.exit(1);
				    	}
						
			//
				    	
						/**
						 * Command for removing zip download file.
						 */
						iw.appendText("Remove zip file");
				    	final String commandMv = "rm " + zipPath;
				    	final String resultClear = Util.executeCommandLinux(commandMv);
						iw.appendText("\t " + resultClear);
						

						/*
						 * Start jar file which copies itself into the program directory.
						 */
						iw.appendText("Launching new project file");
						final String command2a = "java -jar "
								+ tempDirPaint + "paint-master/PaintNotes/paint.jar";
						String ret2a = Util.executeCommandLinux(command2a);
						iw.appendText("\t" + ret2a);
				    	
					}

					
					
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.exit(1);
	//
//					
//					/*
//					 * Check for version number
//					 */
//					final String command1 = "git clone "
//							+ "https://github.com/juliusHuelsmann/paint.git "
//							+ tempDirPaint;
//					String ret2 = Util.executeCommandLinux(command1);
//					model.settings.State.getLogger().severe("Executed"
//							+ "\nC1:\t" + command0 + "\n\t" + ret0_a 
//							+ "\nC2:\t" + command1 + "\n\t" + ret1
//							+ "\nC3:\t" + command2 + "\n\t" + ret2);
					
				}
				
			}
		}.start();
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
