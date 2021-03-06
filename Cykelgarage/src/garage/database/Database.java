package garage.database;

import garage.logging.LogAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private final static String USERNAME = "cykeladmin";
	private final static String PASSWORD = "secretpass";
	private final static long FIFTEEN_MINUTES_IN_MILLIS = 15 * 60 * 1000;
	private Connection conn;

	/**
	 * Create a new database object for communication with
	 * the database. Tries to open a connection to the database,
	 * @throws SQLException Thrown if connection to the database failed.
	 */
	public Database() throws SQLException {
		if (!openConnection(USERNAME, PASSWORD)) {
			LogAccess.error().log("Failed to connect to the database");
			throw new SQLException("Failed to connect to the database");
		}
	}

	/**
	 * Open a connection to the database, using the specified username and
	 * password.
	 * @return true if the connection was successful, false if the username and
	 *         password were not recognized, or if the JDBC driver wasn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://31.208.64.250/cykelgarage", userName,
					password);
		} catch (SQLException se) {
			logError("Failed to get a connection from the DriverManager",
					se.getMessage());
			return false;
		} catch (ClassNotFoundException ce) {
			logError("Failed to find the jdbc driver", ce.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			logError("Failed to close the database connection", e.getMessage());
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/**
	 * Check if a bike is connected to this garage.
	 * 
	 * @param bikeID
	 *            Bike to check.
	 * @return True if bike is connected to this garage, otherwise false.
	 */
	public boolean bikeConnected(String bikeId) {

		PreparedStatement findBikePS = null;
		String findBikeSql = "SELECT * FROM Bike WHERE bikeId = ?";

		try {
			findBikePS = conn.prepareStatement(findBikeSql);
			findBikePS.setInt(1, Integer.parseInt(bikeId));
			ResultSet rs = findBikePS.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			logError("Failed to load a new bike into the database",
					e.getMessage());
		} finally {
			try {
				if (findBikePS != null)
					findBikePS.close();
			} catch (SQLException e2) {
				logError("Failed to load a new bike into the database",
						e2.getMessage());
			}

		}

		return false;
	}

	/**
	 * Checkin a user.
	 * @param username Username of user to check in.
	 */
	public void checkInUser(String username) {

		PreparedStatement lockPS = null;
		String lockSql = "SELECT * FROM User WHERE username = ? FOR UPDATE";

		PreparedStatement checkInPS = null;
		String checkInSql = "UPDATE User SET checkedIn = ? WHERE username = ?";

		try {
			conn.setAutoCommit(false);

			lockPS = conn.prepareStatement(lockSql);
			lockPS.setString(1, username);
			lockPS.executeQuery();

			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			checkInPS = conn.prepareStatement(checkInSql);
			checkInPS.setTimestamp(1, curTime);
			checkInPS.setString(2, username);
			checkInPS.executeUpdate();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logError("Failed to checkin user " + username, e.getMessage());
		} finally { // close the connection
			try {
				if (lockPS != null)
					lockPS.close();
				if (checkInPS != null)
					checkInPS.close();
			} catch (SQLException e2) {
				logError("Failed to close the sql statement after checking in a user", e2.getMessage());
			}
		}
	}

	/**
	 * Checkout a user.
	 * @param username Username of user to checkout.
	 */
	public void checkOutUser(String username) {

		PreparedStatement lockPS = null;
		String lockSql = "SELECT * FROM User WHERE username = ? FOR UPDATE";

		PreparedStatement checkOutPS = null;
		String checkOutSql = "UPDATE User SET checkedIn = ? WHERE username = ?";

		try {
			conn.setAutoCommit(false);

			lockPS = conn.prepareStatement(lockSql);
			lockPS.setString(1, username);
			lockPS.executeQuery();

			checkOutPS = conn.prepareStatement(checkOutSql);
			checkOutPS.setTimestamp(1, new Timestamp(1000));
			checkOutPS.setString(2, username);
			checkOutPS.executeUpdate();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logError("Failed to checkout user " + username, e.getMessage());
		} finally { // close the connection
			try {
				if (lockPS != null)
					lockPS.close();
				if (checkOutPS != null)
					checkOutPS.close();
			} catch (SQLException e2) {
				logError("Failed to close the sql statement after checking out a user", e2.getMessage());
			}
		}
	}

	/**
	 * Connect a new bike to this garage and recieve its new ID value.
	 * @param username The username associated with the bike.
	 * @return null if unsuccessful, otherwise the bikes new ID value.
	 */
	public String connectNewBike(String username) {
		if (!userExists(username)) {
			return null;
		}
		PreparedStatement createBikePS = null;
		String createBikeSql = "INSERT INTO Bike (username) VALUES (?)";

		try {

			createBikePS = conn.prepareStatement(createBikeSql,
					new String[] { "bikeId" });
			createBikePS.setString(1, username);
			Integer bikeId = createBikePS.executeUpdate();

			return bikeId.toString();

		} catch (SQLException e) {
			logError("Failed to connect a new bike", e.getMessage());
		} finally {
			try {
				if (createBikePS != null)
					createBikePS.close();
			} catch (SQLException e2) {
				logError("Failed to close the sql statement after connecting a new bike", e2.getMessage());
			}

		}

		return null;
	}

	/**
	 * Create a new user account.
	 * @param username Username for the account.
	 * @param password Password for the account.
	 * @return True if account creation was successful, otherwise false.
	 */
	public boolean createUser(String username, String password) {
		if (userExists(username)) {
			return false;
		}
		
		PreparedStatement createUserPS = null;
		String createUserSql = "INSERT INTO User (username, password) VALUES (?, ?)";

		try {
			createUserPS = conn.prepareStatement(createUserSql);
			createUserPS.setString(1, username);
			createUserPS.setString(2, password);
			createUserPS.executeUpdate();

			return true;
		} catch (SQLException e) {
			logError("Failed to create a new user", e.getMessage());
		} finally {
			try {
				if (createUserPS != null)
					createUserPS.close();
			} catch (SQLException e2) {
				logError("Failed to close sql statement after creating a new user", e2.getMessage());
			}

		}

		return false;

	}
	
	/**
	 * Delete an existing user. Fails if user has bikes connected
	 * to the garage
	 * @param username Username of user to be removed.
	 * @return True if successful, otherwise false
	 */
	public boolean removeUser(String username) {
		if (!userExists(username) || !getBikes(username).isEmpty()) {
			LogAccess.error().log("Failed to remove user " + username);
			return false;
		}
		
		PreparedStatement deleteUserPS = null;
		String deleteBikeSql = "DELETE FROM User WHERE username = ?";

		try {
			deleteUserPS = conn.prepareStatement(deleteBikeSql);
			deleteUserPS.setString(1, username);


			int deletedUsers = deleteUserPS.executeUpdate();

			if (deletedUsers == 1) {
				return true;
			}
		} catch (SQLException e) {
			logError("Failed to remove user", e.getMessage());
		} finally {
			try {
				if (deleteUserPS != null) {
					deleteUserPS.close();
				}
			} catch (SQLException e2) {
				logError("Failed to close sql statement after removing a user", e2.getMessage());
			}

		}

		return false;
	}

	/**
	 * Get owner of a bike.
	 * @param bikeID The bike's ID.
	 * @return Returns null if bike is not connected to this garage, otherwise
	 *         the username associated with that bike.
	 */
	public String getBikeOwner(String bikeId) {

		PreparedStatement getUserPS = null;
		String getUserSql = "SELECT username FROM User NATURAL JOIN Bike WHERE bikeId = ?";

		try {
			getUserPS = conn.prepareStatement(getUserSql);
			getUserPS.setInt(1, Integer.parseInt(bikeId));
			ResultSet rs = getUserPS.executeQuery();

			if (rs.next()) {
				return rs.getString("username");
			}

		} catch (SQLException e) {
			logError("Failed to get the owner of a bike", e.getMessage());
		} finally {
			try {
				if (getUserPS != null)
					getUserPS.close();
			} catch (SQLException e2) {
				logError("Failed to close sql statement after getting a bike owner by bike ID", e2.getMessage());
			}

		}

		return null;

	}

	/**
	 * Get all Bike ID's of bikes belonging to a specific user.
	 * @param username Username of user whose bikes are requested. If null method
	 * will return all bikes in the bike-garage.
	 * @return List of bikes ID's.
	 */
	public List<String> getBikes(String username) {
		ArrayList<String> bikeIdList = new ArrayList<String>();

		if (username != null && !userExists(username)) {
			return bikeIdList;
		}
		
		PreparedStatement findBikesPS = null;
		String findBikesSql = "SELECT * FROM Bike";

		try {

			if (username != null)
				findBikesSql += " WHERE username = ?";

			findBikesPS = conn.prepareStatement(findBikesSql);

			if (username != null)
				findBikesPS.setString(1, username);

			ResultSet rs = findBikesPS.executeQuery();

			while (rs.next()) {
				StringBuilder bikeID = new StringBuilder(Integer.toString(rs.getInt("bikeId")));
				while (bikeID.length() < 13) {
					bikeID.insert(0, 0);
				}
				bikeIdList.add(bikeID.toString());
			}

		} catch (SQLException e) {
			logError("Failed to get bikes by username", e.getMessage());
		} finally {
			try {
				if (findBikesPS != null)
					findBikesPS.close();
			} catch (SQLException e2) {
				logError("Failed to close sql statement after getting bikes by username", e2.getMessage());
			}

		}

		return bikeIdList;

	}

	/**
	 * Get the password associated with a username.
	 * @param username Username whose password is requested.
	 * @return null if user does not exist, otherwise the user's password is
	 * returned.
	 */
	public String getPassword(String username) {

		PreparedStatement findUserPS = null;
		String findUserSql = "SELECT password FROM User WHERE username = ?";
		String password = null;

		try {

			findUserPS = conn.prepareStatement(findUserSql);
			findUserPS.setString(1, username);
			ResultSet rs = findUserPS.executeQuery();

			if (rs.next())
				password = rs.getString("password");

		} catch (SQLException e) {
			logError("Failed to get password for username " + username, e.getMessage());
		} finally {
			try {
				if (findUserPS != null)
					findUserPS.close();
			} catch (SQLException e2) {
				logError("Failed to close sql statement after getting password by username", e2.getMessage());
			}

		}
		return password;
	}

	/**
	 * Get all usernames of users registered to this garage.
	 * @return List of usernames.
	 */
	public List<String> getUsers() {

		ArrayList<String> usernameList = new ArrayList<String>();

		Statement getUserStmt = null;
		String getUserSql = "SELECT username FROM User";

		try {
			getUserStmt = conn.createStatement();

			ResultSet rs = getUserStmt.executeQuery(getUserSql);

			while (rs.next())
				usernameList.add(rs.getString("username"));

		} catch (SQLException e) {
			logError("Failed to get all users", e.getMessage());
		} finally {
			try {
				if (getUserStmt != null)
					getUserStmt.close();
			} catch (SQLException e2) {
				logError("Failed to close sql statement after getting all users", e2.getMessage());
			}
		}

		return usernameList;

	}

	/**
	 * 'Disconnect' a bike from this garage.
	 * @param bikeID The bikes ID.
	 * @return True if successful, otherwise false.
	 */
	public boolean removeBike(String bikeId) {
		if (!bikeConnected(bikeId)) {
			return false;
		}
		PreparedStatement deleteBikePS = null;
		String deleteBikeSql = "DELETE FROM Bike WHERE bikeId = ?";

		try {
			deleteBikePS = conn.prepareStatement(deleteBikeSql);
			deleteBikePS.setString(1, bikeId);


			int deletedBikes = deleteBikePS.executeUpdate();

			if (deletedBikes == 1) {
				return true;
			}
		} catch (SQLException e) {
			logError("Failed to disconnect bike from garage", e.getMessage());
		} finally {
			try {
				if (deleteBikePS != null) {
					deleteBikePS.close();
				}
			} catch (SQLException e2) {
				logError("Failed to close sql statement after disconnect a bike from the garage", e2.getMessage());
			}

		}

		return false;
	}

	/**
	 * Search for userinfo by username.
	 * 
	 * @param username Username to look for.
	 * @return List of Strings with search result.
	 */
	public List<String> getUserInfo(String username) {
		ArrayList<String> searchResult = new ArrayList<String>();
		
		if (!userExists(username)) {
			return searchResult;
		}

		searchResult.add("Användarnamn: " + username + " Lösenord: "
				+ getPassword(username));

		searchResult.add("Anslutna cyklar:");

		for (String s : getBikes(username)) {
			searchResult.add(s);
		}

		return searchResult;
	}

	/**
	 * Check if a user is checked in to the garage.
	 * @param username Username for the user we want to check.
	 * @return Returns true if user is checked in to the garage, otherwise
	 * false.
	 */
	public boolean userCheckedIn(String username) {

		if (!userExists(username)) {
			return false;
		}
		PreparedStatement getUserPS = null;
		String getUserSql = "SELECT checkedIn FROM User WHERE username = ?";

		try {
			getUserPS = conn.prepareStatement(getUserSql);
			getUserPS.setString(1, username);

			ResultSet rs = getUserPS.executeQuery();

			if (rs.next()) {
				if (rs.getTimestamp("checkedIn").getTime() > java.util.Calendar.getInstance().getTimeInMillis() - FIFTEEN_MINUTES_IN_MILLIS)
					return true;
			}
		} catch (SQLException e) {
			logError("Failed to check if a user was checked in", e.getMessage());
		} finally {
			try {
				if (getUserPS != null)
					getUserPS.close();
			} catch (SQLException e2) {
				logError("Failed to close sql statement after checking if a user was checked in", e2.getMessage());
			}
		}
		return false;
	}

	/**
	 * Method checks if a user with a specific username exists in the database.
	 * @param username Username of user who we want to check.
	 * @return Returns true if user does exist, otherwise false.
	 */
	public boolean userExists(String username) {

		PreparedStatement findUserPS = null;
		String findUserSql = "SELECT * FROM User WHERE username = ?";

		try {
			findUserPS = conn.prepareStatement(findUserSql);
			findUserPS.setString(1, username);

			ResultSet rs = findUserPS.executeQuery();

			if (rs.next())
				return true;
		} catch (SQLException e) {
			logError("Failed to check if a user exists", e.getMessage());
		} finally {
			try {
				if (findUserPS != null)
					findUserPS.close();
			} catch (SQLException e2) {
				logError("Failed to close the sql statement after checking if a user exists", e2.getMessage());
			}

		}

		return false;
	}

	private void logError(String description, String msg) {
		LogAccess.error().log("Database: " + description);
		LogAccess.error().log("Database exception: " + msg);
	}

}
