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
 * Data Access Object for the Comment table.
 * Encapsulates all of the relevant SQL commands.
 * 
 * @version 4/6/2015
 * @author Alexander Miller, Congshu Wang
 */

public class CommentDAO 
{
	private Connection conn;
	private DatabaseManager dbm;
	private Map<Integer, Comment> cache;
	
	public CommentDAO(Connection conn, DatabaseManager dbm)
	{
		this.conn = conn;
		this.dbm = dbm;
		this.cache = new HashMap<Integer, Comment>();
	}
	
	/**
	 * Create the Comment table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "create table COMMENT("
				+ "commentId int not null, "
				+ "userId int not null, "
				+ "postId int not null, "
				+ "commentDate date not null, "
				+ "commentText varchar(1000), "
				+ "primary key(commentId))";
		stmt.executeUpdate(s);
	}
	
	/**
	 * Modify the Comment table to add foreign key constraints
	 * (needs to happen after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "alter table COMMENT add constraint fk_comuser "
				+ "foreign key(userId) references USER on delete cascade";
		stmt.executeUpdate(s);
		s = "alter table COMMENT add constraint fk_compost "
				+ "foreign key(postId) references POST on delete cascade";
		stmt.executeUpdate(s);
	}
	
	/**
	 * Retrieve a Comment object given its key. Checks the cache first,
	 * then executes SQL query if object not already present.
	 * 
	 * @param commentId
	 * @return the Comment object, or null if not found
	 */
	public Comment find(int commentId)
	{
		if(cache.containsKey(commentId))
			return cache.get(commentId);
		try
		{
			String qry = "select userId, postId, commentDate, commentText from COMMENT where commentId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, commentId);
			ResultSet rs = pstmt.executeQuery();
			
			// return null if course doesn't exist
			if (!rs.next())
				return null;
			
			int userId = rs.getInt("userId");
			int postId = rs.getInt("postId");
			Date commentDate = rs.getDate("commentDate");
			String commentText = rs.getString("commentText");
			rs.close();
			
			//User user = dbm.findUser(userId);
			//Post post = dbm.findPost(postId);
			Comment comment = new Comment(this, commentId, userId, postId, commentDate, commentText);
			
			cache.put(commentId, comment);
			return comment;
		}
		catch (SQLException e)
		{
			dbm.cleanup();
			throw new RuntimeException("error finding comment", e);
		}
	}
	
	/**
	 * Add a new Comment with the given attributes.
	 * 
	 * @param commentId
	 * @param userId
	 * @param postId
	 * @param commentDate
	 * @param commentText
	 * @return the new Comment object, or null if key already exists
	 */
	public Comment insert(int commentId, int userId, int postId, Date commentDate, String commentText)
	{
		try
		{
			//check if this particular comment is already existed
			if(find(commentId) != null)
				return null;
			
			String cmd = "insert into COMMENT(commentId, userId, postId, commentDate, commentText) " + "values(?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, commentId);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, postId);
			pstmt.setDate(4, commentDate);
			pstmt.setString(5, commentText);
			pstmt.executeUpdate();
			
			Comment comment = new Comment(this, commentId, userId, postId, commentDate, commentText);
			
			cache.put(commentId, comment);
			return comment;
		}
		catch(SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new comment", e);
		}
	}
	
	/**
	 * commentText was changed in the model object, so propagate the change to the database.
	 * 
	 * @param commentId
	 * @param commentText (may be empty)
	 */
	public  void changeCommentContext(int commentId, String commentText)
	{
		try
		{
			String cmd = "update COMMENT set commentText = ? where commentId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			if(commentText.length() != 0)
				pstmt.setString(1, commentText);
			else
				pstmt.setString(1, "");
			pstmt.setInt(2, commentId);
			pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			dbm.cleanup();
			throw new RuntimeException("error changing comment", e);
		}
	}
}
