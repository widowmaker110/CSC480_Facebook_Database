package facebook.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
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
	private DatabaseManager dbm;
	private static Map<Integer, Like> cache;
	
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
		String s = "create table LIKE("
				+ "likeId int not null, "
				+ "userId int not null, "
				+ "postId int not null, "
				+ "likeDate date not null, "
				+ "primary key(commentId))";
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
		String s = "alter table LIKE add constraint fk_likeuser "
				+ "foreign key(userId) references USER on delete cascade";
		stmt.executeUpdate(s);
		s = "alter table LIKE add constraint fk_likepost "
				+ "foreign key(postId) references POST on delete cascade";
		stmt.executeUpdate(s);
	}
	
	/**
	 * Retrieve a Like object given its key. Checks the cache first,
	 * then executes SQL query if object not already present.
	 * 
	 * @param likeId
	 * @return the Like object, or null if not found
	 */
	public Like find(int likeId)
	{
		if(cache.containsKey(likeId))
			return cache.get(likeId);
		try
		{
			String qry = "select userId, postId, likeDate where likeId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, likeId);
			ResultSet rs = pstmt.executeQuery();
			
			if(!rs.next())
				return null;
			
			int userId = rs.getInt("userId");
			int postId = rs.getInt("postId");
			Date likeDate = rs.getDate("likeDate");
			rs.close();
			
			Like like = new Like(this, likeId, userId, postId, likeDate);
			
			cache.put(likeId, like);
			return like;
		}
		catch (SQLException e)
		{
			dbm.cleanup();
			throw new RuntimeException("error finding like", e);
		}
	}

	/**
	 * Insert a Like object into the LIKE table given the attributes
	 * 
	 * @param likeId
	 * @param userId
	 * @param postId
	 * @param likeDate
	 * @return Like objected which is newly inserted
	 */
	public Like insert(int likeId, int userId, int postId, Date likeDate) 
	{
		
		try 
		{
			// make sure that the friend is currently unused
			if (find(likeId) != null)
				return null;
			
			String cmd = "insert into LIKE(userId, userId, postId, likeDate) "
						+
						"values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setInt(1, userId); 		// userId
			pstmt.setInt(2, likeId);		// likeId
			pstmt.setInt(3, postId); 		// postId
			pstmt.setDate(4, likeDate);     // likeDate
			
			pstmt.executeUpdate();
			
			Like like = new Like(this, likeId, userId, postId, likeDate);
			
			cache.put(likeId, like);
			return like;
		}
		catch(SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error inserting new friend", e);
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
		String s = "delete from LIKE";
		stmt.executeUpdate(s);
		cache.clear();
	}
}