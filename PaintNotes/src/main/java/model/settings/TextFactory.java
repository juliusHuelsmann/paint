package model.settings;


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
	view_tb_zoomIn, view_tb_zoomOut, view_tb_save, view_popup_saveChangesTitle,
	view_popup_saveChangesText,
	
	// tabg debug
	view_debug_headline, view_debug_amountItems, view_debug_console,
	view_debug_generateViewDiagram, view_debug_generateLog, 
	view_debug_reportBug,
	
	colorPanel_red, colorPanel_green, colorPanel_blue, colorPanel_submit,
	
	name_pencil, name_ballPen, name_marker, name_funny, name_selection;

	
	
	//constructor
	
	/**
	 * private utility class.
	 */
	private TextFactory() {
		
		//initialize with language German
		this.specifiedLanguage = 0;
		
		//initialize the texts of strings
		view_tb_paste = new String[]{"Einfügen", "Paste"};
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
        
        
        //view pop-up which is opened if the user tries to exit the program, to
        //load a file from hard drive or to open a blank page if there are 
        //unsaved changes
        view_popup_saveChangesTitle = new String [] {
        		"Änderungen speichern?", 
        		"Save changes?"};
        view_popup_saveChangesText = new String [] {
        		"Möchten Sie vor dem Schließen ungespeicherte Änderungen "
        		+ "speichern?", 
        		"Do you want to save the uncommitted changes before exiting? "};
        
        
        //displayed name of pens
        name_pencil = new String []{
        		"Füller",
        		"Pencil"
        };
        name_ballPen = new String [] {
        		"Bleistift",
        		"Ball-pen"
        };
        name_marker = new String []{
        		"Textmarker",
        		"Marker"
        };
        name_funny = new String []{
        		"Funny",
        		"Funny"
        };
        name_selection = new String[] {
        		"Auswahl",
        		"Selection"
        };
        
        
        
        
        //
        view_debug_headline = new String[] {
        		"Fehlerbehebung",
        		"Debug"
        };
        
        view_debug_amountItems = new String[] {
        		"Anz. PaintObjects",
        		"Amount of paint-objects"
        };
        
        view_debug_console = new String[] {
        		"Konsole",
        		"Console"
        };
        
        view_debug_generateLog = new String[] {
        		"Log-Datei",
        		"log-file"
        };
        
        view_debug_generateViewDiagram = new String[] {
        		"GUI-Diagramm ",
        		"gui-diagram"
        };
        view_debug_reportBug = new String[] {
        		"Fehler melden",
        		"Report bug"
        };
        
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
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getTextViewPopupSaveChangesText() {
		return view_popup_saveChangesText[specifiedLanguage];
	}
	
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getTextViewPopupSaveChangesTitle() {
		return view_popup_saveChangesTitle[specifiedLanguage];
	}


	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getName_ballPen() {
		return name_ballPen[specifiedLanguage];
	}
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getName_Funny() {
		return name_funny[specifiedLanguage];
	}
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getName_marker() {
		return name_marker[specifiedLanguage];
	}
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getName_pencil() {
		return name_pencil[specifiedLanguage];
	}
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getName_selection() {
		return name_selection[specifiedLanguage];
	}


	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getView_debug_amountItems() {
		return view_debug_amountItems[specifiedLanguage];
	}

	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getView_debug_console() {
		return view_debug_console[specifiedLanguage];
	}

	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getView_debug_generateLog() {
		return view_debug_generateLog[specifiedLanguage];
	}

	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getView_debug_generateViewDiagram() {
		return view_debug_generateViewDiagram[specifiedLanguage];
	}
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getView_debug_headline() {
		return view_debug_headline[specifiedLanguage];
	}
	/**
	 * simple getter method.
	 * @return the text
	 */
	public String getView_debug_reportBug() {
		return view_debug_reportBug[specifiedLanguage];
	}

}
