package settings;


/**
 * returns the text in specified language.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class TextFactory {

	//instance of TextFactory
	
	/**
	 * instance which is only created once.
	 */
	private static TextFactory instance;
	
	//constants for languages
	
	/**
	 * static identifier for German / English language.
	 */
	public static final int LANG_DE = 0, LANG_EN = 1;
	
	/**
	 * the currently specified language.
	 */
	private int specifiedLanguage;
	
	//private values for text
	
	/**
	 * string containing the text of component in different languages.
	 */
	private final String[] view_tb_paste, view_tb_copy,  view_tb_cut, 
	view_tb_zoomIn, view_tb_zoomOut, view_tb_save, 
	
	colorPanel_red, colorPanel_green, colorPanel_blue, colorPanel_submit;

	
	
	//constructor
	
	/**
	 * private utility class.
	 */
	private TextFactory() {
		
		//initialize with language German
		this.specifiedLanguage = 0;
		
		//initialize the texts of strings
		view_tb_paste = new String[]{"Einfuegen", "Paste"};
		view_tb_copy = new String[]{"Kopieren", "Copy"};
		view_tb_cut = new String[]{"Ausschneiden", "Cut"};
		
		view_tb_zoomIn = new String [] {"+", "+"};
		view_tb_zoomOut = new String [] {"-", "-"};

        view_tb_save = new String [] {"Speichern", "Save"};
        
        
        //color panel
        colorPanel_red = new String [] {"Rot", "red"};
        colorPanel_green = new String [] {"Gruen", "green"};
        colorPanel_blue = new String [] {"Blau", "blue"};
		
        colorPanel_submit = new String [] {"ok", "add"};
	}

	/**
	 * set current language.
	 * @param _lang the language id.
	 */
	public void setLanguage(final int _lang) {
		this.specifiedLanguage = _lang;
	}

	//method for getting singleton instance of this class.
	
	/**
	 * normal getter method.
	 * @return the only instance of this singleton class
	 */
	public static TextFactory getInstance() {
		
		//create an instance of TextFactory if that is not done yet
		if (instance == null) {
			instance = new TextFactory();
		}
		
		//return the instance of TextFactory
		return instance;
	}

	//getter methods for text
	
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getTextViewTb_paste() {
		return view_tb_paste[specifiedLanguage];
	}
	
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getTextViewTb_copy() {
		return view_tb_copy[specifiedLanguage];
	}
    
    /**
     * simple getter method.
     * @return the text
     */
    public String getTextViewTb_cut() {
        return view_tb_cut[specifiedLanguage];
    }
    
    /**
     * simple getter method.
     * @return the text
     */
    public String getTextColorPanel_red() {
        return colorPanel_red[specifiedLanguage];
    }
    
    /**
     * simple getter method.
     * @return the text
     */
    public String getTextColorPanel_green() {
        return colorPanel_green[specifiedLanguage];
    }

    /**
     * simple getter method.
     * @return the text
     */
    public String getTextColorPanel_blue() {
        return colorPanel_blue[specifiedLanguage];
    }
    /**
     * simple getter method.
     * @return the text
     */
    public String getTextColorPanel_submit() {
        return colorPanel_submit[specifiedLanguage];
    }
	
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getTextViewTb_zoomIn() {
		return view_tb_zoomIn[specifiedLanguage];
	}
	
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getTextViewTb_zoomOut() {
		return view_tb_zoomOut[specifiedLanguage];
	}
	
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getTextViewTb_save() {
		return view_tb_save[specifiedLanguage];
	}
}
