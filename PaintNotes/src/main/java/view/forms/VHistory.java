package view.forms;

//import declarations
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.settings.Status;
import model.settings.ViewSettings;

/**
 * 
 * @author Julius Huelsmann
 * @version "I", "U"
 */
@SuppressWarnings("serial")
public final class VHistory extends JFrame implements Observer {

    
    /**
     * the only instance of this class.
     */
    private static VHistory instance;
    
    /**
     * JTextArea for information.
     */
    private JTextArea jta_info;
    
    
    /**
     * constructor of ViewHistory. initializes the components.
     */
    private VHistory() {
        super();
        super.setSize(ViewSettings.SIZE_OF_HISTORY);
        super.setAlwaysOnTop(true);
        super.setLayout(new BorderLayout());
        
        jta_info = new JTextArea("");
        jta_info.setEditable(false);
        jta_info.setOpaque(false);
        jta_info.setFocusable(false);
        jta_info.setBorder(null);
        jta_info.setLineWrap(true);
        jta_info.setFont(ViewSettings.GENERAL_FONT_ITEM_SMALL);

        JScrollPane scrollPane = new JScrollPane(jta_info);
        scrollPane.setAutoscrolls(true);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.add(scrollPane, BorderLayout.CENTER);
        
        super.setVisible(true);
        super.setResizable(false);
        jta_info.setText(jta_info.getText() + "\nasdf");
        
        
    }

    public void update(final Observable _arg0, final Object _arg1) {
        
        String c = (String) _arg1;
        if (c.startsWith("add ")) {
            jta_info.setText(jta_info.getText() + "\n" + c);
        } else if (c.startsWith("remove")) {
            String newText = jta_info.getText().replace(
                    "\n" + c.replaceFirst("remove ", "add "), "");
            
            Status.getLogger().info("remove: " + newText);
        }
    }
    

    /**
     * this method guarantees that only one instance of this
     * class can be created ad runtime.
     * 
     * @return the only instance of this class.
     */
    public static synchronized VHistory getInstance() {
        
        //if not initialized yet
        if (instance == null) {
            instance = new VHistory();
        }
        
        //return the instance of VHistory
        return instance;
    }
}
