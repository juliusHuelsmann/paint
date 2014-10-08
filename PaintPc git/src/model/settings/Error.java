package model.settings;


/**
 * Class which is handling errors.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class Error {

    
    /**
     * empty utility class constructor.
     */
    private Error() { }
    
    
    /**
     * whether to debug or not.
     */
    private static boolean debug = true;
    
    /**
     * error and log message constants.
     */
    public static final byte ERROR_MESSAGE_INTERRUPT = 0, 
            ERROR_MESSAGE_PRINT_ALL = 1, ERROR_MESSAGE_PRINT_LESS = 2, 
            ERROR_MESSAGE_DO_NOTHING = 3, 
            LOG_MESSAGE_PRINT_ALWAYS = 0, 
            LOG_MESSAGE_PRINT_DEBUG = 1;
    
    /**
     * print normal error message.
     * 
     * @param _class the class - name
     * @param _method the method - name
     * @param _error the error - message
     * @param _exception the exception
     * @param _errorType the error type/ whether to interrupt/not/print 
     *          everything
     */
    public static void printError(
            final String _class, final String _method, 
            final String _error, final Exception _exception,
            final byte _errorType) {

        switch (_errorType) {
        
        case ERROR_MESSAGE_DO_NOTHING:
            break;
        
        case ERROR_MESSAGE_PRINT_LESS:
            
            //print small message.
            System.err.println("An exception occured.\n"
                    + "\tclassName:\t" + _class + "\n"
                    + "\tmethodName:\t" + _method + "\n"
                    + "\terror:\t" + _error + "\n\n");

            //break
            break;
            
        case ERROR_MESSAGE_PRINT_ALL:
            
            //print small message and afterwards stackTrace.
            printError(_class, _method, _error, _exception, 
                    ERROR_MESSAGE_PRINT_LESS);
            
            _exception.printStackTrace();
            
            //break
            break;
            
        case ERROR_MESSAGE_INTERRUPT:
            
            //print small message, stackTrace and then interruption message
            printError(_class, _method, _error, _exception, 
                    ERROR_MESSAGE_PRINT_ALL);
            System.err.println("\n\ninterrupt because of this error.");
            
            //interrupt
            System.exit(1);
            
        default:
            
            //print error message about wrong identifier.
            printError("Error", "printError(...)", "wrong error message id", 
                    null, ERROR_MESSAGE_PRINT_LESS);
            
            //print previous error message and interrupt.
            printError(_class, _method, _error, _exception, 
                    ERROR_MESSAGE_INTERRUPT);
        }
    }
    
    
    
    /**
     * print a log message.
     * @param _class the simple class name
     * @param _method the method name
     * @param _message the message
     * @param _logType the log messageType
     */
    public static void printLog(final String _class, final String _method,
            final String _message, final int _logType) {

        switch (_logType) {
        
        case LOG_MESSAGE_PRINT_DEBUG:
            if (!debug) {
                return;
            }
        case LOG_MESSAGE_PRINT_ALWAYS:
            
            //print small message.
            System.err.println("A log message arrived!.\n"
                    + "\tclassName:\t" + _class + "\t\t"
                    + "\tmethodName:\t" + _method + "\t\t"
                    + "\tmessage:\t" + _message + "\n");
            break;
        default:

            //print previous error message and interrupt.
            printLog(_class, _method, _message, LOG_MESSAGE_PRINT_ALWAYS);
            //print error message about wrong identifier.
            printError("Error", "printError(...)", "wrong log message id", 
                    new Exception(), ERROR_MESSAGE_INTERRUPT);
            
        }
    }
        


        /**
         * @return the debug
         */
        public static boolean isDebug() {
            return debug;
        }


        /**
         * @param _debug the debug to set
         */
        public static void setDebug(final boolean _debug) {
            Error.debug = _debug;
        }
}

