package opencranium.util.log;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * A singleton class for logging information. The class is not thread safe.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Logger {

	/**
	 * Enumeration for logging. The logging levels are:
	 * <ul>
	 * <li><b>VERBOSE</b>: for logging all the information</li>
	 * <li><b>DEBUG</b>: for logging the debug information. The debug
	 * information should be removed when the \"bug\" is solved.</li>
	 * <li><b>INFORMATION</b>: for logging normal information messages.</li>
	 * <li><b>WARNING</b>: for logging information that can cause a failure in
	 * the program or problems that have been solved in runtime but they should
	 * not happen.</li>
	 * <li><b>ERROR</b>: for logging error in the program.</li>
	 * </ul>
	 * 
	 * @author Jorge Muñoz
	 * @author Raúl Arrabales
	 */
	public enum Level {
		VERBOSE(1), DEBUG(2), INFORMATION(3), WARNING(4), ERROR(5);

		private int value;

		/**
		 * Default constructor
		 * 
		 * @param value
		 */
		Level(int value) {
			this.value = value;
		}

		/**
		 * Compares this level with other and returns true when the other level
		 * should be logged.
		 * 
		 * @param other
		 * @return True if the other level compared with this should be logged.
		 */
		public boolean shouldBeLogged(Level other) {
			return this.value <= other.value;
		}

	};

	/**
	 * Main Logger of the program.
	 */
	private static Logger mainLogger = new Logger();;

	/**
	 * Vector with all the outputStreams where the logging information is
	 * written.
	 */
	private Vector<PrintStream> output;

	/**
	 * Current log level, only messages with more logging level are sent to the
	 * printstreams.
	 */
	private Level logLevel;

	/**
	 * A date format to print within the log records.
	 */
	private DateFormat dateFormat;

	/**
	 * Default constructor.
	 */
	public Logger() {
		this.output = new Vector<PrintStream>();
		this.logLevel = Level.INFORMATION;
		this.dateFormat = new SimpleDateFormat("yyyy/MM/dd (Z) HH:mm:ss.SSS");
	}

	/**
	 * Logs a message.
	 * 
	 * @param level
	 *            The level of the record.
	 * @param message
	 *            The message to be recorded.
	 */
	public void log(Level level, String message) {
		this.log(level, null, message);
	}

	/**
	 * Logs a message with a source.
	 * 
	 * @param level
	 *            The level of the record.
	 * @param source
	 *            The source of the record.
	 * @param message
	 *            The message to be recorded.
	 */
	public void log(Level level, String source, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.dateFormat.format(new Date())).append("\t");
		switch (level) {
		case VERBOSE:
			sb.append("[VERBOSE]   ");
			break;
		case DEBUG:
			sb.append("[DEBUG]     ");
			break;
		case INFORMATION:
			sb.append("[INFO]      ");
			break;
		case WARNING:
			sb.append("[WARNING]   ");
			break;
		case ERROR:
			sb.append("[ERROR]     ");
			break;
		default:
			throw new RuntimeException();
		}
		if (source == null) {
			sb.append("\t\t");
		} else {
			sb.append(source).append("\t");
		}
		if (message != null) {
			sb.append(message);
		}
		sb.append("\n");
		for (PrintStream ps : this.output) {
			ps.append(sb);
		}
	}

	/**
	 * Explicit call for flushing the output streams.
	 */
	public void flush() {
		for (PrintStream ps : this.output) {
			ps.flush();
		}
	}

	/**
	 * Logs an exception. If the source is know it is shown.
	 * 
	 * @param exception
	 *            The exception.
	 */
	public void log(Exception exception) {
		Throwable cause = exception.getCause();
		if (cause != null) {
			this.log(cause.toString(), exception);
		} else {
			this.log(null, exception);
		}
	}

	/**
	 * Logs a exception with a source
	 * 
	 * @param source
	 *            The source of the exception.
	 * @param exception
	 *            The exception.
	 */
	public void log(String source, Exception exception) {
		this.log(Level.WARNING, source, exception.getMessage());
		for (StackTraceElement element : exception.getStackTrace()) {
			this.log(Level.WARNING, null, element.toString());
		}
	}

	/**
	 * Sets the current logging level if it is not null.
	 * 
	 * @param level
	 *            The new logging level.
	 */
	public void setLevel(Level level) {
		if (level != null) {
			this.logLevel = level;
		}
	}

	/**
	 * Returns the current logging level.
	 * 
	 * @return The current logging level.
	 */
	public Level getLevel() {
		return this.logLevel;
	}

	/**
	 * Sets another main logger. If the logger is null a new logger is created
	 * with the default output.
	 * 
	 * @param logger
	 *            The new Main logger.
	 */
	public static void setMainLogger(Logger logger) {
		if (logger != null) {
			mainLogger = logger;
		} else {
			mainLogger = new Logger();
			mainLogger.setOnlyDefaultOutput();
		}
	}

	/**
	 * The instance of the logger. The instance of the logger is created without
	 * any output. It should be added before use it.
	 * 
	 * @return the instance of this class.
	 */
	public static Logger instance() {
		return mainLogger;
	}

	/**
	 * Adds an output to the outputs streams. The new printstream is not added
	 * if it already exists in the output list.
	 * 
	 * @param output
	 *            The output to include.
	 * @return true if the output was added.
	 */
	public boolean addOutput(PrintStream output) {
		boolean added = false;
		if (!this.output.contains(output)) {
			added = this.output.add(output);
		}
		return added;
	}

	/**
	 * Deletes an output of the outputStreams.
	 * 
	 * @param output
	 *            The output to delete.
	 * @return true if the output was deleted.
	 */
	public boolean deleteOutput(PrintStream output) {
		return this.output.remove(output);
	}

	/**
	 * Returns the current printStream outputs.
	 * 
	 * @return the current printStream outputs.
	 */
	public PrintStream[] getOutputs() {
		PrintStream[] outputs = new PrintStream[this.output.size()];
		outputs = this.output.toArray(outputs);
		return outputs;
	}

	/**
	 * Remove all the outputs for this logger and sets the default output:
	 * System.out.
	 */
	public void setOnlyDefaultOutput() {
		this.output = new Vector<PrintStream>();
		this.output.add(System.out);
	}

	/**
	 * Log a verbose record in the main logger
	 * 
	 * @param source
	 *            The source of the record.
	 * @param message
	 *            The message to be recorded.
	 */
	public static void verbose(String source, String message) {
		instance().log(Level.VERBOSE, source, message);
	}

	/**
	 * Log a error record in the main logger
	 * 
	 * @param message
	 *            The message to be recorded.
	 */
	public static void verbose(String message) {
		verbose(null, message);
	}

	/**
	 * Log a debug record in the main logger
	 * 
	 * @param source
	 *            The source of the record.
	 * @param message
	 *            The message to be recorded.
	 */
	public static void debug(String source, String message) {
		instance().log(Level.DEBUG, source, message);
	}

	/**
	 * Log a debug record in the main logger
	 * 
	 * @param message
	 *            The message to be recorded.
	 */
	public static void debug(String message) {
		debug(null, message);
	}

	/**
	 * Log a information record in the main logger
	 * 
	 * @param source
	 *            The source of the record.
	 * @param message
	 *            The message to be recorded.
	 */
	public static void info(String source, String message) {
		instance().log(Level.INFORMATION, source, message);
	}

	/**
	 * Log a information record in the main logger
	 * 
	 * @param message
	 *            The message to be recorded.
	 */
	public static void info(String message) {
		info(null, message);
	}

	/**
	 * Log a warning record in the main logger
	 * 
	 * @param source
	 *            The source of the record.
	 * @param message
	 *            The message to be recorded.
	 */
	public static void warning(String source, String message) {
		instance().log(Level.WARNING, source, message);
	}

	/**
	 * Log a warning record in the main logger
	 * 
	 * @param message
	 *            The message to be recorded.
	 */
	public static void warning(String message) {
		warning(null, message);
	}

	/**
	 * Log a error record in the main logger
	 * 
	 * @param source
	 *            The source of the record.
	 * @param message
	 *            The message to be recorded.
	 */
	public static void error(String source, String message) {
		instance().log(Level.ERROR, source, message);
	}

	/**
	 * Log a error record in the main logger
	 * 
	 * @param message
	 *            The message to be recorded.
	 */
	public static void error(String message) {
		error(null, message);
	}

	/**
	 * Log an exception record in the main logger
	 * 
	 * @param exception
	 *            The exception.
	 */
	public static void exception(Exception exception) {
		instance().log(exception);
	}

}