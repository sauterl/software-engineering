/**
 * 
 */
package util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The {@link StandartFormatter} formats {@link LogRecord}s in a human-readable
 * way. It has a clean and simple layout.
 * 
 * <p>
 * The layout of a single formatted {@link LogRecord} is like so:<br>
 * {@code dd-MM-yyyy HH:mm:ss [LEVEL] [name] msg NL}<br>
 * The described time format is in {@link SimpleDateFormat}-notation.<br>
 * The LEVEL stands for the record's level.<br>
 * name stands for the name of the logger which created the record.<br>
 * The placeholder msg will be replaced by the record's message and NL stands
 * for a new line.
 * </p>
 * 
 * @author Loris
 * 
 */
public class StandartFormatter extends Formatter {

    private final DateFormat dateFormat = new SimpleDateFormat(
	    "dd-MM-yyyy HH:mm:ss");

    /**
     * Flag to indicate whether the method name is part of the formatted message
     * or not
     */
    private volatile boolean methodNameEnabled = false; //not used yet

    /**
     * Constructs a new default {@link StandartFormatter}.
     */
    public StandartFormatter() {
	/* empty */
    }
    
    /**
     * Formats the given {@link LogRecord}.
     * 
     * <p>
     * The layout of a single formatted {@link LogRecord} is like so:<br>
     * {@code dd-MM-yyyy HH:mm:ss [LEVEL] [name] msg NL}<br>
     * The described time format is in {@link SimpleDateFormat}-notation.<br>
     * The LEVEL stands for the record's level.<br>
     * name stands for the name of the logger which created the record.<br>
     * The placeholder msg will be replaced by the record's message and NL
     * stands for a new line.
     * </p>
     * 
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @Override
    public String format(LogRecord record) {
	StringBuffer sb = new StringBuffer(256);
	sb.append(dateFormat.format(new Date(record.getMillis())));
	sb.append(" ");
	// adding thread name+number somewhere?
	sb.append("[");
	sb.append(record.getLevel().getName());
	sb.append("]");
	// sb.append(" ");
	sb.append("[");
	sb.append(record.getLoggerName());
	sb.append("]");
	sb.append(" ");
	if (methodNameEnabled) {
	    sb.append("<");
	    sb.append(record.getSourceMethodName());
	    sb.append(">");
	    sb.append(" ");
	}
	sb.append(record.getMessage());
	sb.append("\n");
	Throwable thrown = record.getThrown();
	if(thrown != null){
	    //record has throwable
	    StringWriter strWtr = new StringWriter();
	    PrintWriter pWtr = new PrintWriter(strWtr);
	    pWtr.println();
	    thrown.printStackTrace(pWtr);
	    pWtr.flush();
	    pWtr.close();
	    sb.append(strWtr.toString());
	}
	return sb.toString();
    }
    
}
