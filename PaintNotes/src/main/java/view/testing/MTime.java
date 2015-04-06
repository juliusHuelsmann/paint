package view.testing;

import java.io.Serializable;

public class MTime implements Serializable {

	
	/**
	 * Title of the time item.
	 */
	private String strg_title;
	
	/**
	 * (unique) identifier of the time item.
	 */
	private final int identifier;
	
	
	/**
	 * The spent time.
	 */
	private int time;
	
	/**
	 * static identifier for guaranteeing that the identifier is unique.
	 */
	private static int currentIdentifier = 0;
	
	/**
	 * Working Time.
	 */
	private int workingTime = 0;
	
	/**
	 * Constructor: saves parameters and updates the currentIdentifier.
	 * 	
	 * @param _strg_title 	the title of the Time item.
	 */
	public MTime(final String _strg_title) {
		
		//save parameter
		this.strg_title = _strg_title;
		this.identifier = increaseCurrentIdentifier();
	}
	
	
	public synchronized void applyWorkingTime() {
		time += workingTime;
		workingTime = 0;
	}
	
	
	/**
	 * Increase method for current identifier which returns the identifier
	 * which is to be used for the new Time item.
	 * 
	 * @return the identifier which is to be used for the new Time item.
	 */
	private synchronized int increaseCurrentIdentifier() {
		return currentIdentifier++;
	}


	/**
	 * @return the strg_title
	 */
	public String getStrg_title() {
		return strg_title;
	}


	/**
	 * @param strg_title the strg_title to set
	 */
	public void setStrg_title(String strg_title) {
		this.strg_title = strg_title;
	}


	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}


	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}


	/**
	 * @return the identifier
	 */
	public int getIdentifier() {
		return identifier;
	}


	/**
	 * @return the workingTime
	 */
	public int getWorkingTime() {
		return workingTime;
	}


	/**
	 * @param workingTime the workingTime to set
	 */
	public void setWorkingTime(int workingTime) {
		this.workingTime = workingTime;
	}
}
