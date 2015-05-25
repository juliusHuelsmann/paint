//package declaration
package start;

//import declarations
import java.io.File;

import javax.swing.JOptionPane;
import model.objects.painting.Picture;
import model.settings.Constants;
import model.settings.State;
import control.ControlPaint;



/**
 * Starter: starts the program and loads.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Start {

	/**
	 * Utility class constructor.
	 */
	private Start() { }
	
	/**
	 * open images of a special folder and change them.
	 * @param _arg the folder path
	 */
	public static void changeFolder(final String _arg) {
		
	    //create an instance of picture
		Picture p = new Picture();
		File f = new File(_arg);

		//if file exists and is directory
		if (f.exists() && f.isDirectory()) {
		    State.getLogger().info(f.getPath());
		    
		    //load, edit and save each file
			for (File currentFile: f.listFiles()) {
			    if (currentFile.isFile()
                        && currentFile.getPath().endsWith(".png")) {
                    
                    p.load(currentFile.getPath());
                    p.transformWhiteToAlpha();
                    State.getLogger().info(currentFile.getPath());
                    p.saveQuickPNG(currentFile.getPath() + "2");
                } else if (currentFile.isFile()
                        && currentFile.getPath().endsWith(".gif")) {
                    
                    p.load(currentFile.getPath());
                    p.transformWhiteToAlpha();
                    p.saveIMAGE(currentFile.getPath() + "2", 0, 0, "");
                } else {
                    State.getLogger().info("unknown type");
                }
			}

//			//remove original file
//			for (File currentFile: f.listFiles()) {
//				if (currentFile.isFile() 
//				        && currentFile.getPath().endsWith(".png")
//				        || currentFile.getPath().endsWith(".png2")) {
//				    
//				    //remove
//					currentFile.delete();
//				}
//			}
			
			//rename old files
			for (File currentFile: f.listFiles()) {
				if (currentFile.isFile() 
				        && currentFile.getPath().endsWith(".png22")) {
				    
				    //rename
					currentFile.renameTo(new File(currentFile.getPath()
					        .replace(".png22", ".png")));
				}
			}
		}
	}
	
	/**
	 * method main: creates an instance of Start.
	 * @param _args arguments
	 */
	public static void main(final String[] _args) {

	    //switch the operation depending on length of input
	    switch (_args.length) {
	    
	    //no input: launch program
	    case 0:
	    	
	    	//create new instance of settings which initializes the new created
	    	// status class depending on current startup option.
	    	new State(Constants.ID_STARTUP_LOAD_IMAGE).initialize();
	    	
	        
	        //print case message
	        State.getLogger().info("normal start: launch programm!\n\n");
	        double d = System.currentTimeMillis();
	        
	        //call controller
	        State.setIndexPageBackground(
	        		Constants.CONTROL_PAGE_BACKGROUND_RASTAR);

	        
	        /*
	         * Time logging
	         */
            String timeLog = ("Time used for startup:\nTime 1:"
            		+ "\t setBackground\n\t" 
            + (System.currentTimeMillis() - d) + "!\n");
            d = System.currentTimeMillis();
            
	        ControlPaint cp1 =  new ControlPaint();

	        /*
	         * Time logging
	         */
            timeLog += ("Time 2:\t create paint controller class\n\t" 
            + (System.currentTimeMillis() - d) + "!\n");
            d = System.currentTimeMillis();
            
            
	        cp1.initialize();

	        /*
	         * Time logging
	         */
            timeLog += ("Time 3:\t initialize paint controller class\n\t" 
            + (System.currentTimeMillis() - d) + "!\n");
            d = System.currentTimeMillis();
	        
	        //set the initialization process terminated
            State.increaseInitializationFinished();


	        /*
	         * Time logging
	         */
            timeLog += ("Time 4: set initialization finished in status\n\t" 
            + (System.currentTimeMillis() - d) + "!\n");
            State.getLogger().severe(timeLog);
	        break;
	        
	    //one or more inputs: change folder
        default:

	    	//create new instance of settings which initializes the new created
	    	// status class depending on current startup option.
	    	new State(Constants.ID_STARTUP_LOAD_IMAGE).initialize();
        	
        	boolean newStart = true;
        	if (newStart) {
        		
//        		TODO: automatically create in installation process:
//        		in /usr/share/contractor/paint.contract
//        		
//        		Afterwards chmod a+x .../paint.contract
//        		
//        		the file content:
//        		[Contractor Entry]
//        		Name=Paint
//        				Icon=gksu-root-terminal
//        				Description=Open paint
//        				MimeType=image/png;image/jpg;
//        				Exec=java -jar /home/juli/Software/file.jar %U
//        				Gettext-Domain=java
        		
        		double time0 = System.currentTimeMillis();
        		
        		JOptionPane.showMessageDialog(
        				null, 
        				"Information message: \n"
        				+ "\n"
        				+ "A pre-release paint version has been started "
        				+ " with the following parameter:\n\t" 
        				+ _args[0] 
        				+ "\n"
        				+ "\n"
        				+ "This program is developed and maintained "
        				+ "by Julius Hülsmann "
        				+ "\n" 
        				+ "(For more information visit "
        				+ "https://github.com/juliusHuelsmann/paint)",
        				"PaintNotes",
        				JOptionPane.INFORMATION_MESSAGE);

    	        //call controller
    	        State.setIndexPageBackground(
    	        		Constants.CONTROL_PAGE_BACKGROUND_NONE);
    	        State.setBorderRightPercent(0);
    	        State.setBorderLeftPercent(0);
    	        State.setBorderTopPercent(0);
    	        State.setBorderBottomPercent(0);
    	        ControlPaint cp =  new ControlPaint();
    	        cp.initialize();
    	        cp.getPicture().load(_args[0]);
    	        cp.getView().getTabs().setTabbedPaneOpen(false);
    	        
    	        //set the initialization process terminated
                State.increaseInitializationFinished();
    	        

        		double time1 = System.currentTimeMillis();
        		State.getLogger().warning("Took " 
        		+ (time1 - time0) + "ms for startup");
        		
        	} else {

            	//print case message
                State.getLogger().info(
                		"start with parameters; alter images!\n\n");
                
                //go through array of Strings
                for (int currPath = 0; currPath < _args.length; currPath++) {
                    changeFolder(_args[currPath]);
                }

    	        //set the initialization process terminated
                State.increaseInitializationFinished();
                
        	}
        	
        	
        	
        	
            break;
	    }
	}

}
