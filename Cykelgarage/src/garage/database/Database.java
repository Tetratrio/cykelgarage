package garage.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles communication with the database
 */
public class Database {

	/**
	 * Get all Bike ID's of bikes belonging to a specific user.
	 * @param username Username of user whose bikes are requested.
	 * If null method will return all bikes in the bike-garage.
	 * @return List of bikes ID's.
	 */
	public List<String> getBikes(String username) {
		ArrayList<String> bikeIDList = new ArrayList<String>();
		if (username == null) {
			//TODO Add all bikes to list
		} else {
			//TODO Add bikes belonging to username to list
		}
		return bikeIDList;
	}
	
	/**
	 * Get all usernames of users registered to this garage.
	 * @return List of usernames.
	 */
	public List<String> getUsers() {
		ArrayList<String> usernameList = new ArrayList<String>();
		return usernameList;
	}
	
    /**
     * Check if a bike is connected to this garage.
     * @param bikeID Bike to check.
     * @return True if bike is connected to this garage,
     * otherwise false.
     */
    public boolean bikeConnected(String bikeID) {
        return false;
    }

    /**
     * Connect a new bike to this garage and recieve its new ID value.
     * @param username The username associated with the bike.
     * @return null if unsuccessful, otherwise the bikes new ID value.
     */
    public String connectNewBike(String username) {
        return "";
    }

    /**
     * 'Disconnect' a bike from this garage.
     * @param bikeID The bikes ID.
     * @return True if successful, otherwise false.
     */
    public boolean removeBike(String bikeID) {
        return false;
    }

    /**
     * Get the password associated with a username.
     * @param username Username whose password is requested.
     * @return null if user does not exist, otherwise the user's
     * password is returned.
     */
    public String getPassword(String username) {
        return null;
    }

    /**
     * Method checks if a user with a specific username exists
     * in the database.
     * @param username Username of user who we want to check.
     * @return Returns true if user does exist, otherwise false.
     */
    public boolean userExists(String username) {
        return false;
    }
    
    /**
     * Create a new user account.
     * @param username Username for the account.
     * @param password Password for the account.
     * @return True if account creation was successful, otherwise false.
     */
    public boolean createUser(String username, String password) {
    	return false;
    }

    /**
     * Get owner of a bike.
     * @param bikeID The bikes ID.
     * @return Returns null if bike is not connected to this garage,
     * otherwise the username associated with that bike.
     */
    public String getBikeOwner(String bikeID) {
        return null;
    }

    /**
     * Check if a user is checked in to the garage.
     * @param username Username for the user we want to check.
     * @return Returns true if user is checked in to the garage, otherwise false.
     */
    public boolean userCheckedIn(String username) {
        return false;
    }
    
    /**
     * Checkin a user.
     * @param username Username of user to check in.
     */
    public void checkInUser(String username) {
    	
    }
    
    /**
     * Checkout a user.
     * @param username Username of user to check in.
     */
    public void checkOutUser(String username) {
    	
    }
    
    /**
     * Search for userinfo by username.
     * @param username Username to look for.
     * @return List of Strings with search result.
     */
    public List<String> searchUsername(String username) {
		ArrayList<String> searchResult = new ArrayList<String>();
		/* TODO
		 * Example result:
		 * 
		 * Username: XXXXXXXX Password: XXXXX
		 * Bikes connected to garage:
		 * XXXXXXX
		 * XXXXXXX
		 * XXXXXXX
		 */
		return searchResult;
	}
	
    /**
     * Search for bikeinfo by bike ID.
     * @param bikeID Bike ID to look for.
     * @return List of String with search result.
     */
	public List<String> searchBikeID(String bikeID) {
		ArrayList<String> searchResult = new ArrayList<String>();
		/* TODO
		 * Example result:
		 * 
		 * Username: XXXXXXXX Password: XXXXX
		 * Bikes connected to garage:
		 * XXXXXXX
		 * XXXXXXX
		 * XXXXXXX
		 */
		return searchResult;
	}
}
