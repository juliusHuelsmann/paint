package model.settings;

/**
 * class contains the independant settings.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Settings {

    
    /**
     * the location of the user Workspace if this mode is selected.
     */
    private static String wsLocation = "";
    
    
    /**
     * utility class constructor.
     */
    private Settings() {
        
    }


    /**
     * @return the wsLocation
     */
    public static String getWsLocation() {
        return wsLocation;
    }


    /**
     * @param _wsLocation the wsLocation to set
     */
    public static void setWsLocation(final String _wsLocation) {
        Settings.wsLocation = _wsLocation;
    }
}
