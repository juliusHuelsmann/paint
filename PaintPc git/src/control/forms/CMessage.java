package control.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.forms.Message;

/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CMessage implements ActionListener {

    
    /**
     * The CMessage.
     */
    private static CMessage instance;
    
    /**
     * Empty utility class constructor.
     */
    private CMessage() { }
    
    /**
     * {@inheritDoc}
     */
    @Override public void actionPerformed(final ActionEvent _event) {

        Message.fadeOut();
    }

    /**
     * @return the instance
     */
    public static CMessage getInstance() {
        
        if (instance == null) {
            instance = new CMessage();
        }
        return instance;
    }

}
