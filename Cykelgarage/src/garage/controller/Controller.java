package garage.controller;

import java.util.List;

import javax.swing.JOptionPane;

import garage.database.Database;
import garage.logging.*;
import garage.model.*;
import garage.hardware.interfaces.*;

/**
 * This class is the controller of a bicycle garage. It handles
 * hardware input and output through hardware interfaces and
 * hardware extensions.
 */
public class Controller implements KeyPadBufferListener, BarcodeReaderListener {
	private final static int USERNAME_LENGTH = 10;
	private final static int PASSWORD_LENGTH = 4;
	private final static int UNLOCK_TIME = 10;
	private final static int GREEN_LED_TIME = 1;
	private final static int RED_LED_TIME = 1;
	
	private int maxBikes;
	
	private boolean gui;
	
	private ElectronicLock frontDoorLock;
	
	private ElectronicLock bikeExitDoorLock;
	
	private PinCodeTerminal pinCodeTerminal;
	
	private BarcodePrinter barcodePrinter;
	
	Database database;
	
	KeyPadBuffer buffer;
	
	private String storedUsername = "";

	/**
	 * Create a new Controller object and assign its hardware to control.
	 * @param database The database object for database communications.
	 * @param frontDoorLock Front doorlock hardware.
	 * @param bikeExitDoorLock Bike exit doorlock hardware. 
	 * @param pinCodeTerminal Pincode-terminal hardware.
	 * @param frontDoorBarcodeReader Bike entrance barcode reader hardware.
	 * @param bikeExitDoorBarcodeReader Bike exit barcode reader hardware.
	 * @param barcodePrinter Barcode printer hardware.
	 */
	public Controller(int maxBikes, boolean gui, Database database, ElectronicLock frontDoorLock,
			ElectronicLock bikeExitDoorLock, PinCodeTerminal pinCodeTerminal,
			BarcodeReader frontDoorBarcodeReader, BarcodeReader bikeExitDoorBarcodeReader,
			BarcodePrinter barcodePrinter) {
		this.maxBikes = maxBikes;
		this.gui = gui;
		
		this.database = database;
		
		this.frontDoorLock = frontDoorLock;
		this.bikeExitDoorLock = bikeExitDoorLock;
		this.pinCodeTerminal = pinCodeTerminal;
		frontDoorBarcodeReader.register(this);
		bikeExitDoorBarcodeReader.register(this);
		this.barcodePrinter = barcodePrinter;
		
		buffer = new KeyPadBuffer(USERNAME_LENGTH, pinCodeTerminal, this);
	}
		
	
	/**
	 * Recieve new input from the buffer.
	 * @param input Input to be recieved from the buffer.
	 */
	public void recieveBuffer(String input) {
		if (input.length() == USERNAME_LENGTH) {
			newUsernameInput(input);
		} else {
			newPasswordInput(input);
		}
	}
	
	/**
	 * Method handles new username input. If username exists, light
	 * up green LED and store the username. If username does not exist,
	 * light up red LED.
	 * @param username Username input.
	 */
	private void newUsernameInput(String username) {
		if (database.userExists(username)) {
			storedUsername = username;
			buffer.setExpectedInput(PASSWORD_LENGTH);
			pinCodeTerminal.lightLED(PinCodeTerminal.GREEN_LED, GREEN_LED_TIME);
		} else {
			storedUsername = "";
			pinCodeTerminal.lightLED(PinCodeTerminal.RED_LED, RED_LED_TIME);
		}
	}
	
	/**
	 * Method handles new password input. If password is correct, unlock
	 * front door. If password is incorrect, light up red LED.
	 * @param password String to be compared to actual password for user.
	 */
	private void newPasswordInput(String password) {
		String correctPassword = database.getPassword(storedUsername);
		
		if (correctPassword != null && correctPassword.equals(password)) {
			LogAccess.event().log("User " + storedUsername + "unlocked the front entrance door "
					+ "by entering his/her username/password correctly");
			
			frontDoorLock.open(UNLOCK_TIME);
			pinCodeTerminal.lightLED(PinCodeTerminal.GREEN_LED, GREEN_LED_TIME);
			database.checkInUser(storedUsername);
		} else {
			LogAccess.event().log("User " + storedUsername + "failed to unlock the front entrance door "
					+ "by entering his/her username/password incorrectly");
			
			pinCodeTerminal.lightLED(PinCodeTerminal.RED_LED, RED_LED_TIME);
		}
		storedUsername = "";
	}

	/**
	 * Recieve a barcode scanned by the bike entrance scanner and process the input.
	 * @param code Barcode that was scanned.
	 */
	@Override
	public void entryBarcode(String code) {
		code = fillBarcode(code);
		if (database.bikeConnected(code)) {
			LogAccess.event().log("Front door unlocked by scanning barcode " + code);
			
			frontDoorLock.open(UNLOCK_TIME);
			database.checkInUser(database.getBikeOwner(code));
		}
	}

	/**
	 * Recieve a barcode scanned by the bike exit scanner and process the input.
	 * @param code Barcode that was scanned.
	 */
	@Override
	public void exitBarcode(String code) {
		code = fillBarcode(code);
		String user = database.getBikeOwner(code);
		if (user != null && database.userCheckedIn(user)) {
			LogAccess.event().log("Bike exit door unlocked by scanning barcode " + code);
			
			bikeExitDoorLock.open(UNLOCK_TIME);
			database.checkOutUser(user);
		}
	}
	
	/**
	 * Alter length of barcode to be 13 digits by padding with
	 * 0's infront of previous barcode.
	 * @param code Barcode to be padded.
	 * @return Padded barcode.
	 */
	private String fillBarcode(String code) {
		if (code.length() < 13) {
			StringBuilder sb = new StringBuilder(code);
			while (sb.length() < 13) {
				sb.insert(0, 0);
			}
			return sb.toString();
		}
		return code;
	}
	
	/**
     * Create a new user account.
     * @param username Username for the account.
     * @param password Password for the account.
     * @return True if account creation was successful, otherwise false.
     */
	public boolean newUser(String username, String password) {
		return database.createUser(username, password);
	}
	
	/**
	 * Delete an existing user. Fails if user has bikes connected
	 * to the garage
	 * @param username Username of user to be removed.
	 * @return True if successful, otherwise false
	 */
	public boolean removeUser(String username) {
		if (!database.getBikes(username).isEmpty()) {
			if (gui) {
				JOptionPane.showMessageDialog(null, "Cyklar tillhörande en användare som ska avregistreras måste först tas bort");
			} else {
				System.err.println("Cyklar tillhörande en användare som ska avregistreras måste först tas bort");
			}
			return false;
		}
		return database.removeUser(username);
	}
	
	/**
     * Connect a new bike to this garage and print its new
     * barcode if successful.
     * @param username The username associated with the bike.
     * @return True if successful, otherwise false.
     */
	public boolean connectNewBike(String username) {
		if (database.getBikes(null).size() >= maxBikes) {
			if (gui) {
				JOptionPane.showMessageDialog(null, "Max antal cyklar (" + maxBikes + ") får ej överskridas");
			} else {
				System.err.println("Max antal cyklar (" + maxBikes + ") får ej överskridas");
			}
		}
		String bikeID = database.connectNewBike(username);
		if (bikeID == null) {
			return false;
		}
		barcodePrinter.printBarcode(bikeID);
		return true;
	}
	
	/**
	 * 'Disconnect' a bike from this garage.
	 * @param bikeID The bikes ID.
	 * @return True if successful, otherwise false.
	 */
	public boolean removeBike(String bikeID) {
		return database.removeBike(bikeID);
	}
	
	/**
	 * Get all Bike ID's of bikes belonging to a specific user.
	 * @param username Username of user whose bikes are requested.
	 * If null method will return all bikes in the bike-garage.
	 * @return List of bikes ID's.
	 */
	public List<String> getBikes(String username) {
		return database.getBikes(username);
	}
	
	/**
	 * Get all usernames of users registered to this garage.
	 * @return List of usernames.
	 */
	public List<String> getUsers() {
		return database.getUsers();
	}
	
	/**
     * Search for userinfo by username.
     * @param username Username to look for.
     * @return List of Strings with search result.
     */
    public List<String> searchUsername(String username) {
		return database.getUserInfo(username);
	}
	
    /**
     * Search for bikeinfo by bike ID.
     * @param bikeID Bike ID to look for.
     * @return List of String with search result.
     */
	public List<String> searchBikeID(String bikeID) {
		return database.getUserInfo(database.getBikeOwner(bikeID));
	}
	
}
