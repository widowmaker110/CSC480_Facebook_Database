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

public class PostDAO 
{
	private Connection conn;
	private DatabaseManager dbm;
	private Map<Integer, Post> cache;
	
	public PostDAO(Connection conn, DatabaseManager dbm)
	{
		this.conn = conn;
		this.dbm = dbm;
		this.cache = new HashMap<Integer, Post>();
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
		String s = "create table POST("
				+ "postId int not null, "
				+ "userId int not null, "
				+ "postDate date not null, "
				+ "postText varchar(1000) not null, "
				+ "postImage varchar(256),"
				+ "postVideo varchar(256),"
				
				+ "primary key(postId))";
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
		String s = "alter table POST add constraint fk_postuser "
				+ "foreign key(postId) references USER on delete cascade";
		stmt.executeUpdate(s);
	}
	
	/**
	 * Retrieve a Post object given its key. Checks the cache first,
	 * then executes SQL query if object not already present.
	 * 
	 * @param likeId
	 * @return the Like object, or null if not found
	 */
	public Post find(int postId2, int userId2, Date postDate2, String postText2, String postImage2, String postVideo2)
	{
		if(cache.containsKey(postId2))
			return cache.get(postId2);
		try
		{
			String qry = "select postId, userId, postDate, postText, postImage, postVideo where postId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, postId2);
			ResultSet rs = pstmt.executeQuery();
			
			if(!rs.next())
				return null;
			
			int postId = rs.getInt("postId");
			int userId = rs.getInt("userId");
			Date postDate = rs.getDate("postDate");
			String postText = rs.getString("postText");
			String postImage = rs.getString("postimage");
			String postVideo = rs.getString("postVideo");
			rs.close();
			
			Post post = new Post(this, postId, userId, postDate, postText, postImage, postVideo);
			
			cache.put(postId, post);
			return post;
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
	 *  UNDERCONSTRUCTION FROM HERE DOWNWARD
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
			if (find(likeId, likeDate, null, null, null) != null)
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
	void clear() throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "delete from LIKE";
		stmt.executeUpdate(s);
		cache.clear();
	}
}