package garage.logging;

import java.io.*;
import java.text.*;
import java.util.Calendar;

public abstract class Logger {
	private final static String LOG_FILENAME = "GarageLog.txt";
	private final static String LOG_FORMAT = "[%s] %s";
	private final static String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	private static boolean initialized = false;
	private static PrintWriter writer;
	private static DateFormat dateFormat;
	private static Calendar calendar;
	
	public static void initialize() {
		try {
			writer = new PrintWriter(new File(LOG_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
		calendar = Calendar.getInstance();
		initialized = true;
	}
	
	public static void log(String msg) {
		if (!initialized) {
			initialize();
		}
		writer.printf(LOG_FORMAT, dateFormat.format(calendar.getTime()));
	}
}