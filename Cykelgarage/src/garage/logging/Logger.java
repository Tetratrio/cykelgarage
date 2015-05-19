package garage.logging;

import java.awt.BorderLayout;
import java.io.*;
import java.text.*;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Logger {
	private final static String LOG_FORMAT = "[%s] %s";
	private final static String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	private PrintWriter writer;
	private DateFormat dateFormat;
	private Calendar calendar;
	private DefaultListModel<String> logList = null;
	
	/**
	 * Creates a new Logger object which is used for logging
	 * anything into a file.
	 * @param filename Filename of file where log is stored.
	 * @param gui Used for determining if a gui window should
	 * be created for the Logger. Only used for testing the
	 * software, will not be in final version.
	 */
	public Logger(String filename, boolean gui) {
		try {
			File file = new File(filename);
			file.createNewFile();
			writer = new PrintWriter(new FileWriter(file, true), true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException ie) {
			
		}
		dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
		calendar = Calendar.getInstance();
		
		if (gui) {
			createGUI(filename);
		}
	}
	
	/**
	 * Log a String to the logfile. Adds date and time.
	 * @param msg String to be logged.
	 */
	public void log(String msg) {
		String logEntry = String.format(LOG_FORMAT, dateFormat.format(calendar.getTime()), msg);
		writer.println(logEntry);
		if (logList != null) {
			logList.insertElementAt(logEntry, 0);
		}
	}
	
	private void createGUI(String filename) {
		JFrame frame = new JFrame(filename);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		logList = new DefaultListModel<String>();
		JList<String> list = new JList<String>(logList);
		panel.add(list, BorderLayout.CENTER);
		TitledBorder t = new TitledBorder("Logg");
		panel.setBorder(t);
		frame.getContentPane().add(panel);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
	}
}
