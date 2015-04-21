package model;

//import declarations
import model.objects.history.HistorySession;
import model.objects.painting.Picture;
import model.settings.Status;


/**
 * Project class: the main model class which contains
 * pages and their history.
 * 
 * By now, it is only possible to have one page inside one Project.
 * That is going to be changed in the future.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class Project {

	
	
	/**
	 * HistorySession of one Pictureclass.
	 */
	private HistorySession history;
	
	
	/**
	 * Picture class.
	 */
	private Picture picture;
	
	
	
	/**
	 * Constructor of project: 
	 * 		(1)	Initializes the sub-classes Picture and History.
	 * 		(2) Save the model classes in the sub-model and status if 
	 * 			necessary.
	 */
	public Project() {
		
		//initialize picture and history
		picture = new Picture();
		history = new HistorySession(picture);
		
		//set the Picture into Status and set history to the picture.
        Status.setPicture(picture);
		picture.initialize(history);
	}


	/**
	 * @return the history
	 */
	public final HistorySession getHistory() {
		return history;
	}




	/**
	 * @return the picture
	 */
	public final Picture getPicture() {
		return picture;
	}
}
