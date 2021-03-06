package facebook.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import facebook.database.model.*;

/**
 * Data Access Object for the Like table.
 * Encapsulates all of the relevant SQL commands.
 * 
 * @version 4/7/2015
 * @author Alexander Miller, Congshu Wang
 */

public class LikeDAO 
{
	private static Connection conn;
	private static DatabaseManager dbm;
	private static Map<Integer, Like> cache;
	
	@SuppressWarnings("static-access")
	public LikeDAO(Connection conn, DatabaseManager dbm)
	{
		this.conn = conn;
		this.dbm = dbm;
		this.cache = new HashMap<Integer, Like>();
	}
	
	/**
	 * Create the Like table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "create table FBLIKE("
				+ "likeId int not null, "
				+ "userId int not null, "
				+ "postId int not null, "
				+ "likeDate date not null, "
				+ "primary key(likeId))";
		stmt.executeUpdate(s);
	}
	
	/**
	 * Modify the Like table to add foreign key constraints
	 * (needs to happen after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "alter table FBLIKE add constraint fk_likeuser "
				+ "foreign key(userId) references FBUSER on delete cascade";
		stmt.executeUpdate(s);
		
		s = "alter table FBLIKE add constraint fk_likepost "
				+ "foreign key(postId) references FBPOST on delete cascade";
		stmt.executeUpdate(s);
		
		// CHECKS
		s = "alter table FBLIKE ADD check(likeId > 0 AND userId > 0 AND postId > 0)";
		stmt.executeUpdate(s);
	}
	
	/**
	 * Retrieve a Like object given its key. Checks the cache first,
	 * then executes SQL query if object not already present.
	 * 
	 * @param likeId
	 * @return the Like object, or null if not found
	 */
	public static Like find(int likeId)
	{
		if(cache.containsKey(likeId))
			return cache.get(likeId);
		try
		{
			String qry = "select userId, postId, likeDate from FBLIKE where likeId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, likeId);
			ResultSet rs = pstmt.executeQuery();
			
			if(!rs.next())
				return null;
			
			int userId = rs.getInt("userId");
			int postId = rs.getInt("postId");
			Date likeDate = rs.getDate("likeDate");
			rs.close();
			
			Like like = new Like(likeId, userId, postId, likeDate);
			
			cache.put(likeId, like);
			return like;
		}
		catch (SQLException e)
		{
			dbm.cleanup();
			throw new RuntimeException("error finding like", e);
		}
	}
	
	public static ArrayList<Like> getAllLikes()
	{
		
		try 
		{
			String qry = "SELECT * FROM FBLIKE";
			
			PreparedStatement pstmt = conn.prepareStatement(qry);

			ResultSet rs = pstmt.executeQuery();

			// return null if fbuser doesn't exist
			if (!rs.next())
				return null;

			ArrayList<Like> arrayLikes = new ArrayList<Like>();
			
			do
			{				
				// grab all of the fields
				int likeId = rs.getInt("likeId");
				int userId = rs.getInt("userId");
				int postId = rs.getInt("postId");
				Date likeDate = rs.getDate("likeDate");
				Like like = new Like(likeId, userId, postId, likeDate);
				cache.put(likeId, like);
				arrayLikes.add(like);
			}while (rs.next());
			
			rs.close();
					
			return arrayLikes;
		} 
		catch (SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error finding likes", e);
		}		
	}

	// http://alvinalexander.com/java/java-current-date-example-now
	private static java.sql.Date convertDate(java.util.Date current)
	{
		java.sql.Date date = new java.sql.Date(current.getTime());
		return date;
	}
	
	/**
	 * Insert a Like object into the LIKE table given the attributes
	 * 
	 * @param likeId
	 * @param userId
	 * @param postId
	 * @param date
	 * @return Like objected which is newly inserted
	 */
	public static Like insert(int likeId, int userId, int postId, java.util.Date date) 
	{
		
		try 
		{
			// make sure that the friend is currently unused
			if (find(likeId) != null)
				return null;
			
			String cmd = "insert into FBLIKE(likeId, userId, postId, likeDate) "
						+
						"values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setInt(1, userId); 		// userId
			pstmt.setInt(2, likeId);		// likeId
			pstmt.setInt(3, postId); 		// postId
			pstmt.setDate(4, convertDate(date));     // likeDate
			
			pstmt.executeUpdate();
			
			Like like = new Like(likeId, userId, postId, date);
			
			cache.put(likeId, like);
			return like;
		}
		catch(SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error inserting new like", e);
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
		String s = "delete from FBLIKE";
		stmt.executeUpdate(s);
		cache.clear();
	}
}