//package declaration
package start;

//import declarations
import java.io.File;
import model.objects.painting.Picture;
import model.settings.Status;
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
		Picture p = Picture.getInstance();
		File f = new File(_arg);

		//if file exists and is directory
		if (f.exists() && f.isDirectory()) {
		    Status.getLogger().info(f.getPath());
		    
		    //load, edit and save each file
			for (File currentFile: f.listFiles()) {
			    if (currentFile.isFile()
                        && currentFile.getPath().endsWith(".png")) {
                    
                    p.load(currentFile.getPath());
                    p.transformWhiteToAlpha();
                    Status.getLogger().info(currentFile.getPath());
                    p.saveQuickPNG(currentFile.getPath() + "2");
                } else if (currentFile.isFile()
                        && currentFile.getPath().endsWith(".gif")) {
                    
                    p.load(currentFile.getPath());
                    p.transformWhiteToAlpha();
                    p.saveIMAGE(currentFile.getPath() + "2", 0, 0);
                } else {
                    Status.getLogger().info("unknown type");
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
	        
	        //print case message
	        Status.getLogger().info("normal start: launch programm!\n\n");
	        
	        //call controller
	        new ControlPaint();
	        
	        //set the initialization process terminated
            Status.increaseInitializationFinished();
	        
	        break;
	        
	    //one or more inputs: change folder
        default:

            //print case message
            Status.getLogger().info("start with parameters; alter images!\n\n");
            
            //go through array of Strings
            for (int currPath = 0; currPath < _args.length; currPath++) {
                changeFolder(_args[currPath]);
            }

	        //set the initialization process terminated
            Status.increaseInitializationFinished();
            
            break;
	    }
	}

}
