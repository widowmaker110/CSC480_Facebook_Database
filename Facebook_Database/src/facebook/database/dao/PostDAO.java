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
 * Data Access Object for the Post table.
 * Encapsulates all of the relevant SQL commands.
 * 
 * @version 4/7/2015
 * @author Alexander Miller, Congshu Wang
 */

public class PostDAO 
{
	private static Connection conn;
	private DatabaseManager dbm;
	private static Map<Integer, Post> cache;
	
	@SuppressWarnings("static-access")
	public PostDAO(Connection conn, DatabaseManager dbm)
	{
		this.conn = conn;
		this.dbm = dbm;
		this.cache = new HashMap<Integer, Post>();
	}
	
	/**
	 * Create the Post table via SQL
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
	 * Modify the Post table to add foreign key constraints
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
		
		// CHECKS
		s = "check(userId > 0), check(postId > 0)";
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
	 * Insert a Post object into the LIKE table given the attributes
	 * 
	 * @param postId2
	 * @param userId2
	 * @param postDate2
	 * @param postText2
	 * @param postImage2
	 * @param postVideo2
	 * @return the Post Object with the attributes given
	 */
	public Post insert(int postId2, int userId2, Date postDate2, String postText2, String postImage2, String postVideo2)
	{
		
		try 
		{
			// make sure that the friend is currently unused
			if (find(postId2, userId2, postDate2, postText2,  postImage2, postVideo2) != null)
				return null;
			
			String cmd = "insert into LIKE(postId, userId, postDate, postText, postImage, postVideo) "
						+
						"values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			
			pstmt.setInt(1, postId2); 			// postId
			pstmt.setInt(2, userId2);			// userId
			pstmt.setDate(3, postDate2); 		// postDate
			pstmt.setString(4, postText2);     	// postText
			pstmt.setString(5, postImage2);     // postImage
			pstmt.setString(6, postVideo2);     // postVideo
			
			pstmt.executeUpdate();
			
			Post post = new Post(this, postId2, userId2, postDate2, postText2, postImage2, postVideo2);
			
			cache.put(postId2, post);
			return post;
		}
		catch(SQLException e) 
		{
			dbm.cleanup();
			throw new RuntimeException("error inserting new friend", e);
		}
	}
	
	/**
	 * Clear all data from the Post table.
	 * 
	 * @throws SQLException
	 */
	static void clear() throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "delete from POST";
		stmt.executeUpdate(s);
		cache.clear();
	}
}