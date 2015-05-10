package model;

//import declarations
import model.objects.history.HistorySession;
import model.objects.painting.Picture;


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
	 * Initializes the sub-classes Picture and History.
	 */
	public Project() {

		//initialize picture and history
		picture = new Picture();
		history = new HistorySession(picture);
	}

	
	/**
	 * initialize the history of the picture.
	 */
	public final void initialize() {
		
		//set the Picture into Status and set history to the picture.
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
