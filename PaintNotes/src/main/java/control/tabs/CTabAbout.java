package control.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import model.settings.Status;
import model.util.Util;
import view.tabs.About;
import control.ControlPaint;


/**
 * Controller class for about - tab which contains information on the program 
 * and the web site which contains its different versions and a button for 
 * checking for updates.
 * The ladder functionality is executed by an instnace of this class.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class CTabAbout implements ActionListener {

	
	/**
	 * The current version number.
	 */
	private final int currentVersion = -1;
	
	/**
	 * The URL of the repository release for the next version.
	 */
	private final String repoURL = 
			"https://github.com/juliusHuelsmann/paint/archive/release" 
					+ (currentVersion + 1) + ".zip";
	
	/**
	 * The default path for saving the download as .ZIP file. Saved as 
	 * n-dimensional array; split into directory names and the file name.
	 */
	private final String [] defaultTempLoadPath = 
		{"tmp", "Paint", "program.zip"};

	/**
	 * An instance of the root controller class.
	 */
	private ControlPaint cp;
	
	
	
	/**
	 * Constructor: saves an instance of the root controller class.
	 * 
	 * @param _cp	instance of the root controller class which is saved.
	 */
	public CTabAbout(final ControlPaint _cp) {
		this.cp = _cp;
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	public final void actionPerformed(final ActionEvent _event) {

		if (_event.getSource().equals(
				getAbout().getI1b_checkForUpdates().getActionCause())) {
			checkForUpdates();
		}
	}

	

	/**
	 * Method for checking for program updates.
	 * @return whether there is a valid program update.
	 */
	private boolean checkForUpdates() {

		final int defaultLocationPathLenght = 3;
		
		URL website;
		try {
			
			if (defaultTempLoadPath.length != defaultLocationPathLenght) {
			
				Status.getLogger().severe(
						"implementation error: wrong default temp load path");
				return false;
			}
			
			
			String tempLoadPath = "";
			/*
			 * Check whether where the download is to be saved exists.
			 * Otherwise create a folder in case the root folder exists.
			 * If it does not, download the program temporarily in the location
			 * the program is called.
			 */
			File f = new File(
					System.getProperty("file.separator") 
					+ defaultTempLoadPath[0]);
			if (f.exists() && !f.isFile()) {
				
				tempLoadPath = 
						System.getProperty("file.separator") 
						+ defaultTempLoadPath[0] 
						+ System.getProperty("file.separator") 
						+ defaultTempLoadPath[1];
				
				File f2 = new File(tempLoadPath);
				
				if (f2.exists() && !f2.isFile()) {
					
					
					tempLoadPath +=  
							System.getProperty("file.separator") 
							+ defaultTempLoadPath[2];
					
					
					
					//clear all stuff inserted in this file
					
					/**
					 * The command which is executed in terminal for rotating 
					 * the screen.
					 */
			    	final String commandUnzip = "rm -r " 
					 + tempLoadPath.substring(0, tempLoadPath.length() 
							 - defaultTempLoadPath[2].length()) + "*";
			    	System.out.println(commandUnzip);

			    	/**
			    	 * The result of the command's execution in terminal.
			    	 * If the response tells that the command has been executed
			    	 * successfully, there is nothing to do.
			    	 */
			    	final String resultUnzip = Util.executeCommandLinux(
			    			commandUnzip);
			    	System.out.println(resultUnzip);
			    	if (resultUnzip.startsWith(Util.EXECUTION_SUCCESS)) {
			    		
			    		//print success information
			    		Status.getLogger().info("remove successful");
			    	} else if (resultUnzip.startsWith(Util.EXECUTION_FAILED)) {

			    		Status.getLogger().severe("remove failed" 
			    				+ resultUnzip);
			        }
					
				} else if (f2.exists()) {

					tempLoadPath =  
							System.getProperty("file.separator") 
							+ defaultTempLoadPath[0] 
							+ System.getProperty("file.separator") 
							+ defaultTempLoadPath[2];
				} else {
					if (f2.mkdirs())  {

						tempLoadPath +=  
								System.getProperty("file.separator") 
								+ defaultTempLoadPath[2];
					} else {

						tempLoadPath =  
								System.getProperty("file.separator") 
								+ defaultTempLoadPath[0] 
								+ System.getProperty("file.separator") 
								+ defaultTempLoadPath[2];
					}
				}
				
			} else {
				tempLoadPath = defaultTempLoadPath[2];
			}
			
			
			/*
			 * Download Program from repository URL
			 */
			website = new URL(repoURL);
			ReadableByteChannel rbc = Channels.newChannel(
					website.openStream());
			FileOutputStream fos = new FileOutputStream(tempLoadPath);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);	
			fos.close();
			
			
			/*
			 * Unzip the program
			 */
			System.out.println("hier");
	
			/**
			 * The command which is executed in terminal for rotating the
			 * screen.
			 */
	    	final String commandUnzip = "unzip " + tempLoadPath + " -d " 
			 + tempLoadPath.substring(0, tempLoadPath.length()
					 - defaultTempLoadPath[2].length());

	    	/**
	    	 * The result of the command's execution in terminal.
	    	 * If the response tells that the command has been executed
	    	 * successfully, there is nothing to do. Otherwise 
	    	 * perform rotation done by program and print a warning.
	    	 */
	    	final String resultUnzip = Util.executeCommandLinux(commandUnzip);
	    	if (resultUnzip.startsWith(Util.EXECUTION_SUCCESS)) {
	    		
	    		//print success information
	    		Status.getLogger().info("Download and execution successfull");
	    	} else if (resultUnzip.startsWith(Util.EXECUTION_FAILED)) {

	    		Status.getLogger().severe("Download and execution failed" 
	    				+ resultUnzip);
	    		return false;
	        }
			

	    	
			/**
			 * The command which is executed in terminal for rotating the
			 * screen.
			 */
	    	final String commandMv = "rm " + tempLoadPath;
	    	System.out.println(commandMv);
	    	/**
	    	 * The result of the command's execution in terminal.
	    	 * If the response tells that the command has been executed
	    	 * successfully, there is nothing to do. Otherwise 
	    	 * perform rotation done by program and print a warning.
	    	 */
	    	final String result = Util.executeCommandLinux(commandMv);
	    	System.out.println(result);
	    	if (result.startsWith(Util.EXECUTION_SUCCESS)) {

	    		//print success information
	    		Status.getLogger().info("Entered successfully");
	    	} else if (result.startsWith(Util.EXECUTION_FAILED)) {

	    		Status.getLogger().severe("Enter and remove failed " + result);
	    		return false;
	        }
	    	
	    	/*
	    	 * Check the info file containing the version number
	    	 */
	    	return true;
	    	
			
		} catch (MalformedURLException e1) {
			
			//if the URL can not be found
			
			e1.printStackTrace();
			return false;
		} catch (FileNotFoundException e1) {
			
			
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}	
	}	
	
	
	
	
	/**
	 * Error - checked getter method for the about tab.
	 * 
	 * @return 	instance of About (tab) fetched out of the root controller 
	 * 			class.
	 */
	private About getAbout() {
		
		if (cp != null) {
			if (cp.getView() != null) {
				if (cp.getView().getTabs() != null) {
					if (cp.getView().getTabs().getTab_about() != null) {

						return cp.getView().getTabs().getTab_about();
					} else {
						Status.getLogger().severe("cp.getView()"
								+ ".getTabs().getTab_about()is null.");
					}
				} else {
					Status.getLogger().severe("cp.getView().getTabs()"
							+ " is null.");
				}
			} else {
				Status.getLogger().severe("cp.getView() is null.");
			}
		} else {
			Status.getLogger().severe("cp is null.");
		}
		return null;
	}
}
