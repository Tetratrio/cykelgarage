package garage.launcher;

import java.sql.SQLException;

import test.hardware_simulation.*;
import garage.controller.BicycleGarageManager;
import garage.database.Database;
import garage.gui.BikeGarageGUI;
import garage.logging.LogAccess;

public abstract class Launcher {
	private final static String usage = "usage: Launcher [<options>] [<args>]\n" +
										"Options:\n" +
										"--help\t\tDisplay usage string, then exit" +
										"Arguments:\n" +
										"test\t\tRun program in test mode.";
	private final static int MAX_BIKES = 40;
	
	public static void main(String[] args) {
		/* For the evaluationprocess 'test' option is forced */
		args = new String[1];
		args[0] = "test";
		
		for (String argument : args) {
			if (argument.equalsIgnoreCase("--help")) {
				System.out.println(usage);
				System.exit(0);
			}
			else if (argument.equalsIgnoreCase("test")) {
				LogAccess.initialize(true);
				Database db = null;
				
				try {
					db = new Database();
				} catch (SQLException e) {
					javax.swing.JOptionPane.showMessageDialog(null, "Kunde ej ansluta till databasen, stänger applikationen...");
					System.exit(1);
				}
				
				BicycleGarageManager bicycleGarageManager = new BicycleGarageManager(
						MAX_BIKES,
						true,
						db,
						new ElectronicLockTestDriver("Front entrance"),
						new ElectronicLockTestDriver("Bike exit"),
						new PinCodeTerminalTestDriver(),
						new BarcodeReaderEntryTestDriver(),
						new BarcodeReaderExitTestDriver(),
						new BarcodePrinterTestDriver()
						);
				
				new BikeGarageGUI(bicycleGarageManager);
			} else {
				System.err.println("Invalid argument \"" + argument + "\"");
				System.err.println(usage);
				System.exit(1);
			}
		}
		
		//Start application here...
	}
}
