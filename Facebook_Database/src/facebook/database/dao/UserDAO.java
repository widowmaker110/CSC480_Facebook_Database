package facebook.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import facebook.database.model.*;

/**
 * Data Access Object for the User table.
 * Encapsulates all of the relevant SQL commands.
 * 
 * @version 4/6/2015
 * @author Alexander Miller, Congshu Wang
 */
public class UserDAO 
{
	
	private static Connection conn;
	private static DatabaseManager dbm;
	private static Map<Integer, User> cache;

	@SuppressWarnings("static-access")
	public UserDAO(Connection conn, DatabaseManager dbm) 
	{
		this.conn = conn;
		this.dbm = dbm;
		this.cache = new HashMap<Integer, User>();
	}
	
	/**
	 * Create the Course table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "create table USER("
				+ "userId int not null, "
				+ "userName varchar(100) not null, "
				+ "password varchar(30) not null, "
				+ "email varchar(100) not null, "
				
				+ "primary key(userId))";
		
		stmt.executeUpdate(s);
	}
	
	/**
	 * Modify the User table to add foreign key constraints
	 * (needs to happen after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "check(userId > 0), check(email like '_%@_%._%')";
		stmt.executeUpdate(s);
	}
	
	/**	
	 * Retrieve a User object given its key. Checks the cache first,
	 * then executes SQL query if object not already present.
	 * 
	 * @param userId
	 * @return User object with the given attritbutes. Null if not found
	 */
	public static User find(int userId) 
	{
		if (cache.containsKey(userId)) 
			return cache.get(userId);
		
		try 
		{
			String qry = "select userId, userName, password, email "
					+ "from USER "
					+ "where userId = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(qry);
			
			pstmt.setInt(1, userId);

			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			// grab all of the fields
			String userName = rs.getString("userName");
			String password = rs.getString("password");
			String email = rs.getString("email");
			
			rs.close();
			
			User user = new User(userId, userName, password, email);
			
			cache.put(userId, user);
			return user;
		} 
		catch (SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error finding user", e);
		}
	}
	
	/**
	 * Insert a User object into the FRIEND table given the attributes
	 * 
	 * @param user1 int
	 * @param user2 int 
	 * @param friendSince Date
	 * @param friendRequestPending boolean
	 * @param friendRequestCaneled boolean
	 * @param friendRequestComplete boolean
	 * @return the new Friend object, or null if key already exists
	 */
	public static User insert(int userId, String username, String password, String email) 
	{		
		try 
		{
			if (find(userId) != null)
				return null;
			
			String cmd = "insert into USER(userId, userName, password, email) "
						+ 
						"values(?, ?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setInt(1, userId); 		// userId
			pstmt.setString(2, username);   // username
			pstmt.setString(3, password);  	// password
			pstmt.setString(4, email);  	// email
		
			pstmt.executeUpdate();
			
			User user = new User(userId, username, password, email);
			
			cache.put(userId, user);
			return user;
		}
		catch(SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error inserting new user", e);
		}
	}

	/**
	 * Clear all data from the User table.
	 * 
	 * @throws SQLException
	 */
	static void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from USER";
		stmt.executeUpdate(s);
		cache.clear();
	}
}