package facebook.database.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	private Connection conn;
	private DatabaseManager dbm;
	//private Map<DeptNumPair, Course> cache;

	public FriendDAO(Connection conn, DatabaseManager dbm) 
	{
		this.conn = conn;
		this.dbm = dbm;
		//this.cache = new HashMap<DeptNumPair, Course>();
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
		String s = "create table FRIEND("
				+ "friend1 int not null, "
				+ "friend2 int not null, "
				+ "friendSince date, "
				+ "friendRequestPending boolean not null, "
				+ "friendRequestCaneled boolean not null, "
				+ "friendRequestComplete boolean not null, "
				
				+ "primary key(friend1, friend2)";
		
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
				+ "foreign key(friend1) references User on delete cascade";
		
		stmt.executeUpdate(s);
		
		s = "alter table FRIEND add constraint fk_frienduser2 "
				+ "foreign key(friend2) references User on delete cascade";
		stmt.executeUpdate(s);
	}
	
	/**
	 * Retrieve a User object given its key. Checks the cache first,
	 * then executes SQL query if object not already present.
	 * 
	 * @param deptid
	 * @param num
	 * @return the User object, or null if not found
	 */
	public Friend find(int user1ID, int user2ID) 
	{
		//TODO fix this once the caching is fixed.
//		DeptNumPair deptNum = new DeptNumPair(deptid, num);
//		if (cache.containsKey(deptNum)) return cache.get(deptNum);
		
		try {
			//TODO change this query
			// select all but primary key
			String qry = "select friendSince, friendRequestPending, friendRequestCaneled, friendRequestComplete "
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
			
			//cache.put(deptNum, course);
			return friend;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding course", e);
		}
	}
	
	/**
	 * Insert a Friend object into the FRIEND table given the attributes
	 * 
	 * @param user1 int
	 * @param user2 int 
	 * @param friendSince Date
	 * @param friendRequestPending boolean
	 * @param friendRequestCaneled boolean
	 * @param friendRequestComplete boolean
	 * @return the new Friend object, or null if key already exists
	 */
	public Friend insert(User user1, User user2, Date friendSince, 
			boolean friendRequestPending, boolean friendRequestCaneled, boolean friendRequestComplete) 
	{
		
		try {
			// TODO
			// make sure that the dept, num pair is currently unused
//			if (find(dept.getDeptId(), num) != null)
//				return null;
			
			String cmd = "insert into FRIEND(friend1, friend2, "
						+ "friendSince, "
						+ "friendRequestPending, friendRequestCaneled, friendRequestComplete) "
						+ 
						"values(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setInt(1, user1.getUserId()); 		// friend1
			pstmt.setInt(2, user2.getUserId()); 		// friend2
			pstmt.setDate(3, friendSince);     		 	// friendSince
			pstmt.setBoolean(4, friendRequestPending);  // friendRequestPending
			pstmt.setBoolean(5, friendRequestCaneled);  // friendRequestCaneled
			pstmt.setBoolean(6, friendRequestComplete); // friendRequestComplete
			
			pstmt.executeUpdate();
			
			Friend friend = new Friend(user1.getUserId(), user2.getUserId(), friendSince, friendRequestPending, friendRequestCaneled, friendRequestComplete);
			
			// TODO
//			cache.put(new DeptNumPair(dept.getDeptId(), num), course);
			return friend;
		}
		catch(SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error inserting new course", e);
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
			throw new RuntimeException("error changing faculty", e);
		}
	}

	/**
	 * Clear all data from the Course table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from FRIEND";
		stmt.executeUpdate(s);
		//cache.clear();
	}
}