package garage.logging;

import java.awt.BorderLayout;
import java.io.*;
import java.text.*;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Logger {
	private final static String LOG_FORMAT = "[%s] %s\n";
	private final static String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	private PrintWriter writer;
	private DateFormat dateFormat;
	private Calendar calendar;
	private DefaultListModel<String> logList = null;
	
	public Logger(String filename, boolean gui) {
		try {
			writer = new PrintWriter(new FileWriter(new File(filename)), true);
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
	
	public void log(String msg) {
		String logEntry = String.format(LOG_FORMAT, dateFormat.format(calendar.getTime()), msg);
		writer.printf(logEntry);
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
