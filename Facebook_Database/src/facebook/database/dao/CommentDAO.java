package facebook.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import facebook.database.model.*;

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
	 * Create the Course table via SQL
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
	 * Modify the Course table to add foreign key constraints
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
			//Comment comment = new Comment(this, commentId, commentDate, commentText, user, post);
			
			//cache.put(commentId, comment);
			//return comment;
		}
		catch (SQLException e)
		{
			dbm.cleanup();
			throw new RuntimeException("error finding comment", e);
		}
	}
}
