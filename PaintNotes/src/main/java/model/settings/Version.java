package model.settings;

public class Version {

	
	public static String programName = "Paint";
	public static String RELEASE_DATE = "2015 04 25";
	public static String STATUS = "PRE RELEASE";
	public static String VERSION = "V 0 1";
	
	
	public static String generateVersionInformation() {
//		return "damitghets";
		return programName + " " + VERSION + " " + STATUS + " " + RELEASE_DATE;
	}
}
