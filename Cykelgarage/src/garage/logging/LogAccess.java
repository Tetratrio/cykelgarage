package garage.logging;

public abstract class LogAccess {
	private final static String EVENT_LOG_FILENAME = "EventLog.txt";
	private final static String ERROR_LOG_FILENAME = "ErrorLog.txt";

	private static Logger eventLogger;
	private static Logger errorLogger;
	
	/**
	 * Get the eventlogger object. Creates a new one if there
	 * already isnt one.
	 * @return Logger object for events.
	 */
	public static Logger event() {
		return (eventLogger != null) ? eventLogger : (eventLogger = new Logger(EVENT_LOG_FILENAME, false));
	}
	
	/**
	 * Get the errorlogger object. Creates a new one if there
	 * already isnt one. Only available for testing. Will not
	 * be used in shipped version of the software.
	 * @return Logger object for errors.
	 */
	public static Logger error() {
		return (errorLogger != null) ? errorLogger : (errorLogger = new Logger(ERROR_LOG_FILENAME, false));
	}
	
	/**
	 * Create new Logger objects.
	 * @param showLogs Boolean for creating log-gui or not.
	 */
	public static void initialize(boolean showLogs) {
		eventLogger = new Logger(EVENT_LOG_FILENAME, showLogs);
		errorLogger = new Logger(ERROR_LOG_FILENAME, showLogs);
	}
}
