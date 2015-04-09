package facebook.database.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import facebook.database.model.*;

/**
 * Data Access Object for the Friend table.
 * Encapsulates all of the relevant SQL commands.
 * 
 * @version 4/4/2015
 * @author Alexander Miller, Congshu Wang
 */
public class FriendDAO 
{
	/**
	 * This class represents a key as a single object.
	 * It is only needed because the FRIEND table has a
	 * compound key (both department id and course number)
	 * 
	 * @author Alexander Miller, Congshu Wang
	 */
	private static class FriendPair 
	{
		public int user1, user2;

		public FriendPair(int user1ID, int user2ID) 
		{
			this.user1 = user1ID;
			this.user2 = user2ID;
		}

		@Override
		public int hashCode() 
		{
			return 31 * user1 + user2;
		}

		@Override
		public boolean equals(Object obj) 
		{
			if (this == obj)
				return true;
			if (obj == null || !(obj instanceof FriendPair))
				return false;
			FriendPair other = (FriendPair) obj;
			return user1 == other.user1 && user2 == other.user2;
		}
	}
	
	private static Connection conn;
	private static DatabaseManager dbm;
	// unique ID and object paired in a hashmap
	private static Map<FriendPair, Friend> cache;

	@SuppressWarnings("static-access")
	public FriendDAO(Connection conn, DatabaseManager dbm) 
	{
		this.conn = conn;
		this.dbm = dbm;
		this.cache = new HashMap<FriendPair, Friend>();
	}
	
	/**
	 * Create the Friend table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "create table FRIEND("
				+ "friend1 int not null, "
				+ "friend2 int not null, "
				+ "friendSince date, "
				+ "friendRequestPending boolean not null, "
				+ "friendRequestCaneled boolean not null, "
				+ "friendRequestComplete boolean not null,"
				
				+ "primary key(friend1, friend2))";
		
		stmt.executeUpdate(s);
	}
	
	/**
	 * Modify the Friend table to add foreign key constraints
	 * (needs to happen after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		
		String s = "alter table FRIEND add constraint fk_frienduser1 "
				+ "foreign key(friend1) references FBUSER on delete cascade";
		
		stmt.executeUpdate(s);
		
		s = "alter table FRIEND add constraint fk_frienduser2 "
				+ "foreign key(friend2) references FBUSER on delete cascade";
		stmt.executeUpdate(s);
		
		// CHECKS
		s = "alter table FRIEND ADD check(friend1 > 0 AND friend2 > 0)";
		stmt.execute(s);
	}
	
	/**
	 * Retrieve a User object given its key. Checks the cache first,
	 * then executes SQL query if object not already present.
	 * 
	 * @param deptid
	 * @param num
	 * @return the User object, or null if not found
	 */
	public static Friend find(int user1ID, int user2ID) 
	{
		// if its already in the cache, return it.
		if (cache.containsKey(user1ID)) 
			return cache.get(user1ID);
		else if(cache.containsKey(user2ID))
			return cache.get(user2ID);
		
		// else, do the full retrieval
		try 
		{
			// select all but primary key
			String qry = "select friendSince, friendRequestPending, friendRequestCaneled, friendRequestComplete "
					+ "from FRIEND "
					+ "where friend1 = ? and friend2 = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(qry);
			
			pstmt.setInt(1, user1ID);
			pstmt.setInt(2, user2ID);
			
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			// grab all of the fields
			//User user1 = dbm.findDepartment(deptid);
			Date friendSince = rs.getDate("friendSince");
			boolean friendRequestPending = rs.getBoolean("friendRequestPending");
			boolean friendRequestCaneled = rs.getBoolean("friendRequestCaneled");
			boolean friendRequestComplete = rs.getBoolean("friendRequestComplete");
			
			rs.close();
			
			Friend friend = new Friend(user1ID, user2ID, friendSince, friendRequestPending, friendRequestCaneled, friendRequestComplete);
			
			cache.put(new FriendPair(user1ID, user2ID), friend);
			return friend;
		} 
		catch (SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error finding friend", e);
		}
	}
	
	/**
	 * Insert a Friend object into the FRIEND table given the attributes
	 * 
	 * @param friend1 int
	 * @param friend2 int 
	 * @param friendSince Date
	 * @param friendRequestPending boolean
	 * @param friendRequestCaneled boolean
	 * @param friendRequestComplete boolean
	 * @return the new Friend object, or null if key already exists
	 */
	public static Friend insert(int friend1, int friend2, java.util.Date friendSince, 
			boolean friendRequestPending, boolean friendRequestCaneled, boolean friendRequestComplete) 
	{
		
		try 
		{
			// make sure that the friend is currently unused
			if (find(friend1, friend2) != null)
				return null;
			
			String cmd = "insert into FRIEND(friend1, friend2, "
						+ "friendSince, "
						+ "friendRequestPending, friendRequestCaneled, friendRequestComplete) "
						+ 
						"values(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setInt(1, friend1); 					// friend1
			pstmt.setInt(2, friend2); 					// friend2
			pstmt.setDate(3, (Date) friendSince);     		 	// friendSince
			pstmt.setBoolean(4, friendRequestPending);  // friendRequestPending
			pstmt.setBoolean(5, friendRequestCaneled);  // friendRequestCaneled
			pstmt.setBoolean(6, friendRequestComplete); // friendRequestComplete
			
			pstmt.executeUpdate();
			
			Friend friend = new Friend(friend1, friend2, friendSince, friendRequestPending, friendRequestCaneled, friendRequestComplete);
			
			cache.put(new FriendPair(friend1, friend2), friend);
			return friend;
		}
		catch(SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error inserting new friend", e);
		}
	}

	/**
	 * Changing the status of two user's and their given friend relationship
	 * 
	 * @param user1ID int id of user 1
	 * @param user2ID int id of user 2
	 * @param changeType int describing change in friendshipStatus. 
	 *    1 = Pending
	 *    2 = Canceled
	 *    3 = Complete
	 */
	public void changeFriendStatus(int user1ID, int user2ID, int changeType, boolean status)
	{
		 // define which changeType is appropriate
		String set = "set";
		switch(changeType)
		{
			default:
			case 1:
				// same as "set friendRequestPending = ?"
				set = set + " friendRequestPending = ?";
				break;
			case 2:
				set = set + " friendRequestCaneled = ?";
				break;
			case 3:
				set = set + " friendRequestComplete = ?";
				break;
		}
		
		try 
		{
			String cmd = "update FRIEND "
					+ set
					+ "where friend1 = ? and friend2 = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setBoolean(1, status); // false or true for any of the given friendshipStatuses.
			pstmt.setInt(2, user1ID);    // ID number of user 1
			pstmt.setInt(3, user2ID);    // ID number of user 2
			
			pstmt.executeUpdate();
		}
		catch(SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error changing friend", e);
		}
	}

	/**
	 * Clear all data from the Friend table.
	 * 
	 * @throws SQLException
	 */
	static void clear() throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "delete from FRIEND";
		stmt.executeUpdate(s);
		cache.clear();
	}
}