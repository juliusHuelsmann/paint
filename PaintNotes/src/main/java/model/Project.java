package model;

import model.objects.history.HistorySession;
import model.objects.painting.Picture;
import model.settings.Status;

public class Project {

	
	private HistorySession history;
	
	private Picture picture;
	
	
	public Project() {
		picture = new Picture();
		history = new HistorySession(picture);
        Status.setPicture(picture);
		picture.initialize(history);
	}


	/**
	 * @return the history
	 */
	public HistorySession getHistory() {
		return history;
	}




	/**
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}


}
