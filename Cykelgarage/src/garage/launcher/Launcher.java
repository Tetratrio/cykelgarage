package garage.launcher;

import test.hardware_simulation.*;
import garage.controller.Controller;
import garage.database.Database;
import garage.gui.BikeGarageGUI;
import garage.logging.LogAccess;

public abstract class Launcher {
	private final static String usage = "usage: Launcher [<options>] [<args>]\n" +
										"Options:\n" +
										"--help\t\tDisplay usage string, then exit" +
										"Arguments:\n" +
										"test\t\tRun program in test mode.";
	
	public static void main(String[] args) {
		for (String argument : args) {
			if (argument.equalsIgnoreCase("--help")) {
				System.out.println(usage);
				System.exit(0);
			}
			else if (argument.equalsIgnoreCase("test")) {
				LogAccess.initialize(true);
				
				Controller controller = new Controller(
						new Database(),
						new ElectronicLockTestDriver("Front entrance"),
						new ElectronicLockTestDriver("Bike exit"),
						new PinCodeTerminalTestDriver(),
						new BarcodeReaderEntryTestDriver(),
						new BarcodeReaderExitTestDriver(),
						new BarcodePrinterTestDriver());
				
				new BikeGarageGUI(controller);
			} else {
				System.err.println("Invalid argument \"" + argument + "\"");
				System.err.println(usage);
				System.exit(1);
			}
		}
		
		//Start application here...
	}
}
